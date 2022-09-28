package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SAXLocatorWrapper;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

final class SchemaContentHandler implements ContentHandler {
   private SymbolTable fSymbolTable;
   private SchemaDOMParser fSchemaDOMParser;
   private final SAXLocatorWrapper fSAXLocatorWrapper = new SAXLocatorWrapper();
   private NamespaceSupport fNamespaceContext = new NamespaceSupport();
   private boolean fNeedPushNSContext;
   private boolean fNamespacePrefixes = false;
   private boolean fStringsInternalized = false;
   private final QName fElementQName = new QName();
   private final QName fAttributeQName = new QName();
   private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
   private final XMLString fTempString = new XMLString();

   public SchemaContentHandler() {
   }

   public Document getDocument() {
      return this.fSchemaDOMParser.getDocument();
   }

   public void setDocumentLocator(Locator var1) {
      this.fSAXLocatorWrapper.setLocator(var1);
   }

   public void startDocument() throws SAXException {
      this.fNeedPushNSContext = true;

      try {
         this.fSchemaDOMParser.startDocument(this.fSAXLocatorWrapper, (String)null, this.fNamespaceContext, (Augmentations)null);
      } catch (XMLParseException var3) {
         convertToSAXParseException(var3);
      } catch (XNIException var4) {
         convertToSAXException(var4);
      }

   }

   public void endDocument() throws SAXException {
      this.fSAXLocatorWrapper.setLocator((Locator)null);

      try {
         this.fSchemaDOMParser.endDocument((Augmentations)null);
      } catch (XMLParseException var3) {
         convertToSAXParseException(var3);
      } catch (XNIException var4) {
         convertToSAXException(var4);
      }

   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
      if (this.fNeedPushNSContext) {
         this.fNeedPushNSContext = false;
         this.fNamespaceContext.pushContext();
      }

      if (!this.fStringsInternalized) {
         var1 = var1 != null ? this.fSymbolTable.addSymbol(var1) : XMLSymbols.EMPTY_STRING;
         var2 = var2 != null && var2.length() > 0 ? this.fSymbolTable.addSymbol(var2) : null;
      } else {
         if (var1 == null) {
            var1 = XMLSymbols.EMPTY_STRING;
         }

         if (var2 != null && var2.length() == 0) {
            var2 = null;
         }
      }

      this.fNamespaceContext.declarePrefix(var1, var2);
   }

   public void endPrefixMapping(String var1) throws SAXException {
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.fNeedPushNSContext) {
         this.fNamespaceContext.pushContext();
      }

      this.fNeedPushNSContext = true;
      this.fillQName(this.fElementQName, var1, var2, var3);
      this.fillXMLAttributes(var4);
      if (!this.fNamespacePrefixes) {
         int var5 = this.fNamespaceContext.getDeclaredPrefixCount();
         if (var5 > 0) {
            this.addNamespaceDeclarations(var5);
         }
      }

