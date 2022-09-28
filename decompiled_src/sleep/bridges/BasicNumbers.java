package sleep.bridges;

import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import sleep.engine.types.DoubleValue;
import sleep.engine.types.IntValue;
import sleep.engine.types.LongValue;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Operator;
import sleep.interfaces.Predicate;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarType;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;
import sleep.taint.TaintUtils;

public class BasicNumbers implements Predicate, Operator, Loadable, Function {
   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      Object var3 = TaintUtils.Sanitizer(this);
      String[] var4 = new String[]{"&abs", "&acos", "&asin", "&atan", "&atan2", "&ceil", "&cos", "&log", "&round", "&sin", "&sqrt", "&tan", "&radians", "&degrees", "&exp", "&floor", "&sum"};

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var2.put(var4[var5], var3);
      }

      var2.put("&double", var3);
      var2.put("&int", var3);
      var2.put("&uint", var3);
      var2.put("&long", var3);
      var2.put("&parseNumber", var3);
      var2.put("&formatNumber", var3);
      var2.put("+", var3);
      var2.put("-", var3);
      var2.put("/", var3);
      var2.put("*", var3);
      var2.put("**", var3);
      var2.put("% ", var3);
      var2.put("<<", var3);
      var2.put(">>", var3);
      var2.put("&", var3);
      var2.put("|", var3);
      var2.put("^", var3);
      var2.put("&not", var3);
      var2.put("==", this);
      var2.put("!=", this);
      var2.put("<=", this);
      var2.put(">=", this);
      var2.put("<", this);
      var2.put(">", this);
      var2.put("is", this);
      var2.put("&rand", var3);
      var2.put("&srand", var3);
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&abs")) {
         return SleepUtils.getScalar(Math.abs(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&acos")) {
         return SleepUtils.getScalar(Math.acos(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&asin")) {
         return SleepUtils.getScalar(Math.asin(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&atan")) {
         return SleepUtils.getScalar(Math.atan(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&atan2")) {
         return SleepUtils.getScalar(Math.atan2(BridgeUtilities.getDouble(var3, 0.0), BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&ceil")) {
         return SleepUtils.getScalar(Math.ceil(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&floor")) {
         return SleepUtils.getScalar(Math.floor(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&cos")) {
         return SleepUtils.getScalar(Math.cos(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&log") && var3.size() == 1) {
         return SleepUtils.getScalar(Math.log(BridgeUtilities.getDouble(var3, 0.0)));
      } else if (var1.equals("&log") && var3.size() == 2) {
         return SleepUtils.getScalar(Math.log(BridgeUtilities.getDouble(var3, 0.0)) / Math.log(BridgeUtilities.getDouble(var3, 0.0)));
      } else {
         double var6;
         if (var1.equals("&round")) {
            if (var3.size() == 1) {
               return SleepUtils.getScalar(Math.round(BridgeUtilities.getDouble(var3, 0.0)));
            } else {
               double var21 = BridgeUtilities.getDouble(var3, 0.0);
               var6 = Math.pow(10.0, (double)BridgeUtilities.getInt(var3, 0));
               var21 = (double)Math.round(var21 * var6);
               var21 /= var6;
               return SleepUtils.getScalar(var21);
            }
         } else if (var1.equals("&sin")) {
            return SleepUtils.getScalar(Math.sin(BridgeUtilities.getDouble(var3, 0.0)));
         } else if (var1.equals("&sqrt")) {
            return SleepUtils.getScalar(Math.sqrt(BridgeUtilities.getDouble(var3, 0.0)));
         } else if (var1.equals("&tan")) {
            return SleepUtils.getScalar(Math.tan(BridgeUtilities.getDouble(var3, 0.0)));
         } else if (var1.equals("&radians")) {
            return SleepUtils.getScalar(Math.toRadians(BridgeUtilities.getDouble(var3, 0.0)));
         } else if (var1.equals("&degrees")) {
            return SleepUtils.getScalar(Math.toDegrees(BridgeUtilities.getDouble(var3, 0.0)));
         } else if (var1.equals("&exp")) {
            return SleepUtils.getScalar(Math.exp(BridgeUtilities.getDouble(var3, 0.0)));
         } else if (!var1.equals("&sum")) {
            if (var1.equals("&not")) {
               ScalarType var19 = ((Scalar)var3.pop()).getActualValue();
               return var19.getType() == IntValue.class ? SleepUtils.getScalar(~var19.intValue()) : SleepUtils.getScalar(~var19.longValue());
            } else {
               Scalar var16;
               if (var1.equals("&long")) {
                  var16 = BridgeUtilities.getScalar(var3);
                  return SleepUtils.getScalar(var16.longValue());
               } else if (var1.equals("&double")) {
                  var16 = BridgeUtilities.getScalar(var3);
                  return SleepUtils.getScalar(var16.doubleValue());
               } else if (var1.equals("&int")) {
                  var16 = BridgeUtilities.getScalar(var3);
                  return SleepUtils.getScalar(var16.intValue());
               } else if (var1.equals("&uint")) {
                  int var15 = BridgeUtilities.getInt(var3, 0);
                  long var23 = 4294967295L & (long)var15;
                  return SleepUtils.getScalar(var23);
               } else {
                  String var14;
                  int var18;
                  if (var1.equals("&parseNumber")) {
                     var14 = BridgeUtilities.getString(var3, "0");
                     var18 = BridgeUtilities.getInt(var3, 10);
                     BigInteger var24 = new BigInteger(var14, var18);
                     return SleepUtils.getScalar(var24.longValue());
                  } else {
                     int var20;
                     if (var1.equals("&formatNumber")) {
                        var14 = BridgeUtilities.getString(var3, "0");
                        var18 = 10;
                        boolean var22 = true;
                        if (var3.size() == 2) {
                           var18 = BridgeUtilities.getInt(var3, 10);
                        }

                        var20 = BridgeUtilities.getInt(var3, 10);
                        BigInteger var7 = new BigInteger(var14, var18);
                        return SleepUtils.getScalar(var7.toString(var20));
                     } else {
                        if (var1.equals("&srand")) {
                           long var12 = BridgeUtilities.getLong(var3);
                           var2.getMetadata().put("__RANDOM__", new Random(var12));
                        } else if (var1.equals("&rand")) {
                           if (var2.getMetadata().get("__RANDOM__") == null) {
                              var2.getMetadata().put("__RANDOM__", new Random());
                           }

                           Random var13 = (Random)var2.getMetadata().get("__RANDOM__");
                           if (!var3.isEmpty()) {
                              Scalar var17 = (Scalar)var3.pop();
                              if (var17.getArray() != null) {
                                 var20 = var13.nextInt(var17.getArray().size());
                                 return var17.getArray().getAt(var20);
                              }

                              return SleepUtils.getScalar(var13.nextInt(var17.intValue()));
                           }

                           return SleepUtils.getScalar(var13.nextDouble());
                        }

                        return SleepUtils.getEmptyScalar();
                     }
                  }
               }
            }
         } else {
            Iterator var4 = BridgeUtilities.getIterator(var3, var2);
            LinkedList var5 = null;
            if (var3.size() >= 1) {
               var5 = new LinkedList();

               while(!var3.isEmpty()) {
                  var5.add(BridgeUtilities.getIterator(var3, var2));
               }
            }

            var6 = 0.0;
            double var8;
            if (var5 == null) {
               while(var4.hasNext()) {
                  var6 += ((Scalar)var4.next()).doubleValue();
               }
            } else {
               for(; var4.hasNext(); var6 += var8) {
                  var8 = ((Scalar)var4.next()).doubleValue();

                  Iterator var11;
                  for(Iterator var10 = var5.iterator(); var10.hasNext(); var8 *= ((Scalar)var11.next()).doubleValue()) {
                     var11 = (Iterator)var10.next();
                     if (!var11.hasNext()) {
                        var8 = 0.0;
                        break;
                     }
                  }
               }
            }

            return SleepUtils.getScalar(var6);
         }
      }
   }

   public boolean decide(String var1, ScriptInstance var2, Stack var3) {
      Stack var4 = var2.getScriptEnvironment().getEnvironmentStack();
      Scalar var5 = (Scalar)var3.pop();
      Scalar var6 = (Scalar)var3.pop();
      if (var1.equals("is")) {
         return var6.objectValue() == var5.objectValue();
      } else {
         ScalarType var7 = var5.getActualValue();
         ScalarType var8 = var6.getActualValue();
         if (var8.getType() != DoubleValue.class && var7.getType() != DoubleValue.class) {
            if (var8.getType() != LongValue.class && var7.getType() != LongValue.class) {
               int var14 = var8.intValue();
               int var10 = var7.intValue();
               if (var1.equals("==")) {
                  return var14 == var10;
               }

               if (var1.equals("!=")) {
                  return var14 != var10;
               }

               if (var1.equals("<=")) {
                  return var14 <= var10;
               }

               if (var1.equals(">=")) {
                  return var14 >= var10;
               }

               if (var1.equals("<")) {
                  return var14 < var10;
               }

               if (var1.equals(">")) {
                  return var14 > var10;
               }
            } else {
               long var13 = var8.longValue();
               long var15 = var7.longValue();
               if (var1.equals("==")) {
                  return var13 == var15;
               }

               if (var1.equals("!=")) {
                  return var13 != var15;
               }

               if (var1.equals("<=")) {
                  return var13 <= var15;
               }

               if (var1.equals(">=")) {
                  return var13 >= var15;
               }

               if (var1.equals("<")) {
                  return var13 < var15;
               }

               if (var1.equals(">")) {
                  return var13 > var15;
               }
            }
         } else {
            double var9 = var8.doubleValue();
            double var11 = var7.doubleValue();
            if (var1.equals("==")) {
               return var9 == var11;
            }

            if (var1.equals("!=")) {
               return var9 != var11;
            }

            if (var1.equals("<=")) {
               return var9 <= var11;
            }

            if (var1.equals(">=")) {
               return var9 >= var11;
            }

            if (var1.equals("<")) {
               return var9 < var11;
            }

            if (var1.equals(">")) {
               return var9 > var11;
            }
         }

         return false;
      }
   }

   public Scalar operate(String var1, ScriptInstance var2, Stack var3) {
      ScalarType var4 = ((Scalar)var3.pop()).getActualValue();
      ScalarType var5 = ((Scalar)var3.pop()).getActualValue();
      if ((var5.getType() == DoubleValue.class || var4.getType() == DoubleValue.class) && !var1.equals(">>") && !var1.equals("<<") && !var1.equals("&") && !var1.equals("|") && !var1.equals("^")) {
         double var11 = var4.doubleValue();
         double var12 = var5.doubleValue();
         if (var1.equals("+")) {
            return SleepUtils.getScalar(var11 + var12);
         }

         if (var1.equals("-")) {
            return SleepUtils.getScalar(var11 - var12);
         }

         if (var1.equals("*")) {
            return SleepUtils.getScalar(var11 * var12);
         }

         if (var1.equals("/")) {
            return SleepUtils.getScalar(var11 / var12);
         }

         if (var1.equals("% ")) {
            return SleepUtils.getScalar(var11 % var12);
         }

         if (var1.equals("**")) {
            return SleepUtils.getScalar(Math.pow(var11, var12));
         }
      } else if (var5.getType() != LongValue.class && var4.getType() != LongValue.class) {
         int var10 = var4.intValue();
         int var7 = var5.intValue();
         if (var1.equals("+")) {
            return SleepUtils.getScalar(var10 + var7);
         }

         if (var1.equals("-")) {
            return SleepUtils.getScalar(var10 - var7);
         }

         if (var1.equals("*")) {
            return SleepUtils.getScalar(var10 * var7);
         }

         if (var1.equals("/")) {
            return SleepUtils.getScalar(var10 / var7);
         }

         if (var1.equals("% ")) {
            return SleepUtils.getScalar(var10 % var7);
         }

         if (var1.equals("**")) {
            return SleepUtils.getScalar(Math.pow((double)var10, (double)var7));
         }

         if (var1.equals(">>")) {
            return SleepUtils.getScalar(var10 >> var7);
         }

         if (var1.equals("<<")) {
            return SleepUtils.getScalar(var10 << var7);
         }

         if (var1.equals("&")) {
            return SleepUtils.getScalar(var10 & var7);
         }

         if (var1.equals("|")) {
            return SleepUtils.getScalar(var10 | var7);
         }

         if (var1.equals("^")) {
            return SleepUtils.getScalar(var10 ^ var7);
         }
      } else {
         long var6 = var4.longValue();
         long var8 = var5.longValue();
         if (var1.equals("+")) {
            return SleepUtils.getScalar(var6 + var8);
         }

         if (var1.equals("-")) {
            return SleepUtils.getScalar(var6 - var8);
         }

         if (var1.equals("*")) {
            return SleepUtils.getScalar(var6 * var8);
         }

         if (var1.equals("/")) {
            return SleepUtils.getScalar(var6 / var8);
         }

         if (var1.equals("% ")) {
            return SleepUtils.getScalar(var6 % var8);
         }

         if (var1.equals("**")) {
            return SleepUtils.getScalar(Math.pow((double)var6, (double)var8));
         }

         if (var1.equals(">>")) {
            return SleepUtils.getScalar(var6 >> (int)var8);
         }

         if (var1.equals("<<")) {
            return SleepUtils.getScalar(var6 << (int)var8);
         }

         if (var1.equals("&")) {
            return SleepUtils.getScalar(var6 & var8);
         }

         if (var1.equals("|")) {
            return SleepUtils.getScalar(var6 | var8);
         }

         if (var1.equals("^")) {
            return SleepUtils.getScalar(var6 ^ var8);
         }
      }

      return SleepUtils.getEmptyScalar();
   }
}
