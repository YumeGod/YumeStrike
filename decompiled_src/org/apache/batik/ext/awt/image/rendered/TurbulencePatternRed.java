package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;

public final class TurbulencePatternRed extends AbstractRed {
   private StitchInfo stitchInfo = null;
   private static final AffineTransform IDENTITY = new AffineTransform();
   private double baseFrequencyX;
   private double baseFrequencyY;
   private int numOctaves;
   private int seed;
   private Rectangle2D tile;
   private AffineTransform txf;
   private boolean isFractalNoise;
   private int[] channels;
   double[] tx = new double[]{1.0, 0.0};
   double[] ty = new double[]{0.0, 1.0};
   private static final int RAND_m = Integer.MAX_VALUE;
   private static final int RAND_a = 16807;
   private static final int RAND_q = 127773;
   private static final int RAND_r = 2836;
   private static final int BSize = 256;
   private static final int BM = 255;
   private static final double PerlinN = 4096.0;
   private final int[] latticeSelector = new int[257];
   private final double[] gradient = new double[2056];

   public double getBaseFrequencyX() {
      return this.baseFrequencyX;
   }

   public double getBaseFrequencyY() {
      return this.baseFrequencyY;
   }

   public int getNumOctaves() {
      return this.numOctaves;
   }

   public int getSeed() {
      return this.seed;
   }

   public Rectangle2D getTile() {
      return (Rectangle2D)this.tile.clone();
   }

   public boolean isFractalNoise() {
      return this.isFractalNoise;
   }

   public boolean[] getChannels() {
      boolean[] var1 = new boolean[4];

      for(int var2 = 0; var2 < this.channels.length; ++var2) {
         var1[this.channels[var2]] = true;
      }

      return var1;
   }

   public final int setupSeed(int var1) {
      if (var1 <= 0) {
         var1 = -(var1 % 2147483646) + 1;
      }

      if (var1 > 2147483646) {
         var1 = 2147483646;
      }

      return var1;
   }

   public final int random(int var1) {
      int var2 = 16807 * (var1 % 127773) - 2836 * (var1 / 127773);
      if (var2 <= 0) {
         var2 += Integer.MAX_VALUE;
      }

      return var2;
   }

   private void initLattice(int var1) {
      var1 = this.setupSeed(var1);

      double var6;
      int var8;
      int var10;
      for(var10 = 0; var10 < 4; ++var10) {
         for(var8 = 0; var8 < 256; ++var8) {
            double var2 = (double)((var1 = this.random(var1)) % 512 - 256);
            double var4 = (double)((var1 = this.random(var1)) % 512 - 256);
            var6 = 1.0 / Math.sqrt(var2 * var2 + var4 * var4);
            this.gradient[var8 * 8 + var10 * 2] = var2 * var6;
            this.gradient[var8 * 8 + var10 * 2 + 1] = var4 * var6;
         }
      }

      for(var8 = 0; var8 < 256; this.latticeSelector[var8] = var8++) {
      }

      while(true) {
         --var8;
         int var9;
         if (var8 <= 0) {
            this.latticeSelector[256] = this.latticeSelector[0];

            for(var9 = 0; var9 < 8; ++var9) {
               this.gradient[2048 + var9] = this.gradient[var9];
            }

            return;
         }

         var10 = this.latticeSelector[var8];
         var9 = (var1 = this.random(var1)) % 256;
         this.latticeSelector[var8] = this.latticeSelector[var9];
         this.latticeSelector[var9] = var10;
         int var11 = var8 << 3;
         int var12 = var9 << 3;

         for(var9 = 0; var9 < 8; ++var9) {
            var6 = this.gradient[var11 + var9];
            this.gradient[var11 + var9] = this.gradient[var12 + var9];
            this.gradient[var12 + var9] = var6;
         }
      }
   }

