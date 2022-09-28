package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SinglePixelPackedSampleModel;

public final class BumpMap {
   private RenderedImage texture;
   private double surfaceScale;
   private double surfaceScaleX;
   private double surfaceScaleY;
   private double scaleX;
   private double scaleY;

   public BumpMap(RenderedImage var1, double var2, double var4, double var6) {
      this.texture = var1;
      this.surfaceScaleX = var2 * var4;
      this.surfaceScaleY = var2 * var6;
      this.surfaceScale = var2;
      this.scaleX = var4;
      this.scaleY = var6;
   }

   public double getSurfaceScale() {
      return this.surfaceScale;
   }

   public double[][][] getNormalArray(int var1, int var2, int var3, int var4) {
      double[][][] var5 = new double[var4][var3][4];
      Rectangle var6 = new Rectangle(var1 - 1, var2 - 1, var3 + 2, var4 + 2);
      Rectangle var7 = new Rectangle(this.texture.getMinX(), this.texture.getMinY(), this.texture.getWidth(), this.texture.getHeight());
      if (!var6.intersects(var7)) {
         return var5;
      } else {
         var6 = var6.intersection(var7);
         Raster var8 = this.texture.getData(var6);
         var6 = var8.getBounds();
         DataBufferInt var9 = (DataBufferInt)var8.getDataBuffer();
         int[] var10 = var9.getBankData()[0];
         SinglePixelPackedSampleModel var11 = (SinglePixelPackedSampleModel)var8.getSampleModel();
         int var12 = var11.getScanlineStride();
         int var13 = var12 + 1;
         int var14 = var12 - 1;
         double var15 = 0.0;
         double var17 = 0.0;
         double var19 = 0.0;
         double var21 = 0.0;
         double var23 = 0.0;
         double var25 = 0.0;
         double var27 = 0.0;
         double var29 = 0.0;
         double var31 = 0.0;
         double var35 = this.surfaceScaleX / 4.0;
         double var37 = this.surfaceScaleY / 4.0;
         double var39 = this.surfaceScaleX / 2.0;
         double var41 = this.surfaceScaleY / 2.0;
         double var43 = this.surfaceScaleX / 3.0;
         double var45 = this.surfaceScaleY / 3.0;
         double var47 = this.surfaceScaleX * 2.0 / 3.0;
         double var49 = this.surfaceScaleY * 2.0 / 3.0;
         if (var3 <= 0) {
            return var5;
         } else if (var4 <= 0) {
            return var5;
         } else {
            int var53 = Math.min(var6.x + var6.width - 1, var1 + var3);
            int var54 = Math.min(var6.y + var6.height - 1, var2 + var4);
            int var55 = var9.getOffset() + var11.getOffset(var6.x - var8.getSampleModelTranslateX(), var6.y - var8.getSampleModelTranslateY());
            int var56 = var2;
            if (var2 < var6.y) {
               var56 = var6.y;
            }

            double var33;
            double[][] var57;
            int var58;
            int var59;
            double[] var60;
            if (var56 == var6.y) {
               if (var56 == var54) {
                  var57 = var5[var56 - var2];
                  var58 = var1;
                  if (var1 < var6.x) {
                     var58 = var6.x;
                  }

                  var59 = var55 + (var58 - var6.x) + var12 * (var56 - var6.y);
                  var23 = (double)(var10[var59] >>> 24) * 0.00392156862745098;
                  if (var58 != var6.x) {
                     var21 = (double)(var10[var59 - 1] >>> 24) * 0.00392156862745098;
                  } else if (var58 < var53) {
                     var25 = (double)(var10[var59 + 1] >>> 24) * 0.00392156862745098;
                     var60 = var57[var58 - var1];
                     var60[0] = 2.0 * this.surfaceScaleX * (var23 - var25);
                     var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + 1.0);
                     var60[0] *= var33;
                     var60[1] = 0.0;
                     var60[2] = var33;
                     var60[3] = var23 * this.surfaceScale;
                     ++var59;
                     ++var58;
                     var21 = var23;
                     var23 = var25;
                  } else {
                     var21 = var23;
                  }

                  while(var58 < var53) {
                     var25 = (double)(var10[var59 + 1] >>> 24) * 0.00392156862745098;
                     var60 = var57[var58 - var1];
                     var60[0] = this.surfaceScaleX * (var21 - var25);
                     var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + 1.0);
                     var60[0] *= var33;
                     var60[1] = 0.0;
                     var60[2] = var33;
                     var60[3] = var23 * this.surfaceScale;
                     ++var59;
                     var21 = var23;
                     var23 = var25;
                     ++var58;
                  }

                  if (var58 < var1 + var3 && var58 == var6.x + var6.width - 1) {
                     var60 = var57[var58 - var1];
                     var60[0] = 2.0 * this.surfaceScaleX * (var21 - var23);
                     var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                     var60[0] *= var33;
                     var60[1] *= var33;
                     var60[2] = var33;
                     var60[3] = var23 * this.surfaceScale;
                  }

                  return var5;
               }

               var57 = var5[var56 - var2];
               var58 = var55 + var12 * (var56 - var6.y);
               var59 = var1;
               if (var1 < var6.x) {
                  var59 = var6.x;
               }

               var58 += var59 - var6.x;
               var23 = (double)(var10[var58] >>> 24) * 0.00392156862745098;
               var29 = (double)(var10[var58 + var12] >>> 24) * 0.00392156862745098;
               if (var59 != var6.x) {
                  var21 = (double)(var10[var58 - 1] >>> 24) * 0.00392156862745098;
                  var27 = (double)(var10[var58 + var14] >>> 24) * 0.00392156862745098;
               } else if (var59 < var53) {
                  var25 = (double)(var10[var58 + 1] >>> 24) * 0.00392156862745098;
                  var31 = (double)(var10[var58 + var13] >>> 24) * 0.00392156862745098;
                  var60 = var57[var59 - var1];
                  var60[0] = -var47 * (2.0 * var25 + var31 - 2.0 * var23 - var29);
                  var60[1] = -var49 * (2.0 * var29 + var31 - 2.0 * var23 - var25);
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
                  ++var58;
                  ++var59;
                  var21 = var23;
                  var27 = var29;
                  var23 = var25;
                  var29 = var31;
               } else {
                  var21 = var23;
                  var27 = var29;
               }

               while(var59 < var53) {
                  var25 = (double)(var10[var58 + 1] >>> 24) * 0.00392156862745098;
                  var31 = (double)(var10[var58 + var13] >>> 24) * 0.00392156862745098;
                  var60 = var57[var59 - var1];
                  var60[0] = -var43 * (2.0 * var25 + var31 - (2.0 * var21 + var27));
                  var60[1] = -var41 * (var27 + 2.0 * var29 + var31 - (var21 + 2.0 * var23 + var25));
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
                  ++var58;
                  var21 = var23;
                  var27 = var29;
                  var23 = var25;
                  var29 = var31;
                  ++var59;
               }

               if (var59 < var1 + var3 && var59 == var6.x + var6.width - 1) {
                  var60 = var57[var59 - var1];
                  var60[0] = -var47 * (2.0 * var23 + var29 - (2.0 * var21 + var27));
                  var60[1] = -var49 * (2.0 * var29 + var27 - (2.0 * var23 + var21));
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
               }

               ++var56;
            }

            for(; var56 < var54; ++var56) {
               var57 = var5[var56 - var2];
               var58 = var55 + var12 * (var56 - var6.y);
               var59 = var1;
               if (var1 < var6.x) {
                  var59 = var6.x;
               }

               var58 += var59 - var6.x;
               var17 = (double)(var10[var58 - var12] >>> 24) * 0.00392156862745098;
               var23 = (double)(var10[var58] >>> 24) * 0.00392156862745098;
               var29 = (double)(var10[var58 + var12] >>> 24) * 0.00392156862745098;
               if (var59 != var6.x) {
                  var15 = (double)(var10[var58 - var13] >>> 24) * 0.00392156862745098;
                  var21 = (double)(var10[var58 - 1] >>> 24) * 0.00392156862745098;
                  var27 = (double)(var10[var58 + var14] >>> 24) * 0.00392156862745098;
               } else if (var59 < var53) {
                  var25 = (double)(var10[var58 + 1] >>> 24) * 0.00392156862745098;
                  var19 = (double)(var10[var58 - var14] >>> 24) * 0.00392156862745098;
                  var31 = (double)(var10[var58 + var13] >>> 24) * 0.00392156862745098;
                  var60 = var57[var59 - var1];
                  var60[0] = -var39 * (var19 + 2.0 * var25 + var31 - (var17 + 2.0 * var23 + var29));
                  var60[1] = -var45 * (2.0 * var17 + var19 - (2.0 * var23 + var25));
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
                  ++var58;
                  ++var59;
                  var15 = var17;
                  var21 = var23;
                  var27 = var29;
                  var17 = var19;
                  var23 = var25;
                  var29 = var31;
               } else {
                  var15 = var17;
                  var21 = var23;
                  var27 = var29;
               }

               while(var59 < var53) {
                  var19 = (double)(var10[var58 - var14] >>> 24) * 0.00392156862745098;
                  var25 = (double)(var10[var58 + 1] >>> 24) * 0.00392156862745098;
                  var31 = (double)(var10[var58 + var13] >>> 24) * 0.00392156862745098;
                  var60 = var57[var59 - var1];
                  var60[0] = -var35 * (var19 + 2.0 * var25 + var31 - (var15 + 2.0 * var21 + var27));
                  var60[1] = -var37 * (var27 + 2.0 * var29 + var31 - (var15 + 2.0 * var17 + var19));
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
                  ++var58;
                  var15 = var17;
                  var21 = var23;
                  var27 = var29;
                  var17 = var19;
                  var23 = var25;
                  var29 = var31;
                  ++var59;
               }

               if (var59 < var1 + var3 && var59 == var6.x + var6.width - 1) {
                  var60 = var57[var59 - var1];
                  var60[0] = -var39 * (var17 + 2.0 * var23 + var29 - (var15 + 2.0 * var21 + var27));
                  var60[1] = -var45 * (var27 + 2.0 * var29 - (var15 + 2.0 * var17));
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
               }
            }

            if (var56 < var2 + var4 && var56 == var6.y + var6.height - 1) {
               var57 = var5[var56 - var2];
               var58 = var55 + var12 * (var56 - var6.y);
               var59 = var1;
               if (var1 < var6.x) {
                  var59 = var6.x;
               }

               var58 += var59 - var6.x;
               var23 = (double)(var10[var58] >>> 24) * 0.00392156862745098;
               var17 = (double)(var10[var58 - var12] >>> 24) * 0.00392156862745098;
               if (var59 != var6.x) {
                  var15 = (double)(var10[var58 - var13] >>> 24) * 0.00392156862745098;
                  var21 = (double)(var10[var58 - 1] >>> 24) * 0.00392156862745098;
               } else if (var59 < var53) {
                  var25 = (double)(var10[var58 + 1] >>> 24) * 0.00392156862745098;
                  var19 = (double)(var10[var58 - var14] >>> 24) * 0.00392156862745098;
                  var60 = var57[var59 - var1];
                  var60[0] = -var47 * (2.0 * var25 + var19 - 2.0 * var23 - var17);
                  var60[1] = -var49 * (2.0 * var23 + var25 - 2.0 * var17 - var19);
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
                  ++var58;
                  ++var59;
                  var21 = var23;
                  var15 = var17;
                  var23 = var25;
                  var17 = var19;
               } else {
                  var21 = var23;
                  var15 = var17;
               }

               while(var59 < var53) {
                  var25 = (double)(var10[var58 + 1] >>> 24) * 0.00392156862745098;
                  var19 = (double)(var10[var58 - var14] >>> 24) * 0.00392156862745098;
                  var60 = var57[var59 - var1];
                  var60[0] = -var43 * (2.0 * var25 + var19 - (2.0 * var21 + var15));
                  var60[1] = -var41 * (var21 + 2.0 * var23 + var25 - (var15 + 2.0 * var17 + var19));
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
                  ++var58;
                  var21 = var23;
                  var15 = var17;
                  var23 = var25;
                  var17 = var19;
                  ++var59;
               }

               if (var59 < var1 + var3 && var59 == var6.x + var6.width - 1) {
                  var60 = var57[var59 - var1];
                  var60[0] = -var47 * (2.0 * var23 + var17 - (2.0 * var21 + var15));
                  var60[1] = -var49 * (2.0 * var23 + var21 - (2.0 * var17 + var15));
                  var33 = 1.0 / Math.sqrt(var60[0] * var60[0] + var60[1] * var60[1] + 1.0);
                  var60[0] *= var33;
                  var60[1] *= var33;
                  var60[2] = var33;
                  var60[3] = var23 * this.surfaceScale;
               }
            }

            return var5;
         }
      }
   }
}
