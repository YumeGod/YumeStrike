package common;

import dialog.DialogUtils;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.ProgressMonitor;

public class DownloadFile extends AObject implements Callback {
   protected FileOutputStream out = null;
   protected File file = null;
   protected TeamQueue conn = null;
   protected DownloadNotify listener = null;
   protected String rpath = null;
   protected long total = 0L;
   protected long start = System.currentTimeMillis();
   protected long read = 0L;
   protected long ret = 0L;
   protected long sofar = 0L;
   protected double time = 0.0;
   protected ProgressMonitor progress = null;

   public DownloadFile(TeamQueue var1, String var2, File var3, DownloadNotify var4) {
      this.file = var3;
      this.listener = var4;
      this.conn = var1;
      this.rpath = var2;
   }

   public void result(String var1, Object var2) {
      try {
         this._result(var1, var2);
      } catch (IOException var4) {
         MudgeSanity.logException(var1 + " " + var2, var4, false);
      }

   }

   public void _result(String var1, Object var2) throws IOException {
      DownloadMessage var3 = (DownloadMessage)var2;
      if (var3.getType() == 0) {
         this.total = var3.getSize();
         this.out = new FileOutputStream(this.file, false);
         this.progress = new ProgressMonitor((Component)null, "Download " + this.file.getName(), "Starting download", 0, (int)this.total);
         this.conn.call("download.get", CommonUtils.args(var3.id()), this);
      } else if (var3.getType() == 1) {
         this.time = (double)(System.currentTimeMillis() - this.start) / 1000.0;
         this.sofar += (long)var3.getData().length;
         this.progress.setProgress((int)this.sofar);
         this.progress.setNote("Speed: " + Math.round((double)(this.sofar / 1024L) / this.time) + " KB/s");
         if (this.progress.isCanceled()) {
            this.progress.close();
            this.out.close();
            if (this.listener != null) {
               this.listener.cancel();
            }

            return;
         }

         this.out.write(var3.getData());
         this.conn.call("download.get", CommonUtils.args(var3.id()), this);
      } else if (var3.getType() == 2) {
         this.progress.close();
         this.out.close();
         if (this.listener != null) {
            this.listener.complete(this.file.getAbsolutePath());
         }
      } else if (var3.getType() == 3) {
         if (this.out != null && this.progress != null) {
            this.out.close();
            this.progress.setNote(var3.getError());
            if (this.listener != null) {
               this.listener.cancel();
            }
         } else {
            DialogUtils.showError(var3.getError());
         }
      }

   }

   public void start() {
      this.conn.call("download.start", CommonUtils.args(this.rpath), this);
   }
}
