package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;

public final class GraphicStyle {
   public static final int COLOR_TRANSPARENT = -1;
   public static final int ANCHOR_TYPE_PAGE = 0;
   public static final int ANCHOR_TYPE_FRAME = 1;
   public static final int ANCHOR_TYPE_PARAGRAPH = 2;
   public static final int ANCHOR_TYPE_CHAR = 3;
   public static final int ANCHOR_TYPE_AS_CHAR = 4;
   public static final int VP_TOP = 0;
   public static final int VP_MIDDLE = 1;
   public static final int VP_BOTTOM = 2;
   public static final int VP_FROM_TOP = 3;
   public static final int VP_BELOW = 4;
   public static final int VR_PAGE = 0;
   public static final int VR_PAGE_CONTENT = 1;
   public static final int VR_FRAME = 2;
   public static final int VR_FRAME_CONTENT = 3;
   public static final int VR_PARAGRAPH = 4;
   public static final int VR_PARAGRAPH_CONTENT = 5;
   public static final int VR_CHAR = 6;
   public static final int VR_LINE = 7;
   public static final int VR_BASELINE = 8;
   public static final int VR_TEXT = 9;
   public String name;
   public int anchorType;
   public int verticalPosition;
   public int verticalRelation;
   public double marginTop;
   public double marginBottom;
   public double marginLeft;
   public double marginRight;
   public Borders borders = new Borders();
   public double paddingTop;
   public double paddingBottom;
   public double paddingLeft;
   public double paddingRight;
   public int background = -1;
   public double clipTop;
   public double clipBottom;
   public double clipLeft;
   public double clipRight;

   public void print(PrintWriter var1) {
      var1.print("<style:style style:family=\"graphic\"");
      var1.println(" style:name=\"" + this.name + "\"");
      var1.println(" style:parent-style-name=\"Graphics\"");
      var1.println(">");
      var1.println("<style:graphic-properties");
      var1.println(" text:anchor-type=\"" + this.anchorType() + "\"");
      var1.println(" style:vertical-pos=\"" + this.verticalPosition() + "\"");
      var1.println(" style:vertical-rel=\"" + this.verticalRelation() + "\"");
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

      if (this.clipTop != 0.0 || this.clipBottom != 0.0 || this.clipLeft != 0.0 || this.clipRight != 0.0) {
         var1.println(" fo:clip=\"" + this.clipping() + "\"");
      }

      var1.println("/>");
      var1.println("</style:style>");
   }

   private String anchorType() {
      String var1 = "page";
      switch (this.anchorType) {
         case 1:
            var1 = "frame";
            break;
         case 2:
            var1 = "paragraph";
            break;
         case 3:
            var1 = "char";
            break;
         case 4:
            var1 = "as-char";
      }

      return var1;
   }

   private String verticalPosition() {
      String var1 = "top";
      switch (this.verticalPosition) {
         case 1:
            var1 = "middle";
            break;
         case 2:
            var1 = "bottom";
            break;
         case 3:
            var1 = "from-top";
            break;
         case 4:
            var1 = "below";
      }

      return var1;
   }

   private String verticalRelation() {
      String var1 = "page";
      switch (this.verticalRelation) {
         case 1:
            var1 = "page-content";
            break;
         case 2:
            var1 = "frame";
            break;
         case 3:
            var1 = "frame-content";
            break;
         case 4:
            var1 = "paragraph";
            break;
         case 5:
            var1 = "paragraph-content";
            break;
         case 6:
            var1 = "char";
            break;
         case 7:
            var1 = "line";
            break;
         case 8:
            var1 = "baseline";
            break;
         case 9:
            var1 = "text";
      }

      return var1;
   }

   private String clipping() {
      StringBuffer var1 = new StringBuffer("rect(");
      var1.append(Odt.length(this.clipTop, 1));
      var1.append(" " + Odt.length(this.clipRight, 1));
      var1.append(" " + Odt.length(this.clipBottom, 1));
      var1.append(" " + Odt.length(this.clipLeft, 1));
      var1.append(")");
      return var1.toString();
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.anchorType;
      var1 = shift(var1) ^ this.verticalPosition;
      var1 = shift(var1) ^ this.verticalRelation;
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
      var1 = shift(var1) ^ hash(this.clipTop);
      var1 = shift(var1) ^ hash(this.clipBottom);
      var1 = shift(var1) ^ hash(this.clipLeft);
      var1 = shift(var1) ^ hash(this.clipRight);
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
      } else if (!(var1 instanceof GraphicStyle)) {
         return false;
      } else {
         GraphicStyle var2 = (GraphicStyle)var1;
         return this.anchorType == var2.anchorType && this.verticalPosition == var2.verticalPosition && this.verticalRelation == var2.verticalRelation && this.marginTop == var2.marginTop && this.marginBottom == var2.marginBottom && this.marginLeft == var2.marginLeft && this.marginRight == var2.marginRight && this.borders.equals(var2.borders) && this.paddingTop == var2.paddingTop && this.paddingBottom == var2.paddingBottom && this.paddingLeft == var2.paddingLeft && this.paddingRight == var2.paddingRight && this.background == var2.background && this.clipTop == var2.clipTop && this.clipBottom == var2.clipBottom && this.clipLeft == var2.clipLeft && this.clipRight == var2.clipRight;
      }
   }
}
