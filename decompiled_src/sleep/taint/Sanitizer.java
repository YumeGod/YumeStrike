package sleep.taint;

import java.util.Stack;
import sleep.interfaces.Function;
import sleep.interfaces.Operator;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;

public class Sanitizer implements Function, Operator {
   protected Object function;

   public Sanitizer(Object var1) {
      this.function = var1;
   }

   public Scalar operate(String var1, ScriptInstance var2, Stack var3) {
      Scalar var4 = ((Operator)this.function).operate(var1, var2, var3);
      TaintUtils.untaint(var4);
      return var4;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      Scalar var4 = ((Function)this.function).evaluate(var1, var2, var3);
      TaintUtils.untaint(var4);
      return var4;
   }
}
