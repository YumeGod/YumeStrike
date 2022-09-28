package org.apache.commons.logging.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class LogFactoryImpl extends LogFactory {
   public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
   protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
   private static final String LOG_INTERFACE = "org.apache.commons.logging.Log";
   protected Hashtable attributes = new Hashtable();
   protected Hashtable instances = new Hashtable();
   private String logClassName;
   protected Constructor logConstructor = null;
   protected Class[] logConstructorSignature;
   protected Method logMethod;
   protected Class[] logMethodSignature;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$LogFactory;

   public LogFactoryImpl() {
      this.logConstructorSignature = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
      this.logMethod = null;
      this.logMethodSignature = new Class[]{class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory};
   }

   public Object getAttribute(String name) {
      return this.attributes.get(name);
   }

   public String[] getAttributeNames() {
      Vector names = new Vector();
      Enumeration keys = this.attributes.keys();

      while(keys.hasMoreElements()) {
         names.addElement((String)keys.nextElement());
      }

      String[] results = new String[names.size()];

      for(int i = 0; i < results.length; ++i) {
         results[i] = (String)names.elementAt(i);
      }

      return results;
   }

   public Log getInstance(Class clazz) throws LogConfigurationException {
      return this.getInstance(clazz.getName());
   }

   public Log getInstance(String name) throws LogConfigurationException {
      Log instance = (Log)this.instances.get(name);
      if (instance == null) {
         instance = this.newInstance(name);
         this.instances.put(name, instance);
      }

      return instance;
   }

   public void release() {
      this.instances.clear();
   }

   public void removeAttribute(String name) {
      this.attributes.remove(name);
   }

   public void setAttribute(String name, Object value) {
      if (value == null) {
         this.attributes.remove(name);
      } else {
         this.attributes.put(name, value);
      }

   }

   protected String getLogClassName() {
      if (this.logClassName != null) {
         return this.logClassName;
      } else {
         this.logClassName = (String)this.getAttribute("org.apache.commons.logging.Log");
         if (this.logClassName == null) {
            this.logClassName = (String)this.getAttribute("org.apache.commons.logging.log");
         }

         if (this.logClassName == null) {
            try {
               this.logClassName = System.getProperty("org.apache.commons.logging.Log");
            } catch (SecurityException var3) {
            }
         }

         if (this.logClassName == null) {
            try {
               this.logClassName = System.getProperty("org.apache.commons.logging.log");
            } catch (SecurityException var2) {
            }
         }

         if (this.logClassName == null && this.isLog4JAvailable()) {
            this.logClassName = "org.apache.commons.logging.impl.Log4JLogger";
         }

         if (this.logClassName == null && this.isJdk14Available()) {
            this.logClassName = "org.apache.commons.logging.impl.Jdk14Logger";
         }

         if (this.logClassName == null && this.isJdk13LumberjackAvailable()) {
            this.logClassName = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
         }

         if (this.logClassName == null) {
            this.logClassName = "org.apache.commons.logging.impl.SimpleLog";
         }

         return this.logClassName;
      }
   }

   protected Constructor getLogConstructor() throws LogConfigurationException {
      if (this.logConstructor != null) {
         return this.logConstructor;
      } else {
         String logClassName = this.getLogClassName();
         Class logClass = null;
         Class logInterface = null;

         try {
            logInterface = this.getClass().getClassLoader().loadClass("org.apache.commons.logging.Log");
            logClass = loadClass(logClassName);
            if (logClass == null) {
               throw new LogConfigurationException("No suitable Log implementation for " + logClassName);
            }

            if (!logInterface.isAssignableFrom(logClass)) {
               Class[] interfaces = logClass.getInterfaces();

               for(int i = 0; i < interfaces.length; ++i) {
                  if ("org.apache.commons.logging.Log".equals(interfaces[i].getName())) {
                     throw new LogConfigurationException("Invalid class loader hierarchy.  You have more than one version of 'org.apache.commons.logging.Log' visible, which is not allowed.");
                  }
               }

               throw new LogConfigurationException("Class " + logClassName + " does not implement '" + "org.apache.commons.logging.Log" + "'.");
            }
         } catch (Throwable var8) {
            throw new LogConfigurationException(var8);
         }

         try {
            this.logMethod = logClass.getMethod("setLogFactory", this.logMethodSignature);
         } catch (Throwable var7) {
            this.logMethod = null;
         }

         try {
            this.logConstructor = logClass.getConstructor(this.logConstructorSignature);
            return this.logConstructor;
         } catch (Throwable var6) {
            throw new LogConfigurationException("No suitable Log constructor " + this.logConstructorSignature + " for " + logClassName, var6);
         }
      }
   }

   private static Class loadClass(final String name) throws ClassNotFoundException {
      Object result = AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            ClassLoader threadCL = LogFactory.getContextClassLoader();
            if (threadCL != null) {
               try {
                  return threadCL.loadClass(name);
               } catch (ClassNotFoundException var4) {
               }
            }

            try {
               return Class.forName(name);
            } catch (ClassNotFoundException var3) {
               return var3;
            }
         }
      });
      if (result instanceof Class) {
         return (Class)result;
      } else {
         throw (ClassNotFoundException)result;
      }
   }

   protected boolean isJdk13LumberjackAvailable() {
      try {
         loadClass("java.util.logging.Logger");
         loadClass("org.apache.commons.logging.impl.Jdk13LumberjackLogger");
         return true;
      } catch (Throwable var2) {
         return false;
      }
   }

   protected boolean isJdk14Available() {
      try {
         loadClass("java.util.logging.Logger");
         loadClass("org.apache.commons.logging.impl.Jdk14Logger");
         Class throwable = loadClass("java.lang.Throwable");
         return throwable.getDeclaredMethod("getStackTrace", (Class[])null) != null;
      } catch (Throwable var2) {
         return false;
      }
   }

   protected boolean isLog4JAvailable() {
      try {
         loadClass("org.apache.log4j.Logger");
         loadClass("org.apache.commons.logging.impl.Log4JLogger");
         return true;
      } catch (Throwable var2) {
         return false;
      }
   }

   protected Log newInstance(String name) throws LogConfigurationException {
      Log instance = null;

      try {
         Object[] params = new Object[]{name};
         instance = (Log)this.getLogConstructor().newInstance(params);
         if (this.logMethod != null) {
            params[0] = this;
            this.logMethod.invoke(instance, params);
         }

         return instance;
      } catch (InvocationTargetException var5) {
         Throwable c = var5.getTargetException();
         if (c != null) {
            throw new LogConfigurationException(c);
         } else {
            throw new LogConfigurationException(var5);
         }
      } catch (Throwable var6) {
         throw new LogConfigurationException(var6);
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
