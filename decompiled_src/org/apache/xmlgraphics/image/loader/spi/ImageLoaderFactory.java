package org.apache.xmlgraphics.image.loader.spi;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public interface ImageLoaderFactory {
   String[] getSupportedMIMETypes();

   ImageFlavor[] getSupportedFlavors(String var1);

   boolean isSupported(ImageInfo var1);

   ImageLoader newImageLoader(ImageFlavor var1);

   /** @deprecated */
   int getUsagePenalty(String var1, ImageFlavor var2);

   boolean isAvailable();
}
