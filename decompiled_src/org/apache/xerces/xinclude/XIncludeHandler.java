package org.apache.xerces.xinclude;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.HTTPInputSource;
import org.apache.xerces.util.IntStack;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDFilter;
import org.apache.xerces.xni.parser.XMLDTDSource;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xpointer.XPointerHandler;
import org.apache.xerces.xpointer.XPointerProcessor;

public class XIncludeHandler implements XMLComponent, XMLDocumentFilter, XMLDTDFilter {
   public static final String XINCLUDE_DEFAULT_CONFIGURATION = "org.apache.xerces.parsers.XIncludeParserConfiguration";
   public static final String HTTP_ACCEPT = "Accept";
   public static final String HTTP_ACCEPT_LANGUAGE = "Accept-Language";
   public static final String XPOINTER = "xpointer";
   public static final String XINCLUDE_NS_URI = "http://www.w3.org/2001/XInclude".intern();
   public static final String XINCLUDE_INCLUDE = "include".intern();
   public static final String XINCLUDE_FALLBACK = "fallback".intern();
   public static final String XINCLUDE_PARSE_XML = "xml".intern();
   public static final String XINCLUDE_PARSE_TEXT = "text".intern();
   public static final String XINCLUDE_ATTR_HREF = "href".intern();
   public static final String XINCLUDE_ATTR_PARSE = "parse".intern();
   public static final String XINCLUDE_ATTR_ENCODING = "encoding".intern();
   public static final String XINCLUDE_ATTR_ACCEPT = "accept".intern();
   public static final String XINCLUDE_ATTR_ACCEPT_LANGUAGE = "accept-language".intern();
   public static final String XINCLUDE_INCLUDED = "[included]".intern();
   public static final String CURRENT_BASE_URI = "currentBaseURI";
   public static final String XINCLUDE_BASE = "base".intern();
   public static final QName XML_BASE_QNAME;
   public static final String XINCLUDE_LANG;
   public static final QName XML_LANG_QNAME;
   public static final QName NEW_NS_ATTR_QNAME;
   private static final int STATE_NORMAL_PROCESSING = 1;
   private static final int STATE_IGNORE = 2;
   private static final int STATE_EXPECT_FALLBACK = 3;
   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
   protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
   protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
   protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
   public static final String BUFFER_SIZE = "http://apache.org/xml/properties/input-buffer-size";
   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   private static final String[] RECOGNIZED_FEATURES;
   private static final Boolean[] FEATURE_DEFAULTS;
   private static final String[] RECOGNIZED_PROPERTIES;
   private static final Object[] PROPERTY_DEFAULTS;
   protected XMLDocumentHandler fDocumentHandler;
   protected XMLDocumentSource fDocumentSource;
   protected XMLDTDHandler fDTDHandler;
   protected XMLDTDSource fDTDSource;
   protected XIncludeHandler fParentXIncludeHandler;
   protected int fBufferSize = 2048;
   protected String fParentRelativeURI;
   protected XMLParserConfiguration fChildConfig;
   protected XMLParserConfiguration fXIncludeChildConfig;
   protected XMLParserConfiguration fXPointerChildConfig;
   protected XPointerProcessor fXPtrProcessor = null;
   protected XMLLocator fDocLocation;
   protected XIncludeMessageFormatter fXIncludeMessageFormatter = new XIncludeMessageFormatter();
   protected XIncludeNamespaceSupport fNamespaceContext;
   protected SymbolTable fSymbolTable;
   protected XMLErrorReporter fErrorReporter;
   protected XMLEntityResolver fEntityResolver;
   protected SecurityManager fSecurityManager;
   protected XIncludeTextReader fXInclude10TextReader;
   protected XIncludeTextReader fXInclude11TextReader;
   protected XMLResourceIdentifier fCurrentBaseURI;
   protected IntStack fBaseURIScope;
   protected Stack fBaseURI;
   protected Stack fLiteralSystemID;
   protected Stack fExpandedSystemID;
   protected IntStack fLanguageScope;
   protected Stack fLanguageStack;
   protected String fCurrentLanguage;
   protected ParserConfigurationSettings fSettings;
   private int fDepth = 0;
   private int fResultDepth;
   private static final int INITIAL_SIZE = 8;
   private boolean[] fSawInclude = new boolean[8];
   private boolean[] fSawFallback = new boolean[8];
   private int[] fState = new int[8];
   private ArrayList fNotations;
   private ArrayList fUnparsedEntities;
   private boolean fFixupBaseURIs = true;
   private boolean fFixupLanguage = true;
   private boolean fSendUEAndNotationEvents;
   private boolean fIsXML11;
   private boolean fInDTD;
   private boolean fSeenRootElement;
   private boolean fNeedCopyFeatures = true;
   private static boolean[] gNeedEscaping;
   private static char[] gAfterEscaping1;
   private static char[] gAfterEscaping2;
   private static char[] gHexChs;

   public XIncludeHandler() {
      this.fSawFallback[this.fDepth] = false;
      this.fSawInclude[this.fDepth] = false;
      this.fState[this.fDepth] = 1;
      this.fNotations = new ArrayList();
      this.fUnparsedEntities = new ArrayList();
      this.fBaseURIScope = new IntStack();
      this.fBaseURI = new Stack();
      this.fLiteralSystemID = new Stack();
      this.fExpandedSystemID = new Stack();
      this.fCurrentBaseURI = new XMLResourceIdentifierImpl();
      this.fLanguageScope = new IntStack();
      this.fLanguageStack = new Stack();
      this.fCurrentLanguage = null;
   }

