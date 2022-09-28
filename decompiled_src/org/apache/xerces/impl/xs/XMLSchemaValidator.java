package org.apache.xerces.impl.xs;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.DatatypeException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.identity.Field;
import org.apache.xerces.impl.xs.identity.FieldActivator;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.KeyRef;
import org.apache.xerces.impl.xs.identity.Selector;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.impl.xs.identity.ValueStore;
import org.apache.xerces.impl.xs.identity.XPathMatcher;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.CMNodeFactory;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.IntStack;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
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
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XMLSchemaValidator implements XMLComponent, XMLDocumentFilter, FieldActivator, RevalidationHandler {
   private static final boolean DEBUG = false;
   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
   protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
   protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
   protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
   protected static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
   protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations", "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only"};
   private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null, null, null, null, null, null, null, null, null, null, null};
   private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage"};
   private static final Object[] PROPERTY_DEFAULTS = new Object[]{null, null, null, null, null, null, null, null};
   protected static final int ID_CONSTRAINT_NUM = 1;
   protected ElementPSVImpl fCurrentPSVI = new ElementPSVImpl();
   protected final AugmentationsImpl fAugmentations = new AugmentationsImpl();
   protected final HashMap fMayMatchFieldMap = new HashMap();
   protected XMLString fDefaultValue;
   protected boolean fDynamicValidation = false;
   protected boolean fSchemaDynamicValidation = false;
   protected boolean fDoValidation = false;
   protected boolean fFullChecking = false;
   protected boolean fNormalizeData = true;
   protected boolean fSchemaElementDefault = true;
   protected boolean fAugPSVI = true;
   protected boolean fIdConstraint = false;
   protected boolean fUseGrammarPoolOnly = false;
   private String fSchemaType = null;
   protected boolean fEntityRef = false;
   protected boolean fInCDATA = false;
   protected SymbolTable fSymbolTable;
   private XMLLocator fLocator;
   protected final XSIErrorReporter fXSIErrorReporter = new XSIErrorReporter();
   protected XMLEntityResolver fEntityResolver;
   protected ValidationManager fValidationManager = null;
   protected ValidationState fValidationState = new ValidationState();
   protected XMLGrammarPool fGrammarPool;
   protected String fExternalSchemas = null;
   protected String fExternalNoNamespaceSchema = null;
   protected Object fJaxpSchemaSource = null;
   protected final XSDDescription fXSDDescription = new XSDDescription();
   protected final Hashtable fLocationPairs = new Hashtable();
   protected XMLDocumentHandler fDocumentHandler;
   protected XMLDocumentSource fDocumentSource;
   static final int INITIAL_STACK_SIZE = 8;
   static final int INC_STACK_SIZE = 8;
   private static final boolean DEBUG_NORMALIZATION = false;
   private final XMLString fEmptyXMLStr = new XMLString((char[])null, 0, -1);
   private static final int BUFFER_SIZE = 20;
   private final XMLString fNormalizedStr = new XMLString();
   private boolean fFirstChunk = true;
   private boolean fTrailing = false;
   private short fWhiteSpace = -1;
   private boolean fUnionType = false;
   private final XSGrammarBucket fGrammarBucket = new XSGrammarBucket();
   private final SubstitutionGroupHandler fSubGroupHandler;
   private final XSSimpleType fQNameDV;
   private final CMNodeFactory nodeFactory;
   private final CMBuilder fCMBuilder;
   private final XMLSchemaLoader fSchemaLoader;
   private String fValidationRoot;
   private int fSkipValidationDepth;
   private int fNFullValidationDepth;
   private int fNNoneValidationDepth;
   private int fElementDepth;
   private boolean fSubElement;
   private boolean[] fSubElementStack;
   private XSElementDecl fCurrentElemDecl;
   private XSElementDecl[] fElemDeclStack;
   private boolean fNil;
   private boolean[] fNilStack;
   private XSNotationDecl fNotation;
   private XSNotationDecl[] fNotationStack;
   private XSTypeDefinition fCurrentType;
   private XSTypeDefinition[] fTypeStack;
   private XSCMValidator fCurrentCM;
   private XSCMValidator[] fCMStack;
   private int[] fCurrCMState;
   private int[][] fCMStateStack;
   private boolean fStrictAssess;
   private boolean[] fStrictAssessStack;
   private final StringBuffer fBuffer;
   private boolean fAppendBuffer;
   private boolean fSawText;
   private boolean[] fSawTextStack;
   private boolean fSawCharacters;
   private boolean[] fStringContent;
   private final QName fTempQName;
   private ValidatedInfo fValidatedInfo;
   private ValidationState fState4XsiType;
   private ValidationState fState4ApplyDefault;
   protected XPathMatcherStack fMatcherStack;
   protected ValueStoreCache fValueStoreCache;

   public String[] getRecognizedFeatures() {
      return (String[])RECOGNIZED_FEATURES.clone();
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
   }

   public String[] getRecognizedProperties() {
      return (String[])RECOGNIZED_PROPERTIES.clone();
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
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

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
      this.fValidationState.setNamespaceSupport(var3);
      this.fState4XsiType.setNamespaceSupport(var3);
      this.fState4ApplyDefault.setNamespaceSupport(var3);
      this.fLocator = var1;
      this.handleStartDocument(var1, var2);
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startDocument(var1, var2, var3, var4);
      }

   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.xmlDecl(var1, var2, var3, var4);
      }

   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.doctypeDecl(var1, var2, var3, var4);
      }

   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      Augmentations var4 = this.handleStartElement(var1, var2, var3);
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startElement(var1, var2, var4);
      }

   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      Augmentations var4 = this.handleStartElement(var1, var2, var3);
      this.fDefaultValue = null;
      if (this.fElementDepth != -2) {
         var4 = this.handleEndElement(var1, var4);
      }

      if (this.fDocumentHandler != null) {
         if (this.fSchemaElementDefault && this.fDefaultValue != null) {
            this.fDocumentHandler.startElement(var1, var2, var4);
            this.fDocumentHandler.characters(this.fDefaultValue, (Augmentations)null);
            this.fDocumentHandler.endElement(var1, var4);
         } else {
            this.fDocumentHandler.emptyElement(var1, var2, var4);
         }
      }

   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      var1 = this.handleCharacters(var1);
      if (this.fDocumentHandler != null) {
         if (this.fNormalizeData && this.fUnionType) {
            if (var2 != null) {
               this.fDocumentHandler.characters(this.fEmptyXMLStr, var2);
            }
         } else {
            this.fDocumentHandler.characters(var1, var2);
         }
      }

   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      this.handleIgnorableWhitespace(var1);
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.ignorableWhitespace(var1, var2);
      }

   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      this.fDefaultValue = null;
      Augmentations var3 = this.handleEndElement(var1, var2);
      if (this.fDocumentHandler != null) {
         if (this.fSchemaElementDefault && this.fDefaultValue != null) {
            this.fDocumentHandler.characters(this.fDefaultValue, (Augmentations)null);
            this.fDocumentHandler.endElement(var1, var3);
         } else {
            this.fDocumentHandler.endElement(var1, var3);
         }
      }

   }

   public void startCDATA(Augmentations var1) throws XNIException {
      this.fInCDATA = true;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startCDATA(var1);
      }

   }

   public void endCDATA(Augmentations var1) throws XNIException {
      this.fInCDATA = false;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endCDATA(var1);
      }

   }

   public void endDocument(Augmentations var1) throws XNIException {
      this.handleEndDocument();
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endDocument(var1);
      }

      this.fLocator = null;
   }

   public boolean characterData(String var1, Augmentations var2) {
      this.fSawText = this.fSawText || var1.length() > 0;
      if (this.fNormalizeData && this.fWhiteSpace != -1 && this.fWhiteSpace != 0) {
         this.normalizeWhitespace(var1, this.fWhiteSpace == 2);
         this.fBuffer.append(this.fNormalizedStr.ch, this.fNormalizedStr.offset, this.fNormalizedStr.length);
      } else if (this.fAppendBuffer) {
         this.fBuffer.append(var1);
      }

      boolean var3 = true;
      if (this.fCurrentType != null && this.fCurrentType.getTypeCategory() == 15) {
         XSComplexTypeDecl var4 = (XSComplexTypeDecl)this.fCurrentType;
         if (var4.fContentType == 2) {
            for(int var5 = 0; var5 < var1.length(); ++var5) {
               if (!XMLChar.isSpace(var1.charAt(var5))) {
                  var3 = false;
                  this.fSawCharacters = true;
                  break;
               }
            }
         }
      }

      return var3;
   }

   public void elementDefault(String var1) {
   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      this.fEntityRef = true;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startGeneralEntity(var1, var2, var3, var4);
      }

   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.textDecl(var1, var2, var3);
      }

   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.comment(var1, var2);
      }

   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.processingInstruction(var1, var2, var3);
      }

   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
      this.fEntityRef = false;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endGeneralEntity(var1, var2);
      }

   }

   public XMLSchemaValidator() {
      this.fSubGroupHandler = new SubstitutionGroupHandler(this.fGrammarBucket);
      this.fQNameDV = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("QName");
      this.nodeFactory = new CMNodeFactory();
      this.fCMBuilder = new CMBuilder(this.nodeFactory);
      this.fSchemaLoader = new XMLSchemaLoader(this.fXSIErrorReporter.fErrorReporter, this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder);
      this.fSubElementStack = new boolean[8];
      this.fElemDeclStack = new XSElementDecl[8];
      this.fNilStack = new boolean[8];
      this.fNotationStack = new XSNotationDecl[8];
      this.fTypeStack = new XSTypeDefinition[8];
      this.fCMStack = new XSCMValidator[8];
      this.fCMStateStack = new int[8][];
      this.fStrictAssess = true;
      this.fStrictAssessStack = new boolean[8];
      this.fBuffer = new StringBuffer();
      this.fAppendBuffer = true;
      this.fSawText = false;
      this.fSawTextStack = new boolean[8];
      this.fSawCharacters = false;
      this.fStringContent = new boolean[8];
      this.fTempQName = new QName();
      this.fValidatedInfo = new ValidatedInfo();
      this.fState4XsiType = new ValidationState();
      this.fState4ApplyDefault = new ValidationState();
      this.fMatcherStack = new XPathMatcherStack();
      this.fValueStoreCache = new ValueStoreCache();
      this.fState4XsiType.setExtraChecking(false);
      this.fState4ApplyDefault.setFacetChecking(false);
   }

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      this.fIdConstraint = false;
      this.fLocationPairs.clear();
      this.fValidationState.resetIDTables();
      this.nodeFactory.reset(var1);
      this.fSchemaLoader.reset(var1);
      this.fCurrentElemDecl = null;
      this.fCurrentCM = null;
      this.fCurrCMState = null;
      this.fSkipValidationDepth = -1;
      this.fNFullValidationDepth = -1;
      this.fNNoneValidationDepth = -1;
      this.fElementDepth = -1;
      this.fSubElement = false;
      this.fSchemaDynamicValidation = false;
      this.fEntityRef = false;
      this.fInCDATA = false;
      this.fMatcherStack.clear();
      if (!this.fMayMatchFieldMap.isEmpty()) {
         this.fMayMatchFieldMap.clear();
      }

      this.fXSIErrorReporter.reset((XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter"));

      boolean var2;
      try {
         var2 = var1.getFeature("http://apache.org/xml/features/internal/parser-settings");
      } catch (XMLConfigurationException var17) {
         var2 = true;
      }

      if (!var2) {
         this.fValidationManager.addValidationState(this.fValidationState);
         XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
      } else {
         SymbolTable var3 = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");
         if (var3 != this.fSymbolTable) {
            this.fSymbolTable = var3;
         }

         try {
            this.fDynamicValidation = var1.getFeature("http://apache.org/xml/features/validation/dynamic");
         } catch (XMLConfigurationException var16) {
            this.fDynamicValidation = false;
         }

         if (this.fDynamicValidation) {
            this.fDoValidation = true;
         } else {
            try {
               this.fDoValidation = var1.getFeature("http://xml.org/sax/features/validation");
            } catch (XMLConfigurationException var15) {
               this.fDoValidation = false;
            }
         }

         if (this.fDoValidation) {
            try {
               this.fDoValidation = var1.getFeature("http://apache.org/xml/features/validation/schema");
            } catch (XMLConfigurationException var14) {
            }
         }

         try {
            this.fFullChecking = var1.getFeature("http://apache.org/xml/features/validation/schema-full-checking");
         } catch (XMLConfigurationException var13) {
            this.fFullChecking = false;
         }

         try {
            this.fNormalizeData = var1.getFeature("http://apache.org/xml/features/validation/schema/normalized-value");
         } catch (XMLConfigurationException var12) {
            this.fNormalizeData = false;
         }

         try {
            this.fSchemaElementDefault = var1.getFeature("http://apache.org/xml/features/validation/schema/element-default");
         } catch (XMLConfigurationException var11) {
            this.fSchemaElementDefault = false;
         }

         try {
            this.fAugPSVI = var1.getFeature("http://apache.org/xml/features/validation/schema/augment-psvi");
         } catch (XMLConfigurationException var10) {
            this.fAugPSVI = true;
         }

         try {
            this.fSchemaType = (String)var1.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
         } catch (XMLConfigurationException var9) {
            this.fSchemaType = null;
         }

         try {
            this.fUseGrammarPoolOnly = var1.getFeature("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only");
         } catch (XMLConfigurationException var8) {
            this.fUseGrammarPoolOnly = false;
         }

         this.fEntityResolver = (XMLEntityResolver)var1.getProperty("http://apache.org/xml/properties/internal/entity-manager");
         this.fValidationManager = (ValidationManager)var1.getProperty("http://apache.org/xml/properties/internal/validation-manager");
         this.fValidationManager.addValidationState(this.fValidationState);
         this.fValidationState.setSymbolTable(this.fSymbolTable);

         try {
            this.fExternalSchemas = (String)var1.getProperty("http://apache.org/xml/properties/schema/external-schemaLocation");
            this.fExternalNoNamespaceSchema = (String)var1.getProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation");
         } catch (XMLConfigurationException var7) {
            this.fExternalSchemas = null;
            this.fExternalNoNamespaceSchema = null;
         }

         XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);

         try {
            this.fJaxpSchemaSource = var1.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
         } catch (XMLConfigurationException var6) {
            this.fJaxpSchemaSource = null;
         }

         try {
            this.fGrammarPool = (XMLGrammarPool)var1.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
         } catch (XMLConfigurationException var5) {
            this.fGrammarPool = null;
         }

         this.fState4XsiType.setSymbolTable(var3);
         this.fState4ApplyDefault.setSymbolTable(var3);
      }
   }

   public void startValueScopeFor(IdentityConstraint var1, int var2) {
      ValueStoreBase var3 = this.fValueStoreCache.getValueStoreFor(var1, var2);
      var3.startValueScope();
   }

   public XPathMatcher activateField(Field var1, int var2) {
      ValueStoreBase var3 = this.fValueStoreCache.getValueStoreFor(var1.getIdentityConstraint(), var2);
      this.setMayMatch(var1, Boolean.TRUE);
      XPathMatcher var4 = var1.createMatcher(this, var3);
      this.fMatcherStack.addMatcher(var4);
      var4.startDocumentFragment();
      return var4;
   }

   public void endValueScopeFor(IdentityConstraint var1, int var2) {
      ValueStoreBase var3 = this.fValueStoreCache.getValueStoreFor(var1, var2);
      var3.endValueScope();
   }

   public void setMayMatch(Field var1, Boolean var2) {
      this.fMayMatchFieldMap.put(var1, var2);
   }

   public Boolean mayMatch(Field var1) {
      return (Boolean)this.fMayMatchFieldMap.get(var1);
   }

   private void activateSelectorFor(IdentityConstraint var1) {
      Selector var2 = var1.getSelector();
      if (var2 != null) {
         XPathMatcher var4 = var2.createMatcher(this, this.fElementDepth);
         this.fMatcherStack.addMatcher(var4);
         var4.startDocumentFragment();
      }
   }

   void ensureStackCapacity() {
      if (this.fElementDepth == this.fElemDeclStack.length) {
         int var1 = this.fElementDepth + 8;
         boolean[] var2 = new boolean[var1];
         System.arraycopy(this.fSubElementStack, 0, var2, 0, this.fElementDepth);
         this.fSubElementStack = var2;
         XSElementDecl[] var3 = new XSElementDecl[var1];
         System.arraycopy(this.fElemDeclStack, 0, var3, 0, this.fElementDepth);
         this.fElemDeclStack = var3;
         var2 = new boolean[var1];
         System.arraycopy(this.fNilStack, 0, var2, 0, this.fElementDepth);
         this.fNilStack = var2;
         XSNotationDecl[] var4 = new XSNotationDecl[var1];
         System.arraycopy(this.fNotationStack, 0, var4, 0, this.fElementDepth);
         this.fNotationStack = var4;
         XSTypeDefinition[] var5 = new XSTypeDefinition[var1];
         System.arraycopy(this.fTypeStack, 0, var5, 0, this.fElementDepth);
         this.fTypeStack = var5;
         XSCMValidator[] var6 = new XSCMValidator[var1];
         System.arraycopy(this.fCMStack, 0, var6, 0, this.fElementDepth);
         this.fCMStack = var6;
         var2 = new boolean[var1];
         System.arraycopy(this.fSawTextStack, 0, var2, 0, this.fElementDepth);
         this.fSawTextStack = var2;
         var2 = new boolean[var1];
         System.arraycopy(this.fStringContent, 0, var2, 0, this.fElementDepth);
         this.fStringContent = var2;
         var2 = new boolean[var1];
         System.arraycopy(this.fStrictAssessStack, 0, var2, 0, this.fElementDepth);
         this.fStrictAssessStack = var2;
         int[][] var7 = new int[var1][];
         System.arraycopy(this.fCMStateStack, 0, var7, 0, this.fElementDepth);
         this.fCMStateStack = var7;
      }

   }

   void handleStartDocument(XMLLocator var1, String var2) {
      this.fValueStoreCache.startDocument();
      if (this.fAugPSVI) {
         this.fCurrentPSVI.fGrammars = null;
         this.fCurrentPSVI.fSchemaInformation = null;
      }

   }

   void handleEndDocument() {
      this.fValueStoreCache.endDocument();
   }

   XMLString handleCharacters(XMLString var1) {
      if (this.fSkipValidationDepth >= 0) {
         return var1;
      } else {
         this.fSawText = this.fSawText || var1.length > 0;
         if (this.fNormalizeData && this.fWhiteSpace != -1 && this.fWhiteSpace != 0) {
            this.normalizeWhitespace(var1, this.fWhiteSpace == 2);
            var1 = this.fNormalizedStr;
         }

         if (this.fAppendBuffer) {
            this.fBuffer.append(var1.ch, var1.offset, var1.length);
         }

         if (this.fCurrentType != null && this.fCurrentType.getTypeCategory() == 15) {
            XSComplexTypeDecl var2 = (XSComplexTypeDecl)this.fCurrentType;
            if (var2.fContentType == 2) {
               for(int var3 = var1.offset; var3 < var1.offset + var1.length; ++var3) {
                  if (!XMLChar.isSpace(var1.ch[var3])) {
                     this.fSawCharacters = true;
                     break;
                  }
               }
            }
         }

         return var1;
      }
   }

   private void normalizeWhitespace(XMLString var1, boolean var2) {
      boolean var3 = var2;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      int var8 = var1.offset + var1.length;
      if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < var1.length + 1) {
         this.fNormalizedStr.ch = new char[var1.length + 1];
      }

      this.fNormalizedStr.offset = 1;
      this.fNormalizedStr.length = 1;

      for(int var9 = var1.offset; var9 < var8; ++var9) {
         char var7 = var1.ch[var9];
         if (XMLChar.isSpace(var7)) {
            if (!var3) {
               this.fNormalizedStr.ch[this.fNormalizedStr.length++] = ' ';
               var3 = var2;
            }

            if (!var4) {
               var5 = true;
            }
         } else {
            this.fNormalizedStr.ch[this.fNormalizedStr.length++] = var7;
            var3 = false;
            var4 = true;
         }
      }

      if (var3) {
         if (this.fNormalizedStr.length > 1) {
            --this.fNormalizedStr.length;
            var6 = true;
         } else if (var5 && !this.fFirstChunk) {
            var6 = true;
         }
      }

      if (this.fNormalizedStr.length > 1 && !this.fFirstChunk && this.fWhiteSpace == 2) {
         if (this.fTrailing) {
            this.fNormalizedStr.offset = 0;
            this.fNormalizedStr.ch[0] = ' ';
         } else if (var5) {
            this.fNormalizedStr.offset = 0;
            this.fNormalizedStr.ch[0] = ' ';
         }
      }

      XMLString var10000 = this.fNormalizedStr;
      var10000.length -= this.fNormalizedStr.offset;
      this.fTrailing = var6;
      if (var6 || var4) {
         this.fFirstChunk = false;
      }

   }

   private void normalizeWhitespace(String var1, boolean var2) {
      boolean var3 = var2;
      int var5 = var1.length();
      if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < var5) {
         this.fNormalizedStr.ch = new char[var5];
      }

      this.fNormalizedStr.offset = 0;
      this.fNormalizedStr.length = 0;

      for(int var6 = 0; var6 < var5; ++var6) {
         char var4 = var1.charAt(var6);
         if (XMLChar.isSpace(var4)) {
            if (!var3) {
               this.fNormalizedStr.ch[this.fNormalizedStr.length++] = ' ';
               var3 = var2;
            }
         } else {
            this.fNormalizedStr.ch[this.fNormalizedStr.length++] = var4;
            var3 = false;
         }
      }

      if (var3 && this.fNormalizedStr.length != 0) {
         --this.fNormalizedStr.length;
      }

   }

   void handleIgnorableWhitespace(XMLString var1) {
      if (this.fSkipValidationDepth < 0) {
         ;
      }
   }

   Augmentations handleStartElement(QName var1, XMLAttributes var2, Augmentations var3) {
      if (this.fElementDepth == -1 && this.fValidationManager.isGrammarFound() && this.fSchemaType == null) {
         this.fSchemaDynamicValidation = true;
      }

      String var4 = var2.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_SCHEMALOCATION);
      String var5 = var2.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
      this.storeLocations(var4, var5);
      if (this.fSkipValidationDepth >= 0) {
         ++this.fElementDepth;
         if (this.fAugPSVI) {
            var3 = this.getEmptyAugs(var3);
         }

         return var3;
      } else {
         SchemaGrammar var6 = this.findSchemaGrammar((short)5, var1.uri, (QName)null, var1, var2);
         Object var7 = null;
         String var10;
         if (this.fCurrentCM != null) {
            var7 = this.fCurrentCM.oneTransition(var1, this.fCurrCMState, this.fSubGroupHandler);
            if (this.fCurrCMState[0] == -1) {
               XSComplexTypeDecl var8 = (XSComplexTypeDecl)this.fCurrentType;
               Vector var9;
               if (var8.fParticle != null && (var9 = this.fCurrentCM.whatCanGoHere(this.fCurrCMState)).size() > 0) {
                  var10 = this.expectedStr(var9);
                  this.reportSchemaError("cvc-complex-type.2.4.a", new Object[]{var1.rawname, var10});
               } else {
                  this.reportSchemaError("cvc-complex-type.2.4.d", new Object[]{var1.rawname});
               }
            }
         }

         if (this.fElementDepth != -1) {
            this.ensureStackCapacity();
            this.fSubElementStack[this.fElementDepth] = true;
            this.fSubElement = false;
            this.fElemDeclStack[this.fElementDepth] = this.fCurrentElemDecl;
            this.fNilStack[this.fElementDepth] = this.fNil;
            this.fNotationStack[this.fElementDepth] = this.fNotation;
            this.fTypeStack[this.fElementDepth] = this.fCurrentType;
            this.fStrictAssessStack[this.fElementDepth] = this.fStrictAssess;
            this.fCMStack[this.fElementDepth] = this.fCurrentCM;
            this.fCMStateStack[this.fElementDepth] = this.fCurrCMState;
            this.fSawTextStack[this.fElementDepth] = this.fSawText;
            this.fStringContent[this.fElementDepth] = this.fSawCharacters;
         }

         ++this.fElementDepth;
         this.fCurrentElemDecl = null;
         XSWildcardDecl var17 = null;
         this.fCurrentType = null;
         this.fStrictAssess = true;
         this.fNil = false;
         this.fNotation = null;
         this.fBuffer.setLength(0);
         this.fSawText = false;
         this.fSawCharacters = false;
         if (var7 != null) {
            if (var7 instanceof XSElementDecl) {
               this.fCurrentElemDecl = (XSElementDecl)var7;
            } else {
               var17 = (XSWildcardDecl)var7;
            }
         }

         if (var17 != null && var17.fProcessContents == 2) {
            this.fSkipValidationDepth = this.fElementDepth;
            if (this.fAugPSVI) {
               var3 = this.getEmptyAugs(var3);
            }

            return var3;
         } else {
            if (this.fCurrentElemDecl == null && var6 != null) {
               this.fCurrentElemDecl = var6.getGlobalElementDecl(var1.localpart);
            }

            if (this.fCurrentElemDecl != null) {
               this.fCurrentType = this.fCurrentElemDecl.fType;
            }

            String var18 = var2.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_TYPE);
            XSComplexTypeDecl var20;
            if (this.fCurrentType == null && var18 == null) {
               if (this.fElementDepth == 0) {
                  if (this.fDynamicValidation || this.fSchemaDynamicValidation) {
                     if (this.fDocumentSource != null) {
                        this.fDocumentSource.setDocumentHandler(this.fDocumentHandler);
                        if (this.fDocumentHandler != null) {
                           this.fDocumentHandler.setDocumentSource(this.fDocumentSource);
                        }

                        this.fElementDepth = -2;
                        return var3;
                     }

                     this.fSkipValidationDepth = this.fElementDepth;
                     if (this.fAugPSVI) {
                        var3 = this.getEmptyAugs(var3);
                     }

                     return var3;
                  }

                  this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "cvc-elt.1", new Object[]{var1.rawname}, (short)1);
               } else if (var17 != null && var17.fProcessContents == 1) {
                  this.reportSchemaError("cvc-complex-type.2.4.c", new Object[]{var1.rawname});
               }

               this.fCurrentType = SchemaGrammar.fAnyType;
               this.fStrictAssess = false;
               this.fNFullValidationDepth = this.fElementDepth;
               this.fAppendBuffer = false;
               this.fXSIErrorReporter.pushContext();
            } else {
               this.fXSIErrorReporter.pushContext();
               if (var18 != null) {
                  XSTypeDefinition var19 = this.fCurrentType;
                  this.fCurrentType = this.getAndCheckXsiType(var1, var18, var2);
                  if (this.fCurrentType == null) {
                     if (var19 == null) {
                        this.fCurrentType = SchemaGrammar.fAnyType;
                     } else {
                        this.fCurrentType = var19;
                     }
                  }
               }

               this.fNNoneValidationDepth = this.fElementDepth;
               if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2) {
                  this.fAppendBuffer = true;
               } else if (this.fCurrentType.getTypeCategory() == 16) {
                  this.fAppendBuffer = true;
               } else {
                  var20 = (XSComplexTypeDecl)this.fCurrentType;
                  this.fAppendBuffer = var20.fContentType == 1;
               }
            }

            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getAbstract()) {
               this.reportSchemaError("cvc-elt.2", new Object[]{var1.rawname});
            }

            if (this.fElementDepth == 0) {
               this.fValidationRoot = var1.rawname;
            }

            if (this.fNormalizeData) {
               this.fFirstChunk = true;
               this.fTrailing = false;
               this.fUnionType = false;
               this.fWhiteSpace = -1;
            }

            if (this.fCurrentType.getTypeCategory() == 15) {
               var20 = (XSComplexTypeDecl)this.fCurrentType;
               if (var20.getAbstract()) {
                  this.reportSchemaError("cvc-type.2", new Object[]{var1.rawname});
               }

               if (this.fNormalizeData && var20.fContentType == 1) {
                  if (var20.fXSSimpleType.getVariety() == 3) {
                     this.fUnionType = true;
                  } else {
                     try {
                        this.fWhiteSpace = var20.fXSSimpleType.getWhitespace();
                     } catch (DatatypeException var16) {
                     }
                  }
               }
            } else if (this.fNormalizeData) {
               XSSimpleType var21 = (XSSimpleType)this.fCurrentType;
               if (var21.getVariety() == 3) {
                  this.fUnionType = true;
               } else {
                  try {
                     this.fWhiteSpace = var21.getWhitespace();
                  } catch (DatatypeException var15) {
                  }
               }
            }

            this.fCurrentCM = null;
            if (this.fCurrentType.getTypeCategory() == 15) {
               this.fCurrentCM = ((XSComplexTypeDecl)this.fCurrentType).getContentModel(this.fCMBuilder);
            }

            this.fCurrCMState = null;
            if (this.fCurrentCM != null) {
               this.fCurrCMState = this.fCurrentCM.startContentModel();
            }

            var10 = var2.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NIL);
            if (var10 != null && this.fCurrentElemDecl != null) {
               this.fNil = this.getXsiNil(var1, var10);
            }

            XSAttributeGroupDecl var11 = null;
            if (this.fCurrentType.getTypeCategory() == 15) {
               XSComplexTypeDecl var12 = (XSComplexTypeDecl)this.fCurrentType;
               var11 = var12.getAttrGrp();
            }

            this.fValueStoreCache.startElement();
            this.fMatcherStack.pushContext();
            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.fIDCPos > 0) {
               this.fIdConstraint = true;
               this.fValueStoreCache.initValueStoresFor(this.fCurrentElemDecl, this);
            }

            this.processAttributes(var1, var2, var11);
            if (var11 != null) {
               this.addDefaultAttributes(var1, var2, var11);
            }

            int var22 = this.fMatcherStack.getMatcherCount();

            for(int var13 = 0; var13 < var22; ++var13) {
               XPathMatcher var14 = this.fMatcherStack.getMatcherAt(var13);
               var14.startElement(var1, var2);
            }

            if (this.fAugPSVI) {
               var3 = this.getEmptyAugs(var3);
               this.fCurrentPSVI.fValidationContext = this.fValidationRoot;
               this.fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
               this.fCurrentPSVI.fTypeDecl = this.fCurrentType;
               this.fCurrentPSVI.fNotation = this.fNotation;
            }

            return var3;
         }
      }
   }

   Augmentations handleEndElement(QName var1, Augmentations var2) {
      if (this.fSkipValidationDepth < 0) {
         this.processElementContent(var1);
         int var3 = this.fMatcherStack.getMatcherCount();

         for(int var4 = var3 - 1; var4 >= 0; --var4) {
            XPathMatcher var5 = this.fMatcherStack.getMatcherAt(var4);
            if (this.fCurrentElemDecl == null) {
               var5.endElement(var1, (XSTypeDefinition)null, false, this.fValidatedInfo.actualValue, this.fValidatedInfo.actualValueType, this.fValidatedInfo.itemValueTypes);
            } else {
               var5.endElement(var1, this.fCurrentType, this.fCurrentElemDecl.getNillable(), this.fDefaultValue == null ? this.fValidatedInfo.actualValue : this.fCurrentElemDecl.fDefault.actualValue, this.fDefaultValue == null ? this.fValidatedInfo.actualValueType : this.fCurrentElemDecl.fDefault.actualValueType, this.fDefaultValue == null ? this.fValidatedInfo.itemValueTypes : this.fCurrentElemDecl.fDefault.itemValueTypes);
            }
         }

         if (this.fMatcherStack.size() > 0) {
            this.fMatcherStack.popContext();
         }

         int var12 = this.fMatcherStack.getMatcherCount();

         Selector.Matcher var8;
         for(int var6 = var3 - 1; var6 >= var12; --var6) {
            XPathMatcher var7 = this.fMatcherStack.getMatcherAt(var6);
            if (var7 instanceof Selector.Matcher) {
               var8 = (Selector.Matcher)var7;
               IdentityConstraint var9;
               if ((var9 = var8.getIdentityConstraint()) != null && var9.getCategory() != 2) {
                  this.fValueStoreCache.transplant(var9, var8.getInitialDepth());
               }
            }
         }

         for(int var13 = var3 - 1; var13 >= var12; --var13) {
            XPathMatcher var14 = this.fMatcherStack.getMatcherAt(var13);
            if (var14 instanceof Selector.Matcher) {
               Selector.Matcher var16 = (Selector.Matcher)var14;
               IdentityConstraint var10;
               if ((var10 = var16.getIdentityConstraint()) != null && var10.getCategory() == 2) {
                  ValueStoreBase var11 = this.fValueStoreCache.getValueStoreFor(var10, var16.getInitialDepth());
                  if (var11 != null) {
                     var11.endDocumentFragment();
                  }
               }
            }
         }

         this.fValueStoreCache.endElement();
         var8 = null;
         if (this.fElementDepth == 0) {
            String var17 = this.fValidationState.checkIDRefID();
            this.fValidationState.resetIDTables();
            if (var17 != null) {
               this.reportSchemaError("cvc-id.1", new Object[]{var17});
            }

            if (this.fFullChecking) {
               XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
            }

            SchemaGrammar[] var15 = this.fGrammarBucket.getGrammars();
            if (this.fGrammarPool != null) {
               this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", var15);
            }

            var2 = this.endElementPSVI(true, var15, var2);
         } else {
            var2 = this.endElementPSVI(false, var8, var2);
            --this.fElementDepth;
            this.fSubElement = this.fSubElementStack[this.fElementDepth];
            this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
            this.fNil = this.fNilStack[this.fElementDepth];
            this.fNotation = this.fNotationStack[this.fElementDepth];
            this.fCurrentType = this.fTypeStack[this.fElementDepth];
            this.fCurrentCM = this.fCMStack[this.fElementDepth];
            this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
            this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
            this.fSawText = this.fSawTextStack[this.fElementDepth];
            this.fSawCharacters = this.fStringContent[this.fElementDepth];
            this.fWhiteSpace = -1;
            this.fAppendBuffer = false;
            this.fUnionType = false;
         }

         return var2;
      } else {
         if (this.fSkipValidationDepth == this.fElementDepth && this.fSkipValidationDepth > 0) {
            this.fNFullValidationDepth = this.fSkipValidationDepth - 1;
            this.fSkipValidationDepth = -1;
            --this.fElementDepth;
            this.fSubElement = this.fSubElementStack[this.fElementDepth];
            this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
            this.fNil = this.fNilStack[this.fElementDepth];
            this.fNotation = this.fNotationStack[this.fElementDepth];
            this.fCurrentType = this.fTypeStack[this.fElementDepth];
            this.fCurrentCM = this.fCMStack[this.fElementDepth];
            this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
            this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
            this.fSawText = this.fSawTextStack[this.fElementDepth];
            this.fSawCharacters = this.fStringContent[this.fElementDepth];
         } else {
            --this.fElementDepth;
         }

         if (this.fElementDepth == -1 && this.fFullChecking) {
            XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
         }

         if (this.fAugPSVI) {
            var2 = this.getEmptyAugs(var2);
         }

         return var2;
      }
   }

   final Augmentations endElementPSVI(boolean var1, SchemaGrammar[] var2, Augmentations var3) {
      if (this.fAugPSVI) {
         var3 = this.getEmptyAugs(var3);
         this.fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
         this.fCurrentPSVI.fTypeDecl = this.fCurrentType;
         this.fCurrentPSVI.fNotation = this.fNotation;
         this.fCurrentPSVI.fValidationContext = this.fValidationRoot;
         if (this.fElementDepth > this.fNFullValidationDepth) {
            this.fCurrentPSVI.fValidationAttempted = 2;
         } else if (this.fElementDepth > this.fNNoneValidationDepth) {
            this.fCurrentPSVI.fValidationAttempted = 0;
         } else {
            this.fCurrentPSVI.fValidationAttempted = 1;
            this.fNFullValidationDepth = this.fNNoneValidationDepth = this.fElementDepth - 1;
         }

         if (this.fDefaultValue != null) {
            this.fCurrentPSVI.fSpecified = true;
         }

         this.fCurrentPSVI.fNil = this.fNil;
         this.fCurrentPSVI.fMemberType = this.fValidatedInfo.memberType;
         this.fCurrentPSVI.fNormalizedValue = this.fValidatedInfo.normalizedValue;
         this.fCurrentPSVI.fActualValue = this.fValidatedInfo.actualValue;
         this.fCurrentPSVI.fActualValueType = this.fValidatedInfo.actualValueType;
         this.fCurrentPSVI.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
         if (this.fStrictAssess) {
            String[] var4 = this.fXSIErrorReporter.mergeContext();
            this.fCurrentPSVI.fErrorCodes = var4;
            this.fCurrentPSVI.fValidity = (short)(var4 == null ? 2 : 1);
         } else {
            this.fCurrentPSVI.fValidity = 0;
            this.fXSIErrorReporter.popContext();
         }

         if (var1) {
            this.fCurrentPSVI.fGrammars = var2;
            this.fCurrentPSVI.fSchemaInformation = null;
         }
      }

      return var3;
   }

   Augmentations getEmptyAugs(Augmentations var1) {
      if (var1 == null) {
         var1 = this.fAugmentations;
         ((Augmentations)var1).removeAllItems();
      }

      ((Augmentations)var1).putItem("ELEMENT_PSVI", this.fCurrentPSVI);
      this.fCurrentPSVI.reset();
      return (Augmentations)var1;
   }

   void storeLocations(String var1, String var2) {
      if (var1 != null && !XMLSchemaLoader.tokenizeSchemaLocationStr(var1, this.fLocationPairs)) {
         this.fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "SchemaLocation", new Object[]{var1}, (short)0);
      }

      if (var2 != null) {
         XMLSchemaLoader.LocationArray var3 = (XMLSchemaLoader.LocationArray)this.fLocationPairs.get(XMLSymbols.EMPTY_STRING);
         if (var3 == null) {
            var3 = new XMLSchemaLoader.LocationArray();
            this.fLocationPairs.put(XMLSymbols.EMPTY_STRING, var3);
         }

         var3.addLocation(var2);
      }

   }

   SchemaGrammar findSchemaGrammar(short var1, String var2, QName var3, QName var4, XMLAttributes var5) {
      SchemaGrammar var6 = null;
      var6 = this.fGrammarBucket.getGrammar(var2);
      if (var6 == null) {
         this.fXSDDescription.reset();
         this.fXSDDescription.fContextType = var1;
         this.fXSDDescription.setNamespace(var2);
         this.fXSDDescription.fEnclosedElementName = var3;
         this.fXSDDescription.fTriggeringComponent = var4;
         this.fXSDDescription.fAttributes = var5;
         if (this.fLocator != null) {
            this.fXSDDescription.setBaseSystemId(this.fLocator.getExpandedSystemId());
         }

         String[] var7 = null;
         Object var8 = this.fLocationPairs.get(var2 == null ? XMLSymbols.EMPTY_STRING : var2);
         if (var8 != null) {
            var7 = ((XMLSchemaLoader.LocationArray)var8).getLocationArray();
         }

         if (var7 != null && var7.length != 0) {
            this.fXSDDescription.fLocationHints = new String[var7.length];
            System.arraycopy(var7, 0, this.fXSDDescription.fLocationHints, 0, var7.length);
         }

         if (this.fGrammarPool != null) {
            var6 = (SchemaGrammar)this.fGrammarPool.retrieveGrammar(this.fXSDDescription);
            if (var6 != null && !this.fGrammarBucket.putGrammar(var6, true)) {
               this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "GrammarConflict", (Object[])null, (short)0);
               var6 = null;
            }
         }

         if (var6 == null && !this.fUseGrammarPoolOnly) {
            try {
               XMLInputSource var9 = XMLSchemaLoader.resolveDocument(this.fXSDDescription, this.fLocationPairs, this.fEntityResolver);
               var6 = this.fSchemaLoader.loadSchema(this.fXSDDescription, var9, this.fLocationPairs);
            } catch (IOException var11) {
               String[] var10 = this.fXSDDescription.getLocationHints();
               this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", new Object[]{var10 != null ? var10[0] : XMLSymbols.EMPTY_STRING}, (short)0);
            }
         }
      }

      return var6;
   }

   XSTypeDefinition getAndCheckXsiType(QName var1, String var2, XMLAttributes var3) {
      QName var4 = null;

      try {
         var4 = (QName)this.fQNameDV.validate((String)var2, this.fValidationState, (ValidatedInfo)null);
      } catch (InvalidDatatypeValueException var7) {
         this.reportSchemaError(var7.getKey(), var7.getArgs());
         this.reportSchemaError("cvc-elt.4.1", new Object[]{var1.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_TYPE, var2});
         return null;
      }

      XSTypeDefinition var5 = null;
      if (var4.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
         var5 = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(var4.localpart);
      }

      if (var5 == null) {
         SchemaGrammar var6 = this.findSchemaGrammar((short)7, var4.uri, var1, var4, var3);
         if (var6 != null) {
            var5 = var6.getGlobalTypeDecl(var4.localpart);
         }
      }

      if (var5 == null) {
         this.reportSchemaError("cvc-elt.4.2", new Object[]{var1.rawname, var2});
         return null;
      } else {
         if (this.fCurrentType != null) {
            short var8 = this.fCurrentElemDecl.fBlock;
            if (this.fCurrentType.getTypeCategory() == 15) {
               var8 |= ((XSComplexTypeDecl)this.fCurrentType).fBlock;
            }

            if (!XSConstraints.checkTypeDerivationOk(var5, this.fCurrentType, var8)) {
               this.reportSchemaError("cvc-elt.4.3", new Object[]{var1.rawname, var2, this.fCurrentType.getName()});
            }
         }

         return var5;
      }
   }

   boolean getXsiNil(QName var1, String var2) {
      if (this.fCurrentElemDecl != null && !this.fCurrentElemDecl.getNillable()) {
         this.reportSchemaError("cvc-elt.3.1", new Object[]{var1.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
      } else {
         String var3 = var2.trim();
         if (var3.equals("true") || var3.equals("1")) {
            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2) {
               this.reportSchemaError("cvc-elt.3.2.2", new Object[]{var1.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
            }

            return true;
         }
      }

      return false;
   }

   void processAttributes(QName var1, XMLAttributes var2, XSAttributeGroupDecl var3) {
      String var4 = null;
      int var5 = var2.getLength();
      Augmentations var6 = null;
      AttributePSVImpl var7 = null;
      boolean var8 = this.fCurrentType == null || this.fCurrentType.getTypeCategory() == 16;
      XSObjectList var9 = null;
      int var10 = 0;
      XSWildcardDecl var11 = null;
      if (!var8) {
         var9 = var3.getAttributeUses();
         var10 = var9.getLength();
         var11 = var3.fAttributeWC;
      }

      for(int var12 = 0; var12 < var5; ++var12) {
         var2.getName(var12, this.fTempQName);
         if (this.fAugPSVI || this.fIdConstraint) {
            var6 = var2.getAugmentations(var12);
            var7 = (AttributePSVImpl)var6.getItem("ATTRIBUTE_PSVI");
            if (var7 != null) {
               var7.reset();
            } else {
               var7 = new AttributePSVImpl();
               var6.putItem("ATTRIBUTE_PSVI", var7);
            }

            var7.fValidationContext = this.fValidationRoot;
         }

         if (this.fTempQName.uri == SchemaSymbols.URI_XSI) {
            XSAttributeDecl var13 = null;
            if (this.fTempQName.localpart == SchemaSymbols.XSI_SCHEMALOCATION) {
               var13 = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
            } else if (this.fTempQName.localpart == SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION) {
               var13 = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
            } else if (this.fTempQName.localpart == SchemaSymbols.XSI_NIL) {
               var13 = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NIL);
            } else if (this.fTempQName.localpart == SchemaSymbols.XSI_TYPE) {
               var13 = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_TYPE);
            }

            if (var13 != null) {
               this.processOneAttribute(var1, var2, var12, var13, (XSAttributeUseImpl)null, var7);
               continue;
            }
         }

         if (this.fTempQName.rawname != XMLSymbols.PREFIX_XMLNS && !this.fTempQName.rawname.startsWith("xmlns:")) {
            if (var8) {
               this.reportSchemaError("cvc-type.3.1.1", new Object[]{var1.rawname, this.fTempQName.rawname});
            } else {
               XSAttributeUseImpl var18 = null;

               for(int var15 = 0; var15 < var10; ++var15) {
                  XSAttributeUseImpl var14 = (XSAttributeUseImpl)var9.item(var15);
                  if (var14.fAttrDecl.fName == this.fTempQName.localpart && var14.fAttrDecl.fTargetNamespace == this.fTempQName.uri) {
                     var18 = var14;
                     break;
                  }
               }

               if (var18 != null || var11 != null && var11.allowNamespace(this.fTempQName.uri)) {
                  XSAttributeDecl var16 = null;
                  if (var18 != null) {
                     var16 = var18.fAttrDecl;
                  } else {
                     if (var11.fProcessContents == 2) {
                        continue;
                     }

                     SchemaGrammar var17 = this.findSchemaGrammar((short)6, this.fTempQName.uri, var1, this.fTempQName, var2);
                     if (var17 != null) {
                        var16 = var17.getGlobalAttributeDecl(this.fTempQName.localpart);
                     }

                     if (var16 == null) {
                        if (var11.fProcessContents == 1) {
                           this.reportSchemaError("cvc-complex-type.3.2.2", new Object[]{var1.rawname, this.fTempQName.rawname});
                        }
                        continue;
                     }

                     if (var16.fType.getTypeCategory() == 16 && var16.fType.isIDType()) {
                        if (var4 != null) {
                           this.reportSchemaError("cvc-complex-type.5.1", new Object[]{var1.rawname, var16.fName, var4});
                        } else {
                           var4 = var16.fName;
                        }
                     }
                  }

                  this.processOneAttribute(var1, var2, var12, var16, var18, var7);
               } else {
                  this.reportSchemaError("cvc-complex-type.3.2.2", new Object[]{var1.rawname, this.fTempQName.rawname});
               }
            }
         }
      }

      if (!var8 && var3.fIDAttrName != null && var4 != null) {
         this.reportSchemaError("cvc-complex-type.5.2", new Object[]{var1.rawname, var4, var3.fIDAttrName});
      }

   }

   void processOneAttribute(QName var1, XMLAttributes var2, int var3, XSAttributeDecl var4, XSAttributeUseImpl var5, AttributePSVImpl var6) {
      String var7 = var2.getValue(var3);
      this.fXSIErrorReporter.pushContext();
      XSSimpleType var8 = var4.fType;
      Object var9 = null;

      try {
         var9 = var8.validate((String)var7, this.fValidationState, this.fValidatedInfo);
         if (this.fNormalizeData) {
            var2.setValue(var3, this.fValidatedInfo.normalizedValue);
         }

         if (var2 instanceof XMLAttributesImpl) {
            XMLAttributesImpl var10 = (XMLAttributesImpl)var2;
            boolean var11 = this.fValidatedInfo.memberType != null ? this.fValidatedInfo.memberType.isIDType() : var8.isIDType();
            var10.setSchemaId(var3, var11);
         }

         if (var8.getVariety() == 1 && var8.getPrimitiveKind() == 20) {
            QName var13 = (QName)var9;
            SchemaGrammar var15 = this.fGrammarBucket.getGrammar(var13.uri);
            if (var15 != null) {
               this.fNotation = var15.getGlobalNotationDecl(var13.localpart);
            }
         }
      } catch (InvalidDatatypeValueException var12) {
         this.reportSchemaError(var12.getKey(), var12.getArgs());
         this.reportSchemaError("cvc-attribute.3", new Object[]{var1.rawname, this.fTempQName.rawname, var7, var8.getName()});
      }

      if (var9 != null && var4.getConstraintType() == 2 && (!this.isComparable(this.fValidatedInfo, var4.fDefault) || !var9.equals(var4.fDefault.actualValue))) {
         this.reportSchemaError("cvc-attribute.4", new Object[]{var1.rawname, this.fTempQName.rawname, var7, var4.fDefault.stringValue()});
      }

      if (var9 != null && var5 != null && var5.fConstraintType == 2 && (!this.isComparable(this.fValidatedInfo, var5.fDefault) || !var9.equals(var5.fDefault.actualValue))) {
         this.reportSchemaError("cvc-complex-type.3.1", new Object[]{var1.rawname, this.fTempQName.rawname, var7, var5.fDefault.stringValue()});
      }

      if (this.fIdConstraint) {
         var6.fActualValue = var9;
      }

      if (this.fAugPSVI) {
         var6.fDeclaration = var4;
         var6.fTypeDecl = var8;
         var6.fMemberType = this.fValidatedInfo.memberType;
         var6.fNormalizedValue = this.fValidatedInfo.normalizedValue;
         var6.fActualValue = this.fValidatedInfo.actualValue;
         var6.fActualValueType = this.fValidatedInfo.actualValueType;
         var6.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
         var6.fValidationAttempted = 2;
         String[] var14 = this.fXSIErrorReporter.mergeContext();
         var6.fErrorCodes = var14;
         var6.fValidity = (short)(var14 == null ? 2 : 1);
      }

   }

   void addDefaultAttributes(QName var1, XMLAttributes var2, XSAttributeGroupDecl var3) {
      XSObjectList var4 = var3.getAttributeUses();
      int var5 = var4.getLength();

      for(int var12 = 0; var12 < var5; ++var12) {
         XSAttributeUseImpl var6 = (XSAttributeUseImpl)var4.item(var12);
         XSAttributeDecl var7 = var6.fAttrDecl;
         short var8 = var6.fConstraintType;
         ValidatedInfo var9 = var6.fDefault;
         if (var8 == 0) {
            var8 = var7.getConstraintType();
            var9 = var7.fDefault;
         }

         boolean var10 = var2.getValue(var7.fTargetNamespace, var7.fName) != null;
         if (var6.fUse == 1 && !var10) {
            this.reportSchemaError("cvc-complex-type.4", new Object[]{var1.rawname, var7.fName});
         }

         if (!var10 && var8 != 0) {
            QName var11 = new QName((String)null, var7.fName, var7.fName, var7.fTargetNamespace);
            String var13 = var9 != null ? var9.stringValue() : "";
            int var14 = var2.addAttribute(var11, "CDATA", var13);
            if (var2 instanceof XMLAttributesImpl) {
               XMLAttributesImpl var15 = (XMLAttributesImpl)var2;
               boolean var16 = var9 != null && var9.memberType != null ? var9.memberType.isIDType() : var7.fType.isIDType();
               var15.setSchemaId(var14, var16);
            }

            if (this.fAugPSVI) {
               Augmentations var17 = var2.getAugmentations(var14);
               AttributePSVImpl var18 = new AttributePSVImpl();
               var17.putItem("ATTRIBUTE_PSVI", var18);
               var18.fDeclaration = var7;
               var18.fTypeDecl = var7.fType;
               var18.fMemberType = var9.memberType;
               var18.fNormalizedValue = var13;
               var18.fActualValue = var9.actualValue;
               var18.fActualValueType = var9.actualValueType;
               var18.fItemValueTypes = var9.itemValueTypes;
               var18.fValidationContext = this.fValidationRoot;
               var18.fValidity = 2;
               var18.fValidationAttempted = 2;
               var18.fSpecified = true;
            }
         }
      }

   }

   void processElementContent(QName var1) {
      String var2;
      int var3;
      if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.fDefault != null && !this.fSawText && !this.fSubElement && !this.fNil) {
         var2 = this.fCurrentElemDecl.fDefault.stringValue();
         var3 = var2.length();
         if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < var3) {
            this.fNormalizedStr.ch = new char[var3];
         }

         var2.getChars(0, var3, this.fNormalizedStr.ch, 0);
         this.fNormalizedStr.offset = 0;
         this.fNormalizedStr.length = var3;
         this.fDefaultValue = this.fNormalizedStr;
      }

      this.fValidatedInfo.normalizedValue = null;
      if (this.fNil && (this.fSubElement || this.fSawText)) {
         this.reportSchemaError("cvc-elt.3.2.1", new Object[]{var1.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
      }

      this.fValidatedInfo.reset();
      if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() != 0 && !this.fSubElement && !this.fSawText && !this.fNil) {
         if (this.fCurrentType != this.fCurrentElemDecl.fType && XSConstraints.ElementDefaultValidImmediate(this.fCurrentType, this.fCurrentElemDecl.fDefault.stringValue(), this.fState4XsiType, (ValidatedInfo)null) == null) {
            this.reportSchemaError("cvc-elt.5.1.1", new Object[]{var1.rawname, this.fCurrentType.getName(), this.fCurrentElemDecl.fDefault.stringValue()});
         }

         this.elementLocallyValidType(var1, this.fCurrentElemDecl.fDefault.stringValue());
      } else {
         Object var5 = this.elementLocallyValidType(var1, this.fBuffer);
         if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2 && !this.fNil) {
            String var6 = this.fBuffer.toString();
            if (this.fSubElement) {
               this.reportSchemaError("cvc-elt.5.2.2.1", new Object[]{var1.rawname});
            }

            if (this.fCurrentType.getTypeCategory() == 15) {
               XSComplexTypeDecl var4 = (XSComplexTypeDecl)this.fCurrentType;
               if (var4.fContentType == 3) {
                  if (!this.fCurrentElemDecl.fDefault.normalizedValue.equals(var6)) {
                     this.reportSchemaError("cvc-elt.5.2.2.2.1", new Object[]{var1.rawname, var6, this.fCurrentElemDecl.fDefault.normalizedValue});
                  }
               } else if (var4.fContentType == 1 && var5 != null && (!this.isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) || !var5.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
                  this.reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{var1.rawname, var6, this.fCurrentElemDecl.fDefault.stringValue()});
               }
            } else if (this.fCurrentType.getTypeCategory() == 16 && var5 != null && (!this.isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) || !var5.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
               this.reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{var1.rawname, var6, this.fCurrentElemDecl.fDefault.stringValue()});
            }
         }
      }

      if (this.fDefaultValue == null && this.fNormalizeData && this.fDocumentHandler != null && this.fUnionType) {
         var2 = this.fValidatedInfo.normalizedValue;
         if (var2 == null) {
            var2 = this.fBuffer.toString();
         }

         var3 = var2.length();
         if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < var3) {
            this.fNormalizedStr.ch = new char[var3];
         }

         var2.getChars(0, var3, this.fNormalizedStr.ch, 0);
         this.fNormalizedStr.offset = 0;
         this.fNormalizedStr.length = var3;
         this.fDocumentHandler.characters(this.fNormalizedStr, (Augmentations)null);
      }

   }

   Object elementLocallyValidType(QName var1, Object var2) {
      if (this.fCurrentType == null) {
         return null;
      } else {
         Object var3 = null;
         if (this.fCurrentType.getTypeCategory() == 16) {
            if (this.fSubElement) {
               this.reportSchemaError("cvc-type.3.1.2", new Object[]{var1.rawname});
            }

            if (!this.fNil) {
               XSSimpleType var4 = (XSSimpleType)this.fCurrentType;

               try {
                  if (!this.fNormalizeData || this.fUnionType) {
                     this.fValidationState.setNormalizationRequired(true);
                  }

                  var3 = var4.validate((Object)var2, this.fValidationState, this.fValidatedInfo);
               } catch (InvalidDatatypeValueException var6) {
                  this.reportSchemaError(var6.getKey(), var6.getArgs());
                  this.reportSchemaError("cvc-type.3.1.3", new Object[]{var1.rawname, var2});
               }
            }
         } else {
            var3 = this.elementLocallyValidComplexType(var1, var2);
         }

         return var3;
      }
   }

   Object elementLocallyValidComplexType(QName var1, Object var2) {
      Object var3 = null;
      XSComplexTypeDecl var4 = (XSComplexTypeDecl)this.fCurrentType;
      if (!this.fNil) {
         if (var4.fContentType == 0 && (this.fSubElement || this.fSawText)) {
            this.reportSchemaError("cvc-complex-type.2.1", new Object[]{var1.rawname});
         } else if (var4.fContentType != 1) {
            if (var4.fContentType == 2 && this.fSawCharacters) {
               this.reportSchemaError("cvc-complex-type.2.3", new Object[]{var1.rawname});
            }
         } else {
            if (this.fSubElement) {
               this.reportSchemaError("cvc-complex-type.2.2", new Object[]{var1.rawname});
            }

            XSSimpleType var5 = var4.fXSSimpleType;

            try {
               if (!this.fNormalizeData || this.fUnionType) {
                  this.fValidationState.setNormalizationRequired(true);
               }

               var3 = var5.validate((Object)var2, this.fValidationState, this.fValidatedInfo);
            } catch (InvalidDatatypeValueException var7) {
               this.reportSchemaError(var7.getKey(), var7.getArgs());
               this.reportSchemaError("cvc-complex-type.2.2", new Object[]{var1.rawname});
            }
         }

         if ((var4.fContentType == 2 || var4.fContentType == 3) && this.fCurrCMState[0] >= 0 && !this.fCurrentCM.endContentModel(this.fCurrCMState)) {
            String var8 = this.expectedStr(this.fCurrentCM.whatCanGoHere(this.fCurrCMState));
            this.reportSchemaError("cvc-complex-type.2.4.b", new Object[]{var1.rawname, var8});
         }
      }

      return var3;
   }

   void reportSchemaError(String var1, Object[] var2) {
      if (this.fDoValidation) {
         this.fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", var1, var2, (short)1);
      }

   }

   private boolean isComparable(ValidatedInfo var1, ValidatedInfo var2) {
      short var3 = this.convertToPrimitiveKind(var1.actualValueType);
      short var4 = this.convertToPrimitiveKind(var2.actualValueType);
      if (var3 != var4) {
         return var3 == 1 && var4 == 2 || var3 == 2 && var4 == 1;
      } else {
         if (var3 == 44 || var3 == 43) {
            ShortList var5 = var1.itemValueTypes;
            ShortList var6 = var2.itemValueTypes;
            int var7 = var5 != null ? var5.getLength() : 0;
            int var8 = var6 != null ? var6.getLength() : 0;
            if (var7 != var8) {
               return false;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               short var10 = this.convertToPrimitiveKind(var5.item(var9));
               short var11 = this.convertToPrimitiveKind(var6.item(var9));
               if (var10 != var11 && (var10 != 1 || var11 != 2) && (var10 != 2 || var11 != 1)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private short convertToPrimitiveKind(short var1) {
      if (var1 <= 20) {
         return var1;
      } else if (var1 <= 29) {
         return 2;
      } else {
         return var1 <= 42 ? 4 : var1;
      }
   }

   private String expectedStr(Vector var1) {
      StringBuffer var2 = new StringBuffer("{");
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var4 > 0) {
            var2.append(", ");
         }

         var2.append(var1.elementAt(var4).toString());
      }

      var2.append('}');
      return var2.toString();
   }

   protected static final class ShortVector {
      private int fLength;
      private short[] fData;

      public ShortVector() {
      }

      public ShortVector(int var1) {
         this.fData = new short[var1];
      }

      public int length() {
         return this.fLength;
      }

      public void add(short var1) {
         this.ensureCapacity(this.fLength + 1);
         this.fData[this.fLength++] = var1;
      }

      public short valueAt(int var1) {
         return this.fData[var1];
      }

      public void clear() {
         this.fLength = 0;
      }

      public boolean contains(short var1) {
         for(int var2 = 0; var2 < this.fLength; ++var2) {
            if (this.fData[var2] == var1) {
               return true;
            }
         }

         return false;
      }

      private void ensureCapacity(int var1) {
         if (this.fData == null) {
            this.fData = new short[8];
         } else if (this.fData.length <= var1) {
            short[] var2 = new short[this.fData.length * 2];
            System.arraycopy(this.fData, 0, var2, 0, this.fData.length);
            this.fData = var2;
         }

      }
   }

   protected class LocalIDKey {
      public IdentityConstraint fId;
      public int fDepth;

      public LocalIDKey() {
      }

      public LocalIDKey(IdentityConstraint var2, int var3) {
         this.fId = var2;
         this.fDepth = var3;
      }

      public int hashCode() {
         return this.fId.hashCode() + this.fDepth;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof LocalIDKey)) {
            return false;
         } else {
            LocalIDKey var2 = (LocalIDKey)var1;
            return var2.fId == this.fId && var2.fDepth == this.fDepth;
         }
      }
   }

   protected class ValueStoreCache {
      final LocalIDKey fLocalId = XMLSchemaValidator.this.new LocalIDKey();
      protected final Vector fValueStores = new Vector();
      protected final Hashtable fIdentityConstraint2ValueStoreMap = new Hashtable();
      protected final Stack fGlobalMapStack = new Stack();
      protected final Hashtable fGlobalIDConstraintMap = new Hashtable();

      public ValueStoreCache() {
      }

      public void startDocument() {
         this.fValueStores.removeAllElements();
         this.fIdentityConstraint2ValueStoreMap.clear();
         this.fGlobalIDConstraintMap.clear();
         this.fGlobalMapStack.removeAllElements();
      }

      public void startElement() {
         if (this.fGlobalIDConstraintMap.size() > 0) {
            this.fGlobalMapStack.push(this.fGlobalIDConstraintMap.clone());
         } else {
            this.fGlobalMapStack.push((Object)null);
         }

         this.fGlobalIDConstraintMap.clear();
      }

      public void endElement() {
         if (!this.fGlobalMapStack.isEmpty()) {
            Hashtable var1 = (Hashtable)this.fGlobalMapStack.pop();
            if (var1 != null) {
               Enumeration var2 = var1.keys();

               while(var2.hasMoreElements()) {
                  IdentityConstraint var3 = (IdentityConstraint)var2.nextElement();
                  ValueStoreBase var4 = (ValueStoreBase)var1.get(var3);
                  if (var4 != null) {
                     ValueStoreBase var5 = (ValueStoreBase)this.fGlobalIDConstraintMap.get(var3);
                     if (var5 == null) {
                        this.fGlobalIDConstraintMap.put(var3, var4);
                     } else if (var5 != var4) {
                        var5.append(var4);
                     }
                  }
               }

            }
         }
      }

      public void initValueStoresFor(XSElementDecl var1, FieldActivator var2) {
         IdentityConstraint[] var3 = var1.fIDConstraints;
         int var4 = var1.fIDCPos;

         for(int var5 = 0; var5 < var4; ++var5) {
            LocalIDKey var7;
            switch (var3[var5].getCategory()) {
               case 1:
                  UniqueOrKey var9 = (UniqueOrKey)var3[var5];
                  var7 = XMLSchemaValidator.this.new LocalIDKey(var9, XMLSchemaValidator.this.fElementDepth);
                  KeyValueStore var10 = (KeyValueStore)this.fIdentityConstraint2ValueStoreMap.get(var7);
                  if (var10 == null) {
                     var10 = XMLSchemaValidator.this.new KeyValueStore(var9);
                     this.fIdentityConstraint2ValueStoreMap.put(var7, var10);
                  } else {
                     var10.clear();
                  }

                  this.fValueStores.addElement(var10);
                  XMLSchemaValidator.this.activateSelectorFor(var3[var5]);
                  break;
               case 2:
                  KeyRef var11 = (KeyRef)var3[var5];
                  var7 = XMLSchemaValidator.this.new LocalIDKey(var11, XMLSchemaValidator.this.fElementDepth);
                  KeyRefValueStore var12 = (KeyRefValueStore)this.fIdentityConstraint2ValueStoreMap.get(var7);
                  if (var12 == null) {
                     var12 = XMLSchemaValidator.this.new KeyRefValueStore(var11, (KeyValueStore)null);
                     this.fIdentityConstraint2ValueStoreMap.put(var7, var12);
                  } else {
                     var12.clear();
                  }

                  this.fValueStores.addElement(var12);
                  XMLSchemaValidator.this.activateSelectorFor(var3[var5]);
                  break;
               case 3:
                  UniqueOrKey var6 = (UniqueOrKey)var3[var5];
                  var7 = XMLSchemaValidator.this.new LocalIDKey(var6, XMLSchemaValidator.this.fElementDepth);
                  UniqueValueStore var8 = (UniqueValueStore)this.fIdentityConstraint2ValueStoreMap.get(var7);
                  if (var8 == null) {
                     var8 = XMLSchemaValidator.this.new UniqueValueStore(var6);
                     this.fIdentityConstraint2ValueStoreMap.put(var7, var8);
                  } else {
                     var8.clear();
                  }

                  this.fValueStores.addElement(var8);
                  XMLSchemaValidator.this.activateSelectorFor(var3[var5]);
            }
         }

      }

      public ValueStoreBase getValueStoreFor(IdentityConstraint var1, int var2) {
         this.fLocalId.fDepth = var2;
         this.fLocalId.fId = var1;
         return (ValueStoreBase)this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
      }

      public ValueStoreBase getGlobalValueStoreFor(IdentityConstraint var1) {
         return (ValueStoreBase)this.fGlobalIDConstraintMap.get(var1);
      }

      public void transplant(IdentityConstraint var1, int var2) {
         this.fLocalId.fDepth = var2;
         this.fLocalId.fId = var1;
         ValueStoreBase var3 = (ValueStoreBase)this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
         if (var1.getCategory() != 2) {
            ValueStoreBase var4 = (ValueStoreBase)this.fGlobalIDConstraintMap.get(var1);
            if (var4 != null) {
               var4.append(var3);
               this.fGlobalIDConstraintMap.put(var1, var4);
            } else {
               this.fGlobalIDConstraintMap.put(var1, var3);
            }

         }
      }

      public void endDocument() {
         int var1 = this.fValueStores.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            ValueStoreBase var3 = (ValueStoreBase)this.fValueStores.elementAt(var2);
            var3.endDocument();
         }

      }

      public String toString() {
         String var1 = super.toString();
         int var2 = var1.lastIndexOf(36);
         if (var2 != -1) {
            return var1.substring(var2 + 1);
         } else {
            int var3 = var1.lastIndexOf(46);
            return var3 != -1 ? var1.substring(var3 + 1) : var1;
         }
      }
   }

   protected class KeyRefValueStore extends ValueStoreBase {
      protected ValueStoreBase fKeyValueStore;

      public KeyRefValueStore(KeyRef var2, KeyValueStore var3) {
         super(var2);
         this.fKeyValueStore = var3;
      }

      public void endDocumentFragment() {
         super.endDocumentFragment();
         this.fKeyValueStore = (ValueStoreBase)XMLSchemaValidator.this.fValueStoreCache.fGlobalIDConstraintMap.get(((KeyRef)super.fIdentityConstraint).getKey());
         String var2;
         if (this.fKeyValueStore == null) {
            String var6 = "KeyRefOutOfScope";
            var2 = super.fIdentityConstraint.toString();
            XMLSchemaValidator.this.reportSchemaError(var6, new Object[]{var2});
         } else {
            int var1 = this.fKeyValueStore.contains(this);
            if (var1 != -1) {
               var2 = "KeyNotFound";
               String var3 = this.toString(super.fValues, var1, super.fFieldCount);
               String var4 = super.fIdentityConstraint.getElementName();
               String var5 = super.fIdentityConstraint.getName();
               XMLSchemaValidator.this.reportSchemaError(var2, new Object[]{var5, var3, var4});
            }

         }
      }

      public void endDocument() {
         super.endDocument();
      }
   }

   protected class KeyValueStore extends ValueStoreBase {
      public KeyValueStore(UniqueOrKey var2) {
         super(var2);
      }

      protected void checkDuplicateValues() {
         if (this.contains()) {
            String var1 = "DuplicateKey";
            String var2 = this.toString(super.fLocalValues);
            String var3 = super.fIdentityConstraint.getElementName();
            XMLSchemaValidator.this.reportSchemaError(var1, new Object[]{var2, var3});
         }

      }
   }

   protected class UniqueValueStore extends ValueStoreBase {
      public UniqueValueStore(UniqueOrKey var2) {
         super(var2);
      }

      protected void checkDuplicateValues() {
         if (this.contains()) {
            String var1 = "DuplicateUnique";
            String var2 = this.toString(super.fLocalValues);
            String var3 = super.fIdentityConstraint.getElementName();
            XMLSchemaValidator.this.reportSchemaError(var1, new Object[]{var2, var3});
         }

      }
   }

   protected abstract class ValueStoreBase implements ValueStore {
      protected IdentityConstraint fIdentityConstraint;
      protected int fFieldCount = 0;
      protected Field[] fFields = null;
      protected Object[] fLocalValues = null;
      protected short[] fLocalValueTypes = null;
      protected ShortList[] fLocalItemValueTypes = null;
      protected int fValuesCount;
      public final Vector fValues = new Vector();
      public ShortVector fValueTypes = null;
      public Vector fItemValueTypes = null;
      private boolean fUseValueTypeVector = false;
      private int fValueTypesLength = 0;
      private short fValueType = 0;
      private boolean fUseItemValueTypeVector = false;
      private int fItemValueTypesLength = 0;
      private ShortList fItemValueType = null;
      final StringBuffer fTempBuffer = new StringBuffer();

      protected ValueStoreBase(IdentityConstraint var2) {
         this.fIdentityConstraint = var2;
         this.fFieldCount = this.fIdentityConstraint.getFieldCount();
         this.fFields = new Field[this.fFieldCount];
         this.fLocalValues = new Object[this.fFieldCount];
         this.fLocalValueTypes = new short[this.fFieldCount];
         this.fLocalItemValueTypes = new ShortList[this.fFieldCount];

         for(int var3 = 0; var3 < this.fFieldCount; ++var3) {
            this.fFields[var3] = this.fIdentityConstraint.getFieldAt(var3);
         }

      }

      public void clear() {
         this.fValuesCount = 0;
         this.fUseValueTypeVector = false;
         this.fValueTypesLength = 0;
         this.fValueType = 0;
         this.fUseItemValueTypeVector = false;
         this.fItemValueTypesLength = 0;
         this.fItemValueType = null;
         this.fValues.setSize(0);
         if (this.fValueTypes != null) {
            this.fValueTypes.clear();
         }

         if (this.fItemValueTypes != null) {
            this.fItemValueTypes.setSize(0);
         }

      }

      public void append(ValueStoreBase var1) {
         for(int var2 = 0; var2 < var1.fValues.size(); ++var2) {
            this.fValues.addElement(var1.fValues.elementAt(var2));
         }

      }

      public void startValueScope() {
         this.fValuesCount = 0;

         for(int var1 = 0; var1 < this.fFieldCount; ++var1) {
            this.fLocalValues[var1] = null;
            this.fLocalValueTypes[var1] = 0;
            this.fLocalItemValueTypes[var1] = null;
         }

      }

      public void endValueScope() {
         String var1;
         String var2;
         if (this.fValuesCount == 0) {
            if (this.fIdentityConstraint.getCategory() == 1) {
               var1 = "AbsentKeyValue";
               var2 = this.fIdentityConstraint.getElementName();
               XMLSchemaValidator.this.reportSchemaError(var1, new Object[]{var2});
            }

         } else if (this.fValuesCount != this.fFieldCount) {
            String var3;
            String var4;
            switch (this.fIdentityConstraint.getCategory()) {
               case 1:
                  var1 = "KeyNotEnoughValues";
                  UniqueOrKey var6 = (UniqueOrKey)this.fIdentityConstraint;
                  var3 = this.fIdentityConstraint.getElementName();
                  var4 = var6.getIdentityConstraintName();
                  XMLSchemaValidator.this.reportSchemaError(var1, new Object[]{var3, var4});
                  break;
               case 2:
                  var1 = "KeyRefNotEnoughValues";
                  KeyRef var5 = (KeyRef)this.fIdentityConstraint;
                  var3 = this.fIdentityConstraint.getElementName();
                  var4 = var5.getKey().getIdentityConstraintName();
                  XMLSchemaValidator.this.reportSchemaError(var1, new Object[]{var3, var4});
                  break;
               case 3:
                  var1 = "UniqueNotEnoughValues";
                  var2 = this.fIdentityConstraint.getElementName();
                  XMLSchemaValidator.this.reportSchemaError(var1, new Object[]{var2});
            }

         }
      }

      public void endDocumentFragment() {
      }

      public void endDocument() {
      }

      public void reportError(String var1, Object[] var2) {
         XMLSchemaValidator.this.reportSchemaError(var1, var2);
      }

      public void addValue(Field var1, Object var2, short var3, ShortList var4) {
         int var5;
         for(var5 = this.fFieldCount - 1; var5 > -1 && this.fFields[var5] != var1; --var5) {
         }

         String var6;
         if (var5 == -1) {
            var6 = "UnknownField";
            XMLSchemaValidator.this.reportSchemaError(var6, new Object[]{var1.toString()});
         } else {
            if (Boolean.TRUE != XMLSchemaValidator.this.mayMatch(var1)) {
               var6 = "FieldMultipleMatch";
               XMLSchemaValidator.this.reportSchemaError(var6, new Object[]{var1.toString()});
            } else {
               ++this.fValuesCount;
            }

            this.fLocalValues[var5] = var2;
            this.fLocalValueTypes[var5] = var3;
            this.fLocalItemValueTypes[var5] = var4;
            if (this.fValuesCount == this.fFieldCount) {
               this.checkDuplicateValues();

               for(var5 = 0; var5 < this.fFieldCount; ++var5) {
                  this.fValues.addElement(this.fLocalValues[var5]);
                  this.addValueType(this.fLocalValueTypes[var5]);
                  this.addItemValueType(this.fLocalItemValueTypes[var5]);
               }
            }

         }
      }

      public boolean contains() {
         boolean var1 = false;
         int var2 = this.fValues.size();

         int var11;
         label49:
         for(int var3 = 0; var3 < var2; var3 = var11) {
            var11 = var3 + this.fFieldCount;

            for(int var4 = 0; var4 < this.fFieldCount; ++var4) {
               Object var5 = this.fLocalValues[var4];
               Object var6 = this.fValues.elementAt(var3);
               short var7 = this.fLocalValueTypes[var4];
               short var8 = this.getValueTypeAt(var3);
               if (var5 == null || var6 == null || var7 != var8 || !var5.equals(var6)) {
                  continue label49;
               }

               if (var7 == 44 || var7 == 43) {
                  ShortList var9 = this.fLocalItemValueTypes[var4];
                  ShortList var10 = this.getItemValueTypeAt(var3);
                  if (var9 == null || var10 == null || !var9.equals(var10)) {
                     continue label49;
                  }
               }

               ++var3;
            }

            return true;
         }

         return false;
      }

      public int contains(ValueStoreBase var1) {
         Vector var2 = var1.fValues;
         int var3 = var2.size();
         int var4;
         int var5;
         if (this.fFieldCount > 1) {
            var4 = this.fValues.size();

            label72:
            for(var5 = 0; var5 < var3; var5 += this.fFieldCount) {
               for(int var14 = 0; var14 < var4; var14 += this.fFieldCount) {
                  int var7 = 0;

                  while(true) {
                     if (var7 >= this.fFieldCount) {
                        continue label72;
                     }

                     Object var8 = var2.elementAt(var5 + var7);
                     Object var9 = this.fValues.elementAt(var14 + var7);
                     short var10 = var1.getValueTypeAt(var5 + var7);
                     short var11 = this.getValueTypeAt(var14 + var7);
                     if (var8 != var9 && (var10 != var11 || var8 == null || !var8.equals(var9))) {
                        break;
                     }

                     if (var10 == 44 || var10 == 43) {
                        ShortList var12 = var1.getItemValueTypeAt(var5 + var7);
                        ShortList var13 = this.getItemValueTypeAt(var14 + var7);
                        if (var12 == null || var13 == null || !var12.equals(var13)) {
                           break;
                        }
                     }

                     ++var7;
                  }
               }

               return var5;
            }
         } else {
            for(var4 = 0; var4 < var3; ++var4) {
               var5 = var1.getValueTypeAt(var4);
               if (!this.valueTypeContains((short)var5) || !this.fValues.contains(var2.elementAt(var4))) {
                  return var4;
               }

               if (var5 == 44 || var5 == 43) {
                  ShortList var6 = var1.getItemValueTypeAt(var4);
                  if (!this.itemValueTypeContains(var6)) {
                     return var4;
                  }
               }
            }
         }

         return -1;
      }

      protected void checkDuplicateValues() {
      }

      protected String toString(Object[] var1) {
         int var2 = var1.length;
         if (var2 == 0) {
            return "";
         } else {
            this.fTempBuffer.setLength(0);

            for(int var3 = 0; var3 < var2; ++var3) {
               if (var3 > 0) {
                  this.fTempBuffer.append(',');
               }

               this.fTempBuffer.append(var1[var3]);
            }

            return this.fTempBuffer.toString();
         }
      }

      protected String toString(Vector var1, int var2, int var3) {
         if (var3 == 0) {
            return "";
         } else if (var3 == 1) {
            return String.valueOf(var1.elementAt(var2));
         } else {
            StringBuffer var4 = new StringBuffer();

            for(int var5 = 0; var5 < var3; ++var5) {
               if (var5 > 0) {
                  var4.append(',');
               }

               var4.append(var1.elementAt(var2 + var5));
            }

            return var4.toString();
         }
      }

      public String toString() {
         String var1 = super.toString();
         int var2 = var1.lastIndexOf(36);
         if (var2 != -1) {
            var1 = var1.substring(var2 + 1);
         }

         int var3 = var1.lastIndexOf(46);
         if (var3 != -1) {
            var1 = var1.substring(var3 + 1);
         }

         return var1 + '[' + this.fIdentityConstraint + ']';
      }

      private void addValueType(short var1) {
         if (this.fUseValueTypeVector) {
            this.fValueTypes.add(var1);
         } else if (this.fValueTypesLength++ == 0) {
            this.fValueType = var1;
         } else if (this.fValueType != var1) {
            this.fUseValueTypeVector = true;
            if (this.fValueTypes == null) {
               this.fValueTypes = new ShortVector(this.fValueTypesLength * 2);
            }

            for(int var2 = 1; var2 < this.fValueTypesLength; ++var2) {
               this.fValueTypes.add(this.fValueType);
            }

            this.fValueTypes.add(var1);
         }

      }

      private short getValueTypeAt(int var1) {
         return this.fUseValueTypeVector ? this.fValueTypes.valueAt(var1) : this.fValueType;
      }

      private boolean valueTypeContains(short var1) {
         if (this.fUseValueTypeVector) {
            return this.fValueTypes.contains(var1);
         } else {
            return this.fValueType == var1;
         }
      }

      private void addItemValueType(ShortList var1) {
         if (this.fUseItemValueTypeVector) {
            this.fItemValueTypes.add(var1);
         } else if (this.fItemValueTypesLength++ == 0) {
            this.fItemValueType = var1;
         } else if (this.fItemValueType != var1 && (this.fItemValueType == null || !this.fItemValueType.equals(var1))) {
            this.fUseItemValueTypeVector = true;
            if (this.fItemValueTypes == null) {
               this.fItemValueTypes = new Vector(this.fItemValueTypesLength * 2);
            }

            for(int var2 = 1; var2 < this.fItemValueTypesLength; ++var2) {
               this.fItemValueTypes.add(this.fItemValueType);
            }

            this.fItemValueTypes.add(var1);
         }

      }

      private ShortList getItemValueTypeAt(int var1) {
         return this.fUseItemValueTypeVector ? (ShortList)this.fItemValueTypes.elementAt(var1) : this.fItemValueType;
      }

      private boolean itemValueTypeContains(ShortList var1) {
         if (this.fUseItemValueTypeVector) {
            return this.fItemValueTypes.contains(var1);
         } else {
            return this.fItemValueType == var1 || this.fItemValueType != null && this.fItemValueType.equals(var1);
         }
      }
   }

   protected static class XPathMatcherStack {
      protected XPathMatcher[] fMatchers = new XPathMatcher[4];
      protected int fMatchersCount;
      protected IntStack fContextStack = new IntStack();

      public XPathMatcherStack() {
      }

      public void clear() {
         for(int var1 = 0; var1 < this.fMatchersCount; ++var1) {
            this.fMatchers[var1] = null;
         }

         this.fMatchersCount = 0;
         this.fContextStack.clear();
      }

      public int size() {
         return this.fContextStack.size();
      }

      public int getMatcherCount() {
         return this.fMatchersCount;
      }

      public void addMatcher(XPathMatcher var1) {
         this.ensureMatcherCapacity();
         this.fMatchers[this.fMatchersCount++] = var1;
      }

      public XPathMatcher getMatcherAt(int var1) {
         return this.fMatchers[var1];
      }

      public void pushContext() {
         this.fContextStack.push(this.fMatchersCount);
      }

      public void popContext() {
         this.fMatchersCount = this.fContextStack.pop();
      }

      private void ensureMatcherCapacity() {
         if (this.fMatchersCount == this.fMatchers.length) {
            XPathMatcher[] var1 = new XPathMatcher[this.fMatchers.length * 2];
            System.arraycopy(this.fMatchers, 0, var1, 0, this.fMatchers.length);
            this.fMatchers = var1;
         }

      }
   }

   protected final class XSIErrorReporter {
      XMLErrorReporter fErrorReporter;
      Vector fErrors = new Vector();
      int[] fContext = new int[8];
      int fContextCount;

      public void reset(XMLErrorReporter var1) {
         this.fErrorReporter = var1;
         this.fErrors.removeAllElements();
         this.fContextCount = 0;
      }

      public void pushContext() {
         if (XMLSchemaValidator.this.fAugPSVI) {
            if (this.fContextCount == this.fContext.length) {
               int var1 = this.fContextCount + 8;
               int[] var2 = new int[var1];
               System.arraycopy(this.fContext, 0, var2, 0, this.fContextCount);
               this.fContext = var2;
            }

            this.fContext[this.fContextCount++] = this.fErrors.size();
         }
      }

      public String[] popContext() {
         if (!XMLSchemaValidator.this.fAugPSVI) {
            return null;
         } else {
            int var1 = this.fContext[--this.fContextCount];
            int var2 = this.fErrors.size() - var1;
            if (var2 == 0) {
               return null;
            } else {
               String[] var3 = new String[var2];

               for(int var4 = 0; var4 < var2; ++var4) {
                  var3[var4] = (String)this.fErrors.elementAt(var1 + var4);
               }

               this.fErrors.setSize(var1);
               return var3;
            }
         }
      }

      public String[] mergeContext() {
         if (!XMLSchemaValidator.this.fAugPSVI) {
            return null;
         } else {
            int var1 = this.fContext[--this.fContextCount];
            int var2 = this.fErrors.size() - var1;
            if (var2 == 0) {
               return null;
            } else {
               String[] var3 = new String[var2];

               for(int var4 = 0; var4 < var2; ++var4) {
                  var3[var4] = (String)this.fErrors.elementAt(var1 + var4);
               }

               return var3;
            }
         }
      }

      public void reportError(String var1, String var2, Object[] var3, short var4) throws XNIException {
         this.fErrorReporter.reportError(var1, var2, var3, var4);
         if (XMLSchemaValidator.this.fAugPSVI) {
            this.fErrors.addElement(var2);
         }

      }

      public void reportError(XMLLocator var1, String var2, String var3, Object[] var4, short var5) throws XNIException {
         this.fErrorReporter.reportError(var1, var2, var3, var4, var5);
         if (XMLSchemaValidator.this.fAugPSVI) {
            this.fErrors.addElement(var3);
         }

      }
   }
}
