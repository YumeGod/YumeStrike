package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.browsers.Credentials;
import beacon.TaskBeacon;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ui.ATextField;

public class SpawnAsDialog implements DialogListener, ListSelectionListener {
   protected JFrame dialog = null;
   protected AggressorClient client = null;
   protected ATextField user;
   protected ATextField pass;
   protected ATextField domain;
   protected Credentials browser;
   protected String bid;

   public SpawnAsDialog(AggressorClient var1, String var2) {
      this.client = var1;
      this.browser = new Credentials(var1);
      this.browser.setColumns("user, password, realm, note");
      this.browser.noHashes();
      this.bid = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "listener");
      TaskBeacon var4 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
      DialogUtils.openOrActivate(this.client, this.bid);
      var4.input("spawnas " + this.domain.getText() + "\\" + this.user.getText() + " " + this.pass.getText() + " " + var3);
      var4.SpawnAs(this.domain.getText(), this.user.getText(), this.pass.getText(), var3);
   }

   public void valueChanged(ListSelectionEvent var1) {
      if (!var1.getValueIsAdjusting()) {
         this.user.setText((String)this.browser.getSelectedValueFromColumn("user"));
         this.pass.setText((String)this.browser.getSelectedValueFromColumn("password"));
         this.domain.setText((String)this.browser.getSelectedValueFromColumn("realm"));
      }
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Spawn As", 580, 400);
      this.dialog.addWindowListener(this.browser.onclose());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      JComponent var2 = this.browser.getContent();
      this.browser.getTable().getSelectionModel().addListSelectionListener(this);
      this.user = (ATextField)var1.text("user", "User:", 36).get(1);
      this.pass = (ATextField)var1.text("pass", "Password:", 36).get(1);
      this.domain = (ATextField)var1.text("domain", "Domain:", 36).get(1);
      var1.sc_listener_all("listener", "Listener:", this.client);
      JButton var3 = var1.action("Launch");
      JButton var4 = var1.help("https://www.cobaltstrike.com/help-spawnas");
      this.dialog.add(var2, "Center");
      this.dialog.add(DialogUtils.stackTwo(var1.layout(), DialogUtils.center(var3, var4)), "South");
      this.dialog.setVisible(true);
   }
}
