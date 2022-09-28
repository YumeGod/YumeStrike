package com.xmlmind.fo.converter.rtf;

import java.io.PrintWriter;

public class PageBorder extends Border implements Cloneable {
   public PageBorder(int var1) {
      super(var1);
   }

   public void print(PrintWriter var1) {
      switch (this.side) {
         case 1:
            var1.print("\\pgbrdrt");
            break;
         case 2:
            var1.print("\\pgbrdrb");
            break;
         case 3:
            var1.print("\\pgbrdrl");
            break;
         case 4:
            var1.print("\\pgbrdrr");
            break;
         default:
            return;
      }

      var1.print("\\brdr" + this.style());
      var1.print("\\brdrw" + this.width());
      var1.print("\\brsp" + this.space);
      if (this.color > 0) {
         var1.print("\\brdrcf" + this.color);
      }

   }
}
