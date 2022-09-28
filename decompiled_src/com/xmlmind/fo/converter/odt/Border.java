package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.properties.Keyword;

public final class Border {
   public int style;
   public int color;
   public double width;

   public Border() {
      this(125, 0, 0.0);
   }

   public Border(int var1, int var2, double var3) {
      this.set(var1, var2, var3);
   }

   public void set(int var1, int var2, double var3) {
      this.style = var1;
      this.color = var2;
      this.width = var3;
   }

   public boolean materialized() {
      return this.style != 125 && this.style != 75 && this.width > 0.0;
   }

   public String toString() {
      return Keyword.keyword(this.style) + " " + Odt.color(this.color) + " " + Odt.length(this.width, 1);
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.style;
      var1 = shift(var1) ^ this.color;
      var1 = shift(var1) ^ hash(this.width);
      return var1;
   }

   private static int hash(double var0) {
      long var2 = Double.doubleToLongBits(var0);
      return (int)(var2 ^ var2 >>> 32);
   }

   private static int shift(int var0) {
      return var0 << 1 | var0 >>> 31;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Border)) {
         return false;
      } else {
         Border var2 = (Border)var1;
         return this.style == var2.style && this.color == var2.color && this.width == var2.width;
      }
   }

   public Border copy() {
      return new Border(this.style, this.color, this.width);
   }
}
