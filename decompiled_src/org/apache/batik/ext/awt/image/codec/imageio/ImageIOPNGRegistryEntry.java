package org.apache.batik.ext.awt.image.codec.imageio;

public class ImageIOPNGRegistryEntry extends AbstractImageIORegistryEntry {
   static final byte[] signature = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};

   public ImageIOPNGRegistryEntry() {
      super("PNG", "png", "image/png", 0, signature);
   }
}
