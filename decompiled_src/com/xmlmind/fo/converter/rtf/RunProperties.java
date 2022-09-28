package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.font.Font;
import com.xmlmind.fo.font.FontCache;
import com.xmlmind.fo.objects.Fo;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public class RunProperties implements Cloneable {
   public FontTable.Font font;
   public int fontIndex;
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
   public int fgColor;
   public int bgColor;
   public int ulColor;
   public Border border;
   public boolean preserveSpace;

   public RunProperties() {
   }

   public RunProperties(Context var1, FontTable var2, ColorTable var3) {
      this();
      this.initialize(var1, var2, var3);
   }

   public void initialize(Context var1, FontTable var2, ColorTable var3) {
      Value[] var4 = var1.properties.values;
      Value[] var5 = var4[104].list();
      String var6 = var5[0].string();
      this.fontIndex = var2.index(var6);
      if (this.fontIndex < 0) {
         this.fontIndex = var2.add(var6);
      }

      this.font = var2.font(this.fontIndex);
      double var7 = var4[106].length();
      this.fontSize = (int)Math.round(2.0 * var7);
      int var9 = var4[111].integer();
      if (var9 >= 700) {
         this.bold = true;
      }

      int var10 = var4[109].keyword();
      if (var10 == 92 || var10 == 139) {
         this.italic = true;
      }

      int var11 = var4[110].keyword();
      if (var11 == 184) {
         this.smallCaps = true;
      }

      this.underline = var1.textDecoration.underline;
      this.strikeThrough = var1.textDecoration.lineThrough;
      Value var12 = var4[14];
      int var13;
      if (var12.type == 1) {
         switch (var12.keyword()) {
            case 193:
               this.subscript = true;
               break;
            case 194:
               this.superscript = true;
         }
      } else {
         var13 = (int)Math.round(2.0 * var12.length());
         if (var13 < 0) {
            this.subscript = true;
         } else if (var13 > 0) {
            this.superscript = true;
         }

         this.baselineShift = Math.abs(var13);
      }

      this.alignmentBaseline = var4[3].keyword();
      this.fgColor = Rtf.colorIndex(var4[78].color(), var3);
      if (var1.background != null) {
         this.bgColor = Rtf.colorIndex(var1.background, var3);
      }

      if (var1.textDecoration.color != null) {
         this.ulColor = Rtf.colorIndex(var1.textDecoration.color, var3);
      }

      if (Fo.isInline(var1.fo)) {
         Borders var14 = new Borders(var4, var3);
         if (var14.top.materialized() && var14.bottom.materialized() && var14.left.materialized() && var14.right.materialized()) {
            this.border = new Border(5);
            this.border.set(var14.top.style, var14.top.width, var14.top.color, var14.top.space);
         }
      }

      var13 = var4[306].keyword();
      if (var13 == 152) {
         this.preserveSpace = true;
      }

   }

   public void print(PrintWriter var1) {
      var1.print("\\plain");
      if (this.fontIndex >= 0) {
         var1.print("\\f" + this.fontIndex);
      }

      var1.print("\\fs" + this.fontSize);
      if (this.bold) {
         var1.print("\\b");
      }

      if (this.italic) {
         var1.print("\\i");
      }

      if (this.underline) {
         var1.print("\\ul");
      }

      if (this.strikeThrough) {
         var1.print("\\strike");
      }

      if (this.smallCaps) {
         var1.print("\\scaps");
      }

      if (this.subscript) {
         var1.print("\\sub");
         if (this.baselineShift > 0) {
            var1.print("\\dn" + this.baselineShift);
         }
      } else if (this.superscript) {
         var1.print("\\super");
         if (this.baselineShift > 0) {
            var1.print("\\up" + this.baselineShift);
         }
      }

      if (this.fgColor > 0) {
         var1.print("\\cf" + this.fgColor);
      }

      if (this.bgColor > 0) {
         var1.print("\\chcbpat" + this.bgColor);
      }

      if (this.ulColor > 0) {
         var1.print("\\ulc" + this.ulColor);
      }

      if (this.border != null) {
         this.border.print(var1);
      }

   }

   public Font font() {
      byte var1 = -1;
      if ("roman".equals(this.font.family)) {
         var1 = 1;
      } else if ("swiss".equals(this.font.family)) {
         var1 = 2;
      } else if ("modern".equals(this.font.family)) {
         var1 = 3;
      } else if ("decor".equals(this.font.family)) {
         var1 = 4;
      } else if ("script".equals(this.font.family)) {
         var1 = 5;
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

   public RunProperties copy() {
      RunProperties var1 = null;

      try {
         var1 = (RunProperties)this.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }
}
