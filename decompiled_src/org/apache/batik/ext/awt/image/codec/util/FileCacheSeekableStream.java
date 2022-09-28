package org.apache.batik.ext.awt.image.codec.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public final class FileCacheSeekableStream extends SeekableStream {
   private InputStream stream;
   private File cacheFile;
   private RandomAccessFile cache;
   private int bufLen = 1024;
   private byte[] buf;
   private long length;
   private long pointer;
   private boolean foundEOF;

   public FileCacheSeekableStream(InputStream var1) throws IOException {
      this.buf = new byte[this.bufLen];
      this.length = 0L;
      this.pointer = 0L;
      this.foundEOF = false;
      this.stream = var1;
      this.cacheFile = File.createTempFile("jai-FCSS-", ".tmp");
      this.cacheFile.deleteOnExit();
      this.cache = new RandomAccessFile(this.cacheFile, "rw");
   }

   private long readUntil(long var1) throws IOException {
      if (var1 < this.length) {
         return var1;
      } else if (this.foundEOF) {
         return this.length;
      } else {
         long var3 = var1 - this.length;
         this.cache.seek(this.length);

         while(var3 > 0L) {
            int var5 = this.stream.read(this.buf, 0, (int)Math.min(var3, (long)this.bufLen));
            if (var5 == -1) {
               this.foundEOF = true;
               return this.length;
            }

            this.cache.setLength(this.cache.length() + (long)var5);
            this.cache.write(this.buf, 0, var5);
            var3 -= (long)var5;
            this.length += (long)var5;
         }

         return var1;
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
         throw new IOException(PropertyUtil.getString("FileCacheSeekableStream0"));
      } else {
         this.pointer = var1;
      }
   }

   public int read() throws IOException {
      long var1 = this.pointer + 1L;
      long var3 = this.readUntil(var1);
      if (var3 >= var1) {
         this.cache.seek((long)(this.pointer++));
         return this.cache.read();
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
            var3 = (int)Math.min((long)var3, var4 - this.pointer);
            if (var3 > 0) {
               this.cache.seek(this.pointer);
               this.cache.readFully(var1, var2, var3);
               this.pointer += (long)var3;
               return var3;
            } else {
               return -1;
            }
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void close() throws IOException {
      super.close();
      this.cache.close();
      this.cacheFile.delete();
   }
}
