package com.xmlmind.fo.converter.wml;

import java.io.PrintWriter;
import java.util.Vector;

public final class ListTable {
   private Vector lists = new Vector();

   public int add(List var1) {
      var1.id = this.lists.size() + 1;
      this.lists.addElement(var1);
      return var1.id;
   }

   public void print(PrintWriter var1) {
      if (this.lists.size() != 0) {
         var1.println("<w:lists>");
         int var2 = 0;

         int var3;
         List var4;
         for(var3 = this.lists.size(); var2 < var3; ++var2) {
            var4 = (List)this.lists.elementAt(var2);
            var1.print("<w:listDef w:listDefId=\"" + var4.id + "\">");
            var1.print("<w:plt w:val=\"SingleLevel\" />");
            var1.println("<w:lvl w:ilvl=\"0\">");
            var1.print("<w:start w:val=\"" + var4.start + "\" />");
            var1.print("<w:nfc w:val=\"" + var4.style + "\" />");
            var1.print("<w:suff w:val=\"Tab\" />");
            if (var4.format != null) {
               var1.print("<w:lvlText w:val=\"" + var4.format + "\" />");
            }

            var1.print("<w:lvlJc w:val=\"" + var4.alignment + "\" />");
            if (var4.properties != null) {
               var4.properties.print(var1);
            }

            var1.print("</w:lvl>");
            var1.println("</w:listDef>");
         }

         var2 = 0;

         for(var3 = this.lists.size(); var2 < var3; ++var2) {
            var4 = (List)this.lists.elementAt(var2);
            var1.print("<w:list w:ilfo=\"" + var4.id + "\">");
            var1.print("<w:ilst w:val=\"" + var4.id + "\" />");
            var1.println("</w:list>");
         }

         var1.println("</w:lists>");
      }
   }

   public static class List {
      public static final int STYLE_ARABIC = 0;
      public static final int STYLE_UPPERCASE_ROMAN = 1;
      public static final int STYLE_LOWERCASE_ROMAN = 2;
      public static final int STYLE_UPPERCASE_LETTER = 3;
      public static final int STYLE_LOWERCASE_LETTER = 4;
      public static final int STYLE_BULLET = 23;
      public static final String ALIGNMENT_LEFT = "left";
      public static final String ALIGNMENT_CENTER = "center";
      public static final String ALIGNMENT_RIGHT = "right";
      public int style;
      public int start;
      public String alignment;
      public String format;
      public RunProperties properties;
      private int id;

      public List() {
         this(0, 1, "left");
      }

      public List(int var1, int var2, String var3) {
         this(var1, var2, var3, (String)null, (RunProperties)null);
      }

      public List(int var1, int var2, String var3, String var4, RunProperties var5) {
         this.style = var1;
         this.start = var2;
         this.alignment = var3;
         this.format = var4;
         this.properties = var5;
      }
   }
}
