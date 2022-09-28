package org.apache.xmlgraphics.java2d.color;

import java.awt.Color;

public final class ColorUtil {
   private ColorUtil() {
   }

   public static Color lightenColor(Color col, float factor) {
      float[] cols = new float[4];
      cols = col.getRGBComponents(cols);
      if (factor > 0.0F) {
         cols[0] = (float)((double)cols[0] + (1.0 - (double)cols[0]) * (double)factor);
         cols[1] = (float)((double)cols[1] + (1.0 - (double)cols[1]) * (double)factor);
         cols[2] = (float)((double)cols[2] + (1.0 - (double)cols[2]) * (double)factor);
      } else {
         cols[0] -= cols[0] * -factor;
         cols[1] -= cols[1] * -factor;
         cols[2] -= cols[2] * -factor;
      }

      return new Color(cols[0], cols[1], cols[2], cols[3]);
   }

   public static boolean isGray(Color col) {
      return col.getRed() == col.getBlue() && col.getRed() == col.getGreen();
   }
}
