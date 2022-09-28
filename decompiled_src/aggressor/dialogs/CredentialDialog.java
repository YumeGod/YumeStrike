package aggressor.dialogs;

import aggressor.AggressorClient;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class CredentialDialog implements DialogListener {
   protected JFrame dialog;
   protected Map options;
   protected String title;
   protected AggressorClient client;

   public CredentialDialog(AggressorClient var1) {
      this(var1, new HashMap());
      this.title = "New Credential";
      this.options.put("source", "manual");
   }

   public CredentialDialog(AggressorClient var1, Map var2) {
      this.dialog = null;
      this.options = new HashMap();
      this.title = "Edit Credential";
      this.client = null;
      this.client = var1;
      this.options = new HashMap(var2);
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = CommonUtils.CredKey(this.options);
      String var4 = CommonUtils.CredKey(var2);
      if (!var3.equals(var4)) {
         this.client.getConnection().call("credentials.remove", CommonUtils.args(var3));
      }

      this.client.getConnection().call("credentials.add", CommonUtils.args(var4, var2));
      this.client.getConnection().call("credentials.push");
      if (this.title.equals("Edit Credential")) {
         this.options = new HashMap(var2);
      } else {
         this.options = new HashMap();
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog(this.title, 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set(this.options);
      var1.text("user", "User:", 20);
      var1.text("password", "Password:", 20);
      var1.text("realm", "Realm:", 20);
      var1.text("note", "Note:", 20);
      var1.combobox("source", "Source:", CommonUtils.toArray("hashdump, manual, mimikatz"));
      var1.text("host", "Host:", 20);
      JButton var2 = var1.action("Save");
      this.dialog.add(DialogUtils.description("Edit credential store."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center((JComponent)var2), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
