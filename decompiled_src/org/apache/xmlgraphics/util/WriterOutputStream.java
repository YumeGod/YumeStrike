package org.apache.xmlgraphics.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class WriterOutputStream extends OutputStream {
   private Writer writer;
   private String encoding;

   public WriterOutputStream(Writer writer) {
      this(writer, (String)null);
   }

   public WriterOutputStream(Writer writer, String encoding) {
      this.writer = writer;
      this.encoding = encoding;
   }

   public void close() throws IOException {
      this.writer.close();
   }

   public void flush() throws IOException {
      this.writer.flush();
   }

   public void write(byte[] buf, int offset, int length) throws IOException {
      if (this.encoding != null) {
         this.writer.write(new String(buf, offset, length, this.encoding));
      } else {
         this.writer.write(new String(buf, offset, length));
      }

   }

   public void write(byte[] buf) throws IOException {
      this.write(buf, 0, buf.length);
   }

   public void write(int b) throws IOException {
      this.write(new byte[]{(byte)b});
   }
}
