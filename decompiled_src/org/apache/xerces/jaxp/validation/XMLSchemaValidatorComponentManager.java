package org.apache.xerces.jaxp.validation;

import java.util.HashMap;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;

final class XMLSchemaValidatorComponentManager extends ParserConfigurationSettings implements XMLComponentManager {
   private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
   private static final String VALIDATION = "http://xml.org/sax/features/validation";
   private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
   private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
   private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   private boolean fConfigUpdated = true;
   private boolean fUseGrammarPoolOnly;
   private final HashMap fComponents = new HashMap();
   private XMLEntityManager fEntityManager = new XMLEntityManager();
   private XMLErrorReporter fErrorReporter;
   private NamespaceContext fNamespaceContext;
   private XMLSchemaValidator fSchemaValidator;
   private ValidationManager fValidationManager;
   private ErrorHandler fErrorHandler = null;
   private LSResourceResolver fResourceResolver = null;

   public XMLSchemaValidatorComponentManager(XSGrammarPoolContainer var1) {
      this.fComponents.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
      this.fErrorReporter = new XMLErrorReporter();
      this.fComponents.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
      this.fNamespaceContext = new NamespaceSupport();
      this.fComponents.put("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
      this.fSchemaValidator = new XMLSchemaValidator();
      this.fComponents.put("http://apache.org/xml/properties/internal/validator/schema", this.fSchemaValidator);
      this.fValidationManager = new ValidationManager();
      this.fComponents.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
      this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", (Object)null);
      this.fComponents.put("http://apache.org/xml/properties/internal/error-handler", (Object)null);
      this.fComponents.put("http://apache.org/xml/properties/security-manager", (Object)null);
      this.fComponents.put("http://apache.org/xml/properties/internal/symbol-table", new SymbolTable());
      this.fComponents.put("http://apache.org/xml/properties/internal/grammar-pool", var1.getGrammarPool());
      this.fUseGrammarPoolOnly = var1.isFullyComposed();
      this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
      this.addRecognizedParamsAndSetDefaults(this.fEntityManager);
      this.addRecognizedParamsAndSetDefaults(this.fErrorReporter);
      this.addRecognizedParamsAndSetDefaults(this.fSchemaValidator);
   }

   public boolean getFeature(String var1) throws XMLConfigurationException {
      if ("http://apache.org/xml/features/internal/parser-settings".equals(var1)) {
         return this.fConfigUpdated;
      } else if (!"http://xml.org/sax/features/validation".equals(var1) && !"http://apache.org/xml/features/validation/schema".equals(var1)) {
         if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(var1)) {
            return this.fUseGrammarPoolOnly;
         } else if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(var1)) {
            return this.getProperty("http://apache.org/xml/properties/security-manager") != null;
         } else {
            return super.getFeature(var1);
         }
      } else {
         return true;
      }
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      if ("http://apache.org/xml/features/internal/parser-settings".equals(var1)) {
         throw new XMLConfigurationException((short)1, var1);
      } else if (var2 || !"http://xml.org/sax/features/validation".equals(var1) && !"http://apache.org/xml/features/validation/schema".equals(var1)) {
         if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(var1) && var2 != this.fUseGrammarPoolOnly) {
            throw new XMLConfigurationException((short)1, var1);
         } else {
            this.fConfigUpdated = true;
            if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(var1)) {
               this.setProperty("http://apache.org/xml/properties/security-manager", var2 ? new SecurityManager() : null);
            } else {
               this.fEntityManager.setFeature(var1, var2);
               this.fErrorReporter.setFeature(var1, var2);
               this.fSchemaValidator.setFeature(var1, var2);
               super.setFeature(var1, var2);
            }
         }
      } else {
         throw new XMLConfigurationException((short)1, var1);
      }
   }

   public Object getProperty(String var1) throws XMLConfigurationException {
      Object var2 = this.fComponents.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         return this.fComponents.containsKey(var1) ? null : super.getProperty(var1);
      }
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (!"http://apache.org/xml/properties/internal/entity-manager".equals(var1) && !"http://apache.org/xml/properties/internal/error-reporter".equals(var1) && !"http://apache.org/xml/properties/internal/namespace-context".equals(var1) && !"http://apache.org/xml/properties/internal/validator/schema".equals(var1) && !"http://apache.org/xml/properties/internal/symbol-table".equals(var1) && !"http://apache.org/xml/properties/internal/validation-manager".equals(var1) && !"http://apache.org/xml/properties/internal/grammar-pool".equals(var1)) {
         this.fConfigUpdated = true;
         this.fEntityManager.setProperty(var1, var2);
         this.fErrorReporter.setProperty(var1, var2);
         this.fSchemaValidator.setProperty(var1, var2);
         if (!"http://apache.org/xml/properties/internal/entity-resolver".equals(var1) && !"http://apache.org/xml/properties/internal/error-handler".equals(var1) && !"http://apache.org/xml/properties/security-manager".equals(var1)) {
            super.setProperty(var1, var2);
         } else {
            this.fComponents.put(var1, var2);
         }
      } else {
         throw new XMLConfigurationException((short)1, var1);
      }
   }

   public void addRecognizedParamsAndSetDefaults(XMLComponent var1) {
      String[] var2 = var1.getRecognizedFeatures();
      this.addRecognizedFeatures(var2);
      String[] var3 = var1.getRecognizedProperties();
      this.addRecognizedProperties(var3);
      this.setFeatureDefaults(var1, var2);
      this.setPropertyDefaults(var1, var3);
   }

   public void reset() throws XNIException {
      this.fNamespaceContext.reset();
      this.fValidationManager.reset();
      this.fEntityManager.reset(this);
      this.fErrorReporter.reset(this);
      this.fSchemaValidator.reset(this);
      this.fConfigUpdated = false;
   }

   void setErrorHandler(ErrorHandler var1) {
      this.fErrorHandler = var1;
      this.setProperty("http://apache.org/xml/properties/internal/error-handler", var1 != null ? new ErrorHandlerWrapper(var1) : new ErrorHandlerWrapper(DraconianErrorHandler.getInstance()));
   }

   ErrorHandler getErrorHandler() {
      return this.fErrorHandler;
   }

   void setResourceResolver(LSResourceResolver var1) {
      this.fResourceResolver = var1;
      this.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper(var1));
   }

   public LSResourceResolver getResourceResolver() {
      return this.fResourceResolver;
   }

   void restoreInitialState() {
      this.fConfigUpdated = true;
      super.fFeatures.clear();
      super.fProperties.clear();
      this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", (Object)null);
      this.fComponents.put("http://apache.org/xml/properties/internal/error-handler", (Object)null);
      this.setFeatureDefaults(this.fEntityManager, this.fEntityManager.getRecognizedFeatures());
      this.setPropertyDefaults(this.fEntityManager, this.fEntityManager.getRecognizedProperties());
      this.setFeatureDefaults(this.fErrorReporter, this.fErrorReporter.getRecognizedFeatures());
      this.setPropertyDefaults(this.fErrorReporter, this.fErrorReporter.getRecognizedProperties());
      this.setFeatureDefaults(this.fSchemaValidator, this.fSchemaValidator.getRecognizedFeatures());
      this.setPropertyDefaults(this.fSchemaValidator, this.fSchemaValidator.getRecognizedProperties());
   }

   private void setFeatureDefaults(XMLComponent var1, String[] var2) {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3];
            Boolean var5 = var1.getFeatureDefault(var4);
            if (var5 != null && !super.fFeatures.containsKey(var4)) {
               super.fFeatures.put(var4, var5);
               this.fConfigUpdated = true;
            }
         }
      }

   }

   private void setPropertyDefaults(XMLComponent var1, String[] var2) {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3];
            Object var5 = var1.getPropertyDefault(var4);
            if (var5 != null && !super.fProperties.containsKey(var4)) {
               super.fProperties.put(var4, var5);
               this.fConfigUpdated = true;
            }
         }
      }

   }
}
