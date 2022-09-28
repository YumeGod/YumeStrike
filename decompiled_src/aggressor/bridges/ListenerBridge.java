package aggressor.bridges;

import aggressor.AggressorClient;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.ListenerTasks;
import common.ListenerUtils;
import common.ScListener;
import cortana.Cortana;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ListenerBridge implements Function, Loadable {
   protected AggressorClient client;

   public ListenerBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&listener_create", this);
      Cortana.put(var1, "&listener_pivot_create", this);
      Cortana.put(var1, "&listener_delete", this);
      Cortana.put(var1, "&listener_restart", this);
      Cortana.put(var1, "&listeners", this);
      Cortana.put(var1, "&listeners_local", this);
      Cortana.put(var1, "&listeners_stageless", this);
      Cortana.put(var1, "&listener_info", this);
      Cortana.put(var1, "&listener_describe", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public List getNames(List var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         var2.add((String)var4.get("name"));
      }

      return new LinkedList(var2);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      String var5;
      String var6;
      HashMap var9;
      if ("&listener_create".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         var5 = BridgeUtilities.getString(var3, "");
         var6 = BridgeUtilities.getString(var3, "");
         int var7 = BridgeUtilities.getInt(var3, 80);
         String var8 = BridgeUtilities.getString(var3, "");
         var9 = new HashMap();
         var9.put("name", var4);
         var9.put("payload", var5);
         var9.put("host", var6);
         var9.put("port", var7);
         var9.put("beacons", var8);
         this.client.getConnection().call("listeners.stop", CommonUtils.args(var4));
         this.client.getConnection().call("listeners.create", CommonUtils.args(var4, var9));
      } else if ("&listener_create_ext".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         var5 = BridgeUtilities.getString(var3, "");
         HashMap var14 = new HashMap();
         var14.put("name", var4);
         var14.put("payload", var5);
         Map var16 = SleepUtils.getMapFromHash(BridgeUtilities.getHash(var3));
         Iterator var17 = var16.entrySet().iterator();

         while(var17.hasNext()) {
            Map.Entry var20 = (Map.Entry)var17.next();
            String var10 = var20.getKey().toString();
            Object var11 = var20.getValue();
            if (var11 == null) {
               var14.put(var10, "");
            } else {
               var14.put(var10, var11.toString());
            }
         }

         var16 = ListenerUtils.mapToDialog(var16);
         var16 = ListenerUtils.dialogToMap(var16);
         this.client.getConnection().call("listeners.stop", CommonUtils.args(var4));
         this.client.getConnection().call("listeners.create", CommonUtils.args(var4, var14));
      } else if ("&listener_pivot_create".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         var5 = BridgeUtilities.getString(var3, "");
         var6 = BridgeUtilities.getString(var3, "");
         String var18 = BridgeUtilities.getString(var3, "");
         int var19 = BridgeUtilities.getInt(var3, 80);
         if (!"windows/beacon_reverse_tcp".equals(var6)) {
            throw new IllegalArgumentException("'" + var6 + "' is not a valid payload argument");
         }

         var9 = new HashMap();
         var9.put("name", var5);
         var9.put("payload", var6);
         var9.put("host", var18);
         var9.put("port", var19);
         var9.put("bid", var4);
         TaskBeacon var21 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{var4});
         var21.PivotListenerTCP(var19);
         this.client.getConnection().call("listeners.create", CommonUtils.args(var5, var9));
      } else if ("&listener_delete".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         (new ListenerTasks(this.client, var1)).remove();
      } else if ("&listener_restart".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         this.client.getConnection().call("listeners.restart", CommonUtils.args(var4));
      } else {
         List var15;
         if ("&listeners".equals(var1)) {
            var15 = this.getNames(ListenerUtils.getListenersWithStagers(this.client));
            return SleepUtils.getArrayWrapper(var15);
         }

         if ("&listeners_local".equals(var1)) {
            var15 = this.getNames(ListenerUtils.getListenersLocal(this.client));
            return SleepUtils.getArrayWrapper(var15);
         }

         if ("&listeners_stageless".equals(var1)) {
            var15 = ListenerUtils.getListenerNames(this.client);
            return SleepUtils.getArrayWrapper(var15);
         }

         ScListener var13;
         if ("&listener_info".equals(var1)) {
            try {
               var4 = BridgeUtilities.getString(var3, "");
               var13 = ListenerUtils.getListener(this.client, var4);
               if (var3.isEmpty()) {
                  return SleepUtils.getHashWrapper(var13.toMap());
               }

               var6 = BridgeUtilities.getString(var3, "");
               return SleepUtils.getScalar(var13.toMap().get(var6) + "");
            } catch (RuntimeException var12) {
               return SleepUtils.getEmptyScalar();
            }
         }

         if ("&listener_describe".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var13 = ListenerUtils.getListener(this.client, var4);
            return SleepUtils.getScalar(var13.toString());
         }
      }

      return SleepUtils.getEmptyScalar();
   }
}
