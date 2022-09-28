package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.TabManager;
import beacon.BeaconPivot;
import common.AObject;
import common.AdjustData;
import common.BeaconEntry;
import common.CommonUtils;
import common.TeamQueue;
import cortana.Cortana;
import dialog.ActivityPanel;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import ui.ATable;
import ui.GenericTableModel;
import ui.TablePopup;

public class SOCKSBrowser extends AObject implements AdjustData, ActionListener, TablePopup {
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected TabManager manager = null;
   protected AggressorClient client = null;
   protected ActivityPanel dialog = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"user", "computer", "pid", "type", "port", "fhost", "fport"};

   public SOCKSBrowser(AggressorClient var1) {
      this.client = var1;
      this.engine = var1.getScriptEngine();
      this.conn = var1.getConnection();
      this.data = var1.getData();
      this.manager = var1.getTabManager();
   }

   public Map format(String var1, Object var2) {
      Map var3 = (Map)var2;
      String var4 = var3.get("bid") + "";
      BeaconEntry var5 = DataUtils.getBeacon(this.data, var4);
      if (var5 != null) {
         var3.put("user", var5.getUser());
         var3.put("computer", var5.getComputer());
         var3.put("pid", var5.getPid());
      }

      return var3;
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("socks", this);
   }

   public void actionPerformed(ActionEvent var1) {
      BeaconPivot[] var2 = BeaconPivot.resolve(this.client, this.model.getSelectedRows(this.table));
      int var3;
      if ("Stop".equals(var1.getActionCommand())) {
         for(var3 = 0; var3 < var2.length; ++var3) {
            var2[var3].die();
         }
      } else if ("Tunnel".equals(var1.getActionCommand())) {
         for(var3 = 0; var3 < var2.length; ++var3) {
            var2[var3].tunnel();
         }
      }

   }

   public void showPopup(MouseEvent var1) {
      DialogUtils.showSessionPopup(this.client, var1, this.model.getSelectedValues(this.table));
   }

   public JComponent getContent() {
      LinkedList var1 = this.data.populateListAndSubscribe("socks", this);
      this.model = DialogUtils.setupModel("bid", this.cols, var1);
      this.dialog = new ActivityPanel();
      this.dialog.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      this.table.setPopupMenu(this);
      JButton var2 = new JButton("Stop");
      JButton var3 = new JButton("Tunnel");
      JButton var4 = new JButton("Help");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-socks-proxy-pivoting"));
      this.dialog.add(DialogUtils.FilterAndScroll(this.table), "Center");
      this.dialog.add(DialogUtils.center(var2, var3, var4), "South");
      return this.dialog;
   }

   public void result(String var1, Object var2) {
      LinkedList var3 = CommonUtils.apply(var1, (List)var2, this);
      DialogUtils.setTable(this.table, this.model, var3);
      this.dialog.touch();
   }
}
