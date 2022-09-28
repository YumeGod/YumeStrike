package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.dialogs.ExportDataDialog;
import aggressor.dialogs.ExportReportDialog;
import cortana.Cortana;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ReportingBridge implements Function, Loadable {
   protected AggressorClient client;

   public ReportingBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&reports", this);
      Cortana.put(var1, "&reportDescription", this);
      Cortana.put(var1, "&openReportDialog", this);
      Cortana.put(var1, "&openExportDataDialog", this);
      Cortana.put(var1, "&rehash_reports", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if ("&reports".equals(var1)) {
         return SleepUtils.getArrayWrapper(this.client.getReportEngine().reportTitles());
      } else {
         String var4;
         if ("&reportDescription".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            return SleepUtils.getScalar(this.client.getReportEngine().describe(var4));
         } else {
            if ("&openReportDialog".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               ExportReportDialog var5 = new ExportReportDialog(this.client, var4);
               var5.show();
            } else if ("&openExportDataDialog".equals(var1)) {
               ExportDataDialog var6 = new ExportDataDialog(this.client);
               var6.show();
            } else if ("&rehash_reports".equals(var1)) {
               this.client.getReportEngine().rehash();
               return SleepUtils.getScalar("done");
            }

            return SleepUtils.getEmptyScalar();
         }
      }
   }
}
