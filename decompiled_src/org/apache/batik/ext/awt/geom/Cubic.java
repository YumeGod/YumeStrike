package org.apache.batik.ext.awt.geom;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

public class Cubic extends AbstractSegment {
   public Point2D.Double p1;
   public Point2D.Double p2;
   public Point2D.Double p3;
   public Point2D.Double p4;
   private static int count = 0;

   public Cubic() {
      this.p1 = new Point2D.Double();
      this.p2 = new Point2D.Double();
      this.p3 = new Point2D.Double();
      this.p4 = new Point2D.Double();
   }

   public Cubic(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.p1 = new Point2D.Double(var1, var3);
      this.p2 = new Point2D.Double(var5, var7);
      this.p3 = new Point2D.Double(var9, var11);
      this.p4 = new Point2D.Double(var13, var15);
   }

   public Cubic(Point2D.Double var1, Point2D.Double var2, Point2D.Double var3, Point2D.Double var4) {
      this.p1 = var1;
      this.p2 = var2;
      this.p3 = var3;
      this.p4 = var4;
   }

   public Object clone() {
      return new Cubic(new Point2D.Double(this.p1.x, this.p1.y), new Point2D.Double(this.p2.x, this.p2.y), new Point2D.Double(this.p3.x, this.p3.y), new Point2D.Double(this.p4.x, this.p4.y));
   }

   public Segment reverse() {
      return new Cubic(new Point2D.Double(this.p4.x, this.p4.y), new Point2D.Double(this.p3.x, this.p3.y), new Point2D.Double(this.p2.x, this.p2.y), new Point2D.Double(this.p1.x, this.p1.y));
   }

   private void getMinMax(double var1, double var3, double var5, double var7, double[] var9) {
      if (var7 > var1) {
         var9[0] = var1;
         var9[1] = var7;
      } else {
         var9[0] = var7;
         var9[1] = var1;
      }

      double var10 = 3.0 * (var3 - var1);
      double var12 = 6.0 * (var5 - var3);
      double var14 = 3.0 * (var7 - var5);
      double[] var16 = new double[]{var10, var12 - 2.0 * var10, var14 - var12 + var10};
      int var17 = QuadCurve2D.solveQuadratic(var16);

      for(int var18 = 0; var18 < var17; ++var18) {
         double var19 = var16[var18];
         if (!(var19 <= 0.0) && !(var19 >= 1.0)) {
            var19 = (1.0 - var19) * (1.0 - var19) * (1.0 - var19) * var1 + 3.0 * var19 * (1.0 - var19) * (1.0 - var19) * var3 + 3.0 * var19 * var19 * (1.0 - var19) * var5 + var19 * var19 * var19 * var7;
            if (var19 < var9[0]) {
               var9[0] = var19;
            } else if (var19 > var9[1]) {
               var9[1] = var19;
            }
         }
      }

   }

