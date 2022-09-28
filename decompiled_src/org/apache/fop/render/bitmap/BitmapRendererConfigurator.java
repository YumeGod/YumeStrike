package org.apache.fop.render.bitmap;

import java.awt.Color;
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
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.java2d.Base14FontCollection;
import org.apache.fop.render.java2d.ConfiguredFontCollection;
import org.apache.fop.render.java2d.InstalledFontCollection;
import org.apache.fop.render.java2d.Java2DFontMetrics;
import org.apache.fop.render.java2d.Java2DRendererConfigurator;
import org.apache.fop.util.ColorUtil;

public class BitmapRendererConfigurator extends Java2DRendererConfigurator implements IFDocumentHandlerConfigurator {
   public BitmapRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(IFDocumentHandler documentHandler) throws FOPException {
      super.configure(documentHandler);
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         AbstractBitmapDocumentHandler bitmapHandler = (AbstractBitmapDocumentHandler)documentHandler;
         BitmapRenderingSettings settings = bitmapHandler.getSettings();
         boolean transparent = cfg.getChild("transparent-page-background").getValueAsBoolean(settings.hasTransparentPageBackground());
         if (transparent) {
            settings.setPageBackgroundColor((Color)null);
         } else {
            String background = cfg.getChild("background-color").getValue((String)null);
            if (background != null) {
               settings.setPageBackgroundColor(ColorUtil.parseColorString(this.userAgent, background));
            }
         }

         boolean antiAliasing = cfg.getChild("anti-aliasing").getValueAsBoolean(settings.isAntiAliasingEnabled());
         settings.setAntiAliasing(antiAliasing);
         String optimization = cfg.getChild("rendering").getValue((String)null);
         if ("quality".equalsIgnoreCase(optimization)) {
            settings.setQualityRendering(true);
         } else if ("speed".equalsIgnoreCase(optimization)) {
            settings.setQualityRendering(false);
         }

         String color = cfg.getChild("color-mode").getValue((String)null);
         if (color != null) {
            if ("rgba".equalsIgnoreCase(color)) {
               settings.setBufferedImageType(2);
            } else if ("rgb".equalsIgnoreCase(color)) {
               settings.setBufferedImageType(1);
            } else if ("gray".equalsIgnoreCase(color)) {
               settings.setBufferedImageType(10);
            } else if ("binary".equalsIgnoreCase(color)) {
               settings.setBufferedImageType(12);
            } else {
               if (!"bi-level".equalsIgnoreCase(color)) {
                  throw new FOPException("Invalid value for color-mode: " + color);
               }

               settings.setBufferedImageType(12);
            }
         }
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
