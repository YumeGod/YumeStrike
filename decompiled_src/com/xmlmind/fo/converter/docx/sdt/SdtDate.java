package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.RunProperties;
import com.xmlmind.fo.converter.docx.Wml;
import java.io.PrintWriter;
import org.xml.sax.Attributes;

public final class SdtDate extends SdtElement {
   private static final String DEFAULT_FORMAT = "%Y-%M-%D";
   private String format;

   public SdtDate(Attributes var1, RunProperties var2) {
      super(var1, var2);
      this.format = var1.getValue("", "format");
      if (this.format == null) {
         this.format = "%Y-%M-%D";
      }

      if (this.title == null) {
         this.title = "Date";
      }

   }

   protected void printType(PrintWriter var1) {
      String var2 = dateFormat(this.format);
      if (var2 == null) {
         var2 = dateFormat("%Y-%M-%D");
      }

      var1.println("<w:date>");
      if (var2 != null) {
         var1.println("<w:dateFormat w:val=\"" + Wml.escape(var2) + "\" />");
      }

      var1.println("<w:storeMappedDataAs w:val=\"text\" />");
      var1.println("</w:date>");
   }

   private static String dateFormat(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;

      for(int var3 = var0.length(); var2 < var3; ++var2) {
         char var4 = var0.charAt(var2);
         if (var4 == '%') {
            ++var2;
            if (var2 >= var3) {
               return null;
            }

            switch (var0.charAt(var2)) {
               case '%':
                  var1.append('%');
                  break;
               case 'D':
                  var1.append("dd");
                  break;
               case 'M':
                  var1.append("MM");
                  break;
               case 'Y':
                  var1.append("yyyy");
                  break;
               case 'y':
                  var1.append("yy");
                  break;
               default:
                  return null;
            }
         } else {
            var1.append(var4);
         }
      }

      return var1.toString();
   }
}
