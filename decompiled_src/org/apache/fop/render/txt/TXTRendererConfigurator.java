package org.apache.fop.render.txt;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;

public class TXTRendererConfigurator extends PrintRendererConfigurator {
   public TXTRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = super.getRendererConfig(renderer);
      if (cfg != null) {
         TXTRenderer txtRenderer = (TXTRenderer)renderer;
         txtRenderer.setEncoding(cfg.getChild("encoding", true).getValue((String)null));
      }

   }
}
