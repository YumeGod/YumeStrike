package aggressor.bridges;

import common.CommonUtils;
import cortana.Cortana;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class AttackBridge implements Function, Loadable {
   protected List ids = new LinkedList();
   protected Map data = new HashMap();

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&attack_tactics", this);
      Cortana.put(var1, "&attack_name", this);
      Cortana.put(var1, "&attack_describe", this);
      Cortana.put(var1, "&attack_mitigate", this);
      Cortana.put(var1, "&attack_detect", this);
      Cortana.put(var1, "&attack_url", this);
   }

   public void loadAttackMatrix() {
      if (this.ids.size() <= 0) {
         List var1 = SleepUtils.getListFromArray((Scalar)CommonUtils.readObjectResource("resources/attack.bin"));
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Map var3 = (Map)var2.next();
            String var4 = (String)var3.get("id");
            this.ids.add(var4);
            this.data.put(var4, var3);
         }

      }
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      this.loadAttackMatrix();
      if ("&attack_tactics".equals(var1)) {
         return SleepUtils.getArrayWrapper(this.ids);
      } else {
         String var4 = BridgeUtilities.getString(var3, "");
         Map var5 = (Map)this.data.get(var4);
         if (var5 == null) {
            throw new RuntimeException("ATT&CK Technique '" + var4 + "' was not found.");
         } else if ("&attack_name".equals(var1)) {
            return SleepUtils.getScalar((String)var5.get("name"));
         } else if ("&attack_describe".equals(var1)) {
            return SleepUtils.getScalar((String)var5.get("describe"));
         } else if ("&attack_mitigate".equals(var1)) {
            return SleepUtils.getScalar((String)var5.get("mitigate"));
         } else if ("&attack_detect".equals(var1)) {
            return SleepUtils.getScalar((String)var5.get("detect"));
         } else {
            return "&attack_url".equals(var1) ? SleepUtils.getScalar("https://attack.mitre.org/wiki/Technique/" + var4) : SleepUtils.getEmptyScalar();
         }
      }
   }
}
