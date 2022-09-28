package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class TableCellStyle {
   public static final int COLOR_TRANSPARENT = -1;
   public static final int ALIGNMENT_TOP = 0;
   public static final int ALIGNMENT_MIDDLE = 1;
   public static final int ALIGNMENT_BOTTOM = 2;
   public String name;
   public Borders borders;
   public double paddingTop;
   public double paddingBottom;
   public double paddingLeft;
   public double paddingRight;
   public int background;
   public int alignment;

   public TableCellStyle() {
      this.borders = new Borders();
      this.background = -1;
   }

   public TableCellStyle(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.borders = new Borders(var2);
      this.paddingTop = length(var2[208]);
      this.paddingBottom = length(var2[199]);
      this.paddingLeft = length(var2[203]);
      this.paddingRight = length(var2[204]);
      Value var3 = var2[8];
      if (var3.type == 24) {
         this.background = Odt.rgb(var3.color());
      } else {
         this.background = -1;
      }

      switch (var2[93].keyword()) {
         case 4:
            this.alignment = 2;
            break;
         case 31:
            this.alignment = 1;
      }

   }

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
      }

      return var1;
   }

   public void print(PrintWriter var1) {
      var1.print("<style:style style:family=\"table-cell\"");
      var1.print(" style:name=\"" + this.name + "\"");
      var1.println(">");
      var1.println("<style:table-cell-properties");
      this.borders.print(var1);
      if (this.paddingTop > 0.0) {
         var1.println(" fo:padding-top=\"" + Odt.length(this.paddingTop, 1) + "\"");
      }

      if (this.paddingBottom > 0.0) {
         var1.println(" fo:padding-bottom=\"" + Odt.length(this.paddingBottom, 1) + "\"");
      }

      if (this.paddingLeft > 0.0) {
         var1.println(" fo:padding-left=\"" + Odt.length(this.paddingLeft, 1) + "\"");
      }

      if (this.paddingRight > 0.0) {
         var1.println(" fo:padding-right=\"" + Odt.length(this.paddingRight, 1) + "\"");
      }

      if (this.background != -1) {
         var1.println(" fo:background-color=\"" + Odt.color(this.background) + "\"");
      }

      var1.println(" style:vertical-align=\"" + this.alignment() + "\"");
      var1.println(">");
      var1.println("</style:table-cell-properties>");
      var1.println("</style:style>");
   }

   private String alignment() {
      String var1 = "top";
      switch (this.alignment) {
         case 1:
            var1 = "middle";
            break;
         case 2:
            var1 = "bottom";
      }

      return var1;
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.borders.hashCode();
      var1 = shift(var1) ^ hash(this.paddingTop);
      var1 = shift(var1) ^ hash(this.paddingBottom);
      var1 = shift(var1) ^ hash(this.paddingLeft);
      var1 = shift(var1) ^ hash(this.paddingRight);
      var1 = shift(var1) ^ this.background;
      var1 = shift(var1) ^ this.alignment;
      return var1 >>> 16 | var1 << 16;
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
      } else if (!(var1 instanceof TableCellStyle)) {
         return false;
      } else {
         TableCellStyle var2 = (TableCellStyle)var1;
         return this.borders.equals(var2.borders) && this.paddingTop == var2.paddingTop && this.paddingBottom == var2.paddingBottom && this.paddingLeft == var2.paddingLeft && this.paddingRight == var2.paddingRight && this.background == var2.background && this.alignment == var2.alignment;
      }
   }
}
