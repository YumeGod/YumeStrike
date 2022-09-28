package cortana.core;

import common.ScriptUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class FormatManager {
   protected Map formats = new HashMap();

   public Loadable getBridge() {
      return new Formats(this);
   }

   public void register(String var1, SleepClosure var2) {
      this.formats.put(var1, var2);
   }

   public String format(String var1, Stack var2) {
      SleepClosure var3 = (SleepClosure)this.formats.get(var1);
      if (var3 == null) {
         return null;
      } else if (!var3.getOwner().isLoaded()) {
         return null;
      } else {
         Scalar var4 = SleepUtils.runCode((SleepClosure)var3, var1, (ScriptInstance)null, var2);
         return SleepUtils.isEmptyScalar(var4) ? null : var4.toString();
      }
   }

   public String format(String var1, Object[] var2) {
      Stack var3 = new Stack();
      int var4 = var2.length - 1;

      for(int var5 = 0; var5 < var2.length; ++var5) {
         var3.push(ScriptUtils.convertAll(var2[var4 - var5]));
      }

      return this.format(var1, var3);
   }
}
