package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public interface SVGContext {
   int PERCENTAGE_FONT_SIZE = 0;
   int PERCENTAGE_VIEWPORT_WIDTH = 1;
   int PERCENTAGE_VIEWPORT_HEIGHT = 2;
   int PERCENTAGE_VIEWPORT_SIZE = 3;

   float getPixelUnitToMillimeter();

   float getPixelToMM();

   Rectangle2D getBBox();

   AffineTransform getScreenTransform();

   void setScreenTransform(AffineTransform var1);

   AffineTransform getCTM();

   AffineTransform getGlobalTransform();

   float getViewportWidth();

   float getViewportHeight();

   float getFontSize();
}
