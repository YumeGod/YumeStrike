package common;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.ProgressMonitor;

public class UploadFile implements Callback {
   protected FileInputStream in = null;
   protected byte[] buffer = new byte[262144];
   protected File file = null;
   protected TeamQueue conn = null;
   protected UploadNotify listener = null;
   protected long total;
   protected long start = System.currentTimeMillis();
   protected long read = 0L;
   protected long ret = 0L;
   protected long sofar = 0L;
   protected double time = 0.0;
   protected ProgressMonitor progress;

   public UploadFile(TeamQueue var1, File var2, UploadNotify var3) {
      this.file = var2;
      this.listener = var3;
      this.conn = var1;
   }

   public void result(String var1, Object var2) {
      String var3 = var2 + "";

      try {
         if (this.sofar < this.total) {
            this.time = (double)(System.currentTimeMillis() - this.start) / 1000.0;
            this.progress.setProgress((int)this.sofar);
            this.progress.setNote("Speed: " + Math.round((double)(this.sofar / 1024L) / this.time) + " KB/s");
            if (this.progress.isCanceled()) {
               this.progress.close();
               this.in.close();
               this.listener.cancel();
               return;
            }

            if (var3.startsWith("ERROR: ")) {
               this.progress.setNote(var3);
               this.in.close();
               return;
            }

            this.read = (long)this.in.read(this.buffer);
            this.sofar += this.read;
            this.conn.call("armitage.append", CommonUtils.args(this.file.getName(), this.tailor(this.buffer, this.read)), this);
         } else {
            this.time = (double)(System.currentTimeMillis() - this.start) / 1000.0;
            this.progress.setProgress((int)this.sofar);
            this.progress.setNote("Speed: " + Math.round((double)(this.sofar / 1024L) / this.time) + " KB/s");
            this.progress.close();
            this.in.close();
            this.listener.complete(var2 + "");
         }
      } catch (Exception var5) {
         MudgeSanity.logException("upload" + this.sofar + "/" + this.total + " of " + this.file, var5, false);
         this.listener.cancel();
      }

   }

   public void start() {
      try {
         this.total = this.file.length();
         this.in = new FileInputStream(this.file);
         this.progress = new ProgressMonitor((Component)null, "Upload " + this.file.getName(), "Starting upload", 0, (int)this.total);
         this.conn.call("armitage.upload", CommonUtils.args(this.file.getName()), this);
      } catch (IOException var2) {
         MudgeSanity.logException("upload start: " + this.file, var2, false);
         this.listener.cancel();
      }

   }

   protected byte[] tailor(byte[] var1, long var2) {
      byte[] var4 = new byte[(int)var2];

      for(int var5 = 0; (long)var5 < var2; ++var5) {
         var4[var5] = var1[var5];
      }

      return var4;
   }

   public interface UploadNotify {
      void complete(String var1);

      void cancel();
   }
}
