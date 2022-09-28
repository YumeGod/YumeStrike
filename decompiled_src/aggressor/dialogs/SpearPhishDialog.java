package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataManager;
import aggressor.MultiFrame;
import aggressor.Prefs;
import aggressor.windows.PhishLog;
import common.CommonUtils;
import common.TeamQueue;
import common.UploadFile;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import ui.ATable;
import ui.GenericTableModel;

public class SpearPhishDialog implements DialogListener, UploadFile.UploadNotify {
   protected MultiFrame window;
   protected JFrame dialog = null;
   protected TeamQueue conn = null;
   protected DataManager datal = null;
   protected AggressorClient client = null;
   protected Map options = null;
   protected LinkedList contacts;
   protected String attachment;
   protected String bounce;
   protected String mailserver;
   protected String template;
   protected String server;

   public SpearPhishDialog(AggressorClient var1, MultiFrame var2, TeamQueue var3, DataManager var4) {
      this.window = var2;
      this.conn = var3;
      this.datal = var4;
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      if ("Preview".equals(var1.getActionCommand())) {
         this.preview(var2);
      } else {
         this.send(var1, var2);
      }

      this.save(var2);
   }

   public void preview(Map var1) {
      LinkedList var2 = (LinkedList)var1.get("targets");
      String var3 = DialogUtils.string(var1, "template");
      if (var3 != null && (new File(var3)).exists()) {
         if (var2 != null && var2.size() != 0) {
            (new MailPreview(var1)).show();
         } else {
            DialogUtils.showError("I need a target to show you a preview!");
         }
      } else {
         DialogUtils.showError("I need a template to show you a preview!");
      }

   }

   public boolean checkContactsReverse(LinkedList var1) {
      Iterator var2 = var1.iterator();

      for(int var3 = 1; var2.hasNext(); ++var3) {
         Map var4 = (Map)var2.next();
         String var5 = DialogUtils.string(var4, "To");
         String var6 = DialogUtils.string(var4, "To_Name");
         if (var6.length() > 0 && var6.indexOf(64) > 0 && var5.indexOf(64) < 0) {
            DialogUtils.showError("Your target file is in the wrong format.\nPlease check that the format is:\n\nuser@target<TAB>User's Name\n\nLook at entry " + var3 + ":\n" + var5 + "<TAB>" + var6);
            return true;
         }
      }

      return false;
   }

   public void send(ActionEvent var1, Map var2) {
      this.options = var2;
      this.contacts = (LinkedList)var2.get("targets");
      this.template = DialogUtils.string(var2, "template");
      this.bounce = DialogUtils.string(var2, "bounce");
      this.attachment = DialogUtils.string(var2, "attachment");
      this.server = DialogUtils.string(var2, "server");
      if (this.contacts != null && this.contacts.size() != 0) {
         if (!this.checkContactsReverse(this.contacts)) {
            if ("".equals(this.template)) {
               DialogUtils.showError("Please choose a template message");
            } else if ("".equals(this.bounce)) {
               DialogUtils.showError("Please provide a bounce address");
            } else if (!(new File(this.template)).exists()) {
               DialogUtils.showError("The template does not exist");
            } else if (!"".equals(this.attachment) && !(new File(this.attachment)).exists()) {
               DialogUtils.showError("Hey, the attachment doesn't exist");
            } else if ("".equals(this.server)) {
               DialogUtils.showError("I need a server to send phishes through.");
            } else if (this.server.startsWith("http://")) {
               DialogUtils.showError("Common mistake! The mail server is a host:port, not a URL");
            } else {
               if (!DialogUtils.isShift(var1)) {
                  this.dialog.setVisible(false);
               }

               if (!"".equals(this.attachment)) {
                  (new UploadFile(this.conn, new File(this.attachment), this)).start();
               } else {
                  this.send_phish();
               }

            }
         }
      } else {
         DialogUtils.showError("Please import a target file");
      }
   }

   public void complete(String var1) {
      this.options.put("attachmentr", var1);
      this.send_phish();
   }

   public void cancel() {
      this.dialog.setVisible(true);
   }

   public void send_phish() {
      String var1 = CommonUtils.ID();
      PhishLog var2 = new PhishLog(var1, this.datal, this.client.getScriptEngine(), this.conn);
      this.client.getTabManager().addTab("send email", var2.getConsole(), var2.cleanup(), "Transcript of phishing activity");
      String var3 = CommonUtils.bString(CommonUtils.readFile(this.template));
      this.conn.call("cloudstrike.go_phish", CommonUtils.args(var1, var3, new HashMap(this.options)));
   }

   public void save(Map var1) {
      Prefs.getPreferences().set("cloudstrike.send_email_bounce.string", (String)var1.get("bounce"));
      Prefs.getPreferences().set("cloudstrike.send_email_server.string", (String)var1.get("server"));
      Prefs.getPreferences().set("cloudstrike.send_email_target.file", (String)var1.get("_targets"));
      Prefs.getPreferences().save();
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Spear Phish", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("bounce", Prefs.getPreferences().getString("cloudstrike.send_email_bounce.string", ""));
      var1.set("server", Prefs.getPreferences().getString("cloudstrike.send_email_server.string", ""));
      var1.set("_targets", Prefs.getPreferences().getString("cloudstrike.send_email_target.file", ""));
      GenericTableModel var2 = DialogUtils.setupModel("To", CommonUtils.toArray("To, To_Name"), new LinkedList());
      ATable var3 = DialogUtils.setupTable(var2, CommonUtils.toArray("To, To_Name"), false);
      JScrollPane var4 = new JScrollPane(var3);
      var4.setPreferredSize(new Dimension(var4.getWidth(), 150));
      var1.file_import("targets", "Targets:", var3, var2);
      var1.file("template", "Template:");
      var1.file("attachment", "Attachment:");
      var1.site("url", "Embed URL:", this.conn, this.datal);
      var1.mailserver("server", "Mail Server:");
      var1.text("bounce", "Bounce To:", 30);
      JButton var5 = var1.action_noclose("Preview");
      JButton var6 = var1.action_noclose("Send");
      JButton var7 = var1.help("https://www.cobaltstrike.com/help-spear-phish");
      this.dialog.add(var4);
      this.dialog.add(DialogUtils.stackTwo(var1.layout(), DialogUtils.center(var5, var6, var7)), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
