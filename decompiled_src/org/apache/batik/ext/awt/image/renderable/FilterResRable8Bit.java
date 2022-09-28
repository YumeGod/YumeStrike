package org.apache.batik.ext.awt.image.renderable;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;
import org.apache.batik.ext.awt.image.CompositeRule;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.SVGComposite;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.TileCacheRed;

public class FilterResRable8Bit extends AbstractRable implements FilterResRable, PaintRable {
   private int filterResolutionX = -1;
   private int filterResolutionY = -1;
   Reference resRed = null;
   float resScale = 0.0F;

   public FilterResRable8Bit() {
   }

   public FilterResRable8Bit(Filter var1, int var2, int var3) {
      this.init(var1, (Map)null);
      this.setFilterResolutionX(var2);
      this.setFilterResolutionY(var3);
   }

   public Filter getSource() {
      return (Filter)this.srcs.get(0);
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public int getFilterResolutionX() {
      return this.filterResolutionX;
   }

   public void setFilterResolutionX(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.filterResolutionX = var1;
      }
   }

   public int getFilterResolutionY() {
      return this.filterResolutionY;
   }

   public void setFilterResolutionY(int var1) {
      this.touch();
      this.filterResolutionY = var1;
   }

   public boolean allPaintRable(RenderableImage var1) {
      if (!(var1 instanceof PaintRable)) {
         return false;
      } else {
         Vector var2 = var1.getSources();
         if (var2 == null) {
            return true;
         } else {
            Iterator var3 = var2.iterator();

            RenderableImage var4;
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               var4 = (RenderableImage)var3.next();
            } while(this.allPaintRable(var4));

            return false;
         }
      }
   }

   public boolean distributeAcross(RenderableImage var1, Graphics2D var2) {
      if (var1 instanceof PadRable) {
         PadRable var11 = (PadRable)var1;
         Shape var12 = var2.getClip();
         var2.clip(var11.getPadRect());
         boolean var3 = this.distributeAcross(var11.getSource(), var2);
         var2.setClip(var12);
         return var3;
      } else if (!(var1 instanceof CompositeRable)) {
         return false;
      } else {
         CompositeRable var4 = (CompositeRable)var1;
         if (var4.getCompositeRule() != CompositeRule.OVER) {
            return false;
         } else {
            Vector var5 = var4.getSources();
            if (var5 == null) {
               return true;
            } else {
               ListIterator var6 = var5.listIterator(var5.size());

               while(var6.hasPrevious()) {
                  RenderableImage var7 = (RenderableImage)var6.previous();
                  if (!this.allPaintRable(var7)) {
                     var6.next();
                     break;
                  }
               }

               if (!var6.hasPrevious()) {
                  GraphicsUtil.drawImage(var2, (RenderableImage)var4);
                  return true;
               } else if (!var6.hasNext()) {
                  return false;
               } else {
                  int var13 = var6.nextIndex();
                  CompositeRable8Bit var8 = new CompositeRable8Bit(var5.subList(0, var13), var4.getCompositeRule(), var4.isColorSpaceLinear());
                  FilterResRable8Bit var14 = new FilterResRable8Bit(var8, this.getFilterResolutionX(), this.getFilterResolutionY());
                  GraphicsUtil.drawImage(var2, (RenderableImage)var14);

                  while(var6.hasNext()) {
                     PaintRable var9 = (PaintRable)var6.next();
                     if (!var9.paintRable(var2)) {
                        Filter var10 = (Filter)var9;
                        FilterResRable8Bit var15 = new FilterResRable8Bit(var10, this.getFilterResolutionX(), this.getFilterResolutionY());
                        GraphicsUtil.drawImage(var2, (RenderableImage)var15);
                     }
                  }

                  return true;
               }
            }
         }
      }
   }

   public boolean paintRable(Graphics2D var1) {
      Composite var2 = var1.getComposite();
      if (!SVGComposite.OVER.equals(var2)) {
         return false;
      } else {
         Filter var3 = this.getSource();
         return this.distributeAcross(var3, var1);
      }
   }

   private float getResScale() {
      return this.resScale;
   }

   private RenderedImage getResRed(RenderingHints var1) {
      Rectangle2D var2 = this.getBounds2D();
      double var3 = (double)this.getFilterResolutionX() / var2.getWidth();
      double var5 = (double)this.getFilterResolutionY() / var2.getHeight();
      float var7 = (float)Math.min(var3, var5);
      RenderedImage var8;
      if (var7 == this.resScale) {
         var8 = (RenderedImage)this.resRed.get();
         if (var8 != null) {
            return var8;
         }
      }

      AffineTransform var9 = AffineTransform.getScaleInstance((double)var7, (double)var7);
      RenderContext var10 = new RenderContext(var9, (Shape)null, var1);
      var8 = this.getSource().createRendering(var10);
      TileCacheRed var11 = new TileCacheRed(GraphicsUtil.wrap(var8));
      this.resScale = var7;
      this.resRed = new SoftReference(var11);
      return var11;
   }

   public RenderedImage createRendering(RenderContext var1) {
      AffineTransform var2 = var1.getTransform();
      if (var2 == null) {
         var2 = new AffineTransform();
      }

      RenderingHints var3 = var1.getRenderingHints();
      int var4 = this.getFilterResolutionX();
      int var5 = this.getFilterResolutionY();
      if (var4 > 0 && var5 != 0) {
         Rectangle2D var6 = this.getBounds2D();
         Rectangle var7 = var2.createTransformedShape(var6).getBounds();
         float var8 = 1.0F;
         if (var4 < var7.width) {
            var8 = (float)var4 / (float)var7.width;
         }

         float var9 = 1.0F;
         if (var5 < 0) {
            var9 = var8;
         } else if (var5 < var7.height) {
            var9 = (float)var5 / (float)var7.height;
         }

         if (var8 >= 1.0F && var9 >= 1.0F) {
            return this.getSource().createRendering(var1);
         } else {
            RenderedImage var10 = this.getResRed(var3);
            float var11 = this.getResScale();
            AffineTransform var12 = new AffineTransform(var2.getScaleX() / (double)var11, var2.getShearY() / (double)var11, var2.getShearX() / (double)var11, var2.getScaleY() / (double)var11, var2.getTranslateX(), var2.getTranslateY());
            return new AffineRed(GraphicsUtil.wrap(var10), var12, var3);
         }
      } else {
         return null;
      }
   }
}
