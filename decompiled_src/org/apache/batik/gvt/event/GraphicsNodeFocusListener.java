package org.apache.batik.gvt.event;

import java.util.EventListener;

public interface GraphicsNodeFocusListener extends EventListener {
   void focusGained(GraphicsNodeFocusEvent var1);

   void focusLost(GraphicsNodeFocusEvent var1);
}
