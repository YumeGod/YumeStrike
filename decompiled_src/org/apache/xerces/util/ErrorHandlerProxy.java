package org.apache.xerces.util;

import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class ErrorHandlerProxy implements ErrorHandler {
   public void error(SAXParseException var1) throws SAXException {
      XMLErrorHandler var2 = this.getErrorHandler();
      if (var2 instanceof ErrorHandlerWrapper) {
         ((ErrorHandlerWrapper)var2).fErrorHandler.error(var1);
      } else {
         var2.error("", "", ErrorHandlerWrapper.createXMLParseException(var1));
      }

   }

   public void fatalError(SAXParseException var1) throws SAXException {
      XMLErrorHandler var2 = this.getErrorHandler();
      if (var2 instanceof ErrorHandlerWrapper) {
         ((ErrorHandlerWrapper)var2).fErrorHandler.fatalError(var1);
      } else {
         var2.fatalError("", "", ErrorHandlerWrapper.createXMLParseException(var1));
      }

   }

   public void warning(SAXParseException var1) throws SAXException {
      XMLErrorHandler var2 = this.getErrorHandler();
      if (var2 instanceof ErrorHandlerWrapper) {
         ((ErrorHandlerWrapper)var2).fErrorHandler.warning(var1);
      } else {
         var2.warning("", "", ErrorHandlerWrapper.createXMLParseException(var1));
      }

   }

   protected abstract XMLErrorHandler getErrorHandler();
}
