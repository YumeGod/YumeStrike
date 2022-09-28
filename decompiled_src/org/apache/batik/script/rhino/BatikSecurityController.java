package org.apache.batik.script.rhino;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.GeneratedClassLoader;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.SecurityController;
import org.mozilla.javascript.WrappedException;

public class BatikSecurityController extends SecurityController {
   public GeneratedClassLoader createClassLoader(ClassLoader var1, Object var2) {
      if (var2 instanceof RhinoClassLoader) {
         return (RhinoClassLoader)var2;
      } else {
         throw new SecurityException("Script() objects are not supported");
      }
   }

   public Object getDynamicSecurityDomain(Object var1) {
      RhinoClassLoader var2 = (RhinoClassLoader)var1;
      return var2 != null ? var2 : AccessController.getContext();
   }

   public Object callWithDomain(Object var1, final Context var2, final Callable var3, final Scriptable var4, final Scriptable var5, final Object[] var6) {
      AccessControlContext var7;
      if (var1 instanceof AccessControlContext) {
         var7 = (AccessControlContext)var1;
      } else {
         RhinoClassLoader var8 = (RhinoClassLoader)var1;
         var7 = var8.rhinoAccessControlContext;
      }

      PrivilegedExceptionAction var11 = new PrivilegedExceptionAction() {
         public Object run() {
            return var3.call(var2, var4, var5, var6);
         }
      };

      try {
         return AccessController.doPrivileged(var11, var7);
      } catch (Exception var10) {
         throw new WrappedException(var10);
      }
   }
}
