package org.apache.fop.render.txt;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class TXTRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"text/plain"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new TXTRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new TXTRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
