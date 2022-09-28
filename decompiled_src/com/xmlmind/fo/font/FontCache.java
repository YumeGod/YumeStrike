package com.xmlmind.fo.font;

import java.util.Hashtable;

public final class FontCache {
   private static Hashtable cache = new Hashtable();

   public static Font getFont(int var0, int var1, int var2) {
      String var3 = key(var0, var1, var2);
      synchronized(cache) {
         Font var5 = (Font)cache.get(var3);
         if (var5 == null) {
            try {
               var5 = new Font(var0, var1, var2);
               cache.put(var3, var5);
            } catch (Exception var8) {
            }
         }

         return var5;
      }
   }

   private static String key(int var0, int var1, int var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(Integer.toString(var0));
      var3.append('/');
      var3.append(Integer.toString(var1));
      var3.append('/');
      var3.append(Integer.toString(var2));
      return var3.toString();
   }

   public static Font getFont(String var0, int var1, int var2) {
      int var3 = FontUtil.toGenericFamily(var0, true);
      if (var3 < 0) {
         var3 = 1;
      }

      return getFont(var3, var1, var2);
   }
}
