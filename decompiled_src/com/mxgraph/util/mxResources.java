package com.mxgraph.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class mxResources {
   protected static LinkedList bundles = new LinkedList();

   public static LinkedList getBundles() {
      return bundles;
   }

   public static void setBundles(LinkedList var0) {
      bundles = var0;
   }

   public static void add(String var0) {
      bundles.addFirst(PropertyResourceBundle.getBundle(var0));
   }

   public static void add(String var0, Locale var1) {
      bundles.addFirst(PropertyResourceBundle.getBundle(var0, var1));
   }

   public static String get(String var0) {
      return get(var0, (String[])null, (String)null);
   }

   public static String get(String var0, String var1) {
      return get(var0, (String[])null, var1);
   }

   public static String get(String var0, String[] var1) {
      return get(var0, var1, (String)null);
   }

   public static String get(String var0, String[] var1, String var2) {
      String var3 = getResource(var0);
      if (var3 == null) {
         var3 = var2;
      }

      if (var3 != null && var1 != null) {
         StringBuffer var4 = new StringBuffer();
         String var5 = null;

         for(int var6 = 0; var6 < var3.length(); ++var6) {
            char var7 = var3.charAt(var6);
            if (var7 == '{') {
               var5 = "";
            } else if (var5 != null && var7 == '}') {
               int var8 = Integer.parseInt(var5) - 1;
               if (var8 >= 0 && var8 < var1.length) {
                  var4.append(var1[var8]);
               }

               var5 = null;
            } else if (var5 != null) {
               var5 = var5 + var7;
            } else {
               var4.append(var7);
            }
         }

         var3 = var4.toString();
      }

      return var3;
   }

   protected static String getResource(String var0) {
      Iterator var1 = bundles.iterator();

      while(var1.hasNext()) {
         try {
            return ((ResourceBundle)var1.next()).getString(var0);
         } catch (MissingResourceException var3) {
         }
      }

      return null;
   }
}
