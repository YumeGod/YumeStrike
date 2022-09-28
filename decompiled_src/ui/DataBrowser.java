package ui;

import aggressor.Prefs;
import common.CommonUtils;
import console.Activity;
import cortana.Cortana;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DataBrowser extends JComponent implements ListSelectionListener, Activity, TablePopup {
   protected JSplitPane split = new JSplitPane(1);
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected LinkedList listeners = new LinkedList();
   protected String hook = "";
   protected Cortana engine;
   protected String col;
   protected JLabel label;
   protected Color original;

   public void registerLabel(JLabel var1) {
      this.original = var1.getForeground();
      this.label = var1;
   }

   public void resetNotification() {
      this.label.setForeground(this.original);
   }

   public static DataBrowser getBeaconDataBrowser(Cortana var0, String var1, JComponent var2, LinkedList var3) {
      return new DataBrowser(var0, var1, CommonUtils.toArray("user, computer, pid, when"), var2, var3, "beacon", "id");
   }

   public DataBrowser(Cortana var1, String var2, String[] var3, JComponent var4, LinkedList var5, String var6, String var7) {
      this.hook = var6;
      this.engine = var1;
      this.col = var7;
      this.setLayout(new BorderLayout());
      this.add(this.split, "Center");
      this.model = DialogUtils.setupModel(var2, var3, var5);
      this.table = DialogUtils.setupTable(this.model, var3, false);
      this.table.setPopupMenu(this);
      this.table.getSelectionModel().addListSelectionListener(this);
      this.split.add(DialogUtils.FilterAndScroll(this.table));
      this.split.add(var4);
   }

   public void valueChanged(ListSelectionEvent var1) {
      if (!var1.getValueIsAdjusting()) {
         Iterator var2 = this.listeners.iterator();

         while(var2.hasNext()) {
            DataSelectionListener var3 = (DataSelectionListener)var2.next();
            var3.selected(this.getSelectedValue());
         }

      }
   }

   public void addDataSelectionListener(DataSelectionListener var1) {
      this.listeners.add(var1);
   }

   public Object getSelectedValue() {
      return this.model.getSelectedValue(this.table);
   }

   public ATable getTable() {
      return this.table;
   }

   public void showPopup(MouseEvent var1) {
      Object[] var2 = new Object[]{this.model.getSelectedValueFromColumn(this.table, this.col)};
      Stack var3 = new Stack();
      var3.push(CommonUtils.toSleepArray(var2));
      this.engine.getMenuBuilder().installMenu(var1, this.hook, var3);
   }

   public void addEntry(final Map var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            DataBrowser.this.table.markSelections();
            DataBrowser.this.model.addEntry(var1);
            DataBrowser.this.model.fireListeners();
            DataBrowser.this.table.restoreSelections();
            if (!DataBrowser.this.isShowing()) {
               DataBrowser.this.label.setForeground(Prefs.getPreferences().getColor("tab.highlight.color", "#0000ff"));
            }

         }
      });
   }

   public void setTable(final Collection var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            DialogUtils.setTable(DataBrowser.this.table, DataBrowser.this.model, var1);
            if (!DataBrowser.this.isShowing()) {
               DataBrowser.this.label.setForeground(Prefs.getPreferences().getColor("tab.highlight.color", "#0000ff"));
            }

         }
      });
   }
}
