package sleep.engine.atoms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import sleep.engine.CallRequest;
import sleep.engine.ObjectUtilities;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class ObjectNew extends Step {
   protected Class name;

   public ObjectNew(Class var1) {
      this.name = var1;
   }

   public String toString() {
      return "[Object New]: " + this.name + "\n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Constructor var3 = ObjectUtilities.findConstructor(this.name, var1.getCurrentFrame());
      if (var3 != null) {
         try {
            var3.setAccessible(true);
         } catch (Exception var5) {
         }

         ConstructorCallRequest var4 = new ConstructorCallRequest(var1, this.getLineNumber(), var3, this.name);
         var4.CallFunction();
         return null;
      } else {
         var1.getScriptInstance().fireWarning("no constructor matching " + this.name.getName() + "(" + SleepUtils.describe(var1.getCurrentFrame()) + ")", this.getLineNumber());
         Scalar var2 = SleepUtils.getEmptyScalar();
         var1.FrameResult(var2);
         return null;
      }
   }

   private static class ConstructorCallRequest extends CallRequest {
      protected Constructor theConstructor;
      protected Class name;

      public ConstructorCallRequest(ScriptEnvironment var1, int var2, Constructor var3, Class var4) {
         super(var1, var2);
         this.theConstructor = var3;
         this.name = var4;
      }

      public String getFunctionName() {
         return this.name.toString();
      }

      public String getFrameDescription() {
         return this.name.toString();
      }

      public String formatCall(String var1) {
         if (var1 != null && var1.length() > 0) {
            var1 = ": " + var1;
         }

         StringBuffer var2 = new StringBuffer("[new " + this.name.getName() + var1 + "]");
         return var2.toString();
      }

      protected Scalar execute() {
         Object[] var1 = ObjectUtilities.buildArgumentArray(this.theConstructor.getParameterTypes(), this.getScriptEnvironment().getCurrentFrame(), this.getScriptEnvironment().getScriptInstance());

         try {
            return ObjectUtilities.BuildScalar(false, this.theConstructor.newInstance(var1));
         } catch (InvocationTargetException var3) {
            if (var3.getCause() != null) {
               this.getScriptEnvironment().flagError(var3.getCause());
            }

            throw new RuntimeException(var3);
         } catch (IllegalArgumentException var4) {
            var4.printStackTrace();
            this.getScriptEnvironment().getScriptInstance().fireWarning(ObjectUtilities.buildArgumentErrorMessage(this.name, this.name.getName(), this.theConstructor.getParameterTypes(), var1), this.getLineNumber());
         } catch (InstantiationException var5) {
            this.getScriptEnvironment().getScriptInstance().fireWarning("unable to instantiate abstract class " + this.name.getName(), this.getLineNumber());
         } catch (IllegalAccessException var6) {
            this.getScriptEnvironment().getScriptInstance().fireWarning("cannot access constructor in " + this.name.getName() + ": " + var6.getMessage(), this.getLineNumber());
         }

         return SleepUtils.getEmptyScalar();
      }
   }
}
