package com.xmlmind.fo.converter.docx;

import java.io.PrintWriter;

public final class Numbering {
   public static final int STYLE_DECIMAL = 0;
   public static final int STYLE_UPPER_ROMAN = 1;
   public static final int STYLE_LOWER_ROMAN = 2;
   public static final int STYLE_UPPER_LETTER = 3;
   public static final int STYLE_LOWER_LETTER = 4;
   public static final int STYLE_BULLET = 5;
   public static final int ALIGNMENT_LEFT = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_RIGHT = 2;
   public int id;
   public int style;
   public int start;
   public int alignment;
   public String format;
   public RunProperties properties;

   public Numbering(int var1) {
      this(var1, 1);
   }

   public Numbering(int var1, int var2) {
      this(var1, var2, 0, (String)null, (RunProperties)null);
   }

   public Numbering(int var1, int var2, int var3, String var4, RunProperties var5) {
      this.style = var1;
      this.start = var2;
      this.alignment = var3;
      this.format = var4;
      this.properties = var5;
   }

   public void print(PrintWriter var1) {
      var1.println("<w:abstractNum w:abstractNumId=\"" + this.id + "\">");
      var1.println("<w:nsid w:val=\"" + this.nsid() + "\" />");
      var1.println("<w:multiLevelType w:val=\"singleLevel\" />");
      var1.println("<w:lvl w:ilvl=\"0\">");
      var1.println("<w:start w:val=\"" + this.start + "\" />");
      var1.println("<w:numFmt w:val=\"" + this.style() + "\" />");
      var1.println("<w:suff w:val=\"tab\" />");
      if (this.format != null) {
         var1.println("<w:lvlText w:val=\"" + Wml.escape(this.format) + "\" />");
      }

      var1.println("<w:lvlJc w:val=\"" + this.alignment() + "\" />");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println("</w:lvl>");
      var1.println("</w:abstractNum>");
   }

   private String nsid() {
      String var1 = Integer.toHexString(~this.id).toUpperCase();
      int var2 = var1.length();
      if (var2 < 8) {
         var1 = "00000000".substring(0, 8 - var2) + var1;
      }

      return var1;
   }

   private String style() {
      String var1;
      switch (this.style) {
         case 0:
         default:
            var1 = "decimal";
            break;
         case 1:
            var1 = "upperRoman";
            break;
         case 2:
            var1 = "lowerRoman";
            break;
         case 3:
            var1 = "upperLetter";
            break;
         case 4:
            var1 = "lowerLetter";
            break;
         case 5:
            var1 = "bullet";
      }

      return var1;
   }

   private String alignment() {
      String var1;
      switch (this.alignment) {
         case 0:
         default:
            var1 = "left";
            break;
         case 1:
            var1 = "center";
            break;
         case 2:
            var1 = "right";
      }

      return var1;
   }
}
