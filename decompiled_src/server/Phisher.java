package server;

import common.Reply;
import common.Request;
import java.util.HashMap;
import java.util.Map;
import phish.Campaign;

public class Phisher implements ServerHook {
   protected Resources resources;
   protected Map campaigns = new HashMap();

   public void register(Map var1) {
      var1.put("cloudstrike.go_phish", this);
      var1.put("cloudstrike.stop_phish", this);
   }

   public Phisher(Resources var1) {
      this.resources = var1;
   }

   public void call(Request var1, ManageUser var2) {
      String var4;
      if (var1.is("cloudstrike.go_phish", 3)) {
         synchronized(this) {
            var4 = (String)var1.arg(0);
            this.campaigns.put(var4, new Campaign(this, var1, var2, this.resources));
         }
      } else if (var1.is("cloudstrike.stop_phish", 1)) {
         synchronized(this) {
            var4 = (String)var1.arg(0);
            Campaign var5 = (Campaign)this.campaigns.get(var4);
            if (var5 != null) {
               var5.cancel();
               this.campaigns.remove(var4);
            }
         }
      } else {
         var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
      }

   }
}
