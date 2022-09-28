package com.mxgraph.model;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class mxCellPath {
   public static String PATH_SEPARATOR = ".";

   public static String create(mxICell var0) {
      String var1 = "";
      if (var0 != null) {
         for(mxICell var2 = var0.getParent(); var2 != null; var2 = var2.getParent()) {
            int var3 = var2.getIndex(var0);
            var1 = var3 + PATH_SEPARATOR + var1;
            var0 = var2;
         }
      }

      return var1.length() > 1 ? var1.substring(0, var1.length() - 1) : "";
   }

   public static String getParentPath(String var0) {
      if (var0 != null) {
         int var1 = var0.lastIndexOf(PATH_SEPARATOR);
         if (var1 >= 0) {
            return var0.substring(0, var1);
         }

         if (var0.length() > 0) {
            return "";
         }
      }

      return null;
   }

   public static mxICell resolve(mxICell var0, String var1) {
      mxICell var2 = var0;
      String[] var3 = var1.split(Pattern.quote(PATH_SEPARATOR));

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2 = var2.getChildAt(Integer.parseInt(var3[var4]));
      }

      return var2;
   }

   public static int compare(String var0, String var1) {
      StringTokenizer var2 = new StringTokenizer(var0, PATH_SEPARATOR);
      StringTokenizer var3 = new StringTokenizer(var1, PATH_SEPARATOR);
      int var4 = 0;

      while(var2.hasMoreTokens() && var3.hasMoreTokens()) {
         String var5 = var2.nextToken();
         String var6 = var3.nextToken();
         if (!var5.equals(var6)) {
            if (var5.length() != 0 && var6.length() != 0) {
               var4 = Integer.valueOf(var5).compareTo(Integer.valueOf(var6));
               break;
            }

            var4 = var5.compareTo(var6);
            break;
         }
      }

      if (var4 == 0) {
         int var7 = var2.countTokens();
         int var8 = var3.countTokens();
         if (var7 != var8) {
            var4 = var7 > var8 ? 1 : -1;
         }
      }

      return var4;
   }
}
