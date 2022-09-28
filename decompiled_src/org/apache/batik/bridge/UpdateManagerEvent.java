package org.apache.batik.bridge;

import java.awt.image.BufferedImage;
import java.util.EventObject;
import java.util.List;

public class UpdateManagerEvent extends EventObject {
   protected BufferedImage image;
   protected List dirtyAreas;
   protected boolean clearPaintingTransform;

   public UpdateManagerEvent(Object var1, BufferedImage var2, List var3) {
      super(var1);
      this.image = var2;
      this.dirtyAreas = var3;
      this.clearPaintingTransform = false;
   }

   public UpdateManagerEvent(Object var1, BufferedImage var2, List var3, boolean var4) {
      super(var1);
      this.image = var2;
      this.dirtyAreas = var3;
      this.clearPaintingTransform = var4;
   }

   public BufferedImage getImage() {
      return this.image;
   }

   public List getDirtyAreas() {
      return this.dirtyAreas;
   }

   public boolean getClearPaintingTransform() {
      return this.clearPaintingTransform;
   }
}
