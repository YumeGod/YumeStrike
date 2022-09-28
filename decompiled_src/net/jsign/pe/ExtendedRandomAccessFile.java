package net.jsign.pe;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

class ExtendedRandomAccessFile extends RandomAccessFile {
   ExtendedRandomAccessFile(File file, String mode) throws FileNotFoundException {
      super(file, mode);
   }

   public int readWord() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      if ((ch1 | ch2) < 0) {
         throw new EOFException();
      } else {
         return '\uffff' & ch1 + (ch2 << 8);
      }
   }

   public long readDWord() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      int ch3 = this.read();
      int ch4 = this.read();
      if ((ch1 | ch2 | ch3 | ch4) < 0) {
         throw new EOFException();
      } else {
         return 4294967295L & (long)(ch1 + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
      }
   }

   public long readQWord() throws IOException {
      long ch1 = (long)this.read();
      long ch2 = (long)this.read();
      long ch3 = (long)this.read();
      long ch4 = (long)this.read();
      long ch5 = (long)this.read();
      long ch6 = (long)this.read();
      long ch7 = (long)this.read();
      long ch8 = (long)this.read();
      if ((ch1 | ch2 | ch3 | ch4) < 0L) {
         throw new EOFException();
      } else {
         return ch1 + (ch2 << 8) + (ch3 << 16) + (ch4 << 24) + (ch5 << 32) + (ch6 << 40) + (ch7 << 48) + (ch8 << 56);
      }
   }
}
