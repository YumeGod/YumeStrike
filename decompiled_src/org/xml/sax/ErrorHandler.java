package org.xml.sax;

public interface ErrorHandler {
   void warning(SAXParseException var1) throws SAXException;

   void error(SAXParseException var1) throws SAXException;

   void fatalError(SAXParseException var1) throws SAXException;
}
