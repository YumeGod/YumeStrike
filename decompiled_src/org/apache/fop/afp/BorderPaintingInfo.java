package org.apache.fop.afp;

import java.awt.Color;

public class BorderPaintingInfo implements PaintingInfo {
   private final float x1;
   private final float y1;
   private final float x2;
   private final float y2;
   private final boolean isHorizontal;
   private final int style;
   private final Color color;

   public BorderPaintingInfo(float x1, float y1, float x2, float y2, boolean isHorizontal, int style, Color color) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
      this.isHorizontal = isHorizontal;
      this.style = style;
      this.color = color;
   }

   public float getX1() {
      return this.x1;
   }

   public float getY1() {
      return this.y1;
   }

   public float getX2() {
      return this.x2;
   }

   public float getY2() {
      return this.y2;
   }

   public boolean isHorizontal() {
      return this.isHorizontal;
   }

   public int getStyle() {
      return this.style;
   }

   public Color getColor() {
      return this.color;
   }
}
