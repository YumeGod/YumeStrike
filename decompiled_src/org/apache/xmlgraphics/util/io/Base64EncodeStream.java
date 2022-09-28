package org.apache.xmlgraphics.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Base64EncodeStream extends OutputStream {
   private static final byte[] pem_array = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   byte[] atom = new byte[3];
   int atomLen = 0;
   byte[] encodeBuf = new byte[4];
   int lineLen = 0;
   PrintStream out;
   boolean closeOutOnClose;

   public Base64EncodeStream(OutputStream out) {
      this.out = new PrintStream(out);
      this.closeOutOnClose = true;
   }

   public Base64EncodeStream(OutputStream out, boolean closeOutOnClose) {
      this.out = new PrintStream(out);
      this.closeOutOnClose = closeOutOnClose;
   }

   public void close() throws IOException {
      if (this.out != null) {
         this.encodeAtom();
         this.out.flush();
         if (this.closeOutOnClose) {
            this.out.close();
         }

         this.out = null;
      }

   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void write(int b) throws IOException {
      this.atom[this.atomLen++] = (byte)b;
      if (this.atomLen == 3) {
         this.encodeAtom();
      }

   }

   public void write(byte[] data) throws IOException {
      this.encodeFromArray(data, 0, data.length);
   }

   public void write(byte[] data, int off, int len) throws IOException {
      this.encodeFromArray(data, off, len);
   }

   void encodeAtom() throws IOException {
      byte a;
      byte b;
      switch (this.atomLen) {
         case 0:
            return;
         case 1:
            a = this.atom[0];
            this.encodeBuf[0] = pem_array[a >>> 2 & 63];
            this.encodeBuf[1] = pem_array[a << 4 & 48];
            this.encodeBuf[2] = this.encodeBuf[3] = 61;
            break;
         case 2:
            a = this.atom[0];
            b = this.atom[1];
            this.encodeBuf[0] = pem_array[a >>> 2 & 63];
            this.encodeBuf[1] = pem_array[a << 4 & 48 | b >>> 4 & 15];
            this.encodeBuf[2] = pem_array[b << 2 & 60];
            this.encodeBuf[3] = 61;
            break;
         default:
            a = this.atom[0];
            b = this.atom[1];
            byte c = this.atom[2];
            this.encodeBuf[0] = pem_array[a >>> 2 & 63];
            this.encodeBuf[1] = pem_array[a << 4 & 48 | b >>> 4 & 15];
            this.encodeBuf[2] = pem_array[b << 2 & 60 | c >>> 6 & 3];
            this.encodeBuf[3] = pem_array[c & 63];
      }

      if (this.lineLen == 64) {
         this.out.println();
         this.lineLen = 0;
      }

      this.out.write(this.encodeBuf);
      this.lineLen += 4;
      this.atomLen = 0;
   }

   void encodeFromArray(byte[] data, int offset, int len) throws IOException {
      if (len != 0) {
         if (this.atomLen != 0) {
            switch (this.atomLen) {
               case 1:
                  this.atom[1] = data[offset++];
                  --len;
                  ++this.atomLen;
                  if (len == 0) {
                     return;
                  }

                  this.atom[2] = data[offset++];
                  --len;
                  ++this.atomLen;
                  break;
               case 2:
                  this.atom[2] = data[offset++];
                  --len;
                  ++this.atomLen;
            }

            this.encodeAtom();
         }

         for(; len >= 3; len -= 3) {
            byte a = data[offset++];
            byte b = data[offset++];
            byte c = data[offset++];
            this.encodeBuf[0] = pem_array[a >>> 2 & 63];
            this.encodeBuf[1] = pem_array[a << 4 & 48 | b >>> 4 & 15];
            this.encodeBuf[2] = pem_array[b << 2 & 60 | c >>> 6 & 3];
            this.encodeBuf[3] = pem_array[c & 63];
            this.out.write(this.encodeBuf);
            this.lineLen += 4;
            if (this.lineLen == 64) {
               this.out.println();
               this.lineLen = 0;
            }
         }

         switch (len) {
            case 1:
               this.atom[0] = data[offset];
               break;
            case 2:
               this.atom[0] = data[offset];
               this.atom[1] = data[offset + 1];
         }

         this.atomLen = len;
      }
   }
}
