package org.apache.fop.render.print;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class PrintRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"application/X-fop-print"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new PrintRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new PrintRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return false;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
