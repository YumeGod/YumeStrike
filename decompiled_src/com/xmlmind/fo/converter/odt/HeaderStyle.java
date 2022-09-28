package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.objects.Region;
import java.io.PrintWriter;

public final class HeaderStyle {
   public HeaderFooterProperties properties;

   public HeaderStyle() {
   }

   public HeaderStyle(Region var1) {
      this.initialize(var1);
   }

   public void initialize(Region var1) {
      this.properties = new HeaderFooterProperties(var1);
      this.properties.marginTop = 0.0;
   }

   public void print(PrintWriter var1) {
      var1.println("<style:header-style>");
      if (this.properties != null && this.properties.height > 0.0) {
         this.properties.print(var1);
      }

      var1.println("</style:header-style>");
   }
}
