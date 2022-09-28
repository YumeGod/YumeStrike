package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public abstract class ProxyReader extends FilterReader {
   public ProxyReader(Reader proxy) {
      super(proxy);
   }

   public int read() throws IOException {
      return super.in.read();
   }

   public int read(char[] chr) throws IOException {
      return super.in.read(chr);
   }

   public int read(char[] chr, int st, int end) throws IOException {
      return super.in.read(chr, st, end);
   }

   public long skip(long ln) throws IOException {
      return super.in.skip(ln);
   }

   public boolean ready() throws IOException {
      return super.in.ready();
   }

   public void close() throws IOException {
      super.in.close();
   }

   public synchronized void mark(int idx) throws IOException {
      super.in.mark(idx);
   }

   public synchronized void reset() throws IOException {
      super.in.reset();
   }

   public boolean markSupported() {
      return super.in.markSupported();
   }
}
