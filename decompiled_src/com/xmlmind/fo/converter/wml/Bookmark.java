package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.MsTranslator;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public final class Bookmark {
   public int id;
   public String name;

   public Bookmark(int var1, String var2, Context var3) {
      this.id = var1;
      this.name = ((MsTranslator)var3.translator).checkBookmark(var2);
   }

   public String start(Encoder var1) {
      StringBuffer var2 = new StringBuffer("<aml:annotation");
      var2.append(" aml:id=\"" + this.id + "\"");
      var2.append(" w:type=\"Word.Bookmark.Start\"");
      if (this.name != null) {
         var2.append(" w:name=\"" + Wml.escape(this.name, var1) + "\"");
      }

      var2.append(" />");
      return var2.toString();
   }

   public void start(PrintWriter var1, Encoder var2) {
      var1.println(this.start(var2));
   }

   public String end() {
      StringBuffer var1 = new StringBuffer("<aml:annotation");
      var1.append(" aml:id=\"" + this.id + "\"");
      var1.append(" w:type=\"Word.Bookmark.End\"");
      var1.append(" />");
      return var1.toString();
   }

   public void end(PrintWriter var1) {
      var1.println(this.end());
   }
}
