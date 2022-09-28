package org.apache.batik.ext.awt.image.renderable;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.SVGComposite;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;

public class PadRable8Bit extends AbstractRable implements PadRable, PaintRable {
   PadMode padMode;
   Rectangle2D padRect;

   public PadRable8Bit(Filter var1, Rectangle2D var2, PadMode var3) {
      super.init((Filter)var1, (Map)null);
      this.padRect = var2;
      this.padMode = var3;
   }

   public Filter getSource() {
      return (Filter)this.srcs.get(0);
   }

   public void setSource(Filter var1) {
      super.init((Filter)var1, (Map)null);
   }

   public Rectangle2D getBounds2D() {
      return (Rectangle2D)this.padRect.clone();
   }

   public void setPadRect(Rectangle2D var1) {
      this.touch();
      this.padRect = var1;
   }

   public Rectangle2D getPadRect() {
      return (Rectangle2D)this.padRect.clone();
   }

   public void setPadMode(PadMode var1) {
      this.touch();
      this.padMode = var1;
   }

   public PadMode getPadMode() {
      return this.padMode;
   }

   public boolean paintRable(Graphics2D var1) {
      Composite var2 = var1.getComposite();
      if (!SVGComposite.OVER.equals(var2)) {
         return false;
      } else if (this.getPadMode() != PadMode.ZERO_PAD) {
         return false;
      } else {
         Rectangle2D var3 = this.getPadRect();
         Shape var4 = var1.getClip();
         var1.clip(var3);
         GraphicsUtil.drawImage(var1, (RenderableImage)this.getSource());
         var1.setClip(var4);
         return true;
      }
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderingHints var2 = var1.getRenderingHints();
      if (var2 == null) {
         var2 = new RenderingHints((Map)null);
      }

      Filter var3 = this.getSource();
      Object var4 = var1.getAreaOfInterest();
      if (var4 == null) {
         var4 = this.getBounds2D();
      }

      AffineTransform var5 = var1.getTransform();
      Rectangle2D var6 = var3.getBounds2D();
      Rectangle2D var7 = this.getBounds2D();
      Rectangle2D var8 = ((Shape)var4).getBounds2D();
      if (!var8.intersects(var7)) {
         return null;
      } else {
         Rectangle2D.intersect(var8, var7, var8);
         Object var9 = null;
         if (var8.intersects(var6)) {
            var6 = (Rectangle2D)var6.clone();
            Rectangle2D.intersect(var6, var8, var6);
            RenderContext var10 = new RenderContext(var5, var6, var2);
            var9 = var3.createRendering(var10);
         }

         if (var9 == null) {
            var9 = new BufferedImage(1, 1, 2);
         }

         CachableRed var11 = GraphicsUtil.wrap((RenderedImage)var9);
         var8 = var5.createTransformedShape(var8).getBounds2D();
         PadRed var12 = new PadRed(var11, var8.getBounds(), this.padMode, var2);
         return var12;
      }
   }

   public Shape getDependencyRegion(int var1, Rectangle2D var2) {
      if (var1 != 0) {
         throw new IndexOutOfBoundsException("Affine only has one input");
      } else {
         Rectangle2D var3 = this.getSource().getBounds2D();
         if (!var3.intersects(var2)) {
            return new Rectangle2D.Float();
         } else {
            Rectangle2D.intersect(var3, var2, var3);
            Rectangle2D var4 = this.getBounds2D();
            if (!var3.intersects(var4)) {
               return new Rectangle2D.Float();
            } else {
               Rectangle2D.intersect(var3, var4, var3);
               return var3;
            }
         }
      }
   }

   public Shape getDirtyRegion(int var1, Rectangle2D var2) {
      if (var1 != 0) {
         throw new IndexOutOfBoundsException("Affine only has one input");
      } else {
         var2 = (Rectangle2D)var2.clone();
         Rectangle2D var3 = this.getBounds2D();
         if (!var2.intersects(var3)) {
            return new Rectangle2D.Float();
         } else {
            Rectangle2D.intersect(var2, var3, var2);
            return var2;
         }
      }
   }
}
