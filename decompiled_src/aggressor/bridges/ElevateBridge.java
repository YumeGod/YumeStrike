package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconExploits;
import cortana.Cortana;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.engine.ObjectUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ElevateBridge implements Function, Loadable {
   protected AggressorClient client;

   public ElevateBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&beacon_exploits", this);
      Cortana.put(var1, "&beacon_exploit_describe", this);
      Cortana.put(var1, "&beacon_exploit_register", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&beacon_exploits")) {
         BeaconExploits var9 = DataUtils.getBeaconExploits(this.client.getData());
         return SleepUtils.getArrayWrapper(var9.exploits());
      } else {
         String var4;
         if (var1.equals("&beacon_exploit_describe")) {
            var4 = BridgeUtilities.getString(var3, "");
            BeaconExploits var10 = DataUtils.getBeaconExploits(this.client.getData());
            return SleepUtils.getScalar(var10.getDescription(var4));
         } else {
            if (var1.equals("&beacon_exploit_register")) {
               var4 = BridgeUtilities.getString(var3, "");
               String var5 = BridgeUtilities.getString(var3, "");
               Scalar var6 = (Scalar)var3.pop();
               BeaconExploits.Exploit var7 = (BeaconExploits.Exploit)ObjectUtilities.buildArgument(BeaconExploits.Exploit.class, var6, var2);
               BeaconExploits var8 = DataUtils.getBeaconExploits(this.client.getData());
               var8.register(var4, var5, var7);
            }

            return SleepUtils.getEmptyScalar();
         }
      }
   }
}
