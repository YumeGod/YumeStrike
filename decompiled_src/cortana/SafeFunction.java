package cortana;

import common.MudgeSanity;
import java.util.Stack;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class SafeFunction implements Function {
   protected Function f;

   public SafeFunction(Function var1) {
      this.f = var1;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      try {
         return this.f.evaluate(var1, var2, var3);
      } catch (Exception var5) {
         MudgeSanity.logException("cortana bridge: " + var1, var5, false);
         if (var2 != null && var5 != null) {
            var2.getScriptEnvironment().showDebugMessage("Function call " + var1 + " failed: " + var5.getMessage());
         }

         return SleepUtils.getEmptyScalar();
      }
   }
}
