package org.apache.batik.ext.awt.image;

import java.awt.Color;

public class PointLight extends AbstractLight {
   private double lightX;
   private double lightY;
   private double lightZ;

   public double getLightX() {
      return this.lightX;
   }

   public double getLightY() {
      return this.lightY;
   }

   public double getLightZ() {
      return this.lightZ;
   }

   public PointLight(double var1, double var3, double var5, Color var7) {
      super(var7);
      this.lightX = var1;
      this.lightY = var3;
      this.lightZ = var5;
   }

   public boolean isConstant() {
      return false;
   }

   public final void getLight(double var1, double var3, double var5, double[] var7) {
      double var8 = this.lightX - var1;
      double var10 = this.lightY - var3;
      double var12 = this.lightZ - var5;
      double var14 = Math.sqrt(var8 * var8 + var10 * var10 + var12 * var12);
      if (var14 > 0.0) {
         double var16 = 1.0 / var14;
         var8 *= var16;
         var10 *= var16;
         var12 *= var16;
      }

      var7[0] = var8;
      var7[1] = var10;
      var7[2] = var12;
   }
}
