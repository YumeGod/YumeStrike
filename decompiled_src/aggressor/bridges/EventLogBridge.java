package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.Callback;
import common.CommonUtils;
import common.LoggedEvent;
import cortana.Cortana;
import java.util.LinkedList;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class EventLogBridge implements Function, Loadable {
   protected AggressorClient client;

   public EventLogBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&say", this);
      Cortana.put(var1, "&privmsg", this);
      Cortana.put(var1, "&action", this);
      Cortana.put(var1, "&users", this);
      Cortana.put(var1, "&elog", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      String var5;
      if ("&say".equals(var1)) {
         var4 = DataUtils.getNick(this.client.getData());
         var5 = BridgeUtilities.getString(var3, "");
         this.client.getConnection().call("aggressor.event", CommonUtils.args(LoggedEvent.Public(var4, var5)), (Callback)null);
      } else if ("&privmsg".equals(var1)) {
         var4 = DataUtils.getNick(this.client.getData());
         var5 = BridgeUtilities.getString(var3, "");
         String var6 = BridgeUtilities.getString(var3, "");
         this.client.getConnection().call("aggressor.event", CommonUtils.args(LoggedEvent.Private(var4, var5, var6)), (Callback)null);
      } else if ("&action".equals(var1)) {
         var4 = DataUtils.getNick(this.client.getData());
         var5 = BridgeUtilities.getString(var3, "");
         this.client.getConnection().call("aggressor.event", CommonUtils.args(LoggedEvent.Action(var4, var5)), (Callback)null);
      } else if ("&elog".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         this.client.getConnection().call("aggressor.event", CommonUtils.args(LoggedEvent.Notify(var4)), (Callback)null);
      } else if ("&users".equals(var1)) {
         LinkedList var7 = new LinkedList(DataUtils.getUsers(this.client.getData()));
         return SleepUtils.getArrayWrapper(var7);
      }

      return SleepUtils.getEmptyScalar();
   }
}
