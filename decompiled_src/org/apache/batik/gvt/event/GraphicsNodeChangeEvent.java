package org.apache.batik.gvt.event;

import org.apache.batik.gvt.GraphicsNode;

public class GraphicsNodeChangeEvent extends GraphicsNodeEvent {
   static final int CHANGE_FIRST = 9800;
   public static final int CHANGE_STARTED = 9800;
   public static final int CHANGE_COMPLETED = 9801;
   protected GraphicsNode changeSource;

   public GraphicsNodeChangeEvent(GraphicsNode var1, int var2) {
      super(var1, var2);
   }

   public void setChangeSrc(GraphicsNode var1) {
      this.changeSource = var1;
   }

   public GraphicsNode getChangeSrc() {
      return this.changeSource;
   }
}
