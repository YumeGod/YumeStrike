package common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Stack;

public class DataParser {
   protected DataInputStream content;
   protected byte[] bdata;
   protected ByteBuffer buffer;
   protected byte[] original;
   protected Stack frames;

   public DataParser(InputStream var1) {
      this(CommonUtils.readAll(var1));
   }

   public void jump(long var1) throws IOException {
      this.frames.push(this.content);
      this.content = new DataInputStream(new ByteArrayInputStream(this.original));
      if (var1 > 0L) {
         this.consume((int)var1);
      }

   }

   public void complete() throws IOException {
      this.content.close();
      this.content = (DataInputStream)this.frames.pop();
   }

   public DataParser(byte[] var1) {
      this.content = null;
      this.bdata = new byte[8];
      this.buffer = null;
      this.frames = new Stack();
      this.original = var1;
      this.buffer = ByteBuffer.wrap(this.bdata);
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
      this.content = new DataInputStream(new ByteArrayInputStream(var1));
   }

   public void consume(int var1) throws IOException {
      this.content.skipBytes(var1);
   }

   public int readInt() throws IOException {
      this.buffer.clear();
      this.content.read(this.bdata, 0, 4);
      return this.buffer.getInt(0);
   }

   public long readQWord() throws IOException {
      this.buffer.clear();
      this.content.read(this.bdata, 0, 8);
      return this.buffer.getLong(0);
   }

   public byte readByte() throws IOException {
      return this.content.readByte();
   }

   public char readChar() throws IOException {
      return (char)this.content.readByte();
   }

   public char readChar(DataInputStream var1) throws IOException {
      return (char)var1.readByte();
   }

   public byte[] readBytes(int var1) throws IOException {
      byte[] var2 = new byte[var1];
      this.content.read(var2);
      return var2;
   }

   public int readShort() throws IOException {
      this.content.read(this.bdata, 0, 2);
      return this.buffer.getShort(0) & '\uffff';
   }

   public boolean more() throws IOException {
      return this.content.available() > 0;
   }

   public String readCountedString() throws IOException {
      int var1 = this.readInt();
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(this.readChar());
      }

      return var2.toString();
   }

   public String readString() throws IOException {
      StringBuffer var1 = new StringBuffer();

      while(true) {
         char var2 = this.readChar();
         if (var2 <= 0) {
            return var1.toString();
         }

         var1.append(var2);
      }
   }

   public String readString(int var1) throws IOException {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = this.readChar();
         if (var4 > 0) {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   public DataInputStream getData() {
      return this.content;
   }

   public void little() {
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
   }

   public void big() {
      this.buffer.order(ByteOrder.BIG_ENDIAN);
   }
}
