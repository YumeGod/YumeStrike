package org.apache.batik.gvt.event;

import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.util.EventListener;
import java.util.EventObject;
import org.apache.batik.gvt.GraphicsNode;

public interface EventDispatcher {
   void setRootNode(GraphicsNode var1);

   GraphicsNode getRootNode();

   void setBaseTransform(AffineTransform var1);

   AffineTransform getBaseTransform();

   void dispatchEvent(EventObject var1);

   void addGraphicsNodeMouseListener(GraphicsNodeMouseListener var1);

   void removeGraphicsNodeMouseListener(GraphicsNodeMouseListener var1);

   void addGraphicsNodeMouseWheelListener(GraphicsNodeMouseWheelListener var1);

   void removeGraphicsNodeMouseWheelListener(GraphicsNodeMouseWheelListener var1);

   void addGraphicsNodeKeyListener(GraphicsNodeKeyListener var1);

   void removeGraphicsNodeKeyListener(GraphicsNodeKeyListener var1);

   EventListener[] getListeners(Class var1);

   void setNodeIncrementEvent(InputEvent var1);

   void setNodeDecrementEvent(InputEvent var1);
}
