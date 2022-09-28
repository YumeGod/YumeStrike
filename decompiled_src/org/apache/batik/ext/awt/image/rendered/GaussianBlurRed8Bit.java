package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class GaussianBlurRed8Bit extends AbstractRed {
   int xinset;
   int yinset;
   double stdDevX;
   double stdDevY;
   RenderingHints hints;
   ConvolveOp[] convOp;
   int dX;
   int dY;
   static final float SQRT2PI = (float)Math.sqrt(6.283185307179586);
   static final float DSQRT2PI;
   static final float precision = 0.499F;

   public GaussianBlurRed8Bit(CachableRed var1, double var2, RenderingHints var4) {
      this(var1, var2, var2, var4);
   }

   public GaussianBlurRed8Bit(CachableRed var1, double var2, double var4, RenderingHints var6) {
      this.convOp = new ConvolveOp[2];
      this.stdDevX = var2;
      this.stdDevY = var4;
      this.hints = var6;
      this.xinset = surroundPixels(var2, var6);
      this.yinset = surroundPixels(var4, var6);
      Rectangle var7 = var1.getBounds();
      var7.x += this.xinset;
      var7.y += this.yinset;
      var7.width -= 2 * this.xinset;
      var7.height -= 2 * this.yinset;
      if (var7.width <= 0 || var7.height <= 0) {
         var7.width = 0;
         var7.height = 0;
      }

      ColorModel var8 = fixColorModel(var1);
      SampleModel var9 = var1.getSampleModel();
      int var10 = var9.getWidth();
      int var11 = var9.getHeight();
      if (var10 > var7.width) {
         var10 = var7.width;
      }

      if (var11 > var7.height) {
         var11 = var7.height;
      }

      var9 = var8.createCompatibleSampleModel(var10, var11);
      this.init(var1, var7, var8, var9, var1.getTileGridXOffset() + this.xinset, var1.getTileGridYOffset() + this.yinset, (Map)null);
      boolean var12 = this.hints != null && RenderingHints.VALUE_RENDER_QUALITY.equals(this.hints.get(RenderingHints.KEY_RENDERING));
      if (this.xinset != 0 && (var2 < 2.0 || var12)) {
         this.convOp[0] = new ConvolveOp(this.makeQualityKernelX(this.xinset * 2 + 1));
      } else {
         this.dX = (int)Math.floor((double)DSQRT2PI * var2 + 0.5);
      }

      if (this.yinset == 0 || !(var4 < 2.0) && !var12) {
         this.dY = (int)Math.floor((double)DSQRT2PI * var4 + 0.5);
      } else {
         this.convOp[1] = new ConvolveOp(this.makeQualityKernelY(this.yinset * 2 + 1));
      }

   }

   public static int surroundPixels(double var0) {
      return surroundPixels(var0, (RenderingHints)null);
   }

   public static int surroundPixels(double var0, RenderingHints var2) {
      boolean var3 = var2 != null && RenderingHints.VALUE_RENDER_QUALITY.equals(var2.get(RenderingHints.KEY_RENDERING));
      if (!(var0 < 2.0) && !var3) {
         int var6 = (int)Math.floor((double)DSQRT2PI * var0 + 0.5);
         return var6 % 2 == 0 ? var6 - 1 + var6 / 2 : var6 - 2 + var6 / 2;
      } else {
         float var4 = (float)(0.5 / (var0 * (double)SQRT2PI));

         int var5;
         for(var5 = 0; var4 < 0.499F; ++var5) {
            var4 += (float)(Math.pow(Math.E, (double)(-var5 * var5) / (2.0 * var0 * var0)) / (var0 * (double)SQRT2PI));
         }

         return var5;
      }
   }

   private float[] computeQualityKernelData(int var1, double var2) {
      float[] var4 = new float[var1];
      int var5 = var1 / 2;
      float var6 = 0.0F;

      int var7;
      for(var7 = 0; var7 < var1; ++var7) {
         var4[var7] = (float)(Math.pow(Math.E, (double)(-(var7 - var5) * (var7 - var5)) / (2.0 * var2 * var2)) / ((double)SQRT2PI * var2));
         var6 += var4[var7];
      }

      for(var7 = 0; var7 < var1; ++var7) {
         var4[var7] /= var6;
      }

      return var4;
   }

   private Kernel makeQualityKernelX(int var1) {
      return new Kernel(var1, 1, this.computeQualityKernelData(var1, this.stdDevX));
   }

   private Kernel makeQualityKernelY(int var1) {
      return new Kernel(1, var1, this.computeQualityKernelData(var1, this.stdDevY));
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      Rectangle var3 = var1.getBounds();
      var3.x -= this.xinset;
      var3.y -= this.yinset;
      var3.width += 2 * this.xinset;
      var3.height += 2 * this.yinset;
      ColorModel var4 = var2.getColorModel();
      WritableRaster var5 = null;
      WritableRaster var6 = null;
      var5 = var4.createCompatibleWritableRaster(var3.width, var3.height);
      WritableRaster var7 = var5.createWritableTranslatedChild(var3.x, var3.y);
      var2.copyData(var7);
      if (var4.hasAlpha() && !var4.isAlphaPremultiplied()) {
         GraphicsUtil.coerceData(var5, var4, true);
      }

      int var9;
      if (this.xinset == 0) {
         var9 = 0;
      } else if (this.convOp[0] != null) {
         var6 = this.getColorModel().createCompatibleWritableRaster(var3.width, var3.height);
         var6 = this.convOp[0].filter(var5, var6);
         var9 = this.convOp[0].getKernel().getXOrigin();
         WritableRaster var8 = var5;
         var5 = var6;
         var6 = var8;
      } else if ((this.dX & 1) == 0) {
         var5 = this.boxFilterH(var5, var5, 0, 0, this.dX, this.dX / 2);
         var5 = this.boxFilterH(var5, var5, this.dX / 2, 0, this.dX, this.dX / 2 - 1);
         var5 = this.boxFilterH(var5, var5, this.dX - 1, 0, this.dX + 1, this.dX / 2);
         var9 = this.dX - 1 + this.dX / 2;
      } else {
         var5 = this.boxFilterH(var5, var5, 0, 0, this.dX, this.dX / 2);
         var5 = this.boxFilterH(var5, var5, this.dX / 2, 0, this.dX, this.dX / 2);
         var5 = this.boxFilterH(var5, var5, this.dX - 2, 0, this.dX, this.dX / 2);
         var9 = this.dX - 2 + this.dX / 2;
      }

      if (this.yinset == 0) {
         var6 = var5;
      } else if (this.convOp[1] != null) {
         if (var6 == null) {
            var6 = this.getColorModel().createCompatibleWritableRaster(var3.width, var3.height);
         }

         var6 = this.convOp[1].filter(var5, var6);
      } else {
         if ((this.dY & 1) == 0) {
            var5 = this.boxFilterV(var5, var5, var9, 0, this.dY, this.dY / 2);
            var5 = this.boxFilterV(var5, var5, var9, this.dY / 2, this.dY, this.dY / 2 - 1);
            var5 = this.boxFilterV(var5, var5, var9, this.dY - 1, this.dY + 1, this.dY / 2);
         } else {
            var5 = this.boxFilterV(var5, var5, var9, 0, this.dY, this.dY / 2);
            var5 = this.boxFilterV(var5, var5, var9, this.dY / 2, this.dY, this.dY / 2);
            var5 = this.boxFilterV(var5, var5, var9, this.dY - 2, this.dY, this.dY / 2);
         }

         var6 = var5;
      }

      var6 = var6.createWritableTranslatedChild(var3.x, var3.y);
      GraphicsUtil.copyData((Raster)var6, (WritableRaster)var1);
      return var1;
   }

   private WritableRaster boxFilterH(Raster var1, WritableRaster var2, int var3, int var4, int var5, int var6) {
      int var7 = var1.getWidth();
      int var8 = var1.getHeight();
      if (var7 < 2 * var3 + var5) {
         return var2;
      } else if (var8 < 2 * var4) {
         return var2;
      } else {
         SinglePixelPackedSampleModel var9 = (SinglePixelPackedSampleModel)var1.getSampleModel();
         SinglePixelPackedSampleModel var10 = (SinglePixelPackedSampleModel)var2.getSampleModel();
         int var11 = var9.getScanlineStride();
         int var12 = var10.getScanlineStride();
         DataBufferInt var13 = (DataBufferInt)var1.getDataBuffer();
         DataBufferInt var14 = (DataBufferInt)var2.getDataBuffer();
         int var15 = var13.getOffset() + var9.getOffset(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY());
         int var16 = var14.getOffset() + var10.getOffset(var2.getMinX() - var2.getSampleModelTranslateX(), var2.getMinY() - var2.getSampleModelTranslateY());
         int[] var17 = var13.getBankData()[0];
         int[] var18 = var14.getBankData()[0];
         int[] var19 = new int[var5];
         int var22 = 16777216 / var5;

         for(int var23 = var4; var23 < var8 - var4; ++var23) {
            int var24 = var15 + var23 * var11;
            int var25 = var16 + var23 * var12;
            int var26 = var24 + (var7 - var3);
            int var27 = 0;
            int var28 = 0;
            int var29 = 0;
            int var30 = 0;
            int var31 = 0;
            var24 += var3;

            int var20;
            for(int var32 = var24 + var5; var24 < var32; ++var24) {
               var20 = var19[var27] = var17[var24];
               var28 += var20 >>> 24;
               var29 += var20 >> 16 & 255;
               var30 += var20 >> 8 & 255;
               var31 += var20 & 255;
               ++var27;
            }

            var25 += var3 + var6;
            int var21 = var18[var25] = var28 * var22 & -16777216 | (var29 * var22 & -16777216) >>> 8 | (var30 * var22 & -16777216) >>> 16 | (var31 * var22 & -16777216) >>> 24;
            ++var25;

            for(var27 = 0; var24 < var26; ++var25) {
               var20 = var19[var27];
               if (var20 == var17[var24]) {
                  var18[var25] = var21;
               } else {
                  var28 -= var20 >>> 24;
                  var29 -= var20 >> 16 & 255;
                  var30 -= var20 >> 8 & 255;
                  var31 -= var20 & 255;
                  var20 = var19[var27] = var17[var24];
                  var28 += var20 >>> 24;
                  var29 += var20 >> 16 & 255;
                  var30 += var20 >> 8 & 255;
                  var31 += var20 & 255;
                  var21 = var18[var25] = var28 * var22 & -16777216 | (var29 * var22 & -16777216) >>> 8 | (var30 * var22 & -16777216) >>> 16 | (var31 * var22 & -16777216) >>> 24;
               }

               var27 = (var27 + 1) % var5;
               ++var24;
            }
         }

         return var2;
      }
   }

   private WritableRaster boxFilterV(Raster var1, WritableRaster var2, int var3, int var4, int var5, int var6) {
      int var7 = var1.getWidth();
      int var8 = var1.getHeight();
      if (var7 < 2 * var3) {
         return var2;
      } else if (var8 < 2 * var4 + var5) {
         return var2;
      } else {
         SinglePixelPackedSampleModel var9 = (SinglePixelPackedSampleModel)var1.getSampleModel();
         SinglePixelPackedSampleModel var10 = (SinglePixelPackedSampleModel)var2.getSampleModel();
         int var11 = var9.getScanlineStride();
         int var12 = var10.getScanlineStride();
         DataBufferInt var13 = (DataBufferInt)var1.getDataBuffer();
         DataBufferInt var14 = (DataBufferInt)var2.getDataBuffer();
         int var15 = var13.getOffset() + var9.getOffset(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY());
         int var16 = var14.getOffset() + var10.getOffset(var2.getMinX() - var2.getSampleModelTranslateX(), var2.getMinY() - var2.getSampleModelTranslateY());
         int[] var17 = var13.getBankData()[0];
         int[] var18 = var14.getBankData()[0];
         int[] var19 = new int[var5];
         int var22 = 16777216 / var5;

         for(int var23 = var3; var23 < var7 - var3; ++var23) {
            int var24 = var15 + var23;
            int var25 = var16 + var23;
            int var26 = var24 + (var8 - var4) * var11;
            int var27 = 0;
            int var28 = 0;
            int var29 = 0;
            int var30 = 0;
            int var31 = 0;
            var24 += var4 * var11;

            int var20;
            for(int var32 = var24 + var5 * var11; var24 < var32; var24 += var11) {
               var20 = var19[var27] = var17[var24];
               var28 += var20 >>> 24;
               var29 += var20 >> 16 & 255;
               var30 += var20 >> 8 & 255;
               var31 += var20 & 255;
               ++var27;
            }

            var25 += (var4 + var6) * var12;
            int var21 = var18[var25] = var28 * var22 & -16777216 | (var29 * var22 & -16777216) >>> 8 | (var30 * var22 & -16777216) >>> 16 | (var31 * var22 & -16777216) >>> 24;
            var25 += var12;

            for(var27 = 0; var24 < var26; var25 += var12) {
               var20 = var19[var27];
               if (var20 == var17[var24]) {
                  var18[var25] = var21;
               } else {
                  var28 -= var20 >>> 24;
                  var29 -= var20 >> 16 & 255;
                  var30 -= var20 >> 8 & 255;
                  var31 -= var20 & 255;
                  var20 = var19[var27] = var17[var24];
                  var28 += var20 >>> 24;
                  var29 += var20 >> 16 & 255;
                  var30 += var20 >> 8 & 255;
                  var31 += var20 & 255;
                  var21 = var18[var25] = var28 * var22 & -16777216 | (var29 * var22 & -16777216) >>> 8 | (var30 * var22 & -16777216) >>> 16 | (var31 * var22 & -16777216) >>> 24;
               }

               var27 = (var27 + 1) % var5;
               var24 += var11;
            }
         }

         return var2;
      }
   }

   protected static ColorModel fixColorModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      int var2 = var0.getSampleModel().getNumBands();
      int[] var3 = new int[4];
      switch (var2) {
         case 1:
            var3[0] = 255;
            break;
         case 2:
            var3[0] = 255;
            var3[3] = 65280;
            break;
         case 3:
            var3[0] = 16711680;
            var3[1] = 65280;
            var3[2] = 255;
            break;
         case 4:
            var3[0] = 16711680;
            var3[1] = 65280;
            var3[2] = 255;
            var3[3] = -16777216;
            break;
         default:
            throw new IllegalArgumentException("GaussianBlurRed8Bit only supports one to four band images");
      }

      ColorSpace var4 = var1.getColorSpace();
      return new DirectColorModel(var4, 8 * var2, var3[0], var3[1], var3[2], var3[3], true, 3);
   }

   static {
      DSQRT2PI = SQRT2PI * 3.0F / 4.0F;
   }
}
