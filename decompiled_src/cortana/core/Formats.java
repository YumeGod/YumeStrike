package cortana.core;

import java.util.Hashtable;
import sleep.bridges.SleepClosure;
import sleep.engine.Block;
import sleep.interfaces.Environment;
import sleep.interfaces.Loadable;
import sleep.runtime.ScriptInstance;

public class Formats implements Environment, Loadable {
   protected FormatManager manager;

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("set", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   protected void register(String var1, SleepClosure var2) {
      this.manager.register(var1, var2);
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      SleepClosure var5 = new SleepClosure(var1, var4);
      this.register(var3, var5);
   }

   public Formats(FormatManager var1) {
      this.manager = var1;
   }
}
