package socks;

import common.MudgeSanity;
import java.io.IOException;
import java.net.Socket;

public class PortForwardClient extends BasicClient {
   protected String fhost;
   protected int fport;

   public PortForwardClient(SocksProxy var1, Socket var2, int var3, String var4, int var5) {
      this.client = var2;
      this.parent = var1;
      this.chid = var3;
      this.fhost = var4;
      this.fport = var5;
      (new Thread(this, "SOCKS4a Proxy INIT")).start();
   }

   public void run() {
      try {
         this.setup();
         this.parent.fireEvent(ProxyEvent.EVENT_CONNECT(this.chid, this.fhost, this.fport));
      } catch (IOException var2) {
         MudgeSanity.logException("port forward client", var2, false);
      }

   }
}
