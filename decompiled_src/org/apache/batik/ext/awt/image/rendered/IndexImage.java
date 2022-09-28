package org.apache.batik.ext.awt.image.rendered;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class IndexImage {
   static byte[][] computeRGB(int var0, Cube[] var1) {
      byte[] var2 = new byte[var0];
      byte[] var3 = new byte[var0];
      byte[] var4 = new byte[var0];
      byte[] var5 = new byte[3];

      for(int var6 = 0; var6 < var0; ++var6) {
         var5 = var1[var6].averageColorRGB(var5);
         var2[var6] = var5[0];
         var3[var6] = var5[1];
         var4[var6] = var5[2];
      }

      byte[][] var7 = new byte[][]{var2, var3, var4};
      return var7;
   }

   static void logRGB(byte[] var0, byte[] var1, byte[] var2) {
      StringBuffer var3 = new StringBuffer(100);
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = "(" + (var0[var5] + 128) + ',' + (var1[var5] + 128) + ',' + (var2[var5] + 128) + "),";
         var3.append(var6);
      }

      System.out.println("RGB:" + var4 + var3);
   }

   static List[] createColorList(BufferedImage var0) {
      int var1 = var0.getWidth();
      int var2 = var0.getHeight();
      ArrayList[] var3 = new ArrayList[4096];

      for(int var4 = 0; var4 < var1; ++var4) {
         for(int var5 = 0; var5 < var2; ++var5) {
            int var6 = var0.getRGB(var4, var5) & 16777215;
            int var7 = (var6 & 15728640) >>> 12 | (var6 & '\uf000') >>> 8 | (var6 & 240) >>> 4;
            ArrayList var8 = var3[var7];
            if (var8 == null) {
               var8 = new ArrayList();
               var8.add(new Counter(var6));
               var3[var7] = var8;
            } else {
               Iterator var9 = var8.iterator();

               do {
                  if (!var9.hasNext()) {
                     var8.add(new Counter(var6));
                     break;
                  }
               } while(!((Counter)var9.next()).add(var6));
            }
         }
      }

      return var3;
   }

   static Counter[][] convertColorList(List[] var0) {
      Counter[] var1 = new Counter[0];
      Counter[][] var2 = new Counter[4096][];

      for(int var3 = 0; var3 < var0.length; ++var3) {
         List var4 = var0[var3];
         if (var4 == null) {
            var2[var3] = var1;
         } else {
            int var5 = var4.size();
            var2[var3] = (Counter[])var4.toArray(new Counter[var5]);
            var0[var3] = null;
         }
      }

      return var2;
   }

   public static BufferedImage getIndexedImage(BufferedImage var0, int var1) {
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
      List[] var4 = createColorList(var0);
      Counter[][] var5 = convertColorList(var4);
      var4 = null;
      int var6 = 1;
      int var7 = 0;
      Cube[] var8 = new Cube[var1];
      var8[0] = new Cube(var5, var2 * var3);

      int var13;
      while(var6 < var1) {
         while(var8[var7].isDone()) {
            ++var7;
            if (var7 == var6) {
               break;
            }
         }

         if (var7 == var6) {
            break;
         }

         Cube var9 = var8[var7];
         Cube var10 = var9.split();
         if (var10 != null) {
            if (var10.count > var9.count) {
               Cube var11 = var9;
               var9 = var10;
               var10 = var11;
            }

            int var19 = var7;
            int var12 = var9.count;

            for(var13 = var7 + 1; var13 < var6 && var8[var13].count >= var12; ++var13) {
               var8[var19++] = var8[var13];
            }

            var8[var19++] = var9;

            for(var12 = var10.count; var19 < var6 && var8[var19].count >= var12; ++var19) {
            }

            for(var13 = var6; var13 > var19; --var13) {
               var8[var13] = var8[var13 - 1];
            }

            var8[var19++] = var10;
            ++var6;
         }
      }

      byte[][] var17 = computeRGB(var6, var8);
      IndexColorModel var18 = new IndexColorModel(8, var6, var17[0], var17[1], var17[2]);
      BufferedImage var21 = new BufferedImage(var2, var3, 13, var18);
      Graphics2D var20 = var21.createGraphics();
      var20.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      var20.drawImage(var0, 0, 0, (ImageObserver)null);
      var20.dispose();

      for(var13 = 1; var13 <= 8 && 1 << var13 < var6; ++var13) {
      }

      if (var13 > 4) {
         return var21;
      } else {
         if (var13 == 3) {
            var13 = 4;
         }

         IndexColorModel var14 = new IndexColorModel(var13, var6, var17[0], var17[1], var17[2]);
         MultiPixelPackedSampleModel var15 = new MultiPixelPackedSampleModel(0, var2, var3, var13);
         WritableRaster var16 = Raster.createWritableRaster(var15, new Point(0, 0));
         var0 = var21;
         var21 = new BufferedImage(var14, var16, var21.isAlphaPremultiplied(), (Hashtable)null);
         GraphicsUtil.copyData(var0, var21);
         return var21;
      }
   }

   private static class Cube {
      static final byte[] RGB_BLACK = new byte[]{0, 0, 0};
      int[] min = new int[]{0, 0, 0};
      int[] max = new int[]{255, 255, 255};
      boolean done = false;
      final Counter[][] colors;
      int count = 0;
      static final int RED = 0;
      static final int GRN = 1;
      static final int BLU = 2;

      Cube(Counter[][] var1, int var2) {
         this.colors = var1;
         this.count = var2;
      }

      public boolean isDone() {
         return this.done;
      }

      private boolean contains(int[] var1) {
         int var2 = var1[0];
         int var3 = var1[1];
         int var4 = var1[2];
         return this.min[0] <= var2 && var2 <= this.max[0] && this.min[1] <= var3 && var3 <= this.max[1] && this.min[2] <= var4 && var4 <= this.max[2];
      }

      Cube split() {
         int var1 = this.max[0] - this.min[0] + 1;
         int var2 = this.max[1] - this.min[1] + 1;
         int var3 = this.max[2] - this.min[2] + 1;
         byte var4;
         byte var5;
         byte var6;
         if (var1 >= var2) {
            if (var1 >= var3) {
               var6 = 0;
               var4 = 1;
               var5 = 2;
            } else {
               var6 = 2;
               var4 = 0;
               var5 = 1;
            }
         } else if (var2 >= var3) {
            var6 = 1;
            var4 = 0;
            var5 = 2;
         } else {
            var6 = 2;
            var4 = 1;
            var5 = 0;
         }

         Cube var7 = this.splitChannel(var6, var4, var5);
         if (var7 != null) {
            return var7;
         } else {
            var7 = this.splitChannel(var4, var6, var5);
            if (var7 != null) {
               return var7;
            } else {
               var7 = this.splitChannel(var5, var6, var4);
               if (var7 != null) {
                  return var7;
               } else {
                  this.done = true;
                  return null;
               }
            }
         }
      }

      private void normalize(int var1, int[] var2) {
         if (this.count != 0) {
            int var3 = this.min[var1];
            int var4 = this.max[var1];
            int var5 = -1;
            int var6 = -1;

            int var7;
            for(var7 = var3; var7 <= var4; ++var7) {
               if (var2[var7] != 0) {
                  var5 = var7;
                  break;
               }
            }

            for(var7 = var4; var7 >= var3; --var7) {
               if (var2[var7] != 0) {
                  var6 = var7;
                  break;
               }
            }

            boolean var9 = var5 != -1 && var3 != var5;
            boolean var8 = var6 != -1 && var4 != var6;
            if (var9) {
               this.min[var1] = var5;
            }

            if (var8) {
               this.max[var1] = var6;
            }

         }
      }

      Cube splitChannel(int var1, int var2, int var3) {
         if (this.min[var1] == this.max[var1]) {
            return null;
         } else if (this.count == 0) {
            return null;
         } else {
            int var4 = this.count / 2;
            int[] var5 = this.computeCounts(var1, var2, var3);
            int var6 = 0;
            int var7 = -1;
            int var8 = this.min[var1];
            int var9 = this.max[var1];

            for(int var10 = this.min[var1]; var10 <= this.max[var1]; ++var10) {
               int var11 = var5[var10];
               if (var11 == 0) {
                  if (var6 == 0 && var10 < this.max[var1]) {
                     this.min[var1] = var10 + 1;
                  }
               } else {
                  if (var6 + var11 >= var4) {
                     if (var4 - var6 <= var6 + var11 - var4) {
                        if (var7 == -1) {
                           if (var11 == this.count) {
                              this.max[var1] = var10;
                              return null;
                           }

                           var8 = var10;
                           var9 = var10 + 1;
                           var6 += var11;
                        } else {
                           var8 = var7;
                           var9 = var10;
                        }
                     } else if (var10 == this.max[var1]) {
                        if (var11 == this.count) {
                           return null;
                        }

                        var8 = var7;
                        var9 = var10;
                     } else {
                        var6 += var11;
                        var8 = var10;
                        var9 = var10 + 1;
                     }
                     break;
                  }

                  var7 = var10;
                  var6 += var11;
               }
            }

            Cube var12 = new Cube(this.colors, var6);
            this.count -= var6;
            var12.min[var1] = this.min[var1];
            var12.max[var1] = var8;
            this.min[var1] = var9;
            var12.min[var2] = this.min[var2];
            var12.max[var2] = this.max[var2];
            var12.min[var3] = this.min[var3];
            var12.max[var3] = this.max[var3];
            this.normalize(var1, var5);
            var12.normalize(var1, var5);
            return var12;
         }
      }

      private int[] computeCounts(int var1, int var2, int var3) {
         int var4 = (2 - var1) * 4;
         int var5 = (2 - var2) * 4;
         int var6 = (2 - var3) * 4;
         int var7 = this.count / 2;
         int[] var8 = new int[256];
         int var9 = 0;
         int var10 = this.min[0];
         int var11 = this.min[1];
         int var12 = this.min[2];
         int var13 = this.max[0];
         int var14 = this.max[1];
         int var15 = this.max[2];
         int[] var16 = new int[]{var10 >> 4, var11 >> 4, var12 >> 4};
         int[] var17 = new int[]{var13 >> 4, var14 >> 4, var15 >> 4};
         int[] var18 = new int[]{0, 0, 0};

         for(int var19 = var16[var1]; var19 <= var17[var1]; ++var19) {
            int var20 = var19 << var4;

            for(int var21 = var16[var2]; var21 <= var17[var2]; ++var21) {
               int var22 = var20 | var21 << var5;

               for(int var23 = var16[var3]; var23 <= var17[var3]; ++var23) {
                  int var24 = var22 | var23 << var6;
                  Counter[] var25 = this.colors[var24];

                  for(int var26 = 0; var26 < var25.length; ++var26) {
                     Counter var27 = var25[var26];
                     var18 = var27.getRgb(var18);
                     if (this.contains(var18)) {
                        var8[var18[var1]] += var27.count;
                        var9 += var27.count;
                     }
                  }
               }
            }
         }

         return var8;
      }

      public String toString() {
         return "Cube: [" + this.min[0] + '-' + this.max[0] + "] [" + this.min[1] + '-' + this.max[1] + "] [" + this.min[2] + '-' + this.max[2] + "] n:" + this.count;
      }

      public int averageColor() {
         if (this.count == 0) {
            return 0;
         } else {
            byte[] var1 = this.averageColorRGB((byte[])null);
            return var1[0] << 16 & 16711680 | var1[1] << 8 & '\uff00' | var1[2] & 255;
         }
      }

      public byte[] averageColorRGB(byte[] var1) {
         if (this.count == 0) {
            return RGB_BLACK;
         } else {
            float var2 = 0.0F;
            float var3 = 0.0F;
            float var4 = 0.0F;
            int var5 = this.min[0];
            int var6 = this.min[1];
            int var7 = this.min[2];
            int var8 = this.max[0];
            int var9 = this.max[1];
            int var10 = this.max[2];
            int[] var11 = new int[]{var5 >> 4, var6 >> 4, var7 >> 4};
            int[] var12 = new int[]{var8 >> 4, var9 >> 4, var10 >> 4};
            int[] var13 = new int[3];

            for(int var14 = var11[0]; var14 <= var12[0]; ++var14) {
               int var15 = var14 << 8;

               for(int var16 = var11[1]; var16 <= var12[1]; ++var16) {
                  int var17 = var15 | var16 << 4;

                  for(int var18 = var11[2]; var18 <= var12[2]; ++var18) {
                     int var19 = var17 | var18;
                     Counter[] var20 = this.colors[var19];

                     for(int var21 = 0; var21 < var20.length; ++var21) {
                        Counter var22 = var20[var21];
                        var13 = var22.getRgb(var13);
                        if (this.contains(var13)) {
                           float var23 = (float)var22.count / (float)this.count;
                           var2 += (float)var13[0] * var23;
                           var3 += (float)var13[1] * var23;
                           var4 += (float)var13[2] * var23;
                        }
                     }
                  }
               }
            }

            byte[] var24 = var1 == null ? new byte[3] : var1;
            var24[0] = (byte)((int)(var2 + 0.5F));
            var24[1] = (byte)((int)(var3 + 0.5F));
            var24[2] = (byte)((int)(var4 + 0.5F));
            return var24;
         }
      }
   }

   private static class Counter {
      final int val;
      int count = 1;

      Counter(int var1) {
         this.val = var1;
      }

      boolean add(int var1) {
         if (this.val != var1) {
            return false;
         } else {
            ++this.count;
            return true;
         }
      }

      int[] getRgb(int[] var1) {
         var1[0] = (this.val & 16711680) >> 16;
         var1[1] = (this.val & '\uff00') >> 8;
         var1[2] = this.val & 255;
         return var1;
      }
   }
}
