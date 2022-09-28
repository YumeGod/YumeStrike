package sleep.runtime;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.KeyValuePair;
import sleep.bridges.SleepClosure;
import sleep.bridges.io.IOObject;
import sleep.engine.Block;
import sleep.engine.CallRequest;
import sleep.engine.ProxyInterface;
import sleep.engine.types.DoubleValue;
import sleep.engine.types.HashContainer;
import sleep.engine.types.IntValue;
import sleep.engine.types.ListContainer;
import sleep.engine.types.LongValue;
import sleep.engine.types.NullValue;
import sleep.engine.types.ObjectValue;
import sleep.engine.types.OrderedHashContainer;
import sleep.engine.types.StringValue;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Function;
import sleep.parser.Checkers;
import sleep.parser.Parser;

public class SleepUtils {
   public static final int SLEEP_RELEASE = 20090430;
   public static final String SLEEP_VERSION = "Sleep 2.1";
   protected static ScalarType nullScalar = new NullValue();

   public static void addKeyword(String var0) {
      Checkers.addKeyword(var0);
   }

   public static Block ParseCode(String var0) throws YourCodeSucksException {
      Parser var1 = new Parser("eval", var0);
      var1.parse();
      return var1.getRunnableBlock();
   }

   public static void removeScalar(Iterator var0, Scalar var1) {
      while(var0.hasNext()) {
         Scalar var2 = (Scalar)var0.next();
         if (var1.sameAs(var2)) {
            var0.remove();
         }
      }

   }

   private static Scalar runCode(CallRequest var0, ScriptInstance var1, Stack var2) {
      ScriptEnvironment var3 = var1.getScriptEnvironment();
      synchronized(var3.getScriptVariables()) {
         var3.pushSource(var1.getName());
         var3.CreateFrame();
         var3.CreateFrame(var2);
         var0.CallFunction();
         Scalar var5 = var3.getCurrentFrame().isEmpty() ? getEmptyScalar() : (Scalar)var3.getCurrentFrame().pop();
         var3.KillFrame();
         var3.popSource();
         var3.resetEnvironment();
         return var5;
      }
   }

   public static Scalar runCode(Block var0, ScriptEnvironment var1) {
      CallRequest.InlineCallRequest var2;
      if (var1.getScriptVariables().getLocalVariables() == null) {
         var1.getScriptVariables().pushLocalLevel();
         var2 = new CallRequest.InlineCallRequest(var1, Integer.MIN_VALUE, "eval", var0);
         Scalar var3 = runCode((CallRequest)var2, (ScriptInstance)var1.getScriptInstance(), (Stack)null);
         var1.getScriptVariables().popLocalLevel();
         return var3;
      } else {
         var2 = new CallRequest.InlineCallRequest(var1, Integer.MIN_VALUE, "eval", var0);
         return runCode((CallRequest)var2, (ScriptInstance)var1.getScriptInstance(), (Stack)null);
      }
   }

   public static Scalar runCode(SleepClosure var0, String var1, ScriptInstance var2, Stack var3) {
      if (var2 == null) {
         var2 = var0.getOwner();
      }

      CallRequest.ClosureCallRequest var4 = new CallRequest.ClosureCallRequest(var2.getScriptEnvironment(), Integer.MIN_VALUE, getScalar((Object)var0), var1);
      return runCode((CallRequest)var4, (ScriptInstance)var2, (Stack)var3);
   }

   public static Scalar runCode(Function var0, String var1, ScriptInstance var2, Stack var3) {
      CallRequest.FunctionCallRequest var4 = new CallRequest.FunctionCallRequest(var2.getScriptEnvironment(), Integer.MIN_VALUE, var1, var0);
      return runCode((CallRequest)var4, (ScriptInstance)var2, (Stack)var3);
   }

   public static Scalar runCode(ScriptInstance var0, Block var1, HashMap var2) {
      CallRequest.InlineCallRequest var3 = new CallRequest.InlineCallRequest(var0.getScriptEnvironment(), Integer.MIN_VALUE, "eval", var1);
      return runCode((CallRequest)var3, (ScriptInstance)var0, (Stack)getArgumentStack(var2));
   }

   public static Scalar runCode(ScriptInstance var0, Block var1) {
      return runCode((ScriptInstance)var0, (Block)var1, (HashMap)null);
   }

   public static Scalar getArrayWrapper(Collection var0) {
      Scalar var1 = new Scalar();
      var1.setValue((ScalarArray)(new CollectionWrapper(var0)));
      return var1;
   }

   public static Scalar getHashWrapper(Map var0) {
      Scalar var1 = new Scalar();
      var1.setValue((ScalarHash)(new MapWrapper(var0)));
      return var1;
   }

   public static Scalar getHashScalar(ScalarHash var0) {
      Scalar var1 = new Scalar();
      var1.setValue(var0);
      return var1;
   }

   public static Scalar getArrayScalar(ScalarArray var0) {
      Scalar var1 = new Scalar();
      var1.setValue(var0);
      return var1;
   }

