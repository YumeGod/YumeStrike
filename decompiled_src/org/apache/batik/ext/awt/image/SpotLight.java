package org.apache.batik.ext.awt.image;

import java.awt.Color;

public class SpotLight extends AbstractLight {
   private double lightX;
   private double lightY;
   private double lightZ;
   private double pointAtX;
   private double pointAtY;
   private double pointAtZ;
   private double specularExponent;
   private double limitingConeAngle;
   private double limitingCos;
   private final double[] S = new double[3];

   public double getLightX() {
      return this.lightX;
   }

   public double getLightY() {
      return this.lightY;
   }

   public double getLightZ() {
      return this.lightZ;
   }

   public double getPointAtX() {
      return this.pointAtX;
   }

   public double getPointAtY() {
      return this.pointAtY;
   }

   public double getPointAtZ() {
      return this.pointAtZ;
   }

   public double getSpecularExponent() {
      return this.specularExponent;
   }

   public double getLimitingConeAngle() {
      return this.limitingConeAngle;
   }

   public SpotLight(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, Color var17) {
      super(var17);
      this.lightX = var1;
      this.lightY = var3;
      this.lightZ = var5;
      this.pointAtX = var7;
      this.pointAtY = var9;
      this.pointAtZ = var11;
      this.specularExponent = var13;
      this.limitingConeAngle = var15;
      this.limitingCos = Math.cos(Math.toRadians(var15));
      this.S[0] = var7 - var1;
      this.S[1] = var9 - var3;
      this.S[2] = var11 - var5;
      double var18 = 1.0 / Math.sqrt(this.S[0] * this.S[0] + this.S[1] * this.S[1] + this.S[2] * this.S[2]);
      double[] var10000 = this.S;
      var10000[0] *= var18;
      var10000 = this.S;
      var10000[1] *= var18;
      var10000 = this.S;
      var10000[2] *= var18;
   }

   public boolean isConstant() {
      return false;
   }

   public final double getLightBase(double var1, double var3, double var5, double[] var7) {
      double var8 = this.lightX - var1;
      double var10 = this.lightY - var3;
      double var12 = this.lightZ - var5;
      double var14 = 1.0 / Math.sqrt(var8 * var8 + var10 * var10 + var12 * var12);
      var8 *= var14;
      var10 *= var14;
      var12 *= var14;
      double var16 = -(var8 * this.S[0] + var10 * this.S[1] + var12 * this.S[2]);
      var7[0] = var8;
      var7[1] = var10;
      var7[2] = var12;
      if (var16 <= this.limitingCos) {
         return 0.0;
      } else {
         double var18 = this.limitingCos / var16;
         var18 *= var18;
         var18 *= var18;
         var18 *= var18;
         var18 *= var18;
         var18 *= var18;
         var18 *= var18;
         var18 = 1.0 - var18;
         return var18 * Math.pow(var16, this.specularExponent);
      }
   }

   public final void getLight(double var1, double var3, double var5, double[] var7) {
      double var8 = this.getLightBase(var1, var3, var5, var7);
      var7[0] *= var8;
      var7[1] *= var8;
      var7[2] *= var8;
   }

   public final void getLight4(double var1, double var3, double var5, double[] var7) {
      var7[3] = this.getLightBase(var1, var3, var5, var7);
   }

   public double[][] getLightRow4(double var1, double var3, double var5, int var7, double[][] var8, double[][] var9) {
      double[][] var10 = var9;
      if (var9 == null) {
         var10 = new double[var7][4];
      }

      for(int var11 = 0; var11 < var7; ++var11) {
         this.getLight4(var1, var3, var8[var11][3], var10[var11]);
         var1 += var5;
      }

      return var10;
   }
}
