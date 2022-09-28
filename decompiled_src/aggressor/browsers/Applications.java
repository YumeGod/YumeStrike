package aggressor.browsers;

import aggressor.AggressorClient;
import aggressor.ColorManager;
import common.AObject;
import common.AdjustData;
import common.CommonUtils;
import dialog.ActivityPanel;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import ui.ATable;
import ui.GenericTableModel;
import ui.QueryRows;
import ui.TablePopup;

public class Applications extends AObject implements ActionListener, AdjustData, TablePopup, QueryRows {
   protected AggressorClient client = null;
   protected ActivityPanel dialog = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{" ", "external", "internal", "application", "version", "note", "date"};
   protected boolean nohashes = false;

   public Applications(AggressorClient var1) {
      this.client = var1;
   }

   public ActionListener cleanup() {
      return this.client.getData().unsubOnClose("applications", this);
   }

   public WindowListener onclose() {
      return this.client.getData().unsubOnClose("applications", this);
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Note...".equals(var1.getActionCommand())) {
         SafeDialogs.ask("Set Note to:", "", new SafeDialogCallback() {
            public void dialogResult(String var1) {
               Map[] var2 = Applications.this.model.getSelectedRows(Applications.this.table);

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].put("note", var1);
                  Applications.this.client.getConnection().call("applications.update", CommonUtils.args(CommonUtils.ApplicationKey(var2[var3]), var2[var3]));
               }

               Applications.this.client.getConnection().call("applications.push");
            }
         });
      }

   }

   public Map format(String var1, Object var2) {
      HashMap var3 = new HashMap((Map)var2);
      String var4 = var3.get("os") + "";
      String var5 = var3.get("osver") + "";
      ImageIcon var6 = DialogUtils.TargetVisualizationSmall(var4, CommonUtils.toDoubleNumber(var5, 0.0), false, false);
      var3.put("image", var6);
      return var3;
   }

   public JComponent getContent() {
      LinkedList var1 = this.client.getData().populateListAndSubscribe("applications", this);
      this.model = DialogUtils.setupModel("nonce", this.cols, var1);
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      this.table.setPopupMenu(this);
      this.table.getColumn(" ").setPreferredWidth(32);
      this.table.getColumn(" ").setMaxWidth(32);
      DialogUtils.setupImageRenderer(this.table, this.model, " ", "image");
      DialogUtils.setupDateRenderer(this.table, "date");
      DialogUtils.sortby(this.table, 6);
      return DialogUtils.FilterAndScroll(this.table);
   }

   public JTable getTable() {
      return this.table;
   }

   public Object getSelectedValueFromColumn(String var1) {
      return this.model.getSelectedValueFromColumn(this.table, var1);
   }

   public Map[] getSelectedRows() {
      return this.model.getSelectedRows(this.table);
   }

   public void showPopup(MouseEvent var1) {
      JPopupMenu var2 = new JPopupMenu();
      JMenuItem var3 = new JMenuItem("Note...");
      var3.addActionListener(this);
      var2.add(var3);
      JMenu var4 = new JMenu("Color");
      var4.add((new ColorManager(this.client, this, "applications")).getColorPanel());
      var2.add(var4);
      var2.show((Component)var1.getSource(), var1.getX(), var1.getY());
   }

   public void notifyOnResult(ActivityPanel var1) {
      this.dialog = var1;
   }

   public void result(String var1, Object var2) {
      LinkedList var3 = CommonUtils.apply(var1, (LinkedList)var2, this);
      DialogUtils.setTable(this.table, this.model, var3);
      if (this.dialog != null) {
         this.dialog.touch();
      }

   }
}
