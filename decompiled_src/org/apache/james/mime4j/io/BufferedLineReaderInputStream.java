package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class BufferedLineReaderInputStream extends LineReaderInputStream {
   private boolean truncated;
   boolean tempBuffer;
   private byte[] origBuffer;
   private int origBufpos;
   private int origBuflen;
   private byte[] buffer;
   private int bufpos;
   private int buflen;
   private final int maxLineLen;

   public BufferedLineReaderInputStream(InputStream instream, int buffersize, int maxLineLen) {
      super(instream);
      this.tempBuffer = false;
      if (instream == null) {
         throw new IllegalArgumentException("Input stream may not be null");
      } else if (buffersize <= 0) {
         throw new IllegalArgumentException("Buffer size may not be negative or zero");
      } else {
         this.buffer = new byte[buffersize];
         this.bufpos = 0;
         this.buflen = 0;
         this.maxLineLen = maxLineLen;
         this.truncated = false;
      }
   }

   public BufferedLineReaderInputStream(InputStream instream, int buffersize) {
      this(instream, buffersize, -1);
   }

   private void expand(int newlen) {
      byte[] newbuffer = new byte[newlen];
      int len = this.bufferLen();
      if (len > 0) {
         System.arraycopy(this.buffer, this.bufpos, newbuffer, this.bufpos, len);
      }

      this.buffer = newbuffer;
   }

   public void ensureCapacity(int len) {
      if (len > this.buffer.length) {
         this.expand(len);
      }

   }

   public int fillBuffer() throws IOException {
      if (this.tempBuffer) {
         if (this.bufpos != this.buflen) {
            throw new IllegalStateException("unread only works when a buffer is fully read before the next refill is asked!");
         } else {
            this.buffer = this.origBuffer;
            this.buflen = this.origBuflen;
            this.bufpos = this.origBufpos;
            this.tempBuffer = false;
            return this.bufferLen();
         }
      } else {
         int l;
         if (this.bufpos > 0) {
            l = this.bufferLen();
            if (l > 0) {
               System.arraycopy(this.buffer, this.bufpos, this.buffer, 0, l);
            }

            this.bufpos = 0;
            this.buflen = l;
         }

         int off = this.buflen;
         int len = this.buffer.length - off;
         l = this.in.read(this.buffer, off, len);
         if (l == -1) {
            return -1;
         } else {
            this.buflen = off + l;
            return l;
         }
      }
   }

   private int bufferLen() {
      return this.buflen - this.bufpos;
   }

   public boolean hasBufferedData() {
      return this.bufferLen() > 0;
   }

   public void truncate() {
      this.clear();
      this.truncated = true;
   }

   protected boolean readAllowed() {
      return !this.truncated;
   }

   public int read() throws IOException {
      if (!this.readAllowed()) {
         return -1;
      } else {
         int noRead = false;

         int noRead;
         do {
            if (this.hasBufferedData()) {
               return this.buffer[this.bufpos++] & 255;
            }

            noRead = this.fillBuffer();
         } while(noRead != -1);

         return -1;
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (!this.readAllowed()) {
         return -1;
      } else if (b == null) {
         return 0;
      } else {
         int noRead = false;

         int noRead;
         do {
            if (this.hasBufferedData()) {
               int chunk = this.bufferLen();
               if (chunk > len) {
                  chunk = len;
               }

               System.arraycopy(this.buffer, this.bufpos, b, off, chunk);
               this.bufpos += chunk;
               return chunk;
            }

            noRead = this.fillBuffer();
         } while(noRead != -1);

         return -1;
      }
   }

   public int read(byte[] b) throws IOException {
      if (!this.readAllowed()) {
         return -1;
      } else {
         return b == null ? 0 : this.read(b, 0, b.length);
      }
   }

   public boolean markSupported() {
      return false;
   }

   public int readLine(ByteArrayBuffer dst) throws MaxLineLimitException, IOException {
      if (dst == null) {
         throw new IllegalArgumentException("Buffer may not be null");
      } else if (!this.readAllowed()) {
         return -1;
      } else {
         int total = 0;
         boolean found = false;
         int bytesRead = 0;

         while(!found) {
            if (!this.hasBufferedData()) {
               bytesRead = this.fillBuffer();
               if (bytesRead == -1) {
                  break;
               }
            }

            int i = this.indexOf((byte)10);
            int chunk;
            if (i != -1) {
               found = true;
               chunk = i + 1 - this.pos();
            } else {
               chunk = this.length();
            }

            if (chunk > 0) {
               dst.append(this.buf(), this.pos(), chunk);
               this.skip(chunk);
               total += chunk;
            }

            if (this.maxLineLen > 0 && dst.length() >= this.maxLineLen) {
               throw new MaxLineLimitException("Maximum line length limit exceeded");
            }
         }

         return total == 0 && bytesRead == -1 ? -1 : total;
      }
   }

   public int indexOf(byte[] pattern, int off, int len) {
      if (pattern == null) {
         throw new IllegalArgumentException("Pattern may not be null");
      } else if (off >= this.bufpos && len >= 0 && off + len <= this.buflen) {
         if (len < pattern.length) {
            return -1;
         } else {
            int[] shiftTable = new int[256];

            int j;
            for(j = 0; j < shiftTable.length; ++j) {
               shiftTable[j] = pattern.length + 1;
            }

            int cur;
            for(j = 0; j < pattern.length; ++j) {
               cur = pattern[j] & 255;
               shiftTable[cur] = pattern.length - j;
            }

            int x;
            for(j = 0; j <= len - pattern.length; j += shiftTable[x]) {
               cur = off + j;
               boolean match = true;

               int pos;
               for(pos = 0; pos < pattern.length; ++pos) {
                  if (this.buffer[cur + pos] != pattern[pos]) {
                     match = false;
                     break;
                  }
               }

               if (match) {
                  return cur;
               }

               pos = cur + pattern.length;
               if (pos >= this.buffer.length) {
                  break;
               }

               x = this.buffer[pos] & 255;
            }

            return -1;
         }
      } else {
         throw new IndexOutOfBoundsException("looking for " + off + "(" + len + ")" + " in " + this.bufpos + "/" + this.buflen);
      }
   }

   public int indexOf(byte[] pattern) {
      return this.indexOf(pattern, this.bufpos, this.buflen - this.bufpos);
   }

   public int indexOf(byte b, int off, int len) {
      if (off >= this.bufpos && len >= 0 && off + len <= this.buflen) {
         for(int i = off; i < off + len; ++i) {
            if (this.buffer[i] == b) {
               return i;
            }
         }

         return -1;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int indexOf(byte b) {
      return this.indexOf(b, this.bufpos, this.bufferLen());
   }

   public int byteAt(int pos) {
      if (pos >= this.bufpos && pos <= this.buflen) {
         return this.buffer[pos] & 255;
      } else {
         throw new IndexOutOfBoundsException("looking for " + pos + " in " + this.bufpos + "/" + this.buflen);
      }
   }

   protected byte[] buf() {
      return this.buffer;
   }

   protected int pos() {
      return this.bufpos;
   }

   protected int limit() {
      return this.buflen;
   }

   protected int length() {
      return this.bufferLen();
   }

   public int capacity() {
      return this.buffer.length;
   }

   protected int skip(int n) {
      int chunk = Math.min(n, this.bufferLen());
      this.bufpos += chunk;
      return chunk;
   }

   private void clear() {
      this.bufpos = 0;
      this.buflen = 0;
   }

   public String toString() {
      StringBuilder buffer = new StringBuilder();
      buffer.append("[pos: ");
      buffer.append(this.bufpos);
      buffer.append("]");
      buffer.append("[limit: ");
      buffer.append(this.buflen);
      buffer.append("]");
      buffer.append("[");

      int i;
      for(i = this.bufpos; i < this.buflen; ++i) {
         buffer.append((char)this.buffer[i]);
      }

      buffer.append("]");
      if (this.tempBuffer) {
         buffer.append("-ORIG[pos: ");
         buffer.append(this.origBufpos);
         buffer.append("]");
         buffer.append("[limit: ");
         buffer.append(this.origBuflen);
         buffer.append("]");
         buffer.append("[");

         for(i = this.origBufpos; i < this.origBuflen; ++i) {
            buffer.append((char)this.origBuffer[i]);
         }

         buffer.append("]");
      }

      return buffer.toString();
   }

   public boolean unread(ByteArrayBuffer buf) {
      if (this.tempBuffer) {
         return false;
      } else {
         this.origBuffer = this.buffer;
         this.origBuflen = this.buflen;
         this.origBufpos = this.bufpos;
         this.bufpos = 0;
         this.buflen = buf.length();
         this.buffer = buf.buffer();
         this.tempBuffer = true;
         return true;
      }
   }
}
