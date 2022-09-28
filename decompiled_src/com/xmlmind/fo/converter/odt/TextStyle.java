package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.font.Font;
import com.xmlmind.fo.font.FontCache;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class TextStyle implements Cloneable {
   public static final int FONT_STYLE_NORMAL = 0;
   public static final int FONT_STYLE_ITALIC = 1;
   public static final int FONT_STYLE_OBLIQUE = 2;
   public static final int FONT_VARIANT_NORMAL = 0;
   public static final int FONT_VARIANT_SMALL_CAPS = 1;
   public static final int COLOR_TRANSPARENT = -1;
   public String name;
   public FontFace font;
   public double fontSize;
   public int fontStyle;
   public int fontWeight;
   public int fontVariant;
   public int fgColor;
   public int bgColor;
   public boolean lineThrough;
   public boolean underline;
   public int ulColor;
   public boolean subscript;
   public boolean superscript;
   public double textPosition;

   public TextStyle(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      Value[] var3 = var2[104].list();
      this.font = new FontFace(((OdtTranslator)var1.translator).unaliasFontFamily(var3[0].string()));
      this.fontSize = var2[106].length();
      switch (var2[109].keyword()) {
         case 92:
            this.fontStyle = 1;
            break;
         case 139:
            this.fontStyle = 2;
      }

      this.fontWeight = var2[111].integer();
      switch (var2[110].keyword()) {
         case 184:
            this.fontVariant = 1;
         default:
            this.fgColor = Odt.rgb(var2[78].color());
            if (var1.background != null) {
               this.bgColor = Odt.rgb(var1.background);
            } else {
               this.bgColor = -1;
            }

            if (var1.textDecoration.color != null) {
               this.ulColor = Odt.rgb(var1.textDecoration.color);
            } else {
               this.ulColor = this.fgColor;
            }

            this.lineThrough = var1.textDecoration.lineThrough;
            this.underline = var1.textDecoration.underline;
            Value var4 = var2[14];
            if (var4.type == 1) {
               switch (var4.keyword()) {
                  case 193:
                     this.subscript = true;
                     break;
                  case 194:
                     this.superscript = true;
               }
            } else {
               double var5 = var1.parent().fontSize();
               double var7 = var4.length();
               this.textPosition = var7 / var5 * 100.0;
            }

      }
   }

   public Font getFont() {
      byte var1 = -1;
      if (this.font.family != null) {
         if ("roman".equals(this.font.family)) {
            var1 = 1;
         } else if ("swiss".equals(this.font.family)) {
            var1 = 2;
         } else if ("modern".equals(this.font.family)) {
            var1 = 3;
         } else if ("decorative".equals(this.font.family)) {
            var1 = 4;
         } else if ("script".equals(this.font.family)) {
            var1 = 5;
         }
      }

      int var2 = 0;
      if (this.fontWeight >= 700) {
         var2 |= 1;
      }

      if (this.fontStyle == 1 || this.fontStyle == 2) {
         var2 |= 2;
      }

      int var3 = (int)Math.ceil(this.fontSize);
      return var1 < 0 ? FontCache.getFont(this.font.name, var2, var3) : FontCache.getFont(var1, var2, var3);
   }

   public void print(PrintWriter var1) {
      var1.print("<style:style style:family=\"text\"");
      var1.println(" style:name=\"" + this.name + "\">");
      this.printProperties(var1);
      var1.println("</style:style>");
   }

   public void printProperties(PrintWriter var1) {
      var1.println("<style:text-properties");
      var1.println(" style:font-name=\"" + this.font.id + "\"");
      var1.println(" fo:font-size=\"" + Odt.length(this.fontSize, 1) + "\"");
      var1.println(" fo:font-style=\"" + this.fontStyle() + "\"");
      var1.println(" fo:font-weight=\"" + this.fontWeight() + "\"");
      var1.println(" fo:font-variant=\"" + this.fontVariant() + "\"");
      var1.println(" fo:color=\"" + Odt.color(this.fgColor) + "\"");
      if (this.bgColor != -1) {
         var1.println(" fo:background-color=\"" + Odt.color(this.bgColor) + "\"");
      }

      if (this.lineThrough) {
         var1.println(" style:text-line-through-type=\"single\"");
         var1.println(" style:text-line-through-style=\"solid\"");
      }

      if (this.underline) {
         var1.println(" style:text-underline-type=\"single\"");
         var1.println(" style:text-underline-style=\"solid\"");
         var1.println(" style:text-underline-color=\"" + Odt.color(this.ulColor) + "\"");
      }

      if (this.subscript || this.superscript || this.textPosition != 0.0) {
         var1.println(" style:text-position=\"" + this.textPosition() + "\"");
      }

      var1.println("/>");
   }

   private String fontStyle() {
      String var1 = "normal";
      switch (this.fontStyle) {
         case 1:
            var1 = "italic";
            break;
         case 2:
            var1 = "oblique";
      }

      return var1;
   }

   private String fontWeight() {
      return this.fontWeight >= 700 ? "bold" : "normal";
   }

   private String fontVariant() {
      return this.fontVariant == 1 ? "small-caps" : "normal";
   }

   private String textPosition() {
      if (this.subscript) {
         return "sub";
      } else {
         return this.superscript ? "super" : Odt.percent(this.textPosition, 0);
      }
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.font.hashCode();
      var1 = shift(var1) ^ hash(this.fontSize);
      var1 = shift(var1) ^ this.fontStyle;
      var1 = shift(var1) ^ this.fontWeight;
      var1 = shift(var1) ^ this.fontVariant;
      var1 = shift(var1) ^ this.fgColor;
      var1 = shift(var1) ^ this.bgColor;
      var1 = shift(var1) ^ hash(this.lineThrough);
      var1 = shift(var1) ^ hash(this.underline);
      var1 = shift(var1) ^ this.ulColor;
      var1 = shift(var1) ^ hash(this.subscript);
      var1 = shift(var1) ^ hash(this.superscript);
      var1 = shift(var1) ^ hash(this.textPosition);
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
      } else if (!(var1 instanceof TextStyle)) {
         return false;
      } else {
         TextStyle var2 = (TextStyle)var1;
         return this.font.equals(var2.font) && this.fontSize == var2.fontSize && this.fontStyle == var2.fontStyle && this.fontWeight == var2.fontWeight && this.fontVariant == var2.fontVariant && this.fgColor == var2.fgColor && this.bgColor == var2.bgColor && this.lineThrough == var2.lineThrough && this.underline == var2.underline && this.ulColor == var2.ulColor && this.subscript == var2.subscript && this.superscript == var2.superscript && this.textPosition == var2.textPosition;
      }
   }

   public TextStyle copy() {
      TextStyle var1 = null;

      try {
         var1 = (TextStyle)this.clone();
         var1.name = null;
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }
}
