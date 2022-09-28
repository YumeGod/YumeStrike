package org.apache.xalan.xsltc.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

public final class CallFunction {
   public static String className;
   public static String methodName;
   public static int nArgs;
   public static Class clazz;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Byte;

   public static String invokeMethod(String _className, String _methodName, Object[] _arguments) {
      className = _className;
      methodName = _methodName;
      int size = _arguments.length - 1;
      Object[] arguments = new Object[size];
      Object object = _arguments[0];
      clazz = null;

      try {
         clazz = ObjectFactory.findProviderClass(className, ObjectFactory.findClassLoader(), true);
         if (clazz == null) {
            throw new RuntimeException("Couldn't load the class");
         }
      } catch (ClassNotFoundException var20) {
         throw new RuntimeException("Couldn't load the class");
      }

      int i = 0;

      for(int j = 1; i < size; ++j) {
         arguments[i] = _arguments[j];
         ++i;
      }

      nArgs = size;
      Object obs;
      if (methodName != null) {
         Method method;
         if ((method = findMethods(arguments)) == null) {
            throw new RuntimeException("Method not found");
         } else {
            try {
               obs = method.invoke(object, arguments);
               return obs.toString();
            } catch (IllegalAccessException var13) {
               throw new RuntimeException("Error: Method is inaccessible");
            } catch (IllegalArgumentException var14) {
               throw new RuntimeException("Error: Number of actual and formal argument differ ");
            } catch (InvocationTargetException var15) {
               throw new RuntimeException("Error: underlying constructor throws an exception ");
            }
         }
      } else {
         Constructor constructor;
         if ((constructor = findConstructor(arguments)) == null) {
            throw new RuntimeException("Constructor not found");
         } else {
            try {
               obs = constructor.newInstance(arguments);
               return obs.toString();
            } catch (InvocationTargetException var16) {
               throw new RuntimeException("Error: constructor throws an exception ");
            } catch (IllegalAccessException var17) {
               throw new RuntimeException("Error: constructor is inaccessible");
            } catch (IllegalArgumentException var18) {
               throw new RuntimeException("Error: Number of actual and formal argument differ ");
            } catch (InstantiationException var19) {
               throw new RuntimeException("Error: Class that declares the underlying constructor represents an abstract class");
            }
         }
      }
   }

   private static Constructor findConstructor(Object[] arguments) {
      Vector constructors = null;
      Constructor[] c_constructors = clazz.getConstructors();

      int nConstructors;
      for(int i = 0; i < c_constructors.length; ++i) {
         nConstructors = c_constructors[i].getModifiers();
         if (Modifier.isPublic(nConstructors) && c_constructors[i].getParameterTypes().length == nArgs) {
            if (constructors == null) {
               constructors = new Vector();
            }

            constructors.addElement(c_constructors[i]);
         }
      }

      if (constructors == null) {
         throw new RuntimeException("CONSTRUCTOR_NOT_FOUND_ERR" + className + ":" + methodName);
      } else {
         nConstructors = constructors.size();
         boolean accept = false;

         for(int i = 0; i < nConstructors; ++i) {
            Constructor constructor = (Constructor)constructors.elementAt(i);
            Class[] paramTypes = constructor.getParameterTypes();

            for(int j = 0; j < nArgs; ++j) {
               Class argumentClass = arguments[j].getClass();
               if (argumentClass == paramTypes[j]) {
                  accept = true;
               } else {
                  if (!argumentClass.isAssignableFrom(paramTypes[j])) {
                     accept = false;
                     break;
                  }

                  accept = true;
               }
            }

            if (accept) {
               return constructor;
            }
         }

         return null;
      }
   }

   private static Method findMethods(Object[] arguments) {
      Vector methods = null;
      Method[] m_methods = clazz.getMethods();

      int nMethods;
      for(int i = 0; i < m_methods.length; ++i) {
         nMethods = m_methods[i].getModifiers();
         if (Modifier.isPublic(nMethods) && m_methods[i].getName().equals(methodName) && m_methods[i].getParameterTypes().length == nArgs) {
            if (methods == null) {
               methods = new Vector();
            }

            methods.addElement(m_methods[i]);
         }
      }

      if (methods == null) {
         throw new RuntimeException("METHOD_NOT_FOUND_ERR" + className + ":" + methodName);
      } else {
         nMethods = methods.size();
         boolean accept = false;

         for(int i = 0; i < nMethods; ++i) {
            Method method = (Method)methods.elementAt(i);
            Class[] paramTypes = method.getParameterTypes();

            for(int j = 0; j < nArgs; ++j) {
               Class argumentClass = arguments[j].getClass();
               if (argumentClass == paramTypes[j]) {
                  accept = true;
               } else if (argumentClass.isAssignableFrom(paramTypes[j])) {
                  accept = true;
               } else {
                  if (!paramTypes[j].isPrimitive()) {
                     accept = false;
                     break;
                  }

                  arguments[j] = isPrimitive(paramTypes[j], arguments[j]);
                  accept = true;
               }
            }

            if (accept) {
               return method;
            }
         }

         return null;
      }
   }

   public static Object isPrimitive(Class paramType, Object argument) {
      if (argument.getClass() == (class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer)) {
         return typeCast(paramType, (Integer)argument);
      } else if (argument.getClass() == (class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float)) {
         return typeCast(paramType, (Float)argument);
      } else if (argument.getClass() == (class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double)) {
         return typeCast(paramType, (Double)argument);
      } else if (argument.getClass() == (class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long)) {
         return typeCast(paramType, (Long)argument);
      } else if (argument.getClass() == (class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean)) {
         return (Boolean)argument;
      } else {
         return argument.getClass() == (class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte) ? (Byte)argument : null;
      }
   }

   static Object typeCast(Class paramType, Double object) {
      if (paramType == Long.TYPE) {
         return new Long(object.longValue());
      } else if (paramType == Integer.TYPE) {
         return new Integer(object.intValue());
      } else if (paramType == Float.TYPE) {
         return new Float(object.floatValue());
      } else if (paramType == Short.TYPE) {
         return new Short(object.shortValue());
      } else {
         return paramType == Byte.TYPE ? new Byte(object.byteValue()) : object;
      }
   }

   static Object typeCast(Class paramType, Long object) {
      if (paramType == Integer.TYPE) {
         return new Integer(object.intValue());
      } else if (paramType == Float.TYPE) {
         return new Float(object.floatValue());
      } else if (paramType == Short.TYPE) {
         return new Short(object.shortValue());
      } else {
         return paramType == Byte.TYPE ? new Byte(object.byteValue()) : object;
      }
   }

   static Object typeCast(Class paramType, Integer object) {
      if (paramType == Double.TYPE) {
         return new Double(object.doubleValue());
      } else if (paramType == Float.TYPE) {
         return new Float(object.floatValue());
      } else if (paramType == Short.TYPE) {
         return new Short(object.shortValue());
      } else {
         return paramType == Byte.TYPE ? new Byte(object.byteValue()) : object;
      }
   }

   static Object typeCast(Class paramType, Float object) {
      if (paramType == Double.TYPE) {
         return new Double(object.doubleValue());
      } else if (paramType == Integer.TYPE) {
         return new Float((float)object.intValue());
      } else if (paramType == Short.TYPE) {
         return new Short(object.shortValue());
      } else {
         return paramType == Byte.TYPE ? new Byte(object.byteValue()) : object;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
