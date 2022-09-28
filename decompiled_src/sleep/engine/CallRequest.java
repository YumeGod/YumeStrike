package sleep.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Variable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;

public abstract class CallRequest {
   protected ScriptEnvironment environment;
   protected int lineNumber;

   public CallRequest(ScriptEnvironment var1, int var2) {
      this.environment = var1;
      this.lineNumber = var2;
   }

   protected ScriptEnvironment getScriptEnvironment() {
      return this.environment;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public abstract String getFunctionName();

   public abstract String getFrameDescription();

   protected abstract Scalar execute();

   protected abstract String formatCall(String var1);

   public boolean isDebug() {
      return (this.getScriptEnvironment().getScriptInstance().getDebugFlags() & 8) == 8;
   }

   public void CallFunction() {
      Scalar var1 = null;
      ScriptEnvironment var2 = this.getScriptEnvironment();
      int var3 = this.getScriptEnvironment().markFrame();
      if (this.isDebug() && this.getLineNumber() != Integer.MIN_VALUE) {
         if (var2.getScriptInstance().isProfileOnly()) {
            try {
               long var12 = var2.getScriptInstance().total();
               long var6 = System.currentTimeMillis();
               var1 = this.execute();
               var6 = System.currentTimeMillis() - var6 - (var2.getScriptInstance().total() - var12);
               var2.getScriptInstance().collect(this.getFunctionName(), this.getLineNumber(), var6);
            } catch (RuntimeException var10) {
               if (var10.getCause() == null || !InvocationTargetException.class.isInstance(var10.getCause())) {
                  var2.cleanFrame(var3);
                  var2.KillFrame();
                  throw var10;
               }
            }
         } else {
            String var4 = SleepUtils.describe(var2.getCurrentFrame());

            try {
               long var5 = var2.getScriptInstance().total();
               long var7 = System.currentTimeMillis();
               var1 = this.execute();
               var7 = System.currentTimeMillis() - var7 - (var2.getScriptInstance().total() - var5);
               var2.getScriptInstance().collect(this.getFunctionName(), this.getLineNumber(), var7);
               if (var2.isThrownValue()) {
                  var2.getScriptInstance().fireWarning(this.formatCall(var4) + " - FAILED!", this.getLineNumber(), true);
               } else if (var2.isPassControl()) {
                  var2.getScriptInstance().fireWarning(this.formatCall(var4) + " -goto- " + SleepUtils.describe(var1), this.getLineNumber(), true);
               } else if (SleepUtils.isEmptyScalar(var1)) {
                  var2.getScriptInstance().fireWarning(this.formatCall(var4), this.getLineNumber(), true);
               } else {
                  var2.getScriptInstance().fireWarning(this.formatCall(var4) + " = " + SleepUtils.describe(var1), this.getLineNumber(), true);
               }
            } catch (RuntimeException var9) {
               var2.getScriptInstance().fireWarning(this.formatCall(var4) + " - FAILED!", this.getLineNumber(), true);
               if (var9.getCause() == null || !InvocationTargetException.class.isInstance(var9.getCause())) {
                  var2.cleanFrame(var3);
                  var2.KillFrame();
                  throw var9;
               }
            }
         }
      } else {
         try {
            var1 = this.execute();
         } catch (RuntimeException var11) {
            if (var11.getCause() == null || !InvocationTargetException.class.isInstance(var11.getCause())) {
               var2.cleanFrame(var3);
               var2.KillFrame();
               throw var11;
            }
         }
      }

      if (var2.isThrownValue()) {
         var2.getScriptInstance().recordStackFrame(this.getFrameDescription(), this.getLineNumber());
      }

      if (var1 == null) {
         var1 = SleepUtils.getEmptyScalar();
      }

      var2.cleanFrame(var3);
      var2.FrameResult(var1);
      if (var2.isPassControl()) {
         var2.pushSource(((SleepClosure)var1.objectValue()).getAndRemoveMetadata("sourceFile", "<unknown>") + "");
         int var13 = (Integer)((Integer)((SleepClosure)var1.objectValue()).getAndRemoveMetadata("sourceLine", new Integer(-1)));
         if (var2.markFrame() >= 0) {
            Object var14 = var2.getCurrentFrame().pop();
            if (var14 != var1) {
               var2.getScriptInstance().fireWarning("bad callcc stack: " + SleepUtils.describe((Scalar)var14) + " expected " + SleepUtils.describe(var1), var13);
            }
         }

         var2.flagReturn((Scalar)null, 0);
         var2.CreateFrame();
         var2.getCurrentFrame().push(((SleepClosure)var1.objectValue()).getAndRemoveMetadata("continuation", (Object)null));
         ClosureCallRequest var15 = new ClosureCallRequest(this.environment, var13, var1, "CALLCC");
         var15.CallFunction();
         var2.popSource();
      }

   }

   public static class InlineCallRequest extends CallRequest {
      protected String function;
      protected Block inline;

      public InlineCallRequest(ScriptEnvironment var1, int var2, String var3, Block var4) {
         super(var1, var2);
         this.function = var3;
         this.inline = var4;
      }

      public String getFunctionName() {
         return "<inline> " + this.function;
      }

      public String getFrameDescription() {
         return "<inline> " + this.function + "()";
      }

      protected String formatCall(String var1) {
         return "<inline> " + this.function + "(" + var1 + ")";
      }

      protected Scalar execute() {
         ScriptVariables var1 = this.getScriptEnvironment().getScriptVariables();
         synchronized(var1) {
            Variable var3 = var1.getLocalVariables();
            Scalar var4 = var3.getScalar("@_");
            int var5 = BridgeUtilities.initLocalScope(var1, var3, this.getScriptEnvironment().getCurrentFrame());
            Scalar var6 = this.inline.evaluate(this.getScriptEnvironment());
            if (var4 != null && var4.getArray() != null) {
               var3.putScalar("@_", var4);
               if (var5 > 0) {
                  Iterator var7 = var4.getArray().scalarIterator();

                  for(int var8 = 1; var7.hasNext() && var8 <= var5; ++var8) {
                     Scalar var9 = (Scalar)var7.next();
                     var3.putScalar("$" + var8, var9);
                  }
               }
            }

            return var6;
         }
      }
   }

   public static class FunctionCallRequest extends CallRequest {
      protected String function;
      protected Function callme;

      public FunctionCallRequest(ScriptEnvironment var1, int var2, String var3, Function var4) {
         super(var1, var2);
         this.function = var3;
         this.callme = var4;
      }

      public String getFunctionName() {
         return this.function;
      }

      public String getFrameDescription() {
         return this.function + "()";
      }

      public String formatCall(String var1) {
         return this.function + "(" + var1 + ")";
      }

      public boolean isDebug() {
         return super.isDebug() && !this.function.equals("&@") && !this.function.equals("&%") && !this.function.equals("&warn");
      }

      protected Scalar execute() {
         Scalar var1 = this.callme.evaluate(this.function, this.getScriptEnvironment().getScriptInstance(), this.getScriptEnvironment().getCurrentFrame());
         this.getScriptEnvironment().clearReturn();
         return var1;
      }
   }

   public static class ClosureCallRequest extends CallRequest {
      protected String name;
      protected Scalar scalar;

      public ClosureCallRequest(ScriptEnvironment var1, int var2, Scalar var3, String var4) {
         super(var1, var2);
         this.scalar = var3;
         this.name = var4;
      }

      public String getFunctionName() {
         return ((SleepClosure)this.scalar.objectValue()).toStringGeneric();
      }

      public String getFrameDescription() {
         return this.scalar.toString();
      }

      public String formatCall(String var1) {
         StringBuffer var2 = new StringBuffer("[" + SleepUtils.describe(this.scalar));
         if (this.name != null && this.name.length() > 0) {
            var2.append(" " + this.name);
         }

         if (var1.length() > 0) {
            var2.append(": " + var1);
         }

         var2.append("]");
         return var2.toString();
      }

      protected Scalar execute() {
         SleepClosure var1 = SleepUtils.getFunctionFromScalar(this.scalar, this.getScriptEnvironment().getScriptInstance());
         Scalar var2 = var1.evaluate(this.name, this.getScriptEnvironment().getScriptInstance(), this.getScriptEnvironment().getCurrentFrame());
         this.getScriptEnvironment().clearReturn();
         return var2;
      }
   }
}
