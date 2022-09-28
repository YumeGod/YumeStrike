package org.apache.batik.script.rhino;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import org.mozilla.javascript.GeneratedClassLoader;

public class RhinoClassLoader extends URLClassLoader implements GeneratedClassLoader {
   protected URL documentURL;
   protected CodeSource codeSource;
   protected AccessControlContext rhinoAccessControlContext;

   public RhinoClassLoader(URL var1, ClassLoader var2) {
      super(var1 != null ? new URL[]{var1} : new URL[0], var2);
      this.documentURL = var1;
      if (var1 != null) {
         this.codeSource = new CodeSource(var1, (Certificate[])null);
      }

      ProtectionDomain var3 = new ProtectionDomain(this.codeSource, this.getPermissions(this.codeSource));
      this.rhinoAccessControlContext = new AccessControlContext(new ProtectionDomain[]{var3});
   }

   static URL[] getURL(ClassLoader var0) {
      if (var0 instanceof RhinoClassLoader) {
         URL var1 = ((RhinoClassLoader)var0).documentURL;
         return var1 != null ? new URL[]{var1} : new URL[0];
      } else {
         return new URL[0];
      }
   }

   public Class defineClass(String var1, byte[] var2) {
      return super.defineClass(var1, var2, 0, var2.length, this.codeSource);
   }

   public void linkClass(Class var1) {
      super.resolveClass(var1);
   }

   public AccessControlContext getAccessControlContext() {
      return this.rhinoAccessControlContext;
   }

   protected PermissionCollection getPermissions(CodeSource var1) {
      PermissionCollection var2 = null;
      if (var1 != null) {
         var2 = super.getPermissions(var1);
      }

      if (this.documentURL != null && var2 != null) {
         Permission var3 = null;
         FilePermission var4 = null;

         try {
            var3 = this.documentURL.openConnection().getPermission();
         } catch (IOException var7) {
            var3 = null;
         }

         if (var3 instanceof FilePermission) {
            String var5 = var3.getName();
            if (!var5.endsWith(File.separator)) {
               int var6 = var5.lastIndexOf(File.separator);
               if (var6 != -1) {
                  var5 = var5.substring(0, var6 + 1);
                  var5 = var5 + "-";
                  var4 = new FilePermission(var5, "read");
                  var2.add(var4);
               }
            }
         }
      }

      return var2;
   }
}
