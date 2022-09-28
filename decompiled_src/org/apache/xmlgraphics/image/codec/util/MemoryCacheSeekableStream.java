package org.apache.xmlgraphics.image.codec.util;

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

   public MemoryCacheSeekableStream(InputStream src) {
      this.src = src;
   }

   private long readUntil(long pos) throws IOException {
      if (pos < (long)this.length) {
         return pos;
      } else if (this.foundEOS) {
         return (long)this.length;
      } else {
         int sector = (int)(pos >> 9);
         int startSector = this.length >> 9;

         for(int i = startSector; i <= sector; ++i) {
            byte[] buf = new byte[512];
            this.data.add(buf);
            int len = 512;

            int nbytes;
            for(int off = 0; len > 0; this.length += nbytes) {
               nbytes = this.src.read(buf, off, len);
               if (nbytes == -1) {
                  this.foundEOS = true;
                  return (long)this.length;
               }

               off += nbytes;
               len -= nbytes;
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

   public void seek(long pos) throws IOException {
      if (pos < 0L) {
         throw new IOException(PropertyUtil.getString("MemoryCacheSeekableStream0"));
      } else {
         this.pointer = pos;
      }
   }

   public int read() throws IOException {
      long next = this.pointer + 1L;
      long pos = this.readUntil(next);
      if (pos >= next) {
         byte[] buf = (byte[])this.data.get((int)(this.pointer >> 9));
         return buf[(int)(this.pointer++ & 511L)] & 255;
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
            if (pos <= this.pointer) {
               return -1;
            } else {
               byte[] buf = (byte[])this.data.get((int)(this.pointer >> 9));
               int nbytes = Math.min(len, 512 - (int)(this.pointer & 511L));
               System.arraycopy(buf, (int)(this.pointer & 511L), b, off, nbytes);
               this.pointer += (long)nbytes;
               return nbytes;
            }
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }
}
