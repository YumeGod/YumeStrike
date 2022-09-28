package org.apache.batik.gvt.event;

import org.apache.batik.gvt.GraphicsNode;

public class GraphicsNodeKeyEvent extends GraphicsNodeInputEvent {
   static final int KEY_FIRST = 400;
   public static final int KEY_TYPED = 400;
   public static final int KEY_PRESSED = 401;
   public static final int KEY_RELEASED = 402;
   protected int keyCode;
   protected char keyChar;
   protected int keyLocation;

   public GraphicsNodeKeyEvent(GraphicsNode var1, int var2, long var3, int var5, int var6, int var7, char var8, int var9) {
      super(var1, var2, var3, var5, var6);
      this.keyCode = var7;
      this.keyChar = var8;
      this.keyLocation = var9;
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public char getKeyChar() {
      return this.keyChar;
   }

   public int getKeyLocation() {
      return this.keyLocation;
   }
}
