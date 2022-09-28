package org.apache.fop.afp;

public class RectanglePaintingInfo implements PaintingInfo {
   private final float x;
   private final float y;
   private final float width;
   private final float height;

   public RectanglePaintingInfo(float x, float y, float width, float height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   protected float getX() {
      return this.x;
   }

   protected float getY() {
      return this.y;
   }

   protected float getWidth() {
      return this.width;
   }

   protected float getHeight() {
      return this.height;
   }
}
