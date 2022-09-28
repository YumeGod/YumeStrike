package beacon.inline;

import aggressor.AggressorClient;
import beacon.PostExInline;

public class KerberosTicketUse extends PostExInline {
   protected byte[] ticket;

   public KerberosTicketUse(AggressorClient var1, byte[] var2) {
      super(var1);
      this.ticket = var2;
   }

   public byte[] getArguments() {
      return this.ticket;
   }

   public String getFunction() {
      return "KerberosTicketUse";
   }
}
