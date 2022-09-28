package org.apache.fop.render.afp;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPImageObjectInfo;
import org.apache.fop.afp.AFPObjectAreaInfo;
import org.apache.fop.afp.AFPPaintingState;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceManager;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.util.bitmap.BitmapImageUtil;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;
import org.apache.xmlgraphics.ps.ImageEncodingHelper;
import org.apache.xmlgraphics.util.UnitConv;

public class AFPImageHandlerRenderedImage extends AFPImageHandler implements ImageHandler {
   private static Log log;
   private static final ImageFlavor[] FLAVORS;

   public AFPDataObjectInfo generateDataObjectInfo(AFPRendererImageInfo rendererImageInfo) throws IOException {
      AFPImageObjectInfo imageObjectInfo = (AFPImageObjectInfo)super.generateDataObjectInfo(rendererImageInfo);
      AFPRendererContext rendererContext = (AFPRendererContext)rendererImageInfo.getRendererContext();
      AFPInfo afpInfo = rendererContext.getInfo();
      this.setDefaultResourceLevel(imageObjectInfo, afpInfo.getResourceManager());
      AFPPaintingState paintingState = afpInfo.getPaintingState();
      ImageRendered imageRendered = (ImageRendered)rendererImageInfo.img;
      Dimension targetSize = new Dimension(afpInfo.getWidth(), afpInfo.getHeight());
      this.updateDataObjectInfo(imageObjectInfo, paintingState, imageRendered, targetSize);
      return imageObjectInfo;
   }

   private AFPDataObjectInfo updateDataObjectInfo(AFPImageObjectInfo imageObjectInfo, AFPPaintingState paintingState, ImageRendered imageRendered, Dimension targetSize) throws IOException {
      long start = System.currentTimeMillis();
      int resolution = paintingState.getResolution();
      int maxPixelSize = paintingState.getBitsPerPixel();
      if (paintingState.isColorImages()) {
         if (paintingState.isCMYKImagesSupported()) {
            maxPixelSize *= 4;
         } else {
            maxPixelSize *= 3;
         }
      }

      float ditheringQuality = paintingState.getDitheringQuality();
      RenderedImage renderedImage = imageRendered.getRenderedImage();
      ImageInfo imageInfo = imageRendered.getInfo();
      ImageSize intrinsicSize = imageInfo.getSize();
      boolean useFS10 = maxPixelSize == 1 || BitmapImageUtil.isMonochromeImage(renderedImage);
      int functionSet = useFS10 ? 10 : 11;
      boolean usePageSegments = useFS10 && !imageObjectInfo.getResourceInfo().getLevel().isInline();
      ImageSize effIntrinsicSize = intrinsicSize;
      if (usePageSegments) {
         Dimension resampledDim = new Dimension((int)Math.ceil(UnitConv.mpt2px(targetSize.getWidth(), resolution)), (int)Math.ceil(UnitConv.mpt2px(targetSize.getHeight(), resolution)));
         imageObjectInfo.setCreatePageSegment(true);
         imageObjectInfo.getResourceInfo().setImageDimension(resampledDim);
         boolean resample = resampledDim.width < renderedImage.getWidth() && resampledDim.height < renderedImage.getHeight();
         if (resample) {
            if (log.isDebugEnabled()) {
               log.debug("Resample from " + intrinsicSize.getDimensionPx() + " to " + resampledDim);
            }

            renderedImage = BitmapImageUtil.convertToMonochrome(renderedImage, resampledDim, ditheringQuality);
            effIntrinsicSize = new ImageSize(resampledDim.width, resampledDim.height, (double)resolution);
         } else if (ditheringQuality >= 0.5F) {
            renderedImage = BitmapImageUtil.convertToMonochrome(renderedImage, intrinsicSize.getDimensionPx(), ditheringQuality);
         }
      }

      imageObjectInfo.setDataHeightRes((int)Math.round(effIntrinsicSize.getDpiHorizontal() * 10.0));
      imageObjectInfo.setDataWidthRes((int)Math.round(effIntrinsicSize.getDpiVertical() * 10.0));
      int dataHeight = renderedImage.getHeight();
      imageObjectInfo.setDataHeight(dataHeight);
      int dataWidth = renderedImage.getWidth();
      imageObjectInfo.setDataWidth(dataWidth);
      ColorModel cm = renderedImage.getColorModel();
      if (log.isTraceEnabled()) {
         log.trace("ColorModel: " + cm);
      }

      int pixelSize = cm.getPixelSize();
      if (cm.hasAlpha()) {
         pixelSize -= 8;
      }

      byte[] imageData = null;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      boolean allowDirectEncoding = true;
      if (allowDirectEncoding && pixelSize <= maxPixelSize) {
         ImageEncodingHelper helper = new ImageEncodingHelper(renderedImage, pixelSize == 32);
         ColorModel encodedColorModel = helper.getEncodedColorModel();
         boolean directEncode = true;
         if (helper.getEncodedColorModel().getPixelSize() > maxPixelSize) {
            directEncode = false;
         }

         if (BitmapImageUtil.getColorIndexSize(renderedImage) > 2) {
            directEncode = false;
         }

         if (useFS10 && BitmapImageUtil.isMonochromeImage(renderedImage) && BitmapImageUtil.isZeroBlack(renderedImage)) {
            directEncode = false;
            if (this.encodeInvertedBilevel(helper, imageObjectInfo, baos)) {
               imageData = baos.toByteArray();
            }
         }

         if (directEncode) {
            log.debug("Encoding image directly...");
            imageObjectInfo.setBitsPerPixel(encodedColorModel.getPixelSize());
            if (pixelSize == 32) {
               functionSet = 45;
            }

            helper.encode(baos);
            imageData = baos.toByteArray();
         }
      }

      if (imageData == null) {
         log.debug("Encoding image via RGB...");
         ImageEncodingHelper.encodeRenderedImageAsRGB(renderedImage, baos);
         imageData = baos.toByteArray();
         imageObjectInfo.setBitsPerPixel(24);
         boolean colorImages = paintingState.isColorImages();
         imageObjectInfo.setColor(colorImages);
         if (!colorImages) {
            log.debug("Converting RGB image to grayscale...");
            baos.reset();
            int bitsPerPixel = paintingState.getBitsPerPixel();
            imageObjectInfo.setBitsPerPixel(bitsPerPixel);
            ImageEncodingHelper.encodeRGBAsGrayScale(imageData, dataWidth, dataHeight, bitsPerPixel, baos);
            imageData = baos.toByteArray();
            if (bitsPerPixel == 1) {
               imageObjectInfo.setSubtractive(true);
            }
         }
      }

      switch (functionSet) {
         case 10:
            imageObjectInfo.setMimeType("image/x-afp+fs10");
            break;
         case 11:
            imageObjectInfo.setMimeType("image/x-afp+fs11");
            break;
         case 45:
            imageObjectInfo.setMimeType("image/x-afp+fs45");
            break;
         default:
            throw new IllegalStateException("Invalid IOCA function set: " + functionSet);
      }

      imageObjectInfo.setData(imageData);
      AFPObjectAreaInfo objectAreaInfo = imageObjectInfo.getObjectAreaInfo();
      objectAreaInfo.setWidthRes(resolution);
      objectAreaInfo.setHeightRes(resolution);
      if (log.isDebugEnabled()) {
         long duration = System.currentTimeMillis() - start;
         log.debug("Image encoding took " + duration + "ms.");
      }

      return imageObjectInfo;
   }

