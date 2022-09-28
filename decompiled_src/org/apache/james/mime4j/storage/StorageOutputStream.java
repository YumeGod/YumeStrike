package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.OutputStream;

public abstract class StorageOutputStream extends OutputStream {
   private byte[] singleByte;
   private boolean closed;
   private boolean usedUp;

   protected StorageOutputStream() {
   }

   public final Storage toStorage() throws IOException {
      if (this.usedUp) {
         throw new IllegalStateException("toStorage may be invoked only once");
      } else {
         if (!this.closed) {
            this.close();
         }

         this.usedUp = true;
         return this.toStorage0();
      }
   }

   public final void write(int b) throws IOException {
      if (this.closed) {
         throw new IOException("StorageOutputStream has been closed");
      } else {
         if (this.singleByte == null) {
            this.singleByte = new byte[1];
         }

         this.singleByte[0] = (byte)b;
         this.write0(this.singleByte, 0, 1);
      }
   }

   public final void write(byte[] buffer) throws IOException {
      if (this.closed) {
         throw new IOException("StorageOutputStream has been closed");
      } else if (buffer == null) {
         throw new NullPointerException();
      } else if (buffer.length != 0) {
         this.write0(buffer, 0, buffer.length);
      }
   }

   public final void write(byte[] buffer, int offset, int length) throws IOException {
      if (this.closed) {
         throw new IOException("StorageOutputStream has been closed");
      } else if (buffer == null) {
         throw new NullPointerException();
      } else if (offset >= 0 && length >= 0 && offset + length <= buffer.length) {
         if (length != 0) {
            this.write0(buffer, offset, length);
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void close() throws IOException {
      this.closed = true;
   }

   protected abstract void write0(byte[] var1, int var2, int var3) throws IOException;

   protected abstract Storage toStorage0() throws IOException;
}
