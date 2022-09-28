package org.apache.batik.gvt.filter;

import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.AbstractRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.FilterAsAlphaRable;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.MultiplyAlphaRed;
import org.apache.batik.ext.awt.image.rendered.RenderedImageCachableRed;
import org.apache.batik.gvt.GraphicsNode;

public class MaskRable8Bit extends AbstractRable implements Mask {
   protected GraphicsNode mask;
   protected Rectangle2D filterRegion;

   public MaskRable8Bit(Filter var1, GraphicsNode var2, Rectangle2D var3) {
      super((Filter)var1, (Map)null);
      this.setMaskNode(var2);
      this.setFilterRegion(var3);
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public Rectangle2D getFilterRegion() {
      return (Rectangle2D)this.filterRegion.clone();
   }

   public void setFilterRegion(Rectangle2D var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.filterRegion = var1;
      }
   }

   public void setMaskNode(GraphicsNode var1) {
      this.touch();
      this.mask = var1;
   }

   public GraphicsNode getMaskNode() {
      return this.mask;
   }

   public Rectangle2D getBounds2D() {
      return (Rectangle2D)this.filterRegion.clone();
   }

   public RenderedImage createRendering(RenderContext var1) {
      Filter var2 = this.getMaskNode().getGraphicsNodeRable(true);
      PadRable8Bit var3 = new PadRable8Bit(var2, this.getBounds2D(), PadMode.ZERO_PAD);
      FilterAsAlphaRable var9 = new FilterAsAlphaRable(var3);
      RenderedImage var4 = var9.createRendering(var1);
      if (var4 == null) {
         return null;
      } else {
         CachableRed var5 = RenderedImageCachableRed.wrap(var4);
         PadRable8Bit var6 = new PadRable8Bit(this.getSource(), this.getBounds2D(), PadMode.ZERO_PAD);
         var4 = var6.createRendering(var1);
         if (var4 == null) {
            return null;
         } else {
            CachableRed var7 = GraphicsUtil.wrap(var4);
            var7 = GraphicsUtil.convertToLsRGB(var7);
            MultiplyAlphaRed var8 = new MultiplyAlphaRed(var7, var5);
            return var8;
         }
      }
   }
}
