package org.apache.fop.svg;

import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsDevice;

class PDFGraphicsDevice extends GraphicsDevice {
   protected java.awt.GraphicsConfiguration gc;

   PDFGraphicsDevice(PDFGraphicsConfiguration gc) {
      this.gc = gc;
   }

   public java.awt.GraphicsConfiguration getBestConfiguration(GraphicsConfigTemplate gct) {
      return this.gc;
   }

   public java.awt.GraphicsConfiguration[] getConfigurations() {
      return new java.awt.GraphicsConfiguration[]{this.gc};
   }

   public java.awt.GraphicsConfiguration getDefaultConfiguration() {
      return this.gc;
   }

   public String getIDstring() {
      return this.toString();
   }

   public int getType() {
      return 1;
   }
}
