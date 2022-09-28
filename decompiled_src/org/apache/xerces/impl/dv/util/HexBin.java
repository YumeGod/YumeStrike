package org.apache.xerces.impl.dv.util;

public final class HexBin {
   private static final int BASELENGTH = 128;
   private static final int LOOKUPLENGTH = 16;
   private static final byte[] hexNumberTable = new byte[128];
   private static final char[] lookUpHexAlphabet = new char[16];

   public static String encode(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length;
         int var2 = var1 * 2;
         char[] var3 = new char[var2];

         for(int var5 = 0; var5 < var1; ++var5) {
            int var4 = var0[var5];
            if (var4 < 0) {
               var4 += 256;
            }

            var3[var5 * 2] = lookUpHexAlphabet[var4 >> 4];
            var3[var5 * 2 + 1] = lookUpHexAlphabet[var4 & 15];
         }

         return new String(var3);
      }
   }

   public static byte[] decode(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length();
         if (var1 % 2 != 0) {
            return null;
         } else {
            char[] var2 = var0.toCharArray();
            int var3 = var1 / 2;
            byte[] var4 = new byte[var3];

            for(int var8 = 0; var8 < var3; ++var8) {
               char var7 = var2[var8 * 2];
               byte var5 = var7 < 128 ? hexNumberTable[var7] : -1;
               if (var5 == -1) {
                  return null;
               }

               var7 = var2[var8 * 2 + 1];
               byte var6 = var7 < 128 ? hexNumberTable[var7] : -1;
               if (var6 == -1) {
                  return null;
               }

               var4[var8] = (byte)(var5 << 4 | var6);
            }

            return var4;
         }
      }
   }

   static {
      for(int var0 = 0; var0 < 128; ++var0) {
         hexNumberTable[var0] = -1;
      }

      for(int var1 = 57; var1 >= 48; --var1) {
         hexNumberTable[var1] = (byte)(var1 - 48);
      }

      for(int var2 = 70; var2 >= 65; --var2) {
         hexNumberTable[var2] = (byte)(var2 - 65 + 10);
      }

      for(int var3 = 102; var3 >= 97; --var3) {
         hexNumberTable[var3] = (byte)(var3 - 97 + 10);
      }

      for(int var4 = 0; var4 < 10; ++var4) {
         lookUpHexAlphabet[var4] = (char)(48 + var4);
      }

      for(int var5 = 10; var5 <= 15; ++var5) {
         lookUpHexAlphabet[var5] = (char)(65 + var5 - 10);
      }

   }
}
