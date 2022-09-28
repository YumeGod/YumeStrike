package com.mxgraph.util.png;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class IDATOutputStream extends FilterOutputStream {
   private static final byte[] typeSignature = new byte[]{73, 68, 65, 84};
   private int bytesWritten = 0;
   private int segmentLength;
   byte[] buffer;

   public IDATOutputStream(OutputStream var1, int var2) {
      super(var1);
      this.segmentLength = var2;
      this.buffer = new byte[var2];
   }

   public void close() throws IOException {
      this.flush();
   }

   private void writeInt(int var1) throws IOException {
      this.out.write(var1 >> 24);
      this.out.write(var1 >> 16 & 255);
      this.out.write(var1 >> 8 & 255);
      this.out.write(var1 & 255);
   }

   public void flush() throws IOException {
      this.writeInt(this.bytesWritten);
      this.out.write(typeSignature);
      this.out.write(this.buffer, 0, this.bytesWritten);
      int var1 = -1;
      var1 = CRC.updateCRC(var1, typeSignature, 0, 4);
      var1 = CRC.updateCRC(var1, this.buffer, 0, this.bytesWritten);
      this.writeInt(~var1);
      this.bytesWritten = 0;
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      while(var3 > 0) {
         int var4 = Math.min(this.segmentLength - this.bytesWritten, var3);
         System.arraycopy(var1, var2, this.buffer, this.bytesWritten, var4);
         var2 += var4;
         var3 -= var4;
         this.bytesWritten += var4;
         if (this.bytesWritten == this.segmentLength) {
            this.flush();
         }
      }

   }

   public void write(int var1) throws IOException {
      this.buffer[this.bytesWritten++] = (byte)var1;
      if (this.bytesWritten == this.segmentLength) {
         this.flush();
      }

   }
}
