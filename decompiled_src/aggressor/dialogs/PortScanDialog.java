package aggressor.dialogs;

import aggressor.AggressorClient;
import beacon.TaskBeacon;
import common.AObject;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PortScanDialog extends AObject implements DialogListener {
   protected String[] targets;
   protected AggressorClient client;

   public PortScanDialog(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.targets = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "ports");
      String var4 = DialogUtils.string(var2, "bid");
      String var5 = CommonUtils.join(this.targets, ",");
      String var6 = DialogUtils.string(var2, "sockets");
      if ("".equals(var4)) {
         DialogUtils.showError("You must select a Beacon session to scan through.");
      } else {
         TaskBeacon var7 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{var4});
         DialogUtils.openOrActivate(this.client, var4);
         var7.input("portscan " + var5 + " " + var3 + " none " + var6);
         var7.PortScan(var5, var3, "none", CommonUtils.toNumber(var6, 1024));
      }

   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("Scan", 480, 240);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(this);
      HashMap var3 = new HashMap();
      var3.put("ports", "1-1024,3389,5900-6000");
      var3.put("sockets", "1024");
      var2.set(var3);
      var2.text("ports", "Ports", 25);
      var2.text("sockets", "Max Sockets", 25);
      var2.beacon("bid", "Session", this.client);
      JButton var4 = var2.action("Launch");
      JButton var5 = var2.help("https://www.cobaltstrike.com/help-portscan");
      var1.add(var2.layout(), "Center");
      var1.add(DialogUtils.center(var4, var5), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
