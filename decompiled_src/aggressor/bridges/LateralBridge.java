package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconRemoteExploits;
import cortana.Cortana;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.engine.ObjectUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class LateralBridge implements Function, Loadable {
   protected AggressorClient client;

   public LateralBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&beacon_remote_exploits", this);
      Cortana.put(var1, "&beacon_remote_exploit_describe", this);
      Cortana.put(var1, "&beacon_remote_exploit_arch", this);
      Cortana.put(var1, "&beacon_remote_exploit_register", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&beacon_remote_exploits")) {
         BeaconRemoteExploits var10 = DataUtils.getBeaconRemoteExploits(this.client.getData());
         return SleepUtils.getArrayWrapper(var10.exploits());
      } else {
         String var4;
         BeaconRemoteExploits var11;
         if (var1.equals("&beacon_remote_exploit_describe")) {
            var4 = BridgeUtilities.getString(var3, "");
            var11 = DataUtils.getBeaconRemoteExploits(this.client.getData());
            return SleepUtils.getScalar(var11.getDescription(var4));
         } else if (var1.equals("&beacon_remote_exploit_arch")) {
            var4 = BridgeUtilities.getString(var3, "");
            var11 = DataUtils.getBeaconRemoteExploits(this.client.getData());
            return SleepUtils.getScalar(var11.getArch(var4));
         } else {
            if (var1.equals("&beacon_remote_exploit_register")) {
               var4 = BridgeUtilities.getString(var3, "");
               String var5 = BridgeUtilities.getString(var3, "");
               String var6 = BridgeUtilities.getString(var3, "");
               Scalar var7 = (Scalar)var3.pop();
               BeaconRemoteExploits.RemoteExploit var8 = (BeaconRemoteExploits.RemoteExploit)ObjectUtilities.buildArgument(BeaconRemoteExploits.RemoteExploit.class, var7, var2);
               BeaconRemoteExploits var9 = DataUtils.getBeaconRemoteExploits(this.client.getData());
               var9.register(var4, var5, var6, var8);
            }

            return SleepUtils.getEmptyScalar();
         }
      }
   }
}
