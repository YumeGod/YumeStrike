package aggressor.bridges;

import common.Callback;
import common.DisconnectListener;
import common.TeamQueue;
import common.TeamSocket;
import cortana.Cortana;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.engine.ObjectUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class TeamServerBridge implements Function, Loadable, DisconnectListener {
   protected TeamQueue conn;
   protected Cortana engine;

   public TeamServerBridge(Cortana var1, TeamQueue var2) {
      this.engine = var1;
      this.conn = var2;
      var2.addDisconnectListener(this);
   }

   public void disconnected(TeamSocket var1) {
      this.engine.getEventManager().fireEvent("disconnect", new Stack());
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&call", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&call")) {
         String var4 = BridgeUtilities.getString(var3, "");
         Scalar var5 = (Scalar)var3.pop();
         Callback var6;
         if (SleepUtils.isEmptyScalar(var5)) {
            var6 = null;
         } else {
            var6 = (Callback)ObjectUtilities.buildArgument(Callback.class, var5, var2);
         }

         Object[] var7 = new Object[var3.size()];

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var7[var8] = BridgeUtilities.getObject(var3);
         }

         this.conn.call(var4, var7, var6);
      }

      return SleepUtils.getEmptyScalar();
   }
}
