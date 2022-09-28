package aggressor.dialogs;

import aggressor.AggressorClient;
import beacon.TaskBeacon;
import common.AObject;
import common.Callback;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ui.ATable;
import ui.GenericTableModel;

public class BrowserPivotSetup extends AObject implements Callback, DialogListener {
   protected String bid = "";
   protected AggressorClient client = null;
   protected JFrame dialog = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"PID", "PPID", "Arch", "Name", "User", " "};

   public BrowserPivotSetup(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
      this.model = DialogUtils.setupModel("PID", this.cols, new LinkedList());
   }

   public void refresh() {
      this.client.getConnection().call("beacons.task_ps", CommonUtils.args(this.bid), this);
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      int var3 = Integer.parseInt(this.model.getSelectedValueFromColumn(this.table, "PID") + "");
      String var4 = this.model.getSelectedValueFromColumn(this.table, "Arch") + "";
      int var5 = DialogUtils.number(var2, "ProxyPort");
      TaskBeacon var6 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
      DialogUtils.openOrActivate(this.client, this.bid);
      var6.input("browserpivot " + var3 + " " + var4);
      var6.BrowserPivot(this.bid, var3, var4, var5);
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Browser Pivot", 680, 240);
      this.dialog.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      Map var1 = DialogUtils.toMap("PID: 60, PPID: 60, Arch: 60, Name: 120, User: 240");
      var1.put(" ", "20");
      DialogUtils.setTableColumnWidths(this.table, var1);
      JScrollPane var2 = new JScrollPane(this.table);
      var2.setPreferredSize(new Dimension(var2.getWidth(), 100));
      DialogManager var3 = new DialogManager(this.dialog);
      var3.addDialogListener(this);
      var3.set("ProxyPort", CommonUtils.randomPort() + "");
      var3.text("ProxyPort", "Proxy Server Port:");
      JButton var4 = var3.action("Launch");
      JButton var5 = var3.help("https://www.cobaltstrike.com/help-browser-pivoting");
      JPanel var6 = new JPanel();
      var6.setLayout(new BorderLayout());
      var6.add(var3.row(), "North");
      var6.add(DialogUtils.center(var4, var5), "South");
      this.dialog.add(var2, "Center");
      this.dialog.add(var6, "South");
      this.refresh();
      this.dialog.setVisible(true);
   }

   public void result(String var1, Object var2) {
      LinkedList var3 = new LinkedList();
      HashMap var4 = new HashMap();
      String[] var5 = var2.toString().trim().split("\n");

      String var9;
      for(int var6 = 0; var6 < var5.length; ++var6) {
         String[] var7 = var5[var6].split("\t");
         HashMap var8 = new HashMap();
         if (var7.length >= 1) {
            var8.put("Name", var7[0]);
         }

         if (var7.length >= 2) {
            var8.put("PPID", var7[1]);
         }

         if (var7.length >= 3) {
            var8.put("PID", var7[2]);
         }

         if (var7.length >= 4) {
            var8.put("Arch", var7[3]);
         }

         if (var7.length >= 5) {
            var8.put("User", var7[4]);
         }

         if (var7.length >= 6) {
            var8.put("Session", var7[5]);
         }

         if (var7.length >= 3) {
            var4.put(var7[2], var7[0].toLowerCase());
         }

         var9 = (var8.get("Name") + "").toLowerCase();
         if (var9.equals("explorer.exe") || var9.equals("iexplore.exe")) {
            var3.add(var8);
         }
      }

      Iterator var10 = var3.iterator();

      while(var10.hasNext()) {
         Map var11 = (Map)var10.next();
         String var12 = var11.get("PPID") + "";
         var9 = var4.get(var12) + "";
         if ("iexplore.exe".equals(var9)) {
            var11.put(" ", '✓');
         }
      }

      DialogUtils.setTable(this.table, this.model, var3);
   }
}
