package org.apache.xerces.dom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.ls.LSResourceResolver;

public class DOMConfigurationImpl extends ParserConfigurationSettings implements XMLParserConfiguration, DOMConfiguration {
   protected static final String XERCES_VALIDATION = "http://xml.org/sax/features/validation";
   protected static final String XERCES_NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String SCHEMA = "http://apache.org/xml/features/validation/schema";
   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
   protected static final String SEND_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
   protected static final String DTD_VALIDATOR_FACTORY_PROPERTY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   XMLDocumentHandler fDocumentHandler;
   protected short features;
   protected static final short NAMESPACES = 1;
   protected static final short DTNORMALIZATION = 2;
   protected static final short ENTITIES = 4;
   protected static final short CDATA = 8;
   protected static final short SPLITCDATA = 16;
   protected static final short COMMENTS = 32;
   protected static final short VALIDATE = 64;
   protected static final short PSVI = 128;
   protected static final short WELLFORMED = 256;
   protected static final short NSDECL = 512;
   protected static final short INFOSET_TRUE_PARAMS = 801;
   protected static final short INFOSET_FALSE_PARAMS = 14;
   protected static final short INFOSET_MASK = 815;
   protected SymbolTable fSymbolTable;
   protected ArrayList fComponents;
   protected ValidationManager fValidationManager;
   protected Locale fLocale;
   protected XMLErrorReporter fErrorReporter;
   protected final DOMErrorHandlerWrapper fErrorHandlerWrapper;
   private DOMStringList fRecognizedParameters;

   protected DOMConfigurationImpl() {
      this((SymbolTable)null, (XMLComponentManager)null);
   }

   protected DOMConfigurationImpl(SymbolTable var1) {
      this(var1, (XMLComponentManager)null);
   }

   protected DOMConfigurationImpl(SymbolTable var1, XMLComponentManager var2) {
      super(var2);
      this.features = 0;
      this.fErrorHandlerWrapper = new DOMErrorHandlerWrapper();
      super.fRecognizedFeatures = new ArrayList();
      super.fRecognizedProperties = new ArrayList();
      super.fFeatures = new HashMap();
      super.fProperties = new HashMap();
      String[] var3 = new String[]{"http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/schema/normalized-value", "http://apache.org/xml/features/validation/schema/augment-psvi"};
      this.addRecognizedFeatures(var3);
      this.setFeature("http://xml.org/sax/features/validation", false);
      this.setFeature("http://apache.org/xml/features/validation/schema", false);
      this.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
      this.setFeature("http://apache.org/xml/features/validation/dynamic", false);
      this.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
      this.setFeature("http://xml.org/sax/features/namespaces", true);
      this.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
      String[] var4 = new String[]{"http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/internal/grammar-pool", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/internal/datatype-validator-factory"};
      this.addRecognizedProperties(var4);
      this.features = (short)(this.features | 1);
      this.features = (short)(this.features | 4);
      this.features = (short)(this.features | 32);
      this.features = (short)(this.features | 8);
      this.features = (short)(this.features | 16);
      this.features = (short)(this.features | 256);
      this.features = (short)(this.features | 512);
      if (var1 == null) {
         var1 = new SymbolTable();
      }

      this.fSymbolTable = var1;
      this.fComponents = new ArrayList();
      this.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
      this.fErrorReporter = new XMLErrorReporter();
      this.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
      this.addComponent(this.fErrorReporter);
      this.setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", DTDDVFactory.getInstance());
      XMLEntityManager var5 = new XMLEntityManager();
      this.setProperty("http://apache.org/xml/properties/internal/entity-manager", var5);
      this.addComponent(var5);
      this.fValidationManager = this.createValidationManager();
      this.setProperty("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
      if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
         XMLMessageFormatter var6 = new XMLMessageFormatter();
         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", var6);
         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", var6);
      }

      if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
         MessageFormatter var10 = null;

         try {
            var10 = (MessageFormatter)ObjectFactory.newInstance("org.apache.xerces.impl.xs.XSMessageFormatter", ObjectFactory.findClassLoader(), true);
         } catch (Exception var9) {
         }

         if (var10 != null) {
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", var10);
         }
      }

      try {
         this.setLocale(Locale.getDefault());
      } catch (XNIException var8) {
      }

   }

