package org.apache.james.mime4j.codec;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class QuotedPrintableInputStream extends InputStream {
   private static final int DEFAULT_BUFFER_SIZE = 2048;
   private static final byte EQ = 61;
   private static final byte CR = 13;
   private static final byte LF = 10;
   private final byte[] singleByte;
   private final InputStream in;
   private final ByteArrayBuffer decodedBuf;
   private final ByteArrayBuffer blanks;
   private final byte[] encoded;
   private int pos;
   private int limit;
   private boolean closed;
   private final DecodeMonitor monitor;

   public QuotedPrintableInputStream(InputStream in, DecodeMonitor monitor) {
      this(2048, in, monitor);
   }

   protected QuotedPrintableInputStream(int bufsize, InputStream in, DecodeMonitor monitor) {
      this.singleByte = new byte[1];
      this.pos = 0;
      this.limit = 0;
      this.in = in;
      this.encoded = new byte[bufsize];
      this.decodedBuf = new ByteArrayBuffer(512);
      this.blanks = new ByteArrayBuffer(512);
      this.closed = false;
      this.monitor = monitor;
   }

   protected QuotedPrintableInputStream(int bufsize, InputStream in, boolean strict) {
      this(bufsize, in, strict ? DecodeMonitor.STRICT : DecodeMonitor.SILENT);
   }

   public QuotedPrintableInputStream(InputStream in, boolean strict) {
      this(2048, in, strict);
   }

   public QuotedPrintableInputStream(InputStream in) {
      this(in, false);
   }

   public void close() throws IOException {
      this.closed = true;
   }

   private int fillBuffer() throws IOException {
      if (this.pos < this.limit) {
         System.arraycopy(this.encoded, this.pos, this.encoded, 0, this.limit - this.pos);
         this.limit -= this.pos;
         this.pos = 0;
      } else {
         this.limit = 0;
         this.pos = 0;
      }

      int capacity = this.encoded.length - this.limit;
      if (capacity > 0) {
         int bytesRead = this.in.read(this.encoded, this.limit, capacity);
         if (bytesRead > 0) {
            this.limit += bytesRead;
         }

         return bytesRead;
      } else {
         return 0;
      }
   }

   private int getnext() {
      if (this.pos < this.limit) {
         byte b = this.encoded[this.pos];
         ++this.pos;
         return b & 255;
      } else {
         return -1;
      }
   }

   private int peek(int i) {
      return this.pos + i < this.limit ? this.encoded[this.pos + i] & 255 : -1;
   }

   private int transfer(int b, byte[] buffer, int from, int to, boolean keepblanks) throws IOException {
      int index = from;
      int i;
      if (keepblanks && this.blanks.length() > 0) {
         int chunk = Math.min(this.blanks.length(), to - from);
         System.arraycopy(this.blanks.buffer(), 0, buffer, from, chunk);
         index = from + chunk;
         i = this.blanks.length() - chunk;
         if (i > 0) {
            this.decodedBuf.append(this.blanks.buffer(), chunk, i);
         }

         this.blanks.clear();
      } else if (this.blanks.length() > 0 && !keepblanks) {
         StringBuilder sb = new StringBuilder(this.blanks.length() * 3);

         for(i = 0; i < this.blanks.length(); ++i) {
            sb.append(" " + this.blanks.byteAt(i));
         }

         if (this.monitor.warn("ignored blanks", sb.toString())) {
            throw new IOException("ignored blanks");
         }
      }

      if (b != -1) {
         if (index < to) {
            buffer[index++] = (byte)b;
         } else {
            this.decodedBuf.append(b);
         }
      }

      return index;
   }

   private int read0(byte[] buffer, int off, int len) throws IOException {
      boolean eof = false;
      int to = off + len;
      int index = off;
      int bytesRead;
      if (this.decodedBuf.length() > 0) {
         bytesRead = Math.min(this.decodedBuf.length(), to - off);
         System.arraycopy(this.decodedBuf.buffer(), 0, buffer, off, bytesRead);
         this.decodedBuf.remove(0, bytesRead);
         index = off + bytesRead;
      }

      while(true) {
         label124:
         while(index < to) {
            if (this.limit - this.pos < 3) {
               bytesRead = this.fillBuffer();
               eof = bytesRead == -1;
            }

            if (this.limit - this.pos == 0 && eof) {
               return index == off ? -1 : index - off;
            }

            boolean lastWasCR = false;

            while(true) {
               while(true) {
                  if (this.pos >= this.limit || index >= to) {
                     continue label124;
                  }

                  int b = this.encoded[this.pos++] & 255;
                  if (lastWasCR && b != 10) {
                     if (this.monitor.warn("Found CR without LF", "Leaving it as is")) {
                        throw new IOException("Found CR without LF");
                     }

                     index = this.transfer(13, buffer, index, to, false);
                  } else if (!lastWasCR && b == 10 && this.monitor.warn("Found LF without CR", "Translating to CRLF")) {
                     throw new IOException("Found LF without CR");
                  }

                  if (b == 13) {
                     lastWasCR = true;
                  } else {
                     lastWasCR = false;
                     if (b == 10) {
                        if (this.blanks.length() == 0) {
                           index = this.transfer(13, buffer, index, to, false);
                           index = this.transfer(10, buffer, index, to, false);
                        } else if (this.blanks.byteAt(0) != 61) {
                           index = this.transfer(13, buffer, index, to, false);
                           index = this.transfer(10, buffer, index, to, false);
                        }

                        this.blanks.clear();
                     } else if (b == 61) {
                        if (this.limit - this.pos < 2 && !eof) {
                           --this.pos;
                           continue label124;
                        }

                        int b2 = this.getnext();
                        int b3;
                        int upper;
                        if (b2 != 61) {
                           if (Character.isWhitespace((char)b2)) {
                              index = this.transfer(-1, buffer, index, to, true);
                              if (b2 != 10) {
                                 this.blanks.append(b);
                                 this.blanks.append(b2);
                              }
                           } else {
                              b3 = this.getnext();
                              upper = this.convert(b2);
                              int lower = this.convert(b3);
                              if (upper >= 0 && lower >= 0) {
                                 index = this.transfer(upper << 4 | lower, buffer, index, to, true);
                              } else {
                                 this.monitor.warn("Malformed encoded value encountered", "leaving =" + (char)b2 + (char)b3 + " as is");
                                 index = this.transfer(61, buffer, index, to, true);
                                 index = this.transfer(b2, buffer, index, to, false);
                                 index = this.transfer(b3, buffer, index, to, false);
                              }
                           }
                        } else {
                           index = this.transfer(b2, buffer, index, to, true);
                           b3 = this.peek(0);
                           upper = this.peek(1);
                           if (b3 != 10 && (b3 != 13 || upper != 10)) {
                              this.monitor.warn("Unexpected == encountered", "==");
                           } else {
                              this.monitor.warn("Unexpected ==EOL encountered", "== 0x" + b3 + " 0x" + upper);
                              this.blanks.append(b2);
                           }
                        }
                     } else if (Character.isWhitespace(b)) {
                        this.blanks.append(b);
                     } else {
                        index = this.transfer(b & 255, buffer, index, to, true);
                     }
                  }
               }
            }
         }

         return to - off;
      }
   }

   private int convert(int c) {
      if (c >= 48 && c <= 57) {
         return c - 48;
      } else if (c >= 65 && c <= 70) {
         return 10 + (c - 65);
      } else {
         return c >= 97 && c <= 102 ? 10 + (c - 97) : -1;
      }
   }

   public int read() throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been closed");
      } else {
         int bytes;
         do {
            bytes = this.read(this.singleByte, 0, 1);
            if (bytes == -1) {
               return -1;
            }
         } while(bytes != 1);

         return this.singleByte[0] & 255;
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been closed");
      } else {
         return this.read0(b, off, len);
      }
   }
}
