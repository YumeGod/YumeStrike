package encoders;

import common.CommonUtils;
import common.Packer;

public class Transforms {
   public static byte[] toVeil(byte[] var0) {
      Packer var1 = new Packer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.addString("\\x");
         String var3 = Integer.toString(var0[var2] & 255, 16);
         if (var3.length() == 2) {
            var1.addString(var3);
         } else {
            var1.addString("0" + var3);
         }
      }

      return var1.getBytes();
   }

   public static String toArray(byte[] var0) {
      Packer var1 = new Packer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.addString("0x");
         String var3 = Integer.toString(var0[var2] & 255, 16);
         if (var3.length() == 2) {
            var1.addString(var3);
         } else {
            var1.addString("0" + var3);
         }

         if (var2 < var0.length - 1) {
            var1.addString(", ");
         }
      }

      return CommonUtils.bString(var1.getBytes());
   }

   public static byte[] toC(byte[] var0) {
      Packer var1 = new Packer();
      var1.addString("/* length: " + var0.length + " bytes */\n");
      var1.addString("unsigned char buf[] = \"" + CommonUtils.bString(toVeil(var0)) + "\";\n");
      return var1.getBytes();
   }

   public static byte[] toPerl(byte[] var0) {
      Packer var1 = new Packer();
      var1.addString("# length: " + var0.length + " bytes\n");
      var1.addString("$buf = \"" + CommonUtils.bString(toVeil(var0)) + "\";\n");
      return var1.getBytes();
   }

   public static byte[] toPython(byte[] var0) {
      Packer var1 = new Packer();
      var1.addString("# length: " + var0.length + " bytes\n");
      var1.addString("buf = \"" + CommonUtils.bString(toVeil(var0)) + "\"\n");
      return var1.getBytes();
   }

   public static byte[] toJava(byte[] var0) {
      Packer var1 = new Packer();
      var1.addString("/* length: " + var0.length + " bytes */\n");
      var1.addString("byte buf[] = new byte[] { " + toArray(var0) + " };\n");
      return var1.getBytes();
   }

   public static byte[] toCSharp(byte[] var0) {
      Packer var1 = new Packer();
      var1.addString("/* length: " + var0.length + " bytes */\n");
      var1.addString("byte[] buf = new byte[" + var0.length + "] { " + toArray(var0) + " };\n");
      return var1.getBytes();
   }

   public static String toVBA(byte[] var0) {
      StringBuffer var1 = new StringBuffer(var0.length * 10);
      var1.append("Array(");

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append(var0[var2]);
         if (var2 > 0 && var2 % 40 == 0 && var2 + 1 < var0.length) {
            var1.append(", _\n");
         } else if (var2 + 1 < var0.length) {
            var1.append(",");
         }
      }

      var1.append(")");
      return var1.toString();
   }
}
