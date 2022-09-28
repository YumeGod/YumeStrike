package org.apache.batik.gvt.event;

import java.awt.event.InputEvent;
import org.apache.batik.gvt.GraphicsNode;

public abstract class GraphicsNodeInputEvent extends GraphicsNodeEvent {
   public static final int SHIFT_MASK = 1;
   public static final int CTRL_MASK = 2;
   public static final int META_MASK = 4;
   public static final int ALT_MASK = 8;
   public static final int ALT_GRAPH_MASK = 32;
   public static final int BUTTON1_MASK = 1024;
   public static final int BUTTON2_MASK = 2048;
   public static final int BUTTON3_MASK = 4096;
   public static final int CAPS_LOCK_MASK = 1;
   public static final int NUM_LOCK_MASK = 2;
   public static final int SCROLL_LOCK_MASK = 4;
   public static final int KANA_LOCK_MASK = 8;
   long when;
   int modifiers;
   int lockState;

   protected GraphicsNodeInputEvent(GraphicsNode var1, int var2, long var3, int var5, int var6) {
      super(var1, var2);
      this.when = var3;
      this.modifiers = var5;
      this.lockState = var6;
   }

   protected GraphicsNodeInputEvent(GraphicsNode var1, InputEvent var2, int var3) {
      super(var1, var2.getID());
      this.when = var2.getWhen();
      this.modifiers = var2.getModifiers();
      this.lockState = var3;
   }

   public boolean isShiftDown() {
      return (this.modifiers & 1) != 0;
   }

   public boolean isControlDown() {
      return (this.modifiers & 2) != 0;
   }

   public boolean isMetaDown() {
      return AWTEventDispatcher.isMetaDown(this.modifiers);
   }

   public boolean isAltDown() {
      return (this.modifiers & 8) != 0;
   }

   public boolean isAltGraphDown() {
      return (this.modifiers & 32) != 0;
   }

   public long getWhen() {
      return this.when;
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public int getLockState() {
      return this.lockState;
   }
}
