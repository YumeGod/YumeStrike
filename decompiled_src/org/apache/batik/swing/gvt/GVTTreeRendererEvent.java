package org.apache.batik.swing.gvt;

import java.awt.image.BufferedImage;
import java.util.EventObject;

public class GVTTreeRendererEvent extends EventObject {
   protected BufferedImage image;

   public GVTTreeRendererEvent(Object var1, BufferedImage var2) {
      super(var1);
      this.image = var2;
   }

   public BufferedImage getImage() {
      return this.image;
   }
}
