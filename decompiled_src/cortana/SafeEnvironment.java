package cortana;

import common.MudgeSanity;
import sleep.engine.Block;
import sleep.interfaces.Environment;
import sleep.runtime.ScriptInstance;

public class SafeEnvironment implements Environment {
   protected Environment f;

   public SafeEnvironment(Environment var1) {
      this.f = var1;
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      try {
         this.f.bindFunction(var1, var2, var3, var4);
      } catch (Exception var6) {
         MudgeSanity.logException("cortana bridge: " + var2 + " '" + var3 + "'", var6, false);
      }

   }
}
