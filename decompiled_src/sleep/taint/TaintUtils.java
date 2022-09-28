package sleep.taint;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import sleep.bridges.KeyValuePair;
import sleep.engine.types.ObjectValue;
import sleep.runtime.CollectionWrapper;
import sleep.runtime.MapWrapper;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScalarHash;
import sleep.runtime.ScalarType;
import sleep.runtime.SleepUtils;

public class TaintUtils {
   private static boolean isTaintMode = System.getProperty("sleep.taint", "false").equals("true");

   public static boolean isTaintMode() {
      return isTaintMode;
   }

   public static Scalar taint(Scalar var0) {
      if (isTaintMode() && var0.getActualValue() != null) {
         var0.setValue((ScalarType)(new TaintedValue(var0.getActualValue())));
      }

      return var0;
   }

   public static Stack taint(Stack var0) {
      if (isTaintMode()) {
         Iterator var1 = var0.iterator();

         while(var1.hasNext()) {
            taintAll((Scalar)var1.next());
         }
      }

      return var0;
   }

   public static Scalar taintAll(Scalar var0) {
      if (var0.getArray() != null && var0.getArray().getClass() == CollectionWrapper.class) {
         var0.setValue((ScalarArray)(new TaintArray(var0.getArray())));
      } else {
         Iterator var1;
         if (var0.getArray() != null) {
            var1 = var0.getArray().scalarIterator();

            while(var1.hasNext()) {
               taintAll((Scalar)var1.next());
            }
         } else if (var0.getHash() != null && var0.getHash().getClass() == MapWrapper.class) {
            var0.setValue((ScalarHash)(new TaintHash(var0.getHash())));
         } else if (var0.getHash() != null) {
            var1 = var0.getHash().getData().entrySet().iterator();

            while(var1.hasNext()) {
               Map.Entry var2 = (Map.Entry)var1.next();
               taintAll((Scalar)var2.getValue());
            }
         } else if (var0.getActualValue().getType() == ObjectValue.class && var0.objectValue().getClass() == KeyValuePair.class) {
            KeyValuePair var3 = (KeyValuePair)var0.objectValue();
            var0.setValue(SleepUtils.getScalar((Object)(new KeyValuePair(var3.getKey(), taintAll(var3.getValue())))));
         } else if (var0.getActualValue() != null) {
            var0.setValue((ScalarType)(new TaintedValue(var0.getActualValue())));
         }
      }

      return var0;
   }

   public static Scalar untaint(Scalar var0) {
      if (var0.getActualValue() != null && var0.getActualValue().getClass() == TaintedValue.class) {
         var0.setValue(((TaintedValue)var0.getActualValue()).untaint());
      }

      return var0;
   }

   private static boolean isTainted(Set var0, Scalar var1) {
      Iterator var2;
      if (var1.getHash() != null) {
         if (!var0.contains(var1.getHash())) {
            var0.add(var1.getHash());
            var2 = var1.getHash().getData().values().iterator();

            while(var2.hasNext()) {
               if (isTainted(var0, (Scalar)var2.next())) {
                  return true;
               }
            }
         }

         return false;
      } else if (var1.getArray() != null) {
         if (!var0.contains(var1.getArray())) {
            var0.add(var1.getArray());
            var2 = var1.getArray().scalarIterator();

            while(var2.hasNext()) {
               if (isTainted(var0, (Scalar)var2.next())) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return var1.getActualValue().getClass() == TaintedValue.class && !SleepUtils.isEmptyScalar(var1);
      }
   }

   public static boolean isTainted(Scalar var0) {
      return var0.getActualValue() == null ? isTainted(new HashSet(), var0) : isTainted((Set)null, var0);
   }

   public static Object Sanitizer(Object var0) {
      return isTaintMode() ? new Sanitizer(var0) : var0;
   }

   public static Object Tainter(Object var0) {
      return isTaintMode() ? new Tainter(var0) : var0;
   }

   public static Object Sensitive(Object var0) {
      return isTaintMode() ? new Sensitive(var0) : var0;
   }

   public static String checkArguments(Stack var0) {
      Stack var1 = new Stack();
      String var2 = null;
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Scalar var4 = (Scalar)var3.next();
         if (isTainted(var4)) {
            var1.push(var4);
         }
      }

      if (!var1.isEmpty()) {
         var2 = SleepUtils.describe(var1);
      }

      return var2;
   }
}
