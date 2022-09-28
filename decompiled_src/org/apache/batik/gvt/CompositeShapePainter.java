package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class CompositeShapePainter implements ShapePainter {
   protected Shape shape;
   protected ShapePainter[] painters;
   protected int count;

   public CompositeShapePainter(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.shape = var1;
      }
   }

   public void addShapePainter(ShapePainter var1) {
      if (var1 != null) {
         if (this.shape != var1.getShape()) {
            var1.setShape(this.shape);
         }

         if (this.painters == null) {
            this.painters = new ShapePainter[2];
         }

         if (this.count == this.painters.length) {
            ShapePainter[] var2 = new ShapePainter[this.count + this.count / 2 + 1];
            System.arraycopy(this.painters, 0, var2, 0, this.count);
            this.painters = var2;
         }

         this.painters[this.count++] = var1;
      }
   }

   public ShapePainter getShapePainter(int var1) {
      return this.painters[var1];
   }

   public int getShapePainterCount() {
      return this.count;
   }

   public void paint(Graphics2D var1) {
      if (this.painters != null) {
         for(int var2 = 0; var2 < this.count; ++var2) {
            this.painters[var2].paint(var1);
         }
      }

   }

   public Shape getPaintedArea() {
      if (this.painters == null) {
         return null;
      } else {
         Area var1 = new Area();

         for(int var2 = 0; var2 < this.count; ++var2) {
            Shape var3 = this.painters[var2].getPaintedArea();
            if (var3 != null) {
               var1.add(new Area(var3));
            }
         }

         return var1;
      }
   }

   public Rectangle2D getPaintedBounds2D() {
      if (this.painters == null) {
         return null;
      } else {
         Rectangle2D var1 = null;

         for(int var2 = 0; var2 < this.count; ++var2) {
            Rectangle2D var3 = this.painters[var2].getPaintedBounds2D();
            if (var3 != null) {
               if (var1 == null) {
                  var1 = (Rectangle2D)var3.clone();
               } else {
                  var1.add(var3);
               }
            }
         }

         return var1;
      }
   }

   public boolean inPaintedArea(Point2D var1) {
      if (this.painters == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.count; ++var2) {
            if (this.painters[var2].inPaintedArea(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public Shape getSensitiveArea() {
      if (this.painters == null) {
         return null;
      } else {
         Area var1 = new Area();

         for(int var2 = 0; var2 < this.count; ++var2) {
            Shape var3 = this.painters[var2].getSensitiveArea();
            if (var3 != null) {
               var1.add(new Area(var3));
            }
         }

         return var1;
      }
   }

   public Rectangle2D getSensitiveBounds2D() {
      if (this.painters == null) {
         return null;
      } else {
         Rectangle2D var1 = null;

         for(int var2 = 0; var2 < this.count; ++var2) {
            Rectangle2D var3 = this.painters[var2].getSensitiveBounds2D();
            if (var1 == null) {
               var1 = (Rectangle2D)var3.clone();
            } else {
               var1.add(var3);
            }
         }

         return var1;
      }
   }

   public boolean inSensitiveArea(Point2D var1) {
      if (this.painters == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.count; ++var2) {
            if (this.painters[var2].inSensitiveArea(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public void setShape(Shape var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         if (this.painters != null) {
            for(int var2 = 0; var2 < this.count; ++var2) {
               this.painters[var2].setShape(var1);
            }
         }

         this.shape = var1;
      }
   }

   public Shape getShape() {
      return this.shape;
   }
}
