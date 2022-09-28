package aggressor.browsers;

import aggressor.AggressorClient;
import aggressor.ColorManager;
import common.AObject;
import common.AdjustData;
import common.CommonUtils;
import common.ScriptUtils;
import dialog.ActivityPanel;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import filter.DataFilter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import ui.ATable;
import ui.GenericTableModel;
import ui.QueryRows;
import ui.TablePopup;

public class Credentials extends AObject implements ActionListener, AdjustData, TablePopup, QueryRows {
   protected AggressorClient client = null;
   protected ActivityPanel dialog = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"user", "password", "realm", "note", "source", "host", "added"};
   protected DataFilter filter = new DataFilter();

   public void setColumns(String var1) {
      this.cols = CommonUtils.toArray(var1);
   }

   public void noHashes() {
      this.filter.checkNTLMHash("password", true);
   }

   public DataFilter getFilter() {
      return this.filter;
   }

   public Credentials(AggressorClient var1) {
      this.client = var1;
   }

   public ActionListener cleanup() {
      return this.client.getData().unsubOnClose("credentials", this);
   }

   public WindowListener onclose() {
      return this.client.getData().unsubOnClose("credentials", this);
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Realm...".equals(var1.getActionCommand())) {
         SafeDialogs.ask("Set Domain to:", "", new SafeDialogCallback() {
            public void dialogResult(String var1) {
               Map[] var2 = Credentials.this.model.getSelectedRows(Credentials.this.table);

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  Credentials.this.client.getConnection().call("credentials.remove", CommonUtils.args(CommonUtils.CredKey(var2[var3])));
                  var2[var3].put("realm", var1);
                  Credentials.this.client.getConnection().call("credentials.add", CommonUtils.args(CommonUtils.CredKey(var2[var3]), var2[var3]));
               }

               Credentials.this.client.getConnection().call("credentials.push");
            }
         });
      } else if ("Note...".equals(var1.getActionCommand())) {
         SafeDialogs.ask("Set Note to:", "", new SafeDialogCallback() {
            public void dialogResult(String var1) {
               Map[] var2 = Credentials.this.model.getSelectedRows(Credentials.this.table);

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3].put("note", var1);
                  Credentials.this.client.getConnection().call("credentials.add", CommonUtils.args(CommonUtils.CredKey(var2[var3]), var2[var3]));
               }

               Credentials.this.client.getConnection().call("credentials.push");
            }
         });
      }

   }

   public Map format(String var1, Object var2) {
      return !this.filter.test((Map)var2) ? null : (Map)var2;
   }

   public JComponent getContent() {
      LinkedList var1 = this.client.getData().populateListAndSubscribe("credentials", this);
      this.model = DialogUtils.setupModel("user", this.cols, var1);
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      this.table.setPopupMenu(this);
      if (this.cols.length == 7) {
         DialogUtils.setupDateRenderer(this.getTable(), "added");
      }

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
      JMenuItem var3 = new JMenuItem("Realm...");
      JMenuItem var4 = new JMenuItem("Note...");
      var2.add(var3);
      var2.add(var4);
      JMenu var5 = new JMenu("Color");
      var5.add((new ColorManager(this.client, this, "credentials")).getColorPanel());
      var2.add(var5);
      var3.addActionListener(this);
      var4.addActionListener(this);
      Stack var6 = new Stack();
      var6.push(ScriptUtils.convertAll(this.model.getSelectedRows(this.table)));
      this.client.getScriptEngine().getMenuBuilder().setupMenu(var2, "credentials", var6);
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
