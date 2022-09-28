package sleep.engine.atoms;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class PLiteral extends Step {
   public static final int STRING_FRAGMENT = 1;
   public static final int ALIGN_FRAGMENT = 2;
   public static final int VAR_FRAGMENT = 3;
   private List fragments;

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[Parsed Literal] ");
      Iterator var3 = this.fragments.iterator();

      while(var3.hasNext()) {
         Fragment var4 = (Fragment)var3.next();
         switch (var4.type) {
            case 1:
               var2.append(var4.element);
               break;
            case 2:
               var2.append("[:align:]");
               break;
            case 3:
               var2.append("[:var:]");
         }
      }

      var2.append("\n");
      return var2.toString();
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Scalar var2 = SleepUtils.getScalar(this.buildString(var1));
      var1.getCurrentFrame().push(var2);
      return var2;
   }

   public PLiteral(List var1) {
      this.fragments = var1;
   }

   public static Fragment fragment(int var0, Object var1) {
      Fragment var2 = new Fragment();
      var2.element = var1;
      var2.type = var0;
      return var2;
   }

   private String buildString(ScriptEnvironment var1) {
      StringBuffer var2 = new StringBuffer();
      int var3 = 0;
      Iterator var5 = this.fragments.iterator();

      while(true) {
         while(var5.hasNext()) {
            Fragment var6 = (Fragment)var5.next();
            switch (var6.type) {
               case 1:
                  var2.append(var6.element);
                  break;
               case 2:
                  var3 = ((Scalar)var1.getCurrentFrame().remove(0)).getValue().intValue();
                  break;
               case 3:
                  String var4 = ((Scalar)var1.getCurrentFrame().remove(0)).getValue().toString();

                  int var7;
                  for(var7 = 0 - var4.length(); var7 > var3; --var7) {
                     var2.append(" ");
                  }

                  var2.append(var4);

                  for(var7 = var4.length(); var7 < var3; ++var7) {
                     var2.append(" ");
                  }

                  var3 = 0;
            }
         }

         var1.KillFrame();
         return var2.toString();
      }
   }

   private static final class Fragment implements Serializable {
      public Object element;
      public int type;

      private Fragment() {
      }

      // $FF: synthetic method
      Fragment(Object var1) {
         this();
      }
   }
}
