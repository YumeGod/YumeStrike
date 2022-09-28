package org.apache.batik.ext.awt.image.rendered;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.ColorSpaceHintKey;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class Any2LumRed extends AbstractRed {
   public Any2LumRed(CachableRed var1) {
      super((CachableRed)var1, var1.getBounds(), fixColorModel(var1), fixSampleModel(var1), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
      this.props.put("org.apache.batik.gvt.filter.Colorspace", ColorSpaceHintKey.VALUE_COLORSPACE_GREY);
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      SampleModel var3 = var2.getSampleModel();
      ColorModel var4 = var2.getColorModel();
      Raster var5 = var2.getData(var1.getBounds());
      if (var4 == null) {
         float[][] var6 = (float[][])null;
         if (var3.getNumBands() == 2) {
            var6 = new float[2][2];
            var6[0][0] = 1.0F;
            var6[1][1] = 1.0F;
         } else {
            var6 = new float[var3.getNumBands()][1];
            var6[0][0] = 1.0F;
         }

         BandCombineOp var7 = new BandCombineOp(var6, (RenderingHints)null);
         var7.filter(var5, var1);
      } else {
         WritableRaster var14 = (WritableRaster)var5;
         if (var4.hasAlpha()) {
            GraphicsUtil.coerceData(var14, var4, false);
         }

         BufferedImage var15 = new BufferedImage(var4, var14.createWritableTranslatedChild(0, 0), false, (Hashtable)null);
         ColorModel var9 = this.getColorModel();
         BufferedImage var8;
         if (!var9.hasAlpha()) {
            var8 = new BufferedImage(var9, var1.createWritableTranslatedChild(0, 0), var9.isAlphaPremultiplied(), (Hashtable)null);
         } else {
            PixelInterleavedSampleModel var10 = (PixelInterleavedSampleModel)var1.getSampleModel();
            PixelInterleavedSampleModel var11 = new PixelInterleavedSampleModel(var10.getDataType(), var10.getWidth(), var10.getHeight(), var10.getPixelStride(), var10.getScanlineStride(), new int[]{0});
            WritableRaster var12 = Raster.createWritableRaster(var11, var1.getDataBuffer(), new Point(0, 0));
            var12 = var12.createWritableChild(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY(), var1.getWidth(), var1.getHeight(), 0, 0, (int[])null);
            ComponentColorModel var13 = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0);
            var8 = new BufferedImage(var13, var12, false, (Hashtable)null);
         }

         ColorConvertOp var16 = new ColorConvertOp((RenderingHints)null);
         var16.filter(var15, var8);
         if (var9.hasAlpha()) {
            copyBand(var14, var3.getNumBands() - 1, var1, this.getSampleModel().getNumBands() - 1);
            if (var9.isAlphaPremultiplied()) {
               GraphicsUtil.multiplyAlpha(var1);
            }
         }
      }

      return var1;
   }

   protected static ColorModel fixColorModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      if (var1 != null) {
         return var1.hasAlpha() ? new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8, 8}, true, var1.isAlphaPremultiplied(), 3, 0) : new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0);
      } else {
         SampleModel var2 = var0.getSampleModel();
         return var2.getNumBands() == 2 ? new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8, 8}, true, true, 3, 0) : new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0);
      }
   }

   protected static SampleModel fixSampleModel(CachableRed var0) {
      SampleModel var1 = var0.getSampleModel();
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      ColorModel var4 = var0.getColorModel();
      if (var4 != null) {
         return var4.hasAlpha() ? new PixelInterleavedSampleModel(0, var2, var3, 2, 2 * var2, new int[]{0, 1}) : new PixelInterleavedSampleModel(0, var2, var3, 1, var2, new int[]{0});
      } else {
         return var1.getNumBands() == 2 ? new PixelInterleavedSampleModel(0, var2, var3, 2, 2 * var2, new int[]{0, 1}) : new PixelInterleavedSampleModel(0, var2, var3, 1, var2, new int[]{0});
      }
   }
}
