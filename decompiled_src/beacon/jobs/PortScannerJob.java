package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.AddressList;
import common.CommonUtils;
import common.Packer;
import common.PortFlipper;
import pe.PostExObfuscator;

public class PortScannerJob extends Job {
   protected String targets;
   protected String ports;
   protected String discovery;
   protected int maxsockets;

   public PortScannerJob(TaskBeacon var1, String var2, String var3, String var4, int var5) {
      super(var1);
      this.targets = var2;
      this.ports = var3;
      this.discovery = var4;
      this.maxsockets = var5;
   }

   public String getDescription() {
      return "Tasked beacon to scan ports " + this.ports + " on " + this.targets;
   }

   public String getShortDescription() {
      return "port scanner";
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/portscan.x64.dll" : "resources/portscan.dll";
   }

   public String getPipeName() {
      return "portscan";
   }

   public String getTactic() {
      return "T1046";
   }

   public int getCallbackType() {
      return 25;
   }

   public int getWaitTime() {
      return 1;
   }

   public boolean ignoreToken() {
      return false;
   }

   public byte[] fix(byte[] var1) {
      String var2 = CommonUtils.pad(CommonUtils.bString((new AddressList(this.targets)).export()), '\u0000', 2048);
      var1 = CommonUtils.patch(var1, "TARGETS!12345", var2);
      var1 = CommonUtils.patch(var1, "PORTS!12345", CommonUtils.bString((new PortFlipper(this.ports)).getMask()));
      Packer var3 = new Packer();
      var3.little();
      var3.addInt(this.maxsockets);
      if (this.discovery.equals("none")) {
         var3.addInt(0);
      } else if (this.discovery.equals("icmp")) {
         var3.addInt(1);
      } else if (this.discovery.equals("arp")) {
         var3.addInt(2);
      }

      String var4 = CommonUtils.pad(CommonUtils.bString(var3.getBytes()), '\u0000', 32);
      var1 = CommonUtils.patch(var1, "PREFERENCES!12345", var4);
      return var1;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
