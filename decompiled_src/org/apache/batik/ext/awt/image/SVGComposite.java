package org.apache.batik.ext.awt.image;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.PackedColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

public class SVGComposite implements Composite {
   public static final SVGComposite OVER;
   public static final SVGComposite IN;
   public static final SVGComposite OUT;
   public static final SVGComposite ATOP;
   public static final SVGComposite XOR;
   public static final SVGComposite MULTIPLY;
   public static final SVGComposite SCREEN;
   public static final SVGComposite DARKEN;
   public static final SVGComposite LIGHTEN;
   CompositeRule rule;

   public CompositeRule getRule() {
      return this.rule;
   }

   public SVGComposite(CompositeRule var1) {
      this.rule = var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SVGComposite) {
         SVGComposite var3 = (SVGComposite)var1;
         return var3.getRule() == this.getRule();
      } else if (var1 instanceof AlphaComposite) {
         AlphaComposite var2 = (AlphaComposite)var1;
         switch (this.getRule().getRule()) {
            case 1:
               return var2 == AlphaComposite.SrcOver;
            case 2:
               return var2 == AlphaComposite.SrcIn;
            case 3:
               return var2 == AlphaComposite.SrcOut;
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   public boolean is_INT_PACK(ColorModel var1) {
      if (!(var1 instanceof PackedColorModel)) {
         return false;
      } else {
         PackedColorModel var2 = (PackedColorModel)var1;
         int[] var3 = var2.getMasks();
         if (var3.length != 4) {
            return false;
         } else if (var3[0] != 16711680) {
            return false;
         } else if (var3[1] != 65280) {
            return false;
         } else if (var3[2] != 255) {
            return false;
         } else {
            return var3[3] == -16777216;
         }
      }
   }

   public CompositeContext createContext(ColorModel var1, ColorModel var2, RenderingHints var3) {
      boolean var4 = this.is_INT_PACK(var1) && this.is_INT_PACK(var2);
      switch (this.rule.getRule()) {
         case 1:
            if (!var2.hasAlpha()) {
               if (var4) {
                  return new OverCompositeContext_INT_PACK_NA(var1, var2);
               }

               return new OverCompositeContext_NA(var1, var2);
            } else if (!var4) {
               return new OverCompositeContext(var1, var2);
            } else {
               if (var1.isAlphaPremultiplied()) {
                  return new OverCompositeContext_INT_PACK(var1, var2);
               }

               return new OverCompositeContext_INT_PACK_UNPRE(var1, var2);
            }
         case 2:
            if (var4) {
               return new InCompositeContext_INT_PACK(var1, var2);
            }

            return new InCompositeContext(var1, var2);
         case 3:
            if (var4) {
               return new OutCompositeContext_INT_PACK(var1, var2);
            }

            return new OutCompositeContext(var1, var2);
         case 4:
            if (var4) {
               return new AtopCompositeContext_INT_PACK(var1, var2);
            }

            return new AtopCompositeContext(var1, var2);
         case 5:
            if (var4) {
               return new XorCompositeContext_INT_PACK(var1, var2);
            }

            return new XorCompositeContext(var1, var2);
         case 6:
            float[] var5 = this.rule.getCoefficients();
            if (var4) {
               return new ArithCompositeContext_INT_PACK_LUT(var1, var2, var5[0], var5[1], var5[2], var5[3]);
            }

            return new ArithCompositeContext(var1, var2, var5[0], var5[1], var5[2], var5[3]);
         case 7:
            if (var4) {
               return new MultiplyCompositeContext_INT_PACK(var1, var2);
            }

            return new MultiplyCompositeContext(var1, var2);
         case 8:
            if (var4) {
               return new ScreenCompositeContext_INT_PACK(var1, var2);
            }

            return new ScreenCompositeContext(var1, var2);
         case 9:
            if (var4) {
               return new DarkenCompositeContext_INT_PACK(var1, var2);
            }

            return new DarkenCompositeContext(var1, var2);
         case 10:
            if (var4) {
               return new LightenCompositeContext_INT_PACK(var1, var2);
            }

            return new LightenCompositeContext(var1, var2);
         default:
            throw new UnsupportedOperationException("Unknown composite rule requested.");
      }
   }

   static {
      OVER = new SVGComposite(CompositeRule.OVER);
      IN = new SVGComposite(CompositeRule.IN);
      OUT = new SVGComposite(CompositeRule.OUT);
      ATOP = new SVGComposite(CompositeRule.ATOP);
      XOR = new SVGComposite(CompositeRule.XOR);
      MULTIPLY = new SVGComposite(CompositeRule.MULTIPLY);
      SCREEN = new SVGComposite(CompositeRule.SCREEN);
      DARKEN = new SVGComposite(CompositeRule.DARKEN);
      LIGHTEN = new SVGComposite(CompositeRule.LIGHTEN);
   }

   public static class LightenCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      LightenCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var25 = 0; var25 < var2; ++var25) {
            int var18;
            int var19;
            int var20;
            int var21;
            for(int var26 = var11 + var1; var11 < var26; var9[var11++] = var18 << 24 | var19 << 16 | var20 << 8 | var21) {
               int var14 = var3[var5++];
               int var16 = var6[var8++];
               int var22 = var14 >>> 24;
               int var23 = var16 >>> 24;
               int var15 = (255 - var23) * 65793;
               int var17 = (255 - var22) * 65793;
               var18 = var22 + var23 - (var22 * var23 * 65793 + 8388608 >>> 24);
               var22 = var14 >> 16 & 255;
               var23 = var16 >> 16 & 255;
               var19 = (var15 * var22 + 8388608 >>> 24) + var23;
               int var24 = (var17 * var23 + 8388608 >>> 24) + var22;
               if (var19 < var24) {
                  var19 = var24;
               }

               var22 = var14 >> 8 & 255;
               var23 = var16 >> 8 & 255;
               var20 = (var15 * var22 + 8388608 >>> 24) + var23;
               var24 = (var17 * var23 + 8388608 >>> 24) + var22;
               if (var20 < var24) {
                  var20 = var24;
               }

               var22 = var14 & 255;
               var23 = var16 & 255;
               var21 = (var15 * var22 + 8388608 >>> 24) + var23;
               var24 = (var17 * var23 + 8388608 >>> 24) + var22;
               if (var21 < var24) {
                  var21 = var24;
               }

               var18 &= 255;
               var19 &= 255;
               var20 &= 255;
               var21 &= 255;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class LightenCompositeContext extends AlphaPreCompositeContext {
      LightenCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var17 = var8; var17 < var9; ++var17) {
            var4 = var1.getPixels(var6, var17, var7, 1, var4);
            var5 = var2.getPixels(var6, var17, var7, 1, var5);
            int var12 = 0;

            for(int var18 = var7 * 4; var12 < var18; ++var12) {
               int var13 = 255 - var5[var12 + 3];
               int var14 = 255 - var4[var12 + 3];
               int var15 = (var13 * var4[var12] * 65793 + 8388608 >>> 24) + var5[var12];
               int var16 = (var14 * var5[var12] * 65793 + 8388608 >>> 24) + var4[var12];
               if (var15 > var16) {
                  var5[var12] = var15;
               } else {
                  var5[var12] = var16;
               }

               ++var12;
               var15 = (var13 * var4[var12] * 65793 + 8388608 >>> 24) + var5[var12];
               var16 = (var14 * var5[var12] * 65793 + 8388608 >>> 24) + var4[var12];
               if (var15 > var16) {
                  var5[var12] = var15;
               } else {
                  var5[var12] = var16;
               }

               ++var12;
               var15 = (var13 * var4[var12] * 65793 + 8388608 >>> 24) + var5[var12];
               var16 = (var14 * var5[var12] * 65793 + 8388608 >>> 24) + var4[var12];
               if (var15 > var16) {
                  var5[var12] = var15;
               } else {
                  var5[var12] = var16;
               }

               ++var12;
               var5[var12] = var4[var12] + var5[var12] - (var5[var12] * var4[var12] * 65793 + 8388608 >>> 24);
            }

            var3.setPixels(var6, var17, var7, 1, var5);
         }

      }
   }

   public static class DarkenCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      DarkenCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var25 = 0; var25 < var2; ++var25) {
            int var18;
            int var19;
            int var20;
            int var21;
            for(int var26 = var11 + var1; var11 < var26; var9[var11++] = var18 << 24 | var19 << 16 | var20 << 8 | var21) {
               int var14 = var3[var5++];
               int var16 = var6[var8++];
               int var22 = var14 >>> 24;
               int var23 = var16 >>> 24;
               int var15 = (255 - var23) * 65793;
               int var17 = (255 - var22) * 65793;
               var18 = var22 + var23 - (var22 * var23 * 65793 + 8388608 >>> 24);
               var22 = var14 >> 16 & 255;
               var23 = var16 >> 16 & 255;
               var19 = (var15 * var22 + 8388608 >>> 24) + var23;
               int var24 = (var17 * var23 + 8388608 >>> 24) + var22;
               if (var19 > var24) {
                  var19 = var24;
               }

               var22 = var14 >> 8 & 255;
               var23 = var16 >> 8 & 255;
               var20 = (var15 * var22 + 8388608 >>> 24) + var23;
               var24 = (var17 * var23 + 8388608 >>> 24) + var22;
               if (var20 > var24) {
                  var20 = var24;
               }

               var22 = var14 & 255;
               var23 = var16 & 255;
               var21 = (var15 * var22 + 8388608 >>> 24) + var23;
               var24 = (var17 * var23 + 8388608 >>> 24) + var22;
               if (var21 > var24) {
                  var21 = var24;
               }

               var18 &= 255;
               var19 &= 255;
               var20 &= 255;
               var21 &= 255;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class DarkenCompositeContext extends AlphaPreCompositeContext {
      DarkenCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var17 = var8; var17 < var9; ++var17) {
            var4 = var1.getPixels(var6, var17, var7, 1, var4);
            var5 = var2.getPixels(var6, var17, var7, 1, var5);
            int var12 = 0;

            for(int var18 = var7 * 4; var12 < var18; ++var12) {
               int var13 = 255 - var5[var12 + 3];
               int var14 = 255 - var4[var12 + 3];
               int var15 = (var13 * var4[var12] * 65793 + 8388608 >>> 24) + var5[var12];
               int var16 = (var14 * var5[var12] * 65793 + 8388608 >>> 24) + var4[var12];
               if (var15 > var16) {
                  var5[var12] = var16;
               } else {
                  var5[var12] = var15;
               }

               ++var12;
               var15 = (var13 * var4[var12] * 65793 + 8388608 >>> 24) + var5[var12];
               var16 = (var14 * var5[var12] * 65793 + 8388608 >>> 24) + var4[var12];
               if (var15 > var16) {
                  var5[var12] = var16;
               } else {
                  var5[var12] = var15;
               }

               ++var12;
               var15 = (var13 * var4[var12] * 65793 + 8388608 >>> 24) + var5[var12];
               var16 = (var14 * var5[var12] * 65793 + 8388608 >>> 24) + var4[var12];
               if (var15 > var16) {
                  var5[var12] = var16;
               } else {
                  var5[var12] = var15;
               }

               ++var12;
               var5[var12] = var4[var12] + var5[var12] - (var5[var12] * var4[var12] * 65793 + 8388608 >>> 24);
            }

            var3.setPixels(var6, var17, var7, 1, var5);
         }

      }
   }

