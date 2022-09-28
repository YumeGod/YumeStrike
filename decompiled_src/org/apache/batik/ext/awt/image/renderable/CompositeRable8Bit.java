package org.apache.batik.ext.awt.image.renderable;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.image.CompositeRule;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.SVGComposite;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.CompositeRed;
import org.apache.batik.ext.awt.image.rendered.FloodRed;

public class CompositeRable8Bit extends AbstractColorInterpolationRable implements CompositeRable, PaintRable {
   protected CompositeRule rule;

   public CompositeRable8Bit(List var1, CompositeRule var2, boolean var3) {
      super(var1);
      this.setColorSpaceLinear(var3);
      this.rule = var2;
   }

   public void setSources(List var1) {
      this.init(var1, (Map)null);
   }

   public void setCompositeRule(CompositeRule var1) {
      this.touch();
      this.rule = var1;
   }

   public CompositeRule getCompositeRule() {
      return this.rule;
   }

   public boolean paintRable(Graphics2D var1) {
      Composite var2 = var1.getComposite();
      if (!SVGComposite.OVER.equals(var2)) {
         return false;
      } else if (this.getCompositeRule() != CompositeRule.OVER) {
         return false;
      } else {
         ColorSpace var3 = this.getOperationColorSpace();
         ColorSpace var4 = GraphicsUtil.getDestinationColorSpace(var1);
         if (var4 != null && var4 == var3) {
            Iterator var5 = this.getSources().iterator();

            while(var5.hasNext()) {
               GraphicsUtil.drawImage(var1, (RenderableImage)((Filter)var5.next()));
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public RenderedImage createRendering(RenderContext var1) {
      if (this.srcs.size() == 0) {
         return null;
      } else {
         RenderingHints var2 = var1.getRenderingHints();
         if (var2 == null) {
            var2 = new RenderingHints((Map)null);
         }

         AffineTransform var3 = var1.getTransform();
         Shape var4 = var1.getAreaOfInterest();
         Rectangle2D var5;
         if (var4 == null) {
            var5 = this.getBounds2D();
         } else {
            var5 = var4.getBounds2D();
            Rectangle2D var6 = this.getBounds2D();
            if (!var6.intersects(var5)) {
               return null;
            }

            Rectangle2D.intersect(var5, var6, var5);
         }

         Rectangle var12 = var3.createTransformedShape(var5).getBounds();
         var1 = new RenderContext(var3, var5, var2);
         ArrayList var7 = new ArrayList();
         Iterator var8 = this.getSources().iterator();

         while(var8.hasNext()) {
            Filter var9 = (Filter)var8.next();
            RenderedImage var10 = var9.createRendering(var1);
            if (var10 != null) {
               CachableRed var11 = this.convertSourceCS(var10);
               var7.add(var11);
            } else {
               switch (this.rule.getRule()) {
                  case 2:
                     return null;
                  case 3:
                     var7.clear();
                  case 4:
                  case 5:
                  default:
                     break;
                  case 6:
                     var7.add(new FloodRed(var12));
               }
            }
         }

         if (var7.size() == 0) {
            return null;
         } else {
            CompositeRed var13 = new CompositeRed(var7, this.rule);
            return var13;
         }
      }
   }
}
