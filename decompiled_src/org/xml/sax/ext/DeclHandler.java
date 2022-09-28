package org.xml.sax.ext;

import org.xml.sax.SAXException;

public interface DeclHandler {
   void elementDecl(String var1, String var2) throws SAXException;

   void attributeDecl(String var1, String var2, String var3, String var4, String var5) throws SAXException;

   void internalEntityDecl(String var1, String var2) throws SAXException;

   void externalEntityDecl(String var1, String var2, String var3) throws SAXException;
}
