package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public final class Link {
   private static final int TYPE_NONE = 0;
   private static final int TYPE_INTERNAL = 1;
   private static final int TYPE_EXTERNAL = 2;
   private int type;
   private String target;

   public Link(Context var1) {
      PropertyValues var2 = var1.properties;
      Value var3;
      if (var2.isSpecified(132)) {
         this.type = 1;
         var3 = var2.values[132];
         this.target = var3.idref();
      } else if (var2.isSpecified(100)) {
         this.type = 2;
         var3 = var2.values[100];
         this.target = var3.uriSpecification();
      }

   }

   public void start(PrintWriter var1, Encoder var2) {
      if (this.target != null && this.target.length() > 0) {
         String var3;
         if (this.type == 1) {
            var3 = "#" + Odt.escape(this.target, var2);
         } else {
            var3 = Odt.escape(this.target, var2);
         }

         var1.print("<text:a");
         var1.println(" xlink:href=\"" + var3 + "\"");
         var1.print(">");
      }

   }

   public void end(PrintWriter var1) {
      if (this.target != null && this.target.length() > 0) {
         var1.println("</text:a");
         var1.print(">");
      }

   }
}
