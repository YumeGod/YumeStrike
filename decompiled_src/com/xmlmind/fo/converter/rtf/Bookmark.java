package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.MsTranslator;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public final class Bookmark {
   private String tag;

   public Bookmark(String var1, Context var2) {
      this.tag = ((MsTranslator)var2.translator).checkBookmark(var1);
   }

   public String start(Encoder var1) {
      return "{\\*\\bkmkstart " + Rtf.escape(this.tag, var1) + "}";
   }

   public void start(PrintWriter var1, Encoder var2) {
      var1.println(this.start(var2));
   }

   public String end(Encoder var1) {
      return "{\\*\\bkmkend " + Rtf.escape(this.tag, var1) + "}";
   }

   public void end(PrintWriter var1, Encoder var2) {
      var1.println(this.end(var2));
   }
}
