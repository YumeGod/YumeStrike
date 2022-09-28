package aggressor.dialogs;

import aggressor.AggressorClient;
import common.AddressList;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class TargetDialog implements DialogListener {
   protected JFrame dialog = null;
   protected AggressorClient client = null;

   public TargetDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      HashMap var3 = new HashMap(var2);
      String var4 = DialogUtils.string(var2, "address");
      String var5 = DialogUtils.string(var3, "os");
      if (var5.equals("Windows 2000")) {
         var3.put("os", "Windows");
         var3.put("version", "5.0");
      } else if (var5.equals("Windows XP")) {
         var3.put("os", "Windows");
         var3.put("version", "5.1");
      } else if (var5.equals("Windows 7")) {
         var3.put("os", "Windows");
         var3.put("version", "6.0");
      } else if (var5.equals("Windows 8.1")) {
         var3.put("os", "Windows");
         var3.put("version", "6.2");
      } else if (var5.equals("Windows 10")) {
         var3.put("os", "Windows");
         var3.put("version", "10.0");
      }

      Iterator var6 = (new AddressList(var4)).toList().iterator();

      while(var6.hasNext()) {
         HashMap var7 = new HashMap(var3);
         var7.put("address", (String)var6.next());
         this.client.getConnection().call("targets.add", CommonUtils.args(CommonUtils.TargetKey(var7), var7));
      }

      this.client.getConnection().call("targets.push");
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Add Target", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set(CommonUtils.toMap("os", "Windows 7"));
      var1.text("address", "Address:", 20);
      var1.text("name", "Name:", 20);
      var1.combobox("os", "os:", CommonUtils.toArray("Android, Apple iOS, Cisco IOS, Firewall, FreeBSD, Linux, MacOS X, NetBSD, OpenBSD, Printer, Unknown, VMware, Windows 2000, Windows XP, Windows 7, Windows 8.1, Windows 10"));
      var1.text("note", "Note:", 20);
      JButton var2 = var1.action("Save");
      this.dialog.add(DialogUtils.description("Add a new target."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center((JComponent)var2), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
