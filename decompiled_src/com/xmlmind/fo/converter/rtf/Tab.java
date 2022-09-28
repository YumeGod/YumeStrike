package com.xmlmind.fo.converter.rtf;

import java.io.PrintWriter;

public final class Tab {
   public RunProperties properties;
   public boolean isSeparator;

   public Tab() {
      this((RunProperties)null);
   }

   public Tab(RunProperties var1) {
      this.properties = var1;
   }

   public void print(PrintWriter var1) {
      var1.print("{");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println();
      var1.println("\\tab");
      var1.println("}");
   }
}
