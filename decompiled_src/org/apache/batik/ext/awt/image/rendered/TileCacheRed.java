package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;

public class TileCacheRed extends AbstractTiledRed {
   public TileCacheRed(CachableRed var1) {
      super((CachableRed)var1, (Map)null);
   }

   public TileCacheRed(CachableRed var1, int var2, int var3) {
      ColorModel var4 = var1.getColorModel();
      Rectangle var5 = var1.getBounds();
      if (var2 > var5.width) {
         var2 = var5.width;
      }

      if (var3 > var5.height) {
         var3 = var5.height;
      }

      SampleModel var6 = var4.createCompatibleSampleModel(var2, var3);
      this.init(var1, var5, var4, var6, var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
   }

   public void genRect(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      var2.copyData(var1);
   }

   public void flushCache(Rectangle var1) {
      int var2 = this.getXTile(var1.x);
      int var3 = this.getYTile(var1.y);
      int var4 = this.getXTile(var1.x + var1.width - 1);
      int var5 = this.getYTile(var1.y + var1.height - 1);
      if (var2 < this.minTileX) {
         var2 = this.minTileX;
      }

      if (var3 < this.minTileY) {
         var3 = this.minTileY;
      }

      if (var4 >= this.minTileX + this.numXTiles) {
         var4 = this.minTileX + this.numXTiles - 1;
      }

      if (var5 >= this.minTileY + this.numYTiles) {
         var5 = this.minTileY + this.numYTiles - 1;
      }

      if (var4 >= var2 && var5 >= var3) {
         TileStore var6 = this.getTileStore();

         for(int var7 = var3; var7 <= var5; ++var7) {
            for(int var8 = var2; var8 <= var4; ++var8) {
               var6.setTile(var8, var7, (Raster)null);
            }
         }

      }
   }
}