   public void parse(XMLInputSource var1) throws XNIException, IOException {
   }

   public void setDocumentHandler(XMLDocumentHandler var1) {
      this.fDocumentHandler = var1;
   }

   public XMLDocumentHandler getDocumentHandler() {
      return this.fDocumentHandler;
   }

   public void setDTDHandler(XMLDTDHandler var1) {
   }

   public XMLDTDHandler getDTDHandler() {
      return null;
   }

   public void setDTDContentModelHandler(XMLDTDContentModelHandler var1) {
   }

   public XMLDTDContentModelHandler getDTDContentModelHandler() {
      return null;
   }

   public void setEntityResolver(XMLEntityResolver var1) {
      if (var1 != null) {
         super.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", var1);
      }

   }

   public XMLEntityResolver getEntityResolver() {
      return (XMLEntityResolver)super.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
   }

   public void setErrorHandler(XMLErrorHandler var1) {
      if (var1 != null) {
         super.fProperties.put("http://apache.org/xml/properties/internal/error-handler", var1);
      }

   }

   public XMLErrorHandler getErrorHandler() {
      return (XMLErrorHandler)super.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      super.setFeature(var1, var2);
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      super.setProperty(var1, var2);
   }

   public void setLocale(Locale var1) throws XNIException {
      this.fLocale = var1;
      this.fErrorReporter.setLocale(var1);
   }

   public Locale getLocale() {
      return this.fLocale;
   }

