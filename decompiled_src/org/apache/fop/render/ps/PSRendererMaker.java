package org.apache.fop.render.ps;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class PSRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"application/postscript"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new PSRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new PSRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
