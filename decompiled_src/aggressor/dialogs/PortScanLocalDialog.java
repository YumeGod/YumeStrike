package aggressor.dialogs;

import aggressor.AggressorClient;
import beacon.TaskBeacon;
import common.AObject;
import common.AddressList;
import common.Callback;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import graph.Route;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import ui.ATable;
import ui.GenericTableModel;

public class PortScanLocalDialog extends AObject implements Callback, DialogListener {
   protected String bid = "";
   protected AggressorClient client = null;
   protected JFrame dialog = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"address", "netmask"};

   public PortScanLocalDialog(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
      this.model = DialogUtils.setupModel("address", this.cols, new LinkedList());
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = (String)this.model.getSelectedValueFromColumn(this.table, "address");
      String var4 = (String)this.model.getSelectedValueFromColumn(this.table, "netmask");
      String var5 = DialogUtils.string(var2, "discovery");
      String var6 = DialogUtils.string(var2, "ports");
      String var7 = DialogUtils.string(var2, "sockets");
      String var8 = AddressList.toIP(Route.ipToLong(var3) + (Route.ipToLong("255.255.255.255") - Route.ipToLong(var4)));
      DialogUtils.openOrActivate(this.client, this.bid);
      TaskBeacon var9 = new TaskBeacon(this.client, new String[]{this.bid});
      var9.input("portscan " + var3 + "-" + var8 + " " + var6 + " " + var5 + " " + var7);
      var9.PortScan(var3 + "-" + var8, var6, var5, CommonUtils.toNumber(var7, 1024));
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Scan", 480, 240);
      this.dialog.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      JScrollPane var1 = new JScrollPane(this.table);
      var1.setPreferredSize(new Dimension(var1.getWidth(), 100));
      DialogManager var2 = new DialogManager(this.dialog);
      var2.addDialogListener(this);
      HashMap var3 = new HashMap();
      var3.put("ports", "1-1024,3389,5000-6000");
      var3.put("discovery", "arp");
      var3.put("sockets", "1024");
      var2.set(var3);
      var2.text("ports", "Ports:");
      var2.text("sockets", "Max Sockets:");
      var2.combobox("discovery", "Discovery:", CommonUtils.toArray("arp, icmp, none"));
      JButton var4 = var2.action("Scan");
      JButton var5 = var2.help("https://www.cobaltstrike.com/help-portscan");
      this.dialog.add(var1, "Center");
      this.dialog.add(DialogUtils.stackTwo(var2.layout(), DialogUtils.center(var4, var5)), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
      this.client.getConnection().call("beacons.task_ipconfig", CommonUtils.args(this.bid), this);
   }

   public void result(String var1, Object var2) {
      LinkedList var3 = CommonUtils.parseTabData(var2 + "", CommonUtils.toArray("address, netmask"));
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Map var5 = (Map)var4.next();
         String var6 = (String)var5.get("address");
         String var7 = (String)var5.get("netmask");
         String var8 = AddressList.toIP(Route.ipToLong(var6) & Route.ipToLong(var7));
         var5.put("address", var8);
      }

      DialogUtils.setTable(this.table, this.model, var3);
   }
}
