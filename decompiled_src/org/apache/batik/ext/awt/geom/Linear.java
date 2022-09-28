package org.apache.batik.ext.awt.geom;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Linear implements Segment {
   public Point2D.Double p1;
   public Point2D.Double p2;

   public Linear() {
      this.p1 = new Point2D.Double();
      this.p2 = new Point2D.Double();
   }

   public Linear(double var1, double var3, double var5, double var7) {
      this.p1 = new Point2D.Double(var1, var3);
      this.p2 = new Point2D.Double(var5, var7);
   }

   public Linear(Point2D.Double var1, Point2D.Double var2) {
      this.p1 = var1;
      this.p2 = var2;
   }

   public Object clone() {
      return new Linear(new Point2D.Double(this.p1.x, this.p1.y), new Point2D.Double(this.p2.x, this.p2.y));
   }

   public Segment reverse() {
      return new Linear(new Point2D.Double(this.p2.x, this.p2.y), new Point2D.Double(this.p1.x, this.p1.y));
   }

   public double minX() {
      return this.p1.x < this.p2.x ? this.p1.x : this.p2.x;
   }

   public double maxX() {
      return this.p1.x > this.p2.x ? this.p1.x : this.p2.x;
   }

   public double minY() {
      return this.p1.y < this.p2.y ? this.p1.y : this.p2.y;
   }

   public double maxY() {
      return this.p1.y > this.p2.y ? this.p2.y : this.p1.y;
   }

   public Rectangle2D getBounds2D() {
      double var1;
      double var5;
      if (this.p1.x < this.p2.x) {
         var1 = this.p1.x;
         var5 = this.p2.x - this.p1.x;
      } else {
         var1 = this.p2.x;
         var5 = this.p1.x - this.p2.x;
      }

      double var3;
      double var7;
      if (this.p1.y < this.p2.y) {
         var3 = this.p1.y;
         var7 = this.p2.y - this.p1.y;
      } else {
         var3 = this.p2.y;
         var7 = this.p1.y - this.p2.y;
      }

      return new Rectangle2D.Double(var1, var3, var5, var7);
   }

   public Point2D.Double evalDt(double var1) {
      double var3 = this.p2.x - this.p1.x;
      double var5 = this.p2.y - this.p1.y;
      return new Point2D.Double(var3, var5);
   }

   public Point2D.Double eval(double var1) {
      double var3 = this.p1.x + var1 * (this.p2.x - this.p1.x);
      double var5 = this.p1.y + var1 * (this.p2.y - this.p1.y);
      return new Point2D.Double(var3, var5);
   }

   public Segment.SplitResults split(double var1) {
      if (var1 != this.p1.y && var1 != this.p2.y) {
         if (var1 <= this.p1.y && var1 <= this.p2.y) {
            return null;
         } else if (var1 >= this.p1.y && var1 >= this.p2.y) {
            return null;
         } else {
            double var3 = (var1 - this.p1.y) / (this.p2.y - this.p1.y);
            Segment[] var5 = new Segment[]{this.getSegment(0.0, var3)};
            Segment[] var6 = new Segment[]{this.getSegment(var3, 1.0)};
            return this.p2.y < var1 ? new Segment.SplitResults(var5, var6) : new Segment.SplitResults(var6, var5);
         }
      } else {
         return null;
      }
   }

   public Segment getSegment(double var1, double var3) {
      Point2D.Double var5 = this.eval(var1);
      Point2D.Double var6 = this.eval(var3);
      return new Linear(var5, var6);
   }

   public Segment splitBefore(double var1) {
      return new Linear(this.p1, this.eval(var1));
   }

   public Segment splitAfter(double var1) {
      return new Linear(this.eval(var1), this.p2);
   }

   public void subdivide(Segment var1, Segment var2) {
      Linear var3 = null;
      Linear var4 = null;
      if (var1 instanceof Linear) {
         var3 = (Linear)var1;
      }

      if (var2 instanceof Linear) {
         var4 = (Linear)var2;
      }

      this.subdivide(var3, var4);
   }

   public void subdivide(double var1, Segment var3, Segment var4) {
      Linear var5 = null;
      Linear var6 = null;
      if (var3 instanceof Linear) {
         var5 = (Linear)var3;
      }

      if (var4 instanceof Linear) {
         var6 = (Linear)var4;
      }

      this.subdivide(var1, var5, var6);
   }

   public void subdivide(Linear var1, Linear var2) {
      if (var1 != null || var2 != null) {
         double var3 = (this.p1.x + this.p2.x) * 0.5;
         double var5 = (this.p1.y + this.p2.y) * 0.5;
         if (var1 != null) {
            var1.p1.x = this.p1.x;
            var1.p1.y = this.p1.y;
            var1.p2.x = var3;
            var1.p2.y = var5;
         }

         if (var2 != null) {
            var2.p1.x = var3;
            var2.p1.y = var5;
            var2.p2.x = this.p2.x;
            var2.p2.y = this.p2.y;
         }

      }
   }

   public void subdivide(double var1, Linear var3, Linear var4) {
      if (var3 != null || var4 != null) {
         double var5 = this.p1.x + var1 * (this.p2.x - this.p1.x);
         double var7 = this.p1.y + var1 * (this.p2.y - this.p1.y);
         if (var3 != null) {
            var3.p1.x = this.p1.x;
            var3.p1.y = this.p1.y;
            var3.p2.x = var5;
            var3.p2.y = var7;
         }

         if (var4 != null) {
            var4.p1.x = var5;
            var4.p1.y = var7;
            var4.p2.x = this.p2.x;
            var4.p2.y = this.p2.y;
         }

      }
   }

   public double getLength() {
      double var1 = this.p2.x - this.p1.x;
      double var3 = this.p2.y - this.p1.y;
      return Math.sqrt(var1 * var1 + var3 * var3);
   }

   public double getLength(double var1) {
      return this.getLength();
   }

   public String toString() {
      return "M" + this.p1.x + ',' + this.p1.y + 'L' + this.p2.x + ',' + this.p2.y;
   }
}
