package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.TaskBeacon;
import common.BeaconEntry;
import common.ListenerUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class OneLinerDialog implements DialogListener {
   protected AggressorClient client;
   protected String[] bids;

   public OneLinerDialog(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.bids = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "listener");
      String var4 = DialogUtils.bool(var2, "x64") ? "x64" : "x86";
      TaskBeacon var5 = new TaskBeacon(this.client, this.bids);
      var5.input("oneliner " + var4 + " " + var3);

      for(int var6 = 0; var6 < this.bids.length; ++var6) {
         BeaconEntry var7 = DataUtils.getBeacon(this.client.getData(), this.bids[var6]);
         if (var7 != null) {
            var5.log_task(this.bids[var6], "Created PowerShell one-liner to run " + var3 + " (" + var4 + ")", "T1086");
            String var8 = var5.SetupPayloadDownloadCradle(this.bids[var6], var4, ListenerUtils.getListener(this.client, var3));
            DialogUtils.startedWebService(var7.title("One-liner for"), var8);
         }
      }

   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("PowerShell One-liner", 640, 480);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(this);
      var2.sc_listener_all("listener", "Listener:", this.client);
      var2.checkbox_add("x64", "x64:", "Use x64 payload");
      JButton var3 = var2.action("Launch");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-oneliner");
      var1.add(DialogUtils.description("Generate a single use one-liner that runs payload within this session."), "North");
      var1.add(var2.layout(), "Center");
      var1.add(DialogUtils.center(var3, var4), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
