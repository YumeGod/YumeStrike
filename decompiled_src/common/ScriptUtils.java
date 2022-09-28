package common;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.engine.ObjectUtilities;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.SleepUtils;

public class ScriptUtils {
   public static Scalar toSleepArray(Object[] var0) {
      return SleepUtils.getArrayWrapper(CommonUtils.toList(var0));
   }

   public static String[] toStringArray(ScalarArray var0) {
      int var1 = 0;
      String[] var2 = new String[var0.size()];

      for(Iterator var3 = var0.scalarIterator(); var3.hasNext(); ++var1) {
         var2[var1] = var3.next() + "";
      }

      return var2;
   }

   public static Stack scalar(String var0) {
      Stack var1 = new Stack();
      var1.push(SleepUtils.getScalar(var0));
      return var1;
   }

   public static Scalar convertAll(Object var0) {
      Scalar var7;
      Iterator var9;
      if (var0 instanceof Collection) {
         var7 = SleepUtils.getArrayScalar();
         var9 = ((Collection)var0).iterator();

         while(var9.hasNext()) {
            var7.getArray().push(convertAll(var9.next()));
         }

         return var7;
      } else if (var0 instanceof Map) {
         var7 = SleepUtils.getHashScalar();
         var9 = ((Map)var0).entrySet().iterator();

         while(var9.hasNext()) {
            Map.Entry var11 = (Map.Entry)var9.next();
            Scalar var4 = SleepUtils.getScalar(var11.getKey() + "");
            Scalar var5 = var7.getHash().getAt(var4);
            var5.setValue(convertAll(var11.getValue()));
         }

         return var7;
      } else if (var0 instanceof BeaconEntry) {
         return convertAll(((BeaconEntry)var0).toMap());
      } else if (var0 instanceof Scriptable) {
         Scriptable var6 = (Scriptable)var0;
         Scalar var8 = SleepUtils.getArrayScalar();
         var8.getArray().push(SleepUtils.getScalar(var6.eventName()));
         Stack var10 = var6.eventArguments();

         while(!var10.isEmpty()) {
            var8.getArray().push((Scalar)var10.pop());
         }

         return var8;
      } else if (var0 instanceof ToScalar) {
         return ((ToScalar)var0).toScalar();
      } else if (!(var0 instanceof Object[])) {
         return ObjectUtilities.BuildScalar(true, var0);
      } else {
         Object[] var1 = (Object[])((Object[])var0);
         LinkedList var2 = new LinkedList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.add(var1[var3]);
         }

         return convertAll(var2);
      }
   }

   public static String[] ArrayOrString(Stack var0) {
      if (var0.isEmpty()) {
         return new String[0];
      } else {
         Scalar var1 = (Scalar)var0.peek();
         return var1.getArray() != null ? CommonUtils.toStringArray(BridgeUtilities.getArray(var0)) : new String[]{((Scalar)var0.pop()).stringValue()};
      }
   }

   public static Scalar IndexOrMap(Map var0, Stack var1) {
      if (var1.isEmpty()) {
         return SleepUtils.getHashWrapper(var0);
      } else {
         String var2 = BridgeUtilities.getString(var1, "");
         return CommonUtils.convertAll(var0.get(var2));
      }
   }

   public static Stack StringToArguments(String var0) {
      Stack var1 = new Stack();
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 == ' ') {
            if (var2.length() > 0) {
               var1.add(0, SleepUtils.getScalar(var2.toString()));
            }

            var2 = new StringBuffer();
         } else if (var4 == '"' && var2.length() == 0) {
            ++var3;

            while(var3 < var0.length() && var0.charAt(var3) != '"') {
               var2.append(var0.charAt(var3));
               ++var3;
            }

            var1.add(0, SleepUtils.getScalar(var2.toString()));
            var2 = new StringBuffer();
         } else {
            var2.append(var4);
         }
      }

      if (var2.length() > 0) {
         var1.add(0, SleepUtils.getScalar(var2.toString()));
      }

      var1.pop();
      return var1;
   }
}
