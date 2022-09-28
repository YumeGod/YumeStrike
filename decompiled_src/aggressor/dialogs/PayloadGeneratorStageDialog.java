package aggressor.dialogs;

import aggressor.AggressorClient;
import common.CommonUtils;
import common.ListenerUtils;
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

public class PayloadGeneratorStageDialog implements DialogListener, SafeDialogCallback {
   protected JFrame dialog = null;
   protected byte[] stager = null;
   protected AggressorClient client = null;
   protected Map options = null;

   public PayloadGeneratorStageDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      boolean var3 = DialogUtils.bool(var2, "x64");
      String var4 = DialogUtils.string(var2, "exitf");
      String var5 = DialogUtils.string(var2, "listener");
      this.stager = ListenerUtils.getListener(this.client, var5).export(var3 ? "x64" : "x86", "Process".equals(var4) ? 0 : 1);
      if (this.stager.length != 0) {
         Map var6 = DialogUtils.toMap("ASPX: aspx, C: c, C#: cs, HTML Application: hta, Java: java, Perl: pl, PowerShell: ps1, PowerShell Command: txt, Python: py, Raw: bin, Ruby: rb, COM Scriptlet: sct, Veil: txt, VBA: vba");
         String var7 = DialogUtils.string(var2, "format");
         String var8 = "payload." + var6.get(var7);
         SafeDialogs.saveFile((JFrame)null, var8, this);
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
      } else if (var2.equals("Python")) {
         this.stager = Transforms.toPython(this.stager);
      } else if (!var2.equals("Raw")) {
         if (var2.equals("Ruby")) {
            this.stager = Transforms.toPython(this.stager);
         } else if (var2.equals("VBA")) {
            this.stager = CommonUtils.toBytes("myArray = " + Transforms.toVBA(this.stager));
         }
      }

      CommonUtils.writeToFile(new File(var1), this.stager);
      DialogUtils.showInfo("Saved " + var2 + " to\n" + var1);
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Payload Generator (Stageless)", 640, 480);
      String[] var1 = CommonUtils.toArray("C, C#, Java, Perl, Python, Raw, Ruby, VBA");
      DialogManager var2 = new DialogManager(this.dialog);
      var2.addDialogListener(this);
      var2.set("format", "raw");
      var2.sc_listener_all("listener", "Listener:", this.client);
      var2.combobox("format", "Output:", var1);
      var2.combobox("exitf", "Exit Function:", CommonUtils.toArray("Process, Thread"));
      var2.checkbox_add("x64", "x64:", "Use x64 payload");
      JButton var3 = var2.action("Generate");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-payload-generator-stageless");
      this.dialog.add(DialogUtils.description("This dialog exports a Cobalt Strike payload stage. Several output options are available."), "North");
      this.dialog.add(var2.layout(), "Center");
      this.dialog.add(DialogUtils.center(var3, var4), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
