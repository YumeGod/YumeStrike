package org.apache.batik.ext.awt.image.rendered;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.util.HaltingThread;

public class TileRed extends AbstractRed implements TileGenerator {
   static final AffineTransform IDENTITY = new AffineTransform();
   Rectangle tiledRegion;
   int xStep;
   int yStep;
   TileStore tiles;
   private RenderingHints hints;
   final boolean is_INT_PACK;
   RenderedImage tile;
   WritableRaster raster;

   public TileRed(RenderedImage var1, Rectangle var2) {
      this(var1, var2, var1.getWidth(), var1.getHeight(), (RenderingHints)null);
   }

   public TileRed(RenderedImage var1, Rectangle var2, RenderingHints var3) {
      this(var1, var2, var1.getWidth(), var1.getHeight(), var3);
   }

   public TileRed(RenderedImage var1, Rectangle var2, int var3, int var4) {
      this(var1, var2, var3, var4, (RenderingHints)null);
   }

   public TileRed(RenderedImage var1, Rectangle var2, int var3, int var4, RenderingHints var5) {
      this.tile = null;
      this.raster = null;
      if (var2 == null) {
         throw new IllegalArgumentException();
      } else if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.tiledRegion = var2;
         this.xStep = var3;
         this.yStep = var4;
         this.hints = var5;
         SampleModel var6 = fixSampleModel(var1, var3, var4, var2.width, var2.height);
         ColorModel var7 = var1.getColorModel();
         double var8 = (double)AbstractTiledRed.getDefaultTileSize();
         var8 *= var8;
         double var10 = (double)var3 * (double)var4;
         if (16.1 * var8 > var10) {
            int var12 = var3;
            int var13 = var4;
            if (4.0 * var10 <= var8) {
               int var14 = (int)Math.ceil(Math.sqrt(var8 / var10));
               var12 = var3 * var14;
               var13 = var4 * var14;
            }

            var6 = var6.createCompatibleSampleModel(var12, var13);
            this.raster = Raster.createWritableRaster(var6, new Point(var1.getMinX(), var1.getMinY()));
         }

         this.is_INT_PACK = GraphicsUtil.is_INT_PACK_Data(var6, false);
         this.init((CachableRed)null, var2, var7, var6, var1.getMinX(), var1.getMinY(), (Map)null);
         if (this.raster != null) {
            WritableRaster var15 = this.raster.createWritableChild(var1.getMinX(), var1.getMinY(), var3, var4, var1.getMinX(), var1.getMinY(), (int[])null);
            this.fillRasterFrom(var15, var1);
            this.fillOutRaster(this.raster);
         } else {
            this.tile = new TileCacheRed(GraphicsUtil.wrap(var1));
         }

      }
   }

   public WritableRaster copyData(WritableRaster var1) {
      int var2 = (int)Math.floor((double)(var1.getMinX() / this.xStep)) * this.xStep;
      int var3 = (int)Math.floor((double)(var1.getMinY() / this.yStep)) * this.yStep;
      int var4 = var1.getMinX() - var2;
      int var5 = var1.getMinY() - var3;
      int var6 = this.getXTile(var4);
      int var7 = this.getYTile(var5);
      int var8 = this.getXTile(var4 + var1.getWidth() - 1);
      int var9 = this.getYTile(var5 + var1.getHeight() - 1);

      for(int var10 = var7; var10 <= var9; ++var10) {
         for(int var11 = var6; var11 <= var8; ++var11) {
            Raster var12 = this.getTile(var11, var10);
            var12 = var12.createChild(var12.getMinX(), var12.getMinY(), var12.getWidth(), var12.getHeight(), var12.getMinX() + var2, var12.getMinY() + var3, (int[])null);
            if (this.is_INT_PACK) {
               GraphicsUtil.copyData_INT_PACK(var12, var1);
            } else {
               GraphicsUtil.copyData_FALLBACK(var12, var1);
            }
         }
      }

      return var1;
   }

   public Raster getTile(int var1, int var2) {
      if (this.raster != null) {
         int var3 = this.tileGridXOff + var1 * this.tileWidth;
         int var4 = this.tileGridYOff + var2 * this.tileHeight;
         return this.raster.createTranslatedChild(var3, var4);
      } else {
         return this.genTile(var1, var2);
      }
   }

   public Raster genTile(int var1, int var2) {
      int var3 = this.tileGridXOff + var1 * this.tileWidth;
      int var4 = this.tileGridYOff + var2 * this.tileHeight;
      if (this.raster != null) {
         return this.raster.createTranslatedChild(var3, var4);
      } else {
         Point var5 = new Point(var3, var4);
         WritableRaster var6 = Raster.createWritableRaster(this.sm, var5);
         this.fillRasterFrom(var6, this.tile);
         return var6;
      }
   }

   public WritableRaster fillRasterFrom(WritableRaster var1, RenderedImage var2) {
      ColorModel var3 = this.getColorModel();
      BufferedImage var4 = new BufferedImage(var3, var1.createWritableTranslatedChild(0, 0), var3.isAlphaPremultiplied(), (Hashtable)null);
      Graphics2D var5 = GraphicsUtil.createGraphics(var4, this.hints);
      int var6 = var1.getMinX();
      int var7 = var1.getMinY();
      int var8 = var1.getWidth();
      int var9 = var1.getHeight();
      var5.setComposite(AlphaComposite.Clear);
      var5.setColor(new Color(0, 0, 0, 0));
      var5.fillRect(0, 0, var8, var9);
      var5.setComposite(AlphaComposite.SrcOver);
      var5.translate(-var6, -var7);
      int var10 = var2.getMinX() + var2.getWidth() - 1;
      int var11 = var2.getMinY() + var2.getHeight() - 1;
      int var12 = (int)Math.ceil((double)((var6 - var10) / this.xStep)) * this.xStep;
      int var13 = (int)Math.ceil((double)((var7 - var11) / this.yStep)) * this.yStep;
      var5.translate(var12, var13);
      int var14 = var12 - var1.getMinX() + var2.getMinX();
      int var15 = var13 - var1.getMinY() + var2.getMinY();

      for(var6 = var14; var15 < var9; var14 = var6) {
         if (HaltingThread.hasBeenHalted()) {
            return var1;
         }

         while(var14 < var8) {
            GraphicsUtil.drawImage(var5, var2);
            var14 += this.xStep;
            var5.translate(this.xStep, 0);
         }

         var15 += this.yStep;
         var5.translate(var6 - var14, this.yStep);
      }

      return var1;
   }

   protected void fillOutRaster(WritableRaster var1) {
      if (this.is_INT_PACK) {
         this.fillOutRaster_INT_PACK(var1);
      } else {
         this.fillOutRaster_FALLBACK(var1);
      }

   }

   protected void fillOutRaster_INT_PACK(WritableRaster var1) {
      int var2 = var1.getMinX();
      int var3 = var1.getMinY();
      int var4 = var1.getWidth();
      int var5 = var1.getHeight();
      SinglePixelPackedSampleModel var6 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var7 = var6.getScanlineStride();
      DataBufferInt var8 = (DataBufferInt)var1.getDataBuffer();
      int[] var9 = var8.getBankData()[0];
      int var10 = var8.getOffset() + var6.getOffset(var2 - var1.getSampleModelTranslateX(), var3 - var1.getSampleModelTranslateY());
      int var11 = this.xStep;

      int var12;
      int var13;
      int var14;
      for(var12 = this.xStep; var12 < var4; var11 *= 2) {
         var13 = var11;
         if (var12 + var11 > var4) {
            var13 = var4 - var12;
         }

         int var15;
         int var16;
         if (var13 >= 128) {
            var14 = var10;
            var15 = var10 + var12;

            for(var16 = 0; var16 < this.yStep; ++var16) {
               System.arraycopy(var9, var14, var9, var15, var13);
               var14 += var7;
               var15 += var7;
            }
         } else {
            var14 = var10;
            var15 = var10 + var12;

            for(var16 = 0; var16 < this.yStep; ++var16) {
               int var17 = var14;
               var14 += var13 - 1;

               for(var15 += var13 - 1; var14 >= var17; var9[var15--] = var9[var14--]) {
               }

               var14 += var7 + 1;
               var15 += var7 + 1;
            }
         }

         var12 += var11;
      }

      var11 = this.yStep;

      for(var12 = this.yStep; var12 < var5; var11 *= 2) {
         var13 = var11;
         if (var12 + var11 > var5) {
            var13 = var5 - var12;
         }

         var14 = var10 + var12 * var7;
         System.arraycopy(var9, var10, var9, var14, var13 * var7);
         var12 += var11;
      }

   }

   protected void fillOutRaster_FALLBACK(WritableRaster var1) {
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      Object var4 = null;
      int var5 = this.xStep;

      int var6;
      int var7;
      for(var6 = this.xStep; var6 < var2; var5 *= 4) {
         var7 = var5;
         if (var6 + var5 > var2) {
            var7 = var2 - var6;
         }

         var4 = var1.getDataElements(0, 0, var7, this.yStep, var4);
         var1.setDataElements(var6, 0, var7, this.yStep, var4);
         var6 += var7;
         if (var6 >= var2) {
            break;
         }

         if (var6 + var7 > var2) {
            var7 = var2 - var6;
         }

         var1.setDataElements(var6, 0, var7, this.yStep, var4);
         var6 += var7;
         if (var6 >= var2) {
            break;
         }

         if (var6 + var7 > var2) {
            var7 = var2 - var6;
         }

         var1.setDataElements(var6, 0, var7, this.yStep, var4);
         var6 += var5;
      }

      var5 = this.yStep;

      for(var6 = this.yStep; var6 < var3; var5 *= 4) {
         var7 = var5;
         if (var6 + var5 > var3) {
            var7 = var3 - var6;
         }

         var4 = var1.getDataElements(0, 0, var2, var7, var4);
         var1.setDataElements(0, var6, var2, var7, var4);
         var6 += var7;
         if (var7 >= var3) {
            break;
         }

         if (var6 + var7 > var3) {
            var7 = var3 - var6;
         }

         var1.setDataElements(0, var6, var2, var7, var4);
         var6 += var7;
         if (var7 >= var3) {
            break;
         }

         if (var6 + var7 > var3) {
            var7 = var3 - var6;
         }

         var1.setDataElements(0, var6, var2, var7, var4);
         var6 += var7;
         var6 += var5;
      }

   }

   protected static SampleModel fixSampleModel(RenderedImage var0, int var1, int var2, int var3, int var4) {
      int var5 = AbstractTiledRed.getDefaultTileSize();
      SampleModel var6 = var0.getSampleModel();
      int var7 = var6.getWidth();
      if (var7 < var5) {
         var7 = var5;
      }

      if (var7 > var1) {
         var7 = var1;
      }

      int var8 = var6.getHeight();
      if (var8 < var5) {
         var8 = var5;
      }

      if (var8 > var2) {
         var8 = var2;
      }

      return var6.createCompatibleSampleModel(var7, var8);
   }
}
