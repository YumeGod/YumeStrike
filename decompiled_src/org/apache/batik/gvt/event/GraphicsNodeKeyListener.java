package org.apache.batik.gvt.event;

import java.util.EventListener;

public interface GraphicsNodeKeyListener extends EventListener {
   void keyPressed(GraphicsNodeKeyEvent var1);

   void keyReleased(GraphicsNodeKeyEvent var1);

   void keyTyped(GraphicsNodeKeyEvent var1);
}
