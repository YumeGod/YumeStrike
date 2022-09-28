package org.apache.james.mime4j.storage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TempFileStorageProvider extends AbstractStorageProvider {
   private static final String DEFAULT_PREFIX = "m4j";
   private final String prefix;
   private final String suffix;
   private final File directory;

   public TempFileStorageProvider() {
      this("m4j", (String)null, (File)null);
   }

   public TempFileStorageProvider(File directory) {
      this("m4j", (String)null, directory);
   }

   public TempFileStorageProvider(String prefix, String suffix, File directory) {
      if (prefix != null && prefix.length() >= 3) {
         if (directory != null && !directory.isDirectory() && !directory.mkdirs()) {
            throw new IllegalArgumentException("invalid directory");
         } else {
            this.prefix = prefix;
            this.suffix = suffix;
            this.directory = directory;
         }
      } else {
         throw new IllegalArgumentException("invalid prefix");
      }
   }

   public StorageOutputStream createStorageOutputStream() throws IOException {
      File file = File.createTempFile(this.prefix, this.suffix, this.directory);
      file.deleteOnExit();
      return new TempFileStorageOutputStream(file);
   }

   private static final class TempFileStorage implements Storage {
      private File file;
      private static final Set filesToDelete = new HashSet();

      public TempFileStorage(File file) {
         this.file = file;
      }

      public void delete() {
         synchronized(filesToDelete) {
            if (this.file != null) {
               filesToDelete.add(this.file);
               this.file = null;
            }

            Iterator iterator = filesToDelete.iterator();

            while(iterator.hasNext()) {
               File file = (File)iterator.next();
               if (file.delete()) {
                  iterator.remove();
               }
            }

         }
      }

      public InputStream getInputStream() throws IOException {
         if (this.file == null) {
            throw new IllegalStateException("storage has been deleted");
         } else {
            return new BufferedInputStream(new FileInputStream(this.file));
         }
      }
   }

   private static final class TempFileStorageOutputStream extends StorageOutputStream {
      private File file;
      private OutputStream out;

      public TempFileStorageOutputStream(File file) throws IOException {
         this.file = file;
         this.out = new FileOutputStream(file);
      }

      public void close() throws IOException {
         super.close();
         this.out.close();
      }

      protected void write0(byte[] buffer, int offset, int length) throws IOException {
         this.out.write(buffer, offset, length);
      }

      protected Storage toStorage0() throws IOException {
         return new TempFileStorage(this.file);
      }
   }
}
