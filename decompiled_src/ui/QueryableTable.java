package ui;

import java.util.Map;
import javax.swing.JTable;

public class QueryableTable implements QueryRows {
   protected JTable table;
   protected GenericTableModel model;

   public QueryableTable(JTable var1, GenericTableModel var2) {
      this.table = var1;
      this.model = var2;
   }

   public Map[] getSelectedRows() {
      return this.model.getSelectedRows(this.table);
   }
}
