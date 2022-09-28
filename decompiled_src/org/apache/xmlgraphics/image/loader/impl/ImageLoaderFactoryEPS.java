package org.apache.xmlgraphics.image.loader.impl;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class ImageLoaderFactoryEPS extends AbstractImageLoaderFactory {
   private static final String[] MIMES = new String[]{"application/postscript"};
   private static final ImageFlavor[] FLAVORS;

   public String[] getSupportedMIMETypes() {
      return MIMES;
   }

   public ImageFlavor[] getSupportedFlavors(String mime) {
      if ("application/postscript".equals(mime)) {
         return FLAVORS;
      } else {
         throw new IllegalArgumentException("Unsupported MIME type: " + mime);
      }
   }

   public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
      return new ImageLoaderEPS();
   }

   public boolean isAvailable() {
      return true;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RAW_EPS};
   }
}
