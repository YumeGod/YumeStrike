package org.apache.batik.extension.svg;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.rendered.AbstractRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;

public class HistogramRed extends AbstractRed {
   boolean[] computed;
   int tallied = 0;
   int[] bins = new int[256];

   public HistogramRed(CachableRed var1) {
      super((CachableRed)var1, (Map)null);
      int var2 = this.getNumXTiles() * this.getNumYTiles();
      this.computed = new boolean[var2];
   }

   public void tallyTile(Raster var1) {
      int var2 = var1.getMinX();
      int var3 = var1.getMinY();
      int var4 = var1.getWidth();
      int var5 = var1.getHeight();
      int[] var6 = null;

      for(int var8 = var3; var8 < var3 + var5; ++var8) {
         var6 = var1.getPixels(var2, var8, var4, 1, var6);

         for(int var9 = 0; var9 < 3 * var4; ++var9) {
            int var7 = var6[var9++] * 5;
            var7 += var6[var9++] * 9;
            var7 += var6[var9++] * 2;
            int var10002 = this.bins[var7 >> 4]++;
         }
      }

      ++this.tallied;
   }

   public int[] getHistogram() {
      if (this.tallied == this.computed.length) {
         return this.bins;
      } else {
         CachableRed var1 = (CachableRed)this.getSources().get(0);
         int var2 = var1.getMinTileY();
         int var3 = var1.getNumXTiles();
         int var4 = var1.getMinTileX();

         for(int var5 = 0; var5 < var1.getNumYTiles(); ++var5) {
            for(int var6 = 0; var6 < var3; ++var6) {
               int var7 = var6 + var4 + var5 * var3;
               if (!this.computed[var7]) {
                  Raster var8 = var1.getTile(var6 + var4, var5 + var2);
                  this.tallyTile(var8);
                  this.computed[var7] = true;
               }
            }
         }

         return this.bins;
      }
   }

   public WritableRaster copyData(WritableRaster var1) {
      this.copyToRaster(var1);
      return var1;
   }

   public Raster getTile(int var1, int var2) {
      int var3 = var2 - this.getMinTileY();
      int var4 = var1 - this.getMinTileX();
      CachableRed var5 = (CachableRed)this.getSources().get(0);
      Raster var6 = var5.getTile(var1, var2);
      int var7 = var4 + var3 * this.getNumXTiles();
      if (this.computed[var7]) {
         return var6;
      } else {
         this.tallyTile(var6);
         this.computed[var7] = true;
         return var6;
      }
   }
}
