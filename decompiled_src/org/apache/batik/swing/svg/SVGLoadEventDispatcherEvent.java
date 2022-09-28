package org.apache.batik.swing.svg;

import java.util.EventObject;
import org.apache.batik.gvt.GraphicsNode;

public class SVGLoadEventDispatcherEvent extends EventObject {
   protected GraphicsNode gvtRoot;

   public SVGLoadEventDispatcherEvent(Object var1, GraphicsNode var2) {
      super(var1);
      this.gvtRoot = var2;
   }

   public GraphicsNode getGVTRoot() {
      return this.gvtRoot;
   }
}
