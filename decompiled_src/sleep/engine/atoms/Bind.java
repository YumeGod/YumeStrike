package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.Step;
import sleep.interfaces.Environment;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class Bind extends Step {
   String funcenv;
   Block code;
   Block name;

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Bind Function]: \n");
      var2.append(var1);
      var2.append("   [Name]:       \n");
      var2.append(var1);
      var2.append(this.name.toString(var1 + "      "));
      var2.append(var1);
      var2.append("   [Code]:       \n");
      var2.append(var1);
      var2.append(this.code.toString(var1 + "      "));
      return var2.toString();
   }

   public Bind(String var1, Block var2, Block var3) {
      this.funcenv = var1;
      this.name = var2;
      this.code = var3;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Environment var2 = var1.getFunctionEnvironment(this.funcenv);
      if (var2 != null) {
         var1.CreateFrame();
         this.name.evaluate(var1);
         Scalar var3 = (Scalar)var1.getCurrentFrame().pop();
         var1.KillFrame();
         var2.bindFunction(var1.getScriptInstance(), this.funcenv, var3.getValue().toString(), this.code);
      } else {
         var1.getScriptInstance().fireWarning("Attempting to bind code to non-existent environment: " + this.funcenv, this.getLineNumber());
      }

      return null;
   }
}
