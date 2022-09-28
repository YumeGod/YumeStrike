package sleep.bridges;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class TimeDateBridge implements Loadable {
   public void scriptLoaded(ScriptInstance var1) {
      var1.getScriptEnvironment().getEnvironment().put("&ticks", new ticks());
      var1.getScriptEnvironment().getEnvironment().put("&formatDate", new formatDate());
      var1.getScriptEnvironment().getEnvironment().put("&parseDate", new parseDate());
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   private static class ticks implements Function {
      private ticks() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getScalar(System.currentTimeMillis());
      }

      // $FF: synthetic method
      ticks(Object var1) {
         this();
      }
   }

   private static class parseDate implements Function {
      private parseDate() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = var3.pop().toString();
         String var5 = var3.pop().toString();
         SimpleDateFormat var6 = new SimpleDateFormat(var4);
         Date var7 = var6.parse(var5, new ParsePosition(0));
         return SleepUtils.getScalar(var7.getTime());
      }

      // $FF: synthetic method
      parseDate(Object var1) {
         this();
      }
   }

   private static class formatDate implements Function {
      private formatDate() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         long var4 = System.currentTimeMillis();
         if (var3.size() == 2) {
            var4 = BridgeUtilities.getLong(var3);
         }

         String var6 = var3.pop().toString();
         SimpleDateFormat var7 = new SimpleDateFormat(var6);
         Date var8 = new Date(var4);
         return SleepUtils.getScalar(var7.format(var8, new StringBuffer(), new FieldPosition(0)).toString());
      }

      // $FF: synthetic method
      formatDate(Object var1) {
         this();
      }
   }
}
