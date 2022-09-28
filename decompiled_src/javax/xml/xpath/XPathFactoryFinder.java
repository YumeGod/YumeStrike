package javax.xml.xpath;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;

final class XPathFactoryFinder {
   private static boolean debug = false;
   private static final int DEFAULT_LINE_LENGTH = 80;
   private static Properties cacheProps;
   private static boolean firstTime;
   private final ClassLoader classLoader;
   private static final Class SERVICE_CLASS;
   private static final String SERVICE_ID;
   // $FF: synthetic field
   static Class class$javax$xml$xpath$XPathFactory;

   private static void debugPrintln(String var0) {
      if (debug) {
         System.err.println("JAXP: " + var0);
      }

   }

   public XPathFactoryFinder(ClassLoader var1) {
      this.classLoader = var1;
      if (debug) {
         this.debugDisplayClassLoader();
      }

   }

   private void debugDisplayClassLoader() {
      try {
         if (this.classLoader == SecuritySupport.getContextClassLoader()) {
            debugPrintln("using thread context class loader (" + this.classLoader + ") for search");
            return;
         }
      } catch (Throwable var2) {
      }

      if (this.classLoader == ClassLoader.getSystemClassLoader()) {
         debugPrintln("using system class loader (" + this.classLoader + ") for search");
      } else {
         debugPrintln("using class loader (" + this.classLoader + ") for search");
      }
   }