   public void reset(XMLComponentManager var1) throws XNIException {
      this.fNamespaceContext = null;
      this.fDepth = 0;
      this.fResultDepth = this.isRootDocument() ? 0 : this.fParentXIncludeHandler.getResultDepth();
      this.fNotations.clear();
      this.fUnparsedEntities.clear();
      this.fParentRelativeURI = null;
      this.fIsXML11 = false;
      this.fInDTD = false;
      this.fSeenRootElement = false;
      this.fBaseURIScope.clear();
      this.fBaseURI.clear();
      this.fLiteralSystemID.clear();
      this.fExpandedSystemID.clear();
      this.fLanguageScope.clear();
      this.fLanguageStack.clear();

      for(int var2 = 0; var2 < this.fState.length; ++var2) {
         this.fState[var2] = 1;
      }

      for(int var3 = 0; var3 < this.fSawFallback.length; ++var3) {
         this.fSawFallback[var3] = false;
      }

      for(int var4 = 0; var4 < this.fSawInclude.length; ++var4) {
         this.fSawInclude[var4] = false;
      }

      try {
         if (!var1.getFeature("http://apache.org/xml/features/internal/parser-settings")) {
            return;
         }
      } catch (XMLConfigurationException var14) {
      }

      this.fNeedCopyFeatures = true;

      try {
         this.fSendUEAndNotationEvents = var1.getFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD");
         if (this.fChildConfig != null) {
            this.fChildConfig.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", this.fSendUEAndNotationEvents);
         }
      } catch (XMLConfigurationException var13) {
      }

      try {
         this.fFixupBaseURIs = var1.getFeature("http://apache.org/xml/features/xinclude/fixup-base-uris");
         if (this.fChildConfig != null) {
            this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", this.fFixupBaseURIs);
         }
      } catch (XMLConfigurationException var12) {
         this.fFixupBaseURIs = true;
      }

      try {
         this.fFixupLanguage = var1.getFeature("http://apache.org/xml/features/xinclude/fixup-language");
         if (this.fChildConfig != null) {
            this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-language", this.fFixupLanguage);
         }
      } catch (XMLConfigurationException var11) {
         this.fFixupLanguage = true;
      }

      try {
         SymbolTable var5 = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");
         if (var5 != null) {
            this.fSymbolTable = var5;
            if (this.fChildConfig != null) {
               this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", var5);
            }
         }
      } catch (XMLConfigurationException var10) {
         this.fSymbolTable = null;
      }

      try {
         XMLErrorReporter var16 = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");
         if (var16 != null) {
            this.setErrorReporter(var16);
            if (this.fChildConfig != null) {
               this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", var16);
            }
         }
      } catch (XMLConfigurationException var9) {
         this.fErrorReporter = null;
      }

      try {
         XMLEntityResolver var17 = (XMLEntityResolver)var1.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
         if (var17 != null) {
            this.fEntityResolver = var17;
            if (this.fChildConfig != null) {
               this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", var17);
            }
         }
      } catch (XMLConfigurationException var8) {
         this.fEntityResolver = null;
      }

      try {
         SecurityManager var18 = (SecurityManager)var1.getProperty("http://apache.org/xml/properties/security-manager");
         if (var18 != null) {
            this.fSecurityManager = var18;
            if (this.fChildConfig != null) {
               this.fChildConfig.setProperty("http://apache.org/xml/properties/security-manager", var18);
            }
         }
      } catch (XMLConfigurationException var7) {
         this.fSecurityManager = null;
      }

      try {
         Integer var19 = (Integer)var1.getProperty("http://apache.org/xml/properties/input-buffer-size");
         if (var19 != null && var19 > 0) {
            this.fBufferSize = var19;
            if (this.fChildConfig != null) {
               this.fChildConfig.setProperty("http://apache.org/xml/properties/input-buffer-size", var19);
            }
         } else {
            this.fBufferSize = (Integer)this.getPropertyDefault("http://apache.org/xml/properties/input-buffer-size");
         }
      } catch (XMLConfigurationException var15) {
         this.fBufferSize = (Integer)this.getPropertyDefault("http://apache.org/xml/properties/input-buffer-size");
      }

      if (this.fXInclude10TextReader != null) {
         this.fXInclude10TextReader.setBufferSize(this.fBufferSize);
      }

      if (this.fXInclude11TextReader != null) {
         this.fXInclude11TextReader.setBufferSize(this.fBufferSize);
      }

      this.fSettings = new ParserConfigurationSettings();
      this.copyFeatures(var1, this.fSettings);

      try {
         if (var1.getFeature("http://apache.org/xml/features/validation/schema")) {
            this.fSettings.setFeature("http://apache.org/xml/features/validation/schema", false);
            if (var1.getFeature("http://xml.org/sax/features/validation")) {
               this.fSettings.setFeature("http://apache.org/xml/features/validation/dynamic", true);
            }
         }
      } catch (XMLConfigurationException var6) {
      }

   }

   public String[] getRecognizedFeatures() {
      return (String[])RECOGNIZED_FEATURES.clone();
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      if (var1.equals("http://xml.org/sax/features/allow-dtd-events-after-endDTD")) {
         this.fSendUEAndNotationEvents = var2;
      }

      if (this.fSettings != null) {
         this.fNeedCopyFeatures = true;
         this.fSettings.setFeature(var1, var2);
      }

   }

