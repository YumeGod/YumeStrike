package common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Packer {
   protected ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
   protected DataOutputStream data;
   protected byte[] bdata = new byte[8];
   protected ByteBuffer buffer = null;

   public Packer() {
      this.data = new DataOutputStream(this.out);
      this.buffer = ByteBuffer.wrap(this.bdata);
   }

   public void little() {
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
   }

   public void big() {
      this.buffer.order(ByteOrder.BIG_ENDIAN);
   }

   public void addInteger(int var1) {
      this.addInt(var1);
   }

   public void addInt(int var1) {
      this.buffer.putInt(0, var1);
      this.write(this.bdata, 0, 4);
   }

   public void append(byte[] var1) {
      this.write(var1, 0, var1.length);
   }

   public void addIntWithMask(int var1, int var2) {
      this.buffer.putInt(0, var1);
      ByteOrder var3 = this.buffer.order();
      this.big();
      int var4 = this.buffer.getInt(0);
      this.buffer.putInt(0, var4 ^ var2);
      this.write(this.bdata, 0, 4);
      this.buffer.order(var3);
   }

   public void addUnicodeString(String var1, int var2) {
      try {
         this.addShort(var1.length());
         this.addShort(var2);

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            this.data.writeChar(var1.charAt(var3));
         }
      } catch (IOException var4) {
         MudgeSanity.logException("addUnicodeString: " + var1, var4, false);
      }

   }

   public void addByte(int var1) {
      try {
         this.data.write((byte)var1);
      } catch (IOException var3) {
         MudgeSanity.logException("addByte: " + var1, var3, false);
      }

   }

   public void addHex(String var1) {
      try {
         char[] var2 = var1.toCharArray();
         StringBuffer var3 = new StringBuffer("FF");

         for(int var4 = 0; var4 < var2.length; var4 += 2) {
            var3.setCharAt(0, var2[var4]);
            var3.setCharAt(1, var2[var4 + 1]);
            this.data.writeByte(Integer.parseInt(var3.toString(), 16));
         }
      } catch (IOException var5) {
         MudgeSanity.logException("addHex: " + var1, var5, false);
      }

   }

   protected void write(byte[] var1, int var2, int var3) {
      try {
         this.data.write(var1, var2, var3);
      } catch (IOException var5) {
         MudgeSanity.logException("write", var5, false);
      }

   }

   public void addShort(int var1) {
      this.buffer.putShort(0, (short)var1);
      this.write(this.bdata, 0, 2);
   }

   public void addString(String var1) {
      this.addString(var1, var1.length());
   }

   public void addString(String var1, int var2) {
      this.addString(CommonUtils.toBytes(var1), var2);
   }

   public void pad(char var1, int var2) {
      byte[] var3 = new byte[var2];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = (byte)var1;
      }

      this.write(var3, 0, var3.length);
   }

   public void addString(byte[] var1, int var2) {
      this.write(var1, 0, var1.length);
      if (var1.length < var2) {
         byte[] var3 = new byte[var2 - var1.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = 0;
         }

         this.write(var3, 0, var3.length);
      }

   }

   public void addStringUTF8(String var1, int var2) {
      try {
         this.addString(var1.getBytes("UTF-8"), var2);
      } catch (Exception var4) {
         MudgeSanity.logException("addStringUTF8", var4, false);
      }

   }

   public void addWideString(String var1) {
      try {
         this.append(var1.getBytes("UTF-16LE"));
      } catch (Exception var3) {
         MudgeSanity.logException("addWideString", var3, false);
      }

   }

   public void addWideString(String var1, int var2) {
      try {
         this.addString(var1.getBytes("UTF-16LE"), var2);
      } catch (Exception var4) {
         MudgeSanity.logException("addWideString", var4, false);
      }

   }

   public byte[] getBytes() {
      byte[] var1 = this.out.toByteArray();

      try {
         this.data.close();
      } catch (IOException var3) {
         MudgeSanity.logException("getBytes", var3, false);
      }

      return var1;
   }

   public long size() {
      return (long)this.out.size();
   }

   public void addLengthAndString(String var1) {
      this.addLengthAndString(CommonUtils.toBytes(var1));
   }

   public void addLengthAndStringASCIIZ(String var1) {
      this.addLengthAndString(var1 + '\u0000');
   }

   public void addLengthAndString(byte[] var1) {
      if (var1.length == 0) {
         this.addInt(0);
      } else {
         this.addInt(var1.length);
         this.append(var1);
      }

   }
}
