package org.xml.sax.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecuritySupport12 extends SecuritySupport {
   public ClassLoader getContextClassLoader() {
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

   public String getSystemProperty(final String var1) {
      return (String)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return System.getProperty(var1);
         }
      });
   }

   public FileInputStream getFileInputStream(final File var1) throws FileNotFoundException {
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

   public InputStream getResourceAsStream(final ClassLoader var1, final String var2) {
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
}
