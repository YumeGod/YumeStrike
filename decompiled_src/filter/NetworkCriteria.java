package filter;

import common.AddressList;

public class NetworkCriteria implements Criteria {
   protected AddressList hosts;

   public NetworkCriteria(String var1) {
      this.hosts = new AddressList(var1);
   }

   public boolean test(Object var1) {
      return var1 == null ? false : this.hosts.hit(var1.toString());
   }
}
