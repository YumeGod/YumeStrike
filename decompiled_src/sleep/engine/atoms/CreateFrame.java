package sleep.engine.atoms;

import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class CreateFrame extends Step {
   public String toString(String var1) {
      return var1 + "[Create Frame]\n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      var1.CreateFrame();
      return null;
   }
}
