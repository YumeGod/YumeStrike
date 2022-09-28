package org.apache.xmlgraphics.image.loader.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class ImageLoaderFactoryRawCCITTFax extends AbstractImageLoaderFactory {
   private transient Log log;
   private static final String[] MIMES = new String[]{"image/tiff"};
   private static final ImageFlavor[][] FLAVORS;

   public ImageLoaderFactoryRawCCITTFax() {
      this.log = LogFactory.getLog(ImageLoaderFactoryRawCCITTFax.class);
   }

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
      if (targetFlavor.equals(ImageFlavor.RAW_CCITTFAX)) {
         return new ImageLoaderRawCCITTFax();
      } else {
         throw new IllegalArgumentException("Unsupported image flavor: " + targetFlavor);
      }
   }

   public boolean isAvailable() {
      return true;
   }

   public boolean isSupported(ImageInfo imageInfo) {
      Boolean tiled = (Boolean)imageInfo.getCustomObjects().get("TIFF_TILED");
      if (Boolean.TRUE.equals(tiled)) {
         this.log.trace("Raw CCITT loading not supported for tiled TIFF image");
         return false;
      } else {
         Integer compression = (Integer)imageInfo.getCustomObjects().get("TIFF_COMPRESSION");
         if (compression == null) {
            return false;
         } else {
            switch (compression) {
               case 2:
               case 3:
               case 4:
                  Integer stripCount = (Integer)imageInfo.getCustomObjects().get("TIFF_STRIP_COUNT");
                  boolean supported = stripCount != null && stripCount == 1;
                  if (!supported) {
                     this.log.trace("Raw CCITT loading not supported for multi-strip TIFF image");
                  }

                  return supported;
               default:
                  return false;
            }
         }
      }
   }

   static {
      FLAVORS = new ImageFlavor[][]{{ImageFlavor.RAW_CCITTFAX}};
   }
}