      try {
         this.fSchemaDOMParser.startElement(this.fElementQName, this.fAttributes, (Augmentations)null);
      } catch (XMLParseException var7) {
         convertToSAXParseException(var7);
      } catch (XNIException var8) {
         convertToSAXException(var8);
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      this.fillQName(this.fElementQName, var1, var2, var3);

      try {
         this.fSchemaDOMParser.endElement(this.fElementQName, (Augmentations)null);
      } catch (XMLParseException var11) {
         convertToSAXParseException(var11);
      } catch (XNIException var12) {
         convertToSAXException(var12);
      } finally {
         this.fNamespaceContext.popContext();
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      try {
         this.fTempString.setValues(var1, var2, var3);
         this.fSchemaDOMParser.characters(this.fTempString, (Augmentations)null);
      } catch (XMLParseException var6) {
         convertToSAXParseException(var6);
      } catch (XNIException var7) {
         convertToSAXException(var7);
      }

   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      try {
         this.fTempString.setValues(var1, var2, var3);
         this.fSchemaDOMParser.ignorableWhitespace(this.fTempString, (Augmentations)null);
      } catch (XMLParseException var6) {
         convertToSAXParseException(var6);
      } catch (XNIException var7) {
         convertToSAXException(var7);
      }

   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      try {
         this.fTempString.setValues(var2.toCharArray(), 0, var2.length());
         this.fSchemaDOMParser.processingInstruction(var1, this.fTempString, (Augmentations)null);
      } catch (XMLParseException var5) {
         convertToSAXParseException(var5);
      } catch (XNIException var6) {
         convertToSAXException(var6);
      }

   }

   public void skippedEntity(String var1) throws SAXException {
   }

   private void fillQName(QName var1, String var2, String var3, String var4) {
      if (!this.fStringsInternalized) {
         var2 = var2 != null && var2.length() > 0 ? this.fSymbolTable.addSymbol(var2) : null;
         var3 = var3 != null ? this.fSymbolTable.addSymbol(var3) : XMLSymbols.EMPTY_STRING;
         var4 = var4 != null ? this.fSymbolTable.addSymbol(var4) : XMLSymbols.EMPTY_STRING;
      } else {
         if (var2 != null && var2.length() == 0) {
            var2 = null;
         }

         if (var3 == null) {
            var3 = XMLSymbols.EMPTY_STRING;
         }

         if (var4 == null) {
            var4 = XMLSymbols.EMPTY_STRING;
         }
      }

      String var5 = XMLSymbols.EMPTY_STRING;
      int var6 = var4.indexOf(58);
      if (var6 != -1) {
         var5 = this.fSymbolTable.addSymbol(var4.substring(0, var6));
         if (var3 == XMLSymbols.EMPTY_STRING) {
            var3 = this.fSymbolTable.addSymbol(var4.substring(var6 + 1));
         }
      } else if (var3 == XMLSymbols.EMPTY_STRING) {
         var3 = var4;
      }

      var1.setValues(var5, var3, var4, var2);
   }

   private void fillXMLAttributes(Attributes var1) {
      this.fAttributes.removeAllAttributes();
      int var2 = var1.getLength();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.fillQName(this.fAttributeQName, var1.getURI(var3), var1.getLocalName(var3), var1.getQName(var3));
         String var4 = var1.getType(var3);
         this.fAttributes.addAttributeNS(this.fAttributeQName, var4 != null ? var4 : XMLSymbols.fCDATASymbol, var1.getValue(var3));
         this.fAttributes.setSpecified(var3, true);
      }

   }

   private void addNamespaceDeclarations(int var1) {
      String var2 = null;
      String var3 = null;
      String var4 = null;
      String var5 = null;
      String var6 = null;

      for(int var7 = 0; var7 < var1; ++var7) {
         var5 = this.fNamespaceContext.getDeclaredPrefixAt(var7);
         var6 = this.fNamespaceContext.getURI(var5);
         if (var5.length() > 0) {
            var2 = XMLSymbols.PREFIX_XMLNS;
            var3 = var5;
            var4 = this.fSymbolTable.addSymbol(var2 + ":" + var5);
         } else {
            var2 = XMLSymbols.EMPTY_STRING;
            var3 = XMLSymbols.PREFIX_XMLNS;
            var4 = XMLSymbols.PREFIX_XMLNS;
         }

         this.fAttributeQName.setValues(var2, var3, var4, NamespaceContext.XMLNS_URI);
         this.fAttributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, var6);
      }

   }

   public void reset(SchemaDOMParser var1, SymbolTable var2, boolean var3, boolean var4) {
      this.fSchemaDOMParser = var1;
      this.fSymbolTable = var2;
      this.fNamespacePrefixes = var3;
      this.fStringsInternalized = var4;
   }

   static void convertToSAXParseException(XMLParseException var0) throws SAXException {
      Exception var1 = var0.getException();
      if (var1 == null) {
         LocatorImpl var2 = new LocatorImpl();
         var2.setPublicId(var0.getPublicId());
         var2.setSystemId(var0.getExpandedSystemId());
         var2.setLineNumber(var0.getLineNumber());
         var2.setColumnNumber(var0.getColumnNumber());
         throw new SAXParseException(var0.getMessage(), var2);
      } else if (var1 instanceof SAXException) {
         throw (SAXException)var1;
      } else {
         throw new SAXException(var1);
      }
   }

   static void convertToSAXException(XNIException var0) throws SAXException {
      Exception var1 = var0.getException();
      if (var1 == null) {
         throw new SAXException(var0.getMessage());
      } else if (var1 instanceof SAXException) {
         throw (SAXException)var1;
      } else {
         throw new SAXException(var1);
      }
   }
}
