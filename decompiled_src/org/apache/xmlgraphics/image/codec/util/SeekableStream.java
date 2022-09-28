package org.apache.xmlgraphics.image.codec.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public abstract class SeekableStream extends InputStream implements DataInput {
   protected long markPos = -1L;
   private byte[] ruileBuf = new byte[4];

   public static SeekableStream wrapInputStream(InputStream is, boolean canSeekBackwards) {
      SeekableStream stream = null;
      if (canSeekBackwards) {
         try {
            stream = new FileCacheSeekableStream(is);
         } catch (Exception var4) {
            stream = new MemoryCacheSeekableStream(is);
         }
      } else {
         stream = new ForwardSeekableStream(is);
      }

      return (SeekableStream)stream;
   }

   public abstract int read() throws IOException;

   public abstract int read(byte[] var1, int var2, int var3) throws IOException;

   public synchronized void mark(int readLimit) {
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

   public final void readFully(byte[] b) throws IOException {
      this.readFully(b, 0, b.length);
   }

   public final void readFully(byte[] b, int off, int len) throws IOException {
      int n = 0;

      do {
         int count = this.read(b, off + n, len - n);
         if (count < 0) {
            throw new EOFException();
         }

         n += count;
      } while(n < len);

   }

   public int skipBytes(int n) throws IOException {
      return n <= 0 ? 0 : (int)this.skip((long)n);
   }

   public final boolean readBoolean() throws IOException {
      int ch = this.read();
      if (ch < 0) {
         throw new EOFException();
      } else {
         return ch != 0;
      }
   }

   public final byte readByte() throws IOException {
      int ch = this.read();
      if (ch < 0) {
         throw new EOFException();
      } else {
         return (byte)ch;
      }
   }

   public final int readUnsignedByte() throws IOException {
      int ch = this.read();
      if (ch < 0) {
         throw new EOFException();
      } else {
         return ch;
      }
   }

   public final short readShort() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      if ((ch1 | ch2) < 0) {
         throw new EOFException();
      } else {
         return (short)((ch1 << 8) + (ch2 << 0));
      }
   }

   public final short readShortLE() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      if ((ch1 | ch2) < 0) {
         throw new EOFException();
      } else {
         return (short)((ch2 << 8) + (ch1 << 0));
      }
   }

   public final int readUnsignedShort() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      if ((ch1 | ch2) < 0) {
         throw new EOFException();
      } else {
         return (ch1 << 8) + (ch2 << 0);
      }
   }

   public final int readUnsignedShortLE() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      if ((ch1 | ch2) < 0) {
         throw new EOFException();
      } else {
         return (ch2 << 8) + (ch1 << 0);
      }
   }

   public final char readChar() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      if ((ch1 | ch2) < 0) {
         throw new EOFException();
      } else {
         return (char)((ch1 << 8) + (ch2 << 0));
      }
   }

   public final char readCharLE() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      if ((ch1 | ch2) < 0) {
         throw new EOFException();
      } else {
         return (char)((ch2 << 8) + (ch1 << 0));
      }
   }

   public final int readInt() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      int ch3 = this.read();
      int ch4 = this.read();
      if ((ch1 | ch2 | ch3 | ch4) < 0) {
         throw new EOFException();
      } else {
         return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
      }
   }

   public final int readIntLE() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      int ch3 = this.read();
      int ch4 = this.read();
      if ((ch1 | ch2 | ch3 | ch4) < 0) {
         throw new EOFException();
      } else {
         return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
      }
   }

   public final long readUnsignedInt() throws IOException {
      long ch1 = (long)this.read();
      long ch2 = (long)this.read();
      long ch3 = (long)this.read();
      long ch4 = (long)this.read();
      if ((ch1 | ch2 | ch3 | ch4) < 0L) {
         throw new EOFException();
      } else {
         return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
      }
   }

   public final long readUnsignedIntLE() throws IOException {
      this.readFully(this.ruileBuf);
      long ch1 = (long)(this.ruileBuf[0] & 255);
      long ch2 = (long)(this.ruileBuf[1] & 255);
      long ch3 = (long)(this.ruileBuf[2] & 255);
      long ch4 = (long)(this.ruileBuf[3] & 255);
      return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
   }

   public final long readLong() throws IOException {
      return ((long)this.readInt() << 32) + ((long)this.readInt() & 4294967295L);
   }

   public final long readLongLE() throws IOException {
      int i1 = this.readIntLE();
      int i2 = this.readIntLE();
      return ((long)i2 << 32) + ((long)i1 & 4294967295L);
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
      StringBuffer input = new StringBuffer();
      int c = -1;
      boolean eol = false;

      while(!eol) {
         switch (c = this.read()) {
            case -1:
            case 10:
               eol = true;
               break;
            case 13:
               eol = true;
               long cur = this.getFilePointer();
               if (this.read() != 10) {
                  this.seek(cur);
               }
               break;
            default:
               input.append((char)c);
         }
      }

      if (c == -1 && input.length() == 0) {
         return null;
      } else {
         return input.toString();
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
