package org.apache.batik.ext.awt.image.rendered;

import java.awt.image.Raster;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

public class TileLRUMember implements LRUCache.LRUObj {
   private static final boolean DEBUG = false;
   protected LRUCache.LRUNode myNode = null;
   protected Reference wRaster = null;
   protected Raster hRaster = null;

   public TileLRUMember() {
   }

   public TileLRUMember(Raster var1) {
      this.setRaster(var1);
   }

   public void setRaster(Raster var1) {
      this.hRaster = var1;
      this.wRaster = new SoftReference(var1);
   }

   public boolean checkRaster() {
      if (this.hRaster != null) {
         return true;
      } else {
         return this.wRaster != null && this.wRaster.get() != null;
      }
   }

   public Raster retrieveRaster() {
      if (this.hRaster != null) {
         return this.hRaster;
      } else if (this.wRaster == null) {
         return null;
      } else {
         this.hRaster = (Raster)this.wRaster.get();
         if (this.hRaster == null) {
            this.wRaster = null;
         }

         return this.hRaster;
      }
   }

   public LRUCache.LRUNode lruGet() {
      return this.myNode;
   }

   public void lruSet(LRUCache.LRUNode var1) {
      this.myNode = var1;
   }

   public void lruRemove() {
      this.myNode = null;
      this.hRaster = null;
   }
}
