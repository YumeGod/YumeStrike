package org.apache.fop.afp.svg;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

public class AFPGraphicsDevice extends GraphicsDevice {
   protected GraphicsConfiguration gc;

   public AFPGraphicsDevice(AFPGraphicsConfiguration gc) {
      this.gc = gc;
   }

   public GraphicsConfiguration[] getConfigurations() {
      return new GraphicsConfiguration[]{this.gc};
   }

   public GraphicsConfiguration getDefaultConfiguration() {
      return this.gc;
   }

   public String getIDstring() {
      return this.toString();
   }

   public int getType() {
      return 1;
   }
}
