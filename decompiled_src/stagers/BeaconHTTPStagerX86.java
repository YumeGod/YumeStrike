package stagers;

import common.ScListener;

public class BeaconHTTPStagerX86 extends GenericHTTPStagerX86 {
   public BeaconHTTPStagerX86(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/beacon_http/reverse_http";
   }

   public String getURI() {
      return this.getConfig().getURI() + this.getConfig().getQueryString();
   }
}
