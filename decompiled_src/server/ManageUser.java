package server;

import aggressor.Aggressor;
import beacon.BeaconSetup;
import common.CommonUtils;
import common.LoggedEvent;
import common.MudgeSanity;
import common.Reply;
import common.Request;
import common.TabScreenshot;
import common.TeamSocket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ManageUser implements Runnable {
   protected TeamSocket client;
   protected boolean authenticated = false;
   protected String nickname = "";
   protected Resources resources;
   protected BroadcastWriter writer = null;
   protected Map calls = null;
   protected Thread mine = null;

   public ManageUser(TeamSocket var1, Resources var2, Map var3) {
      this.client = var1;
      this.resources = var2;
      this.calls = var3;
   }

   public boolean isConnected() {
      return this.client.isConnected();
   }

   public String getNick() {
      return this.nickname;
   }

   public void write(Reply var1) {
      this.writer.addReply(var1);
   }

   public void writeNow(Reply var1) {
      if (Thread.currentThread() != this.mine) {
         CommonUtils.print_error("writeNow " + var1 + " should be called in: " + this.mine + " not: " + Thread.currentThread());
         this.write(var1);
      } else {
         this.client.writeObject(var1);
      }

   }

   public void process(Request var1) throws Exception {
      String var7;
      if (!this.authenticated && "aggressor.authenticate".equals(var1.getCall()) && var1.size() == 3) {
         var7 = var1.arg(0) + "";
         String var11 = var1.arg(1) + "";
         String var13 = var1.arg(2) + "";
         if (!Aggressor.VERSION.equals(var13)) {
            this.client.writeObject(var1.reply("Your client software does not match this server\nClient: " + var13 + "\nServer: " + Aggressor.VERSION));
         } else if (ServerUtils.getServerPassword(this.resources, var7).equals(var11)) {
            if (this.resources.isRegistered(var7)) {
               this.client.writeObject(var1.reply("User is already connected."));
            } else {
               this.client.writeObject(var1.reply("SUCCESS"));
               this.authenticated = true;
               this.nickname = var7;
               Thread.currentThread().setName("Manage: " + this.nickname);
               this.writer = new BroadcastWriter();
               (new Thread(this.writer, "Writer for: " + this.nickname)).start();
            }
         } else {
            this.client.writeObject(var1.reply("Logon failure"));
         }
      } else if (!this.authenticated) {
         this.client.close();
      } else if ("aggressor.metadata".equals(var1.getCall()) && var1.size() == 1) {
         HashMap var14 = new HashMap();
         var14.put("nick", this.nickname);
         ServerUtils.getProfile(this.resources).getPreview().summarize(var14);
         long var10 = System.currentTimeMillis() - Long.parseLong(var1.arg(0) + "");
         var14.put("clockskew", var10);
         var14.put("signer", ServerUtils.getProfile(this.resources).getCodeSigner());
         var14.put("validssl", ServerUtils.getProfile(this.resources).hasValidSSL() ? "true" : "false");
         var14.put("amsi_disable", ServerUtils.getProfile(this.resources).option(".post-ex.amsi_disable") ? "true" : "false");
         var14.put("postex_obfuscate", ServerUtils.getProfile(this.resources).option(".post-ex.obfuscate") ? "true" : "false");
         var14.put("postex_smartinject", ServerUtils.getProfile(this.resources).option(".post-ex.smartinject") ? "true" : "false");
         var14.put("c2profile", ServerUtils.getProfile(this.resources));
         var14.put("pubkey", BeaconSetup.beacon_asymmetric().exportPublicKey());
         this.client.writeObject(var1.reply(var14));
      } else if ("aggressor.ready".equals(var1.getCall())) {
         this.resources.register(this.nickname, this);
         this.resources.broadcast("eventlog", LoggedEvent.Join(this.nickname));
      } else if ("aggressor.ping".equals(var1.getCall()) && var1.size() == 1) {
         this.client.writeObject(var1.reply(var1.arg(0)));
      } else if ("aggressor.users".equals(var1.getCall())) {
         this.client.writeObject(var1.reply(this.resources.getUsers()));
      } else if ("aggressor.event".equals(var1.getCall()) && var1.size() == 1) {
         LoggedEvent var12 = (LoggedEvent)var1.arg(0);
         var12.touch();
         if (var12.type == 1) {
            if (this.resources.isRegistered(var12.to)) {
               if (var12.from.equals(var12.to)) {
                  this.resources.send((String)var12.from, "eventlog", var12);
               } else {
                  this.resources.send((String)var12.from, "eventlog", var12);
                  this.resources.send((String)var12.to, "eventlog", var12);
               }
            } else {
               this.resources.send((String)var12.from, "eventlog", LoggedEvent.NoUser(var12));
            }
         } else {
            this.resources.broadcast("eventlog", var12);
         }
      } else {
         File var8;
         if ("armitage.upload".equals(var1.getCall()) && var1.size() == 1) {
            var8 = CommonUtils.SafeFile("uploads", var1.arg(0) + "");
            var8.mkdirs();
            var8.delete();
            this.client.writeObject(var1.reply(var8.getAbsolutePath()));
         } else if ("aggressor.resource".equals(var1.getCall()) && var1.size() == 1) {
            var7 = (String)var1.arg(0);
            if ("winvnc.x86.dll".equals(var7)) {
               this.client.writeObject(var1.reply(CommonUtils.readFile("third-party/winvnc.x86.dll")));
            } else if ("winvnc.x64.dll".equals(var7)) {
               this.client.writeObject(var1.reply(CommonUtils.readFile("third-party/winvnc.x64.dll")));
            }
         } else if ("aggressor.sysinfo".equals(var1.getCall())) {
            this.client.writeObject(var1.reply(MudgeSanity.systemInformation()));
         } else if (var1.is("aggressor.screenshot", 1)) {
            TabScreenshot var2 = (TabScreenshot)var1.arg(0);
            var2.touch(this.nickname);
            this.resources.process(var2);
         } else if ("armitage.append".equals(var1.getCall()) && var1.size() == 2) {
            var8 = CommonUtils.SafeFile("uploads", var1.arg(0) + "");
            byte[] var9 = (byte[])((byte[])var1.arg(1));

            try {
               FileOutputStream var4 = new FileOutputStream(var8, true);
               var4.write(var9);
               var4.close();
               this.client.writeObject(var1.reply(var8.getAbsolutePath()));
            } catch (IOException var5) {
               this.client.writeObject(var1.reply("ERROR: " + var5.getMessage()));
               MudgeSanity.logException(var1.getCall() + " " + var8, var5, true);
            }
         } else if ("armitage.broadcast".equals(var1.getCall()) && var1.size() == 2) {
            var7 = (String)var1.arg(0);
            Object var3 = var1.arg(1);
            this.resources.broadcast(var7, var3, true);
         } else if ("aggressor.reset_data".equals(var1.getCall()) && var1.size() == 0) {
            CommonUtils.print_warn(this.getNick() + " reset the data model.");
            this.resources.reset();
         } else if (this.calls.containsKey(var1.getCall())) {
            ServerHook var6 = (ServerHook)this.calls.get(var1.getCall());
            var6.call(var1, this);
         } else {
            this.client.writeObject(new Reply("server_error", 0L, var1 + ": unknown call [or bad arguments]"));
         }
      }

   }

   public void run() {
      try {
         this.mine = Thread.currentThread();

         while(this.client.isConnected()) {
            Request var1 = (Request)this.client.readObject();
            if (var1 != null) {
               this.process(var1);
            }
         }
      } catch (Exception var2) {
         MudgeSanity.logException("manage user", var2, false);
         this.client.close();
      }

      if (this.authenticated) {
         this.resources.deregister(this.nickname, this);
         this.resources.broadcast("eventlog", LoggedEvent.Quit(this.nickname));
      }

   }

   private class BroadcastWriter implements Runnable {
      protected LinkedList replies = new LinkedList();

      protected Reply grabReply() {
         synchronized(this) {
            return (Reply)this.replies.pollFirst();
         }
      }

      protected void addReply(Reply var1) {
         synchronized(this) {
            if (this.replies.size() > 100000) {
               this.replies.removeFirst();
            }

            this.replies.add(var1);
         }
      }

      public BroadcastWriter() {
      }

      public void run() {
         while(true) {
            try {
               if (ManageUser.this.client.isConnected()) {
                  Reply var1 = this.grabReply();
                  if (var1 != null) {
                     ManageUser.this.client.writeObject(var1);
                     Thread.yield();
                     continue;
                  }

                  Thread.sleep(25L);
                  continue;
               }
            } catch (Exception var2) {
               MudgeSanity.logException("bwriter", var2, false);
            }

            return;
         }
      }
   }
}
