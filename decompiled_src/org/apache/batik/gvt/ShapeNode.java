package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.apache.batik.util.HaltingThread;

public class ShapeNode extends AbstractGraphicsNode {
   protected Shape shape;
   protected ShapePainter shapePainter;
   private Rectangle2D primitiveBounds;
   private Rectangle2D geometryBounds;
   private Rectangle2D sensitiveBounds;
   private Shape paintedArea;
   private Shape sensitiveArea;

   public void setShape(Shape var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      this.shape = var1;
      if (this.shapePainter != null) {
         if (var1 != null) {
            this.shapePainter.setShape(var1);
         } else {
            this.shapePainter = null;
         }
      }

      this.fireGraphicsNodeChangeCompleted();
   }

   public Shape getShape() {
      return this.shape;
   }

   public void setShapePainter(ShapePainter var1) {
      if (this.shape != null) {
         this.fireGraphicsNodeChangeStarted();
         this.invalidateGeometryCache();
         this.shapePainter = var1;
         if (this.shapePainter != null && this.shape != this.shapePainter.getShape()) {
            this.shapePainter.setShape(this.shape);
         }

         this.fireGraphicsNodeChangeCompleted();
      }
   }

   public ShapePainter getShapePainter() {
      return this.shapePainter;
   }

   public void paint(Graphics2D var1) {
      if (this.isVisible) {
         super.paint(var1);
      }

   }

   public void primitivePaint(Graphics2D var1) {
      if (this.shapePainter != null) {
         this.shapePainter.paint(var1);
      }

   }

   protected void invalidateGeometryCache() {
      super.invalidateGeometryCache();
      this.primitiveBounds = null;
      this.geometryBounds = null;
      this.sensitiveBounds = null;
      this.paintedArea = null;
      this.sensitiveArea = null;
   }

   public void setPointerEventType(int var1) {
      super.setPointerEventType(var1);
      this.sensitiveBounds = null;
      this.sensitiveArea = null;
   }

   public boolean contains(Point2D var1) {
      switch (this.pointerEventType) {
         case 0:
         case 1:
         case 2:
         case 3:
            if (!this.isVisible) {
               return false;
            }
         case 4:
         case 5:
         case 6:
         case 7:
            Rectangle2D var2 = this.getSensitiveBounds();
            if (var2 != null && var2.contains(var1)) {
               return this.inSensitiveArea(var1);
            }

            return false;
         case 8:
         default:
            return false;
      }
   }

   public boolean intersects(Rectangle2D var1) {
      Rectangle2D var2 = this.getBounds();
      if (var2 == null) {
         return false;
      } else {
         return var2.intersects(var1) && this.paintedArea != null && this.paintedArea.intersects(var1);
      }
   }

   public Rectangle2D getPrimitiveBounds() {
      if (!this.isVisible) {
         return null;
      } else if (this.shape == null) {
         return null;
      } else if (this.primitiveBounds != null) {
         return this.primitiveBounds;
      } else {
         if (this.shapePainter == null) {
            this.primitiveBounds = this.shape.getBounds2D();
         } else {
            this.primitiveBounds = this.shapePainter.getPaintedBounds2D();
         }

         if (HaltingThread.hasBeenHalted()) {
            this.invalidateGeometryCache();
         }

         return this.primitiveBounds;
      }
   }

   public boolean inSensitiveArea(Point2D var1) {
      if (this.shapePainter == null) {
         return false;
      } else {
         ShapePainter var2 = null;
         ShapePainter var3 = null;
         if (this.shapePainter instanceof StrokeShapePainter) {
            var2 = this.shapePainter;
         } else if (this.shapePainter instanceof FillShapePainter) {
            var3 = this.shapePainter;
         } else {
            if (!(this.shapePainter instanceof CompositeShapePainter)) {
               return false;
            }

            CompositeShapePainter var4 = (CompositeShapePainter)this.shapePainter;

            for(int var5 = 0; var5 < var4.getShapePainterCount(); ++var5) {
               ShapePainter var6 = var4.getShapePainter(var5);
               if (var6 instanceof StrokeShapePainter) {
                  var2 = var6;
               } else if (var6 instanceof FillShapePainter) {
                  var3 = var6;
               }
            }
         }

         switch (this.pointerEventType) {
            case 0:
            case 4:
               return this.shapePainter.inPaintedArea(var1);
            case 1:
            case 5:
               if (var3 != null) {
                  return var3.inSensitiveArea(var1);
               }
               break;
            case 2:
            case 6:
               if (var2 != null) {
                  return var2.inSensitiveArea(var1);
               }
               break;
            case 3:
            case 7:
               return this.shapePainter.inSensitiveArea(var1);
            case 8:
         }

         return false;
      }
   }

