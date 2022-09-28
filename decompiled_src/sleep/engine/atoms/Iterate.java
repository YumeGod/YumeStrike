package sleep.engine.atoms;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import sleep.engine.Step;
import sleep.interfaces.Variable;
import sleep.runtime.ProxyIterator;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class Iterate extends Step {
   public static final int ITERATOR_CREATE = 1;
   public static final int ITERATOR_DESTROY = 2;
   public static final int ITERATOR_NEXT = 3;
   protected int type = 0;
   protected String key;
   protected String value;

   public String toString(String var1) {
      switch (this.type) {
         case 1:
            return var1 + "[Create Iterator]\n";
         case 2:
            return var1 + "[Destroy Iterator]\n";
         case 3:
            return var1 + "[Iterator next]\n";
         default:
            return var1 + "[Iterator Unknown!@]";
      }
   }

   public Iterate(String var1, String var2, int var3) {
      this.type = var3;
      this.key = var1;
      this.value = var2;
   }

   private void iterator_destroy(ScriptEnvironment var1) {
      Stack var2 = (Stack)((Stack)var1.getContextMetadata("iterators"));
      var2.pop();
   }

   private void iterator_create(ScriptEnvironment var1) {
      Stack var2 = var1.getCurrentFrame();
      IteratorData var3 = new IteratorData();
      var3.source = (Scalar)((Scalar)var2.pop());
      var1.KillFrame();
      var3.value = this.value;
      var3.venv = var1.getScriptVariables().getScalarLevel(this.value, var1.getScriptInstance());
      if (var3.venv == null) {
         var3.venv = var1.getScriptVariables().getGlobalVariables();
         if ((var1.getScriptInstance().getDebugFlags() & 4) == 4) {
            var1.showDebugMessage("variable '" + var3.value + "' not declared");
         }
      }

      if (this.key != null) {
         var3.key = this.key;
         var3.kenv = var1.getScriptVariables().getScalarLevel(this.key, var1.getScriptInstance());
         if (var3.kenv == null) {
            var3.kenv = var1.getScriptVariables().getGlobalVariables();
            if ((var1.getScriptInstance().getDebugFlags() & 4) == 4) {
               var1.showDebugMessage("variable '" + var3.key + "' not declared");
            }
         }
      }

      if (var3.source.getHash() != null) {
         var3.iterator = var3.source.getHash().getData().entrySet().iterator();
      } else if (var3.source.getArray() != null) {
         var3.iterator = var3.source.getArray().scalarIterator();
      } else if (SleepUtils.isFunctionScalar(var3.source)) {
         var3.iterator = SleepUtils.getFunctionFromScalar(var3.source, var1.getScriptInstance()).scalarIterator();
      } else if (ProxyIterator.isIterator(var3.source)) {
         var3.iterator = new ProxyIterator((Iterator)var3.source.objectValue(), true);
      } else {
         var1.getScriptInstance().fireWarning("Attempted to use foreach on non-array: '" + var3.source + "'", this.getLineNumber());
         var3.iterator = null;
      }

      Stack var4 = (Stack)((Stack)var1.getContextMetadata("iterators"));
      if (var4 == null) {
         var4 = new Stack();
         var1.setContextMetadata("iterators", var4);
      }

      var4.push(var3);
   }

   private void iterator_next(ScriptEnvironment var1) {
      Stack var2 = (Stack)((Stack)var1.getContextMetadata("iterators"));
      IteratorData var3 = (IteratorData)((IteratorData)var2.peek());
      if (var3.iterator != null && var3.iterator.hasNext()) {
         var1.getCurrentFrame().push(SleepUtils.getScalar(true));
         Object var4 = null;

         try {
            var4 = var3.iterator.next();
         } catch (ConcurrentModificationException var6) {
            var3.iterator = null;
            throw var6;
         }

         if (var3.source.getHash() != null) {
            if (SleepUtils.isEmptyScalar((Scalar)((Map.Entry)var4).getValue())) {
               var1.getCurrentFrame().pop();
               this.iterator_next(var1);
               return;
            }

            if (var3.key != null) {
               var3.kenv.putScalar(var3.key, SleepUtils.getScalar(((Map.Entry)var4).getKey()));
               var3.venv.putScalar(var3.value, (Scalar)((Map.Entry)var4).getValue());
            } else {
               var3.venv.putScalar(var3.value, SleepUtils.getScalar(((Map.Entry)var4).getKey()));
            }
         } else if (var3.key != null) {
            var3.kenv.putScalar(var3.key, SleepUtils.getScalar(var3.count));
            var3.venv.putScalar(var3.value, (Scalar)var4);
         } else {
            var3.venv.putScalar(var3.value, (Scalar)var4);
         }

         ++var3.count;
      } else {
         var1.getCurrentFrame().push(SleepUtils.getScalar(false));
      }
   }

   public Scalar evaluate(ScriptEnvironment var1) {
      if (this.type == 3) {
         this.iterator_next(var1);
      } else if (this.type == 1) {
         this.iterator_create(var1);
      } else if (this.type == 2) {
         this.iterator_destroy(var1);
      }

      return null;
   }

   public static class IteratorData {
      public String key = null;
      public Variable kenv = null;
      public String value = null;
      public Variable venv = null;
      public Scalar source = null;
      public Iterator iterator = null;
      public int count = 0;
   }
}
