package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import aggressor.browsers.Credentials;
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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ui.ATextField;

public class SecureShellDialog implements DialogListener, ListSelectionListener {
   protected JFrame dialog = null;
   protected AggressorClient client = null;
   protected ATextField user;
   protected ATextField pass;
   protected ATextField port;
   protected Credentials browser;
   protected String[] targets;
   protected JCheckBox b;

   public SecureShellDialog(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.browser = new Credentials(var1);
      this.targets = var2;
      this.browser.setColumns("user, password, realm, note");
      this.browser.noHashes();
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "bid");
      BeaconEntry var4 = DataUtils.getBeacon(this.client.getData(), var3);
      TaskBeacon var5 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{var3});
      String var6 = DialogUtils.string(var2, "user");
      String var7 = DialogUtils.string(var2, "pass");
      String var8 = DialogUtils.string(var2, "port");
      if ("".equals(var6)) {
         DialogUtils.showError("You must specify a user");
      } else if ("".equals(var7)) {
         DialogUtils.showError("You must specify a password");
      } else if ("".equals(var8)) {
         DialogUtils.showError("You must specify a port");
      } else if (var4 == null) {
         DialogUtils.showError("You must select a Beacon session!");
      } else {
         DialogUtils.openOrActivate(this.client, var3);

         for(int var9 = 0; var9 < this.targets.length; ++var9) {
            String var10 = this.targets[var9];
            var5.input("ssh " + var10 + ":" + var8 + " " + var6 + " " + var7);
            var5.SecureShell(var6, var7, var10, CommonUtils.toNumber(var8, 22));
         }

      }
   }

   public void valueChanged(ListSelectionEvent var1) {
      if (!var1.getValueIsAdjusting()) {
         this.user.setText((String)this.browser.getSelectedValueFromColumn("user"));
         this.pass.setText((String)this.browser.getSelectedValueFromColumn("password"));
      }
   }

   public void show() {
      this.dialog = DialogUtils.dialog("SSH Login", 580, 350);
      this.dialog.addWindowListener(this.browser.onclose());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("port", "22");
      JComponent var2 = this.browser.getContent();
      this.browser.getTable().getSelectionModel().addListSelectionListener(this);
      this.user = (ATextField)var1.text("user", "User:", 36).get(1);
      this.pass = (ATextField)var1.text("pass", "Password:", 36).get(1);
      var1.text("port", "Port:", 10);
      var1.beacon("bid", "Session:", this.client);
      JButton var3 = var1.action("Login");
      JButton var4 = var1.help("https://www.cobaltstrike.com/help-ssh");
      this.dialog.add(var2, "Center");
      this.dialog.add(DialogUtils.stack(var1.layout(), DialogUtils.center(var3, var4)), "South");
      this.dialog.setVisible(true);
   }
}
