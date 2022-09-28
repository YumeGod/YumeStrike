package org.apache.batik.gvt.event;

import java.util.EventListener;

public interface GraphicsNodeMouseListener extends EventListener {
   void mouseClicked(GraphicsNodeMouseEvent var1);

   void mousePressed(GraphicsNodeMouseEvent var1);

   void mouseReleased(GraphicsNodeMouseEvent var1);

   void mouseEntered(GraphicsNodeMouseEvent var1);

   void mouseExited(GraphicsNodeMouseEvent var1);

   void mouseDragged(GraphicsNodeMouseEvent var1);

   void mouseMoved(GraphicsNodeMouseEvent var1);
}
