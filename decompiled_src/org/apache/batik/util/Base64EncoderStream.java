package org.apache.batik.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Base64EncoderStream extends OutputStream {
   private static final byte[] pem_array = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   byte[] atom = new byte[3];
   int atomLen = 0;
   byte[] encodeBuf = new byte[4];
   int lineLen = 0;
   PrintStream out;
   boolean closeOutOnClose;

   public Base64EncoderStream(OutputStream var1) {
      this.out = new PrintStream(var1);
      this.closeOutOnClose = true;
   }

   public Base64EncoderStream(OutputStream var1, boolean var2) {
      this.out = new PrintStream(var1);
      this.closeOutOnClose = var2;
   }

   public void close() throws IOException {
      if (this.out != null) {
         this.encodeAtom();
         this.out.flush();
         if (this.closeOutOnClose) {
            this.out.close();
         }

         this.out = null;
      }

   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void write(int var1) throws IOException {
      this.atom[this.atomLen++] = (byte)var1;
      if (this.atomLen == 3) {
         this.encodeAtom();
      }

   }

   public void write(byte[] var1) throws IOException {
      this.encodeFromArray(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.encodeFromArray(var1, var2, var3);
   }

   void encodeAtom() throws IOException {
      byte var1;
      byte var2;
      switch (this.atomLen) {
         case 0:
            return;
         case 1:
            var1 = this.atom[0];
            this.encodeBuf[0] = pem_array[var1 >>> 2 & 63];
            this.encodeBuf[1] = pem_array[var1 << 4 & 48];
            this.encodeBuf[2] = this.encodeBuf[3] = 61;
            break;
         case 2:
            var1 = this.atom[0];
            var2 = this.atom[1];
            this.encodeBuf[0] = pem_array[var1 >>> 2 & 63];
            this.encodeBuf[1] = pem_array[var1 << 4 & 48 | var2 >>> 4 & 15];
            this.encodeBuf[2] = pem_array[var2 << 2 & 60];
            this.encodeBuf[3] = 61;
            break;
         default:
            var1 = this.atom[0];
            var2 = this.atom[1];
            byte var3 = this.atom[2];
            this.encodeBuf[0] = pem_array[var1 >>> 2 & 63];
            this.encodeBuf[1] = pem_array[var1 << 4 & 48 | var2 >>> 4 & 15];
            this.encodeBuf[2] = pem_array[var2 << 2 & 60 | var3 >>> 6 & 3];
            this.encodeBuf[3] = pem_array[var3 & 63];
      }

      if (this.lineLen == 64) {
         this.out.println();
         this.lineLen = 0;
      }

      this.out.write(this.encodeBuf);
      this.lineLen += 4;
      this.atomLen = 0;
   }

   void encodeFromArray(byte[] var1, int var2, int var3) throws IOException {
      if (var3 != 0) {
         if (this.atomLen != 0) {
            switch (this.atomLen) {
               case 1:
                  this.atom[1] = var1[var2++];
                  --var3;
                  ++this.atomLen;
                  if (var3 == 0) {
                     return;
                  }

                  this.atom[2] = var1[var2++];
                  --var3;
                  ++this.atomLen;
                  break;
               case 2:
                  this.atom[2] = var1[var2++];
                  --var3;
                  ++this.atomLen;
            }

            this.encodeAtom();
         }

         for(; var3 >= 3; var3 -= 3) {
            byte var4 = var1[var2++];
            byte var5 = var1[var2++];
            byte var6 = var1[var2++];
            this.encodeBuf[0] = pem_array[var4 >>> 2 & 63];
            this.encodeBuf[1] = pem_array[var4 << 4 & 48 | var5 >>> 4 & 15];
            this.encodeBuf[2] = pem_array[var5 << 2 & 60 | var6 >>> 6 & 3];
            this.encodeBuf[3] = pem_array[var6 & 63];
            this.out.write(this.encodeBuf);
            this.lineLen += 4;
            if (this.lineLen == 64) {
               this.out.println();
               this.lineLen = 0;
            }
         }

         switch (var3) {
            case 1:
               this.atom[0] = var1[var2];
               break;
            case 2:
               this.atom[0] = var1[var2];
               this.atom[1] = var1[var2 + 1];
         }

         this.atomLen = var3;
      }
   }
}
