package com.mxgraph.util.png;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class ChunkStream extends OutputStream implements DataOutput {
   private String type;
   private ByteArrayOutputStream baos;
   private DataOutputStream dos;

   ChunkStream(String var1) throws IOException {
      this.type = var1;
      this.baos = new ByteArrayOutputStream();
      this.dos = new DataOutputStream(this.baos);
   }

   public void write(byte[] var1) throws IOException {
      this.dos.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.dos.write(var1, var2, var3);
   }

   public void write(int var1) throws IOException {
      this.dos.write(var1);
   }

   public void writeBoolean(boolean var1) throws IOException {
      this.dos.writeBoolean(var1);
   }

   public void writeByte(int var1) throws IOException {
      this.dos.writeByte(var1);
   }

   public void writeBytes(String var1) throws IOException {
      this.dos.writeBytes(var1);
   }

   public void writeChar(int var1) throws IOException {
      this.dos.writeChar(var1);
   }

   public void writeChars(String var1) throws IOException {
      this.dos.writeChars(var1);
   }

   public void writeDouble(double var1) throws IOException {
      this.dos.writeDouble(var1);
   }

   public void writeFloat(float var1) throws IOException {
      this.dos.writeFloat(var1);
   }

   public void writeInt(int var1) throws IOException {
      this.dos.writeInt(var1);
   }

   public void writeLong(long var1) throws IOException {
      this.dos.writeLong(var1);
   }

   public void writeShort(int var1) throws IOException {
      this.dos.writeShort(var1);
   }

   public void writeUTF(String var1) throws IOException {
      this.dos.writeUTF(var1);
   }

   public void writeToStream(DataOutputStream var1) throws IOException {
      byte[] var2 = new byte[]{(byte)this.type.charAt(0), (byte)this.type.charAt(1), (byte)this.type.charAt(2), (byte)this.type.charAt(3)};
      this.dos.flush();
      this.baos.flush();
      byte[] var3 = this.baos.toByteArray();
      int var4 = var3.length;
      var1.writeInt(var4);
      var1.write(var2);
      var1.write(var3, 0, var4);
      int var5 = -1;
      var5 = CRC.updateCRC(var5, var2, 0, 4);
      var5 = CRC.updateCRC(var5, var3, 0, var4);
      var1.writeInt(~var5);
   }

   public void close() throws IOException {
      if (this.baos != null) {
         this.baos.close();
         this.baos = null;
      }

      if (this.dos != null) {
         this.dos.close();
         this.dos = null;
      }

   }
}
