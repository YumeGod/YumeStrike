package sleep.engine;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Stack;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ProxyInterface implements InvocationHandler {
   protected ScriptInstance script;
   protected Function func;

   public ProxyInterface(Function var1, ScriptInstance var2) {
      this.func = var1;
      this.script = var2;
   }

   public ScriptInstance getOwner() {
      return this.script;
   }

   public String toString() {
      return this.func.toString();
   }

   public static Object BuildInterface(Class var0, Function var1, ScriptInstance var2) {
      return BuildInterface(new Class[]{var0}, var1, var2);
   }

   public static Object BuildInterface(Class[] var0, Function var1, ScriptInstance var2) {
      ProxyInterface var3 = new ProxyInterface(var1, var2);
      return Proxy.newProxyInstance(var0[0].getClassLoader(), var0, var3);
   }

   public static Object BuildInterface(Class var0, Block var1, ScriptInstance var2) {
      return BuildInterface((Class)var0, (Function)(new SleepClosure(var2, var1)), var2);
   }

   public static Object BuildInterface(Class[] var0, Block var1, ScriptInstance var2) {
      return BuildInterface((Class[])var0, (Function)(new SleepClosure(var2, var1)), var2);
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      synchronized(this.script.getScriptVariables()) {
         this.script.getScriptEnvironment().pushSource("<Java>");
         Stack var5 = new Stack();
         boolean var6 = (this.script.getDebugFlags() & 8) == 8;
         StringBuffer var7 = null;
         if (var3 != null) {
            for(int var8 = var3.length - 1; var8 >= 0; --var8) {
               var5.push(ObjectUtilities.BuildScalar(true, var3[var8]));
            }
         }

         this.script.getScriptEnvironment().installExceptionHandler((Block)null, (Block)null, (String)null);
         Scalar var13;
         if (var6) {
            if (!this.script.isProfileOnly()) {
               var7 = new StringBuffer("[" + this.func + " " + var2.getName());
               if (!var5.isEmpty()) {
                  var7.append(": " + SleepUtils.describe(var5));
               }

               var7.append("]");
            }

            long var9 = System.currentTimeMillis();
            var13 = this.func.evaluate(var2.getName(), this.script, var5);
            var9 = System.currentTimeMillis() - var9;
            if (this.func.getClass() == SleepClosure.class) {
               this.script.collect(((SleepClosure)this.func).toStringGeneric(), -1, var9);
            }

            if (var7 != null) {
               if (this.script.getScriptEnvironment().isThrownValue()) {
                  var7.append(" - FAILED!");
               } else {
                  var7.append(" = " + SleepUtils.describe(var13));
               }

               this.script.fireWarning(var7.toString(), -1, true);
            }
         } else {
            var13 = this.func.evaluate(var2.getName(), this.script, var5);
         }

         this.script.getScriptEnvironment().popExceptionContext();
         this.script.getScriptEnvironment().clearReturn();
         this.script.getScriptEnvironment().popSource();
         if (this.script.getScriptEnvironment().isThrownValue()) {
            this.script.recordStackFrame(this.func + " as " + var2.toString(), "<Java>", -1);
            Object var14 = this.script.getScriptEnvironment().getExceptionMessage().objectValue();
            if (var14 instanceof Throwable) {
               throw (Throwable)var14;
            } else {
               throw new RuntimeException(var14.toString());
            }
         } else {
            return var13 != null ? ObjectUtilities.buildArgument(var2.getReturnType(), var13, this.script) : null;
         }
      }
   }
}
