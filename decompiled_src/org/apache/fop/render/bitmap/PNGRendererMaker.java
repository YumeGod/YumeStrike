package org.apache.fop.render.bitmap;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;
import org.apache.fop.render.java2d.Java2DRendererConfigurator;

public class PNGRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"image/png"};

   public Renderer makeRenderer(FOUserAgent ua) {
      return new PNGRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new Java2DRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
