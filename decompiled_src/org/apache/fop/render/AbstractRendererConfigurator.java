package org.apache.fop.render;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.apps.FOUserAgent;

public abstract class AbstractRendererConfigurator extends AbstractConfigurator {
   private static final String TYPE = "renderer";

   public AbstractRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   protected Configuration getRendererConfig(Renderer renderer) {
      return super.getConfig(renderer.getMimeType());
   }

   protected Configuration getRendererConfig(String mimeType) {
      return super.getConfig(mimeType);
   }

   public String getType() {
      return "renderer";
   }
}
