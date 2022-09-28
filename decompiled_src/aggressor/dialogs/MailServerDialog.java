package aggressor.dialogs;

import common.AObject;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import phish.MailServer;
import phish.PhishingUtils;

public class MailServerDialog extends AObject implements DialogListener {
   protected JFrame dialog = null;
   protected SafeDialogCallback callback = null;
   protected String oldv = "";

   public MailServerDialog(String var1, SafeDialogCallback var2) {
      this.callback = var2;
      this.oldv = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "USERNAME");
      String var4 = DialogUtils.string(var2, "PASSWORD");
      int var5 = DialogUtils.number(var2, "Delay");
      String var6 = DialogUtils.string(var2, "connect");
      String var7 = DialogUtils.string(var2, "LHOST");
      int var8 = DialogUtils.number(var2, "LPORT");
      StringBuffer var9 = new StringBuffer();
      if (!"".equals(var3) && !"".equals(var4)) {
         var9.append(var3);
         var9.append(":");
         var9.append(var4);
         var9.append("@");
      }

      var9.append(var7);
      if (var8 != 25) {
         var9.append(":");
         var9.append(var8);
      }

      if ("SSL".equals(var6)) {
         var9.append("-ssl");
      } else if ("STARTTLS".equals(var6)) {
         var9.append("-starttls");
      }

      if (var5 > 0) {
         var9.append(",");
         var9.append(var5);
      }

      this.callback.dialogResult(var9.toString());
   }

   public void parseOld(DialogManager var1) {
      MailServer var2 = PhishingUtils.parseServerString(this.oldv);
      if (var2.username != null) {
         var1.set("USERNAME", var2.username);
      }

      if (var2.password != null) {
         var1.set("PASSWORD", var2.password);
      }

      var1.set("Delay", var2.delay + "");
      if (var2.lhost != null) {
         var1.set("LHOST", var2.lhost);
      }

      var1.set("LPORT", var2.lport + "");
      if (var2.starttls) {
         var1.set("connect", "STARTTLS");
      } else if (var2.ssl) {
         var1.set("connect", "SSL");
      } else {
         var1.set("connect", "Plain");
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("Mail Server", 320, 240);
      this.dialog.setLayout(new BorderLayout());
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      this.parseOld(var1);
      var1.text("LHOST", "SMTP Host:", 20);
      var1.text("LPORT", "SMTP Port:", 20);
      var1.text("USERNAME", "Username:", 20);
      var1.text("PASSWORD", "Password:", 20);
      var1.text("Delay", "Random Delay:", 20);
      var1.combobox("connect", "Connection:", CommonUtils.toArray("Plain, SSL, STARTTLS"));
      JButton var2 = var1.action("Set");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center((JComponent)var2), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
      this.dialog.show();
   }
}
