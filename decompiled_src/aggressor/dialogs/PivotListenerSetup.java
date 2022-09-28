package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.TaskBeacon;
import common.AObject;
import common.BeaconEntry;
import common.Callback;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PivotListenerSetup extends AObject implements DialogListener, Callback {
   protected AggressorClient client = null;
   protected JFrame dialog = null;
   protected Map options = new HashMap();
   protected String title = "New Listener";

   public PivotListenerSetup(AggressorClient var1, String var2) {
      this.client = var1;
      BeaconEntry var3 = DataUtils.getBeacon(var1.getData(), var2);
      this.options.put("host", var3.getInternal());
      this.options.put("port", "4444");
      this.options.put("bid", var2);
   }

   public PivotListenerSetup(AggressorClient var1, Map var2) {
      this.client = var1;
      this.options = var2;
      this.title = "Edit Listener";
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "name");
      String var4 = DialogUtils.string(var2, "host");
      int var5 = DialogUtils.number(var2, "port");
      String var6 = DialogUtils.string(var2, "payload");
      String var7 = DialogUtils.string(var2, "bid");
      TaskBeacon var8 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{var7});
      if (!this.title.equals("Edit Listener")) {
         DialogUtils.openOrActivate(this.client, var7);
         var8.input("rportfwd " + var5 + " " + var6);
         var8.PivotListenerTCP(var5);
      }

      HashMap var9 = new HashMap();
      var9.put("payload", var6);
      var9.put("port", var5 + "");
      var9.put("host", var4);
      var9.put("name", var3);
      var9.put("bid", var7 + "");
      this.client.getConnection().call("listeners.create", CommonUtils.args(var3, var9), this);
   }

   public void result(String var1, Object var2) {
      String var3 = var2 + "";
      if (!"".equals(var3)) {
         if (var3.equals("success")) {
            DialogUtils.showInfo("Started Listener");
         } else {
            DialogUtils.showError("Could not start listener: \n" + var3);
         }
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog(this.title, 640, 480);
      this.dialog.setLayout(new BorderLayout());
      String[] var1 = CommonUtils.toArray("windows/beacon_reverse_tcp");
      DialogManager var2 = new DialogManager(this.dialog);
      var2.addDialogListener(this);
      var2.set(this.options);
      if (this.title.equals("Edit Listener")) {
         var2.text_disabled("name", "Name:");
         var2.combobox("payload", "Payload:", var1);
         var2.text("host", "Listen Host:", 20);
         var2.text_disabled("port", "Listen Port:");
         var2.beacon_disabled("bid", "Session", this.client);
      } else {
         var2.text("name", "Name:", 20);
         var2.combobox("payload", "Payload:", var1);
         var2.text("host", "Listen Host:", 20);
         var2.text("port", "Listen Port:", 10);
         var2.beacon("bid", "Session", this.client);
      }

      JButton var3 = var2.action("Save");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-pivot-listener");
      this.dialog.add(DialogUtils.description("A pivot listener is a way to use a compromised system as a redirector for other Beacon sessions."), "North");
      this.dialog.add(var2.layout(), "Center");
      this.dialog.add(DialogUtils.center(var3, var4), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
