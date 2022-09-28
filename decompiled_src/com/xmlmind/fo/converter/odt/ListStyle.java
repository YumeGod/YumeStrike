package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;

public final class ListStyle {
   public static final int TYPE_NUMBER = 0;
   public static final int TYPE_BULLET = 1;
   public static final int FORMAT_DECIMAL = 0;
   public static final int FORMAT_LOWERCASE_LETTER = 1;
   public static final int FORMAT_UPPERCASE_LETTER = 2;
   public static final int FORMAT_LOWERCASE_ROMAN = 3;
   public static final int FORMAT_UPPERCASE_ROMAN = 4;
   public String name;
   public int listLevel;
   public int labelType;
   public int numberFormat;
   public String numberPrefix;
   public String numberSuffix;
   public int startValue;
   public char bulletCharacter;
   public int labelAlignment;
   public double labelWidth;
   public double labelDistance;
   public TextStyle textStyle;

   public ListStyle(int var1, int var2) {
      this(var1, var2, (TextStyle)null);
   }

   public ListStyle(int var1, int var2, TextStyle var3) {
      this.listLevel = 1;
      this.numberPrefix = "";
      this.numberSuffix = "";
      this.startValue = 1;
      this.labelAlignment = 0;
      this.labelType = 0;
      this.numberFormat = var1;
      this.startValue = var2;
      this.textStyle = var3;
   }

   public ListStyle(char var1) {
      this(var1, (TextStyle)null);
   }

   public ListStyle(char var1, TextStyle var2) {
      this.listLevel = 1;
      this.numberPrefix = "";
      this.numberSuffix = "";
      this.startValue = 1;
      this.labelAlignment = 0;
      this.labelType = 1;
      this.bulletCharacter = var1;
      this.textStyle = var2;
   }

   public void print(PrintWriter var1) {
      var1.print("<text:list-style");
      var1.println(" style:name=\"" + this.name + "\">");

      for(int var2 = 1; var2 <= this.listLevel; ++var2) {
         var1.println("<text:list-level-style-" + this.labelType());
         var1.println(" text:level=\"" + var2 + "\"");
         if (this.textStyle != null && this.textStyle.name != null) {
            var1.println(" text:style-name=\"" + this.textStyle.name + "\"");
         }

         if (this.labelType == 1) {
            var1.println(" text:bullet-char=\"&#" + Integer.toString(this.bulletCharacter) + ";\"");
         } else {
            var1.println(" style:num-format=\"" + this.numberFormat() + "\"");
            if (this.numberPrefix.length() > 0) {
               var1.println(" style:num-prefix=\"" + this.numberPrefix + "\"");
            }

            if (this.numberSuffix.length() > 0) {
               var1.println(" style:num-suffix=\"" + this.numberSuffix + "\"");
            }

            if (this.startValue != 1) {
               var1.println(" text:start-value=\"" + this.startValue + "\"");
            }
         }

         var1.println(">");
         var1.println("<style:list-level-properties");
         var1.println(" fo:text-align=\"" + this.labelAlignment() + "\"");
         if (this.labelWidth > 0.0) {
            var1.println(" text:min-label-width=\"" + Odt.length(this.labelWidth, 1) + "\"");
         }

         if (this.labelDistance > 0.0) {
            var1.println(" text:min-label-distance=\"" + Odt.length(this.labelDistance, 1) + "\"");
         }

         var1.println("/>");
         var1.println("</text:list-level-style-" + this.labelType() + ">");
      }

      var1.println("</text:list-style>");
   }

   private String labelType() {
      return this.labelType == 1 ? "bullet" : "number";
   }

   private char numberFormat() {
      char var1 = '1';
      switch (this.numberFormat) {
         case 1:
            var1 = 'a';
            break;
         case 2:
            var1 = 'A';
            break;
         case 3:
            var1 = 'i';
            break;
         case 4:
            var1 = 'I';
      }

      return var1;
   }

   private String labelAlignment() {
      return ParagraphStyle.toString(this.labelAlignment);
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.labelType;
      var1 = shift(var1) ^ this.numberFormat;
      var1 = shift(var1) ^ this.numberPrefix.hashCode();
      var1 = shift(var1) ^ this.numberSuffix.hashCode();
      var1 = shift(var1) ^ this.startValue;
      var1 = shift(var1) ^ this.bulletCharacter;
      var1 = shift(var1) ^ this.labelAlignment;
      var1 = shift(var1) ^ hash(this.labelWidth);
      var1 = shift(var1) ^ hash(this.labelDistance);
      if (this.textStyle != null) {
         var1 = shift(var1) ^ this.textStyle.hashCode();
      }

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
      } else if (!(var1 instanceof ListStyle)) {
         return false;
      } else {
         ListStyle var2 = (ListStyle)var1;
         return this.labelType == var2.labelType && this.numberFormat == var2.numberFormat && this.numberPrefix.equals(var2.numberPrefix) && this.numberSuffix.equals(var2.numberSuffix) && this.startValue == var2.startValue && this.bulletCharacter == var2.bulletCharacter && this.labelAlignment == var2.labelAlignment && this.labelWidth == var2.labelWidth && this.labelDistance == var2.labelDistance && (this.textStyle == null && var2.textStyle == null || this.textStyle != null && this.textStyle.equals(var2.textStyle));
      }
   }
}
