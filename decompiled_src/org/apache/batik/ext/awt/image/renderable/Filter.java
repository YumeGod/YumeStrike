package org.apache.batik.ext.awt.image.renderable;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.renderable.RenderableImage;

public interface Filter extends RenderableImage {
   Rectangle2D getBounds2D();

   long getTimeStamp();

   Shape getDependencyRegion(int var1, Rectangle2D var2);

   Shape getDirtyRegion(int var1, Rectangle2D var2);
}