   private static final double s_curve(double var0) {
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static final double lerp(double var0, double var2, double var4) {
      return var2 + var0 * (var4 - var2);
   }

   private final void noise2(double[] var1, double var2, double var4) {
      var2 += 4096.0;
      int var6 = (int)var2 & 255;
      int var8 = this.latticeSelector[var6];
      int var9 = this.latticeSelector[var6 + 1];
      double var10 = var2 - (double)((int)var2);
      double var12 = var10 - 1.0;
      double var18 = s_curve(var10);
      var4 += 4096.0;
      var6 = (int)var4;
      int var7 = (var9 + var6 & 255) << 3;
      var6 = (var8 + var6 & 255) << 3;
      double var14 = var4 - (double)((int)var4);
      double var16 = var14 - 1.0;
      double var20 = s_curve(var14);
      switch (this.channels.length) {
         case 4:
            var1[3] = lerp(var20, lerp(var18, var10 * this.gradient[var6 + 6] + var14 * this.gradient[var6 + 7], var12 * this.gradient[var7 + 6] + var14 * this.gradient[var7 + 7]), lerp(var18, var10 * this.gradient[var6 + 8 + 6] + var16 * this.gradient[var6 + 8 + 7], var12 * this.gradient[var7 + 8 + 6] + var16 * this.gradient[var7 + 8 + 7]));
         case 3:
            var1[2] = lerp(var20, lerp(var18, var10 * this.gradient[var6 + 4] + var14 * this.gradient[var6 + 5], var12 * this.gradient[var7 + 4] + var14 * this.gradient[var7 + 5]), lerp(var18, var10 * this.gradient[var6 + 8 + 4] + var16 * this.gradient[var6 + 8 + 5], var12 * this.gradient[var7 + 8 + 4] + var16 * this.gradient[var7 + 8 + 5]));
         case 2:
            var1[1] = lerp(var20, lerp(var18, var10 * this.gradient[var6 + 2] + var14 * this.gradient[var6 + 3], var12 * this.gradient[var7 + 2] + var14 * this.gradient[var7 + 3]), lerp(var18, var10 * this.gradient[var6 + 8 + 2] + var16 * this.gradient[var6 + 8 + 3], var12 * this.gradient[var7 + 8 + 2] + var16 * this.gradient[var7 + 8 + 3]));
         case 1:
            var1[0] = lerp(var20, lerp(var18, var10 * this.gradient[var6 + 0] + var14 * this.gradient[var6 + 1], var12 * this.gradient[var7 + 0] + var14 * this.gradient[var7 + 1]), lerp(var18, var10 * this.gradient[var6 + 8 + 0] + var16 * this.gradient[var6 + 8 + 1], var12 * this.gradient[var7 + 8 + 0] + var16 * this.gradient[var7 + 8 + 1]));
         default:
      }
   }

   private final void noise2Stitch(double[] var1, double var2, double var4, StitchInfo var6) {
      double var15 = var2 + 4096.0;
      int var7 = (int)var15;
      int var8 = var7 + 1;
      if (var8 >= var6.wrapX) {
         if (var7 >= var6.wrapX) {
            var7 -= var6.width;
            var8 -= var6.width;
         } else {
            var8 -= var6.width;
         }
      }

      int var9 = this.latticeSelector[var7 & 255];
      int var10 = this.latticeSelector[var8 & 255];
      double var17 = var15 - (double)((int)var15);
      double var19 = var17 - 1.0;
      double var25 = s_curve(var17);
      var15 = var4 + 4096.0;
      var7 = (int)var15;
      var8 = var7 + 1;
      if (var8 >= var6.wrapY) {
         if (var7 >= var6.wrapY) {
            var7 -= var6.height;
            var8 -= var6.height;
         } else {
            var8 -= var6.height;
         }
      }

      int var11 = (var9 + var7 & 255) << 3;
      int var12 = (var10 + var7 & 255) << 3;
      int var13 = (var9 + var8 & 255) << 3;
      int var14 = (var10 + var8 & 255) << 3;
      double var21 = var15 - (double)((int)var15);
      double var23 = var21 - 1.0;
      double var27 = s_curve(var21);
      switch (this.channels.length) {
         case 4:
            var1[3] = lerp(var27, lerp(var25, var17 * this.gradient[var11 + 6] + var21 * this.gradient[var11 + 7], var19 * this.gradient[var12 + 6] + var21 * this.gradient[var12 + 7]), lerp(var25, var17 * this.gradient[var13 + 6] + var23 * this.gradient[var13 + 7], var19 * this.gradient[var14 + 6] + var23 * this.gradient[var14 + 7]));
         case 3:
            var1[2] = lerp(var27, lerp(var25, var17 * this.gradient[var11 + 4] + var21 * this.gradient[var11 + 5], var19 * this.gradient[var12 + 4] + var21 * this.gradient[var12 + 5]), lerp(var25, var17 * this.gradient[var13 + 4] + var23 * this.gradient[var13 + 5], var19 * this.gradient[var14 + 4] + var23 * this.gradient[var14 + 5]));
         case 2:
            var1[1] = lerp(var27, lerp(var25, var17 * this.gradient[var11 + 2] + var21 * this.gradient[var11 + 3], var19 * this.gradient[var12 + 2] + var21 * this.gradient[var12 + 3]), lerp(var25, var17 * this.gradient[var13 + 2] + var23 * this.gradient[var13 + 3], var19 * this.gradient[var14 + 2] + var23 * this.gradient[var14 + 3]));
         case 1:
            var1[0] = lerp(var27, lerp(var25, var17 * this.gradient[var11 + 0] + var21 * this.gradient[var11 + 1], var19 * this.gradient[var12 + 0] + var21 * this.gradient[var12 + 1]), lerp(var25, var17 * this.gradient[var13 + 0] + var23 * this.gradient[var13 + 1], var19 * this.gradient[var14 + 0] + var23 * this.gradient[var14 + 1]));
         default:
      }
   }

   private final int turbulence_4(double var1, double var3, double[] var5) {
      double var8 = 255.0;
      var1 *= this.baseFrequencyX;
      var3 *= this.baseFrequencyY;
      var5[0] = var5[1] = var5[2] = var5[3] = 0.0;

      int var10;
      int var11;
      for(int var14 = this.numOctaves; var14 > 0; --var14) {
         double var15 = var1 + 4096.0;
         int var12 = (int)var15 & 255;
         var10 = this.latticeSelector[var12];
         var11 = this.latticeSelector[var12 + 1];
         double var19 = var15 - (double)((int)var15);
         double var21 = var19 - 1.0;
         double var27 = s_curve(var19);
         double var17 = var3 + 4096.0;
         var12 = (int)var17 & 255;
         int var13 = var12 + 1 & 255;
         var13 = (var11 + var12 & 255) << 3;
         var12 = (var10 + var12 & 255) << 3;
         double var23 = var17 - (double)((int)var17);
         double var25 = var23 - 1.0;
         double var29 = s_curve(var23);
         double var6 = lerp(var29, lerp(var27, var19 * this.gradient[var12 + 0] + var23 * this.gradient[var12 + 1], var21 * this.gradient[var13 + 0] + var23 * this.gradient[var13 + 1]), lerp(var27, var19 * this.gradient[var12 + 8 + 0] + var25 * this.gradient[var12 + 8 + 1], var21 * this.gradient[var13 + 8 + 0] + var25 * this.gradient[var13 + 8 + 1]));
         if (var6 < 0.0) {
            var5[0] -= var6 * var8;
         } else {
            var5[0] += var6 * var8;
         }

         var6 = lerp(var29, lerp(var27, var19 * this.gradient[var12 + 2] + var23 * this.gradient[var12 + 3], var21 * this.gradient[var13 + 2] + var23 * this.gradient[var13 + 3]), lerp(var27, var19 * this.gradient[var12 + 8 + 2] + var25 * this.gradient[var12 + 8 + 3], var21 * this.gradient[var13 + 8 + 2] + var25 * this.gradient[var13 + 8 + 3]));
         if (var6 < 0.0) {
            var5[1] -= var6 * var8;
         } else {
            var5[1] += var6 * var8;
         }

         var6 = lerp(var29, lerp(var27, var19 * this.gradient[var12 + 4] + var23 * this.gradient[var12 + 5], var21 * this.gradient[var13 + 4] + var23 * this.gradient[var13 + 5]), lerp(var27, var19 * this.gradient[var12 + 8 + 4] + var25 * this.gradient[var12 + 8 + 5], var21 * this.gradient[var13 + 8 + 4] + var25 * this.gradient[var13 + 8 + 5]));
         if (var6 < 0.0) {
            var5[2] -= var6 * var8;
         } else {
            var5[2] += var6 * var8;
         }

         var6 = lerp(var29, lerp(var27, var19 * this.gradient[var12 + 6] + var23 * this.gradient[var12 + 7], var21 * this.gradient[var13 + 6] + var23 * this.gradient[var13 + 7]), lerp(var27, var19 * this.gradient[var12 + 8 + 6] + var25 * this.gradient[var12 + 8 + 7], var21 * this.gradient[var13 + 8 + 6] + var25 * this.gradient[var13 + 8 + 7]));
         if (var6 < 0.0) {
            var5[3] -= var6 * var8;
         } else {
            var5[3] += var6 * var8;
         }

         var8 *= 0.5;
         var1 *= 2.0;
         var3 *= 2.0;
      }

      var10 = (int)var5[0];
      if ((var10 & -256) == 0) {
         var11 = var10 << 16;
      } else {
         var11 = (var10 & Integer.MIN_VALUE) != 0 ? 0 : 16711680;
      }

      var10 = (int)var5[1];
      if ((var10 & -256) == 0) {
         var11 |= var10 << 8;
      } else {
         var11 |= (var10 & Integer.MIN_VALUE) != 0 ? 0 : '\uff00';
      }

      var10 = (int)var5[2];
      if ((var10 & -256) == 0) {
         var11 |= var10;
      } else {
         var11 |= (var10 & Integer.MIN_VALUE) != 0 ? 0 : 255;
      }

      var10 = (int)var5[3];
      if ((var10 & -256) == 0) {
         var11 |= var10 << 24;
      } else {
         var11 |= (var10 & Integer.MIN_VALUE) != 0 ? 0 : -16777216;
      }

      return var11;
   }

   private final void turbulence(int[] var1, double var2, double var4, double[] var6, double[] var7) {
      var6[0] = var6[1] = var6[2] = var6[3] = 0.0;
      double var8 = 255.0;
      var2 *= this.baseFrequencyX;
      var4 *= this.baseFrequencyY;
      int var10;
      switch (this.channels.length) {
         case 1:
            for(var10 = 0; var10 < this.numOctaves; ++var10) {
               this.noise2(var7, var2, var4);
               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var8;
               } else {
                  var6[0] += var7[0] * var8;
               }

               var8 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
            }

            var1[0] = (int)var6[0];
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
            break;
         case 2:
            for(var10 = 0; var10 < this.numOctaves; ++var10) {
               this.noise2(var7, var2, var4);
               if (var7[1] < 0.0) {
                  var6[1] -= var7[1] * var8;
               } else {
                  var6[1] += var7[1] * var8;
               }

               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var8;
               } else {
                  var6[0] += var7[0] * var8;
               }

               var8 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
            }

            var1[1] = (int)var6[1];
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[0] = (int)var6[0];
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
            break;
         case 3:
            for(var10 = 0; var10 < this.numOctaves; ++var10) {
               this.noise2(var7, var2, var4);
               if (var7[2] < 0.0) {
                  var6[2] -= var7[2] * var8;
               } else {
                  var6[2] += var7[2] * var8;
               }

               if (var7[1] < 0.0) {
                  var6[1] -= var7[1] * var8;
               } else {
                  var6[1] += var7[1] * var8;
               }

               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var8;
               } else {
                  var6[0] += var7[0] * var8;
               }

               var8 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
            }

