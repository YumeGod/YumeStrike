package org.apache.batik.ext.awt.image.renderable;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.color.ICCColorSpaceExt;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.ProfileRed;

public class ProfileRable extends AbstractRable {
   private ICCColorSpaceExt colorSpace;

   public ProfileRable(Filter var1, ICCColorSpaceExt var2) {
      super(var1);
      this.colorSpace = var2;
   }

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public void setColorSpace(ICCColorSpaceExt var1) {
      this.touch();
      this.colorSpace = var1;
   }

   public ICCColorSpaceExt getColorSpace() {
      return this.colorSpace;
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderedImage var2 = this.getSource().createRendering(var1);
      if (var2 == null) {
         return null;
      } else {
         CachableRed var3 = GraphicsUtil.wrap(var2);
         return new ProfileRed(var3, this.colorSpace);
      }
   }
}
