package aggressor.bridges;

import java.util.Hashtable;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.engine.Block;
import sleep.interfaces.Environment;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class Aliases implements Function, Environment, Loadable {
   protected AliasManager manager;

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("&alias", this);
      var2.put("alias", this);
      var2.put("&alias_clear", this);
      var2.put("&fireAlias", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      SleepClosure var5 = new SleepClosure(var1, var4);
      this.manager.registerCommand(var3, var5);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      if (var1.equals("&fireAlias")) {
         var4 = BridgeUtilities.getString(var3, "");
         String var5 = BridgeUtilities.getString(var3, "");
         String var6 = BridgeUtilities.getString(var3, "");
         this.manager.fireCommand(var4, var5, var6);
      } else if (var1.equals("&alias")) {
         var4 = BridgeUtilities.getString(var3, "");
         SleepClosure var7 = BridgeUtilities.getFunction(var3, var2);
         this.manager.registerCommand(var4, var7);
      } else if (var1.equals("&alias_clear")) {
         var4 = BridgeUtilities.getString(var3, "");
         this.manager.clearCommand(var4);
      }

      return SleepUtils.getEmptyScalar();
   }

   public Aliases(AliasManager var1) {
      this.manager = var1;
   }
}
