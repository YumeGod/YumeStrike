package org.apache.xerces.xinclude;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

final class ObjectFactory {
   private static final String DEFAULT_PROPERTIES_FILENAME = "xerces.properties";
   private static final boolean DEBUG = false;
   private static final int DEFAULT_LINE_LENGTH = 80;
   private static Properties fXercesProperties = null;
   private static long fLastModified = -1L;
   // $FF: synthetic field
   static Class class$org$apache$xerces$xinclude$ObjectFactory;

   static Object createObject(String var0, String var1) throws ConfigurationError {
      return createObject(var0, (String)null, var1);
   }

   static Object createObject(String var0, String var1, String var2) throws ConfigurationError {
      SecuritySupport var3 = SecuritySupport.getInstance();
      ClassLoader var4 = findClassLoader();

      String var5;
      try {
         var5 = var3.getSystemProperty(var0);
         if (var5 != null) {
            return newInstance(var5, var4, true);
         }
      } catch (SecurityException var47) {
      }

      var5 = null;
      if (var1 == null) {
         File var6 = null;
         boolean var7 = false;

         try {
            String var8 = var3.getSystemProperty("java.home");
            var1 = var8 + File.separator + "lib" + File.separator + "xerces.properties";
            var6 = new File(var1);
            var7 = var3.getFileExists(var6);
         } catch (SecurityException var46) {
            fLastModified = -1L;
            fXercesProperties = null;
         }

         Class var54 = class$org$apache$xerces$xinclude$ObjectFactory == null ? (class$org$apache$xerces$xinclude$ObjectFactory = class$("org.apache.xerces.xinclude.ObjectFactory")) : class$org$apache$xerces$xinclude$ObjectFactory;
         synchronized(var54) {
            boolean var9 = false;
            FileInputStream var10 = null;

            try {
               if (fLastModified >= 0L) {
                  if (var7 && fLastModified < (fLastModified = var3.getLastModified(var6))) {
                     var9 = true;
                  } else if (!var7) {
                     fLastModified = -1L;
                     fXercesProperties = null;
                  }
               } else if (var7) {
                  var9 = true;
                  fLastModified = var3.getLastModified(var6);
               }

               if (var9) {
                  fXercesProperties = new Properties();
                  var10 = var3.getFileInputStream(var6);
                  fXercesProperties.load(var10);
               }
            } catch (Exception var49) {
               fXercesProperties = null;
               fLastModified = -1L;
            } finally {
               if (var10 != null) {
                  try {
                     var10.close();
                  } catch (IOException var44) {
                  }
               }

            }
         }

         if (fXercesProperties != null) {
            var5 = fXercesProperties.getProperty(var0);
         }
      } else {
         FileInputStream var52 = null;

         try {
            var52 = var3.getFileInputStream(new File(var1));
            Properties var53 = new Properties();
            var53.load(var52);
            var5 = var53.getProperty(var0);
         } catch (Exception var45) {
         } finally {
            if (var52 != null) {
               try {
                  var52.close();
               } catch (IOException var43) {
               }
            }

         }
      }

      if (var5 != null) {
         return newInstance(var5, var4, true);
      } else {
         Object var55 = findJarServiceProvider(var0);
         if (var55 != null) {
            return var55;
         } else if (var2 == null) {
            throw new ConfigurationError("Provider for " + var0 + " cannot be found", (Exception)null);
         } else {
            return newInstance(var2, var4, true);
         }
      }
   }

   private static void debugPrintln(String var0) {
   }

   static ClassLoader findClassLoader() throws ConfigurationError {
      SecuritySupport var0 = SecuritySupport.getInstance();
      ClassLoader var1 = var0.getContextClassLoader();
      ClassLoader var2 = var0.getSystemClassLoader();

      ClassLoader var3;
      for(var3 = var2; var1 != var3; var3 = var0.getParentClassLoader(var3)) {
         if (var3 == null) {
            return var1;
         }
      }

      ClassLoader var4 = (class$org$apache$xerces$xinclude$ObjectFactory == null ? (class$org$apache$xerces$xinclude$ObjectFactory = class$("org.apache.xerces.xinclude.ObjectFactory")) : class$org$apache$xerces$xinclude$ObjectFactory).getClassLoader();

      for(var3 = var2; var4 != var3; var3 = var0.getParentClassLoader(var3)) {
         if (var3 == null) {
            return var4;
         }
      }

      return var2;
   }

   static Object newInstance(String var0, ClassLoader var1, boolean var2) throws ConfigurationError {
      try {
         Class var3 = findProviderClass(var0, var1, var2);
         Object var4 = var3.newInstance();
         return var4;
      } catch (ClassNotFoundException var5) {
         throw new ConfigurationError("Provider " + var0 + " not found", var5);
      } catch (Exception var6) {
         throw new ConfigurationError("Provider " + var0 + " could not be instantiated: " + var6, var6);
      }
   }

   static Class findProviderClass(String var0, ClassLoader var1, boolean var2) throws ClassNotFoundException, ConfigurationError {
      SecurityManager var3 = System.getSecurityManager();
      if (var3 != null) {
         int var4 = var0.lastIndexOf(".");
         String var5 = var0;
         if (var4 != -1) {
            var5 = var0.substring(0, var4);
         }

         var3.checkPackageAccess(var5);
      }

      Class var8;
      if (var1 == null) {
         var8 = Class.forName(var0);
      } else {
         try {
            var8 = var1.loadClass(var0);
         } catch (ClassNotFoundException var7) {
            if (!var2) {
               throw var7;
            }

            ClassLoader var6 = (class$org$apache$xerces$xinclude$ObjectFactory == null ? (class$org$apache$xerces$xinclude$ObjectFactory = class$("org.apache.xerces.xinclude.ObjectFactory")) : class$org$apache$xerces$xinclude$ObjectFactory).getClassLoader();
            if (var6 == null) {
               var8 = Class.forName(var0);
            } else {
               if (var1 == var6) {
                  throw var7;
               }

               var8 = var6.loadClass(var0);
            }
         }
      }

      return var8;
   }

   private static Object findJarServiceProvider(String var0) throws ConfigurationError {
      SecuritySupport var1 = SecuritySupport.getInstance();
      String var2 = "META-INF/services/" + var0;
      InputStream var3 = null;
      ClassLoader var4 = findClassLoader();
      var3 = var1.getResourceAsStream(var4, var2);
      if (var3 == null) {
         ClassLoader var5 = (class$org$apache$xerces$xinclude$ObjectFactory == null ? (class$org$apache$xerces$xinclude$ObjectFactory = class$("org.apache.xerces.xinclude.ObjectFactory")) : class$org$apache$xerces$xinclude$ObjectFactory).getClassLoader();
         if (var4 != var5) {
            var4 = var5;
            var3 = var1.getResourceAsStream(var5, var2);
         }
      }

      if (var3 == null) {
         return null;
      } else {
         BufferedReader var22;
         try {
            var22 = new BufferedReader(new InputStreamReader(var3, "UTF-8"), 80);
         } catch (UnsupportedEncodingException var19) {
            var22 = new BufferedReader(new InputStreamReader(var3), 80);
         }

         String var6 = null;

         Object var8;
         try {
            var6 = var22.readLine();
            return var6 != null && !"".equals(var6) ? newInstance(var6, var4, false) : null;
         } catch (IOException var20) {
            var8 = null;
         } finally {
            try {
               var22.close();
            } catch (IOException var18) {
            }

         }

         return var8;
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

   static final class ConfigurationError extends Error {
      static final long serialVersionUID = 5061904944269807898L;
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
