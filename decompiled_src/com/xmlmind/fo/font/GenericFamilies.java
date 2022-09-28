package com.xmlmind.fo.font;

import java.util.StringTokenizer;

public final class GenericFamilies implements Cloneable {
   public String serif;
   public String sansSerif;
   public String monospace;
   public String cursive;
   public String fantasy;

   public static GenericFamilies fromString(String var0) {
      try {
         GenericFamilies var1 = new GenericFamilies();
         parse(var0, var1);
         return var1;
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public static void parse(String var0, GenericFamilies var1) throws IllegalArgumentException {
      StringTokenizer var2 = new StringTokenizer(var0, ",");

      while(var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         int var4 = var3.indexOf(61);
         if (var4 <= 0 || var4 >= var3.length() - 1) {
            throw new IllegalArgumentException("'" + var3 + "', invalid font family mapping");
         }

         String var5 = var3.substring(0, var4);
         String var6 = var3.substring(var4 + 1);
         String var7 = FontUtil.normalizeFamily(var6);
         if (var7 == null) {
            throw new IllegalArgumentException("'" + var6 + "', invalid actual font family");
         }

         String var8 = FontUtil.normalizeFamily(var5);
         if ("serif".equals(var8)) {
            var1.serif = var7;
         } else if ("sans-serif".equals(var8)) {
            var1.sansSerif = var7;
         } else if ("monospace".equals(var8)) {
            var1.monospace = var7;
         } else if ("cursive".equals(var8)) {
            var1.cursive = var7;
         } else {
            if (!"fantasy".equals(var8)) {
               throw new IllegalArgumentException("'" + var5 + "', not a generic font family");
            }

            var1.fantasy = var7;
         }
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      int var2 = 0;
      if (this.serif != null) {
         var1.append("serif=");
         var1.append(this.serif);
         ++var2;
      }

      if (this.sansSerif != null) {
         if (var2 > 0) {
            var1.append(',');
         }

         var1.append("sans-serif=");
         var1.append(this.sansSerif);
         ++var2;
      }

      if (this.monospace != null) {
         if (var2 > 0) {
            var1.append(',');
         }

         var1.append("monospace=");
         var1.append(this.monospace);
         ++var2;
      }

      if (this.cursive != null) {
         if (var2 > 0) {
            var1.append(',');
         }

         var1.append("cursive=");
         var1.append(this.cursive);
         ++var2;
      }

      if (this.fantasy != null) {
         if (var2 > 0) {
            var1.append(',');
         }

         var1.append("fantasy=");
         var1.append(this.fantasy);
         ++var2;
      }

      return var1.toString();
   }
}
