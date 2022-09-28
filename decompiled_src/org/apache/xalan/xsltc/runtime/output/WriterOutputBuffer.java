package org.apache.xalan.xsltc.runtime.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

class WriterOutputBuffer implements OutputBuffer {
   private static final int KB = 1024;
   private static int BUFFER_SIZE = 4096;
   private Writer _writer;

   public WriterOutputBuffer(Writer writer) {
      this._writer = new BufferedWriter(writer, BUFFER_SIZE);
   }

   public String close() {
      try {
         this._writer.flush();
         return "";
      } catch (IOException var2) {
         throw new RuntimeException(var2.toString());
      }
   }

   public OutputBuffer append(String s) {
      try {
         this._writer.write(s);
         return this;
      } catch (IOException var3) {
         throw new RuntimeException(var3.toString());
      }
   }

   public OutputBuffer append(char[] s, int from, int to) {
      try {
         this._writer.write(s, from, to);
         return this;
      } catch (IOException var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public OutputBuffer append(char ch) {
      try {
         this._writer.write(ch);
         return this;
      } catch (IOException var3) {
         throw new RuntimeException(var3.toString());
      }
   }

   static {
      String osName = System.getProperty("os.name");
      if (osName.equalsIgnoreCase("solaris")) {
         BUFFER_SIZE = 32768;
      }

   }
}
