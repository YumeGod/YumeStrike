package org.apache.xmlgraphics.image.loader.impl;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class ImageLoaderFactoryInternalTIFF extends AbstractImageLoaderFactory {
   private static final String[] MIMES = new String[]{"image/tiff"};
   private static final ImageFlavor[] FLAVORS;

   public String[] getSupportedMIMETypes() {
      return MIMES;
   }

   public ImageFlavor[] getSupportedFlavors(String mime) {
      if ("image/tiff".equals(mime)) {
         return FLAVORS;
      } else {
         throw new IllegalArgumentException("Unsupported MIME type: " + mime);
      }
   }

   public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
      return new ImageLoaderInternalTIFF();
   }

   public boolean isAvailable() {
      return true;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RENDERED_IMAGE};
   }
}
