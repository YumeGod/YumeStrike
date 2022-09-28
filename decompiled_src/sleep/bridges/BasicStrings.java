package sleep.bridges;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Stack;
import sleep.engine.types.DoubleValue;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Operator;
import sleep.interfaces.Predicate;
import sleep.parser.ParserConfig;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScalarType;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class BasicStrings implements Loadable, Predicate {
   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      var2.put("&left", new func_left());
      var2.put("&right", new func_right());
      var2.put("&charAt", new func_charAt());
      var2.put("&byteAt", var2.get("&charAt"));
      var2.put("&uc", new func_uc());
      var2.put("&lc", new func_lc());
      func_substr var3 = new func_substr();
      var2.put("&substr", var3);
      var2.put("&mid", var3);
      var2.put("&indexOf", new func_indexOf());
      var2.put("&lindexOf", var2.get("&indexOf"));
      var2.put("&strlen", new func_strlen());
      var2.put("&strrep", new func_strrep());
      var2.put("&replaceAt", new func_replaceAt());
      var2.put("&tr", new func_tr());
      var2.put("&asc", new func_asc());
      var2.put("&chr", new func_chr());
      var2.put("&sort", new func_sort());
      func_sorters var4 = new func_sorters();
      var2.put("&sorta", var4);
      var2.put("&sortn", var4);
      var2.put("&sortd", var4);
      var2.put("eq", this);
      var2.put("ne", this);
      var2.put("lt", this);
      var2.put("gt", this);
      var2.put("-isletter", this);
      var2.put("-isnumber", this);
      var2.put("-isupper", this);
      var2.put("-islower", this);
      var2.put("isin", this);
      var2.put("iswm", new pred_iswm());
      var2.put(".", new oper_concat());
      var2.put("x", new oper_multiply());
      var2.put("cmp", new oper_compare());
      var2.put("<=>", new oper_spaceship());
   }

   public boolean decide(String var1, ScriptInstance var2, Stack var3) {
      String var4;
      if (var3.size() == 1) {
         var4 = BridgeUtilities.getString(var3, "");
         if (var1.equals("-isupper")) {
            return var4.toUpperCase().equals(var4);
         }

         if (var1.equals("-islower")) {
            return var4.toLowerCase().equals(var4);
         }

         int var5;
         if (var1.equals("-isletter")) {
            if (var4.length() <= 0) {
               return false;
            }

            for(var5 = 0; var5 < var4.length(); ++var5) {
               if (!Character.isLetter(var4.charAt(var5))) {
                  return false;
               }
            }

            return true;
         }

         if (var1.equals("-isnumber")) {
            if (var4.length() <= 0) {
               return false;
            }

            if (var4.indexOf(46) > -1 && var4.indexOf(46) != var4.lastIndexOf(46)) {
               return false;
            }

            for(var5 = 0; var5 < var4.length(); ++var5) {
               if (!Character.isDigit(var4.charAt(var5)) && (var4.charAt(var5) != '.' || var5 + 1 >= var4.length())) {
                  return false;
               }
            }

            return true;
         }
      } else {
         var4 = BridgeUtilities.getString(var3, "");
         String var6 = BridgeUtilities.getString(var3, "");
         if (var1.equals("eq")) {
            return var6.equals(var4);
         }

         if (var1.equals("ne")) {
            return !var6.equals(var4);
         }

         if (var1.equals("isin")) {
            return var4.indexOf(var6) > -1;
         }

         if (var1.equals("gt")) {
            return var6.compareTo(var4) > 0;
         }

         if (var1.equals("lt")) {
            return var6.compareTo(var4) < 0;
         }
      }

      return false;
   }

   private static final String substring(String var0, String var1, int var2, int var3) {
      int var4 = var1.length();
      int var5 = BridgeUtilities.normalize(var2, var4);
      int var6 = var3 < 0 ? var3 + var4 : var3;
      var6 = var6 <= var4 ? var6 : var4;
      if (var5 == var6) {
         return "";
      } else if (var5 > var6) {
         throw new IllegalArgumentException(var0 + ": illegal substring('" + var1 + "', " + var2 + " -> " + var5 + ", " + var3 + " -> " + var6 + ") indices");
      } else {
         return var1.substring(var5, var6);
      }
   }

   private static final char charAt(String var0, int var1) {
      return var0.charAt(BridgeUtilities.normalize(var1, var0.length()));
   }

   static {
      ParserConfig.addKeyword("x");
      ParserConfig.addKeyword("eq");
      ParserConfig.addKeyword("ne");
      ParserConfig.addKeyword("lt");
      ParserConfig.addKeyword("gt");
      ParserConfig.addKeyword("isin");
      ParserConfig.addKeyword("iswm");
      ParserConfig.addKeyword("cmp");
   }

   private static class oper_spaceship implements Operator {
      private oper_spaceship() {
      }

      public Scalar operate(String var1, ScriptInstance var2, Stack var3) {
         ScalarType var4 = BridgeUtilities.getScalar(var3).getActualValue();
         ScalarType var5 = BridgeUtilities.getScalar(var3).getActualValue();
         if (var4.getType() != DoubleValue.class && var5.getType() != DoubleValue.class) {
            if (var4.longValue() > var5.longValue()) {
               return SleepUtils.getScalar((int)1);
            }

            if (var4.longValue() < var5.longValue()) {
               return SleepUtils.getScalar((int)-1);
            }
         } else {
            if (var4.doubleValue() > var5.doubleValue()) {
               return SleepUtils.getScalar((int)1);
            }

            if (var4.doubleValue() < var5.doubleValue()) {
               return SleepUtils.getScalar((int)-1);
            }
         }

         return SleepUtils.getScalar((int)0);
      }

      // $FF: synthetic method
      oper_spaceship(Object var1) {
         this();
      }
   }

   private static class oper_compare implements Operator {
      private oper_compare() {
      }

      public Scalar operate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = (Scalar)((Scalar)var3.pop());
         Scalar var5 = (Scalar)((Scalar)var3.pop());
         return SleepUtils.getScalar(var4.toString().compareTo(var5.toString()));
      }

      // $FF: synthetic method
      oper_compare(Object var1) {
         this();
      }
   }

   private static class oper_multiply implements Operator {
      private oper_multiply() {
      }

      public Scalar operate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = (Scalar)((Scalar)var3.pop());
         Scalar var5 = (Scalar)((Scalar)var3.pop());
         String var6 = var4.toString();
         int var7 = var5.intValue();
         StringBuffer var8 = new StringBuffer();

         for(int var9 = 0; var9 < var7; ++var9) {
            var8.append(var6);
         }

         return SleepUtils.getScalar((Object)var8);
      }

      // $FF: synthetic method
      oper_multiply(Object var1) {
         this();
      }
   }

   private static class oper_concat implements Operator {
      private oper_concat() {
      }

      public Scalar operate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = (Scalar)((Scalar)var3.pop());
         Scalar var5 = (Scalar)((Scalar)var3.pop());
         return var1.equals(".") ? SleepUtils.getScalar(var4.toString() + var5.toString()) : null;
      }

      // $FF: synthetic method
      oper_concat(Object var1) {
         this();
      }
   }

   private static class CompareStrings implements Comparator {
      private CompareStrings() {
      }

      public int compare(Object var1, Object var2) {
         return var1.toString().compareTo(var2.toString());
      }

      // $FF: synthetic method
      CompareStrings(Object var1) {
         this();
      }
   }

   private static class CompareDoubles implements Comparator {
      private CompareDoubles() {
      }

      public int compare(Object var1, Object var2) {
         double var3 = ((Scalar)var1).doubleValue();
         double var5 = ((Scalar)var2).doubleValue();
         if (var3 == var5) {
            return 0;
         } else {
            return var3 < var5 ? -1 : 1;
         }
      }

      // $FF: synthetic method
      CompareDoubles(Object var1) {
         this();
      }
   }

   private static class CompareNumbers implements Comparator {
      private CompareNumbers() {
      }

      public int compare(Object var1, Object var2) {
         long var3 = ((Scalar)var1).longValue();
         long var5 = ((Scalar)var2).longValue();
         return (int)(var3 - var5);
      }

      // $FF: synthetic method
      CompareNumbers(Object var1) {
         this();
      }
   }

   private static class CompareFunction implements Comparator {
      protected SleepClosure func;
      protected ScriptInstance script;
      protected Stack locals;

      public CompareFunction(Function var1, ScriptInstance var2) {
         this.func = (SleepClosure)var1;
         this.script = var2;
         this.locals = new Stack();
      }

      public int compare(Object var1, Object var2) {
         this.locals.push(var2);
         this.locals.push(var1);
         Scalar var3 = this.func.callClosure("&sort", this.script, this.locals);
         return var3.intValue();
      }
   }

   private static class func_sorters implements Function {
      private func_sorters() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         ScalarArray var4 = BridgeUtilities.getWorkableArray(var3);
         if (var1.equals("&sorta")) {
            var4.sort(new CompareStrings());
         } else if (var1.equals("&sortn")) {
            var4.sort(new CompareNumbers());
         } else if (var1.equals("&sortd")) {
            var4.sort(new CompareDoubles());
         }

         return SleepUtils.getArrayScalar(var4);
      }

      // $FF: synthetic method
      func_sorters(Object var1) {
         this();
      }
   }

   private static class func_sort implements Function {
      private func_sort() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         if (var3.size() != 2) {
            throw new IllegalArgumentException("&sort requires a function to specify how to sort the data");
         } else {
            SleepClosure var4 = BridgeUtilities.getFunction(var3, var2);
            ScalarArray var5 = BridgeUtilities.getWorkableArray(var3);
            if (var4 == null) {
               return SleepUtils.getArrayScalar();
            } else {
               var5.sort(new CompareFunction(var4, var2));
               return SleepUtils.getArrayScalar(var5);
            }
         }
      }

      // $FF: synthetic method
      func_sort(Object var1) {
         this();
      }
   }

   private static class func_charAt implements Function {
      private func_charAt() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = var3.pop().toString();
         int var5 = BridgeUtilities.getInt(var3);
         return var1.equals("&charAt") ? SleepUtils.getScalar(BasicStrings.charAt(var4, var5) + "") : SleepUtils.getScalar((int)BasicStrings.charAt(var4, var5));
      }

      // $FF: synthetic method
      func_charAt(Object var1) {
         this();
      }
   }

   private static class func_indexOf implements Function {
      private func_indexOf() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = var3.pop().toString();
         String var5 = var3.pop().toString();
         int var6;
         int var7;
         if (var1.equals("&lindexOf")) {
            var7 = BridgeUtilities.normalize(BridgeUtilities.getInt(var3, var4.length()), var4.length());
            var6 = var4.lastIndexOf(var5, var7);
         } else {
            var7 = BridgeUtilities.normalize(BridgeUtilities.getInt(var3, 0), var4.length());
            var6 = var4.indexOf(var5, var7);
         }

         return var6 == -1 ? SleepUtils.getEmptyScalar() : SleepUtils.getScalar(var6);
      }

      // $FF: synthetic method
      func_indexOf(Object var1) {
         this();
      }
   }

   private static class func_substr implements Function {
      private func_substr() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = BridgeUtilities.getString(var3, "");
         int var5 = BridgeUtilities.getInt(var3);
         int var6;
         if (var1.equals("&mid")) {
            var6 = BridgeUtilities.getInt(var3, var4.length() - var5) + var5;
         } else {
            var6 = BridgeUtilities.getInt(var3, var4.length());
         }

         return SleepUtils.getScalar(BasicStrings.substring(var1, var4, var5, var6));
      }

      // $FF: synthetic method
      func_substr(Object var1) {
         this();
      }
   }

   private static class func_replaceAt implements Function {
      private func_replaceAt() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         StringBuffer var4 = new StringBuffer(BridgeUtilities.getString(var3, ""));
         String var5 = BridgeUtilities.getString(var3, "");
         int var6 = BridgeUtilities.normalize(BridgeUtilities.getInt(var3, 0), var4.length());
         int var7 = BridgeUtilities.getInt(var3, var5.length());
         var4.delete(var6, var6 + var7);
         var4.insert(var6, var5);
         return SleepUtils.getScalar(var4.toString());
      }

      // $FF: synthetic method
      func_replaceAt(Object var1) {
         this();
      }
   }

   private static class func_strrep implements Function {
      private func_strrep() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         StringBuffer var4 = new StringBuffer(BridgeUtilities.getString(var3, ""));

         while(true) {
            String var5;
            String var6;
            do {
               if (var3.isEmpty()) {
                  return SleepUtils.getScalar(var4.toString());
               }

               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
            } while(var5.length() == 0);

            int var7 = 0;
            int var8 = var5.length();

            for(int var9 = var6.length(); (var7 = var4.indexOf(var5, var7)) > -1; var7 += var6.length()) {
               var4.replace(var7, var7 + var8, var6);
            }
         }
      }

      // $FF: synthetic method
      func_strrep(Object var1) {
         this();
      }
   }

   private static class func_strlen implements Function {
      private func_strlen() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getScalar(var3.pop().toString().length());
      }

      // $FF: synthetic method
      func_strlen(Object var1) {
         this();
      }
   }

   private static class func_lc implements Function {
      private func_lc() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getScalar(var3.pop().toString().toLowerCase());
      }

      // $FF: synthetic method
      func_lc(Object var1) {
         this();
      }
   }

   private static class func_uc implements Function {
      private func_uc() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getScalar(var3.pop().toString().toUpperCase());
      }

      // $FF: synthetic method
      func_uc(Object var1) {
         this();
      }
   }

   private static class func_chr implements Function {
      private func_chr() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getScalar((char)BridgeUtilities.getInt(var3) + "");
      }

      // $FF: synthetic method
      func_chr(Object var1) {
         this();
      }
   }

   private static class func_asc implements Function {
      private func_asc() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getScalar((int)BridgeUtilities.getString(var3, "\u0000").charAt(0));
      }

      // $FF: synthetic method
      func_asc(Object var1) {
         this();
      }
   }

   private static class func_right implements Function {
      private func_right() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = var3.pop().toString();
         int var5 = ((Scalar)var3.pop()).intValue();
         return SleepUtils.getScalar(BasicStrings.substring(var1, var4, 0 - var5, var4.length()));
      }

      // $FF: synthetic method
      func_right(Object var1) {
         this();
      }
   }

   private static class func_tr implements Function {
      private func_tr() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = BridgeUtilities.getString(var3, "");
         String var5 = BridgeUtilities.getString(var3, "");
         String var6 = BridgeUtilities.getString(var3, "");
         String var7 = BridgeUtilities.getString(var3, "");
         int var8 = 0;
         if (var7.indexOf(99) > -1) {
            var8 |= 2;
         }

         if (var7.indexOf(100) > -1) {
            var8 |= 1;
         }

         if (var7.indexOf(115) > -1) {
            var8 |= 4;
         }

         Transliteration var9 = Transliteration.compile(var5, var6, var8);
         return SleepUtils.getScalar(var9.translate(var4));
      }

      // $FF: synthetic method
      func_tr(Object var1) {
         this();
      }
   }

   private static class func_left implements Function {
      private func_left() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = var3.pop().toString();
         int var5 = ((Scalar)var3.pop()).intValue();
         return SleepUtils.getScalar(BasicStrings.substring(var1, var4, 0, var5));
      }

      // $FF: synthetic method
      func_left(Object var1) {
         this();
      }
   }

   private static class pred_iswm implements Predicate {
      private pred_iswm() {
      }

      public boolean decide(String var1, ScriptInstance var2, Stack var3) {
         String var4 = var3.pop().toString();
         String var5 = var3.pop().toString();

         try {
            if ((var5.length() == 0 || var4.length() == 0) && var5.length() != var4.length()) {
               return false;
            } else {
               int var6 = 0;

               int var7;
               for(var7 = 0; var6 < var5.length(); ++var7) {
                  if (var5.charAt(var6) == '*') {
                     boolean var9 = var6 + 1 < var5.length() && var5.charAt(var6 + 1) == '*';

                     while(var5.charAt(var6) == '*') {
                        ++var6;
                        if (var6 == var5.length()) {
                           return true;
                        }
                     }

                     int var8;
                     for(var8 = var6; var8 < var5.length() && var5.charAt(var8) != '?' && var5.charAt(var8) != '\\' && var5.charAt(var8) != '*'; ++var8) {
                     }

                     if (var8 != var6) {
                        if (var9) {
                           var8 = var4.lastIndexOf(var5.substring(var6, var8));
                        } else {
                           var8 = var4.indexOf(var5.substring(var6, var8), var7);
                        }

                        if (var8 == -1 || var8 < var7) {
                           return false;
                        }

                        var7 = var8;
                     }

                     if (var5.charAt(var6) == '?') {
                        --var6;
                     }
                  } else {
                     if (var7 >= var4.length()) {
                        return false;
                     }

                     if (var5.charAt(var6) == '\\') {
                        ++var6;
                        if (var6 < var5.length() && var5.charAt(var6) != var4.charAt(var7)) {
                           return false;
                        }
                     } else if (var5.charAt(var6) != '?' && var5.charAt(var6) != var4.charAt(var7)) {
                        return false;
                     }
                  }

                  ++var6;
               }

               return var7 == var4.length();
            }
         } catch (Exception var10) {
            var10.printStackTrace();
            return false;
         }
      }

      // $FF: synthetic method
      pred_iswm(Object var1) {
         this();
      }
   }
}
