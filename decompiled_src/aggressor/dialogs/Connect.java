package aggressor.dialogs;

import aggressor.Aggressor;
import aggressor.AggressorClient;
import aggressor.MultiFrame;
import aggressor.Prefs;
import common.Callback;
import common.CommonUtils;
import common.MudgeSanity;
import common.TeamQueue;
import common.TeamSocket;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ssl.ArmitageTrustListener;
import ssl.SecureSocket;

public class Connect implements DialogListener, Callback, ArmitageTrustListener {
   protected MultiFrame window;
   protected TeamQueue tqueue = null;
   protected String desc = "";
   protected Map options = null;

   public Connect(MultiFrame var1) {
      this.window = var1;
   }

   public boolean trust(String var1) {
      HashSet var2 = new HashSet(Prefs.getPreferences().getList("trusted.servers"));
      if (var2.contains(var1)) {
         return true;
      } else {
         int var3 = JOptionPane.showConfirmDialog((Component)null, "The team server's fingerprint is:\n\n<html><body><b>" + var1 + "</b></body></html>\n\nDoes this match the fingerprint shown when the team server started?", "VerifyFingerprint", 0);
         if (var3 == 0) {
            Prefs.getPreferences().appendList("trusted.servers", var1);
            Prefs.getPreferences().save();
            return true;
         } else {
            return false;
         }
      }
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      String var3 = var2.get("user") + "";
      String var4 = var2.get("host") + "";
      String var5 = var2.get("port") + "";
      String var6 = var2.get("pass") + "";
      Prefs.getPreferences().appendList("connection.profiles", var4);
      Prefs.getPreferences().set("connection.profiles." + var4 + ".user", var3);
      Prefs.getPreferences().set("connection.profiles." + var4 + ".port", var5);
      Prefs.getPreferences().set("connection.profiles." + var4 + ".password", var6);
      Prefs.getPreferences().save();
      this.desc = var3 + "@" + var4;

      try {
         SecureSocket var7 = new SecureSocket(var4, Integer.parseInt(var5), this);
         var7.authenticate(var6);
         TeamSocket var8 = new TeamSocket(var7.getSocket());
         this.tqueue = new TeamQueue(var8);
         this.tqueue.call("aggressor.authenticate", CommonUtils.args(var3, var6, Aggressor.VERSION), this);
      } catch (Exception var9) {
         if ("127.0.0.1".equals(var4) && "Connection refused".equals(var9.getMessage())) {
            MudgeSanity.logException("client connect", var9, true);
            SafeDialogs.askYesNo("Connection refused\n\nA Cobalt Strike team server is not available on\nthe specified host and port. You must start a\nCobalt Strike team server first. Would you like\nto review the documentation on how to do this?", "Connection Error", new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  DialogUtils.gotoURL("https://www.cobaltstrike.com/help-start-cobaltstrike").actionPerformed((ActionEvent)null);
               }
            });
         } else {
            MudgeSanity.logException("client connect", var9, true);
            DialogUtils.showError(var9.getMessage());
         }
      }

   }

   public void result(String var1, Object var2) {
      if ("aggressor.authenticate".equals(var1)) {
         String var3 = var2 + "";
         if (var3.equals("SUCCESS")) {
            this.tqueue.call("aggressor.metadata", CommonUtils.args(System.currentTimeMillis()), this);
         } else {
            DialogUtils.showError(var3);
            this.tqueue.close();
         }
      } else if ("aggressor.metadata".equals(var1)) {
         final AggressorClient var4 = new AggressorClient(this.window, this.tqueue, (Map)var2, this.options);
         CommonUtils.runSafe(new Runnable() {
            public void run() {
               Connect.this.window.addButton(Connect.this.desc, var4);
               var4.showTime();
            }
         });
      }

   }

   public JComponent getContent(JFrame var1, String var2, String var3, String var4, String var5) {
      JPanel var6 = new JPanel();
      var6.setLayout(new BorderLayout());
      DialogManager var7 = new DialogManager(var1);
      var7.addDialogListener(this);
      var7.set("user", var2);
      var7.set("pass", var3);
      var7.set("host", var4);
      var7.set("port", var5);
      var7.text("host", "Host:", 20);
      var7.text("port", "Port:", 10);
      var7.text("user", "User:", 20);
      var7.password("pass", "Password:", 20);
      JButton var8 = var7.action("Connect");
      JButton var9 = var7.help("https://www.cobaltstrike.com/help-setup-collaboration");
      var6.add(var7.layout(), "Center");
      var6.add(DialogUtils.center(var8, var9), "South");
      return var6;
   }
}
