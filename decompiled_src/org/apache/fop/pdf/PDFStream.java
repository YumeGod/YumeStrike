package org.apache.fop.pdf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class PDFStream extends AbstractPDFStream {
   protected StreamCache data;
   private transient Writer streamWriter;

   public PDFStream() {
      try {
         this.data = StreamCacheFactory.getInstance().createStreamCache();
         this.streamWriter = new OutputStreamWriter(this.getBufferOutputStream(), "ISO-8859-1");
         this.streamWriter = new BufferedWriter(this.streamWriter);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void add(String s) {
      try {
         this.streamWriter.write(s);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private void flush() throws IOException {
      this.streamWriter.flush();
   }

   public Writer getBufferWriter() {
      return this.streamWriter;
   }

   public OutputStream getBufferOutputStream() throws IOException {
      if (this.streamWriter != null) {
         this.flush();
      }

      return this.data.getOutputStream();
   }

   public void setData(byte[] data) throws IOException {
      this.data.clear();
      this.data.write(data);
   }

   public int getDataLength() {
      try {
         this.flush();
         return this.data.getSize();
      } catch (Exception var2) {
         var2.printStackTrace();
         return 0;
      }
   }

   protected int getSizeHint() throws IOException {
      this.flush();
      return this.data.getSize();
   }

   protected void outputRawStreamData(OutputStream out) throws IOException {
      this.flush();
      this.data.outputContents(out);
   }

   protected int output(OutputStream stream) throws IOException {
      int len = super.output(stream);
      this.data = null;
      return len;
   }
}
