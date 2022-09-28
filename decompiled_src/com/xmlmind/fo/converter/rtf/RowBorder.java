package com.xmlmind.fo.converter.rtf;

import java.io.PrintWriter;

public class RowBorder extends Border implements Cloneable {
   public RowBorder(int var1) {
      super(var1);
   }

   public void print(PrintWriter var1) {
      switch (this.side) {
         case 1:
            var1.print("\\trbrdrt");
            break;
         case 2:
            var1.print("\\trbrdrb");
            break;
         case 3:
            var1.print("\\trbrdrl");
            break;
         case 4:
            var1.print("\\trbrdrr");
            break;
         default:
            return;
      }

      var1.print("\\brdr" + this.style());
      var1.print("\\brdrw" + this.width());
      if (this.color > 0) {
         var1.print("\\brdrcf" + this.color);
      }

   }
}
