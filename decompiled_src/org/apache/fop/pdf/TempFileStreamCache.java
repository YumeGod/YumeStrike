package org.apache.fop.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class TempFileStreamCache implements StreamCache {
   private OutputStream output;
   private File tempFile = File.createTempFile("org.apache.fop.pdf.StreamCache-", ".temp");

   public TempFileStreamCache() throws IOException {
      this.tempFile.deleteOnExit();
   }

   public OutputStream getOutputStream() throws IOException {
      if (this.output == null) {
         this.output = new BufferedOutputStream(new FileOutputStream(this.tempFile));
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
         this.output.close();
         this.output = null;
         InputStream input = new FileInputStream(this.tempFile);

         int var3;
         try {
            var3 = IOUtils.copy((InputStream)input, (OutputStream)out);
         } finally {
            IOUtils.closeQuietly((InputStream)input);
         }

         return var3;
      }
   }

   public int getSize() throws IOException {
      if (this.output != null) {
         this.output.flush();
      }

      return (int)this.tempFile.length();
   }

   public void clear() throws IOException {
      if (this.output != null) {
         this.output.close();
         this.output = null;
      }

      if (this.tempFile.exists()) {
         this.tempFile.delete();
      }

   }
}
