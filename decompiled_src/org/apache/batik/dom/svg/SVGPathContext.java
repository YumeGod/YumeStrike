package org.apache.batik.dom.svg;

import java.awt.geom.Point2D;

public interface SVGPathContext extends SVGContext {
   float getTotalLength();

   Point2D getPointAtLength(float var1);

   int getPathSegAtLength(float var1);
}
