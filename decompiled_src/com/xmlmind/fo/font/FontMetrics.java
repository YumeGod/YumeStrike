package com.xmlmind.fo.font;

public abstract class FontMetrics {
   public final boolean fixedPitch;
   public final short ascent;
   public final short descent;
   public final CharMetrics bounds;
   public final CharMetrics[] charMetrics;

   protected FontMetrics(boolean var1, int var2, int var3, CharMetrics var4, CharMetrics[] var5) {
      this.fixedPitch = var1;
      this.ascent = (short)var2;
      this.descent = (short)var3;
      this.bounds = var4;
      this.charMetrics = var5;
   }

   public static FontMetrics find(int var0, int var1) throws Exception {
      String var2 = FontMetrics.class.getName();
      String var3 = var2.substring(0, var2.lastIndexOf(46));
      StringBuffer var4 = new StringBuffer(var3);
      var4.append('.');
      switch (var0) {
         case 1:
         default:
            var4.append("Serif");
            break;
         case 2:
            var4.append("SansSerif");
            break;
         case 3:
            var4.append("Monospace");
      }

      if ((var1 & 1) != 0) {
         var4.append("Bold");
      }

      if ((var1 & 2) != 0) {
         var4.append("Italic");
      }

      var2 = var4.toString();
      return (FontMetrics)Class.forName(var2).newInstance();
   }
}
