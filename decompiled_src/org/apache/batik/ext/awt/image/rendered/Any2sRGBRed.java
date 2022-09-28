package org.apache.batik.ext.awt.image.rendered;

import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class Any2sRGBRed extends AbstractRed {
   boolean srcIsLsRGB = false;
   private static final double GAMMA = 2.4;
   private static final int[] linearToSRGBLut = new int[256];

   public Any2sRGBRed(CachableRed var1) {
      super((CachableRed)var1, var1.getBounds(), fixColorModel(var1), fixSampleModel(var1), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
      ColorModel var2 = var1.getColorModel();
      if (var2 != null) {
         ColorSpace var3 = var2.getColorSpace();
         if (var3 == ColorSpace.getInstance(1004)) {
            this.srcIsLsRGB = true;
         }

      }
   }

   public static boolean is_INT_PACK_COMP(SampleModel var0) {
      if (!(var0 instanceof SinglePixelPackedSampleModel)) {
         return false;
      } else if (var0.getDataType() != 3) {
         return false;
      } else {
         SinglePixelPackedSampleModel var1 = (SinglePixelPackedSampleModel)var0;
         int[] var2 = var1.getBitMasks();
         if (var2.length != 3 && var2.length != 4) {
            return false;
         } else if (var2[0] != 16711680) {
            return false;
         } else if (var2[1] != 65280) {
            return false;
         } else if (var2[2] != 255) {
            return false;
         } else {
            return var2.length != 4 || var2[3] == -16777216;
         }
      }
   }

   public static WritableRaster applyLut_INT(WritableRaster var0, int[] var1) {
      SinglePixelPackedSampleModel var2 = (SinglePixelPackedSampleModel)var0.getSampleModel();
      DataBufferInt var3 = (DataBufferInt)var0.getDataBuffer();
      int var4 = var3.getOffset() + var2.getOffset(var0.getMinX() - var0.getSampleModelTranslateX(), var0.getMinY() - var0.getSampleModelTranslateY());
      int[] var5 = var3.getBankData()[0];
      int var6 = var0.getWidth();
      int var7 = var0.getHeight();
      int var8 = var2.getScanlineStride();

      for(int var11 = 0; var11 < var7; ++var11) {
         int var12 = var4 + var11 * var8;

         for(int var9 = var12 + var6; var12 < var9; ++var12) {
            int var10 = var5[var12];
            var5[var12] = var10 & -16777216 | var1[var10 >>> 16 & 255] << 16 | var1[var10 >>> 8 & 255] << 8 | var1[var10 & 255];
         }
      }

      return var0;
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      ColorModel var3 = var2.getColorModel();
      SampleModel var4 = var2.getSampleModel();
      if (this.srcIsLsRGB && is_INT_PACK_COMP(var1.getSampleModel())) {
         var2.copyData(var1);
         if (var3.hasAlpha()) {
            GraphicsUtil.coerceData(var1, var3, false);
         }

         applyLut_INT(var1, linearToSRGBLut);
         return var1;
      } else {
         Raster var6;
         float[][] var15;
         BandCombineOp var16;
         if (var3 == null) {
            var15 = (float[][])null;
            switch (var4.getNumBands()) {
               case 1:
                  var15 = new float[3][1];
                  var15[0][0] = 1.0F;
                  var15[1][0] = 1.0F;
                  var15[2][0] = 1.0F;
                  break;
               case 2:
                  var15 = new float[4][2];
                  var15[0][0] = 1.0F;
                  var15[1][0] = 1.0F;
                  var15[2][0] = 1.0F;
                  var15[3][1] = 1.0F;
                  break;
               case 3:
                  var15 = new float[3][3];
                  var15[0][0] = 1.0F;
                  var15[1][1] = 1.0F;
                  var15[2][2] = 1.0F;
                  break;
               default:
                  var15 = new float[4][var4.getNumBands()];
                  var15[0][0] = 1.0F;
                  var15[1][1] = 1.0F;
                  var15[2][2] = 1.0F;
                  var15[3][3] = 1.0F;
            }

            var6 = var2.getData(var1.getBounds());
            var16 = new BandCombineOp(var15, (RenderingHints)null);
            var16.filter(var6, var1);
            return var1;
         } else if (var3.getColorSpace() == ColorSpace.getInstance(1003)) {
            try {
               var15 = (float[][])null;
               switch (var4.getNumBands()) {
                  case 1:
                     var15 = new float[3][1];
                     var15[0][0] = 1.0F;
                     var15[1][0] = 1.0F;
                     var15[2][0] = 1.0F;
                     break;
                  case 2:
                  default:
                     var15 = new float[4][2];
                     var15[0][0] = 1.0F;
                     var15[1][0] = 1.0F;
                     var15[2][0] = 1.0F;
                     var15[3][1] = 1.0F;
               }

               var6 = var2.getData(var1.getBounds());
               var16 = new BandCombineOp(var15, (RenderingHints)null);
               var16.filter(var6, var1);
            } catch (Throwable var14) {
               var14.printStackTrace();
            }

            return var1;
         } else {
            ColorModel var5 = this.getColorModel();
            if (var3.getColorSpace() == var5.getColorSpace()) {
               if (is_INT_PACK_COMP(var4)) {
                  var2.copyData(var1);
               } else {
                  GraphicsUtil.copyData(var2.getData(var1.getBounds()), var1);
               }

               return var1;
            } else {
               var6 = var2.getData(var1.getBounds());
               WritableRaster var7 = (WritableRaster)var6;
               ColorModel var8 = var3;
               if (var3.hasAlpha()) {
                  var8 = GraphicsUtil.coerceData(var7, var3, false);
               }

               BufferedImage var9 = new BufferedImage(var8, var7.createWritableTranslatedChild(0, 0), false, (Hashtable)null);
               ColorConvertOp var11 = new ColorConvertOp(var5.getColorSpace(), (RenderingHints)null);
               BufferedImage var10 = var11.filter(var9, (BufferedImage)null);
               WritableRaster var12 = var1.createWritableTranslatedChild(0, 0);

               for(int var13 = 0; var13 < var5.getColorSpace().getNumComponents(); ++var13) {
                  copyBand(var10.getRaster(), var13, var12, var13);
               }

               if (var5.hasAlpha()) {
                  copyBand(var7, var4.getNumBands() - 1, var1, this.getSampleModel().getNumBands() - 1);
               }

               return var1;
            }
         }
      }
   }

   protected static ColorModel fixColorModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      if (var1 != null) {
         return var1.hasAlpha() ? GraphicsUtil.sRGB_Unpre : GraphicsUtil.sRGB;
      } else {
         SampleModel var2 = var0.getSampleModel();
         switch (var2.getNumBands()) {
            case 1:
               return GraphicsUtil.sRGB;
            case 2:
               return GraphicsUtil.sRGB_Unpre;
            case 3:
               return GraphicsUtil.sRGB;
            default:
               return GraphicsUtil.sRGB_Unpre;
         }
      }
   }

   protected static SampleModel fixSampleModel(CachableRed var0) {
      SampleModel var1 = var0.getSampleModel();
      ColorModel var2 = var0.getColorModel();
      boolean var3 = false;
      if (var2 != null) {
         var3 = var2.hasAlpha();
      } else {
         switch (var1.getNumBands()) {
            case 1:
            case 3:
               var3 = false;
               break;
            default:
               var3 = true;
         }
      }

      return var3 ? new SinglePixelPackedSampleModel(3, var1.getWidth(), var1.getHeight(), new int[]{16711680, 65280, 255, -16777216}) : new SinglePixelPackedSampleModel(3, var1.getWidth(), var1.getHeight(), new int[]{16711680, 65280, 255});
   }

   static {
      for(int var4 = 0; var4 < 256; ++var4) {
         double var5 = (double)var4 * 0.00392156862745098;
         if (var5 <= 0.0031308) {
            var5 *= 12.92;
         } else {
            var5 = 1.055 * Math.pow(var5, 0.4166666666666667) - 0.055;
         }

         linearToSRGBLut[var4] = (int)Math.round(var5 * 255.0);
      }

   }
}
