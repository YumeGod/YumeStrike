package beacon.inline;

import aggressor.AggressorClient;
import beacon.PostExInline;
import common.CommonUtils;

public class RunAsAdmin extends PostExInline {
   protected String command;

   public RunAsAdmin(AggressorClient var1, String var2) {
      super(var1);
      this.command = var2;
   }

   public byte[] getArguments() {
      return CommonUtils.toBytes(this.command + '\u0000', "UTF-16LE");
   }

   public String getFunction() {
      return "RunAsAdmin";
   }
}
