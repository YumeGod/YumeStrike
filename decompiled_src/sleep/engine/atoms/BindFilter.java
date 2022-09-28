package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.Step;
import sleep.interfaces.FilterEnvironment;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class BindFilter extends Step {
   String funcenv;
   Block code;
   String filter;
   String name;

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[Bind Filter]: " + this.name + "\n");
      var1.append("   [Filter]:       \n");
      var1.append("      " + this.filter.toString());
      var1.append("   [Code]:       \n");
      var1.append(this.code.toString("      "));
      return var1.toString();
   }

   public BindFilter(String var1, String var2, Block var3, String var4) {
      this.funcenv = var1;
      this.code = var3;
      this.filter = var4;
      this.name = var2;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      FilterEnvironment var2 = var1.getFilterEnvironment(this.funcenv);
      if (var2 != null) {
         var2.bindFilteredFunction(var1.getScriptInstance(), this.funcenv, this.name, this.filter, this.code);
      } else {
         var1.getScriptInstance().fireWarning("Attempting to bind code to non-existent predicate environment: " + this.funcenv, this.getLineNumber());
      }

      return null;
   }
}
