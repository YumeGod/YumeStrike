package org.apache.xmlgraphics.java2d;

import java.awt.geom.Dimension2D;
import java.io.Serializable;

public class Dimension2DDouble extends Dimension2D implements Serializable {
   private static final long serialVersionUID = 7909028357685520189L;
   private double width;
   private double height;

   public Dimension2DDouble() {
      this.width = 0.0;
      this.height = 0.0;
   }

   public Dimension2DDouble(double width, double height) {
      this.width = width;
      this.height = height;
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public void setSize(double w, double h) {
      this.width = w;
      this.height = h;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj instanceof Dimension2DDouble) {
         Dimension2DDouble other = (Dimension2DDouble)obj;
         if (Double.doubleToLongBits(this.height) != Double.doubleToLongBits(other.height)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.width) == Double.doubleToLongBits(other.width);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      double sum = this.width + this.height;
      return (int)(sum * (sum + 1.0) / 2.0 + this.width);
   }

   public String toString() {
      return this.getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
   }
}
