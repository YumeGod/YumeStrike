package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.browsers.Targets;
import aggressor.dialogs.ImportHosts;
import aggressor.dialogs.TargetDialog;
import common.AObject;
import common.CommonUtils;
import dialog.ActivityPanel;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;

public class TargetBrowser extends AObject implements ActionListener {
   protected AggressorClient client = null;
   protected Targets browser = null;
   protected ActivityPanel dialog = null;

   public TargetBrowser(AggressorClient var1) {
      this.client = var1;
      this.browser = new Targets(var1);
   }

   public ActionListener cleanup() {
      return this.browser.cleanup();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Add".equals(var1.getActionCommand())) {
         (new TargetDialog(this.client)).show();
      } else if ("Import".equals(var1.getActionCommand())) {
         SafeDialogs.openFile("Choose a file", (String)null, (String)null, true, false, new SafeDialogCallback() {
            public void dialogResult(String var1) {
               String[] var2 = CommonUtils.toArray(var1);
               new ImportHosts(TargetBrowser.this.client, var2);
            }
         });
      } else if ("Remove".equals(var1.getActionCommand())) {
         Map[] var2 = this.browser.getSelectedRows();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.client.getConnection().call("targets.remove", CommonUtils.args(CommonUtils.TargetKey(var2[var3])));
         }

         this.client.getConnection().call("targets.push");
      } else if ("Note...".equals(var1.getActionCommand())) {
         SafeDialogs.ask("Set Note to:", "", new SafeDialogCallback() {
            public void dialogResult(String var1) {
               Map[] var2 = TargetBrowser.this.browser.getSelectedRows();

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  HashMap var4 = new HashMap(var2[var3]);
                  var4.put("note", var1);
                  var4.remove("image");
                  TargetBrowser.this.client.getConnection().call("targets.add", CommonUtils.args(CommonUtils.TargetKey(var4), var4));
               }

               TargetBrowser.this.client.getConnection().call("targets.push");
            }
         });
      }

   }

   public JComponent getContent() {
      this.dialog = new ActivityPanel();
      this.dialog.setLayout(new BorderLayout());
      this.browser.notifyOnResult(this.dialog);
      JButton var1 = new JButton("Add");
      JButton var2 = new JButton("Import");
      JButton var3 = new JButton("Remove");
      JButton var4 = new JButton("Note...");
      JButton var5 = new JButton("Help");
      var1.addActionListener(this);
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(this);
      var5.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-targets"));
      this.dialog.add(this.browser.getContent(), "Center");
      this.dialog.add(DialogUtils.center(var1, var2, var3, var4, var5), "South");
      return this.dialog;
   }
}
