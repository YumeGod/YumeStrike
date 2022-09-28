package com.xmlmind.fo.properties;

import java.text.NumberFormat;
import java.util.Locale;

public final class Shadow {
   public double hOffset;
   public double vOffset;
   public double radius;
   public Color color;

   public Shadow(double var1, double var3, double var5, Color var7) {
      this.hOffset = var1;
      this.vOffset = var3;
      this.radius = var5;
      this.color = var7;
   }

   public String toString() {
      NumberFormat var1 = NumberFormat.getInstance(Locale.ENGLISH);
      StringBuffer var2 = new StringBuffer();
      var1.setMinimumFractionDigits(1);
      var1.setMaximumFractionDigits(1);
      var2.append('(');
      var2.append(var1.format(this.hOffset));
      var2.append("pt");
      var2.append(' ');
      var2.append(var1.format(this.vOffset));
      var2.append("pt");
      if (this.radius > 0.0) {
         var2.append(' ');
         var2.append(var1.format(this.radius));
         var2.append("pt");
      }

      if (this.color != null) {
         var2.append(' ');
         var2.append(this.color.toString());
      }

      var2.append(')');
      return var2.toString();
   }
}
