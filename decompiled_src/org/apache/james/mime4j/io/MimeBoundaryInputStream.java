package org.apache.james.mime4j.io;

import java.io.IOException;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.CharsetUtil;

public class MimeBoundaryInputStream extends LineReaderInputStream {
   private final byte[] boundary;
   private final boolean strict;
   private boolean eof;
   private int limit;
   private boolean atBoundary;
   private int boundaryLen;
   private boolean lastPart;
   private boolean completed;
   private BufferedLineReaderInputStream buffer;
   private int initialLength;

   public MimeBoundaryInputStream(BufferedLineReaderInputStream inbuffer, String boundary, boolean strict) throws IOException {
      super(inbuffer);
      int bufferSize = 2 * boundary.length();
      if (bufferSize < 4096) {
         bufferSize = 4096;
      }

      inbuffer.ensureCapacity(bufferSize);
      this.buffer = inbuffer;
      this.eof = false;
      this.limit = -1;
      this.atBoundary = false;
      this.boundaryLen = 0;
      this.lastPart = false;
      this.initialLength = -1;
      this.completed = false;
      this.strict = strict;
      this.boundary = new byte[boundary.length() + 2];
      this.boundary[0] = 45;
      this.boundary[1] = 45;

      for(int i = 0; i < boundary.length(); ++i) {
         byte ch = (byte)boundary.charAt(i);
         this.boundary[i + 2] = ch;
      }

      this.fillBuffer();
   }

   public MimeBoundaryInputStream(BufferedLineReaderInputStream inbuffer, String boundary) throws IOException {
      this(inbuffer, boundary, false);
   }

   public void close() throws IOException {
   }

   public boolean markSupported() {
      return false;
   }

   public boolean readAllowed() throws IOException {
      if (this.completed) {
         return false;
      } else if (this.endOfStream() && !this.hasData()) {
         this.skipBoundary();
         this.verifyEndOfStream();
         return false;
      } else {
         return true;
      }
   }

   public int read() throws IOException {
      while(this.readAllowed()) {
         if (this.hasData()) {
            return this.buffer.read();
         }

         this.fillBuffer();
      }

      return -1;
   }

   public int read(byte[] b, int off, int len) throws IOException {
      while(this.readAllowed()) {
         if (this.hasData()) {
            int chunk = Math.min(len, this.limit - this.buffer.pos());
            return this.buffer.read(b, off, chunk);
         }

         this.fillBuffer();
      }

      return -1;
   }

   public int readLine(ByteArrayBuffer dst) throws IOException {
      if (dst == null) {
         throw new IllegalArgumentException("Destination buffer may not be null");
      } else if (!this.readAllowed()) {
         return -1;
      } else {
         int total = 0;
         boolean found = false;
         int bytesRead = 0;

         while(!found) {
            if (!this.hasData()) {
               bytesRead = this.fillBuffer();
               if (this.endOfStream() && !this.hasData()) {
                  this.skipBoundary();
                  this.verifyEndOfStream();
                  bytesRead = -1;
                  break;
               }
            }

            int len = this.limit - this.buffer.pos();
            int i = this.buffer.indexOf((byte)10, this.buffer.pos(), len);
            int chunk;
            if (i != -1) {
               found = true;
               chunk = i + 1 - this.buffer.pos();
            } else {
               chunk = len;
            }

            if (chunk > 0) {
               dst.append(this.buffer.buf(), this.buffer.pos(), chunk);
               this.buffer.skip(chunk);
               total += chunk;
            }
         }

         return total == 0 && bytesRead == -1 ? -1 : total;
      }
   }

   private void verifyEndOfStream() throws IOException {
      if (this.strict && this.eof && !this.atBoundary) {
         throw new MimeIOException(new MimeException("Unexpected end of stream"));
      }
   }

   private boolean endOfStream() {
      return this.eof || this.atBoundary;
   }

   private boolean hasData() {
      return this.limit > this.buffer.pos() && this.limit <= this.buffer.limit();
   }

   private int fillBuffer() throws IOException {
      if (this.eof) {
         return -1;
      } else {
         int bytesRead;
         if (!this.hasData()) {
            bytesRead = this.buffer.fillBuffer();
            if (bytesRead == -1) {
               this.eof = true;
            }
         } else {
            bytesRead = 0;
         }

         int off = this.buffer.pos();

         int i;
         while(true) {
            i = this.buffer.indexOf(this.boundary, off, this.buffer.limit() - off);
            if (i == -1) {
               break;
            }

            if (i == this.buffer.pos() || this.buffer.byteAt(i - 1) == 10) {
               int pos = i + this.boundary.length;
               int remaining = this.buffer.limit() - pos;
               if (remaining <= 0) {
                  break;
               }

               char ch = (char)this.buffer.byteAt(pos);
               if (CharsetUtil.isWhitespace(ch) || ch == '-') {
                  break;
               }
            }

            off = i + this.boundary.length;
         }

         if (i != -1) {
            this.limit = i;
            this.atBoundary = true;
            this.calculateBoundaryLen();
         } else if (this.eof) {
            this.limit = this.buffer.limit();
         } else {
            this.limit = this.buffer.limit() - (this.boundary.length + 2);
         }

         return bytesRead;
      }
   }

   public boolean isEmptyStream() {
      return this.initialLength == 0;
   }

   public boolean isFullyConsumed() {
      return this.completed && !this.buffer.hasBufferedData();
   }

   private void calculateBoundaryLen() throws IOException {
      this.boundaryLen = this.boundary.length;
      int len = this.limit - this.buffer.pos();
      if (len >= 0 && this.initialLength == -1) {
         this.initialLength = len;
      }

      if (len > 0 && this.buffer.byteAt(this.limit - 1) == 10) {
         ++this.boundaryLen;
         --this.limit;
      }

      if (len > 1 && this.buffer.byteAt(this.limit - 1) == 13) {
         ++this.boundaryLen;
         --this.limit;
      }

   }

   private void skipBoundary() throws IOException {
      if (!this.completed) {
         this.completed = true;
         this.buffer.skip(this.boundaryLen);
         boolean checkForLastPart = true;

         while(true) {
            while(true) {
               while(this.buffer.length() <= 1) {
                  if (this.eof) {
                     return;
                  }

                  this.fillBuffer();
               }

               int ch1 = this.buffer.byteAt(this.buffer.pos());
               int ch2 = this.buffer.byteAt(this.buffer.pos() + 1);
               if (checkForLastPart && ch1 == 45 && ch2 == 45) {
                  this.lastPart = true;
                  this.buffer.skip(2);
                  checkForLastPart = false;
               } else {
                  if (ch1 == 13 && ch2 == 10) {
                     this.buffer.skip(2);
                     return;
                  }

                  if (ch1 == 10) {
                     this.buffer.skip(1);
                     return;
                  }

                  this.buffer.skip(1);
               }
            }
         }
      }
   }

   public boolean isLastPart() {
      return this.lastPart;
   }

   public boolean eof() {
      return this.eof && !this.buffer.hasBufferedData();
   }

   public String toString() {
      StringBuilder buffer = new StringBuilder("MimeBoundaryInputStream, boundary ");
      byte[] arr$ = this.boundary;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         byte b = arr$[i$];
         buffer.append((char)b);
      }

      return buffer.toString();
   }

   public boolean unread(ByteArrayBuffer buf) {
      return false;
   }
}
