package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.browsers.Applications;
import common.AObject;
import common.CommonUtils;
import dialog.ActivityPanel;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;

public class ApplicationManager extends AObject implements ActionListener {
   protected AggressorClient client = null;
   protected Applications browser = null;
   protected ActivityPanel dialog;

   public ApplicationManager(AggressorClient var1) {
      this.client = var1;
      this.browser = new Applications(var1);
   }

   public ActionListener cleanup() {
      return this.browser.cleanup();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Remove".equals(var1.getActionCommand())) {
         Map[] var2 = this.browser.getSelectedRows();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.client.getConnection().call("applications.remove", CommonUtils.args(CommonUtils.ApplicationKey(var2[var3])));
         }

         this.client.getConnection().call("applications.push");
      }

   }

   public JComponent getContent() {
      this.dialog = new ActivityPanel();
      this.dialog.setLayout(new BorderLayout());
      this.browser.notifyOnResult(this.dialog);
      JButton var1 = new JButton("Remove");
      JButton var2 = new JButton("Help");
      var1.addActionListener(this);
      var2.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-application-browser"));
      this.dialog.add(this.browser.getContent(), "Center");
      this.dialog.add(DialogUtils.center(var1, var2), "South");
      return this.dialog;
   }
}
