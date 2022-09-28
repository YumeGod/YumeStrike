package filter;

import common.CommonUtils;
import common.RangeList;

public class RangeCriteria implements Criteria {
   protected RangeList range;

   public RangeCriteria(String var1) {
      this.range = new RangeList(var1);
   }

   public boolean test(Object var1) {
      return var1 == null ? false : this.range.hit((long)CommonUtils.toNumber(var1.toString(), 0));
   }
}
