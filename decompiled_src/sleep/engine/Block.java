package sleep.engine;

import java.io.File;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import sleep.engine.atoms.Goto;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Block implements Serializable {
   protected Step first;
   protected Step last;
   protected String source = "unknown";

   public Block(String var1) {
      this.source = var1;
   }

   public String toString(String var1) {
      StringBuffer var2 = new StringBuffer();

      for(Step var3 = this.first; var3 != null; var3 = var3.next) {
         var2.append(var3.toString(var1));
      }

      return var2.toString();
   }

   public String toString() {
      return this.toString("");
   }

   public String getSource() {
      return this.source;
   }

   public int getApproximateLineNumber() {
      return this.first != null ? this.first.getLineNumber() : -1;
   }

   public int getHighLineNumber() {
      int var1 = 0;

      for(Step var3 = this.first; var3 != null; var3 = var3.next) {
         int var2 = var3.getHighLineNumber();
         if (var2 > var1) {
            var1 = var2;
         }
      }

      return var1;
   }

   public int getLowLineNumber() {
      int var1 = Integer.MAX_VALUE;

      for(Step var3 = this.first; var3 != null; var3 = var3.next) {
         int var2 = var3.getLowLineNumber();
         if (var2 < var1) {
            var1 = var2;
         }
      }

      return var1;
   }

   public String getApproximateLineRange() {
      int var1 = this.getLowLineNumber();
      int var2 = this.getHighLineNumber();
      return var1 == var2 ? var1 + "" : var1 + "-" + var2;
   }

   public String getSourceLocation() {
      return (new File(this.source)).getName() + ":" + this.getApproximateLineRange();
   }

   public void add(Step var1) {
      if (this.first == null) {
         this.first = var1;
      } else {
         this.last.next = var1;
      }

      this.last = var1;
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      return this.evaluate(var1, this.first);
   }

   private void handleException(ScriptEnvironment var1) {
      if (var1.isResponsible(this)) {
         Block var2 = var1.getExceptionHandler();
         var2.evaluate(var1);
         var1.getScriptInstance().clearStackTrace();
      }

   }

   private void cleanupEnvironment(ScriptEnvironment var1) {
      var1.popSource();
      if (var1.isResponsible(this)) {
         var1.popExceptionContext();
      }

   }

   public Scalar evaluate(ScriptEnvironment var1, Step var2) {
      if (var1.isReturn()) {
         if (var1.isThrownValue()) {
            var1.pushSource(this.source);
            this.handleException(var1);
            this.cleanupEnvironment(var1);
         }

         return var1.getReturnValue();
      } else {
         var1.pushSource(this.source);

         for(Step var3 = var2; var3 != null; var3 = var3.next) {
            try {
               var3.evaluate(var1);
            } catch (Exception var5) {
               if (var5 instanceof IllegalArgumentException) {
                  var1.getScriptInstance().fireWarning(var5.getMessage(), var3.getLineNumber());
               } else if (var5 instanceof IndexOutOfBoundsException) {
                  if (var5.getMessage() != null) {
                     var1.getScriptInstance().fireWarning("attempted an invalid index: " + var5.getMessage(), var3.getLineNumber());
                  } else {
                     var1.getScriptInstance().fireWarning("attempted an invalid index", var3.getLineNumber());
                  }
               } else if (var5 instanceof ClassCastException) {
                  var1.getScriptInstance().fireWarning("attempted an invalid cast: " + var5.getMessage(), var3.getLineNumber());
               } else if (var5 instanceof NullPointerException) {
                  var1.getScriptInstance().fireWarning("null value error", var3.getLineNumber());
               } else if (var5 instanceof ConcurrentModificationException) {
                  if (var5.getMessage() != null) {
                     var1.getScriptInstance().fireWarning("unsafe data modification: " + var5.getMessage(), var3.getLineNumber());
                  } else {
                     var1.getScriptInstance().fireWarning("detected unsafe data modification", var3.getLineNumber());
                  }
               } else if (var5 instanceof RuntimeException) {
                  if (var5.getMessage() == null) {
                     var1.getScriptInstance().fireWarning("internal error - " + var5.getClass(), var3.getLineNumber());
                  } else {
                     var1.getScriptInstance().fireWarning(var5.getMessage(), var3.getLineNumber());
                  }
               } else {
                  var1.getScriptInstance().fireWarning(var5.toString(), var3.getLineNumber());
               }

               this.cleanupEnvironment(var1);
               return SleepUtils.getEmptyScalar();
            } catch (Error var6) {
               var1.getScriptInstance().fireWarning("critical internal error - " + var6.toString(), var3.getLineNumber());
               this.cleanupEnvironment(var1);
               throw var6;
            }

            while(var1.isReturn()) {
               if (var1.isYield()) {
                  if (var3 instanceof Goto) {
                     var1.addToContext(this, var3);
                  } else {
                     var1.addToContext(this, var3.next);
                  }
               }

               if (var1.isCallCC()) {
                  var1.getCurrentFrame().push(this.source);
                  var1.getCurrentFrame().push(new Integer(var3.getLineNumber()));
               }

               if (var1.isThrownValue()) {
                  if (!var1.isExceptionHandlerInstalled()) {
                     if (!SleepUtils.isEmptyScalar(var1.getReturnValue())) {
                        var1.getScriptInstance().fireWarning("Uncaught exception: " + var1.getExceptionMessage(), var3.getLineNumber());
                        var1.flagReturn((Scalar)null, 16);
                     }
                  } else if (!SleepUtils.isEmptyScalar(var1.getReturnValue())) {
                     this.handleException(var1);
                  }

                  this.cleanupEnvironment(var1);
                  return var1.getReturnValue();
               }

               if (!var1.isDebugInterrupt()) {
                  this.cleanupEnvironment(var1);
                  return var1.getReturnValue();
               }

               var1.getScriptInstance().fireWarning(var1.getDebugString(), var3.getLineNumber());
            }
         }

         this.cleanupEnvironment(var1);
         return SleepUtils.getEmptyScalar();
      }
   }
}
