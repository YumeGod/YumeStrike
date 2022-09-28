package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.Light;

public class DiffuseLightingRed extends AbstractRed {
   private double kd;
   private Light light;
   private BumpMap bumpMap;
   private double scaleX;
   private double scaleY;
   private Rectangle litRegion;
   private boolean linear;

   public DiffuseLightingRed(double var1, Light var3, BumpMap var4, Rectangle var5, double var6, double var8, boolean var10) {
      this.kd = var1;
      this.light = var3;
      this.bumpMap = var4;
      this.litRegion = var5;
      this.scaleX = var6;
      this.scaleY = var8;
      this.linear = var10;
      ColorModel var11;
      if (var10) {
         var11 = GraphicsUtil.Linear_sRGB_Pre;
      } else {
         var11 = GraphicsUtil.sRGB_Pre;
      }

      SampleModel var12 = var11.createCompatibleSampleModel(var5.width, var5.height);
      this.init((CachableRed)null, var5, var11, var12, var5.x, var5.y, (Map)null);
   }

   public WritableRaster copyData(WritableRaster var1) {
      double[] var2 = this.light.getColor(this.linear);
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      int var5 = var1.getMinX();
      int var6 = var1.getMinY();
      DataBufferInt var7 = (DataBufferInt)var1.getDataBuffer();
      int[] var8 = var7.getBankData()[0];
      SinglePixelPackedSampleModel var9 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var10 = var7.getOffset() + var9.getOffset(var5 - var1.getSampleModelTranslateX(), var6 - var1.getSampleModelTranslateY());
      int var11 = var9.getScanlineStride();
      int var12 = var11 - var3;
      int var13 = var10;
      boolean var14 = false;
      boolean var15 = false;
      boolean var16 = false;
      boolean var17 = false;
      boolean var18 = false;
      double var19 = this.scaleX * (double)var5;
      double var21 = this.scaleY * (double)var6;
      double var23 = 0.0;
      double[][][] var25 = this.bumpMap.getNormalArray(var5, var6, var3, var4);
      double[][] var27;
      double[] var28;
      int var30;
      int var31;
      int var32;
      int var33;
      int var34;
      if (!this.light.isConstant()) {
         double[][] var26 = new double[var3][3];

         for(var33 = 0; var33 < var4; ++var33) {
            var27 = var25[var33];
            this.light.getLightRow(var19, var21 + (double)var33 * this.scaleY, this.scaleX, var3, var27, var26);

            for(var34 = 0; var34 < var3; ++var34) {
               var28 = var27[var34];
               double[] var29 = var26[var34];
               var23 = 255.0 * this.kd * (var28[0] * var29[0] + var28[1] * var29[1] + var28[2] * var29[2]);
               var30 = (int)(var23 * var2[0]);
               var31 = (int)(var23 * var2[1]);
               var32 = (int)(var23 * var2[2]);
               if ((var30 & -256) != 0) {
                  var30 = (var30 & Integer.MIN_VALUE) != 0 ? 0 : 255;
               }

               if ((var31 & -256) != 0) {
                  var31 = (var31 & Integer.MIN_VALUE) != 0 ? 0 : 255;
               }

               if ((var32 & -256) != 0) {
                  var32 = (var32 & Integer.MIN_VALUE) != 0 ? 0 : 255;
               }

               var8[var13++] = -16777216 | var30 << 16 | var31 << 8 | var32;
            }

            var13 += var12;
         }
      } else {
         double[] var35 = new double[3];
         this.light.getLight(0.0, 0.0, 0.0, var35);

         for(var33 = 0; var33 < var4; ++var33) {
            var27 = var25[var33];

            for(var34 = 0; var34 < var3; ++var34) {
               var28 = var27[var34];
               var23 = 255.0 * this.kd * (var28[0] * var35[0] + var28[1] * var35[1] + var28[2] * var35[2]);
               var30 = (int)(var23 * var2[0]);
               var31 = (int)(var23 * var2[1]);
               var32 = (int)(var23 * var2[2]);
               if ((var30 & -256) != 0) {
                  var30 = (var30 & Integer.MIN_VALUE) != 0 ? 0 : 255;
               }

               if ((var31 & -256) != 0) {
                  var31 = (var31 & Integer.MIN_VALUE) != 0 ? 0 : 255;
               }

               if ((var32 & -256) != 0) {
                  var32 = (var32 & Integer.MIN_VALUE) != 0 ? 0 : 255;
               }

               var8[var13++] = -16777216 | var30 << 16 | var31 << 8 | var32;
            }

            var13 += var12;
         }
      }

      return var1;
   }
}
