package org.apache.batik.ext.awt.image.rendered;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.util.HaltingThread;

public abstract class AbstractTiledRed extends AbstractRed implements TileGenerator {
   private TileStore tiles;
   private static int defaultTileSize = 128;

   public static int getDefaultTileSize() {
      return defaultTileSize;
   }

   protected AbstractTiledRed() {
   }

   protected AbstractTiledRed(Rectangle var1, Map var2) {
      super(var1, var2);
   }

   protected AbstractTiledRed(CachableRed var1, Map var2) {
      super(var1, var2);
   }

   protected AbstractTiledRed(CachableRed var1, Rectangle var2, Map var3) {
      super(var1, var2, var3);
   }

   protected AbstractTiledRed(CachableRed var1, Rectangle var2, ColorModel var3, SampleModel var4, Map var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected AbstractTiledRed(CachableRed var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected void init(CachableRed var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      this.init(var1, var2, var3, var4, var5, var6, (TileStore)null, var7);
   }

   protected void init(CachableRed var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, TileStore var7, Map var8) {
      super.init(var1, var2, var3, var4, var5, var6, var8);
      this.tiles = var7;
      if (this.tiles == null) {
         this.tiles = this.createTileStore();
      }

   }

   protected AbstractTiledRed(List var1, Rectangle var2, Map var3) {
      super(var1, var2, var3);
   }

   protected AbstractTiledRed(List var1, Rectangle var2, ColorModel var3, SampleModel var4, Map var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected AbstractTiledRed(List var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected void init(List var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      super.init(var1, var2, var3, var4, var5, var6, var7);
      this.tiles = this.createTileStore();
   }

   public TileStore getTileStore() {
      return this.tiles;
   }

   protected void setTileStore(TileStore var1) {
      this.tiles = var1;
   }

   protected TileStore createTileStore() {
      return TileCache.getTileMap(this);
   }

   public WritableRaster copyData(WritableRaster var1) {
      this.copyToRasterByBlocks(var1);
      return var1;
   }

   public Raster getData(Rectangle var1) {
      int var2 = this.getXTile(var1.x);
      int var3 = this.getXTile(var1.x + var1.width - 1);
      int var4 = this.getYTile(var1.y);
      int var5 = this.getYTile(var1.y + var1.height - 1);
      if (var2 == var3 && var4 == var5) {
         Raster var6 = this.getTile(var2, var4);
         return var6.createChild(var1.x, var1.y, var1.width, var1.height, var1.x, var1.y, (int[])null);
      } else {
         return super.getData(var1);
      }
   }

   public Raster getTile(int var1, int var2) {
      return this.tiles.getTile(var1, var2);
   }

   public Raster genTile(int var1, int var2) {
      WritableRaster var3 = this.makeTile(var1, var2);
      this.genRect(var3);
      return var3;
   }

   public abstract void genRect(WritableRaster var1);

   public void setTile(int var1, int var2, Raster var3) {
      this.tiles.setTile(var1, var2, var3);
   }

   public void copyToRasterByBlocks(WritableRaster var1) {
      boolean var2 = GraphicsUtil.is_INT_PACK_Data(this.getSampleModel(), false);
      Rectangle var3 = this.getBounds();
      Rectangle var4 = var1.getBounds();
      int var5 = this.getXTile(var4.x);
      int var6 = this.getYTile(var4.y);
      int var7 = this.getXTile(var4.x + var4.width - 1);
      int var8 = this.getYTile(var4.y + var4.height - 1);
      if (var5 < this.minTileX) {
         var5 = this.minTileX;
      }

      if (var6 < this.minTileY) {
         var6 = this.minTileY;
      }

      if (var7 >= this.minTileX + this.numXTiles) {
         var7 = this.minTileX + this.numXTiles - 1;
      }

      if (var8 >= this.minTileY + this.numYTiles) {
         var8 = this.minTileY + this.numYTiles - 1;
      }

      if (var7 >= var5 && var8 >= var6) {
         int var9 = var5;
         int var10 = var7;
         int var11 = var6;
         int var12 = var8;
         int var13 = var5 * this.tileWidth + this.tileGridXOff;
         if (var13 < var4.x && var3.x != var4.x) {
            var9 = var5 + 1;
         }

         int var14 = var6 * this.tileHeight + this.tileGridYOff;
         if (var14 < var4.y && var3.y != var4.y) {
            var11 = var6 + 1;
         }

         var13 = (var7 + 1) * this.tileWidth + this.tileGridXOff - 1;
         if (var13 >= var4.x + var4.width && var3.x + var3.width != var4.x + var4.width) {
            var10 = var7 - 1;
         }

         var14 = (var8 + 1) * this.tileHeight + this.tileGridYOff - 1;
         if (var14 >= var4.y + var4.height && var3.y + var3.height != var4.y + var4.height) {
            var12 = var8 - 1;
         }

         int var15 = var10 - var9 + 1;
         int var16 = var12 - var11 + 1;
         boolean[] var17 = null;
         if (var15 > 0 && var16 > 0) {
            var17 = new boolean[var15 * var16];
         }

         boolean[] var18 = new boolean[2 * (var7 - var5 + 1) + 2 * (var8 - var6 + 1)];
         int var19 = 0;
         int var20 = 0;

         for(int var21 = var6; var21 <= var8; ++var21) {
            for(int var22 = var5; var22 <= var7; ++var22) {
               Raster var23 = this.tiles.getTileNoCompute(var22, var21);
               boolean var24 = var23 != null;
               if (var21 >= var11 && var21 <= var12 && var22 >= var9 && var22 <= var10) {
                  var17[var22 - var9 + (var21 - var11) * var15] = var24;
               } else {
                  var18[var19++] = var24;
               }

               if (var24) {
                  ++var20;
                  if (var2) {
                     GraphicsUtil.copyData_INT_PACK(var23, var1);
                  } else {
                     GraphicsUtil.copyData_FALLBACK(var23, var1);
                  }
               }
            }
         }

         if (var15 > 0 && var16 > 0) {
            TileBlock var25 = new TileBlock(var9, var11, var15, var16, var17, 0, 0, var15, var16);
            this.drawBlock(var25, var1);
         }

         Thread var26 = Thread.currentThread();
         if (!HaltingThread.hasBeenHalted()) {
            var19 = 0;

            for(var14 = var6; var14 <= var8; ++var14) {
               for(var13 = var5; var13 <= var7; ++var13) {
                  Raster var27 = this.tiles.getTileNoCompute(var13, var14);
                  if (var14 >= var11 && var14 <= var12 && var13 >= var9 && var13 <= var10) {
                     if (var27 == null) {
                        WritableRaster var28 = this.makeTile(var13, var14);
                        if (var2) {
                           GraphicsUtil.copyData_INT_PACK(var1, var28);
                        } else {
                           GraphicsUtil.copyData_FALLBACK(var1, var28);
                        }

                        this.tiles.setTile(var13, var14, var28);
                     }
                  } else if (!var18[var19++]) {
                     var27 = this.getTile(var13, var14);
                     if (HaltingThread.hasBeenHalted(var26)) {
                        return;
                     }

                     if (var2) {
                        GraphicsUtil.copyData_INT_PACK(var27, var1);
                     } else {
                        GraphicsUtil.copyData_FALLBACK(var27, var1);
                     }
                  }
               }
            }

         }
      }
   }

   public void copyToRaster(WritableRaster var1) {
      Rectangle var2 = var1.getBounds();
      int var3 = this.getXTile(var2.x);
      int var4 = this.getYTile(var2.y);
      int var5 = this.getXTile(var2.x + var2.width - 1);
      int var6 = this.getYTile(var2.y + var2.height - 1);
      if (var3 < this.minTileX) {
         var3 = this.minTileX;
      }

      if (var4 < this.minTileY) {
         var4 = this.minTileY;
      }

      if (var5 >= this.minTileX + this.numXTiles) {
         var5 = this.minTileX + this.numXTiles - 1;
      }

      if (var6 >= this.minTileY + this.numYTiles) {
         var6 = this.minTileY + this.numYTiles - 1;
      }

      boolean var7 = GraphicsUtil.is_INT_PACK_Data(this.getSampleModel(), false);
      int var8 = var5 - var3 + 1;
      boolean[] var9 = new boolean[var8 * (var6 - var4 + 1)];

      int var10;
      int var11;
      Raster var12;
      for(var10 = var4; var10 <= var6; ++var10) {
         for(var11 = var3; var11 <= var5; ++var11) {
            var12 = this.tiles.getTileNoCompute(var11, var10);
            if (var12 != null) {
               var9[var11 - var3 + (var10 - var4) * var8] = true;
               if (var7) {
                  GraphicsUtil.copyData_INT_PACK(var12, var1);
               } else {
                  GraphicsUtil.copyData_FALLBACK(var12, var1);
               }
            }
         }
      }

      for(var10 = var4; var10 <= var6; ++var10) {
         for(var11 = var3; var11 <= var5; ++var11) {
            if (!var9[var11 - var3 + (var10 - var4) * var8]) {
               var12 = this.getTile(var11, var10);
               if (var7) {
                  GraphicsUtil.copyData_INT_PACK(var12, var1);
               } else {
                  GraphicsUtil.copyData_FALLBACK(var12, var1);
               }
            }
         }
      }

   }

   protected void drawBlock(TileBlock var1, WritableRaster var2) {
      TileBlock[] var3 = var1.getBestSplit();
      if (var3 != null) {
         this.drawBlockInPlace(var3, var2);
      }
   }

   protected void drawBlockAndCopy(TileBlock[] var1, WritableRaster var2) {
      int var4;
      int var5;
      if (var1.length == 1) {
         TileBlock var3 = var1[0];
         var4 = var3.getXLoc() * this.tileWidth + this.tileGridXOff;
         var5 = var3.getYLoc() * this.tileHeight + this.tileGridYOff;
         if (var4 == var2.getMinX() && var5 == var2.getMinY()) {
            this.drawBlockInPlace(var1, var2);
            return;
         }
      }

      int var17 = this.tileWidth;
      var4 = this.tileHeight;
      var5 = 0;

      for(int var6 = 0; var6 < var1.length; ++var6) {
         TileBlock var7 = var1[var6];
         int var8 = var7.getWidth() * var17 * var7.getHeight() * var4;
         if (var8 > var5) {
            var5 = var8;
         }
      }

      DataBufferInt var18 = new DataBufferInt(var5);
      int[] var19 = new int[]{16711680, 65280, 255, -16777216};
      boolean var20 = GraphicsUtil.is_INT_PACK_Data(var2.getSampleModel(), false);
      Thread var9 = Thread.currentThread();

      for(int var10 = 0; var10 < var1.length; ++var10) {
         TileBlock var11 = var1[var10];
         int var12 = var11.getXLoc() * var17 + this.tileGridXOff;
         int var13 = var11.getYLoc() * var4 + this.tileGridYOff;
         Rectangle var14 = new Rectangle(var12, var13, var11.getWidth() * var17, var11.getHeight() * var4);
         var14 = var14.intersection(this.bounds);
         Point var15 = new Point(var14.x, var14.y);
         WritableRaster var16 = Raster.createPackedRaster(var18, var14.width, var14.height, var14.width, var19, var15);
         this.genRect(var16);
         if (var20) {
            GraphicsUtil.copyData_INT_PACK(var16, var2);
         } else {
            GraphicsUtil.copyData_FALLBACK(var16, var2);
         }

         if (HaltingThread.hasBeenHalted(var9)) {
            return;
         }
      }

   }

   protected void drawBlockInPlace(TileBlock[] var1, WritableRaster var2) {
      Thread var3 = Thread.currentThread();
      int var4 = this.tileWidth;
      int var5 = this.tileHeight;

      for(int var6 = 0; var6 < var1.length; ++var6) {
         TileBlock var7 = var1[var6];
         int var8 = var7.getXLoc() * var4 + this.tileGridXOff;
         int var9 = var7.getYLoc() * var5 + this.tileGridYOff;
         Rectangle var10 = new Rectangle(var8, var9, var7.getWidth() * var4, var7.getHeight() * var5);
         var10 = var10.intersection(this.bounds);
         WritableRaster var11 = var2.createWritableChild(var10.x, var10.y, var10.width, var10.height, var10.x, var10.y, (int[])null);
         this.genRect(var11);
         if (HaltingThread.hasBeenHalted(var3)) {
            return;
         }
      }

   }
}
