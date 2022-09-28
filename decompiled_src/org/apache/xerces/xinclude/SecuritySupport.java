package org.apache.xerces.xinclude;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

final class SecuritySupport {
   private static final SecuritySupport securitySupport = new SecuritySupport();

   static SecuritySupport getInstance() {
      return securitySupport;
   }

   ClassLoader getContextClassLoader() {
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

   ClassLoader getSystemClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            ClassLoader var1 = null;

            try {
               var1 = ClassLoader.getSystemClassLoader();
            } catch (SecurityException var3) {
            }

            return var1;
         }
      });
   }

   ClassLoader getParentClassLoader(final ClassLoader var1) {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            ClassLoader var1x = null;

            try {
               var1x = var1.getParent();
            } catch (SecurityException var3) {
            }

            return var1x == var1 ? null : var1x;
         }
      });
   }

   String getSystemProperty(final String var1) {
      return (String)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return System.getProperty(var1);
         }
      });
   }

   FileInputStream getFileInputStream(final File var1) throws FileNotFoundException {
      try {
         return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws FileNotFoundException {
               return new FileInputStream(var1);
            }
         });
      } catch (PrivilegedActionException var3) {
         throw (FileNotFoundException)var3.getException();
      }
   }

   InputStream getResourceAsStream(final ClassLoader var1, final String var2) {
      return (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            InputStream var1x;
            if (var1 == null) {
               var1x = ClassLoader.getSystemResourceAsStream(var2);
            } else {
               var1x = var1.getResourceAsStream(var2);
            }

            return var1x;
         }
      });
   }

   boolean getFileExists(final File var1) {
      return (Boolean)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return new Boolean(var1.exists());
         }
      });
   }

   long getLastModified(final File var1) {
      return (Long)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return new Long(var1.lastModified());
         }
      });
   }

   private SecuritySupport() {
   }
}
