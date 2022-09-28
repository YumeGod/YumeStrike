package aggressor.dialogs;

import aggressor.DataManager;
import common.Callback;
import common.CommonUtils;
import common.TeamQueue;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class InterfaceDialog implements DialogListener, Callback {
   protected JFrame dialog = null;
   protected TeamQueue conn = null;
   protected DataManager datal = null;
   protected Callback notifyme = null;
   private static int intno = 0;

   public void notify(Callback var1) {
      this.notifyme = var1;
   }

   public InterfaceDialog(TeamQueue var1, DataManager var2) {
      this.conn = var1;
      this.datal = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "INTERFACE");
      String var4 = DialogUtils.string(var2, "HWADDRESS");
      String var5 = DialogUtils.string(var2, "PORT");
      String var6 = DialogUtils.string(var2, "CHANNEL");
      this.conn.call("cloudstrike.start_tap", CommonUtils.args(var3, var4, var5, var6), this);
      if (this.notifyme != null) {
         this.notifyme.result("interface create", var3);
      }

   }

   public void result(String var1, Object var2) {
      DialogUtils.showError(var2 + "");
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Setup Interface", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("INTERFACE", "phear" + intno);
      ++intno;
      var1.set("HWADDRESS", CommonUtils.randomMac());
      var1.set("PORT", CommonUtils.randomPort() + "");
      var1.set("CHANNEL", "UDP");
      var1.text("INTERFACE", "Interface:", 20);
      var1.text("HWADDRESS", "MAC Address:", 20);
      var1.text("PORT", "Local Port:", 20);
      var1.combobox("CHANNEL", "Channel:", new String[]{"HTTP", "ICMP", "TCP (Bind)", "TCP (Reverse)", "UDP"});
      JButton var2 = var1.action("Launch");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-covert-vpn");
      this.dialog.add(DialogUtils.description("Start a network interface and listener for CovertVPN. When a CovertVPN client is deployed, you will have a layer 2 tap into your target's network."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
