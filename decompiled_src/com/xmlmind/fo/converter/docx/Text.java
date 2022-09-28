package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.font.Font;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public final class Text {
   public String content;
   public RunProperties properties;
   public boolean preserveSpace;
   public int footnoteId;

   public Text() {
      this("");
   }

   public Text(String var1) {
      this(var1, (RunProperties)null);
   }

   public Text(String var1, RunProperties var2) {
      this(var1, var2, false);
   }

   public Text(String var1, RunProperties var2, boolean var3) {
      this.content = var1;
      this.properties = var2;
      this.preserveSpace = var3;
   }

   public boolean isEmpty() {
      return this.content.length() == 0 && this.footnoteId == 0;
   }

   public boolean isSpace() {
      int var1 = 0;

      for(int var2 = this.content.length(); var1 < var2; ++var1) {
         if (this.content.charAt(var1) != ' ') {
            return false;
         }
      }

      return true;
   }

   public void trimLeft() {
      int var1 = this.content.length();
      if (var1 != 0 && this.content.charAt(0) == ' ') {
         for(int var2 = 1; var2 < var1; ++var2) {
            if (this.content.charAt(var2) != ' ') {
               this.content = this.content.substring(var2);
               return;
            }
         }

         this.content = "";
      }
   }

   public void trimRight() {
      int var1 = this.content.length();
      if (var1 != 0 && this.content.charAt(var1 - 1) == ' ') {
         for(int var2 = var1 - 2; var2 >= 0; --var2) {
            if (this.content.charAt(var2) != ' ') {
               this.content = this.content.substring(0, var2 + 1);
               return;
            }
         }

         this.content = "";
      }
   }

   public void print(PrintWriter var1) {
      StringTokenizer var2 = new StringTokenizer(this.content, "\u00ad‑", true);
      var1.println("<w:r>");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      if (this.footnoteId != 0) {
         var1.println("<w:footnoteReference w:customMarkFollows=\"true\" w:id=\"" + this.footnoteId + "\" />");
      }

      while(true) {
         while(var2.hasMoreTokens()) {
            String var3 = var2.nextToken();
            switch (var3.charAt(0)) {
               case '\u00ad':
                  var1.println("<w:softHyphen />");
                  break;
               case '‑':
                  var1.println("<w:noBreakHyphen />");
                  break;
               default:
                  if (!this.preserveSpace && !var3.startsWith(" ") && !var3.endsWith(" ")) {
                     var1.println("<w:t>" + Wml.escape(var3) + "</w:t>");
                  } else {
                     var1.println("<w:t xml:space=\"preserve\">" + Wml.escape(var3) + "</w:t>");
                  }
            }
         }

         var1.println("</w:r>");
         return;
      }
   }

   public double textWidth() {
      Font var1 = null;
      if (this.properties != null) {
         var1 = this.properties.getFont();
      }

      double var2;
      if (var1 != null) {
         var2 = (double)var1.getTextExtents(this.content).width / 20.0;
      } else {
         var2 = (double)this.content.length() * this.charWidth();
      }

      return var2;
   }

   public double wordWidth() {
      Font var1 = null;
      if (this.properties != null) {
         var1 = this.properties.getFont();
      }

      int var4 = 0;
      StringTokenizer var5 = new StringTokenizer(this.content);
      double var2;
      int var6;
      if (var1 != null) {
         while(var5.hasMoreTokens()) {
            var6 = var1.getTextExtents(var5.nextToken()).width;
            if (var6 > var4) {
               var4 = var6;
            }
         }

         var2 = (double)var4 / 20.0;
      } else {
         while(var5.hasMoreTokens()) {
            var6 = var5.nextToken().length();
            if (var6 > var4) {
               var4 = var6;
            }
         }

         var2 = (double)var4 * this.charWidth();
      }

      return var2;
   }

   private double charWidth() {
      double var1;
      if (this.properties != null) {
         var1 = this.properties.fontSize;
      } else {
         var1 = 12.0;
      }

      return 3.0 * var1 / 4.0;
   }
}
