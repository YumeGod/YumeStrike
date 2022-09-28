package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.browsers.Sessions;
import common.AObject;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class SessionChooser extends AObject implements ActionListener {
   protected JFrame dialog = null;
   protected SafeDialogCallback callback = null;
   protected Sessions browser = null;

   public SessionChooser(AggressorClient var1, SafeDialogCallback var2) {
      this.callback = var2;
      this.browser = new Sessions(var1, false);
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Choose".equals(var1.getActionCommand())) {
         String var2 = (String)this.browser.getSelectedValue();
         this.dialog.setVisible(false);
         this.dialog.dispose();
         this.callback.dialogResult(var2);
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("Choose a Session", 800, 240);
      this.dialog.setLayout(new BorderLayout());
      JButton var1 = new JButton("Choose");
      var1.addActionListener(this);
      this.dialog.add(this.browser.getContent(), "Center");
      this.dialog.add(DialogUtils.center((JComponent)var1), "South");
      this.dialog.addWindowListener(this.browser.onclose());
      this.dialog.setVisible(true);
      this.dialog.show();
   }
}
