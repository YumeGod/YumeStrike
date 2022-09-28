package sleep.bridges.io;

import java.io.File;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class ProcessObject extends IOObject {
   protected Process process;

   public Object getSource() {
      return this.process;
   }

   public void open(String[] var1, String[] var2, File var3, ScriptEnvironment var4) {
      try {
         if (var1.length > 0) {
            var1[0] = var1[0].replace('/', File.separatorChar);
         }

         this.process = Runtime.getRuntime().exec(var1, var2, var3);
         this.openRead(this.process.getInputStream());
         this.openWrite(this.process.getOutputStream());
      } catch (Exception var6) {
         var4.flagError(var6);
      }

   }

   public Scalar wait(ScriptEnvironment var1, long var2) {
      if (this.getThread() != null && this.getThread().isAlive()) {
         super.wait(var1, var2);
      }

      try {
         this.process.waitFor();
         return SleepUtils.getScalar(this.process.waitFor());
      } catch (Exception var5) {
         var1.flagError(var5);
         return SleepUtils.getEmptyScalar();
      }
   }

   public void close() {
      super.close();
      this.process.destroy();
   }
}
