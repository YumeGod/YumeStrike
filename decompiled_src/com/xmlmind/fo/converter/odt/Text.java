package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.font.Font;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public final class Text {
   public String content;
   public TextStyle style;
   public boolean preserveSpace;

   public Text() {
      this("");
   }

   public Text(String var1) {
      this(var1, (TextStyle)null);
   }

   public Text(String var1, TextStyle var2) {
      this(var1, var2, false);
   }

   public Text(String var1, TextStyle var2, boolean var3) {
      this.content = var1;
      this.style = var2;
      this.preserveSpace = var3;
   }

   public void print(PrintWriter var1, Encoder var2) {
      Span.start(var1, this.style);
      String var4;
      if (this.preserveSpace) {
         for(StringTokenizer var3 = new StringTokenizer(this.content, " ", true); var3.hasMoreTokens(); var1.print(Odt.escape(var4, var2))) {
            var4 = var3.nextToken();
            if (var4.equals(" ")) {
               int var5;
               for(var5 = 1; var3.hasMoreTokens(); ++var5) {
                  var4 = var3.nextToken();
                  if (!var4.equals(" ")) {
                     break;
                  }
               }

               var1.print(" ");
               if (var5 > 1) {
                  var1.print("<text:s text:c=\"" + (var5 - 1) + "\"/>");
               }

               if (var4.equals(" ")) {
                  break;
               }
            }
         }
      } else {
         var1.print(Odt.escape(this.content, var2));
      }

      Span.end(var1);
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

   public double textWidth() {
      Font var1 = null;
      if (this.style != null) {
         var1 = this.style.getFont();
      }

      double var2;
      if (var1 != null) {
         var2 = (double)var1.getTextExtents(this.content).width / 20.0;
         var2 = this.adjustWidth(var2);
      } else {
         var2 = (double)this.content.length() * this.charWidth();
      }

      return var2;
   }

   public double wordWidth() {
      Font var1 = null;
      if (this.style != null) {
         var1 = this.style.getFont();
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
         var2 = this.adjustWidth(var2);
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

   private double adjustWidth(double var1) {
      String var3 = this.style.font.name.toLowerCase();
      if (var3.indexOf("dejavu") >= 0 || var3.indexOf("vera") >= 0) {
         var1 *= 1.25;
      }

      return var1;
   }

   private double charWidth() {
      double var1;
      if (this.style != null) {
         var1 = this.style.fontSize;
      } else {
         var1 = 12.0;
      }

      return 3.0 * var1 / 4.0;
   }
}
