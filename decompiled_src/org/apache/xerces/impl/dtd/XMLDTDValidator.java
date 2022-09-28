package org.apache.xerces.impl.dtd;

import java.io.IOException;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.util.SymbolTable;
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
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XMLDTDValidator implements XMLComponent, XMLDocumentFilter, XMLDTDValidatorFilter, RevalidationHandler {
   private static final int TOP_LEVEL_SCOPE = -1;
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
   protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/dynamic"};
   private static final Boolean[] FEATURE_DEFAULTS;
   private static final String[] RECOGNIZED_PROPERTIES;
   private static final Object[] PROPERTY_DEFAULTS;
   private static final boolean DEBUG_ATTRIBUTES = false;
   private static final boolean DEBUG_ELEMENT_CHILDREN = false;
   protected ValidationManager fValidationManager = null;
   protected ValidationState fValidationState = new ValidationState();
   protected boolean fNamespaces;
   protected boolean fValidation;
   protected boolean fDTDValidation;
   protected boolean fDynamicValidation;
   protected boolean fWarnDuplicateAttdef;
   protected SymbolTable fSymbolTable;
   protected XMLErrorReporter fErrorReporter;
   protected XMLGrammarPool fGrammarPool;
   protected DTDGrammarBucket fGrammarBucket;
   protected XMLLocator fDocLocation;
   protected NamespaceContext fNamespaceContext = null;
   protected DTDDVFactory fDatatypeValidatorFactory;
   protected XMLDocumentHandler fDocumentHandler;
   protected XMLDocumentSource fDocumentSource;
   protected DTDGrammar fDTDGrammar;
   protected boolean fSeenDoctypeDecl = false;
   private boolean fPerformValidation;
   private String fSchemaType;
   private final QName fCurrentElement = new QName();
   private int fCurrentElementIndex = -1;
   private int fCurrentContentSpecType = -1;
   private final QName fRootElement = new QName();
   private boolean fInCDATASection = false;
   private int[] fElementIndexStack = new int[8];
   private int[] fContentSpecTypeStack = new int[8];
   private QName[] fElementQNamePartsStack = new QName[8];
   private QName[] fElementChildren = new QName[32];
   private int fElementChildrenLength = 0;
   private int[] fElementChildrenOffsetStack = new int[32];
   private int fElementDepth = -1;
   private boolean fSeenRootElement = false;
   private boolean fInElementContent = false;
   private XMLElementDecl fTempElementDecl = new XMLElementDecl();
   private XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
   private XMLEntityDecl fEntityDecl = new XMLEntityDecl();
   private QName fTempQName = new QName();
   private StringBuffer fBuffer = new StringBuffer();
   protected DatatypeValidator fValID;
   protected DatatypeValidator fValIDRef;
   protected DatatypeValidator fValIDRefs;
   protected DatatypeValidator fValENTITY;
   protected DatatypeValidator fValENTITIES;
   protected DatatypeValidator fValNMTOKEN;
   protected DatatypeValidator fValNMTOKENS;
   protected DatatypeValidator fValNOTATION;

   public XMLDTDValidator() {
      for(int var1 = 0; var1 < this.fElementQNamePartsStack.length; ++var1) {
         this.fElementQNamePartsStack[var1] = new QName();
      }

      this.fGrammarBucket = new DTDGrammarBucket();
   }

   DTDGrammarBucket getGrammarBucket() {
      return this.fGrammarBucket;
   }

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      this.fDTDGrammar = null;
      this.fSeenDoctypeDecl = false;
      this.fInCDATASection = false;
      this.fSeenRootElement = false;
      this.fInElementContent = false;
      this.fCurrentElementIndex = -1;
      this.fCurrentContentSpecType = -1;
      this.fRootElement.clear();
      this.fValidationState.resetIDTables();
      this.fGrammarBucket.clear();
      this.fElementDepth = -1;
      this.fElementChildrenLength = 0;

      boolean var2;
      try {
         var2 = var1.getFeature("http://apache.org/xml/features/internal/parser-settings");
      } catch (XMLConfigurationException var11) {
         var2 = true;
      }

      if (!var2) {
         this.fValidationManager.addValidationState(this.fValidationState);
      } else {
         try {
            this.fNamespaces = var1.getFeature("http://xml.org/sax/features/namespaces");
         } catch (XMLConfigurationException var10) {
            this.fNamespaces = true;
         }

         try {
            this.fValidation = var1.getFeature("http://xml.org/sax/features/validation");
         } catch (XMLConfigurationException var9) {
            this.fValidation = false;
         }

         try {
            this.fDTDValidation = !var1.getFeature("http://apache.org/xml/features/validation/schema");
         } catch (XMLConfigurationException var8) {
            this.fDTDValidation = true;
         }

         try {
            this.fDynamicValidation = var1.getFeature("http://apache.org/xml/features/validation/dynamic");
         } catch (XMLConfigurationException var7) {
            this.fDynamicValidation = false;
         }

         try {
            this.fWarnDuplicateAttdef = var1.getFeature("http://apache.org/xml/features/validation/warn-on-duplicate-attdef");
         } catch (XMLConfigurationException var6) {
            this.fWarnDuplicateAttdef = false;
         }

         try {
            this.fSchemaType = (String)var1.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
         } catch (XMLConfigurationException var5) {
            this.fSchemaType = null;
         }

         this.fValidationManager = (ValidationManager)var1.getProperty("http://apache.org/xml/properties/internal/validation-manager");
         this.fValidationManager.addValidationState(this.fValidationState);
         this.fValidationState.setUsingNamespaces(this.fNamespaces);
         this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");
         this.fSymbolTable = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");

         try {
            this.fGrammarPool = (XMLGrammarPool)var1.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
         } catch (XMLConfigurationException var4) {
            this.fGrammarPool = null;
         }

         this.fDatatypeValidatorFactory = (DTDDVFactory)var1.getProperty("http://apache.org/xml/properties/internal/datatype-validator-factory");
         this.init();
      }
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
      if (this.fGrammarPool != null) {
         Grammar[] var5 = this.fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/TR/REC-xml");

         for(int var6 = 0; var6 < var5.length; ++var6) {
            this.fGrammarBucket.putGrammar((DTDGrammar)var5[var6]);
         }
      }

      this.fDocLocation = var1;
      this.fNamespaceContext = var3;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startDocument(var1, var2, var3, var4);
      }

   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      this.fGrammarBucket.setStandalone(var3 != null && var3.equals("yes"));
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.xmlDecl(var1, var2, var3, var4);
      }

   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      this.fSeenDoctypeDecl = true;
      this.fRootElement.setValues((String)null, var1, var1, (String)null);
      String var5 = null;

      try {
         var5 = XMLEntityManager.expandSystemId(var3, this.fDocLocation.getExpandedSystemId(), false);
      } catch (IOException var7) {
      }

      XMLDTDDescription var6 = new XMLDTDDescription(var2, var3, this.fDocLocation.getExpandedSystemId(), var5, var1);
      this.fDTDGrammar = this.fGrammarBucket.getGrammar(var6);
      if (this.fDTDGrammar == null && this.fGrammarPool != null && (var3 != null || var2 != null)) {
         this.fDTDGrammar = (DTDGrammar)this.fGrammarPool.retrieveGrammar(var6);
      }

      if (this.fDTDGrammar == null) {
         this.fDTDGrammar = new DTDGrammar(this.fSymbolTable, var6);
      } else {
         this.fValidationManager.setCachedDTD(true);
      }

      this.fGrammarBucket.setActiveGrammar(this.fDTDGrammar);
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.doctypeDecl(var1, var2, var3, var4);
      }

   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      this.handleStartElement(var1, var2, var3);
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startElement(var1, var2, var3);
      }

   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      boolean var4 = this.handleStartElement(var1, var2, var3);
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.emptyElement(var1, var2, var3);
      }

      if (!var4) {
         this.handleEndElement(var1, var3, true);
      }

   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      boolean var3 = true;
      boolean var4 = true;

      for(int var5 = var1.offset; var5 < var1.offset + var1.length; ++var5) {
         if (!this.isSpace(var1.ch[var5])) {
            var4 = false;
            break;
         }
      }

      if (this.fInElementContent && var4 && !this.fInCDATASection && this.fDocumentHandler != null) {
         this.fDocumentHandler.ignorableWhitespace(var1, var2);
         var3 = false;
      }

      if (this.fPerformValidation) {
         if (this.fInElementContent) {
            if (this.fGrammarBucket.getStandalone() && this.fDTDGrammar.getElementDeclIsExternal(this.fCurrentElementIndex) && var4) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE", (Object[])null, (short)1);
            }

            if (!var4) {
               this.charDataInContent();
            }

            if (var2 != null && var2.getItem("CHAR_REF_PROBABLE_WS") == Boolean.TRUE) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, this.fDTDGrammar.getContentSpecAsString(this.fElementDepth), "character reference"}, (short)1);
            }
         }

         if (this.fCurrentContentSpecType == 1) {
            this.charDataInContent();
         }
      }

      if (var3 && this.fDocumentHandler != null) {
         this.fDocumentHandler.characters(var1, var2);
      }

   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.ignorableWhitespace(var1, var2);
      }

   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      this.handleEndElement(var1, var2, false);
   }

   public void startCDATA(Augmentations var1) throws XNIException {
      if (this.fPerformValidation && this.fInElementContent) {
         this.charDataInContent();
      }

      this.fInCDATASection = true;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startCDATA(var1);
      }

   }

   public void endCDATA(Augmentations var1) throws XNIException {
      this.fInCDATASection = false;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endCDATA(var1);
      }

   }

   public void endDocument(Augmentations var1) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endDocument(var1);
      }

   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
         this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
         if (this.fTempElementDecl.type == 1) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "comment"}, (short)1);
         }
      }

      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.comment(var1, var2);
      }

   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
         this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
         if (this.fTempElementDecl.type == 1) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "processing instruction"}, (short)1);
         }
      }

      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.processingInstruction(var1, var2, var3);
      }

   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
         this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
         if (this.fTempElementDecl.type == 1) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "ENTITY"}, (short)1);
         }

         if (this.fGrammarBucket.getStandalone()) {
            XMLDTDProcessor.checkStandaloneEntityRef(var1, this.fDTDGrammar, this.fEntityDecl, this.fErrorReporter);
         }
      }

      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startGeneralEntity(var1, var2, var3, var4);
      }

   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endGeneralEntity(var1, var2);
      }

   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.textDecl(var1, var2, var3);
      }

   }

   public final boolean hasGrammar() {
      return this.fDTDGrammar != null;
   }

   public final boolean validate() {
      return this.fSchemaType != Constants.NS_XMLSCHEMA && (!this.fDynamicValidation && this.fValidation || this.fDynamicValidation && this.fSeenDoctypeDecl) && (this.fDTDValidation || this.fSeenDoctypeDecl);
   }

   protected void addDTDDefaultAttrsAndValidate(QName var1, int var2, XMLAttributes var3) throws XNIException {
      if (var2 != -1 && this.fDTDGrammar != null) {
         String var7;
         String var10;
         boolean var12;
         for(int var4 = this.fDTDGrammar.getFirstAttributeDeclIndex(var2); var4 != -1; var4 = this.fDTDGrammar.getNextAttributeDeclIndex(var4)) {
            this.fDTDGrammar.getAttributeDecl(var4, this.fTempAttDecl);
            String var5 = this.fTempAttDecl.name.prefix;
            String var6 = this.fTempAttDecl.name.localpart;
            var7 = this.fTempAttDecl.name.rawname;
            String var8 = this.getAttributeTypeName(this.fTempAttDecl);
            short var9 = this.fTempAttDecl.simpleType.defaultType;
            var10 = null;
            if (this.fTempAttDecl.simpleType.defaultValue != null) {
               var10 = this.fTempAttDecl.simpleType.defaultValue;
            }

            boolean var11 = false;
            var12 = var9 == 2;
            boolean var13 = var8 == XMLSymbols.fCDATASymbol;
            int var14;
            if (!var13 || var12 || var10 != null) {
               var14 = var3.getLength();

               for(int var15 = 0; var15 < var14; ++var15) {
                  if (var3.getQName(var15) == var7) {
                     var11 = true;
                     break;
                  }
               }
            }

            if (!var11) {
               Object[] var26;
               if (var12) {
                  if (this.fPerformValidation) {
                     var26 = new Object[]{var1.localpart, var7};
                     this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED", var26, (short)1);
                  }
               } else if (var10 != null) {
                  if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && this.fDTDGrammar.getAttributeDeclIsExternal(var4)) {
                     var26 = new Object[]{var1.localpart, var7};
                     this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED", var26, (short)1);
                  }

                  if (this.fNamespaces) {
                     var14 = var7.indexOf(58);
                     if (var14 != -1) {
                        var5 = var7.substring(0, var14);
                        var5 = this.fSymbolTable.addSymbol(var5);
                        var6 = var7.substring(var14 + 1);
                        var6 = this.fSymbolTable.addSymbol(var6);
                     }
                  }

                  this.fTempQName.setValues(var5, var6, var7, this.fTempAttDecl.name.uri);
                  var3.addAttribute(this.fTempQName, var8, var10);
               }
            }
         }

         int var17 = var3.getLength();

         for(int var18 = 0; var18 < var17; ++var18) {
            var7 = var3.getQName(var18);
            boolean var19 = false;
            if (this.fPerformValidation && this.fGrammarBucket.getStandalone()) {
               String var20 = var3.getNonNormalizedValue(var18);
               if (var20 != null) {
                  var10 = this.getExternalEntityRefInAttrValue(var20);
                  if (var10 != null) {
                     this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[]{var10}, (short)1);
                  }
               }
            }

            boolean var21 = true;

            int var22;
            for(var22 = this.fDTDGrammar.getFirstAttributeDeclIndex(var2); var22 != -1; var22 = this.fDTDGrammar.getNextAttributeDeclIndex(var22)) {
               this.fDTDGrammar.getAttributeDecl(var22, this.fTempAttDecl);
               if (this.fTempAttDecl.name.rawname == var7) {
                  var19 = true;
                  break;
               }
            }

            if (!var19) {
               if (this.fPerformValidation) {
                  Object[] var24 = new Object[]{var1.rawname, var7};
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_NOT_DECLARED", var24, (short)1);
               }
            } else {
               String var23 = this.getAttributeTypeName(this.fTempAttDecl);
               var3.setType(var18, var23);
               var3.getAugmentations(var18).putItem("ATTRIBUTE_DECLARED", Boolean.TRUE);
               var12 = false;
               String var25 = var3.getValue(var18);
               String var27 = var25;
               if (var3.isSpecified(var18) && var23 != XMLSymbols.fCDATASymbol) {
                  var12 = this.normalizeAttrValue(var3, var18);
                  var27 = var3.getValue(var18);
                  if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && var12 && this.fDTDGrammar.getAttributeDeclIsExternal(var22)) {
                     this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE", new Object[]{var7, var25, var27}, (short)1);
                  }
               }

               if (this.fPerformValidation) {
                  if (this.fTempAttDecl.simpleType.defaultType == 1) {
                     String var28 = this.fTempAttDecl.simpleType.defaultValue;
                     if (!var27.equals(var28)) {
                        Object[] var16 = new Object[]{var1.localpart, var7, var27, var28};
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_FIXED_ATTVALUE_INVALID", var16, (short)1);
                     }
                  }

                  if (this.fTempAttDecl.simpleType.type == 1 || this.fTempAttDecl.simpleType.type == 2 || this.fTempAttDecl.simpleType.type == 3 || this.fTempAttDecl.simpleType.type == 4 || this.fTempAttDecl.simpleType.type == 5 || this.fTempAttDecl.simpleType.type == 6) {
                     this.validateDTDattribute(var1, var27, this.fTempAttDecl);
                  }
               }
            }
         }

      }
   }

   protected String getExternalEntityRefInAttrValue(String var1) {
      int var2 = var1.length();

      for(int var3 = var1.indexOf(38); var3 != -1; var3 = var1.indexOf(38, var3 + 1)) {
         if (var3 + 1 < var2 && var1.charAt(var3 + 1) != '#') {
            int var4 = var1.indexOf(59, var3 + 1);
            String var5 = var1.substring(var3 + 1, var4);
            var5 = this.fSymbolTable.addSymbol(var5);
            int var6 = this.fDTDGrammar.getEntityDeclIndex(var5);
            if (var6 > -1) {
               this.fDTDGrammar.getEntityDecl(var6, this.fEntityDecl);
               if (this.fEntityDecl.inExternal || (var5 = this.getExternalEntityRefInAttrValue(this.fEntityDecl.value)) != null) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   protected void validateDTDattribute(QName var1, String var2, XMLAttributeDecl var3) throws XNIException {
      boolean var4;
      switch (var3.simpleType.type) {
         case 1:
            var4 = var3.simpleType.list;

            try {
               if (var4) {
                  this.fValENTITIES.validate(var2, this.fValidationState);
               } else {
                  this.fValENTITY.validate(var2, this.fValidationState);
               }
            } catch (InvalidDatatypeValueException var10) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", var10.getKey(), var10.getArgs(), (short)1);
            }
            break;
         case 2:
         case 6:
            var4 = false;
            String[] var5 = var3.simpleType.enumeration;
            if (var5 == null) {
               var4 = false;
            } else {
               for(int var6 = 0; var6 < var5.length; ++var6) {
                  if (var2 == var5[var6] || var2.equals(var5[var6])) {
                     var4 = true;
                     break;
                  }
               }
            }

            if (!var4) {
               StringBuffer var7 = new StringBuffer();
               if (var5 != null) {
                  for(int var8 = 0; var8 < var5.length; ++var8) {
                     var7.append(var5[var8] + " ");
                  }
               }

               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_VALUE_NOT_IN_LIST", new Object[]{var3.name.rawname, var2, var7}, (short)1);
            }
            break;
         case 3:
            try {
               this.fValID.validate(var2, this.fValidationState);
            } catch (InvalidDatatypeValueException var9) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", var9.getKey(), var9.getArgs(), (short)1);
            }
            break;
         case 4:
            var4 = var3.simpleType.list;

            try {
               if (var4) {
                  this.fValIDRefs.validate(var2, this.fValidationState);
               } else {
                  this.fValIDRef.validate(var2, this.fValidationState);
               }
            } catch (InvalidDatatypeValueException var12) {
               if (var4) {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IDREFSInvalid", new Object[]{var2}, (short)1);
               } else {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", var12.getKey(), var12.getArgs(), (short)1);
               }
            }
            break;
         case 5:
            var4 = var3.simpleType.list;

            try {
               if (var4) {
                  this.fValNMTOKENS.validate(var2, this.fValidationState);
               } else {
                  this.fValNMTOKEN.validate(var2, this.fValidationState);
               }
            } catch (InvalidDatatypeValueException var11) {
               if (var4) {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENSInvalid", new Object[]{var2}, (short)1);
               } else {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENInvalid", new Object[]{var2}, (short)1);
               }
            }
      }

   }

   protected boolean invalidStandaloneAttDef(QName var1, QName var2) {
      boolean var3 = true;
      return var3;
   }

   private boolean normalizeAttrValue(XMLAttributes var1, int var2) {
      boolean var3 = true;
      boolean var4 = false;
      boolean var5 = false;
      int var6 = 0;
      int var7 = 0;
      String var8 = var1.getValue(var2);
      char[] var9 = new char[var8.length()];
      this.fBuffer.setLength(0);
      var8.getChars(0, var8.length(), var9, 0);

      for(int var10 = 0; var10 < var9.length; ++var10) {
         if (var9[var10] == ' ') {
            if (var5) {
               var4 = true;
               var5 = false;
            }

            if (var4 && !var3) {
               var4 = false;
               this.fBuffer.append(var9[var10]);
               ++var6;
            } else if (var3 || !var4) {
               ++var7;
            }
         } else {
            var5 = true;
            var4 = false;
            var3 = false;
            this.fBuffer.append(var9[var10]);
            ++var6;
         }
      }

      if (var6 > 0 && this.fBuffer.charAt(var6 - 1) == ' ') {
         this.fBuffer.setLength(var6 - 1);
      }

      String var11 = this.fBuffer.toString();
      var1.setValue(var2, var11);
      return !var8.equals(var11);
   }

   private final void rootElementSpecified(QName var1) throws XNIException {
      if (this.fPerformValidation) {
         String var2 = this.fRootElement.rawname;
         String var3 = var1.rawname;
         if (var2 == null || !var2.equals(var3)) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[]{var2, var3}, (short)1);
         }
      }

   }

   private int checkContent(int var1, QName[] var2, int var3, int var4) throws XNIException {
      this.fDTDGrammar.getElementDecl(var1, this.fTempElementDecl);
      String var5 = this.fCurrentElement.rawname;
      int var6 = this.fCurrentContentSpecType;
      if (var6 == 1) {
         if (var4 != 0) {
            return 0;
         }
      } else if (var6 != 0) {
         if (var6 == 2 || var6 == 3) {
            ContentModelValidator var7 = null;
            var7 = this.fTempElementDecl.contentModelValidator;
            int var8 = var7.validate(var2, var3, var4);
            return var8;
         }

         if (var6 != -1 && var6 == 4) {
         }
      }

      return -1;
   }

   private int getContentSpecType(int var1) {
      short var2 = -1;
      if (var1 > -1 && this.fDTDGrammar.getElementDecl(var1, this.fTempElementDecl)) {
         var2 = this.fTempElementDecl.type;
      }

      return var2;
   }

   private void charDataInContent() {
      if (this.fElementChildren.length <= this.fElementChildrenLength) {
         QName[] var1 = new QName[this.fElementChildren.length * 2];
         System.arraycopy(this.fElementChildren, 0, var1, 0, this.fElementChildren.length);
         this.fElementChildren = var1;
      }

      QName var3 = this.fElementChildren[this.fElementChildrenLength];
      if (var3 == null) {
         for(int var2 = this.fElementChildrenLength; var2 < this.fElementChildren.length; ++var2) {
            this.fElementChildren[var2] = new QName();
         }

         var3 = this.fElementChildren[this.fElementChildrenLength];
      }

      var3.clear();
      ++this.fElementChildrenLength;
   }

   private String getAttributeTypeName(XMLAttributeDecl var1) {
      switch (var1.simpleType.type) {
         case 1:
            return var1.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
         case 2:
            StringBuffer var2 = new StringBuffer();
            var2.append('(');

            for(int var3 = 0; var3 < var1.simpleType.enumeration.length; ++var3) {
               if (var3 > 0) {
                  var2.append("|");
               }

               var2.append(var1.simpleType.enumeration[var3]);
            }

            var2.append(')');
            return this.fSymbolTable.addSymbol(var2.toString());
         case 3:
            return XMLSymbols.fIDSymbol;
         case 4:
            return var1.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
         case 5:
            return var1.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
         case 6:
            return XMLSymbols.fNOTATIONSymbol;
         default:
            return XMLSymbols.fCDATASymbol;
      }
   }

   protected void init() {
      if (this.fValidation || this.fDynamicValidation) {
         try {
            this.fValID = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDSymbol);
            this.fValIDRef = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSymbol);
            this.fValIDRefs = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSSymbol);
            this.fValENTITY = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITYSymbol);
            this.fValENTITIES = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITIESSymbol);
            this.fValNMTOKEN = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSymbol);
            this.fValNMTOKENS = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSSymbol);
            this.fValNOTATION = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNOTATIONSymbol);
         } catch (Exception var2) {
            var2.printStackTrace(System.err);
         }
      }

   }

   private void ensureStackCapacity(int var1) {
      if (var1 == this.fElementQNamePartsStack.length) {
         int[] var2 = new int[var1 * 2];
         QName[] var3 = new QName[var1 * 2];
         System.arraycopy(this.fElementQNamePartsStack, 0, var3, 0, var1);
         this.fElementQNamePartsStack = var3;
         QName var4 = this.fElementQNamePartsStack[var1];
         if (var4 == null) {
            for(int var5 = var1; var5 < this.fElementQNamePartsStack.length; ++var5) {
               this.fElementQNamePartsStack[var5] = new QName();
            }
         }

         var2 = new int[var1 * 2];
         System.arraycopy(this.fElementIndexStack, 0, var2, 0, var1);
         this.fElementIndexStack = var2;
         var2 = new int[var1 * 2];
         System.arraycopy(this.fContentSpecTypeStack, 0, var2, 0, var1);
         this.fContentSpecTypeStack = var2;
      }

   }

   protected boolean handleStartElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      if (!this.fSeenRootElement) {
         this.fPerformValidation = this.validate();
         this.fSeenRootElement = true;
         this.fValidationManager.setEntityState(this.fDTDGrammar);
         this.fValidationManager.setGrammarFound(this.fSeenDoctypeDecl);
         this.rootElementSpecified(var1);
      }

      if (this.fDTDGrammar == null) {
         if (!this.fPerformValidation) {
            this.fCurrentElementIndex = -1;
            this.fCurrentContentSpecType = -1;
            this.fInElementContent = false;
         }

         if (this.fPerformValidation) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[]{var1.rawname}, (short)1);
         }

         if (this.fDocumentSource != null) {
            this.fDocumentSource.setDocumentHandler(this.fDocumentHandler);
            if (this.fDocumentHandler != null) {
               this.fDocumentHandler.setDocumentSource(this.fDocumentSource);
            }

            return true;
         }
      } else {
         this.fCurrentElementIndex = this.fDTDGrammar.getElementDeclIndex(var1);
         this.fCurrentContentSpecType = this.fDTDGrammar.getContentSpecType(this.fCurrentElementIndex);
         if (this.fCurrentContentSpecType == -1 && this.fPerformValidation) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_NOT_DECLARED", new Object[]{var1.rawname}, (short)1);
         }

         this.addDTDDefaultAttrsAndValidate(var1, this.fCurrentElementIndex, var2);
      }

      this.fInElementContent = this.fCurrentContentSpecType == 3;
      ++this.fElementDepth;
      if (this.fPerformValidation) {
         if (this.fElementChildrenOffsetStack.length <= this.fElementDepth) {
            int[] var4 = new int[this.fElementChildrenOffsetStack.length * 2];
            System.arraycopy(this.fElementChildrenOffsetStack, 0, var4, 0, this.fElementChildrenOffsetStack.length);
            this.fElementChildrenOffsetStack = var4;
         }

         this.fElementChildrenOffsetStack[this.fElementDepth] = this.fElementChildrenLength;
         if (this.fElementChildren.length <= this.fElementChildrenLength) {
            QName[] var6 = new QName[this.fElementChildrenLength * 2];
            System.arraycopy(this.fElementChildren, 0, var6, 0, this.fElementChildren.length);
            this.fElementChildren = var6;
         }

         QName var7 = this.fElementChildren[this.fElementChildrenLength];
         if (var7 == null) {
            for(int var5 = this.fElementChildrenLength; var5 < this.fElementChildren.length; ++var5) {
               this.fElementChildren[var5] = new QName();
            }

            var7 = this.fElementChildren[this.fElementChildrenLength];
         }

         var7.setValues(var1);
         ++this.fElementChildrenLength;
      }

      this.fCurrentElement.setValues(var1);
      this.ensureStackCapacity(this.fElementDepth);
      this.fElementQNamePartsStack[this.fElementDepth].setValues(this.fCurrentElement);
      this.fElementIndexStack[this.fElementDepth] = this.fCurrentElementIndex;
      this.fContentSpecTypeStack[this.fElementDepth] = this.fCurrentContentSpecType;
      this.startNamespaceScope(var1, var2, var3);
      return false;
   }

   protected void startNamespaceScope(QName var1, XMLAttributes var2, Augmentations var3) {
   }

   protected void handleEndElement(QName var1, Augmentations var2, boolean var3) throws XNIException {
      --this.fElementDepth;
      if (this.fPerformValidation) {
         int var4 = this.fCurrentElementIndex;
         if (var4 != -1 && this.fCurrentContentSpecType != -1) {
            QName[] var5 = this.fElementChildren;
            int var6 = this.fElementChildrenOffsetStack[this.fElementDepth + 1] + 1;
            int var7 = this.fElementChildrenLength - var6;
            int var8 = this.checkContent(var4, var5, var6, var7);
            if (var8 != -1) {
               this.fDTDGrammar.getElementDecl(var4, this.fTempElementDecl);
               if (this.fTempElementDecl.type == 1) {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID", new Object[]{var1.rawname, "EMPTY"}, (short)1);
               } else {
                  String var9 = var8 != var7 ? "MSG_CONTENT_INVALID" : "MSG_CONTENT_INCOMPLETE";
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", var9, new Object[]{var1.rawname, this.fDTDGrammar.getContentSpecAsString(var4)}, (short)1);
               }
            }
         }

         this.fElementChildrenLength = this.fElementChildrenOffsetStack[this.fElementDepth + 1] + 1;
      }

      this.endNamespaceScope(this.fCurrentElement, var2, var3);
      if (this.fElementDepth < -1) {
         throw new RuntimeException("FWK008 Element stack underflow");
      } else if (this.fElementDepth < 0) {
         this.fCurrentElement.clear();
         this.fCurrentElementIndex = -1;
         this.fCurrentContentSpecType = -1;
         this.fInElementContent = false;
         if (this.fPerformValidation) {
            String var10 = this.fValidationState.checkIDRefID();
            if (var10 != null) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_WITH_ID_REQUIRED", new Object[]{var10}, (short)1);
            }
         }

      } else {
         this.fCurrentElement.setValues(this.fElementQNamePartsStack[this.fElementDepth]);
         this.fCurrentElementIndex = this.fElementIndexStack[this.fElementDepth];
         this.fCurrentContentSpecType = this.fContentSpecTypeStack[this.fElementDepth];
         this.fInElementContent = this.fCurrentContentSpecType == 3;
      }
   }

   protected void endNamespaceScope(QName var1, Augmentations var2, boolean var3) {
      if (this.fDocumentHandler != null && !var3) {
         this.fDocumentHandler.endElement(this.fCurrentElement, var2);
      }

   }

   protected boolean isSpace(int var1) {
      return XMLChar.isSpace(var1);
   }

   public boolean characterData(String var1, Augmentations var2) {
      this.characters(new XMLString(var1.toCharArray(), 0, var1.length()), var2);
      return true;
   }

   static {
      FEATURE_DEFAULTS = new Boolean[]{null, null, Boolean.FALSE};
      RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager"};
      PROPERTY_DEFAULTS = new Object[]{null, null, null, null, null};
   }
}
