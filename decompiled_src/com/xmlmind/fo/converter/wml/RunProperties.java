package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.font.Font;
import com.xmlmind.fo.font.FontCache;
import com.xmlmind.fo.objects.Fo;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class RunProperties {
   public FontTable.Font font;
   public int fontSize;
   public boolean bold;
   public boolean italic;
   public boolean underline;
   public boolean strikeThrough;
   public boolean smallCaps;
   public boolean subscript;
   public boolean superscript;
   public int baselineShift;
   public int alignmentBaseline;
   public Color fgColor;
   public Color bgColor;
   public Color ulColor;
   public Border border;
   public boolean preserveSpace;

   public RunProperties() {
   }

   public RunProperties(Context var1, FontTable var2) {
      this();
      this.initialize(var1, var2);
   }

   public void initialize(Context var1, FontTable var2) {
      Value[] var3 = var1.properties.values;
      Value[] var4 = var3[104].list();
      this.font = var2.add(var4[0].string());
      double var5 = var3[106].length();
      this.fontSize = (int)Math.round(2.0 * var5);
      int var7 = var3[111].integer();
      if (var7 >= 700) {
         this.bold = true;
      }

      int var8 = var3[109].keyword();
      if (var8 == 92 || var8 == 139) {
         this.italic = true;
      }

      int var9 = var3[110].keyword();
      if (var9 == 184) {
         this.smallCaps = true;
      }

      this.underline = var1.textDecoration.underline;
      this.strikeThrough = var1.textDecoration.lineThrough;
      Value var10 = var3[14];
      if (var10.type == 1) {
         switch (var10.keyword()) {
            case 193:
               this.subscript = true;
               break;
            case 194:
               this.superscript = true;
         }
      } else {
         double var11 = var10.length();
         this.baselineShift = (int)Math.round(2.0 * var11);
      }

      this.alignmentBaseline = var3[3].keyword();
      this.fgColor = var3[78].color();
      this.bgColor = var1.background;
      this.ulColor = var1.textDecoration.color;
      if (Fo.isInline(var1.fo)) {
         Borders var13 = new Borders(var3);
         if (var13.top.materialized() && var13.bottom.materialized() && var13.left.materialized() && var13.right.materialized()) {
            this.border = new Border("bdr");
            this.border.set(var13.top.style, var13.top.width, var13.top.color, var13.top.space);
         }
      }

      int var14 = var3[306].keyword();
      if (var14 == 152) {
         this.preserveSpace = true;
      }

   }

   public void print(PrintWriter var1) {
      var1.print("<w:rPr>");
      if (this.font != null) {
         var1.print("<w:rFonts");
         var1.print(" w:ascii=\"" + this.font.name + "\"");
         var1.print(" w:h-ansi=\"" + this.font.name + "\"");
         var1.print(" />");
      }

      if (this.fontSize > 0) {
         var1.print("<w:sz w:val=\"" + this.fontSize + "\" />");
      }

      if (this.bold) {
         var1.print("<w:b />");
      }

      if (this.italic) {
         var1.print("<w:i />");
      }

      if (this.smallCaps) {
         var1.print("<w:smallCaps />");
      }

      if (this.underline) {
         var1.print("<w:u");
         var1.print(" w:val=\"single\"");
         if (this.ulColor != null) {
            var1.print(" w:color=\"" + Wml.hexColorType(this.ulColor) + "\"");
         }

         var1.print(" />");
      }

      if (this.strikeThrough) {
         var1.print("<w:strike />");
      }

      if (this.subscript) {
         var1.print("<w:vertAlign w:val=\"subscript\" />");
      } else if (this.superscript) {
         var1.print("<w:vertAlign w:val=\"superscript\" />");
      } else if (this.baselineShift != 0) {
         var1.print("<w:position w:val=\"" + this.baselineShift + "\" />");
      }

      if (this.fgColor != null) {
         var1.print("<w:color");
         var1.print(" w:val=\"" + Wml.hexColorType(this.fgColor) + "\"");
         var1.print(" />");
      }

      if (this.bgColor != null) {
         var1.print("<w:shd");
         var1.print(" w:val=\"clear\"");
         var1.print(" w:fill=\"" + Wml.hexColorType(this.bgColor) + "\"");
         var1.print(" />");
      }

      if (this.border != null) {
         this.border.print(var1);
      }

      var1.println("</w:rPr>");
   }

   public Font font() {
      byte var1 = -1;
      if (this.font.family != null) {
         if ("Roman".equals(this.font.family)) {
            var1 = 1;
         } else if ("Swiss".equals(this.font.family)) {
            var1 = 2;
         } else if ("Modern".equals(this.font.family)) {
            var1 = 3;
         } else if ("Decorative".equals(this.font.family)) {
            var1 = 4;
         } else if ("Script".equals(this.font.family)) {
            var1 = 5;
         }
      }

      int var2 = 0;
      if (this.bold) {
         var2 |= 1;
      }

      if (this.italic) {
         var2 |= 2;
      }

      int var3 = this.fontSize / 2;
      return var1 < 0 ? FontCache.getFont(this.font.name, var2, var3) : FontCache.getFont(var1, var2, var3);
   }
}
