package com.xmlmind.fo.util;

import java.io.IOException;
import java.io.Writer;

public final class Base64 {
   private static byte[] fromDigit = new byte[256];
   private static final int DIGIT1 = 0;
   private static final int DIGIT2 = 1;
   private static final int DIGIT3_OR_EQUAL = 2;
   private static final int EQUAL = 3;
   private static final int DIGIT4_OR_EQUAL = 4;
   private static final int END = 5;
   private static final String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
   private static final char[] toDigit;

   private Base64() {
   }

   public static byte[] decode(String var0) {
      int var1 = var0.length();
      byte[] var2 = new byte[3 * (var1 / 4) + 2];
      int var3 = 0;
      byte var4 = 0;
      int var5 = 0;
      int var6 = 0;

      for(int var7 = 0; var7 < var1; ++var7) {
         char var8 = var0.charAt(var7);
         if (!Character.isWhitespace(var8)) {
            boolean var9 = false;
            byte var11;
            switch (var4) {
               case 0:
                  if (var8 > 255 || (var11 = fromDigit[var8]) < 0) {
                     return null;
                  }

                  var4 = 1;
                  break;
               case 1:
                  if (var8 > 255 || (var11 = fromDigit[var8]) < 0) {
                     return null;
                  }

                  var4 = 2;
                  break;
               case 2:
                  if (var8 == '=') {
                     var4 = 3;
                     continue;
                  }

                  if (var8 > 255 || (var11 = fromDigit[var8]) < 0) {
                     return null;
                  }

                  var4 = 4;
                  break;
               case 3:
                  if (var8 != '=') {
                     return null;
                  }

                  var4 = 5;
                  continue;
               case 4:
                  if (var8 == '=') {
                     var4 = 5;
                     continue;
                  }

                  if (var8 > 255 || (var11 = fromDigit[var8]) < 0) {
                     return null;
                  }

                  var4 = 0;
                  break;
               case 5:
                  return null;
               default:
                  throw new RuntimeException("unknown state " + var4);
            }

            var5 <<= 6;
            var5 |= var11;
            var6 += 6;
            if (var6 >= 8) {
               var6 -= 8;
               var2[var3++] = (byte)(var5 >> var6 & 255);
            }
         }
      }

      switch (var4) {
         case 0:
         case 5:
            if (var3 != var2.length) {
               byte[] var10 = new byte[var3];
               System.arraycopy(var2, 0, var10, 0, var3);
               var2 = var10;
            }

            return var2;
         default:
            return null;
      }
   }

   static {
      int var0;
      for(var0 = 0; var0 < 256; ++var0) {
         fromDigit[var0] = -1;
      }

      for(var0 = 65; var0 <= 90; ++var0) {
         fromDigit[var0] = (byte)(var0 - 65);
      }

      for(var0 = 97; var0 <= 122; ++var0) {
         fromDigit[var0] = (byte)(26 + var0 - 97);
      }

      for(var0 = 48; var0 <= 57; ++var0) {
         fromDigit[var0] = (byte)(52 + var0 - 48);
      }

      fromDigit[43] = 62;
      fromDigit[47] = 63;
      toDigit = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
   }

   public static final class OutputStream extends java.io.OutputStream {
      private Writer writer;
      private byte[] bytes;
      private int byteCount;
      private char[] encoded;
      private int encodedSize;
      private int lineLength;

      public OutputStream(Writer var1) {
         this(var1, 4096);
      }

      public OutputStream(Writer var1, int var2) {
         this.writer = var1;
         this.bytes = new byte[3];
         this.byteCount = 0;
         this.encoded = new char[var2];
         this.encodedSize = this.lineLength = 0;
      }

      public void write(int var1) throws IOException {
         if (this.byteCount == 3) {
            this.encode();
         }

         this.bytes[this.byteCount++] = (byte)var1;
      }

      public void write(byte[] var1) throws IOException {
         this.write(var1, 0, var1.length);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         for(int var4 = 0; var4 < var3; ++var4) {
            if (this.byteCount == 3) {
               this.encode();
            }

            this.bytes[this.byteCount++] = var1[var2 + var4];
         }

      }

      public void flush() throws IOException {
         if (this.encodedSize > 0) {
            this.writer.write(this.encoded, 0, this.encodedSize);
            this.encodedSize = 0;
         }

         this.writer.flush();
      }

      public void finish() throws IOException {
         this.encode();
         if (this.lineLength > 0) {
            this.encoded[this.encodedSize++] = '\r';
            this.encoded[this.encodedSize++] = '\n';
            this.lineLength = 0;
         }

         this.flush();
      }

      public void close() throws IOException {
         this.finish();
         this.writer.close();
      }

      private void encode() throws IOException {
         if (this.byteCount > 0) {
            if (this.encodedSize + 6 >= this.encoded.length) {
               this.writer.write(this.encoded, 0, this.encodedSize);
               this.encodedSize = 0;
            }

            boolean var1 = false;
            boolean var2 = false;
            int var3 = this.bytes[0] & 255;
            var3 <<= 8;
            if (this.byteCount > 1) {
               var3 |= this.bytes[1] & 255;
               var2 = true;
            }

            var3 <<= 8;
            if (this.byteCount > 2) {
               var3 |= this.bytes[2] & 255;
               var1 = true;
            }

            this.encoded[this.encodedSize + 3] = Base64.toDigit[var1 ? var3 & 63 : 64];
            var3 >>>= 6;
            this.encoded[this.encodedSize + 2] = Base64.toDigit[var2 ? var3 & 63 : 64];
            var3 >>>= 6;
            this.encoded[this.encodedSize + 1] = Base64.toDigit[var3 & 63];
            var3 >>>= 6;
            this.encoded[this.encodedSize] = Base64.toDigit[var3 & 63];
            this.encodedSize += 4;
            this.lineLength += 4;
            if (this.lineLength == 76) {
               this.encoded[this.encodedSize++] = '\r';
               this.encoded[this.encodedSize++] = '\n';
               this.lineLength = 0;
            }

            this.byteCount = 0;
         }

      }
   }
}
