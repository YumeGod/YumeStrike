package org.apache.batik.ext.awt.geom;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

public class Quadradic extends AbstractSegment {
   public Point2D.Double p1;
   public Point2D.Double p2;
   public Point2D.Double p3;
   static int count = 0;

   public Quadradic() {
      this.p1 = new Point2D.Double();
      this.p2 = new Point2D.Double();
      this.p3 = new Point2D.Double();
   }

   public Quadradic(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.p1 = new Point2D.Double(var1, var3);
      this.p2 = new Point2D.Double(var5, var7);
      this.p3 = new Point2D.Double(var9, var11);
   }

   public Quadradic(Point2D.Double var1, Point2D.Double var2, Point2D.Double var3) {
      this.p1 = var1;
      this.p2 = var2;
      this.p3 = var3;
   }

   public Object clone() {
      return new Quadradic(new Point2D.Double(this.p1.x, this.p1.y), new Point2D.Double(this.p2.x, this.p2.y), new Point2D.Double(this.p3.x, this.p3.y));
   }

   public Segment reverse() {
      return new Quadradic(new Point2D.Double(this.p3.x, this.p3.y), new Point2D.Double(this.p2.x, this.p2.y), new Point2D.Double(this.p1.x, this.p1.y));
   }

   private void getMinMax(double var1, double var3, double var5, double[] var7) {
      if (var5 > var1) {
         var7[0] = var1;
         var7[1] = var5;
      } else {
         var7[0] = var5;
         var7[1] = var1;
      }

      double var8 = var1 - 2.0 * var3 + var5;
      double var10 = var3 - var1;
      if (var8 != 0.0) {
         double var12 = var10 / var8;
         if (!(var12 <= 0.0) && !(var12 >= 1.0)) {
            var12 = ((var1 - 2.0 * var3 + var5) * var12 + 2.0 * (var3 - var1)) * var12 + var1;
            if (var12 < var7[0]) {
               var7[0] = var12;
            } else if (var12 > var7[1]) {
               var7[1] = var12;
            }

         }
      }
   }

