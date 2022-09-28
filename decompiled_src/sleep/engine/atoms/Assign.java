package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class Assign extends Step {
   Block variable;
   Step operator;

   public Assign(Block var1, Step var2) {
      this.variable = null;
      this.operator = null;
      this.operator = var2;
      this.variable = var1;
   }

   public Assign(Block var1) {
      this(var1, (Step)null);
   }

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Assign]:\n");
      var2.append(this.variable.toString(var1 + "   "));
      return var2.toString();
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      if (var1.getCurrentFrame().size() > 1) {
         throw new RuntimeException("assignment is corrupted, did you forget a semicolon?");
      } else {
         var1.CreateFrame();
         this.variable.evaluate(var1);
         Scalar var2 = (Scalar)((Scalar)var1.getCurrentFrame().pop());
         var1.KillFrame();
         Scalar var3 = (Scalar)((Scalar)var1.getCurrentFrame().pop());
         if (this.operator != null) {
            var1.CreateFrame();
            var1.getCurrentFrame().push(var3);
            var1.getCurrentFrame().push(var2);
            this.operator.evaluate(var1);
            var3 = (Scalar)var1.getCurrentFrame().pop();
         }

         var2.setValue(var3);
         var1.FrameResult(var3);
         return null;
      }
   }
}
