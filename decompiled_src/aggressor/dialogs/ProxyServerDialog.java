package aggressor.dialogs;

import common.AObject;
import common.CommonUtils;
import common.ProxyServer;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.LightSwitch;
import dialog.SafeDialogCallback;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ProxyServerDialog extends AObject implements DialogListener {
   protected JFrame dialog = null;
   protected SafeDialogCallback callback = null;
   protected String oldv = "";

   public ProxyServerDialog(String var1, SafeDialogCallback var2) {
      this.callback = var2;
      this.oldv = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      if ("Reset".equals(var1.getActionCommand())) {
         this.callback.dialogResult("");
      } else {
         ProxyServer var3 = ProxyServer.resolve(var2);
         this.callback.dialogResult(var3.toString());
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("(Manual) Proxy Settings", 320, 240);
      this.dialog.setLayout(new BorderLayout());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      LightSwitch var2 = new LightSwitch();
      var1.set(ProxyServer.parse(this.oldv).toMap());
      var1.combobox("ptype", "Proxy Type: ", CommonUtils.toArray("http, socks"));
      var1.text("phost", "Proxy Host: ", 20);
      var1.text("pport", "Proxy Port: ", 20);
      var1.text("puser", "Username: ", 20);
      var1.text("ppass", "Password: ", 20);
      var2.add((List)var1.getRows());
      var2.set(var1.checkbox_add("pdirect", "", "Ignore proxy settings; use direct connection"), true);
      JButton var3 = var1.action("Set");
      JButton var4 = var1.action("Reset");
      JButton var5 = var1.help("https://www.cobaltstrike.com/help-http-beacon#proxy");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var3, var4, var5), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
      this.dialog.show();
   }
}
