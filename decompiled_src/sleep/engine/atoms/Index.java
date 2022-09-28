package sleep.engine.atoms;

import sleep.bridges.SleepClosure;
import sleep.engine.Block;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Index extends Step {
   String value;
   Block index;

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Scalar index]: " + this.value + "\n");
      if (this.index != null) {
         var2.append(this.index.toString(var1 + "   "));
      }

      return var2.toString();
   }

   public Index(String var1, Block var2) {
      this.value = var1;
      this.index = var2;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Scalar var3 = null;
      Scalar var4 = (Scalar)var1.getCurrentFrame().pop();
      if (SleepUtils.isEmptyScalar(var4)) {
         if (this.value.charAt(0) == '@') {
            var4.setValue(SleepUtils.getArrayScalar());
         } else if (this.value.charAt(0) == '%') {
            var4.setValue(SleepUtils.getHashScalar());
         }
      }

      this.index.evaluate(var1);
      Scalar var2 = (Scalar)((Scalar)var1.getCurrentFrame().pop());
      if (var4.getArray() != null) {
         int var5 = var2.getValue().intValue();
         if (var5 < 0) {
            for(int var6 = var4.getArray().size(); var5 < 0; var5 += var6) {
            }
         }

         var3 = var4.getArray().getAt(var5);
      } else if (var4.getHash() != null) {
         var3 = var4.getHash().getAt(var2);
      } else {
         if (var4.objectValue() == null || !(var4.objectValue() instanceof SleepClosure)) {
            var1.KillFrame();
            throw new IllegalArgumentException("invalid use of index operator: " + SleepUtils.describe(var4) + "[" + SleepUtils.describe(var2) + "]");
         }

         SleepClosure var7 = (SleepClosure)var4.objectValue();
         if (!var7.getVariables().scalarExists(var2.toString())) {
            var7.getVariables().putScalar(var2.toString(), SleepUtils.getEmptyScalar());
         }

         var3 = var7.getVariables().getScalar(var2.toString());
      }

      var1.FrameResult(var3);
      return null;
   }
}
