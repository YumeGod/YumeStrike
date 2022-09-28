package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class ParagraphStyle implements Cloneable {
   public static final int BREAK_AUTO = 0;
   public static final int BREAK_COLUMN = 1;
   public static final int BREAK_PAGE = 2;
   public static final int COLOR_TRANSPARENT = -1;
   public static final int ALIGN_START = 0;
   public static final int ALIGN_END = 1;
   public static final int ALIGN_LEFT = 2;
   public static final int ALIGN_RIGHT = 3;
   public static final int ALIGN_CENTER = 4;
   public static final int ALIGN_JUSTIFY = 5;
   public String name;
   public int breakBefore = 0;
   public boolean keepTogether;
   public boolean keepWithNext;
   public double marginTop;
   public double marginBottom;
   public double marginLeft;
   public double marginRight;
   public Borders borders;
   public double paddingTop;
   public double paddingBottom;
   public double paddingLeft;
   public double paddingRight;
   public int background = -1;
   public int textAlign = 0;
   public int textAlignLast = 0;
   public double textIndent;
   public double lineHeight;
   public int outlineLevel;
   public TabStops tabStops = new TabStops();
   public String listStyleName = "";
   public String masterPageName = "";

   public ParagraphStyle() {
      this.borders = new Borders();
   }

   public ParagraphStyle(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Context var2 = var1.block();
      Value[] var3 = var2.properties.values;
      switch (var2.breakBefore) {
         case 1:
            this.breakBefore = 1;
            break;
         case 2:
            this.breakBefore = 2;
      }

      this.keepTogether = keep(var3[137]);
      this.keepWithNext = keep(var3[141]);
      this.marginTop = var2.spaceBefore();
      this.marginLeft = length(var3[277]);
      this.marginRight = length(var3[97]);
      this.borders = new Borders(var3);
      this.paddingTop = length(var3[208]);
      this.paddingBottom = length(var3[199]);
      this.paddingLeft = length(var3[203]);
      this.paddingRight = length(var3[204]);
      this.marginLeft -= this.paddingLeft;
      if (this.borders.left.materialized()) {
         this.marginLeft -= this.borders.left.width;
      }

      this.marginRight -= this.paddingRight;
      if (this.borders.right.materialized()) {
         this.marginRight -= this.borders.right.width;
      }

      if (var2.background != null) {
         this.background = Odt.rgb(var2.background);
      }

      switch (var3[289].keyword()) {
         case 31:
            this.textAlign = 4;
            break;
         case 52:
         case 165:
            this.textAlign = 1;
            break;
         case 93:
            this.textAlign = 5;
            break;
         default:
            this.textAlign = 0;
      }

      if (this.textAlign == 5) {
         switch (var3[290].keyword()) {
            case 31:
               this.textAlignLast = 4;
               break;
            case 93:
               this.textAlignLast = 5;
               break;
            default:
               this.textAlignLast = 0;
         }
      }

      this.textIndent = length(var3[294]);
      this.lineHeight = length(var2.lineHeight);
      this.outlineLevel = integer(var3[322], 0);
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

   private static int integer(Value var0, int var1) {
      return var0 != null && var0.type == 2 ? var0.integer() : var1;
   }

   public void print(PrintWriter var1) {
      var1.print("<style:style style:family=\"paragraph\"");
      var1.print(" style:name=\"" + this.name + "\"");
      if (this.listStyleName.length() > 0) {
         var1.print(" style:list-style-name=\"" + this.listStyleName + "\"");
      }

      if (this.masterPageName.length() > 0) {
         var1.print(" style:master-page-name=\"" + this.masterPageName + "\"");
      }

      if (this.outlineLevel >= 1 && this.outlineLevel <= 9) {
         var1.print(" style:default-outline-level=\"" + Integer.toString(this.outlineLevel) + "\"");
      }

      var1.println(">");
      var1.println("<style:paragraph-properties");
      if (this.breakBefore != 0) {
         var1.println(" fo:break-before=\"" + this.breakBefore() + "\"");
      }

      if (this.keepTogether) {
         var1.println(" fo:keep-together=\"always\"");
      }

      if (this.keepWithNext) {
         var1.println(" fo:keep-with-next=\"always\"");
      }

      if (this.marginTop > 0.0) {
         var1.println(" fo:margin-top=\"" + Odt.length(this.marginTop, 1) + "\"");
      }

      if (this.marginBottom > 0.0) {
         var1.println(" fo:margin-bottom=\"" + Odt.length(this.marginBottom, 1) + "\"");
      }

      if (this.marginLeft != 0.0) {
         var1.println(" fo:margin-left=\"" + Odt.length(this.marginLeft, 1) + "\"");
      }

      if (this.marginRight != 0.0) {
         var1.println(" fo:margin-right=\"" + Odt.length(this.marginRight, 1) + "\"");
      }

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

      var1.println(" fo:text-align=\"" + this.textAlign() + "\"");
      if (this.textAlign == 5) {
         var1.println(" fo:text-align-last=\"" + this.textAlignLast() + "\"");
      }

      if (this.textIndent != 0.0) {
         var1.println(" fo:text-indent=\"" + Odt.length(this.textIndent, 1) + "\"");
      }

      if (this.lineHeight > 0.0) {
         var1.println(" style:line-height-at-least=\"" + Odt.length(this.lineHeight, 1) + "\"");
      }

      var1.println(">");
      if (this.tabStops.count() > 0) {
         this.tabStops.print(var1);
      }

      var1.println("</style:paragraph-properties>");
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

   private String textAlign() {
      return toString(this.textAlign);
   }

   public static String toString(int var0) {
      String var1 = "start";
      switch (var0) {
         case 1:
            var1 = "end";
            break;
         case 2:
            var1 = "left";
            break;
         case 3:
            var1 = "right";
            break;
         case 4:
            var1 = "center";
            break;
         case 5:
            var1 = "justify";
      }

      return var1;
   }

   private String textAlignLast() {
      String var1 = "start";
      switch (this.textAlignLast) {
         case 4:
            var1 = "center";
            break;
         case 5:
            var1 = "justify";
      }

      return var1;
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
      var1 = shift(var1) ^ this.borders.hashCode();
      var1 = shift(var1) ^ hash(this.paddingTop);
      var1 = shift(var1) ^ hash(this.paddingBottom);
      var1 = shift(var1) ^ hash(this.paddingLeft);
      var1 = shift(var1) ^ hash(this.paddingRight);
      var1 = shift(var1) ^ this.background;
      var1 = shift(var1) ^ this.textAlign;
      var1 = shift(var1) ^ this.textAlignLast;
      var1 = shift(var1) ^ hash(this.textIndent);
      var1 = shift(var1) ^ hash(this.lineHeight);
      var1 = shift(var1) ^ this.outlineLevel;
      var1 = shift(var1) ^ this.tabStops.hashCode();
      var1 = shift(var1) ^ this.listStyleName.hashCode();
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
      } else if (!(var1 instanceof ParagraphStyle)) {
         return false;
      } else {
         ParagraphStyle var2 = (ParagraphStyle)var1;
         return this.breakBefore == var2.breakBefore && this.keepTogether == var2.keepTogether && this.keepWithNext == var2.keepWithNext && this.marginTop == var2.marginTop && this.marginBottom == var2.marginBottom && this.marginLeft == var2.marginLeft && this.marginRight == var2.marginRight && this.borders.equals(var2.borders) && this.paddingTop == var2.paddingTop && this.paddingBottom == var2.paddingBottom && this.paddingLeft == var2.paddingLeft && this.paddingRight == var2.paddingRight && this.background == var2.background && this.textAlign == var2.textAlign && this.textAlignLast == var2.textAlignLast && this.textIndent == var2.textIndent && this.lineHeight == var2.lineHeight && this.outlineLevel == var2.outlineLevel && this.tabStops.equals(var2.tabStops) && this.listStyleName.equals(var2.listStyleName) && this.masterPageName.equals(var2.masterPageName);
      }
   }

   public ParagraphStyle copy() {
      ParagraphStyle var1 = null;

      try {
         var1 = (ParagraphStyle)this.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }
}
