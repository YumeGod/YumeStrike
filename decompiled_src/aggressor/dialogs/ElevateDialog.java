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

public class ElevateDialog implements DialogListener {
   protected AggressorClient client;
   protected String[] bids;

   public ElevateDialog(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.bids = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "exploit");
      String var4 = DialogUtils.string(var2, "listener");
      TaskBeacon var5 = new TaskBeacon(this.client, this.bids);
      if (this.bids.length == 1) {
         DialogUtils.openOrActivate(this.client, this.bids[0]);
      }

      var5.input("elevate " + var3 + " " + var4);
      var5.Elevate(var3, var4);
   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("Elevate", 640, 480);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(this);
      var2.sc_listener_all("listener", "Listener:", this.client);
      var2.exploits("exploit", "Exploit:", this.client);
      JButton var3 = var2.action("Launch");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-elevate");
      var1.add(DialogUtils.description("Attempt to execute a listener in an elevated context."), "North");
      var1.add(var2.layout(), "Center");
      var1.add(DialogUtils.center(var3, var4), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
