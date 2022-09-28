package report;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.File;
import java.io.InputStream;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Stack;
import sleep.error.RuntimeWarningWatcher;
import sleep.error.ScriptWarning;
import sleep.error.YourCodeSucksException;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.SleepUtils;

public class ReportTest implements RuntimeWarningWatcher {
   protected ReportBridge bridge = new ReportBridge();

   public void processScriptWarning(ScriptWarning var1) {
      String var2 = var1.getNameShort() + ":" + var1.getLineNumber();
      SimpleDateFormat var3 = new SimpleDateFormat("HH:mm:ss");
      Date var4 = new Date();
      String var5 = var3.format(var4, new StringBuffer(), new FieldPosition(0)).toString();
      if (var1.isDebugTrace()) {
         CommonUtils.print_info("[" + var5 + "] Trace: " + var1.getMessage() + " at " + var2);
      } else {
         CommonUtils.print_info("[" + var5 + "] " + var1.getMessage() + " at " + var2);
      }

   }

   public Document buildReport(String var1, Stack var2) {
      return this.bridge.buildReport(var1, var1, var2);
   }

   public void load(String var1, InputStream var2) {
      new Hashtable();
      ScriptLoader var4 = new ScriptLoader();

      try {
         var4.addGlobalBridge(this.bridge);
         ScriptInstance var5 = var4.loadScript(var1, var2);
         var5.addWarningWatcher(this);
         var5.runScript();
      } catch (YourCodeSucksException var6) {
         CommonUtils.print_error("Could not load: " + var1 + "\n" + var6.formatErrors());
      } catch (Exception var7) {
         MudgeSanity.logException("Could not load:" + var1, var7, false);
      }

   }

   public static void main(String[] var0) {
      if (var0.length < 3) {
         CommonUtils.print_warn("ReportTest [file.rpt] [title] [/path/to/out.pdf] [args...]");
      } else {
         try {
            ReportTest var1 = new ReportTest();
            var1.load(var0[0], CommonUtils.resource(var0[0]));
            Stack var2 = new Stack();

            for(int var3 = 3; var3 < var0.length; ++var3) {
               var2.add(0, SleepUtils.getScalar(var0[var3]));
            }

            Document var5 = var1.buildReport(var0[1], var2);
            var5.toPDF(new File(var0[2]));
         } catch (Exception var4) {
            MudgeSanity.logException("Error", var4, false);
         }

      }
   }
}
