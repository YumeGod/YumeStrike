package org.apache.batik.ext.awt;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

final class RadialGradientPaintContext extends MultipleGradientPaintContext {
   private boolean isSimpleFocus = false;
   private boolean isNonCyclic = false;
   private float radius;
   private float centerX;
   private float centerY;
   private float focusX;
   private float focusY;
   private float radiusSq;
   private float constA;
   private float constB;
   private float trivial;
   private static final int FIXED_POINT_IMPL = 1;
   private static final int DEFAULT_IMPL = 2;
   private static final int ANTI_ALIAS_IMPL = 3;
   private int fillMethod;
   private static final float SCALEBACK = 0.999F;
   private float invSqStepFloat;
   private static final int MAX_PRECISION = 256;
   private int[] sqrtLutFixed = new int[256];

   public RadialGradientPaintContext(ColorModel var1, Rectangle var2, Rectangle2D var3, AffineTransform var4, RenderingHints var5, float var6, float var7, float var8, float var9, float var10, float[] var11, Color[] var12, MultipleGradientPaint.CycleMethodEnum var13, MultipleGradientPaint.ColorSpaceEnum var14) throws NoninvertibleTransformException {
      super(var1, var2, var3, var4, var5, var11, var12, var13, var14);
      this.centerX = var6;
      this.centerY = var7;
      this.focusX = var9;
      this.focusY = var10;
      this.radius = var8;
      this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
      this.isNonCyclic = var13 == RadialGradientPaint.NO_CYCLE;
      this.radiusSq = this.radius * this.radius;
      float var15 = this.focusX - this.centerX;
      float var16 = this.focusY - this.centerY;
      double var17 = Math.sqrt((double)(var15 * var15 + var16 * var16));
      if (var17 > (double)(this.radius * 0.999F)) {
         double var19 = Math.atan2((double)var16, (double)var15);
         this.focusX = (float)((double)(0.999F * this.radius) * Math.cos(var19)) + this.centerX;
         this.focusY = (float)((double)(0.999F * this.radius) * Math.sin(var19)) + this.centerY;
      }

      var15 = this.focusX - this.centerX;
      this.trivial = (float)Math.sqrt((double)(this.radiusSq - var15 * var15));
      this.constA = this.a02 - this.centerX;
      this.constB = this.a12 - this.centerY;
      Object var21 = var5.get(RenderingHints.KEY_COLOR_RENDERING);
      Object var20 = var5.get(RenderingHints.KEY_RENDERING);
      this.fillMethod = 0;
      if (var20 == RenderingHints.VALUE_RENDER_QUALITY || var21 == RenderingHints.VALUE_COLOR_RENDER_QUALITY) {
         this.fillMethod = 3;
      }

      if (var20 == RenderingHints.VALUE_RENDER_SPEED || var21 == RenderingHints.VALUE_COLOR_RENDER_SPEED) {
         this.fillMethod = 2;
      }

      if (this.fillMethod == 0) {
         this.fillMethod = 2;
      }

      if (this.fillMethod == 2 && this.isSimpleFocus && this.isNonCyclic && this.isSimpleLookup) {
         this.calculateFixedPointSqrtLookupTable();
         this.fillMethod = 1;
      }

   }

