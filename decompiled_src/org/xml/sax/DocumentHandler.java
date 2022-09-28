package org.xml.sax;

/** @deprecated */
public interface DocumentHandler {
   void setDocumentLocator(Locator var1);

   void startDocument() throws SAXException;

   void endDocument() throws SAXException;

   void startElement(String var1, AttributeList var2) throws SAXException;

   void endElement(String var1) throws SAXException;

   void characters(char[] var1, int var2, int var3) throws SAXException;

   void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException;

   void processingInstruction(String var1, String var2) throws SAXException;
}
