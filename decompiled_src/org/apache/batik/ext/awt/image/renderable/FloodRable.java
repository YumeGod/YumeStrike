package org.apache.batik.ext.awt.image.renderable;

import java.awt.Paint;
import java.awt.geom.Rectangle2D;

public interface FloodRable extends Filter {
   void setFloodPaint(Paint var1);

   Paint getFloodPaint();

   void setFloodRegion(Rectangle2D var1);

   Rectangle2D getFloodRegion();
}
