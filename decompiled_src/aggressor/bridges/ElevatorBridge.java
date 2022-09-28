package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconElevators;
import cortana.Cortana;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.engine.ObjectUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ElevatorBridge implements Function, Loadable {
   protected AggressorClient client;

   public ElevatorBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&beacon_elevators", this);
      Cortana.put(var1, "&beacon_elevator_describe", this);
      Cortana.put(var1, "&beacon_elevator_register", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&beacon_elevators")) {
         BeaconElevators var9 = DataUtils.getBeaconElevators(this.client.getData());
         return SleepUtils.getArrayWrapper(var9.elevators());
      } else {
         String var4;
         if (var1.equals("&beacon_elevator_describe")) {
            var4 = BridgeUtilities.getString(var3, "");
            BeaconElevators var10 = DataUtils.getBeaconElevators(this.client.getData());
            return SleepUtils.getScalar(var10.getDescription(var4));
         } else {
            if (var1.equals("&beacon_elevator_register")) {
               var4 = BridgeUtilities.getString(var3, "");
               String var5 = BridgeUtilities.getString(var3, "");
               Scalar var6 = (Scalar)var3.pop();
               BeaconElevators.Elevator var7 = (BeaconElevators.Elevator)ObjectUtilities.buildArgument(BeaconElevators.Elevator.class, var6, var2);
               BeaconElevators var8 = DataUtils.getBeaconElevators(this.client.getData());
               var8.register(var4, var5, var7);
            }

            return SleepUtils.getEmptyScalar();
         }
      }
   }
}
