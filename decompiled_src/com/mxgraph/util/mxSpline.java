package com.mxgraph.util;

import java.util.Iterator;
import java.util.List;

public class mxSpline {
   private double[] t;
   private mxSpline1D splineX;
   private mxSpline1D splineY;
   private double length;

   public mxSpline(List var1) {
      if (var1 != null) {
         double[] var2 = new double[var1.size()];
         double[] var3 = new double[var1.size()];
         int var4 = 0;

         mxPoint var6;
         for(Iterator var5 = var1.iterator(); var5.hasNext(); var3[var4++] = var6.getY()) {
            var6 = (mxPoint)var5.next();
            var2[var4] = var6.getX();
         }

         this.init(var2, var3);
      }

   }

   public void Spline2D(double[] var1, double[] var2) {
      this.init(var1, var2);
   }

   protected void init(double[] var1, double[] var2) {
      if (var1.length == var2.length) {
         if (var1.length >= 2) {
            this.t = new double[var1.length];
            this.t[0] = 0.0;
            this.length = 0.0;

            int var3;
            for(var3 = 1; var3 < this.t.length; ++var3) {
               double var4 = var1[var3] - var1[var3 - 1];
               double var6 = var2[var3] - var2[var3 - 1];
               if (0.0 == var4) {
                  this.t[var3] = Math.abs(var6);
               } else if (0.0 == var6) {
                  this.t[var3] = Math.abs(var4);
               } else {
                  this.t[var3] = Math.sqrt(var4 * var4 + var6 * var6);
               }

               this.length += this.t[var3];
               double[] var10000 = this.t;
               var10000[var3] += this.t[var3 - 1];
            }

            for(var3 = 1; var3 < this.t.length - 1; ++var3) {
               this.t[var3] /= this.length;
            }

            this.t[this.t.length - 1] = 1.0;
            this.splineX = new mxSpline1D(this.t, var1);
            this.splineY = new mxSpline1D(this.t, var2);
         }
      }
   }

   public mxPoint getPoint(double var1) {
      mxPoint var3 = new mxPoint(this.splineX.getValue(var1), this.splineY.getValue(var1));
      return var3;
   }

   public boolean checkValues() {
      return this.splineX.checkValues() && this.splineY.checkValues();
   }

   public double getDx(double var1) {
      return this.splineX.getDx(var1);
   }

   public double getDy(double var1) {
      return this.splineY.getDx(var1);
   }

   public mxSpline1D getSplineX() {
      return this.splineX;
   }

   public mxSpline1D getSplineY() {
      return this.splineY;
   }

   public double getLength() {
      return this.length;
   }
}
