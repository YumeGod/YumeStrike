package org.apache.fop.render.pdf;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererConfigurator;

public class PDFRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"application/pdf"};

   public Renderer makeRenderer(FOUserAgent userAgent) {
      return new PDFRenderer();
   }

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return new PDFRendererConfigurator(userAgent);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
