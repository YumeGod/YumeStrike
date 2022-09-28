package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public final class Footer {
   public StaticContent content;
   public boolean isLeft;

   public Footer() {
      this(new StaticContent());
   }

   public Footer(StaticContent var1) {
      this(var1, false);
   }

   public Footer(StaticContent var1, boolean var2) {
      this.content = var1;
      this.isLeft = var2;
   }

   public void print(PrintWriter var1, Encoder var2) {
      if (this.isLeft) {
         var1.println("<style:footer-left>");
      } else {
         var1.println("<style:footer>");
      }

      this.content.print(var1, var2);
      if (this.isLeft) {
         var1.println("</style:footer-left>");
      } else {
         var1.println("</style:footer>");
      }

   }
}
