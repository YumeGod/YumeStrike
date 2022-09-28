package org.apache.batik.script.rhino.svg12;

import org.apache.batik.dom.svg12.SVGGlobal;
import org.apache.batik.script.rhino.WindowWrapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.events.EventTarget;

public class GlobalWrapper extends WindowWrapper {
   // $FF: synthetic field
   static Class class$org$apache$batik$script$rhino$svg12$GlobalWrapper;

   public GlobalWrapper(Context var1) {
      super(var1);
      String[] var2 = new String[]{"startMouseCapture", "stopMouseCapture"};
      this.defineFunctionProperties(var2, class$org$apache$batik$script$rhino$svg12$GlobalWrapper == null ? (class$org$apache$batik$script$rhino$svg12$GlobalWrapper = class$("org.apache.batik.script.rhino.svg12.GlobalWrapper")) : class$org$apache$batik$script$rhino$svg12$GlobalWrapper, 2);
   }

   public String getClassName() {
      return "SVGGlobal";
   }

   public String toString() {
      return "[object SVGGlobal]";
   }

   public static void startMouseCapture(Context var0, Scriptable var1, Object[] var2, Function var3) {
      int var4 = var2.length;
      GlobalWrapper var5 = (GlobalWrapper)var1;
      SVGGlobal var6 = (SVGGlobal)var5.window;
      if (var4 >= 3) {
         EventTarget var7 = null;
         if (var2[0] instanceof NativeJavaObject) {
            Object var8 = ((NativeJavaObject)var2[0]).unwrap();
            if (var8 instanceof EventTarget) {
               var7 = (EventTarget)var8;
            }
         }

         if (var7 == null) {
            throw Context.reportRuntimeError("First argument to startMouseCapture must be an EventTarget");
         }

         boolean var10 = Context.toBoolean(var2[1]);
         boolean var9 = Context.toBoolean(var2[2]);
         var6.startMouseCapture(var7, var10, var9);
      }

   }

   public static void stopMouseCapture(Context var0, Scriptable var1, Object[] var2, Function var3) {
      GlobalWrapper var4 = (GlobalWrapper)var1;
      SVGGlobal var5 = (SVGGlobal)var4.window;
      var5.stopMouseCapture();
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
