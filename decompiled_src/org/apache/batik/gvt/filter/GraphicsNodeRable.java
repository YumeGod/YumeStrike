package org.apache.batik.gvt.filter;

import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.GraphicsNode;

public interface GraphicsNodeRable extends Filter {
   GraphicsNode getGraphicsNode();

   void setGraphicsNode(GraphicsNode var1);

   boolean getUsePrimitivePaint();

   void setUsePrimitivePaint(boolean var1);
}
