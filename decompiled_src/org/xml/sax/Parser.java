package org.xml.sax;

import java.io.IOException;
import java.util.Locale;

/** @deprecated */
public interface Parser {
   void setLocale(Locale var1) throws SAXException;

   void setEntityResolver(EntityResolver var1);

   void setDTDHandler(DTDHandler var1);

   void setDocumentHandler(DocumentHandler var1);

   void setErrorHandler(ErrorHandler var1);

   void parse(InputSource var1) throws SAXException, IOException;

   void parse(String var1) throws SAXException, IOException;
}
