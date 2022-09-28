package beacon.methods;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconRemoteExecMethods;
import beacon.TaskBeacon;

public class PsExec implements BeaconRemoteExecMethods.RemoteExecMethod {
   protected AggressorClient client;

   public PsExec(AggressorClient var1) {
      this.client = var1;
      DataUtils.getBeaconRemoteExecMethods(var1.getData()).register("psexec", "Remote execute via Service Control Manager", this);
   }

   public void remoteexec(String var1, String var2, String var3) {
      (new TaskBeacon(this.client, new String[]{var1})).PsExecCommand(var2, var3);
   }
}
