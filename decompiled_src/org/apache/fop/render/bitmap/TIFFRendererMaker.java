package org.apache.fop.render.bitmap;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class TIFFRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"image/tiff"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new TIFFRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new TIFFRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
