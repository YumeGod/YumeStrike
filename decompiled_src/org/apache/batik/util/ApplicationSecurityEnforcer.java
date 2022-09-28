package org.apache.batik.util;

import java.net.URL;
import java.security.Policy;

public class ApplicationSecurityEnforcer {
   public static final String EXCEPTION_ALIEN_SECURITY_MANAGER = "ApplicationSecurityEnforcer.message.security.exception.alien.security.manager";
   public static final String EXCEPTION_NO_POLICY_FILE = "ApplicationSecurityEnforcer.message.null.pointer.exception.no.policy.file";
   public static final String PROPERTY_JAVA_SECURITY_POLICY = "java.security.policy";
   public static final String JAR_PROTOCOL = "jar:";
   public static final String JAR_URL_FILE_SEPARATOR = "!/";
   public static final String PROPERTY_APP_DEV_BASE = "app.dev.base";
   public static final String PROPERTY_APP_JAR_BASE = "app.jar.base";
   public static final String APP_MAIN_CLASS_DIR = "classes/";
   protected Class appMainClass;
   protected String securityPolicy;
   protected String appMainClassRelativeURL;
   protected BatikSecurityManager lastSecurityManagerInstalled;

   /** @deprecated */
   public ApplicationSecurityEnforcer(Class var1, String var2, String var3) {
      this(var1, var2);
   }

   public ApplicationSecurityEnforcer(Class var1, String var2) {
      this.appMainClass = var1;
      this.securityPolicy = var2;
      this.appMainClassRelativeURL = var1.getName().replace('.', '/') + ".class";
   }

   public void enforceSecurity(boolean var1) {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null && var2 != this.lastSecurityManagerInstalled) {
         throw new SecurityException(Messages.getString("ApplicationSecurityEnforcer.message.security.exception.alien.security.manager"));
      } else {
         if (var1) {
            System.setSecurityManager((SecurityManager)null);
            this.installSecurityManager();
         } else if (var2 != null) {
            System.setSecurityManager((SecurityManager)null);
            this.lastSecurityManagerInstalled = null;
         }

      }
   }

   public URL getPolicyURL() {
      ClassLoader var1 = this.appMainClass.getClassLoader();
      URL var2 = var1.getResource(this.securityPolicy);
      if (var2 == null) {
         throw new NullPointerException(Messages.formatMessage("ApplicationSecurityEnforcer.message.null.pointer.exception.no.policy.file", new Object[]{this.securityPolicy}));
      } else {
         return var2;
      }
   }

   public void installSecurityManager() {
      Policy var1 = Policy.getPolicy();
      BatikSecurityManager var2 = new BatikSecurityManager();
      ClassLoader var3 = this.appMainClass.getClassLoader();
      String var4 = System.getProperty("java.security.policy");
      URL var5;
      if (var4 == null || var4.equals("")) {
         var5 = this.getPolicyURL();
         System.setProperty("java.security.policy", var5.toString());
      }

      var5 = var3.getResource(this.appMainClassRelativeURL);
      if (var5 == null) {
         throw new Error(this.appMainClassRelativeURL);
      } else {
         String var6 = var5.toString();
         if (var6.startsWith("jar:")) {
            this.setJarBase(var6);
         } else {
            this.setDevBase(var6);
         }

         System.setSecurityManager(var2);
         this.lastSecurityManagerInstalled = var2;
         var1.refresh();
         if (var4 == null || var4.equals("")) {
            System.setProperty("java.security.policy", "");
         }

      }
   }

   private void setJarBase(String var1) {
      String var2 = System.getProperty("app.jar.base");
      if (var2 == null) {
         var1 = var1.substring("jar:".length());
         int var3 = var1.indexOf("!/" + this.appMainClassRelativeURL);
         if (var3 == -1) {
            throw new Error();
         }

         String var4 = var1.substring(0, var3);
         var3 = var4.lastIndexOf(47);
         if (var3 == -1) {
            var4 = "";
         } else {
            var4 = var4.substring(0, var3);
         }

         System.setProperty("app.jar.base", var4);
      }

   }

   private void setDevBase(String var1) {
      String var2 = System.getProperty("app.dev.base");
      if (var2 == null) {
         int var3 = var1.indexOf("classes/" + this.appMainClassRelativeURL);
         if (var3 == -1) {
            throw new Error();
         }

         String var4 = var1.substring(0, var3);
         System.setProperty("app.dev.base", var4);
      }

   }
}
