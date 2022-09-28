package org.apache.fop.util;

import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;

public class DelegatingContentHandler implements EntityResolver, DTDHandler, ContentHandler, LexicalHandler, ErrorHandler {
   private ContentHandler delegate;
   private EntityResolver entityResolver;
   private DTDHandler dtdHandler;
   private LexicalHandler lexicalHandler;
   private ErrorHandler errorHandler;

   public DelegatingContentHandler() {
   }

   public DelegatingContentHandler(ContentHandler handler) {
      this.setDelegateContentHandler(handler);
      if (handler instanceof EntityResolver) {
         this.setDelegateEntityResolver((EntityResolver)handler);
      }

      if (handler instanceof DTDHandler) {
         this.setDelegateDTDHandler((DTDHandler)handler);
      }

      if (handler instanceof LexicalHandler) {
         this.setDelegateLexicalHandler((LexicalHandler)handler);
      }

      if (handler instanceof ErrorHandler) {
         this.setDelegateErrorHandler((ErrorHandler)handler);
      }

   }

   public ContentHandler getDelegateContentHandler() {
      return this.delegate;
   }

   public void setDelegateContentHandler(ContentHandler handler) {
      this.delegate = handler;
   }

   public void setDelegateEntityResolver(EntityResolver resolver) {
      this.entityResolver = resolver;
   }

   public void setDelegateDTDHandler(DTDHandler handler) {
      this.dtdHandler = handler;
   }

   public void setDelegateLexicalHandler(LexicalHandler handler) {
      this.lexicalHandler = handler;
   }

   public void setDelegateErrorHandler(ErrorHandler handler) {
      this.errorHandler = handler;
   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
      return this.entityResolver != null ? this.entityResolver.resolveEntity(publicId, systemId) : null;
   }

   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
      if (this.dtdHandler != null) {
         this.dtdHandler.notationDecl(name, publicId, systemId);
      }

   }

   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
      if (this.dtdHandler != null) {
         this.dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
      }

   }

   public void setDocumentLocator(Locator locator) {
      this.delegate.setDocumentLocator(locator);
   }

   public void startDocument() throws SAXException {
      this.delegate.startDocument();
   }

   public void endDocument() throws SAXException {
      this.delegate.endDocument();
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      this.delegate.startPrefixMapping(prefix, uri);
   }

   public void endPrefixMapping(String prefix) throws SAXException {
      this.delegate.endPrefixMapping(prefix);
   }

   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      this.delegate.startElement(uri, localName, qName, atts);
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      this.delegate.endElement(uri, localName, qName);
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.delegate.characters(ch, start, length);
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      this.delegate.ignorableWhitespace(ch, start, length);
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.delegate.processingInstruction(target, data);
   }

   public void skippedEntity(String name) throws SAXException {
      this.delegate.skippedEntity(name);
   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
      if (this.lexicalHandler != null) {
         this.lexicalHandler.startDTD(name, publicId, systemId);
      }

   }

   public void endDTD() throws SAXException {
      if (this.lexicalHandler != null) {
         this.lexicalHandler.endDTD();
      }

   }

   public void startEntity(String name) throws SAXException {
      if (this.lexicalHandler != null) {
         this.lexicalHandler.startEntity(name);
      }

   }

   public void endEntity(String name) throws SAXException {
      if (this.lexicalHandler != null) {
         this.lexicalHandler.endEntity(name);
      }

   }

   public void startCDATA() throws SAXException {
      if (this.lexicalHandler != null) {
         this.lexicalHandler.startCDATA();
      }

   }

   public void endCDATA() throws SAXException {
      if (this.lexicalHandler != null) {
         this.lexicalHandler.endCDATA();
      }

   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      if (this.lexicalHandler != null) {
         this.lexicalHandler.comment(ch, start, length);
      }

   }

   public void warning(SAXParseException exception) throws SAXException {
      if (this.errorHandler != null) {
         this.errorHandler.warning(exception);
      }

   }

   public void error(SAXParseException exception) throws SAXException {
      if (this.errorHandler != null) {
         this.errorHandler.error(exception);
      }

   }

   public void fatalError(SAXParseException exception) throws SAXException {
      if (this.errorHandler != null) {
         this.errorHandler.fatalError(exception);
      }

   }
}
