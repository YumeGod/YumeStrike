package org.apache.batik.ext.awt.image.codec.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class MemoryCacheSeekableStream extends SeekableStream {
   private InputStream src;
   private long pointer = 0L;
   private static final int SECTOR_SHIFT = 9;
   private static final int SECTOR_SIZE = 512;
   private static final int SECTOR_MASK = 511;
   private List data = new ArrayList();
   int sectors = 0;
   int length = 0;
   boolean foundEOS = false;

   public MemoryCacheSeekableStream(InputStream var1) {
      this.src = var1;
   }

   private long readUntil(long var1) throws IOException {
      if (var1 < (long)this.length) {
         return var1;
      } else if (this.foundEOS) {
         return (long)this.length;
      } else {
         int var3 = (int)(var1 >> 9);
         int var4 = this.length >> 9;

         for(int var5 = var4; var5 <= var3; ++var5) {
            byte[] var6 = new byte[512];
            this.data.add(var6);
            int var7 = 512;

            int var9;
            for(int var8 = 0; var7 > 0; this.length += var9) {
               var9 = this.src.read(var6, var8, var7);
               if (var9 == -1) {
                  this.foundEOS = true;
                  return (long)this.length;
               }

               var8 += var9;
               var7 -= var9;
            }
         }

         return (long)this.length;
      }
   }

   public boolean canSeekBackwards() {
      return true;
   }

   public long getFilePointer() {
      return this.pointer;
   }

   public void seek(long var1) throws IOException {
      if (var1 < 0L) {
         throw new IOException(PropertyUtil.getString("MemoryCacheSeekableStream0"));
      } else {
         this.pointer = var1;
      }
   }

   public int read() throws IOException {
      long var1 = this.pointer + 1L;
      long var3 = this.readUntil(var1);
      if (var3 >= var1) {
         byte[] var5 = (byte[])this.data.get((int)(this.pointer >> 9));
         return var5[(int)(this.pointer++ & 511L)] & 255;
      } else {
         return -1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var2 >= 0 && var3 >= 0 && var2 + var3 <= var1.length) {
         if (var3 == 0) {
            return 0;
         } else {
            long var4 = this.readUntil(this.pointer + (long)var3);
            if (var4 <= this.pointer) {
               return -1;
            } else {
               byte[] var6 = (byte[])this.data.get((int)(this.pointer >> 9));
               int var7 = Math.min(var3, 512 - (int)(this.pointer & 511L));
               System.arraycopy(var6, (int)(this.pointer & 511L), var1, var2, var7);
               this.pointer += (long)var7;
               return var7;
            }
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }
}