   public Rectangle2D getSensitiveBounds() {
      if (this.sensitiveBounds != null) {
         return this.sensitiveBounds;
      } else if (this.shapePainter == null) {
         return null;
      } else {
         ShapePainter var1 = null;
         ShapePainter var2 = null;
         if (this.shapePainter instanceof StrokeShapePainter) {
            var1 = this.shapePainter;
         } else if (this.shapePainter instanceof FillShapePainter) {
            var2 = this.shapePainter;
         } else {
            if (!(this.shapePainter instanceof CompositeShapePainter)) {
               return null;
            }

            CompositeShapePainter var3 = (CompositeShapePainter)this.shapePainter;

            for(int var4 = 0; var4 < var3.getShapePainterCount(); ++var4) {
               ShapePainter var5 = var3.getShapePainter(var4);
               if (var5 instanceof StrokeShapePainter) {
                  var1 = var5;
               } else if (var5 instanceof FillShapePainter) {
                  var2 = var5;
               }
            }
         }

         switch (this.pointerEventType) {
            case 0:
            case 4:
               this.sensitiveBounds = this.shapePainter.getPaintedBounds2D();
               break;
            case 1:
            case 5:
               if (var2 != null) {
                  this.sensitiveBounds = var2.getSensitiveBounds2D();
               }
               break;
            case 2:
            case 6:
               if (var1 != null) {
                  this.sensitiveBounds = var1.getSensitiveBounds2D();
               }
               break;
            case 3:
            case 7:
               this.sensitiveBounds = this.shapePainter.getSensitiveBounds2D();
            case 8:
         }

         return this.sensitiveBounds;
      }
   }

   public Shape getSensitiveArea() {
      if (this.sensitiveArea != null) {
         return this.sensitiveArea;
      } else if (this.shapePainter == null) {
         return null;
      } else {
         ShapePainter var1 = null;
         ShapePainter var2 = null;
         if (this.shapePainter instanceof StrokeShapePainter) {
            var1 = this.shapePainter;
         } else if (this.shapePainter instanceof FillShapePainter) {
            var2 = this.shapePainter;
         } else {
            if (!(this.shapePainter instanceof CompositeShapePainter)) {
               return null;
            }

            CompositeShapePainter var3 = (CompositeShapePainter)this.shapePainter;

            for(int var4 = 0; var4 < var3.getShapePainterCount(); ++var4) {
               ShapePainter var5 = var3.getShapePainter(var4);
               if (var5 instanceof StrokeShapePainter) {
                  var1 = var5;
               } else if (var5 instanceof FillShapePainter) {
                  var2 = var5;
               }
            }
         }

         switch (this.pointerEventType) {
            case 0:
            case 4:
               this.sensitiveArea = this.shapePainter.getPaintedArea();
               break;
            case 1:
            case 5:
               if (var2 != null) {
                  this.sensitiveArea = var2.getSensitiveArea();
               }
               break;
            case 2:
            case 6:
               if (var1 != null) {
                  this.sensitiveArea = var1.getSensitiveArea();
               }
               break;
            case 3:
            case 7:
               this.sensitiveArea = this.shapePainter.getSensitiveArea();
            case 8:
         }

         return this.sensitiveArea;
      }
   }

   public Rectangle2D getGeometryBounds() {
      if (this.geometryBounds == null) {
         if (this.shape == null) {
            return null;
         }

         this.geometryBounds = this.normalizeRectangle(this.shape.getBounds2D());
      }

      return this.geometryBounds;
   }

   public Shape getOutline() {
      return this.shape;
   }
}
