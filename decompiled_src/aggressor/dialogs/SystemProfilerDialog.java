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

public class SystemProfilerDialog implements DialogListener, Callback {
   protected MultiFrame window;
   protected JFrame dialog = null;
   protected TeamQueue conn = null;
   protected DataManager datal = null;
   protected String port;
   protected String uri;
   protected String java;
   protected String redir;
   protected String host;
   protected String proto;
   protected boolean ssl;

   public SystemProfilerDialog(MultiFrame var1, TeamQueue var2, DataManager var3) {
      this.window = var1;
      this.conn = var2;
      this.datal = var3;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.java = DialogUtils.string(var2, "java");
      this.redir = DialogUtils.string(var2, "redirect");
      this.uri = DialogUtils.string(var2, "uri");
      this.port = DialogUtils.string(var2, "port");
      this.host = DialogUtils.string(var2, "host");
      this.ssl = DialogUtils.bool(var2, "ssl");
      this.proto = this.ssl ? "https://" : "http://";
      if (!"".equals(this.redir)) {
         this.conn.call("cloudstrike.start_profiler", CommonUtils.args(this.host, Integer.parseInt(this.port), this.ssl, this.uri, this.redir, this.java, "System Profiler. Redirects to " + this.redir), this);
      } else {
         this.conn.call("cloudstrike.start_profiler", CommonUtils.args(this.host, Integer.parseInt(this.port), this.ssl, this.uri, (Object)null, this.java, "System Profiler"), this);
      }

   }

   public void result(String var1, Object var2) {
      String var3 = var2 + "";
      if ("success".equals(var3)) {
         DialogUtils.startedWebService("system profiler", this.proto + this.host + ":" + this.port + this.uri);
      } else {
         DialogUtils.showError("Unable to start profiler:\n" + var3);
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("System Profiler", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("uri", "/");
      var1.set("port", "80");
      var1.set("java", "true");
      var1.set("host", DataUtils.getLocalIP(this.datal));
      var1.text("uri", "Local URI:", 20);
      var1.text("host", "Local Host:", 20);
      var1.text("port", "Local Port:", 20);
      var1.text("redirect", "Redirect URL:", 20);
      var1.checkbox_add("ssl", "SSL:", "Enable SSL", DataUtils.hasValidSSL(this.datal));
      var1.checkbox_add("java", "", "Use Java Applet to get information");
      JButton var2 = var1.action("Launch");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-system-profiler");
      this.dialog.add(DialogUtils.description("The system profiler is a client-side reconaissance tool. It finds common applications (with version numbers) used by the user."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
