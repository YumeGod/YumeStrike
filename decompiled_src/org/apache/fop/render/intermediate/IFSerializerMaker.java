package org.apache.fop.render.intermediate;

import org.apache.fop.apps.FOUserAgent;

public class IFSerializerMaker extends AbstractIFDocumentHandlerMaker {
   public IFDocumentHandler makeIFDocumentHandler(FOUserAgent ua) {
      IFSerializer handler = new IFSerializer();
      handler.setContext(new IFContext(ua));
      return handler;
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return new String[]{"application/X-fop-intermediate-format"};
   }
}
