package org.apache.xmlgraphics.image.codec.png;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class IDATOutputStream extends FilterOutputStream {
   private static final byte[] typeSignature = new byte[]{73, 68, 65, 84};
   private int bytesWritten = 0;
   private int segmentLength;
   byte[] buffer;

   public IDATOutputStream(OutputStream output, int segmentLength) {
      super(output);
      this.segmentLength = segmentLength;
      this.buffer = new byte[segmentLength];
   }

   public void close() throws IOException {
      this.flush();
   }

   private void writeInt(int x) throws IOException {
      this.out.write(x >> 24);
      this.out.write(x >> 16 & 255);
      this.out.write(x >> 8 & 255);
      this.out.write(x & 255);
   }

   public void flush() throws IOException {
      this.writeInt(this.bytesWritten);
      this.out.write(typeSignature);
      this.out.write(this.buffer, 0, this.bytesWritten);
      int crc = -1;
      crc = CRC.updateCRC(crc, typeSignature, 0, 4);
      crc = CRC.updateCRC(crc, this.buffer, 0, this.bytesWritten);
      this.writeInt(~crc);
      this.bytesWritten = 0;
   }

   public void write(byte[] b) throws IOException {
      this.write(b, 0, b.length);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      while(len > 0) {
         int bytes = Math.min(this.segmentLength - this.bytesWritten, len);
         System.arraycopy(b, off, this.buffer, this.bytesWritten, bytes);
         off += bytes;
         len -= bytes;
         this.bytesWritten += bytes;
         if (this.bytesWritten == this.segmentLength) {
            this.flush();
         }
      }

   }

   public void write(int b) throws IOException {
      this.buffer[this.bytesWritten++] = (byte)b;
      if (this.bytesWritten == this.segmentLength) {
         this.flush();
      }

   }
}
