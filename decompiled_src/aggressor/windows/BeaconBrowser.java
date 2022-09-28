package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import aggressor.browsers.Beacons;
import common.AObject;
import common.BeaconEntry;
import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class BeaconBrowser extends AObject implements ActionListener {
   protected AggressorClient client = null;
   protected Beacons browser;

   public BeaconBrowser(AggressorClient var1) {
      this.client = var1;
      this.browser = new Beacons(var1, true);
   }

   public ActionListener cleanup() {
      return this.browser.cleanup();
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Interact".equals(var1.getActionCommand())) {
         String var2 = this.browser.getSelectedValue() + "";
         BeaconEntry var3 = DataUtils.getBeacon(this.client.getData(), var2);
         BeaconConsole var4 = new BeaconConsole(var2, this.client);
         this.client.getTabManager().addTab(var3.title(), var4.getConsole(), var4.cleanup(), "Beacon console");
      } else if ("Remove".equals(var1.getActionCommand())) {
         Object[] var5 = this.browser.getSelectedValues();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            this.client.getConnection().call("beacons.remove", CommonUtils.args(var5[var6]));
         }
      }

   }

   public JComponent getContent() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      JButton var2 = new JButton("Interact");
      JButton var3 = new JButton("Remove");
      JButton var4 = new JButton("Help");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-beacon"));
      var1.add(this.browser.getContent(), "Center");
      var1.add(DialogUtils.center(var2, var3, var4), "South");
      return var1;
   }
}
