package sleep.engine.atoms;

import java.util.Iterator;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class AssignT extends Step {
   protected Step operator;

   public AssignT(Step var1) {
      this.operator = var1;
   }

   public AssignT() {
      this((Step)null);
   }

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1);
      var2.append("[AssignT]:\n");
      return var2.toString();
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Iterator var4 = null;
      Scalar var5 = (Scalar)var1.getCurrentFrame().pop();
      Scalar var6 = (Scalar)var1.getCurrentFrame().peek();
      if (var1.getCurrentFrame().size() == 1 && var6.getArray() != null && this.operator != null) {
         var4 = var6.getArray().scalarIterator();
      } else {
         var4 = var1.getCurrentFrame().iterator();
      }

      Scalar var2;
      Iterator var7;
      if (var5.getArray() == null) {
         var7 = var4;

         while(var7.hasNext()) {
            var2 = (Scalar)var7.next();
            if (this.operator != null) {
               var1.CreateFrame();
               var1.CreateFrame();
               var1.getCurrentFrame().push(var5);
               var1.getCurrentFrame().push(var2);
               this.operator.evaluate(var1);
               var2.setValue((Scalar)var1.getCurrentFrame().pop());
               var1.KillFrame();
            } else {
               var2.setValue(var5);
            }
         }

         var1.KillFrame();
         return null;
      } else {
         try {
            var7 = var5.getArray().scalarIterator();

            Scalar var3;
            for(Iterator var8 = var4; var8.hasNext(); var2.setValue(var3)) {
               var2 = (Scalar)var8.next();
               if (var7.hasNext()) {
                  var3 = (Scalar)var7.next();
               } else {
                  var3 = SleepUtils.getEmptyScalar();
               }

               if (this.operator != null) {
                  var1.CreateFrame();
                  var1.CreateFrame();
                  var1.getCurrentFrame().push(var3);
                  var1.getCurrentFrame().push(var2);
                  this.operator.evaluate(var1);
                  var3 = (Scalar)var1.getCurrentFrame().pop();
                  var1.KillFrame();
               }
            }

            var1.FrameResult(var5);
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         return null;
      }
   }
}
