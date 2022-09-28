package beacon.jobs;

import beacon.TaskBeacon;

public class MimikatzJobSmall extends MimikatzJob {
   public MimikatzJobSmall(TaskBeacon var1, String var2) {
      super(var1, var2);
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/mimikatz-min.x64.dll" : "resources/mimikatz-min.x86.dll";
   }
}
