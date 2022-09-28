package com.xmlmind.fo.properties;

import java.util.Hashtable;
import java.util.StringTokenizer;

public class FontFamily extends Property {
   public static final int SERIF = 0;
   public static final int SANS_SERIF = 1;
   public static final int CURSIVE = 2;
   public static final int FANTASY = 3;
   public static final int MONOSPACE = 4;
   public static final String[] familyNames = new String[]{"serif", "sans-serif", "cursive", "fantasy", "monospace"};
   private static final Hashtable familyIndexes = new Hashtable();

   public FontFamily(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected Value list(String var1) {
      StringTokenizer var4 = new StringTokenizer(var1, ",");
      int var2;
      if ((var2 = var4.countTokens()) == 0) {
         return null;
      } else {
         Value[] var3 = new Value[var2];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var1 = var4.nextToken().trim();
            if (var1.length() == 0) {
               return null;
            }

            byte var6;
            int var7;
            if (var1.charAt(0) == '"') {
               var6 = 1;
               var7 = var1.lastIndexOf(34);
               if (var7 <= var6) {
                  return null;
               }

               var1 = var1.substring(var6, var7);
            } else if (var1.charAt(0) == '\'') {
               var6 = 1;
               var7 = var1.lastIndexOf(39);
               if (var7 <= var6) {
                  return null;
               }

               var1 = var1.substring(var6, var7);
            }

            var3[var5] = new Value((byte)15, var1);
         }

         return new Value((byte)27, var3);
      }
   }

   public static int genericFamily(String var0) {
      Object var1 = familyIndexes.get(var0);
      return var1 != null ? (Integer)var1 : -1;
   }

   static {
      for(int var0 = 0; var0 < familyNames.length; ++var0) {
         familyIndexes.put(familyNames[var0], new Integer(var0));
      }

   }
}
