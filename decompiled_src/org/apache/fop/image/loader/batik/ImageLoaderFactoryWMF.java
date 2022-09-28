package org.apache.fop.image.loader.batik;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class ImageLoaderFactoryWMF extends AbstractImageLoaderFactory {
   private static final ImageFlavor[] FLAVORS;
   private static final String[] MIMES;

   public String[] getSupportedMIMETypes() {
      return MIMES;
   }

   public ImageFlavor[] getSupportedFlavors(String mime) {
      return FLAVORS;
   }

   public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
      return new ImageLoaderWMF(targetFlavor);
   }

   public boolean isAvailable() {
      return BatikUtil.isBatikAvailable();
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageWMF.WMF_IMAGE};
      MIMES = new String[]{"image/x-wmf"};
   }
}