   public double minX() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.x, this.p2.x, this.p3.x, this.p4.x, var1);
      return var1[0];
   }

   public double maxX() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.x, this.p2.x, this.p3.x, this.p4.x, var1);
      return var1[1];
   }

   public double minY() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.y, this.p2.y, this.p3.y, this.p4.y, var1);
      return var1[0];
   }

   public double maxY() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.y, this.p2.y, this.p3.y, this.p4.y, var1);
      return var1[1];
   }

   public Rectangle2D getBounds2D() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.x, this.p2.x, this.p3.x, this.p4.x, var1);
      double[] var2 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.y, this.p2.y, this.p3.y, this.p4.y, var2);
      return new Rectangle2D.Double(var1[0], var2[0], var1[1] - var1[0], var2[1] - var2[0]);
   }

   protected int findRoots(double var1, double[] var3) {
      double[] var4 = new double[]{this.p1.y - var1, 3.0 * (this.p2.y - this.p1.y), 3.0 * (this.p1.y - 2.0 * this.p2.y + this.p3.y), 3.0 * this.p2.y - this.p1.y + this.p4.y - 3.0 * this.p3.y};
      return CubicCurve2D.solveCubic(var4, var3);
   }

   public Point2D.Double evalDt(double var1) {
      double var3 = 3.0 * ((this.p2.x - this.p1.x) * (1.0 - var1) * (1.0 - var1) + 2.0 * (this.p3.x - this.p2.x) * (1.0 - var1) * var1 + (this.p4.x - this.p3.x) * var1 * var1);
      double var5 = 3.0 * ((this.p2.y - this.p1.y) * (1.0 - var1) * (1.0 - var1) + 2.0 * (this.p3.y - this.p2.y) * (1.0 - var1) * var1 + (this.p4.y - this.p3.y) * var1 * var1);
      return new Point2D.Double(var3, var5);
   }

   public Point2D.Double eval(double var1) {
      double var3 = (1.0 - var1) * (1.0 - var1) * (1.0 - var1) * this.p1.x + 3.0 * (var1 * (1.0 - var1) * (1.0 - var1) * this.p2.x + var1 * var1 * (1.0 - var1) * this.p3.x) + var1 * var1 * var1 * this.p4.x;
      double var5 = (1.0 - var1) * (1.0 - var1) * (1.0 - var1) * this.p1.y + 3.0 * (var1 * (1.0 - var1) * (1.0 - var1) * this.p2.y + var1 * var1 * (1.0 - var1) * this.p3.y) + var1 * var1 * var1 * this.p4.y;
      return new Point2D.Double(var3, var5);
   }

   public void subdivide(Segment var1, Segment var2) {
      Cubic var3 = null;
      Cubic var4 = null;
      if (var1 instanceof Cubic) {
         var3 = (Cubic)var1;
      }

      if (var2 instanceof Cubic) {
         var4 = (Cubic)var2;
      }

      this.subdivide(var3, var4);
   }

   public void subdivide(double var1, Segment var3, Segment var4) {
      Cubic var5 = null;
      Cubic var6 = null;
      if (var3 instanceof Cubic) {
         var5 = (Cubic)var3;
      }

      if (var4 instanceof Cubic) {
         var6 = (Cubic)var4;
      }

      this.subdivide(var1, var5, var6);
   }

   public void subdivide(Cubic var1, Cubic var2) {
      if (var1 != null || var2 != null) {
         double var3 = (this.p1.x + 3.0 * (this.p2.x + this.p3.x) + this.p4.x) * 0.125;
         double var5 = (this.p1.y + 3.0 * (this.p2.y + this.p3.y) + this.p4.y) * 0.125;
         double var7 = (this.p2.x - this.p1.x + 2.0 * (this.p3.x - this.p2.x) + (this.p4.x - this.p3.x)) * 0.125;
         double var9 = (this.p2.y - this.p1.y + 2.0 * (this.p3.y - this.p2.y) + (this.p4.y - this.p3.y)) * 0.125;
         if (var1 != null) {
            var1.p1.x = this.p1.x;
            var1.p1.y = this.p1.y;
            var1.p2.x = (this.p2.x + this.p1.x) * 0.5;
            var1.p2.y = (this.p2.y + this.p1.y) * 0.5;
            var1.p3.x = var3 - var7;
            var1.p3.y = var5 - var9;
            var1.p4.x = var3;
            var1.p4.y = var5;
         }

         if (var2 != null) {
            var2.p1.x = var3;
            var2.p1.y = var5;
            var2.p2.x = var3 + var7;
            var2.p2.y = var5 + var9;
            var2.p3.x = (this.p4.x + this.p3.x) * 0.5;
            var2.p3.y = (this.p4.y + this.p3.y) * 0.5;
            var2.p4.x = this.p4.x;
            var2.p4.y = this.p4.y;
         }

      }
   }

   public void subdivide(double var1, Cubic var3, Cubic var4) {
      if (var3 != null || var4 != null) {
         Point2D.Double var5 = this.eval(var1);
         Point2D.Double var6 = this.evalDt(var1);
         if (var3 != null) {
            var3.p1.x = this.p1.x;
            var3.p1.y = this.p1.y;
            var3.p2.x = (this.p2.x + this.p1.x) * var1;
            var3.p2.y = (this.p2.y + this.p1.y) * var1;
            var3.p3.x = var5.x - var6.x * var1 / 3.0;
            var3.p3.y = var5.y - var6.y * var1 / 3.0;
            var3.p4.x = var5.x;
            var3.p4.y = var5.y;
         }

         if (var4 != null) {
            var4.p1.x = var5.x;
            var4.p1.y = var5.y;
            var4.p2.x = var5.x + var6.x * (1.0 - var1) / 3.0;
            var4.p2.y = var5.y + var6.y * (1.0 - var1) / 3.0;
            var4.p3.x = (this.p4.x + this.p3.x) * (1.0 - var1);
            var4.p3.y = (this.p4.y + this.p3.y) * (1.0 - var1);
            var4.p4.x = this.p4.x;
            var4.p4.y = this.p4.y;
         }

      }
   }

   public Segment getSegment(double var1, double var3) {
      double var5 = var3 - var1;
      Point2D.Double var7 = this.eval(var1);
      Point2D.Double var8 = this.evalDt(var1);
      Point2D.Double var9 = new Point2D.Double(var7.x + var5 * var8.x / 3.0, var7.y + var5 * var8.y / 3.0);
      Point2D.Double var10 = this.eval(var3);
      Point2D.Double var11 = this.evalDt(var3);
      Point2D.Double var12 = new Point2D.Double(var10.x - var5 * var11.x / 3.0, var10.y - var5 * var11.y / 3.0);
      return new Cubic(var7, var9, var12, var10);
   }

   protected double subLength(double var1, double var3, double var5) {
      ++count;
      double var7 = this.p3.x - this.p2.x;
      double var9 = this.p3.y - this.p2.y;
      double var15 = Math.sqrt(var7 * var7 + var9 * var9);
      double var11 = this.p4.x - this.p1.x;
      double var13 = this.p4.y - this.p1.y;
      double var17 = Math.sqrt(var11 * var11 + var13 * var13);
      double var19 = var1 + var3 + var15;
      if (var19 < var5) {
         return (var19 + var17) / 2.0;
      } else {
         double var21 = var19 - var17;
         if (var21 < var5) {
            return (var19 + var17) / 2.0;
         } else {
            Cubic var23 = new Cubic();
            double var24 = (this.p1.x + 3.0 * (this.p2.x + this.p3.x) + this.p4.x) * 0.125;
            double var26 = (this.p1.y + 3.0 * (this.p2.y + this.p3.y) + this.p4.y) * 0.125;
            double var28 = (var7 + var11) * 0.125;
            double var30 = (var9 + var13) * 0.125;
            var23.p1.x = this.p1.x;
            var23.p1.y = this.p1.y;
            var23.p2.x = (this.p2.x + this.p1.x) * 0.5;
            var23.p2.y = (this.p2.y + this.p1.y) * 0.5;
            var23.p3.x = var24 - var28;
            var23.p3.y = var26 - var30;
            var23.p4.x = var24;
            var23.p4.y = var26;
            double var32 = Math.sqrt(var28 * var28 + var30 * var30);
            double var34 = var23.subLength(var1 / 2.0, var32, var5 / 2.0);
            var23.p1.x = var24;
            var23.p1.y = var26;
            var23.p2.x = var24 + var28;
            var23.p2.y = var26 + var30;
            var23.p3.x = (this.p4.x + this.p3.x) * 0.5;
            var23.p3.y = (this.p4.y + this.p3.y) * 0.5;
            var23.p4.x = this.p4.x;
            var23.p4.y = this.p4.y;
            var34 += var23.subLength(var32, var3 / 2.0, var5 / 2.0);
            return var34;
         }
      }
   }

   public double getLength() {
      return this.getLength(1.0E-6);
   }

   public double getLength(double var1) {
      double var3 = this.p2.x - this.p1.x;
      double var5 = this.p2.y - this.p1.y;
      double var7 = Math.sqrt(var3 * var3 + var5 * var5);
      var3 = this.p4.x - this.p3.x;
      var5 = this.p4.y - this.p3.y;
      double var9 = Math.sqrt(var3 * var3 + var5 * var5);
      var3 = this.p3.x - this.p2.x;
      var5 = this.p3.y - this.p2.y;
      double var11 = Math.sqrt(var3 * var3 + var5 * var5);
      double var13 = var1 * (var7 + var9 + var11);
      return this.subLength(var7, var9, var13);
   }

   public String toString() {
      return "M" + this.p1.x + ',' + this.p1.y + 'C' + this.p2.x + ',' + this.p2.y + ' ' + this.p3.x + ',' + this.p3.y + ' ' + this.p4.x + ',' + this.p4.y;
   }
}
