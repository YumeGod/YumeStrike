package org.apache.batik.ext.awt.image.codec.util;

import java.awt.image.ColorModel;
import java.awt.image.Raster;

public class SingleTileRenderedImage extends SimpleRenderedImage {
   Raster ras;

   public SingleTileRenderedImage(Raster var1, ColorModel var2) {
      this.ras = var1;
      this.tileGridXOffset = this.minX = var1.getMinX();
      this.tileGridYOffset = this.minY = var1.getMinY();
      this.tileWidth = this.width = var1.getWidth();
      this.tileHeight = this.height = var1.getHeight();
      this.sampleModel = var1.getSampleModel();
      this.colorModel = var2;
   }

   public Raster getTile(int var1, int var2) {
      if (var1 == 0 && var2 == 0) {
         return this.ras;
      } else {
         throw new IllegalArgumentException(PropertyUtil.getString("SingleTileRenderedImage0"));
      }
   }
}
