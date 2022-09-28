package aggressor;

import common.CommonUtils;
import common.Keys;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.MenuSelectionManager;
import ui.ColorPanel;
import ui.QueryRows;

public class ColorManager implements ActionListener {
   protected ColorPanel colors = new ColorPanel();
   protected String prefix;
   protected AggressorClient client;
   protected QueryRows rows;

   public ColorManager(AggressorClient var1, QueryRows var2, String var3) {
      this.colors.addActionListener(this);
      this.prefix = var3;
      this.client = var1;
      this.rows = var2;
   }

   public void actionPerformed(ActionEvent var1) {
      Map[] var2 = this.rows.getSelectedRows();
      HashMap var3 = new HashMap();
      var3.put("_accent", var1.getActionCommand());

      for(int var4 = 0; var4 < var2.length; ++var4) {
         this.client.getConnection().call(this.prefix + ".update", CommonUtils.args(Keys.ToKey(this.prefix, var2[var4]), var3));
      }

      this.client.getConnection().call(this.prefix + ".push");
      MenuSelectionManager.defaultManager().clearSelectedPath();
   }

   public JComponent getColorPanel() {
      return this.colors;
   }
}
