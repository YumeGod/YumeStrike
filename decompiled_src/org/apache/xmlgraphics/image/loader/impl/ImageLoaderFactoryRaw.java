package org.apache.xmlgraphics.image.loader.impl;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class ImageLoaderFactoryRaw extends AbstractImageLoaderFactory {
   public static final String MIME_EMF = "image/x-emf";
   private static final String[] MIMES = new String[]{"image/png", "image/jpeg", "image/tiff", "image/x-emf"};
   private static final ImageFlavor[][] FLAVORS;

   public static String getMimeForRawFlavor(ImageFlavor flavor) {
      int i = 0;

      for(int ci = FLAVORS.length; i < ci; ++i) {
         int j = 0;

         for(int cj = FLAVORS[i].length; j < cj; ++j) {
            if (FLAVORS[i][j].equals(flavor)) {
               return MIMES[i];
            }
         }
      }

      throw new IllegalArgumentException("ImageFlavor is not a \"raw\" flavor: " + flavor);
   }

   public String[] getSupportedMIMETypes() {
      return MIMES;
   }

   public ImageFlavor[] getSupportedFlavors(String mime) {
      int i = 0;

      for(int c = MIMES.length; i < c; ++i) {
         if (MIMES[i].equals(mime)) {
            return FLAVORS[i];
         }
      }

      throw new IllegalArgumentException("Unsupported MIME type: " + mime);
   }

   public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
      return (ImageLoader)(targetFlavor.equals(ImageFlavor.RAW_JPEG) ? new ImageLoaderRawJPEG() : new ImageLoaderRaw(targetFlavor));
   }

   public boolean isAvailable() {
      return true;
   }

   static {
      FLAVORS = new ImageFlavor[][]{{ImageFlavor.RAW_PNG}, {ImageFlavor.RAW_JPEG}, {ImageFlavor.RAW_TIFF}, {ImageFlavor.RAW_EMF}};
   }
}
