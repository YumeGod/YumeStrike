package org.apache.batik.ext.awt;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

final class LinearGradientPaintContext extends MultipleGradientPaintContext {
   private float dgdX;
   private float dgdY;
   private float gc;
   private float pixSz;
   private static final int DEFAULT_IMPL = 1;
   private static final int ANTI_ALIAS_IMPL = 3;
   private int fillMethod;

   public LinearGradientPaintContext(ColorModel var1, Rectangle var2, Rectangle2D var3, AffineTransform var4, RenderingHints var5, Point2D var6, Point2D var7, float[] var8, Color[] var9, MultipleGradientPaint.CycleMethodEnum var10, MultipleGradientPaint.ColorSpaceEnum var11) throws NoninvertibleTransformException {
      super(var1, var2, var3, var4, var5, var8, var9, var10, var11);
      Point2D.Float var12 = new Point2D.Float((float)var6.getX(), (float)var6.getY());
      Point2D.Float var13 = new Point2D.Float((float)var7.getX(), (float)var7.getY());
      float var14 = var13.x - var12.x;
      float var15 = var13.y - var12.y;
      float var16 = var14 * var14 + var15 * var15;
      float var17 = var14 / var16;
      float var18 = var15 / var16;
      this.dgdX = this.a00 * var17 + this.a10 * var18;
      this.dgdY = this.a01 * var17 + this.a11 * var18;
      float var19 = Math.abs(this.dgdX);
      float var20 = Math.abs(this.dgdY);
      if (var19 > var20) {
         this.pixSz = var19;
      } else {
         this.pixSz = var20;
      }

      this.gc = (this.a02 - var12.x) * var17 + (this.a12 - var12.y) * var18;
      Object var21 = var5.get(RenderingHints.KEY_COLOR_RENDERING);
      Object var22 = var5.get(RenderingHints.KEY_RENDERING);
      this.fillMethod = 1;
      if (var10 == MultipleGradientPaint.REPEAT || this.hasDiscontinuity) {
         if (var22 == RenderingHints.VALUE_RENDER_QUALITY) {
            this.fillMethod = 3;
         }

         if (var21 == RenderingHints.VALUE_COLOR_RENDER_SPEED) {
            this.fillMethod = 1;
         } else if (var21 == RenderingHints.VALUE_COLOR_RENDER_QUALITY) {
            this.fillMethod = 3;
         }
      }

   }

