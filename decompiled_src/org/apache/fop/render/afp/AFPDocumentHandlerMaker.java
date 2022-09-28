package org.apache.fop.render.afp;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.intermediate.AbstractIFDocumentHandlerMaker;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandler;

public class AFPDocumentHandlerMaker extends AbstractIFDocumentHandlerMaker {
   private static final String[] MIMES = new String[]{"application/x-afp", "application/vnd.ibm.modcap"};

   public IFDocumentHandler makeIFDocumentHandler(FOUserAgent ua) {
      AFPDocumentHandler handler = new AFPDocumentHandler();
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
