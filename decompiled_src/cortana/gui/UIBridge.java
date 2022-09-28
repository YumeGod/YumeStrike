package cortana.gui;

import common.CommonUtils;
import cortana.core.EventManager;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class UIBridge implements Loadable, Function {
   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&later")) {
         final SleepClosure var4 = BridgeUtilities.getFunction(var3, var2);
         final Stack var5 = EventManager.shallowCopy(var3);
         CommonUtils.runSafe(new Runnable() {
            public void run() {
               SleepUtils.runCode((SleepClosure)var4, "laterz", (ScriptInstance)null, var5);
            }
         });
      }

      return SleepUtils.getEmptyScalar();
   }

   public void scriptLoaded(ScriptInstance var1) {
      var1.getScriptEnvironment().getEnvironment().put("&later", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }
}
