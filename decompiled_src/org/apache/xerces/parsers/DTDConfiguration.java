package org.apache.xerces.parsers;

import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.impl.XMLDTDScannerImpl;
import org.apache.xerces.impl.XMLDocumentScannerImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLNamespaceBinder;
import org.apache.xerces.impl.dtd.XMLDTDProcessor;
import org.apache.xerces.impl.dtd.XMLDTDValidator;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLDocumentScanner;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLPullParserConfiguration;

public class DTDConfiguration extends BasicParserConfiguration implements XMLPullParserConfiguration {
   protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
   protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
   protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
   protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
   protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
   protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
   protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   protected static final String DTD_PROCESSOR = "http://apache.org/xml/properties/internal/dtd-processor";
   protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
   protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   protected static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
   protected XMLGrammarPool fGrammarPool;
   protected DTDDVFactory fDatatypeValidatorFactory;
   protected XMLErrorReporter fErrorReporter;
   protected XMLEntityManager fEntityManager;
   protected XMLDocumentScanner fScanner;
   protected XMLInputSource fInputSource;
   protected XMLDTDScanner fDTDScanner;
   protected XMLDTDProcessor fDTDProcessor;
   protected XMLDTDValidator fDTDValidator;
   protected XMLNamespaceBinder fNamespaceBinder;
   protected ValidationManager fValidationManager;
   protected XMLLocator fLocator;
   protected boolean fParseInProgress;

   public DTDConfiguration() {
      this((SymbolTable)null, (XMLGrammarPool)null, (XMLComponentManager)null);
   }

   public DTDConfiguration(SymbolTable var1) {
      this(var1, (XMLGrammarPool)null, (XMLComponentManager)null);
   }

   public DTDConfiguration(SymbolTable var1, XMLGrammarPool var2) {
      this(var1, var2, (XMLComponentManager)null);
   }

   public DTDConfiguration(SymbolTable var1, XMLGrammarPool var2, XMLComponentManager var3) {
      super(var1, var3);
      this.fParseInProgress = false;
      String[] var4 = new String[]{"http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/nonvalidating/load-external-dtd"};
      this.addRecognizedFeatures(var4);
      this.setFeature("http://apache.org/xml/features/continue-after-fatal-error", false);
      this.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
      String[] var5 = new String[]{"http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/dtd-processor", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/namespace-binder", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage"};
      this.addRecognizedProperties(var5);
      this.fGrammarPool = var2;
      if (this.fGrammarPool != null) {
         this.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
      }

      this.fEntityManager = this.createEntityManager();
      this.setProperty("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
      this.addComponent(this.fEntityManager);
      this.fErrorReporter = this.createErrorReporter();
      this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
      this.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
      this.addComponent(this.fErrorReporter);
      this.fScanner = this.createDocumentScanner();
      this.setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fScanner);
      if (this.fScanner instanceof XMLComponent) {
         this.addComponent((XMLComponent)this.fScanner);
      }

      this.fDTDScanner = this.createDTDScanner();
      if (this.fDTDScanner != null) {
         this.setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
         if (this.fDTDScanner instanceof XMLComponent) {
            this.addComponent((XMLComponent)this.fDTDScanner);
         }
      }

      this.fDTDProcessor = this.createDTDProcessor();
      if (this.fDTDProcessor != null) {
         this.setProperty("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
         if (this.fDTDProcessor instanceof XMLComponent) {
            this.addComponent(this.fDTDProcessor);
         }
      }

      this.fDTDValidator = this.createDTDValidator();
      if (this.fDTDValidator != null) {
         this.setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fDTDValidator);
         this.addComponent(this.fDTDValidator);
      }

      this.fNamespaceBinder = this.createNamespaceBinder();
      if (this.fNamespaceBinder != null) {
         this.setProperty("http://apache.org/xml/properties/internal/namespace-binder", this.fNamespaceBinder);
         this.addComponent(this.fNamespaceBinder);
      }

      this.fDatatypeValidatorFactory = this.createDatatypeValidatorFactory();
      if (this.fDatatypeValidatorFactory != null) {
         this.setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
      }

      this.fValidationManager = this.createValidationManager();
      if (this.fValidationManager != null) {
         this.setProperty("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
      }

      if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
         XMLMessageFormatter var6 = new XMLMessageFormatter();
         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", var6);
         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", var6);
      }

      try {
         this.setLocale(Locale.getDefault());
      } catch (XNIException var7) {
      }

   }

   public void setLocale(Locale var1) throws XNIException {
      super.setLocale(var1);
      this.fErrorReporter.setLocale(var1);
   }

   public void setInputSource(XMLInputSource var1) throws XMLConfigurationException, IOException {
      this.fInputSource = var1;
   }

   public boolean parse(boolean var1) throws XNIException, IOException {
      if (this.fInputSource != null) {
         try {
            this.reset();
            this.fScanner.setInputSource(this.fInputSource);
            this.fInputSource = null;
         } catch (XNIException var10) {
            throw var10;
         } catch (IOException var11) {
            throw var11;
         } catch (RuntimeException var12) {
            throw var12;
         } catch (Exception var13) {
            throw new XNIException(var13);
         }
      }

      try {
         return this.fScanner.scanDocument(var1);
      } catch (XNIException var6) {
         throw var6;
      } catch (IOException var7) {
         throw var7;
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new XNIException(var9);
      }
   }

   public void cleanup() {
      this.fEntityManager.closeReaders();
   }

