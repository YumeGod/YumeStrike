package org.apache.batik.script.rhino.svg12;

import java.net.URL;
import org.apache.batik.script.rhino.RhinoInterpreter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class SVG12RhinoInterpreter extends RhinoInterpreter {
   // $FF: synthetic field
   static Class class$org$apache$batik$script$rhino$svg12$GlobalWrapper;

   public SVG12RhinoInterpreter(URL var1) {
      super(var1);
   }

   protected void defineGlobalWrapperClass(Scriptable var1) {
      try {
         ScriptableObject.defineClass(var1, class$org$apache$batik$script$rhino$svg12$GlobalWrapper == null ? (class$org$apache$batik$script$rhino$svg12$GlobalWrapper = class$("org.apache.batik.script.rhino.svg12.GlobalWrapper")) : class$org$apache$batik$script$rhino$svg12$GlobalWrapper);
      } catch (Exception var3) {
      }

   }

   protected ScriptableObject createGlobalObject(Context var1) {
      return new GlobalWrapper(var1);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
