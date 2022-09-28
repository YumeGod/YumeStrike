package javax.xml.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;

final class SecuritySupport {
   private SecuritySupport() {
   }

   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
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

   static String getSystemProperty(final String var0) {
      return (String)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return System.getProperty(var0);
         }
      });
   }

   static FileInputStream getFileInputStream(final File var0) throws FileNotFoundException {
      try {
         return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws FileNotFoundException {
               return new FileInputStream(var0);
            }
         });
      } catch (PrivilegedActionException var2) {
         throw (FileNotFoundException)var2.getException();
      }
   }

   static InputStream getURLInputStream(final URL var0) throws IOException {
      try {
         return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws IOException {
               return var0.openStream();
            }
         });
      } catch (PrivilegedActionException var2) {
         throw (IOException)var2.getException();
      }
   }

   static URL getResourceAsURL(final ClassLoader var0, final String var1) {
      return (URL)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            URL var1x;
            if (var0 == null) {
               var1x = ClassLoader.getSystemResource(var1);
            } else {
               var1x = ClassLoader.getSystemResource(var1);
            }

            return var1x;
         }
      });
   }

   static Enumeration getResources(final ClassLoader var0, final String var1) throws IOException {
      try {
         return (Enumeration)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws IOException {
               Enumeration var1x;
               if (var0 == null) {
                  var1x = ClassLoader.getSystemResources(var1);
               } else {
                  var1x = ClassLoader.getSystemResources(var1);
               }

               return var1x;
            }
         });
      } catch (PrivilegedActionException var3) {
         throw (IOException)var3.getException();
      }
   }

   static InputStream getResourceAsStream(final ClassLoader var0, final String var1) {
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

   static boolean doesFileExist(final File var0) {
      return (Boolean)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return var0.exists() ? Boolean.TRUE : Boolean.FALSE;
         }
      });
   }
}