   public void parse(XMLInputSource var1) throws XNIException, IOException {
      if (this.fParseInProgress) {
         throw new XNIException("FWK005 parse may not be called while parsing.");
      } else {
         this.fParseInProgress = true;

         try {
            this.setInputSource(var1);
            this.parse(true);
         } catch (XNIException var13) {
            throw var13;
         } catch (IOException var14) {
            throw var14;
         } catch (RuntimeException var15) {
            throw var15;
         } catch (Exception var16) {
            throw new XNIException(var16);
         } finally {
            this.fParseInProgress = false;
            this.cleanup();
         }

      }
   }

   protected void reset() throws XNIException {
      if (this.fValidationManager != null) {
         this.fValidationManager.reset();
      }

      this.configurePipeline();
      super.reset();
   }

   protected void configurePipeline() {
      if (this.fDTDValidator != null) {
         this.fScanner.setDocumentHandler(this.fDTDValidator);
         if (super.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
            this.fDTDValidator.setDocumentHandler(this.fNamespaceBinder);
            this.fDTDValidator.setDocumentSource(this.fScanner);
            this.fNamespaceBinder.setDocumentHandler(super.fDocumentHandler);
            this.fNamespaceBinder.setDocumentSource(this.fDTDValidator);
            super.fLastComponent = this.fNamespaceBinder;
         } else {
            this.fDTDValidator.setDocumentHandler(super.fDocumentHandler);
            this.fDTDValidator.setDocumentSource(this.fScanner);
            super.fLastComponent = this.fDTDValidator;
         }
      } else if (super.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
         this.fScanner.setDocumentHandler(this.fNamespaceBinder);
         this.fNamespaceBinder.setDocumentHandler(super.fDocumentHandler);
         this.fNamespaceBinder.setDocumentSource(this.fScanner);
         super.fLastComponent = this.fNamespaceBinder;
      } else {
         this.fScanner.setDocumentHandler(super.fDocumentHandler);
         super.fLastComponent = this.fScanner;
      }

      this.configureDTDPipeline();
   }

   protected void configureDTDPipeline() {
      if (this.fDTDScanner != null) {
         super.fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
         if (this.fDTDProcessor != null) {
            super.fProperties.put("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
            this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
            this.fDTDProcessor.setDTDSource(this.fDTDScanner);
            this.fDTDProcessor.setDTDHandler(super.fDTDHandler);
            if (super.fDTDHandler != null) {
               super.fDTDHandler.setDTDSource(this.fDTDProcessor);
            }

            this.fDTDScanner.setDTDContentModelHandler(this.fDTDProcessor);
            this.fDTDProcessor.setDTDContentModelSource(this.fDTDScanner);
            this.fDTDProcessor.setDTDContentModelHandler(super.fDTDContentModelHandler);
            if (super.fDTDContentModelHandler != null) {
               super.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDProcessor);
            }
         } else {
            this.fDTDScanner.setDTDHandler(super.fDTDHandler);
            if (super.fDTDHandler != null) {
               super.fDTDHandler.setDTDSource(this.fDTDScanner);
            }

            this.fDTDScanner.setDTDContentModelHandler(super.fDTDContentModelHandler);
            if (super.fDTDContentModelHandler != null) {
               super.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDScanner);
            }
         }
      }

   }

   protected void checkFeature(String var1) throws XMLConfigurationException {
      if (var1.startsWith("http://apache.org/xml/features/")) {
         int var2 = var1.length() - "http://apache.org/xml/features/".length();
         if (var2 == "validation/dynamic".length() && var1.endsWith("validation/dynamic")) {
            return;
         }

         byte var3;
         if (var2 == "validation/default-attribute-values".length() && var1.endsWith("validation/default-attribute-values")) {
            var3 = 1;
            throw new XMLConfigurationException(var3, var1);
         }

         if (var2 == "validation/validate-content-models".length() && var1.endsWith("validation/validate-content-models")) {
            var3 = 1;
            throw new XMLConfigurationException(var3, var1);
         }

         if (var2 == "nonvalidating/load-dtd-grammar".length() && var1.endsWith("nonvalidating/load-dtd-grammar")) {
            return;
         }

         if (var2 == "nonvalidating/load-external-dtd".length() && var1.endsWith("nonvalidating/load-external-dtd")) {
            return;
         }

         if (var2 == "validation/validate-datatypes".length() && var1.endsWith("validation/validate-datatypes")) {
            var3 = 1;
            throw new XMLConfigurationException(var3, var1);
         }
      }

      super.checkFeature(var1);
   }

   protected void checkProperty(String var1) throws XMLConfigurationException {
      if (var1.startsWith("http://apache.org/xml/properties/")) {
         int var2 = var1.length() - "http://apache.org/xml/properties/".length();
         if (var2 == "internal/dtd-scanner".length() && var1.endsWith("internal/dtd-scanner")) {
            return;
         }
      }

      super.checkProperty(var1);
   }

   protected XMLEntityManager createEntityManager() {
      return new XMLEntityManager();
   }

   protected XMLErrorReporter createErrorReporter() {
      return new XMLErrorReporter();
   }

   protected XMLDocumentScanner createDocumentScanner() {
      return new XMLDocumentScannerImpl();
   }

   protected XMLDTDScanner createDTDScanner() {
      return new XMLDTDScannerImpl();
   }

   protected XMLDTDProcessor createDTDProcessor() {
      return new XMLDTDProcessor();
   }

   protected XMLDTDValidator createDTDValidator() {
      return new XMLDTDValidator();
   }

   protected XMLNamespaceBinder createNamespaceBinder() {
      return new XMLNamespaceBinder();
   }

   protected DTDDVFactory createDatatypeValidatorFactory() {
      return DTDDVFactory.getInstance();
   }

   protected ValidationManager createValidationManager() {
      return new ValidationManager();
   }
}
