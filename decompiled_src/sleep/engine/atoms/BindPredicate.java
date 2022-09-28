package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.Step;
import sleep.interfaces.PredicateEnvironment;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class BindPredicate extends Step {
   String funcenv;
   Check pred;
   Block code;

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[Bind Predicate]: \n");
      var1.append("   [Pred]:       \n");
      var1.append(this.pred.toString("      "));
      var1.append("   [Code]:       \n");
      var1.append(this.code.toString("      "));
      return var1.toString();
   }

   public BindPredicate(String var1, Check var2, Block var3) {
      this.funcenv = var1;
      this.pred = var2;
      this.code = var3;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      PredicateEnvironment var2 = var1.getPredicateEnvironment(this.funcenv);
      if (var2 != null) {
         var2.bindPredicate(var1.getScriptInstance(), this.funcenv, this.pred, this.code);
      } else {
         var1.getScriptInstance().fireWarning("Attempting to bind code to non-existent predicate environment: " + this.funcenv, this.getLineNumber());
      }

      return null;
   }
}
