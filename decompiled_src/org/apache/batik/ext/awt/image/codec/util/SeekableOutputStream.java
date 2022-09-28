package org.apache.batik.ext.awt.image.codec.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class SeekableOutputStream extends OutputStream {
   private RandomAccessFile file;

   public SeekableOutputStream(RandomAccessFile var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("SeekableOutputStream0");
      } else {
         this.file = var1;
      }
   }

   public void write(int var1) throws IOException {
      this.file.write(var1);
   }

   public void write(byte[] var1) throws IOException {
      this.file.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.file.write(var1, var2, var3);
   }

   public void flush() throws IOException {
      this.file.getFD().sync();
   }

   public void close() throws IOException {
      this.file.close();
   }

   public long getFilePointer() throws IOException {
      return this.file.getFilePointer();
   }

   public void seek(long var1) throws IOException {
      this.file.seek(var1);
   }
}
