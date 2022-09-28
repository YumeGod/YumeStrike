package org.apache.batik.util;

import java.io.IOException;
import java.io.InputStream;

public class Base64DecodeStream extends InputStream {
   InputStream src;
   private static final byte[] pem_array = new byte[256];
   byte[] decode_buffer = new byte[4];
   byte[] out_buffer = new byte[3];
   int out_offset = 3;
   boolean EOF = false;

   public Base64DecodeStream(InputStream var1) {
      this.src = var1;
   }

   public boolean markSupported() {
      return false;
   }

   public void close() throws IOException {
      this.EOF = true;
   }

   public int available() throws IOException {
      return 3 - this.out_offset;
   }

   public int read() throws IOException {
      if (this.out_offset != 3 || !this.EOF && !this.getNextAtom()) {
         return this.out_buffer[this.out_offset++] & 255;
      } else {
         this.EOF = true;
         return -1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         if (this.out_offset == 3 && (this.EOF || this.getNextAtom())) {
            this.EOF = true;
            return var4 == 0 ? -1 : var4;
         }

         var1[var2 + var4] = this.out_buffer[this.out_offset++];
      }

      return var4;
   }

   final boolean getNextAtom() throws IOException {
      int var8;
      for(int var6 = 0; var6 != 4; var6 = var8) {
         int var1 = this.src.read(this.decode_buffer, var6, 4 - var6);
         if (var1 == -1) {
            return true;
         }

         int var7 = var6;

         for(var8 = var6; var7 < var6 + var1; ++var7) {
            if (this.decode_buffer[var7] != 10 && this.decode_buffer[var7] != 13 && this.decode_buffer[var7] != 32) {
               this.decode_buffer[var8++] = this.decode_buffer[var7];
            }
         }
      }

      byte var2 = pem_array[this.decode_buffer[0] & 255];
      byte var3 = pem_array[this.decode_buffer[1] & 255];
      byte var4 = pem_array[this.decode_buffer[2] & 255];
      byte var5 = pem_array[this.decode_buffer[3] & 255];
      this.out_buffer[0] = (byte)(var2 << 2 | var3 >>> 4);
      this.out_buffer[1] = (byte)(var3 << 4 | var4 >>> 2);
      this.out_buffer[2] = (byte)(var4 << 6 | var5);
      if (this.decode_buffer[3] != 61) {
         this.out_offset = 0;
      } else if (this.decode_buffer[2] == 61) {
         this.out_buffer[2] = this.out_buffer[0];
         this.out_offset = 2;
         this.EOF = true;
      } else {
         this.out_buffer[2] = this.out_buffer[1];
         this.out_buffer[1] = this.out_buffer[0];
         this.out_offset = 1;
         this.EOF = true;
      }

      return false;
   }

   static {
      int var0;
      for(var0 = 0; var0 < pem_array.length; ++var0) {
         pem_array[var0] = -1;
      }

      var0 = 0;

      char var1;
      for(var1 = 'A'; var1 <= 'Z'; ++var1) {
         pem_array[var1] = (byte)(var0++);
      }

      for(var1 = 'a'; var1 <= 'z'; ++var1) {
         pem_array[var1] = (byte)(var0++);
      }

      for(var1 = '0'; var1 <= '9'; ++var1) {
         pem_array[var1] = (byte)(var0++);
      }

      pem_array[43] = (byte)(var0++);
      pem_array[47] = (byte)(var0++);
   }
}
