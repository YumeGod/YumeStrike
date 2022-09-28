package org.apache.batik.gvt.filter;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.SVGComposite;
import org.apache.batik.ext.awt.image.renderable.AbstractRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PaintRable;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.TranslateRed;
import org.apache.batik.gvt.GraphicsNode;

public class GraphicsNodeRable8Bit extends AbstractRable implements GraphicsNodeRable, PaintRable {
   private AffineTransform cachedGn2dev = null;
   private AffineTransform cachedUsr2dev = null;
   private CachableRed cachedRed = null;
   private Rectangle2D cachedBounds = null;
   private boolean usePrimitivePaint = true;
   private GraphicsNode node;

   public boolean getUsePrimitivePaint() {
      return this.usePrimitivePaint;
   }

   public void setUsePrimitivePaint(boolean var1) {
      this.usePrimitivePaint = var1;
   }

   public GraphicsNode getGraphicsNode() {
      return this.node;
   }

   public void setGraphicsNode(GraphicsNode var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.node = var1;
      }
   }

   public void clearCache() {
      this.cachedRed = null;
      this.cachedUsr2dev = null;
      this.cachedGn2dev = null;
      this.cachedBounds = null;
   }

   public GraphicsNodeRable8Bit(GraphicsNode var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.node = var1;
         this.usePrimitivePaint = true;
      }
   }

   public GraphicsNodeRable8Bit(GraphicsNode var1, Map var2) {
      super((Filter)null, var2);
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.node = var1;
         this.usePrimitivePaint = true;
      }
   }

   public GraphicsNodeRable8Bit(GraphicsNode var1, boolean var2) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.node = var1;
         this.usePrimitivePaint = var2;
      }
   }

   public Rectangle2D getBounds2D() {
      Rectangle2D var1;
      if (this.usePrimitivePaint) {
         var1 = this.node.getPrimitiveBounds();
         return (Rectangle2D)(var1 == null ? new Rectangle2D.Double(0.0, 0.0, 0.0, 0.0) : (Rectangle2D)var1.clone());
      } else {
         var1 = this.node.getBounds();
         if (var1 == null) {
            return new Rectangle2D.Double(0.0, 0.0, 0.0, 0.0);
         } else {
            AffineTransform var2 = this.node.getTransform();
            if (var2 != null) {
               var1 = var2.createTransformedShape(var1).getBounds2D();
            }

            return var1;
         }
      }
   }

   public boolean isDynamic() {
      return false;
   }

   public boolean paintRable(Graphics2D var1) {
      Composite var2 = var1.getComposite();
      if (!SVGComposite.OVER.equals(var2)) {
         return false;
      } else {
         ColorSpace var3 = GraphicsUtil.getDestinationColorSpace(var1);
         if (var3 != null && var3 == ColorSpace.getInstance(1000)) {
            GraphicsNode var4 = this.getGraphicsNode();
            if (this.getUsePrimitivePaint()) {
               var4.primitivePaint(var1);
            } else {
               var4.paint(var1);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public RenderedImage createRendering(RenderContext var1) {
      AffineTransform var2 = var1.getTransform();
      AffineTransform var3;
      if (var2 == null) {
         var2 = new AffineTransform();
         var3 = var2;
      } else {
         var3 = (AffineTransform)var2.clone();
      }

      AffineTransform var4 = this.node.getTransform();
      if (var4 != null) {
         var3.concatenate(var4);
      }

      Rectangle2D var5 = this.getBounds2D();
      if (this.cachedBounds != null && this.cachedGn2dev != null && this.cachedBounds.equals(var5) && var3.getScaleX() == this.cachedGn2dev.getScaleX() && var3.getScaleY() == this.cachedGn2dev.getScaleY() && var3.getShearX() == this.cachedGn2dev.getShearX() && var3.getShearY() == this.cachedGn2dev.getShearY()) {
         double var6 = var2.getTranslateX() - this.cachedUsr2dev.getTranslateX();
         double var8 = var2.getTranslateY() - this.cachedUsr2dev.getTranslateY();
         if (var6 == 0.0 && var8 == 0.0) {
            return this.cachedRed;
         }

         if (var6 == (double)((int)var6) && var8 == (double)((int)var8)) {
            return new TranslateRed(this.cachedRed, (int)Math.round((double)this.cachedRed.getMinX() + var6), (int)Math.round((double)this.cachedRed.getMinY() + var8));
         }
      }

      if (var5.getWidth() > 0.0 && var5.getHeight() > 0.0) {
         this.cachedUsr2dev = (AffineTransform)var2.clone();
         this.cachedGn2dev = var3;
         this.cachedBounds = var5;
         this.cachedRed = new GraphicsNodeRed8Bit(this.node, var2, this.usePrimitivePaint, var1.getRenderingHints());
         return this.cachedRed;
      } else {
         this.cachedUsr2dev = null;
         this.cachedGn2dev = null;
         this.cachedBounds = null;
         this.cachedRed = null;
         return null;
      }
   }
}
