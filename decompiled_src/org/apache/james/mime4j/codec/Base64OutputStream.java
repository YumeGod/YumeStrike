package org.apache.james.mime4j.codec;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class Base64OutputStream extends FilterOutputStream {
   private static final int DEFAULT_LINE_LENGTH = 76;
   private static final byte[] CRLF_SEPARATOR = new byte[]{13, 10};
   static final byte[] BASE64_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   private static final byte BASE64_PAD = 61;
   private static final Set BASE64_CHARS = new HashSet();
   private static final int MASK_6BITS = 63;
   private static final int ENCODED_BUFFER_SIZE = 2048;
   private final byte[] singleByte;
   private final int lineLength;
   private final byte[] lineSeparator;
   private boolean closed;
   private final byte[] encoded;
   private int position;
   private int data;
   private int modulus;
   private int linePosition;

   public Base64OutputStream(OutputStream out) {
      this(out, 76, CRLF_SEPARATOR);
   }

   public Base64OutputStream(OutputStream out, int lineLength) {
      this(out, lineLength, CRLF_SEPARATOR);
   }

   public Base64OutputStream(OutputStream out, int lineLength, byte[] lineSeparator) {
      super(out);
      this.singleByte = new byte[1];
      this.closed = false;
      this.position = 0;
      this.data = 0;
      this.modulus = 0;
      this.linePosition = 0;
      if (out == null) {
         throw new IllegalArgumentException();
      } else if (lineLength < 0) {
         throw new IllegalArgumentException();
      } else {
         this.checkLineSeparator(lineSeparator);
         this.lineLength = lineLength;
         this.lineSeparator = new byte[lineSeparator.length];
         System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
         this.encoded = new byte[2048];
      }
   }

   public final void write(int b) throws IOException {
      if (this.closed) {
         throw new IOException("Base64OutputStream has been closed");
      } else {
         this.singleByte[0] = (byte)b;
         this.write0(this.singleByte, 0, 1);
      }
   }

   public final void write(byte[] buffer) throws IOException {
      if (this.closed) {
         throw new IOException("Base64OutputStream has been closed");
      } else if (buffer == null) {
         throw new NullPointerException();
      } else if (buffer.length != 0) {
         this.write0(buffer, 0, buffer.length);
      }
   }

   public final void write(byte[] buffer, int offset, int length) throws IOException {
      if (this.closed) {
         throw new IOException("Base64OutputStream has been closed");
      } else if (buffer == null) {
         throw new NullPointerException();
      } else if (offset >= 0 && length >= 0 && offset + length <= buffer.length) {
         if (length != 0) {
            this.write0(buffer, offset, offset + length);
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void flush() throws IOException {
      if (this.closed) {
         throw new IOException("Base64OutputStream has been closed");
      } else {
         this.flush0();
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.close0();
      }
   }

   private void write0(byte[] buffer, int from, int to) throws IOException {
      for(int i = from; i < to; ++i) {
         this.data = this.data << 8 | buffer[i] & 255;
         if (++this.modulus == 3) {
            this.modulus = 0;
            if (this.lineLength > 0 && this.linePosition >= this.lineLength) {
               this.linePosition = 0;
               if (this.encoded.length - this.position < this.lineSeparator.length) {
                  this.flush0();
               }

               byte[] arr$ = this.lineSeparator;
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  byte ls = arr$[i$];
                  this.encoded[this.position++] = ls;
               }
            }

            if (this.encoded.length - this.position < 4) {
               this.flush0();
            }

            this.encoded[this.position++] = BASE64_TABLE[this.data >> 18 & 63];
            this.encoded[this.position++] = BASE64_TABLE[this.data >> 12 & 63];
            this.encoded[this.position++] = BASE64_TABLE[this.data >> 6 & 63];
            this.encoded[this.position++] = BASE64_TABLE[this.data & 63];
            this.linePosition += 4;
         }
      }

   }

   private void flush0() throws IOException {
      if (this.position > 0) {
         this.out.write(this.encoded, 0, this.position);
         this.position = 0;
      }

   }

   private void close0() throws IOException {
      if (this.modulus != 0) {
         this.writePad();
      }

      if (this.lineLength > 0 && this.linePosition > 0) {
         this.writeLineSeparator();
      }

      this.flush0();
   }

   private void writePad() throws IOException {
      if (this.lineLength > 0 && this.linePosition >= this.lineLength) {
         this.writeLineSeparator();
      }

      if (this.encoded.length - this.position < 4) {
         this.flush0();
      }

      if (this.modulus == 1) {
         this.encoded[this.position++] = BASE64_TABLE[this.data >> 2 & 63];
         this.encoded[this.position++] = BASE64_TABLE[this.data << 4 & 63];
         this.encoded[this.position++] = 61;
         this.encoded[this.position++] = 61;
      } else {
         assert this.modulus == 2;

         this.encoded[this.position++] = BASE64_TABLE[this.data >> 10 & 63];
         this.encoded[this.position++] = BASE64_TABLE[this.data >> 4 & 63];
         this.encoded[this.position++] = BASE64_TABLE[this.data << 2 & 63];
         this.encoded[this.position++] = 61;
      }

      this.linePosition += 4;
   }

   private void writeLineSeparator() throws IOException {
      this.linePosition = 0;
      if (this.encoded.length - this.position < this.lineSeparator.length) {
         this.flush0();
      }

      byte[] arr$ = this.lineSeparator;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         byte ls = arr$[i$];
         this.encoded[this.position++] = ls;
      }

   }

   private void checkLineSeparator(byte[] lineSeparator) {
      if (lineSeparator.length > 2048) {
         throw new IllegalArgumentException("line separator length exceeds 2048");
      } else {
         byte[] arr$ = lineSeparator;
         int len$ = lineSeparator.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte b = arr$[i$];
            if (BASE64_CHARS.contains(b)) {
               throw new IllegalArgumentException("line separator must not contain base64 character '" + (char)(b & 255) + "'");
            }
         }

      }
   }

   static {
      byte[] arr$ = BASE64_TABLE;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         byte b = arr$[i$];
         BASE64_CHARS.add(b);
      }

      BASE64_CHARS.add((byte)61);
   }
}
