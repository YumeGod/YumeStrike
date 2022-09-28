package socks;

import common.MudgeSanity;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocksProxyServer implements Runnable, Mortal {
   protected ServerSocket server = null;
   protected SocksProxy broker = null;
   protected int port = 0;

   public void die() {
      try {
         if (this.server != null) {
            this.server.close();
         }
      } catch (IOException var2) {
         MudgeSanity.logException("die: " + this.port, var2, false);
      }

   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      var1.put("type", "SOCKS4a Proxy");
      var1.put("port", this.port + "");
      return var1;
   }

   public SocksProxyServer(SocksProxy var1) {
      this.broker = var1;
   }

   public int getPort() {
      return this.port;
   }

   public void go(int var1) throws IOException {
      this.server = new ServerSocket(var1, 128);
      this.port = var1;
      (new Thread(this, "SOCKS4a on " + var1)).start();
   }

   private void waitForClient(ServerSocket var1) throws IOException {
      Socket var2 = var1.accept();
      var2.setKeepAlive(true);
      var2.setSoTimeout(0);
      this.broker.addClient(new ProxyClient(this.broker, var2, this.broker.nextId()));
   }

   public void run() {
      try {
         this.server.setSoTimeout(0);

         while(true) {
            this.waitForClient(this.server);
         }
      } catch (IOException var2) {
         MudgeSanity.logException("run: " + this.port, var2, false);
      }
   }
}
