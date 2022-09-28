package org.apache.fop.render.bitmap;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.intermediate.IFDocumentHandler;

public class TIFFRendererConfigurator extends BitmapRendererConfigurator {
   public TIFFRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = super.getRendererConfig(renderer);
      if (cfg != null) {
         TIFFRenderer tiffRenderer = (TIFFRenderer)renderer;
         String name = cfg.getChild("compression").getValue("PackBits");
         tiffRenderer.setBufferedImageType(this.getBufferedImageTypeFor(name));
         if (!"NONE".equalsIgnoreCase(name)) {
            tiffRenderer.getWriterParams().setCompressionMethod(name);
         }

         if (log.isInfoEnabled()) {
            log.info("TIFF compression set to " + name);
         }
      }

      super.configure(renderer);
   }

   private int getBufferedImageTypeFor(String compressionName) {
      if (compressionName.equalsIgnoreCase("CCITT T.6")) {
         return 12;
      } else {
         return compressionName.equalsIgnoreCase("CCITT T.4") ? 12 : 2;
      }
   }

   public void configure(IFDocumentHandler documentHandler) throws FOPException {
      super.configure(documentHandler);
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         TIFFDocumentHandler tiffHandler = (TIFFDocumentHandler)documentHandler;
         BitmapRenderingSettings settings = tiffHandler.getSettings();
         String name = cfg.getChild("compression").getValue("PackBits");
         settings.setBufferedImageType(this.getBufferedImageTypeFor(name));
         if (!"NONE".equalsIgnoreCase(name)) {
            settings.getWriterParams().setCompressionMethod(name);
         }

         if (log.isInfoEnabled()) {
            log.info("TIFF compression set to " + name);
         }
      }

   }
}
