package org.apache.xmlgraphics.image.loader.impl;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import org.apache.xmlgraphics.image.loader.ImageContext;

public class DefaultImageContext implements ImageContext {
   private final float sourceResolution;

   public DefaultImageContext() {
      if (GraphicsEnvironment.isHeadless()) {
         this.sourceResolution = 72.0F;
      } else {
         this.sourceResolution = (float)Toolkit.getDefaultToolkit().getScreenResolution();
      }

   }

   public float getSourceResolution() {
      return this.sourceResolution;
   }
}
