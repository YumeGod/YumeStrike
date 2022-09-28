package org.apache.batik.transcoder.wmf.tosvg;

import java.awt.Font;

public class WMFFont {
   public Font font;
   public int charset;
   public int underline = 0;
   public int strikeOut = 0;
   public int italic = 0;
   public int weight = 0;
   public int orientation = 0;
   public int escape = 0;

   public WMFFont(Font var1, int var2) {
      this.font = var1;
      this.charset = var2;
   }

   public WMFFont(Font var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.font = var1;
      this.charset = var2;
      this.underline = var3;
      this.strikeOut = var4;
      this.italic = var5;
      this.weight = var6;
      this.orientation = var7;
      this.escape = var8;
   }
}