   protected void fillHardNoCycle(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = this.dgdX * (float)var4 + this.gc;

      for(int var9 = 0; var9 < var7; ++var9) {
         float var10 = var8 + this.dgdY * (float)(var5 + var9);
         int var11 = var2 + var6;
         int var12;
         int var13;
         if (this.dgdX == 0.0F) {
            if (var10 <= 0.0F) {
               var12 = this.gradientUnderflow;
            } else if (var10 >= 1.0F) {
               var12 = this.gradientOverflow;
            } else {
               for(var13 = 0; var13 < this.gradientsLength - 1 && !(var10 < this.fractions[var13 + 1]); ++var13) {
               }

               float var28 = var10 - this.fractions[var13];
               float var29 = var28 * 255.0F / this.normalizedIntervals[var13] + 0.5F;
               var12 = this.gradients[var13][(int)var29];
            }

            while(var2 < var11) {
               var1[var2++] = var12;
            }
         } else {
            int var14;
            int var15;
            float var16;
            float var17;
            if (this.dgdX >= 0.0F) {
               var16 = (1.0F - var10) / this.dgdX;
               var17 = (float)Math.ceil((double)((0.0F - var10) / this.dgdX));
               var14 = this.gradientUnderflow;
               var15 = this.gradientOverflow;
            } else {
               var16 = (0.0F - var10) / this.dgdX;
               var17 = (float)Math.ceil((double)((1.0F - var10) / this.dgdX));
               var14 = this.gradientOverflow;
               var15 = this.gradientUnderflow;
            }

            if (var16 > (float)var6) {
               var12 = var6;
            } else {
               var12 = (int)var16;
            }

            if (var17 > (float)var6) {
               var13 = var6;
            } else {
               var13 = (int)var17;
            }

            int var18 = var2 + var12;
            int var19;
            if (var13 > 0) {
               for(var19 = var2 + var13; var2 < var19; var1[var2++] = var14) {
               }

               var10 += this.dgdX * (float)var13;
            }

            float var20;
            int[] var21;
            double var22;
            int var24;
            int var25;
            int var26;
            int var27;
            if (this.dgdX > 0.0F) {
               for(var19 = 0; var19 < this.gradientsLength - 1 && !(var10 < this.fractions[var19 + 1]); ++var19) {
               }

               while(var2 < var18) {
                  var20 = var10 - this.fractions[var19];
                  var21 = this.gradients[var19];
                  var22 = Math.ceil((double)((this.fractions[var19 + 1] - var10) / this.dgdX));
                  if (var22 > (double)var6) {
                     var24 = var6;
                  } else {
                     var24 = (int)var22;
                  }

                  var25 = var2 + var24;
                  if (var25 > var18) {
                     var25 = var18;
                  }

                  var26 = (int)(var20 * 255.0F / this.normalizedIntervals[var19] * 65536.0F) + '耀';

                  for(var27 = (int)(this.dgdX * 255.0F / this.normalizedIntervals[var19] * 65536.0F); var2 < var25; var26 += var27) {
                     var1[var2++] = var21[var26 >> 16];
                  }

                  var10 = (float)((double)var10 + (double)this.dgdX * var22);
                  ++var19;
               }
            } else {
               for(var19 = this.gradientsLength - 1; var19 > 0 && !(var10 > this.fractions[var19]); --var19) {
               }

               while(var2 < var18) {
                  var20 = var10 - this.fractions[var19];
                  var21 = this.gradients[var19];
                  var22 = Math.ceil((double)(var20 / -this.dgdX));
                  if (var22 > (double)var6) {
                     var24 = var6;
                  } else {
                     var24 = (int)var22;
                  }

                  var25 = var2 + var24;
                  if (var25 > var18) {
                     var25 = var18;
                  }

                  var26 = (int)(var20 * 255.0F / this.normalizedIntervals[var19] * 65536.0F) + '耀';

                  for(var27 = (int)(this.dgdX * 255.0F / this.normalizedIntervals[var19] * 65536.0F); var2 < var25; var26 += var27) {
                     var1[var2++] = var21[var26 >> 16];
                  }

                  var10 = (float)((double)var10 + (double)this.dgdX * var22);
                  --var19;
               }
            }

            while(var2 < var11) {
               var1[var2++] = var15;
            }
         }

         var2 += var3;
      }

   }

   protected void fillSimpleNoCycle(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = this.dgdX * (float)var4 + this.gc;
      float var9 = this.dgdX * (float)this.fastGradientArraySize;
      int var10 = (int)(var9 * 65536.0F);
      int[] var11 = this.gradient;

      for(int var12 = 0; var12 < var7; ++var12) {
         float var13 = var8 + this.dgdY * (float)(var5 + var12);
         var13 *= (float)this.fastGradientArraySize;
         var13 = (float)((double)var13 + 0.5);
         int var14 = var2 + var6;
         float var15 = this.dgdX * (float)this.fastGradientArraySize * (float)var6;
         if (var15 < 0.0F) {
            var15 = -var15;
         }

         int var16;
         if ((double)var15 < 0.3) {
            if (var13 <= 0.0F) {
               var16 = this.gradientUnderflow;
            } else if (var13 >= (float)this.fastGradientArraySize) {
               var16 = this.gradientOverflow;
            } else {
               var16 = var11[(int)var13];
            }

            while(var2 < var14) {
               var1[var2++] = var16;
            }
         } else {
            int var17;
            int var18;
            int var19;
            if (this.dgdX > 0.0F) {
               var16 = (int)(((float)this.fastGradientArraySize - var13) / var9);
               var17 = (int)Math.ceil((double)(0.0F - var13 / var9));
               var18 = this.gradientUnderflow;
               var19 = this.gradientOverflow;
            } else {
               var16 = (int)((0.0F - var13) / var9);
               var17 = (int)Math.ceil((double)(((float)this.fastGradientArraySize - var13) / var9));
               var18 = this.gradientOverflow;
               var19 = this.gradientUnderflow;
            }

            if (var16 > var6) {
               var16 = var6;
            }

            int var20 = var2 + var16;
            int var21;
            if (var17 > 0) {
               if (var17 > var6) {
                  var17 = var6;
               }

               for(var21 = var2 + var17; var2 < var21; var1[var2++] = var18) {
               }

               var13 += var9 * (float)var17;
            }

            for(var21 = (int)(var13 * 65536.0F); var2 < var20; var21 += var10) {
               var1[var2++] = var11[var21 >> 16];
            }

            while(var2 < var14) {
               var1[var2++] = var19;
            }
         }

         var2 += var3;
      }

   }

