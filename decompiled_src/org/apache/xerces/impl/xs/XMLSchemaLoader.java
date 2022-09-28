package org.apache.xerces.impl.xs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DOMStringListImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.CMNodeFactory;
import org.apache.xerces.impl.xs.traversers.XSDHandler;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarLoader;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.LSInputList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;

public class XMLSchemaLoader implements XMLGrammarLoader, XMLComponent, XSLoader, DOMConfiguration {
   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
   protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
   protected static final String AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/validation/schema/augment-psvi", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/disallow-doctype-decl", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations"};
   public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
   protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://apache.org/xml/properties/security-manager"};
   private ParserConfigurationSettings fLoaderConfig;
   private SymbolTable fSymbolTable;
   private XMLErrorReporter fErrorReporter;
   private XMLEntityManager fEntityManager;
   private XMLEntityResolver fUserEntityResolver;
   private XMLGrammarPool fGrammarPool;
   private String fExternalSchemas;
   private String fExternalNoNSSchema;
   private Object fJAXPSource;
   private boolean fIsCheckedFully;
   private boolean fJAXPProcessed;
   private boolean fSettingsChanged;
   private XSDHandler fSchemaHandler;
   private XSGrammarBucket fGrammarBucket;
   private XSDeclarationPool fDeclPool;
   private SubstitutionGroupHandler fSubGroupHandler;
   private CMBuilder fCMBuilder;
   private XSDDescription fXSDDescription;
   private Hashtable fJAXPCache;
   private Locale fLocale;
   private DOMStringList fRecognizedParameters;
   private DOMErrorHandlerWrapper fErrorHandler;
   private DOMEntityResolverWrapper fResourceResolver;
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$io$File;
   // $FF: synthetic field
   static Class class$java$io$InputStream;
   // $FF: synthetic field
   static Class class$org$xml$sax$InputSource;

   public XMLSchemaLoader() {
      this(new SymbolTable(), (XMLErrorReporter)null, new XMLEntityManager(), (XSGrammarBucket)null, (SubstitutionGroupHandler)null, (CMBuilder)null);
   }

   public XMLSchemaLoader(SymbolTable var1) {
      this(var1, (XMLErrorReporter)null, new XMLEntityManager(), (XSGrammarBucket)null, (SubstitutionGroupHandler)null, (CMBuilder)null);
   }

   XMLSchemaLoader(XMLErrorReporter var1, XSGrammarBucket var2, SubstitutionGroupHandler var3, CMBuilder var4) {
      this((SymbolTable)null, var1, (XMLEntityManager)null, var2, var3, var4);
   }

   XMLSchemaLoader(SymbolTable var1, XMLErrorReporter var2, XMLEntityManager var3, XSGrammarBucket var4, SubstitutionGroupHandler var5, CMBuilder var6) {
      this.fLoaderConfig = new ParserConfigurationSettings();
      this.fSymbolTable = null;
      this.fErrorReporter = new XMLErrorReporter();
      this.fEntityManager = null;
      this.fUserEntityResolver = null;
      this.fGrammarPool = null;
      this.fExternalSchemas = null;
      this.fExternalNoNSSchema = null;
      this.fJAXPSource = null;
      this.fIsCheckedFully = false;
      this.fJAXPProcessed = false;
      this.fSettingsChanged = true;
      this.fDeclPool = null;
      this.fXSDDescription = new XSDDescription();
      this.fLocale = Locale.getDefault();
      this.fRecognizedParameters = null;
      this.fErrorHandler = null;
      this.fResourceResolver = null;
      this.fLoaderConfig.addRecognizedFeatures(RECOGNIZED_FEATURES);
      this.fLoaderConfig.addRecognizedProperties(RECOGNIZED_PROPERTIES);
      if (var1 != null) {
         this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", var1);
      }

      if (var2 == null) {
         var2 = new XMLErrorReporter();
         var2.setLocale(this.fLocale);
         var2.setProperty("http://apache.org/xml/properties/internal/error-handler", new DefaultErrorHandler());
      }

      this.fErrorReporter = var2;
      if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
      }

      this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
      this.fEntityManager = var3;
      if (this.fEntityManager != null) {
         this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
      }

