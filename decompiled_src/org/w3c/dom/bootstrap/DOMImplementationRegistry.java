package org.w3c.dom.bootstrap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;
import org.w3c.dom.DOMImplementationSource;

public final class DOMImplementationRegistry {
   public static final String PROPERTY = "org.w3c.dom.DOMImplementationSourceList";
   private static final int DEFAULT_LINE_LENGTH = 80;
   private static final String DEFAULT_DOM_IMPLEMENTATION_SOURCE = "org.apache.xerces.dom.DOMXSImplementationSourceImpl";
   private Vector sources;
   // $FF: synthetic field
   static Class class$org$w3c$dom$bootstrap$DOMImplementationRegistry;

   private DOMImplementationRegistry(Vector var1) {
      this.sources = var1;
   }

   public static DOMImplementationRegistry newInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
      Vector var0 = new Vector();
      ClassLoader var1 = getClassLoader();
      String var2 = getSystemProperty("org.w3c.dom.DOMImplementationSourceList");
      if (var2 == null) {
         var2 = getServiceValue(var1);
      }

      if (var2 == null) {
         var2 = "org.apache.xerces.dom.DOMXSImplementationSourceImpl";
      }

      if (var2 != null) {
         StringTokenizer var3 = new StringTokenizer(var2);

         while(var3.hasMoreTokens()) {
            String var4 = var3.nextToken();
            Class var5 = null;
            if (var1 != null) {
               var5 = var1.loadClass(var4);
            } else {
               var5 = Class.forName(var4);
            }

            DOMImplementationSource var6 = (DOMImplementationSource)var5.newInstance();
            var0.addElement(var6);
         }
      }

      return new DOMImplementationRegistry(var0);
   }

   public DOMImplementation getDOMImplementation(String var1) {
      int var2 = this.sources.size();
      Object var3 = null;

      for(int var4 = 0; var4 < var2; ++var4) {
         DOMImplementationSource var5 = (DOMImplementationSource)this.sources.elementAt(var4);
         DOMImplementation var6 = var5.getDOMImplementation(var1);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   public DOMImplementationList getDOMImplementationList(String var1) {
      final Vector var2 = new Vector();
      int var3 = this.sources.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         DOMImplementationSource var5 = (DOMImplementationSource)this.sources.elementAt(var4);
         DOMImplementationList var6 = var5.getDOMImplementationList(var1);

         for(int var7 = 0; var7 < var6.getLength(); ++var7) {
            DOMImplementation var8 = var6.item(var7);
            var2.addElement(var8);
         }
      }

      return new DOMImplementationList() {
         public DOMImplementation item(int var1) {
            if (var1 >= 0 && var1 < var2.size()) {
               try {
                  return (DOMImplementation)var2.elementAt(var1);
               } catch (ArrayIndexOutOfBoundsException var3) {
                  return null;
               }
            } else {
               return null;
            }
         }

         public int getLength() {
            return var2.size();
         }
      };
   }

   public void addSource(DOMImplementationSource var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (!this.sources.contains(var1)) {
            this.sources.addElement(var1);
         }

      }
   }

   private static ClassLoader getClassLoader() {
      try {
         ClassLoader var0 = getContextClassLoader();
         if (var0 != null) {
            return var0;
         }
      } catch (Exception var1) {
         return (class$org$w3c$dom$bootstrap$DOMImplementationRegistry == null ? (class$org$w3c$dom$bootstrap$DOMImplementationRegistry = class$("org.w3c.dom.bootstrap.DOMImplementationRegistry")) : class$org$w3c$dom$bootstrap$DOMImplementationRegistry).getClassLoader();
      }

      return (class$org$w3c$dom$bootstrap$DOMImplementationRegistry == null ? (class$org$w3c$dom$bootstrap$DOMImplementationRegistry = class$("org.w3c.dom.bootstrap.DOMImplementationRegistry")) : class$org$w3c$dom$bootstrap$DOMImplementationRegistry).getClassLoader();
   }

   private static String getServiceValue(ClassLoader var0) {
      String var1 = "META-INF/services/org.w3c.dom.DOMImplementationSourceList";

      try {
         InputStream var2 = getResourceAsStream(var0, var1);
         if (var2 != null) {
            BufferedReader var3;
            try {
               var3 = new BufferedReader(new InputStreamReader(var2, "UTF-8"), 80);
            } catch (UnsupportedEncodingException var11) {
               var3 = new BufferedReader(new InputStreamReader(var2), 80);
            }

            String var4 = null;

            try {
               var4 = var3.readLine();
            } finally {
               var3.close();
            }

            if (var4 != null && var4.length() > 0) {
               return var4;
            }
         }

         return null;
      } catch (Exception var12) {
         return null;
      }
   }

   private static boolean isJRE11() {
      try {
         Class var0 = Class.forName("java.security.AccessController");
         return false;
      } catch (Exception var1) {
         return true;
      }
   }

   private static ClassLoader getContextClassLoader() {
      return isJRE11() ? null : (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            ClassLoader var1 = null;

            try {
               var1 = Thread.currentThread().getContextClassLoader();
            } catch (SecurityException var3) {
            }

            return var1;
         }
      });
   }

   private static String getSystemProperty(final String var0) {
      return isJRE11() ? System.getProperty(var0) : (String)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return System.getProperty(var0);
         }
      });
   }

   private static InputStream getResourceAsStream(final ClassLoader var0, final String var1) {
      if (isJRE11()) {
         InputStream var2;
         if (var0 == null) {
            var2 = ClassLoader.getSystemResourceAsStream(var1);
         } else {
            var2 = var0.getResourceAsStream(var1);
         }

         return var2;
      } else {
         return (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               InputStream var1x;
               if (var0 == null) {
                  var1x = ClassLoader.getSystemResourceAsStream(var1);
               } else {
                  var1x = var0.getResourceAsStream(var1);
               }

               return var1x;
            }
         });
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
}
