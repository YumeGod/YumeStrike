package org.apache.batik.ext.awt.image.codec.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public abstract class SeekableStream extends InputStream implements DataInput {
   protected long markPos = -1L;
   private byte[] ruileBuf = new byte[4];

   public static SeekableStream wrapInputStream(InputStream var0, boolean var1) {
      Object var2 = null;
      if (var1) {
         try {
            var2 = new FileCacheSeekableStream(var0);
         } catch (Exception var4) {
            var2 = new MemoryCacheSeekableStream(var0);
         }
      } else {
         var2 = new ForwardSeekableStream(var0);
      }

      return (SeekableStream)var2;
   }

   public abstract int read() throws IOException;

   public abstract int read(byte[] var1, int var2, int var3) throws IOException;

   public synchronized void mark(int var1) {
      try {
         this.markPos = this.getFilePointer();
      } catch (IOException var3) {
         this.markPos = -1L;
      }

   }

   public synchronized void reset() throws IOException {
      if (this.markPos != -1L) {
         this.seek(this.markPos);
      }

   }

   public boolean markSupported() {
      return this.canSeekBackwards();
   }

   public boolean canSeekBackwards() {
      return false;
   }

   public abstract long getFilePointer() throws IOException;

   public abstract void seek(long var1) throws IOException;

   public final void readFully(byte[] var1) throws IOException {
      this.readFully(var1, 0, var1.length);
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 0;

      do {
         int var5 = this.read(var1, var2 + var4, var3 - var4);
         if (var5 < 0) {
            throw new EOFException();
         }

         var4 += var5;
      } while(var4 < var3);

   }

   public int skipBytes(int var1) throws IOException {
      return var1 <= 0 ? 0 : (int)this.skip((long)var1);
   }

   public final boolean readBoolean() throws IOException {
      int var1 = this.read();
      if (var1 < 0) {
         throw new EOFException();
      } else {
         return var1 != 0;
      }
   }

   public final byte readByte() throws IOException {
      int var1 = this.read();
      if (var1 < 0) {
         throw new EOFException();
      } else {
         return (byte)var1;
      }
   }

   public final int readUnsignedByte() throws IOException {
      int var1 = this.read();
      if (var1 < 0) {
         throw new EOFException();
      } else {
         return var1;
      }
   }

   public final short readShort() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (short)((var1 << 8) + (var2 << 0));
      }
   }

   public final short readShortLE() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (short)((var2 << 8) + (var1 << 0));
      }
   }

   public final int readUnsignedShort() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 8) + (var2 << 0);
      }
   }

   public final int readUnsignedShortLE() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (var2 << 8) + (var1 << 0);
      }
   }

   public final char readChar() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (char)((var1 << 8) + (var2 << 0));
      }
   }

   public final char readCharLE() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (char)((var2 << 8) + (var1 << 0));
      }
   }

   public final int readInt() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      int var3 = this.read();
      int var4 = this.read();
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 24) + (var2 << 16) + (var3 << 8) + (var4 << 0);
      }
   }

   public final int readIntLE() throws IOException {
      int var1 = this.read();
      int var2 = this.read();
      int var3 = this.read();
      int var4 = this.read();
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var4 << 24) + (var3 << 16) + (var2 << 8) + (var1 << 0);
      }
   }

   public final long readUnsignedInt() throws IOException {
      long var1 = (long)this.read();
      long var3 = (long)this.read();
      long var5 = (long)this.read();
      long var7 = (long)this.read();
      if ((var1 | var3 | var5 | var7) < 0L) {
         throw new EOFException();
      } else {
         return (var1 << 24) + (var3 << 16) + (var5 << 8) + (var7 << 0);
      }
   }

   public final long readUnsignedIntLE() throws IOException {
      this.readFully(this.ruileBuf);
      long var1 = (long)(this.ruileBuf[0] & 255);
      long var3 = (long)(this.ruileBuf[1] & 255);
      long var5 = (long)(this.ruileBuf[2] & 255);
      long var7 = (long)(this.ruileBuf[3] & 255);
      return (var7 << 24) + (var5 << 16) + (var3 << 8) + (var1 << 0);
   }

   public final long readLong() throws IOException {
      return ((long)this.readInt() << 32) + ((long)this.readInt() & 4294967295L);
   }

   public final long readLongLE() throws IOException {
      int var1 = this.readIntLE();
      int var2 = this.readIntLE();
      return ((long)var2 << 32) + ((long)var1 & 4294967295L);
   }

   public final float readFloat() throws IOException {
      return Float.intBitsToFloat(this.readInt());
   }

   public final float readFloatLE() throws IOException {
      return Float.intBitsToFloat(this.readIntLE());
   }

   public final double readDouble() throws IOException {
      return Double.longBitsToDouble(this.readLong());
   }

   public final double readDoubleLE() throws IOException {
      return Double.longBitsToDouble(this.readLongLE());
   }

   public final String readLine() throws IOException {
      StringBuffer var1 = new StringBuffer();
      int var2 = -1;
      boolean var3 = false;

      while(!var3) {
         switch (var2 = this.read()) {
            case -1:
            case 10:
               var3 = true;
               break;
            case 13:
               var3 = true;
               long var4 = this.getFilePointer();
               if (this.read() != 10) {
                  this.seek(var4);
               }
               break;
            default:
               var1.append((char)var2);
         }
      }

      if (var2 == -1 && var1.length() == 0) {
         return null;
      } else {
         return var1.toString();
      }
   }

   public final String readUTF() throws IOException {
      return DataInputStream.readUTF(this);
   }

   protected void finalize() throws Throwable {
      super.finalize();
      this.close();
   }
}
