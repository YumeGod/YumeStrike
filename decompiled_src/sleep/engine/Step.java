package sleep.engine;

import java.io.Serializable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Step implements Serializable {
   protected int line;
   public Step next;

   public String toString(String var1) {
      return var1 + "[NOP]\n";
   }

   public void setInfo(int var1) {
      this.line = var1;
   }

   public int getHighLineNumber() {
      return this.getLineNumber();
   }

   public int getLowLineNumber() {
      return this.getLineNumber();
   }

   public int getLineNumber() {
      return this.line;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      return SleepUtils.getEmptyScalar();
   }

   public String toString() {
      return this.toString("");
   }
}
