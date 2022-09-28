package org.apache.xml.dtm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class SecuritySupport {
   private static final Object securitySupport;

   static SecuritySupport getInstance() {
      return (SecuritySupport)securitySupport;
   }

   ClassLoader getContextClassLoader() {
      return null;
   }

   ClassLoader getSystemClassLoader() {
      return null;
   }

   ClassLoader getParentClassLoader(ClassLoader cl) {
      return null;
   }

   String getSystemProperty(String propName) {
      return System.getProperty(propName);
   }

   FileInputStream getFileInputStream(File file) throws FileNotFoundException {
      return new FileInputStream(file);
   }

   InputStream getResourceAsStream(ClassLoader cl, String name) {
      InputStream ris;
      if (cl == null) {
         ris = ClassLoader.getSystemResourceAsStream(name);
      } else {
         ris = cl.getResourceAsStream(name);
      }

      return ris;
   }

   boolean getFileExists(File f) {
      return f.exists();
   }

   long getLastModified(File f) {
      return f.lastModified();
   }

   static {
      SecuritySupport ss = null;

      try {
         Class c = Class.forName("java.security.AccessController");
         ss = new SecuritySupport12();
      } catch (Exception var6) {
      } finally {
         if (ss == null) {
            ss = new SecuritySupport();
         }

         securitySupport = ss;
      }

   }
}
