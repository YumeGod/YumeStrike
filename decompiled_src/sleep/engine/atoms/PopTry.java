package sleep.engine.atoms;

import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class PopTry extends Step {
   public String toString(String var1) {
      return var1 + "[Pop Try]\n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      var1.popExceptionContext();
      return null;
   }
}