   public static Scalar getArrayScalar() {
      Scalar var0 = new Scalar();
      var0.setValue((ScalarArray)(new ListContainer()));
      return var0;
   }

   public static Map getMapFromHash(Scalar var0) {
      return getMapFromHash(var0.getHash());
   }

   public static Map getMapFromHash(ScalarHash var0) {
      HashMap var1 = new HashMap();
      if (var0 != null) {
         Iterator var2 = var0.keys().scalarIterator();

         while(var2.hasNext()) {
            Scalar var3 = (Scalar)var2.next();
            Scalar var4 = var0.getAt(var3);
            if (var4.getHash() != null) {
               var1.put(var3.toString(), getMapFromHash(var4.getHash()));
            } else if (var4.getArray() != null) {
               var1.put(var3.toString(), getListFromArray(var4.getArray()));
            } else {
               var1.put(var3.toString(), var4.objectValue());
            }
         }
      }

      return var1;
   }

   public static Stack getArgumentStack(Map var0) {
      Stack var1 = new Stack();
      if (var0 != null) {
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.push(getScalar((Object)(new KeyValuePair(getScalar(var3.getKey().toString()), (Scalar)var3.getValue()))));
         }
      }

      return var1;
   }

   public static Iterator getIterator(Scalar var0, ScriptInstance var1) {
      if (var0.getArray() != null) {
         return var0.getArray().scalarIterator();
      } else if (isFunctionScalar(var0)) {
         return getFunctionFromScalar(var0).scalarIterator();
      } else if (ProxyIterator.isIterator(var0)) {
         return new ProxyIterator((Iterator)var0.objectValue(), true);
      } else {
         throw new IllegalArgumentException("expected iterator (@array or &closure)--received: " + describe(var0));
      }
   }

   public static List getListFromArray(Scalar var0) {
      return getListFromArray(var0.getArray());
   }

   public static List getListFromArray(ScalarArray var0) {
      LinkedList var1 = new LinkedList();
      if (var0 != null) {
         Iterator var2 = var0.scalarIterator();

         while(var2.hasNext()) {
            Scalar var3 = (Scalar)var2.next();
            if (var3.getHash() != null) {
               var1.add(getMapFromHash(var3.getHash()));
            } else if (var3.getArray() != null) {
               var1.add(getListFromArray(var3.getArray()));
            } else {
               var1.add(var3.objectValue());
            }
         }
      }

      return var1;
   }

   public static Scalar getEmptyScalar() {
      Scalar var0 = new Scalar();
      var0.setValue(nullScalar);
      return var0;
   }

   public static boolean isEmptyScalar(Scalar var0) {
      return var0 == null || var0.getActualValue() == nullScalar;
   }

   public static boolean isFunctionScalar(Scalar var0) {
      return var0.objectValue() != null && var0.objectValue() instanceof SleepClosure;
   }

   public static SleepClosure getFunctionFromScalar(Scalar var0) {
      return var0.objectValue() != null && var0.objectValue() instanceof SleepClosure ? (SleepClosure)var0.objectValue() : null;
   }

   public static SleepClosure getFunctionFromScalar(Scalar var0, ScriptInstance var1) {
      return var0.objectValue() != null && var0.objectValue() instanceof SleepClosure ? (SleepClosure)var0.objectValue() : (SleepClosure)var1.getScriptEnvironment().getFunction(var0.toString());
   }

   public static Scalar getIOHandleScalar(InputStream var0, OutputStream var1) {
      return getScalar((Object)getIOHandle(var0, var1));
   }

   public static IOObject getIOHandle(InputStream var0, OutputStream var1) {
      IOObject var2 = new IOObject();
      var2.openRead(var0);
      var2.openWrite(var1);
      return var2;
   }

   public static Object newInstance(Class var0, SleepClosure var1, ScriptInstance var2) {
      return ProxyInterface.BuildInterface((Class)var0, (Function)var1, var2 != null ? var2 : var1.getOwner());
   }

   public static Object newInstance(Class var0, Block var1, ScriptInstance var2) {
      return ProxyInterface.BuildInterface((Class)var0, (Function)(new SleepClosure(var2, var1)), var2);
   }

   public static String describe(Stack var0) {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Scalar var3 = (Scalar)var2.next();
         var1.insert(0, describe(var3));
         if (var2.hasNext()) {
            var1.insert(0, ", ");
         }
      }

      return var1.toString();
   }

   private static String describeEntries(List var0, Scalar var1) {
      StringBuffer var2;
      Iterator var7;
      if (var1.getArray() != null) {
         if (var0.contains(var1.getArray())) {
            return "@" + var0.indexOf(var1.getArray());
         } else {
            var0.add(var1.getArray());
            var2 = new StringBuffer("@(");
            var7 = var1.getArray().scalarIterator();

            while(var7.hasNext()) {
               Scalar var9 = (Scalar)var7.next();
               var2.append(describeEntries(var0, var9));
               if (var7.hasNext()) {
                  var2.append(", ");
               }
            }

            var2.append(")");
            return var2.toString();
         }
      } else if (var1.getHash() != null) {
         if (var0.contains(var1.getHash())) {
            return "%" + var0.indexOf(var1.getHash());
         } else {
            var0.add(var1.getHash());
            var2 = new StringBuffer("%(");
            var7 = var1.getHash().getData().entrySet().iterator();

            while(var7.hasNext()) {
               Map.Entry var8 = (Map.Entry)var7.next();
               Scalar var5 = (Scalar)var8.getValue();
               if (!isEmptyScalar((Scalar)var8.getValue())) {
                  if (var2.length() > 2) {
                     var2.append(", ");
                  }

                  var2.append(var8.getKey());
                  var2.append(" => ");
                  var2.append(describeEntries(var0, var5));
               }
            }

            var2.append(")");
            return var2.toString();
         }
      } else if (var1.getActualValue().getType() == NullValue.class) {
         return "$null";
      } else if (var1.getActualValue().getType() == StringValue.class) {
         return "'" + var1.toString() + "'";
      } else if (isFunctionScalar(var1)) {
         return var1.toString();
      } else if (var1.objectValue() instanceof KeyValuePair) {
         KeyValuePair var6 = (KeyValuePair)var1.objectValue();
         return var6.getKey().toString() + " => " + describe(var6.getValue());
      } else if (var1.getActualValue().getType() == ObjectValue.class) {
         if (Proxy.isProxyClass(var1.objectValue().getClass())) {
            var2 = new StringBuffer();
            var2.append("[");
            var2.append(Proxy.getInvocationHandler(var1.objectValue()).toString());
            var2.append(" as ");
            Class[] var3 = var1.objectValue().getClass().getInterfaces();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var4 > 0) {
                  var2.append(", ");
               }

               var2.append(var3[var4].getName());
            }

            var2.append("]");
            return var2.toString();
         } else {
            return var1.toString();
         }
      } else {
         return var1.getActualValue().getType() == LongValue.class ? var1.toString() + "L" : var1.toString();
      }
   }

   public static String describe(Scalar var0) {
      return describeEntries(new LinkedList(), var0);
   }

   public static Scalar getHashScalar() {
      Scalar var0 = new Scalar();
      var0.setValue((ScalarHash)(new HashContainer()));
      return var0;
   }

   public static Scalar getOrderedHashScalar() {
      Scalar var0 = new Scalar();
      var0.setValue((ScalarHash)(new OrderedHashContainer(11, 0.75F, false)));
      return var0;
   }

   public static Scalar getAccessOrderedHashScalar() {
      Scalar var0 = new Scalar();
      var0.setValue((ScalarHash)(new OrderedHashContainer(11, 0.75F, true)));
      return var0;
   }

   public static Scalar getScalar(int var0) {
      Scalar var1 = new Scalar();
      var1.setValue((ScalarType)(new IntValue(var0)));
      return var1;
   }

   public static Scalar getScalar(short var0) {
      Scalar var1 = new Scalar();
      var1.setValue((ScalarType)(new IntValue(var0)));
      return var1;
   }

   public static Scalar getScalar(float var0) {
      Scalar var1 = new Scalar();
      var1.setValue((ScalarType)(new DoubleValue((double)var0)));
      return var1;
   }

   public static Scalar getScalar(double var0) {
      Scalar var2 = new Scalar();
      var2.setValue((ScalarType)(new DoubleValue(var0)));
      return var2;
   }

   public static Scalar getScalar(Scalar var0) {
      Scalar var1 = new Scalar();
      var1.setValue(var0);
      return var1;
   }

   public static Scalar getScalar(long var0) {
      Scalar var2 = new Scalar();
      var2.setValue((ScalarType)(new LongValue(var0)));
      return var2;
   }

   public static Scalar getScalar(byte[] var0) {
      return getScalar(var0, var0.length);
   }

   public static Scalar getScalar(byte[] var0, int var1) {
      Scalar var2 = new Scalar();
      StringBuffer var3 = new StringBuffer(var1);

      for(int var4 = 0; var4 < var1; ++var4) {
         char var5 = (char)(var0[var4] & 255);
         var3.append(var5);
      }

      var2.setValue((ScalarType)(new StringValue(var3.toString())));
      return var2;
   }

   public static Scalar getScalar(String var0) {
      if (var0 == null) {
         return getEmptyScalar();
      } else {
         Scalar var1 = new Scalar();
         var1.setValue((ScalarType)(new StringValue(var0)));
         return var1;
      }
   }

   public static Scalar getScalar(Object var0) {
      if (var0 == null) {
         return getEmptyScalar();
      } else {
         Scalar var1 = new Scalar();
         var1.setValue((ScalarType)(new ObjectValue(var0)));
         return var1;
      }
   }

   public static Scalar getScalar(boolean var0) {
      return var0 ? getScalar((int)1) : getEmptyScalar();
   }

   public static boolean isTrueScalar(Scalar var0) {
      return var0.getArray() != null || var0.getHash() != null || var0.getActualValue().toString().length() != 0 && !"0".equals(var0.getActualValue().toString());
   }
}
