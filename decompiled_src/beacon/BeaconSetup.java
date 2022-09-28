package beacon;

import beacon.c2setup.BeaconSetupC2;
import beacon.c2setup.BeaconSetupDNS;
import beacon.c2setup.BeaconSetupExternalC2;
import beacon.c2setup.BeaconSetupHTTP;
import c2profile.Profile;
import common.CommonUtils;
import common.ListenerUtils;
import common.MudgeSanity;
import common.ScListener;
import dialog.DialogUtils;
import dns.AsymmetricCrypto;
import dns.QuickSecurity;
import java.io.File;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import server.Resources;
import server.ServerUtils;

public class BeaconSetup extends BeaconConstants {
   protected Profile c2profile = null;
   protected BeaconC2 controller = null;
   protected Map servers = new HashMap();
   protected String error = "";
   protected Resources resources;

   public BeaconSetup(Resources var1) {
      this.resources = var1;
      this.c2profile = ServerUtils.getProfile(var1);
      this.controller = new BeaconC2(var1);
   }

   public ScListener getListener(Map var1) {
      return new ScListener(this.c2profile, beacon_asymmetric().exportPublicKey(), var1);
   }

   public ScListener getExternalC2(String var1) {
      return new ScListener(this.c2profile, beacon_asymmetric().exportPublicKey(), ListenerUtils.ExternalC2Map(var1));
   }

   public Map getC2Info(String var1) {
      HashMap var2 = new HashMap();
      var2.put("bid", var1);
      return var2;
   }

   public BeaconC2 getController() {
      return this.controller;
   }

   public static AsymmetricCrypto beacon_asymmetric() {
      try {
         File var0 = new File(".cobaltstrike.beacon_keys");
         if (!var0.exists()) {
            CommonUtils.writeObject(var0, AsymmetricCrypto.generateKeys());
         }

         KeyPair var1 = (KeyPair)CommonUtils.readObject(var0, (Object)null);
         return new AsymmetricCrypto(var1);
      } catch (Exception var2) {
         MudgeSanity.logException("generate beacon asymmetric keys", var2, false);
         return null;
      }
   }

   public void stop(String var1) {
      BeaconSetupC2 var2 = null;
      ScListener var3 = null;
      synchronized(this) {
         if (!this.servers.containsKey(var1)) {
            return;
         }

         var2 = (BeaconSetupC2)this.servers.get(var1);
         this.servers.remove(var1);
      }

      var2.stop();
      var3 = var2.getListener();
      CommonUtils.print_info("Listener: " + var3.getName() + " (" + var3.getPayload() + ") on port " + var3.getBindPort() + " stopped.");
   }

   public void initCrypto() {
      QuickSecurity var1 = null;
      AsymmetricCrypto var2 = null;
      var1 = new QuickSecurity();
      var2 = beacon_asymmetric();
      this.controller.setCrypto(var1, var2);
      if (QuickSecurity.getCryptoScheme() == 1) {
         CommonUtils.print_trial("WARNING! Beacon will not encrypt tasks or responses!");
      }

   }

   public boolean start(Map var1) {
      ScListener var2 = this.getListener(var1);
      String var3 = DialogUtils.string(var1, "payload");
      Object var4 = null;
      if (!"windows/beacon_http/reverse_http".equals(var3) && !"windows/beacon_https/reverse_https".equals(var3)) {
         if ("windows/beacon_dns/reverse_dns_txt".equals(var3)) {
            var4 = new BeaconSetupDNS(this.resources, var2, this.controller);
         } else {
            if (!"windows/beacon_extc2".equals(var3)) {
               return true;
            }

            var4 = new BeaconSetupExternalC2(this.resources, var2, this.controller, this);
         }
      } else {
         var4 = new BeaconSetupHTTP(this.resources, var2, this.controller);
      }

      try {
         this.initCrypto();
         ((BeaconSetupC2)var4).start();
         synchronized(this) {
            this.servers.put(var2.getName(), var4);
            return true;
         }
      } catch (Exception var8) {
         MudgeSanity.logException("Start Beacon: " + var2.getName() + " (" + var2.getPayload() + ") bound to port " + var2.getBindPort(), var8, false);
         this.error = var8.getMessage();
         return false;
      }
   }

   public String getLastError() {
      return this.error;
   }
}
