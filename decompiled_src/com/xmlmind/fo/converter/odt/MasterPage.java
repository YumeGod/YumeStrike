package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public class MasterPage {
   public String name;
   public String pageLayout;
   public String nextMasterPage;
   public Header header;
   public Footer footer;

   public MasterPage(String var1) {
      this(var1, (String)null);
   }

   public MasterPage(String var1, String var2) {
      this.pageLayout = var1;
      this.nextMasterPage = var2;
   }

   public void print(PrintWriter var1, Encoder var2) {
      var1.println("<style:master-page style:name=\"" + this.name + "\"");
      var1.println(" style:page-layout-name=\"" + this.pageLayout + "\"");
      if (this.nextMasterPage != null) {
         var1.println(" style:next-style-name=\"" + this.nextMasterPage + "\"");
      }

      var1.println(">");
      if (this.header != null) {
         this.header.print(var1, var2);
      }

      if (this.footer != null) {
         this.footer.print(var1, var2);
      }

      var1.println("</style:master-page>");
   }
}
