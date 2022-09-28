package org.apache.fop.render.afp;

import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPImageObjectInfo;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageRawCCITTFax;
import org.apache.xmlgraphics.image.loader.impl.ImageRawStream;

public class AFPImageHandlerRawCCITTFax extends AbstractAFPImageHandlerRawStream {
   private static final ImageFlavor[] FLAVORS;

   protected void setAdditionalParameters(AFPDataObjectInfo dataObjectInfo, ImageRawStream image) {
      AFPImageObjectInfo imageObjectInfo = (AFPImageObjectInfo)dataObjectInfo;
      ImageRawCCITTFax ccitt = (ImageRawCCITTFax)image;
      int compression = ccitt.getCompression();
      imageObjectInfo.setCompression(compression);
      imageObjectInfo.setBitsPerPixel(1);
      imageObjectInfo.setMimeType("image/tiff");
   }

   protected AFPDataObjectInfo createDataObjectInfo() {
      return new AFPImageObjectInfo();
   }

   public int getPriority() {
      return 400;
   }

   public Class getSupportedImageClass() {
      return ImageRawCCITTFax.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      if (!(targetContext instanceof AFPRenderingContext)) {
         return false;
      } else {
         AFPRenderingContext afpContext = (AFPRenderingContext)targetContext;
         return afpContext.getPaintingState().isNativeImagesSupported() && (image == null || image instanceof ImageRawCCITTFax);
      }
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RAW_CCITTFAX};
   }
}
