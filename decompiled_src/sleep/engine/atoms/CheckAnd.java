package sleep.engine.atoms;

import java.io.Serializable;
import sleep.runtime.ScriptEnvironment;

public class CheckAnd implements Check, Serializable {
   private Check left;
   private Check right;

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[AND]:\n");
      var2.append(this.left.toString(var1 + "      "));
      var2.append(this.right.toString(var1 + "      "));
      return var2.toString();
   }

   public String toString() {
      return this.toString("");
   }

   public CheckAnd(Check var1, Check var2) {
      this.left = var1;
      this.right = var2;
   }

   public boolean check(ScriptEnvironment var1) {
      return this.left.check(var1) && this.right.check(var1);
   }

   public void setInfo(int var1) {
   }
}