   protected void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      switch (this.fillMethod) {
         case 1:
            this.fixedPointSimplestCaseNonCyclicFillRaster(var1, var2, var3, var4, var5, var6, var7);
            break;
         case 2:
         default:
            this.cyclicCircularGradientFillRaster(var1, var2, var3, var4, var5, var6, var7);
            break;
         case 3:
            this.antiAliasFillRaster(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   private void fixedPointSimplestCaseNonCyclicFillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = 0.0F;
      float var9 = (float)this.fastGradientArraySize / this.radius;
      float var10 = this.a00 * (float)var4 + this.a01 * (float)var5 + this.constA;
      float var11 = this.a10 * (float)var4 + this.a11 * (float)var5 + this.constB;
      float var12 = var9 * this.a00;
      float var13 = var9 * this.a10;
      int var16 = this.fastGradientArraySize * this.fastGradientArraySize;
      int var25 = var2;
      float var20 = var12 * var12 + var13 * var13;
      float var19 = var20 * 2.0F;
      int var23;
      int var24;
      if (var20 > (float)var16) {
         int var26 = this.gradientOverflow;

         for(var24 = 0; var24 < var7; ++var24) {
            for(var23 = var25 + var6; var25 < var23; ++var25) {
               var1[var25] = var26;
            }

            var25 += var3;
         }

      } else {
         for(var24 = 0; var24 < var7; ++var24) {
            float var14 = var9 * (this.a01 * (float)var24 + var10);
            float var15 = var9 * (this.a11 * (float)var24 + var11);
            float var17 = var15 * var15 + var14 * var14;
            float var18 = (var13 * var15 + var12 * var14) * 2.0F + var20;

            for(var23 = var25 + var6; var25 < var23; ++var25) {
               if (var17 >= (float)var16) {
                  var1[var25] = this.gradientOverflow;
               } else {
                  var8 = var17 * this.invSqStepFloat;
                  int var22 = (int)var8;
                  var8 -= (float)var22;
                  int var21 = this.sqrtLutFixed[var22];
                  var21 += (int)(var8 * (float)(this.sqrtLutFixed[var22 + 1] - var21));
                  var1[var25] = this.gradient[var21];
               }

               var17 += var18;
               var18 += var19;
            }

            var25 += var3;
         }

      }
   }

   private void calculateFixedPointSqrtLookupTable() {
      float var1 = (float)(this.fastGradientArraySize * this.fastGradientArraySize) / 254.0F;
      int[] var2 = this.sqrtLutFixed;

      int var3;
      for(var3 = 0; var3 < 255; ++var3) {
         var2[var3] = (int)Math.sqrt((double)((float)var3 * var1));
      }

      var2[var3] = var2[var3 - 1];
      this.invSqStepFloat = 1.0F / var1;
   }

   private void cyclicCircularGradientFillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      double var8 = (double)(-this.radiusSq + this.centerX * this.centerX + this.centerY * this.centerY);
      float var24 = this.a00 * (float)var4 + this.a01 * (float)var5 + this.a02;
      float var25 = this.a10 * (float)var4 + this.a11 * (float)var5 + this.a12;
      float var26 = 2.0F * this.centerY;
      float var27 = -2.0F * this.centerX;
      int var36 = var2;
      int var39 = var6 + var3;

      for(int var38 = 0; var38 < var7; ++var38) {
         float var28 = this.a01 * (float)var38 + var24;
         float var29 = this.a11 * (float)var38 + var25;

         for(int var37 = 0; var37 < var6; ++var37) {
            double var20;
            double var22;
            if (var28 - this.focusX > -1.0E-6F && var28 - this.focusX < 1.0E-6F) {
               var20 = (double)this.focusX;
               var22 = (double)this.centerY;
               var22 += var29 > this.focusY ? (double)this.trivial : (double)(-this.trivial);
            } else {
               double var16 = (double)((var29 - this.focusY) / (var28 - this.focusX));
               double var18 = (double)var29 - var16 * (double)var28;
               double var10 = var16 * var16 + 1.0;
               double var12 = (double)var27 + -2.0 * var16 * ((double)this.centerY - var18);
               double var14 = var8 + var18 * (var18 - (double)var26);
               float var31 = (float)Math.sqrt(var12 * var12 - 4.0 * var10 * var14);
               var20 = -var12;
               var20 += var28 < this.focusX ? (double)(-var31) : (double)var31;
               var20 /= 2.0 * var10;
               var22 = var16 * var20 + var18;
            }

            float var34 = (float)var20 - this.focusX;
            var34 *= var34;
            float var35 = (float)var22 - this.focusY;
            var35 *= var35;
            float var33 = var34 + var35;
            var34 = var28 - this.focusX;
            var34 *= var34;
            var35 = var29 - this.focusY;
            var35 *= var35;
            float var32 = var34 + var35;
            float var30 = (float)Math.sqrt((double)(var32 / var33));
            var1[var36 + var37] = this.indexIntoGradientsArrays(var30);
            var28 += this.a00;
            var29 += this.a10;
         }

         var36 += var39;
      }

   }

   private void antiAliasFillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      double var8 = (double)(-this.radiusSq + this.centerX * this.centerX + this.centerY * this.centerY);
      float var10 = 2.0F * this.centerY;
      float var11 = -2.0F * this.centerX;
      float var12 = this.a00 * ((float)var4 - 0.5F) + this.a01 * ((float)var5 + 0.5F) + this.a02;
      float var13 = this.a10 * ((float)var4 - 0.5F) + this.a11 * ((float)var5 + 0.5F) + this.a12;
      int var18 = var2 - 1;
      double[] var19 = new double[var6 + 1];
      float var14 = var12 - this.a01;
      float var15 = var13 - this.a11;

