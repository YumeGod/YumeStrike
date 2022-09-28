package filter;

import common.CommonUtils;

public class WildcardCriteria implements Criteria {
   protected String wildcard;

   public WildcardCriteria(String var1) {
      this.wildcard = var1.toLowerCase();
   }

   public boolean test(Object var1) {
      return var1 == null ? false : CommonUtils.iswm(this.wildcard, var1.toString().toLowerCase());
   }
}
