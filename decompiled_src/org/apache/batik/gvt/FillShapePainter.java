package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class FillShapePainter implements ShapePainter {
   protected Shape shape;
   protected Paint paint;

   public FillShapePainter(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Shape can not be null!");
      } else {
         this.shape = var1;
      }
   }

   public void setPaint(Paint var1) {
      this.paint = var1;
   }

   public Paint getPaint() {
      return this.paint;
   }

   public void paint(Graphics2D var1) {
      if (this.paint != null) {
         var1.setPaint(this.paint);
         var1.fill(this.shape);
      }

   }

   public Shape getPaintedArea() {
      return this.paint == null ? null : this.shape;
   }

   public Rectangle2D getPaintedBounds2D() {
      return this.paint != null && this.shape != null ? this.shape.getBounds2D() : null;
   }

   public boolean inPaintedArea(Point2D var1) {
      return this.paint != null && this.shape != null ? this.shape.contains(var1) : false;
   }

   public Shape getSensitiveArea() {
      return this.shape;
   }

   public Rectangle2D getSensitiveBounds2D() {
      return this.shape == null ? null : this.shape.getBounds2D();
   }

   public boolean inSensitiveArea(Point2D var1) {
      return this.shape == null ? false : this.shape.contains(var1);
   }

   public void setShape(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.shape = var1;
      }
   }

   public Shape getShape() {
      return this.shape;
   }
}
