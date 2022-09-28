package beacon.c2setup;

import beacon.BeaconC2;
import beacon.BeaconSetup;
import common.ScListener;
import extc2.ExternalC2Server;
import server.Resources;

public class BeaconSetupExternalC2 extends BeaconSetupC2 {
   protected int port = 0;
   protected String addr = "";
   protected ExternalC2Server server = null;
   protected BeaconSetup setup = null;

   public BeaconSetupExternalC2(Resources var1, ScListener var2, BeaconC2 var3, BeaconSetup var4) {
      super(var1, var2, var3);
      this.port = var2.getBindPort();
      this.addr = var2.isLocalHostOnly() ? "127.0.0.1" : "0.0.0.0";
      this.setup = var4;
   }

   public void start() throws Exception {
      this.server = new ExternalC2Server(this.setup, this.getListener(), this.addr, this.port);
      this.server.start();
   }

   public void stop() {
      this.server.die();
   }
}
