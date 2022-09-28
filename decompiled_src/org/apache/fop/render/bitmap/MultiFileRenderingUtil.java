package org.apache.fop.render.bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MultiFileRenderingUtil {
   private String filePrefix;
   private String fileExtension;
   private File outputDir;

   public MultiFileRenderingUtil(String ext, File outputFile) {
      this.fileExtension = ext;
      if (outputFile == null) {
         this.outputDir = null;
         this.filePrefix = null;
      } else {
         this.outputDir = outputFile.getParentFile();
         String s = outputFile.getName();
         int i = s.lastIndexOf(".");
         if (i > 0) {
            String extension = s.substring(i + 1).toLowerCase();
            if (!ext.equals(extension)) {
               throw new IllegalArgumentException("Invalid file extension ('" + extension + "') specified");
            }
         } else {
            if (i != -1) {
               throw new IllegalArgumentException("Invalid file name ('" + s + "') specified");
            }

            i = s.length();
         }

         if (s.charAt(i - 1) == '1') {
            --i;
         }

         this.filePrefix = s.substring(0, i);
      }

   }

   public OutputStream createOutputStream(int pageNumber) throws IOException {
      if (this.filePrefix == null) {
         return null;
      } else {
         File f = new File(this.outputDir, this.filePrefix + (pageNumber + 1) + "." + this.fileExtension);
         OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
         return os;
      }
   }
}
