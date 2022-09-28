package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProxyOutputStream extends FilterOutputStream {
   public ProxyOutputStream(OutputStream proxy) {
      super(proxy);
   }

   public void write(int idx) throws IOException {
      super.out.write(idx);
   }

   public void write(byte[] bts) throws IOException {
      super.out.write(bts);
   }

   public void write(byte[] bts, int st, int end) throws IOException {
      super.out.write(bts, st, end);
   }

   public void flush() throws IOException {
      super.out.flush();
   }

   public void close() throws IOException {
      super.out.close();
   }
}
