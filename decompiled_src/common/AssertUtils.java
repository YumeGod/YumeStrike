package common;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class AssertUtils {
   public static boolean Test(boolean var0, String var1) {
      return !var0 ? TestFail(var1) : true;
   }

   public static boolean TestFail(String var0) {
      CommonUtils.print_error("Assertion failed: " + var0);
      Thread.currentThread();
      Thread.dumpStack();
      return false;
   }

   public static boolean TestNotNull(Object var0, String var1) {
      if (var0 == null) {
         CommonUtils.print_error("Assertion failed: " + var1 + " is null");
         Thread.currentThread();
         Thread.dumpStack();
         return false;
      } else {
         return true;
      }
   }

   public static boolean TestUnique(Object var0, Collection var1) {
      if (var1.contains(var0)) {
         CommonUtils.print_error("Assertion failed: '" + var0 + "' is not unique in: " + var1);
         Thread.currentThread();
         Thread.dumpStack();
         return false;
      } else {
         return true;
      }
   }

   public static boolean TestSetValue(String var0, String var1) {
      Set var2 = CommonUtils.toSet(var1);
      if (var2.contains(var0)) {
         return true;
      } else {
         CommonUtils.print_error("Assertion failed: '" + var0 + "' is not in: " + var1);
         Thread.currentThread();
         Thread.dumpStack();
         return false;
      }
   }

   public static boolean TestArch(String var0) {
      return TestSetValue(var0, "x86, x64");
   }

   public static boolean TestPID(int var0) {
      return TestRange(var0, 0, Integer.MAX_VALUE);
   }

   public static boolean TestPort(int var0) {
      return TestRange(var0, 0, 65535);
   }

   public static boolean TestPatchI(byte[] var0, int var1, int var2) {
      try {
         DataParser var3 = new DataParser(var0);
         var3.jump((long)var2);
         int var4 = var3.readInt();
         if (var4 == var1) {
            return true;
         } else {
            CommonUtils.print_error("Assertion failed: 0x" + CommonUtils.toHex((long)var4) + " at " + var2 + " is not 0x" + CommonUtils.toHex((long)var1));
            Thread.currentThread();
            Thread.dumpStack();
            return false;
         }
      } catch (IOException var5) {
         CommonUtils.print_error("Assertion failed: jump to " + var2 + " exception: " + var5.getMessage());
         Thread.currentThread();
         Thread.dumpStack();
         return false;
      }
   }

   public static boolean TestPatchS(byte[] var0, int var1, int var2) {
      try {
         DataParser var3 = new DataParser(var0);
         var3.jump((long)var2);
         int var4 = var3.readShort();
         if (var4 == var1) {
            return true;
         } else {
            CommonUtils.print_error("Assertion failed: 0x" + CommonUtils.toHex((long)var4) + " at " + var2 + " is not 0x" + CommonUtils.toHex((long)var1));
            Thread.currentThread();
            Thread.dumpStack();
            return false;
         }
      } catch (IOException var5) {
         CommonUtils.print_error("Assertion failed: jump to " + var2 + " exception: " + var5.getMessage());
         Thread.currentThread();
         Thread.dumpStack();
         return false;
      }
   }

   public static boolean TestPatch(String var0, String var1, int var2) {
      String var3 = var0.substring(var2, var2 + var1.length());
      if (var3.equals(var1)) {
         return true;
      } else {
         CommonUtils.print_error("Assertion failed: " + CommonUtils.toHexString(CommonUtils.toBytes(var3)) + " at " + var2 + " is not " + CommonUtils.toHexString(CommonUtils.toBytes(var1)));
         Thread.currentThread();
         Thread.dumpStack();
         return false;
      }
   }

   public static boolean TestRange(int var0, int var1, int var2) {
      if (var0 >= var1 && var0 <= var2) {
         return true;
      } else {
         CommonUtils.print_error("Assertion failed: " + var1 + " <= " + var0 + " (value) <= " + var2 + " does not hold");
         Thread.currentThread();
         Thread.dumpStack();
         return false;
      }
   }
}
