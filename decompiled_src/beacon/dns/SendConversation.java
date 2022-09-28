package beacon.dns;

import dns.DNSServer;

public abstract class SendConversation {
   protected String id;
   protected String dtype;
   protected long idlemask;

   public SendConversation(String var1, String var2, long var3) {
      this.id = var1;
      this.dtype = var2;
      this.idlemask = var3;
   }

   public abstract boolean started();

   public abstract DNSServer.Response start(byte[] var1);

   public abstract DNSServer.Response next();

   public abstract boolean isComplete();
}
