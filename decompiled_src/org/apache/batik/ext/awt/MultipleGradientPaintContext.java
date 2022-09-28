package org.apache.batik.ext.awt;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;
import org.apache.batik.ext.awt.image.GraphicsUtil;

abstract class MultipleGradientPaintContext implements PaintContext {
   protected static final boolean DEBUG = false;
   protected ColorModel dataModel;
   protected ColorModel model;
   private static ColorModel lrgbmodel_NA = new DirectColorModel(ColorSpace.getInstance(1004), 24, 16711680, 65280, 255, 0, false, 3);
   private static ColorModel srgbmodel_NA = new DirectColorModel(ColorSpace.getInstance(1000), 24, 16711680, 65280, 255, 0, false, 3);
   private static ColorModel lrgbmodel_A = new DirectColorModel(ColorSpace.getInstance(1004), 32, 16711680, 65280, 255, -16777216, false, 3);
   private static ColorModel srgbmodel_A = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, false, 3);
   protected static ColorModel cachedModel;
   protected static WeakReference cached;
   protected WritableRaster saved;
   protected MultipleGradientPaint.CycleMethodEnum cycleMethod;
   protected MultipleGradientPaint.ColorSpaceEnum colorSpace;
   protected float a00;
   protected float a01;
   protected float a10;
   protected float a11;
   protected float a02;
   protected float a12;
   protected boolean isSimpleLookup = true;
   protected boolean hasDiscontinuity = false;
   protected int fastGradientArraySize;
   protected int[] gradient;
   protected int[][] gradients;
   protected int gradientAverage;
   protected int gradientUnderflow;
   protected int gradientOverflow;
   protected int gradientsLength;
   protected float[] normalizedIntervals;
   protected float[] fractions;
   private int transparencyTest;
   private static final int[] SRGBtoLinearRGB = new int[256];
   private static final int[] LinearRGBtoSRGB = new int[256];
   protected static final int GRADIENT_SIZE = 256;
   protected static final int GRADIENT_SIZE_INDEX = 255;
   private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;

   protected MultipleGradientPaintContext(ColorModel var1, Rectangle var2, Rectangle2D var3, AffineTransform var4, RenderingHints var5, float[] var6, Color[] var7, MultipleGradientPaint.CycleMethodEnum var8, MultipleGradientPaint.ColorSpaceEnum var9) throws NoninvertibleTransformException {
      boolean var10 = false;
      boolean var11 = false;
      int var12 = var6.length;
      if (var6[0] != 0.0F) {
         var10 = true;
         ++var12;
      }

      if (var6[var6.length - 1] != 1.0F) {
         var11 = true;
         ++var12;
      }

      for(int var13 = 0; var13 < var6.length - 1; ++var13) {
         if (var6[var13] == var6[var13 + 1]) {
            --var12;
         }
      }

      this.fractions = new float[var12];
      Color[] var18 = new Color[var12 - 1];
      Color[] var14 = new Color[var12 - 1];
      this.normalizedIntervals = new float[var12 - 1];
      this.gradientUnderflow = var7[0].getRGB();
      this.gradientOverflow = var7[var7.length - 1].getRGB();
      int var15 = 0;
      if (var10) {
         this.fractions[0] = 0.0F;
         var18[0] = var7[0];
         var14[0] = var7[0];
         this.normalizedIntervals[0] = var6[0];
         ++var15;
      }

      for(int var16 = 0; var16 < var6.length - 1; ++var16) {
         if (var6[var16] == var6[var16 + 1]) {
            if (!var7[var16].equals(var7[var16 + 1])) {
               this.hasDiscontinuity = true;
            }
         } else {
            this.fractions[var15] = var6[var16];
            var18[var15] = var7[var16];
            var14[var15] = var7[var16 + 1];
            this.normalizedIntervals[var15] = var6[var16 + 1] - var6[var16];
            ++var15;
         }
      }

      this.fractions[var15] = var6[var6.length - 1];
      if (var11) {
         var18[var15] = var14[var15] = var7[var7.length - 1];
         this.normalizedIntervals[var15] = 1.0F - var6[var6.length - 1];
         ++var15;
         this.fractions[var15] = 1.0F;
      }

      AffineTransform var19 = var4.createInverse();
      double[] var17 = new double[6];
      var19.getMatrix(var17);
      this.a00 = (float)var17[0];
      this.a10 = (float)var17[1];
      this.a01 = (float)var17[2];
      this.a11 = (float)var17[3];
      this.a02 = (float)var17[4];
      this.a12 = (float)var17[5];
      this.cycleMethod = var8;
      this.colorSpace = var9;
      if (var1.getColorSpace() == lrgbmodel_A.getColorSpace()) {
         this.dataModel = lrgbmodel_A;
      } else {
         if (var1.getColorSpace() != srgbmodel_A.getColorSpace()) {
            throw new IllegalArgumentException("Unsupported ColorSpace for interpolation");
         }

         this.dataModel = srgbmodel_A;
      }

      this.calculateGradientFractions(var18, var14);
      this.model = GraphicsUtil.coerceColorModel(this.dataModel, var1.isAlphaPremultiplied());
   }

   protected final void calculateGradientFractions(Color[] var1, Color[] var2) {
      if (this.colorSpace == LinearGradientPaint.LINEAR_RGB) {
         int[] var3 = SRGBtoLinearRGB;

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var1[var4] = interpolateColor(var3, var1[var4]);
            var2[var4] = interpolateColor(var3, var2[var4]);
         }
      }

      this.transparencyTest = -16777216;
      if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
         this.transparencyTest &= this.gradientUnderflow;
         this.transparencyTest &= this.gradientOverflow;
      }

      this.gradients = new int[this.fractions.length - 1][];
      this.gradientsLength = this.gradients.length;
      int var8 = this.normalizedIntervals.length;
      float var9 = 1.0F;
      float[] var5 = this.normalizedIntervals;

      int var6;
      for(var6 = 0; var6 < var8; ++var6) {
         var9 = var9 > var5[var6] ? var5[var6] : var9;
      }

      var6 = 0;
      if (var9 == 0.0F) {
         var6 = Integer.MAX_VALUE;
         this.hasDiscontinuity = true;
      } else {
         for(int var7 = 0; var7 < var5.length; ++var7) {
            var6 = (int)((float)var6 + var5[var7] / var9 * 256.0F);
         }
      }

      if (var6 > 5000) {
         this.calculateMultipleArrayGradient(var1, var2);
         if (this.cycleMethod == MultipleGradientPaint.REPEAT && this.gradients[0][0] != this.gradients[this.gradients.length - 1][255]) {
            this.hasDiscontinuity = true;
         }
      } else {
         this.calculateSingleArrayGradient(var1, var2, var9);
         if (this.cycleMethod == MultipleGradientPaint.REPEAT && this.gradient[0] != this.gradient[this.fastGradientArraySize]) {
            this.hasDiscontinuity = true;
         }
      }

      if (this.transparencyTest >>> 24 == 255) {
         if (this.dataModel.getColorSpace() == lrgbmodel_NA.getColorSpace()) {
            this.dataModel = lrgbmodel_NA;
         } else if (this.dataModel.getColorSpace() == srgbmodel_NA.getColorSpace()) {
            this.dataModel = srgbmodel_NA;
         }

         this.model = this.dataModel;
      }

   }

   private static Color interpolateColor(int[] var0, Color var1) {
      int var2 = var1.getRGB();
      int var3 = (var0[var2 >> 24 & 255] & 255) << 24 | (var0[var2 >> 16 & 255] & 255) << 16 | (var0[var2 >> 8 & 255] & 255) << 8 | var0[var2 & 255] & 255;
      return new Color(var3, true);
   }

   private void calculateSingleArrayGradient(Color[] var1, Color[] var2, float var3) {
      this.isSimpleLookup = true;
      int var4 = 1;
      int var5 = 32768;
      int var6 = 32768;
      int var7 = 32768;
      int var8 = 32768;

      int var9;
      int var10;
      for(var9 = 0; var9 < this.gradients.length; ++var9) {
         var10 = (int)(this.normalizedIntervals[var9] / var3 * 255.0F);
         var4 += var10;
         this.gradients[var9] = new int[var10];
         int var11 = var1[var9].getRGB();
         int var12 = var2[var9].getRGB();
         this.interpolate(var11, var12, this.gradients[var9]);
         int var13 = this.gradients[var9][128];
         float var14 = this.normalizedIntervals[var9];
         var5 += (int)((float)(var13 >> 8 & 16711680) * var14);
         var6 += (int)((float)(var13 & 16711680) * var14);
         var7 += (int)((float)(var13 << 8 & 16711680) * var14);
         var8 += (int)((float)(var13 << 16 & 16711680) * var14);
         this.transparencyTest &= var11 & var12;
      }

      this.gradientAverage = (var5 & 16711680) << 8 | var6 & 16711680 | (var7 & 16711680) >> 8 | (var8 & 16711680) >> 16;
      this.gradient = new int[var4];
      var9 = 0;

      for(var10 = 0; var10 < this.gradients.length; ++var10) {
         System.arraycopy(this.gradients[var10], 0, this.gradient, var9, this.gradients[var10].length);
         var9 += this.gradients[var10].length;
      }

      this.gradient[this.gradient.length - 1] = var2[var2.length - 1].getRGB();
      if (this.colorSpace == LinearGradientPaint.LINEAR_RGB) {
         if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1000)) {
            for(var10 = 0; var10 < this.gradient.length; ++var10) {
               this.gradient[var10] = convertEntireColorLinearRGBtoSRGB(this.gradient[var10]);
            }

            this.gradientAverage = convertEntireColorLinearRGBtoSRGB(this.gradientAverage);
         }
      } else if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1004)) {
         for(var10 = 0; var10 < this.gradient.length; ++var10) {
            this.gradient[var10] = convertEntireColorSRGBtoLinearRGB(this.gradient[var10]);
         }

         this.gradientAverage = convertEntireColorSRGBtoLinearRGB(this.gradientAverage);
      }

      this.fastGradientArraySize = this.gradient.length - 1;
   }

   private void calculateMultipleArrayGradient(Color[] var1, Color[] var2) {
      this.isSimpleLookup = false;
      int var5 = 32768;
      int var6 = 32768;
      int var7 = 32768;
      int var8 = 32768;

      int var9;
      int var10;
      for(var9 = 0; var9 < this.gradients.length; ++var9) {
         if (this.normalizedIntervals[var9] != 0.0F) {
            this.gradients[var9] = new int[256];
            int var3 = var1[var9].getRGB();
            int var4 = var2[var9].getRGB();
            this.interpolate(var3, var4, this.gradients[var9]);
            var10 = this.gradients[var9][128];
            float var11 = this.normalizedIntervals[var9];
            var5 += (int)((float)(var10 >> 8 & 16711680) * var11);
            var6 += (int)((float)(var10 & 16711680) * var11);
            var7 += (int)((float)(var10 << 8 & 16711680) * var11);
            var8 += (int)((float)(var10 << 16 & 16711680) * var11);
            this.transparencyTest &= var3;
            this.transparencyTest &= var4;
         }
      }

      this.gradientAverage = (var5 & 16711680) << 8 | var6 & 16711680 | (var7 & 16711680) >> 8 | (var8 & 16711680) >> 16;
      if (this.colorSpace == LinearGradientPaint.LINEAR_RGB) {
         if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1000)) {
            for(var9 = 0; var9 < this.gradients.length; ++var9) {
               for(var10 = 0; var10 < this.gradients[var9].length; ++var10) {
                  this.gradients[var9][var10] = convertEntireColorLinearRGBtoSRGB(this.gradients[var9][var10]);
               }
            }

            this.gradientAverage = convertEntireColorLinearRGBtoSRGB(this.gradientAverage);
         }
      } else if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1004)) {
         for(var9 = 0; var9 < this.gradients.length; ++var9) {
            for(var10 = 0; var10 < this.gradients[var9].length; ++var10) {
               this.gradients[var9][var10] = convertEntireColorSRGBtoLinearRGB(this.gradients[var9][var10]);
            }
         }

         this.gradientAverage = convertEntireColorSRGBtoLinearRGB(this.gradientAverage);
      }

   }

   private void interpolate(int var1, int var2, int[] var3) {
      int var4 = var3.length;
      float var5 = 1.0F / (float)var4;
      int var6 = var1 >> 24 & 255;
      int var7 = var1 >> 16 & 255;
      int var8 = var1 >> 8 & 255;
      int var9 = var1 & 255;
      int var10 = (var2 >> 24 & 255) - var6;
      int var11 = (var2 >> 16 & 255) - var7;
      int var12 = (var2 >> 8 & 255) - var8;
      int var13 = (var2 & 255) - var9;
      float var14 = 2.0F * (float)var10 * var5;
      float var15 = 2.0F * (float)var11 * var5;
      float var16 = 2.0F * (float)var12 * var5;
      float var17 = 2.0F * (float)var13 * var5;
      var3[0] = var1;
      --var4;
      var3[var4] = var2;

      for(int var18 = 1; var18 < var4; ++var18) {
         float var19 = (float)var18;
         var3[var18] = (var6 + ((int)(var19 * var14) + 1 >> 1) & 255) << 24 | (var7 + ((int)(var19 * var15) + 1 >> 1) & 255) << 16 | (var8 + ((int)(var19 * var16) + 1 >> 1) & 255) << 8 | var9 + ((int)(var19 * var17) + 1 >> 1) & 255;
      }

   }

   private static int convertEntireColorLinearRGBtoSRGB(int var0) {
      int var1 = var0 >> 24 & 255;
      int var2 = var0 >> 16 & 255;
      int var3 = var0 >> 8 & 255;
      int var4 = var0 & 255;
      int[] var5 = LinearRGBtoSRGB;
      var2 = var5[var2];
      var3 = var5[var3];
      var4 = var5[var4];
      return var1 << 24 | var2 << 16 | var3 << 8 | var4;
   }

   private static int convertEntireColorSRGBtoLinearRGB(int var0) {
      int var1 = var0 >> 24 & 255;
      int var2 = var0 >> 16 & 255;
      int var3 = var0 >> 8 & 255;
      int var4 = var0 & 255;
      int[] var5 = SRGBtoLinearRGB;
      var2 = var5[var2];
      var3 = var5[var3];
      var4 = var5[var4];
      return var1 << 24 | var2 << 16 | var3 << 8 | var4;
   }

   protected final int indexIntoGradientsArrays(float var1) {
      int var2;
      int var4;
      if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
         if (var1 >= 1.0F) {
            return this.gradientOverflow;
         }

         if (var1 <= 0.0F) {
            return this.gradientUnderflow;
         }
      } else {
         if (this.cycleMethod == MultipleGradientPaint.REPEAT) {
            var1 -= (float)((int)var1);
            if (var1 < 0.0F) {
               ++var1;
            }

            var2 = 0;
            int var8 = 0;
            var4 = 0;
            int var5;
            if (this.isSimpleLookup) {
               var1 *= (float)this.gradient.length;
               var5 = (int)var1;
               if (var5 + 1 < this.gradient.length) {
                  return this.gradient[var5];
               }

               var2 = (int)((var1 - (float)var5) * 65536.0F);
               var8 = this.gradient[var5];
               var4 = this.gradient[0];
            } else {
               for(var5 = 0; var5 < this.gradientsLength; ++var5) {
                  if (var1 < this.fractions[var5 + 1]) {
                     float var6 = var1 - this.fractions[var5];
                     var6 = var6 / this.normalizedIntervals[var5] * 256.0F;
                     int var7 = (int)var6;
                     if (var7 + 1 < this.gradients[var5].length || var5 + 1 < this.gradientsLength) {
                        return this.gradients[var5][var7];
                     }

                     var2 = (int)((var6 - (float)var7) * 65536.0F);
                     var8 = this.gradients[var5][var7];
                     var4 = this.gradients[0][0];
                     break;
                  }
               }
            }

            return ((var8 >> 8 & 16711680) + ((var4 >>> 24) - (var8 >>> 24)) * var2 & 16711680) << 8 | (var8 & 16711680) + ((var4 >> 16 & 255) - (var8 >> 16 & 255)) * var2 & 16711680 | ((var8 << 8 & 16711680) + ((var4 >> 8 & 255) - (var8 >> 8 & 255)) * var2 & 16711680) >> 8 | ((var8 << 16 & 16711680) + ((var4 & 255) - (var8 & 255)) * var2 & 16711680) >> 16;
         }

         if (var1 < 0.0F) {
            var1 = -var1;
         }

         var2 = (int)var1;
         var1 -= (float)var2;
         if ((var2 & 1) == 1) {
            var1 = 1.0F - var1;
         }
      }

      if (this.isSimpleLookup) {
         return this.gradient[(int)(var1 * (float)this.fastGradientArraySize)];
      } else {
         for(var2 = 0; var2 < this.gradientsLength; ++var2) {
            if (var1 < this.fractions[var2 + 1]) {
               float var3 = var1 - this.fractions[var2];
               var4 = (int)(var3 / this.normalizedIntervals[var2] * 255.0F);
               return this.gradients[var2][var4];
            }
         }

         return this.gradientOverflow;
      }
   }

   protected final int indexGradientAntiAlias(float var1, float var2) {
      float var4;
      float var6;
      int var9;
      int var10;
      if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
         float var15 = var1 - var2 / 2.0F;
         var4 = var1 + var2 / 2.0F;
         if (var15 >= 1.0F) {
            return this.gradientOverflow;
         } else if (var4 <= 0.0F) {
            return this.gradientUnderflow;
         } else {
            var6 = 0.0F;
            float var17 = 0.0F;
            int var16;
            float var18;
            if (var4 >= 1.0F) {
               var6 = (var4 - 1.0F) / var2;
               if (var15 <= 0.0F) {
                  var17 = -var15 / var2;
                  var18 = 1.0F;
                  var16 = this.gradientAverage;
               } else {
                  var18 = 1.0F - var15;
                  var16 = this.getAntiAlias(var15, true, 1.0F, false, 1.0F - var15, 1.0F);
               }
            } else {
               if (!(var15 <= 0.0F)) {
                  return this.getAntiAlias(var15, true, var4, false, var2, 1.0F);
               }

               var17 = -var15 / var2;
               var18 = var4;
               var16 = this.getAntiAlias(0.0F, true, var4, false, var4, 1.0F);
            }

            var9 = (int)(65536.0F * var18 / var2);
            var10 = (var16 >>> 20 & 4080) * var9 >> 16;
            int var19 = (var16 >> 12 & 4080) * var9 >> 16;
            int var12 = (var16 >> 4 & 4080) * var9 >> 16;
            int var13 = (var16 << 4 & 4080) * var9 >> 16;
            int var14;
            if (var17 != 0.0F) {
               var14 = this.gradientUnderflow;
               var9 = (int)(65536.0F * var17);
               var10 += (var14 >>> 20 & 4080) * var9 >> 16;
               var19 += (var14 >> 12 & 4080) * var9 >> 16;
               var12 += (var14 >> 4 & 4080) * var9 >> 16;
               var13 += (var14 << 4 & 4080) * var9 >> 16;
            }

            if (var6 != 0.0F) {
               var14 = this.gradientOverflow;
               var9 = (int)(65536.0F * var6);
               var10 += (var14 >>> 20 & 4080) * var9 >> 16;
               var19 += (var14 >> 12 & 4080) * var9 >> 16;
               var12 += (var14 >> 4 & 4080) * var9 >> 16;
               var13 += (var14 << 4 & 4080) * var9 >> 16;
            }

            return (var10 & 4080) << 20 | (var19 & 4080) << 12 | (var12 & 4080) << 4 | (var13 & 4080) >> 4;
         }
      } else {
         int var3 = (int)var2;
         var4 = 1.0F;
         if (var3 != 0) {
            var2 -= (float)var3;
            var4 = var2 / ((float)var3 + var2);
            if ((double)var4 < 0.1) {
               return this.gradientAverage;
            }
         }

         if ((double)var2 > 0.99) {
            return this.gradientAverage;
         } else {
            float var5 = var1 - var2 / 2.0F;
            var6 = var1 + var2 / 2.0F;
            boolean var7 = true;
            boolean var8 = false;
            if (this.cycleMethod == MultipleGradientPaint.REPEAT) {
               var5 -= (float)((int)var5);
               var6 -= (float)((int)var6);
               if (var5 < 0.0F) {
                  ++var5;
               }

               if (var6 < 0.0F) {
                  ++var6;
               }
            } else {
               if (var6 < 0.0F) {
                  var5 = -var5;
                  var7 = !var7;
                  var6 = -var6;
                  var8 = !var8;
               } else if (var5 < 0.0F) {
                  var5 = -var5;
                  var7 = !var7;
               }

               var9 = (int)var5;
               var5 -= (float)var9;
               var10 = (int)var6;
               var6 -= (float)var10;
               if ((var9 & 1) == 1) {
                  var5 = 1.0F - var5;
                  var7 = !var7;
               }

               if ((var10 & 1) == 1) {
                  var6 = 1.0F - var6;
                  var8 = !var8;
               }

               if (var5 > var6 && !var7 && var8) {
                  float var11 = var5;
                  var5 = var6;
                  var6 = var11;
                  var7 = true;
                  var8 = false;
               }
            }

            return this.getAntiAlias(var5, var7, var6, var8, var2, var4);
         }
      }
   }

   private final int getAntiAlias(float var1, boolean var2, float var3, boolean var4, float var5, float var6) {
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      int var11;
      int var12;
      int var13;
      int var14;
      int var15;
      int var16;
      if (this.isSimpleLookup) {
         var1 *= (float)this.fastGradientArraySize;
         var3 *= (float)this.fastGradientArraySize;
         var11 = (int)var1;
         var12 = (int)var3;
         if (var2 && !var4 && var11 <= var12) {
            if (var11 == var12) {
               return this.gradient[var11];
            }

            for(var13 = var11 + 1; var13 < var12; ++var13) {
               var14 = this.gradient[var13];
               var7 += var14 >>> 20 & 4080;
               var8 += var14 >>> 12 & 4080;
               var9 += var14 >>> 4 & 4080;
               var10 += var14 << 4 & 4080;
            }
         } else {
            if (var2) {
               var15 = var11 + 1;
               var16 = this.fastGradientArraySize;
            } else {
               var15 = 0;
               var16 = var11;
            }

            for(var13 = var15; var13 < var16; ++var13) {
               var14 = this.gradient[var13];
               var7 += var14 >>> 20 & 4080;
               var8 += var14 >>> 12 & 4080;
               var9 += var14 >>> 4 & 4080;
               var10 += var14 << 4 & 4080;
            }

            if (var4) {
               var15 = var12 + 1;
               var16 = this.fastGradientArraySize;
            } else {
               var15 = 0;
               var16 = var12;
            }

            for(var13 = var15; var13 < var16; ++var13) {
               var14 = this.gradient[var13];
               var7 += var14 >>> 20 & 4080;
               var8 += var14 >>> 12 & 4080;
               var9 += var14 >>> 4 & 4080;
               var10 += var14 << 4 & 4080;
            }
         }

         var16 = (int)(65536.0F / (var5 * (float)this.fastGradientArraySize));
         var7 = var7 * var16 >> 16;
         var8 = var8 * var16 >> 16;
         var9 = var9 * var16 >> 16;
         var10 = var10 * var16 >> 16;
         if (var2) {
            var15 = (int)((1.0F - (var1 - (float)var11)) * (float)var16);
         } else {
            var15 = (int)((var1 - (float)var11) * (float)var16);
         }

         var14 = this.gradient[var11];
         var7 += (var14 >>> 20 & 4080) * var15 >> 16;
         var8 += (var14 >>> 12 & 4080) * var15 >> 16;
         var9 += (var14 >>> 4 & 4080) * var15 >> 16;
         var10 += (var14 << 4 & 4080) * var15 >> 16;
         if (var4) {
            var15 = (int)((1.0F - (var3 - (float)var12)) * (float)var16);
         } else {
            var15 = (int)((var3 - (float)var12) * (float)var16);
         }

         var14 = this.gradient[var12];
         var7 += (var14 >>> 20 & 4080) * var15 >> 16;
         var8 += (var14 >>> 12 & 4080) * var15 >> 16;
         var9 += (var14 >>> 4 & 4080) * var15 >> 16;
         var10 += (var14 << 4 & 4080) * var15 >> 16;
         var7 = var7 + 8 >> 4;
         var8 = var8 + 8 >> 4;
         var9 = var9 + 8 >> 4;
         var10 = var10 + 8 >> 4;
      } else {
         var11 = 0;
         var12 = 0;
         var13 = -1;
         var14 = -1;
         float var24 = 0.0F;
         float var23 = 0.0F;

         int var17;
         for(var17 = 0; var17 < this.gradientsLength; ++var17) {
            if (var1 < this.fractions[var17 + 1] && var13 == -1) {
               var13 = var17;
               var24 = var1 - this.fractions[var17];
               var24 = var24 / this.normalizedIntervals[var17] * 255.0F;
               var11 = (int)var24;
               if (var14 != -1) {
                  break;
               }
            }

            if (var3 < this.fractions[var17 + 1] && var14 == -1) {
               var14 = var17;
               var23 = var3 - this.fractions[var17];
               var23 = var23 / this.normalizedIntervals[var17] * 255.0F;
               var12 = (int)var23;
               if (var13 != -1) {
                  break;
               }
            }
         }

         if (var13 == -1) {
            var13 = this.gradients.length - 1;
            var11 = 255;
            var24 = (float)255;
         }

         if (var14 == -1) {
            var14 = this.gradients.length - 1;
            var12 = 255;
            var23 = (float)255;
         }

         if (var13 == var14 && var11 <= var12 && var2 && !var4) {
            return this.gradients[var13][var11 + var12 + 1 >> 1];
         }

         int var19 = (int)(65536.0F / var5);
         int var18;
         int var20;
         if (var13 < var14 && var2 && !var4) {
            var18 = (int)((float)var19 * this.normalizedIntervals[var13] * (255.0F - var24) / 255.0F);
            var17 = this.gradients[var13][var11 + 256 >> 1];
            var7 += (var17 >>> 20 & 4080) * var18 >> 16;
            var8 += (var17 >>> 12 & 4080) * var18 >> 16;
            var9 += (var17 >>> 4 & 4080) * var18 >> 16;
            var10 += (var17 << 4 & 4080) * var18 >> 16;

            for(var20 = var13 + 1; var20 < var14; ++var20) {
               var18 = (int)((float)var19 * this.normalizedIntervals[var20]);
               var17 = this.gradients[var20][128];
               var7 += (var17 >>> 20 & 4080) * var18 >> 16;
               var8 += (var17 >>> 12 & 4080) * var18 >> 16;
               var9 += (var17 >>> 4 & 4080) * var18 >> 16;
               var10 += (var17 << 4 & 4080) * var18 >> 16;
            }

            var18 = (int)((float)var19 * this.normalizedIntervals[var14] * var23 / 255.0F);
            var17 = this.gradients[var14][var12 + 1 >> 1];
            var7 += (var17 >>> 20 & 4080) * var18 >> 16;
            var8 += (var17 >>> 12 & 4080) * var18 >> 16;
            var9 += (var17 >>> 4 & 4080) * var18 >> 16;
            var10 += (var17 << 4 & 4080) * var18 >> 16;
         } else {
            if (var2) {
               var18 = (int)((float)var19 * this.normalizedIntervals[var13] * (255.0F - var24) / 255.0F);
               var17 = this.gradients[var13][var11 + 256 >> 1];
            } else {
               var18 = (int)((float)var19 * this.normalizedIntervals[var13] * var24 / 255.0F);
               var17 = this.gradients[var13][var11 + 1 >> 1];
            }

            var7 += (var17 >>> 20 & 4080) * var18 >> 16;
            var8 += (var17 >>> 12 & 4080) * var18 >> 16;
            var9 += (var17 >>> 4 & 4080) * var18 >> 16;
            var10 += (var17 << 4 & 4080) * var18 >> 16;
            if (var4) {
               var18 = (int)((float)var19 * this.normalizedIntervals[var14] * (255.0F - var23) / 255.0F);
               var17 = this.gradients[var14][var12 + 256 >> 1];
            } else {
               var18 = (int)((float)var19 * this.normalizedIntervals[var14] * var23 / 255.0F);
               var17 = this.gradients[var14][var12 + 1 >> 1];
            }

            var7 += (var17 >>> 20 & 4080) * var18 >> 16;
            var8 += (var17 >>> 12 & 4080) * var18 >> 16;
            var9 += (var17 >>> 4 & 4080) * var18 >> 16;
            var10 += (var17 << 4 & 4080) * var18 >> 16;
            int var21;
            if (var2) {
               var20 = var13 + 1;
               var21 = this.gradientsLength;
            } else {
               var20 = 0;
               var21 = var13;
            }

            int var22;
            for(var22 = var20; var22 < var21; ++var22) {
               var18 = (int)((float)var19 * this.normalizedIntervals[var22]);
               var17 = this.gradients[var22][128];
               var7 += (var17 >>> 20 & 4080) * var18 >> 16;
               var8 += (var17 >>> 12 & 4080) * var18 >> 16;
               var9 += (var17 >>> 4 & 4080) * var18 >> 16;
               var10 += (var17 << 4 & 4080) * var18 >> 16;
            }

            if (var4) {
               var20 = var14 + 1;
               var21 = this.gradientsLength;
            } else {
               var20 = 0;
               var21 = var14;
            }

            for(var22 = var20; var22 < var21; ++var22) {
               var18 = (int)((float)var19 * this.normalizedIntervals[var22]);
               var17 = this.gradients[var22][128];
               var7 += (var17 >>> 20 & 4080) * var18 >> 16;
               var8 += (var17 >>> 12 & 4080) * var18 >> 16;
               var9 += (var17 >>> 4 & 4080) * var18 >> 16;
               var10 += (var17 << 4 & 4080) * var18 >> 16;
            }
         }

         var7 = var7 + 8 >> 4;
         var8 = var8 + 8 >> 4;
         var9 = var9 + 8 >> 4;
         var10 = var10 + 8 >> 4;
      }

      if (var6 != 1.0F) {
         var11 = (int)(65536.0F * (1.0F - var6));
         var12 = (this.gradientAverage >>> 24 & 255) * var11;
         var13 = (this.gradientAverage >> 16 & 255) * var11;
         var14 = (this.gradientAverage >> 8 & 255) * var11;
         var15 = (this.gradientAverage & 255) * var11;
         var16 = (int)(var6 * 65536.0F);
         var7 = var7 * var16 + var12 >> 16;
         var8 = var8 * var16 + var13 >> 16;
         var9 = var9 * var16 + var14 >> 16;
         var10 = var10 * var16 + var15 >> 16;
      }

      return var7 << 24 | var8 << 16 | var9 << 8 | var10;
   }

   private static int convertSRGBtoLinearRGB(int var0) {
      float var2 = (float)var0 / 255.0F;
      float var1;
      if (var2 <= 0.04045F) {
         var1 = var2 / 12.92F;
      } else {
         var1 = (float)Math.pow(((double)var2 + 0.055) / 1.055, 2.4);
      }

      int var3 = Math.round(var1 * 255.0F);
      return var3;
   }

   private static int convertLinearRGBtoSRGB(int var0) {
      float var2 = (float)var0 / 255.0F;
      float var1;
      if (var2 <= 0.0031308F) {
         var1 = var2 * 12.92F;
      } else {
         var1 = 1.055F * (float)Math.pow((double)var2, 0.4166666666666667) - 0.055F;
      }

      int var3 = Math.round(var1 * 255.0F);
      return var3;
   }

   public final Raster getRaster(int var1, int var2, int var3, int var4) {
      if (var3 != 0 && var4 != 0) {
         WritableRaster var5 = this.saved;
         if (var5 == null || var5.getWidth() < var3 || var5.getHeight() < var4) {
            var5 = getCachedRaster(this.dataModel, var3, var4);
            this.saved = var5;
            var5 = var5.createWritableChild(var5.getMinX(), var5.getMinY(), var3, var4, 0, 0, (int[])null);
         }

         DataBufferInt var6 = (DataBufferInt)var5.getDataBuffer();
         int[] var7 = var6.getBankData()[0];
         int var8 = var6.getOffset();
         int var9 = ((SinglePixelPackedSampleModel)var5.getSampleModel()).getScanlineStride();
         int var10 = var9 - var3;
         this.fillRaster(var7, var8, var10, var1, var2, var3, var4);
         GraphicsUtil.coerceData(var5, this.dataModel, this.model.isAlphaPremultiplied());
         return var5;
      } else {
         return null;
      }
   }

   protected abstract void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

   protected static final synchronized WritableRaster getCachedRaster(ColorModel var0, int var1, int var2) {
      if (var0 == cachedModel && cached != null) {
         WritableRaster var3 = (WritableRaster)cached.get();
         if (var3 != null && var3.getWidth() >= var1 && var3.getHeight() >= var2) {
            cached = null;
            return var3;
         }
      }

      if (var1 < 32) {
         var1 = 32;
      }

      if (var2 < 32) {
         var2 = 32;
      }

      return var0.createCompatibleWritableRaster(var1, var2);
   }

   protected static final synchronized void putCachedRaster(ColorModel var0, WritableRaster var1) {
      if (cached != null) {
         WritableRaster var2 = (WritableRaster)cached.get();
         if (var2 != null) {
            int var3 = var2.getWidth();
            int var4 = var2.getHeight();
            int var5 = var1.getWidth();
            int var6 = var1.getHeight();
            if (var3 >= var5 && var4 >= var6) {
               return;
            }

            if (var3 * var4 >= var5 * var6) {
               return;
            }
         }
      }

      cachedModel = var0;
      cached = new WeakReference(var1);
   }

   public final void dispose() {
      if (this.saved != null) {
         putCachedRaster(this.model, this.saved);
         this.saved = null;
      }

   }

   public final ColorModel getColorModel() {
      return this.model;
   }

   static {
      for(int var0 = 0; var0 < 256; ++var0) {
         SRGBtoLinearRGB[var0] = convertSRGBtoLinearRGB(var0);
         LinearRGBtoSRGB[var0] = convertLinearRGBtoSRGB(var0);
      }

   }
}
