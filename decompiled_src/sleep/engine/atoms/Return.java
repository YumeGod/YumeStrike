package sleep.engine.atoms;

import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Return extends Step {
   protected int return_type;

   public Return(int var1) {
      this.return_type = var1;
   }

   public String toString(String var1) {
      return var1 + "[Return]: " + this.return_type + " \n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Scalar var2;
      if (this.return_type == 16) {
         var2 = (Scalar)var1.getCurrentFrame().pop();
         if (!SleepUtils.isEmptyScalar(var2)) {
            var1.getScriptInstance().clearStackTrace();
            var1.getScriptInstance().recordStackFrame("<origin of exception>", this.getLineNumber());
            var1.flagReturn(var2, 16);
         }
      } else if (this.return_type != 2 && this.return_type != 4) {
         if (this.return_type == 72) {
            var2 = var1.getCurrentFrame().isEmpty() ? SleepUtils.getEmptyScalar() : (Scalar)var1.getCurrentFrame().pop();
            if (!SleepUtils.isFunctionScalar(var2)) {
               var1.getScriptInstance().fireWarning("callcc requires a function: " + SleepUtils.describe(var2), this.getLineNumber());
               var1.flagReturn(var2, 8);
            } else {
               var1.flagReturn(var2, this.return_type);
            }
         } else if (var1.getCurrentFrame().isEmpty()) {
            var1.flagReturn(SleepUtils.getEmptyScalar(), this.return_type);
         } else {
            var1.flagReturn((Scalar)var1.getCurrentFrame().pop(), this.return_type);
         }
      } else {
         var1.flagReturn((Scalar)null, this.return_type);
      }

      var1.KillFrame();
      return null;
   }
}
