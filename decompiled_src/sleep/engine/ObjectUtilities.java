package sleep.engine;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.engine.types.DoubleValue;
import sleep.engine.types.IntValue;
import sleep.engine.types.LongValue;
import sleep.engine.types.ObjectValue;
import sleep.engine.types.StringValue;
import sleep.interfaces.Function;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScalarHash;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;
import sleep.runtime.WatchScalar;

public class ObjectUtilities {
   private static Class STRING_SCALAR;
   private static Class INT_SCALAR;
   private static Class DOUBLE_SCALAR;
   private static Class LONG_SCALAR;
   private static Class OBJECT_SCALAR;
   public static final int ARG_MATCH_YES = 3;
   public static final int ARG_MATCH_NO = 0;
   public static final int ARG_MATCH_MAYBE = 1;

   public static int isArgMatch(Class[] var0, Stack var1) {
      int var2 = 3;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         Scalar var4 = (Scalar)var1.get(var0.length - var3 - 1);
         var2 &= isArgMatch(var0[var3], var4);
         if (var2 == 0) {
            return 0;
         }
      }

      return var2;
   }

   private static Class normalizePrimitive(Class var0) {
      if (var0 == Integer.TYPE) {
         var0 = Integer.class;
      } else if (var0 == Double.TYPE) {
         var0 = Double.class;
      } else if (var0 == Long.TYPE) {
         var0 = Long.class;
      } else if (var0 == Float.TYPE) {
         var0 = Float.class;
      } else if (var0 == Boolean.TYPE) {
         var0 = Boolean.class;
      } else if (var0 == Byte.TYPE) {
         var0 = Byte.class;
      } else if (var0 == Character.TYPE) {
         var0 = Character.class;
      } else if (var0 == Short.TYPE) {
         var0 = Short.class;
      }

      return var0;
   }

   public static int isArgMatch(Class var0, Scalar var1) {
      if (SleepUtils.isEmptyScalar(var1)) {
         return 3;
      } else {
         Class var2;
         if (var1.getArray() != null) {
            if (!var0.isArray()) {
               if (var0 != List.class && var0 != Collection.class) {
                  if (var0 == ScalarArray.class) {
                     return 3;
                  } else {
                     return var0 == Object.class ? 1 : 0;
                  }
               } else {
                  return 3;
               }
            } else {
               for(var2 = var0.getComponentType(); var2.isArray(); var2 = var2.getComponentType()) {
               }

               Class var3 = getArrayType(var1, (Class)null);
               return var3 != null && var2.isAssignableFrom(var3) ? 3 : 0;
            }
         } else if (var1.getHash() != null) {
            if (var0 == Map.class) {
               return 3;
            } else if (var0 == ScalarHash.class) {
               return 3;
            } else {
               return var0 == Object.class ? 1 : 0;
            }
         } else if (var0.isPrimitive()) {
            var2 = var1.getActualValue().getType();
            if (var2 == INT_SCALAR && var0 == Integer.TYPE) {
               return 3;
            } else if (var2 == DOUBLE_SCALAR && var0 == Double.TYPE) {
               return 3;
            } else if (var2 == LONG_SCALAR && var0 == Long.TYPE) {
               return 3;
            } else if (var0 == Character.TYPE && var2 == STRING_SCALAR && var1.getActualValue().toString().length() == 1) {
               return 3;
            } else if (var2 == OBJECT_SCALAR) {
               var0 = normalizePrimitive(var0);
               return var1.objectValue().getClass() == var0 ? 3 : 0;
            } else {
               return var2 == STRING_SCALAR ? 0 : 1;
            }
         } else if (var0.isInterface()) {
            return !SleepUtils.isFunctionScalar(var1) && !var0.isInstance(var1.objectValue()) ? 0 : 3;
         } else if (var0 == String.class) {
            var2 = var1.getActualValue().getType();
            return var2 == STRING_SCALAR ? 3 : 1;
         } else if (var0 == Object.class) {
            return 1;
         } else if (var0.isInstance(var1.objectValue())) {
            var2 = var1.getActualValue().getType();
            return var2 == OBJECT_SCALAR ? 3 : 1;
         } else if (!var0.isArray()) {
            return 0;
         } else {
            var2 = var1.getActualValue().getType();
            return var2 != STRING_SCALAR || var0.getComponentType() != Character.TYPE && var0.getComponentType() != Byte.TYPE ? 0 : 1;
         }
      }
   }

   public static Method findMethod(Class var0, String var1, Stack var2) {
      int var3 = var2.size();
      Method var4 = null;
      Method[] var5 = var0.getMethods();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (var5[var6].getName().equals(var1) && var5[var6].getParameterTypes().length == var3) {
            if (var3 == 0) {
               return var5[var6];
            }

            int var7 = isArgMatch(var5[var6].getParameterTypes(), var2);
            if (var7 == 3) {
               return var5[var6];
            }

            if (var7 == 1) {
               var4 = var5[var6];
            }
         }
      }

      return var4;
   }

   public static Constructor findConstructor(Class var0, Stack var1) {
      int var2 = var1.size();
      Constructor var3 = null;
      Constructor[] var4 = var0.getConstructors();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].getParameterTypes().length == var2) {
            if (var2 == 0) {
               return var4[var5];
            }

            int var6 = isArgMatch(var4[var5].getParameterTypes(), var1);
            if (var6 == 3) {
               return var4[var5];
            }

            if (var6 == 1) {
               var3 = var4[var5];
            }
         }
      }

      return var3;
   }

   public static Class convertScalarDescriptionToClass(Scalar var0) {
      return var0.objectValue() instanceof Class ? (Class)var0.objectValue() : convertDescriptionToClass(var0.toString());
   }

   public static Class convertDescriptionToClass(String var0) {
      if (var0.length() != 1) {
         return null;
      } else {
         Class var1 = null;
         switch (var0.charAt(0)) {
            case '*':
               var1 = null;
               break;
            case 'b':
               var1 = Byte.TYPE;
               break;
            case 'c':
               var1 = Character.TYPE;
               break;
            case 'd':
               var1 = Double.TYPE;
               break;
            case 'f':
               var1 = Float.TYPE;
               break;
            case 'h':
               var1 = Short.TYPE;
               break;
            case 'i':
               var1 = Integer.TYPE;
               break;
            case 'l':
               var1 = Long.TYPE;
               break;
            case 'o':
               var1 = Object.class;
               break;
            case 'z':
               var1 = Boolean.TYPE;
         }

         return var1;
      }
   }

   public static Object buildArgument(Class var0, Scalar var1, ScriptInstance var2) {
      if (var0 == String.class) {
         return SleepUtils.isEmptyScalar(var1) ? null : var1.toString();
      } else if (var1.getArray() != null) {
         if (!var0.isArray()) {
            return var0 == ScalarArray.class ? var1.objectValue() : SleepUtils.getListFromArray(var1);
         } else {
            Class var3 = getArrayType(var1, var0.getComponentType());
            Object var4 = Array.newInstance(var3, var1.getArray().size());
            Iterator var5 = var1.getArray().scalarIterator();

            for(int var6 = 0; var5.hasNext(); ++var6) {
               Scalar var7 = (Scalar)var5.next();
               Object var8 = buildArgument(var3, var7, var2);
               if ((var8 != null || var3.isPrimitive()) && !var3.isInstance(var8) && !var3.isPrimitive()) {
                  if (var3.isArray()) {
                     throw new RuntimeException("incorrect dimensions for conversion to " + var0);
                  }

                  throw new RuntimeException(SleepUtils.describe(var7) + " at " + var6 + " is not compatible with " + var3.getName());
               }

               Array.set(var4, var6, var8);
            }

            return var4;
         }
      } else if (var1.getHash() != null) {
         return var0 == ScalarHash.class ? var1.objectValue() : SleepUtils.getMapFromHash(var1);
      } else {
         if (var0.isPrimitive()) {
            if (var0 == Boolean.TYPE) {
               return var1.intValue() != 0;
            }

            if (var0 == Byte.TYPE) {
               return new Byte((byte)var1.intValue());
            }

            if (var0 == Character.TYPE) {
               return new Character(var1.toString().charAt(0));
            }

            if (var0 == Double.TYPE) {
               return new Double(var1.doubleValue());
            }

            if (var0 == Float.TYPE) {
               return new Float((float)var1.doubleValue());
            }

            if (var0 == Integer.TYPE) {
               return new Integer(var1.intValue());
            }

            if (var0 == Short.TYPE) {
               return new Short((short)var1.intValue());
            }

            if (var0 == Long.TYPE) {
               return new Long(var1.longValue());
            }
         } else {
            if (SleepUtils.isEmptyScalar(var1)) {
               return null;
            }

            if (var0.isArray() && var1.getActualValue().getType() == StringValue.class) {
               if (var0.getComponentType() == Byte.TYPE || var0.getComponentType() == Byte.class) {
                  return BridgeUtilities.toByteArrayNoConversion(var1.toString());
               }

               if (var0.getComponentType() == Character.TYPE || var0.getComponentType() == Character.class) {
                  return var1.toString().toCharArray();
               }
            } else if (var0.isInterface() && SleepUtils.isFunctionScalar(var1)) {
               return ProxyInterface.BuildInterface((Class)var0, (Function)SleepUtils.getFunctionFromScalar(var1, var2), var2);
            }
         }

         return var1.objectValue();
      }
   }

   public static String buildArgumentErrorMessage(Class var0, String var1, Class[] var2, Object[] var3) {
      StringBuffer var4 = new StringBuffer(var1 + "(");

      for(int var5 = 0; var5 < var2.length; ++var5) {
         var4.append(var2[var5].getName());
         if (var5 + 1 < var2.length) {
            var4.append(", ");
         }
      }

      var4.append(")");
      StringBuffer var7 = new StringBuffer("(");

      for(int var6 = 0; var6 < var3.length; ++var6) {
         if (var3[var6] != null) {
            var7.append(var3[var6].getClass().getName());
         } else {
            var7.append("null");
         }

         if (var6 + 1 < var3.length) {
            var7.append(", ");
         }
      }

      var7.append(")");
      return "bad arguments " + var7.toString() + " for " + var4.toString() + " in " + var0;
   }

   public static Object[] buildArgumentArray(Class[] var0, Stack var1, ScriptInstance var2) {
      Object[] var3 = new Object[var0.length];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Scalar var5 = (Scalar)var1.pop();
         var3[var4] = buildArgument(var0[var4], var5, var2);
      }

      return var3;
   }

   public static Scalar BuildScalar(boolean var0, Object var1) {
      if (var1 == null) {
         return SleepUtils.getEmptyScalar();
      } else {
         Class var2 = var1.getClass();
         if (!var2.isArray()) {
            if (var0) {
               if (var2.isPrimitive()) {
                  var2 = normalizePrimitive(var2);
               }

               if (var2 == Boolean.class) {
                  return SleepUtils.getScalar((Boolean)var1 ? 1 : 0);
               }

               if (var2 == Byte.class) {
                  return SleepUtils.getScalar((int)(Byte)var1);
               }

               if (var2 == Character.class) {
                  return SleepUtils.getScalar(var1.toString());
               }

               if (var2 == Double.class) {
                  return SleepUtils.getScalar((Double)var1);
               }

               if (var2 == Float.class) {
                  return SleepUtils.getScalar((double)(Float)var1);
               }

               if (var2 == Integer.class) {
                  return SleepUtils.getScalar((Integer)var1);
               }

               if (var2 == Long.class) {
                  return SleepUtils.getScalar((Long)var1);
               }
            }

            if (var2 == String.class) {
               return SleepUtils.getScalar(var1.toString());
            } else {
               return var2 != Scalar.class && var2 != WatchScalar.class ? SleepUtils.getScalar(var1) : (Scalar)var1;
            }
         } else if (var2.getComponentType() != Byte.TYPE && var2.getComponentType() != Byte.class) {
            if (var2.getComponentType() != Character.TYPE && var2.getComponentType() != Character.class) {
               Scalar var3 = SleepUtils.getArrayScalar();

               for(int var4 = 0; var4 < Array.getLength(var1); ++var4) {
                  var3.getArray().push(BuildScalar(true, Array.get(var1, var4)));
               }

               return var3;
            } else {
               return SleepUtils.getScalar(new String((char[])((char[])var1)));
            }
         } else {
            return SleepUtils.getScalar((byte[])((byte[])var1));
         }
      }
   }

   public static Class getArrayType(Scalar var0, Class var1) {
      if (var0.getArray() != null && var0.getArray().size() > 0 && (var1 == null || var1 == Object.class)) {
         for(int var2 = 0; var2 < var0.getArray().size(); ++var2) {
            if (var0.getArray().getAt(var2).getArray() != null) {
               return getArrayType(var0.getArray().getAt(var2), var1);
            }

            Class var3 = var0.getArray().getAt(var2).getValue().getClass();
            Object var4 = var0.getArray().getAt(var2).objectValue();
            if (var3 == DOUBLE_SCALAR) {
               return Double.TYPE;
            }

            if (var3 == INT_SCALAR) {
               return Integer.TYPE;
            }

            if (var3 == LONG_SCALAR) {
               return Long.TYPE;
            }

            if (var4 != null) {
               return var4.getClass();
            }
         }
      }

      return var1;
   }

   public static void handleExceptionFromJava(Throwable var0, ScriptEnvironment var1, String var2, int var3) {
      if (var0 != null) {
         var1.flagError(var0);
         if (var1.isThrownValue() && var2 != null && var2.length() > 0) {
            var1.getScriptInstance().recordStackFrame(var2, var3);
         }
      }

   }

   static {
      STRING_SCALAR = StringValue.class;
      INT_SCALAR = IntValue.class;
      DOUBLE_SCALAR = DoubleValue.class;
      LONG_SCALAR = LongValue.class;
      OBJECT_SCALAR = ObjectValue.class;
   }
}
