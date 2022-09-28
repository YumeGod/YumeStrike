package org.apache.fop.render.java2d;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;

public class Java2DRendererConfigurator extends PrintRendererConfigurator {
   public Java2DRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = super.getRendererConfig(renderer);
      if (cfg != null) {
         Java2DRenderer java2dRenderer = (Java2DRenderer)renderer;
         String value = cfg.getChild("transparent-page-background", true).getValue((String)null);
         if (value != null) {
            java2dRenderer.setTransparentPageBackground("true".equalsIgnoreCase(value));
         }
      }

      super.configure(renderer);
   }
}
