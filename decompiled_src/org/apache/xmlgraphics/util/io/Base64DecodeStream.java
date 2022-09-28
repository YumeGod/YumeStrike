package org.apache.xmlgraphics.util.io;

import java.io.IOException;
import java.io.InputStream;

public class Base64DecodeStream extends InputStream {
   InputStream src;
   private static final byte[] pem_array = new byte[256];
   byte[] decode_buffer = new byte[4];
   byte[] out_buffer = new byte[3];
   int out_offset = 3;
   boolean EOF = false;

   public Base64DecodeStream(InputStream src) {
      this.src = src;
   }

   public boolean markSupported() {
      return false;
   }

   public void close() throws IOException {
      this.EOF = true;
   }

   public int available() throws IOException {
      return 3 - this.out_offset;
   }

   public int read() throws IOException {
      if (this.out_offset != 3 || !this.EOF && !this.getNextAtom()) {
         return this.out_buffer[this.out_offset++] & 255;
      } else {
         this.EOF = true;
         return -1;
      }
   }

   public int read(byte[] out, int offset, int len) throws IOException {
      int idx;
      for(idx = 0; idx < len; ++idx) {
         if (this.out_offset == 3 && (this.EOF || this.getNextAtom())) {
            this.EOF = true;
            return idx == 0 ? -1 : idx;
         }

         out[offset + idx] = this.out_buffer[this.out_offset++];
      }

      return idx;
   }

   final boolean getNextAtom() throws IOException {
      int out;
      for(int off = 0; off != 4; off = out) {
         int count = this.src.read(this.decode_buffer, off, 4 - off);
         if (count == -1) {
            return true;
         }

         int in = off;

         for(out = off; in < off + count; ++in) {
            if (this.decode_buffer[in] != 10 && this.decode_buffer[in] != 13 && this.decode_buffer[in] != 32) {
               this.decode_buffer[out++] = this.decode_buffer[in];
            }
         }
      }

      int a = pem_array[this.decode_buffer[0] & 255];
      int b = pem_array[this.decode_buffer[1] & 255];
      int c = pem_array[this.decode_buffer[2] & 255];
      int d = pem_array[this.decode_buffer[3] & 255];
      this.out_buffer[0] = (byte)(a << 2 | b >>> 4);
      this.out_buffer[1] = (byte)(b << 4 | c >>> 2);
      this.out_buffer[2] = (byte)(c << 6 | d);
      if (this.decode_buffer[3] != 61) {
         this.out_offset = 0;
      } else if (this.decode_buffer[2] == 61) {
         this.out_buffer[2] = this.out_buffer[0];
         this.out_offset = 2;
         this.EOF = true;
      } else {
         this.out_buffer[2] = this.out_buffer[1];
         this.out_buffer[1] = this.out_buffer[0];
         this.out_offset = 1;
         this.EOF = true;
      }

      return false;
   }

   static {
      int idx;
      for(idx = 0; idx < pem_array.length; ++idx) {
         pem_array[idx] = -1;
      }

      idx = 0;

      char c;
      for(c = 'A'; c <= 'Z'; ++c) {
         pem_array[c] = (byte)(idx++);
      }

      for(c = 'a'; c <= 'z'; ++c) {
         pem_array[c] = (byte)(idx++);
      }

      for(c = '0'; c <= '9'; ++c) {
         pem_array[c] = (byte)(idx++);
      }

      pem_array[43] = (byte)(idx++);
      pem_array[47] = (byte)(idx++);
   }
}
