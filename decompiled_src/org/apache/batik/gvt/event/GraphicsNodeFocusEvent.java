package org.apache.batik.gvt.event;

import org.apache.batik.gvt.GraphicsNode;

public class GraphicsNodeFocusEvent extends GraphicsNodeEvent {
   static final int FOCUS_FIRST = 1004;
   public static final int FOCUS_GAINED = 1004;
   public static final int FOCUS_LOST = 1005;

   public GraphicsNodeFocusEvent(GraphicsNode var1, int var2) {
      super(var1, var2);
   }
}