   public void setParameter(String var1, Object var2) throws DOMException {
      boolean var3 = true;
      if (var2 instanceof Boolean) {
         boolean var4 = (Boolean)var2;
         if (var1.equalsIgnoreCase("comments")) {
            this.features = var4 ? (short)(this.features | 32) : (short)(this.features & -33);
         } else if (var1.equalsIgnoreCase("datatype-normalization")) {
            this.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", var4);
            this.features = var4 ? (short)(this.features | 2) : (short)(this.features & -3);
            if (var4) {
               this.features = (short)(this.features | 64);
            }
         } else if (var1.equalsIgnoreCase("namespaces")) {
            this.features = var4 ? (short)(this.features | 1) : (short)(this.features & -2);
         } else if (var1.equalsIgnoreCase("cdata-sections")) {
            this.features = var4 ? (short)(this.features | 8) : (short)(this.features & -9);
         } else if (var1.equalsIgnoreCase("entities")) {
            this.features = var4 ? (short)(this.features | 4) : (short)(this.features & -5);
         } else if (var1.equalsIgnoreCase("split-cdata-sections")) {
            this.features = var4 ? (short)(this.features | 16) : (short)(this.features & -17);
         } else if (var1.equalsIgnoreCase("validate")) {
            this.features = var4 ? (short)(this.features | 64) : (short)(this.features & -65);
         } else if (var1.equalsIgnoreCase("well-formed")) {
            this.features = var4 ? (short)(this.features | 256) : (short)(this.features & -257);
         } else if (var1.equalsIgnoreCase("namespace-declarations")) {
            this.features = var4 ? (short)(this.features | 512) : (short)(this.features & -513);
         } else if (var1.equalsIgnoreCase("infoset")) {
            if (var4) {
               this.features = (short)(this.features | 801);
               this.features = (short)(this.features & -15);
               this.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
            }
         } else {
            String var5;
            if (!var1.equalsIgnoreCase("normalize-characters") && !var1.equalsIgnoreCase("canonical-form") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("check-character-normalization")) {
               if (var1.equalsIgnoreCase("element-content-whitespace")) {
                  if (!var4) {
                     var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
                     throw new DOMException((short)9, var5);
                  }
               } else if (var1.equalsIgnoreCase("http://apache.org/xml/features/validation/schema/augment-psvi")) {
                  if (!var4) {
                     var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
                     throw new DOMException((short)9, var5);
                  }
               } else if (var1.equalsIgnoreCase("psvi")) {
                  this.features = var4 ? (short)(this.features | 128) : (short)(this.features & -129);
               } else {
                  var3 = false;
               }
            } else if (var4) {
               var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
               throw new DOMException((short)9, var5);
            }
         }
      }

      if (!var3 || !(var2 instanceof Boolean)) {
         var3 = true;
         String var9;
         if (var1.equalsIgnoreCase("error-handler")) {
            if (!(var2 instanceof DOMErrorHandler) && var2 != null) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var9);
            }

            this.fErrorHandlerWrapper.setErrorHandler((DOMErrorHandler)var2);
            this.setErrorHandler(this.fErrorHandlerWrapper);
         } else if (var1.equalsIgnoreCase("resource-resolver")) {
            if (!(var2 instanceof LSResourceResolver) && var2 != null) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var9);
            }

            try {
               this.setEntityResolver(new DOMEntityResolverWrapper((LSResourceResolver)var2));
            } catch (XMLConfigurationException var8) {
            }
         } else if (var1.equalsIgnoreCase("schema-location")) {
            if (!(var2 instanceof String) && var2 != null) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var9);
            }

            try {
               this.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", var2);
            } catch (XMLConfigurationException var7) {
            }
         } else if (var1.equalsIgnoreCase("schema-type")) {
            if (!(var2 instanceof String) && var2 != null) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var9);
            }

            try {
               if (var2 == null) {
                  this.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", (Object)null);
               } else if (var2.equals(Constants.NS_XMLSCHEMA)) {
                  this.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
               } else if (var2.equals(Constants.NS_DTD)) {
                  this.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
               }
            } catch (XMLConfigurationException var6) {
            }
         } else if (var1.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
            if (!(var2 instanceof SymbolTable)) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var9);
            }

            this.setProperty("http://apache.org/xml/properties/internal/symbol-table", var2);
         } else {
            if (!var1.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
               throw new DOMException((short)8, var9);
            }

            if (!(var2 instanceof XMLGrammarPool)) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var9);
            }

            this.setProperty("http://apache.org/xml/properties/internal/grammar-pool", var2);
         }
      }

   }

   public Object getParameter(String var1) throws DOMException {
      if (var1.equalsIgnoreCase("comments")) {
         return (this.features & 32) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("namespaces")) {
         return (this.features & 1) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("datatype-normalization")) {
         return (this.features & 2) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("cdata-sections")) {
         return (this.features & 8) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("entities")) {
         return (this.features & 4) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("split-cdata-sections")) {
         return (this.features & 16) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("validate")) {
         return (this.features & 64) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("well-formed")) {
         return (this.features & 256) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("namespace-declarations")) {
         return (this.features & 512) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("infoset")) {
         return (this.features & 815) == 801 ? Boolean.TRUE : Boolean.FALSE;
      } else if (!var1.equalsIgnoreCase("normalize-characters") && !var1.equalsIgnoreCase("canonical-form") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("check-character-normalization")) {
         if (var1.equalsIgnoreCase("http://apache.org/xml/features/validation/schema/augment-psvi")) {
            return Boolean.TRUE;
         } else if (var1.equalsIgnoreCase("psvi")) {
            return (this.features & 128) != 0 ? Boolean.TRUE : Boolean.FALSE;
         } else if (var1.equalsIgnoreCase("element-content-whitespace")) {
            return Boolean.TRUE;
         } else if (var1.equalsIgnoreCase("error-handler")) {
            return this.fErrorHandlerWrapper.getErrorHandler();
         } else if (var1.equalsIgnoreCase("resource-resolver")) {
            XMLEntityResolver var3 = this.getEntityResolver();
            return var3 != null && var3 instanceof DOMEntityResolverWrapper ? ((DOMEntityResolverWrapper)var3).getEntityResolver() : null;
         } else if (var1.equalsIgnoreCase("schema-type")) {
            return this.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
         } else if (var1.equalsIgnoreCase("schema-location")) {
            return this.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
         } else if (var1.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
            return this.getProperty("http://apache.org/xml/properties/internal/symbol-table");
         } else if (var1.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
            return this.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
         } else {
            String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
            throw new DOMException((short)8, var2);
         }
      } else {
         return Boolean.FALSE;
      }
   }

   public boolean canSetParameter(String var1, Object var2) {
      if (var2 == null) {
         return true;
      } else if (var2 instanceof Boolean) {
         if (!var1.equalsIgnoreCase("comments") && !var1.equalsIgnoreCase("datatype-normalization") && !var1.equalsIgnoreCase("cdata-sections") && !var1.equalsIgnoreCase("entities") && !var1.equalsIgnoreCase("split-cdata-sections") && !var1.equalsIgnoreCase("namespaces") && !var1.equalsIgnoreCase("validate") && !var1.equalsIgnoreCase("well-formed") && !var1.equalsIgnoreCase("infoset") && !var1.equalsIgnoreCase("namespace-declarations")) {
            if (!var1.equalsIgnoreCase("normalize-characters") && !var1.equalsIgnoreCase("canonical-form") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("check-character-normalization")) {
               if (!var1.equalsIgnoreCase("element-content-whitespace") && !var1.equalsIgnoreCase("http://apache.org/xml/features/validation/schema/augment-psvi")) {
                  return false;
               } else {
                  return var2.equals(Boolean.TRUE);
               }
            } else {
               return !var2.equals(Boolean.TRUE);
            }
         } else {
            return true;
         }
      } else if (var1.equalsIgnoreCase("error-handler")) {
         return var2 instanceof DOMErrorHandler;
      } else if (var1.equalsIgnoreCase("resource-resolver")) {
         return var2 instanceof LSResourceResolver;
      } else if (var1.equalsIgnoreCase("schema-location")) {
         return var2 instanceof String;
      } else if (!var1.equalsIgnoreCase("schema-type")) {
         if (var1.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
            return var2 instanceof SymbolTable;
         } else if (var1.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
            return var2 instanceof XMLGrammarPool;
         } else {
            return false;
         }
      } else {
         return var2 instanceof String && var2.equals(Constants.NS_XMLSCHEMA);
      }
   }

   public DOMStringList getParameterNames() {
      if (this.fRecognizedParameters == null) {
         Vector var1 = new Vector();
         var1.add("comments");
         var1.add("datatype-normalization");
         var1.add("cdata-sections");
         var1.add("entities");
         var1.add("split-cdata-sections");
         var1.add("namespaces");
         var1.add("validate");
         var1.add("infoset");
         var1.add("normalize-characters");
         var1.add("canonical-form");
         var1.add("validate-if-schema");
         var1.add("check-character-normalization");
         var1.add("well-formed");
         var1.add("namespace-declarations");
         var1.add("element-content-whitespace");
         var1.add("error-handler");
         var1.add("schema-type");
         var1.add("schema-location");
         var1.add("resource-resolver");
         var1.add("http://apache.org/xml/properties/internal/grammar-pool");
         var1.add("http://apache.org/xml/properties/internal/symbol-table");
         var1.add("http://apache.org/xml/features/validation/schema/augment-psvi");
         this.fRecognizedParameters = new DOMStringListImpl(var1);
      }

      return this.fRecognizedParameters;
   }

   protected void reset() throws XNIException {
      if (this.fValidationManager != null) {
         this.fValidationManager.reset();
      }

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

   protected void addComponent(XMLComponent var1) {
      if (!this.fComponents.contains(var1)) {
         this.fComponents.add(var1);
         String[] var2 = var1.getRecognizedFeatures();
         this.addRecognizedFeatures(var2);
         String[] var3 = var1.getRecognizedProperties();
         this.addRecognizedProperties(var3);
      }
   }

   protected ValidationManager createValidationManager() {
      return new ValidationManager();
   }
}
