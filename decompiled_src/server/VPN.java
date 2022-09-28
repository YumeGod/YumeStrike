package server;

import cloudstrike.WebServer;
import common.CommonUtils;
import common.Do;
import common.MudgeSanity;
import common.RegexParser;
import common.Reply;
import common.Request;
import common.Timers;
import endpoint.Base;
import endpoint.HTTP;
import endpoint.ICMP;
import endpoint.TCP;
import endpoint.UDP;
import icmp.Server;
import java.io.File;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tap.EncryptedTap;

public class VPN implements ServerHook, Do {
   protected Resources resources;
   protected Map vpn = new HashMap();
   protected Map taps = new HashMap();
   protected Map srv = new HashMap();
   protected boolean loaded = false;
   protected Server iserver = null;

   public void register(Map var1) {
      var1.put("cloudstrike.start_tap", this);
      var1.put("cloudstrike.stop_tap", this);
      var1.put("cloudstrike.set_tap_hwaddr", this);
   }

   public VPN(Resources var1) {
      this.resources = var1;
   }

   public boolean hasVPN(String var1) {
      synchronized(this) {
         return this.vpn.containsKey(var1);
      }
   }

   public EncryptedTap getTap(String var1) {
      synchronized(this) {
         return (EncryptedTap)this.taps.get(var1);
      }
   }

   public Base getServer(String var1) {
      synchronized(this) {
         return (Base)this.srv.get(var1);
      }
   }

   public List buildVPNModel() {
      synchronized(this) {
         LinkedList var2 = new LinkedList();

         Map var4;
         for(Iterator var3 = this.vpn.values().iterator(); var3.hasNext(); var2.add(new HashMap(var4))) {
            var4 = (Map)var3.next();
            String var5 = (String)var4.get("interface");
            EncryptedTap var6 = this.getTap(var5);
            Base var7 = this.getServer(var5);
            if (var6.isActive()) {
               var4.put("client", var6.getRemoteHost());
               var4.put("tx", var7.getTransmittedBytes());
               var4.put("rx", var7.getReceivedBytes());
            }
         }

         return var2;
      }
   }

   public boolean moment(String var1) {
      this.resources.broadcast("interfaces", this.buildVPNModel());
      return true;
   }

   public void report(String var1, String var2, byte[] var3, String var4, int var5, String var6, String var7) {
      synchronized(this) {
         HashMap var9 = new HashMap();
         var9.put("interface", var1);
         var9.put("mac", var2);
         var9.put("secret", var3);
         var9.put("channel", var4);
         var9.put("port", new Integer(var5));
         var9.put("client", var6);
         var9.put("useragent", ServerUtils.randua(this.resources));
         var9.put("hook", var7);
         this.vpn.put(var1, var9);
      }
   }

   public boolean loadTapLibrary() {
      synchronized(this) {
         if (this.loaded) {
            return true;
         } else {
            boolean var10000;
            try {
               if (CommonUtils.is64bit()) {
                  System.load(CommonUtils.dropFile("libtapmanager64.so", "cobalt_tapmanager", ".so"));
               } else {
                  System.load(CommonUtils.dropFile("libtapmanager.so", "cobalt_tapmanager", ".so"));
               }

               this.loaded = true;
               Timers.getTimers().every(1000L, "vpn", this);
               var10000 = true;
            } catch (Exception var4) {
               MudgeSanity.logException("loadTapLibrary", var4, false);
               return false;
            }

            return var10000;
         }
      }
   }

   public Server loadICMPLibrary() {
      synchronized(this) {
         if (this.iserver != null) {
            return this.iserver;
         } else {
            Server var10000;
            try {
               if (CommonUtils.is64bit()) {
                  System.load(CommonUtils.dropFile("libicmp64.so", "icmp", ".so"));
               } else {
                  System.load(CommonUtils.dropFile("libicmp.so", "icmp", ".so"));
               }

               this.iserver = new Server();
               var10000 = this.iserver;
            } catch (Exception var4) {
               MudgeSanity.logException("loadICMPLibrary", var4, false);
               return null;
            }

            return var10000;
         }
      }
   }

