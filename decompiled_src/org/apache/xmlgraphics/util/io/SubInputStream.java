package org.apache.xmlgraphics.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SubInputStream extends FilterInputStream {
   private long bytesToRead;
   private boolean closeUnderlying;

   public SubInputStream(InputStream in, long maxLen, boolean closeUnderlying) {
      super(in);
      this.closeUnderlying = false;
      this.bytesToRead = maxLen;
      this.closeUnderlying = closeUnderlying;
   }

   public SubInputStream(InputStream in, long maxLen) {
      this(in, maxLen, false);
   }

   public int read() throws IOException {
      if (this.bytesToRead > 0L) {
         int result = super.read();
         if (result >= 0) {
            --this.bytesToRead;
            return result;
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (this.bytesToRead == 0L) {
         return -1;
      } else {
         int effRead = (int)Math.min(this.bytesToRead, (long)len);
         int result = super.read(b, off, effRead);
         if (result >= 0) {
            this.bytesToRead -= (long)result;
         }

         return result;
      }
   }

   public long skip(long n) throws IOException {
      long effRead = Math.min(this.bytesToRead, n);
      long result = super.skip(effRead);
      this.bytesToRead -= result;
      return result;
   }

   public void close() throws IOException {
      this.bytesToRead = 0L;
      if (this.closeUnderlying) {
         super.close();
      }

   }
}
