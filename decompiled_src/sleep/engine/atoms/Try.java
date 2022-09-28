package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class Try extends Step {
   Block owner;
   Block handler;
   String var;

   public Try(Block var1, Block var2, String var3) {
      this.owner = var1;
      this.handler = var2;
      this.var = var3;
   }

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Try]\n");
      var2.append(this.owner.toString(var1 + "   "));
      var2.append(var1);
      var2.append("[Catch]: " + this.var + "\n");
      var2.append(this.handler.toString(var1 + "   "));
      return var2.toString();
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      int var2 = var1.markFrame();
      var1.installExceptionHandler(this.owner, this.handler, this.var);
      Scalar var3 = this.owner.evaluate(var1);
      var1.cleanFrame(var2);
      return var3;
   }
}