      this.fLoaderConfig.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
      if (var4 == null) {
         var4 = new XSGrammarBucket();
      }

      this.fGrammarBucket = var4;
      if (var5 == null) {
         var5 = new SubstitutionGroupHandler(this.fGrammarBucket);
      }

      this.fSubGroupHandler = var5;
      CMNodeFactory var7 = new CMNodeFactory();
      if (var6 == null) {
         var6 = new CMBuilder(var7);
      }

      this.fCMBuilder = var6;
      this.fSchemaHandler = new XSDHandler(this.fGrammarBucket);
      this.fDeclPool = new XSDeclarationPool();
      this.fJAXPCache = new Hashtable();
      this.fSettingsChanged = true;
   }

   public String[] getRecognizedFeatures() {
      return (String[])RECOGNIZED_FEATURES.clone();
   }

   public boolean getFeature(String var1) throws XMLConfigurationException {
      return this.fLoaderConfig.getFeature(var1);
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      this.fSettingsChanged = true;
      if (var1.equals("http://apache.org/xml/features/continue-after-fatal-error")) {
         this.fErrorReporter.setFeature("http://apache.org/xml/features/continue-after-fatal-error", var2);
      } else if (var1.equals("http://apache.org/xml/features/generate-synthetic-annotations")) {
         this.fSchemaHandler.setGenerateSyntheticAnnotations(var2);
      }

      this.fLoaderConfig.setFeature(var1, var2);
   }

   public String[] getRecognizedProperties() {
      return (String[])RECOGNIZED_PROPERTIES.clone();
   }

   public Object getProperty(String var1) throws XMLConfigurationException {
      return this.fLoaderConfig.getProperty(var1);
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      this.fSettingsChanged = true;
      this.fLoaderConfig.setProperty(var1, var2);
      if (var1.equals("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
         this.fJAXPSource = var2;
         this.fJAXPProcessed = false;
      } else if (var1.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
         this.fGrammarPool = (XMLGrammarPool)var2;
      } else if (var1.equals("http://apache.org/xml/properties/schema/external-schemaLocation")) {
         this.fExternalSchemas = (String)var2;
      } else if (var1.equals("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation")) {
         this.fExternalNoNSSchema = (String)var2;
      } else if (var1.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
         this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", var2);
      } else if (var1.equals("http://apache.org/xml/properties/internal/error-reporter")) {
         this.fErrorReporter = (XMLErrorReporter)var2;
         if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
         }
      }

   }

   public void setLocale(Locale var1) {
      this.fLocale = var1;
      this.fErrorReporter.setLocale(var1);
   }

   public Locale getLocale() {
      return this.fLocale;
   }

   public void setErrorHandler(XMLErrorHandler var1) {
      this.fErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", var1);
   }

   public XMLErrorHandler getErrorHandler() {
      return this.fErrorReporter.getErrorHandler();
   }

   public void setEntityResolver(XMLEntityResolver var1) {
      this.fUserEntityResolver = var1;
      this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", var1);
      this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", var1);
   }

   public XMLEntityResolver getEntityResolver() {
      return this.fUserEntityResolver;
   }

   public void loadGrammar(XMLInputSource[] var1) throws IOException, XNIException {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.loadGrammar(var1[var3]);
      }

   }

   public Grammar loadGrammar(XMLInputSource var1) throws IOException, XNIException {
      this.reset(this.fLoaderConfig);
      this.fSettingsChanged = false;
      XSDDescription var2 = new XSDDescription();
      var2.fContextType = 3;
      var2.setBaseSystemId(var1.getBaseSystemId());
      var2.setLiteralSystemId(var1.getSystemId());
      Hashtable var3 = new Hashtable();
      processExternalHints(this.fExternalSchemas, this.fExternalNoNSSchema, var3, this.fErrorReporter);
      SchemaGrammar var4 = this.loadSchema(var2, var1, var3);
      if (var4 != null && this.fGrammarPool != null) {
         this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", this.fGrammarBucket.getGrammars());
         if (this.fIsCheckedFully && this.fJAXPCache.get(var4) != var4) {
            XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
         }
      }

      return var4;
   }

   SchemaGrammar loadSchema(XSDDescription var1, XMLInputSource var2, Hashtable var3) throws IOException, XNIException {
      if (!this.fJAXPProcessed) {
         this.processJAXPSchemaSource(var3);
      }

      SchemaGrammar var4 = this.fSchemaHandler.parseSchema(var2, var1, var3);
      return var4;
   }

   public static XMLInputSource resolveDocument(XSDDescription var0, Hashtable var1, XMLEntityResolver var2) throws IOException {
      String var3 = null;
      String var4;
      if (var0.getContextType() == 2 || var0.fromInstance()) {
         var4 = var0.getTargetNamespace();
         String var5 = var4 == null ? XMLSymbols.EMPTY_STRING : var4;
         LocationArray var6 = (LocationArray)var1.get(var5);
         if (var6 != null) {
            var3 = var6.getFirstLocation();
         }
      }

      if (var3 == null) {
         String[] var7 = var0.getLocationHints();
         if (var7 != null && var7.length > 0) {
            var3 = var7[0];
         }
      }

      var4 = XMLEntityManager.expandSystemId(var3, var0.getBaseSystemId(), false);
      var0.setLiteralSystemId(var3);
      var0.setExpandedSystemId(var4);
      return var2.resolveEntity(var0);
   }

   public static void processExternalHints(String var0, String var1, Hashtable var2, XMLErrorReporter var3) {
      XSAttributeDecl var4;
      if (var0 != null) {
         try {
            var4 = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
            var4.fType.validate((String)var0, (ValidationContext)null, (ValidatedInfo)null);
            if (!tokenizeSchemaLocationStr(var0, var2)) {
               var3.reportError("http://www.w3.org/TR/xml-schema-1", "SchemaLocation", new Object[]{var0}, (short)0);
            }
         } catch (InvalidDatatypeValueException var7) {
            var3.reportError("http://www.w3.org/TR/xml-schema-1", var7.getKey(), var7.getArgs(), (short)0);
         }
      }

      if (var1 != null) {
         try {
            var4 = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
            var4.fType.validate((String)var1, (ValidationContext)null, (ValidatedInfo)null);
            LocationArray var5 = (LocationArray)var2.get(XMLSymbols.EMPTY_STRING);
            if (var5 == null) {
               var5 = new LocationArray();
               var2.put(XMLSymbols.EMPTY_STRING, var5);
            }

            var5.addLocation(var1);
         } catch (InvalidDatatypeValueException var6) {
            var3.reportError("http://www.w3.org/TR/xml-schema-1", var6.getKey(), var6.getArgs(), (short)0);
         }
      }

   }

   public static boolean tokenizeSchemaLocationStr(String var0, Hashtable var1) {
      String var4;
      LocationArray var5;
      if (var0 != null) {
         for(StringTokenizer var2 = new StringTokenizer(var0, " \n\t\r"); var2.hasMoreTokens(); var5.addLocation(var4)) {
            String var3 = var2.nextToken();
            if (!var2.hasMoreTokens()) {
               return false;
            }

            var4 = var2.nextToken();
            var5 = (LocationArray)var1.get(var3);
            if (var5 == null) {
               var5 = new LocationArray();
               var1.put(var3, var5);
            }
         }
      }

      return true;
   }

   private void processJAXPSchemaSource(Hashtable var1) throws IOException {
      this.fJAXPProcessed = true;
      if (this.fJAXPSource != null) {
         Class var2 = this.fJAXPSource.getClass().getComponentType();
         XMLInputSource var3 = null;
         String var4 = null;
         if (var2 == null) {
            SchemaGrammar var10;
            if (this.fJAXPSource instanceof InputStream || this.fJAXPSource instanceof InputSource) {
               var10 = (SchemaGrammar)this.fJAXPCache.get(this.fJAXPSource);
               if (var10 != null) {
                  this.fGrammarBucket.putGrammar(var10);
                  return;
               }
            }

            this.fXSDDescription.reset();
            var3 = this.xsdToXMLInputSource(this.fJAXPSource);
            var4 = var3.getSystemId();
            this.fXSDDescription.fContextType = 3;
            if (var4 != null) {
               this.fXSDDescription.setBaseSystemId(var3.getBaseSystemId());
               this.fXSDDescription.setLiteralSystemId(var4);
               this.fXSDDescription.setExpandedSystemId(var4);
               this.fXSDDescription.fLocationHints = new String[]{var4};
            }

            var10 = this.loadSchema(this.fXSDDescription, var3, var1);
            if (var10 != null) {
               if (this.fJAXPSource instanceof InputStream || this.fJAXPSource instanceof InputSource) {
                  this.fJAXPCache.put(this.fJAXPSource, var10);
                  if (this.fIsCheckedFully) {
                     XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
                  }
               }

               this.fGrammarBucket.putGrammar(var10);
            }

         } else if (var2 != (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object) && var2 != (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String) && var2 != (class$java$io$File == null ? (class$java$io$File = class$("java.io.File")) : class$java$io$File) && var2 != (class$java$io$InputStream == null ? (class$java$io$InputStream = class$("java.io.InputStream")) : class$java$io$InputStream) && var2 != (class$org$xml$sax$InputSource == null ? (class$org$xml$sax$InputSource = class$("org.xml.sax.InputSource")) : class$org$xml$sax$InputSource)) {
            throw new XMLConfigurationException((short)1, "\"http://java.sun.com/xml/jaxp/properties/schemaSource\" property cannot have an array of type {" + var2.getName() + "}. Possible types of the array supported are Object, String, File, " + "InputStream, InputSource.");
         } else {
            Object[] var5 = (Object[])this.fJAXPSource;
            Vector var6 = new Vector();

            for(int var7 = 0; var7 < var5.length; ++var7) {
               SchemaGrammar var8;
               if (var5[var7] instanceof InputStream || var5[var7] instanceof InputSource) {
                  var8 = (SchemaGrammar)this.fJAXPCache.get(var5[var7]);
                  if (var8 != null) {
                     this.fGrammarBucket.putGrammar(var8);
                     continue;
                  }
               }

               this.fXSDDescription.reset();
               var3 = this.xsdToXMLInputSource(var5[var7]);
               var4 = var3.getSystemId();
               this.fXSDDescription.fContextType = 3;
               if (var4 != null) {
                  this.fXSDDescription.setBaseSystemId(var3.getBaseSystemId());
                  this.fXSDDescription.setLiteralSystemId(var4);
                  this.fXSDDescription.setExpandedSystemId(var4);
                  this.fXSDDescription.fLocationHints = new String[]{var4};
               }

               var8 = null;
               SchemaGrammar var9 = this.fSchemaHandler.parseSchema(var3, this.fXSDDescription, var1);
               if (this.fIsCheckedFully) {
                  XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
               }

               if (var9 != null) {
                  String var11 = var9.getTargetNamespace();
                  if (var6.contains(var11)) {
                     throw new IllegalArgumentException(" When using array of Objects as the value of SCHEMA_SOURCE property , no two Schemas should share the same targetNamespace. ");
                  }

                  var6.add(var11);
                  if (var5[var7] instanceof InputStream || var5[var7] instanceof InputSource) {
                     this.fJAXPCache.put(var5[var7], var9);
                  }

                  this.fGrammarBucket.putGrammar(var9);
               }
            }

         }
      }
   }

   private XMLInputSource xsdToXMLInputSource(Object var1) {
      if (var1 instanceof String) {
         String var7 = (String)var1;
         this.fXSDDescription.reset();
         this.fXSDDescription.setValues((String)null, var7, (String)null, (String)null);
         XMLInputSource var8 = null;

         try {
            var8 = this.fEntityManager.resolveEntity(this.fXSDDescription);
         } catch (IOException var5) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", new Object[]{var7}, (short)1);
         }

         return var8 == null ? new XMLInputSource((String)null, var7, (String)null) : var8;
      } else if (var1 instanceof InputSource) {
         return saxToXMLInputSource((InputSource)var1);
      } else if (var1 instanceof InputStream) {
         return new XMLInputSource((String)null, (String)null, (String)null, (InputStream)var1, (String)null);
      } else if (var1 instanceof File) {
         File var2 = (File)var1;
         BufferedInputStream var3 = null;

         try {
            var3 = new BufferedInputStream(new FileInputStream(var2));
         } catch (FileNotFoundException var6) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", new Object[]{var2.toString()}, (short)1);
         }

         return new XMLInputSource((String)null, (String)null, (String)null, var3, (String)null);
      } else {
         throw new XMLConfigurationException((short)1, "\"http://java.sun.com/xml/jaxp/properties/schemaSource\" property cannot have a value of type {" + var1.getClass().getName() + "}. Possible types of the value supported are String, File, InputStream, " + "InputSource OR an array of these types.");
      }
   }

   private static XMLInputSource saxToXMLInputSource(InputSource var0) {
      String var1 = var0.getPublicId();
      String var2 = var0.getSystemId();
      Reader var3 = var0.getCharacterStream();
      if (var3 != null) {
         return new XMLInputSource(var1, var2, (String)null, var3, (String)null);
      } else {
         InputStream var4 = var0.getByteStream();
         return var4 != null ? new XMLInputSource(var1, var2, (String)null, var4, var0.getEncoding()) : new XMLInputSource(var1, var2, (String)null);
      }
   }

   public Boolean getFeatureDefault(String var1) {
      return var1.equals("http://apache.org/xml/features/validation/schema/augment-psvi") ? Boolean.TRUE : null;
   }

   public Object getPropertyDefault(String var1) {
      return null;
   }

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      this.fGrammarBucket.reset();
      this.fSubGroupHandler.reset();

      boolean var2;
      try {
         var2 = var1.getFeature("http://apache.org/xml/features/internal/parser-settings");
      } catch (XMLConfigurationException var12) {
         var2 = true;
      }

      if (var2 && this.fSettingsChanged) {
         this.fEntityManager = (XMLEntityManager)var1.getProperty("http://apache.org/xml/properties/internal/entity-manager");
         this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");
         boolean var3 = true;

         try {
            var3 = var1.getFeature("http://apache.org/xml/features/validation/schema/augment-psvi");
         } catch (XMLConfigurationException var11) {
            var3 = false;
         }

         if (!var3) {
            this.fDeclPool.reset();
            this.fCMBuilder.setDeclPool(this.fDeclPool);
            this.fSchemaHandler.setDeclPool(this.fDeclPool);
         } else {
            this.fCMBuilder.setDeclPool((XSDeclarationPool)null);
            this.fSchemaHandler.setDeclPool((XSDeclarationPool)null);
         }

         try {
            this.fExternalSchemas = (String)var1.getProperty("http://apache.org/xml/properties/schema/external-schemaLocation");
            this.fExternalNoNSSchema = (String)var1.getProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation");
         } catch (XMLConfigurationException var10) {
            this.fExternalSchemas = null;
            this.fExternalNoNSSchema = null;
         }

         try {
            this.fJAXPSource = var1.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
            this.fJAXPProcessed = false;
         } catch (XMLConfigurationException var9) {
            this.fJAXPSource = null;
            this.fJAXPProcessed = false;
         }

         try {
            this.fGrammarPool = (XMLGrammarPool)var1.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
         } catch (XMLConfigurationException var8) {
            this.fGrammarPool = null;
         }

         this.initGrammarBucket();

         try {
            boolean var4 = var1.getFeature("http://apache.org/xml/features/continue-after-fatal-error");
            this.fErrorReporter.setFeature("http://apache.org/xml/features/continue-after-fatal-error", var4);
         } catch (XMLConfigurationException var7) {
         }

         try {
            this.fIsCheckedFully = var1.getFeature("http://apache.org/xml/features/validation/schema-full-checking");
         } catch (XMLConfigurationException var6) {
            this.fIsCheckedFully = false;
         }

         try {
            this.fSchemaHandler.setGenerateSyntheticAnnotations(var1.getFeature("http://apache.org/xml/features/generate-synthetic-annotations"));
         } catch (XMLConfigurationException var5) {
            this.fSchemaHandler.setGenerateSyntheticAnnotations(false);
         }

         this.fSchemaHandler.reset(var1);
      } else {
         this.initGrammarBucket();
      }
   }

   private void initGrammarBucket() {
      if (this.fGrammarPool != null) {
         Grammar[] var1 = this.fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!this.fGrammarBucket.putGrammar((SchemaGrammar)var1[var2], true)) {
               this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "GrammarConflict", (Object[])null, (short)0);
            }
         }
      }

   }

   public DOMConfiguration getConfig() {
      return this;
   }

   public XSModel load(LSInput var1) {
      try {
         Grammar var2 = this.loadGrammar(this.dom2xmlInputSource(var1));
         return ((XSGrammar)var2).toXSModel();
      } catch (Exception var3) {
         this.reportDOMFatalError(var3);
         return null;
      }
   }

   public XSModel loadInputList(LSInputList var1) {
      int var2 = var1.getLength();
      if (var2 == 0) {
         return null;
      } else {
         SchemaGrammar[] var3 = new SchemaGrammar[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            try {
               var3[var4] = (SchemaGrammar)this.loadGrammar(this.dom2xmlInputSource(var1.item(var4)));
            } catch (Exception var6) {
               this.reportDOMFatalError(var6);
               return null;
            }
         }

         return new XSModelImpl(var3);
      }
   }

   public XSModel loadURI(String var1) {
      try {
         Grammar var2 = this.loadGrammar(new XMLInputSource((String)null, var1, (String)null));
         return ((XSGrammar)var2).toXSModel();
      } catch (Exception var3) {
         this.reportDOMFatalError(var3);
         return null;
      }
   }

   public XSModel loadURIList(StringList var1) {
      int var2 = var1.getLength();
      if (var2 == 0) {
         return null;
      } else {
         SchemaGrammar[] var3 = new SchemaGrammar[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            try {
               var3[var4] = (SchemaGrammar)this.loadGrammar(new XMLInputSource((String)null, var1.item(var4), (String)null));
            } catch (Exception var6) {
               this.reportDOMFatalError(var6);
               return null;
            }
         }

         return new XSModelImpl(var3);
      }
   }

   void reportDOMFatalError(Exception var1) {
      if (this.fErrorHandler != null) {
         DOMErrorImpl var2 = new DOMErrorImpl();
         var2.fException = var1;
         var2.fMessage = var1.getMessage();
         var2.fSeverity = 3;
         this.fErrorHandler.getErrorHandler().handleError(var2);
      }

   }

   public boolean canSetParameter(String var1, Object var2) {
      if (var2 instanceof Boolean) {
         return var1.equals("validate") || var1.equals("http://apache.org/xml/features/validation/schema-full-checking") || var1.equals("http://apache.org/xml/features/validate-annotations") || var1.equals("http://apache.org/xml/features/continue-after-fatal-error") || var1.equals("http://apache.org/xml/features/allow-java-encodings") || var1.equals("http://apache.org/xml/features/standard-uri-conformant") || var1.equals("http://apache.org/xml/features/generate-synthetic-annotations") || var1.equals("http://apache.org/xml/features/honour-all-schemaLocations");
      } else {
         return var1.equals("error-handler") || var1.equals("resource-resolver") || var1.equals("http://apache.org/xml/properties/internal/symbol-table") || var1.equals("http://apache.org/xml/properties/internal/error-reporter") || var1.equals("http://apache.org/xml/properties/internal/error-handler") || var1.equals("http://apache.org/xml/properties/internal/entity-resolver") || var1.equals("http://apache.org/xml/properties/internal/grammar-pool") || var1.equals("http://apache.org/xml/properties/schema/external-schemaLocation") || var1.equals("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation") || var1.equals("http://java.sun.com/xml/jaxp/properties/schemaSource");
      }
   }

   public Object getParameter(String var1) throws DOMException {
      if (var1.equals("error-handler")) {
         return this.fErrorHandler != null ? this.fErrorHandler.getErrorHandler() : null;
      } else if (var1.equals("resource-resolver")) {
         return this.fResourceResolver != null ? this.fResourceResolver.getEntityResolver() : null;
      } else {
         try {
            boolean var2 = this.getFeature(var1);
            return var2 ? Boolean.TRUE : Boolean.FALSE;
         } catch (Exception var7) {
            try {
               Object var3 = this.getProperty(var1);
               return var3;
            } catch (Exception var6) {
               String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
               throw new DOMException((short)9, var5);
            }
         }
      }
   }

   public DOMStringList getParameterNames() {
      if (this.fRecognizedParameters == null) {
         Vector var1 = new Vector();
         var1.add("validate");
         var1.add("error-handler");
         var1.add("resource-resolver");
         var1.add("http://apache.org/xml/properties/internal/symbol-table");
         var1.add("http://apache.org/xml/properties/internal/error-reporter");
         var1.add("http://apache.org/xml/properties/internal/error-handler");
         var1.add("http://apache.org/xml/properties/internal/entity-resolver");
         var1.add("http://apache.org/xml/properties/internal/grammar-pool");
         var1.add("http://apache.org/xml/properties/schema/external-schemaLocation");
         var1.add("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation");
         var1.add("http://java.sun.com/xml/jaxp/properties/schemaSource");
         var1.add("http://apache.org/xml/features/validation/schema-full-checking");
         var1.add("http://apache.org/xml/features/continue-after-fatal-error");
         var1.add("http://apache.org/xml/features/allow-java-encodings");
         var1.add("http://apache.org/xml/features/standard-uri-conformant");
         var1.add("http://apache.org/xml/features/validate-annotations");
         var1.add("http://apache.org/xml/features/generate-synthetic-annotations");
         var1.add("http://apache.org/xml/features/honour-all-schemaLocations");
         this.fRecognizedParameters = new DOMStringListImpl(var1);
      }

      return this.fRecognizedParameters;
   }

   public void setParameter(String var1, Object var2) throws DOMException {
      if (var2 instanceof Boolean) {
         boolean var10 = (Boolean)var2;
         if (!var1.equals("validate") || !var10) {
            try {
               this.setFeature(var1, var10);
            } catch (Exception var6) {
               String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
               throw new DOMException((short)9, var5);
            }
         }
      } else {
         String var3;
         if (var1.equals("error-handler")) {
            if (var2 instanceof DOMErrorHandler) {
               try {
                  this.fErrorHandler = new DOMErrorHandlerWrapper((DOMErrorHandler)var2);
                  this.setErrorHandler(this.fErrorHandler);
               } catch (XMLConfigurationException var7) {
               }

            } else {
               var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
               throw new DOMException((short)9, var3);
            }
         } else if (var1.equals("resource-resolver")) {
            if (var2 instanceof LSResourceResolver) {
               try {
                  this.fResourceResolver = new DOMEntityResolverWrapper((LSResourceResolver)var2);
                  this.setEntityResolver(this.fResourceResolver);
               } catch (XMLConfigurationException var8) {
               }

            } else {
               var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
               throw new DOMException((short)9, var3);
            }
         } else {
            try {
               this.setProperty(var1, var2);
            } catch (Exception var9) {
               String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
               throw new DOMException((short)9, var4);
            }
         }
      }
   }

   XMLInputSource dom2xmlInputSource(LSInput var1) {
      XMLInputSource var2 = null;
      if (var1.getCharacterStream() != null) {
         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI(), var1.getCharacterStream(), "UTF-16");
      } else if (var1.getByteStream() != null) {
         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI(), var1.getByteStream(), var1.getEncoding());
      } else if (var1.getStringData() != null && var1.getStringData().length() != 0) {
         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI(), new StringReader(var1.getStringData()), "UTF-16");
      } else {
         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI());
      }

      return var2;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static class LocationArray {
      int length;
      String[] locations = new String[2];

      public void resize(int var1, int var2) {
         String[] var3 = new String[var2];
         System.arraycopy(this.locations, 0, var3, 0, Math.min(var1, var2));
         this.locations = var3;
         this.length = Math.min(var1, var2);
      }

      public void addLocation(String var1) {
         if (this.length >= this.locations.length) {
            this.resize(this.length, Math.max(1, this.length * 2));
         }

         this.locations[this.length++] = var1;
      }

      public String[] getLocationArray() {
         if (this.length < this.locations.length) {
            this.resize(this.locations.length, this.length);
         }

         return this.locations;
      }

      public String getFirstLocation() {
         return this.length > 0 ? this.locations[0] : null;
      }

      public int getLength() {
         return this.length;
      }
   }
}
