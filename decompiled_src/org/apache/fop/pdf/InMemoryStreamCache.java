package org.apache.fop.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class InMemoryStreamCache implements StreamCache {
   private int hintSize = -1;
   private ByteArrayOutputStream output;

   public InMemoryStreamCache() {
   }

   public InMemoryStreamCache(int hintSize) {
      this.hintSize = hintSize;
   }

   public OutputStream getOutputStream() throws IOException {
      if (this.output == null) {
         if (this.hintSize <= 0) {
            this.output = new ByteArrayOutputStream(512);
         } else {
            this.output = new ByteArrayOutputStream(this.hintSize);
         }
      }

      return this.output;
   }

   public void write(byte[] data) throws IOException {
      this.getOutputStream().write(data);
   }

   public int outputContents(OutputStream out) throws IOException {
      if (this.output == null) {
         return 0;
      } else {
         this.output.writeTo(out);
         return this.output.size();
      }
   }

   public int getSize() throws IOException {
      return this.output == null ? 0 : this.output.size();
   }

   public void clear() throws IOException {
      if (this.output != null) {
         this.output.close();
         this.output = null;
      }

   }
}
