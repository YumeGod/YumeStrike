package org.apache.batik.ext.awt.image.renderable;

import java.awt.color.ColorSpace;
import java.awt.image.RenderedImage;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.CachableRed;

public abstract class AbstractColorInterpolationRable extends AbstractRable {
   protected boolean csLinear = true;

   protected AbstractColorInterpolationRable() {
   }

   protected AbstractColorInterpolationRable(Filter var1) {
      super(var1);
   }

   protected AbstractColorInterpolationRable(Filter var1, Map var2) {
      super(var1, var2);
   }

   protected AbstractColorInterpolationRable(List var1) {
      super(var1);
   }

   protected AbstractColorInterpolationRable(List var1, Map var2) {
      super(var1, var2);
   }

   public boolean isColorSpaceLinear() {
      return this.csLinear;
   }

   public void setColorSpaceLinear(boolean var1) {
      this.touch();
      this.csLinear = var1;
   }

   public ColorSpace getOperationColorSpace() {
      return this.csLinear ? ColorSpace.getInstance(1004) : ColorSpace.getInstance(1000);
   }

   protected CachableRed convertSourceCS(CachableRed var1) {
      return this.csLinear ? GraphicsUtil.convertToLsRGB(var1) : GraphicsUtil.convertTosRGB(var1);
   }

   protected CachableRed convertSourceCS(RenderedImage var1) {
      return this.convertSourceCS(GraphicsUtil.wrap(var1));
   }
}
