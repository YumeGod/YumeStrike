package org.apache.batik.ext.awt.image.rendered;

import java.awt.image.RenderedImage;

public class TileCache {
   private static LRUCache cache = new LRUCache(50);

   public static void setSize(int var0) {
      cache.setSize(var0);
   }

   public static TileStore getTileGrid(int var0, int var1, int var2, int var3, TileGenerator var4) {
      return new TileGrid(var0, var1, var2, var3, var4, cache);
   }

   public static TileStore getTileGrid(RenderedImage var0, TileGenerator var1) {
      return new TileGrid(var0.getMinTileX(), var0.getMinTileY(), var0.getNumXTiles(), var0.getNumYTiles(), var1, cache);
   }

   public static TileStore getTileMap(TileGenerator var0) {
      return new TileMap(var0, cache);
   }
}
