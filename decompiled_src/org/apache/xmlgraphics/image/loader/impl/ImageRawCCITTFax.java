package org.apache.xmlgraphics.image.loader.impl;

import java.awt.color.ColorSpace;
import java.io.InputStream;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public class ImageRawCCITTFax extends ImageRawStream {
   private int compression;

   public ImageRawCCITTFax(ImageInfo info, InputStream in, int compression) {
      super(info, ImageFlavor.RAW_CCITTFAX, in);
      this.compression = compression;
   }

   public ColorSpace getColorSpace() {
      return ColorSpace.getInstance(1003);
   }

   public int getCompression() {
      return this.compression;
   }
}
