package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.MsTranslator;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class Link {
   public static final int TYPE_INTERNAL = 0;
   public static final int TYPE_EXTERNAL = 1;
   public int type;
   public String target;
   public String id;

   public Link(Context var1) {
      PropertyValues var3 = var1.properties;
      Value var2;
      if (var3.isSpecified(132)) {
         this.type = 0;
         var2 = var3.values[132];
         this.target = ((MsTranslator)var1.translator).checkBookmark(var2.idref());
      } else if (var3.isSpecified(100)) {
         this.type = 1;
         var2 = var3.values[100];
         this.target = escape(var2.uriSpecification());
      }

   }

   private static String escape(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      for(int var3 = var0.length(); var2 < var3; ++var2) {
         char var4 = var0.charAt(var2);
         if (var4 > 31 && var4 != 127) {
            switch (var4) {
               case ' ':
               case '"':
               case '<':
               case '>':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '`':
               case '{':
               case '|':
               case '}':
                  var1.append(escape(var4));
                  break;
               default:
                  var1.append(var4);
            }
         } else {
            var1.append(escape(var4));
         }
      }

      return var1.toString();
   }

   private static String escape(char var0) {
      return "%" + Integer.toHexString(var0 | 256).substring(1);
   }

   public void start(PrintWriter var1) {
      var1.print("<w:hyperlink");
      switch (this.type) {
         case 0:
            if (this.target != null) {
               var1.print(" w:anchor=\"" + Wml.escape(this.target) + "\"");
            }
            break;
         case 1:
            if (this.id != null) {
               var1.print(" r:id=\"" + this.id + "\"");
            }
      }

      var1.println(">");
   }

   public void end(PrintWriter var1) {
      var1.println("</w:hyperlink>");
   }
}
