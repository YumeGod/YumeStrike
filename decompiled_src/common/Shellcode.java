package common;

public class Shellcode {
   public static byte[] BindProtocolPackage(byte[] var0) {
      Packer var1 = new Packer();
      var1.little();
      var1.addInt(var0.length);
      var1.addInt(var0.length);
      var1.addString(var0, var0.length);
      return var1.getBytes();
   }
}
