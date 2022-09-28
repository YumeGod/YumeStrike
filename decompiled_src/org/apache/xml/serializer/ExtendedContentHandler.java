package org.apache.xml.serializer;

import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

interface ExtendedContentHandler extends ContentHandler {
   int NO_BAD_CHARS = 1;
   int HTML_ATTREMPTY = 2;
   int HTML_ATTRURL = 4;

   void addAttribute(String var1, String var2, String var3, String var4, String var5, boolean var6) throws SAXException;

   void addAttributes(Attributes var1) throws SAXException;

   void addAttribute(String var1, String var2);

   void characters(String var1) throws SAXException;

   void characters(Node var1) throws SAXException;

   void endElement(String var1) throws SAXException;

   void startElement(String var1, String var2, String var3) throws SAXException;

   void startElement(String var1) throws SAXException;

   void namespaceAfterStartElement(String var1, String var2) throws SAXException;

   boolean startPrefixMapping(String var1, String var2, boolean var3) throws SAXException;

   void entityReference(String var1) throws SAXException;

   NamespaceMappings getNamespaceMappings();

   String getPrefix(String var1);

   String getNamespaceURI(String var1, boolean var2);

   String getNamespaceURIFromPrefix(String var1);

   void setSourceLocator(SourceLocator var1);

   void addUniqueAttribute(String var1, String var2, int var3) throws SAXException;

   void addXSLAttribute(String var1, String var2, String var3);

   void addAttribute(String var1, String var2, String var3, String var4, String var5) throws SAXException;
}
