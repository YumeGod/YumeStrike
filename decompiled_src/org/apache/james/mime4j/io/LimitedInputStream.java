package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends PositionInputStream {
   private final long limit;

   public LimitedInputStream(InputStream instream, long limit) {
      super(instream);
      if (limit < 0L) {
         throw new IllegalArgumentException("Limit may not be negative");
      } else {
         this.limit = limit;
      }
   }

   private void enforceLimit() throws IOException {
      if (this.position >= this.limit) {
         throw new IOException("Input stream limit exceeded");
      }
   }

   public int read() throws IOException {
      this.enforceLimit();
      return super.read();
   }

   public int read(byte[] b, int off, int len) throws IOException {
      this.enforceLimit();
      len = Math.min(len, this.getBytesLeft());
      return super.read(b, off, len);
   }

   public long skip(long n) throws IOException {
      this.enforceLimit();
      n = Math.min(n, (long)this.getBytesLeft());
      return super.skip(n);
   }

   private int getBytesLeft() {
      return (int)Math.min(2147483647L, this.limit - this.position);
   }
}
