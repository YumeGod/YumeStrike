package sleep.taint;

import java.util.Iterator;
import java.util.Stack;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class Sensitive implements Function {
   protected Object function;

   public Sensitive(Object var1) {
      this.function = var1;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      Stack var4 = new Stack();
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Scalar var6 = (Scalar)var5.next();
         if (TaintUtils.isTainted(var6)) {
            var4.push(var6);
         }
      }

      if (var4.isEmpty()) {
         return ((Function)this.function).evaluate(var1, var2, var3);
      } else {
         throw new RuntimeException("Insecure " + var1 + ": " + SleepUtils.describe(var4) + " is tainted");
      }
   }
}
