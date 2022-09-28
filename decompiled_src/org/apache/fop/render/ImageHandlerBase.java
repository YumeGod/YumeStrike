package org.apache.fop.render;

import org.apache.xmlgraphics.image.loader.ImageFlavor;

public interface ImageHandlerBase {
   int getPriority();

   ImageFlavor[] getSupportedImageFlavors();

   Class getSupportedImageClass();
}
