package org.apache.batik.gvt.renderer;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.apache.batik.ext.awt.geom.RectListManager;
import org.apache.batik.gvt.GraphicsNode;

public interface Renderer {
   void setTree(GraphicsNode var1);

   GraphicsNode getTree();

   void repaint(Shape var1);

   void repaint(RectListManager var1);

   void setTransform(AffineTransform var1);

   AffineTransform getTransform();

   boolean isDoubleBuffered();

   void setDoubleBuffered(boolean var1);

   void dispose();
}
