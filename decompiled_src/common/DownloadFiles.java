package common;

import dialog.DialogUtils;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class DownloadFiles implements DownloadNotify {
   protected Iterator queue = null;
   protected File dest = null;
   protected TeamQueue conn = null;

   public void startNextDownload() {
      if (!this.queue.hasNext()) {
         DialogUtils.showInfo("Download complete!");
      } else {
         Map var1 = (Map)this.queue.next();
         String var2 = (String)var1.get("lpath");
         String var3 = (String)var1.get("name");
         (new DownloadFile(this.conn, var2, CommonUtils.SafeFile(this.dest, var3), this)).start();
      }
   }

   public DownloadFiles(TeamQueue var1, Map[] var2, File var3) {
      this.conn = var1;
      this.queue = CommonUtils.toList((Object[])var2).iterator();
      this.dest = var3;
      var3.mkdirs();
   }

   public void complete(String var1) {
      this.startNextDownload();
   }

   public void cancel() {
   }
}
