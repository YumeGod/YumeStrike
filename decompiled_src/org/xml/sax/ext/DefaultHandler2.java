package org.xml.sax.ext;

import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DefaultHandler2 extends DefaultHandler implements LexicalHandler, DeclHandler, EntityResolver2 {
   public void startCDATA() throws SAXException {
   }

   public void endCDATA() throws SAXException {
   }

   public void startDTD(String var1, String var2, String var3) throws SAXException {
   }

   public void endDTD() throws SAXException {
   }

   public void startEntity(String var1) throws SAXException {
   }

   public void endEntity(String var1) throws SAXException {
   }

   public void comment(char[] var1, int var2, int var3) throws SAXException {
   }

   public void attributeDecl(String var1, String var2, String var3, String var4, String var5) throws SAXException {
   }

   public void elementDecl(String var1, String var2) throws SAXException {
   }

   public void externalEntityDecl(String var1, String var2, String var3) throws SAXException {
   }

   public void internalEntityDecl(String var1, String var2) throws SAXException {
   }

   public InputSource getExternalSubset(String var1, String var2) throws SAXException, IOException {
      return null;
   }

   public InputSource resolveEntity(String var1, String var2, String var3, String var4) throws SAXException, IOException {
      return null;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      return this.resolveEntity((String)null, var1, (String)null, var2);
   }
}
