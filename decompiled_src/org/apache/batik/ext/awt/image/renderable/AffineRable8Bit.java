package org.apache.batik.ext.awt.image.renderable;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class AffineRable8Bit extends AbstractRable implements AffineRable, PaintRable {
   AffineTransform affine;
   AffineTransform invAffine;

   public AffineRable8Bit(Filter var1, AffineTransform var2) {
      this.init(var1);
      this.setAffine(var2);
   }

   public Rectangle2D getBounds2D() {
      Filter var1 = this.getSource();
      Rectangle2D var2 = var1.getBounds2D();
      return this.affine.createTransformedShape(var2).getBounds2D();
   }

   public Filter getSource() {
      return (Filter)this.srcs.get(0);
   }

   public void setSource(Filter var1) {
      this.init(var1);
   }

   public void setAffine(AffineTransform var1) {
      this.touch();
      this.affine = var1;

      try {
         this.invAffine = var1.createInverse();
      } catch (NoninvertibleTransformException var3) {
         this.invAffine = null;
      }

   }

   public AffineTransform getAffine() {
      return (AffineTransform)this.affine.clone();
   }

   public boolean paintRable(Graphics2D var1) {
      AffineTransform var2 = var1.getTransform();
      var1.transform(this.getAffine());
      GraphicsUtil.drawImage(var1, (RenderableImage)this.getSource());
      var1.setTransform(var2);
      return true;
   }

   public RenderedImage createRendering(RenderContext var1) {
      if (this.invAffine == null) {
         return null;
      } else {
         RenderingHints var2 = var1.getRenderingHints();
         if (var2 == null) {
            var2 = new RenderingHints((Map)null);
         }

         Shape var3 = var1.getAreaOfInterest();
         if (var3 != null) {
            var3 = this.invAffine.createTransformedShape(var3);
         }

         AffineTransform var4 = var1.getTransform();
         var4.concatenate(this.affine);
         return this.getSource().createRendering(new RenderContext(var4, var3, var2));
      }
   }

   public Shape getDependencyRegion(int var1, Rectangle2D var2) {
      if (var1 != 0) {
         throw new IndexOutOfBoundsException("Affine only has one input");
      } else {
         return this.invAffine == null ? null : this.invAffine.createTransformedShape(var2);
      }
   }

   public Shape getDirtyRegion(int var1, Rectangle2D var2) {
      if (var1 != 0) {
         throw new IndexOutOfBoundsException("Affine only has one input");
      } else {
         return this.affine.createTransformedShape(var2);
      }
   }
}
