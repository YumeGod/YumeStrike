package org.apache.fop.render.pdf;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.pdf.AlphaRasterImage;
import org.apache.fop.pdf.PDFArray;
import org.apache.fop.pdf.PDFColor;
import org.apache.fop.pdf.PDFDeviceColorSpace;
import org.apache.fop.pdf.PDFDictionary;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFFilter;
import org.apache.fop.pdf.PDFName;
import org.apache.fop.pdf.PDFReference;
import org.apache.fop.pdf.PDFResourceContext;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;
import org.apache.xmlgraphics.ps.ImageEncodingHelper;

public class ImageRenderedAdapter extends AbstractImageAdapter {
   private static Log log;
   private ImageEncodingHelper encodingHelper;
   private PDFFilter pdfFilter = null;
   private String maskRef;
   private PDFReference softMask;
   private static final int MAX_HIVAL = 255;

   public ImageRenderedAdapter(ImageRendered image, String key) {
      super(image, key);
      this.encodingHelper = new ImageEncodingHelper(image.getRenderedImage());
   }

   public ImageRendered getImage() {
      return (ImageRendered)this.image;
   }

   public int getWidth() {
      RenderedImage ri = this.getImage().getRenderedImage();
      return ri.getWidth();
   }

   public int getHeight() {
      RenderedImage ri = this.getImage().getRenderedImage();
      return ri.getHeight();
   }

   private ColorModel getEffectiveColorModel() {
      return this.encodingHelper.getEncodedColorModel();
   }

   protected ColorSpace getImageColorSpace() {
      return this.getEffectiveColorModel().getColorSpace();
   }

   protected ICC_Profile getEffectiveICCProfile() {
      ColorSpace cs = this.getImageColorSpace();
      if (cs instanceof ICC_ColorSpace) {
         ICC_ColorSpace iccSpace = (ICC_ColorSpace)cs;
         return iccSpace.getProfile();
      } else {
         return null;
      }
   }

   public void setup(PDFDocument doc) {
      RenderedImage ri = this.getImage().getRenderedImage();
      super.setup(doc);
      ColorModel orgcm = ri.getColorModel();
      if (orgcm.hasAlpha() && orgcm.getTransparency() == 3) {
         doc.getProfile().verifyTransparencyAllowed(this.image.getInfo().getOriginalURI());
         AlphaRasterImage alphaImage = new AlphaRasterImage("Mask:" + this.getKey(), ri);
         this.softMask = doc.addImage((PDFResourceContext)null, alphaImage).makeReference();
      }

   }

   public PDFDeviceColorSpace getColorSpace() {
      return toPDFColorSpace(this.getEffectiveColorModel().getColorSpace());
   }

   public int getBitsPerComponent() {
      ColorModel cm = this.getEffectiveColorModel();
      if (cm instanceof IndexColorModel) {
         IndexColorModel icm = (IndexColorModel)cm;
         return icm.getComponentSize(0);
      } else {
         return cm.getComponentSize(0);
      }
   }

   public boolean isTransparent() {
      ColorModel cm = this.getEffectiveColorModel();
      if (cm instanceof IndexColorModel && cm.getTransparency() == 3) {
         return true;
      } else {
         return this.getImage().getTransparentColor() != null;
      }
   }

   private static Integer getIndexOfFirstTransparentColorInPalette(RenderedImage image) {
      ColorModel cm = image.getColorModel();
      if (cm instanceof IndexColorModel) {
         IndexColorModel icm = (IndexColorModel)cm;
         byte[] alphas = new byte[icm.getMapSize()];
         byte[] reds = new byte[icm.getMapSize()];
         byte[] greens = new byte[icm.getMapSize()];
         byte[] blues = new byte[icm.getMapSize()];
         icm.getAlphas(alphas);
         icm.getReds(reds);
         icm.getGreens(greens);
         icm.getBlues(blues);

         for(int i = 0; i < ((IndexColorModel)cm).getMapSize(); ++i) {
            if ((alphas[i] & 255) == 0) {
               return new Integer(i);
            }
         }
      }

      return null;
   }

   public PDFColor getTransparentColor() {
      ColorModel cm = this.getEffectiveColorModel();
      if (cm instanceof IndexColorModel) {
         IndexColorModel icm = (IndexColorModel)cm;
         if (cm.getTransparency() == 3) {
            int transPixel = icm.getTransparentPixel();
            return new PDFColor(icm.getRed(transPixel), icm.getGreen(transPixel), icm.getBlue(transPixel));
         }
      }

      return new PDFColor(this.getImage().getTransparentColor());
   }

   public String getMask() {
      return this.maskRef;
   }

   public PDFReference getSoftMaskReference() {
      return this.softMask;
   }

   public PDFFilter getPDFFilter() {
      return this.pdfFilter;
   }

   public void outputContents(OutputStream out) throws IOException {
      this.encodingHelper.encode(out);
   }

   public void populateXObjectDictionary(PDFDictionary dict) {
      ColorModel cm = this.getEffectiveColorModel();
      if (cm instanceof IndexColorModel) {
         IndexColorModel icm = (IndexColorModel)cm;
         PDFArray indexed = new PDFArray(dict);
         indexed.add(new PDFName("Indexed"));
         if (icm.getColorSpace().getType() != 5) {
            log.warn("Indexed color space is not using RGB as base color space. The image may not be handled correctly. Base color space: " + icm.getColorSpace() + " Image: " + this.image.getInfo());
         }

         indexed.add(new PDFName(toPDFColorSpace(icm.getColorSpace()).getName()));
         int c = icm.getMapSize();
         int hival = c - 1;
         if (hival > 255) {
            throw new UnsupportedOperationException("hival must not go beyond 255");
         }

         indexed.add(new Integer(hival));
         int[] palette = new int[c];
         icm.getRGBs(palette);
         ByteArrayOutputStream baout = new ByteArrayOutputStream();

         for(int i = 0; i < c; ++i) {
            int entry = palette[i];
            baout.write((entry & 16711680) >> 16);
            baout.write((entry & '\uff00') >> 8);
            baout.write(entry & 255);
         }

         indexed.add(baout.toByteArray());
         dict.put("ColorSpace", indexed);
         dict.put("BitsPerComponent", icm.getPixelSize());
         Integer index = getIndexOfFirstTransparentColorInPalette(this.getImage().getRenderedImage());
         if (index != null) {
            PDFArray mask = new PDFArray(dict);
            mask.add(index);
            mask.add(index);
            dict.put("Mask", mask);
         }
      }

   }

   public String getFilterHint() {
      return "image";
   }

   static {
      log = LogFactory.getLog(ImageRenderedAdapter.class);
   }
}
