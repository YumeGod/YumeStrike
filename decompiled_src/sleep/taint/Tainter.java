package sleep.taint;

import java.util.Stack;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;

public class Tainter implements Function {
   protected Object function;

   public Tainter(Object var1) {
      this.function = var1;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      Scalar var4 = ((Function)this.function).evaluate(var1, var2, var3);
      TaintUtils.taintAll(var4);
      return var4;
   }
}
