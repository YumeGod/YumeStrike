package org.apache.batik.gvt.event;

import java.util.EventListener;

public interface GraphicsNodeChangeListener extends EventListener {
   void changeStarted(GraphicsNodeChangeEvent var1);

   void changeCompleted(GraphicsNodeChangeEvent var1);
}
