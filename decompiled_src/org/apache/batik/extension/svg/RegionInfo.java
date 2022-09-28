package org.apache.batik.extension.svg;

import java.awt.geom.Rectangle2D;

public class RegionInfo extends Rectangle2D.Float {
   private float verticalAlignment = 0.0F;

   public RegionInfo(float var1, float var2, float var3, float var4, float var5) {
      super(var1, var2, var3, var4);
      this.verticalAlignment = var5;
   }

   public float getVerticalAlignment() {
      return this.verticalAlignment;
   }

   public void setVerticalAlignment(float var1) {
      this.verticalAlignment = var1;
   }
}
