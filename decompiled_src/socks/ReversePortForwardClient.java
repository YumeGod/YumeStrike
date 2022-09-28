package socks;

import java.io.IOException;
import java.net.Socket;

public class ReversePortForwardClient extends BasicClient {
   protected String fhost;
   protected int fport;
   protected int lport;

   public ReversePortForwardClient(SocksProxy var1, int var2, int var3, String var4, int var5) {
      this.client = null;
      this.parent = var1;
      this.chid = var2;
      this.fhost = var4;
      this.fport = var5;
      this.lport = var3;
   }

   public void start() {
      try {
         this.client = new Socket(this.fhost, this.fport);
         this.setup();
         super.start();
      } catch (IOException var2) {
         this.die();
      }

   }

   public void run() {
   }
}
