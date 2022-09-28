package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.browsers.Credentials;
import aggressor.dialogs.CredentialDialog;
import common.AObject;
import common.CommonUtils;
import dialog.ActivityPanel;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class CredentialManager extends AObject implements ActionListener {
   protected AggressorClient client = null;
   protected Credentials browser = null;
   protected ActivityPanel dialog;

   public CredentialManager(AggressorClient var1) {
      this.client = var1;
      this.browser = new Credentials(var1);
   }

   public ActionListener cleanup() {
      return this.browser.cleanup();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Add".equals(var1.getActionCommand())) {
         (new CredentialDialog(this.client)).show();
      } else {
         Map[] var2;
         int var3;
         if ("Edit".equals(var1.getActionCommand())) {
            var2 = this.browser.getSelectedRows();

            for(var3 = 0; var3 < var2.length; ++var3) {
               (new CredentialDialog(this.client, var2[var3])).show();
            }
         } else if ("Remove".equals(var1.getActionCommand())) {
            var2 = this.browser.getSelectedRows();

            for(var3 = 0; var3 < var2.length; ++var3) {
               this.client.getConnection().call("credentials.remove", CommonUtils.args(CommonUtils.CredKey(var2[var3])));
            }

            this.client.getConnection().call("credentials.push");
         } else {
            final StringBuffer var7;
            if ("Copy".equals(var1.getActionCommand())) {
               var7 = new StringBuffer();
               Map[] var8 = this.browser.getSelectedRows();

               for(int var4 = 0; var4 < var8.length; ++var4) {
                  if (var7.length() > 0) {
                     var7.append("\n");
                  }

                  if (!"".equals(var8[var4].get("realm"))) {
                     var7.append(var8[var4].get("realm"));
                     var7.append("\\");
                  }

                  var7.append(var8[var4].get("user"));
                  var7.append(" ");
                  var7.append(var8[var4].get("password"));
               }

               DialogUtils.addToClipboard(var7.toString());
            } else if ("Export".equals(var1.getActionCommand())) {
               var7 = new StringBuffer();
               var7.append("# Cobalt Strike Credential Export\n");
               var7.append("# " + CommonUtils.formatTime(System.currentTimeMillis()) + "\n\n");

               for(Iterator var9 = this.client.getData().getListSafe("credentials").iterator(); var9.hasNext(); var7.append("\n")) {
                  Map var10 = (Map)var9.next();
                  if (!"".equals(var10.get("realm"))) {
                     var7.append(var10.get("realm"));
                     var7.append("\\");
                  }

                  String var5 = var10.get("user") + "";
                  String var6 = var10.get("password") + "";
                  if (var6.length() == 32) {
                     var7.append(var5);
                     var7.append(":::");
                     var7.append(var6);
                     var7.append(":::");
                  } else {
                     var7.append(var5);
                     var7.append(" ");
                     var7.append(var6);
                  }
               }

               SafeDialogs.saveFile((JFrame)null, "credentials.txt", new SafeDialogCallback() {
                  public void dialogResult(String var1) {
                     CommonUtils.writeToFile(new File(var1), CommonUtils.toBytes(var7.toString()));
                     DialogUtils.showInfo("Exported Credentials");
                  }
               });
            }
         }
      }

   }

   public JComponent getContent() {
      this.dialog = new ActivityPanel();
      this.dialog.setLayout(new BorderLayout());
      this.browser.notifyOnResult(this.dialog);
      JButton var1 = new JButton("Add");
      JButton var2 = new JButton("Edit");
      JButton var3 = new JButton("Copy");
      JButton var4 = new JButton("Remove");
      JButton var5 = new JButton("Export");
      JButton var6 = new JButton("Help");
      var1.addActionListener(this);
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(this);
      var5.addActionListener(this);
      var6.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-credential-management"));
      this.dialog.add(this.browser.getContent(), "Center");
      this.dialog.add(DialogUtils.center(var1, var2, var3, var5, var4, var6), "South");
      return this.dialog;
   }
}
