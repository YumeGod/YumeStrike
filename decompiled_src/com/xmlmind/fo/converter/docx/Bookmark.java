package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.MsTranslator;
import java.io.PrintWriter;

public final class Bookmark {
   public int id;
   public String name;

   public Bookmark(int var1, String var2, Context var3) {
      this.id = var1;
      this.name = ((MsTranslator)var3.translator).checkBookmark(var2);
   }

   public void start(PrintWriter var1) {
      var1.println("<w:bookmarkStart w:id=\"" + this.id + "\"" + " w:name=\"" + Wml.escape(this.name) + "\" />");
   }

   public void end(PrintWriter var1) {
      var1.println("<w:bookmarkEnd w:id=\"" + this.id + "\" />");
   }
}
