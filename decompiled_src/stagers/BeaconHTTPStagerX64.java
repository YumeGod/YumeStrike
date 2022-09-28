package stagers;

import common.ScListener;

public class BeaconHTTPStagerX64 extends GenericHTTPStagerX64 {
   public BeaconHTTPStagerX64(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/beacon_http/reverse_http";
   }

   public String getURI() {
      return this.getConfig().getURI_X64() + this.getConfig().getQueryString();
   }
}
