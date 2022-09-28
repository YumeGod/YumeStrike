package org.apache.xerces.jaxp;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class DefaultValidationErrorHandler extends DefaultHandler {
   private static int ERROR_COUNT_LIMIT = 10;
   private int errorCount = 0;

   public void error(SAXParseException var1) throws SAXException {
      if (this.errorCount < ERROR_COUNT_LIMIT) {
         if (this.errorCount == 0) {
            System.err.println("Warning: validation was turned on but an org.xml.sax.ErrorHandler was not");
            System.err.println("set, which is probably not what is desired.  Parser will use a default");
            System.err.println("ErrorHandler to print the first " + ERROR_COUNT_LIMIT + " errors.  Please call");
            System.err.println("the 'setErrorHandler' method to fix this.");
         }

         String var2 = var1.getSystemId();
         if (var2 == null) {
            var2 = "null";
         }

         String var3 = "Error: URI=" + var2 + " Line=" + var1.getLineNumber() + ": " + var1.getMessage();
         System.err.println(var3);
         ++this.errorCount;
      }
   }
}
