package beacon;

public class TaskBeaconCallback {
   protected CommandBuilder builder = new CommandBuilder();

   protected byte[] taskNoArgsCallback(int var1, int var2) {
      this.builder.setCommand(var1);
      this.builder.addInteger(var2);
      return this.builder.build();
   }

   public byte[] IPConfig(int var1) {
      return this.taskNoArgsCallback(48, var1);
   }

   public byte[] Ps(int var1) {
      return this.taskNoArgsCallback(32, var1);
   }

   public byte[] Ls(int var1, String var2) {
      this.builder.setCommand(53);
      this.builder.addInteger(var1);
      if (var2.endsWith("\\")) {
         this.builder.addLengthAndString(var2 + "*");
      } else {
         this.builder.addLengthAndString(var2 + "\\*");
      }

      return this.builder.build();
   }

   public byte[] Drives(int var1) {
      return this.taskNoArgsCallback(55, var1);
   }
}
