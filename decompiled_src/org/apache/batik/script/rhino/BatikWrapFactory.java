package org.apache.batik.script.rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;
import org.w3c.dom.events.EventTarget;

class BatikWrapFactory extends WrapFactory {
   private RhinoInterpreter interpreter;

   public BatikWrapFactory(RhinoInterpreter var1) {
      this.interpreter = var1;
      this.setJavaPrimitiveWrap(false);
   }

   public Object wrap(Context var1, Scriptable var2, Object var3, Class var4) {
      return var3 instanceof EventTarget ? this.interpreter.buildEventTargetWrapper((EventTarget)var3) : super.wrap(var1, var2, var3, var4);
   }
}
