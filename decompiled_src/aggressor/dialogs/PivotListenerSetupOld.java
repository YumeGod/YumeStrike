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

public class PivotListenerSetupOld extends AObject implements DialogListener, Callback {
   protected String bid = "";
   protected AggressorClient client = null;
   protected JFrame dialog = null;

   public PivotListenerSetupOld(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "name");
      String var4 = DialogUtils.string(var2, "lhost");
      int var5 = DialogUtils.number(var2, "lport");
      String var6 = DialogUtils.string(var2, "fhost");
      int var7 = DialogUtils.number(var2, "fport");
      String var8 = DialogUtils.string(var2, "payload");
      TaskBeacon var9 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
      DialogUtils.openOrActivate(this.client, this.bid);
      var9.input("rportfwd " + var5 + " " + var6 + " " + var7);
      var9.PortForward(var5, var6, var7);
      HashMap var10 = new HashMap();
      var10.put("payload", var8);
      var10.put("port", var5 + "");
      var10.put("host", var4);
      var10.put("name", var3);
      this.client.getConnection().call("listeners.create", CommonUtils.args(var3, var10), this);
   }

   public void result(String var1, Object var2) {
      String var3 = var2 + "";
      if (var3.equals("success")) {
         DialogUtils.showInfo("Started Listener");
      } else {
         DialogUtils.showError("Could not start listener: \n" + var3);
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("New Listener", 640, 480);
      this.dialog.setLayout(new BorderLayout());
      String[] var1 = CommonUtils.toArray("windows/foreign/reverse_http, windows/foreign/reverse_https, windows/foreign/reverse_tcp");
      DialogManager var2 = new DialogManager(this.dialog);
      var2.addDialogListener(this);
      BeaconEntry var3 = DataUtils.getBeacon(this.client.getData(), this.bid);
      var2.set("lhost", var3.getInternal());
      var2.set("fhost", DataUtils.getLocalIP(this.client.getData()));
      var2.text("name", "Name:", 20);
      var2.combobox("payload", "Payload:", var1);
      var2.text("lhost", "Listen Host:", 20);
      var2.text("lport", "Listen Port:", 10);
      var2.text("fhost", "Remote Host:", 20);
      var2.text("fport", "Remote Port:", 10);
      JButton var4 = var2.action("Save");
      JButton var5 = var2.help("https://www.cobaltstrike.com/help-pivot-listener");
      this.dialog.add(DialogUtils.description("A pivot listener is a way to setup a foreign listener and a reverse port forward that relays traffic to it in one step."), "North");
      this.dialog.add(var2.layout(), "Center");
      this.dialog.add(DialogUtils.center(var4, var5), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
