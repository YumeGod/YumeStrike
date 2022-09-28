package aggressor.bridges;

import aggressor.AggressorClient;
import cortana.Cortana;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.util.Stack;
import javax.swing.JFrame;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class SafeDialogBridge implements Function, Loadable {
   protected AggressorClient client;

   public SafeDialogBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&show_message", this);
      Cortana.put(var1, "&show_error", this);
      Cortana.put(var1, "&prompt_confirm", this);
      Cortana.put(var1, "&prompt_text", this);
      Cortana.put(var1, "&prompt_file_open", this);
      Cortana.put(var1, "&prompt_directory_open", this);
      Cortana.put(var1, "&prompt_file_save", this);
   }

   public SafeDialogCallback popCallback(Stack var1, ScriptInstance var2) {
      final SleepClosure var3 = BridgeUtilities.getFunction(var1, var2);
      return new SafeDialogCallback() {
         public void dialogResult(String var1) {
            if (var1 != null) {
               Stack var2 = new Stack();
               var2.push(SleepUtils.getScalar(var1));
               SleepUtils.runCode((SleepClosure)var3, "dialogResult", (ScriptInstance)null, var2);
            }
         }
      };
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      if ("&show_message".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         DialogUtils.showInfo(var4);
      } else if ("&show_error".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         DialogUtils.showError(var4);
      } else {
         String var5;
         if ("&prompt_confirm".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var5 = BridgeUtilities.getString(var3, "");
            SafeDialogs.askYesNo(var4, var5, this.popCallback(var3, var2));
         } else if ("&prompt_text".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var5 = BridgeUtilities.getString(var3, "");
            SafeDialogs.ask(var4, var5, this.popCallback(var3, var2));
         } else {
            boolean var6;
            if ("&prompt_file_open".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               var6 = SleepUtils.isTrueScalar(BridgeUtilities.getScalar(var3));
               SafeDialogs.openFile(var4, var5, (String)null, var6, false, this.popCallback(var3, var2));
            } else if ("&prompt_directory_open".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               var6 = SleepUtils.isTrueScalar(BridgeUtilities.getScalar(var3));
               SafeDialogs.openFile(var4, var5, (String)null, var6, true, this.popCallback(var3, var2));
            } else if ("&prompt_file_save".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               SafeDialogs.saveFile((JFrame)null, var4, this.popCallback(var3, var2));
            }
         }
      }

      return SleepUtils.getEmptyScalar();
   }
}
