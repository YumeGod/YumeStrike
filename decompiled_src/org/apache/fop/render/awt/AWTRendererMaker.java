package org.apache.fop.render.awt;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.AbstractRendererMaker;
import org.apache.fop.render.Renderer;

public class AWTRendererMaker extends AbstractRendererMaker {
   private static final String[] MIMES = new String[]{"application/X-fop-awt-preview"};

   public Renderer makeRenderer(FOUserAgent ua) {
      return new AWTRenderer();
   }

   public boolean needsOutputStream() {
      return false;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