   public String[] getRecognizedProperties() {
      return (String[])RECOGNIZED_PROPERTIES.clone();
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      if (var1.equals("http://apache.org/xml/properties/internal/symbol-table")) {
         this.fSymbolTable = (SymbolTable)var2;
         if (this.fChildConfig != null) {
            this.fChildConfig.setProperty(var1, var2);
         }

      } else if (var1.equals("http://apache.org/xml/properties/internal/error-reporter")) {
         this.setErrorReporter((XMLErrorReporter)var2);
         if (this.fChildConfig != null) {
            this.fChildConfig.setProperty(var1, var2);
         }

      } else if (var1.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
         this.fEntityResolver = (XMLEntityResolver)var2;
         if (this.fChildConfig != null) {
            this.fChildConfig.setProperty(var1, var2);
         }

      } else if (var1.equals("http://apache.org/xml/properties/security-manager")) {
         this.fSecurityManager = (SecurityManager)var2;
         if (this.fChildConfig != null) {
            this.fChildConfig.setProperty(var1, var2);
         }

      } else if (var1.equals("http://apache.org/xml/properties/input-buffer-size")) {
         Integer var3 = (Integer)var2;
         if (this.fChildConfig != null) {
            this.fChildConfig.setProperty(var1, var2);
         }

         if (var3 != null && var3 > 0) {
            this.fBufferSize = var3;
            if (this.fXInclude10TextReader != null) {
               this.fXInclude10TextReader.setBufferSize(this.fBufferSize);
            }

            if (this.fXInclude11TextReader != null) {
               this.fXInclude11TextReader.setBufferSize(this.fBufferSize);
            }
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

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
      this.fErrorReporter.setDocumentLocator(var1);
      if (!this.isRootDocument() && this.fParentXIncludeHandler.searchForRecursiveIncludes(var1)) {
         this.reportFatalError("RecursiveInclude", new Object[]{var1.getExpandedSystemId()});
      }

      if (!(var3 instanceof XIncludeNamespaceSupport)) {
         this.reportFatalError("IncompatibleNamespaceContext");
      }

      this.fNamespaceContext = (XIncludeNamespaceSupport)var3;
      this.fDocLocation = var1;
      this.fCurrentBaseURI.setBaseSystemId(var1.getBaseSystemId());
      this.fCurrentBaseURI.setExpandedSystemId(var1.getExpandedSystemId());
      this.fCurrentBaseURI.setLiteralSystemId(var1.getLiteralSystemId());
      this.saveBaseURI();
      if (var4 == null) {
         var4 = new AugmentationsImpl();
      }

      ((Augmentations)var4).putItem("currentBaseURI", this.fCurrentBaseURI);
      this.fCurrentLanguage = XMLSymbols.EMPTY_STRING;
      this.saveLanguage(this.fCurrentLanguage);
      if (this.isRootDocument() && this.fDocumentHandler != null) {
         this.fDocumentHandler.startDocument(var1, var2, var3, (Augmentations)var4);
      }

   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      this.fIsXML11 = "1.1".equals(var1);
      if (this.isRootDocument() && this.fDocumentHandler != null) {
         this.fDocumentHandler.xmlDecl(var1, var2, var3, var4);
      }

   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      if (this.isRootDocument() && this.fDocumentHandler != null) {
         this.fDocumentHandler.doctypeDecl(var1, var2, var3, var4);
      }

   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (!this.fInDTD) {
         if (this.fDocumentHandler != null && this.getState() == 1) {
            ++this.fDepth;
            var2 = this.modifyAugmentations(var2);
            this.fDocumentHandler.comment(var1, var2);
            --this.fDepth;
         }
      } else if (this.fDTDHandler != null) {
         this.fDTDHandler.comment(var1, var2);
      }

   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (!this.fInDTD) {
         if (this.fDocumentHandler != null && this.getState() == 1) {
            ++this.fDepth;
            var3 = this.modifyAugmentations(var3);
            this.fDocumentHandler.processingInstruction(var1, var2, var3);
            --this.fDepth;
         }
      } else if (this.fDTDHandler != null) {
         this.fDTDHandler.processingInstruction(var1, var2, var3);
      }

   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      ++this.fDepth;
      int var4 = this.getState(this.fDepth - 1);
      if (var4 == 3 && this.getState(this.fDepth - 2) == 3) {
         this.setState(2);
      } else {
         this.setState(var4);
      }

      this.processXMLBaseAttributes(var2);
      if (this.fFixupLanguage) {
         this.processXMLLangAttributes(var2);
      }

      if (this.isIncludeElement(var1)) {
         boolean var5 = this.handleIncludeElement(var2);
         if (var5) {
            this.setState(2);
         } else {
            this.setState(3);
         }
      } else if (this.isFallbackElement(var1)) {
         this.handleFallbackElement();
      } else if (this.hasXIncludeNamespace(var1)) {
         if (this.getSawInclude(this.fDepth - 1)) {
            this.reportFatalError("IncludeChild", new Object[]{var1.rawname});
         }

         if (this.getSawFallback(this.fDepth - 1)) {
            this.reportFatalError("FallbackChild", new Object[]{var1.rawname});
         }

         if (this.getState() == 1) {
            if (this.fResultDepth++ == 0) {
               this.checkMultipleRootElements();
            }

            if (this.fDocumentHandler != null) {
               var3 = this.modifyAugmentations(var3);
               var2 = this.processAttributes(var2);
               this.fDocumentHandler.startElement(var1, var2, var3);
            }
         }
      } else if (this.getState() == 1) {
         if (this.fResultDepth++ == 0) {
            this.checkMultipleRootElements();
         }

         if (this.fDocumentHandler != null) {
            var3 = this.modifyAugmentations(var3);
            var2 = this.processAttributes(var2);
            this.fDocumentHandler.startElement(var1, var2, var3);
         }
      }

   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      ++this.fDepth;
      int var4 = this.getState(this.fDepth - 1);
      if (var4 == 3 && this.getState(this.fDepth - 2) == 3) {
         this.setState(2);
      } else {
         this.setState(var4);
      }

      this.processXMLBaseAttributes(var2);
      if (this.fFixupLanguage) {
         this.processXMLLangAttributes(var2);
      }

      if (this.isIncludeElement(var1)) {
         boolean var5 = this.handleIncludeElement(var2);
         if (var5) {
            this.setState(2);
         } else {
            this.reportFatalError("NoFallback");
         }
      } else if (this.isFallbackElement(var1)) {
         this.handleFallbackElement();
      } else if (this.hasXIncludeNamespace(var1)) {
         if (this.getSawInclude(this.fDepth - 1)) {
            this.reportFatalError("IncludeChild", new Object[]{var1.rawname});
         }

         if (this.getSawFallback(this.fDepth - 1)) {
            this.reportFatalError("FallbackChild", new Object[]{var1.rawname});
         }

         if (this.getState() == 1) {
            if (this.fResultDepth == 0) {
               this.checkMultipleRootElements();
            }

            if (this.fDocumentHandler != null) {
               var3 = this.modifyAugmentations(var3);
               var2 = this.processAttributes(var2);
               this.fDocumentHandler.emptyElement(var1, var2, var3);
            }
         }
      } else if (this.getState() == 1) {
         if (this.fResultDepth == 0) {
            this.checkMultipleRootElements();
         }

         if (this.fDocumentHandler != null) {
            var3 = this.modifyAugmentations(var3);
            var2 = this.processAttributes(var2);
            this.fDocumentHandler.emptyElement(var1, var2, var3);
         }
      }

      this.setSawFallback(this.fDepth + 1, false);
      this.setSawInclude(this.fDepth, false);
      if (this.fBaseURIScope.size() > 0 && this.fDepth == this.fBaseURIScope.peek()) {
         this.restoreBaseURI();
      }

      --this.fDepth;
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      if (this.isIncludeElement(var1) && this.getState() == 3 && !this.getSawFallback(this.fDepth + 1)) {
         this.reportFatalError("NoFallback");
      }

      if (this.isFallbackElement(var1)) {
         if (this.getState() == 1) {
            this.setState(2);
         }
      } else if (this.getState() == 1) {
         --this.fResultDepth;
         if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endElement(var1, var2);
         }
      }

      this.setSawFallback(this.fDepth + 1, false);
      this.setSawInclude(this.fDepth, false);
      if (this.fBaseURIScope.size() > 0 && this.fDepth == this.fBaseURIScope.peek()) {
         this.restoreBaseURI();
      }

      if (this.fLanguageScope.size() > 0 && this.fDepth == this.fLanguageScope.peek()) {
         this.fCurrentLanguage = this.restoreLanguage();
      }

      --this.fDepth;
   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (this.getState() == 1) {
         if (this.fResultDepth == 0) {
            if (var4 != null && Boolean.TRUE.equals(var4.getItem("ENTITY_SKIPPED"))) {
               this.reportFatalError("UnexpandedEntityReferenceIllegal");
            }
         } else if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startGeneralEntity(var1, var2, var3, var4);
         }
      }

   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fDocumentHandler != null && this.getState() == 1) {
         this.fDocumentHandler.textDecl(var1, var2, var3);
      }

   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null && this.getState() == 1 && this.fResultDepth != 0) {
         this.fDocumentHandler.endGeneralEntity(var1, var2);
      }

   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      if (this.getState() == 1) {
         if (this.fResultDepth == 0) {
            this.checkWhitespace(var1);
         } else if (this.fDocumentHandler != null) {
            ++this.fDepth;
            var2 = this.modifyAugmentations(var2);
            this.fDocumentHandler.characters(var1, var2);
            --this.fDepth;
         }
      }

   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDocumentHandler != null && this.getState() == 1 && this.fResultDepth != 0) {
         this.fDocumentHandler.ignorableWhitespace(var1, var2);
      }

   }

   public void startCDATA(Augmentations var1) throws XNIException {
      if (this.fDocumentHandler != null && this.getState() == 1 && this.fResultDepth != 0) {
         this.fDocumentHandler.startCDATA(var1);
      }

   }

   public void endCDATA(Augmentations var1) throws XNIException {
      if (this.fDocumentHandler != null && this.getState() == 1 && this.fResultDepth != 0) {
         this.fDocumentHandler.endCDATA(var1);
      }

   }

   public void endDocument(Augmentations var1) throws XNIException {
      if (this.isRootDocument()) {
         if (!this.fSeenRootElement) {
            this.reportFatalError("RootElementRequired");
         }

         if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endDocument(var1);
         }
      }

   }

   public void setDocumentSource(XMLDocumentSource var1) {
      this.fDocumentSource = var1;
   }

   public XMLDocumentSource getDocumentSource() {
      return this.fDocumentSource;
   }

   public void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.attributeDecl(var1, var2, var3, var4, var5, var6, var7, var8);
      }

   }

   public void elementDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.elementDecl(var1, var2, var3);
      }

   }

   public void endAttlist(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endAttlist(var1);
      }

   }

   public void endConditional(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endConditional(var1);
      }

   }

   public void endDTD(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endDTD(var1);
      }

      this.fInDTD = false;
   }

   public void endExternalSubset(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endExternalSubset(var1);
      }

   }

   public void endParameterEntity(String var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endParameterEntity(var1, var2);
      }

   }

   public void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.externalEntityDecl(var1, var2, var3);
      }

   }

   public XMLDTDSource getDTDSource() {
      return this.fDTDSource;
   }

   public void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.ignoredCharacters(var1, var2);
      }

   }

   public void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.internalEntityDecl(var1, var2, var3, var4);
      }

   }

   public void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      this.addNotation(var1, var2, var3);
      if (this.fDTDHandler != null) {
         this.fDTDHandler.notationDecl(var1, var2, var3);
      }

   }

   public void setDTDSource(XMLDTDSource var1) {
      this.fDTDSource = var1;
   }

   public void startAttlist(String var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startAttlist(var1, var2);
      }

   }

   public void startConditional(short var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startConditional(var1, var2);
      }

   }

   public void startDTD(XMLLocator var1, Augmentations var2) throws XNIException {
      this.fInDTD = true;
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startDTD(var1, var2);
      }

   }

   public void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startExternalSubset(var1, var2);
      }

   }

   public void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startParameterEntity(var1, var2, var3, var4);
      }

   }

   public void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      this.addUnparsedEntity(var1, var2, var3, var4);
      if (this.fDTDHandler != null) {
         this.fDTDHandler.unparsedEntityDecl(var1, var2, var3, var4);
      }

   }

   public XMLDTDHandler getDTDHandler() {
      return this.fDTDHandler;
   }

   public void setDTDHandler(XMLDTDHandler var1) {
      this.fDTDHandler = var1;
   }

   private void setErrorReporter(XMLErrorReporter var1) {
      this.fErrorReporter = var1;
      if (this.fErrorReporter != null) {
         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xinclude", this.fXIncludeMessageFormatter);
         if (this.fDocLocation != null) {
            this.fErrorReporter.setDocumentLocator(this.fDocLocation);
         }
      }

   }

   protected void handleFallbackElement() {
      if (!this.getSawInclude(this.fDepth - 1)) {
         if (this.getState() == 2) {
            return;
         }

         this.reportFatalError("FallbackParent");
      }

      this.setSawInclude(this.fDepth, false);
      this.fNamespaceContext.setContextInvalid();
      if (this.getSawFallback(this.fDepth)) {
         this.reportFatalError("MultipleFallbacks");
      } else {
         this.setSawFallback(this.fDepth, true);
      }

      if (this.getState() == 3) {
         this.setState(1);
      }

   }

   protected boolean handleIncludeElement(XMLAttributes var1) throws XNIException {
      if (this.getSawInclude(this.fDepth - 1)) {
         this.reportFatalError("IncludeChild", new Object[]{XINCLUDE_INCLUDE});
      }

      if (this.getState() == 2) {
         return true;
      } else {
         this.setSawInclude(this.fDepth, true);
         this.fNamespaceContext.setContextInvalid();
         String var2 = var1.getValue(XINCLUDE_ATTR_HREF);
         String var3 = var1.getValue(XINCLUDE_ATTR_PARSE);
         String var4 = var1.getValue("xpointer");
         String var5 = var1.getValue(XINCLUDE_ATTR_ACCEPT);
         String var6 = var1.getValue(XINCLUDE_ATTR_ACCEPT_LANGUAGE);
         if (var3 == null) {
            var3 = XINCLUDE_PARSE_XML;
         }

         if (var2 == null) {
            var2 = XMLSymbols.EMPTY_STRING;
         }

         if (var2.length() == 0 && XINCLUDE_PARSE_XML.equals(var3)) {
            if (var4 != null) {
               Locale var58 = this.fErrorReporter != null ? this.fErrorReporter.getLocale() : null;
               String var60 = this.fXIncludeMessageFormatter.formatMessage(var58, "XPointerStreamability", (Object[])null);
               this.reportResourceError("XMLResourceError", new Object[]{var2, var60});
               return false;
            }

            this.reportFatalError("XpointerMissing");
         }

         URI var7 = null;

         String var9;
         try {
            var7 = new URI(var2, true);
            if (var7.getFragment() != null) {
               this.reportFatalError("HrefFragmentIdentifierIllegal", new Object[]{var2});
            }
         } catch (URI.MalformedURIException var57) {
            var9 = this.escapeHref(var2);
            if (var2 != var9) {
               var2 = var9;

               try {
                  var7 = new URI(var2, true);
                  if (var7.getFragment() != null) {
                     this.reportFatalError("HrefFragmentIdentifierIllegal", new Object[]{var2});
                  }
               } catch (URI.MalformedURIException var48) {
                  this.reportFatalError("HrefSyntacticallyInvalid", new Object[]{var9});
               }
            } else {
               this.reportFatalError("HrefSyntacticallyInvalid", new Object[]{var2});
            }
         }

         if (var5 != null && !this.isValidInHTTPHeader(var5)) {
            this.reportFatalError("AcceptMalformed", (Object[])null);
            var5 = null;
         }

         if (var6 != null && !this.isValidInHTTPHeader(var6)) {
            this.reportFatalError("AcceptLanguageMalformed", (Object[])null);
            var6 = null;
         }

         XMLInputSource var8 = null;
         if (this.fEntityResolver != null) {
            try {
               XMLResourceIdentifierImpl var59 = new XMLResourceIdentifierImpl((String)null, var2, this.fCurrentBaseURI.getExpandedSystemId(), XMLEntityManager.expandSystemId(var2, this.fCurrentBaseURI.getExpandedSystemId(), false));
               var8 = this.fEntityResolver.resolveEntity(var59);
               if (var8 != null && !(var8 instanceof HTTPInputSource) && (var5 != null || var6 != null) && var8.getCharacterStream() == null && var8.getByteStream() == null) {
                  var8 = this.createInputSource(var8.getPublicId(), var8.getSystemId(), var8.getBaseSystemId(), var5, var6);
               }
            } catch (IOException var56) {
               this.reportResourceError("XMLResourceError", new Object[]{var2, var56.getMessage()});
               return false;
            }
         }

         if (var8 == null) {
            if (var5 == null && var6 == null) {
               var8 = new XMLInputSource((String)null, var2, this.fCurrentBaseURI.getExpandedSystemId());
            } else {
               var8 = this.createInputSource((String)null, var2, this.fCurrentBaseURI.getExpandedSystemId(), var5, var6);
            }
         }

         if (var3.equals(XINCLUDE_PARSE_XML)) {
            if (var4 != null && this.fXPointerChildConfig == null || var4 == null && this.fXIncludeChildConfig == null) {
               var9 = "org.apache.xerces.parsers.XIncludeParserConfiguration";
               if (var4 != null) {
                  var9 = "org.apache.xerces.parsers.XPointerParserConfiguration";
               }

               this.fChildConfig = (XMLParserConfiguration)ObjectFactory.newInstance(var9, ObjectFactory.findClassLoader(), true);
               if (this.fSymbolTable != null) {
                  this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
               }

               if (this.fErrorReporter != null) {
                  this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
               }

               if (this.fEntityResolver != null) {
                  this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fEntityResolver);
               }

               this.fChildConfig.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
               this.fChildConfig.setProperty("http://apache.org/xml/properties/input-buffer-size", new Integer(this.fBufferSize));
               this.fNeedCopyFeatures = true;
               this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
               this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", this.fFixupBaseURIs);
               this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-language", this.fFixupLanguage);
               if (var4 != null) {
                  XPointerHandler var62 = (XPointerHandler)this.fChildConfig.getProperty("http://apache.org/xml/properties/internal/xpointer-handler");
                  this.fXPtrProcessor = var62;
                  ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
                  ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/features/xinclude/fixup-base-uris", new Boolean(this.fFixupBaseURIs));
                  ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/features/xinclude/fixup-language", new Boolean(this.fFixupLanguage));
                  if (this.fErrorReporter != null) {
                     ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
                  }

                  var62.setParent(this);
                  var62.setDocumentHandler(this.getDocumentHandler());
                  this.fXPointerChildConfig = this.fChildConfig;
               } else {
                  XIncludeHandler var63 = (XIncludeHandler)this.fChildConfig.getProperty("http://apache.org/xml/properties/internal/xinclude-handler");
                  var63.setParent(this);
                  var63.setDocumentHandler(this.getDocumentHandler());
                  this.fXIncludeChildConfig = this.fChildConfig;
               }
            }

            if (var4 != null) {
               this.fChildConfig = this.fXPointerChildConfig;

               try {
                  this.fXPtrProcessor.parseXPointer(var4);
               } catch (XNIException var47) {
                  this.reportResourceError("XMLResourceError", new Object[]{var2, var47.getMessage()});
                  return false;
               }
            } else {
               this.fChildConfig = this.fXIncludeChildConfig;
            }

            if (this.fNeedCopyFeatures) {
               this.copyFeatures(this.fSettings, (XMLParserConfiguration)this.fChildConfig);
            }

            this.fNeedCopyFeatures = false;

            boolean var11;
            try {
               this.fNamespaceContext.pushScope();
               this.fChildConfig.parse(var8);
               if (this.fErrorReporter != null) {
                  this.fErrorReporter.setDocumentLocator(this.fDocLocation);
               }

               if (var4 == null || this.fXPtrProcessor.isXPointerResolved()) {
                  return true;
               }

               Locale var61 = this.fErrorReporter != null ? this.fErrorReporter.getLocale() : null;
               String var64 = this.fXIncludeMessageFormatter.formatMessage(var61, "XPointerResolutionUnsuccessful", (Object[])null);
               this.reportResourceError("XMLResourceError", new Object[]{var2, var64});
               var11 = false;
               return var11;
            } catch (XNIException var49) {
               if (this.fErrorReporter != null) {
                  this.fErrorReporter.setDocumentLocator(this.fDocLocation);
               }

               this.reportFatalError("XMLParseError", new Object[]{var2});
               return true;
            } catch (IOException var50) {
               if (this.fErrorReporter != null) {
                  this.fErrorReporter.setDocumentLocator(this.fDocLocation);
               }

               this.reportResourceError("XMLResourceError", new Object[]{var2, var50.getMessage()});
               var11 = false;
            } finally {
               this.fNamespaceContext.popScope();
            }

            return var11;
         } else if (var3.equals(XINCLUDE_PARSE_TEXT)) {
            var9 = var1.getValue(XINCLUDE_ATTR_ENCODING);
            var8.setEncoding(var9);
            XIncludeTextReader var10 = null;

            boolean var14;
            try {
               if (!this.fIsXML11) {
                  if (this.fXInclude10TextReader == null) {
                     this.fXInclude10TextReader = new XIncludeTextReader(var8, this, this.fBufferSize);
                  } else {
                     this.fXInclude10TextReader.setInputSource(var8);
                  }

                  var10 = this.fXInclude10TextReader;
               } else {
                  if (this.fXInclude11TextReader == null) {
                     this.fXInclude11TextReader = new XInclude11TextReader(var8, this, this.fBufferSize);
                  } else {
                     this.fXInclude11TextReader.setInputSource(var8);
                  }

                  var10 = this.fXInclude11TextReader;
               }

               var10.setErrorReporter(this.fErrorReporter);
               var10.parse();
               return true;
            } catch (MalformedByteSequenceException var52) {
               this.fErrorReporter.reportError(var52.getDomain(), var52.getKey(), var52.getArguments(), (short)2);
               return true;
            } catch (CharConversionException var53) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", (Object[])null, (short)2);
               return true;
            } catch (IOException var54) {
               this.reportResourceError("TextResourceError", new Object[]{var2, var54.getMessage()});
               var14 = false;
            } finally {
               if (var10 != null) {
                  try {
                     var10.close();
                  } catch (IOException var46) {
                     this.reportResourceError("TextResourceError", new Object[]{var2, var46.getMessage()});
                     return false;
                  }
               }

            }

            return var14;
         } else {
            this.reportFatalError("InvalidParseValue", new Object[]{var3});
            return true;
         }
      }
   }

   protected boolean hasXIncludeNamespace(QName var1) {
      return var1.uri == XINCLUDE_NS_URI || this.fNamespaceContext.getURI(var1.prefix) == XINCLUDE_NS_URI;
   }

   protected boolean isIncludeElement(QName var1) {
      return var1.localpart.equals(XINCLUDE_INCLUDE) && this.hasXIncludeNamespace(var1);
   }

   protected boolean isFallbackElement(QName var1) {
      return var1.localpart.equals(XINCLUDE_FALLBACK) && this.hasXIncludeNamespace(var1);
   }

   protected boolean sameBaseURIAsIncludeParent() {
      String var1 = this.getIncludeParentBaseURI();
      String var2 = this.fCurrentBaseURI.getExpandedSystemId();
      return var1 != null && var1.equals(var2);
   }

   protected boolean sameLanguageAsIncludeParent() {
      String var1 = this.getIncludeParentLanguage();
      return var1 != null && var1.equalsIgnoreCase(this.fCurrentLanguage);
   }

   protected boolean searchForRecursiveIncludes(XMLLocator var1) {
      String var2 = var1.getExpandedSystemId();
      if (var2 == null) {
         try {
            var2 = XMLEntityManager.expandSystemId(var1.getLiteralSystemId(), var1.getBaseSystemId(), false);
         } catch (URI.MalformedURIException var4) {
            this.reportFatalError("ExpandedSystemId");
         }
      }

      if (var2.equals(this.fCurrentBaseURI.getExpandedSystemId())) {
         return true;
      } else {
         return this.fParentXIncludeHandler == null ? false : this.fParentXIncludeHandler.searchForRecursiveIncludes(var1);
      }
   }

   protected boolean isTopLevelIncludedItem() {
      return this.isTopLevelIncludedItemViaInclude() || this.isTopLevelIncludedItemViaFallback();
   }

   protected boolean isTopLevelIncludedItemViaInclude() {
      return this.fDepth == 1 && !this.isRootDocument();
   }

   protected boolean isTopLevelIncludedItemViaFallback() {
      return this.getSawFallback(this.fDepth - 1);
   }

   protected XMLAttributes processAttributes(XMLAttributes var1) {
      int var3;
      String var4;
      String var5;
      int var9;
      if (this.isTopLevelIncludedItem()) {
         if (this.fFixupBaseURIs && !this.sameBaseURIAsIncludeParent()) {
            if (var1 == null) {
               var1 = new XMLAttributesImpl();
            }

            String var2 = null;

            try {
               var2 = this.getRelativeBaseURI();
            } catch (URI.MalformedURIException var8) {
               var2 = this.fCurrentBaseURI.getExpandedSystemId();
            }

            var3 = ((XMLAttributes)var1).addAttribute(XML_BASE_QNAME, XMLSymbols.fCDATASymbol, var2);
            ((XMLAttributes)var1).setSpecified(var3, true);
         }

         if (this.fFixupLanguage && !this.sameLanguageAsIncludeParent()) {
            if (var1 == null) {
               var1 = new XMLAttributesImpl();
            }

            var9 = ((XMLAttributes)var1).addAttribute(XML_LANG_QNAME, XMLSymbols.fCDATASymbol, this.fCurrentLanguage);
            ((XMLAttributes)var1).setSpecified(var9, true);
         }

         Enumeration var11 = this.fNamespaceContext.getAllPrefixes();

         while(var11.hasMoreElements()) {
            String var10 = (String)var11.nextElement();
            var4 = this.fNamespaceContext.getURIFromIncludeParent(var10);
            var5 = this.fNamespaceContext.getURI(var10);
            if (var4 != var5 && var1 != null) {
               QName var6;
               int var7;
               if (var10 == XMLSymbols.EMPTY_STRING) {
                  if (((XMLAttributes)var1).getValue(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS) == null) {
                     if (var1 == null) {
                        var1 = new XMLAttributesImpl();
                     }

                     var6 = (QName)NEW_NS_ATTR_QNAME.clone();
                     var6.prefix = null;
                     var6.localpart = XMLSymbols.PREFIX_XMLNS;
                     var6.rawname = XMLSymbols.PREFIX_XMLNS;
                     var7 = ((XMLAttributes)var1).addAttribute(var6, XMLSymbols.fCDATASymbol, var5 != null ? var5 : XMLSymbols.EMPTY_STRING);
                     ((XMLAttributes)var1).setSpecified(var7, true);
                     this.fNamespaceContext.declarePrefix(var10, var5);
                  }
               } else if (((XMLAttributes)var1).getValue(NamespaceContext.XMLNS_URI, var10) == null) {
                  if (var1 == null) {
                     var1 = new XMLAttributesImpl();
                  }

                  var6 = (QName)NEW_NS_ATTR_QNAME.clone();
                  var6.localpart = var10;
                  var6.rawname = var6.rawname + var10;
                  var6.rawname = this.fSymbolTable != null ? this.fSymbolTable.addSymbol(var6.rawname) : var6.rawname.intern();
                  var7 = ((XMLAttributes)var1).addAttribute(var6, XMLSymbols.fCDATASymbol, var5 != null ? var5 : XMLSymbols.EMPTY_STRING);
                  ((XMLAttributes)var1).setSpecified(var7, true);
                  this.fNamespaceContext.declarePrefix(var10, var5);
               }
            }
         }
      }

      if (var1 != null) {
         var9 = ((XMLAttributes)var1).getLength();

         for(var3 = 0; var3 < var9; ++var3) {
            var4 = ((XMLAttributes)var1).getType(var3);
            var5 = ((XMLAttributes)var1).getValue(var3);
            if (var4 == XMLSymbols.fENTITYSymbol) {
               this.checkUnparsedEntity(var5);
            }

            if (var4 == XMLSymbols.fENTITIESSymbol) {
               StringTokenizer var12 = new StringTokenizer(var5);

               while(var12.hasMoreTokens()) {
                  String var13 = var12.nextToken();
                  this.checkUnparsedEntity(var13);
               }
            } else if (var4 == XMLSymbols.fNOTATIONSymbol) {
               this.checkNotation(var5);
            }
         }
      }

      return (XMLAttributes)var1;
   }

   protected String getRelativeBaseURI() throws URI.MalformedURIException {
      int var1 = this.getIncludeParentDepth();
      String var2 = this.getRelativeURI(var1);
      if (this.isRootDocument()) {
         return var2;
      } else {
         if (var2.equals("")) {
            var2 = this.fCurrentBaseURI.getLiteralSystemId();
         }

         if (var1 == 0) {
            if (this.fParentRelativeURI == null) {
               this.fParentRelativeURI = this.fParentXIncludeHandler.getRelativeBaseURI();
            }

            if (this.fParentRelativeURI.equals("")) {
               return var2;
            } else {
               URI var3 = new URI(this.fParentRelativeURI, true);
               URI var4 = new URI(var3, var2);
               String var5 = var3.getScheme();
               String var6 = var4.getScheme();
               if (!this.isEqual(var5, var6)) {
                  return var2;
               } else {
                  String var7 = var3.getAuthority();
                  String var8 = var4.getAuthority();
                  if (!this.isEqual(var7, var8)) {
                     return var4.getSchemeSpecificPart();
                  } else {
                     String var9 = var4.getPath();
                     String var10 = var4.getQueryString();
                     String var11 = var4.getFragment();
                     if (var10 == null && var11 == null) {
                        return var9;
                     } else {
                        StringBuffer var12 = new StringBuffer();
                        if (var9 != null) {
                           var12.append(var9);
                        }

                        if (var10 != null) {
                           var12.append('?');
                           var12.append(var10);
                        }

                        if (var11 != null) {
                           var12.append('#');
                           var12.append(var11);
                        }

                        return var12.toString();
                     }
                  }
               }
            }
         } else {
            return var2;
         }
      }
   }

   private String getIncludeParentBaseURI() {
      int var1 = this.getIncludeParentDepth();
      return !this.isRootDocument() && var1 == 0 ? this.fParentXIncludeHandler.getIncludeParentBaseURI() : this.getBaseURI(var1);
   }

   private String getIncludeParentLanguage() {
      int var1 = this.getIncludeParentDepth();
      return !this.isRootDocument() && var1 == 0 ? this.fParentXIncludeHandler.getIncludeParentLanguage() : this.getLanguage(var1);
   }

   private int getIncludeParentDepth() {
      for(int var1 = this.fDepth - 1; var1 >= 0; --var1) {
         if (!this.getSawInclude(var1) && !this.getSawFallback(var1)) {
            return var1;
         }
      }

      return 0;
   }

   private int getResultDepth() {
      return this.fResultDepth;
   }

   protected Augmentations modifyAugmentations(Augmentations var1) {
      return this.modifyAugmentations(var1, false);
   }

   protected Augmentations modifyAugmentations(Augmentations var1, boolean var2) {
      if (var2 || this.isTopLevelIncludedItem()) {
         if (var1 == null) {
            var1 = new AugmentationsImpl();
         }

         ((Augmentations)var1).putItem(XINCLUDE_INCLUDED, Boolean.TRUE);
      }

      return (Augmentations)var1;
   }

   protected int getState(int var1) {
      return this.fState[var1];
   }

   protected int getState() {
      return this.fState[this.fDepth];
   }

   protected void setState(int var1) {
      if (this.fDepth >= this.fState.length) {
         int[] var2 = new int[this.fDepth * 2];
         System.arraycopy(this.fState, 0, var2, 0, this.fState.length);
         this.fState = var2;
      }

      this.fState[this.fDepth] = var1;
   }

   protected void setSawFallback(int var1, boolean var2) {
      if (var1 >= this.fSawFallback.length) {
         boolean[] var3 = new boolean[var1 * 2];
         System.arraycopy(this.fSawFallback, 0, var3, 0, this.fSawFallback.length);
         this.fSawFallback = var3;
      }

      this.fSawFallback[var1] = var2;
   }

   protected boolean getSawFallback(int var1) {
      return var1 >= this.fSawFallback.length ? false : this.fSawFallback[var1];
   }

   protected void setSawInclude(int var1, boolean var2) {
      if (var1 >= this.fSawInclude.length) {
         boolean[] var3 = new boolean[var1 * 2];
         System.arraycopy(this.fSawInclude, 0, var3, 0, this.fSawInclude.length);
         this.fSawInclude = var3;
      }

      this.fSawInclude[var1] = var2;
   }

   protected boolean getSawInclude(int var1) {
      return var1 >= this.fSawInclude.length ? false : this.fSawInclude[var1];
   }

   protected void reportResourceError(String var1) {
      this.reportFatalError(var1, (Object[])null);
   }

   protected void reportResourceError(String var1, Object[] var2) {
      this.reportError(var1, var2, (short)0);
   }

   protected void reportFatalError(String var1) {
      this.reportFatalError(var1, (Object[])null);
   }

   protected void reportFatalError(String var1, Object[] var2) {
      this.reportError(var1, var2, (short)2);
   }

   private void reportError(String var1, Object[] var2, short var3) {
      if (this.fErrorReporter != null) {
         this.fErrorReporter.reportError("http://www.w3.org/TR/xinclude", var1, var2, var3);
      }

   }

   protected void setParent(XIncludeHandler var1) {
      this.fParentXIncludeHandler = var1;
   }

   protected boolean isRootDocument() {
      return this.fParentXIncludeHandler == null;
   }

   protected void addUnparsedEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) {
      UnparsedEntity var5 = new UnparsedEntity();
      var5.name = var1;
      var5.systemId = var2.getLiteralSystemId();
      var5.publicId = var2.getPublicId();
      var5.baseURI = var2.getBaseSystemId();
      var5.expandedSystemId = var2.getExpandedSystemId();
      var5.notation = var3;
      var5.augmentations = var4;
      this.fUnparsedEntities.add(var5);
   }

   protected void addNotation(String var1, XMLResourceIdentifier var2, Augmentations var3) {
      Notation var4 = new Notation();
      var4.name = var1;
      var4.systemId = var2.getLiteralSystemId();
      var4.publicId = var2.getPublicId();
      var4.baseURI = var2.getBaseSystemId();
      var4.expandedSystemId = var2.getExpandedSystemId();
      var4.augmentations = var3;
      this.fNotations.add(var4);
   }

   protected void checkUnparsedEntity(String var1) {
      UnparsedEntity var2 = new UnparsedEntity();
      var2.name = var1;
      int var3 = this.fUnparsedEntities.indexOf(var2);
      if (var3 != -1) {
         var2 = (UnparsedEntity)this.fUnparsedEntities.get(var3);
         this.checkNotation(var2.notation);
         this.checkAndSendUnparsedEntity(var2);
      }

   }

   protected void checkNotation(String var1) {
      Notation var2 = new Notation();
      var2.name = var1;
      int var3 = this.fNotations.indexOf(var2);
      if (var3 != -1) {
         var2 = (Notation)this.fNotations.get(var3);
         this.checkAndSendNotation(var2);
      }

   }

   protected void checkAndSendUnparsedEntity(UnparsedEntity var1) {
      if (this.isRootDocument()) {
         int var2 = this.fUnparsedEntities.indexOf(var1);
         if (var2 == -1) {
            XMLResourceIdentifierImpl var3 = new XMLResourceIdentifierImpl(var1.publicId, var1.systemId, var1.baseURI, var1.expandedSystemId);
            this.addUnparsedEntity(var1.name, var3, var1.notation, var1.augmentations);
            if (this.fSendUEAndNotationEvents && this.fDTDHandler != null) {
               this.fDTDHandler.unparsedEntityDecl(var1.name, var3, var1.notation, var1.augmentations);
            }
         } else {
            UnparsedEntity var4 = (UnparsedEntity)this.fUnparsedEntities.get(var2);
            if (!var1.isDuplicate(var4)) {
               this.reportFatalError("NonDuplicateUnparsedEntity", new Object[]{var1.name});
            }
         }
      } else {
         this.fParentXIncludeHandler.checkAndSendUnparsedEntity(var1);
      }

   }

   protected void checkAndSendNotation(Notation var1) {
      if (this.isRootDocument()) {
         int var2 = this.fNotations.indexOf(var1);
         if (var2 == -1) {
            XMLResourceIdentifierImpl var3 = new XMLResourceIdentifierImpl(var1.publicId, var1.systemId, var1.baseURI, var1.expandedSystemId);
            this.addNotation(var1.name, var3, var1.augmentations);
            if (this.fSendUEAndNotationEvents && this.fDTDHandler != null) {
               this.fDTDHandler.notationDecl(var1.name, var3, var1.augmentations);
            }
         } else {
            Notation var4 = (Notation)this.fNotations.get(var2);
            if (!var1.isDuplicate(var4)) {
               this.reportFatalError("NonDuplicateNotation", new Object[]{var1.name});
            }
         }
      } else {
         this.fParentXIncludeHandler.checkAndSendNotation(var1);
      }

   }

   private void checkWhitespace(XMLString var1) {
      int var2 = var1.offset + var1.length;

      for(int var3 = var1.offset; var3 < var2; ++var3) {
         if (!XMLChar.isSpace(var1.ch[var3])) {
            this.reportFatalError("ContentIllegalAtTopLevel");
            return;
         }
      }

   }

   private void checkMultipleRootElements() {
      if (this.getRootElementProcessed()) {
         this.reportFatalError("MultipleRootElements");
      }

      this.setRootElementProcessed(true);
   }

   private void setRootElementProcessed(boolean var1) {
      if (this.isRootDocument()) {
         this.fSeenRootElement = var1;
      } else {
         this.fParentXIncludeHandler.setRootElementProcessed(var1);
      }
   }

   private boolean getRootElementProcessed() {
      return this.isRootDocument() ? this.fSeenRootElement : this.fParentXIncludeHandler.getRootElementProcessed();
   }

   protected void copyFeatures(XMLComponentManager var1, ParserConfigurationSettings var2) {
      Enumeration var3 = Constants.getXercesFeatures();
      this.copyFeatures1(var3, "http://apache.org/xml/features/", var1, var2);
      var3 = Constants.getSAXFeatures();
      this.copyFeatures1(var3, "http://xml.org/sax/features/", var1, var2);
   }

   protected void copyFeatures(XMLComponentManager var1, XMLParserConfiguration var2) {
      Enumeration var3 = Constants.getXercesFeatures();
      this.copyFeatures1(var3, "http://apache.org/xml/features/", var1, var2);
      var3 = Constants.getSAXFeatures();
      this.copyFeatures1(var3, "http://xml.org/sax/features/", var1, var2);
   }

   private void copyFeatures1(Enumeration var1, String var2, XMLComponentManager var3, ParserConfigurationSettings var4) {
      while(var1.hasMoreElements()) {
         String var5 = var2 + (String)var1.nextElement();
         var4.addRecognizedFeatures(new String[]{var5});

         try {
            var4.setFeature(var5, var3.getFeature(var5));
         } catch (XMLConfigurationException var7) {
         }
      }

   }

   private void copyFeatures1(Enumeration var1, String var2, XMLComponentManager var3, XMLParserConfiguration var4) {
      while(var1.hasMoreElements()) {
         String var5 = var2 + (String)var1.nextElement();
         boolean var6 = var3.getFeature(var5);

         try {
            var4.setFeature(var5, var6);
         } catch (XMLConfigurationException var8) {
         }
      }

   }

   protected void saveBaseURI() {
      this.fBaseURIScope.push(this.fDepth);
      this.fBaseURI.push(this.fCurrentBaseURI.getBaseSystemId());
      this.fLiteralSystemID.push(this.fCurrentBaseURI.getLiteralSystemId());
      this.fExpandedSystemID.push(this.fCurrentBaseURI.getExpandedSystemId());
   }

   protected void restoreBaseURI() {
      this.fBaseURI.pop();
      this.fLiteralSystemID.pop();
      this.fExpandedSystemID.pop();
      this.fBaseURIScope.pop();
      this.fCurrentBaseURI.setBaseSystemId((String)this.fBaseURI.peek());
      this.fCurrentBaseURI.setLiteralSystemId((String)this.fLiteralSystemID.peek());
      this.fCurrentBaseURI.setExpandedSystemId((String)this.fExpandedSystemID.peek());
   }

   protected void saveLanguage(String var1) {
      this.fLanguageScope.push(this.fDepth);
      this.fLanguageStack.push(var1);
   }

   public String restoreLanguage() {
      this.fLanguageStack.pop();
      this.fLanguageScope.pop();
      return (String)this.fLanguageStack.peek();
   }

   public String getBaseURI(int var1) {
      int var2 = this.scopeOfBaseURI(var1);
      return (String)this.fExpandedSystemID.elementAt(var2);
   }

   public String getLanguage(int var1) {
      int var2 = this.scopeOfLanguage(var1);
      return (String)this.fLanguageStack.elementAt(var2);
   }

   public String getRelativeURI(int var1) throws URI.MalformedURIException {
      int var2 = this.scopeOfBaseURI(var1) + 1;
      if (var2 == this.fBaseURIScope.size()) {
         return "";
      } else {
         URI var3 = new URI("file", (String)this.fLiteralSystemID.elementAt(var2));

         for(int var4 = var2 + 1; var4 < this.fBaseURIScope.size(); ++var4) {
            var3 = new URI(var3, (String)this.fLiteralSystemID.elementAt(var4));
         }

         return var3.getPath();
      }
   }

   private int scopeOfBaseURI(int var1) {
      for(int var2 = this.fBaseURIScope.size() - 1; var2 >= 0; --var2) {
         if (this.fBaseURIScope.elementAt(var2) <= var1) {
            return var2;
         }
      }

      return -1;
   }

   private int scopeOfLanguage(int var1) {
      for(int var2 = this.fLanguageScope.size() - 1; var2 >= 0; --var2) {
         if (this.fLanguageScope.elementAt(var2) <= var1) {
            return var2;
         }
      }

      return -1;
   }

   protected void processXMLBaseAttributes(XMLAttributes var1) {
      String var2 = var1.getValue(NamespaceContext.XML_URI, "base");
      if (var2 != null) {
         try {
            String var3 = XMLEntityManager.expandSystemId(var2, this.fCurrentBaseURI.getExpandedSystemId(), false);
            this.fCurrentBaseURI.setLiteralSystemId(var2);
            this.fCurrentBaseURI.setBaseSystemId(this.fCurrentBaseURI.getExpandedSystemId());
            this.fCurrentBaseURI.setExpandedSystemId(var3);
            this.saveBaseURI();
         } catch (URI.MalformedURIException var4) {
         }
      }

   }

   protected void processXMLLangAttributes(XMLAttributes var1) {
      String var2 = var1.getValue(NamespaceContext.XML_URI, "lang");
      if (var2 != null) {
         this.fCurrentLanguage = var2;
         this.saveLanguage(this.fCurrentLanguage);
      }

   }

   private boolean isValidInHTTPHeader(String var1) {
      for(int var3 = var1.length() - 1; var3 >= 0; --var3) {
         char var2 = var1.charAt(var3);
         if (var2 < ' ' || var2 > '~') {
            return false;
         }
      }

      return true;
   }

   private XMLInputSource createInputSource(String var1, String var2, String var3, String var4, String var5) {
      HTTPInputSource var6 = new HTTPInputSource(var1, var2, var3);
      if (var4 != null && var4.length() > 0) {
         var6.setHTTPRequestProperty("Accept", var4);
      }

      if (var5 != null && var5.length() > 0) {
         var6.setHTTPRequestProperty("Accept-Language", var5);
      }

      return var6;
   }

   private boolean isEqual(String var1, String var2) {
      return var1 == var2 || var1 != null && var1.equals(var2);
   }

   private String escapeHref(String var1) {
      int var2 = var1.length();
      StringBuffer var4 = new StringBuffer(var2 * 3);

      int var3;
      int var5;
      for(var5 = 0; var5 < var2; ++var5) {
         var3 = var1.charAt(var5);
         if (var3 > 126) {
            break;
         }

         if (var3 < 32) {
            return var1;
         }

         if (gNeedEscaping[var3]) {
            var4.append('%');
            var4.append(gAfterEscaping1[var3]);
            var4.append(gAfterEscaping2[var3]);
         } else {
            var4.append((char)var3);
         }
      }

      if (var5 < var2) {
         int var6 = var5;

         while(true) {
            if (var6 >= var2) {
               Object var11 = null;

               byte[] var12;
               try {
                  var12 = var1.substring(var5).getBytes("UTF-8");
               } catch (UnsupportedEncodingException var10) {
                  return var1;
               }

               var2 = var12.length;

               for(var5 = 0; var5 < var2; ++var5) {
                  byte var8 = var12[var5];
                  if (var8 < 0) {
                     var3 = var8 + 256;
                     var4.append('%');
                     var4.append(gHexChs[var3 >> 4]);
                     var4.append(gHexChs[var3 & 15]);
                  } else if (gNeedEscaping[var8]) {
                     var4.append('%');
                     var4.append(gAfterEscaping1[var8]);
                     var4.append(gAfterEscaping2[var8]);
                  } else {
                     var4.append((char)var8);
                  }
               }

               return var4.length() != var2 ? var4.toString() : var1;
            }

            var3 = var1.charAt(var6);
            if ((var3 < 32 || var3 > 126) && (var3 < 160 || var3 > 55295) && (var3 < 63744 || var3 > 64975) && (var3 < 65008 || var3 > 65519)) {
               if (!XMLChar.isHighSurrogate(var3)) {
                  break;
               }

               ++var6;
               if (var6 >= var2) {
                  break;
               }

               int var7 = var1.charAt(var6);
               if (!XMLChar.isLowSurrogate(var7)) {
                  break;
               }

               var7 = XMLChar.supplemental((char)var3, (char)var7);
               if (var7 >= 983040 || (var7 & '\uffff') > 65533) {
                  break;
               }
            }

            ++var6;
         }

         return var1;
      } else {
         return var4.length() != var2 ? var4.toString() : var1;
      }
   }

   static {
      XML_BASE_QNAME = new QName(XMLSymbols.PREFIX_XML, XINCLUDE_BASE, (XMLSymbols.PREFIX_XML + ":" + XINCLUDE_BASE).intern(), NamespaceContext.XML_URI);
      XINCLUDE_LANG = "lang".intern();
      XML_LANG_QNAME = new QName(XMLSymbols.PREFIX_XML, XINCLUDE_LANG, (XMLSymbols.PREFIX_XML + ":" + XINCLUDE_LANG).intern(), NamespaceContext.XML_URI);
      NEW_NS_ATTR_QNAME = new QName(XMLSymbols.PREFIX_XMLNS, "", XMLSymbols.PREFIX_XMLNS + ":", NamespaceContext.XMLNS_URI);
      RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language"};
      FEATURE_DEFAULTS = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
      RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/security-manager", "http://apache.org/xml/properties/input-buffer-size"};
      PROPERTY_DEFAULTS = new Object[]{null, null, null, new Integer(2048)};
      gNeedEscaping = new boolean[128];
      gAfterEscaping1 = new char[128];
      gAfterEscaping2 = new char[128];
      gHexChs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      char[] var0 = new char[]{' ', '<', '>', '"', '{', '}', '|', '\\', '^', '`'};
      int var1 = var0.length;

      for(int var3 = 0; var3 < var1; ++var3) {
         char var2 = var0[var3];
         gNeedEscaping[var2] = true;
         gAfterEscaping1[var2] = gHexChs[var2 >> 4];
         gAfterEscaping2[var2] = gHexChs[var2 & 15];
      }

   }

   protected static class UnparsedEntity {
      public String name;
      public String systemId;
      public String baseURI;
      public String publicId;
      public String expandedSystemId;
      public String notation;
      public Augmentations augmentations;

      public boolean equals(Object var1) {
         if (var1 == null) {
            return false;
         } else if (var1 instanceof UnparsedEntity) {
            UnparsedEntity var2 = (UnparsedEntity)var1;
            return this.name.equals(var2.name);
         } else {
            return false;
         }
      }

      public boolean isDuplicate(Object var1) {
         if (var1 != null && var1 instanceof UnparsedEntity) {
            UnparsedEntity var2 = (UnparsedEntity)var1;
            return this.name.equals(var2.name) && this.isEqual(this.publicId, var2.publicId) && this.isEqual(this.expandedSystemId, var2.expandedSystemId) && this.isEqual(this.notation, var2.notation);
         } else {
            return false;
         }
      }

      private boolean isEqual(String var1, String var2) {
         return var1 == var2 || var1 != null && var1.equals(var2);
      }
   }

   protected static class Notation {
      public String name;
      public String systemId;
      public String baseURI;
      public String publicId;
      public String expandedSystemId;
      public Augmentations augmentations;

      public boolean equals(Object var1) {
         if (var1 == null) {
            return false;
         } else if (var1 instanceof Notation) {
            Notation var2 = (Notation)var1;
            return this.name.equals(var2.name);
         } else {
            return false;
         }
      }

      public boolean isDuplicate(Object var1) {
         if (var1 != null && var1 instanceof Notation) {
            Notation var2 = (Notation)var1;
            return this.name.equals(var2.name) && this.isEqual(this.publicId, var2.publicId) && this.isEqual(this.expandedSystemId, var2.expandedSystemId);
         } else {
            return false;
         }
      }

      private boolean isEqual(String var1, String var2) {
         return var1 == var2 || var1 != null && var1.equals(var2);
      }
   }
}
