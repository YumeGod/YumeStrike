package sleep.engine.atoms;

import sleep.engine.Step;
import sleep.interfaces.Operator;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Operate extends Step {
   String oper;

   public Operate(String var1) {
      this.oper = var1;
   }

   public String toString(String var1) {
      return var1 + "[Operator]: " + this.oper + "\n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Operator var2 = var1.getOperator(this.oper);
      if (var2 != null) {
         Scalar var3 = var2.operate(this.oper, var1.getScriptInstance(), var1.getCurrentFrame());
         var1.KillFrame();
         var1.getCurrentFrame().push(var3);
      } else {
         var1.getScriptInstance().fireWarning("Attempting to use non-existent operator: '" + this.oper + "'", this.getLineNumber());
         var1.KillFrame();
         var1.getCurrentFrame().push(SleepUtils.getEmptyScalar());
      }

      return null;
   }
}
