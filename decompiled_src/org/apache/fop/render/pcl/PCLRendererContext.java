package org.apache.fop.render.pcl;

import org.apache.fop.render.ImageHandlerUtil;
import org.apache.fop.render.RendererContext;

public class PCLRendererContext extends RendererContext.RendererContextWrapper {
   public static PCLRendererContext wrapRendererContext(RendererContext context) {
      PCLRendererContext pcli = new PCLRendererContext(context);
      return pcli;
   }

   public PCLRendererContext(RendererContext context) {
      super(context);
   }

   public boolean paintAsBitmap() {
      return ImageHandlerUtil.isConversionModeBitmap(this.getForeignAttributes());
   }

   public boolean isClippingDisabled() {
      return this.getForeignAttributes() != null && "true".equalsIgnoreCase((String)this.getForeignAttributes().get(PCLConstants.DISABLE_CLIPPING));
   }

   public boolean isSourceTransparency() {
      return this.getForeignAttributes() != null && "true".equalsIgnoreCase((String)this.getForeignAttributes().get(PCLConstants.SRC_TRANSPARENCY));
   }

   public boolean isColorCanvas() {
      Boolean prop = (Boolean)this.context.getProperty("color-canvas");
      return Boolean.TRUE.equals(prop) || this.getForeignAttributes() != null && "true".equalsIgnoreCase((String)this.getForeignAttributes().get(PCLConstants.COLOR_CANVAS));
   }
}
