package sleep.bridges;

import java.util.Hashtable;
import sleep.engine.Block;
import sleep.interfaces.Environment;
import sleep.interfaces.Loadable;
import sleep.runtime.ScriptInstance;

public class DefaultEnvironment implements Loadable, Environment {
   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("sub", this);
      var2.put("inline", this);
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      Hashtable var5 = var1.getScriptEnvironment().getEnvironment();
      if (var2.equals("sub")) {
         var5.put("&" + var3, new SleepClosure(var1, var4));
      } else if (var2.equals("inline")) {
         var5.put("^&" + var3, var4);
      }

   }
}
