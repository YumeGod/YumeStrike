package com.xmlmind.fo.util;

public final class StringUtil {
   public static final String[] EMPTY_LIST = new String[0];

   private StringUtil() {
   }

   public static boolean contains(String[] var0, String var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         if (var0[var2].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public static String[] split(String var0, char var1) {
      if (var0.length() == 0) {
         return EMPTY_LIST;
      } else {
         int var2 = 0;

         int var3;
         for(var3 = 0; (var3 = var0.indexOf(var1, var3)) >= 0; ++var3) {
            ++var2;
         }

         ++var2;
         String[] var4 = new String[var2];
         var2 = 0;

         int var5;
         for(var3 = 0; (var5 = var0.indexOf(var1, var3)) >= 0; var3 = var5 + 1) {
            var4[var2++] = var3 == var5 ? "" : var0.substring(var3, var5);
         }

         var4[var2++] = var0.substring(var3);
         return var4;
      }
   }

   public static String replaceAll(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = var1.length();

      int var5;
      int var6;
      for(var5 = 0; (var6 = var0.indexOf(var1, var5)) >= 0; var5 = var6 + var4) {
         if (var6 > var5) {
            var3.append(var0.substring(var5, var6));
         }

         var3.append(var2);
      }

      if (var5 < var0.length()) {
         var3.append(var0.substring(var5));
      }

      return var3.toString();
   }
}
