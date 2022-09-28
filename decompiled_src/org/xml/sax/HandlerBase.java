package org.xml.sax;

/** @deprecated */
public class HandlerBase implements EntityResolver, DTDHandler, DocumentHandler, ErrorHandler {
   public InputSource resolveEntity(String var1, String var2) throws SAXException {
      return null;
   }

   public void notationDecl(String var1, String var2, String var3) {
   }

   public void unparsedEntityDecl(String var1, String var2, String var3, String var4) {
   }

   public void setDocumentLocator(Locator var1) {
   }

   public void startDocument() throws SAXException {
   }

   public void endDocument() throws SAXException {
   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
   }

   public void endElement(String var1) throws SAXException {
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
   }

   public void warning(SAXParseException var1) throws SAXException {
   }

   public void error(SAXParseException var1) throws SAXException {
   }

   public void fatalError(SAXParseException var1) throws SAXException {
      throw var1;
   }
}
