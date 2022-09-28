package org.apache.batik.gvt.renderer;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;

public interface ImageRenderer extends Renderer {
   void dispose();

   void updateOffScreen(int var1, int var2);

   void setTransform(AffineTransform var1);

   AffineTransform getTransform();

   void setRenderingHints(RenderingHints var1);

   RenderingHints getRenderingHints();

   BufferedImage getOffScreen();

   void clearOffScreen();

   void flush();

   void flush(Rectangle var1);

   void flush(Collection var1);
}
