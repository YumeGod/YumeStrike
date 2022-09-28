package org.apache.fop.render.ps;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;

public class PSRendererConfigurator extends PrintRendererConfigurator implements IFDocumentHandlerConfigurator {
   public PSRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = super.getRendererConfig(renderer);
      if (cfg != null) {
         super.configure(renderer);
         PSRenderer psRenderer = (PSRenderer)renderer;
         this.configure(psRenderer.getPSUtil(), cfg);
      }

   }

   private void configure(PSRenderingUtil psUtil, Configuration cfg) {
      psUtil.setAutoRotateLandscape(cfg.getChild("auto-rotate-landscape").getValueAsBoolean(false));
      Configuration child = cfg.getChild("language-level");
      if (child != null) {
         psUtil.setLanguageLevel(child.getValueAsInteger(3));
      }

      child = cfg.getChild("optimize-resources");
      if (child != null) {
         psUtil.setOptimizeResources(child.getValueAsBoolean(false));
      }

      psUtil.setSafeSetPageDevice(cfg.getChild("safe-set-page-device").getValueAsBoolean(false));
      psUtil.setDSCComplianceEnabled(cfg.getChild("dsc-compliant").getValueAsBoolean(true));
   }

   public void configure(IFDocumentHandler documentHandler) throws FOPException {
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         PSDocumentHandler psDocumentHandler = (PSDocumentHandler)documentHandler;
         this.configure(psDocumentHandler.getPSUtil(), cfg);
      }

   }
}