   public static class ScreenCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      ScreenCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var24 = 0; var24 < var2; ++var24) {
            int var15;
            int var16;
            int var17;
            int var18;
            int var20;
            int var21;
            int var22;
            int var23;
            for(int var25 = var11 + var1; var11 < var25; var9[var11++] = var16 + var21 - (var16 * var21 * 65793 + 8388608 >>> 24) << 16 | var17 + var22 - (var17 * var22 * 65793 + 8388608 >>> 24) << 8 | var18 + var23 - (var18 * var23 * 65793 + 8388608 >>> 24) | var15 + var20 - (var15 * var20 * 65793 + 8388608 >>> 24) << 24) {
               int var14 = var3[var5++];
               int var19 = var6[var8++];
               var15 = var14 >>> 24;
               var20 = var19 >>> 24;
               var16 = var14 >> 16 & 255;
               var21 = var19 >> 16 & 255;
               var17 = var14 >> 8 & 255;
               var22 = var19 >> 8 & 255;
               var18 = var14 & 255;
               var23 = var19 & 255;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class ScreenCompositeContext extends AlphaPreCompositeContext {
      ScreenCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var12 = var8; var12 < var9; ++var12) {
            var4 = var1.getPixels(var6, var12, var7, 1, var4);
            var5 = var2.getPixels(var6, var12, var7, 1, var5);
            int var13 = 0;

            for(int var14 = var7 * 4; var13 < var14; ++var13) {
               int var15 = var4[var13];
               int var16 = var5[var13];
               var5[var13] = var15 + var16 - (var16 * var15 * 65793 + 8388608 >>> 24);
               ++var13;
               var15 = var4[var13];
               var16 = var5[var13];
               var5[var13] = var15 + var16 - (var16 * var15 * 65793 + 8388608 >>> 24);
               ++var13;
               var15 = var4[var13];
               var16 = var5[var13];
               var5[var13] = var15 + var16 - (var16 * var15 * 65793 + 8388608 >>> 24);
               ++var13;
               var15 = var4[var13];
               var16 = var5[var13];
               var5[var13] = var15 + var16 - (var16 * var15 * 65793 + 8388608 >>> 24);
            }

            var3.setPixels(var6, var12, var7, 1, var5);
         }

      }
   }

   public static class MultiplyCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      MultiplyCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var26 = 0; var26 < var2; ++var26) {
            int var15;
            int var16;
            int var17;
            int var18;
            int var19;
            int var21;
            int var22;
            int var23;
            int var24;
            int var25;
            for(int var27 = var11 + var1; var11 < var27; var9[var11++] = ((var16 * var19 + var22 * var25 + var16 * var22) * 65793 + 8388608 & -16777216) >>> 8 | ((var17 * var19 + var23 * var25 + var17 * var23) * 65793 + 8388608 & -16777216) >>> 16 | (var18 * var19 + var24 * var25 + var18 * var24) * 65793 + 8388608 >>> 24 | var15 + var21 - (var15 * var21 * 65793 + 8388608 >>> 24) << 24) {
               int var14 = var3[var5++];
               int var20 = var6[var8++];
               var15 = var14 >>> 24;
               var21 = var20 >>> 24;
               var16 = var14 >> 16 & 255;
               var22 = var20 >> 16 & 255;
               var17 = var14 >> 8 & 255;
               var23 = var20 >> 8 & 255;
               var18 = var14 & 255;
               var24 = var20 & 255;
               var19 = 255 - var21;
               var25 = 255 - var15;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class MultiplyCompositeContext extends AlphaPreCompositeContext {
      MultiplyCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var14 = var8; var14 < var9; ++var14) {
            var4 = var1.getPixels(var6, var14, var7, 1, var4);
            var5 = var2.getPixels(var6, var14, var7, 1, var5);
            int var15 = 0;

            for(int var16 = var7 * 4; var15 < var16; ++var15) {
               int var12 = 255 - var5[var15 + 3];
               int var13 = 255 - var4[var15 + 3];
               var5[var15] = (var4[var15] * var12 + var5[var15] * var13 + var4[var15] * var5[var15]) * 65793 + 8388608 >>> 24;
               ++var15;
               var5[var15] = (var4[var15] * var12 + var5[var15] * var13 + var4[var15] * var5[var15]) * 65793 + 8388608 >>> 24;
               ++var15;
               var5[var15] = (var4[var15] * var12 + var5[var15] * var13 + var4[var15] * var5[var15]) * 65793 + 8388608 >>> 24;
               ++var15;
               var5[var15] = var4[var15] + var5[var15] - (var5[var15] * var4[var15] * 65793 + 8388608 >>> 24);
            }

            var3.setPixels(var6, var14, var7, 1, var5);
         }

      }
   }

   public static class ArithCompositeContext_INT_PACK_LUT extends AlphaPreCompositeContext_INT_PACK {
      byte[] lut;

      ArithCompositeContext_INT_PACK_LUT(ColorModel var1, ColorModel var2, float var3, float var4, float var5, float var6) {
         super(var1, var2);
         var3 /= 255.0F;
         var6 = var6 * 255.0F + 0.5F;
         int var7 = 65536;
         this.lut = new byte[var7];

         for(int var9 = 0; var9 < var7; ++var9) {
            int var8 = (int)((float)((var9 >> 8) * (var9 & 255)) * var3 + (float)(var9 >> 8) * var4 + (float)(var9 & 255) * var5 + var6);
            if ((var8 & -256) != 0) {
               if ((var8 & Integer.MIN_VALUE) != 0) {
                  var8 = 0;
               } else {
                  var8 = 255;
               }
            }

            this.lut[var9] = (byte)var8;
         }

      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         byte[] var12 = this.lut;

         for(int var15 = 0; var15 < var2; ++var15) {
            int var17;
            int var18;
            int var19;
            int var20;
            for(int var16 = var11 + var1; var11 < var16; var9[var11++] = var17 << 24 | var18 << 16 | var19 << 8 | var20) {
               int var13 = var3[var5++];
               int var14 = var6[var8++];
               var17 = 255 & var12[var13 >> 16 & '\uff00' | var14 >>> 24];
               var18 = 255 & var12[var13 >> 8 & '\uff00' | var14 >> 16 & 255];
               var19 = 255 & var12[var13 & '\uff00' | var14 >> 8 & 255];
               var20 = 255 & var12[var13 << 8 & '\uff00' | var14 & 255];
               if (var18 > var17) {
                  var17 = var18;
               }

               if (var19 > var17) {
                  var17 = var19;
               }

               if (var20 > var17) {
                  var17 = var20;
               }
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class ArithCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      float k1;
      float k2;
      float k3;
      float k4;

      ArithCompositeContext_INT_PACK(ColorModel var1, ColorModel var2, float var3, float var4, float var5, float var6) {
         super(var1, var2);
         this.k1 = var3 / 255.0F;
         this.k2 = var4;
         this.k3 = var5;
         this.k4 = var6 * 255.0F + 0.5F;
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var18 = 0; var18 < var2; ++var18) {
            int var14;
            int var15;
            int var16;
            int var17;
            for(int var19 = var11 + var1; var11 < var19; var9[var11++] = var14 << 24 | var15 << 16 | var16 << 8 | var17) {
               int var12 = var3[var5++];
               int var13 = var6[var8++];
               var14 = (int)((float)((var12 >>> 24) * (var13 >>> 24)) * this.k1 + (float)(var12 >>> 24) * this.k2 + (float)(var13 >>> 24) * this.k3 + this.k4);
               if ((var14 & -256) != 0) {
                  if ((var14 & Integer.MIN_VALUE) != 0) {
                     var14 = 0;
                  } else {
                     var14 = 255;
                  }
               }

               var15 = (int)((float)((var12 >> 16 & 255) * (var13 >> 16 & 255)) * this.k1 + (float)(var12 >> 16 & 255) * this.k2 + (float)(var13 >> 16 & 255) * this.k3 + this.k4);
               if ((var15 & -256) != 0) {
                  if ((var15 & Integer.MIN_VALUE) != 0) {
                     var15 = 0;
                  } else {
                     var15 = 255;
                  }
               }

               if (var14 < var15) {
                  var14 = var15;
               }

               var16 = (int)((float)((var12 >> 8 & 255) * (var13 >> 8 & 255)) * this.k1 + (float)(var12 >> 8 & 255) * this.k2 + (float)(var13 >> 8 & 255) * this.k3 + this.k4);
               if ((var16 & -256) != 0) {
                  if ((var16 & Integer.MIN_VALUE) != 0) {
                     var16 = 0;
                  } else {
                     var16 = 255;
                  }
               }

               if (var14 < var16) {
                  var14 = var16;
               }

               var17 = (int)((float)((var12 & 255) * (var13 & 255)) * this.k1 + (float)(var12 & 255) * this.k2 + (float)(var13 & 255) * this.k3 + this.k4);
               if ((var17 & -256) != 0) {
                  if ((var17 & Integer.MIN_VALUE) != 0) {
                     var17 = 0;
                  } else {
                     var17 = 255;
                  }
               }

               if (var14 < var17) {
                  var14 = var17;
               }
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class ArithCompositeContext extends AlphaPreCompositeContext {
      float k1;
      float k2;
      float k3;
      float k4;

      ArithCompositeContext(ColorModel var1, ColorModel var2, float var3, float var4, float var5, float var6) {
         super(var1, var2);
         this.k1 = var3;
         this.k2 = var4;
         this.k3 = var5;
         this.k4 = var6;
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getNumBands();
         int var9 = var3.getMinY();
         int var10 = var9 + var3.getHeight();
         float var11 = this.k1 / 255.0F;
         float var12 = this.k4 * 255.0F + 0.5F;

         for(int var13 = var9; var13 < var10; ++var13) {
            var4 = var1.getPixels(var6, var13, var7, 1, var4);
            var5 = var2.getPixels(var6, var13, var7, 1, var5);

            for(int var14 = 0; var14 < var4.length; ++var14) {
               int var17 = 0;

               int var16;
               for(int var15 = 1; var15 < var8; ++var14) {
                  var16 = (int)(var11 * (float)var4[var14] * (float)var5[var14] + this.k2 * (float)var4[var14] + this.k3 * (float)var5[var14] + var12);
                  if ((var16 & -256) != 0) {
                     if ((var16 & Integer.MIN_VALUE) != 0) {
                        var16 = 0;
                     } else {
                        var16 = 255;
                     }
                  }

                  if (var16 > var17) {
                     var17 = var16;
                  }

                  var5[var14] = var16;
                  ++var15;
               }

               var16 = (int)(var11 * (float)var4[var14] * (float)var5[var14] + this.k2 * (float)var4[var14] + this.k3 * (float)var5[var14] + var12);
               if ((var16 & -256) != 0) {
                  if ((var16 & Integer.MIN_VALUE) != 0) {
                     var16 = 0;
                  } else {
                     var16 = 255;
                  }
               }

               if (var16 > var17) {
                  var5[var14] = var16;
               } else {
                  var5[var14] = var17;
               }
            }

            var3.setPixels(var6, var13, var7, 1, var5);
         }

      }
   }

   public static class XorCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      XorCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var18 = 0; var18 < var2; ++var18) {
            int var14;
            int var15;
            int var16;
            int var17;
            for(int var19 = var11 + var1; var11 < var19; var9[var11++] = (var14 >>> 24) * var15 + (var16 >>> 24) * var17 + 8388608 & -16777216 | ((var14 >> 16 & 255) * var15 + (var16 >> 16 & 255) * var17 + 8388608 & -16777216) >>> 8 | ((var14 >> 8 & 255) * var15 + (var16 >> 8 & 255) * var17 + 8388608 & -16777216) >>> 16 | (var14 & 255) * var15 + (var16 & 255) * var17 + 8388608 >>> 24) {
               var14 = var3[var5++];
               var16 = var6[var8++];
               var15 = (255 - (var16 >>> 24)) * 65793;
               var17 = (255 - (var14 >>> 24)) * 65793;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class XorCompositeContext extends AlphaPreCompositeContext {
      XorCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var12 = var8; var12 < var9; ++var12) {
            var4 = var1.getPixels(var6, var12, var7, 1, var4);
            var5 = var2.getPixels(var6, var12, var7, 1, var5);
            int var13 = 0;

            for(int var14 = var7 * 4; var13 < var14; ++var13) {
               int var15 = (255 - var5[var13 + 3]) * 65793;
               int var16 = (255 - var4[var13 + 3]) * 65793;
               var5[var13] = var4[var13] * var15 + var5[var13] * var16 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + var5[var13] * var16 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + var5[var13] * var16 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + var5[var13] * var16 + 8388608 >>> 24;
            }

            var3.setPixels(var6, var12, var7, 1, var5);
         }

      }
   }

   public static class AtopCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      AtopCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var18 = 0; var18 < var2; ++var18) {
            int var14;
            int var15;
            int var16;
            int var17;
            for(int var19 = var11 + var1; var11 < var19; var9[var11++] = var16 & -16777216 | ((var14 >> 16 & 255) * var15 + (var16 >> 16 & 255) * var17 + 8388608 & -16777216) >>> 8 | ((var14 >> 8 & 255) * var15 + (var16 >> 8 & 255) * var17 + 8388608 & -16777216) >>> 16 | (var14 & 255) * var15 + (var16 & 255) * var17 + 8388608 >>> 24) {
               var14 = var3[var5++];
               var16 = var6[var8++];
               var15 = (var16 >>> 24) * 65793;
               var17 = (255 - (var14 >>> 24)) * 65793;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class AtopCompositeContext extends AlphaPreCompositeContext {
      AtopCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var12 = var8; var12 < var9; ++var12) {
            var4 = var1.getPixels(var6, var12, var7, 1, var4);
            var5 = var2.getPixels(var6, var12, var7, 1, var5);
            int var13 = 0;

            for(int var14 = var7 * 4; var13 < var14; var13 += 2) {
               int var15 = var5[var13 + 3] * 65793;
               int var16 = (255 - var4[var13 + 3]) * 65793;
               var5[var13] = var4[var13] * var15 + var5[var13] * var16 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + var5[var13] * var16 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + var5[var13] * var16 + 8388608 >>> 24;
            }

            var3.setPixels(var6, var12, var7, 1, var5);
         }

      }
   }

   public static class OutCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      OutCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var16 = 0; var16 < var2; ++var16) {
            int var14;
            int var15;
            for(int var17 = var11 + var1; var11 < var17; var9[var11++] = (var14 >>> 24) * var15 + 8388608 & -16777216 | ((var14 >> 16 & 255) * var15 + 8388608 & -16777216) >>> 8 | ((var14 >> 8 & 255) * var15 + 8388608 & -16777216) >>> 16 | (var14 & 255) * var15 + 8388608 >>> 24) {
               var15 = (255 - (var6[var8++] >>> 24)) * 65793;
               var14 = var3[var5++];
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class OutCompositeContext extends AlphaPreCompositeContext {
      OutCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var12 = var8; var12 < var9; ++var12) {
            var4 = var1.getPixels(var6, var12, var7, 1, var4);
            var5 = var2.getPixels(var6, var12, var7, 1, var5);
            int var13 = 0;

            for(int var14 = var7 * 4; var13 < var14; ++var13) {
               int var15 = (255 - var5[var13 + 3]) * 65793;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
            }

            var3.setPixels(var6, var12, var7, 1, var5);
         }

      }
   }

   public static class InCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      InCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var16 = 0; var16 < var2; ++var16) {
            int var14;
            int var15;
            for(int var17 = var11 + var1; var11 < var17; var9[var11++] = (var14 >>> 24) * var15 + 8388608 & -16777216 | ((var14 >> 16 & 255) * var15 + 8388608 & -16777216) >>> 8 | ((var14 >> 8 & 255) * var15 + 8388608 & -16777216) >>> 16 | (var14 & 255) * var15 + 8388608 >>> 24) {
               var15 = (var6[var8++] >>> 24) * 65793;
               var14 = var3[var5++];
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class InCompositeContext extends AlphaPreCompositeContext {
      InCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var12 = var8; var12 < var9; ++var12) {
            var4 = var1.getPixels(var6, var12, var7, 1, var4);
            var5 = var2.getPixels(var6, var12, var7, 1, var5);
            int var13 = 0;

            for(int var14 = var7 * 4; var13 < var14; ++var13) {
               int var15 = var5[var13 + 3] * 65793;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
               ++var13;
               var5[var13] = var4[var13] * var15 + 8388608 >>> 24;
            }

            var3.setPixels(var6, var12, var7, 1, var5);
         }

      }
   }

   public static class OverCompositeContext_INT_PACK_UNPRE extends AlphaPreCompositeContext_INT_PACK {
      OverCompositeContext_INT_PACK_UNPRE(ColorModel var1, ColorModel var2) {
         super(var1, var2);
         if (var1.isAlphaPremultiplied()) {
            throw new IllegalArgumentException("OverCompositeContext_INT_PACK_UNPRE is only forsources with unpremultiplied alpha");
         }
      }

      public void compose(Raster var1, Raster var2, WritableRaster var3) {
         ColorModel var4 = this.dstCM;
         if (!this.dstCM.isAlphaPremultiplied()) {
            var4 = GraphicsUtil.coerceData((WritableRaster)var2, this.dstCM, true);
         }

         this.precompose(var1, var2, var3);
         if (!this.dstCM.isAlphaPremultiplied()) {
            GraphicsUtil.coerceData(var3, var4, false);
            if (var2 != var3) {
               GraphicsUtil.coerceData((WritableRaster)var2, var4, false);
            }
         }

      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var18 = 0; var18 < var2; ++var18) {
            int var14;
            int var15;
            int var16;
            int var17;
            for(int var19 = var11 + var1; var11 < var19; var9[var11++] = (var14 & -16777216) + (var16 >>> 24) * var17 + 8388608 & -16777216 | ((var14 >> 16 & 255) * var15 + (var16 >> 16 & 255) * var17 + 8388608 & -16777216) >>> 8 | ((var14 >> 8 & 255) * var15 + (var16 >> 8 & 255) * var17 + 8388608 & -16777216) >>> 16 | (var14 & 255) * var15 + (var16 & 255) * var17 + 8388608 >>> 24) {
               var14 = var3[var5++];
               var16 = var6[var8++];
               var15 = (var14 >>> 24) * 65793;
               var17 = (255 - (var14 >>> 24)) * 65793;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class OverCompositeContext_INT_PACK_NA extends AlphaPreCompositeContext_INT_PACK {
      OverCompositeContext_INT_PACK_NA(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var17 = 0; var17 < var2; ++var17) {
            int var14;
            int var15;
            int var16;
            for(int var18 = var11 + var1; var11 < var18; var9[var11++] = (var14 & 16711680) + (((var15 >> 16 & 255) * var16 + 8388608 & -16777216) >>> 8) | (var14 & '\uff00') + (((var15 >> 8 & 255) * var16 + 8388608 & -16777216) >>> 16) | (var14 & 255) + ((var15 & 255) * var16 + 8388608 >>> 24)) {
               var14 = var3[var5++];
               var15 = var6[var8++];
               var16 = (255 - (var14 >>> 24)) * 65793;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class OverCompositeContext_INT_PACK extends AlphaPreCompositeContext_INT_PACK {
      OverCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11) {
         for(int var17 = 0; var17 < var2; ++var17) {
            int var14;
            int var15;
            int var16;
            for(int var18 = var11 + var1; var11 < var18; var9[var11++] = (var14 & -16777216) + ((var15 >>> 24) * var16 + 8388608 & -16777216) | (var14 & 16711680) + (((var15 >> 16 & 255) * var16 + 8388608 & -16777216) >>> 8) | (var14 & '\uff00') + (((var15 >> 8 & 255) * var16 + 8388608 & -16777216) >>> 16) | (var14 & 255) + ((var15 & 255) * var16 + 8388608 >>> 24)) {
               var14 = var3[var5++];
               var15 = var6[var8++];
               var16 = (255 - (var14 >>> 24)) * 65793;
            }

            var5 += var4;
            var8 += var7;
            var11 += var10;
         }

      }
   }

   public static class OverCompositeContext_NA extends AlphaPreCompositeContext {
      OverCompositeContext_NA(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var12 = var8; var12 < var9; ++var12) {
            var4 = var1.getPixels(var6, var12, var7, 1, var4);
            var5 = var2.getPixels(var6, var12, var7, 1, var5);
            int var13 = 0;
            int var14 = 0;

            for(int var15 = var7 * 4; var13 < var15; ++var14) {
               int var16 = (255 - var4[var13 + 3]) * 65793;
               var5[var14] = var4[var13] + (var5[var14] * var16 + 8388608 >>> 24);
               ++var13;
               ++var14;
               var5[var14] = var4[var13] + (var5[var14] * var16 + 8388608 >>> 24);
               ++var13;
               ++var14;
               var5[var14] = var4[var13] + (var5[var14] * var16 + 8388608 >>> 24);
               var13 += 2;
            }

            var3.setPixels(var6, var12, var7, 1, var5);
         }

      }
   }

   public static class OverCompositeContext extends AlphaPreCompositeContext {
      OverCompositeContext(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      public void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int[] var4 = null;
         int[] var5 = null;
         int var6 = var3.getMinX();
         int var7 = var3.getWidth();
         int var8 = var3.getMinY();
         int var9 = var8 + var3.getHeight();

         for(int var12 = var8; var12 < var9; ++var12) {
            var4 = var1.getPixels(var6, var12, var7, 1, var4);
            var5 = var2.getPixels(var6, var12, var7, 1, var5);
            int var13 = 0;

            for(int var14 = var7 * 4; var13 < var14; ++var13) {
               int var15 = (255 - var4[var13 + 3]) * 65793;
               var5[var13] = var4[var13] + (var5[var13] * var15 + 8388608 >>> 24);
               ++var13;
               var5[var13] = var4[var13] + (var5[var13] * var15 + 8388608 >>> 24);
               ++var13;
               var5[var13] = var4[var13] + (var5[var13] * var15 + 8388608 >>> 24);
               ++var13;
               var5[var13] = var4[var13] + (var5[var13] * var15 + 8388608 >>> 24);
            }

            var3.setPixels(var6, var12, var7, 1, var5);
         }

      }
   }

   public abstract static class AlphaPreCompositeContext_INT_PACK extends AlphaPreCompositeContext {
      AlphaPreCompositeContext_INT_PACK(ColorModel var1, ColorModel var2) {
         super(var1, var2);
      }

      protected abstract void precompose_INT_PACK(int var1, int var2, int[] var3, int var4, int var5, int[] var6, int var7, int var8, int[] var9, int var10, int var11);

      protected void precompose(Raster var1, Raster var2, WritableRaster var3) {
         int var4 = var3.getMinX();
         int var5 = var3.getWidth();
         int var6 = var3.getMinY();
         int var7 = var3.getHeight();
         SinglePixelPackedSampleModel var8 = (SinglePixelPackedSampleModel)var1.getSampleModel();
         int var9 = var8.getScanlineStride();
         DataBufferInt var10 = (DataBufferInt)var1.getDataBuffer();
         int[] var11 = var10.getBankData()[0];
         int var12 = var10.getOffset() + var8.getOffset(var4 - var1.getSampleModelTranslateX(), var6 - var1.getSampleModelTranslateY());
         SinglePixelPackedSampleModel var13 = (SinglePixelPackedSampleModel)var2.getSampleModel();
         int var14 = var13.getScanlineStride();
         DataBufferInt var15 = (DataBufferInt)var2.getDataBuffer();
         int[] var16 = var15.getBankData()[0];
         int var17 = var15.getOffset() + var13.getOffset(var4 - var2.getSampleModelTranslateX(), var6 - var2.getSampleModelTranslateY());
         SinglePixelPackedSampleModel var18 = (SinglePixelPackedSampleModel)var3.getSampleModel();
         int var19 = var18.getScanlineStride();
         DataBufferInt var20 = (DataBufferInt)var3.getDataBuffer();
         int[] var21 = var20.getBankData()[0];
         int var22 = var20.getOffset() + var18.getOffset(var4 - var3.getSampleModelTranslateX(), var6 - var3.getSampleModelTranslateY());
         int var23 = var9 - var5;
         int var24 = var14 - var5;
         int var25 = var19 - var5;
         this.precompose_INT_PACK(var5, var7, var11, var23, var12, var16, var24, var17, var21, var25, var22);
      }
   }

   public abstract static class AlphaPreCompositeContext implements CompositeContext {
      ColorModel srcCM;
      ColorModel dstCM;

      AlphaPreCompositeContext(ColorModel var1, ColorModel var2) {
         this.srcCM = var1;
         this.dstCM = var2;
      }

      public void dispose() {
         this.srcCM = null;
         this.dstCM = null;
      }

      protected abstract void precompose(Raster var1, Raster var2, WritableRaster var3);

      public void compose(Raster var1, Raster var2, WritableRaster var3) {
         ColorModel var4 = this.srcCM;
         if (!this.srcCM.isAlphaPremultiplied()) {
            var4 = GraphicsUtil.coerceData((WritableRaster)var1, this.srcCM, true);
         }

         ColorModel var5 = this.dstCM;
         if (!this.dstCM.isAlphaPremultiplied()) {
            var5 = GraphicsUtil.coerceData((WritableRaster)var2, this.dstCM, true);
         }

         this.precompose(var1, var2, var3);
         if (!this.srcCM.isAlphaPremultiplied()) {
            GraphicsUtil.coerceData((WritableRaster)var1, var4, false);
         }

         if (!this.dstCM.isAlphaPremultiplied()) {
            GraphicsUtil.coerceData(var3, var5, false);
            if (var2 != var3) {
               GraphicsUtil.coerceData((WritableRaster)var2, var5, false);
            }
         }

      }
   }
}
