package com.mxgraph.util;

import java.util.Arrays;

public class mxSpline1D {
   protected double[] xx;
   protected double[] yy;
   protected double[] a;
   protected double[] b;
   protected double[] c;
   protected double[] d;
   private int storageIndex = 0;

   public mxSpline1D(double[] var1, double[] var2) {
      this.setValues(var1, var2);
   }

   public void setValues(double[] var1, double[] var2) {
      this.xx = var1;
      this.yy = var2;
      if (var1.length > 1) {
         this.calculateCoefficients();
      }

   }

   public double getValue(double var1) {
      if (this.xx.length == 0) {
         return Double.NaN;
      } else if (this.xx.length == 1) {
         return this.xx[0] == var1 ? this.yy[0] : Double.NaN;
      } else {
         int var3 = Arrays.binarySearch(this.xx, var1);
         if (var3 > 0) {
            return this.yy[var3];
         } else {
            var3 = -(var3 + 1) - 1;
            return var3 < 0 ? this.yy[0] : this.a[var3] + this.b[var3] * (var1 - this.xx[var3]) + this.c[var3] * Math.pow(var1 - this.xx[var3], 2.0) + this.d[var3] * Math.pow(var1 - this.xx[var3], 3.0);
         }
      }
   }

   public double getFastValue(double var1) {
      if (this.storageIndex <= -1 || this.storageIndex >= this.xx.length - 1 || !(var1 > this.xx[this.storageIndex]) || !(var1 < this.xx[this.storageIndex + 1])) {
         int var3 = Arrays.binarySearch(this.xx, var1);
         if (var3 > 0) {
            return this.yy[var3];
         }

         var3 = -(var3 + 1) - 1;
         this.storageIndex = var3;
      }

      if (this.storageIndex < 0) {
         return this.yy[0];
      } else {
         double var5 = var1 - this.xx[this.storageIndex];
         return this.a[this.storageIndex] + this.b[this.storageIndex] * var5 + this.c[this.storageIndex] * var5 * var5 + this.d[this.storageIndex] * var5 * var5 * var5;
      }
   }

   public boolean checkValues() {
      return this.xx.length >= 2;
   }

   public double getDx(double var1) {
      if (this.xx.length != 0 && this.xx.length != 1) {
         int var3 = Arrays.binarySearch(this.xx, var1);
         if (var3 < 0) {
            var3 = -(var3 + 1) - 1;
         }

         return this.b[var3] + 2.0 * this.c[var3] * (var1 - this.xx[var3]) + 3.0 * this.d[var3] * Math.pow(var1 - this.xx[var3], 2.0);
      } else {
         return 0.0;
      }
   }

   private void calculateCoefficients() {
      int var1 = this.yy.length;
      this.a = new double[var1];
      this.b = new double[var1];
      this.c = new double[var1];
      this.d = new double[var1];
      if (var1 == 2) {
         this.a[0] = this.yy[0];
         this.b[0] = this.yy[1] - this.yy[0];
      } else {
         double[] var2 = new double[var1 - 1];

         for(int var3 = 0; var3 < var1 - 1; ++var3) {
            this.a[var3] = this.yy[var3];
            var2[var3] = this.xx[var3 + 1] - this.xx[var3];
            if (var2[var3] == 0.0) {
               var2[var3] = 0.01;
            }
         }

         this.a[var1 - 1] = this.yy[var1 - 1];
         double[][] var6 = new double[var1 - 2][var1 - 2];
         double[] var4 = new double[var1 - 2];

         int var5;
         for(var5 = 0; var5 < var1 - 2; ++var5) {
            var4[var5] = 3.0 * ((this.yy[var5 + 2] - this.yy[var5 + 1]) / var2[var5 + 1] - (this.yy[var5 + 1] - this.yy[var5]) / var2[var5]);
            var6[var5][var5] = 2.0 * (var2[var5] + var2[var5 + 1]);
            if (var5 > 0) {
               var6[var5][var5 - 1] = var2[var5];
            }

            if (var5 < var1 - 3) {
               var6[var5][var5 + 1] = var2[var5 + 1];
            }
         }

         this.solve(var6, var4);

         for(var5 = 0; var5 < var1 - 2; ++var5) {
            this.c[var5 + 1] = var4[var5];
            this.b[var5] = (this.a[var5 + 1] - this.a[var5]) / var2[var5] - (2.0 * this.c[var5] + this.c[var5 + 1]) / 3.0 * var2[var5];
            this.d[var5] = (this.c[var5 + 1] - this.c[var5]) / (3.0 * var2[var5]);
         }

         this.b[var1 - 2] = (this.a[var1 - 1] - this.a[var1 - 2]) / var2[var1 - 2] - (2.0 * this.c[var1 - 2] + this.c[var1 - 1]) / 3.0 * var2[var1 - 2];
         this.d[var1 - 2] = (this.c[var1 - 1] - this.c[var1 - 2]) / (3.0 * var2[var1 - 2]);
      }
   }

   public void solve(double[][] var1, double[] var2) {
      int var3 = var2.length;

      int var4;
      for(var4 = 1; var4 < var3; ++var4) {
         var1[var4][var4 - 1] /= var1[var4 - 1][var4 - 1];
         var1[var4][var4] -= var1[var4 - 1][var4] * var1[var4][var4 - 1];
         var2[var4] -= var1[var4][var4 - 1] * var2[var4 - 1];
      }

      var2[var3 - 1] /= var1[var3 - 1][var3 - 1];

      for(var4 = var2.length - 2; var4 >= 0; --var4) {
         var2[var4] = (var2[var4] - var1[var4][var4 + 1] * var2[var4 + 1]) / var1[var4][var4];
      }

   }
}
