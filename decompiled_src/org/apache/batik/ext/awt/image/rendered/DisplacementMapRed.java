package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.ARGBChannel;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;

public class DisplacementMapRed extends AbstractRed {
   private static final boolean TIME = false;
   private static final boolean USE_NN = false;
   private float scaleX;
   private float scaleY;
   private ARGBChannel xChannel;
   private ARGBChannel yChannel;
   CachableRed image;
   CachableRed offsets;
   int maxOffX;
   int maxOffY;
   RenderingHints hints;
   TileOffsets[] xOffsets;
   TileOffsets[] yOffsets;

   public DisplacementMapRed(CachableRed var1, CachableRed var2, ARGBChannel var3, ARGBChannel var4, float var5, float var6, RenderingHints var7) {
      if (var3 == null) {
         throw new IllegalArgumentException("Must provide xChannel");
      } else if (var4 == null) {
         throw new IllegalArgumentException("Must provide yChannel");
      } else {
         this.offsets = var2;
         this.scaleX = var5;
         this.scaleY = var6;
         this.xChannel = var3;
         this.yChannel = var4;
         this.hints = var7;
         this.maxOffX = (int)Math.ceil((double)(var5 / 2.0F));
         this.maxOffY = (int)Math.ceil((double)(var6 / 2.0F));
         Rectangle var8 = var1.getBounds();
         Rectangle var9 = var1.getBounds();
         var9.x -= this.maxOffX;
         var9.width += 2 * this.maxOffX;
         var9.y -= this.maxOffY;
         var9.height += 2 * this.maxOffY;
         PadRed var11 = new PadRed(var1, var9, PadMode.ZERO_PAD, (RenderingHints)null);
         TileCacheRed var12 = new TileCacheRed(var11);
         this.image = var12;
         ColorModel var10 = var12.getColorModel();
         var10 = GraphicsUtil.coerceColorModel(var10, true);
         this.init(var12, var8, var10, var12.getSampleModel(), var8.x, var8.y, (Map)null);
         this.xOffsets = new TileOffsets[this.getNumXTiles()];
         this.yOffsets = new TileOffsets[this.getNumYTiles()];
      }
   }

   public WritableRaster copyData(WritableRaster var1) {
      this.copyToRaster(var1);
      return var1;
   }

   public Raster getTile(int var1, int var2) {
      WritableRaster var3 = this.makeTile(var1, var2);
      Rectangle var4 = var3.getBounds();
      Raster var5 = this.offsets.getData(var4);
      ColorModel var6 = this.offsets.getColorModel();
      GraphicsUtil.coerceData((WritableRaster)var5, var6, false);
      TileOffsets var7 = this.getXOffsets(var1);
      TileOffsets var8 = this.getYOffsets(var2);
      if (this.image.getColorModel().isAlphaPremultiplied()) {
         this.filterBL(var5, var3, var7.tile, var7.off, var8.tile, var8.off);
      } else {
         this.filterBLPre(var5, var3, var7.tile, var7.off, var8.tile, var8.off);
      }

      return var3;
   }

   public TileOffsets getXOffsets(int var1) {
      TileOffsets var2 = this.xOffsets[var1 - this.getMinTileX()];
      if (var2 != null) {
         return var2;
      } else {
         SinglePixelPackedSampleModel var3 = (SinglePixelPackedSampleModel)this.getSampleModel();
         int var4 = var3.getOffset(0, 0);
         int var5 = var3.getWidth();
         int var6 = var5 + 2 * this.maxOffX;
         int var7 = this.getTileGridXOffset() + var1 * var5 - this.maxOffX - this.image.getTileGridXOffset();
         int var8 = var7 + var6 - 1;
         int var9 = (int)Math.floor((double)var7 / (double)var5);
         int var10 = (int)Math.floor((double)var8 / (double)var5);
         int var11 = var7 - var9 * var5;
         int var13 = (var10 + 1) * var5 - 1 - var8;
         var2 = new TileOffsets(var6, var4, 1, var11, var5, var13, var9, var10);
         this.xOffsets[var1 - this.getMinTileX()] = var2;
         return var2;
      }
   }

