package org.apache.xmlgraphics.image.loader.impl.imageio;

import java.awt.Color;
import java.awt.Point;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.spi.IIOServiceProvider;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoader;
import org.apache.xmlgraphics.image.loader.impl.ImageBuffered;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.w3c.dom.Element;

public class ImageLoaderImageIO extends AbstractImageLoader {
   protected static Log log;
   private ImageFlavor targetFlavor;
   private static final String PNG_METADATA_NODE = "javax_imageio_png_1.0";
   private static final String JPEG_METADATA_NODE = "javax_imageio_jpeg_image_1.0";
   private static final Set providersIgnoringICC;

   public ImageLoaderImageIO(ImageFlavor targetFlavor) {
      if (!ImageFlavor.BUFFERED_IMAGE.equals(targetFlavor) && !ImageFlavor.RENDERED_IMAGE.equals(targetFlavor)) {
         throw new IllegalArgumentException("Unsupported target ImageFlavor: " + targetFlavor);
      } else {
         this.targetFlavor = targetFlavor;
      }
   }

   public ImageFlavor getTargetFlavor() {
      return this.targetFlavor;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      RenderedImage imageData = null;
      IIOException firstException = null;
      IIOMetadata iiometa = (IIOMetadata)info.getCustomObjects().get(ImageIOUtil.IMAGEIO_METADATA);
      boolean ignoreMetadata = iiometa != null;
      boolean providerIgnoresICC = false;
      Source src = session.needSource(info.getOriginalURI());
      ImageInputStream imgStream = ImageUtil.needImageInputStream(src);

      try {
         Iterator iter = ImageIO.getImageReaders(imgStream);

         while(iter.hasNext()) {
            ImageReader reader = (ImageReader)iter.next();

            try {
               imgStream.mark();
               ImageReadParam param = reader.getDefaultReadParam();
               reader.setInput(imgStream, false, ignoreMetadata);
               int pageIndex = ImageUtil.needPageIndexFromURI(info.getOriginalURI());

               try {
                  if (ImageFlavor.BUFFERED_IMAGE.equals(this.targetFlavor)) {
                     imageData = reader.read(pageIndex, param);
                  } else {
                     imageData = reader.read(pageIndex, param);
                  }

                  if (iiometa == null) {
                     iiometa = reader.getImageMetadata(pageIndex);
                  }

                  providerIgnoresICC = this.checkProviderIgnoresICC(reader.getOriginatingProvider());
                  break;
               } catch (IndexOutOfBoundsException var31) {
                  throw new ImageException("Page does not exist. Invalid image index: " + pageIndex);
               } catch (IllegalArgumentException var32) {
                  throw new ImageException("Error loading image using ImageIO codec", var32);
               } catch (IIOException var33) {
                  if (firstException == null) {
                     firstException = var33;
                  } else {
                     log.debug("non-first error loading image: " + var33.getMessage());
                  }

                  try {
                     BufferedImage bi = this.getFallbackBufferedImage(reader, pageIndex, param);
                     imageData = bi;
                     firstException = null;
                     break;
                  } catch (IIOException var30) {
                     imgStream.reset();
                  }
               }
            } finally {
               reader.dispose();
            }
         }
      } finally {
         ImageUtil.closeQuietly(src);
      }

      if (firstException != null) {
         throw new ImageException("Error while loading image: " + firstException.getMessage(), firstException);
      } else if (imageData == null) {
         throw new ImageException("No ImageIO ImageReader found .");
      } else {
         ColorModel cm = imageData.getColorModel();
         Color transparentColor = null;
         if (!(cm instanceof IndexColorModel)) {
            if (providerIgnoresICC && cm instanceof ComponentColorModel) {
               ICC_Profile iccProf = this.tryToExctractICCProfile(iiometa);
               if (iccProf != null) {
                  ColorModel cm2 = new ComponentColorModel(new ICC_ColorSpace(iccProf), ((ColorModel)cm).hasAlpha(), ((ColorModel)cm).isAlphaPremultiplied(), ((ColorModel)cm).getTransparency(), ((ColorModel)cm).getTransferType());
                  WritableRaster wr = Raster.createWritableRaster(imageData.getSampleModel(), (Point)null);
                  imageData.copyData(wr);
                  BufferedImage bi = new BufferedImage(cm2, wr, cm2.isAlphaPremultiplied(), (Hashtable)null);
                  imageData = bi;
                  cm = cm2;
               }
            }

            if (iiometa != null && iiometa.isStandardMetadataFormatSupported()) {
               Element metanode = (Element)iiometa.getAsTree("javax_imageio_1.0");
               Element dim = ImageIOUtil.getChild(metanode, "Transparency");
               if (dim != null) {
                  Element child = ImageIOUtil.getChild(dim, "TransparentColor");
                  if (child != null) {
                     String value = child.getAttribute("value");
                     if (value != null && value.length() != 0) {
                        if (((ColorModel)cm).getNumColorComponents() == 1) {
                           int gray = Integer.parseInt(value);
                           transparentColor = new Color(gray, gray, gray);
                        } else {
                           StringTokenizer st = new StringTokenizer(value);
                           transparentColor = new Color(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                        }
                     }
                  }
               }
            }
         }

         return (Image)(ImageFlavor.BUFFERED_IMAGE.equals(this.targetFlavor) ? new ImageBuffered(info, (BufferedImage)imageData, transparentColor) : new ImageRendered(info, imageData, transparentColor));
      }
   }

   private boolean checkProviderIgnoresICC(IIOServiceProvider provider) {
      StringBuffer b = new StringBuffer(provider.getDescription(Locale.ENGLISH));
      b.append('/').append(provider.getVendorName());
      b.append('/').append(provider.getVersion());
      if (log.isDebugEnabled()) {
         log.debug("Image Provider: " + b.toString());
      }

      return providersIgnoringICC.contains(b.toString());
   }

   private ICC_Profile tryToExctractICCProfile(IIOMetadata iiometa) {
      ICC_Profile iccProf = null;
      String[] supportedFormats = iiometa.getMetadataFormatNames();

      for(int i = 0; i < supportedFormats.length; ++i) {
         String format = supportedFormats[i];
         Element root = (Element)iiometa.getAsTree(format);
         if ("javax_imageio_png_1.0".equals(format)) {
            iccProf = this.tryToExctractICCProfileFromPNGMetadataNode(root);
         } else if ("javax_imageio_jpeg_image_1.0".equals(format)) {
            iccProf = this.tryToExctractICCProfileFromJPEGMetadataNode(root);
         }
      }

      return iccProf;
   }

   private ICC_Profile tryToExctractICCProfileFromPNGMetadataNode(Element pngNode) {
      ICC_Profile iccProf = null;
      Element iccpNode = ImageIOUtil.getChild(pngNode, "iCCP");
      if (iccpNode instanceof IIOMetadataNode) {
         IIOMetadataNode imn = (IIOMetadataNode)iccpNode;
         byte[] prof = (byte[])imn.getUserObject();
         String comp = imn.getAttribute("compressionMethod");
         if ("deflate".equalsIgnoreCase(comp)) {
            Inflater decompresser = new Inflater();
            decompresser.setInput(prof);
            byte[] result = new byte[100];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            boolean failed = false;

            while(!decompresser.finished() && !failed) {
               try {
                  int resultLength = decompresser.inflate(result);
                  bos.write(result, 0, resultLength);
                  if (resultLength == 0) {
                     log.debug("Failed to deflate ICC Profile");
                     failed = true;
                  }
               } catch (DataFormatException var13) {
                  log.debug("Failed to deflate ICC Profile", var13);
                  failed = true;
               }
            }

            decompresser.end();

            try {
               iccProf = ICC_Profile.getInstance(bos.toByteArray());
            } catch (IllegalArgumentException var12) {
               log.debug("Failed to interpret embedded ICC Profile", var12);
               iccProf = null;
            }
         }
      }

      return iccProf;
   }

   private ICC_Profile tryToExctractICCProfileFromJPEGMetadataNode(Element jpgNode) {
      ICC_Profile iccProf = null;
      Element jfifNode = ImageIOUtil.getChild(jpgNode, "app0JFIF");
      if (jfifNode != null) {
         Element app2iccNode = ImageIOUtil.getChild(jfifNode, "app2ICC");
         if (app2iccNode instanceof IIOMetadataNode) {
            IIOMetadataNode imn = (IIOMetadataNode)app2iccNode;
            iccProf = (ICC_Profile)imn.getUserObject();
         }
      }

      return iccProf;
   }

   private BufferedImage getFallbackBufferedImage(ImageReader reader, int pageIndex, ImageReadParam param) throws IOException {
      Raster raster = reader.readRaster(pageIndex, param);
      byte var5;
      switch (raster.getNumBands()) {
         case 1:
            var5 = 10;
            break;
         case 2:
         default:
            throw new UnsupportedOperationException();
         case 3:
            var5 = 5;
            break;
         case 4:
            var5 = 6;
      }

      BufferedImage bi = new BufferedImage(raster.getWidth(), raster.getHeight(), var5);
      bi.getRaster().setRect(raster);
      return bi;
   }

   static {
      log = LogFactory.getLog(ImageLoaderImageIO.class);
      providersIgnoringICC = new HashSet();
      providersIgnoringICC.add("Standard PNG image reader/Sun Microsystems, Inc./1.0");
      providersIgnoringICC.add("Standard JPEG Image Reader/Sun Microsystems, Inc./0.5");
   }
}
