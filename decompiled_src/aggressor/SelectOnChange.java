package aggressor;

import common.Callback;
import common.CommonUtils;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import ui.GenericTableModel;

public class SelectOnChange implements Callback, Runnable, Observer {
   public AggressorClient client;
   public JTable table;
   public GenericTableModel model;
   public String column = "";
   public String value = "";

   public SelectOnChange(AggressorClient var1, JTable var2, GenericTableModel var3, String var4) {
      this.client = var1;
      this.table = var2;
      this.model = var3;
      this.column = var4;
   }

   public void update(Observable var1, Object var2) {
      String var3 = (String)var2;
      this.client.getConnection().call("aggressor.ping", CommonUtils.args(var3), this);
   }

   public void result(String var1, Object var2) {
      this.value = (String)var2;
      if (this.value != null) {
         CommonUtils.runSafe(this);
      }

   }

   public void run() {
      ListSelectionModel var1 = this.table.getSelectionModel();
      List var2 = this.model.getRows();
      int var3 = -1;
      var1.setValueIsAdjusting(true);
      var1.clearSelection();
      Iterator var4 = var2.iterator();

      for(int var5 = 0; var4.hasNext(); ++var5) {
         Map var6 = (Map)var4.next();
         if (this.value.equals(var6.get(this.column))) {
            int var7 = this.table.convertRowIndexToView(var5);
            var1.addSelectionInterval(var7, var7);
            if (var7 > var3) {
               var3 = var7;
            }
         }
      }

      var1.setValueIsAdjusting(false);
      if (var3 > -1) {
         this.table.scrollRectToVisible(new Rectangle(this.table.getCellRect(var3, 0, true)));
      }

   }
}
