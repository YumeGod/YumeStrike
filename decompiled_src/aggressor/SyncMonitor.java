package aggressor;

import common.Callback;
import common.PlaybackStatus;
import javax.swing.ProgressMonitor;

public class SyncMonitor implements Callback {
   protected ProgressMonitor monitor = null;
   protected AggressorClient client;

   public SyncMonitor(AggressorClient var1) {
      this.client = var1;
      var1.getData().subscribe("playback.status", this);
   }

   public void result(String var1, Object var2) {
      PlaybackStatus var3 = (PlaybackStatus)var2;
      if (var3.isStart()) {
         this.monitor = new ProgressMonitor(this.client, "Sync to Team Server", var3.getMessage(), 0, 100);
      } else if (var3.isDone() && this.monitor != null) {
         this.monitor.close();
      } else if (this.monitor != null) {
         this.monitor.setNote("[" + var3.getSent() + "/" + var3.getTotal() + "] " + var3.getMessage());
         this.monitor.setProgress(var3.percentage());
      }

   }
}
