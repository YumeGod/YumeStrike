package sleep.taint;

import sleep.engine.Step;
import sleep.interfaces.Operator;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class TaintOperate extends PermeableStep {
   String oper;

   public TaintOperate(String var1, Step var2) {
      super(var2);
      this.oper = var1;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Operator var2 = var1.getOperator(this.oper);
      if (var2 != null && var2.getClass() == Sanitizer.class) {
         this.wrapped.evaluate(var1);
      } else {
         super.evaluate(var1);
      }

      return null;
   }
}
