package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class TableStyle {
   public static final int BREAK_AUTO = 0;
   public static final int BREAK_COLUMN = 1;
   public static final int BREAK_PAGE = 2;
   public static final int ALIGNMENT_LEFT = 0;
   public static final int ALIGNMENT_RIGHT = 1;
   public static final int ALIGNMENT_CENTER = 2;
   public static final int ALIGNMENT_MARGINS = 3;
   public static final int BORDER_COLLAPSE = 0;
   public static final int BORDER_SEPARATE = 1;
   public static final int COLOR_TRANSPARENT = -1;
   public String name;
   public int breakBefore;
   public boolean keepTogether;
   public boolean keepWithNext;
   public double marginTop;
   public double marginBottom;
   public double marginLeft;
   public double marginRight;
   public int alignment;
   public int borderModel;
   public double width;
   public boolean relativeWidth;
   public int background;
   public String masterPageName;

   public TableStyle() {
      this.width = 100.0;
      this.relativeWidth = true;
      this.background = -1;
      this.masterPageName = "";
   }

   public TableStyle(Context var1) {
      this.initialize(var1);
      this.masterPageName = "";
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      switch (var1.breakBefore) {
         case 1:
            this.breakBefore = 1;
            break;
         case 2:
            this.breakBefore = 2;
      }

      this.keepTogether = keep(var2[137]);
      this.keepWithNext = keep(var2[141]);
      this.marginTop = var1.spaceBefore();
      this.marginLeft = length(var2[277]);
      this.marginRight = length(var2[97]);
      int var3 = var2[37].keyword();
      if (var3 == 177) {
         byte var4 = 54;
         byte var5 = 55;
         double var6 = length(var2[var4]);
         double var8 = length(var2[var5]);
         if (var6 > 0.0 || var8 > 0.0) {
            this.borderModel = 1;
         }
      }

      Value var10 = var2[308];
      switch (var10.type) {
         case 4:
            this.width = var10.length();
            break;
         case 13:
            this.width = var10.percentage();
            this.relativeWidth = true;
      }

      if (var1.background != null) {
         this.background = Odt.rgb(var1.background);
      } else {
         this.background = -1;
      }

      Context var11 = var1.parent();
      if (var11.fo == 46) {
         var2 = var11.properties.values;
         switch (var2[289].keyword()) {
            case 31:
            case 93:
               this.alignment = 2;
               break;
            case 52:
            case 165:
               this.alignment = 1;
         }
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

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
      }

      return var1;
   }

   public void print(PrintWriter var1) {
      var1.print("<style:style style:family=\"table\"");
      var1.print(" style:name=\"" + this.name + "\"");
      if (this.masterPageName.length() > 0) {
         var1.print(" style:master-page-name=\"" + this.masterPageName + "\"");
      }

      var1.println(">");
      var1.println("<style:table-properties");
      if (this.breakBefore != 0) {
         var1.println(" fo:break-before=\"" + this.breakBefore() + "\"");
      }

      if (this.keepWithNext) {
         var1.println(" fo:keep-with-next=\"always\"");
      }

      var1.println(" style:may-break-between-rows=\"" + this.mayBreakBetweenRows() + "\"");
      if (this.marginTop > 0.0) {
         var1.println(" fo:margin-top=\"" + Odt.length(this.marginTop, 1) + "\"");
      }

      if (this.marginBottom > 0.0) {
         var1.println(" fo:margin-bottom=\"" + Odt.length(this.marginBottom, 1) + "\"");
      }

      if ((this.alignment == 0 || this.alignment == 3) && this.marginLeft != 0.0) {
         var1.println(" fo:margin-left=\"" + Odt.length(this.marginLeft, 1) + "\"");
      }

      if ((this.alignment == 1 || this.alignment == 3) && this.marginRight != 0.0) {
         var1.println(" fo:margin-right=\"" + Odt.length(this.marginRight, 1) + "\"");
      }

      var1.println(" table:align=\"" + this.alignment() + "\"");
      var1.println(" table:border-model=\"" + this.borderModel() + "\"");
      if (this.relativeWidth) {
         var1.println(" style:rel-width=\"" + Odt.percent(this.width, 0) + "\"");
      } else {
         var1.println(" style:width=\"" + Odt.length(this.width, 1) + "\"");
      }

      if (this.background != -1) {
         var1.println(" fo:background-color=\"" + Odt.color(this.background) + "\"");
      }

      var1.println(">");
      var1.println("</style:table-properties>");
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

   private String mayBreakBetweenRows() {
      return this.keepTogether ? "false" : "true";
   }

   private String alignment() {
      String var1 = "left";
      switch (this.alignment) {
         case 1:
            var1 = "right";
            break;
         case 2:
            var1 = "center";
            break;
         case 3:
            var1 = "margins";
      }

      return var1;
   }

   private String borderModel() {
      return this.borderModel == 1 ? "separating" : "collapsing";
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.breakBefore;
      var1 = shift(var1) ^ hash(this.keepTogether);
      var1 = shift(var1) ^ hash(this.keepWithNext);
      var1 = shift(var1) ^ hash(this.marginTop);
      var1 = shift(var1) ^ hash(this.marginBottom);
      var1 = shift(var1) ^ hash(this.marginLeft);
      var1 = shift(var1) ^ hash(this.marginRight);
      var1 = shift(var1) ^ this.alignment;
      var1 = shift(var1) ^ this.borderModel;
      var1 = shift(var1) ^ hash(this.width);
      var1 = shift(var1) ^ hash(this.relativeWidth);
      var1 = shift(var1) ^ this.background;
      var1 = shift(var1) ^ this.masterPageName.hashCode();
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
      } else if (!(var1 instanceof TableStyle)) {
         return false;
      } else {
         TableStyle var2 = (TableStyle)var1;
         return this.breakBefore == var2.breakBefore && this.keepTogether == var2.keepTogether && this.keepWithNext == var2.keepWithNext && this.marginTop == var2.marginTop && this.marginBottom == var2.marginBottom && this.marginLeft == var2.marginLeft && this.marginRight == var2.marginRight && this.alignment == var2.alignment && this.borderModel == var2.borderModel && this.width == var2.width && this.relativeWidth == var2.relativeWidth && this.background == var2.background && this.masterPageName.equals(var2.masterPageName);
      }
   }
}
