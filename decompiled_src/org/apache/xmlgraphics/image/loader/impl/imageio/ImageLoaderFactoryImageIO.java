package org.apache.xmlgraphics.image.loader.impl.imageio;

import javax.imageio.ImageIO;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class ImageLoaderFactoryImageIO extends AbstractImageLoaderFactory {
   private static final ImageFlavor[] FLAVORS;

   public String[] getSupportedMIMETypes() {
      return ImageIO.getReaderMIMETypes();
   }

   public ImageFlavor[] getSupportedFlavors(String mime) {
      return FLAVORS;
   }

   public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
      return new ImageLoaderImageIO(targetFlavor);
   }

   public boolean isAvailable() {
      return this.getSupportedMIMETypes().length > 0;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RENDERED_IMAGE, ImageFlavor.BUFFERED_IMAGE};
   }
}
