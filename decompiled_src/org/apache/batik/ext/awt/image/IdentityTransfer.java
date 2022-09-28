package org.apache.batik.ext.awt.image;

public class IdentityTransfer implements TransferFunction {
   public static byte[] lutData = new byte[256];

   public byte[] getLookupTable() {
      return lutData;
   }

   static {
      for(int var0 = 0; var0 <= 255; ++var0) {
         lutData[var0] = (byte)var0;
      }

   }
}
