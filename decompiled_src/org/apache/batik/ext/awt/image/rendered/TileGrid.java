package org.apache.batik.ext.awt.image.rendered;

import java.awt.image.Raster;
import org.apache.batik.util.HaltingThread;

public class TileGrid implements TileStore {
   private static final boolean DEBUG = false;
   private static final boolean COUNT = false;
   private int xSz;
   private int ySz;
   private int minTileX;
   private int minTileY;
   private TileLRUMember[][] rasters = (TileLRUMember[][])null;
   private TileGenerator source = null;
   private LRUCache cache = null;
   static int requests;
   static int misses;

   public TileGrid(int var1, int var2, int var3, int var4, TileGenerator var5, LRUCache var6) {
      this.cache = var6;
      this.source = var5;
      this.minTileX = var1;
      this.minTileY = var2;
      this.xSz = var3;
      this.ySz = var4;
      this.rasters = new TileLRUMember[var4][];
   }

   public void setTile(int var1, int var2, Raster var3) {
      var1 -= this.minTileX;
      var2 -= this.minTileY;
      if (var1 >= 0 && var1 < this.xSz) {
         if (var2 >= 0 && var2 < this.ySz) {
            TileLRUMember[] var4 = this.rasters[var2];
            TileLRUMember var5;
            if (var3 == null) {
               if (var4 != null) {
                  var5 = var4[var1];
                  if (var5 != null) {
                     var4[var1] = null;
                     this.cache.remove(var5);
                  }
               }
            } else {
               if (var4 != null) {
                  var5 = var4[var1];
                  if (var5 == null) {
                     var5 = new TileLRUMember();
                     var4[var1] = var5;
                  }
               } else {
                  var4 = new TileLRUMember[this.xSz];
                  var5 = new TileLRUMember();
                  var4[var1] = var5;
                  this.rasters[var2] = var4;
               }

               var5.setRaster(var3);
               this.cache.add(var5);
            }
         }
      }
   }

   public Raster getTileNoCompute(int var1, int var2) {
      var1 -= this.minTileX;
      var2 -= this.minTileY;
      if (var1 >= 0 && var1 < this.xSz) {
         if (var2 >= 0 && var2 < this.ySz) {
            TileLRUMember[] var3 = this.rasters[var2];
            if (var3 == null) {
               return null;
            } else {
               TileLRUMember var4 = var3[var1];
               if (var4 == null) {
                  return null;
               } else {
                  Raster var5 = var4.retrieveRaster();
                  if (var5 != null) {
                     this.cache.add(var4);
                  }

                  return var5;
               }
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public Raster getTile(int var1, int var2) {
      var1 -= this.minTileX;
      var2 -= this.minTileY;
      if (var1 >= 0 && var1 < this.xSz) {
         if (var2 >= 0 && var2 < this.ySz) {
            Raster var3 = null;
            TileLRUMember[] var4 = this.rasters[var2];
            TileLRUMember var5 = null;
            if (var4 != null) {
               var5 = var4[var1];
               if (var5 != null) {
                  var3 = var5.retrieveRaster();
               } else {
                  var5 = new TileLRUMember();
                  var4[var1] = var5;
               }
            } else {
               var4 = new TileLRUMember[this.xSz];
               this.rasters[var2] = var4;
               var5 = new TileLRUMember();
               var4[var1] = var5;
            }

            if (var3 == null) {
               var3 = this.source.genTile(var1 + this.minTileX, var2 + this.minTileY);
               if (HaltingThread.hasBeenHalted()) {
                  return var3;
               }

               var5.setRaster(var3);
            }

            this.cache.add(var5);
            return var3;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
