package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class UCSReader extends Reader {
   public static final int DEFAULT_BUFFER_SIZE = 8192;
   public static final short UCS2LE = 1;
   public static final short UCS2BE = 2;
   public static final short UCS4LE = 4;
   public static final short UCS4BE = 8;
   protected InputStream fInputStream;
   protected byte[] fBuffer;
   protected short fEncoding;

   public UCSReader(InputStream var1, short var2) {
      this(var1, 8192, var2);
   }

   public UCSReader(InputStream var1, int var2, short var3) {
      this.fInputStream = var1;
      this.fBuffer = new byte[var2];
      this.fEncoding = var3;
   }

   public int read() throws IOException {
      int var1 = this.fInputStream.read() & 255;
      if (var1 == 255) {
         return -1;
      } else {
         int var2 = this.fInputStream.read() & 255;
         if (var2 == 255) {
            return -1;
         } else if (this.fEncoding >= 4) {
            int var3 = this.fInputStream.read() & 255;
            if (var3 == 255) {
               return -1;
            } else {
               int var4 = this.fInputStream.read() & 255;
               if (var4 == 255) {
                  return -1;
               } else {
                  System.err.println("b0 is " + (var1 & 255) + " b1 " + (var2 & 255) + " b2 " + (var3 & 255) + " b3 " + (var4 & 255));
                  return this.fEncoding == 8 ? (var1 << 24) + (var2 << 16) + (var3 << 8) + var4 : (var4 << 24) + (var3 << 16) + (var2 << 8) + var1;
               }
            }
         } else {
            return this.fEncoding == 2 ? (var1 << 8) + var2 : (var2 << 8) + var1;
         }
      }
   }

   public int read(char[] var1, int var2, int var3) throws IOException {
      int var4 = var3 << (this.fEncoding >= 4 ? 2 : 1);
      if (var4 > this.fBuffer.length) {
         var4 = this.fBuffer.length;
      }

      int var5 = this.fInputStream.read(this.fBuffer, 0, var4);
      if (var5 == -1) {
         return -1;
      } else {
         int var6;
         int var7;
         int var8;
         int var9;
         if (this.fEncoding >= 4) {
            var6 = 4 - (var5 & 3) & 3;

            label62:
            for(var7 = 0; var7 < var6; ++var7) {
               var8 = this.fInputStream.read();
               if (var8 == -1) {
                  var9 = var7;

                  while(true) {
                     if (var9 >= var6) {
                        break label62;
                     }

                     this.fBuffer[var5 + var9] = 0;
                     ++var9;
                  }
               }

               this.fBuffer[var5 + var7] = (byte)var8;
            }

            var5 += var6;
         } else {
            var6 = var5 & 1;
            if (var6 != 0) {
               ++var5;
               var7 = this.fInputStream.read();
               if (var7 == -1) {
                  this.fBuffer[var5] = 0;
               } else {
                  this.fBuffer[var5] = (byte)var7;
               }
            }
         }

         var6 = var5 >> (this.fEncoding >= 4 ? 2 : 1);
         var7 = 0;

         for(var8 = 0; var8 < var6; ++var8) {
            var9 = this.fBuffer[var7++] & 255;
            int var10 = this.fBuffer[var7++] & 255;
            if (this.fEncoding >= 4) {
               int var11 = this.fBuffer[var7++] & 255;
               int var12 = this.fBuffer[var7++] & 255;
               if (this.fEncoding == 8) {
                  var1[var2 + var8] = (char)((var9 << 24) + (var10 << 16) + (var11 << 8) + var12);
               } else {
                  var1[var2 + var8] = (char)((var12 << 24) + (var11 << 16) + (var10 << 8) + var9);
               }
            } else if (this.fEncoding == 2) {
               var1[var2 + var8] = (char)((var9 << 8) + var10);
            } else {
               var1[var2 + var8] = (char)((var10 << 8) + var9);
            }
         }

         return var6;
      }
   }

   public long skip(long var1) throws IOException {
      int var3 = this.fEncoding >= 4 ? 2 : 1;
      long var4 = this.fInputStream.skip(var1 << var3);
      return (var4 & (long)(var3 | 1)) == 0L ? var4 >> var3 : (var4 >> var3) + 1L;
   }

   public boolean ready() throws IOException {
      return false;
   }

   public boolean markSupported() {
      return this.fInputStream.markSupported();
   }

   public void mark(int var1) throws IOException {
      this.fInputStream.mark(var1);
   }

   public void reset() throws IOException {
      this.fInputStream.reset();
   }

   public void close() throws IOException {
      this.fInputStream.close();
   }
}
