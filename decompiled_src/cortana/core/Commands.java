package cortana.core;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
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

public class Commands implements Function, Environment, Loadable {
   protected CommandManager manager;

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("&command", this);
      var2.put("command", this);
      var2.put("&fire_command", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      SleepClosure var5 = new SleepClosure(var1, var4);
      this.manager.registerCommand(var3, var5);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4 = BridgeUtilities.getString(var3, "");
      if (var1.equals("&fire_command")) {
         StringBuffer var8 = new StringBuffer();
         LinkedList var6 = new LinkedList(var3);
         var6.add(var4);
         Collections.reverse(var6);
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            var8.append(var7.next() + "");
            if (var7.hasNext()) {
               var8.append(" ");
            }
         }

         this.manager.fireCommand(var4, var8 + "", var3);
         return SleepUtils.getEmptyScalar();
      } else {
         SleepClosure var5 = BridgeUtilities.getFunction(var3, var2);
         this.manager.registerCommand(var4, var5);
         return SleepUtils.getEmptyScalar();
      }
   }

   public Commands(CommandManager var1) {
      this.manager = var1;
   }
}
