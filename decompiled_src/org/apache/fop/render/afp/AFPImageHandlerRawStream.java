package org.apache.fop.render.afp;

import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageRawEPS;
import org.apache.xmlgraphics.image.loader.impl.ImageRawJPEG;
import org.apache.xmlgraphics.image.loader.impl.ImageRawStream;

public class AFPImageHandlerRawStream extends AbstractAFPImageHandlerRawStream {
   private static final ImageFlavor[] FLAVORS;

   public int getPriority() {
      return 200;
   }

   public Class getSupportedImageClass() {
      return ImageRawStream.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   protected AFPDataObjectInfo createDataObjectInfo() {
      return new AFPDataObjectInfo();
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      if (!(targetContext instanceof AFPRenderingContext)) {
         return false;
      } else {
         AFPRenderingContext afpContext = (AFPRenderingContext)targetContext;
         return afpContext.getPaintingState().isNativeImagesSupported() && (image == null || image instanceof ImageRawJPEG || image instanceof ImageRawEPS || image instanceof ImageRawStream && "image/tiff".equals(((ImageRawStream)image).getMimeType()));
      }
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RAW_JPEG, ImageFlavor.RAW_TIFF, ImageFlavor.RAW_EPS};
   }
}
