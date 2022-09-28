package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class LineReaderInputStreamAdaptor extends LineReaderInputStream {
   private final LineReaderInputStream bis;
   private final int maxLineLen;
   private boolean used;
   private boolean eof;

   public LineReaderInputStreamAdaptor(InputStream is, int maxLineLen) {
      super(is);
      this.used = false;
      this.eof = false;
      if (is instanceof LineReaderInputStream) {
         this.bis = (LineReaderInputStream)is;
      } else {
         this.bis = null;
      }

      this.maxLineLen = maxLineLen;
   }

   public LineReaderInputStreamAdaptor(InputStream is) {
      this(is, -1);
   }

   public int read() throws IOException {
      int i = this.in.read();
      this.eof = i == -1;
      this.used = true;
      return i;
   }

   public int read(byte[] b, int off, int len) throws IOException {
      int i = this.in.read(b, off, len);
      this.eof = i == -1;
      this.used = true;
      return i;
   }

   public int readLine(ByteArrayBuffer dst) throws MaxLineLimitException, IOException {
      int i;
      if (this.bis != null) {
         i = this.bis.readLine(dst);
      } else {
         i = this.doReadLine(dst);
      }

      this.eof = i == -1;
      this.used = true;
      return i;
   }

   private int doReadLine(ByteArrayBuffer dst) throws MaxLineLimitException, IOException {
      int total = 0;

      int ch;
      while((ch = this.in.read()) != -1) {
         dst.append(ch);
         ++total;
         if (this.maxLineLen > 0 && dst.length() >= this.maxLineLen) {
            throw new MaxLineLimitException("Maximum line length limit exceeded");
         }

         if (ch == 10) {
            break;
         }
      }

      return total == 0 && ch == -1 ? -1 : total;
   }

   public boolean eof() {
      return this.eof;
   }

   public boolean isUsed() {
      return this.used;
   }

   public String toString() {
      return "[LineReaderInputStreamAdaptor: " + this.bis + "]";
   }

   public boolean unread(ByteArrayBuffer buf) {
      return this.bis != null ? this.bis.unread(buf) : false;
   }

   public long skip(long count) throws IOException {
      if (count <= 0L) {
         return 0L;
      } else {
         int bufferSize = count > 8192L ? 8192 : (int)count;
         byte[] buffer = new byte[bufferSize];

         long result;
         int res;
         for(result = 0L; count > 0L; count -= (long)res) {
            res = this.read(buffer);
            if (res == -1) {
               break;
            }

            result += (long)res;
         }

         return result;
      }
   }
}
