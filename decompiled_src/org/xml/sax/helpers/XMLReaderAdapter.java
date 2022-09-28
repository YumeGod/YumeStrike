package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Locale;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XMLReaderAdapter implements Parser, ContentHandler {
   XMLReader xmlReader;
   DocumentHandler documentHandler;
   AttributesAdapter qAtts;

   public XMLReaderAdapter() throws SAXException {
      this.setup(XMLReaderFactory.createXMLReader());
   }

   public XMLReaderAdapter(XMLReader var1) {
      this.setup(var1);
   }

   private void setup(XMLReader var1) {
      if (var1 == null) {
         throw new NullPointerException("XMLReader must not be null");
      } else {
         this.xmlReader = var1;
         this.qAtts = new AttributesAdapter();
      }
   }

   public void setLocale(Locale var1) throws SAXException {
      throw new SAXNotSupportedException("setLocale not supported");
   }

   public void setEntityResolver(EntityResolver var1) {
      this.xmlReader.setEntityResolver(var1);
   }

   public void setDTDHandler(DTDHandler var1) {
      this.xmlReader.setDTDHandler(var1);
   }

   public void setDocumentHandler(DocumentHandler var1) {
      this.documentHandler = var1;
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.xmlReader.setErrorHandler(var1);
   }

   public void parse(String var1) throws IOException, SAXException {
      this.parse(new InputSource(var1));
   }

   public void parse(InputSource var1) throws IOException, SAXException {
      this.setupXMLReader();
      this.xmlReader.parse(var1);
   }

   private void setupXMLReader() throws SAXException {
      this.xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);

      try {
         this.xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
      } catch (SAXException var2) {
      }

      this.xmlReader.setContentHandler(this);
   }

   public void setDocumentLocator(Locator var1) {
      if (this.documentHandler != null) {
         this.documentHandler.setDocumentLocator(var1);
      }

   }

   public void startDocument() throws SAXException {
      if (this.documentHandler != null) {
         this.documentHandler.startDocument();
      }

   }

   public void endDocument() throws SAXException {
      if (this.documentHandler != null) {
         this.documentHandler.endDocument();
      }

   }

   public void startPrefixMapping(String var1, String var2) {
   }

   public void endPrefixMapping(String var1) {
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.documentHandler != null) {
         this.qAtts.setAttributes(var4);
         this.documentHandler.startElement(var3, this.qAtts);
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (this.documentHandler != null) {
         this.documentHandler.endElement(var3);
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.documentHandler != null) {
         this.documentHandler.characters(var1, var2, var3);
      }

   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      if (this.documentHandler != null) {
         this.documentHandler.ignorableWhitespace(var1, var2, var3);
      }

   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (this.documentHandler != null) {
         this.documentHandler.processingInstruction(var1, var2);
      }

   }

   public void skippedEntity(String var1) throws SAXException {
   }

   final class AttributesAdapter implements AttributeList {
      private Attributes attributes;

      void setAttributes(Attributes var1) {
         this.attributes = var1;
      }

      public int getLength() {
         return this.attributes.getLength();
      }

      public String getName(int var1) {
         return this.attributes.getQName(var1);
      }

      public String getType(int var1) {
         return this.attributes.getType(var1);
      }

      public String getValue(int var1) {
         return this.attributes.getValue(var1);
      }

      public String getType(String var1) {
         return this.attributes.getType(var1);
      }

      public String getValue(String var1) {
         return this.attributes.getValue(var1);
      }
   }
}
