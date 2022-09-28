package com.xmlmind.fo.converter.rtf;

import java.io.PrintWriter;

public final class Break {
   public static final String TYPE_LINE = "line";
   public static final String TYPE_COLUMN = "column";
   public static final String TYPE_PAGE = "page";
   public static final Break COLUMN = new Break("column");
   public static final Break PAGE = new Break("page");
   public String type;
   public RunProperties properties;

   public Break(String var1) {
      this(var1, (RunProperties)null);
   }

   public Break(String var1, RunProperties var2) {
      this.type = var1;
      this.properties = var2;
   }

   public void print(PrintWriter var1) {
      var1.print("{");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println();
      var1.println("\\" + this.type);
      var1.println("}");
   }
}
