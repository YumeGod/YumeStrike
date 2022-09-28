package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface ShapePainter {
   void paint(Graphics2D var1);

   Shape getPaintedArea();

   Rectangle2D getPaintedBounds2D();

   boolean inPaintedArea(Point2D var1);

   Shape getSensitiveArea();

   Rectangle2D getSensitiveBounds2D();

   boolean inSensitiveArea(Point2D var1);

   void setShape(Shape var1);

   Shape getShape();
}
