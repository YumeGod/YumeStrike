package filter;

import common.CommonUtils;

public class BeaconCriteria implements Criteria {
   public boolean test(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.toString();
         int var3 = CommonUtils.toNumber(var2, 0);
         return "beacon".equals(CommonUtils.session(var3));
      }
   }
}