            var1[2] = (int)var6[2];
            if ((var1[2] & -256) != 0) {
               var1[2] = (var1[2] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[1] = (int)var6[1];
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[0] = (int)var6[0];
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
            break;
         case 4:
            for(var10 = 0; var10 < this.numOctaves; ++var10) {
               this.noise2(var7, var2, var4);
               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var8;
               } else {
                  var6[0] += var7[0] * var8;
               }

               if (var7[1] < 0.0) {
                  var6[1] -= var7[1] * var8;
               } else {
                  var6[1] += var7[1] * var8;
               }

               if (var7[2] < 0.0) {
                  var6[2] -= var7[2] * var8;
               } else {
                  var6[2] += var7[2] * var8;
               }

               if (var7[3] < 0.0) {
                  var6[3] -= var7[3] * var8;
               } else {
                  var6[3] += var7[3] * var8;
               }

               var8 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
            }

            var1[0] = (int)var6[0];
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[1] = (int)var6[1];
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[2] = (int)var6[2];
            if ((var1[2] & -256) != 0) {
               var1[2] = (var1[2] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[3] = (int)var6[3];
            if ((var1[3] & -256) != 0) {
               var1[3] = (var1[3] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
      }

   }

   private final void turbulenceStitch(int[] var1, double var2, double var4, double[] var6, double[] var7, StitchInfo var8) {
      double var9 = 1.0;
      var2 *= this.baseFrequencyX;
      var4 *= this.baseFrequencyY;
      var6[0] = var6[1] = var6[2] = var6[3] = 0.0;
      int var11;
      switch (this.channels.length) {
         case 1:
            for(var11 = 0; var11 < this.numOctaves; ++var11) {
               this.noise2Stitch(var7, var2, var4, var8);
               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var9;
               } else {
                  var6[0] += var7[0] * var9;
               }

               var9 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
               var8.doubleFrequency();
            }

            var1[0] = (int)(var6[0] * 255.0);
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
            break;
         case 2:
            for(var11 = 0; var11 < this.numOctaves; ++var11) {
               this.noise2Stitch(var7, var2, var4, var8);
               if (var7[1] < 0.0) {
                  var6[1] -= var7[1] * var9;
               } else {
                  var6[1] += var7[1] * var9;
               }

               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var9;
               } else {
                  var6[0] += var7[0] * var9;
               }

               var9 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
               var8.doubleFrequency();
            }

            var1[1] = (int)(var6[1] * 255.0);
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[0] = (int)(var6[0] * 255.0);
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
            break;
         case 3:
            for(var11 = 0; var11 < this.numOctaves; ++var11) {
               this.noise2Stitch(var7, var2, var4, var8);
               if (var7[2] < 0.0) {
                  var6[2] -= var7[2] * var9;
               } else {
                  var6[2] += var7[2] * var9;
               }

               if (var7[1] < 0.0) {
                  var6[1] -= var7[1] * var9;
               } else {
                  var6[1] += var7[1] * var9;
               }

               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var9;
               } else {
                  var6[0] += var7[0] * var9;
               }

               var9 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
               var8.doubleFrequency();
            }

            var1[2] = (int)(var6[2] * 255.0);
            if ((var1[2] & -256) != 0) {
               var1[2] = (var1[2] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[1] = (int)(var6[1] * 255.0);
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[0] = (int)(var6[0] * 255.0);
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
            break;
         case 4:
            for(var11 = 0; var11 < this.numOctaves; ++var11) {
               this.noise2Stitch(var7, var2, var4, var8);
               if (var7[3] < 0.0) {
                  var6[3] -= var7[3] * var9;
               } else {
                  var6[3] += var7[3] * var9;
               }

               if (var7[2] < 0.0) {
                  var6[2] -= var7[2] * var9;
               } else {
                  var6[2] += var7[2] * var9;
               }

               if (var7[1] < 0.0) {
                  var6[1] -= var7[1] * var9;
               } else {
                  var6[1] += var7[1] * var9;
               }

               if (var7[0] < 0.0) {
                  var6[0] -= var7[0] * var9;
               } else {
                  var6[0] += var7[0] * var9;
               }

               var9 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
               var8.doubleFrequency();
            }

            var1[3] = (int)(var6[3] * 255.0);
            if ((var1[3] & -256) != 0) {
               var1[3] = (var1[3] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[2] = (int)(var6[2] * 255.0);
            if ((var1[2] & -256) != 0) {
               var1[2] = (var1[2] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[1] = (int)(var6[1] * 255.0);
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var1[0] = (int)(var6[0] * 255.0);
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
      }

   }

   private final int turbulenceFractal_4(double var1, double var3, double[] var5) {
      double var27 = 127.5;
      var1 *= this.baseFrequencyX;
      var3 *= this.baseFrequencyY;
      var5[0] = var5[1] = var5[2] = var5[3] = 127.5;

      int var9;
      int var10;
      for(int var8 = this.numOctaves; var8 > 0; --var8) {
         double var11 = var1 + 4096.0;
         int var6 = (int)var11 & 255;
         var9 = this.latticeSelector[var6];
         var10 = this.latticeSelector[var6 + 1];
         double var15 = var11 - (double)((int)var11);
         double var17 = var15 - 1.0;
         double var23 = s_curve(var15);
         double var13 = var3 + 4096.0;
         var6 = (int)var13 & 255;
         int var7 = var6 + 1 & 255;
         var7 = (var10 + var6 & 255) << 3;
         var6 = (var9 + var6 & 255) << 3;
         double var19 = var13 - (double)((int)var13);
         double var21 = var19 - 1.0;
         double var25 = s_curve(var19);
         var5[0] += lerp(var25, lerp(var23, var15 * this.gradient[var6 + 0] + var19 * this.gradient[var6 + 1], var17 * this.gradient[var7 + 0] + var19 * this.gradient[var7 + 1]), lerp(var23, var15 * this.gradient[var6 + 8 + 0] + var21 * this.gradient[var6 + 8 + 1], var17 * this.gradient[var7 + 8 + 0] + var21 * this.gradient[var7 + 8 + 1])) * var27;
         var5[1] += lerp(var25, lerp(var23, var15 * this.gradient[var6 + 2] + var19 * this.gradient[var6 + 3], var17 * this.gradient[var7 + 2] + var19 * this.gradient[var7 + 3]), lerp(var23, var15 * this.gradient[var6 + 8 + 2] + var21 * this.gradient[var6 + 8 + 3], var17 * this.gradient[var7 + 8 + 2] + var21 * this.gradient[var7 + 8 + 3])) * var27;
         var5[2] += lerp(var25, lerp(var23, var15 * this.gradient[var6 + 4] + var19 * this.gradient[var6 + 5], var17 * this.gradient[var7 + 4] + var19 * this.gradient[var7 + 5]), lerp(var23, var15 * this.gradient[var6 + 8 + 4] + var21 * this.gradient[var6 + 8 + 5], var17 * this.gradient[var7 + 8 + 4] + var21 * this.gradient[var7 + 8 + 5])) * var27;
         var5[3] += lerp(var25, lerp(var23, var15 * this.gradient[var6 + 6] + var19 * this.gradient[var6 + 7], var17 * this.gradient[var7 + 6] + var19 * this.gradient[var7 + 7]), lerp(var23, var15 * this.gradient[var6 + 8 + 6] + var21 * this.gradient[var6 + 8 + 7], var17 * this.gradient[var7 + 8 + 6] + var21 * this.gradient[var7 + 8 + 7])) * var27;
         var27 *= 0.5;
         var1 *= 2.0;
         var3 *= 2.0;
      }

      var9 = (int)var5[0];
      if ((var9 & -256) == 0) {
         var10 = var9 << 16;
      } else {
         var10 = (var9 & Integer.MIN_VALUE) != 0 ? 0 : 16711680;
      }

      var9 = (int)var5[1];
      if ((var9 & -256) == 0) {
         var10 |= var9 << 8;
      } else {
         var10 |= (var9 & Integer.MIN_VALUE) != 0 ? 0 : '\uff00';
      }

      var9 = (int)var5[2];
      if ((var9 & -256) == 0) {
         var10 |= var9;
      } else {
         var10 |= (var9 & Integer.MIN_VALUE) != 0 ? 0 : 255;
      }

      var9 = (int)var5[3];
      if ((var9 & -256) == 0) {
         var10 |= var9 << 24;
      } else {
         var10 |= (var9 & Integer.MIN_VALUE) != 0 ? 0 : -16777216;
      }

      return var10;
   }

   private final void turbulenceFractal(int[] var1, double var2, double var4, double[] var6, double[] var7) {
      double var8 = 127.5;
      var6[0] = var6[1] = var6[2] = var6[3] = 127.5;
      var2 *= this.baseFrequencyX;
      var4 *= this.baseFrequencyY;
      int var10 = this.numOctaves;

      while(var10 > 0) {
         this.noise2(var7, var2, var4);
         switch (this.channels.length) {
            case 4:
               var6[3] += var7[3] * var8;
            case 3:
               var6[2] += var7[2] * var8;
            case 2:
               var6[1] += var7[1] * var8;
            case 1:
               var6[0] += var7[0] * var8;
            default:
               var8 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
               --var10;
         }
      }

      switch (this.channels.length) {
         case 4:
            var1[3] = (int)var6[3];
            if ((var1[3] & -256) != 0) {
               var1[3] = (var1[3] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         case 3:
            var1[2] = (int)var6[2];
            if ((var1[2] & -256) != 0) {
               var1[2] = (var1[2] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         case 2:
            var1[1] = (int)var6[1];
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         case 1:
            var1[0] = (int)var6[0];
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         default:
      }
   }

   private final void turbulenceFractalStitch(int[] var1, double var2, double var4, double[] var6, double[] var7, StitchInfo var8) {
      double var9 = 127.5;
      var6[0] = var6[1] = var6[2] = var6[3] = 127.5;
      var2 *= this.baseFrequencyX;
      var4 *= this.baseFrequencyY;
      int var11 = this.numOctaves;

      while(var11 > 0) {
         this.noise2Stitch(var7, var2, var4, var8);
         switch (this.channels.length) {
            case 4:
               var6[3] += var7[3] * var9;
            case 3:
               var6[2] += var7[2] * var9;
            case 2:
               var6[1] += var7[1] * var9;
            case 1:
               var6[0] += var7[0] * var9;
            default:
               var9 *= 0.5;
               var2 *= 2.0;
               var4 *= 2.0;
               var8.doubleFrequency();
               --var11;
         }
      }

      switch (this.channels.length) {
         case 4:
            var1[3] = (int)var6[3];
            if ((var1[3] & -256) != 0) {
               var1[3] = (var1[3] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         case 3:
            var1[2] = (int)var6[2];
            if ((var1[2] & -256) != 0) {
               var1[2] = (var1[2] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         case 2:
            var1[1] = (int)var6[1];
            if ((var1[1] & -256) != 0) {
               var1[1] = (var1[1] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         case 1:
            var1[0] = (int)var6[0];
            if ((var1[0] & -256) != 0) {
               var1[0] = (var1[0] & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }
         default:
      }
   }

   public WritableRaster copyData(WritableRaster var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot generate a noise pattern into a null raster");
      } else {
         int var2 = var1.getWidth();
         int var3 = var1.getHeight();
         DataBufferInt var4 = (DataBufferInt)var1.getDataBuffer();
         int var6 = var1.getMinX();
         int var7 = var1.getMinY();
         SinglePixelPackedSampleModel var5 = (SinglePixelPackedSampleModel)var1.getSampleModel();
         int var8 = var4.getOffset() + var5.getOffset(var6 - var1.getSampleModelTranslateX(), var7 - var1.getSampleModelTranslateY());
         int[] var9 = var4.getBankData()[0];
         int var10 = var5.getScanlineStride() - var2;
         int var13 = var8;
         int[] var14 = new int[4];
         double[] var15 = new double[]{0.0, 0.0, 0.0, 0.0};
         double[] var16 = new double[]{0.0, 0.0, 0.0, 0.0};
         double var17 = this.tx[0];
         double var19 = this.tx[1];
         double var21 = this.ty[0] - (double)var2 * var17;
         double var23 = this.ty[1] - (double)var2 * var19;
         double[] var25 = new double[]{(double)var6, (double)var7};
         this.txf.transform(var25, 0, var25, 0, 1);
         double var26 = var25[0];
         double var28 = var25[1];
         int var11;
         int var12;
         StitchInfo var30;
         if (this.isFractalNoise) {
            if (this.stitchInfo == null) {
               if (this.channels.length == 4) {
                  for(var11 = 0; var11 < var3; ++var11) {
                     for(var12 = var13 + var2; var13 < var12; ++var13) {
                        var9[var13] = this.turbulenceFractal_4(var26, var28, var15);
                        var26 += var17;
                        var28 += var19;
                     }

                     var26 += var21;
                     var28 += var23;
                     var13 += var10;
                  }
               } else {
                  for(var11 = 0; var11 < var3; ++var11) {
                     for(var12 = var13 + var2; var13 < var12; ++var13) {
                        this.turbulenceFractal(var14, var26, var28, var15, var16);
                        var9[var13] = var14[3] << 24 | var14[0] << 16 | var14[1] << 8 | var14[2];
                        var26 += var17;
                        var28 += var19;
                     }

                     var26 += var21;
                     var28 += var23;
                     var13 += var10;
                  }
               }
            } else {
               var30 = new StitchInfo();

               for(var11 = 0; var11 < var3; ++var11) {
                  for(var12 = var13 + var2; var13 < var12; ++var13) {
                     var30.assign(this.stitchInfo);
                     this.turbulenceFractalStitch(var14, var26, var28, var15, var16, var30);
                     var9[var13] = var14[3] << 24 | var14[0] << 16 | var14[1] << 8 | var14[2];
                     var26 += var17;
                     var28 += var19;
                  }

                  var26 += var21;
                  var28 += var23;
                  var13 += var10;
               }
            }
         } else if (this.stitchInfo == null) {
            if (this.channels.length == 4) {
               for(var11 = 0; var11 < var3; ++var11) {
                  for(var12 = var13 + var2; var13 < var12; ++var13) {
                     var9[var13] = this.turbulence_4(var26, var28, var15);
                     var26 += var17;
                     var28 += var19;
                  }

                  var26 += var21;
                  var28 += var23;
                  var13 += var10;
               }
            } else {
               for(var11 = 0; var11 < var3; ++var11) {
                  for(var12 = var13 + var2; var13 < var12; ++var13) {
                     this.turbulence(var14, var26, var28, var15, var16);
                     var9[var13] = var14[3] << 24 | var14[0] << 16 | var14[1] << 8 | var14[2];
                     var26 += var17;
                     var28 += var19;
                  }

                  var26 += var21;
                  var28 += var23;
                  var13 += var10;
               }
            }
         } else {
            var30 = new StitchInfo();

            for(var11 = 0; var11 < var3; ++var11) {
               for(var12 = var13 + var2; var13 < var12; ++var13) {
                  var30.assign(this.stitchInfo);
                  this.turbulenceStitch(var14, var26, var28, var15, var16, var30);
                  var9[var13] = var14[3] << 24 | var14[0] << 16 | var14[1] << 8 | var14[2];
                  var26 += var17;
                  var28 += var19;
               }

               var26 += var21;
               var28 += var23;
               var13 += var10;
            }
         }

         return var1;
      }
   }

   public TurbulencePatternRed(double var1, double var3, int var5, int var6, boolean var7, Rectangle2D var8, AffineTransform var9, Rectangle var10, ColorSpace var11, boolean var12) {
      this.baseFrequencyX = var1;
      this.baseFrequencyY = var3;
      this.seed = var6;
      this.isFractalNoise = var7;
      this.tile = var8;
      this.txf = var9;
      if (this.txf == null) {
         this.txf = IDENTITY;
      }

      int var13 = var11.getNumComponents();
      if (var12) {
         ++var13;
      }

      this.channels = new int[var13];

      for(int var14 = 0; var14 < this.channels.length; this.channels[var14] = var14++) {
      }

      var9.deltaTransform(this.tx, 0, this.tx, 0, 1);
      var9.deltaTransform(this.ty, 0, this.ty, 0, 1);
      double[] var26 = new double[]{0.5, 0.0};
      double[] var15 = new double[]{0.0, 0.5};
      var9.deltaTransform(var26, 0, var26, 0, 1);
      var9.deltaTransform(var15, 0, var15, 0, 1);
      double var16 = Math.max(Math.abs(var26[0]), Math.abs(var15[0]));
      int var18 = -((int)Math.round((Math.log(var16) + Math.log(var1)) / Math.log(2.0)));
      double var19 = Math.max(Math.abs(var26[1]), Math.abs(var15[1]));
      int var21 = -((int)Math.round((Math.log(var19) + Math.log(var3)) / Math.log(2.0)));
      this.numOctaves = var5 > var18 ? var18 : var5;
      this.numOctaves = this.numOctaves > var21 ? var21 : this.numOctaves;
      if (this.numOctaves < 1 && var5 > 1) {
         this.numOctaves = 1;
      }

      if (this.numOctaves > 8) {
         this.numOctaves = 8;
      }

      if (var8 != null) {
         double var22 = Math.floor(var8.getWidth() * var1) / var8.getWidth();
         double var24 = Math.ceil(var8.getWidth() * var1) / var8.getWidth();
         if (var1 / var22 < var24 / var1) {
            this.baseFrequencyX = var22;
         } else {
            this.baseFrequencyX = var24;
         }

         var22 = Math.floor(var8.getHeight() * var3) / var8.getHeight();
         var24 = Math.ceil(var8.getHeight() * var3) / var8.getHeight();
         if (var3 / var22 < var24 / var3) {
            this.baseFrequencyY = var22;
         } else {
            this.baseFrequencyY = var24;
         }

         this.stitchInfo = new StitchInfo();
         this.stitchInfo.width = (int)(var8.getWidth() * this.baseFrequencyX);
         this.stitchInfo.height = (int)(var8.getHeight() * this.baseFrequencyY);
         this.stitchInfo.wrapX = (int)(var8.getX() * this.baseFrequencyX + 4096.0 + (double)this.stitchInfo.width);
         this.stitchInfo.wrapY = (int)(var8.getY() * this.baseFrequencyY + 4096.0 + (double)this.stitchInfo.height);
         if (this.stitchInfo.width == 0) {
            this.stitchInfo.width = 1;
         }

         if (this.stitchInfo.height == 0) {
            this.stitchInfo.height = 1;
         }
      }

      this.initLattice(var6);
      DirectColorModel var27;
      if (var12) {
         var27 = new DirectColorModel(var11, 32, 16711680, 65280, 255, -16777216, false, 3);
      } else {
         var27 = new DirectColorModel(var11, 24, 16711680, 65280, 255, 0, false, 3);
      }

      int var23 = AbstractTiledRed.getDefaultTileSize();
      this.init((CachableRed)null, var10, var27, var27.createCompatibleSampleModel(var23, var23), 0, 0, (Map)null);
   }

   static final class StitchInfo {
      int width;
      int height;
      int wrapX;
      int wrapY;

      StitchInfo() {
      }

      StitchInfo(StitchInfo var1) {
         this.width = var1.width;
         this.height = var1.height;
         this.wrapX = var1.wrapX;
         this.wrapY = var1.wrapY;
      }

      final void assign(StitchInfo var1) {
         this.width = var1.width;
         this.height = var1.height;
         this.wrapX = var1.wrapX;
         this.wrapY = var1.wrapY;
      }

      final void doubleFrequency() {
         this.width *= 2;
         this.height *= 2;
         this.wrapX *= 2;
         this.wrapY *= 2;
         this.wrapX = (int)((double)this.wrapX - 4096.0);
         this.wrapY = (int)((double)this.wrapY - 4096.0);
      }
   }
}
