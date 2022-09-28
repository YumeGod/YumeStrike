package beacon;

import aggressor.AggressorClient;
import beacon.pivots.PortForwardPivot;
import beacon.pivots.ReversePortForwardPivot;
import beacon.pivots.SOCKSPivot;
import dialog.DialogUtils;
import java.util.Map;

public abstract class BeaconPivot {
   protected AggressorClient client = null;
   protected String bid = null;
   protected int port = 0;

   public static BeaconPivot resolve(AggressorClient var0, Map var1) {
      Object var2 = null;
      String var3 = DialogUtils.string(var1, "type");
      String var4 = DialogUtils.string(var1, "bid");
      int var5 = DialogUtils.number(var1, "port");
      if (var3.equals("reverse port forward")) {
         var2 = new ReversePortForwardPivot();
      } else if (var3.equals("port forward")) {
         var2 = new PortForwardPivot();
      } else {
         var2 = new SOCKSPivot();
      }

      ((BeaconPivot)var2).client = var0;
      ((BeaconPivot)var2).bid = var4;
      ((BeaconPivot)var2).port = var5;
      return (BeaconPivot)var2;
   }

   public static BeaconPivot[] resolve(AggressorClient var0, Map[] var1) {
      BeaconPivot[] var2 = new BeaconPivot[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = resolve(var0, var1[var3]);
      }

      return var2;
   }

   public abstract void die();

   public abstract void tunnel();
}
