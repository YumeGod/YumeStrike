package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;

public class Span {
   public static void start(PrintWriter var0, TextStyle var1) {
      var0.print("<text:span");
      if (var1 != null && var1.name != null) {
         var0.println(" text:style-name=\"" + var1.name + "\"");
      } else {
         var0.println("");
      }

      var0.print(">");
   }

   public static void end(PrintWriter var0) {
      var0.println("</text:span");
      var0.print(">");
   }
}
