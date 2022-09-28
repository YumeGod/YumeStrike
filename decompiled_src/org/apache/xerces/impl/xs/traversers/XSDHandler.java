package org.apache.xerces.impl.xs.traversers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.impl.xs.opti.SchemaDOM;
import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.impl.xs.opti.SchemaParsingConfig;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XML11Configuration;
import org.apache.xerces.util.DOMInputSource;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.SAXInputSource;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XSDHandler {
   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
   protected static final String XMLSCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
   protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
   private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
   protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
   public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
   protected static final boolean DEBUG_NODE_POOL = false;
   static final int ATTRIBUTE_TYPE = 1;
   static final int ATTRIBUTEGROUP_TYPE = 2;
   static final int ELEMENT_TYPE = 3;
   static final int GROUP_TYPE = 4;
   static final int IDENTITYCONSTRAINT_TYPE = 5;
   static final int NOTATION_TYPE = 6;
   static final int TYPEDECL_TYPE = 7;
   public static final String REDEF_IDENTIFIER = "_fn3dktizrknc9pi";
   protected Hashtable fNotationRegistry;
   protected XSDeclarationPool fDeclPool;
   private Hashtable fUnparsedAttributeRegistry;
   private Hashtable fUnparsedAttributeGroupRegistry;
   private Hashtable fUnparsedElementRegistry;
   private Hashtable fUnparsedGroupRegistry;
   private Hashtable fUnparsedIdentityConstraintRegistry;
   private Hashtable fUnparsedNotationRegistry;
   private Hashtable fUnparsedTypeRegistry;
   private Hashtable fUnparsedAttributeRegistrySub;
   private Hashtable fUnparsedAttributeGroupRegistrySub;
   private Hashtable fUnparsedElementRegistrySub;
   private Hashtable fUnparsedGroupRegistrySub;
   private Hashtable fUnparsedIdentityConstraintRegistrySub;
   private Hashtable fUnparsedNotationRegistrySub;
   private Hashtable fUnparsedTypeRegistrySub;
   private Hashtable fXSDocumentInfoRegistry;
   private Hashtable fDependencyMap;
   private Hashtable fImportMap;
   private Vector fAllTNSs;
   private Hashtable fLocationPairs;
   private Hashtable fHiddenNodes;
   private Hashtable fTraversed;
   private Hashtable fDoc2SystemId;
   private XSDocumentInfo fRoot;
   private Hashtable fDoc2XSDocumentMap;
   private Hashtable fRedefine2XSDMap;
   private Hashtable fRedefine2NSSupport;
   private Hashtable fRedefinedRestrictedAttributeGroupRegistry;
   private Hashtable fRedefinedRestrictedGroupRegistry;
   private boolean fLastSchemaWasDuplicate;
   private boolean fValidateAnnotations;
   private boolean fHonourAllSchemaLocations;
   private XMLErrorReporter fErrorReporter;
   private XMLEntityResolver fEntityResolver;
   private XSAttributeChecker fAttributeChecker;
   private SymbolTable fSymbolTable;
   private XSGrammarBucket fGrammarBucket;
   private XSDDescription fSchemaGrammarDescription;
   private XMLGrammarPool fGrammarPool;
   XSDAttributeGroupTraverser fAttributeGroupTraverser;
   XSDAttributeTraverser fAttributeTraverser;
   XSDComplexTypeTraverser fComplexTypeTraverser;
   XSDElementTraverser fElementTraverser;
   XSDGroupTraverser fGroupTraverser;
   XSDKeyrefTraverser fKeyrefTraverser;
   XSDNotationTraverser fNotationTraverser;
   XSDSimpleTypeTraverser fSimpleTypeTraverser;
   XSDUniqueOrKeyTraverser fUniqueOrKeyTraverser;
   XSDWildcardTraverser fWildCardTraverser;
   SchemaDOMParser fSchemaParser;
   SchemaContentHandler fXSContentHandler;
   XML11Configuration fAnnotationValidator;
   XSAnnotationGrammarPool fGrammarBucketAdapter;
   private static final int INIT_STACK_SIZE = 30;
   private static final int INC_STACK_SIZE = 10;
   private int fLocalElemStackPos;
   private XSParticleDecl[] fParticle;
   private Element[] fLocalElementDecl;
   private XSDocumentInfo[] fLocalElementDecl_schema;
   private int[] fAllContext;
   private XSObject[] fParent;
   private String[][] fLocalElemNamespaceContext;
   private static final int INIT_KEYREF_STACK = 2;
   private static final int INC_KEYREF_STACK_AMOUNT = 2;
   private int fKeyrefStackPos;
   private Element[] fKeyrefs;
   private XSDocumentInfo[] fKeyrefsMapXSDocumentInfo;
   private XSElementDecl[] fKeyrefElems;
   private String[][] fKeyrefNamespaceContext;
   private static final String[][] NS_ERROR_CODES = new String[][]{{"src-include.2.1", "src-include.2.1"}, {"src-redefine.3.1", "src-redefine.3.1"}, {"src-import.3.1", "src-import.3.2"}, null, {"TargetNamespace.1", "TargetNamespace.2"}, {"TargetNamespace.1", "TargetNamespace.2"}, {"TargetNamespace.1", "TargetNamespace.2"}, {"TargetNamespace.1", "TargetNamespace.2"}};
   private static final String[] ELE_ERROR_CODES = new String[]{"src-include.1", "src-redefine.2", "src-import.2", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4"};
   private Vector fReportedTNS;
   private static final String[] COMP_TYPE = new String[]{null, "attribute declaration", "attribute group", "element declaration", "group", "identity constraint", "notation", "type definition"};
   private static final String[] CIRCULAR_CODES = new String[]{"Internal-Error", "Internal-Error", "src-attribute_group.3", "e-props-correct.6", "mg-props-correct.2", "Internal-Error", "Internal-Error", "st-props-correct.2"};
   private SimpleLocator xl;

   private String null2EmptyString(String var1) {
      return var1 == null ? XMLSymbols.EMPTY_STRING : var1;
   }

   private String emptyString2Null(String var1) {
      return var1 == XMLSymbols.EMPTY_STRING ? null : var1;
   }

   private String doc2SystemId(Element var1) {
      String var2 = null;
      if (var1.getOwnerDocument() instanceof SchemaDOM) {
         var2 = ((SchemaDOM)var1.getOwnerDocument()).getDocumentURI();
      }

      return var2 != null ? var2 : (String)this.fDoc2SystemId.get(var1);
   }

   public XSDHandler() {
      this.fNotationRegistry = new Hashtable();
      this.fDeclPool = null;
      this.fUnparsedAttributeRegistry = new Hashtable();
      this.fUnparsedAttributeGroupRegistry = new Hashtable();
      this.fUnparsedElementRegistry = new Hashtable();
      this.fUnparsedGroupRegistry = new Hashtable();
      this.fUnparsedIdentityConstraintRegistry = new Hashtable();
      this.fUnparsedNotationRegistry = new Hashtable();
      this.fUnparsedTypeRegistry = new Hashtable();
      this.fUnparsedAttributeRegistrySub = new Hashtable();
      this.fUnparsedAttributeGroupRegistrySub = new Hashtable();
      this.fUnparsedElementRegistrySub = new Hashtable();
      this.fUnparsedGroupRegistrySub = new Hashtable();
      this.fUnparsedIdentityConstraintRegistrySub = new Hashtable();
      this.fUnparsedNotationRegistrySub = new Hashtable();
      this.fUnparsedTypeRegistrySub = new Hashtable();
      this.fXSDocumentInfoRegistry = new Hashtable();
      this.fDependencyMap = new Hashtable();
      this.fImportMap = new Hashtable();
      this.fAllTNSs = new Vector();
      this.fLocationPairs = null;
      this.fHiddenNodes = null;
      this.fTraversed = new Hashtable();
      this.fDoc2SystemId = new Hashtable();
      this.fRoot = null;
      this.fDoc2XSDocumentMap = new Hashtable();
      this.fRedefine2XSDMap = new Hashtable();
      this.fRedefine2NSSupport = new Hashtable();
      this.fRedefinedRestrictedAttributeGroupRegistry = new Hashtable();
      this.fRedefinedRestrictedGroupRegistry = new Hashtable();
      this.fValidateAnnotations = false;
      this.fHonourAllSchemaLocations = false;
      this.fLocalElemStackPos = 0;
      this.fParticle = new XSParticleDecl[30];
      this.fLocalElementDecl = new Element[30];
      this.fLocalElementDecl_schema = new XSDocumentInfo[30];
      this.fAllContext = new int[30];
      this.fParent = new XSObject[30];
      this.fLocalElemNamespaceContext = new String[30][1];
      this.fKeyrefStackPos = 0;
      this.fKeyrefs = new Element[2];
      this.fKeyrefsMapXSDocumentInfo = new XSDocumentInfo[2];
      this.fKeyrefElems = new XSElementDecl[2];
      this.fKeyrefNamespaceContext = new String[2][1];
      this.fReportedTNS = null;
      this.xl = new SimpleLocator();
      this.fHiddenNodes = new Hashtable();
      this.fSchemaParser = new SchemaDOMParser(new SchemaParsingConfig());
   }

   public XSDHandler(XSGrammarBucket var1) {
      this();
      this.fGrammarBucket = var1;
      this.fSchemaGrammarDescription = new XSDDescription();
   }

   public SchemaGrammar parseSchema(XMLInputSource var1, XSDDescription var2, Hashtable var3) throws IOException {
      this.fLocationPairs = var3;
      this.fSchemaParser.resetNodePool();
      SchemaGrammar var4 = null;
      String var5 = null;
      short var6 = var2.getContextType();
      if (var6 != 3) {
         if (this.fHonourAllSchemaLocations && var6 == 2 && this.isExistingGrammar(var2)) {
            var4 = this.fGrammarBucket.getGrammar(var2.getTargetNamespace());
         } else {
            var4 = this.findGrammar(var2);
         }

         if (var4 != null) {
            return var4;
         }

         var5 = var2.getTargetNamespace();
         if (var5 != null) {
            var5 = this.fSymbolTable.addSymbol(var5);
         }
      }

      this.prepareForParse();
      Document var7 = null;
      Element var8 = null;
      if (var1 instanceof DOMInputSource) {
         this.fHiddenNodes.clear();
         Node var9 = ((DOMInputSource)var1).getNode();
         if (var9 instanceof Document) {
            var7 = (Document)var9;
            var8 = DOMUtil.getRoot(var7);
         } else {
            if (!(var9 instanceof Element)) {
               return null;
            }

            var8 = (Element)var9;
         }
      } else if (var1 instanceof SAXInputSource) {
         Object var22 = ((SAXInputSource)var1).getXMLReader();
         InputSource var10 = ((SAXInputSource)var1).getInputSource();
         boolean var11 = false;
         if (var22 != null) {
            try {
               var11 = ((XMLReader)var22).getFeature("http://xml.org/sax/features/namespace-prefixes");
            } catch (SAXException var21) {
            }
         } else {
            try {
               var22 = XMLReaderFactory.createXMLReader();
            } catch (SAXException var20) {
               var22 = new SAXParser();
            }

            try {
               ((XMLReader)var22).setFeature("http://xml.org/sax/features/namespace-prefixes", true);
               var11 = true;
            } catch (SAXException var19) {
            }
         }

         boolean var12 = false;

         try {
            var12 = ((XMLReader)var22).getFeature("http://xml.org/sax/features/string-interning");
         } catch (SAXException var18) {
         }

         if (this.fXSContentHandler == null) {
            this.fXSContentHandler = new SchemaContentHandler();
         }

         this.fXSContentHandler.reset(this.fSchemaParser, this.fSymbolTable, var11, var12);
         ((XMLReader)var22).setContentHandler(this.fXSContentHandler);
         ((XMLReader)var22).setErrorHandler(this.fErrorReporter.getSAXErrorHandler());

         try {
            ((XMLReader)var22).parse(var10);
         } catch (SAXException var17) {
            return null;
         }

         var7 = this.fXSContentHandler.getDocument();
         if (var7 == null) {
            return null;
         }

         var8 = DOMUtil.getRoot(var7);
      } else {
         var8 = this.getSchemaDocument(var5, var1, var6 == 3, var6, (Element)null);
      }

      if (var8 == null) {
         return null;
      } else {
         if (var6 == 3) {
            var5 = DOMUtil.getAttrValue(var8, SchemaSymbols.ATT_TARGETNAMESPACE);
            if (var5 != null && var5.length() > 0) {
               var5 = this.fSymbolTable.addSymbol(var5);
               var2.setTargetNamespace(var5);
            } else {
               var5 = null;
            }

            var4 = this.findGrammar(var2);
            if (var4 != null) {
               return var4;
            }

            String var23 = XMLEntityManager.expandSystemId(var1.getSystemId(), var1.getBaseSystemId(), false);
            XSDKey var26 = new XSDKey(var23, var6, var5);
            this.fTraversed.put(var26, var8);
            if (var23 != null) {
               this.fDoc2SystemId.put(var8, var23);
            }
         }

         this.prepareForTraverse();
         this.fRoot = this.constructTrees(var8, var1.getSystemId(), var2);
         if (this.fRoot == null) {
            return null;
         } else {
            this.buildGlobalNameRegistries();
            ArrayList var25 = this.fValidateAnnotations ? new ArrayList() : null;
            this.traverseSchemas(var25);
            this.traverseLocalElements();
            this.resolveKeyRefs();

            for(int var24 = this.fAllTNSs.size() - 1; var24 >= 0; --var24) {
               String var27 = (String)this.fAllTNSs.elementAt(var24);
               Vector var28 = (Vector)this.fImportMap.get(var27);
               SchemaGrammar var13 = this.fGrammarBucket.getGrammar(this.emptyString2Null(var27));
               if (var13 != null) {
                  int var15 = 0;

                  for(int var16 = 0; var16 < var28.size(); ++var16) {
                     SchemaGrammar var14 = this.fGrammarBucket.getGrammar((String)var28.elementAt(var16));
                     if (var14 != null) {
                        var28.setElementAt(var14, var15++);
                     }
                  }

                  var28.setSize(var15);
                  var13.setImportedGrammars(var28);
               }
            }

            if (this.fValidateAnnotations && var25.size() > 0) {
               this.validateAnnotations(var25);
            }

            return this.fGrammarBucket.getGrammar(this.fRoot.fTargetNamespace);
         }
      }
   }

   private void validateAnnotations(ArrayList var1) {
      if (this.fAnnotationValidator == null) {
         this.createAnnotationValidator();
      }

      int var2 = var1.size();
      XMLInputSource var3 = new XMLInputSource((String)null, (String)null, (String)null);
      this.fGrammarBucketAdapter.refreshGrammars(this.fGrammarBucket);

      for(int var4 = 0; var4 < var2; var4 += 2) {
         var3.setSystemId((String)var1.get(var4));

         for(XSAnnotationInfo var5 = (XSAnnotationInfo)var1.get(var4 + 1); var5 != null; var5 = var5.next) {
            var3.setCharacterStream(new StringReader(var5.fAnnotation));

            try {
               this.fAnnotationValidator.parse(var3);
            } catch (IOException var7) {
            }
         }
      }

   }

   private void createAnnotationValidator() {
      this.fAnnotationValidator = new XML11Configuration();
      this.fGrammarBucketAdapter = new XSAnnotationGrammarPool();
      this.fAnnotationValidator.setFeature("http://xml.org/sax/features/validation", true);
      this.fAnnotationValidator.setFeature("http://apache.org/xml/features/validation/schema", true);
      this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarBucketAdapter);
      XMLErrorHandler var1 = this.fErrorReporter.getErrorHandler();
      this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", var1 != null ? var1 : new DefaultErrorHandler());
   }

   SchemaGrammar getGrammar(String var1) {
      return this.fGrammarBucket.getGrammar(var1);
   }

   protected SchemaGrammar findGrammar(XSDDescription var1) {
      SchemaGrammar var2 = this.fGrammarBucket.getGrammar(var1.getTargetNamespace());
      if (var2 == null && this.fGrammarPool != null) {
         var2 = (SchemaGrammar)this.fGrammarPool.retrieveGrammar(var1);
         if (var2 != null && !this.fGrammarBucket.putGrammar(var2, true)) {
            this.reportSchemaWarning("GrammarConflict", (Object[])null, (Element)null);
            var2 = null;
         }
      }

      return var2;
   }

   protected XSDocumentInfo constructTrees(Element var1, String var2, XSDDescription var3) {
      if (var1 == null) {
         return null;
      } else {
         String var4 = var3.getTargetNamespace();
         short var5 = var3.getContextType();
         XSDocumentInfo var6 = null;

         try {
            var6 = new XSDocumentInfo(var1, this.fAttributeChecker, this.fSymbolTable);
         } catch (XMLSchemaException var20) {
            this.reportSchemaError(ELE_ERROR_CODES[var5], new Object[]{var2}, var1);
            return null;
         }

         if (var6.fTargetNamespace != null && var6.fTargetNamespace.length() == 0) {
            this.reportSchemaWarning("EmptyTargetNamespace", new Object[]{var2}, var1);
            var6.fTargetNamespace = null;
         }

         byte var7;
         if (var4 != null) {
            var7 = 0;
            if (var5 != 0 && var5 != 1) {
               if (var5 != 3 && var4 != var6.fTargetNamespace) {
                  this.reportSchemaError(NS_ERROR_CODES[var5][var7], new Object[]{var4, var6.fTargetNamespace}, var1);
                  return null;
               }
            } else if (var6.fTargetNamespace == null) {
               var6.fTargetNamespace = var4;
               var6.fIsChameleonSchema = true;
            } else if (var4 != var6.fTargetNamespace) {
               this.reportSchemaError(NS_ERROR_CODES[var5][var7], new Object[]{var4, var6.fTargetNamespace}, var1);
               return null;
            }
         } else if (var6.fTargetNamespace != null) {
            if (var5 != 3) {
               var7 = 1;
               this.reportSchemaError(NS_ERROR_CODES[var5][var7], new Object[]{var4, var6.fTargetNamespace}, var1);
               return null;
            }

            var3.setTargetNamespace(var6.fTargetNamespace);
            var4 = var6.fTargetNamespace;
         }

         var6.addAllowedNS(var6.fTargetNamespace);
         SchemaGrammar var21 = null;
         if (var5 != 0 && var5 != 1) {
            if (this.fHonourAllSchemaLocations && var5 == 2) {
               var21 = this.findGrammar(var3);
               if (var21 == null) {
                  var21 = new SchemaGrammar(var6.fTargetNamespace, var3.makeClone(), this.fSymbolTable);
                  this.fGrammarBucket.putGrammar(var21);
               }
            } else {
               var21 = new SchemaGrammar(var6.fTargetNamespace, var3.makeClone(), this.fSymbolTable);
               this.fGrammarBucket.putGrammar(var21);
            }
         } else {
            var21 = this.fGrammarBucket.getGrammar(var6.fTargetNamespace);
         }

         var21.addDocument((Object)null, (String)this.fDoc2SystemId.get(var6.fSchemaElement));
         this.fDoc2XSDocumentMap.put(var1, var6);
         Vector var8 = new Vector();
         Element var10 = null;

         for(Element var11 = DOMUtil.getFirstChildElement(var1); var11 != null; var11 = DOMUtil.getNextSiblingElement(var11)) {
            String var12 = null;
            String var13 = null;
            String var14 = DOMUtil.getLocalName(var11);
            boolean var15 = true;
            if (!var14.equals(SchemaSymbols.ELT_ANNOTATION)) {
               Object[] var16;
               Element var17;
               String var18;
               if (var14.equals(SchemaSymbols.ELT_IMPORT)) {
                  var15 = true;
                  var16 = this.fAttributeChecker.checkAttributes(var11, true, var6);
                  var13 = (String)var16[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
                  var12 = (String)var16[XSAttributeChecker.ATTIDX_NAMESPACE];
                  if (var12 != null) {
                     var12 = this.fSymbolTable.addSymbol(var12);
                  }

                  if (var12 == var6.fTargetNamespace) {
                     this.reportSchemaError("src-import.1.1", new Object[]{var12}, var11);
                  }

                  var17 = DOMUtil.getFirstChildElement(var11);
                  if (var17 != null) {
                     var18 = DOMUtil.getLocalName(var17);
                     if (var18.equals(SchemaSymbols.ELT_ANNOTATION)) {
                        var21.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(var17, var16, true, var6));
                     } else {
                        this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var14, "annotation?", var18}, var11);
                     }

                     if (DOMUtil.getNextSiblingElement(var17) != null) {
                        this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var14, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(var17))}, var11);
                     }
                  } else {
                     var18 = DOMUtil.getSyntheticAnnotation(var11);
                     if (var18 != null) {
                        var21.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(var11, var18, var16, true, var6));
                     }
                  }

                  this.fAttributeChecker.returnAttrArray(var16, var6);
                  if (var6.isAllowedNS(var12)) {
                     if (!this.fHonourAllSchemaLocations) {
                        continue;
                     }
                  } else {
                     var6.addAllowedNS(var12);
                  }

                  var18 = this.null2EmptyString(var6.fTargetNamespace);
                  Vector var19 = (Vector)this.fImportMap.get(var18);
                  if (var19 == null) {
                     this.fAllTNSs.addElement(var18);
                     var19 = new Vector();
                     this.fImportMap.put(var18, var19);
                     var19.addElement(var12);
                  } else if (!var19.contains(var12)) {
                     var19.addElement(var12);
                  }

                  this.fSchemaGrammarDescription.reset();
                  this.fSchemaGrammarDescription.setContextType((short)2);
                  this.fSchemaGrammarDescription.setBaseSystemId(this.doc2SystemId(var1));
                  this.fSchemaGrammarDescription.setLocationHints(new String[]{var13});
                  this.fSchemaGrammarDescription.setTargetNamespace(var12);
                  if (!this.fHonourAllSchemaLocations && this.findGrammar(this.fSchemaGrammarDescription) != null || this.isExistingGrammar(this.fSchemaGrammarDescription)) {
                     continue;
                  }

                  var10 = this.resolveSchema(this.fSchemaGrammarDescription, false, var11);
               } else {
                  if (!var14.equals(SchemaSymbols.ELT_INCLUDE) && !var14.equals(SchemaSymbols.ELT_REDEFINE)) {
                     break;
                  }

                  var16 = this.fAttributeChecker.checkAttributes(var11, true, var6);
                  var13 = (String)var16[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
                  if (var14.equals(SchemaSymbols.ELT_REDEFINE)) {
                     this.fRedefine2NSSupport.put(var11, new SchemaNamespaceSupport(var6.fNamespaceSupport));
                  }

                  if (var14.equals(SchemaSymbols.ELT_INCLUDE)) {
                     var17 = DOMUtil.getFirstChildElement(var11);
                     if (var17 != null) {
                        var18 = DOMUtil.getLocalName(var17);
                        if (var18.equals(SchemaSymbols.ELT_ANNOTATION)) {
                           var21.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(var17, var16, true, var6));
                        } else {
                           this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var14, "annotation?", var18}, var11);
                        }

                        if (DOMUtil.getNextSiblingElement(var17) != null) {
                           this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var14, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(var17))}, var11);
                        }
                     } else {
                        var18 = DOMUtil.getSyntheticAnnotation(var11);
                        if (var18 != null) {
                           var21.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(var11, var18, var16, true, var6));
                        }
                     }
                  } else {
                     for(var17 = DOMUtil.getFirstChildElement(var11); var17 != null; var17 = DOMUtil.getNextSiblingElement(var17)) {
                        var18 = DOMUtil.getLocalName(var17);
                        if (var18.equals(SchemaSymbols.ELT_ANNOTATION)) {
                           var21.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(var17, var16, true, var6));
                           DOMUtil.setHidden(var17, this.fHiddenNodes);
                        } else {
                           String var25 = DOMUtil.getSyntheticAnnotation(var11);
                           if (var25 != null) {
                              var21.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(var11, var25, var16, true, var6));
                           }
                        }
                     }
                  }

                  this.fAttributeChecker.returnAttrArray(var16, var6);
                  if (var13 == null) {
                     this.reportSchemaError("s4s-att-must-appear", new Object[]{"<include> or <redefine>", "schemaLocation"}, var11);
                  }

                  boolean var24 = false;
                  byte var22 = 0;
                  if (var14.equals(SchemaSymbols.ELT_REDEFINE)) {
                     var24 = this.nonAnnotationContent(var11);
                     var22 = 1;
                  }

                  this.fSchemaGrammarDescription.reset();
                  this.fSchemaGrammarDescription.setContextType(var22);
                  this.fSchemaGrammarDescription.setBaseSystemId(this.doc2SystemId(var1));
                  this.fSchemaGrammarDescription.setLocationHints(new String[]{var13});
                  this.fSchemaGrammarDescription.setTargetNamespace(var4);
                  var10 = this.resolveSchema(this.fSchemaGrammarDescription, var24, var11);
                  var12 = var6.fTargetNamespace;
               }

               var16 = null;
               XSDocumentInfo var23;
               if (this.fLastSchemaWasDuplicate) {
                  var23 = var10 == null ? null : (XSDocumentInfo)this.fDoc2XSDocumentMap.get(var10);
               } else {
                  var23 = this.constructTrees(var10, var13, this.fSchemaGrammarDescription);
               }

               if (var14.equals(SchemaSymbols.ELT_REDEFINE) && var23 != null) {
                  this.fRedefine2XSDMap.put(var11, var23);
               }

               if (var10 != null) {
                  if (var23 != null) {
                     var8.addElement(var23);
                  }

                  var10 = null;
               }
            }
         }

         this.fDependencyMap.put(var6, var8);
         return var6;
      }
   }

   private boolean isExistingGrammar(XSDDescription var1) {
      SchemaGrammar var2 = this.fGrammarBucket.getGrammar(var1.getTargetNamespace());
      if (var2 == null) {
         return this.findGrammar(var1) != null;
      } else {
         try {
            return var2.getDocumentLocations().contains(XMLEntityManager.expandSystemId(var1.getLiteralSystemId(), var1.getBaseSystemId(), false));
         } catch (URI.MalformedURIException var4) {
            return false;
         }
      }
   }

   protected void buildGlobalNameRegistries() {
      Stack var1 = new Stack();
      var1.push(this.fRoot);

      while(true) {
         XSDocumentInfo var2;
         Element var3;
         do {
            if (var1.empty()) {
               return;
            }

            var2 = (XSDocumentInfo)var1.pop();
            var3 = var2.fSchemaElement;
         } while(DOMUtil.isHidden(var3, this.fHiddenNodes));

         boolean var5 = true;

         for(Element var6 = DOMUtil.getFirstChildElement(var3); var6 != null; var6 = DOMUtil.getNextSiblingElement(var6)) {
            if (!DOMUtil.getLocalName(var6).equals(SchemaSymbols.ELT_ANNOTATION)) {
               if (!DOMUtil.getLocalName(var6).equals(SchemaSymbols.ELT_INCLUDE) && !DOMUtil.getLocalName(var6).equals(SchemaSymbols.ELT_IMPORT)) {
                  String var8;
                  String var9;
                  if (!DOMUtil.getLocalName(var6).equals(SchemaSymbols.ELT_REDEFINE)) {
                     var5 = false;
                     String var12 = DOMUtil.getAttrValue(var6, SchemaSymbols.ATT_NAME);
                     if (var12.length() != 0) {
                        var8 = var2.fTargetNamespace == null ? "," + var12 : var2.fTargetNamespace + "," + var12;
                        var9 = DOMUtil.getLocalName(var6);
                        if (var9.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                           this.checkForDuplicateNames(var8, this.fUnparsedAttributeRegistry, this.fUnparsedAttributeRegistrySub, var6, var2);
                        } else if (var9.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                           this.checkForDuplicateNames(var8, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, var6, var2);
                        } else if (!var9.equals(SchemaSymbols.ELT_COMPLEXTYPE) && !var9.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                           if (var9.equals(SchemaSymbols.ELT_ELEMENT)) {
                              this.checkForDuplicateNames(var8, this.fUnparsedElementRegistry, this.fUnparsedElementRegistrySub, var6, var2);
                           } else if (var9.equals(SchemaSymbols.ELT_GROUP)) {
                              this.checkForDuplicateNames(var8, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, var6, var2);
                           } else if (var9.equals(SchemaSymbols.ELT_NOTATION)) {
                              this.checkForDuplicateNames(var8, this.fUnparsedNotationRegistry, this.fUnparsedNotationRegistrySub, var6, var2);
                           }
                        } else {
                           this.checkForDuplicateNames(var8, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, var6, var2);
                        }
                     }
                  } else {
                     if (!var5) {
                        this.reportSchemaError("s4s-elt-invalid-content.3", new Object[]{DOMUtil.getLocalName(var6)}, var6);
                     }

                     for(Element var7 = DOMUtil.getFirstChildElement(var6); var7 != null; var7 = DOMUtil.getNextSiblingElement(var7)) {
                        var8 = DOMUtil.getAttrValue(var7, SchemaSymbols.ATT_NAME);
                        if (var8.length() != 0) {
                           var9 = var2.fTargetNamespace == null ? "," + var8 : var2.fTargetNamespace + "," + var8;
                           String var10 = DOMUtil.getLocalName(var7);
                           String var11;
                           if (var10.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                              this.checkForDuplicateNames(var9, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, var7, var2);
                              var11 = DOMUtil.getAttrValue(var7, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                              this.renameRedefiningComponents(var2, var7, SchemaSymbols.ELT_ATTRIBUTEGROUP, var8, var11);
                           } else if (!var10.equals(SchemaSymbols.ELT_COMPLEXTYPE) && !var10.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                              if (var10.equals(SchemaSymbols.ELT_GROUP)) {
                                 this.checkForDuplicateNames(var9, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, var7, var2);
                                 var11 = DOMUtil.getAttrValue(var7, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                                 this.renameRedefiningComponents(var2, var7, SchemaSymbols.ELT_GROUP, var8, var11);
                              }
                           } else {
                              this.checkForDuplicateNames(var9, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, var7, var2);
                              var11 = DOMUtil.getAttrValue(var7, SchemaSymbols.ATT_NAME) + "_fn3dktizrknc9pi";
                              if (var10.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                                 this.renameRedefiningComponents(var2, var7, SchemaSymbols.ELT_COMPLEXTYPE, var8, var11);
                              } else {
                                 this.renameRedefiningComponents(var2, var7, SchemaSymbols.ELT_SIMPLETYPE, var8, var11);
                              }
                           }
                        }
                     }
                  }
               } else {
                  if (!var5) {
                     this.reportSchemaError("s4s-elt-invalid-content.3", new Object[]{DOMUtil.getLocalName(var6)}, var6);
                  }

                  DOMUtil.setHidden(var6, this.fHiddenNodes);
               }
            }
         }

         DOMUtil.setHidden(var3, this.fHiddenNodes);
         Vector var13 = (Vector)this.fDependencyMap.get(var2);

         for(int var14 = 0; var14 < var13.size(); ++var14) {
            var1.push(var13.elementAt(var14));
         }
      }
   }

   protected void traverseSchemas(ArrayList var1) {
      this.setSchemasVisible(this.fRoot);
      Stack var2 = new Stack();
      var2.push(this.fRoot);

      while(true) {
         XSDocumentInfo var3;
         Element var4;
         SchemaGrammar var5;
         do {
            if (var2.empty()) {
               return;
            }

            var3 = (XSDocumentInfo)var2.pop();
            var4 = var3.fSchemaElement;
            var5 = this.fGrammarBucket.getGrammar(var3.fTargetNamespace);
         } while(DOMUtil.isHidden(var4, this.fHiddenNodes));

         boolean var7 = false;

         String var9;
         for(Element var8 = DOMUtil.getFirstVisibleChildElement(var4, this.fHiddenNodes); var8 != null; var8 = DOMUtil.getNextVisibleSiblingElement(var8, this.fHiddenNodes)) {
            DOMUtil.setHidden(var8, this.fHiddenNodes);
            var9 = DOMUtil.getLocalName(var8);
            if (!DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_REDEFINE)) {
               if (var9.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                  this.fAttributeTraverser.traverseGlobal(var8, var3, var5);
               } else if (var9.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                  this.fAttributeGroupTraverser.traverseGlobal(var8, var3, var5);
               } else if (var9.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                  this.fComplexTypeTraverser.traverseGlobal(var8, var3, var5);
               } else if (var9.equals(SchemaSymbols.ELT_ELEMENT)) {
                  this.fElementTraverser.traverseGlobal(var8, var3, var5);
               } else if (var9.equals(SchemaSymbols.ELT_GROUP)) {
                  this.fGroupTraverser.traverseGlobal(var8, var3, var5);
               } else if (var9.equals(SchemaSymbols.ELT_NOTATION)) {
                  this.fNotationTraverser.traverse(var8, var3, var5);
               } else if (var9.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                  this.fSimpleTypeTraverser.traverseGlobal(var8, var3, var5);
               } else if (var9.equals(SchemaSymbols.ELT_ANNOTATION)) {
                  var5.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(var8, var3.getSchemaAttrs(), true, var3));
                  var7 = true;
               } else {
                  this.reportSchemaError("s4s-elt-invalid-content.1", new Object[]{SchemaSymbols.ELT_SCHEMA, DOMUtil.getLocalName(var8)}, var8);
               }
            } else {
               var3.backupNSSupport((SchemaNamespaceSupport)this.fRedefine2NSSupport.get(var8));

               for(Element var10 = DOMUtil.getFirstVisibleChildElement(var8, this.fHiddenNodes); var10 != null; var10 = DOMUtil.getNextVisibleSiblingElement(var10, this.fHiddenNodes)) {
                  String var11 = DOMUtil.getLocalName(var10);
                  DOMUtil.setHidden(var10, this.fHiddenNodes);
                  if (var11.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                     this.fAttributeGroupTraverser.traverseGlobal(var10, var3, var5);
                  } else if (var11.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                     this.fComplexTypeTraverser.traverseGlobal(var10, var3, var5);
                  } else if (var11.equals(SchemaSymbols.ELT_GROUP)) {
                     this.fGroupTraverser.traverseGlobal(var10, var3, var5);
                  } else if (var11.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                     this.fSimpleTypeTraverser.traverseGlobal(var10, var3, var5);
                  } else {
                     this.reportSchemaError("s4s-elt-must-match.1", new Object[]{DOMUtil.getLocalName(var8), "(annotation | (simpleType | complexType | group | attributeGroup))*", var11}, var10);
                  }
               }

               var3.restoreNSSupport();
            }
         }

         if (!var7) {
            var9 = DOMUtil.getSyntheticAnnotation(var4);
            if (var9 != null) {
               var5.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(var4, var9, var3.getSchemaAttrs(), true, var3));
            }
         }

         if (var1 != null) {
            XSAnnotationInfo var12 = var3.getAnnotations();
            if (var12 != null) {
               var1.add(this.doc2SystemId(var4));
               var1.add(var12);
            }
         }

         var3.returnSchemaAttrs();
         DOMUtil.setHidden(var4, this.fHiddenNodes);
         Vector var13 = (Vector)this.fDependencyMap.get(var3);

         for(int var14 = 0; var14 < var13.size(); ++var14) {
            var2.push(var13.elementAt(var14));
         }
      }
   }

   private final boolean needReportTNSError(String var1) {
      if (this.fReportedTNS == null) {
         this.fReportedTNS = new Vector();
      } else if (this.fReportedTNS.contains(var1)) {
         return false;
      }

      this.fReportedTNS.addElement(var1);
      return true;
   }

   protected Object getGlobalDecl(XSDocumentInfo var1, int var2, QName var3, Element var4) {
      if (var3.uri != null && var3.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && var2 == 7) {
         XSTypeDefinition var5 = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(var3.localpart);
         if (var5 != null) {
            return var5;
         }
      }

      if (!var1.isAllowedNS(var3.uri)) {
         if (var1.needReportTNSError(var3.uri)) {
            String var14 = var3.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
            this.reportSchemaError(var14, new Object[]{this.fDoc2SystemId.get(var1.fSchemaElement), var3.uri, var3.rawname}, var4);
         }

         return null;
      } else {
         SchemaGrammar var13 = this.fGrammarBucket.getGrammar(var3.uri);
         if (var13 == null) {
            if (this.needReportTNSError(var3.uri)) {
               this.reportSchemaError("src-resolve", new Object[]{var3.rawname, COMP_TYPE[var2]}, var4);
            }

            return null;
         } else {
            Object var6 = null;
            switch (var2) {
               case 1:
                  var6 = var13.getGlobalAttributeDecl(var3.localpart);
                  break;
               case 2:
                  var6 = var13.getGlobalAttributeGroupDecl(var3.localpart);
                  break;
               case 3:
                  var6 = var13.getGlobalElementDecl(var3.localpart);
                  break;
               case 4:
                  var6 = var13.getGlobalGroupDecl(var3.localpart);
                  break;
               case 5:
                  var6 = var13.getIDConstraintDecl(var3.localpart);
                  break;
               case 6:
                  var6 = var13.getGlobalNotationDecl(var3.localpart);
                  break;
               case 7:
                  var6 = var13.getGlobalTypeDecl(var3.localpart);
            }

            if (var6 != null) {
               return var6;
            } else {
               XSDocumentInfo var7 = null;
               Element var8 = null;
               XSDocumentInfo var9 = null;
               String var10 = var3.uri == null ? "," + var3.localpart : var3.uri + "," + var3.localpart;
               switch (var2) {
                  case 1:
                     var8 = (Element)this.fUnparsedAttributeRegistry.get(var10);
                     var9 = (XSDocumentInfo)this.fUnparsedAttributeRegistrySub.get(var10);
                     break;
                  case 2:
                     var8 = (Element)this.fUnparsedAttributeGroupRegistry.get(var10);
                     var9 = (XSDocumentInfo)this.fUnparsedAttributeGroupRegistrySub.get(var10);
                     break;
                  case 3:
                     var8 = (Element)this.fUnparsedElementRegistry.get(var10);
                     var9 = (XSDocumentInfo)this.fUnparsedElementRegistrySub.get(var10);
                     break;
                  case 4:
                     var8 = (Element)this.fUnparsedGroupRegistry.get(var10);
                     var9 = (XSDocumentInfo)this.fUnparsedGroupRegistrySub.get(var10);
                     break;
                  case 5:
                     var8 = (Element)this.fUnparsedIdentityConstraintRegistry.get(var10);
                     var9 = (XSDocumentInfo)this.fUnparsedIdentityConstraintRegistrySub.get(var10);
                     break;
                  case 6:
                     var8 = (Element)this.fUnparsedNotationRegistry.get(var10);
                     var9 = (XSDocumentInfo)this.fUnparsedNotationRegistrySub.get(var10);
                     break;
                  case 7:
                     var8 = (Element)this.fUnparsedTypeRegistry.get(var10);
                     var9 = (XSDocumentInfo)this.fUnparsedTypeRegistrySub.get(var10);
                     break;
                  default:
                     this.reportSchemaError("Internal-Error", new Object[]{"XSDHandler asked to locate component of type " + var2 + "; it does not recognize this type!"}, var4);
               }

               if (var8 == null) {
                  this.reportSchemaError("src-resolve", new Object[]{var3.rawname, COMP_TYPE[var2]}, var4);
                  return null;
               } else {
                  var7 = this.findXSDocumentForDecl(var1, var8, var9);
                  String var15;
                  if (var7 == null) {
                     var15 = var3.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
                     this.reportSchemaError(var15, new Object[]{this.fDoc2SystemId.get(var1.fSchemaElement), var3.uri, var3.rawname}, var4);
                     return null;
                  } else if (DOMUtil.isHidden(var8, this.fHiddenNodes)) {
                     var15 = CIRCULAR_CODES[var2];
                     if (var2 == 7 && SchemaSymbols.ELT_COMPLEXTYPE.equals(DOMUtil.getLocalName(var8))) {
                        var15 = "ct-props-correct.3";
                     }

                     this.reportSchemaError(var15, new Object[]{var3.prefix + ":" + var3.localpart}, var4);
                     return null;
                  } else {
                     DOMUtil.setHidden(var8, this.fHiddenNodes);
                     SchemaNamespaceSupport var11 = null;
                     Element var12 = DOMUtil.getParent(var8);
                     if (DOMUtil.getLocalName(var12).equals(SchemaSymbols.ELT_REDEFINE)) {
                        var11 = (SchemaNamespaceSupport)this.fRedefine2NSSupport.get(var12);
                     }

                     var7.backupNSSupport(var11);
                     switch (var2) {
                        case 1:
                           var6 = this.fAttributeTraverser.traverseGlobal(var8, var7, var13);
                           break;
                        case 2:
                           var6 = this.fAttributeGroupTraverser.traverseGlobal(var8, var7, var13);
                           break;
                        case 3:
                           var6 = this.fElementTraverser.traverseGlobal(var8, var7, var13);
                           break;
                        case 4:
                           var6 = this.fGroupTraverser.traverseGlobal(var8, var7, var13);
                           break;
                        case 5:
                           var6 = null;
                           break;
                        case 6:
                           var6 = this.fNotationTraverser.traverse(var8, var7, var13);
                           break;
                        case 7:
                           if (DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                              var6 = this.fComplexTypeTraverser.traverseGlobal(var8, var7, var13);
                           } else {
                              var6 = this.fSimpleTypeTraverser.traverseGlobal(var8, var7, var13);
                           }
                     }

                     var7.restoreNSSupport();
                     return var6;
                  }
               }
            }
         }
      }
   }

   Object getGrpOrAttrGrpRedefinedByRestriction(int var1, QName var2, XSDocumentInfo var3, Element var4) {
      String var5 = var2.uri != null ? var2.uri + "," + var2.localpart : "," + var2.localpart;
      String var6 = null;
      switch (var1) {
         case 2:
            var6 = (String)this.fRedefinedRestrictedAttributeGroupRegistry.get(var5);
            break;
         case 4:
            var6 = (String)this.fRedefinedRestrictedGroupRegistry.get(var5);
            break;
         default:
            return null;
      }

      if (var6 == null) {
         return null;
      } else {
         int var7 = var6.indexOf(",");
         QName var8 = new QName(XMLSymbols.EMPTY_STRING, var6.substring(var7 + 1), var6.substring(var7), var7 == 0 ? null : var6.substring(0, var7));
         Object var9 = this.getGlobalDecl(var3, var1, var8, var4);
         if (var9 == null) {
            switch (var1) {
               case 2:
                  this.reportSchemaError("src-redefine.7.2.1", new Object[]{var2.localpart}, var4);
                  break;
               case 4:
                  this.reportSchemaError("src-redefine.6.2.1", new Object[]{var2.localpart}, var4);
            }

            return null;
         } else {
            return var9;
         }
      }
   }

   protected void resolveKeyRefs() {
      for(int var1 = 0; var1 < this.fKeyrefStackPos; ++var1) {
         XSDocumentInfo var2 = this.fKeyrefsMapXSDocumentInfo[var1];
         var2.fNamespaceSupport.makeGlobal();
         var2.fNamespaceSupport.setEffectiveContext(this.fKeyrefNamespaceContext[var1]);
         SchemaGrammar var3 = this.fGrammarBucket.getGrammar(var2.fTargetNamespace);
         DOMUtil.setHidden(this.fKeyrefs[var1], this.fHiddenNodes);
         this.fKeyrefTraverser.traverse(this.fKeyrefs[var1], this.fKeyrefElems[var1], var2, var3);
      }

   }

   protected Hashtable getIDRegistry() {
      return this.fUnparsedIdentityConstraintRegistry;
   }

   protected Hashtable getIDRegistry_sub() {
      return this.fUnparsedIdentityConstraintRegistrySub;
   }

   protected void storeKeyRef(Element var1, XSDocumentInfo var2, XSElementDecl var3) {
      String var4 = DOMUtil.getAttrValue(var1, SchemaSymbols.ATT_NAME);
      if (var4.length() != 0) {
         String var5 = var2.fTargetNamespace == null ? "," + var4 : var2.fTargetNamespace + "," + var4;
         this.checkForDuplicateNames(var5, this.fUnparsedIdentityConstraintRegistry, this.fUnparsedIdentityConstraintRegistrySub, var1, var2);
      }

      if (this.fKeyrefStackPos == this.fKeyrefs.length) {
         Element[] var9 = new Element[this.fKeyrefStackPos + 2];
         System.arraycopy(this.fKeyrefs, 0, var9, 0, this.fKeyrefStackPos);
         this.fKeyrefs = var9;
         XSElementDecl[] var6 = new XSElementDecl[this.fKeyrefStackPos + 2];
         System.arraycopy(this.fKeyrefElems, 0, var6, 0, this.fKeyrefStackPos);
         this.fKeyrefElems = var6;
         String[][] var7 = new String[this.fKeyrefStackPos + 2][];
         System.arraycopy(this.fKeyrefNamespaceContext, 0, var7, 0, this.fKeyrefStackPos);
         this.fKeyrefNamespaceContext = var7;
         XSDocumentInfo[] var8 = new XSDocumentInfo[this.fKeyrefStackPos + 2];
         System.arraycopy(this.fKeyrefsMapXSDocumentInfo, 0, var8, 0, this.fKeyrefStackPos);
         this.fKeyrefsMapXSDocumentInfo = var8;
      }

      this.fKeyrefs[this.fKeyrefStackPos] = var1;
      this.fKeyrefElems[this.fKeyrefStackPos] = var3;
      this.fKeyrefNamespaceContext[this.fKeyrefStackPos] = var2.fNamespaceSupport.getEffectiveLocalContext();
      this.fKeyrefsMapXSDocumentInfo[this.fKeyrefStackPos++] = var2;
   }

   private Element resolveSchema(XSDDescription var1, boolean var2, Element var3) {
      XMLInputSource var4 = null;

      try {
         var4 = XMLSchemaLoader.resolveDocument(var1, this.fLocationPairs, this.fEntityResolver);
      } catch (IOException var17) {
         if (var2) {
            this.reportSchemaError("schema_reference.4", new Object[]{var1.getLocationHints()[0]}, var3);
         } else {
            this.reportSchemaWarning("schema_reference.4", new Object[]{var1.getLocationHints()[0]}, var3);
         }
      }

      if (var4 instanceof DOMInputSource) {
         this.fHiddenNodes.clear();
         Node var18 = ((DOMInputSource)var4).getNode();
         if (var18 instanceof Document) {
            return DOMUtil.getRoot((Document)var18);
         } else {
            return var18 instanceof Element ? (Element)var18 : null;
         }
      } else if (var4 instanceof SAXInputSource) {
         Object var5 = ((SAXInputSource)var4).getXMLReader();
         InputSource var6 = ((SAXInputSource)var4).getInputSource();
         boolean var7 = false;
         if (var5 != null) {
            try {
               var7 = ((XMLReader)var5).getFeature("http://xml.org/sax/features/namespace-prefixes");
            } catch (SAXException var16) {
            }
         } else {
            try {
               var5 = XMLReaderFactory.createXMLReader();
            } catch (SAXException var15) {
               var5 = new SAXParser();
            }

            try {
               ((XMLReader)var5).setFeature("http://xml.org/sax/features/namespace-prefixes", true);
               var7 = true;
            } catch (SAXException var14) {
            }
         }

         boolean var8 = false;

         try {
            var8 = ((XMLReader)var5).getFeature("http://xml.org/sax/features/string-interning");
         } catch (SAXException var13) {
         }

         if (this.fXSContentHandler == null) {
            this.fXSContentHandler = new SchemaContentHandler();
         }

         this.fXSContentHandler.reset(this.fSchemaParser, this.fSymbolTable, var7, var8);
         ((XMLReader)var5).setContentHandler(this.fXSContentHandler);
         ((XMLReader)var5).setErrorHandler(this.fErrorReporter.getSAXErrorHandler());

         try {
            ((XMLReader)var5).parse(var6);
         } catch (SAXException var11) {
            return null;
         } catch (IOException var12) {
            return null;
         }

         Document var9 = this.fXSContentHandler.getDocument();
         return var9 == null ? null : DOMUtil.getRoot(var9);
      } else {
         return this.getSchemaDocument(var1.getTargetNamespace(), var4, var2, var1.getContextType(), var3);
      }
   }

   private Element getSchemaDocument(String var1, XMLInputSource var2, boolean var3, short var4, Element var5) {
      boolean var6 = true;
      Element var7 = null;

      try {
         if (var2 != null && (var2.getSystemId() != null || var2.getByteStream() != null || var2.getCharacterStream() != null)) {
            XSDKey var8 = null;
            String var9 = null;
            if (var4 != 3) {
               var9 = XMLEntityManager.expandSystemId(var2.getSystemId(), var2.getBaseSystemId(), false);
               var8 = new XSDKey(var9, var4, var1);
               if ((var7 = (Element)this.fTraversed.get(var8)) != null) {
                  this.fLastSchemaWasDuplicate = true;
                  return var7;
               }
            }

            this.fSchemaParser.parse(var2);
            var7 = this.fSchemaParser.getDocument2() == null ? null : DOMUtil.getRoot(this.fSchemaParser.getDocument2());
            if (var8 != null) {
               this.fTraversed.put(var8, var7);
            }

            if (var9 != null) {
               this.fDoc2SystemId.put(var7, var9);
            }

            this.fLastSchemaWasDuplicate = false;
            return var7;
         }

         var6 = false;
      } catch (IOException var10) {
      }

      if (var3) {
         if (var6) {
            this.reportSchemaError("schema_reference.4", new Object[]{var2.getSystemId()}, var5);
         } else {
            this.reportSchemaError("schema_reference.4", new Object[]{var2 == null ? "" : var2.getSystemId()}, var5);
         }
      } else if (var6) {
         this.reportSchemaWarning("schema_reference.4", new Object[]{var2.getSystemId()}, var5);
      }

      this.fLastSchemaWasDuplicate = false;
      return null;
   }

   private void createTraversers() {
      this.fAttributeChecker = new XSAttributeChecker(this);
      this.fAttributeGroupTraverser = new XSDAttributeGroupTraverser(this, this.fAttributeChecker);
      this.fAttributeTraverser = new XSDAttributeTraverser(this, this.fAttributeChecker);
      this.fComplexTypeTraverser = new XSDComplexTypeTraverser(this, this.fAttributeChecker);
      this.fElementTraverser = new XSDElementTraverser(this, this.fAttributeChecker);
      this.fGroupTraverser = new XSDGroupTraverser(this, this.fAttributeChecker);
      this.fKeyrefTraverser = new XSDKeyrefTraverser(this, this.fAttributeChecker);
      this.fNotationTraverser = new XSDNotationTraverser(this, this.fAttributeChecker);
      this.fSimpleTypeTraverser = new XSDSimpleTypeTraverser(this, this.fAttributeChecker);
      this.fUniqueOrKeyTraverser = new XSDUniqueOrKeyTraverser(this, this.fAttributeChecker);
      this.fWildCardTraverser = new XSDWildcardTraverser(this, this.fAttributeChecker);
   }

   void prepareForParse() {
      this.fTraversed.clear();
      this.fDoc2SystemId.clear();
      this.fHiddenNodes.clear();
      this.fLastSchemaWasDuplicate = false;
   }

   void prepareForTraverse() {
      this.fUnparsedAttributeRegistry.clear();
      this.fUnparsedAttributeGroupRegistry.clear();
      this.fUnparsedElementRegistry.clear();
      this.fUnparsedGroupRegistry.clear();
      this.fUnparsedIdentityConstraintRegistry.clear();
      this.fUnparsedNotationRegistry.clear();
      this.fUnparsedTypeRegistry.clear();
      this.fUnparsedAttributeRegistrySub.clear();
      this.fUnparsedAttributeGroupRegistrySub.clear();
      this.fUnparsedElementRegistrySub.clear();
      this.fUnparsedGroupRegistrySub.clear();
      this.fUnparsedIdentityConstraintRegistrySub.clear();
      this.fUnparsedNotationRegistrySub.clear();
      this.fUnparsedTypeRegistrySub.clear();
      this.fXSDocumentInfoRegistry.clear();
      this.fDependencyMap.clear();
      this.fDoc2XSDocumentMap.clear();
      this.fRedefine2XSDMap.clear();
      this.fRedefine2NSSupport.clear();
      this.fAllTNSs.removeAllElements();
      this.fImportMap.clear();
      this.fRoot = null;

      for(int var1 = 0; var1 < this.fLocalElemStackPos; ++var1) {
         this.fParticle[var1] = null;
         this.fLocalElementDecl[var1] = null;
         this.fLocalElementDecl_schema[var1] = null;
         this.fLocalElemNamespaceContext[var1] = null;
      }

      this.fLocalElemStackPos = 0;

      for(int var2 = 0; var2 < this.fKeyrefStackPos; ++var2) {
         this.fKeyrefs[var2] = null;
         this.fKeyrefElems[var2] = null;
         this.fKeyrefNamespaceContext[var2] = null;
         this.fKeyrefsMapXSDocumentInfo[var2] = null;
      }

      this.fKeyrefStackPos = 0;
      if (this.fAttributeChecker == null) {
         this.createTraversers();
      }

      this.fAttributeChecker.reset(this.fSymbolTable);
      this.fAttributeGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fAttributeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fComplexTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fElementTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fKeyrefTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fNotationTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fSimpleTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fUniqueOrKeyTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fWildCardTraverser.reset(this.fSymbolTable, this.fValidateAnnotations);
      this.fRedefinedRestrictedAttributeGroupRegistry.clear();
      this.fRedefinedRestrictedGroupRegistry.clear();
   }

   public void setDeclPool(XSDeclarationPool var1) {
      this.fDeclPool = var1;
   }

   public void reset(XMLComponentManager var1) {
      this.fSymbolTable = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");
      this.fEntityResolver = (XMLEntityResolver)var1.getProperty("http://apache.org/xml/properties/internal/entity-manager");
      XMLEntityResolver var2 = (XMLEntityResolver)var1.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
      if (var2 != null) {
         this.fSchemaParser.setEntityResolver(var2);
      }

      this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");

      try {
         XMLErrorHandler var3 = this.fErrorReporter.getErrorHandler();
         if (var3 != this.fSchemaParser.getProperty("http://apache.org/xml/properties/internal/error-handler")) {
            this.fSchemaParser.setProperty("http://apache.org/xml/properties/internal/error-handler", var3 != null ? var3 : new DefaultErrorHandler());
            if (this.fAnnotationValidator != null) {
               this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/error-handler", var3 != null ? var3 : new DefaultErrorHandler());
            }
         }
      } catch (XMLConfigurationException var12) {
      }

      try {
         this.fValidateAnnotations = var1.getFeature("http://apache.org/xml/features/validate-annotations");
      } catch (XMLConfigurationException var11) {
         this.fValidateAnnotations = false;
      }

      try {
         this.fHonourAllSchemaLocations = var1.getFeature("http://apache.org/xml/features/honour-all-schemaLocations");
      } catch (XMLConfigurationException var10) {
         this.fHonourAllSchemaLocations = false;
      }

      try {
         this.fSchemaParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", this.fErrorReporter.getFeature("http://apache.org/xml/features/continue-after-fatal-error"));
      } catch (XMLConfigurationException var9) {
      }

      try {
         this.fSchemaParser.setFeature("http://apache.org/xml/features/allow-java-encodings", var1.getFeature("http://apache.org/xml/features/allow-java-encodings"));
      } catch (XMLConfigurationException var8) {
      }

      try {
         this.fSchemaParser.setFeature("http://apache.org/xml/features/standard-uri-conformant", var1.getFeature("http://apache.org/xml/features/standard-uri-conformant"));
      } catch (XMLConfigurationException var7) {
      }

      try {
         this.fGrammarPool = (XMLGrammarPool)var1.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
      } catch (XMLConfigurationException var6) {
         this.fGrammarPool = null;
      }

      try {
         this.fSchemaParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", var1.getFeature("http://apache.org/xml/features/disallow-doctype-decl"));
      } catch (XMLConfigurationException var5) {
      }

      try {
         Object var13 = var1.getProperty("http://apache.org/xml/properties/security-manager");
         if (var13 != null) {
            this.fSchemaParser.setProperty("http://apache.org/xml/properties/security-manager", var13);
         }
      } catch (XMLConfigurationException var4) {
      }

   }

   void traverseLocalElements() {
      this.fElementTraverser.fDeferTraversingLocalElements = false;

      for(int var1 = 0; var1 < this.fLocalElemStackPos; ++var1) {
         Element var2 = this.fLocalElementDecl[var1];
         XSDocumentInfo var3 = this.fLocalElementDecl_schema[var1];
         SchemaGrammar var4 = this.fGrammarBucket.getGrammar(var3.fTargetNamespace);
         this.fElementTraverser.traverseLocal(this.fParticle[var1], var2, var3, var4, this.fAllContext[var1], this.fParent[var1], this.fLocalElemNamespaceContext[var1]);
         if (this.fParticle[var1].fType == 0) {
            XSModelGroupImpl var5 = null;
            if (this.fParent[var1] instanceof XSComplexTypeDecl) {
               XSParticle var6 = ((XSComplexTypeDecl)this.fParent[var1]).getParticle();
               if (var6 != null) {
                  var5 = (XSModelGroupImpl)var6.getTerm();
               }
            } else {
               var5 = ((XSGroupDecl)this.fParent[var1]).fModelGroup;
            }

            if (var5 != null) {
               this.removeParticle(var5, this.fParticle[var1]);
            }
         }
      }

   }

   private boolean removeParticle(XSModelGroupImpl var1, XSParticleDecl var2) {
      for(int var4 = 0; var4 < var1.fParticleCount; ++var4) {
         XSParticleDecl var3 = var1.fParticles[var4];
         if (var3 == var2) {
            for(int var5 = var4; var5 < var1.fParticleCount - 1; ++var5) {
               var1.fParticles[var5] = var1.fParticles[var5 + 1];
            }

            --var1.fParticleCount;
            return true;
         }

         if (var3.fType == 3 && this.removeParticle((XSModelGroupImpl)var3.fValue, var2)) {
            return true;
         }
      }

      return false;
   }

   void fillInLocalElemInfo(Element var1, XSDocumentInfo var2, int var3, XSObject var4, XSParticleDecl var5) {
      if (this.fParticle.length == this.fLocalElemStackPos) {
         XSParticleDecl[] var6 = new XSParticleDecl[this.fLocalElemStackPos + 10];
         System.arraycopy(this.fParticle, 0, var6, 0, this.fLocalElemStackPos);
         this.fParticle = var6;
         Element[] var7 = new Element[this.fLocalElemStackPos + 10];
         System.arraycopy(this.fLocalElementDecl, 0, var7, 0, this.fLocalElemStackPos);
         this.fLocalElementDecl = var7;
         XSDocumentInfo[] var8 = new XSDocumentInfo[this.fLocalElemStackPos + 10];
         System.arraycopy(this.fLocalElementDecl_schema, 0, var8, 0, this.fLocalElemStackPos);
         this.fLocalElementDecl_schema = var8;
         int[] var9 = new int[this.fLocalElemStackPos + 10];
         System.arraycopy(this.fAllContext, 0, var9, 0, this.fLocalElemStackPos);
         this.fAllContext = var9;
         XSObject[] var10 = new XSObject[this.fLocalElemStackPos + 10];
         System.arraycopy(this.fParent, 0, var10, 0, this.fLocalElemStackPos);
         this.fParent = var10;
         String[][] var11 = new String[this.fLocalElemStackPos + 10][];
         System.arraycopy(this.fLocalElemNamespaceContext, 0, var11, 0, this.fLocalElemStackPos);
         this.fLocalElemNamespaceContext = var11;
      }

      this.fParticle[this.fLocalElemStackPos] = var5;
      this.fLocalElementDecl[this.fLocalElemStackPos] = var1;
      this.fLocalElementDecl_schema[this.fLocalElemStackPos] = var2;
      this.fAllContext[this.fLocalElemStackPos] = var3;
      this.fParent[this.fLocalElemStackPos] = var4;
      this.fLocalElemNamespaceContext[this.fLocalElemStackPos++] = var2.fNamespaceSupport.getEffectiveLocalContext();
   }

   void checkForDuplicateNames(String var1, Hashtable var2, Hashtable var3, Element var4, XSDocumentInfo var5) {
      Object var6 = null;
      if ((var6 = var2.get(var1)) == null) {
         var2.put(var1, var4);
         var3.put(var1, var5);
      } else {
         Element var7 = (Element)var6;
         XSDocumentInfo var8 = (XSDocumentInfo)var3.get(var1);
         if (var7 == var4) {
            return;
         }

         Element var9 = null;
         XSDocumentInfo var10 = null;
         boolean var11 = true;
         if (DOMUtil.getLocalName(var9 = DOMUtil.getParent(var7)).equals(SchemaSymbols.ELT_REDEFINE)) {
            var10 = (XSDocumentInfo)this.fRedefine2XSDMap.get(var9);
         } else if (DOMUtil.getLocalName(DOMUtil.getParent(var4)).equals(SchemaSymbols.ELT_REDEFINE)) {
            var10 = var8;
            var11 = false;
         }

         if (var10 != null) {
            if (var8 == var5) {
               this.reportSchemaError("sch-props-correct.2", new Object[]{var1}, var4);
               return;
            }

            String var12 = var1.substring(var1.lastIndexOf(44) + 1) + "_fn3dktizrknc9pi";
            if (var10 == var5) {
               var4.setAttribute(SchemaSymbols.ATT_NAME, var12);
               if (var5.fTargetNamespace == null) {
                  var2.put("," + var12, var4);
                  var3.put("," + var12, var5);
               } else {
                  var2.put(var5.fTargetNamespace + "," + var12, var4);
                  var3.put(var5.fTargetNamespace + "," + var12, var5);
               }

               if (var5.fTargetNamespace == null) {
                  this.checkForDuplicateNames("," + var12, var2, var3, var4, var5);
               } else {
                  this.checkForDuplicateNames(var5.fTargetNamespace + "," + var12, var2, var3, var4, var5);
               }
            } else if (var11) {
               if (var5.fTargetNamespace == null) {
                  this.checkForDuplicateNames("," + var12, var2, var3, var4, var5);
               } else {
                  this.checkForDuplicateNames(var5.fTargetNamespace + "," + var12, var2, var3, var4, var5);
               }
            } else {
               this.reportSchemaError("sch-props-correct.2", new Object[]{var1}, var4);
            }
         } else {
            this.reportSchemaError("sch-props-correct.2", new Object[]{var1}, var4);
         }
      }

   }

   private void renameRedefiningComponents(XSDocumentInfo var1, Element var2, String var3, String var4, String var5) {
      Element var6;
      if (var3.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
         var6 = DOMUtil.getFirstChildElement(var2);
         if (var6 == null) {
            this.reportSchemaError("src-redefine.5.a.a", (Object[])null, var2);
         } else {
            String var7 = DOMUtil.getLocalName(var6);
            if (var7.equals(SchemaSymbols.ELT_ANNOTATION)) {
               var6 = DOMUtil.getNextSiblingElement(var6);
            }

            if (var6 == null) {
               this.reportSchemaError("src-redefine.5.a.a", (Object[])null, var2);
            } else {
               var7 = DOMUtil.getLocalName(var6);
               if (!var7.equals(SchemaSymbols.ELT_RESTRICTION)) {
                  this.reportSchemaError("src-redefine.5.a.b", new Object[]{var7}, var2);
               } else {
                  Object[] var8 = this.fAttributeChecker.checkAttributes(var6, false, var1);
                  QName var9 = (QName)var8[XSAttributeChecker.ATTIDX_BASE];
                  if (var9 != null && var9.uri == var1.fTargetNamespace && var9.localpart.equals(var4)) {
                     if (var9.prefix != null && var9.prefix.length() > 0) {
                        var6.setAttribute(SchemaSymbols.ATT_BASE, var9.prefix + ":" + var5);
                     } else {
                        var6.setAttribute(SchemaSymbols.ATT_BASE, var5);
                     }
                  } else {
                     this.reportSchemaError("src-redefine.5.a.c", new Object[]{var7, (var1.fTargetNamespace == null ? "" : var1.fTargetNamespace) + "," + var4}, var2);
                  }

                  this.fAttributeChecker.returnAttrArray(var8, var1);
               }
            }
         }
      } else if (var3.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
         var6 = DOMUtil.getFirstChildElement(var2);
         if (var6 == null) {
            this.reportSchemaError("src-redefine.5.b.a", (Object[])null, var2);
         } else {
            if (DOMUtil.getLocalName(var6).equals(SchemaSymbols.ELT_ANNOTATION)) {
               var6 = DOMUtil.getNextSiblingElement(var6);
            }

            if (var6 == null) {
               this.reportSchemaError("src-redefine.5.b.a", (Object[])null, var2);
            } else {
               Element var11 = DOMUtil.getFirstChildElement(var6);
               if (var11 == null) {
                  this.reportSchemaError("src-redefine.5.b.b", (Object[])null, var6);
               } else {
                  String var13 = DOMUtil.getLocalName(var11);
                  if (var13.equals(SchemaSymbols.ELT_ANNOTATION)) {
                     var11 = DOMUtil.getNextSiblingElement(var11);
                  }

                  if (var11 == null) {
                     this.reportSchemaError("src-redefine.5.b.b", (Object[])null, var6);
                  } else {
                     var13 = DOMUtil.getLocalName(var11);
                     if (!var13.equals(SchemaSymbols.ELT_RESTRICTION) && !var13.equals(SchemaSymbols.ELT_EXTENSION)) {
                        this.reportSchemaError("src-redefine.5.b.c", new Object[]{var13}, var11);
                     } else {
                        Object[] var15 = this.fAttributeChecker.checkAttributes(var11, false, var1);
                        QName var10 = (QName)var15[XSAttributeChecker.ATTIDX_BASE];
                        if (var10 != null && var10.uri == var1.fTargetNamespace && var10.localpart.equals(var4)) {
                           if (var10.prefix != null && var10.prefix.length() > 0) {
                              var11.setAttribute(SchemaSymbols.ATT_BASE, var10.prefix + ":" + var5);
                           } else {
                              var11.setAttribute(SchemaSymbols.ATT_BASE, var5);
                           }
                        } else {
                           this.reportSchemaError("src-redefine.5.b.d", new Object[]{var13, (var1.fTargetNamespace == null ? "" : var1.fTargetNamespace) + "," + var4}, var11);
                        }
                     }
                  }
               }
            }
         }
      } else {
         String var12;
         int var14;
         if (var3.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
            var12 = var1.fTargetNamespace == null ? "," + var4 : var1.fTargetNamespace + "," + var4;
            var14 = this.changeRedefineGroup(var12, var3, var5, var2, var1);
            if (var14 > 1) {
               this.reportSchemaError("src-redefine.7.1", new Object[]{new Integer(var14)}, var2);
            } else if (var14 != 1) {
               if (var1.fTargetNamespace == null) {
                  this.fRedefinedRestrictedAttributeGroupRegistry.put(var12, "," + var5);
               } else {
                  this.fRedefinedRestrictedAttributeGroupRegistry.put(var12, var1.fTargetNamespace + "," + var5);
               }
            }
         } else if (var3.equals(SchemaSymbols.ELT_GROUP)) {
            var12 = var1.fTargetNamespace == null ? "," + var4 : var1.fTargetNamespace + "," + var4;
            var14 = this.changeRedefineGroup(var12, var3, var5, var2, var1);
            if (var14 > 1) {
               this.reportSchemaError("src-redefine.6.1.1", new Object[]{new Integer(var14)}, var2);
            } else if (var14 != 1) {
               if (var1.fTargetNamespace == null) {
                  this.fRedefinedRestrictedGroupRegistry.put(var12, "," + var5);
               } else {
                  this.fRedefinedRestrictedGroupRegistry.put(var12, var1.fTargetNamespace + "," + var5);
               }
            }
         } else {
            this.reportSchemaError("Internal-Error", new Object[]{"could not handle this particular <redefine>; please submit your schemas and instance document in a bug report!"}, var2);
         }
      }

   }

   private String findQName(String var1, XSDocumentInfo var2) {
      SchemaNamespaceSupport var3 = var2.fNamespaceSupport;
      int var4 = var1.indexOf(58);
      String var5 = XMLSymbols.EMPTY_STRING;
      if (var4 > 0) {
         var5 = var1.substring(0, var4);
      }

      String var6 = var3.getURI(this.fSymbolTable.addSymbol(var5));
      String var7 = var4 == 0 ? var1 : var1.substring(var4 + 1);
      if (var5 == XMLSymbols.EMPTY_STRING && var6 == null && var2.fIsChameleonSchema) {
         var6 = var2.fTargetNamespace;
      }

      return var6 == null ? "," + var7 : var6 + "," + var7;
   }

   private int changeRedefineGroup(String var1, String var2, String var3, Element var4, XSDocumentInfo var5) {
      int var6 = 0;

      for(Element var7 = DOMUtil.getFirstChildElement(var4); var7 != null; var7 = DOMUtil.getNextSiblingElement(var7)) {
         String var8 = DOMUtil.getLocalName(var7);
         if (!var8.equals(var2)) {
            var6 += this.changeRedefineGroup(var1, var2, var3, var7, var5);
         } else {
            String var9 = var7.getAttribute(SchemaSymbols.ATT_REF);
            if (var9.length() != 0) {
               String var10 = this.findQName(var9, var5);
               if (var1.equals(var10)) {
                  String var11 = XMLSymbols.EMPTY_STRING;
                  int var12 = var9.indexOf(":");
                  if (var12 > 0) {
                     var11 = var9.substring(0, var12);
                     var7.setAttribute(SchemaSymbols.ATT_REF, var11 + ":" + var3);
                  } else {
                     var7.setAttribute(SchemaSymbols.ATT_REF, var3);
                  }

                  ++var6;
                  if (var2.equals(SchemaSymbols.ELT_GROUP)) {
                     String var13 = var7.getAttribute(SchemaSymbols.ATT_MINOCCURS);
                     String var14 = var7.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
                     if (var14.length() != 0 && !var14.equals("1") || var13.length() != 0 && !var13.equals("1")) {
                        this.reportSchemaError("src-redefine.6.1.2", new Object[]{var9}, var7);
                     }
                  }
               }
            }
         }
      }

      return var6;
   }

   private XSDocumentInfo findXSDocumentForDecl(XSDocumentInfo var1, Element var2, XSDocumentInfo var3) {
      if (var3 == null) {
         return null;
      } else {
         XSDocumentInfo var5 = (XSDocumentInfo)var3;
         return var5;
      }
   }

   private boolean nonAnnotationContent(Element var1) {
      for(Element var2 = DOMUtil.getFirstChildElement(var1); var2 != null; var2 = DOMUtil.getNextSiblingElement(var2)) {
         if (!DOMUtil.getLocalName(var2).equals(SchemaSymbols.ELT_ANNOTATION)) {
            return true;
         }
      }

      return false;
   }

   private void setSchemasVisible(XSDocumentInfo var1) {
      if (DOMUtil.isHidden(var1.fSchemaElement, this.fHiddenNodes)) {
         DOMUtil.setVisible(var1.fSchemaElement, this.fHiddenNodes);
         Vector var2 = (Vector)this.fDependencyMap.get(var1);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            this.setSchemasVisible((XSDocumentInfo)var2.elementAt(var3));
         }
      }

   }

   public SimpleLocator element2Locator(Element var1) {
      if (!(var1 instanceof ElementImpl)) {
         return null;
      } else {
         SimpleLocator var2 = new SimpleLocator();
         return this.element2Locator(var1, var2) ? var2 : null;
      }
   }

   public boolean element2Locator(Element var1, SimpleLocator var2) {
      if (var2 == null) {
         return false;
      } else if (var1 instanceof ElementImpl) {
         ElementImpl var3 = (ElementImpl)var1;
         Document var4 = var3.getOwnerDocument();
         String var5 = (String)this.fDoc2SystemId.get(DOMUtil.getRoot(var4));
         int var6 = var3.getLineNumber();
         int var7 = var3.getColumnNumber();
         var2.setValues(var5, var5, var6, var7, var3.getCharacterOffset());
         return true;
      } else {
         return false;
      }
   }

   void reportSchemaError(String var1, Object[] var2, Element var3) {
      if (this.element2Locator(var3, this.xl)) {
         this.fErrorReporter.reportError(this.xl, "http://www.w3.org/TR/xml-schema-1", var1, var2, (short)1);
      } else {
         this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", var1, var2, (short)1);
      }

   }

   void reportSchemaWarning(String var1, Object[] var2, Element var3) {
      if (this.element2Locator(var3, this.xl)) {
         this.fErrorReporter.reportError(this.xl, "http://www.w3.org/TR/xml-schema-1", var1, var2, (short)0);
      } else {
         this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", var1, var2, (short)0);
      }

   }

   public void setGenerateSyntheticAnnotations(boolean var1) {
      this.fSchemaParser.setFeature("http://apache.org/xml/features/generate-synthetic-annotations", var1);
   }

   private static class XSDKey {
      String systemId;
      short referType;
      String referNS;

      XSDKey(String var1, short var2, String var3) {
         this.systemId = var1;
         this.referType = var2;
         this.referNS = var3;
      }

      public int hashCode() {
         return this.referNS == null ? 0 : this.referNS.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof XSDKey)) {
            return false;
         } else {
            XSDKey var2 = (XSDKey)var1;
            if ((this.referType == 1 || var2.referType == 1) && this.referType != var2.referType) {
               return false;
            } else if (this.referNS != var2.referNS) {
               return false;
            } else {
               return this.systemId != null && this.systemId.equals(var2.systemId);
            }
         }
      }
   }

   private static class XSAnnotationGrammarPool implements XMLGrammarPool {
      private XSGrammarBucket fGrammarBucket;
      private Grammar[] fInitialGrammarSet;

      private XSAnnotationGrammarPool() {
      }

      public Grammar[] retrieveInitialGrammarSet(String var1) {
         if (var1 != "http://www.w3.org/2001/XMLSchema") {
            return new Grammar[0];
         } else {
            if (this.fInitialGrammarSet == null) {
               if (this.fGrammarBucket == null) {
                  this.fInitialGrammarSet = new Grammar[]{SchemaGrammar.SG_Schema4Annotations};
               } else {
                  SchemaGrammar[] var2 = this.fGrammarBucket.getGrammars();

                  for(int var3 = 0; var3 < var2.length; ++var3) {
                     if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(var2[var3].getTargetNamespace())) {
                        this.fInitialGrammarSet = var2;
                        return this.fInitialGrammarSet;
                     }
                  }

                  Grammar[] var4 = new Grammar[var2.length + 1];
                  System.arraycopy(var2, 0, var4, 0, var2.length);
                  var4[var4.length - 1] = SchemaGrammar.SG_Schema4Annotations;
                  this.fInitialGrammarSet = var4;
               }
            }

            return this.fInitialGrammarSet;
         }
      }

      public void cacheGrammars(String var1, Grammar[] var2) {
      }

      public Grammar retrieveGrammar(XMLGrammarDescription var1) {
         if (var1.getGrammarType() == "http://www.w3.org/2001/XMLSchema") {
            String var2 = ((XMLSchemaDescription)var1).getTargetNamespace();
            if (this.fGrammarBucket != null) {
               SchemaGrammar var3 = this.fGrammarBucket.getGrammar(var2);
               if (var3 != null) {
                  return var3;
               }
            }

            if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(var2)) {
               return SchemaGrammar.SG_Schema4Annotations;
            }
         }

         return null;
      }

      public void refreshGrammars(XSGrammarBucket var1) {
         this.fGrammarBucket = var1;
         this.fInitialGrammarSet = null;
      }

      public void lockPool() {
      }

      public void unlockPool() {
      }

      public void clear() {
      }

      // $FF: synthetic method
      XSAnnotationGrammarPool(Object var1) {
         this();
      }
   }
}
