package sleep.bridges.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import sleep.bridges.BridgeUtilities;
import sleep.runtime.ScriptEnvironment;

public class FileObject extends IOObject {
   protected File file;

   public Object getSource() {
      return this.file;
   }

   public void open(String var1, ScriptEnvironment var2) {
      try {
         if (var1.charAt(0) == '>' && var1.charAt(1) == '>') {
            this.file = BridgeUtilities.toSleepFile(var1.substring(2, var1.length()).trim(), var2.getScriptInstance());
            this.openWrite(new FileOutputStream(this.file, true));
         } else if (var1.charAt(0) == '>') {
            this.file = BridgeUtilities.toSleepFile(var1.substring(1, var1.length()).trim(), var2.getScriptInstance());
            this.openWrite(new FileOutputStream(this.file, false));
         } else {
            this.file = BridgeUtilities.toSleepFile(var1, var2.getScriptInstance());
            this.openRead(new FileInputStream(this.file));
         }
      } catch (Exception var4) {
         var2.flagError(var4);
      }

   }
}