   public TileOffsets getYOffsets(int var1) {
      TileOffsets var2 = this.yOffsets[var1 - this.getMinTileY()];
      if (var2 != null) {
         return var2;
      } else {
         SinglePixelPackedSampleModel var3 = (SinglePixelPackedSampleModel)this.getSampleModel();
         int var4 = var3.getScanlineStride();
         int var5 = var3.getHeight();
         int var6 = var5 + 2 * this.maxOffY;
         int var7 = this.getTileGridYOffset() + var1 * var5 - this.maxOffY - this.image.getTileGridYOffset();
         int var8 = var7 + var6 - 1;
         int var9 = (int)Math.floor((double)var7 / (double)var5);
         int var10 = (int)Math.floor((double)var8 / (double)var5);
         int var11 = var7 - var9 * var5;
         int var13 = (var10 + 1) * var5 - 1 - var8;
         var2 = new TileOffsets(var6, 0, var4, var11, var5, var13, var9, var10);
         this.yOffsets[var1 - this.getMinTileY()] = var2;
         return var2;
      }
   }

   public void filterBL(Raster var1, WritableRaster var2, int[] var3, int[] var4, int[] var5, int[] var6) {
      int var7 = var2.getWidth();
      int var8 = var2.getHeight();
      int var9 = this.maxOffX;
      int var10 = this.maxOffY;
      int var11 = var9 + var7;
      int var12 = var10 + var8;
      DataBufferInt var13 = (DataBufferInt)var2.getDataBuffer();
      DataBufferInt var14 = (DataBufferInt)var1.getDataBuffer();
      SinglePixelPackedSampleModel var15 = (SinglePixelPackedSampleModel)var2.getSampleModel();
      int var17 = var13.getOffset() + var15.getOffset(var2.getMinX() - var2.getSampleModelTranslateX(), var2.getMinY() - var2.getSampleModelTranslateY());
      SinglePixelPackedSampleModel var16 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var18 = var14.getOffset() + var16.getOffset(var2.getMinX() - var1.getSampleModelTranslateX(), var2.getMinY() - var1.getSampleModelTranslateY());
      int var19 = var15.getScanlineStride();
      int var20 = var16.getScanlineStride();
      int var21 = var19 - var7;
      int var22 = var20 - var7;
      int[] var23 = var13.getBankData()[0];
      int[] var24 = var14.getBankData()[0];
      int var25 = this.xChannel.toInt() * 8;
      int var26 = this.yChannel.toInt() * 8;
      int var27 = var17;
      int var28 = var18;
      int var29 = (int)((double)this.scaleX / 255.0 * 32768.0 + 0.5);
      int var30 = (int)(-127.5 * (double)var29 - 0.5);
      int var31 = (int)((double)this.scaleY / 255.0 * 32768.0 + 0.5);
      int var32 = (int)(-127.5 * (double)var31 - 0.5);
      long var33 = System.currentTimeMillis();
      int var53 = var3[0] - 1;
      int var54 = var5[0] - 1;
      int[] var57 = null;

      for(int var47 = var10; var47 < var12; ++var47) {
         for(int var46 = var9; var46 < var11; ++var28) {
            int var52 = var24[var28];
            int var50 = var29 * (var52 >> var25 & 255) + var30;
            int var51 = var31 * (var52 >> var26 & 255) + var32;
            int var48 = var46 + (var50 >> 15);
            int var49 = var47 + (var51 >> 15);
            if (var53 != var3[var48] || var54 != var5[var49]) {
               var53 = var3[var48];
               var54 = var5[var49];
               var57 = ((DataBufferInt)this.image.getTile(var53, var54).getDataBuffer()).getBankData()[0];
            }

            int var35 = var57[var4[var48] + var6[var49]];
            int var55 = var3[var48 + 1];
            int var56 = var5[var49 + 1];
            int var36;
            int var37;
            int var38;
            if (var54 == var56) {
               if (var53 == var55) {
                  var37 = var57[var4[var48 + 1] + var6[var49]];
                  var36 = var57[var4[var48] + var6[var49 + 1]];
                  var38 = var57[var4[var48 + 1] + var6[var49 + 1]];
               } else {
                  var36 = var57[var4[var48] + var6[var49 + 1]];
                  var57 = ((DataBufferInt)this.image.getTile(var55, var54).getDataBuffer()).getBankData()[0];
                  var37 = var57[var4[var48 + 1] + var6[var49]];
                  var38 = var57[var4[var48 + 1] + var6[var49 + 1]];
                  var53 = var55;
               }
            } else if (var53 == var55) {
               var37 = var57[var4[var48 + 1] + var6[var49]];
               var57 = ((DataBufferInt)this.image.getTile(var53, var56).getDataBuffer()).getBankData()[0];
               var36 = var57[var4[var48] + var6[var49 + 1]];
               var38 = var57[var4[var48 + 1] + var6[var49 + 1]];
               var54 = var56;
            } else {
               var57 = ((DataBufferInt)this.image.getTile(var53, var56).getDataBuffer()).getBankData()[0];
               var36 = var57[var4[var48] + var6[var49 + 1]];
               var57 = ((DataBufferInt)this.image.getTile(var55, var56).getDataBuffer()).getBankData()[0];
               var38 = var57[var4[var48 + 1] + var6[var49 + 1]];
               var57 = ((DataBufferInt)this.image.getTile(var55, var54).getDataBuffer()).getBankData()[0];
               var37 = var57[var4[var48 + 1] + var6[var49]];
               var53 = var55;
            }

            int var39 = var50 & 32767;
            int var40 = var51 & 32767;
            int var42 = var35 >>> 16 & '\uff00';
            int var43 = var37 >>> 16 & '\uff00';
            int var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var42 = var36 >>> 16 & '\uff00';
            var43 = var38 >>> 16 & '\uff00';
            int var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            int var41 = ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) << 1;
            var42 = var35 >> 8 & '\uff00';
            var43 = var37 >> 8 & '\uff00';
            var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var42 = var36 >> 8 & '\uff00';
            var43 = var38 >> 8 & '\uff00';
            var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var41 |= ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) >>> 7;
            var42 = var35 & '\uff00';
            var43 = var37 & '\uff00';
            var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var42 = var36 & '\uff00';
            var43 = var38 & '\uff00';
            var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var41 |= ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) >>> 15;
            var42 = var35 << 8 & '\uff00';
            var43 = var37 << 8 & '\uff00';
            var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var42 = var36 << 8 & '\uff00';
            var43 = var38 << 8 & '\uff00';
            var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var41 |= ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) >>> 23;
            var23[var27] = var41;
            ++var46;
            ++var27;
         }

         var27 += var21;
         var28 += var22;
      }

   }

   public void filterBLPre(Raster var1, WritableRaster var2, int[] var3, int[] var4, int[] var5, int[] var6) {
      int var7 = var2.getWidth();
      int var8 = var2.getHeight();
      int var9 = this.maxOffX;
      int var10 = this.maxOffY;
      int var11 = var9 + var7;
      int var12 = var10 + var8;
      DataBufferInt var13 = (DataBufferInt)var2.getDataBuffer();
      DataBufferInt var14 = (DataBufferInt)var1.getDataBuffer();
      SinglePixelPackedSampleModel var15 = (SinglePixelPackedSampleModel)var2.getSampleModel();
      int var17 = var13.getOffset() + var15.getOffset(var2.getMinX() - var2.getSampleModelTranslateX(), var2.getMinY() - var2.getSampleModelTranslateY());
      SinglePixelPackedSampleModel var16 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var18 = var14.getOffset() + var16.getOffset(var2.getMinX() - var1.getSampleModelTranslateX(), var2.getMinY() - var1.getSampleModelTranslateY());
      int var19 = var15.getScanlineStride();
      int var20 = var16.getScanlineStride();
      int var21 = var19 - var7;
      int var22 = var20 - var7;
      int[] var23 = var13.getBankData()[0];
      int[] var24 = var14.getBankData()[0];
      int var25 = this.xChannel.toInt() * 8;
      int var26 = this.yChannel.toInt() * 8;
      int var27 = var17;
      int var28 = var18;
      int var29 = (int)((double)this.scaleX / 255.0 * 32768.0 + 0.5);
      int var30 = (int)(-127.5 * (double)var29 - 0.5);
      int var31 = (int)((double)this.scaleY / 255.0 * 32768.0 + 0.5);
      int var32 = (int)(-127.5 * (double)var31 - 0.5);
      long var33 = System.currentTimeMillis();
      int var58 = var3[0] - 1;
      int var59 = var5[0] - 1;
      int[] var62 = null;

      for(int var51 = var10; var51 < var12; ++var51) {
         for(int var50 = var9; var50 < var11; ++var28) {
            int var56 = var24[var28];
            int var54 = var29 * (var56 >> var25 & 255) + var30;
            int var55 = var31 * (var56 >> var26 & 255) + var32;
            int var52 = var50 + (var54 >> 15);
            int var53 = var51 + (var55 >> 15);
            if (var58 != var3[var52] || var59 != var5[var53]) {
               var58 = var3[var52];
               var59 = var5[var53];
               var62 = ((DataBufferInt)this.image.getTile(var58, var59).getDataBuffer()).getBankData()[0];
            }

            int var35 = var62[var4[var52] + var6[var53]];
            int var60 = var3[var52 + 1];
            int var61 = var5[var53 + 1];
            int var36;
            int var37;
            int var38;
            if (var59 == var61) {
               if (var58 == var60) {
                  var37 = var62[var4[var52 + 1] + var6[var53]];
                  var36 = var62[var4[var52] + var6[var53 + 1]];
                  var38 = var62[var4[var52 + 1] + var6[var53 + 1]];
               } else {
                  var36 = var62[var4[var52] + var6[var53 + 1]];
                  var62 = ((DataBufferInt)this.image.getTile(var60, var59).getDataBuffer()).getBankData()[0];
                  var37 = var62[var4[var52 + 1] + var6[var53]];
                  var38 = var62[var4[var52 + 1] + var6[var53 + 1]];
                  var58 = var60;
               }
            } else if (var58 == var60) {
               var37 = var62[var4[var52 + 1] + var6[var53]];
               var62 = ((DataBufferInt)this.image.getTile(var58, var61).getDataBuffer()).getBankData()[0];
               var36 = var62[var4[var52] + var6[var53 + 1]];
               var38 = var62[var4[var52 + 1] + var6[var53 + 1]];
               var59 = var61;
            } else {
               var62 = ((DataBufferInt)this.image.getTile(var58, var61).getDataBuffer()).getBankData()[0];
               var36 = var62[var4[var52] + var6[var53 + 1]];
               var62 = ((DataBufferInt)this.image.getTile(var60, var61).getDataBuffer()).getBankData()[0];
               var38 = var62[var4[var52 + 1] + var6[var53 + 1]];
               var62 = ((DataBufferInt)this.image.getTile(var60, var59).getDataBuffer()).getBankData()[0];
               var37 = var62[var4[var52 + 1] + var6[var53]];
               var58 = var60;
            }

            int var39 = var54 & 32767;
            int var40 = var55 & 32767;
            int var42 = var35 >>> 16 & '\uff00';
            int var43 = var37 >>> 16 & '\uff00';
            int var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            int var46 = (var42 >> 8) * 65793 + 128 >> 8;
            int var48 = (var43 >> 8) * 65793 + 128 >> 8;
            var42 = var36 >>> 16 & '\uff00';
            var43 = var38 >>> 16 & '\uff00';
            int var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            int var47 = (var42 >> 8) * 65793 + 128 >> 8;
            int var49 = (var43 >> 8) * 65793 + 128 >> 8;
            int var41 = ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) << 1;
            var42 = (var35 >> 16 & 255) * var46 + 128 >> 8;
            var43 = (var37 >> 16 & 255) * var48 + 128 >> 8;
            var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var42 = (var36 >> 16 & 255) * var47 + 128 >> 8;
            var43 = (var38 >> 16 & 255) * var49 + 128 >> 8;
            var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var41 |= ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) >>> 7;
            var42 = (var35 >> 8 & 255) * var46 + 128 >> 8;
            var43 = (var37 >> 8 & 255) * var48 + 128 >> 8;
            var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var42 = (var36 >> 8 & 255) * var47 + 128 >> 8;
            var43 = (var38 >> 8 & 255) * var49 + 128 >> 8;
            var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var41 |= ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) >>> 15;
            var42 = (var35 & 255) * var46 + 128 >> 8;
            var43 = (var37 & 255) * var48 + 128 >> 8;
            var44 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var42 = (var36 & 255) * var47 + 128 >> 8;
            var43 = (var38 & 255) * var49 + 128 >> 8;
            var45 = var42 + ((var43 - var42) * var39 + 16384 >> 15) & '\uffff';
            var41 |= ((var44 << 15) + (var45 - var44) * var40 + 4194304 & 2139095040) >>> 23;
            var23[var27] = var41;
            ++var50;
            ++var27;
         }

         var27 += var21;
         var28 += var22;
      }

   }

   public void filterNN(Raster var1, WritableRaster var2, int[] var3, int[] var4, int[] var5, int[] var6) {
      int var7 = var2.getWidth();
      int var8 = var2.getHeight();
      int var9 = this.maxOffX;
      int var10 = this.maxOffY;
      int var11 = var9 + var7;
      int var12 = var10 + var8;
      DataBufferInt var13 = (DataBufferInt)var2.getDataBuffer();
      DataBufferInt var14 = (DataBufferInt)var1.getDataBuffer();
      SinglePixelPackedSampleModel var15 = (SinglePixelPackedSampleModel)var2.getSampleModel();
      int var17 = var13.getOffset() + var15.getOffset(var2.getMinX() - var2.getSampleModelTranslateX(), var2.getMinY() - var2.getSampleModelTranslateY());
      SinglePixelPackedSampleModel var16 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var18 = var14.getOffset() + var16.getOffset(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY());
      int var19 = var15.getScanlineStride();
      int var20 = var16.getScanlineStride();
      int var21 = var19 - var7;
      int var22 = var20 - var7;
      int[] var23 = var13.getBankData()[0];
      int[] var24 = var14.getBankData()[0];
      int var25 = this.xChannel.toInt() * 8;
      int var26 = this.yChannel.toInt() * 8;
      int var27 = (int)((double)this.scaleX / 255.0 * 32768.0 + 0.5);
      int var28 = (int)((double)this.scaleY / 255.0 * 32768.0 + 0.5);
      int var29 = (int)(-127.5 * (double)var27 - 0.5) + 16384;
      int var30 = (int)(-127.5 * (double)var28 - 0.5) + 16384;
      int var31 = var17;
      int var32 = var18;
      long var33 = System.currentTimeMillis();
      int var35 = var10;
      int var36 = var3[0] - 1;
      int var37 = var5[0] - 1;

      for(int[] var38 = null; var35 < var12; ++var35) {
         for(int var44 = var9; var44 < var11; ++var44) {
            int var43 = var24[var32];
            int var41 = var27 * (var43 >> var25 & 255) + var29;
            int var42 = var28 * (var43 >> var26 & 255) + var30;
            int var39 = var44 + (var41 >> 15);
            int var40 = var35 + (var42 >> 15);
            if (var36 != var3[var39] || var37 != var5[var40]) {
               var36 = var3[var39];
               var37 = var5[var40];
               var38 = ((DataBufferInt)this.image.getTile(var36, var37).getDataBuffer()).getBankData()[0];
            }

            var23[var31] = var38[var4[var39] + var6[var40]];
            ++var31;
            ++var32;
         }

         var31 += var21;
         var32 += var22;
      }

   }

   static class TileOffsets {
      int[] tile;
      int[] off;

      TileOffsets(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
         this.tile = new int[var1 + 1];
         this.off = new int[var1 + 1];
         if (var7 == var8) {
            var5 -= var6;
         }

         for(int var9 = 0; var9 < var1; ++var9) {
            this.tile[var9] = var7;
            this.off[var9] = var2 + var4 * var3;
            ++var4;
            if (var4 == var5) {
               var4 = 0;
               ++var7;
               if (var7 == var8) {
                  var5 -= var6;
               }
            }
         }

         this.tile[var1] = this.tile[var1 - 1];
         this.off[var1] = this.off[var1 - 1];
      }
   }
}