   public double minX() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.x, this.p2.x, this.p3.x, var1);
      return var1[0];
   }

   public double maxX() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.x, this.p2.x, this.p3.x, var1);
      return var1[1];
   }

   public double minY() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.y, this.p2.y, this.p3.y, var1);
      return var1[0];
   }

   public double maxY() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.y, this.p2.y, this.p3.y, var1);
      return var1[1];
   }

   public Rectangle2D getBounds2D() {
      double[] var1 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.x, this.p2.x, this.p3.x, var1);
      double[] var2 = new double[]{0.0, 0.0};
      this.getMinMax(this.p1.y, this.p2.y, this.p3.y, var2);
      return new Rectangle2D.Double(var1[0], var2[0], var1[1] - var1[0], var2[1] - var2[0]);
   }

   protected int findRoots(double var1, double[] var3) {
      double[] var4 = new double[]{this.p1.y - var1, 2.0 * (this.p2.y - this.p1.y), this.p1.y - 2.0 * this.p2.y + this.p3.y};
      return QuadCurve2D.solveQuadratic(var4, var3);
   }

   public Point2D.Double evalDt(double var1) {
      double var3 = 2.0 * (this.p1.x - 2.0 * this.p2.x + this.p3.x) * var1 + 2.0 * (this.p2.x - this.p1.x);
      double var5 = 2.0 * (this.p1.y - 2.0 * this.p2.y + this.p3.y) * var1 + 2.0 * (this.p2.y - this.p1.y);
      return new Point2D.Double(var3, var5);
   }

   public Point2D.Double eval(double var1) {
      double var3 = ((this.p1.x - 2.0 * this.p2.x + this.p3.x) * var1 + 2.0 * (this.p2.x - this.p1.x)) * var1 + this.p1.x;
      double var5 = ((this.p1.y - 2.0 * this.p2.y + this.p3.y) * var1 + 2.0 * (this.p2.y - this.p1.y)) * var1 + this.p1.y;
      return new Point2D.Double(var3, var5);
   }

   public Segment getSegment(double var1, double var3) {
      double var5 = var3 - var1;
      Point2D.Double var7 = this.eval(var1);
      Point2D.Double var8 = this.evalDt(var1);
      Point2D.Double var9 = new Point2D.Double(var7.x + 0.5 * var5 * var8.x, var7.y + 0.5 * var5 * var8.y);
      Point2D.Double var10 = this.eval(var3);
      return new Quadradic(var7, var9, var10);
   }

   public void subdivide(Quadradic var1, Quadradic var2) {
      if (var1 != null || var2 != null) {
         double var3 = (this.p1.x - 2.0 * this.p2.x + this.p3.x) * 0.25 + (this.p2.x - this.p1.x) + this.p1.x;
         double var5 = (this.p1.y - 2.0 * this.p2.y + this.p3.y) * 0.25 + (this.p2.y - this.p1.y) + this.p1.y;
         double var7 = (this.p1.x - 2.0 * this.p2.x + this.p3.x) * 0.25 + (this.p2.x - this.p1.x) * 0.5;
         double var9 = (this.p1.y - 2.0 * this.p2.y + this.p3.y) * 0.25 + (this.p2.y - this.p1.y) * 0.5;
         if (var1 != null) {
            var1.p1.x = this.p1.x;
            var1.p1.y = this.p1.y;
            var1.p2.x = var3 - var7;
            var1.p2.y = var5 - var9;
            var1.p3.x = var3;
            var1.p3.y = var5;
         }

         if (var2 != null) {
            var2.p1.x = var3;
            var2.p1.y = var5;
            var2.p2.x = var3 + var7;
            var2.p2.y = var5 + var9;
            var2.p3.x = this.p3.x;
            var2.p3.y = this.p3.y;
         }

      }
   }

   public void subdivide(double var1, Quadradic var3, Quadradic var4) {
      Point2D.Double var5 = this.eval(var1);
      Point2D.Double var6 = this.evalDt(var1);
      if (var3 != null) {
         var3.p1.x = this.p1.x;
         var3.p1.y = this.p1.y;
         var3.p2.x = var5.x - var6.x * var1 * 0.5;
         var3.p2.y = var5.y - var6.y * var1 * 0.5;
         var3.p3.x = var5.x;
         var3.p3.y = var5.y;
      }

      if (var4 != null) {
         var4.p1.x = var5.x;
         var4.p1.y = var5.y;
         var4.p2.x = var5.x + var6.x * (1.0 - var1) * 0.5;
         var4.p2.y = var5.y + var6.y * (1.0 - var1) * 0.5;
         var4.p3.x = this.p3.x;
         var4.p3.y = this.p3.y;
      }

   }

   public void subdivide(Segment var1, Segment var2) {
      Quadradic var3 = null;
      Quadradic var4 = null;
      if (var1 instanceof Quadradic) {
         var3 = (Quadradic)var1;
      }

      if (var2 instanceof Quadradic) {
         var4 = (Quadradic)var2;
      }

      this.subdivide(var3, var4);
   }

   public void subdivide(double var1, Segment var3, Segment var4) {
      Quadradic var5 = null;
      Quadradic var6 = null;
      if (var3 instanceof Quadradic) {
         var5 = (Quadradic)var3;
      }

      if (var4 instanceof Quadradic) {
         var6 = (Quadradic)var4;
      }

      this.subdivide(var1, var5, var6);
   }

   protected double subLength(double var1, double var3, double var5) {
      ++count;
      double var7 = this.p3.x - this.p1.x;
      double var9 = this.p3.y - this.p1.y;
      double var11 = Math.sqrt(var7 * var7 + var9 * var9);
      double var13 = var1 + var3;
      if (var13 < var5) {
         return (var13 + var11) * 0.5;
      } else {
         double var15 = var13 - var11;
         if (var15 < var5) {
            return (var13 + var11) * 0.5;
         } else {
            Quadradic var17 = new Quadradic();
            double var18 = (this.p1.x + 2.0 * this.p2.x + this.p3.x) * 0.25;
            double var20 = (this.p1.y + 2.0 * this.p2.y + this.p3.y) * 0.25;
            var7 = 0.25 * var7;
            var9 = 0.25 * var9;
            var17.p1.x = this.p1.x;
            var17.p1.y = this.p1.y;
            var17.p2.x = var18 - var7;
            var17.p2.y = var20 - var9;
            var17.p3.x = var18;
            var17.p3.y = var20;
            double var22 = 0.25 * var11;
            double var24 = var17.subLength(var1 * 0.5, var22, var5 * 0.5);
            var17.p1.x = var18;
            var17.p1.y = var20;
            var17.p2.x = var18 + var7;
            var17.p2.y = var20 + var9;
            var17.p3.x = this.p3.x;
            var17.p3.y = this.p3.y;
            var24 += var17.subLength(var22, var3 * 0.5, var5 * 0.5);
            return var24;
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
      var3 = this.p3.x - this.p2.x;
      var5 = this.p3.y - this.p2.y;
      double var9 = Math.sqrt(var3 * var3 + var5 * var5);
      double var11 = var1 * (var7 + var9);
      return this.subLength(var7, var9, var11);
   }

   public String toString() {
      return "M" + this.p1.x + ',' + this.p1.y + 'Q' + this.p2.x + ',' + this.p2.y + ' ' + this.p3.x + ',' + this.p3.y;
   }
}
