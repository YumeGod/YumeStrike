package org.apache.commons.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class LogFactory {
   public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
   public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
   public static final String FACTORY_PROPERTIES = "commons-logging.properties";
   protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
   protected static Hashtable factories = new Hashtable();
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$LogFactory;
   // $FF: synthetic field
   static Class class$java$lang$Thread;

   protected LogFactory() {
   }

   public abstract Object getAttribute(String var1);

   public abstract String[] getAttributeNames();

   public abstract Log getInstance(Class var1) throws LogConfigurationException;

   public abstract Log getInstance(String var1) throws LogConfigurationException;

   public abstract void release();

   public abstract void removeAttribute(String var1);

   public abstract void setAttribute(String var1, Object var2);

   public static LogFactory getFactory() throws LogConfigurationException {
      ClassLoader contextClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return LogFactory.getContextClassLoader();
         }
      });
      LogFactory factory = getCachedFactory(contextClassLoader);
      if (factory != null) {
         return factory;
      } else {
         Properties props = null;

         InputStream is;
         try {
            is = getResourceAsStream(contextClassLoader, "commons-logging.properties");
            if (is != null) {
               props = new Properties();
               props.load(is);
               is.close();
            }
         } catch (IOException var9) {
         } catch (SecurityException var10) {
         }

         String factoryClass;
         try {
            factoryClass = System.getProperty("org.apache.commons.logging.LogFactory");
            if (factoryClass != null) {
               factory = newFactory(factoryClass, contextClassLoader);
            }
         } catch (SecurityException var8) {
         }

         String value;
         if (factory == null) {
            try {
               is = getResourceAsStream(contextClassLoader, "META-INF/services/org.apache.commons.logging.LogFactory");
               if (is != null) {
                  BufferedReader rd;
                  try {
                     rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                  } catch (UnsupportedEncodingException var6) {
                     rd = new BufferedReader(new InputStreamReader(is));
                  }

                  value = rd.readLine();
                  rd.close();
                  if (value != null && !"".equals(value)) {
                     factory = newFactory(value, contextClassLoader);
                  }
               }
            } catch (Exception var7) {
            }
         }

         if (factory == null && props != null) {
            factoryClass = props.getProperty("org.apache.commons.logging.LogFactory");
            if (factoryClass != null) {
               factory = newFactory(factoryClass, contextClassLoader);
            }
         }

         if (factory == null) {
            factory = newFactory("org.apache.commons.logging.impl.LogFactoryImpl", (class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).getClassLoader());
         }

         if (factory != null) {
            cacheFactory(contextClassLoader, factory);
            if (props != null) {
               Enumeration names = props.propertyNames();

               while(names.hasMoreElements()) {
                  String name = (String)names.nextElement();
                  value = props.getProperty(name);
                  factory.setAttribute(name, value);
               }
            }
         }

         return factory;
      }
   }

   public static Log getLog(Class clazz) throws LogConfigurationException {
      return getFactory().getInstance(clazz);
   }

   public static Log getLog(String name) throws LogConfigurationException {
      return getFactory().getInstance(name);
   }

   public static void release(ClassLoader classLoader) {
      Hashtable var1 = factories;
      synchronized(var1) {
         LogFactory factory = (LogFactory)factories.get(classLoader);
         if (factory != null) {
            factory.release();
            factories.remove(classLoader);
         }

      }
   }

   public static void releaseAll() {
      Hashtable var0 = factories;
      synchronized(var0) {
         Enumeration elements = factories.elements();

         while(elements.hasMoreElements()) {
            LogFactory element = (LogFactory)elements.nextElement();
            element.release();
         }

         factories.clear();
      }
   }

   protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
      ClassLoader classLoader = null;

      try {
         Method method = (class$java$lang$Thread == null ? (class$java$lang$Thread = class$("java.lang.Thread")) : class$java$lang$Thread).getMethod("getContextClassLoader", (Class[])null);

         try {
            classLoader = (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
         } catch (IllegalAccessException var4) {
            throw new LogConfigurationException("Unexpected IllegalAccessException", var4);
         } catch (InvocationTargetException var5) {
            if (!(var5.getTargetException() instanceof SecurityException)) {
               throw new LogConfigurationException("Unexpected InvocationTargetException", var5.getTargetException());
            }
         }
      } catch (NoSuchMethodException var6) {
         classLoader = (class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).getClassLoader();
      }

      return classLoader;
   }

   private static LogFactory getCachedFactory(ClassLoader contextClassLoader) {
      LogFactory factory = null;
      if (contextClassLoader != null) {
         factory = (LogFactory)factories.get(contextClassLoader);
      }

      return factory;
   }

   private static void cacheFactory(ClassLoader classLoader, LogFactory factory) {
      if (classLoader != null && factory != null) {
         factories.put(classLoader, factory);
      }

   }

   protected static LogFactory newFactory(final String factoryClass, final ClassLoader classLoader) throws LogConfigurationException {
      Object result = AccessController.doPrivileged(new PrivilegedAction() {
         // $FF: synthetic field
         static Class class$org$apache$commons$logging$LogFactory;

         public Object run() {
            Class logFactoryClass = null;

            try {
               if (classLoader != null) {
                  try {
                     logFactoryClass = classLoader.loadClass(factoryClass);
                     return (LogFactory)logFactoryClass.newInstance();
                  } catch (ClassNotFoundException var5) {
                     if (classLoader == (class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).getClassLoader()) {
                        throw var5;
                     }
                  } catch (NoClassDefFoundError var6) {
                     if (classLoader == (class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).getClassLoader()) {
                        throw var6;
                     }
                  } catch (ClassCastException var7) {
                     if (classLoader == (class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).getClassLoader()) {
                        throw var7;
                     }
                  }
               }

               logFactoryClass = Class.forName(factoryClass);
               return (LogFactory)logFactoryClass.newInstance();
            } catch (Exception var8) {
               return logFactoryClass != null && !(class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).isAssignableFrom(logFactoryClass) ? new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", var8) : new LogConfigurationException(var8);
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
      });
      if (result instanceof LogConfigurationException) {
         throw (LogConfigurationException)result;
      } else {
         return (LogFactory)result;
      }
   }

   private static InputStream getResourceAsStream(final ClassLoader loader, final String name) {
      return (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return loader != null ? loader.getResourceAsStream(name) : ClassLoader.getSystemResourceAsStream(name);
         }
      });
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
