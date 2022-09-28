package aggressor;

import common.CommonUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.MenuSelectionManager;
import ui.ColorPanel;

public class ColorManagerScripted implements ActionListener {
   protected ColorPanel colors = new ColorPanel();
   protected String prefix;
   protected AggressorClient client;
   protected String[] ids;

   public ColorManagerScripted(AggressorClient var1, String var2, String[] var3) {
      this.colors.addActionListener(this);
      this.prefix = var2;
      this.client = var1;
      this.ids = var3;
   }

   public void actionPerformed(ActionEvent var1) {
      HashMap var2 = new HashMap();
      var2.put("_accent", var1.getActionCommand());

      for(int var3 = 0; var3 < this.ids.length; ++var3) {
         this.client.getConnection().call(this.prefix + ".update", CommonUtils.args(this.ids[var3], var2));
      }

      this.client.getConnection().call(this.prefix + ".push");
      MenuSelectionManager.defaultManager().clearSelectedPath();
   }

   public JComponent getColorPanel() {
      return this.colors;
   }
}
