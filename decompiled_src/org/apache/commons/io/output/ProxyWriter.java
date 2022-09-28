package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class ProxyWriter extends FilterWriter {
   public ProxyWriter(Writer proxy) {
      super(proxy);
   }

   public void write(int idx) throws IOException {
      super.out.write(idx);
   }

   public void write(char[] chr) throws IOException {
      super.out.write(chr);
   }

   public void write(char[] chr, int st, int end) throws IOException {
      super.out.write(chr, st, end);
   }

   public void write(String str) throws IOException {
      super.out.write(str);
   }

   public void write(String str, int st, int end) throws IOException {
      super.out.write(str, st, end);
   }

   public void flush() throws IOException {
      super.out.flush();
   }

   public void close() throws IOException {
      super.out.close();
   }
}
