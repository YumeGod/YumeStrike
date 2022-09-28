package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.font.Font;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public final class Text {
   public String text;
   public RunProperties properties;
   public Footnote footnote;

   public Text() {
      this("");
   }

   public Text(String var1) {
      this(var1, (RunProperties)null);
   }

   public Text(String var1, RunProperties var2) {
      this.text = var1;
      this.properties = var2;
   }

   public boolean isVoid() {
      return this.text.length() == 0 && this.footnote == null;
   }

   public boolean isSpace() {
      int var1 = 0;

      for(int var2 = this.text.length(); var1 < var2; ++var1) {
         if (this.text.charAt(var1) != ' ') {
            return false;
         }
      }

      return true;
   }

   public boolean preserveSpace() {
      return this.properties != null && this.properties.preserveSpace;
   }

   public void trimLeft() {
      int var1 = this.text.length();
      if (var1 != 0 && this.text.charAt(0) == ' ' && !this.preserveSpace()) {
         for(int var2 = 1; var2 < var1; ++var2) {
            if (this.text.charAt(var2) != ' ') {
               this.text = this.text.substring(var2);
               return;
            }
         }

         this.text = "";
      }
   }

   public void trimRight() {
      int var1 = this.text.length();
      if (var1 != 0 && this.text.charAt(var1 - 1) == ' ' && !this.preserveSpace()) {
         for(int var2 = var1 - 2; var2 >= 0; --var2) {
            if (this.text.charAt(var2) != ' ') {
               this.text = this.text.substring(0, var2 + 1);
               return;
            }
         }

         this.text = "";
      }
   }

   public void print(PrintWriter var1) throws Exception {
      this.print(var1, (Encoder)null);
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      var1.print("{");
      if (this.properties != null) {
         this.properties.print(var1);
      }

      var1.println();
      var1.println(Rtf.escape(this.text, var2));
      if (this.footnote != null) {
         this.footnote.print(var1, var2);
      }

      var1.println("}");
   }

   public int textWidth() {
      Font var1 = null;
      if (this.properties != null) {
         var1 = this.properties.font();
      }

      int var2;
      if (var1 != null) {
         var2 = var1.getTextExtents(this.text).width;
      } else {
         var2 = this.text.length() * this.charWidth();
      }

      return var2;
   }

   public int wordWidth() {
      Font var1 = null;
      if (this.properties != null) {
         var1 = this.properties.font();
      }

      int var2 = 0;
      StringTokenizer var3 = new StringTokenizer(this.text);
      int var4;
      if (var1 != null) {
         while(var3.hasMoreTokens()) {
            var4 = var1.getTextExtents(var3.nextToken()).width;
            if (var4 > var2) {
               var2 = var4;
            }
         }
      } else {
         while(var3.hasMoreTokens()) {
            var4 = var3.nextToken().length();
            if (var4 > var2) {
               var2 = var4;
            }
         }

         var2 *= this.charWidth();
      }

      return var2;
   }

   private int charWidth() {
      int var1 = 24;
      if (this.properties != null) {
         var1 = this.properties.fontSize;
      }

      int var2 = 3 * var1 / 4;
      return 10 * var2;
   }
}
