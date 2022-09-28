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
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ui.ATable;
import ui.GenericTableModel;

public class CovertVPNSetup extends AObject implements Callback, DialogListener {
   protected String bid = "";
   protected AggressorClient client = null;
   protected JFrame dialog = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"IPv4 Address", "IPv4 Netmask", "Hardware MAC"};

   public CovertVPNSetup(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
      this.model = DialogUtils.setupModel("IPv4 Address", this.cols, new LinkedList());
   }

   public void refresh() {
      this.client.getConnection().call("beacons.task_ipconfig", CommonUtils.args(this.bid), this);
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "VPNInterface");
      String var4 = this.model.getSelectedValueFromColumn(this.table, "IPv4 Address") + "";
      String var5 = this.model.getSelectedValueFromColumn(this.table, "Hardware MAC") + "";
      if (!DialogUtils.bool(var2, "CloneMAC")) {
         var5 = null;
      }

      if (var2.get("VPNInterface") == null) {
         DialogUtils.showError("Please select or add a VPN interface");
      } else if (this.model.getSelectedValueFromColumn(this.table, "IPv4 Address") == null) {
         DialogUtils.showError("Please select a network interface");
      } else {
         if (!DialogUtils.isShift(var1)) {
            this.dialog.setVisible(false);
         }

         DialogUtils.openOrActivate(this.client, this.bid);
         TaskBeacon var6 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
         var6.input("covertvpn " + var3 + " " + var4);
         var6.CovertVPN(this.bid, var3, var4, var5);
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("Deploy VPN Client", 480, 240);
      this.dialog.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      JScrollPane var1 = new JScrollPane(this.table);
      var1.setPreferredSize(new Dimension(var1.getWidth(), 100));
      DialogManager var2 = new DialogManager(this.dialog);
      var2.addDialogListener(this);
      var2.set("CloneMAC", "true");
      var2.interfaces("VPNInterface", "Local Interface: ", this.client.getConnection(), this.client.getData());
      JComponent var3 = var2.row();
      JCheckBox var4 = var2.checkbox("CloneMAC", "Clone host MAC address");
      JButton var5 = var2.action_noclose("Deploy");
      JButton var6 = var2.help("https://www.cobaltstrike.com/help-covert-vpn");
      JPanel var7 = new JPanel();
      var7.setLayout(new BorderLayout());
      var7.add(var3, "North");
      var7.add(var4, "Center");
      var7.add(DialogUtils.center(var5, var6), "South");
      this.dialog.add(var1, "Center");
      this.dialog.add(var7, "South");
      this.refresh();
      this.dialog.setVisible(true);
   }

   public void result(String var1, Object var2) {
      LinkedList var3 = CommonUtils.parseTabData(var2 + "", CommonUtils.toArray("IPv4 Address, IPv4 Netmask, MTU, Hardware MAC"));
      DialogUtils.setTable(this.table, this.model, var3);
   }
}
