package org.apache.batik.ext.awt.image;

import java.awt.Color;

public abstract class AbstractLight implements Light {
   private double[] color;

   public static final double sRGBToLsRGB(double var0) {
      return var0 <= 0.003928 ? var0 / 12.92 : Math.pow((var0 + 0.055) / 1.055, 2.4);
   }

   public double[] getColor(boolean var1) {
      double[] var2 = new double[3];
      if (var1) {
         var2[0] = sRGBToLsRGB(this.color[0]);
         var2[1] = sRGBToLsRGB(this.color[1]);
         var2[2] = sRGBToLsRGB(this.color[2]);
      } else {
         var2[0] = this.color[0];
         var2[1] = this.color[1];
         var2[2] = this.color[2];
      }

      return var2;
   }

   public AbstractLight(Color var1) {
      this.setColor(var1);
   }

   public void setColor(Color var1) {
      this.color = new double[3];
      this.color[0] = (double)var1.getRed() / 255.0;
      this.color[1] = (double)var1.getGreen() / 255.0;
      this.color[2] = (double)var1.getBlue() / 255.0;
   }

   public boolean isConstant() {
      return true;
   }

   public double[][][] getLightMap(double var1, double var3, double var5, double var7, int var9, int var10, double[][][] var11) {
      double[][][] var12 = new double[var10][][];

      for(int var13 = 0; var13 < var10; ++var13) {
         var12[var13] = this.getLightRow(var1, var3, var5, var9, var11[var13], (double[][])null);
         var3 += var7;
      }

      return var12;
   }

   public double[][] getLightRow(double var1, double var3, double var5, int var7, double[][] var8, double[][] var9) {
      double[][] var10 = var9;
      if (var9 == null) {
         var10 = new double[var7][3];
      }

      for(int var11 = 0; var11 < var7; ++var11) {
         this.getLight(var1, var3, var8[var11][3], var10[var11]);
         var1 += var5;
      }

      return var10;
   }
}
