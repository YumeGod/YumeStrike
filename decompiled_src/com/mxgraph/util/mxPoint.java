package com.mxgraph.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class mxPoint implements Serializable, Cloneable {
   private static final long serialVersionUID = 6554231393215892186L;
   protected double x;
   protected double y;

   public mxPoint() {
      this(0.0, 0.0);
   }

   public mxPoint(Point2D var1) {
      this(var1.getX(), var1.getY());
   }

   public mxPoint(mxPoint var1) {
      this(var1.getX(), var1.getY());
   }

   public mxPoint(double var1, double var3) {
      this.setX(var1);
      this.setY(var3);
   }

   public double getX() {
      return this.x;
   }

   public void setX(double var1) {
      this.x = var1;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double var1) {
      this.y = var1;
   }

   public Point getPoint() {
      return new Point((int)Math.round(this.x), (int)Math.round(this.y));
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof mxPoint)) {
         return false;
      } else {
         mxPoint var2 = (mxPoint)var1;
         return var2.getX() == this.getX() && var2.getY() == this.getY();
      }
   }

   public Object clone() {
      mxPoint var1;
      try {
         var1 = (mxPoint)super.clone();
      } catch (CloneNotSupportedException var3) {
         var1 = new mxPoint();
      }

      var1.setX(this.getX());
      var1.setY(this.getY());
      return var1;
   }
}
