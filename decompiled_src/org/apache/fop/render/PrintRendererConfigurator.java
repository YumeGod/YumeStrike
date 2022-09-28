package org.apache.fop.render;

import java.util.ArrayList;
import java.util.List;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.fonts.CustomFontCollection;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontEventAdapter;
import org.apache.fop.fonts.FontEventListener;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontInfoConfigurator;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.fonts.base14.Base14FontCollection;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;

public class PrintRendererConfigurator extends AbstractRendererConfigurator implements RendererConfigurator, IFDocumentHandlerConfigurator {
   protected static Log log;

   public PrintRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = this.getRendererConfig(renderer);
      if (cfg == null) {
         log.trace("no configuration found for " + renderer);
      } else {
         PrintRenderer printRenderer = (PrintRenderer)renderer;
         FontResolver fontResolver = printRenderer.getFontResolver();
         FontEventListener listener = new FontEventAdapter(renderer.getUserAgent().getEventBroadcaster());
         List embedFontInfoList = this.buildFontList(cfg, fontResolver, listener);
         printRenderer.addFontList(embedFontInfoList);
      }
   }

   protected List buildFontList(Configuration cfg, FontResolver fontResolver, FontEventListener listener) throws FOPException {
      FopFactory factory = this.userAgent.getFactory();
      FontManager fontManager = factory.getFontManager();
      if (fontResolver == null) {
         fontResolver = FontManager.createMinimalFontResolver();
      }

      boolean strict = factory.validateUserConfigStrictly();
      FontInfoConfigurator fontInfoConfigurator = new FontInfoConfigurator(cfg, fontManager, fontResolver, listener, strict);
      List fontInfoList = new ArrayList();
      fontInfoConfigurator.configure(fontInfoList);
      return fontInfoList;
   }

   public void configure(IFDocumentHandler documentHandler) throws FOPException {
   }

   public void setupFontInfo(IFDocumentHandler documentHandler, FontInfo fontInfo) throws FOPException {
      FontManager fontManager = this.userAgent.getFactory().getFontManager();
      List fontCollections = new ArrayList();
      fontCollections.add(new Base14FontCollection(fontManager.isBase14KerningEnabled()));
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         FontResolver fontResolver = new DefaultFontResolver(this.userAgent);
         FontEventListener listener = new FontEventAdapter(this.userAgent.getEventBroadcaster());
         List fontList = this.buildFontList(cfg, fontResolver, listener);
         fontCollections.add(new CustomFontCollection(fontResolver, fontList));
      }

      fontManager.setup(fontInfo, (FontCollection[])fontCollections.toArray(new FontCollection[fontCollections.size()]));
      documentHandler.setFontInfo(fontInfo);
   }

   static {
      log = LogFactory.getLog(PrintRendererConfigurator.class);
   }
}
