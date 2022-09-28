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

public class SecureShellAliases implements Function, Environment, Loadable {
   protected AliasManager manager;

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("&ssh_alias", this);
      var2.put("ssh_alias", this);
      var2.put("&fireSSHAlias", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      SleepClosure var5 = new SleepClosure(var1, var4);
      this.manager.registerCommand(var3, var5);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      if (var1.equals("&fireSSHAlias")) {
         var4 = BridgeUtilities.getString(var3, "");
         String var7 = BridgeUtilities.getString(var3, "");
         String var6 = BridgeUtilities.getString(var3, "");
         this.manager.fireCommand(var4, var7, var6);
         return SleepUtils.getEmptyScalar();
      } else {
         var4 = BridgeUtilities.getString(var3, "");
         SleepClosure var5 = BridgeUtilities.getFunction(var3, var2);
         this.manager.registerCommand(var4, var5);
         return SleepUtils.getEmptyScalar();
      }
   }

   public SecureShellAliases(AliasManager var1) {
      this.manager = var1;
   }
}
