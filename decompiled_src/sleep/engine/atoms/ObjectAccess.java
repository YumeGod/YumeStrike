package sleep.engine.atoms;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import sleep.engine.CallRequest;
import sleep.engine.ObjectUtilities;
import sleep.engine.Step;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class ObjectAccess extends Step {
   protected String name;
   protected Class classRef;

   public ObjectAccess(String var1, Class var2) {
      this.name = var1;
      this.classRef = var2;
   }

   public String toString() {
      return "[Object Access]: " + this.classRef + "#" + this.name + "\n";
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      Object var2 = null;
      Class var3 = null;
      Scalar var4 = null;
      if (this.classRef == null) {
         var4 = (Scalar)var1.getCurrentFrame().pop();
         var2 = var4.objectValue();
         if (var2 == null) {
            var1.getScriptInstance().fireWarning("Attempted to call a non-static method on a null reference", this.getLineNumber());
            var1.KillFrame();
            var1.getCurrentFrame().push(SleepUtils.getEmptyScalar());
            return null;
         }

         var3 = var2.getClass();
      } else {
         var3 = this.classRef;
      }

      if (var4 != null && SleepUtils.isFunctionScalar(var4)) {
         CallRequest.ClosureCallRequest var14 = new CallRequest.ClosureCallRequest(var1, this.getLineNumber(), var4, this.name);
         var14.CallFunction();
         return null;
      } else if (this.name == null) {
         var1.getScriptInstance().fireWarning("Attempted to query an object with no method/field", this.getLineNumber());
         var1.KillFrame();
         var1.getCurrentFrame().push(SleepUtils.getEmptyScalar());
         return null;
      } else {
         Scalar var5 = SleepUtils.getEmptyScalar();
         Method var6 = ObjectUtilities.findMethod(var3, this.name, var1.getCurrentFrame());
         if (var6 != null && (this.classRef == null || (var6.getModifiers() & 8) == 8)) {
            try {
               var6.setAccessible(true);
            } catch (Exception var9) {
            }

            MethodCallRequest var15 = new MethodCallRequest(var1, this.getLineNumber(), var6, var4, this.name, var3);
            var15.CallFunction();
            return null;
         } else {
            if (var6 == null && !var1.getCurrentFrame().isEmpty()) {
               var1.getScriptInstance().fireWarning("there is no method that matches " + this.name + "(" + SleepUtils.describe(var1.getCurrentFrame()) + ") in " + var3.getName(), this.getLineNumber());
            } else {
               try {
                  Field var7;
                  try {
                     var7 = var3.getDeclaredField(this.name);
                  } catch (NoSuchFieldException var11) {
                     var7 = var3.getField(this.name);
                  }

                  if (var7 != null) {
                     try {
                        var7.setAccessible(true);
                     } catch (Exception var10) {
                     }

                     var5 = ObjectUtilities.BuildScalar(true, var7.get(var2));
                  } else {
                     var5 = SleepUtils.getEmptyScalar();
                  }
               } catch (NoSuchFieldException var12) {
                  var1.getScriptInstance().fireWarning("no field/method named " + this.name + " in " + var3, this.getLineNumber());
               } catch (IllegalAccessException var13) {
                  var1.getScriptInstance().fireWarning("cannot access " + this.name + " in " + var3 + ": " + var13.getMessage(), this.getLineNumber());
               }
            }

            var1.FrameResult(var5);
            return null;
         }
      }
   }

   private static class MethodCallRequest extends CallRequest {
      protected Method theMethod;
      protected Scalar scalar;
      protected String name;
      protected Class theClass;

      public MethodCallRequest(ScriptEnvironment var1, int var2, Method var3, Scalar var4, String var5, Class var6) {
         super(var1, var2);
         this.theMethod = var3;
         this.scalar = var4;
         this.name = var5;
         this.theClass = var6;
      }

      public String getFunctionName() {
         return this.theMethod.toString();
      }

      public String getFrameDescription() {
         return this.theMethod.toString();
      }

      public String formatCall(String var1) {
         StringBuffer var2 = new StringBuffer("[");
         if (var1 != null && var1.length() > 0) {
            var1 = ": " + var1;
         }

         if (this.scalar == null) {
            var2.append(this.theClass.getName() + " " + this.name + var1 + "]");
         } else {
            var2.append(SleepUtils.describe(this.scalar) + " " + this.name + var1 + "]");
         }

         return var2.toString();
      }

      protected Scalar execute() {
         Object[] var1 = ObjectUtilities.buildArgumentArray(this.theMethod.getParameterTypes(), this.getScriptEnvironment().getCurrentFrame(), this.getScriptEnvironment().getScriptInstance());

         try {
            return ObjectUtilities.BuildScalar(true, this.theMethod.invoke(this.scalar != null ? this.scalar.objectValue() : null, var1));
         } catch (InvocationTargetException var3) {
            if (var3.getCause() != null) {
               this.getScriptEnvironment().flagError(var3.getCause());
            }

            throw new RuntimeException(var3);
         } catch (IllegalArgumentException var4) {
            var4.printStackTrace();
            this.getScriptEnvironment().getScriptInstance().fireWarning(ObjectUtilities.buildArgumentErrorMessage(this.theClass, this.name, this.theMethod.getParameterTypes(), var1), this.getLineNumber());
         } catch (IllegalAccessException var5) {
            this.getScriptEnvironment().getScriptInstance().fireWarning("cannot access " + this.name + " in " + this.theClass + ": " + var5.getMessage(), this.getLineNumber());
         }

         return SleepUtils.getEmptyScalar();
      }
   }
}
