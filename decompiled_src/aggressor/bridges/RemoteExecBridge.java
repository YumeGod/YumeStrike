package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconRemoteExecMethods;
import cortana.Cortana;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.engine.ObjectUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class RemoteExecBridge implements Function, Loadable {
   protected AggressorClient client;

   public RemoteExecBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&beacon_remote_exec_methods", this);
      Cortana.put(var1, "&beacon_remote_exec_method_describe", this);
      Cortana.put(var1, "&beacon_remote_exec_method_register", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&beacon_remote_exec_methods")) {
         BeaconRemoteExecMethods var9 = DataUtils.getBeaconRemoteExecMethods(this.client.getData());
         return SleepUtils.getArrayWrapper(var9.methods());
      } else {
         String var4;
         if (var1.equals("&beacon_remote_exec_method_describe")) {
            var4 = BridgeUtilities.getString(var3, "");
            BeaconRemoteExecMethods var10 = DataUtils.getBeaconRemoteExecMethods(this.client.getData());
            return SleepUtils.getScalar(var10.getDescription(var4));
         } else {
            if (var1.equals("&beacon_remote_exec_method_register")) {
               var4 = BridgeUtilities.getString(var3, "");
               String var5 = BridgeUtilities.getString(var3, "");
               Scalar var6 = (Scalar)var3.pop();
               BeaconRemoteExecMethods.RemoteExecMethod var7 = (BeaconRemoteExecMethods.RemoteExecMethod)ObjectUtilities.buildArgument(BeaconRemoteExecMethods.RemoteExecMethod.class, var6, var2);
               BeaconRemoteExecMethods var8 = DataUtils.getBeaconRemoteExecMethods(this.client.getData());
               var8.register(var4, var5, var7);
            }

            return SleepUtils.getEmptyScalar();
         }
      }
   }
}
