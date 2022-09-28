package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.font.Font;
import com.xmlmind.fo.font.FontCache;
import com.xmlmind.fo.objects.Fo;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class RunProperties implements Cloneable {
   public FontFace font;
   public double fontSize;
   public boolean bold;
   public boolean italic;
   public boolean underline;
   public boolean strikeThrough;
   public boolean smallCaps;
   public boolean subscript;
   public boolean superscript;
   public double baselineShift;
   public int alignmentBaseline;
   public Color fgColor;
   public Color bgColor;
   public Color ulColor;
   public Border border;

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
      this.fontSize = var3[106].length();
      int var5 = var3[111].integer();
      if (var5 >= 700) {
         this.bold = true;
      }

      int var6 = var3[109].keyword();
      if (var6 == 92 || var6 == 139) {
         this.italic = true;
      }

      int var7 = var3[110].keyword();
      if (var7 == 184) {
         this.smallCaps = true;
      }

      this.underline = var1.textDecoration.underline;
      this.strikeThrough = var1.textDecoration.lineThrough;
      Value var8 = var3[14];
      if (var8.type == 1) {
         switch (var8.keyword()) {
            case 193:
               this.subscript = true;
               break;
            case 194:
               this.superscript = true;
         }
      } else {
         this.baselineShift = var8.length();
      }

      this.alignmentBaseline = var3[3].keyword();
      this.fgColor = var3[78].color();
      this.bgColor = var1.background;
      this.ulColor = var1.textDecoration.color;
      if (Fo.isInline(var1.fo)) {
         Borders var9 = new Borders(var3);
         if (var9.top.materialized() && var9.bottom.materialized() && var9.left.materialized() && var9.right.materialized()) {
            this.border = new Border(0);
            this.border.set(var9.top.style, var9.top.width, var9.top.color, var9.top.space);
         }
      }

   }

   public void print(PrintWriter var1) {
      var1.print("<w:rPr>");
      if (this.font != null) {
         var1.print("<w:rFonts");
         var1.print(" w:ascii=\"" + this.font.name + "\"");
         var1.print(" w:hAnsi=\"" + this.font.name + "\"");
         var1.print(" />");
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

      if (this.strikeThrough) {
         var1.print("<w:strike />");
      }

      if (this.fgColor != null) {
         var1.print("<w:color");
         var1.print(" w:val=\"" + Wml.hexColorType(this.fgColor) + "\"");
         var1.print(" />");
      }

      if (this.baselineShift != 0.0) {
         var1.print("<w:position w:val=\"" + Math.round(2.0 * this.baselineShift) + "\" />");
      }

      if (this.fontSize > 0.0) {
         var1.print("<w:sz w:val=\"" + Math.round(2.0 * this.fontSize) + "\" />");
      }

      if (this.underline) {
         var1.print("<w:u");
         var1.print(" w:val=\"single\"");
         if (this.ulColor != null) {
            var1.print(" w:color=\"" + Wml.hexColorType(this.ulColor) + "\"");
         }

         var1.print(" />");
      }

      if (this.border != null) {
         this.border.print(var1);
      }

      if (this.bgColor != null) {
         var1.print("<w:shd");
         var1.print(" w:val=\"clear\"");
         var1.print(" w:fill=\"" + Wml.hexColorType(this.bgColor) + "\"");
         var1.print(" />");
      }

      if (this.subscript) {
         var1.print("<w:vertAlign w:val=\"subscript\" />");
      } else if (this.superscript) {
         var1.print("<w:vertAlign w:val=\"superscript\" />");
      }

      var1.println("</w:rPr>");
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
      if (this.bold) {
         var2 |= 1;
      }

      if (this.italic) {
         var2 |= 2;
      }

      int var3 = (int)Math.ceil(this.fontSize);
      return var1 < 0 ? FontCache.getFont(this.font.name, var2, var3) : FontCache.getFont(var1, var2, var3);
   }

   public RunProperties copy() {
      RunProperties var1 = null;

      try {
         var1 = (RunProperties)this.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }
}
