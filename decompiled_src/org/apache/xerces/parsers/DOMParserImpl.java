package org.apache.xerces.parsers;

import java.io.StringReader;
import java.util.Locale;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DOMStringListImpl;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDSource;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.ls.LSResourceResolver;

public class DOMParserImpl extends AbstractDOMParser implements LSParser, DOMConfiguration {
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
   protected static final String XMLSCHEMA = "http://apache.org/xml/features/validation/schema";
   protected static final String XMLSCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
   protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String PSVI_AUGMENT = "http://apache.org/xml/features/validation/schema/augment-psvi";
   protected boolean fNamespaceDeclarations;
   protected String fSchemaType;
   protected boolean fBusy;
   private boolean abortNow;
   private Thread currentThread;
   protected static final boolean DEBUG = false;
   private Vector fSchemaLocations;
   private String fSchemaLocation;
   private DOMStringList fRecognizedParameters;
   private AbortHandler abortHandler;

   public DOMParserImpl(String var1, String var2) {
      this((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", var1));
      if (var2 != null) {
         if (var2.equals(Constants.NS_DTD)) {
            super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
            this.fSchemaType = Constants.NS_DTD;
         } else if (var2.equals(Constants.NS_XMLSCHEMA)) {
            super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
         }
      }

   }

   public DOMParserImpl(XMLParserConfiguration var1) {
      super(var1);
      this.fNamespaceDeclarations = true;
      this.fSchemaType = null;
      this.fBusy = false;
      this.abortNow = false;
      this.fSchemaLocations = new Vector();
      this.fSchemaLocation = null;
      this.abortHandler = new AbortHandler();
      String[] var2 = new String[]{"canonical-form", "cdata-sections", "charset-overrides-xml-encoding", "infoset", "namespace-declarations", "split-cdata-sections", "supported-media-types-only", "certified", "well-formed", "ignore-unknown-character-denormalizations"};
      super.fConfiguration.addRecognizedFeatures(var2);
      super.fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
      super.fConfiguration.setFeature("namespace-declarations", true);
      super.fConfiguration.setFeature("well-formed", true);
      super.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
      super.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
      super.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
      super.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
      super.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
      super.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
      super.fConfiguration.setFeature("canonical-form", false);
      super.fConfiguration.setFeature("charset-overrides-xml-encoding", true);
      super.fConfiguration.setFeature("split-cdata-sections", true);
      super.fConfiguration.setFeature("supported-media-types-only", false);
      super.fConfiguration.setFeature("ignore-unknown-character-denormalizations", true);
      super.fConfiguration.setFeature("certified", true);

      try {
         super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
      } catch (XMLConfigurationException var4) {
      }

   }

   public DOMParserImpl(SymbolTable var1) {
      this((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
      super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", var1);
   }

   public DOMParserImpl(SymbolTable var1, XMLGrammarPool var2) {
      this((XMLParserConfiguration)ObjectFactory.createObject("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration"));
      super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", var1);
      super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", var2);
   }

   public void reset() {
      super.reset();
      this.fNamespaceDeclarations = super.fConfiguration.getFeature("namespace-declarations");
      if (super.fSkippedElemStack != null) {
         super.fSkippedElemStack.removeAllElements();
      }

      this.fSchemaLocations.clear();
      super.fRejectedElement.clear();
      super.fFilterReject = false;
      this.fSchemaType = null;
   }

   public DOMConfiguration getDomConfig() {
      return this;
   }

   public LSParserFilter getFilter() {
      return super.fDOMFilter;
   }

   public void setFilter(LSParserFilter var1) {
      super.fDOMFilter = var1;
      if (super.fSkippedElemStack == null) {
         super.fSkippedElemStack = new Stack();
      }

   }

   public void setParameter(String var1, Object var2) throws DOMException {
      if (var2 instanceof Boolean) {
         boolean var3 = (Boolean)var2;

         try {
            if (var1.equalsIgnoreCase("comments")) {
               super.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", var3);
            } else if (var1.equalsIgnoreCase("datatype-normalization")) {
               super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", var3);
            } else if (var1.equalsIgnoreCase("entities")) {
               super.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", var3);
            } else if (var1.equalsIgnoreCase("disallow-doctype")) {
               super.fConfiguration.setFeature("http://apache.org/xml/features/disallow-doctype-decl", var3);
            } else {
               String var4;
               if (!var1.equalsIgnoreCase("supported-media-types-only") && !var1.equalsIgnoreCase("normalize-characters") && !var1.equalsIgnoreCase("check-character-normalization") && !var1.equalsIgnoreCase("canonical-form")) {
                  if (var1.equalsIgnoreCase("namespaces")) {
                     super.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", var3);
                  } else if (var1.equalsIgnoreCase("infoset")) {
                     if (var3) {
                        super.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
                        super.fConfiguration.setFeature("namespace-declarations", true);
                        super.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
                        super.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
                        super.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
                        super.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
                        super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
                        super.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
                     }
                  } else if (var1.equalsIgnoreCase("cdata-sections")) {
                     super.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", var3);
                  } else if (var1.equalsIgnoreCase("namespace-declarations")) {
                     super.fConfiguration.setFeature("namespace-declarations", var3);
                  } else if (!var1.equalsIgnoreCase("well-formed") && !var1.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
                     if (var1.equalsIgnoreCase("validate")) {
                        super.fConfiguration.setFeature("http://xml.org/sax/features/validation", var3);
                        if (this.fSchemaType != Constants.NS_DTD) {
                           super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", var3);
                           super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", var3);
                        }

                        if (var3) {
                           super.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", false);
                        }
                     } else if (var1.equalsIgnoreCase("validate-if-schema")) {
                        super.fConfiguration.setFeature("http://apache.org/xml/features/validation/dynamic", var3);
                        if (var3) {
                           super.fConfiguration.setFeature("http://xml.org/sax/features/validation", false);
                        }
                     } else if (var1.equalsIgnoreCase("element-content-whitespace")) {
                        super.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", var3);
                     } else if (var1.equalsIgnoreCase("psvi")) {
                        super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
                        super.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "org.apache.xerces.dom.PSVIDocumentImpl");
                     } else {
                        super.fConfiguration.setFeature(var1.toLowerCase(Locale.ENGLISH), var3);
                     }
                  } else if (!var3) {
                     var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
                     throw new DOMException((short)9, var4);
                  }
               } else if (var3) {
                  var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
                  throw new DOMException((short)9, var4);
               }
            }
         } catch (XMLConfigurationException var9) {
            String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
            throw new DOMException((short)8, var5);
         }
      } else {
         String var11;
         if (var1.equalsIgnoreCase("error-handler")) {
            if (!(var2 instanceof DOMErrorHandler) && var2 != null) {
               var11 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var11);
            }

            try {
               super.fErrorHandler = new DOMErrorHandlerWrapper((DOMErrorHandler)var2);
               super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", super.fErrorHandler);
            } catch (XMLConfigurationException var8) {
            }
         } else if (var1.equalsIgnoreCase("resource-resolver")) {
            if (!(var2 instanceof LSResourceResolver) && var2 != null) {
               var11 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var11);
            }

            try {
               super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper((LSResourceResolver)var2));
            } catch (XMLConfigurationException var7) {
            }
         } else if (var1.equalsIgnoreCase("schema-location")) {
            if (!(var2 instanceof String) && var2 != null) {
               var11 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var11);
            }

            try {
               if (var2 == null) {
                  this.fSchemaLocation = null;
                  super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", (Object)null);
               } else {
                  this.fSchemaLocation = (String)var2;
                  StringTokenizer var12 = new StringTokenizer(this.fSchemaLocation, " \n\t\r");
                  if (var12.hasMoreTokens()) {
                     this.fSchemaLocations.clear();
                     this.fSchemaLocations.add(var12.nextToken());

                     while(var12.hasMoreTokens()) {
                        this.fSchemaLocations.add(var12.nextToken());
                     }

                     super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", this.fSchemaLocations.toArray());
                  } else {
                     super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", var2);
                  }
               }
            } catch (XMLConfigurationException var10) {
            }
         } else if (var1.equalsIgnoreCase("schema-type")) {
            if (!(var2 instanceof String) && var2 != null) {
               var11 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
               throw new DOMException((short)17, var11);
            }

            try {
               if (var2 == null) {
                  super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", false);
                  super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
                  super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", (Object)null);
                  this.fSchemaType = null;
               } else if (var2.equals(Constants.NS_XMLSCHEMA)) {
                  super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", true);
                  super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
                  super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
                  this.fSchemaType = Constants.NS_XMLSCHEMA;
               } else if (var2.equals(Constants.NS_DTD)) {
                  super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", false);
                  super.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
                  super.fConfiguration.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
                  this.fSchemaType = Constants.NS_DTD;
               }
            } catch (XMLConfigurationException var6) {
            }
         } else {
            if (!var1.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
               var11 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
               throw new DOMException((short)8, var11);
            }

            super.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", var2);
         }
      }

   }

   public Object getParameter(String var1) throws DOMException {
      if (var1.equalsIgnoreCase("comments")) {
         return super.fConfiguration.getFeature("http://apache.org/xml/features/include-comments") ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("datatype-normalization")) {
         return super.fConfiguration.getFeature("http://apache.org/xml/features/validation/schema/normalized-value") ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("entities")) {
         return super.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes") ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("namespaces")) {
         return super.fConfiguration.getFeature("http://xml.org/sax/features/namespaces") ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("validate")) {
         return super.fConfiguration.getFeature("http://xml.org/sax/features/validation") ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("validate-if-schema")) {
         return super.fConfiguration.getFeature("http://apache.org/xml/features/validation/dynamic") ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("element-content-whitespace")) {
         return super.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace") ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("disallow-doctype")) {
         return super.fConfiguration.getFeature("http://apache.org/xml/features/disallow-doctype-decl") ? Boolean.TRUE : Boolean.FALSE;
      } else if (!var1.equalsIgnoreCase("infoset")) {
         if (var1.equalsIgnoreCase("cdata-sections")) {
            return super.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes") ? Boolean.TRUE : Boolean.FALSE;
         } else if (!var1.equalsIgnoreCase("check-character-normalization") && !var1.equalsIgnoreCase("normalize-characters")) {
            if (!var1.equalsIgnoreCase("namespace-declarations") && !var1.equalsIgnoreCase("well-formed") && !var1.equalsIgnoreCase("ignore-unknown-character-denormalizations") && !var1.equalsIgnoreCase("canonical-form") && !var1.equalsIgnoreCase("supported-media-types-only") && !var1.equalsIgnoreCase("split-cdata-sections") && !var1.equalsIgnoreCase("charset-overrides-xml-encoding")) {
               if (var1.equalsIgnoreCase("error-handler")) {
                  return super.fErrorHandler != null ? super.fErrorHandler.getErrorHandler() : null;
               } else if (var1.equalsIgnoreCase("resource-resolver")) {
                  try {
                     XMLEntityResolver var5 = (XMLEntityResolver)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
                     return var5 != null && var5 instanceof DOMEntityResolverWrapper ? ((DOMEntityResolverWrapper)var5).getEntityResolver() : null;
                  } catch (XMLConfigurationException var3) {
                     return null;
                  }
               } else if (var1.equalsIgnoreCase("schema-type")) {
                  return super.fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
               } else if (var1.equalsIgnoreCase("schema-location")) {
                  return this.fSchemaLocation;
               } else if (var1.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
                  return super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table");
               } else if (var1.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
                  return super.fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name");
               } else {
                  String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
                  throw new DOMException((short)8, var4);
               }
            } else {
               return super.fConfiguration.getFeature(var1.toLowerCase(Locale.ENGLISH)) ? Boolean.TRUE : Boolean.FALSE;
            }
         } else {
            return Boolean.FALSE;
         }
      } else {
         boolean var2 = super.fConfiguration.getFeature("http://xml.org/sax/features/namespaces") && super.fConfiguration.getFeature("namespace-declarations") && super.fConfiguration.getFeature("http://apache.org/xml/features/include-comments") && super.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace") && !super.fConfiguration.getFeature("http://apache.org/xml/features/validation/dynamic") && !super.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes") && !super.fConfiguration.getFeature("http://apache.org/xml/features/validation/schema/normalized-value") && !super.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes");
         return var2 ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public boolean canSetParameter(String var1, Object var2) {
      if (var2 == null) {
         return true;
      } else if (var2 instanceof Boolean) {
         boolean var3 = (Boolean)var2;
         if (!var1.equalsIgnoreCase("supported-media-types-only") && !var1.equalsIgnoreCase("normalize-characters") && !var1.equalsIgnoreCase("check-character-normalization") && !var1.equalsIgnoreCase("canonical-form")) {
            if (!var1.equalsIgnoreCase("well-formed") && !var1.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
               if (!var1.equalsIgnoreCase("cdata-sections") && !var1.equalsIgnoreCase("charset-overrides-xml-encoding") && !var1.equalsIgnoreCase("comments") && !var1.equalsIgnoreCase("datatype-normalization") && !var1.equalsIgnoreCase("disallow-doctype") && !var1.equalsIgnoreCase("entities") && !var1.equalsIgnoreCase("infoset") && !var1.equalsIgnoreCase("namespaces") && !var1.equalsIgnoreCase("namespace-declarations") && !var1.equalsIgnoreCase("validate") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("element-content-whitespace") && !var1.equalsIgnoreCase("xml-declaration")) {
                  try {
                     super.fConfiguration.getFeature(var1.toLowerCase(Locale.ENGLISH));
                     return true;
                  } catch (XMLConfigurationException var5) {
                     return false;
                  }
               } else {
                  return true;
               }
            } else {
               return var3;
            }
         } else {
            return !var3;
         }
      } else if (var1.equalsIgnoreCase("error-handler")) {
         return var2 instanceof DOMErrorHandler || var2 == null;
      } else if (var1.equalsIgnoreCase("resource-resolver")) {
         return var2 instanceof LSResourceResolver || var2 == null;
      } else if (!var1.equalsIgnoreCase("schema-type")) {
         if (var1.equalsIgnoreCase("schema-location")) {
            return var2 instanceof String || var2 == null;
         } else {
            return var1.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name");
         }
      } else {
         return var2 instanceof String && (var2.equals(Constants.NS_XMLSCHEMA) || var2.equals(Constants.NS_DTD)) || var2 == null;
      }
   }

   public DOMStringList getParameterNames() {
      if (this.fRecognizedParameters == null) {
         Vector var1 = new Vector();
         var1.add("namespaces");
         var1.add("cdata-sections");
         var1.add("canonical-form");
         var1.add("namespace-declarations");
         var1.add("split-cdata-sections");
         var1.add("entities");
         var1.add("validate-if-schema");
         var1.add("validate");
         var1.add("datatype-normalization");
         var1.add("charset-overrides-xml-encoding");
         var1.add("check-character-normalization");
         var1.add("supported-media-types-only");
         var1.add("ignore-unknown-character-denormalizations");
         var1.add("normalize-characters");
         var1.add("well-formed");
         var1.add("infoset");
         var1.add("disallow-doctype");
         var1.add("element-content-whitespace");
         var1.add("comments");
         var1.add("error-handler");
         var1.add("resource-resolver");
         var1.add("schema-location");
         var1.add("schema-type");
         this.fRecognizedParameters = new DOMStringListImpl(var1);
      }

      return this.fRecognizedParameters;
   }

   public Document parseURI(String var1) throws LSException {
      if (this.fBusy) {
         String var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null);
         throw new DOMException((short)11, var6);
      } else {
         XMLInputSource var2 = new XMLInputSource((String)null, var1, (String)null);

         try {
            this.currentThread = Thread.currentThread();
            this.fBusy = true;
            this.parse(var2);
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
               this.abortNow = false;
               Thread.interrupted();
            }
         } catch (Exception var5) {
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
               Thread.interrupted();
            }

            if (this.abortNow) {
               this.abortNow = false;
               this.restoreHandlers();
               return null;
            }

            if (var5 != AbstractDOMParser.abort) {
               if (!(var5 instanceof XMLParseException) && super.fErrorHandler != null) {
                  DOMErrorImpl var4 = new DOMErrorImpl();
                  var4.fException = var5;
                  var4.fMessage = var5.getMessage();
                  var4.fSeverity = 3;
                  super.fErrorHandler.getErrorHandler().handleError(var4);
               }

               throw new LSException((short)81, var5.getMessage());
            }
         }

         return this.getDocument();
      }
   }

   public Document parse(LSInput var1) throws LSException {
      XMLInputSource var2 = this.dom2xmlInputSource(var1);
      if (this.fBusy) {
         String var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null);
         throw new DOMException((short)11, var3);
      } else {
         try {
            this.currentThread = Thread.currentThread();
            this.fBusy = true;
            this.parse(var2);
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
               this.abortNow = false;
               Thread.interrupted();
            }
         } catch (Exception var5) {
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
               Thread.interrupted();
            }

            if (this.abortNow) {
               this.abortNow = false;
               this.restoreHandlers();
               return null;
            }

            if (var5 != AbstractDOMParser.abort) {
               if (!(var5 instanceof XMLParseException) && super.fErrorHandler != null) {
                  DOMErrorImpl var4 = new DOMErrorImpl();
                  var4.fException = var5;
                  var4.fMessage = var5.getMessage();
                  var4.fSeverity = 3;
                  super.fErrorHandler.getErrorHandler().handleError(var4);
               }

               throw new LSException((short)81, var5.getMessage());
            }
         }

         return this.getDocument();
      }
   }

   private void restoreHandlers() {
      super.fConfiguration.setDocumentHandler(this);
      super.fConfiguration.setDTDHandler(this);
      super.fConfiguration.setDTDContentModelHandler(this);
   }

   public Node parseWithContext(LSInput var1, Node var2, short var3) throws DOMException, LSException {
      throw new DOMException((short)9, "Not supported");
   }

   XMLInputSource dom2xmlInputSource(LSInput var1) {
      XMLInputSource var2 = null;
      if (var1.getCharacterStream() != null) {
         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI(), var1.getCharacterStream(), "UTF-16");
      } else if (var1.getByteStream() != null) {
         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI(), var1.getByteStream(), var1.getEncoding());
      } else if (var1.getStringData() != null && var1.getStringData().length() > 0) {
         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI(), new StringReader(var1.getStringData()), "UTF-16");
      } else {
         if ((var1.getSystemId() == null || var1.getSystemId().length() <= 0) && (var1.getPublicId() == null || var1.getPublicId().length() <= 0)) {
            if (super.fErrorHandler != null) {
               DOMErrorImpl var3 = new DOMErrorImpl();
               var3.fType = "no-input-specified";
               var3.fMessage = "no-input-specified";
               var3.fSeverity = 3;
               super.fErrorHandler.getErrorHandler().handleError(var3);
            }

            throw new LSException((short)81, "no-input-specified");
         }

         var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), var1.getBaseURI());
      }

      return var2;
   }

   public boolean getAsync() {
      return false;
   }

   public boolean getBusy() {
      return this.fBusy;
   }

   public void abort() {
      if (this.fBusy) {
         this.fBusy = false;
         if (this.currentThread != null) {
            this.abortNow = true;
            super.fConfiguration.setDocumentHandler(this.abortHandler);
            super.fConfiguration.setDTDHandler(this.abortHandler);
            super.fConfiguration.setDTDContentModelHandler(this.abortHandler);
            if (this.currentThread == Thread.currentThread()) {
               throw AbstractDOMParser.abort;
            }

            this.currentThread.interrupt();
         }
      }

   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) {
      if (!this.fNamespaceDeclarations && super.fNamespaceAware) {
         int var4 = var2.getLength();

         for(int var5 = var4 - 1; var5 >= 0; --var5) {
            if (XMLSymbols.PREFIX_XMLNS == var2.getPrefix(var5) || XMLSymbols.PREFIX_XMLNS == var2.getQName(var5)) {
               var2.removeAttributeAt(var5);
            }
         }
      }

      super.startElement(var1, var2, var3);
   }

   private class AbortHandler implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler {
      private XMLDocumentSource documentSource;
      private XMLDTDContentModelSource dtdContentSource;
      private XMLDTDSource dtdSource;

      private AbortHandler() {
      }

      public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void comment(XMLString var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void characters(XMLString var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endElement(QName var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startCDATA(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endCDATA(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endDocument(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void setDocumentSource(XMLDocumentSource var1) {
         this.documentSource = var1;
      }

      public XMLDocumentSource getDocumentSource() {
         return this.documentSource;
      }

      public void startDTD(XMLLocator var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endParameterEntity(String var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endExternalSubset(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void elementDecl(String var1, String var2, Augmentations var3) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startAttlist(String var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endAttlist(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startConditional(short var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endConditional(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endDTD(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void setDTDSource(XMLDTDSource var1) {
         this.dtdSource = var1;
      }

      public XMLDTDSource getDTDSource() {
         return this.dtdSource;
      }

      public void startContentModel(String var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void any(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void empty(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void startGroup(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void pcdata(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void element(String var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void separator(short var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void occurrence(short var1, Augmentations var2) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endGroup(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void endContentModel(Augmentations var1) throws XNIException {
         throw AbstractDOMParser.abort;
      }

      public void setDTDContentModelSource(XMLDTDContentModelSource var1) {
         this.dtdContentSource = var1;
      }

      public XMLDTDContentModelSource getDTDContentModelSource() {
         return this.dtdContentSource;
      }

      // $FF: synthetic method
      AbortHandler(Object var2) {
         this();
      }
   }
}
