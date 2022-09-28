package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.ArtifactUtils;
import common.CommonUtils;
import common.ListenerUtils;
import common.ResourceUtils;
import common.ScListener;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;

public class WindowsExecutableStageDialog implements DialogListener, SafeDialogCallback {
   protected JFrame dialog = null;
   protected ActionEvent event = null;
   protected Map options = null;
   protected String outfile = "";
   protected AggressorClient client = null;

   public WindowsExecutableStageDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.event = var1;
      this.options = var2;
      String var3 = var2.get("output") + "";
      String var4 = "";
      if (var3.indexOf("PowerShell") > -1) {
         var4 = "beacon.ps1";
      } else if (var3.indexOf("Raw") > -1) {
         var4 = "beacon.bin";
      } else if (var3.indexOf("EXE") > -1) {
         var4 = "beacon.exe";
      } else if (var3.indexOf("DLL") > -1) {
         var4 = "beacon.dll";
      }

      SafeDialogs.saveFile((JFrame)null, var4, this);
   }

   public void dialogResult(String var1) {
      this.outfile = var1;
      String var2 = DialogUtils.string(this.options, "stage");
      String var3 = DialogUtils.bool(this.options, "x64") ? "x64" : "x86";
      ScListener var4 = ListenerUtils.getListener(this.client, var2);
      byte[] var5 = var4.export(var3);
      if (var5.length == 0) {
         DialogUtils.showError("Could not generate " + var3 + " payload for " + var2);
      } else {
         String var6 = this.options.get("output") + "";
         boolean var7 = DialogUtils.bool(this.options, "x64");
         boolean var8 = DialogUtils.bool(this.options, "sign");
         if (var7) {
            if (var6.equals("Windows EXE")) {
               (new ArtifactUtils(this.client)).patchArtifact(var5, "artifact64big.exe", this.outfile);
            } else if (var6.equals("Windows Service EXE")) {
               (new ArtifactUtils(this.client)).patchArtifact(var5, "artifact64svcbig.exe", this.outfile);
            } else {
               if (var6.equals("Windows DLL (32-bit)")) {
                  DialogUtils.showError("I can't generate an x86 artifact for an x64 payload.");
                  return;
               }

               if (var6.equals("Windows DLL (64-bit)")) {
                  (new ArtifactUtils(this.client)).patchArtifact(var5, "artifact64big.x64.dll", this.outfile);
               } else if (var6.equals("PowerShell")) {
                  (new ResourceUtils(this.client)).buildPowerShell(var5, this.outfile, true);
               } else {
                  CommonUtils.writeToFile(new File(this.outfile), var5);
               }
            }
         } else if (var6.equals("Windows EXE")) {
            (new ArtifactUtils(this.client)).patchArtifact(var5, "artifact32big.exe", this.outfile);
         } else if (var6.equals("Windows Service EXE")) {
            (new ArtifactUtils(this.client)).patchArtifact(var5, "artifact32svcbig.exe", this.outfile);
         } else if (var6.equals("Windows DLL (32-bit)")) {
            (new ArtifactUtils(this.client)).patchArtifact(var5, "artifact32big.dll", this.outfile);
         } else if (var6.equals("Windows DLL (64-bit)")) {
            (new ArtifactUtils(this.client)).patchArtifact(var5, "artifact64big.dll", this.outfile);
         } else if (var6.equals("PowerShell")) {
            (new ResourceUtils(this.client)).buildPowerShell(var5, this.outfile);
         } else {
            CommonUtils.writeToFile(new File(this.outfile), var5);
         }

         if (var8) {
            if (!this.outfile.toLowerCase().endsWith(".exe") && !this.outfile.toLowerCase().endsWith(".dll")) {
               DialogUtils.showError("Can only sign EXE and DLL files");
               return;
            }

            DataUtils.getSigner(this.client.getData()).sign(new File(this.outfile));
         }

         DialogUtils.showInfo("Saved " + var6 + " to\n" + this.outfile);
      }
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Windows Executable (Stageless)", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("output", "Windows EXE");
      var1.sc_listener_all("stage", "Listener:", this.client);
      var1.combobox("output", "Output:", CommonUtils.toArray("PowerShell, Raw, Windows EXE, Windows Service EXE, Windows DLL (32-bit), Windows DLL (64-bit)"));
      var1.checkbox_add("x64", "x64:", "Use x64 payload");
      var1.checkbox_add("sign", "sign:", "Sign executable file", DataUtils.getSigner(this.client.getData()).available());
      JButton var2 = var1.action("Generate");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-staged-exe");
      this.dialog.add(DialogUtils.description("Export a stageless Beacon as a Windows executable. Use Cobalt Strike Arsenal scripts (Help -> Arsenal) to customize this process."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
