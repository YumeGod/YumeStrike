package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public class Footer {
   public static final String TYPE_ALL = "";
   public static final String TYPE_ODD = "r";
   public static final String TYPE_EVEN = "l";
   public static final String TYPE_FIRST = "f";
   private String type;
   private StaticContent content;

   public Footer() {
      this("", (StaticContent)null);
   }

   public Footer(String var1, StaticContent var2) {
      this.type = var1;
      this.content = var2;
   }

   public void print(PrintWriter var1, Encoder var2, int var3) throws Exception {
      var1.println("{\\footer" + this.type);
      if (this.content != null) {
         this.content.print(var1, var2, var3);
      }

      var1.println("}");
   }
}
