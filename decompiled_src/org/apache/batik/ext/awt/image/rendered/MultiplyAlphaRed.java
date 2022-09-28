package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiplyAlphaRed extends AbstractRed {
   public MultiplyAlphaRed(CachableRed var1, CachableRed var2) {
      super((List)makeList(var1, var2), makeBounds(var1, var2), fixColorModel(var1), fixSampleModel(var1), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
   }

   public boolean is_INT_PACK_BYTE_COMP(SampleModel var1, SampleModel var2) {
      if (!(var1 instanceof SinglePixelPackedSampleModel)) {
         return false;
      } else if (!(var2 instanceof ComponentSampleModel)) {
         return false;
      } else if (var1.getDataType() != 3) {
         return false;
      } else if (var2.getDataType() != 0) {
         return false;
      } else {
         SinglePixelPackedSampleModel var3 = (SinglePixelPackedSampleModel)var1;
         int[] var4 = var3.getBitMasks();
         if (var4.length != 4) {
            return false;
         } else if (var4[0] != 16711680) {
            return false;
         } else if (var4[1] != 65280) {
            return false;
         } else if (var4[2] != 255) {
            return false;
         } else if (var4[3] != -16777216) {
            return false;
         } else {
            ComponentSampleModel var5 = (ComponentSampleModel)var2;
            if (var5.getNumBands() != 1) {
               return false;
            } else {
               return var5.getPixelStride() == 1;
            }
         }
      }
   }

   public WritableRaster INT_PACK_BYTE_COMP_Impl(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      CachableRed var3 = (CachableRed)this.getSources().get(1);
      var2.copyData(var1);
      Rectangle var4 = var1.getBounds();
      var4 = var4.intersection(var3.getBounds());
      Raster var5 = var3.getData(var4);
      ComponentSampleModel var6 = (ComponentSampleModel)var5.getSampleModel();
      int var7 = var6.getScanlineStride();
      DataBufferByte var8 = (DataBufferByte)var5.getDataBuffer();
      int var9 = var8.getOffset() + var6.getOffset(var4.x - var5.getSampleModelTranslateX(), var4.y - var5.getSampleModelTranslateY());
      byte[] var10 = var8.getBankData()[0];
      SinglePixelPackedSampleModel var11 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var12 = var11.getScanlineStride();
      DataBufferInt var13 = (DataBufferInt)var1.getDataBuffer();
      int var14 = var13.getOffset() + var11.getOffset(var4.x - var1.getSampleModelTranslateX(), var4.y - var1.getSampleModelTranslateY());
      int[] var15 = var13.getBankData()[0];
      ColorModel var16 = var2.getColorModel();
      int var17;
      int var18;
      int var19;
      int var20;
      int var21;
      int var22;
      if (var16.isAlphaPremultiplied()) {
         for(var17 = 0; var17 < var4.height; ++var17) {
            var18 = var14 + var17 * var12;
            var19 = var9 + var17 * var7;

            for(var20 = var18 + var4.width; var18 < var20; ++var18) {
               var21 = var10[var19++] & 255;
               var22 = var15[var18];
               var15[var18] = ((var22 >>> 24) * var21 & '\uff00') << 16 | ((var22 >>> 16 & 255) * var21 & '\uff00') << 8 | (var22 >>> 8 & 255) * var21 & '\uff00' | ((var22 & 255) * var21 & '\uff00') >> 8;
            }
         }
      } else {
         for(var17 = 0; var17 < var4.height; ++var17) {
            var18 = var14 + var17 * var12;
            var19 = var9 + var17 * var7;

            for(var20 = var18 + var4.width; var18 < var20; ++var18) {
               var21 = var10[var19++] & 255;
               var22 = var15[var18] >>> 24;
               var15[var18] = (var22 * var21 & '\uff00') << 16 | var15[var18] & 16777215;
            }
         }
      }

      return var1;
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      CachableRed var3 = (CachableRed)this.getSources().get(1);
      if (this.is_INT_PACK_BYTE_COMP(var2.getSampleModel(), var3.getSampleModel())) {
         return this.INT_PACK_BYTE_COMP_Impl(var1);
      } else {
         ColorModel var4 = var2.getColorModel();
         if (var4.hasAlpha()) {
            var2.copyData(var1);
            Rectangle var16 = var1.getBounds();
            if (!var16.intersects(var3.getBounds())) {
               return var1;
            } else {
               var16 = var16.intersection(var3.getBounds());
               int[] var18 = null;
               int[] var19 = null;
               Raster var8 = var3.getData(var16);
               int var9 = var16.width;
               int var10 = var1.getSampleModel().getNumBands();
               int var11;
               int var12;
               int var13;
               if (var4.isAlphaPremultiplied()) {
                  for(var11 = var16.y; var11 < var16.y + var16.height; ++var11) {
                     label91: {
                        var18 = var1.getPixels(var16.x, var11, var9, 1, var18);
                        var19 = var8.getSamples(var16.x, var11, var9, 1, 0, var19);
                        var12 = 0;
                        int var15;
                        switch (var10) {
                           case 2:
                              var15 = 0;

                              while(true) {
                                 if (var15 >= var19.length) {
                                    break label91;
                                 }

                                 var13 = var19[var15] & 255;
                                 var18[var12] = (var18[var12] & 255) * var13 >> 8;
                                 ++var12;
                                 var18[var12] = (var18[var12] & 255) * var13 >> 8;
                                 ++var12;
                                 ++var15;
                              }
                           case 4:
                              var15 = 0;

                              while(true) {
                                 if (var15 >= var19.length) {
                                    break label91;
                                 }

                                 var13 = var19[var15] & 255;
                                 var18[var12] = (var18[var12] & 255) * var13 >> 8;
                                 ++var12;
                                 var18[var12] = (var18[var12] & 255) * var13 >> 8;
                                 ++var12;
                                 var18[var12] = (var18[var12] & 255) * var13 >> 8;
                                 ++var12;
                                 var18[var12] = (var18[var12] & 255) * var13 >> 8;
                                 ++var12;
                                 ++var15;
                              }
                           default:
                              var15 = 0;
                        }

                        while(var15 < var19.length) {
                           var13 = var19[var15] & 255;

                           for(int var14 = 0; var14 < var10; ++var14) {
                              var18[var12] = (var18[var12] & 255) * var13 >> 8;
                              ++var12;
                           }

                           ++var15;
                        }
                     }

                     var1.setPixels(var16.x, var11, var9, 1, var18);
                  }
               } else {
                  var11 = var2.getSampleModel().getNumBands() - 1;

                  for(var12 = var16.y; var12 < var16.y + var16.height; ++var12) {
                     var18 = var1.getSamples(var16.x, var12, var9, 1, var11, var18);
                     var19 = var8.getSamples(var16.x, var12, var9, 1, 0, var19);

                     for(var13 = 0; var13 < var18.length; ++var13) {
                        var18[var13] = (var18[var13] & 255) * (var19[var13] & 255) >> 8;
                     }

                     var1.setSamples(var16.x, var12, var9, 1, var11, var18);
                  }
               }

               return var1;
            }
         } else {
            int[] var5 = new int[var1.getNumBands() - 1];

            for(int var6 = 0; var6 < var5.length; var5[var6] = var6++) {
            }

            WritableRaster var17 = var1.createWritableChild(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight(), var1.getMinX(), var1.getMinY(), var5);
            var2.copyData(var17);
            Rectangle var7 = var1.getBounds();
            var7 = var7.intersection(var3.getBounds());
            var5 = new int[]{var1.getNumBands() - 1};
            var17 = var1.createWritableChild(var7.x, var7.y, var7.width, var7.height, var7.x, var7.y, var5);
            var3.copyData(var17);
            return var1;
         }
      }
   }

   public static List makeList(CachableRed var0, CachableRed var1) {
      ArrayList var2 = new ArrayList(2);
      var2.add(var0);
      var2.add(var1);
      return var2;
   }

   public static Rectangle makeBounds(CachableRed var0, CachableRed var1) {
      Rectangle var2 = var0.getBounds();
      Rectangle var3 = var1.getBounds();
      return var2.intersection(var3);
   }

   public static SampleModel fixSampleModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      SampleModel var2 = var0.getSampleModel();
      if (var1.hasAlpha()) {
         return var2;
      } else {
         int var3 = var2.getWidth();
         int var4 = var2.getHeight();
         int var5 = var2.getNumBands() + 1;
         int[] var6 = new int[var5];

         for(int var7 = 0; var7 < var5; var6[var7] = var7++) {
         }

         return new PixelInterleavedSampleModel(0, var3, var4, var5, var3 * var5, var6);
      }
   }

   public static ColorModel fixColorModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      if (var1.hasAlpha()) {
         return var1;
      } else {
         int var2 = var0.getSampleModel().getNumBands() + 1;
         int[] var3 = new int[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = 8;
         }

         ColorSpace var5 = var1.getColorSpace();
         return new ComponentColorModel(var5, var3, true, false, 3, 0);
      }
   }
}
