package org.apache.batik.gvt.flow;

import java.awt.Shape;

public class RegionInfo {
   private Shape shape;
   private float verticalAlignment;

   public RegionInfo(Shape var1, float var2) {
      this.shape = var1;
      this.verticalAlignment = var2;
   }

   public Shape getShape() {
      return this.shape;
   }

   public void setShape(Shape var1) {
      this.shape = var1;
   }

   public float getVerticalAlignment() {
      return this.verticalAlignment;
   }

   public void setVerticalAlignment(float var1) {
      this.verticalAlignment = var1;
   }
}
