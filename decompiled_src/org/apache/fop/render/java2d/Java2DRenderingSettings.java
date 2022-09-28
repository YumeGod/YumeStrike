package org.apache.fop.render.java2d;

import java.awt.Color;

public class Java2DRenderingSettings {
   private Color pageBackgroundColor;

   public Java2DRenderingSettings() {
      this.pageBackgroundColor = Color.WHITE;
   }

   public Color getPageBackgroundColor() {
      return this.pageBackgroundColor;
   }

   public void setPageBackgroundColor(Color color) {
      this.pageBackgroundColor = color;
   }

   public boolean hasTransparentPageBackground() {
      return this.pageBackgroundColor == null;
   }
}
