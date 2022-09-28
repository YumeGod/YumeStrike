package beacon.inline;

import aggressor.AggressorClient;
import beacon.PostExInline;

public class KerberosTicketPurge extends PostExInline {
   public KerberosTicketPurge(AggressorClient var1) {
      super(var1);
   }

   public String getFunction() {
      return "KerberosTicketPurge";
   }
}
