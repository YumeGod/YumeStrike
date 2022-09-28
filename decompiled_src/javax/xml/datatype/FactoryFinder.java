package javax.xml.datatype;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

final class FactoryFinder {
   private static final String CLASS_NAME = "javax.xml.datatype.FactoryFinder";
   private static boolean debug = false;
   private static Properties cacheProps = new Properties();
   private static boolean firstTime = true;
   private static final int DEFAULT_LINE_LENGTH = 80;
   // $FF: synthetic field
   static Class class$javax$xml$datatype$FactoryFinder;

   private FactoryFinder() {
   }

   private static void debugPrintln(String var0) {
      if (debug) {
         System.err.println("javax.xml.datatype.FactoryFinder:" + var0);
      }

   }

   private static ClassLoader findClassLoader() throws ConfigurationError {
      ClassLoader var0 = SecuritySupport.getContextClassLoader();
      if (debug) {
         debugPrintln("Using context class loader: " + var0);
      }

      if (var0 == null) {
         var0 = (class$javax$xml$datatype$FactoryFinder == null ? (class$javax$xml$datatype$FactoryFinder = class$("javax.xml.datatype.FactoryFinder")) : class$javax$xml$datatype$FactoryFinder).getClassLoader();
         if (debug) {
            debugPrintln("Using the class loader of FactoryFinder: " + var0);
         }
      }

      return var0;
   }

   private static Object newInstance(String var0, ClassLoader var1) throws ConfigurationError {
      try {
         Class var2;
         if (var1 == null) {
            var2 = Class.forName(var0);
         } else {
            var2 = var1.loadClass(var0);
         }

         if (debug) {
            debugPrintln("Loaded " + var0 + " from " + which(var2));
         }

         return var2.newInstance();
      } catch (ClassNotFoundException var4) {
         throw new ConfigurationError("Provider " + var0 + " not found", var4);
      } catch (Exception var5) {
         throw new ConfigurationError("Provider " + var0 + " could not be instantiated: " + var5, var5);
      }
   }

   static Object find(String var0, String var1) throws ConfigurationError {
      ClassLoader var2 = findClassLoader();

      String var3;
      try {
         var3 = SecuritySupport.getSystemProperty(var0);
         if (var3 != null) {
            if (debug) {
               debugPrintln("found " + var3 + " in the system property " + var0);
            }

            return newInstance(var3, var2);
         }
      } catch (SecurityException var10) {
      }

      try {
         var3 = SecuritySupport.getSystemProperty("java.home");
         String var4 = var3 + File.separator + "lib" + File.separator + "jaxp.properties";
         String var5 = null;
         if (firstTime) {
            Properties var6 = cacheProps;
            synchronized(var6) {
               if (firstTime) {
                  File var7 = new File(var4);
                  firstTime = false;
                  if (SecuritySupport.doesFileExist(var7)) {
                     if (debug) {
                        debugPrintln("Read properties file " + var7);
                     }

                     cacheProps.load(SecuritySupport.getFileInputStream(var7));
                  }
               }
            }
         }

         var5 = cacheProps.getProperty(var0);
         if (debug) {
            debugPrintln("found " + var5 + " in $java.home/jaxp.properties");
         }

         if (var5 != null) {
            return newInstance(var5, var2);
         }
      } catch (Exception var11) {
         if (debug) {
            var11.printStackTrace();
         }
      }

      Object var12 = findJarServiceProvider(var0);
      if (var12 != null) {
         return var12;
      } else if (var1 == null) {
         throw new ConfigurationError("Provider for " + var0 + " cannot be found", (Exception)null);
      } else {
         if (debug) {
            debugPrintln("loaded from fallback value: " + var1);
         }

         return newInstance(var1, var2);
      }
   }

   private static Object findJarServiceProvider(String var0) throws ConfigurationError {
      String var1 = "META-INF/services/" + var0;
      InputStream var2 = null;
      ClassLoader var3 = SecuritySupport.getContextClassLoader();
      if (var3 != null) {
         var2 = SecuritySupport.getResourceAsStream(var3, var1);
         if (var2 == null) {
            var3 = (class$javax$xml$datatype$FactoryFinder == null ? (class$javax$xml$datatype$FactoryFinder = class$("javax.xml.datatype.FactoryFinder")) : class$javax$xml$datatype$FactoryFinder).getClassLoader();
            var2 = SecuritySupport.getResourceAsStream(var3, var1);
         }
      } else {
         var3 = (class$javax$xml$datatype$FactoryFinder == null ? (class$javax$xml$datatype$FactoryFinder = class$("javax.xml.datatype.FactoryFinder")) : class$javax$xml$datatype$FactoryFinder).getClassLoader();
         var2 = SecuritySupport.getResourceAsStream(var3, var1);
      }

      if (var2 == null) {
         return null;
      } else {
         if (debug) {
            debugPrintln("found jar resource=" + var1 + " using ClassLoader: " + var3);
         }

         BufferedReader var4;
         try {
            var4 = new BufferedReader(new InputStreamReader(var2, "UTF-8"), 80);
         } catch (UnsupportedEncodingException var18) {
            var4 = new BufferedReader(new InputStreamReader(var2), 80);
         }

         String var5 = null;

         label147: {
            Object var7;
            try {
               var5 = var4.readLine();
               break label147;
            } catch (IOException var19) {
               var7 = null;
            } finally {
               try {
                  var4.close();
               } catch (IOException var17) {
               }

            }

            return var7;
         }

         if (var5 != null && !"".equals(var5)) {
            if (debug) {
               debugPrintln("found in resource, value=" + var5);
            }

            return newInstance(var5, var3);
         } else {
            return null;
         }
      }
   }

   private static String which(Class var0) {
      try {
         String var1 = var0.getName().replace('.', '/') + ".class";
         ClassLoader var2 = var0.getClassLoader();
         URL var3;
         if (var2 != null) {
            var3 = var2.getResource(var1);
         } else {
            var3 = ClassLoader.getSystemResource(var1);
         }

         if (var3 != null) {
            return var3.toString();
         }
      } catch (Throwable var4) {
         if (debug) {
            var4.printStackTrace();
         }
      }

      return "unknown location";
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      try {
         String var0 = SecuritySupport.getSystemProperty("jaxp.debug");
         debug = var0 != null && !"false".equals(var0);
      } catch (Exception var1) {
         debug = false;
      }

   }

   static class ConfigurationError extends Error {
      private static final long serialVersionUID = -3644413026244211347L;
      private Exception exception;

      ConfigurationError(String var1, Exception var2) {
         super(var1);
         this.exception = var2;
      }

      Exception getException() {
         return this.exception;
      }
   }
}
