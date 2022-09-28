package aggressor.browsers;

import aggressor.AggressorClient;
import aggressor.ColorManager;
import common.AObject;
import common.AdjustData;
import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import ui.ATable;
import ui.GenericTableModel;
import ui.QueryRows;
import ui.TablePopup;

public class Services extends AObject implements AdjustData, TablePopup, QueryRows {
   protected AggressorClient client = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"address", "port", "banner", "note"};
   protected Set targets = null;

   public Services(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.targets = CommonUtils.toSet((Object[])var2);
   }

   public ActionListener cleanup() {
      return this.client.getData().unsubOnClose("services", this);
   }

   public Map format(String var1, Object var2) {
      Map var3 = (Map)var2;
      return this.targets.contains(var3.get("address")) ? var3 : null;
   }

   public JComponent getContent() {
      LinkedList var1 = this.client.getData().populateListAndSubscribe("services", this);
      this.model = DialogUtils.setupModel("address", this.cols, var1);
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      DialogUtils.sortby(this.table, 1);
      this.table.setPopupMenu(this);
      Map var2 = DialogUtils.toMap("address: 125, port: 60, banner: 250, note: 250");
      DialogUtils.setTableColumnWidths(this.table, var2);
      return DialogUtils.FilterAndScroll(this.table);
   }

   public Map[] getSelectedRows() {
      return this.model.getSelectedRows(this.table);
   }

   public void showPopup(MouseEvent var1) {
      JPopupMenu var2 = new JPopupMenu();
      Set var3 = CommonUtils.toSet(this.model.getSelectedValues(this.table));
      String[] var4 = CommonUtils.toArray((Collection)var3);
      Stack var5 = new Stack();
      var5.push(CommonUtils.toSleepArray(var4));
      this.client.getScriptEngine().getMenuBuilder().setupMenu(var2, "targets", var5);
      JMenu var6 = new JMenu("Color (Service)");
      var6.add((new ColorManager(this.client, this, "services")).getColorPanel());
      var2.add(var6);
      var2.show((Component)var1.getSource(), var1.getX(), var1.getY());
   }

   public void result(String var1, Object var2) {
      DialogUtils.setTable(this.table, this.model, CommonUtils.apply(var1, (List)var2, this));
   }
}
