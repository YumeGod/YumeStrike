package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public final class Bookmark {
   public String name;

   public Bookmark(String var1) {
      this.name = var1;
   }

   public void print(PrintWriter var1, Encoder var2) {
      var1.print("<text:bookmark text:name=\"" + Odt.escape(this.name, var2) + "\"/>");
   }

   public void start(PrintWriter var1, Encoder var2) {
      var1.print("<text:bookmark-start text:name=\"" + Odt.escape(this.name, var2) + "\"/>");
   }

   public void end(PrintWriter var1, Encoder var2) {
      var1.print("<text:bookmark-end text:name=\"" + Odt.escape(this.name, var2) + "\"/>");
   }
}
