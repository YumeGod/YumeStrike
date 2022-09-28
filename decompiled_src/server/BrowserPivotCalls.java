package server;

import common.BeaconOutput;
import common.CommonUtils;
import common.MudgeSanity;
import common.Reply;
import common.Request;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import proxy.HTTPProxy;
import proxy.HTTPProxyEventListener;

public class BrowserPivotCalls implements ServerHook {
   protected Resources resources;
   protected Map sessions = new HashMap();

   public void register(Map var1) {
      var1.put("browserpivot.start", this);
      var1.put("browserpivot.stop", this);
   }

   public BrowserPivotCalls(Resources var1) {
      this.resources = var1;
   }

   public void start(Request var1, ManageUser var2) {
      final String var3 = (String)var1.arg(0);
      int var4 = Integer.parseInt((String)var1.arg(1));
      int var5 = Integer.parseInt((String)var1.arg(2));
      if (this.sessions.containsKey(var3)) {
         this.resources.broadcast("beaconlog", BeaconOutput.Error(var3, "This beacon already has a browser pivot session. Use 'browserpivot stop' to stop it."));
      } else {
         try {
            HTTPProxy var6 = new HTTPProxy(var4, "127.0.0.1", var5);
            var6.addProxyListener(new HTTPProxyEventListener() {
               public void proxyEvent(int var1, String var2) {
                  if (var1 == 0) {
                     BrowserPivotCalls.this.resources.broadcast("beaconlog", BeaconOutput.OutputB(var3, var2));
                  } else if (var1 == 1) {
                     BrowserPivotCalls.this.resources.broadcast("beaconlog", BeaconOutput.Error(var3, var2));
                  } else if (var1 == 2) {
                     BrowserPivotCalls.this.resources.broadcast("beaconlog", BeaconOutput.Output(var3, var2));
                  }

               }
            });
            this.resources.broadcast("beaconlog", BeaconOutput.Output(var3, "Browser Pivot HTTP proxy is at: " + ServerUtils.getMyIP(this.resources) + ":" + var4));
            var6.start();
            this.sessions.put(var3, var6);
         } catch (IOException var7) {
            this.resources.broadcast("beaconlog", BeaconOutput.Error(var3, "Could not start Browser Pivot on port " + var4 + ": " + var7.getMessage()));
            MudgeSanity.logException("browser pivot start", var7, true);
         }

      }
   }

   public void stop(Request var1, ManageUser var2) {
      String var3 = (String)var1.arg(0);
      HTTPProxy var4 = (HTTPProxy)this.sessions.get(var3);
      if (var4 != null) {
         var4.stop();
         this.resources.call(var2, var1.derive("beacons.pivot_stop_port", CommonUtils.args(var4.getPort())));
         this.sessions.remove(var3);
         this.resources.broadcast("beaconlog", BeaconOutput.OutputB(var3, "Stopped Browser Pivot"));
      } else {
         this.resources.broadcast("beaconlog", BeaconOutput.Error(var3, "There is no active browser pivot"));
      }

   }

   public void call(Request var1, ManageUser var2) {
      if (var1.is("browserpivot.start", 3)) {
         this.start(var1, var2);
      } else if (var1.is("browserpivot.stop", 1)) {
         this.stop(var1, var2);
      } else {
         var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
      }

   }
}
