package org.apache.batik.ext.awt.image.rendered;

import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.ColorSpaceHintKey;

public class FilterAsAlphaRed extends AbstractRed {
   public FilterAsAlphaRed(CachableRed var1) {
      super((CachableRed)(new Any2LumRed(var1)), var1.getBounds(), new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0), new PixelInterleavedSampleModel(0, var1.getSampleModel().getWidth(), var1.getSampleModel().getHeight(), 1, var1.getSampleModel().getWidth(), new int[]{0}), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
      this.props.put("org.apache.batik.gvt.filter.Colorspace", ColorSpaceHintKey.VALUE_COLORSPACE_ALPHA);
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      SampleModel var3 = var2.getSampleModel();
      if (var3.getNumBands() == 1) {
         return var2.copyData(var1);
      } else {
         Raster var4 = var2.getData(var1.getBounds());
         PixelInterleavedSampleModel var5 = (PixelInterleavedSampleModel)var4.getSampleModel();
         DataBufferByte var6 = (DataBufferByte)var4.getDataBuffer();
         byte[] var7 = var6.getData();
         PixelInterleavedSampleModel var8 = (PixelInterleavedSampleModel)var1.getSampleModel();
         DataBufferByte var9 = (DataBufferByte)var1.getDataBuffer();
         byte[] var10 = var9.getData();
         int var11 = var4.getMinX() - var4.getSampleModelTranslateX();
         int var12 = var4.getMinY() - var4.getSampleModelTranslateY();
         int var13 = var1.getMinX() - var1.getSampleModelTranslateX();
         int var14 = var13 + var1.getWidth() - 1;
         int var15 = var1.getMinY() - var1.getSampleModelTranslateY();
         int var16 = var5.getPixelStride();
         int[] var17 = var5.getBandOffsets();
         int var18 = var17[0];
         int var19 = var17[1];
         int var20;
         int var21;
         int var22;
         int var23;
         if (var2.getColorModel().isAlphaPremultiplied()) {
            for(var20 = 0; var20 < var4.getHeight(); ++var20) {
               var21 = var6.getOffset() + var5.getOffset(var11, var12);
               var22 = var9.getOffset() + var8.getOffset(var13, var15);
               var23 = var9.getOffset() + var8.getOffset(var14 + 1, var15);

               for(var21 += var18; var22 < var23; var21 += var16) {
                  var10[var22++] = var7[var21];
               }

               ++var12;
               ++var15;
            }
         } else {
            var19 -= var18;

            for(var20 = 0; var20 < var4.getHeight(); ++var20) {
               var21 = var6.getOffset() + var5.getOffset(var11, var12);
               var22 = var9.getOffset() + var8.getOffset(var13, var15);
               var23 = var9.getOffset() + var8.getOffset(var14 + 1, var15);

               for(var21 += var18; var22 < var23; var21 += var16) {
                  int var24 = var7[var21] & 255;
                  int var25 = var7[var21 + var19] & 255;
                  var10[var22++] = (byte)(var24 * var25 + 128 >> 8);
               }

               ++var12;
               ++var15;
            }
         }

         return var1;
      }
   }
}
