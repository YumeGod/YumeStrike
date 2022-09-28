package org.apache.xerces.jaxp.validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

final class DraconianErrorHandler implements ErrorHandler {
   private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();

   private DraconianErrorHandler() {
   }

   public static DraconianErrorHandler getInstance() {
      return ERROR_HANDLER_INSTANCE;
   }

   public void warning(SAXParseException var1) throws SAXException {
   }

   public void error(SAXParseException var1) throws SAXException {
      throw var1;
   }

   public void fatalError(SAXParseException var1) throws SAXException {
      throw var1;
   }
}
