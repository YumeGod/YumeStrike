package org.xml.sax.helpers;

import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

public class XMLFilterImpl implements XMLFilter, EntityResolver, DTDHandler, ContentHandler, ErrorHandler {
   private XMLReader parent = null;
   private Locator locator = null;
   private EntityResolver entityResolver = null;
   private DTDHandler dtdHandler = null;
   private ContentHandler contentHandler = null;
   private ErrorHandler errorHandler = null;

   public XMLFilterImpl() {
   }

   public XMLFilterImpl(XMLReader var1) {
      this.setParent(var1);
   }

   public void setParent(XMLReader var1) {
      this.parent = var1;
   }

   public XMLReader getParent() {
      return this.parent;
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (this.parent != null) {
         this.parent.setFeature(var1, var2);
      } else {
         throw new SAXNotRecognizedException("Feature: " + var1);
      }
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (this.parent != null) {
         return this.parent.getFeature(var1);
      } else {
         throw new SAXNotRecognizedException("Feature: " + var1);
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (this.parent != null) {
         this.parent.setProperty(var1, var2);
      } else {
         throw new SAXNotRecognizedException("Property: " + var1);
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (this.parent != null) {
         return this.parent.getProperty(var1);
      } else {
         throw new SAXNotRecognizedException("Property: " + var1);
      }
   }

   public void setEntityResolver(EntityResolver var1) {
      this.entityResolver = var1;
   }

   public EntityResolver getEntityResolver() {
      return this.entityResolver;
   }

   public void setDTDHandler(DTDHandler var1) {
      this.dtdHandler = var1;
   }

   public DTDHandler getDTDHandler() {
      return this.dtdHandler;
   }

   public void setContentHandler(ContentHandler var1) {
      this.contentHandler = var1;
   }

   public ContentHandler getContentHandler() {
      return this.contentHandler;
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public void parse(InputSource var1) throws SAXException, IOException {
      this.setupParse();
      this.parent.parse(var1);
   }

   public void parse(String var1) throws SAXException, IOException {
      this.parse(new InputSource(var1));
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      return this.entityResolver != null ? this.entityResolver.resolveEntity(var1, var2) : null;
   }

   public void notationDecl(String var1, String var2, String var3) throws SAXException {
      if (this.dtdHandler != null) {
         this.dtdHandler.notationDecl(var1, var2, var3);
      }

   }

   public void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException {
      if (this.dtdHandler != null) {
         this.dtdHandler.unparsedEntityDecl(var1, var2, var3, var4);
      }

   }

   public void setDocumentLocator(Locator var1) {
      this.locator = var1;
      if (this.contentHandler != null) {
         this.contentHandler.setDocumentLocator(var1);
      }

   }

   public void startDocument() throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.startDocument();
      }

   }

   public void endDocument() throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.endDocument();
      }

   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.startPrefixMapping(var1, var2);
      }

   }

   public void endPrefixMapping(String var1) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.endPrefixMapping(var1);
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.startElement(var1, var2, var3, var4);
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.endElement(var1, var2, var3);
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.characters(var1, var2, var3);
      }

   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.ignorableWhitespace(var1, var2, var3);
      }

   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.processingInstruction(var1, var2);
      }

   }

   public void skippedEntity(String var1) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.skippedEntity(var1);
      }

   }

   public void warning(SAXParseException var1) throws SAXException {
      if (this.errorHandler != null) {
         this.errorHandler.warning(var1);
      }

   }

   public void error(SAXParseException var1) throws SAXException {
      if (this.errorHandler != null) {
         this.errorHandler.error(var1);
      }

   }

   public void fatalError(SAXParseException var1) throws SAXException {
      if (this.errorHandler != null) {
         this.errorHandler.fatalError(var1);
      }

   }

   private void setupParse() {
      if (this.parent == null) {
         throw new NullPointerException("No parent for filter");
      } else {
         this.parent.setEntityResolver(this);
         this.parent.setDTDHandler(this);
         this.parent.setContentHandler(this);
         this.parent.setErrorHandler(this);
      }
   }
}
