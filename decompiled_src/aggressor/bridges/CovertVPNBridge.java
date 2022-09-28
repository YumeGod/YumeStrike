package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.CommonUtils;
import cortana.Cortana;
import dialog.DialogUtils;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class CovertVPNBridge implements Function, Loadable {
   protected AggressorClient client;

   public CovertVPNBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&vpn_tap_create", this);
      Cortana.put(var1, "&vpn_tap_delete", this);
      Cortana.put(var1, "&vpn_interfaces", this);
      Cortana.put(var1, "&vpn_interface_info", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      String var6;
      String var7;
      if ("&vpn_tap_create".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         String var5 = BridgeUtilities.getString(var3, "");
         var6 = BridgeUtilities.getString(var3, "");
         var7 = BridgeUtilities.getInt(var3, 0) + "";
         String var8 = BridgeUtilities.getString(var3, "udp");
         if ("".equals(var5)) {
            var5 = CommonUtils.randomMac();
         }

         if ("udp".equals(var8)) {
            var8 = "UDP";
         } else if ("http".equals(var8)) {
            var8 = "HTTP";
         } else if ("icmp".equals(var8)) {
            var8 = "ICMP";
         } else if ("bind".equals(var8)) {
            var8 = "TCP (Bind)";
         } else {
            if (!"reverse".equals(var8)) {
               throw new RuntimeException("Unknown channel: '" + var8 + "'");
            }

            var8 = "TCP (Reverse)";
         }

         this.client.getConnection().call("cloudstrike.start_tap", CommonUtils.args(var4, var5, var7, var8));
      } else {
         Map var9;
         if ("&vpn_tap_delete".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var9 = DataUtils.getInterface(this.client.getData(), var4);
            var6 = DialogUtils.string(var9, "channel");
            var7 = DialogUtils.string(var9, "port");
            if ("TCP (Bind)".equals(var6)) {
               this.client.getConnection().call("beacons.pivot_stop_port", CommonUtils.args(var7));
            }

            this.client.getConnection().call("cloudstrike.stop_tap", CommonUtils.args(var4));
         } else {
            if ("&vpn_interfaces".equals(var1)) {
               return SleepUtils.getArrayWrapper(DataUtils.getInterfaceList(this.client.getData()));
            }

            if ("&vpn_interface_info".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var9 = DataUtils.getInterface(this.client.getData(), var4);
               if (var3.isEmpty()) {
                  return SleepUtils.getHashWrapper(var9);
               }

               var6 = BridgeUtilities.getString(var3, "");
               return CommonUtils.convertAll(var9.get(var6));
            }
         }
      }

      return SleepUtils.getEmptyScalar();
   }
}
