package org.apache.fop.render.pcl;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontEventAdapter;
import org.apache.fop.fonts.FontEventListener;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.render.DefaultFontResolver;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.java2d.Base14FontCollection;
import org.apache.fop.render.java2d.ConfiguredFontCollection;
import org.apache.fop.render.java2d.InstalledFontCollection;
import org.apache.fop.render.java2d.Java2DFontMetrics;

public class PCLRendererConfigurator extends PrintRendererConfigurator implements IFDocumentHandlerConfigurator {
   public PCLRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = super.getRendererConfig(renderer);
      if (cfg != null) {
         PCLRenderer pclRenderer = (PCLRenderer)renderer;
         PCLRenderingUtil pclUtil = pclRenderer.getPCLUtil();
         this.configure(cfg, pclUtil);
      }

      super.configure(renderer);
   }

   private void configure(Configuration cfg, PCLRenderingUtil pclUtil) throws FOPException {
      String rendering = cfg.getChild("rendering").getValue((String)null);
      if (rendering != null) {
         try {
            pclUtil.setRenderingMode(PCLRenderingMode.valueOf(rendering));
         } catch (IllegalArgumentException var5) {
            throw new FOPException("Valid values for 'rendering' are 'quality', 'speed' and 'bitmap'. Value found: " + rendering);
         }
      }

      String textRendering = cfg.getChild("text-rendering").getValue((String)null);
      if ("bitmap".equalsIgnoreCase(textRendering)) {
         pclUtil.setAllTextAsBitmaps(true);
      } else if ("auto".equalsIgnoreCase(textRendering)) {
         pclUtil.setAllTextAsBitmaps(false);
      } else if (textRendering != null) {
         throw new FOPException("Valid values for 'text-rendering' are 'auto' and 'bitmap'. Value found: " + textRendering);
      }

      pclUtil.setPJLDisabled(cfg.getChild("disable-pjl").getValueAsBoolean(false));
   }

   public void configure(IFDocumentHandler documentHandler) throws FOPException {
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         PCLDocumentHandler pclDocumentHandler = (PCLDocumentHandler)documentHandler;
         PCLRenderingUtil pclUtil = pclDocumentHandler.getPCLUtil();
         this.configure(cfg, pclUtil);
      }

   }

   public void setupFontInfo(IFDocumentHandler documentHandler, FontInfo fontInfo) throws FOPException {
      FontManager fontManager = this.userAgent.getFactory().getFontManager();
      Graphics2D graphics2D = Java2DFontMetrics.createFontMetricsGraphics2D();
      List fontCollections = new ArrayList();
      fontCollections.add(new Base14FontCollection(graphics2D));
      fontCollections.add(new InstalledFontCollection(graphics2D));
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         FontResolver fontResolver = new DefaultFontResolver(this.userAgent);
         FontEventListener listener = new FontEventAdapter(this.userAgent.getEventBroadcaster());
         List fontList = this.buildFontList(cfg, fontResolver, listener);
         fontCollections.add(new ConfiguredFontCollection(fontResolver, fontList));
      }

      fontManager.setup(fontInfo, (FontCollection[])fontCollections.toArray(new FontCollection[fontCollections.size()]));
      documentHandler.setFontInfo(fontInfo);
   }
}
