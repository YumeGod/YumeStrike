package org.apache.fop.render.bitmap;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.intermediate.AbstractIFDocumentHandlerMaker;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandler;

public class TIFFDocumentHandlerMaker extends AbstractIFDocumentHandlerMaker {
   private static final String[] MIMES = new String[]{"image/tiff"};

   public IFDocumentHandler makeIFDocumentHandler(FOUserAgent ua) {
      TIFFDocumentHandler handler = new TIFFDocumentHandler();
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
