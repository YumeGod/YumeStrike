package org.apache.fop.util;

import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.AttributesImpl;

public class TransformerDefaultHandler extends DefaultHandler2 {
   private TransformerHandler transformerHandler;

   public TransformerDefaultHandler(TransformerHandler transformerHandler) {
      this.transformerHandler = transformerHandler;
   }

   public TransformerHandler getTransformerHandler() {
      return this.transformerHandler;
   }

   public void setDocumentLocator(Locator locator) {
      this.transformerHandler.setDocumentLocator(locator);
   }

   public void startDocument() throws SAXException {
      this.transformerHandler.startDocument();
   }

   public void endDocument() throws SAXException {
      this.transformerHandler.endDocument();
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      this.transformerHandler.startPrefixMapping(prefix, uri);
   }

   public void endPrefixMapping(String string) throws SAXException {
      this.transformerHandler.endPrefixMapping(string);
   }

   public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
      AttributesImpl ai = new AttributesImpl(attrs);
      this.transformerHandler.startElement(uri, localName, qName, ai);
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      this.transformerHandler.endElement(uri, localName, qName);
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.transformerHandler.characters(ch, start, length);
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      this.transformerHandler.ignorableWhitespace(ch, start, length);
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.transformerHandler.processingInstruction(target, data);
   }

   public void skippedEntity(String name) throws SAXException {
      this.transformerHandler.skippedEntity(name);
   }

   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
      this.transformerHandler.notationDecl(name, publicId, systemId);
   }

   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
      this.transformerHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
   }

   public void startDTD(String name, String pid, String lid) throws SAXException {
      this.transformerHandler.startDTD(name, pid, lid);
   }

   public void endDTD() throws SAXException {
      this.transformerHandler.endDTD();
   }

   public void startEntity(String name) throws SAXException {
      this.transformerHandler.startEntity(name);
   }

   public void endEntity(String name) throws SAXException {
      this.transformerHandler.endEntity(name);
   }

   public void startCDATA() throws SAXException {
      this.transformerHandler.startCDATA();
   }

   public void endCDATA() throws SAXException {
      this.transformerHandler.endCDATA();
   }

   public void comment(char[] charArray, int start, int length) throws SAXException {
      this.transformerHandler.comment(charArray, start, length);
   }
}
