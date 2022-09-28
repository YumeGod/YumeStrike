package beacon;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CommandBuilder {
   protected ByteArrayOutputStream backing = null;
   protected DataOutputStream output = null;
   protected int command = 0;

   public CommandBuilder() {
      this.backing = new ByteArrayOutputStream(1024);
      this.output = new DataOutputStream(this.backing);
   }

   public void setCommand(int var1) {
      this.command = var1;
   }

   public void addStringArray(String[] var1) {
      this.addShort(var1.length);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addLengthAndString(var1[var2]);
      }

   }

   public void addString(String var1) {
      try {
         this.backing.write(CommonUtils.toBytes(var1));
      } catch (IOException var3) {
         MudgeSanity.logException("addString: '" + var1 + "'", var3, false);
      }

   }

   public void addStringASCIIZ(String var1) {
      this.addString(var1);
      this.addByte(0);
   }

   public void addString(byte[] var1) {
      this.backing.write(var1, 0, var1.length);
   }

   public void addLengthAndString(String var1) {
      this.addLengthAndString(CommonUtils.toBytes(var1));
   }

   public void addLengthAndStringASCIIZ(String var1) {
      this.addLengthAndString(var1 + '\u0000');
   }

   public void addLengthAndString(byte[] var1) {
      try {
         if (var1.length == 0) {
            this.addInteger(0);
         } else {
            this.addInteger(var1.length);
            this.backing.write(var1);
         }
      } catch (IOException var3) {
         MudgeSanity.logException("addLengthAndString: '" + var1 + "'", var3, false);
      }

   }

   public void addShort(int var1) {
      byte[] var2 = new byte[8];
      ByteBuffer var3 = ByteBuffer.wrap(var2);
      var3.putShort((short)var1);
      this.backing.write(var2, 0, 2);
   }

   public void addByte(int var1) {
      this.backing.write(var1 & 255);
   }

   public void addInteger(int var1) {
      byte[] var2 = new byte[8];
      ByteBuffer var3 = ByteBuffer.wrap(var2);
      var3.putInt(var1);
      this.backing.write(var2, 0, 4);
   }

   public void pad(int var1, int var2) {
      while(var1 % 1024 != 0) {
         this.addByte(0);
         ++var1;
      }

   }

   public byte[] build() {
      try {
         this.output.flush();
         byte[] var1 = this.backing.toByteArray();
         this.backing.reset();
         this.output.writeInt(this.command);
         this.output.writeInt(var1.length);
         if (var1.length > 0) {
            this.output.write(var1, 0, var1.length);
         }

         this.output.flush();
         byte[] var2 = this.backing.toByteArray();
         this.backing.reset();
         return var2;
      } catch (IOException var3) {
         MudgeSanity.logException("command builder", var3, false);
         return new byte[0];
      }
   }
}
