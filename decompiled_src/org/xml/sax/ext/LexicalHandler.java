package org.xml.sax.ext;

import org.xml.sax.SAXException;

public interface LexicalHandler {
   void startDTD(String var1, String var2, String var3) throws SAXException;

   void endDTD() throws SAXException;

   void startEntity(String var1) throws SAXException;

   void endEntity(String var1) throws SAXException;

   void startCDATA() throws SAXException;

   void endCDATA() throws SAXException;

   void comment(char[] var1, int var2, int var3) throws SAXException;
}
