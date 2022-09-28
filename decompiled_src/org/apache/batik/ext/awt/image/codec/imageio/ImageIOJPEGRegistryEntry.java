package org.apache.batik.ext.awt.image.codec.imageio;

import org.apache.batik.ext.awt.image.spi.MagicNumberRegistryEntry;

public class ImageIOJPEGRegistryEntry extends AbstractImageIORegistryEntry {
   static final byte[] sigJPEG = new byte[]{-1, -40, -1};
   static final String[] exts = new String[]{"jpeg", "jpg"};
   static final String[] mimeTypes = new String[]{"image/jpeg", "image/jpg"};
   static final MagicNumberRegistryEntry.MagicNumber[] magicNumbers;

   public ImageIOJPEGRegistryEntry() {
      super("JPEG", exts, mimeTypes, magicNumbers);
   }

   static {
      magicNumbers = new MagicNumberRegistryEntry.MagicNumber[]{new MagicNumberRegistryEntry.MagicNumber(0, sigJPEG)};
   }
}
