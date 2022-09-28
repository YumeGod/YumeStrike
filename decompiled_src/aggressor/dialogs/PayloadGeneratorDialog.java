package aggressor.dialogs;

import aggressor.AggressorClient;
import common.ArtifactUtils;
import common.CommonUtils;
import common.ListenerUtils;
import common.PowerShellUtils;
import common.ResourceUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import encoders.Transforms;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PayloadGeneratorDialog implements DialogListener, SafeDialogCallback {
   protected JFrame dialog = null;
   protected byte[] stager = null;
   protected AggressorClient client = null;
   protected Map options = null;

   public PayloadGeneratorDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      boolean var3 = DialogUtils.bool(var2, "x64");
      String var4 = DialogUtils.string(var2, "listener");
      this.stager = ListenerUtils.getListener(this.client, var4).getPayloadStager(var3 ? "x64" : "x86");
      if (this.stager.length == 0) {
         if (var3) {
            DialogUtils.showError("No x64 stager for listener " + var4);
         } else {
            DialogUtils.showError("No x86 stager for listener " + var4);
         }

      } else {
         Map var5 = DialogUtils.toMap("ASPX: aspx, C: c, C#: cs, HTML Application: hta, Java: java, Perl: pl, PowerShell: ps1, PowerShell Command: txt, Python: py, Raw: bin, Ruby: rb, COM Scriptlet: sct, Veil: txt, VBA: vba");
         String var6 = DialogUtils.string(var2, "format");
         String var7 = "payload." + var5.get(var6);
         SafeDialogs.saveFile((JFrame)null, var7, this);
      }
   }

   public void dialogResult(String var1) {
      String var2 = DialogUtils.string(this.options, "format");
      boolean var3 = DialogUtils.bool(this.options, "x64");
      String var4 = DialogUtils.string(this.options, "listener");
      if (var2.equals("C")) {
         this.stager = Transforms.toC(this.stager);
      } else if (var2.equals("C#")) {
         this.stager = Transforms.toCSharp(this.stager);
      } else if (var2.equals("Java")) {
         this.stager = Transforms.toJava(this.stager);
      } else if (var2.equals("Perl")) {
         this.stager = Transforms.toPerl(this.stager);
      } else if (var2.equals("PowerShell") && var3) {
         this.stager = (new ResourceUtils(this.client)).buildPowerShell(this.stager, true);
      } else if (var2.equals("PowerShell") && !var3) {
         this.stager = (new ResourceUtils(this.client)).buildPowerShell(this.stager);
      } else if (var2.equals("PowerShell Command") && var3) {
         this.stager = (new PowerShellUtils(this.client)).buildPowerShellCommand(this.stager, true);
      } else if (var2.equals("PowerShell Command") && !var3) {
         this.stager = (new PowerShellUtils(this.client)).buildPowerShellCommand(this.stager, false);
      } else if (var2.equals("Python")) {
         this.stager = Transforms.toPython(this.stager);
      } else if (!var2.equals("Raw")) {
         if (var2.equals("Ruby")) {
            this.stager = Transforms.toPython(this.stager);
         } else if (var2.equals("COM Scriptlet")) {
            if (var3) {
               DialogUtils.showError(var2 + " is not compatible with x64 stagers");
               return;
            }

            this.stager = (new ArtifactUtils(this.client)).buildSCT(this.stager);
         } else if (var2.equals("Veil")) {
            this.stager = Transforms.toVeil(this.stager);
         } else if (var2.equals("VBA")) {
            this.stager = CommonUtils.toBytes("myArray = " + Transforms.toVBA(this.stager));
         }
      }

      CommonUtils.writeToFile(new File(var1), this.stager);
      DialogUtils.showInfo("Saved " + var2 + " to\n" + var1);
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Payload Generator", 640, 480);
      String[] var1 = CommonUtils.toArray("C, C#, COM Scriptlet, Java, Perl, PowerShell, PowerShell Command, Python, Raw, Ruby, Veil, VBA");
      DialogManager var2 = new DialogManager(this.dialog);
      var2.addDialogListener(this);
      var2.set("format", "raw");
      var2.sc_listener_stagers("listener", "Listener:", this.client);
      var2.combobox("format", "Output:", var1);
      var2.checkbox_add("x64", "x64:", "Use x64 payload");
      JButton var3 = var2.action("Generate");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-payload-generator");
      this.dialog.add(DialogUtils.description("This dialog generates a payload to stage a Cobalt Strike listener. Several output options are available."), "North");
      this.dialog.add(var2.layout(), "Center");
      this.dialog.add(DialogUtils.center(var3, var4), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
