package socks;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PortForward implements Runnable, Mortal {
   protected ServerSocket server = null;
   protected SocksProxy broker = null;
   protected String fhost = "";
   protected int fport = 0;
   protected int port = 0;

   public void die() {
      try {
         if (this.server != null) {
            this.server.close();
         }
      } catch (IOException var2) {
      }

   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      var1.put("type", "port forward");
      var1.put("port", this.port + "");
      var1.put("fhost", this.fhost);
      var1.put("fport", this.fport + "");
      return var1;
   }

   public int getPort() {
      return this.port;
   }

   public PortForward(SocksProxy var1, String var2, int var3) {
      this.broker = var1;
      this.fhost = var2;
      this.fport = var3;
   }

   public void go(int var1) throws IOException {
      this.server = new ServerSocket(var1, 128);
      this.port = var1;
      (new Thread(this, "PortForward 0.0.0.0:" + var1 + " -> " + this.fhost + ":" + this.fport)).start();
   }

   private void waitForClient(ServerSocket var1) throws IOException {
      Socket var2 = var1.accept();
      var2.setKeepAlive(true);
      var2.setSoTimeout(0);
      this.broker.addClient(new PortForwardClient(this.broker, var2, this.broker.nextId(), this.fhost, this.fport));
   }

   public void run() {
      try {
         this.server.setSoTimeout(0);

         while(true) {
            this.waitForClient(this.server);
         }
      } catch (IOException var2) {
      }
   }
}
