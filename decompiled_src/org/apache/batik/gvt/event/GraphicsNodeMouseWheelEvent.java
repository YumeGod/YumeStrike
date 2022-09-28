package org.apache.batik.gvt.event;

import org.apache.batik.gvt.GraphicsNode;

public class GraphicsNodeMouseWheelEvent extends GraphicsNodeInputEvent {
   public static final int MOUSE_WHEEL = 600;
   protected int wheelDelta;

   public GraphicsNodeMouseWheelEvent(GraphicsNode var1, int var2, long var3, int var5, int var6, int var7) {
      super(var1, var2, var3, var5, var6);
      this.wheelDelta = var7;
   }

   public int getWheelDelta() {
      return this.wheelDelta;
   }
}
