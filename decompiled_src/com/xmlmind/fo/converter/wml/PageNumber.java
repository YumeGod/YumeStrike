package com.xmlmind.fo.converter.wml;

import java.io.PrintWriter;

public final class PageNumber {
   public RunProperties properties;

   public PageNumber() {
      this((RunProperties)null);
   }

   public PageNumber(RunProperties var1) {
      this.properties = var1;
   }

   public void print(PrintWriter var1) {
      var1.println("<w:r>");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println("<w:pgNum />");
      var1.println("</w:r>");
   }
}
