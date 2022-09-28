package beacon;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.BeaconEntry;
import common.CommonUtils;
import common.MudgeSanity;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class EncodedCommandBuilder extends CommandBuilder {
   protected AggressorClient client;

   public EncodedCommandBuilder(AggressorClient var1) {
      this.client = var1;
   }

   public byte[] process(String var1, String var2) {
      String var3 = this.getCharset(var1);

      try {
         Charset var4 = Charset.forName(var3);
         if (var4 == null) {
            return CommonUtils.toBytes(var2);
         } else {
            ByteBuffer var5 = var4.encode(var2);
            byte[] var6 = new byte[var5.remaining()];
            var5.get(var6, 0, var6.length);
            return var6;
         }
      } catch (Exception var7) {
         MudgeSanity.logException("could not convert text for id " + var1 + " with " + var3, var7, false);
         return CommonUtils.toBytes(var2);
      }
   }

   public String getCharset(String var1) {
      BeaconEntry var2 = DataUtils.getBeacon(this.client.getData(), var1);
      return var2 != null ? var2.getCharset() : null;
   }

   public void addEncodedString(String var1, String var2) {
      this.addString(this.process(var1, var2));
   }

   public void addLengthAndEncodedString(String var1, String var2) {
      this.addLengthAndString(this.process(var1, var2));
   }
}
