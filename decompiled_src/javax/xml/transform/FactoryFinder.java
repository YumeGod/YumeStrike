package javax.xml.transform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

final class FactoryFinder {
   private static boolean debug = false;
   static Properties cacheProps = new Properties();
   static boolean firstTime = true;
   private static final int DEFAULT_LINE_LENGTH = 80;
   // $FF: synthetic field
   static Class class$javax$xml$transform$FactoryFinder;

   private FactoryFinder() {
   }

   private static void dPrint(String var0) {
      if (debug) {
         System.err.println("JAXP: " + var0);
      }

   }

   private static Object newInstance(String var0, ClassLoader var1, boolean var2) throws ConfigurationError {
      try {
         Class var3;
         if (var1 == null) {
            var3 = Class.forName(var0);
         } else {
            try {
               var3 = var1.loadClass(var0);
            } catch (ClassNotFoundException var5) {
               if (!var2) {
                  throw var5;
               }

               var1 = (class$javax$xml$transform$FactoryFinder == null ? (class$javax$xml$transform$FactoryFinder = class$("javax.xml.transform.FactoryFinder")) : class$javax$xml$transform$FactoryFinder).getClassLoader();
               if (var1 != null) {
                  var3 = var1.loadClass(var0);
               } else {
                  var3 = Class.forName(var0);
               }
            }
         }

         Object var4 = var3.newInstance();
         if (debug) {
            dPrint("created new instance of " + var3 + " using ClassLoader: " + var1);
         }

         return var4;
      } catch (ClassNotFoundException var6) {
         throw new ConfigurationError("Provider " + var0 + " not found", var6);
      } catch (Exception var7) {
         throw new ConfigurationError("Provider " + var0 + " could not be instantiated: " + var7, var7);
      }
   }

   static Object find(String var0, String var1) throws ConfigurationError {
      ClassLoader var2 = SecuritySupport.getContextClassLoader();
      if (var2 == null) {
         var2 = (class$javax$xml$transform$FactoryFinder == null ? (class$javax$xml$transform$FactoryFinder = class$("javax.xml.transform.FactoryFinder")) : class$javax$xml$transform$FactoryFinder).getClassLoader();
      }

      if (debug) {
         dPrint("find factoryId =" + var0);
      }

      String var3;
      try {
         var3 = SecuritySupport.getSystemProperty(var0);
         if (var3 != null) {
            if (debug) {
               dPrint("found system property, value=" + var3);
            }

            return newInstance(var3, var2, true);
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
                        dPrint("Read properties file " + var7);
                     }

                     cacheProps.load(SecuritySupport.getFileInputStream(var7));
                  }
               }
            }
         }

         var5 = cacheProps.getProperty(var0);
         if (var5 != null) {
            if (debug) {
               dPrint("found in $java.home/jaxp.properties, value=" + var5);
            }

            return newInstance(var5, var2, true);
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
            dPrint("loaded from fallback value: " + var1);
         }

         return newInstance(var1, var2, true);
      }
   }

   private static Object findJarServiceProvider(String var0) throws ConfigurationError {
      String var1 = "META-INF/services/" + var0;
      InputStream var2 = null;
      ClassLoader var3 = SecuritySupport.getContextClassLoader();
      if (var3 != null) {
         var2 = SecuritySupport.getResourceAsStream(var3, var1);
         if (var2 == null) {
            var3 = (class$javax$xml$transform$FactoryFinder == null ? (class$javax$xml$transform$FactoryFinder = class$("javax.xml.transform.FactoryFinder")) : class$javax$xml$transform$FactoryFinder).getClassLoader();
            var2 = SecuritySupport.getResourceAsStream(var3, var1);
         }
      } else {
         var3 = (class$javax$xml$transform$FactoryFinder == null ? (class$javax$xml$transform$FactoryFinder = class$("javax.xml.transform.FactoryFinder")) : class$javax$xml$transform$FactoryFinder).getClassLoader();
         var2 = SecuritySupport.getResourceAsStream(var3, var1);
      }

      if (var2 == null) {
         return null;
      } else {
         if (debug) {
            dPrint("found jar resource=" + var1 + " using ClassLoader: " + var3);
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
               dPrint("found in resource, value=" + var5);
            }

            return newInstance(var5, var3, false);
         } else {
            return null;
         }
      }
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
      } catch (SecurityException var1) {
         debug = false;
      }

   }

   static class ConfigurationError extends Error {
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
