package sleep.taint;

import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class TaintObjectAccess extends PermeableStep {
   protected String name;
   protected Class classRef;

   public TaintObjectAccess(Step var1, String var2, Class var3) {
      super(var1);
      this.name = var2;
      this.classRef = var3;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Scalar var2 = null;
      Object var3 = null;
      if (this.classRef == null && !SleepUtils.isFunctionScalar((Scalar)var1.getCurrentFrame().peek())) {
         String var4 = var1.hasFrame() ? TaintUtils.checkArguments(var1.getCurrentFrame()) : null;
         var2 = (Scalar)var1.getCurrentFrame().peek();
         if (var4 != null && !TaintUtils.isTainted(var2)) {
            TaintUtils.taint(var2);
            if ((var1.getScriptInstance().getDebugFlags() & 128) == 128) {
               var1.getScriptInstance().fireWarning("tainted object: " + SleepUtils.describe(var2) + " from: " + var4, this.getLineNumber());
            }
         }

         return this.callit(var1, var4);
      } else {
         return super.evaluate(var1);
      }
   }
}
