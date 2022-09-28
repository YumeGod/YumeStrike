package org.xml.sax;

public interface DTDHandler {
   void notationDecl(String var1, String var2, String var3) throws SAXException;

   void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException;
}
