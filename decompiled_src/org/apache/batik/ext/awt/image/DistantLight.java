package org.apache.batik.ext.awt.image;

import java.awt.Color;

public class DistantLight extends AbstractLight {
   private double azimuth;
   private double elevation;
   private double Lx;
   private double Ly;
   private double Lz;

   public double getAzimuth() {
      return this.azimuth;
   }

   public double getElevation() {
      return this.elevation;
   }

   public DistantLight(double var1, double var3, Color var5) {
      super(var5);
      this.azimuth = var1;
      this.elevation = var3;
      this.Lx = Math.cos(Math.toRadians(var1)) * Math.cos(Math.toRadians(var3));
      this.Ly = Math.sin(Math.toRadians(var1)) * Math.cos(Math.toRadians(var3));
      this.Lz = Math.sin(Math.toRadians(var3));
   }

   public boolean isConstant() {
      return true;
   }

   public void getLight(double var1, double var3, double var5, double[] var7) {
      var7[0] = this.Lx;
      var7[1] = this.Ly;
      var7[2] = this.Lz;
   }

   public double[][] getLightRow(double var1, double var3, double var5, int var7, double[][] var8, double[][] var9) {
      double[][] var10 = var9;
      if (var9 == null) {
         var10 = new double[var7][];
         double[] var11 = new double[]{this.Lx, this.Ly, this.Lz};

         for(int var12 = 0; var12 < var7; ++var12) {
            var10[var12] = var11;
         }
      } else {
         double var18 = this.Lx;
         double var13 = this.Ly;
         double var15 = this.Lz;

         for(int var17 = 0; var17 < var7; ++var17) {
            var10[var17][0] = var18;
            var10[var17][1] = var13;
            var10[var17][2] = var15;
         }
      }

      return var10;
   }
}
