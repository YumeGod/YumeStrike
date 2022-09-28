package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.browsers.Beacons;
import common.AObject;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class BeaconChooser extends AObject implements ActionListener {
   protected JFrame dialog = null;
   protected SafeDialogCallback callback = null;
   protected Beacons browser = null;

   public BeaconChooser(AggressorClient var1, SafeDialogCallback var2) {
      this.callback = var2;
      this.browser = new Beacons(var1, false);
      this.browser.setColumns(new String[]{" ", "internal", "user", "computer", "note", "pid", "last"});
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
      this.dialog = DialogUtils.dialog("Choose a Beacon", 800, 240);
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
