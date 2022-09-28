package org.apache.batik.ext.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.image.ARGBChannel;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.DisplacementMapRed;

public class DisplacementMapRable8Bit extends AbstractColorInterpolationRable implements DisplacementMapRable {
   private double scale;
   private ARGBChannel xChannelSelector;
   private ARGBChannel yChannelSelector;

   public DisplacementMapRable8Bit(List var1, double var2, ARGBChannel var4, ARGBChannel var5) {
      this.setSources(var1);
      this.setScale(var2);
      this.setXChannelSelector(var4);
      this.setYChannelSelector(var5);
   }

   public Rectangle2D getBounds2D() {
      return ((Filter)this.getSources().get(0)).getBounds2D();
   }

   public void setScale(double var1) {
      this.touch();
      this.scale = var1;
   }

   public double getScale() {
      return this.scale;
   }

   public void setSources(List var1) {
      if (var1.size() != 2) {
         throw new IllegalArgumentException();
      } else {
         this.init(var1, (Map)null);
      }
   }

   public void setXChannelSelector(ARGBChannel var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.xChannelSelector = var1;
      }
   }

   public ARGBChannel getXChannelSelector() {
      return this.xChannelSelector;
   }

   public void setYChannelSelector(ARGBChannel var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.yChannelSelector = var1;
      }
   }

   public ARGBChannel getYChannelSelector() {
      return this.yChannelSelector;
   }

   public RenderedImage createRendering(RenderContext var1) {
      Filter var2 = (Filter)this.getSources().get(0);
      Filter var3 = (Filter)this.getSources().get(1);
      RenderingHints var4 = var1.getRenderingHints();
      if (var4 == null) {
         var4 = new RenderingHints((Map)null);
      }

      AffineTransform var5 = var1.getTransform();
      double var6 = var5.getScaleX();
      double var8 = var5.getScaleY();
      double var10 = var5.getShearX();
      double var12 = var5.getShearY();
      double var14 = var5.getTranslateX();
      double var16 = var5.getTranslateY();
      double var18 = Math.sqrt(var6 * var6 + var12 * var12);
      double var20 = Math.sqrt(var8 * var8 + var10 * var10);
      float var22 = (float)(this.scale * var18);
      float var23 = (float)(this.scale * var20);
      if (var22 == 0.0F && var23 == 0.0F) {
         return var2.createRendering(var1);
      } else {
         AffineTransform var24 = AffineTransform.getScaleInstance(var18, var20);
         Object var25 = var1.getAreaOfInterest();
         if (var25 == null) {
            var25 = this.getBounds2D();
         }

         Rectangle2D var26 = ((Shape)var25).getBounds2D();
         RenderContext var27 = new RenderContext(var24, var26, var4);
         RenderedImage var28 = var3.createRendering(var27);
         if (var28 == null) {
            return null;
         } else {
            Rectangle2D.Double var33 = new Rectangle2D.Double(var26.getX() - this.scale / 2.0, var26.getY() - this.scale / 2.0, var26.getWidth() + this.scale, var26.getHeight() + this.scale);
            Rectangle2D var29 = var2.getBounds2D();
            if (!var33.intersects(var29)) {
               return null;
            } else {
               var26 = var33.createIntersection(var29);
               var27 = new RenderContext(var24, var26, var4);
               RenderedImage var30 = var2.createRendering(var27);
               if (var30 == null) {
                  return null;
               } else {
                  CachableRed var34 = this.convertSourceCS(var28);
                  Object var31 = new DisplacementMapRed(GraphicsUtil.wrap(var30), GraphicsUtil.wrap(var34), this.xChannelSelector, this.yChannelSelector, var22, var23, var4);
                  AffineTransform var32 = new AffineTransform(var6 / var18, var12 / var18, var10 / var20, var8 / var20, var14, var16);
                  if (!var32.isIdentity()) {
                     var31 = new AffineRed((CachableRed)var31, var32, var4);
                  }

                  return (RenderedImage)var31;
               }
            }
         }
      }
   }

   public Shape getDependencyRegion(int var1, Rectangle2D var2) {
      return super.getDependencyRegion(var1, var2);
   }

   public Shape getDirtyRegion(int var1, Rectangle2D var2) {
      return super.getDirtyRegion(var1, var2);
   }
}
