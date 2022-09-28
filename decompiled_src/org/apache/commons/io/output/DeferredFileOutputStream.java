package org.apache.commons.io.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class DeferredFileOutputStream extends ThresholdingOutputStream {
   private ByteArrayOutputStream memoryOutputStream;
   private OutputStream currentOutputStream;
   private File outputFile;
   private boolean closed = false;

   public DeferredFileOutputStream(int threshold, File outputFile) {
      super(threshold);
      this.outputFile = outputFile;
      this.memoryOutputStream = new ByteArrayOutputStream();
      this.currentOutputStream = this.memoryOutputStream;
   }

   protected OutputStream getStream() throws IOException {
      return this.currentOutputStream;
   }

   protected void thresholdReached() throws IOException {
      FileOutputStream fos = new FileOutputStream(this.outputFile);
      this.memoryOutputStream.writeTo(fos);
      this.currentOutputStream = fos;
      this.memoryOutputStream = null;
   }

   public boolean isInMemory() {
      return !this.isThresholdExceeded();
   }

   public byte[] getData() {
      return this.memoryOutputStream != null ? this.memoryOutputStream.toByteArray() : null;
   }

   public File getFile() {
      return this.outputFile;
   }

   public void close() throws IOException {
      super.close();
      this.closed = true;
   }

   public void writeTo(OutputStream out) throws IOException {
      if (!this.closed) {
         throw new IOException("Stream not closed");
      } else {
         if (this.isInMemory()) {
            this.memoryOutputStream.writeTo(out);
         } else {
            FileInputStream fis = new FileInputStream(this.outputFile);

            try {
               IOUtils.copy((InputStream)fis, (OutputStream)out);
            } finally {
               IOUtils.closeQuietly((InputStream)fis);
            }
         }

      }
   }
}
