package beacon.jobs;

import beacon.JobSimple;
import beacon.TaskBeacon;
import common.CommonUtils;
import java.io.File;

public class DllSpawnJob extends JobSimple {
   protected String file;
   protected String arg;
   protected String desc;
   protected int waittime;
   protected boolean usetoken;

   public DllSpawnJob(TaskBeacon var1, String var2, String var3, String var4, int var5, boolean var6) {
      super(var1);
      this.file = var2;
      this.arg = var3;
      this.desc = var4;
      this.waittime = var5;
      this.usetoken = var6;
      if (var4 == null || var4.length() == 0) {
         this.desc = CommonUtils.stripRight((new File(var2)).getName(), ".dll");
      }

      if (var4.length() > 48) {
         var4 = var4.substring(0, 48);
      }

   }

   public boolean ignoreToken() {
      return !this.usetoken;
   }

   public String getDescription() {
      return "Tasked beacon to spawn " + this.desc;
   }

   public String getShortDescription() {
      return this.desc;
   }

   public String getDLLName() {
      return this.file;
   }

   public int getWaitTime() {
      return this.waittime;
   }

   public byte[] getArgument() {
      return CommonUtils.toBytes(this.arg + '\u0000');
   }

   public byte[] getDLLContent() {
      return CommonUtils.readFile(this.getDLLName());
   }
}
