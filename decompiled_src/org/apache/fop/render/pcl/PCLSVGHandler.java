package org.apache.fop.render.pcl;

import org.apache.fop.render.AbstractGenericSVGHandler;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;

public class PCLSVGHandler extends AbstractGenericSVGHandler {
   public boolean supportsRenderer(Renderer renderer) {
      return renderer instanceof PCLRenderer;
   }

   protected void updateRendererContext(RendererContext context) {
      context.setProperty("color-canvas", Boolean.TRUE);
   }
}
