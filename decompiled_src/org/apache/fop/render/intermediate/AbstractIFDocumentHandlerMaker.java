package org.apache.fop.render.intermediate;

import org.apache.fop.apps.FOUserAgent;

public abstract class AbstractIFDocumentHandlerMaker {
   public abstract IFDocumentHandler makeIFDocumentHandler(FOUserAgent var1);

   public abstract boolean needsOutputStream();

   public abstract String[] getSupportedMimeTypes();

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
