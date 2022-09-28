package org.apache.xerces.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

public abstract class BasicParserConfiguration extends ParserConfigurationSettings implements XMLParserConfiguration {
   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
   protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
   protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   protected SymbolTable fSymbolTable;
   protected Locale fLocale;
   protected ArrayList fComponents;
   protected XMLDocumentHandler fDocumentHandler;
   protected XMLDTDHandler fDTDHandler;
   protected XMLDTDContentModelHandler fDTDContentModelHandler;
   protected XMLDocumentSource fLastComponent;

   protected BasicParserConfiguration() {
      this((SymbolTable)null, (XMLComponentManager)null);
   }

   protected BasicParserConfiguration(SymbolTable var1) {
      this(var1, (XMLComponentManager)null);
   }

   protected BasicParserConfiguration(SymbolTable var1, XMLComponentManager var2) {
      super(var2);
      this.fComponents = new ArrayList();
      super.fRecognizedFeatures = new ArrayList();
      super.fRecognizedProperties = new ArrayList();
      super.fFeatures = new HashMap();
      super.fProperties = new HashMap();
      String[] var3 = new String[]{"http://apache.org/xml/features/internal/parser-settings", "http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities"};
      this.addRecognizedFeatures(var3);
      super.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
      super.fFeatures.put("http://xml.org/sax/features/validation", Boolean.FALSE);
      super.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
      super.fFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.TRUE);
      super.fFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.TRUE);
      String[] var4 = new String[]{"http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver"};
      this.addRecognizedProperties(var4);
      if (var1 == null) {
         var1 = new SymbolTable();
      }

      this.fSymbolTable = var1;
      super.fProperties.put("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
   }

   protected void addComponent(XMLComponent var1) {
      if (!this.fComponents.contains(var1)) {
         this.fComponents.add(var1);
         String[] var2 = var1.getRecognizedFeatures();
         this.addRecognizedFeatures(var2);
         String[] var3 = var1.getRecognizedProperties();
         this.addRecognizedProperties(var3);
         int var4;
         String var5;
         if (var2 != null) {
            for(var4 = 0; var4 < var2.length; ++var4) {
               var5 = var2[var4];
               Boolean var6 = var1.getFeatureDefault(var5);
               if (var6 != null) {
                  super.setFeature(var5, var6);
               }
            }
         }

         if (var3 != null) {
            for(var4 = 0; var4 < var3.length; ++var4) {
               var5 = var3[var4];
               Object var7 = var1.getPropertyDefault(var5);
               if (var7 != null) {
                  super.setProperty(var5, var7);
               }
            }
         }

      }
   }

   public abstract void parse(XMLInputSource var1) throws XNIException, IOException;

   public void setDocumentHandler(XMLDocumentHandler var1) {
      this.fDocumentHandler = var1;
      if (this.fLastComponent != null) {
         this.fLastComponent.setDocumentHandler(this.fDocumentHandler);
         if (this.fDocumentHandler != null) {
            this.fDocumentHandler.setDocumentSource(this.fLastComponent);
         }
      }

   }

   public XMLDocumentHandler getDocumentHandler() {
      return this.fDocumentHandler;
   }

   public void setDTDHandler(XMLDTDHandler var1) {
      this.fDTDHandler = var1;
   }

   public XMLDTDHandler getDTDHandler() {
      return this.fDTDHandler;
   }

   public void setDTDContentModelHandler(XMLDTDContentModelHandler var1) {
      this.fDTDContentModelHandler = var1;
   }

   public XMLDTDContentModelHandler getDTDContentModelHandler() {
      return this.fDTDContentModelHandler;
   }

   public void setEntityResolver(XMLEntityResolver var1) {
      super.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", var1);
   }

   public XMLEntityResolver getEntityResolver() {
      return (XMLEntityResolver)super.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
   }

   public void setErrorHandler(XMLErrorHandler var1) {
      super.fProperties.put("http://apache.org/xml/properties/internal/error-handler", var1);
   }

   public XMLErrorHandler getErrorHandler() {
      return (XMLErrorHandler)super.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      int var3 = this.fComponents.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         XMLComponent var5 = (XMLComponent)this.fComponents.get(var4);
         var5.setFeature(var1, var2);
      }

      super.setFeature(var1, var2);
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      int var3 = this.fComponents.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         XMLComponent var5 = (XMLComponent)this.fComponents.get(var4);
         var5.setProperty(var1, var2);
      }

      super.setProperty(var1, var2);
   }

   public void setLocale(Locale var1) throws XNIException {
      this.fLocale = var1;
   }

   public Locale getLocale() {
      return this.fLocale;
   }

   protected void reset() throws XNIException {
      int var1 = this.fComponents.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         XMLComponent var3 = (XMLComponent)this.fComponents.get(var2);
         var3.reset(this);
      }

   }

   protected void checkProperty(String var1) throws XMLConfigurationException {
      if (var1.startsWith("http://xml.org/sax/properties/")) {
         int var2 = var1.length() - "http://xml.org/sax/properties/".length();
         if (var2 == "xml-string".length() && var1.endsWith("xml-string")) {
            byte var3 = 1;
            throw new XMLConfigurationException(var3, var1);
         }
      }

      super.checkProperty(var1);
   }

   protected void checkFeature(String var1) throws XMLConfigurationException {
      if (var1.startsWith("http://apache.org/xml/features/")) {
         int var2 = var1.length() - "http://apache.org/xml/features/".length();
         if (var2 == "internal/parser-settings".length() && var1.endsWith("internal/parser-settings")) {
            byte var3 = 1;
            throw new XMLConfigurationException(var3, var1);
         }
      }

      super.checkFeature(var1);
   }
}
