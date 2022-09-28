package sleep.bridges;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import sleep.engine.types.ObjectValue;
import sleep.interfaces.Variable;
import sleep.parser.Checkers;
import sleep.runtime.CollectionWrapper;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScalarHash;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;

public class BridgeUtilities {
   private static final boolean doReplace;

   public static byte[] toByteArrayNoConversion(String var0) {
      byte[] var1 = new byte[var0.length()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = (byte)var0.charAt(var2);
      }

      return var1;
   }

   public static int getInt(Stack var0) {
      return getInt(var0, 0);
   }

   public static int getInt(Stack var0, int var1) {
      return var0.isEmpty() ? var1 : ((Scalar)var0.pop()).intValue();
   }

   public static Class getClass(Stack var0, Class var1) {
      Object var2 = getObject(var0);
      return var2 == null ? var1 : (Class)var2;
   }

   public static long getLong(Stack var0) {
      return getLong(var0, 0L);
   }

   public static long getLong(Stack var0, long var1) {
      return var0.isEmpty() ? var1 : ((Scalar)var0.pop()).longValue();
   }

   public static double getDouble(Stack var0) {
      return getDouble(var0, 0.0);
   }

   public static double getDouble(Stack var0, double var1) {
      return var0.isEmpty() ? var1 : ((Scalar)var0.pop()).doubleValue();
   }

   public static Map extractNamedParameters(Stack var0) {
      HashMap var1 = new HashMap();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Scalar var3 = (Scalar)var2.next();
         if (var3.objectValue() != null && var3.objectValue().getClass() == KeyValuePair.class) {
            var2.remove();
            KeyValuePair var4 = (KeyValuePair)var3.objectValue();
            var1.put(var4.getKey().toString(), var4.getValue());
         }
      }

      return var1;
   }

   public static Iterator getIterator(Stack var0, ScriptInstance var1) {
      if (var0.isEmpty()) {
         return getArray(var0).scalarIterator();
      } else {
         Scalar var2 = (Scalar)var0.pop();
         return SleepUtils.getIterator(var2, var1);
      }
   }

   public static ScalarArray getArray(Stack var0) {
      Scalar var1 = getScalar(var0);
      return var1.getArray() == null ? SleepUtils.getArrayScalar().getArray() : var1.getArray();
   }

   public static ScalarHash getHash(Stack var0) {
      return var0.isEmpty() ? SleepUtils.getHashScalar().getHash() : ((Scalar)var0.pop()).getHash();
   }

   public static ScalarArray getWorkableArray(Stack var0) {
      if (var0.isEmpty()) {
         return SleepUtils.getArrayScalar().getArray();
      } else {
         Scalar var1 = (Scalar)var0.pop();
         if (var1.getArray().getClass() != CollectionWrapper.class) {
            return var1.getArray();
         } else {
            ScalarArray var2 = SleepUtils.getArrayScalar().getArray();
            Iterator var3 = var1.getArray().scalarIterator();

            while(var3.hasNext()) {
               var2.push((Scalar)var3.next());
            }

            return var2;
         }
      }
   }

   public static Object getObject(Stack var0) {
      return var0.isEmpty() ? null : ((Scalar)var0.pop()).objectValue();
   }

   public static SleepClosure getFunction(Stack var0, ScriptInstance var1) {
      Scalar var2 = getScalar(var0);
      SleepClosure var3 = SleepUtils.getFunctionFromScalar(var2, var1);
      if (var3 == null) {
         throw new IllegalArgumentException("expected &closure--received: " + SleepUtils.describe(var2));
      } else {
         return var3;
      }
   }

   public static Scalar getScalar(Stack var0) {
      return var0.isEmpty() ? SleepUtils.getEmptyScalar() : (Scalar)var0.pop();
   }

   public static String getString(Stack var0, String var1) {
      if (var0.isEmpty()) {
         return var1;
      } else {
         String var2 = var0.pop().toString();
         return var2 == null ? var1 : var2;
      }
   }

   public static File toSleepFile(String var0, ScriptInstance var1) {
      if (var0 == null) {
         return var1.cwd();
      } else {
         if (doReplace) {
            var0 = var0.replace('/', File.separatorChar);
         }

         File var2 = new File(var0);
         return !var2.isAbsolute() && var0.length() > 0 ? new File(var1.cwd(), var0) : var2;
      }
   }

   public static File getFile(Stack var0, ScriptInstance var1) {
      return toSleepFile(var0.isEmpty() ? null : var0.pop().toString(), var1);
   }

   public static KeyValuePair getKeyValuePair(Stack var0) {
      Scalar var1 = getScalar(var0);
      if (var1.objectValue() != null && var1.objectValue().getClass() == KeyValuePair.class) {
         return (KeyValuePair)var1.objectValue();
      } else {
         if (var1.getActualValue() != null) {
            String var4 = var1.getActualValue().toString();
            if (var4.indexOf(61) > -1) {
               Scalar var2 = SleepUtils.getScalar(var4.substring(0, var4.indexOf(61)));
               Scalar var3 = SleepUtils.getScalar(var4.substring(var4.indexOf(61) + 1, var4.length()));
               return new KeyValuePair(var2, var3);
            }
         }

         throw new IllegalArgumentException("attempted to pass a malformed key value pair: " + var1);
      }
   }

   public static Scalar flattenArray(Scalar var0, Scalar var1) {
      return flattenIterator(var0.getArray().scalarIterator(), var1);
   }

   public static Scalar flattenIterator(Iterator var0, Scalar var1) {
      if (var1 == null) {
         var1 = SleepUtils.getArrayScalar();
      }

      while(var0.hasNext()) {
         Scalar var2 = (Scalar)var0.next();
         if (var2.getArray() != null) {
            flattenArray(var2, var1);
         } else {
            var1.getArray().push(var2);
         }
      }

      return var1;
   }

   public static int initLocalScope(ScriptVariables var0, Variable var1, Stack var2) {
      int var3 = 1;
      Scalar var4 = SleepUtils.getArrayScalar();

      while(true) {
         while(!var2.isEmpty()) {
            Scalar var5 = (Scalar)var2.pop();
            if (var5.getActualValue() != null && var5.getActualValue().getType() == ObjectValue.class && var5.getActualValue().objectValue() != null && var5.getActualValue().objectValue().getClass() == KeyValuePair.class) {
               KeyValuePair var6 = (KeyValuePair)var5.getActualValue().objectValue();
               if (!Checkers.isVariable(var6.getKey().toString())) {
                  throw new IllegalArgumentException("unreachable named parameter: " + var6.getKey());
               }

               var0.setScalarLevel(var6.getKey().toString(), var6.getValue(), var1);
            } else {
               var4.getArray().push(var5);
               var0.setScalarLevel("$" + var3, var5, var1);
               ++var3;
            }
         }

         var0.setScalarLevel("@_", var4, var1);
         return var3;
      }
   }

   public static final int normalize(int var0, int var1) {
      return var0 < 0 ? var0 + var1 : var0;
   }

   public static boolean expectArray(String var0, Scalar var1) {
      if (var1.getArray() == null) {
         throw new IllegalArgumentException(var0 + ": expected array. received " + SleepUtils.describe(var1));
      } else {
         return true;
      }
   }

   static {
      doReplace = File.separatorChar != '/';
   }
}
