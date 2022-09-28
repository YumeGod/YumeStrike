package aggressor.windows;

import aggressor.DataManager;
import aggressor.dialogs.InterfaceDialog;
import common.AObject;
import common.Callback;
import common.CommonUtils;
import common.TeamQueue;
import cortana.Cortana;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ui.ATable;
import ui.GenericTableModel;

public class InterfaceManager extends AObject implements Callback, ActionListener {
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"interface", "channel", "port", "mac", "client", "tx", "rx"};

   public InterfaceManager(DataManager var1, Cortana var2, TeamQueue var3) {
      this.engine = var2;
      this.conn = var3;
      this.data = var1;
      this.model = DialogUtils.setupModel("interface", this.cols, new LinkedList());
      var1.subscribe("interfaces", this);
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("interfaces", this);
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Add".equals(var1.getActionCommand())) {
         (new InterfaceDialog(this.conn, this.data)).show();
      } else if ("Remove".equals(var1.getActionCommand())) {
         String var2 = this.model.getSelectedValue(this.table) + "";
         String var3 = this.model.getSelectedValueFromColumn(this.table, "channel") + "";
         String var4 = this.model.getSelectedValueFromColumn(this.table, "port") + "";
         this.conn.call("cloudstrike.stop_tap", CommonUtils.args(var2));
         if ("TCP (Bind)".equals(var3)) {
            this.conn.call("beacons.pivot_stop_port", CommonUtils.args(var4));
         }
      }

   }

   public JComponent getContent() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      JButton var2 = new JButton("Add");
      JButton var3 = new JButton("Remove");
      JButton var4 = new JButton("Help");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-covert-vpn"));
      var1.add(DialogUtils.FilterAndScroll(this.table), "Center");
      var1.add(DialogUtils.center(var2, var3, var4), "South");
      return var1;
   }

   public void result(String var1, Object var2) {
      DialogUtils.setTable(this.table, this.model, (List)var2);
   }
}
