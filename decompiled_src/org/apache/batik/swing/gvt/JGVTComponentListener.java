package org.apache.batik.swing.gvt;

import java.awt.event.ComponentEvent;

public interface JGVTComponentListener {
   int COMPONENT_TRANSFORM_CHANGED = 1337;

   void componentTransformChanged(ComponentEvent var1);
}
