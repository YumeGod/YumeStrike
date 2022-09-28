package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.browsers.Services;
import common.AObject;
import common.CommonUtils;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ServiceBrowser extends AObject implements ActionListener {
   protected AggressorClient client = null;
   protected Services browser = null;

   public ServiceBrowser(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.browser = new Services(var1, var2);
   }

   public ActionListener cleanup() {
      return this.browser.cleanup();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Remove".equals(var1.getActionCommand())) {
         Map[] var2 = this.browser.getSelectedRows();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.client.getConnection().call("services.remove", CommonUtils.args(CommonUtils.ServiceKey(var2[var3])));
         }

         this.client.getConnection().call("services.push");
      } else if ("Note...".equals(var1.getActionCommand())) {
         SafeDialogs.ask("Set Note to:", "", new SafeDialogCallback() {
            public void dialogResult(String var1) {
               Map[] var2 = ServiceBrowser.this.browser.getSelectedRows();

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].put("note", var1);
                  ServiceBrowser.this.client.getConnection().call("services.add", CommonUtils.args(CommonUtils.ServiceKey(var2[var3]), var2[var3]));
               }

               ServiceBrowser.this.client.getConnection().call("services.push");
            }
         });
      }

   }

   public JComponent getContent() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      JButton var2 = new JButton("Remove");
      JButton var3 = new JButton("Note...");
      JButton var4 = new JButton("Help");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-targets"));
      var1.add(this.browser.getContent(), "Center");
      var1.add(DialogUtils.center(var2, var3, var4), "South");
      return var1;
   }
}
