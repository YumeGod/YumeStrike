package org.apache.xerces.impl;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XMLNamespaceBinder implements XMLComponent, XMLDocumentFilter {
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces"};
   private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null};
   private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter"};
   private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null};
   protected boolean fNamespaces;
   protected SymbolTable fSymbolTable;
   protected XMLErrorReporter fErrorReporter;
   protected XMLDocumentHandler fDocumentHandler;
   protected XMLDocumentSource fDocumentSource;
   protected boolean fOnlyPassPrefixMappingEvents;
   private NamespaceContext fNamespaceContext;
   private QName fAttributeQName = new QName();

   public void setOnlyPassPrefixMappingEvents(boolean var1) {
      this.fOnlyPassPrefixMappingEvents = var1;
   }

   public boolean getOnlyPassPrefixMappingEvents() {
      return this.fOnlyPassPrefixMappingEvents;
   }

   public void reset(XMLComponentManager var1) throws XNIException {
      try {
         this.fNamespaces = var1.getFeature("http://xml.org/sax/features/namespaces");
      } catch (XMLConfigurationException var3) {
         this.fNamespaces = true;
      }

      this.fSymbolTable = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");
      this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");
   }

   public String[] getRecognizedFeatures() {
      return (String[])RECOGNIZED_FEATURES.clone();
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
   }

   public String[] getRecognizedProperties() {
      return (String[])RECOGNIZED_PROPERTIES.clone();
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (var1.startsWith("http://apache.org/xml/properties/")) {
         int var3 = var1.length() - "http://apache.org/xml/properties/".length();
         if (var3 == "internal/symbol-table".length() && var1.endsWith("internal/symbol-table")) {
            this.fSymbolTable = (SymbolTable)var2;
         } else if (var3 == "internal/error-reporter".length() && var1.endsWith("internal/error-reporter")) {
            this.fErrorReporter = (XMLErrorReporter)var2;
         }

      }
   }

   public Boolean getFeatureDefault(String var1) {
      for(int var2 = 0; var2 < RECOGNIZED_FEATURES.length; ++var2) {
         if (RECOGNIZED_FEATURES[var2].equals(var1)) {
            return FEATURE_DEFAULTS[var2];
         }
      }

      return null;
   }

   public Object getPropertyDefault(String var1) {
      for(int var2 = 0; var2 < RECOGNIZED_PROPERTIES.length; ++var2) {
         if (RECOGNIZED_PROPERTIES[var2].equals(var1)) {
            return PROPERTY_DEFAULTS[var2];
         }
      }

      return null;
   }

   public void setDocumentHandler(XMLDocumentHandler var1) {
      this.fDocumentHandler = var1;
   }

   public XMLDocumentHandler getDocumentHandler() {
      return this.fDocumentHandler;
   }

   public void setDocumentSource(XMLDocumentSource var1) {
      this.fDocumentSource = var1;
   }

   public XMLDocumentSource getDocumentSource() {
      return this.fDocumentSource;
   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.startGeneralEntity(var1, var2, var3, var4);
      }

   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.textDecl(var1, var2, var3);
      }

   }

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
      this.fNamespaceContext = var3;
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.startDocument(var1, var2, var3, var4);
      }

   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.xmlDecl(var1, var2, var3, var4);
      }

   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.doctypeDecl(var1, var2, var3, var4);
      }

   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.comment(var1, var2);
      }

   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.processingInstruction(var1, var2, var3);
      }

   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      if (this.fNamespaces) {
         this.handleStartElement(var1, var2, var3, false);
      } else if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startElement(var1, var2, var3);
      }

   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      if (this.fNamespaces) {
         this.handleStartElement(var1, var2, var3, true);
         this.handleEndElement(var1, var3, true);
      } else if (this.fDocumentHandler != null) {
         this.fDocumentHandler.emptyElement(var1, var2, var3);
      }

   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.characters(var1, var2);
      }

   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.ignorableWhitespace(var1, var2);
      }

   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      if (this.fNamespaces) {
         this.handleEndElement(var1, var2, false);
      } else if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endElement(var1, var2);
      }

   }

   public void startCDATA(Augmentations var1) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.startCDATA(var1);
      }

   }

   public void endCDATA(Augmentations var1) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.endCDATA(var1);
      }

   }

   public void endDocument(Augmentations var1) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.endDocument(var1);
      }

   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         this.fDocumentHandler.endGeneralEntity(var1, var2);
      }

   }

   protected void handleStartElement(QName var1, XMLAttributes var2, Augmentations var3, boolean var4) throws XNIException {
      this.fNamespaceContext.pushContext();
      if (var1.prefix == XMLSymbols.PREFIX_XMLNS) {
         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{var1.rawname}, (short)2);
      }

      int var5 = var2.getLength();

      String var7;
      String var9;
      for(int var6 = 0; var6 < var5; ++var6) {
         var7 = var2.getLocalName(var6);
         String var8 = var2.getPrefix(var6);
         if (var8 == XMLSymbols.PREFIX_XMLNS || var8 == XMLSymbols.EMPTY_STRING && var7 == XMLSymbols.PREFIX_XMLNS) {
            var9 = this.fSymbolTable.addSymbol(var2.getValue(var6));
            if (var8 == XMLSymbols.PREFIX_XMLNS && var7 == XMLSymbols.PREFIX_XMLNS) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{var2.getQName(var6)}, (short)2);
            }

            if (var9 == NamespaceContext.XMLNS_URI) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{var2.getQName(var6)}, (short)2);
            }

            if (var7 == XMLSymbols.PREFIX_XML) {
               if (var9 != NamespaceContext.XML_URI) {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{var2.getQName(var6)}, (short)2);
               }
            } else if (var9 == NamespaceContext.XML_URI) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{var2.getQName(var6)}, (short)2);
            }

            var8 = var7 != XMLSymbols.PREFIX_XMLNS ? var7 : XMLSymbols.EMPTY_STRING;
            if (this.prefixBoundToNullURI(var9, var7)) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[]{var2.getQName(var6)}, (short)2);
            } else {
               this.fNamespaceContext.declarePrefix(var8, var9.length() != 0 ? var9 : null);
            }
         }
      }

      var7 = var1.prefix != null ? var1.prefix : XMLSymbols.EMPTY_STRING;
      var1.uri = this.fNamespaceContext.getURI(var7);
      if (var1.prefix == null && var1.uri != null) {
         var1.prefix = XMLSymbols.EMPTY_STRING;
      }

      if (var1.prefix != null && var1.uri == null) {
         this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{var1.prefix, var1.rawname}, (short)2);
      }

      for(int var16 = 0; var16 < var5; ++var16) {
         var2.getName(var16, this.fAttributeQName);
         var9 = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
         String var10 = this.fAttributeQName.rawname;
         if (var10 == XMLSymbols.PREFIX_XMLNS) {
            this.fAttributeQName.uri = this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
            var2.setName(var16, this.fAttributeQName);
         } else if (var9 != XMLSymbols.EMPTY_STRING) {
            this.fAttributeQName.uri = this.fNamespaceContext.getURI(var9);
            if (this.fAttributeQName.uri == null) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{var1.rawname, var10, var9}, (short)2);
            }

            var2.setName(var16, this.fAttributeQName);
         }
      }

      int var17 = var2.getLength();

      for(int var18 = 0; var18 < var17 - 1; ++var18) {
         String var11 = var2.getURI(var18);
         if (var11 != null && var11 != NamespaceContext.XMLNS_URI) {
            String var12 = var2.getLocalName(var18);

            for(int var13 = var18 + 1; var13 < var17; ++var13) {
               String var14 = var2.getLocalName(var13);
               String var15 = var2.getURI(var13);
               if (var12 == var14 && var11 == var15) {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{var1.rawname, var12, var11}, (short)2);
               }
            }
         }
      }

      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
         if (var4) {
            this.fDocumentHandler.emptyElement(var1, var2, var3);
         } else {
            this.fDocumentHandler.startElement(var1, var2, var3);
         }
      }

   }

   protected void handleEndElement(QName var1, Augmentations var2, boolean var3) throws XNIException {
      String var4 = var1.prefix != null ? var1.prefix : XMLSymbols.EMPTY_STRING;
      var1.uri = this.fNamespaceContext.getURI(var4);
      if (var1.uri != null) {
         var1.prefix = var4;
      }

      if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents && !var3) {
         this.fDocumentHandler.endElement(var1, var2);
      }

      this.fNamespaceContext.popContext();
   }

   protected boolean prefixBoundToNullURI(String var1, String var2) {
      return var1 == XMLSymbols.EMPTY_STRING && var2 != XMLSymbols.PREFIX_XMLNS;
   }
}
