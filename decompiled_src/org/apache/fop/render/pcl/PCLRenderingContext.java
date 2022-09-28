package org.apache.fop.render.pcl;

import java.awt.geom.Point2D;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRenderingContext;
import org.apache.xmlgraphics.java2d.GraphicContext;

public abstract class PCLRenderingContext extends AbstractRenderingContext {
   private PCLGenerator generator;
   private PCLRenderingUtil pclUtil;
   private boolean sourceTransparency = false;

   public PCLRenderingContext(FOUserAgent userAgent, PCLGenerator generator, PCLRenderingUtil pclUtil) {
      super(userAgent);
      this.generator = generator;
      this.pclUtil = pclUtil;
   }

   public String getMimeType() {
      return "application/x-pcl";
   }

   public PCLGenerator getPCLGenerator() {
      return this.generator;
   }

   public PCLRenderingUtil getPCLUtil() {
      return this.pclUtil;
   }

   public boolean isSourceTransparencyEnabled() {
      return this.sourceTransparency;
   }

   public void setSourceTransparencyEnabled(boolean value) {
      this.sourceTransparency = value;
   }

   public abstract Point2D transformedPoint(int var1, int var2);

   public abstract GraphicContext getGraphicContext();
}
