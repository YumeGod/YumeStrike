package org.apache.batik.script.rhino;

import org.mozilla.javascript.ClassShutter;

public class RhinoClassShutter implements ClassShutter {
   public boolean visibleToScripts(String var1) {
      if (var1.startsWith("org.mozilla.javascript")) {
         return false;
      } else {
         if (var1.startsWith("org.apache.batik.")) {
            String var2 = var1.substring(17);
            if (var2.startsWith("script")) {
               return false;
            }

            if (var2.startsWith("apps")) {
               return false;
            }

            if (var2.startsWith("bridge.")) {
               if (var2.indexOf(".BaseScriptingEnvironment") != -1) {
                  return false;
               }

               if (var2.indexOf(".ScriptingEnvironment") != -1) {
                  return false;
               }
            }
         }

         return true;
      }
   }
}
