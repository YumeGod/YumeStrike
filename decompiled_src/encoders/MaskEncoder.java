package encoders;

import common.CommonUtils;
import common.DataParser;
import common.MudgeSanity;

public class MaskEncoder {
   public static byte[] decode(byte[] var0) {
      try {
         byte[] var1 = new byte[var0.length - 4];
         DataParser var2 = new DataParser(var0);
         byte[] var3 = var2.readBytes(4);

         for(int var4 = 0; var4 < var1.length && var2.more(); ++var4) {
            var1[var4] = (byte)(var2.readByte() ^ var3[var4 % 4]);
         }

         return var1;
      } catch (Throwable var5) {
         MudgeSanity.logException("'mask' decode [" + var0.length + " bytes] failed", var5, false);
         return new byte[0];
      }
   }

   public static byte[] encode(byte[] var0) {
      byte[] var1 = new byte[var0.length];
      byte[] var2 = new byte[]{(byte)CommonUtils.rand(255), (byte)CommonUtils.rand(255), (byte)CommonUtils.rand(255), (byte)CommonUtils.rand(255)};

      for(int var3 = 0; var3 < var0.length; ++var3) {
         var1[var3] = (byte)(var0[var3] ^ var2[var3 % 4]);
      }

      return CommonUtils.join(var2, var1);
   }

   public static void main(String[] var0) {
      String var1 = "Ìthis is a test and should show up after decoding.";
      byte[] var2 = CommonUtils.toBytes(var1);
      System.err.println(CommonUtils.toNasmHexString(encode(var2)));
   }
}
