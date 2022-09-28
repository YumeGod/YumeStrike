package server;

import common.CommonUtils;
import common.Reply;
import common.Request;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Listeners implements ServerHook {
   protected Resources resources;
   protected PersistentData store;
   protected Map listeners = new HashMap();

   public void register(Map var1) {
      var1.put("listeners.create", this);
      var1.put("listeners.remove", this);
      var1.put("listeners.go", this);
      var1.put("listeners.restart", this);
      var1.put("listeners.stop", this);
      var1.put("listeners.localip", this);
      var1.put("listeners.set_status", this);
      var1.put("listeners.export", this);
      var1.put("listeners.update", this);
      var1.put("listeners.push", this);
   }

   public Listeners(Resources var1) {
      this.resources = var1;
      this.store = new PersistentData("listeners", this);
      this.listeners = (Map)this.store.getValue(new HashMap());
      this.resources.broadcast("listeners", this.buildListenerModel(), true);
      this.resources.broadcast("localip", ServerUtils.getMyIP(this.resources), true);
   }

   public void save() {
      this.store.save(this.listeners);
   }

   public Map buildListenerModel() {
      synchronized(this) {
         HashMap var2 = new HashMap();
         Iterator var3 = this.listeners.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            String var5 = (String)var4.getKey();
            HashMap var6 = new HashMap((Map)var4.getValue());
            var2.put(var5, var6);
         }

         return var2;
      }
   }

   public void call(Request var1, ManageUser var2) {
      String var3;
      Map var4;
      if (var1.is("listeners.create", 2)) {
         var3 = var1.arg(0) + "";
         var4 = (Map)var1.arg(1);
         synchronized(this) {
            this.listeners.put(var3, var4);
            this.save();
            this.resources.broadcast("listeners", this.buildListenerModel(), true);
         }

         if (isBeacon(var4)) {
            this.resources.call(var2, var1.derive("beacons.start", CommonUtils.args(var4)));
         } else {
            var2.write(var1.reply(""));
         }
      } else if (var1.is("listeners.remove", 1)) {
         var3 = var1.arg(0) + "";
         var4 = null;
         synchronized(this) {
            var4 = (Map)this.listeners.get(var3);
            if (var4 == null) {
               return;
            }

            this.listeners.remove(var3);
            this.save();
            this.resources.broadcast("listeners", this.buildListenerModel(), true);
         }

         if (isBeacon(var4)) {
            this.resources.call(var2, var1.derive("beacons.stop", CommonUtils.args(var4)));
         }
      } else if (var1.is("listeners.restart", 1)) {
         var3 = var1.arg(0) + "";
         var4 = null;
         synchronized(this) {
            var4 = (Map)this.listeners.get(var3);
         }

         if (isBeacon(var4)) {
            this.resources.call("beacons.stop", CommonUtils.args(var4));
            this.resources.call(var2, var1.derive("beacons.start", CommonUtils.args(var4)));
         } else {
            var2.write(var1.reply((Object)null));
         }
      } else if (var1.is("listeners.stop", 1)) {
         var3 = var1.arg(0) + "";
         var4 = null;
         synchronized(this) {
            var4 = (Map)this.listeners.get(var3);
         }

         if (var4 == null) {
            return;
         }

         if (isBeacon(var4)) {
            this.resources.call("beacons.stop", CommonUtils.args(var4));
         }
      } else if (var1.is("listeners.go", 0)) {
         Iterator var20 = this.buildListenerModel().entrySet().iterator();

         while(var20.hasNext()) {
            Map.Entry var21 = (Map.Entry)var20.next();
            if (isBeacon((Map)var21.getValue())) {
               this.resources.call("beacons.start", CommonUtils.args(var21.getValue()));
            }
         }
      } else if (var1.is("listeners.localip", 1)) {
         var3 = var1.arg(0) + "";
         this.resources.put("localip", var3);
         this.resources.broadcast("localip", var3, true);
      } else if (var1.is("listeners.update", 2)) {
         var3 = var1.arg(0) + "";
         var4 = (Map)var1.arg(1);
         synchronized(this) {
            Map var6 = (Map)this.listeners.get(var3);
            if (var6 == null) {
               return;
            }

            Iterator var7 = var4.entrySet().iterator();

            while(var7.hasNext()) {
               Map.Entry var8 = (Map.Entry)var7.next();
               var6.put(var8.getKey(), var8.getValue());
            }
         }
      } else if (var1.is("listeners.push", 0)) {
         synchronized(this) {
            this.save();
            this.resources.broadcast("listeners", this.buildListenerModel(), true);
         }
      } else if (var1.is("listeners.set_status", 2)) {
         var3 = var1.arg(0) + "";
         String var22 = var1.arg(1) + "";
         Map var5 = null;
         synchronized(this) {
            var5 = (Map)this.listeners.get(var3);
            var5.put("status", var22);
            this.resources.broadcast("listeners", this.buildListenerModel(), true);
         }
      } else {
         var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
      }

   }

   public static boolean isBeacon(Map var0) {
      String var1 = var0.get("payload") + "";
      HashSet var2 = new HashSet();
      var2.add("windows/beacon_bind_pipe");
      var2.add("windows/beacon_reverse_tcp");
      var2.add("windows/beacon_bind_tcp");
      return CommonUtils.isin("beacon", var1) && !var2.contains(var1);
   }
}
