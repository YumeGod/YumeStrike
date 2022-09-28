package org.apache.fop.svg;

import java.awt.image.VolatileImage;

public abstract class GraphicsConfiguration extends java.awt.GraphicsConfiguration {
   public VolatileImage createCompatibleVolatileImage(int width, int height) {
      return null;
   }

   public VolatileImage createCompatibleVolatileImage(int width, int height, int transparency) {
      return null;
   }
}
