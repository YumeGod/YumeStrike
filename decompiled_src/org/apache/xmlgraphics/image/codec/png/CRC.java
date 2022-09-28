package org.apache.xmlgraphics.image.codec.png;

class CRC {
   private static int[] crcTable = new int[256];

   public static int updateCRC(int crc, byte[] data, int off, int len) {
      int c = crc;

      for(int n = 0; n < len; ++n) {
         c = crcTable[(c ^ data[off + n]) & 255] ^ c >>> 8;
      }

      return c;
   }

   static {
      for(int n = 0; n < 256; ++n) {
         int c = n;

         for(int k = 0; k < 8; ++k) {
            if ((c & 1) == 1) {
               c = -306674912 ^ c >>> 1;
            } else {
               c >>>= 1;
            }

            crcTable[n] = c;
         }
      }

   }
}
