package com.xmlmind.fo.objects;

import com.xmlmind.fo.graphic.GraphicUtil;
import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Value;
import java.io.PrintStream;

public class ExternalGraphic {
   public String src;
   public Value contentType;
   public Value width;
   public Value height;
   public Value contentWidth;
   public Value contentHeight;
   public int scaling;
   public int scalingMethod;
   public int textAlign;
   public int displayAlign;
   public int overflow;
   public String role;

   public ExternalGraphic(String var1, String var2) {
      this.src = var1;
      this.contentType = new Value((byte)15, "content-type:" + var2);
      this.width = Value.KEYWORD_AUTO;
      this.height = Value.KEYWORD_AUTO;
      this.contentWidth = Value.KEYWORD_AUTO;
      this.contentHeight = Value.KEYWORD_AUTO;
      this.scaling = 214;
      this.scalingMethod = 10;
      this.textAlign = 100;
      this.displayAlign = 10;
      this.overflow = 10;
      this.role = null;
   }

   public ExternalGraphic(Value[] var1) {
      if (var1[276] != null) {
         this.src = var1[276].uriSpecification();
      }

      this.contentType = var1[85];
      this.width = var1[308];
      this.height = var1[118];
      this.contentWidth = var1[86];
      this.contentHeight = var1[84];
      this.scaling = var1[239].keyword();
      this.scalingMethod = var1[240].keyword();
      if (var1[289].type == 1) {
         this.textAlign = var1[289].keyword();
      } else {
         this.textAlign = 100;
      }

      this.displayAlign = var1[93].keyword();
      this.overflow = var1[191].keyword();
      if (var1[236] != null) {
         this.role = var1[236].string();
      }

   }

   public String mimeType() {
      String var1 = null;
      if (this.contentType.type == 15) {
         String var2 = this.contentType.string();
         if (var2.startsWith("content-type:")) {
            var1 = var2.substring(var2.indexOf(":") + 1);
         } else if (!var2.startsWith("namespace-prefix:")) {
            var1 = var2;
         }
      }

      if (var1 != null) {
         if ("image/jpg".equals(var1)) {
            var1 = "image/jpeg";
         }
      } else if (this.src != null) {
         if (!this.src.startsWith("data:")) {
            return GraphicUtil.extensionToFormat(this.src);
         }

         int var3 = this.src.indexOf(59, 5);
         if (var3 < 0) {
            var3 = this.src.indexOf(44, 5);
         }

         if (var3 >= 5) {
            var1 = this.src.substring(5, var3).trim();
         }

         if (var1 == null || var1.length() == 0) {
            var1 = "text/plain";
         }
      }

      return var1;
   }

   public void dump(PrintStream var1) {
      var1.println("src = " + this.src);
      var1.println("contentWidth = " + this.contentWidth.toString());
      var1.println("contentHeight = " + this.contentHeight.toString());
      var1.println("scaling = " + Keyword.keyword(this.scaling));
      var1.println("scalingMethod = " + Keyword.keyword(this.scalingMethod));
      var1.flush();
   }
}
