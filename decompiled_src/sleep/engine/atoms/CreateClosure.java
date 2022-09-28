package sleep.engine.atoms;

import sleep.bridges.SleepClosure;
import sleep.engine.Block;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class CreateClosure extends Step {
   Block block = null;

   public String toString(String var1) {
      return var1 + "[Create Closure]\n" + this.block.toString(var1 + "   ");
   }

   public CreateClosure(Block var1) {
      this.block = var1;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Scalar var2 = SleepUtils.getScalar((Object)(new SleepClosure(var1.getScriptInstance(), this.block)));
      var1.getCurrentFrame().push(var2);
      return null;
   }
}