      int var16;
      double var20;
      double var22;
      double var24;
      double var26;
      double var28;
      double var30;
      double var32;
      double var34;
      double var36;
      double var38;
      double var40;
      double var42;
      float var52;
      for(var16 = 0; var16 <= var6; ++var16) {
         var52 = var14 - this.focusX;
         if (var52 > -1.0E-6F && var52 < 1.0E-6F) {
            var24 = (double)this.focusX;
            var26 = (double)this.centerY;
            var26 += var15 > this.focusY ? (double)this.trivial : (double)(-this.trivial);
         } else {
            var28 = (double)((var15 - this.focusY) / (var14 - this.focusX));
            var30 = (double)var15 - var28 * (double)var14;
            var32 = var28 * var28 + 1.0;
            var34 = (double)var11 + -2.0 * var28 * ((double)this.centerY - var30);
            var36 = var8 + var30 * (var30 - (double)var10);
            var38 = Math.sqrt(var34 * var34 - 4.0 * var32 * var36);
            var24 = -var34;
            var24 += var14 < this.focusX ? -var38 : var38;
            var24 /= 2.0 * var32;
            var26 = var28 * var24 + var30;
         }

         var20 = var24 - (double)this.focusX;
         var20 *= var20;
         var22 = var26 - (double)this.focusY;
         var22 *= var22;
         var40 = var20 + var22;
         var20 = (double)(var14 - this.focusX);
         var20 *= var20;
         var22 = (double)(var15 - this.focusY);
         var22 *= var22;
         var42 = var20 + var22;
         var19[var16] = Math.sqrt(var42 / var40);
         var14 += this.a00;
         var15 += this.a10;
      }

      for(int var17 = 0; var17 < var7; ++var17) {
         var14 = this.a01 * (float)var17 + var12;
         var15 = this.a11 * (float)var17 + var13;
         double var48 = var19[0];
         var52 = var14 - this.focusX;
         if (var52 > -1.0E-6F && var52 < 1.0E-6F) {
            var24 = (double)this.focusX;
            var26 = (double)this.centerY;
            var26 += var15 > this.focusY ? (double)this.trivial : (double)(-this.trivial);
         } else {
            var28 = (double)((var15 - this.focusY) / (var14 - this.focusX));
            var30 = (double)var15 - var28 * (double)var14;
            var32 = var28 * var28 + 1.0;
            var34 = (double)var11 + -2.0 * var28 * ((double)this.centerY - var30);
            var36 = var8 + var30 * (var30 - (double)var10);
            var38 = Math.sqrt(var34 * var34 - 4.0 * var32 * var36);
            var24 = -var34;
            var24 += var14 < this.focusX ? -var38 : var38;
            var24 /= 2.0 * var32;
            var26 = var28 * var24 + var30;
         }

         var20 = var24 - (double)this.focusX;
         var20 *= var20;
         var22 = var26 - (double)this.focusY;
         var22 *= var22;
         var40 = var20 + var22;
         var20 = (double)(var14 - this.focusX);
         var20 *= var20;
         var22 = (double)(var15 - this.focusY);
         var22 *= var22;
         var42 = var20 + var22;
         double var50 = Math.sqrt(var42 / var40);
         var19[0] = var50;
         var14 += this.a00;
         var15 += this.a10;

         for(var16 = 1; var16 <= var6; ++var16) {
            double var44 = var48;
            double var46 = var50;
            var48 = var19[var16];
            var52 = var14 - this.focusX;
            if (var52 > -1.0E-6F && var52 < 1.0E-6F) {
               var24 = (double)this.focusX;
               var26 = (double)this.centerY;
               var26 += var15 > this.focusY ? (double)this.trivial : (double)(-this.trivial);
            } else {
               var28 = (double)((var15 - this.focusY) / (var14 - this.focusX));
               var30 = (double)var15 - var28 * (double)var14;
               var32 = var28 * var28 + 1.0;
               var34 = (double)var11 + -2.0 * var28 * ((double)this.centerY - var30);
               var36 = var8 + var30 * (var30 - (double)var10);
               var38 = Math.sqrt(var34 * var34 - 4.0 * var32 * var36);
               var24 = -var34;
               var24 += var14 < this.focusX ? -var38 : var38;
               var24 /= 2.0 * var32;
               var26 = var28 * var24 + var30;
            }

            var20 = var24 - (double)this.focusX;
            var20 *= var20;
            var22 = var26 - (double)this.focusY;
            var22 *= var22;
            var40 = var20 + var22;
            var20 = (double)(var14 - this.focusX);
            var20 *= var20;
            var22 = (double)(var15 - this.focusY);
            var22 *= var22;
            var42 = var20 + var22;
            var50 = Math.sqrt(var42 / var40);
            var19[var16] = var50;
            var1[var18 + var16] = this.indexGradientAntiAlias((float)((var44 + var46 + var48 + var50) / 4.0), (float)Math.max(Math.abs(var50 - var44), Math.abs(var48 - var46)));
            var14 += this.a00;
            var15 += this.a10;
         }

         var18 += var6 + var3;
      }

   }
}
