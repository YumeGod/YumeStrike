package aggressor.dialogs;

import aggressor.AggressorClient;
import beacon.TaskBeacon;
import common.AObject;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class SOCKSSetup extends AObject implements DialogListener {
   protected String bid = "";
   protected AggressorClient client = null;
   protected JFrame dialog = null;

   public SOCKSSetup(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      int var3 = DialogUtils.number(var2, "ProxyPort");
      TaskBeacon var4 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
      DialogUtils.openOrActivate(this.client, this.bid);
      var4.input("socks " + var3);
      var4.SocksStart(var3);
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Start SOCKS", 240, 240);
      this.dialog.setLayout(new BorderLayout());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("ProxyPort", CommonUtils.randomPort() + "");
      var1.text("ProxyPort", "Proxy Server Port:", 8);
      JButton var2 = var1.action("Launch");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-socks-proxy-pivoting");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
