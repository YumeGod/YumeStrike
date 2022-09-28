package org.apache.fop.render.afp;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class AFPRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"application/x-afp", "application/vnd.ibm.modcap"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new AFPRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new AFPRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