   protected void fillSimpleRepeat(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = this.dgdX * (float)var4 + this.gc;
      float var9 = (this.dgdX - (float)((int)this.dgdX)) * (float)this.fastGradientArraySize;
      if (var9 < 0.0F) {
         var9 += (float)this.fastGradientArraySize;
      }

      int[] var10 = this.gradient;

      for(int var11 = 0; var11 < var7; ++var11) {
         float var12 = var8 + this.dgdY * (float)(var5 + var11);
         var12 -= (float)((int)var12);
         if (var12 < 0.0F) {
            ++var12;
         }

         var12 *= (float)this.fastGradientArraySize;
         var12 = (float)((double)var12 + 0.5);

         for(int var13 = var2 + var6; var2 < var13; var12 += var9) {
            int var14 = (int)var12;
            if (var14 >= this.fastGradientArraySize) {
               var12 -= (float)this.fastGradientArraySize;
               var14 -= this.fastGradientArraySize;
            }

            var1[var2++] = var10[var14];
         }

         var2 += var3;
      }

   }

   protected void fillSimpleReflect(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = this.dgdX * (float)var4 + this.gc;
      int[] var9 = this.gradient;

      for(int var10 = 0; var10 < var7; ++var10) {
         float var11 = var8 + this.dgdY * (float)(var5 + var10);
         var11 -= (float)(2 * (int)(var11 / 2.0F));
         float var12 = this.dgdX;
         if (var11 < 0.0F) {
            var11 = -var11;
            var12 = -var12;
         }

         var12 -= 2.0F * ((float)((int)var12) / 2.0F);
         if (var12 < 0.0F) {
            var12 = (float)((double)var12 + 2.0);
         }

         int var13 = 2 * this.fastGradientArraySize;
         var11 *= (float)this.fastGradientArraySize;
         var11 = (float)((double)var11 + 0.5);
         var12 *= (float)this.fastGradientArraySize;

         for(int var14 = var2 + var6; var2 < var14; var11 += var12) {
            int var15 = (int)var11;
            if (var15 >= var13) {
               var11 -= (float)var13;
               var15 -= var13;
            }

            if (var15 <= this.fastGradientArraySize) {
               var1[var2++] = var9[var15];
            } else {
               var1[var2++] = var9[var13 - var15];
            }
         }

         var2 += var3;
      }

   }

   protected void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = this.dgdX * (float)var4 + this.gc;
      int var9;
      float var10;
      int var11;
      if (this.fillMethod == 3) {
         for(var9 = 0; var9 < var7; ++var9) {
            var10 = var8 + this.dgdY * (float)(var5 + var9);

            for(var11 = var2 + var6; var2 < var11; var10 += this.dgdX) {
               var1[var2++] = this.indexGradientAntiAlias(var10, this.pixSz);
            }

            var2 += var3;
         }
      } else if (!this.isSimpleLookup) {
         if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
            this.fillHardNoCycle(var1, var2, var3, var4, var5, var6, var7);
         } else {
            for(var9 = 0; var9 < var7; ++var9) {
               var10 = var8 + this.dgdY * (float)(var5 + var9);

               for(var11 = var2 + var6; var2 < var11; var10 += this.dgdX) {
                  var1[var2++] = this.indexIntoGradientsArrays(var10);
               }

               var2 += var3;
            }
         }
      } else if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
         this.fillSimpleNoCycle(var1, var2, var3, var4, var5, var6, var7);
      } else if (this.cycleMethod == MultipleGradientPaint.REPEAT) {
         this.fillSimpleRepeat(var1, var2, var3, var4, var5, var6, var7);
      } else {
         this.fillSimpleReflect(var1, var2, var3, var4, var5, var6, var7);
      }

   }
}
