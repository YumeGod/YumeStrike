package aggressor.bridges;

import aggressor.AggressorClient;
import common.CommonUtils;
import cortana.Cortana;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class SiteBridge implements Function, Loadable {
   protected AggressorClient client;

   public SiteBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&site_kill", this);
      Cortana.put(var1, "&site_host", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      if ("&site_kill".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         String var5 = BridgeUtilities.getString(var3, "");
         this.client.getConnection().call("cloudstrike.kill_site", CommonUtils.args(var4, var5));
      } else if ("&site_host".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         int var12 = BridgeUtilities.getInt(var3, 80);
         String var6 = BridgeUtilities.getString(var3, "");
         String var7 = BridgeUtilities.getString(var3, "");
         String var8 = BridgeUtilities.getString(var3, "application/octet-stream");
         String var9 = BridgeUtilities.getString(var3, "content");
         boolean var10 = var3.isEmpty() ? false : SleepUtils.isTrueScalar((Scalar)var3.pop());
         String var11 = var10 ? "https://" : "http://";
         this.client.getConnection().call("cloudstrike.host_data", CommonUtils.args(var4, var12, var10, var6, var7, var8, var9));
         return SleepUtils.getScalar(var11 + var4 + ":" + var12 + var6);
      }

      return SleepUtils.getEmptyScalar();
   }
}
