package org.apache.james.mime4j.codec;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class Base64InputStream extends InputStream {
   private static final int ENCODED_BUFFER_SIZE = 1536;
   private static final int[] BASE64_DECODE = new int[256];
   private static final byte BASE64_PAD = 61;
   private static final int EOF = -1;
   private final byte[] singleByte;
   private final InputStream in;
   private final byte[] encoded;
   private final ByteArrayBuffer decodedBuf;
   private int position;
   private int size;
   private boolean closed;
   private boolean eof;
   private final DecodeMonitor monitor;

   public Base64InputStream(InputStream in, DecodeMonitor monitor) {
      this(1536, in, monitor);
   }

   protected Base64InputStream(int bufsize, InputStream in, DecodeMonitor monitor) {
      this.singleByte = new byte[1];
      this.position = 0;
      this.size = 0;
      this.closed = false;
      if (in == null) {
         throw new IllegalArgumentException();
      } else {
         this.encoded = new byte[bufsize];
         this.decodedBuf = new ByteArrayBuffer(512);
         this.in = in;
         this.monitor = monitor;
      }
   }

   public Base64InputStream(InputStream in) {
      this(in, false);
   }

   public Base64InputStream(InputStream in, boolean strict) {
      this(1536, in, strict ? DecodeMonitor.STRICT : DecodeMonitor.SILENT);
   }

   public int read() throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been closed");
      } else {
         int bytes;
         do {
            bytes = this.read0(this.singleByte, 0, 1);
            if (bytes == -1) {
               return -1;
            }
         } while(bytes != 1);

         return this.singleByte[0] & 255;
      }
   }

   public int read(byte[] buffer) throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been closed");
      } else if (buffer == null) {
         throw new NullPointerException();
      } else {
         return buffer.length == 0 ? 0 : this.read0(buffer, 0, buffer.length);
      }
   }

   public int read(byte[] buffer, int offset, int length) throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been closed");
      } else if (buffer == null) {
         throw new NullPointerException();
      } else if (offset >= 0 && length >= 0 && offset + length <= buffer.length) {
         return length == 0 ? 0 : this.read0(buffer, offset, length);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
      }
   }

   private int read0(byte[] buffer, int off, int len) throws IOException {
      int to = off + len;
      int index = off;
      int data;
      if (this.decodedBuf.length() > 0) {
         data = Math.min(this.decodedBuf.length(), len);
         System.arraycopy(this.decodedBuf.buffer(), 0, buffer, off, data);
         this.decodedBuf.remove(0, data);
         index = off + data;
      }

      if (this.eof) {
         return index == off ? -1 : index - off;
      } else {
         data = 0;
         int sextets = 0;

         while(index < to) {
            int value;
            while(this.position == this.size) {
               value = this.in.read(this.encoded, 0, this.encoded.length);
               if (value == -1) {
                  this.eof = true;
                  if (sextets != 0) {
                     this.handleUnexpectedEof(sextets);
                  }

                  return index == off ? -1 : index - off;
               }

               if (value > 0) {
                  this.position = 0;
                  this.size = value;
               } else {
                  assert value == 0;
               }
            }

            while(this.position < this.size && index < to) {
               value = this.encoded[this.position++] & 255;
               if (value == 61) {
                  index = this.decodePad(data, sextets, buffer, index, to);
                  return index - off;
               }

               int decoded = BASE64_DECODE[value];
               if (decoded < 0) {
                  if (value != 13 && value != 10 && value != 32 && this.monitor.warn("Unexpected base64 byte: " + (byte)value, "ignoring.")) {
                     throw new IOException("Unexpected base64 byte");
                  }
               } else {
                  data = data << 6 | decoded;
                  ++sextets;
                  if (sextets == 4) {
                     sextets = 0;
                     byte b1 = (byte)(data >>> 16);
                     byte b2 = (byte)(data >>> 8);
                     byte b3 = (byte)data;
                     if (index >= to - 2) {
                        if (index < to - 1) {
                           buffer[index++] = b1;
                           buffer[index++] = b2;
                           this.decodedBuf.append(b3);
                        } else if (index < to) {
                           buffer[index++] = b1;
                           this.decodedBuf.append(b2);
                           this.decodedBuf.append(b3);
                        } else {
                           this.decodedBuf.append(b1);
                           this.decodedBuf.append(b2);
                           this.decodedBuf.append(b3);
                        }

                        assert index == to;

                        return to - off;
                     }

                     buffer[index++] = b1;
                     buffer[index++] = b2;
                     buffer[index++] = b3;
                  }
               }
            }
         }

         assert sextets == 0;

         assert index == to;

         return to - off;
      }
   }

   private int decodePad(int data, int sextets, byte[] buffer, int index, int end) throws IOException {
      this.eof = true;
      byte b1;
      if (sextets == 2) {
         b1 = (byte)(data >>> 4);
         if (index < end) {
            buffer[index++] = b1;
         } else {
            this.decodedBuf.append(b1);
         }
      } else if (sextets == 3) {
         b1 = (byte)(data >>> 10);
         byte b2 = (byte)(data >>> 2 & 255);
         if (index < end - 1) {
            buffer[index++] = b1;
            buffer[index++] = b2;
         } else if (index < end) {
            buffer[index++] = b1;
            this.decodedBuf.append(b2);
         } else {
            this.decodedBuf.append(b1);
            this.decodedBuf.append(b2);
         }
      } else {
         this.handleUnexpecedPad(sextets);
      }

      return index;
   }

   private void handleUnexpectedEof(int sextets) throws IOException {
      if (this.monitor.warn("Unexpected end of BASE64 stream", "dropping " + sextets + " sextet(s)")) {
         throw new IOException("Unexpected end of BASE64 stream");
      }
   }

   private void handleUnexpecedPad(int sextets) throws IOException {
      if (this.monitor.warn("Unexpected padding character", "dropping " + sextets + " sextet(s)")) {
         throw new IOException("Unexpected padding character");
      }
   }

   static {
      int i;
      for(i = 0; i < 256; ++i) {
         BASE64_DECODE[i] = -1;
      }

      for(i = 0; i < Base64OutputStream.BASE64_TABLE.length; BASE64_DECODE[Base64OutputStream.BASE64_TABLE[i] & 255] = i++) {
      }

   }
}
