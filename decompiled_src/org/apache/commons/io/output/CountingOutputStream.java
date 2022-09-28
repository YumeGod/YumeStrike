package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends ProxyOutputStream {
   private long count;

   public CountingOutputStream(OutputStream out) {
      super(out);
   }

   public void write(byte[] b) throws IOException {
      this.count += (long)b.length;
      super.write(b);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      this.count += (long)len;
      super.write(b, off, len);
   }

   public void write(int b) throws IOException {
      ++this.count;
      super.write(b);
   }

   public synchronized int getCount() {
      long result = this.getByteCount();
      if (result > 2147483647L) {
         throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
      } else {
         return (int)result;
      }
   }

   public synchronized int resetCount() {
      long result = this.resetByteCount();
      if (result > 2147483647L) {
         throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
      } else {
         return (int)result;
      }
   }

   public synchronized long getByteCount() {
      return this.count;
   }

   public synchronized long resetByteCount() {
      long tmp = this.count;
      this.count = 0L;
      return tmp;
   }
}
