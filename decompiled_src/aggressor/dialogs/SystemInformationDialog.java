package aggressor.dialogs;

import aggressor.AggressorClient;
import common.Callback;
import common.CommonUtils;
import common.MudgeSanity;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SystemInformationDialog implements SafeDialogCallback, Callback, ActionListener {
   protected AggressorClient client;
   protected JTextArea contents;

   public SystemInformationDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void result(String var1, Object var2) {
      this.contents.append("\n\n*** Client Information ***\n\n");
      this.contents.append(MudgeSanity.systemInformation());
      this.contents.append("\n\n== Loaded Scripts ==\n\n");
      Iterator var3 = this.client.getScriptEngine().getScripts().iterator();

      while(var3.hasNext()) {
         this.contents.append(var3.next() + "\n");
      }

      this.contents.append("\n\n*** Team Server Information ***\n\n");
      this.contents.append(var2.toString());
   }

   public void actionPerformed(ActionEvent var1) {
      SafeDialogs.saveFile((JFrame)null, "debug.txt", this);
   }

   public void dialogResult(String var1) {
      CommonUtils.writeToFile(new File(var1), CommonUtils.toBytes(this.contents.getText()));
      DialogUtils.showInfo("Saved " + var1);
   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("System Information", 640, 480);
      this.contents = new JTextArea();
      JButton var2 = new JButton("Save");
      var2.addActionListener(this);
      var1.add(DialogUtils.description("This dialog provides information about your Cobalt Strike client and server. This information can greatly speed up support requests."), "North");
      var1.add(new JScrollPane(this.contents), "Center");
      var1.add(DialogUtils.center((JComponent)var2), "South");
      var1.setVisible(true);
      this.client.getConnection().call("aggressor.sysinfo", (Callback)this);
   }
}
