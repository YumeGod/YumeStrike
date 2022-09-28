package encoders;

import common.CommonUtils;
import common.MudgeSanity;
import common.Packer;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class XorEncoder {
   public static byte[] encode(byte[] var0) {
      try {
         Packer var1 = new Packer();
         DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(CommonUtils.pad(var0)));
         int var3 = CommonUtils.rand(Integer.MAX_VALUE);
         var1.addInt(var3);
         var1.little();
         var1.addIntWithMask(var2.available(), var3);
         var1.big();

         while(var2.available() > 0) {
            var3 ^= var2.readInt();
            var1.addInt(var3);
         }

         var2.close();
         return var1.getBytes();
      } catch (IOException var4) {
         MudgeSanity.logException("encode: " + var0.length + " bytes", var4, false);
         return new byte[0];
      }
   }

   public static void main(String[] var0) {
      String var1 = "Ìthis is a test and should show up after decoding.";
      byte[] var2 = CommonUtils.toBytes(var1);
      System.err.println(CommonUtils.toNasmHexString(encode(var2)));
   }
}
