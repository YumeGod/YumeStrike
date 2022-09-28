package org.apache.batik.ext.awt.image.renderable;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.TranslateRed;

public class RedRable extends AbstractRable {
   CachableRed src;

   public RedRable(CachableRed var1) {
      super((Filter)null);
      this.src = var1;
   }

   public CachableRed getSource() {
      return this.src;
   }

   public Object getProperty(String var1) {
      return this.src.getProperty(var1);
   }

   public String[] getPropertyNames() {
      return this.src.getPropertyNames();
   }

   public Rectangle2D getBounds2D() {
      return this.getSource().getBounds();
   }

   public RenderedImage createDefaultRendering() {
      return this.getSource();
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderingHints var2 = var1.getRenderingHints();
      if (var2 == null) {
         var2 = new RenderingHints((Map)null);
      }

      Shape var3 = var1.getAreaOfInterest();
      Rectangle var4;
      if (var3 != null) {
         var4 = var3.getBounds();
      } else {
         var4 = this.getBounds2D().getBounds();
      }

      AffineTransform var5 = var1.getTransform();
      CachableRed var6 = this.getSource();
      if (!var4.intersects(var6.getBounds())) {
         return null;
      } else if (var5.isIdentity()) {
         return var6;
      } else {
         if (var5.getScaleX() == 1.0 && var5.getScaleY() == 1.0 && var5.getShearX() == 0.0 && var5.getShearY() == 0.0) {
            int var7 = (int)((double)var6.getMinX() + var5.getTranslateX());
            int var8 = (int)((double)var6.getMinY() + var5.getTranslateY());
            double var9 = (double)var7 - ((double)var6.getMinX() + var5.getTranslateX());
            double var11 = (double)var8 - ((double)var6.getMinY() + var5.getTranslateY());
            if (var9 > -1.0E-4 && var9 < 1.0E-4 && var11 > -1.0E-4 && var11 < 1.0E-4) {
               return new TranslateRed(var6, var7, var8);
            }
         }

         return new AffineRed(var6, var5, var2);
      }
   }
}
