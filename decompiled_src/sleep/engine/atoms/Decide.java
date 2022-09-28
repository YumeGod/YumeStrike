package sleep.engine.atoms;

import sleep.engine.Block;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;

public class Decide extends Step {
   public Block iftrue;
   public Block iffalse;
   public Check start;

   public Decide(Check var1) {
      this.start = var1;
   }

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Decide]:\n");
      var2.append(var1);
      var2.append("  [Condition]: \n");
      var2.append(this.start.toString(var1 + "      "));
      if (this.iftrue != null) {
         var2.append(var1);
         var2.append("  [If true]:   \n");
         var2.append(this.iftrue.toString(var1 + "      "));
      }

      if (this.iffalse != null) {
         var2.append(var1);
         var2.append("  [If False]:   \n");
         var2.append(this.iffalse.toString(var1 + "      "));
      }

      return var2.toString();
   }

   public int getHighLineNumber() {
      if (this.iftrue == null) {
         return this.iffalse.getHighLineNumber();
      } else if (this.iffalse == null) {
         return this.iftrue.getHighLineNumber();
      } else {
         int var1 = this.iftrue.getHighLineNumber();
         int var2 = this.iffalse.getHighLineNumber();
         return var1 > var2 ? var1 : var2;
      }
   }

   public void setChoices(Block var1, Block var2) {
      this.iftrue = var1;
      this.iffalse = var2;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      if (this.start.check(var1)) {
         if (this.iftrue != null) {
            this.iftrue.evaluate(var1);
         }
      } else if (this.iffalse != null) {
         this.iffalse.evaluate(var1);
      }

      return null;
   }
}
