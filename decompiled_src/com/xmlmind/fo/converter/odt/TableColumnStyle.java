package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;

public final class TableColumnStyle {
   public String name;
   public double width;
   public boolean relativeWidth;
   public boolean optimizeWidth;

   public TableColumnStyle(double var1) {
      this(var1, false);
   }

   public TableColumnStyle(double var1, boolean var3) {
      this.width = var1;
      this.relativeWidth = var3;
   }

   public void print(PrintWriter var1) {
      var1.print("<style:style style:family=\"table-column\"");
      var1.print(" style:name=\"" + this.name + "\"");
      var1.println(">");
      var1.println("<style:table-column-properties");
      if (this.relativeWidth) {
         var1.println(" style:rel-column-width=\"" + Odt.relativeLength(this.width, 0) + "\"");
      } else {
         var1.println(" style:column-width=\"" + Odt.length(this.width, 1) + "\"");
      }

      var1.println(" style:use-optimal-column-width=\"" + this.optimizeWidth() + "\"");
      var1.println(">");
      var1.println("</style:table-column-properties>");
      var1.println("</style:style>");
   }

   private String optimizeWidth() {
      return this.optimizeWidth ? "true" : "false";
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ hash(this.width);
      var1 = shift(var1) ^ hash(this.relativeWidth);
      var1 = shift(var1) ^ hash(this.optimizeWidth);
      return var1 >>> 16 | var1 << 16;
   }

   private static int hash(double var0) {
      long var2 = Double.doubleToLongBits(var0);
      return (int)(var2 ^ var2 >>> 32);
   }

   private static int hash(boolean var0) {
      return var0 ? 1 : 0;
   }

   private static int shift(int var0) {
      return var0 << 1 | var0 >>> 31;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TableColumnStyle)) {
         return false;
      } else {
         TableColumnStyle var2 = (TableColumnStyle)var1;
         return this.width == var2.width && this.relativeWidth == var2.relativeWidth && this.optimizeWidth == var2.optimizeWidth;
      }
   }
}
