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
import org.apache.batik.ext.awt.image.SpotLight;

public class SpecularLightingRed extends AbstractTiledRed {
   private double ks;
   private double specularExponent;
   private Light light;
   private BumpMap bumpMap;
   private double scaleX;
   private double scaleY;
   private Rectangle litRegion;
   private boolean linear;

   public SpecularLightingRed(double var1, double var3, Light var5, BumpMap var6, Rectangle var7, double var8, double var10, boolean var12) {
      this.ks = var1;
      this.specularExponent = var3;
      this.light = var5;
      this.bumpMap = var6;
      this.litRegion = var7;
      this.scaleX = var8;
      this.scaleY = var10;
      this.linear = var12;
      ColorModel var13;
      if (var12) {
         var13 = GraphicsUtil.Linear_sRGB_Unpre;
      } else {
         var13 = GraphicsUtil.sRGB_Unpre;
      }

      int var14 = var7.width;
      int var15 = var7.height;
      int var16 = AbstractTiledRed.getDefaultTileSize();
      if (var14 > var16) {
         var14 = var16;
      }

      if (var15 > var16) {
         var15 = var16;
      }

      SampleModel var17 = var13.createCompatibleSampleModel(var14, var15);
      this.init((CachableRed)null, var7, var13, var17, var7.x, var7.y, (Map)null);
   }

   public WritableRaster copyData(WritableRaster var1) {
      this.copyToRaster(var1);
      return var1;
   }

   public void genRect(WritableRaster var1) {
      double var2 = this.scaleX;
      double var4 = this.scaleY;
      double[] var6 = this.light.getColor(this.linear);
      int var7 = var1.getWidth();
      int var8 = var1.getHeight();
      int var9 = var1.getMinX();
      int var10 = var1.getMinY();
      DataBufferInt var11 = (DataBufferInt)var1.getDataBuffer();
      int[] var12 = var11.getBankData()[0];
      SinglePixelPackedSampleModel var13 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var14 = var11.getOffset() + var13.getOffset(var9 - var1.getSampleModelTranslateX(), var10 - var1.getSampleModelTranslateY());
      int var15 = var13.getScanlineStride();
      int var16 = var15 - var7;
      int var17 = var14;
      boolean var18 = false;
      boolean var19 = false;
      boolean var20 = false;
      double var21 = var2 * (double)var9;
      double var23 = var4 * (double)var10;
      double var25 = 0.0;
      boolean var27 = false;
      double var29 = var6[0] > var6[1] ? var6[0] : var6[1];
      var29 = var29 > var6[2] ? var29 : var6[2];
      double var31 = 255.0 / var29;
      int var46 = (int)(var6[0] * var31 + 0.5);
      int var28 = (int)(var6[1] * var31 + 0.5);
      var46 = var46 << 8 | var28;
      var28 = (int)(var6[2] * var31 + 0.5);
      var46 = var46 << 8 | var28;
      var29 *= 255.0 * this.ks;
      double[][][] var33 = this.bumpMap.getNormalArray(var9, var10, var7, var8);
      int var10002;
      double[][] var35;
      double[] var37;
      int var43;
      int var44;
      int var45;
      if (this.light instanceof SpotLight) {
         SpotLight var34 = (SpotLight)this.light;
         var35 = new double[var7][4];

         for(var44 = 0; var44 < var8; ++var44) {
            double[][] var36 = var33[var44];
            var34.getLightRow4(var21, var23 + (double)var44 * var4, var2, var7, var36, var35);

            for(var45 = 0; var45 < var7; ++var45) {
               var37 = var36[var45];
               double[] var38 = var35[var45];
               double var39 = var38[3];
               if (var39 == 0.0) {
                  var43 = 0;
               } else {
                  var10002 = var38[2]++;
                  var25 = var38[0] * var38[0] + var38[1] * var38[1] + var38[2] * var38[2];
                  var25 = Math.sqrt(var25);
                  double var41 = var37[0] * var38[0] + var37[1] * var38[1] + var37[2] * var38[2];
                  var39 *= Math.pow(var41 / var25, this.specularExponent);
                  var43 = (int)(var29 * var39 + 0.5);
                  if ((var43 & -256) != 0) {
                     var43 = (var43 & Integer.MIN_VALUE) != 0 ? 0 : 255;
                  }
               }

               var12[var17++] = var43 << 24 | var46;
            }

            var17 += var16;
         }
      } else {
         double[] var49;
         if (!this.light.isConstant()) {
            double[][] var47 = new double[var7][4];

            for(var44 = 0; var44 < var8; ++var44) {
               var35 = var33[var44];
               this.light.getLightRow(var21, var23 + (double)var44 * var4, var2, var7, var35, var47);

               for(var45 = 0; var45 < var7; ++var45) {
                  var49 = var35[var45];
                  var37 = var47[var45];
                  var10002 = var37[2]++;
                  var25 = var37[0] * var37[0] + var37[1] * var37[1] + var37[2] * var37[2];
                  var25 = Math.sqrt(var25);
                  double var50 = var49[0] * var37[0] + var49[1] * var37[1] + var49[2] * var37[2];
                  var25 = Math.pow(var50 / var25, this.specularExponent);
                  var43 = (int)(var29 * var25 + 0.5);
                  if ((var43 & -256) != 0) {
                     var43 = (var43 & Integer.MIN_VALUE) != 0 ? 0 : 255;
                  }

                  var12[var17++] = var43 << 24 | var46;
               }

               var17 += var16;
            }
         } else {
            double[] var48 = new double[3];
            this.light.getLight(0.0, 0.0, 0.0, var48);
            var10002 = var48[2]++;
            var25 = Math.sqrt(var48[0] * var48[0] + var48[1] * var48[1] + var48[2] * var48[2]);
            if (var25 > 0.0) {
               var48[0] /= var25;
               var48[1] /= var25;
               var48[2] /= var25;
            }

            for(var44 = 0; var44 < var8; ++var44) {
               var35 = var33[var44];

               for(var45 = 0; var45 < var7; ++var45) {
                  var49 = var35[var45];
                  var43 = (int)(var29 * Math.pow(var49[0] * var48[0] + var49[1] * var48[1] + var49[2] * var48[2], this.specularExponent) + 0.5);
                  if ((var43 & -256) != 0) {
                     var43 = (var43 & Integer.MIN_VALUE) != 0 ? 0 : 255;
                  }

                  var12[var17++] = var43 << 24 | var46;
               }

               var17 += var16;
            }
         }
      }

   }
}
