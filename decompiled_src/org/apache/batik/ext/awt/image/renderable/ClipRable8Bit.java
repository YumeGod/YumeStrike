package org.apache.batik.ext.awt.image.renderable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.BufferedImageCachableRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.MultiplyAlphaRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;
import org.apache.batik.ext.awt.image.rendered.RenderedImageCachableRed;

public class ClipRable8Bit extends AbstractRable implements ClipRable {
   protected boolean useAA;
   protected Shape clipPath;

   public ClipRable8Bit(Filter var1, Shape var2) {
      super((Filter)var1, (Map)null);
      this.setClipPath(var2);
      this.setUseAntialiasedClip(false);
   }

   public ClipRable8Bit(Filter var1, Shape var2, boolean var3) {
      super((Filter)var1, (Map)null);
      this.setClipPath(var2);
      this.setUseAntialiasedClip(var3);
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public void setUseAntialiasedClip(boolean var1) {
      this.touch();
      this.useAA = var1;
   }

   public boolean getUseAntialiasedClip() {
      return this.useAA;
   }

   public void setClipPath(Shape var1) {
      this.touch();
      this.clipPath = var1;
   }

   public Shape getClipPath() {
      return this.clipPath;
   }

   public Rectangle2D getBounds2D() {
      return this.getSource().getBounds2D();
   }

   public RenderedImage createRendering(RenderContext var1) {
      AffineTransform var2 = var1.getTransform();
      RenderingHints var3 = var1.getRenderingHints();
      if (var3 == null) {
         var3 = new RenderingHints((Map)null);
      }

      Object var4 = var1.getAreaOfInterest();
      if (var4 == null) {
         var4 = this.getBounds2D();
      }

      Rectangle2D var5 = this.getBounds2D();
      Rectangle2D var6 = this.clipPath.getBounds2D();
      Rectangle2D var7 = ((Shape)var4).getBounds2D();
      if (!var5.intersects(var6)) {
         return null;
      } else {
         Rectangle2D.intersect(var5, var6, var5);
         if (!var5.intersects(var7)) {
            return null;
         } else {
            Rectangle2D.intersect(var5, ((Shape)var4).getBounds2D(), var5);
            Rectangle var8 = var2.createTransformedShape(var5).getBounds();
            if (var8.width != 0 && var8.height != 0) {
               BufferedImage var9 = new BufferedImage(var8.width, var8.height, 10);
               Shape var10 = var2.createTransformedShape(this.getClipPath());
               Rectangle var11 = var2.createTransformedShape((Shape)var4).getBounds();
               Graphics2D var12 = GraphicsUtil.createGraphics(var9, var3);
               var12.translate(-var8.x, -var8.y);
               var12.setPaint(Color.white);
               var12.fill(var10);
               var12.dispose();
               RenderedImage var13 = this.getSource().createRendering(new RenderContext(var2, var5, var3));
               CachableRed var14 = RenderedImageCachableRed.wrap(var13);
               BufferedImageCachableRed var15 = new BufferedImageCachableRed(var9, var8.x, var8.y);
               MultiplyAlphaRed var16 = new MultiplyAlphaRed(var14, var15);
               PadRed var17 = new PadRed(var16, var11, PadMode.ZERO_PAD, var3);
               return var17;
            } else {
               return null;
            }
         }
      }
   }
}