   public XPathFactory newFactory(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         XPathFactory var2 = this._newFactory(var1);
         if (debug) {
            if (var2 != null) {
               debugPrintln("factory '" + var2.getClass().getName() + "' was found for " + var1);
            } else {
               debugPrintln("unable to find a factory for " + var1);
            }
         }

         return var2;
      }
   }

   private XPathFactory _newFactory(String var1) {
      String var3 = SERVICE_CLASS.getName() + ":" + var1;

      XPathFactory var2;
      String var4;
      try {
         if (debug) {
            debugPrintln("Looking up system property '" + var3 + "'");
         }

         var4 = SecuritySupport.getSystemProperty(var3);
         if (var4 != null) {
            if (debug) {
               debugPrintln("The value is '" + var4 + "'");
            }

            var2 = this.createInstance(var4);
            if (var2 != null) {
               return var2;
            }
         } else if (debug) {
            debugPrintln("The property is undefined.");
         }
      } catch (Throwable var13) {
         if (debug) {
            debugPrintln("failed to look up system property '" + var3 + "'");
            var13.printStackTrace();
         }
      }

      var4 = SecuritySupport.getSystemProperty("java.home");
      String var5 = var4 + File.separator + "lib" + File.separator + "jaxp.properties";
      String var6 = null;

      try {
         if (firstTime) {
            Properties var7 = cacheProps;
            synchronized(var7) {
               if (firstTime) {
                  File var8 = new File(var5);
                  firstTime = false;
                  if (SecuritySupport.doesFileExist(var8)) {
                     if (debug) {
                        debugPrintln("Read properties file " + var8);
                     }

                     cacheProps.load(SecuritySupport.getFileInputStream(var8));
                  }
               }
            }
         }

         var6 = cacheProps.getProperty(var3);
         if (debug) {
            debugPrintln("found " + var6 + " in $java.home/jaxp.properties");
         }

         if (var6 != null) {
            var2 = this.createInstance(var6);
            if (var2 != null) {
               return var2;
            }
         }
      } catch (Exception var12) {
         if (debug) {
            var12.printStackTrace();
         }
      }

      Iterator var14 = this.createServiceFileIterator();

      while(var14.hasNext()) {
         URL var15 = (URL)var14.next();
         if (debug) {
            debugPrintln("looking into " + var15);
         }

         try {
            var2 = this.loadFromServicesFile(var1, var15.toExternalForm(), SecuritySupport.getURLInputStream(var15));
            if (var2 != null) {
               return var2;
            }
         } catch (IOException var11) {
            if (debug) {
               debugPrintln("failed to read " + var15);
               var11.printStackTrace();
            }
         }
      }

      if (var1.equals("http://java.sun.com/jaxp/xpath/dom")) {
         if (debug) {
            debugPrintln("attempting to use the platform default W3C DOM XPath lib");
         }

         return this.createInstance("org.apache.xpath.jaxp.XPathFactoryImpl");
      } else {
         if (debug) {
            debugPrintln("all things were tried, but none was found. bailing out.");
         }

         return null;
      }
   }

   private XPathFactory createInstance(String var1) {
      try {
         if (debug) {
            debugPrintln("instanciating " + var1);
         }

         Class var2;
         if (this.classLoader != null) {
            var2 = this.classLoader.loadClass(var1);
         } else {
            var2 = Class.forName(var1);
         }

         if (debug) {
            debugPrintln("loaded it from " + which(var2));
         }

         Object var3 = var2.newInstance();
         if (var3 instanceof XPathFactory) {
            return (XPathFactory)var3;
         }

         if (debug) {
            debugPrintln(var1 + " is not assignable to " + SERVICE_CLASS.getName());
         }
      } catch (Throwable var4) {
         if (debug) {
            debugPrintln("failed to instanciate " + var1);
            var4.printStackTrace();
         }
      }

      return null;
   }

   private XPathFactory loadFromServicesFile(String var1, String var2, InputStream var3) {
      if (debug) {
         debugPrintln("Reading " + var2);
      }

      BufferedReader var4;
      try {
         var4 = new BufferedReader(new InputStreamReader(var3, "UTF-8"), 80);
      } catch (UnsupportedEncodingException var10) {
         var4 = new BufferedReader(new InputStreamReader(var3), 80);
      }

      String var5 = null;
      XPathFactory var6 = null;

      while(true) {
         try {
            var5 = var4.readLine();
         } catch (IOException var12) {
            break;
         }

         if (var5 == null) {
            break;
         }

         int var7 = var5.indexOf(35);
         if (var7 != -1) {
            var5 = var5.substring(0, var7);
         }

         var5 = var5.trim();
         if (var5.length() != 0) {
            try {
               XPathFactory var8 = this.createInstance(var5);
               if (var8.isObjectModelSupported(var1)) {
                  var6 = var8;
                  break;
               }
            } catch (Exception var11) {
            }
         }
      }

      try {
         var4.close();
      } catch (IOException var9) {
      }

      return var6;
   }

   private Iterator createServiceFileIterator() {
      if (this.classLoader == null) {
         return new SingleIterator() {
            // $FF: synthetic field
            static Class class$javax$xml$xpath$XPathFactoryFinder;

            protected Object value() {
               ClassLoader var1 = (class$javax$xml$xpath$XPathFactoryFinder == null ? (class$javax$xml$xpath$XPathFactoryFinder = class$("javax.xml.xpath.XPathFactoryFinder")) : class$javax$xml$xpath$XPathFactoryFinder).getClassLoader();
               return SecuritySupport.getResourceAsURL(var1, XPathFactoryFinder.SERVICE_ID);
            }

            // $FF: synthetic method
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         };
      } else {
         try {
            final Enumeration var1 = SecuritySupport.getResources(this.classLoader, SERVICE_ID);
            if (debug && !var1.hasMoreElements()) {
               debugPrintln("no " + SERVICE_ID + " file was found");
            }

            return new Iterator() {
               public void remove() {
                  throw new UnsupportedOperationException();
               }

               public boolean hasNext() {
                  return var1.hasMoreElements();
               }

               public Object next() {
                  return var1.nextElement();
               }
            };
         } catch (IOException var2) {
            if (debug) {
               debugPrintln("failed to enumerate resources " + SERVICE_ID);
               var2.printStackTrace();
            }

            return (new ArrayList()).iterator();
         }
      }
   }

   private static String which(Class var0) {
      return which(var0.getName(), var0.getClassLoader());
   }

   private static String which(String var0, ClassLoader var1) {
      String var2 = var0.replace('.', '/') + ".class";
      if (var1 == null) {
         var1 = ClassLoader.getSystemClassLoader();
      }

      URL var3 = SecuritySupport.getResourceAsURL(var1, var2);
      return var3 != null ? var3.toString() : null;
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

      cacheProps = new Properties();
      firstTime = true;
      SERVICE_CLASS = class$javax$xml$xpath$XPathFactory == null ? (class$javax$xml$xpath$XPathFactory = class$("javax.xml.xpath.XPathFactory")) : class$javax$xml$xpath$XPathFactory;
      SERVICE_ID = "META-INF/services/" + SERVICE_CLASS.getName();
   }

   private abstract static class SingleIterator implements Iterator {
      private boolean seen;

      private SingleIterator() {
         this.seen = false;
      }

      public final void remove() {
         throw new UnsupportedOperationException();
      }

      public final boolean hasNext() {
         return !this.seen;
      }

      public final Object next() {
         if (this.seen) {
            throw new NoSuchElementException();
         } else {
            this.seen = true;
            return this.value();
         }
      }

      protected abstract Object value();

      // $FF: synthetic method
      SingleIterator(Object var1) {
         this();
      }
   }
}
