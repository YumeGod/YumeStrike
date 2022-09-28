package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class StrokeShapePainter implements ShapePainter {
   protected Shape shape;
   protected Shape strokedShape;
   protected Stroke stroke;
   protected Paint paint;

   public StrokeShapePainter(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.shape = var1;
      }
   }

   public void setStroke(Stroke var1) {
      this.stroke = var1;
      this.strokedShape = null;
   }

   public Stroke getStroke() {
      return this.stroke;
   }

   public void setPaint(Paint var1) {
      this.paint = var1;
   }

   public Paint getPaint() {
      return this.paint;
   }

   public void paint(Graphics2D var1) {
      if (this.stroke != null && this.paint != null) {
         var1.setPaint(this.paint);
         var1.setStroke(this.stroke);
         var1.draw(this.shape);
      }

   }

   public Shape getPaintedArea() {
      if (this.paint != null && this.stroke != null) {
         if (this.strokedShape == null) {
            this.strokedShape = this.stroke.createStrokedShape(this.shape);
         }

         return this.strokedShape;
      } else {
         return null;
      }
   }

   public Rectangle2D getPaintedBounds2D() {
      Shape var1 = this.getPaintedArea();
      return var1 == null ? null : var1.getBounds2D();
   }

   public boolean inPaintedArea(Point2D var1) {
      Shape var2 = this.getPaintedArea();
      return var2 == null ? false : var2.contains(var1);
   }

   public Shape getSensitiveArea() {
      if (this.stroke == null) {
         return null;
      } else {
         if (this.strokedShape == null) {
            this.strokedShape = this.stroke.createStrokedShape(this.shape);
         }

         return this.strokedShape;
      }
   }

   public Rectangle2D getSensitiveBounds2D() {
      Shape var1 = this.getSensitiveArea();
      return var1 == null ? null : var1.getBounds2D();
   }

   public boolean inSensitiveArea(Point2D var1) {
      Shape var2 = this.getSensitiveArea();
      return var2 == null ? false : var2.contains(var1);
   }

   public void setShape(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.shape = var1;
         this.strokedShape = null;
      }
   }

   public Shape getShape() {
      return this.shape;
   }
}
