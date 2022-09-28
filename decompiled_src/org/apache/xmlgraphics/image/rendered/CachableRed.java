package org.apache.xmlgraphics.image.rendered;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.RenderedImage;

public interface CachableRed extends RenderedImage {
   Rectangle getBounds();

   Shape getDependencyRegion(int var1, Rectangle var2);

   Shape getDirtyRegion(int var1, Rectangle var2);
}