   public void stop_tap(ManageUser var1, Request var2) {
      synchronized(this) {
         String var4 = (String)var2.arg(0);
         if (this.srv.containsKey(var4)) {
            Base var5 = (Base)this.srv.get(var4);
            var5.quit();
         }

         this.taps.remove(var4);
         this.srv.remove(var4);
         this.vpn.remove(var4);
      }
   }

   public void set_tap_address(ManageUser var1, Request var2) {
      String var3 = (String)var2.arg(0);
      String var4 = (String)var2.arg(1);
      synchronized(this) {
         if (this.hasVPN(var3)) {
            EncryptedTap var6 = (EncryptedTap)this.taps.get(var3);
            var6.setHWAddress(this.macToByte(var4));
            Map var7 = (Map)this.vpn.get(var3);
            var7.put("mac", var4);
         }

      }
   }

   public byte[] macToByte(String var1) {
      String[] var2 = var1.split(":");
      byte[] var3 = new byte[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3[var4] = (byte)Integer.parseInt(var2[var4], 16);
      }

      return var3;
   }

   public void start_tap(ManageUser var1, Request var2, String var3, String var4, int var5, String var6) {
      if (!(new File("/dev/net/tun")).exists()) {
         var1.writeNow(var2.reply("/dev/net/tun does not exist on team server system."));
      } else if (this.hasVPN(var3)) {
         var1.writeNow(var2.reply(var3 + " is already defined"));
      } else if (!RegexParser.isMatch(var4, "[a-fA-F0-9]{2}:[a-fA-F0-9]{2}:[a-fA-F0-9]{2}:[a-fA-F0-9]{2}:[a-fA-F0-9]{2}:[a-fA-F0-9]{2}")) {
         var1.writeNow(var2.reply("invalid mac address"));
      } else if (!this.loadTapLibrary()) {
         var1.writeNow(var2.reply("could not load tap library"));
      } else {
         try {
            new SecureRandom();
            byte[] var7 = SecureRandom.getSeed(16);
            EncryptedTap var8 = new EncryptedTap(var3, var7);
            String var9 = "";
            if ("UDP".equals(var6)) {
               UDP var10 = new UDP(var8, var5);
               this.srv.put(var3, var10);
            } else {
               TCP var14;
               if ("TCP (Bind)".equals(var6)) {
                  var14 = new TCP(var8, var5, false);
                  this.srv.put(var3, var14);
               } else if ("TCP (Reverse)".equals(var6)) {
                  var14 = new TCP(var8, var5, true);
                  this.srv.put(var3, var14);
               } else if ("HTTP".equals(var6)) {
                  WebCalls var15 = ServerUtils.getWebCalls(this.resources);
                  WebServer var11 = var15.getWebServer(var5);
                  HTTP var12 = new HTTP(var8);
                  var9 = "/" + var3 + ".json";
                  var12.setup(var11, var9);
                  this.srv.put(var3, var12);
               } else if ("ICMP".equals(var6)) {
                  this.loadICMPLibrary();
                  ICMP var16 = new ICMP(var8);
                  var9 = CommonUtils.ID().substring(0, 4);
                  this.iserver.addIcmpListener(var9, var16);
                  this.srv.put(var3, var16);
               }
            }

            this.report(var3, var4, var7, var6, var5, "not connected", var9);
            this.taps.put(var3, var8);
         } catch (Exception var13) {
            MudgeSanity.logException("start_tap", var13, false);
            var1.writeNow(var2.reply(var13.getMessage()));
         }

      }
   }

   public void call(Request var1, ManageUser var2) {
      if (var1.is("cloudstrike.start_tap", 4)) {
         this.start_tap(var2, var1, (String)var1.arg(0), (String)var1.arg(1), Integer.parseInt((String)var1.arg(2)), (String)var1.arg(3));
      } else if (var1.is("cloudstrike.stop_tap", 1)) {
         this.stop_tap(var2, var1);
      } else if (var1.is("cloudstrike.set_tap_hwaddr", 2)) {
         this.set_tap_address(var2, var1);
      } else {
         var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
      }

   }
}