   private boolean encodeInvertedBilevel(ImageEncodingHelper helper, AFPImageObjectInfo imageObjectInfo, OutputStream out) throws IOException {
      RenderedImage renderedImage = helper.getImage();
      if (!BitmapImageUtil.isMonochromeImage(renderedImage)) {
         throw new IllegalStateException("This method only supports binary images!");
      } else {
         int tiles = renderedImage.getNumXTiles() * renderedImage.getNumYTiles();
         if (tiles > 1) {
            return false;
         } else {
            imageObjectInfo.setBitsPerPixel(1);
            Raster raster = renderedImage.getTile(0, 0);
            DataBuffer buffer = raster.getDataBuffer();
            if (!(buffer instanceof DataBufferByte)) {
               return false;
            } else {
               DataBufferByte byteBuffer = (DataBufferByte)buffer;
               log.debug("Encoding image as inverted bi-level...");
               byte[] rawData = byteBuffer.getData();
               int remaining = rawData.length;
               int pos = 0;

               int size;
               for(byte[] data = new byte[4096]; remaining > 0; remaining -= size) {
                  size = Math.min(remaining, data.length);

                  for(int i = 0; i < size; ++i) {
                     data[i] = (byte)(~rawData[pos]);
                     ++pos;
                  }

                  out.write(data, 0, size);
               }

               return true;
            }
         }
      }
   }

   private void setDefaultResourceLevel(AFPImageObjectInfo imageObjectInfo, AFPResourceManager resourceManager) {
      AFPResourceInfo resourceInfo = imageObjectInfo.getResourceInfo();
      if (!resourceInfo.levelChanged()) {
         resourceInfo.setLevel(resourceManager.getResourceLevelDefaults().getDefaultResourceLevel((byte)6));
      }

   }

   protected AFPDataObjectInfo createDataObjectInfo() {
      return new AFPImageObjectInfo();
   }

   public int getPriority() {
      return 300;
   }

   public Class getSupportedImageClass() {
      return ImageRendered.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      AFPRenderingContext afpContext = (AFPRenderingContext)context;
      AFPImageObjectInfo imageObjectInfo = (AFPImageObjectInfo)this.createDataObjectInfo();
      this.setResourceInformation(imageObjectInfo, image.getInfo().getOriginalURI(), afpContext.getForeignAttributes());
      this.setDefaultResourceLevel(imageObjectInfo, afpContext.getResourceManager());
      imageObjectInfo.setObjectAreaInfo(createObjectAreaInfo(afpContext.getPaintingState(), pos));
      Dimension targetSize = pos.getSize();
      ImageRendered imageRend = (ImageRendered)image;
      this.updateDataObjectInfo(imageObjectInfo, afpContext.getPaintingState(), imageRend, targetSize);
      afpContext.getResourceManager().createObject(imageObjectInfo);
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageRendered) && targetContext instanceof AFPRenderingContext;
   }

   static {
      log = LogFactory.getLog(AFPImageHandlerRenderedImage.class);
      FLAVORS = new ImageFlavor[]{ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE};
   }
}
