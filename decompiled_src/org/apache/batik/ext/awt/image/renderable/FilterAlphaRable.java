package org.apache.batik.ext.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.ColorSpaceHintKey;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.FilterAlphaRed;
import org.apache.batik.ext.awt.image.rendered.RenderedImageCachableRed;

public class FilterAlphaRable extends AbstractRable {
   public FilterAlphaRable(Filter var1) {
      super((Filter)var1, (Map)null);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
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

      var3.put(RenderingHintsKeyExt.KEY_COLORSPACE, ColorSpaceHintKey.VALUE_COLORSPACE_ALPHA);
      RenderedImage var5 = this.getSource().createRendering(new RenderContext(var2, (Shape)var4, var3));
      if (var5 == null) {
         return null;
      } else {
         CachableRed var6 = RenderedImageCachableRed.wrap(var5);
         Object var7 = var6.getProperty("org.apache.batik.gvt.filter.Colorspace");
         return (RenderedImage)(var7 == ColorSpaceHintKey.VALUE_COLORSPACE_ALPHA ? var6 : new FilterAlphaRed(var6));
      }
   }
}
