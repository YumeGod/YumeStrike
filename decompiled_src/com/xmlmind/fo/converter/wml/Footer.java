package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public class Footer {
   public static final String TYPE_ODD = "odd";
   public static final String TYPE_EVEN = "even";
   public static final String TYPE_FIRST = "first";
   private String type;
   private StaticContent content;

   public Footer() {
      this("odd", (StaticContent)null);
   }

   public Footer(String var1, StaticContent var2) {
      this.type = var1;
      this.content = var2;
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      var1.println("<w:ftr w:type=\"" + this.type + "\">");
      if (this.content != null) {
         this.content.print(var1, var2);
      }

      var1.println("</w:ftr>");
   }
}
