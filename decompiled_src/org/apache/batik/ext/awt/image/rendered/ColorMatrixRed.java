package org.apache.batik.ext.awt.image.rendered;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class ColorMatrixRed extends AbstractRed {
   private float[][] matrix;

   public float[][] getMatrix() {
      return this.copyMatrix(this.matrix);
   }

   public void setMatrix(float[][] var1) {
      float[][] var2 = this.copyMatrix(var1);
      if (var2 == null) {
         throw new IllegalArgumentException();
      } else if (var2.length != 4) {
         throw new IllegalArgumentException();
      } else {
         for(int var3 = 0; var3 < 4; ++var3) {
            if (var2[var3].length != 5) {
               throw new IllegalArgumentException(var3 + " : " + var2[var3].length);
            }
         }

         this.matrix = var1;
      }
   }

   private float[][] copyMatrix(float[][] var1) {
      if (var1 == null) {
         return (float[][])null;
      } else {
         float[][] var2 = new float[var1.length][];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3] != null) {
               var2[var3] = new float[var1[var3].length];
               System.arraycopy(var1[var3], 0, var2[var3], 0, var1[var3].length);
            }
         }

         return var2;
      }
   }

   public ColorMatrixRed(CachableRed var1, float[][] var2) {
      this.setMatrix(var2);
      ColorModel var3 = var1.getColorModel();
      ColorSpace var4 = null;
      if (var3 != null) {
         var4 = var3.getColorSpace();
      }

      ColorModel var5;
      if (var4 == null) {
         var5 = GraphicsUtil.Linear_sRGB_Unpre;
      } else if (var4 == ColorSpace.getInstance(1004)) {
         var5 = GraphicsUtil.Linear_sRGB_Unpre;
      } else {
         var5 = GraphicsUtil.sRGB_Unpre;
      }

      SampleModel var6 = var5.createCompatibleSampleModel(var1.getWidth(), var1.getHeight());
      this.init(var1, var1.getBounds(), var5, var6, var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      var1 = var2.copyData(var1);
      ColorModel var3 = var2.getColorModel();
      GraphicsUtil.coerceData(var1, var3, false);
      int var4 = var1.getMinX();
      int var5 = var1.getMinY();
      int var6 = var1.getWidth();
      int var7 = var1.getHeight();
      DataBufferInt var8 = (DataBufferInt)var1.getDataBuffer();
      int[] var9 = var8.getBankData()[0];
      SinglePixelPackedSampleModel var10 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var11 = var8.getOffset() + var10.getOffset(var4 - var1.getSampleModelTranslateX(), var5 - var1.getSampleModelTranslateY());
      int var12 = ((SinglePixelPackedSampleModel)var1.getSampleModel()).getScanlineStride();
      int var13 = var12 - var6;
      int var14 = var11;
      boolean var15 = false;
      boolean var16 = false;
      float var17 = this.matrix[0][0] / 255.0F;
      float var18 = this.matrix[0][1] / 255.0F;
      float var19 = this.matrix[0][2] / 255.0F;
      float var20 = this.matrix[0][3] / 255.0F;
      float var21 = this.matrix[0][4] / 255.0F;
      float var22 = this.matrix[1][0] / 255.0F;
      float var23 = this.matrix[1][1] / 255.0F;
      float var24 = this.matrix[1][2] / 255.0F;
      float var25 = this.matrix[1][3] / 255.0F;
      float var26 = this.matrix[1][4] / 255.0F;
      float var27 = this.matrix[2][0] / 255.0F;
      float var28 = this.matrix[2][1] / 255.0F;
      float var29 = this.matrix[2][2] / 255.0F;
      float var30 = this.matrix[2][3] / 255.0F;
      float var31 = this.matrix[2][4] / 255.0F;
      float var32 = this.matrix[3][0] / 255.0F;
      float var33 = this.matrix[3][1] / 255.0F;
      float var34 = this.matrix[3][2] / 255.0F;
      float var35 = this.matrix[3][3] / 255.0F;
      float var36 = this.matrix[3][4] / 255.0F;

      for(int var46 = 0; var46 < var7; ++var46) {
         for(int var47 = 0; var47 < var6; ++var47) {
            int var37 = var9[var14];
            int var38 = var37 >>> 24;
            int var39 = var37 >> 16 & 255;
            int var40 = var37 >> 8 & 255;
            int var41 = var37 & 255;
            int var42 = (int)((var17 * (float)var39 + var18 * (float)var40 + var19 * (float)var41 + var20 * (float)var38 + var21) * 255.0F);
            int var43 = (int)((var22 * (float)var39 + var23 * (float)var40 + var24 * (float)var41 + var25 * (float)var38 + var26) * 255.0F);
            int var44 = (int)((var27 * (float)var39 + var28 * (float)var40 + var29 * (float)var41 + var30 * (float)var38 + var31) * 255.0F);
            int var45 = (int)((var32 * (float)var39 + var33 * (float)var40 + var34 * (float)var41 + var35 * (float)var38 + var36) * 255.0F);
            if ((var42 & -256) != 0) {
               var42 = (var42 & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            if ((var43 & -256) != 0) {
               var43 = (var43 & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            if ((var44 & -256) != 0) {
               var44 = (var44 & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            if ((var45 & -256) != 0) {
               var45 = (var45 & Integer.MIN_VALUE) != 0 ? 0 : 255;
            }

            var9[var14++] = var45 << 24 | var42 << 16 | var43 << 8 | var44;
         }

         var14 += var13;
      }

      return var1;
   }
}
