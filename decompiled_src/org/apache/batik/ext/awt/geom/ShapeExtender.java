package org.apache.batik.ext.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ShapeExtender implements ExtendedShape {
   Shape shape;

   public ShapeExtender(Shape var1) {
      this.shape = var1;
   }

   public boolean contains(double var1, double var3) {
      return this.shape.contains(var1, var3);
   }

   public boolean contains(double var1, double var3, double var5, double var7) {
      return this.shape.contains(var1, var3, var5, var7);
   }

   public boolean contains(Point2D var1) {
      return this.shape.contains(var1);
   }

   public boolean contains(Rectangle2D var1) {
      return this.shape.contains(var1);
   }

   public Rectangle getBounds() {
      return this.shape.getBounds();
   }

   public Rectangle2D getBounds2D() {
      return this.shape.getBounds2D();
   }

   public PathIterator getPathIterator(AffineTransform var1) {
      return this.shape.getPathIterator(var1);
   }

   public PathIterator getPathIterator(AffineTransform var1, double var2) {
      return this.shape.getPathIterator(var1, var2);
   }

   public ExtendedPathIterator getExtendedPathIterator() {
      return new EPIWrap(this.shape.getPathIterator((AffineTransform)null));
   }

   public boolean intersects(double var1, double var3, double var5, double var7) {
      return this.shape.intersects(var1, var3, var5, var7);
   }

   public boolean intersects(Rectangle2D var1) {
      return this.shape.intersects(var1);
   }

   public static class EPIWrap implements ExtendedPathIterator {
      PathIterator pi = null;

      public EPIWrap(PathIterator var1) {
         this.pi = var1;
      }

      public int currentSegment() {
         float[] var1 = new float[6];
         return this.pi.currentSegment(var1);
      }

      public int currentSegment(double[] var1) {
         return this.pi.currentSegment(var1);
      }

      public int currentSegment(float[] var1) {
         return this.pi.currentSegment(var1);
      }

      public int getWindingRule() {
         return this.pi.getWindingRule();
      }

      public boolean isDone() {
         return this.pi.isDone();
      }

      public void next() {
         this.pi.next();
      }
   }
}
