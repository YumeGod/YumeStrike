package common;

import aggressor.AggressorClient;
import aggressor.dialogs.PivotListenerSetup;
import aggressor.dialogs.ScListenerDialog;
import beacon.TaskBeacon;
import dialog.DialogUtils;
import java.util.Map;

public class ListenerTasks {
   protected AggressorClient client;
   protected String name;

   public ListenerTasks(AggressorClient var1, String var2) {
      this.client = var1;
      this.name = var2;
   }

   protected Map getListenerMap() {
      Map var1 = this.client.getData().getMapSafe("listeners");
      return (Map)var1.get(this.name);
   }

   public boolean isPivotListener() {
      Map var1 = this.getListenerMap();
      String var2 = DialogUtils.string(var1, "payload");
      String var3 = DialogUtils.string(var1, "bid");
      return "windows/beacon_reverse_tcp".equals(var2) && !"".equals(var3);
   }

   public String getBeaconID() {
      return DialogUtils.string(this.getListenerMap(), "bid");
   }

   public int getPort() {
      return DialogUtils.number(this.getListenerMap(), "port");
   }

   public void remove() {
      if (this.isPivotListener()) {
         TaskBeacon var1 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.getBeaconID()});
         var1.input("rportfwd stop " + this.getPort());
         var1.PortForwardStop(this.getPort());
      }

      this.client.getConnection().call("listeners.remove", CommonUtils.args(this.name));
   }

   public void edit() {
      if (this.isPivotListener()) {
         (new PivotListenerSetup(this.client, this.getListenerMap())).show();
      } else {
         (new ScListenerDialog(this.client, this.getListenerMap())).show();
      }

   }
}
