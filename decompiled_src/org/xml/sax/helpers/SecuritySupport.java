package org.xml.sax.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class SecuritySupport {
   private static final Object securitySupport;

   public static SecuritySupport getInstance() {
      return (SecuritySupport)securitySupport;
   }

   public ClassLoader getContextClassLoader() {
      return null;
   }

   public String getSystemProperty(String var1) {
      return System.getProperty(var1);
   }

   public FileInputStream getFileInputStream(File var1) throws FileNotFoundException {
      return new FileInputStream(var1);
   }

   public InputStream getResourceAsStream(ClassLoader var1, String var2) {
      InputStream var3;
      if (var1 == null) {
         var3 = ClassLoader.getSystemResourceAsStream(var2);
      } else {
         var3 = var1.getResourceAsStream(var2);
      }

      return var3;
   }

   static {
      Object var0 = null;

      try {
         Class var1 = Class.forName("java.security.AccessController");
         var0 = new SecuritySupport12();
      } catch (Exception var6) {
      } finally {
         if (var0 == null) {
            var0 = new SecuritySupport();
         }

         securitySupport = var0;
      }

   }
}
