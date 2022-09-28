package sleep.engine.atoms;

import java.io.Serializable;
import sleep.engine.Block;
import sleep.interfaces.Predicate;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class CheckEval implements Check, Serializable {
   private Check iftrue;
   private Check iffalse;
   private Block setup;
   private boolean negate;
   public String name;
   private int hint = -1;

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Predicate]: ");
      var2.append("name->");
      var2.append(this.name);
      var2.append("  negated->");
      var2.append(this.negate);
      var2.append("\n");
      var2.append(var1);
      var2.append("   ");
      var2.append("[Setup]: \n");
      var2.append(this.setup.toString(var1 + "      "));
      return var2.toString();
   }

   public String toString() {
      return this.toString("");
   }

   public CheckEval(String var1, Block var2) {
      if (var1.charAt(0) == '!' && var1.length() > 2) {
         this.name = var1.substring(1, var1.length());
         this.negate = true;
      } else {
         this.name = var1;
         this.negate = false;
      }

      this.setup = var2;
      this.iftrue = null;
      this.iffalse = null;
   }

   public void setInfo(int var1) {
      this.hint = var1;
   }

   public boolean check(ScriptEnvironment var1) {
      var1.CreateFrame();
      this.setup.evaluate(var1);
      Predicate var2 = var1.getPredicate(this.name);
      boolean var3;
      if (var2 == null) {
         var1.getScriptInstance().fireWarning("Attempted to use non-existent predicate: " + this.name, this.hint);
         var3 = false;
      } else if ((var1.getScriptInstance().getDebugFlags() & 64) == 64) {
         StringBuffer var4 = new StringBuffer(64);
         if (var1.getCurrentFrame().size() >= 2) {
            var4.append(SleepUtils.describe((Scalar)var1.getCurrentFrame().get(0)));
            var4.append(" ");
            if (this.negate) {
               var4.append("!");
            }

            var4.append(this.name);
            var4.append(" ");
            var4.append(SleepUtils.describe((Scalar)var1.getCurrentFrame().get(1)));
         } else if (var1.getCurrentFrame().size() == 1) {
            if (this.negate) {
               var4.append("!");
            }

            var4.append(this.name);
            var4.append(" ");
            var4.append(SleepUtils.describe((Scalar)var1.getCurrentFrame().get(0)));
         } else {
            var4.append("corrupted stack frame: " + this.name);
         }

         var3 = var2.decide(this.name, var1.getScriptInstance(), var1.getCurrentFrame());
         var4.append(" ? ");
         if (this.negate) {
            var4.append((!var3 + "").toUpperCase());
         } else {
            var4.append((var3 + "").toUpperCase());
         }

         var1.getScriptInstance().fireWarning(var4.toString(), this.hint, true);
      } else {
         var3 = var2.decide(this.name, var1.getScriptInstance(), var1.getCurrentFrame());
      }

      var1.KillFrame();
      if (this.negate) {
         var3 = !var3;
      }

      return var3;
   }
}
