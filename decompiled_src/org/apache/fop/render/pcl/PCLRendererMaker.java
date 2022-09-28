package org.apache.fop.render.pcl;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class PCLRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"application/x-pcl", "application/vnd.hp-PCL"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new PCLRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new PCLRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
