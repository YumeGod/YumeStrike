package console;

import javax.swing.JPanel;

public class AssociatedPanel extends JPanel implements Associated {
   protected String apanel_bid = "";

   public AssociatedPanel() {
   }

   public AssociatedPanel(String var1) {
      this.setBeaconID(var1);
   }

   public void setBeaconID(String var1) {
      this.apanel_bid = var1;
   }

   public String getBeaconID() {
      return this.apanel_bid;
   }
}
