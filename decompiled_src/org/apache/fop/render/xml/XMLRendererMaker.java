package org.apache.fop.render.xml;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class XMLRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"application/X-fop-areatree"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new XMLRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new PrintRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
