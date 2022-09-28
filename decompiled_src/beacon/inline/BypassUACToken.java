package beacon.inline;

import aggressor.AggressorClient;
import beacon.PostExInline;

public class BypassUACToken extends PostExInline {
   protected byte[] payload;

   public BypassUACToken(AggressorClient var1, byte[] var2) {
      super(var1);
      this.payload = var2;
   }

   public byte[] getArguments() {
      return this.payload;
   }

   public String getFunction() {
      return "SpawnAsAdmin";
   }
}
