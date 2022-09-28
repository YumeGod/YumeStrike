package sleep.taint;

import sleep.engine.Step;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class TaintCall extends PermeableStep {
   protected String function;

   public TaintCall(String var1, Step var2) {
      super(var2);
      this.function = var1;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Function var2 = var1.getFunction(this.function);
      return var2 == null || var2.getClass() != Tainter.class && var2.getClass() != Sanitizer.class ? super.evaluate(var1) : this.wrapped.evaluate(var1);
   }
}
