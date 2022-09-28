package org.apache.xmlgraphics.image.codec.util;

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

   public FileCacheSeekableStream(InputStream stream) throws IOException {
      this.buf = new byte[this.bufLen];
      this.length = 0L;
      this.pointer = 0L;
      this.foundEOF = false;
      this.stream = stream;
      this.cacheFile = File.createTempFile("jai-FCSS-", ".tmp");
      this.cacheFile.deleteOnExit();
      this.cache = new RandomAccessFile(this.cacheFile, "rw");
   }

   private long readUntil(long pos) throws IOException {
      if (pos < this.length) {
         return pos;
      } else if (this.foundEOF) {
         return this.length;
      } else {
         long len = pos - this.length;
         this.cache.seek(this.length);

         while(len > 0L) {
            int nbytes = this.stream.read(this.buf, 0, (int)Math.min(len, (long)this.bufLen));
            if (nbytes == -1) {
               this.foundEOF = true;
               return this.length;
            }

            this.cache.setLength(this.cache.length() + (long)nbytes);
            this.cache.write(this.buf, 0, nbytes);
            len -= (long)nbytes;
            this.length += (long)nbytes;
         }

         return pos;
      }
   }

   public boolean canSeekBackwards() {
      return true;
   }

   public long getFilePointer() {
      return this.pointer;
   }

   public void seek(long pos) throws IOException {
      if (pos < 0L) {
         throw new IOException(PropertyUtil.getString("FileCacheSeekableStream0"));
      } else {
         this.pointer = pos;
      }
   }

   public int read() throws IOException {
      long next = this.pointer + 1L;
      long pos = this.readUntil(next);
      if (pos >= next) {
         this.cache.seek((long)(this.pointer++));
         return this.cache.read();
      } else {
         return -1;
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (b == null) {
         throw new NullPointerException();
      } else if (off >= 0 && len >= 0 && off + len <= b.length) {
         if (len == 0) {
            return 0;
         } else {
            long pos = this.readUntil(this.pointer + (long)len);
            len = (int)Math.min((long)len, pos - this.pointer);
            if (len > 0) {
               this.cache.seek(this.pointer);
               this.cache.readFully(b, off, len);
               this.pointer += (long)len;
               return len;
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
