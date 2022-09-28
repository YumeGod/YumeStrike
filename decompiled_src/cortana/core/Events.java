package cortana.core;

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

public class Events implements Function, Environment, Loadable {
   protected EventManager manager;

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("&on", this);
      var2.put("on", this);
      var2.put("&when", this);
      var2.put("when", this);
      var2.put("&fireEvent", this);
      var2.put("&fire_event_local", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   protected void addListener(String var1, SleepClosure var2, boolean var3) {
      this.manager.addListener(var1, var2, var3);
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      boolean var5 = var2.equals("when");
      SleepClosure var6 = new SleepClosure(var1, var4);
      this.addListener(var3, var6, var5);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var7;
      if (var1.equals("&fireEvent")) {
         var7 = BridgeUtilities.getString(var3, "");
         this.manager.fireEvent(var7, EventManager.shallowCopy(var3));
         return SleepUtils.getEmptyScalar();
      } else if (var1.equals("&fire_event_local")) {
         var7 = BridgeUtilities.getString(var3, "");
         return SleepUtils.getEmptyScalar();
      } else {
         boolean var4 = var1.equals("&when");
         String var5 = BridgeUtilities.getString(var3, "");
         SleepClosure var6 = BridgeUtilities.getFunction(var3, var2);
         this.addListener(var5, var6, var4);
         return SleepUtils.getEmptyScalar();
      }
   }

   public Events(EventManager var1) {
      this.manager = var1;
   }
}
