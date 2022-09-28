package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public final class BookmarkReference {
   public static final int FORMAT_PAGE = 0;
   public static final int FORMAT_TEXT = 1;
   public String name;
   public int format;
   public TextStyle style;
   public String value;

   public BookmarkReference(String var1, int var2) {
      this(var1, var2, (TextStyle)null);
   }

   public BookmarkReference(String var1, int var2, TextStyle var3) {
      this.name = var1;
      this.format = var2;
      this.style = var3;
   }

   public static BookmarkReference page(String var0) {
      return page(var0, (TextStyle)null);
   }

   public static BookmarkReference page(String var0, TextStyle var1) {
      BookmarkReference var2 = new BookmarkReference(var0, 0, var1);
      var2.value = "0";
      return var2;
   }

   public void print(PrintWriter var1, Encoder var2) {
      if (this.name != null && this.name.length() != 0) {
         if (this.style != null) {
            Span.start(var1, this.style);
         }

         var1.print("<text:bookmark-ref");
         var1.print(" text:ref-name=\"" + Odt.escape(this.name, var2) + "\"");
         var1.println(" text:reference-format=\"" + this.format() + "\"");
         var1.print(">");
         if (this.value != null) {
            var1.print(Odt.escape(this.value, var2));
         }

         var1.println("</text:bookmark-ref");
         var1.print(">");
         if (this.style != null) {
            Span.end(var1);
         }

      }
   }

   private String format() {
      return this.format == 1 ? "text" : "page";
   }
}
