package org.apache.commons.io.input;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.EndianUtils;

public class SwappedDataInputStream extends ProxyInputStream implements DataInput {
   public SwappedDataInputStream(InputStream input) {
      super(input);
   }

   public boolean readBoolean() throws IOException, EOFException {
      return 0 == this.readByte();
   }

   public byte readByte() throws IOException, EOFException {
      return (byte)super.in.read();
   }

   public char readChar() throws IOException, EOFException {
      return (char)this.readShort();
   }

   public double readDouble() throws IOException, EOFException {
      return EndianUtils.readSwappedDouble(super.in);
   }

   public float readFloat() throws IOException, EOFException {
      return EndianUtils.readSwappedFloat(super.in);
   }

   public void readFully(byte[] data) throws IOException, EOFException {
      this.readFully(data, 0, data.length);
   }

   public void readFully(byte[] data, int offset, int length) throws IOException, EOFException {
      int count;
      for(int remaining = length; remaining > 0; remaining -= count) {
         int location = offset + (length - remaining);
         count = this.read(data, location, remaining);
         if (-1 == count) {
            throw new EOFException();
         }
      }

   }

   public int readInt() throws IOException, EOFException {
      return EndianUtils.readSwappedInteger(super.in);
   }

   public String readLine() throws IOException, EOFException {
      throw new UnsupportedOperationException("Operation not supported: readLine()");
   }

   public long readLong() throws IOException, EOFException {
      return EndianUtils.readSwappedLong(super.in);
   }

   public short readShort() throws IOException, EOFException {
      return EndianUtils.readSwappedShort(super.in);
   }

   public int readUnsignedByte() throws IOException, EOFException {
      return super.in.read();
   }

   public int readUnsignedShort() throws IOException, EOFException {
      return EndianUtils.readSwappedUnsignedShort(super.in);
   }

   public String readUTF() throws IOException, EOFException {
      throw new UnsupportedOperationException("Operation not supported: readUTF()");
   }

   public int skipBytes(int count) throws IOException, EOFException {
      return (int)super.in.skip((long)count);
   }
}
