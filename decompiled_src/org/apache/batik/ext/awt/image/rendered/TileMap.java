package org.apache.batik.ext.awt.image.rendered;

import java.awt.Point;
import java.awt.image.Raster;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import org.apache.batik.util.CleanerThread;
import org.apache.batik.util.HaltingThread;

public class TileMap implements TileStore {
   private static final boolean DEBUG = false;
   private static final boolean COUNT = false;
   private HashMap rasters = new HashMap();
   private TileGenerator source = null;
   private LRUCache cache = null;
   static int requests;
   static int misses;

   public TileMap(TileGenerator var1, LRUCache var2) {
      this.cache = var2;
      this.source = var1;
   }

   public void setTile(int var1, int var2, Raster var3) {
      Point var4 = new Point(var1, var2);
      Object var5;
      if (var3 == null) {
         var5 = this.rasters.remove(var4);
         if (var5 != null) {
            this.cache.remove((TileMapLRUMember)var5);
         }

      } else {
         var5 = this.rasters.get(var4);
         TileMapLRUMember var6;
         if (var5 == null) {
            var6 = new TileMapLRUMember(this, var4, var3);
            this.rasters.put(var4, var6);
         } else {
            var6 = (TileMapLRUMember)var5;
            var6.setRaster(var3);
         }

         this.cache.add(var6);
      }
   }

   public Raster getTileNoCompute(int var1, int var2) {
      Point var3 = new Point(var1, var2);
      Object var4 = this.rasters.get(var3);
      if (var4 == null) {
         return null;
      } else {
         TileMapLRUMember var5 = (TileMapLRUMember)var4;
         Raster var6 = var5.retrieveRaster();
         if (var6 != null) {
            this.cache.add(var5);
         }

         return var6;
      }
   }

   public Raster getTile(int var1, int var2) {
      Raster var3 = null;
      Point var4 = new Point(var1, var2);
      Object var5 = this.rasters.get(var4);
      TileMapLRUMember var6 = null;
      if (var5 != null) {
         var6 = (TileMapLRUMember)var5;
         var3 = var6.retrieveRaster();
      }

      if (var3 == null) {
         var3 = this.source.genTile(var1, var2);
         if (HaltingThread.hasBeenHalted()) {
            return var3;
         }

         if (var6 != null) {
            var6.setRaster(var3);
         } else {
            var6 = new TileMapLRUMember(this, var4, var3);
            this.rasters.put(var4, var6);
         }
      }

      this.cache.add(var6);
      return var3;
   }

   static class TileMapLRUMember extends TileLRUMember {
      public Point pt;
      public SoftReference parent;

      TileMapLRUMember(TileMap var1, Point var2, Raster var3) {
         super(var3);
         this.parent = new SoftReference(var1);
         this.pt = var2;
      }

      public void setRaster(Raster var1) {
         this.hRaster = var1;
         this.wRaster = new RasterSoftRef(var1);
      }

      class RasterSoftRef extends CleanerThread.SoftReferenceCleared {
         RasterSoftRef(Object var2) {
            super(var2);
         }

         public void cleared() {
            TileMap var1 = (TileMap)TileMapLRUMember.this.parent.get();
            if (var1 != null) {
               var1.rasters.remove(TileMapLRUMember.this.pt);
            }

         }
      }
   }
}
