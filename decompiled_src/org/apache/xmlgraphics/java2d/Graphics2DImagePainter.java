package org.apache.xmlgraphics.java2d;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Graphics2DImagePainter {
   void paint(Graphics2D var1, Rectangle2D var2);

   Dimension getImageSize();
}
