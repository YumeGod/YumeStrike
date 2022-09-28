package org.apache.xmlgraphics.image.loader.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImageCacheStatistics implements ImageCacheListener {
   private int invalidHits;
   private int imageInfoCacheHits;
   private int imageInfoCacheMisses;
   private int imageCacheHits;
   private int imageCacheMisses;
   private Map imageCacheHitMap;
   private Map imageCacheMissMap;

   public ImageCacheStatistics(boolean detailed) {
      if (detailed) {
         this.imageCacheHitMap = new HashMap();
         this.imageCacheMissMap = new HashMap();
      }

   }

   public void reset() {
      this.imageInfoCacheHits = 0;
      this.imageInfoCacheMisses = 0;
      this.invalidHits = 0;
   }

   public void invalidHit(String uri) {
      ++this.invalidHits;
   }

   public void cacheHitImageInfo(String uri) {
      ++this.imageInfoCacheHits;
   }

   public void cacheMissImageInfo(String uri) {
      ++this.imageInfoCacheMisses;
   }

   private void increaseEntry(Map map, Object key) {
      Integer v = (Integer)map.get(key);
      if (v == null) {
         v = new Integer(1);
      } else {
         v = new Integer(v + 1);
      }

      map.put(key, v);
   }

   public void cacheHitImage(ImageKey key) {
      ++this.imageCacheHits;
      if (this.imageCacheHitMap != null) {
         this.increaseEntry(this.imageCacheHitMap, key);
      }

   }

   public void cacheMissImage(ImageKey key) {
      ++this.imageCacheMisses;
      if (this.imageCacheMissMap != null) {
         this.increaseEntry(this.imageCacheMissMap, key);
      }

   }

   public int getInvalidHits() {
      return this.invalidHits;
   }

   public int getImageInfoCacheHits() {
      return this.imageInfoCacheHits;
   }

   public int getImageInfoCacheMisses() {
      return this.imageInfoCacheMisses;
   }

   public int getImageCacheHits() {
      return this.imageCacheHits;
   }

   public int getImageCacheMisses() {
      return this.imageCacheMisses;
   }

   public Map getImageCacheHitMap() {
      return Collections.unmodifiableMap(this.imageCacheHitMap);
   }

   public Map getImageCacheMissMap() {
      return Collections.unmodifiableMap(this.imageCacheMissMap);
   }
}
