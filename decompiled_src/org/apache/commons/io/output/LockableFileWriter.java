package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class LockableFileWriter extends Writer {
   private static final String LCK = ".lck";
   private final Writer out;
   private final File lockFile;
   // $FF: synthetic field
   static Class class$org$apache$commons$io$output$LockableFileWriter;

   public LockableFileWriter(String fileName) throws IOException {
      this((String)fileName, false, (String)null);
   }

   public LockableFileWriter(String fileName, boolean append) throws IOException {
      this((String)fileName, append, (String)null);
   }

   public LockableFileWriter(String fileName, boolean append, String lockDir) throws IOException {
      this(new File(fileName), append, lockDir);
   }

   public LockableFileWriter(File file) throws IOException {
      this((File)file, false, (String)null);
   }

   public LockableFileWriter(File file, boolean append) throws IOException {
      this((File)file, append, (String)null);
   }

   public LockableFileWriter(File file, boolean append, String lockDir) throws IOException {
      this(file, (String)null, append, lockDir);
   }

   public LockableFileWriter(File file, String encoding) throws IOException {
      this(file, encoding, false, (String)null);
   }

   public LockableFileWriter(File file, String encoding, boolean append, String lockDir) throws IOException {
      file = file.getAbsoluteFile();
      if (file.getParentFile() != null) {
         FileUtils.forceMkdir(file.getParentFile());
      }

      if (file.isDirectory()) {
         throw new IOException("File specified is a directory");
      } else {
         if (lockDir == null) {
            lockDir = System.getProperty("java.io.tmpdir");
         }

         File lockDirFile = new File(lockDir);
         FileUtils.forceMkdir(lockDirFile);
         this.testLockDir(lockDirFile);
         this.lockFile = new File(lockDirFile, file.getName() + ".lck");
         this.createLock();
         this.out = this.initWriter(file, encoding, append);
      }
   }

   private void testLockDir(File lockDir) throws IOException {
      if (!lockDir.exists()) {
         throw new IOException("Could not find lockDir: " + lockDir.getAbsolutePath());
      } else if (!lockDir.canWrite()) {
         throw new IOException("Could not write to lockDir: " + lockDir.getAbsolutePath());
      }
   }

   private void createLock() throws IOException {
      Class var1 = class$org$apache$commons$io$output$LockableFileWriter == null ? (class$org$apache$commons$io$output$LockableFileWriter = class$("org.apache.commons.io.output.LockableFileWriter")) : class$org$apache$commons$io$output$LockableFileWriter;
      synchronized(var1) {
         if (!this.lockFile.createNewFile()) {
            throw new IOException("Can't write file, lock " + this.lockFile.getAbsolutePath() + " exists");
         } else {
            this.lockFile.deleteOnExit();
         }
      }
   }

   private Writer initWriter(File file, String encoding, boolean append) throws IOException {
      boolean fileExistedAlready = file.exists();
      OutputStream stream = null;
      Writer writer = null;

      try {
         if (encoding == null) {
            writer = new FileWriter(file.getAbsolutePath(), append);
         } else {
            stream = new FileOutputStream(file.getAbsolutePath(), append);
            writer = new OutputStreamWriter(stream, encoding);
         }

         return (Writer)writer;
      } catch (IOException var9) {
         IOUtils.closeQuietly((Writer)writer);
         IOUtils.closeQuietly((OutputStream)stream);
         this.lockFile.delete();
         if (!fileExistedAlready) {
            file.delete();
         }

         throw var9;
      } catch (RuntimeException var10) {
         IOUtils.closeQuietly((Writer)writer);
         IOUtils.closeQuietly((OutputStream)stream);
         this.lockFile.delete();
         if (!fileExistedAlready) {
            file.delete();
         }

         throw var10;
      }
   }

   public void close() throws IOException {
      try {
         this.out.close();
      } finally {
         this.lockFile.delete();
      }

   }

   public void write(int idx) throws IOException {
      this.out.write(idx);
   }

   public void write(char[] chr) throws IOException {
      this.out.write(chr);
   }

   public void write(char[] chr, int st, int end) throws IOException {
      this.out.write(chr, st, end);
   }

   public void write(String str) throws IOException {
      this.out.write(str);
   }

   public void write(String str, int st, int end) throws IOException {
      this.out.write(str, st, end);
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
