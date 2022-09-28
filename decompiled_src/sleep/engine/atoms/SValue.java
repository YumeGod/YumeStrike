package sleep.engine.atoms;

import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class SValue extends Step {
   Scalar value;

   public String toString(String var1) {
      return var1 + "[Scalar]: " + this.value + "\n";
   }

   public SValue(Scalar var1) {
      this.value = var1;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      var1.getCurrentFrame().push(SleepUtils.getScalar(this.value));
      return this.value;
   }
}
