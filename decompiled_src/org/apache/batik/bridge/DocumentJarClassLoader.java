package org.apache.batik.bridge;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class DocumentJarClassLoader extends URLClassLoader {
   protected CodeSource documentCodeSource = null;

   public DocumentJarClassLoader(URL var1, URL var2) {
      super(new URL[]{var1});
      if (var2 != null) {
         this.documentCodeSource = new CodeSource(var2, (Certificate[])null);
      }

   }

   protected PermissionCollection getPermissions(CodeSource var1) {
      Policy var2 = Policy.getPolicy();
      PermissionCollection var3 = null;
      if (var2 != null) {
         var3 = var2.getPermissions(var1);
      }

      if (this.documentCodeSource != null) {
         PermissionCollection var4 = super.getPermissions(this.documentCodeSource);
         if (var3 != null) {
            Enumeration var5 = var4.elements();

            while(var5.hasMoreElements()) {
               var3.add((Permission)var5.nextElement());
            }
         } else {
            var3 = var4;
         }
      }

      return var3;
   }
}
