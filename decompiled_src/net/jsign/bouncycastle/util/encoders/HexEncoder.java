package net.jsign.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;

public class HexEncoder implements Encoder {
   protected final byte[] encodingTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
   protected final byte[] decodingTable = new byte[128];

   protected void initialiseDecodingTable() {
      int var1;
      for(var1 = 0; var1 < this.decodingTable.length; ++var1) {
         this.decodingTable[var1] = -1;
      }

      for(var1 = 0; var1 < this.encodingTable.length; ++var1) {
         this.decodingTable[this.encodingTable[var1]] = (byte)var1;
      }

      this.decodingTable[65] = this.decodingTable[97];
      this.decodingTable[66] = this.decodingTable[98];
      this.decodingTable[67] = this.decodingTable[99];
      this.decodingTable[68] = this.decodingTable[100];
      this.decodingTable[69] = this.decodingTable[101];
      this.decodingTable[70] = this.decodingTable[102];
   }

   public HexEncoder() {
      this.initialiseDecodingTable();
   }

   public int encode(byte[] var1, int var2, int var3, OutputStream var4) throws IOException {
      for(int var5 = var2; var5 < var2 + var3; ++var5) {
         int var6 = var1[var5] & 255;
         var4.write(this.encodingTable[var6 >>> 4]);
         var4.write(this.encodingTable[var6 & 15]);
      }

      return var3 * 2;
   }

   private static boolean ignore(char var0) {
      return var0 == '\n' || var0 == '\r' || var0 == '\t' || var0 == ' ';
   }

   public int decode(byte[] var1, int var2, int var3, OutputStream var4) throws IOException {
      int var5 = 0;

      int var6;
      for(var6 = var2 + var3; var6 > var2 && ignore((char)var1[var6 - 1]); --var6) {
      }

      for(int var7 = var2; var7 < var6; ++var5) {
         while(var7 < var6 && ignore((char)var1[var7])) {
            ++var7;
         }

         byte var8;
         for(var8 = this.decodingTable[var1[var7++]]; var7 < var6 && ignore((char)var1[var7]); ++var7) {
         }

         byte var9 = this.decodingTable[var1[var7++]];
         if ((var8 | var9) < 0) {
            throw new IOException("invalid characters encountered in Hex data");
         }

         var4.write(var8 << 4 | var9);
      }

      return var5;
   }

   public int decode(String var1, OutputStream var2) throws IOException {
      int var3 = 0;

      int var4;
      for(var4 = var1.length(); var4 > 0 && ignore(var1.charAt(var4 - 1)); --var4) {
      }

      for(int var5 = 0; var5 < var4; ++var3) {
         while(var5 < var4 && ignore(var1.charAt(var5))) {
            ++var5;
         }

         byte var6;
         for(var6 = this.decodingTable[var1.charAt(var5++)]; var5 < var4 && ignore(var1.charAt(var5)); ++var5) {
         }

         byte var7 = this.decodingTable[var1.charAt(var5++)];
         if ((var6 | var7) < 0) {
            throw new IOException("invalid characters encountered in Hex string");
         }

         var2.write(var6 << 4 | var7);
      }

      return var3;
   }
}
