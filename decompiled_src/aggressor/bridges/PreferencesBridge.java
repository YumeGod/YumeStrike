package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.Prefs;
import cortana.Cortana;
import java.util.List;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class PreferencesBridge implements Function, Loadable {
   protected AggressorClient client;

   public PreferencesBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&pref_set", this);
      Cortana.put(var1, "&pref_set_list", this);
      Cortana.put(var1, "&pref_get", this);
      Cortana.put(var1, "&pref_get_list", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      String var5;
      if ("&pref_set".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         var5 = BridgeUtilities.getString(var3, "");
         Prefs.getPreferences().set(var4, var5);
         Prefs.getPreferences().save();
      } else if ("&pref_set_list".equals(var1)) {
         var4 = BridgeUtilities.getString(var3, "");
         List var6 = SleepUtils.getListFromArray(BridgeUtilities.getScalar(var3));
         Prefs.getPreferences().setList(var4, var6);
         Prefs.getPreferences().save();
      } else {
         if ("&pref_get".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var5 = BridgeUtilities.getString(var3, "");
            return SleepUtils.getScalar(Prefs.getPreferences().getString(var4, var5));
         }

         if ("&pref_get_list".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            return SleepUtils.getArrayWrapper(Prefs.getPreferences().getList(var4));
         }
      }

      return SleepUtils.getEmptyScalar();
   }
}
