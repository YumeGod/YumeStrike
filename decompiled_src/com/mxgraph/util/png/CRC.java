package com.mxgraph.util.png;

class CRC {
   private static int[] crcTable = new int[256];

   public static int updateCRC(int var0, byte[] var1, int var2, int var3) {
      int var4 = var0;

      for(int var5 = 0; var5 < var3; ++var5) {
         var4 = crcTable[(var4 ^ var1[var2 + var5]) & 255] ^ var4 >>> 8;
      }

      return var4;
   }

   static {
      for(int var0 = 0; var0 < 256; ++var0) {
         int var1 = var0;

         for(int var2 = 0; var2 < 8; ++var2) {
            if ((var1 & 1) == 1) {
               var1 = -306674912 ^ var1 >>> 1;
            } else {
               var1 >>>= 1;
            }

            crcTable[var0] = var1;
         }
      }

   }
}
