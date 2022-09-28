package org.apache.batik.ext.awt.image.rendered;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class Any2LsRGBRed extends AbstractRed {
   boolean srcIssRGB = false;
   private static final double GAMMA = 2.4;
   private static final double LFACT = 0.07739938080495357;
   private static final int[] sRGBToLsRGBLut = new int[256];

   public Any2LsRGBRed(CachableRed var1) {
      super((CachableRed)var1, var1.getBounds(), fixColorModel(var1), fixSampleModel(var1), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
      ColorModel var2 = var1.getColorModel();
      if (var2 != null) {
         ColorSpace var3 = var2.getColorSpace();
         if (var3 == ColorSpace.getInstance(1000)) {
            this.srcIssRGB = true;
         }

      }
   }

   public static final double sRGBToLsRGB(double var0) {
      return var0 <= 0.003928 ? var0 * 0.07739938080495357 : Math.pow((var0 + 0.055) / 1.055, 2.4);
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      ColorModel var3 = var2.getColorModel();
      SampleModel var4 = var2.getSampleModel();
      if (this.srcIssRGB && Any2sRGBRed.is_INT_PACK_COMP(var1.getSampleModel())) {
         var2.copyData(var1);
         if (var3.hasAlpha()) {
            GraphicsUtil.coerceData(var1, var3, false);
         }

         Any2sRGBRed.applyLut_INT(var1, sRGBToLsRGBLut);
         return var1;
      } else {
         if (var3 == null) {
            float[][] var5 = (float[][])null;
            switch (var4.getNumBands()) {
               case 1:
                  var5 = new float[1][3];
                  var5[0][0] = 1.0F;
                  var5[0][1] = 1.0F;
                  var5[0][2] = 1.0F;
                  break;
               case 2:
                  var5 = new float[2][4];
                  var5[0][0] = 1.0F;
                  var5[0][1] = 1.0F;
                  var5[0][2] = 1.0F;
                  var5[1][3] = 1.0F;
                  break;
               case 3:
                  var5 = new float[3][3];
                  var5[0][0] = 1.0F;
                  var5[1][1] = 1.0F;
                  var5[2][2] = 1.0F;
                  break;
               default:
                  var5 = new float[var4.getNumBands()][4];
                  var5[0][0] = 1.0F;
                  var5[1][1] = 1.0F;
                  var5[2][2] = 1.0F;
                  var5[3][3] = 1.0F;
            }

            Raster var6 = var2.getData(var1.getBounds());
            BandCombineOp var7 = new BandCombineOp(var5, (RenderingHints)null);
            var7.filter(var6, var1);
         } else {
            ColorModel var13 = this.getColorModel();
            BufferedImage var12;
            if (!var13.hasAlpha()) {
               var12 = new BufferedImage(var13, var1.createWritableTranslatedChild(0, 0), var13.isAlphaPremultiplied(), (Hashtable)null);
            } else {
               SinglePixelPackedSampleModel var14 = (SinglePixelPackedSampleModel)var1.getSampleModel();
               int[] var8 = var14.getBitMasks();
               SinglePixelPackedSampleModel var9 = new SinglePixelPackedSampleModel(var14.getDataType(), var14.getWidth(), var14.getHeight(), var14.getScanlineStride(), new int[]{var8[0], var8[1], var8[2]});
               ColorModel var10 = GraphicsUtil.Linear_sRGB;
               WritableRaster var11 = Raster.createWritableRaster(var9, var1.getDataBuffer(), new Point(0, 0));
               var11 = var11.createWritableChild(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY(), var1.getWidth(), var1.getHeight(), 0, 0, (int[])null);
               var12 = new BufferedImage(var10, var11, false, (Hashtable)null);
            }

            ColorModel var15 = var3;
            WritableRaster var16;
            if (var3.hasAlpha() && var3.isAlphaPremultiplied()) {
               Rectangle var18 = var1.getBounds();
               SampleModel var20 = var3.createCompatibleSampleModel(var18.width, var18.height);
               var16 = Raster.createWritableRaster(var20, new Point(var18.x, var18.y));
               var2.copyData(var16);
               var15 = GraphicsUtil.coerceData(var16, var3, false);
            } else {
               Raster var17 = var2.getData(var1.getBounds());
               var16 = GraphicsUtil.makeRasterWritable(var17);
            }

            BufferedImage var19 = new BufferedImage(var15, var16.createWritableTranslatedChild(0, 0), false, (Hashtable)null);
            ColorConvertOp var21 = new ColorConvertOp((RenderingHints)null);
            var21.filter(var19, var12);
            if (var13.hasAlpha()) {
               copyBand(var16, var4.getNumBands() - 1, var1, this.getSampleModel().getNumBands() - 1);
            }
         }

         return var1;
      }
   }

   protected static ColorModel fixColorModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      if (var1 != null) {
         return var1.hasAlpha() ? GraphicsUtil.Linear_sRGB_Unpre : GraphicsUtil.Linear_sRGB;
      } else {
         SampleModel var2 = var0.getSampleModel();
         switch (var2.getNumBands()) {
            case 1:
               return GraphicsUtil.Linear_sRGB;
            case 2:
               return GraphicsUtil.Linear_sRGB_Unpre;
            case 3:
               return GraphicsUtil.Linear_sRGB;
            default:
               return GraphicsUtil.Linear_sRGB_Unpre;
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
      for(int var2 = 0; var2 < 256; ++var2) {
         double var3 = sRGBToLsRGB((double)var2 * 0.00392156862745098);
         sRGBToLsRGBLut[var2] = (int)Math.round(var3 * 255.0);
      }

   }
}
