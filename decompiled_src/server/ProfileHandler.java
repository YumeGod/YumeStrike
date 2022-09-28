package server;

import common.CommonUtils;
import common.LoggedEvent;
import common.ProfilerEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import profiler.SystemProfiler;

public class ProfileHandler implements SystemProfiler.ProfileListener {
   protected Resources resources;

   public ProfileHandler(Resources var1) {
      this.resources = var1;
   }

   public void receivedProfile(String var1, String var2, String var3, Map var4, String var5) {
      String var6 = "unknown";
      double var7 = 0.0;
      Iterator var9 = var4.keySet().iterator();

      while(true) {
         while(true) {
            while(var9.hasNext()) {
               String var10 = ((String)var9.next()).toLowerCase();
               if (CommonUtils.iswm("*windows*", var10)) {
                  var6 = "Windows";
                  if (CommonUtils.isin("2000", var10)) {
                     var7 = 5.0;
                  } else if (!CommonUtils.isin("xp", var10) && !CommonUtils.isin("2003", var10)) {
                     if (!CommonUtils.isin("7", var10) && !CommonUtils.isin("vista", var10)) {
                        if (CommonUtils.isin("8", var10)) {
                           var7 = 6.2;
                        } else if (CommonUtils.isin("10", var10)) {
                           var7 = 10.0;
                        }
                     } else {
                        var7 = 6.0;
                     }
                  } else {
                     var7 = 5.1;
                  }
               } else if (CommonUtils.iswm("*mac*ip*", var10)) {
                  var6 = "Apple iOS";
               } else if (CommonUtils.iswm("*mac*os*x*", var10)) {
                  var6 = "MacOS X";
               } else if (CommonUtils.iswm("*linux*", var10)) {
                  var6 = "Linux";
               } else if (CommonUtils.iswm("*android*", var10)) {
                  var6 = "Android";
               }
            }

            var9 = var4.entrySet().iterator();

            while(var9.hasNext()) {
               Map.Entry var13 = (Map.Entry)var9.next();
               HashMap var11 = new HashMap();
               var11.put("nonce", CommonUtils.ID());
               var11.put("external", var1);
               var11.put("internal", var2);
               var11.put("useragent", var3);
               var11.put("id", var5);
               var11.put("application", var13.getKey());
               var11.put("version", var13.getValue());
               var11.put("date", System.currentTimeMillis() + "");
               var11.put("os", var6);
               var11.put("osver", var7 + "");
               String var12 = CommonUtils.ApplicationKey(var11);
               this.resources.call("applications.add", CommonUtils.args(var12, var11));
            }

            if (!"unknown".equals(var2)) {
               ServerUtils.addTarget(this.resources, var2, (String)null, (String)null, var6, var7);
               if (!var2.equals(var1)) {
                  ServerUtils.addTarget(this.resources, var1, (String)null, (String)null, "firewall", 0.0);
               }
            } else {
               ServerUtils.addTarget(this.resources, var1, (String)null, (String)null, var6, var7);
            }

            this.resources.call("applications.push");
            this.resources.broadcast("weblog", new ProfilerEvent(var1, var2, var3, var4, var5));
            this.resources.broadcast("eventlog", LoggedEvent.Notify("received system profile (" + var4.size() + " applications)"));
            return;
         }
      }
   }
}
