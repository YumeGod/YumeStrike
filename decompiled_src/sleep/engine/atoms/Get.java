package sleep.engine.atoms;

import sleep.engine.Step;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Get extends Step {
   String value;

   public Get(String var1) {
      this.value = var1;
   }

   public String toString(String var1) {
      return var1 + "[Get Item]: " + this.value + "\n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      if (this.value.charAt(0) == '&') {
         Function var2 = var1.getFunction(this.value);
         Scalar var3 = SleepUtils.getScalar((Object)var2);
         var1.getCurrentFrame().push(var3);
      } else {
         Scalar var4 = var1.getScalar(this.value);
         if (var4 == null) {
            if (this.value.charAt(0) == '@') {
               var4 = SleepUtils.getArrayScalar();
            } else if (this.value.charAt(0) == '%') {
               var4 = SleepUtils.getHashScalar();
            } else {
               var4 = SleepUtils.getEmptyScalar();
            }

            var1.putScalar(this.value, var4);
            if ((var1.getScriptInstance().getDebugFlags() & 4) == 4) {
               var1.showDebugMessage("variable '" + this.value + "' not declared");
            }
         }

         var1.getCurrentFrame().push(var4);
      }

      return null;
   }
}
