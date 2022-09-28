package socks;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class ReversePortForward implements Mortal {
   protected ServerSocket server = null;
   protected SocksProxy broker = null;
   protected String fhost = "";
   protected int fport = 0;
   protected int port = 0;

   public void die() {
   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      var1.put("type", "reverse port forward");
      var1.put("port", this.port + "");
      var1.put("fhost", this.fhost);
      var1.put("fport", this.fport + "");
      return var1;
   }

   public int getPort() {
      return this.port;
   }

   public ReversePortForward(SocksProxy var1, int var2, String var3, int var4) {
      this.broker = var1;
      this.port = var2;
      this.fhost = var3;
      this.fport = var4;
   }

   public void accept(int var1) {
      ReversePortForwardClient var2 = new ReversePortForwardClient(this.broker, var1, this.port, this.fhost, this.fport);
      this.broker.addClient(var2);
      var2.start();
   }

   public void go(int var1) {
   }
}
