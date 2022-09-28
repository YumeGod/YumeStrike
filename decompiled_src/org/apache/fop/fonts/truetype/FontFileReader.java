package org.apache.fop.fonts.truetype;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class FontFileReader {
   private int fsize;
   private int current;
   private byte[] file;

   private void init(InputStream in) throws IOException {
      this.file = IOUtils.toByteArray(in);
      this.fsize = this.file.length;
      this.current = 0;
   }

   public FontFileReader(String fileName) throws IOException {
      File f = new File(fileName);
      InputStream in = new FileInputStream(f);

      try {
         this.init(in);
      } finally {
         in.close();
      }

   }

   public FontFileReader(InputStream in) throws IOException {
      this.init(in);
   }

   public void seekSet(long offset) throws IOException {
      if (offset <= (long)this.fsize && offset >= 0L) {
         this.current = (int)offset;
      } else {
         throw new EOFException("Reached EOF, file size=" + this.fsize + " offset=" + offset);
      }
   }

   public void seekAdd(long add) throws IOException {
      this.seekSet((long)this.current + add);
   }

   public void skip(long add) throws IOException {
      this.seekAdd(add);
   }

   public int getCurrentPos() {
      return this.current;
   }

   public int getFileSize() {
      return this.fsize;
   }

   public byte read() throws IOException {
      if (this.current >= this.fsize) {
         throw new EOFException("Reached EOF, file size=" + this.fsize);
      } else {
         byte ret = this.file[this.current++];
         return ret;
      }
   }

   public final byte readTTFByte() throws IOException {
      return this.read();
   }

   public final int readTTFUByte() throws IOException {
      byte buf = this.read();
      return buf < 0 ? 256 + buf : buf;
   }

   public final short readTTFShort() throws IOException {
      int ret = (this.readTTFUByte() << 8) + this.readTTFUByte();
      short sret = (short)ret;
      return sret;
   }

   public final int readTTFUShort() throws IOException {
      int ret = (this.readTTFUByte() << 8) + this.readTTFUByte();
      return ret;
   }

   public final void writeTTFUShort(int pos, int val) throws IOException {
      if (pos + 2 > this.fsize) {
         throw new EOFException("Reached EOF");
      } else {
         byte b1 = (byte)(val >> 8 & 255);
         byte b2 = (byte)(val & 255);
         this.file[pos] = b1;
         this.file[pos + 1] = b2;
      }
   }

   public final short readTTFShort(long pos) throws IOException {
      long cp = (long)this.getCurrentPos();
      this.seekSet(pos);
      short ret = this.readTTFShort();
      this.seekSet(cp);
      return ret;
   }

   public final int readTTFUShort(long pos) throws IOException {
      long cp = (long)this.getCurrentPos();
      this.seekSet(pos);
      int ret = this.readTTFUShort();
      this.seekSet(cp);
      return ret;
   }

   public final int readTTFLong() throws IOException {
      long ret = (long)this.readTTFUByte();
      ret = (ret << 8) + (long)this.readTTFUByte();
      ret = (ret << 8) + (long)this.readTTFUByte();
      ret = (ret << 8) + (long)this.readTTFUByte();
      return (int)ret;
   }

   public final long readTTFULong() throws IOException {
      long ret = (long)this.readTTFUByte();
      ret = (ret << 8) + (long)this.readTTFUByte();
      ret = (ret << 8) + (long)this.readTTFUByte();
      ret = (ret << 8) + (long)this.readTTFUByte();
      return ret;
   }

   public final String readTTFString() throws IOException {
      int i = this.current;

      do {
         if (this.file[i++] == 0) {
            byte[] tmp = new byte[i - this.current];
            System.arraycopy(this.file, this.current, tmp, 0, i - this.current);
            return new String(tmp, "ISO-8859-1");
         }
      } while(i <= this.fsize);

      throw new EOFException("Reached EOF, file size=" + this.fsize);
   }

   public final String readTTFString(int len) throws IOException {
      if (len + this.current > this.fsize) {
         throw new EOFException("Reached EOF, file size=" + this.fsize);
      } else {
         byte[] tmp = new byte[len];
         System.arraycopy(this.file, this.current, tmp, 0, len);
         this.current += len;
         String encoding;
         if (tmp.length > 0 && tmp[0] == 0) {
            encoding = "UTF-16BE";
         } else {
            encoding = "ISO-8859-1";
         }

         return new String(tmp, encoding);
      }
   }

   public final String readTTFString(int len, int encodingID) throws IOException {
      if (len + this.current > this.fsize) {
         throw new EOFException("Reached EOF, file size=" + this.fsize);
      } else {
         byte[] tmp = new byte[len];
         System.arraycopy(this.file, this.current, tmp, 0, len);
         this.current += len;
         String encoding = "UTF-16BE";
         return new String(tmp, encoding);
      }
   }

   public byte[] getBytes(int offset, int length) throws IOException {
      if (offset + length > this.fsize) {
         throw new IOException("Reached EOF");
      } else {
         byte[] ret = new byte[length];
         System.arraycopy(this.file, offset, ret, 0, length);
         return ret;
      }
   }
}
