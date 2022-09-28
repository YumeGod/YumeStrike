package server;

import common.ChangeLog;
import common.Reply;
import common.Request;
import java.util.HashMap;
import java.util.Map;

public class DataCalls implements ServerHook {
   protected Resources resources;
   protected Map data = new HashMap();
   protected PersistentData store;
   protected String model;
   protected long previousid = -1L;
   protected ChangeLog changes = null;

   public void save() {
      this.store.save(this.data);
   }

   public void register(Map var1) {
      var1.put(this.model + ".add", this);
      var1.put(this.model + ".addnew", this);
      var1.put(this.model + ".update", this);
      var1.put(this.model + ".remove", this);
      var1.put(this.model + ".push", this);
      var1.put(this.model + ".reset", this);
   }

   public DataCalls(Resources var1, String var2) {
      this.resources = var1;
      this.model = var2;
      this.changes = new ChangeLog(var2);
      this.store = new PersistentData(this.model, this);
      this.data = (Map)this.store.getValue(new HashMap());
      this.resources.broadcast(this.model, this.buildDataModel(), true);
   }

   public void push() {
      synchronized(this) {
         this.changes.applyOptimize(this.data);
         if (this.changes.isDifferent()) {
            this.save();
            if (this.changes.size() < this.data.size()) {
               this.resources.broadcast(this.model, this.data, this.changes, true);
            } else {
               this.resources.broadcast(this.model, this.data, true);
            }
         }

         this.changes = new ChangeLog(this.model);
      }
   }

   public Map buildDataModel() {
      synchronized(this) {
         return new HashMap(this.data);
      }
   }

   public void call(Request var1, ManageUser var2) {
      String var3;
      Map var4;
      if (var1.is(this.model + ".add", 2)) {
         var3 = (String)var1.arg(0);
         var4 = (Map)var1.arg(1);
         if ("credentials".equals(this.model)) {
            var4.put("added", System.currentTimeMillis());
         }

         synchronized(this) {
            this.changes.add(var3, var4);
         }
      } else if (var1.is(this.model + ".addnew", 2)) {
         var3 = (String)var1.arg(0);
         var4 = (Map)var1.arg(1);
         if ("credentials".equals(this.model)) {
            var4.put("added", System.currentTimeMillis());
         }

         synchronized(this) {
            this.changes.addnew(var3, var4);
         }
      } else if (var1.is(this.model + ".update", 2)) {
         var3 = (String)var1.arg(0);
         var4 = (Map)var1.arg(1);
         synchronized(this) {
            this.changes.update(var3, var4);
         }
      } else if (var1.is(this.model + ".remove", 1)) {
         var3 = (String)var1.arg(0);
         synchronized(this) {
            this.changes.delete(var3);
         }
      } else if (var1.is(this.model + ".push", 0)) {
         this.push();
      } else if (var1.is(this.model + ".reset", 0)) {
         synchronized(this) {
            this.changes = new ChangeLog(this.model);
            this.data = new HashMap();
            this.save();
            this.resources.broadcast(this.model, this.data, true);
         }
      } else {
         var2.writeNow(new Reply("server_error", 0L, var1 + ": incorrect number of arguments"));
      }

   }
}
