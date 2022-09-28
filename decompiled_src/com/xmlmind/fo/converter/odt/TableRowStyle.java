package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class TableRowStyle {
   public static final int BREAK_AUTO = 0;
   public static final int BREAK_COLUMN = 1;
   public static final int BREAK_PAGE = 2;
   public static final int COLOR_TRANSPARENT = -1;
   public String name;
   public int breakBefore;
   public int breakAfter;
   public boolean keepTogether;
   public double height;
   public boolean optimizeHeight;
   public int background;

   public TableRowStyle() {
      this.background = -1;
   }

   public TableRowStyle(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      switch (var1.properties.breakBefore()) {
         case 1:
            this.breakBefore = 1;
            break;
         case 2:
            this.breakBefore = 2;
      }

      switch (var1.properties.breakAfter()) {
         case 1:
            this.breakAfter = 1;
            break;
         case 2:
            this.breakAfter = 2;
      }

      this.keepTogether = keep(var2[137]);
      Value var3;
      if (var1.properties.isSpecified(118)) {
         var3 = var2[118];
         if (var3.type == 4) {
            this.height = var3.length();
         }
      } else {
         this.optimizeHeight = true;
      }

      var3 = var2[8];
      if (var3.type == 24) {
         this.background = Odt.rgb(var3.color());
      } else {
         this.background = -1;
      }

   }

   private static boolean keep(Value var0) {
      boolean var1 = false;
      switch (var0.type) {
         case 1:
            if (var0.keyword() == 8) {
               var1 = true;
            }
            break;
         case 2:
            if (var0.integer() > 0) {
               var1 = true;
            }
      }

      return var1;
   }

   public void print(PrintWriter var1) {
      var1.print("<style:style style:family=\"table-row\"");
      var1.print(" style:name=\"" + this.name + "\"");
      var1.println(">");
      var1.println("<style:table-row-properties");
      if (this.breakBefore != 0) {
         var1.println(" fo:break-before=\"" + this.breakBefore() + "\"");
      }

      if (this.breakAfter != 0) {
         var1.println(" fo:break-after=\"" + this.breakAfter() + "\"");
      }

      if (this.keepTogether) {
         var1.println(" fo:keep-together=\"always\"");
      }

      if (this.height > 0.0) {
         var1.println(" style:min-row-height=\"" + Odt.length(this.height, 1) + "\"");
      }

      var1.println(" style:use-optimal-row-height=\"" + this.optimizeHeight() + "\"");
      if (this.background != -1) {
         var1.println(" fo:background-color=\"" + Odt.color(this.background) + "\"");
      }

      var1.println(">");
      var1.println("</style:table-row-properties>");
      var1.println("</style:style>");
   }

   private String breakBefore() {
      String var1 = "auto";
      switch (this.breakBefore) {
         case 1:
            var1 = "column";
            break;
         case 2:
            var1 = "page";
      }

      return var1;
   }

   private String breakAfter() {
      String var1 = "auto";
      switch (this.breakAfter) {
         case 1:
            var1 = "column";
            break;
         case 2:
            var1 = "page";
      }

      return var1;
   }

   private String optimizeHeight() {
      return this.optimizeHeight ? "true" : "false";
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.breakBefore;
      var1 = shift(var1) ^ this.breakAfter;
      var1 = shift(var1) ^ hash(this.keepTogether);
      var1 = shift(var1) ^ hash(this.height);
      var1 = shift(var1) ^ hash(this.optimizeHeight);
      var1 = shift(var1) ^ this.background;
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
      } else if (!(var1 instanceof TableRowStyle)) {
         return false;
      } else {
         TableRowStyle var2 = (TableRowStyle)var1;
         return this.breakBefore == var2.breakBefore && this.breakAfter == var2.breakAfter && this.keepTogether == var2.keepTogether && this.height == var2.height && this.optimizeHeight == var2.optimizeHeight && this.background == var2.background;
      }
   }
}
