package org.apache.batik.ext.awt.image.codec.imageio;

import org.apache.batik.ext.awt.image.spi.MagicNumberRegistryEntry;

public class ImageIOTIFFRegistryEntry extends AbstractImageIORegistryEntry {
   static final byte[] sig1 = new byte[]{73, 73, 42, 0};
   static final byte[] sig2 = new byte[]{77, 77, 0, 42};
   static MagicNumberRegistryEntry.MagicNumber[] magicNumbers;
   static final String[] exts;
   static final String[] mimeTypes;

   public ImageIOTIFFRegistryEntry() {
      super("TIFF", exts, mimeTypes, magicNumbers);
   }

   static {
      magicNumbers = new MagicNumberRegistryEntry.MagicNumber[]{new MagicNumberRegistryEntry.MagicNumber(0, sig1), new MagicNumberRegistryEntry.MagicNumber(0, sig2)};
      exts = new String[]{"tiff", "tif"};
      mimeTypes = new String[]{"image/tiff", "image/tif"};
   }
}
