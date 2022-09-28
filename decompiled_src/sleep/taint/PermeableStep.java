package sleep.taint;

import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class PermeableStep extends Step {
   protected Step wrapped;

   public PermeableStep(Step var1) {
      this.wrapped = var1;
   }

   public void setInfo(int var1) {
      this.wrapped.setInfo(var1);
   }

   public int getLineNumber() {
      return this.wrapped.getLineNumber();
   }

   public String toString(String var1) {
      return var1 + "[Taint Wrap]\n" + this.wrapped.toString(var1 + "   ");
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      String var2 = var1.hasFrame() ? TaintUtils.checkArguments(var1.getCurrentFrame()) : null;
      return this.callit(var1, var2);
   }

   protected Scalar callit(ScriptEnvironment var1, String var2) {
      this.wrapped.evaluate(var1);
      if (var2 != null && var1.hasFrame() && !var1.getCurrentFrame().isEmpty() && !SleepUtils.isEmptyScalar((Scalar)var1.getCurrentFrame().peek()) && ((Scalar)var1.getCurrentFrame().peek()).getActualValue() != null) {
         TaintUtils.taint((Scalar)var1.getCurrentFrame().peek());
         if ((var1.getScriptInstance().getDebugFlags() & 128) == 128) {
            var1.getScriptInstance().fireWarning("tainted value: " + SleepUtils.describe((Scalar)var1.getCurrentFrame().peek()) + " from: " + var2, this.getLineNumber());
         }

         return (Scalar)var1.getCurrentFrame().peek();
      } else {
         return null;
      }
   }
}
