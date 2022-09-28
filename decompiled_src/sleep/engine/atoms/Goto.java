package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class Goto extends Step {
   protected Block iftrue;
   protected Check start;
   protected Block increment;

   public Goto(Check var1) {
      this.start = var1;
   }

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Goto]: \n");
      var2.append(var1);
      var2.append("  [Condition]: \n");
      var2.append(this.start.toString(var1 + "      "));
      if (this.iftrue != null) {
         var2.append(var1);
         var2.append("  [If true]:   \n");
         var2.append(this.iftrue.toString(var1 + "      "));
      }

      if (this.increment != null) {
         var2.append(var1);
         var2.append("  [Increment]:   \n");
         var2.append(this.increment.toString(var1 + "      "));
      }

      return var2.toString();
   }

   public void setIncrement(Block var1) {
      this.increment = var1;
   }

   public void setChoices(Block var1) {
      this.iftrue = var1;
   }

   public int getHighLineNumber() {
      return this.iftrue.getHighLineNumber();
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      while(!var1.isReturn() && this.start.check(var1)) {
         this.iftrue.evaluate(var1);
         if (var1.getFlowControlRequest() == 4) {
            var1.clearReturn();
            if (this.increment != null) {
               this.increment.evaluate(var1);
            }
         }

         if (var1.markFrame() >= 0) {
            var1.getCurrentFrame().clear();
         }
      }

      if (var1.getFlowControlRequest() == 2) {
         var1.clearReturn();
      }

      return null;
   }
}
