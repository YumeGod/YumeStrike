package common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadFileSimple extends AObject implements Callback {
   protected FileOutputStream out = null;
   protected File file = null;
   protected TeamQueue conn = null;
   protected DownloadNotify listener = null;
   protected String rpath = null;

   public DownloadFileSimple(TeamQueue var1, String var2, File var3, DownloadNotify var4) {
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
         this.out = new FileOutputStream(this.file, false);
         this.conn.call("download.get", CommonUtils.args(var3.id()), this);
      } else if (var3.getType() == 1) {
         this.out.write(var3.getData());
         this.conn.call("download.get", CommonUtils.args(var3.id()), this);
      } else if (var3.getType() == 2) {
         this.out.close();
         if (this.listener != null) {
            this.listener.complete(this.file.getAbsolutePath());
         }
      } else if (var3.getType() == 3) {
         if (this.out != null) {
            this.out.close();
            if (this.listener != null) {
               this.listener.cancel();
            }
         } else {
            CommonUtils.print_error("download sync " + this.rpath + " failed: " + var3.getError());
         }
      }

   }

   public void start() {
      this.conn.call("download.start", CommonUtils.args(this.rpath), this);
   }
}
