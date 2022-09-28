package common;

import aggressor.AggressorClient;
import encoders.XorEncoder;

public class ArtifactUtils extends BaseArtifactUtils {
   public ArtifactUtils(AggressorClient var1) {
      super(var1);
   }

   public static byte[] XorStubBegin() {
      Packer var0 = new Packer();
      var0.addByte(252);
      var0.addByte(232);
      int var1 = CommonUtils.rand(31) + 1;
      byte[] var2 = CommonUtils.randomData(var1);
      var0.little();
      var0.addInt(var1);
      var0.append(var2);
      return var0.getBytes();
   }

   public static byte[] XorStub() {
      byte[] var0 = CommonUtils.pickOption("resources/xor.bin");
      var0 = CommonUtils.shift(var0, 6);
      byte[] var1 = XorStubBegin();
      return CommonUtils.join(var1, var0);
   }

   public static byte[] _XorEncode(byte[] var0, String var1) {
      AssertUtils.TestArch(var1);
      byte[] var2;
      byte[] var3;
      if ("x86".equals(var1)) {
         var2 = XorStub();
         var3 = XorEncoder.encode(var0);
         return CommonUtils.join(var2, var3);
      } else if ("x64".equals(var1)) {
         var2 = CommonUtils.readResource("resources/xor64.bin");
         var3 = XorEncoder.encode(var0);
         return CommonUtils.join(var2, var3);
      } else {
         return new byte[0];
      }
   }

   public static byte[] XorEncode(byte[] var0, String var1) {
      if (License.isTrial()) {
         CommonUtils.print_trial("Disabled " + var1 + " payload stage encoding.");
         return var0;
      } else {
         AssertUtils.Test(var0.length > 16384, "XorEncode used on a stager (or some other small thing)");
         return _XorEncode(var0, var1);
      }
   }
}
