package ui;

import common.CommonUtils;
import filter.DataFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class GenericTableModel extends AbstractTableModel {
   protected String[] columnNames;
   protected List rows;
   protected String leadColumn;
   protected boolean[] editable;
   protected List all;
   protected DataFilter filter = null;

   public void apply(DataFilter var1) {
      synchronized(this) {
         List var3 = var1.apply(this.rows);
         this.rows = new ArrayList(var3.size());
         this.rows.addAll(var3);
         this.filter = var1;
      }

      this.fireListeners();
   }

   public void reset() {
      synchronized(this) {
         this.rows = new ArrayList(this.all.size());
         this.rows.addAll(this.all);
         this.filter = null;
      }

      this.fireListeners();
   }

   public List getRows() {
      return this.rows;
   }

   public List export() {
      synchronized(this) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = this.rows.iterator();

         while(var3.hasNext()) {
            var2.add(new HashMap((Map)var3.next()));
         }

         return var2;
      }
   }

   public GenericTableModel(String[] var1, String var2, int var3) {
      this.columnNames = var1;
      this.leadColumn = var2;
      this.rows = new ArrayList(var3);
      this.all = new ArrayList(var3);
      this.editable = new boolean[var1.length];

      for(int var4 = 0; var4 < this.editable.length; ++var4) {
         this.editable[var4] = false;
      }

   }

   public void setCellEditable(int var1) {
      this.editable[var1] = true;
   }

   public boolean isCellEditable(int var1, int var2) {
      return this.editable[var2];
   }

   public Object[] getSelectedValues(JTable var1) {
      synchronized(this) {
         int[] var3 = var1.getSelectedRows();
         Object[] var4 = new Object[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            int var6 = var1.convertRowIndexToModel(var3[var5]);
            if (var6 < this.rows.size() && var6 >= 0) {
               var4[var5] = ((Map)this.rows.get(var6)).get(this.leadColumn);
            } else {
               var4[var5] = null;
            }
         }

         return var4;
      }
   }

   public Map[] getSelectedRows(JTable var1) {
      synchronized(this) {
         int[] var3 = var1.getSelectedRows();
         HashMap[] var4 = new HashMap[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            int var6 = var1.convertRowIndexToModel(var3[var5]);
            var4[var5] = (Map)this.rows.get(var6);
         }

         return var4;
      }
   }

   public Object[][] getSelectedValuesFromColumns(JTable var1, String[] var2) {
      synchronized(this) {
         int[] var4 = var1.getSelectedRows();
         Object[][] var5 = new Object[var4.length][var2.length];

         for(int var6 = 0; var6 < var4.length; ++var6) {
            int var7 = var1.convertRowIndexToModel(var4[var6]);

            for(int var8 = 0; var8 < var2.length; ++var8) {
               var5[var6][var8] = ((Map)this.rows.get(var7)).get(var2[var8]);
            }
         }

         return var5;
      }
   }

   public Object getSelectedValue(JTable var1) {
      synchronized(this) {
         Object[] var3 = this.getSelectedValues(var1);
         return var3.length == 0 ? null : var3[0];
      }
   }

   public Object getValueAt(JTable var1, int var2, String var3) {
      synchronized(this) {
         var2 = var1.convertRowIndexToModel(var2);
         return var2 == -1 ? null : ((Map)this.rows.get(var2)).get(var3);
      }
   }

   public int getSelectedRow(JTable var1) {
      synchronized(this) {
         return var1.convertRowIndexToModel(var1.getSelectedRow());
      }
   }

   public void _setValueAtRow(int var1, String var2, String var3) {
      ((Map)this.rows.get(var1)).put(var2, var3);
   }

   public void setValueForKey(String var1, String var2, String var3) {
      int var4 = -1;
      synchronized(this) {
         Iterator var6 = this.rows.iterator();
         int var7 = 0;

         while(var6.hasNext()) {
            Map var8 = (Map)var6.next();
            if (!var1.equals(var8.get(this.leadColumn))) {
               ++var7;
            } else {
               var4 = var7;
               break;
            }
         }
      }

      if (var4 != -1) {
         this.setValueAtRow(var4, var2, var3);
      }

   }

   public void setValueAtRow(final int var1, final String var2, final String var3) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            GenericTableModel.this._setValueAtRow(var1, var2, var3);
         }
      });
   }

   public Object getSelectedValueFromColumn(JTable var1, String var2) {
      synchronized(this) {
         int var4 = var1.getSelectedRow();
         return var4 == -1 ? null : this.getValueAt(var1, var4, var2);
      }
   }

   public String getColumnName(int var1) {
      return this.columnNames[var1];
   }

   public int getColumnCount() {
      return this.columnNames.length;
   }

   public String[] getColumnNames() {
      return this.columnNames;
   }

   public void addEntry(final Map var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            GenericTableModel.this._addEntry(var1);
         }
      });
   }

   public void clear(final int var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            GenericTableModel.this._clear(var1);
         }
      });
   }

   public void fireListeners() {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            GenericTableModel.this.fireTableDataChanged();
         }
      });
   }

   public void _addEntry(Map var1) {
      synchronized(this) {
         if (this.filter == null || this.filter.test(var1)) {
            this.rows.add(var1);
         }

         this.all.add(var1);
         int var2 = this.rows.size() - 1;
      }
   }

   public void activateRow(JTable var1, int var2) {
      var2 = var1.convertRowIndexToView(var2);
      var1.setRowSelectionInterval(var2, var2);
      var1.scrollRectToVisible(var1.getCellRect(var2, 0, false));
   }

   public boolean isSelected(JTable var1, int var2) {
      try {
         var2 = var1.convertRowIndexToView(var2);
         return var1.isCellSelected(var2, 0);
      } catch (ArrayIndexOutOfBoundsException var4) {
         return false;
      }
   }

   public void _clear(int var1) {
      synchronized(this) {
         this.rows = new ArrayList(var1);
         this.all = new ArrayList(var1);
      }
   }

   public int getRowCount() {
      synchronized(this) {
         return this.rows.size();
      }
   }

   public Object getValueAtColumn(JTable var1, int var2, String var3) {
      synchronized(this) {
         var2 = var1.convertRowIndexToModel(var2);
         Map var5 = (Map)this.rows.get(var2);
         return var5.get(var3);
      }
   }

   public Object getValueAt(int var1, int var2) {
      synchronized(this) {
         if (var1 < this.rows.size()) {
            Map var4 = (Map)this.rows.get(var1);
            return var4.get(this.getColumnName(var2));
         } else {
            return null;
         }
      }
   }

   public void setValueAt(Object var1, int var2, int var3) {
      synchronized(this) {
         Map var5 = (Map)this.rows.get(var2);
         var5.put(this.getColumnName(var3), var1);
      }
   }
}
