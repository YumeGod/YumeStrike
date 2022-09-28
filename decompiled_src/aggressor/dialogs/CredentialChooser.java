package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.browsers.Credentials;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import filter.DataFilter;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class CredentialChooser implements DialogListener {
   protected JFrame dialog = null;
   protected AggressorClient client = null;
   protected Credentials browser;
   protected SafeDialogCallback callback;

   public CredentialChooser(AggressorClient var1, SafeDialogCallback var2) {
      this.client = var1;
      this.callback = var2;
      this.browser = new Credentials(var1);
      this.browser.setColumns("user, password, realm, note");
   }

   public DataFilter getFilter() {
      return this.browser.getFilter();
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = (String)this.browser.getSelectedValueFromColumn("user");
      String var4 = (String)this.browser.getSelectedValueFromColumn("realm");
      String var5 = (String)this.browser.getSelectedValueFromColumn("password");
      if (var4 != null && var4.length() != 0) {
         this.callback.dialogResult(var4 + "\\" + var3 + " " + var5);
      } else {
         this.callback.dialogResult(var3 + " " + var5);
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("Choose a Credential", 580, 200);
      this.dialog.addWindowListener(this.browser.onclose());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      JComponent var2 = this.browser.getContent();
      JButton var3 = var1.action("Choose");
      this.dialog.add(var2, "Center");
      this.dialog.add(DialogUtils.center((JComponent)var3), "South");
      this.dialog.setVisible(true);
   }
}
