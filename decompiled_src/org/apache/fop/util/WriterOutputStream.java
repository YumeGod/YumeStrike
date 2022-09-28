package org.apache.fop.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/** @deprecated */
public class WriterOutputStream extends OutputStream {
   private final org.apache.xmlgraphics.util.WriterOutputStream writerOutputStream;

   /** @deprecated */
   public WriterOutputStream(Writer writer) {
      this.writerOutputStream = new org.apache.xmlgraphics.util.WriterOutputStream(writer);
   }

   /** @deprecated */
   public WriterOutputStream(Writer writer, String encoding) {
      this.writerOutputStream = new org.apache.xmlgraphics.util.WriterOutputStream(writer, encoding);
   }

   /** @deprecated */
   public void close() throws IOException {
      this.writerOutputStream.close();
   }

   /** @deprecated */
   public void flush() throws IOException {
      this.writerOutputStream.flush();
   }

   /** @deprecated */
   public void write(byte[] buf, int offset, int length) throws IOException {
      this.writerOutputStream.write(buf, offset, length);
   }

   /** @deprecated */
   public void write(byte[] buf) throws IOException {
      this.writerOutputStream.write(buf);
   }

   /** @deprecated */
   public void write(int b) throws IOException {
      this.writerOutputStream.write(b);
   }
}
