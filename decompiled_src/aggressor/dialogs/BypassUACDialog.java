package aggressor.dialogs;

import aggressor.AggressorClient;
import beacon.TaskBeacon;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class BypassUACDialog implements DialogListener {
   protected AggressorClient client;
   protected String[] bids;

   public BypassUACDialog(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.bids = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "listener");
      TaskBeacon var4 = new TaskBeacon(this.client, this.bids);
      if (this.bids.length == 1) {
         DialogUtils.openOrActivate(this.client, this.bids[0]);
      }

      var4.input("bypassuac " + var3);
      var4.BypassUAC(var3);
   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("Bypass UAC", 640, 480);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(this);
      var2.sc_listener_all("listener", "Listener:", this.client);
      JButton var3 = var2.action("Launch");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-bypassuac");
      var1.add(DialogUtils.description("Execute a listener in a high-integrity context. This feature uses Cobalt Strike's Artifact Kit to generate an AV-safe DLL."), "North");
      var1.add(var2.layout(), "Center");
      var1.add(DialogUtils.center(var3, var4), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
