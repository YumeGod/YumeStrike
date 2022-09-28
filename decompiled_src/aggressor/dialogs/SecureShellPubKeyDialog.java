package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.TaskBeacon;
import common.BeaconEntry;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import ui.ATextField;

public class SecureShellPubKeyDialog implements DialogListener {
   protected JFrame dialog = null;
   protected AggressorClient client = null;
   protected ATextField user;
   protected ATextField pass;
   protected ATextField port;
   protected String[] targets;
   protected JCheckBox b;

   public SecureShellPubKeyDialog(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.targets = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "bid");
      BeaconEntry var4 = DataUtils.getBeacon(this.client.getData(), var3);
      TaskBeacon var5 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{var3});
      String var6 = DialogUtils.string(var2, "user");
      String var7 = DialogUtils.string(var2, "key");
      String var8 = DialogUtils.string(var2, "port");
      if ("".equals(var6)) {
         DialogUtils.showError("You must specify a user");
      } else if ("".equals(var7)) {
         DialogUtils.showError("You must specify a key file");
      } else if ("".equals(var8)) {
         DialogUtils.showError("You must specify a port");
      } else if (var4 == null) {
         DialogUtils.showError("You must select a Beacon session!");
      } else {
         byte[] var9 = CommonUtils.readFile(var7);
         DialogUtils.openOrActivate(this.client, var3);

         for(int var10 = 0; var10 < this.targets.length; ++var10) {
            String var11 = this.targets[var10];
            var5.input("ssh-key " + var11 + ":" + var8 + " " + var6 + " " + var7);
            var5.SecureShellPubKey(var6, var9, var11, CommonUtils.toNumber(var8, 22));
         }

      }
   }

   public void show() {
      this.dialog = DialogUtils.dialog("SSH Login (Key)", 580, 350);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("port", "22");
      var1.text("user", "User:", 24);
      var1.file("key", "PEM File:");
      var1.text("port", "Port:", 10);
      var1.beacon("bid", "Session:", this.client);
      JButton var2 = var1.action("Login");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-ssh");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
