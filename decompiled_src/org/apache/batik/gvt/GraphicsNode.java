package org.apache.batik.gvt;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.apache.batik.ext.awt.image.renderable.ClipRable;
import org.apache.batik.ext.awt.image.renderable.Filter;

public interface GraphicsNode {
   int VISIBLE_PAINTED = 0;
   int VISIBLE_FILL = 1;
   int VISIBLE_STROKE = 2;
   int VISIBLE = 3;
   int PAINTED = 4;
   int FILL = 5;
   int STROKE = 6;
   int ALL = 7;
   int NONE = 8;
   AffineTransform IDENTITY = new AffineTransform();

   WeakReference getWeakReference();

   int getPointerEventType();

   void setPointerEventType(int var1);

   void setTransform(AffineTransform var1);

   AffineTransform getTransform();

   AffineTransform getInverseTransform();

   AffineTransform getGlobalTransform();

   void setComposite(Composite var1);

   Composite getComposite();

   void setVisible(boolean var1);

   boolean isVisible();

   void setClip(ClipRable var1);

   ClipRable getClip();

   void setRenderingHint(RenderingHints.Key var1, Object var2);

   void setRenderingHints(Map var1);

   void setRenderingHints(RenderingHints var1);

   RenderingHints getRenderingHints();

   void setMask(org.apache.batik.gvt.filter.Mask var1);

   org.apache.batik.gvt.filter.Mask getMask();

   void setFilter(Filter var1);

   Filter getFilter();

   Filter getGraphicsNodeRable(boolean var1);

   Filter getEnableBackgroundGraphicsNodeRable(boolean var1);

   void paint(Graphics2D var1);

   void primitivePaint(Graphics2D var1);

   CompositeGraphicsNode getParent();

   RootGraphicsNode getRoot();

   Rectangle2D getBounds();

   Rectangle2D getTransformedBounds(AffineTransform var1);

   Rectangle2D getPrimitiveBounds();

   Rectangle2D getTransformedPrimitiveBounds(AffineTransform var1);

   Rectangle2D getGeometryBounds();

   Rectangle2D getTransformedGeometryBounds(AffineTransform var1);

   Rectangle2D getSensitiveBounds();

   Rectangle2D getTransformedSensitiveBounds(AffineTransform var1);

   boolean contains(Point2D var1);

   boolean intersects(Rectangle2D var1);

   GraphicsNode nodeHitAt(Point2D var1);

   Shape getOutline();
}
