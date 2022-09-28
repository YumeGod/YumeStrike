package org.apache.fop.image.loader.batik;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.XMLNamespaceEnabledImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class ImageLoaderFactorySVG extends AbstractImageLoaderFactory {
   private static final ImageFlavor[] FLAVORS;
   private static final String[] MIMES;

   public String[] getSupportedMIMETypes() {
      return MIMES;
   }

   public ImageFlavor[] getSupportedFlavors(String mime) {
      return FLAVORS;
   }

   public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
      return new ImageLoaderSVG(targetFlavor);
   }

   public boolean isAvailable() {
      return BatikUtil.isBatikAvailable();
   }

   static {
      FLAVORS = new ImageFlavor[]{XMLNamespaceEnabledImageFlavor.SVG_DOM};
      MIMES = new String[]{"image/svg+xml"};
   }
}
