package org.apache.fop.render.bitmap;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.intermediate.AbstractIFDocumentHandlerMaker;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandler;

public class PNGDocumentHandlerMaker extends AbstractIFDocumentHandlerMaker {
   private static final String[] MIMES = new String[]{"image/png"};

   public IFDocumentHandler makeIFDocumentHandler(FOUserAgent ua) {
      PNGDocumentHandler handler = new PNGDocumentHandler();
      handler.setContext(new IFContext(ua));
      return handler;
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
