package org.apache.fop.render;

import org.apache.fop.apps.FOUserAgent;

public abstract class AbstractRendererMaker {
   public abstract Renderer makeRenderer(FOUserAgent var1);

   public abstract boolean needsOutputStream();

   public abstract String[] getSupportedMimeTypes();

   public RendererConfigurator getConfigurator(FOUserAgent userAgent) {
      return null;
   }

   public boolean isMimeTypeSupported(String mimeType) {
      String[] mimes = this.getSupportedMimeTypes();

      for(int i = 0; i < mimes.length; ++i) {
         if (mimes[i].equals(mimeType)) {
            return true;
         }
      }

      return false;
   }
}
