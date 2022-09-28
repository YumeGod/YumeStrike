package org.apache.xerces.parsers;

import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.util.EntityResolver2Wrapper;
import org.apache.xerces.util.EntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.xml.sax.AttributeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.LocatorImpl;

public abstract class AbstractSAXParser extends AbstractXMLDocumentParser implements PSVIProvider, Parser, XMLReader {
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
   protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/namespace-prefixes", "http://xml.org/sax/features/string-interning"};
   protected static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
   protected static final String DECLARATION_HANDLER = "http://xml.org/sax/properties/declaration-handler";
   protected static final String DOM_NODE = "http://xml.org/sax/properties/dom-node";
   private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://xml.org/sax/properties/lexical-handler", "http://xml.org/sax/properties/declaration-handler", "http://xml.org/sax/properties/dom-node"};
   protected boolean fNamespaces;
   protected boolean fNamespacePrefixes = false;
   protected boolean fLexicalHandlerParameterEntities = true;
   protected boolean fStandalone;
   protected boolean fResolveDTDURIs = true;
   protected boolean fUseEntityResolver2 = true;
   protected boolean fXMLNSURIs = false;
   protected ContentHandler fContentHandler;
   protected DocumentHandler fDocumentHandler;
   protected NamespaceContext fNamespaceContext;
   protected DTDHandler fDTDHandler;
   protected DeclHandler fDeclHandler;
   protected LexicalHandler fLexicalHandler;
   protected QName fQName = new QName();
   protected boolean fParseInProgress = false;
   protected String fVersion;
   private final AttributesProxy fAttributesProxy = new AttributesProxy();
   private Augmentations fAugmentations = null;
   private static final int BUFFER_SIZE = 20;
   private char[] fCharBuffer = new char[20];
   protected SymbolHash fDeclaredAttrs = null;

   protected AbstractSAXParser(XMLParserConfiguration var1) {
      super(var1);
      var1.addRecognizedFeatures(RECOGNIZED_FEATURES);
      var1.addRecognizedProperties(RECOGNIZED_PROPERTIES);

      try {
         var1.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", false);
      } catch (XMLConfigurationException var3) {
      }

   }

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
      this.fNamespaceContext = var3;

      try {
         if (this.fDocumentHandler != null) {
            if (var1 != null) {
               this.fDocumentHandler.setDocumentLocator(new LocatorProxy(var1));
            }

            this.fDocumentHandler.startDocument();
         }

         if (this.fContentHandler != null) {
            if (var1 != null) {
               this.fContentHandler.setDocumentLocator(new LocatorProxy(var1));
            }

            this.fContentHandler.startDocument();
         }

      } catch (SAXException var6) {
         throw new XNIException(var6);
      }
   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      this.fVersion = var1;
      this.fStandalone = "yes".equals(var3);
   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      super.fInDTD = true;

      try {
         if (this.fLexicalHandler != null) {
            this.fLexicalHandler.startDTD(var1, var2, var3);
         }
      } catch (SAXException var6) {
         throw new XNIException(var6);
      }

      if (this.fDeclHandler != null) {
         this.fDeclaredAttrs = new SymbolHash();
      }

   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      try {
         if (var4 != null && Boolean.TRUE.equals(var4.getItem("ENTITY_SKIPPED"))) {
            if (this.fContentHandler != null) {
               this.fContentHandler.skippedEntity(var1);
            }
         } else if (this.fLexicalHandler != null) {
            this.fLexicalHandler.startEntity(var1);
         }

      } catch (SAXException var6) {
         throw new XNIException(var6);
      }
   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
      try {
         if ((var2 == null || !Boolean.TRUE.equals(var2.getItem("ENTITY_SKIPPED"))) && this.fLexicalHandler != null) {
            this.fLexicalHandler.endEntity(var1);
         }

      } catch (SAXException var4) {
         throw new XNIException(var4);
      }
   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      try {
         if (this.fDocumentHandler != null) {
            this.fAttributesProxy.setAttributes(var2);
            this.fDocumentHandler.startElement(var1.rawname, this.fAttributesProxy);
         }

         if (this.fContentHandler != null) {
            if (this.fNamespaces) {
               this.startNamespaceMapping();
               int var4 = var2.getLength();
               int var5;
               if (!this.fNamespacePrefixes) {
                  for(var5 = var4 - 1; var5 >= 0; --var5) {
                     var2.getName(var5, this.fQName);
                     if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                        var2.removeAttributeAt(var5);
                     }
                  }
               } else if (!this.fXMLNSURIs) {
                  for(var5 = var4 - 1; var5 >= 0; --var5) {
                     var2.getName(var5, this.fQName);
                     if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                        this.fQName.prefix = "";
                        this.fQName.uri = "";
                        this.fQName.localpart = "";
                        var2.setName(var5, this.fQName);
                     }
                  }
               }
            }

            this.fAugmentations = var3;
            String var7 = var1.uri != null ? var1.uri : "";
            String var8 = this.fNamespaces ? var1.localpart : "";
            this.fAttributesProxy.setAttributes(var2);
            this.fContentHandler.startElement(var7, var8, var1.rawname, this.fAttributesProxy);
         }

      } catch (SAXException var6) {
         throw new XNIException(var6);
      }
   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      if (var1.length != 0) {
         try {
            if (this.fDocumentHandler != null) {
               this.fDocumentHandler.characters(var1.ch, var1.offset, var1.length);
            }

            if (this.fContentHandler != null) {
               this.fContentHandler.characters(var1.ch, var1.offset, var1.length);
            }

         } catch (SAXException var4) {
            throw new XNIException(var4);
         }
      }
   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      try {
         if (this.fDocumentHandler != null) {
            this.fDocumentHandler.ignorableWhitespace(var1.ch, var1.offset, var1.length);
         }

         if (this.fContentHandler != null) {
            this.fContentHandler.ignorableWhitespace(var1.ch, var1.offset, var1.length);
         }

      } catch (SAXException var4) {
         throw new XNIException(var4);
      }
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      try {
         if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endElement(var1.rawname);
         }

         if (this.fContentHandler != null) {
            this.fAugmentations = var2;
            String var3 = var1.uri != null ? var1.uri : "";
            String var4 = this.fNamespaces ? var1.localpart : "";
            this.fContentHandler.endElement(var3, var4, var1.rawname);
            if (this.fNamespaces) {
               this.endNamespaceMapping();
            }
         }

      } catch (SAXException var5) {
         throw new XNIException(var5);
      }
   }

   public void startCDATA(Augmentations var1) throws XNIException {
      try {
         if (this.fLexicalHandler != null) {
            this.fLexicalHandler.startCDATA();
         }

      } catch (SAXException var3) {
         throw new XNIException(var3);
      }
   }

   public void endCDATA(Augmentations var1) throws XNIException {
      try {
         if (this.fLexicalHandler != null) {
            this.fLexicalHandler.endCDATA();
         }

      } catch (SAXException var3) {
         throw new XNIException(var3);
      }
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      try {
         if (this.fLexicalHandler != null) {
            this.fLexicalHandler.comment(var1.ch, 0, var1.length);
         }

      } catch (SAXException var4) {
         throw new XNIException(var4);
      }
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      try {
         if (this.fDocumentHandler != null) {
            this.fDocumentHandler.processingInstruction(var1, var2.toString());
         }

         if (this.fContentHandler != null) {
            this.fContentHandler.processingInstruction(var1, var2.toString());
         }

      } catch (SAXException var5) {
         throw new XNIException(var5);
      }
   }

   public void endDocument(Augmentations var1) throws XNIException {
      try {
         if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endDocument();
         }

         if (this.fContentHandler != null) {
            this.fContentHandler.endDocument();
         }

      } catch (SAXException var3) {
         throw new XNIException(var3);
      }
   }

   public void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException {
      this.startParameterEntity("[dtd]", (XMLResourceIdentifier)null, (String)null, var2);
   }

   public void endExternalSubset(Augmentations var1) throws XNIException {
      this.endParameterEntity("[dtd]", var1);
   }

   public void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      try {
         if (var4 != null && Boolean.TRUE.equals(var4.getItem("ENTITY_SKIPPED"))) {
            if (this.fContentHandler != null) {
               this.fContentHandler.skippedEntity(var1);
            }
         } else if (this.fLexicalHandler != null && this.fLexicalHandlerParameterEntities) {
            this.fLexicalHandler.startEntity(var1);
         }

      } catch (SAXException var6) {
         throw new XNIException(var6);
      }
   }

   public void endParameterEntity(String var1, Augmentations var2) throws XNIException {
      try {
         if ((var2 == null || !Boolean.TRUE.equals(var2.getItem("ENTITY_SKIPPED"))) && this.fLexicalHandler != null && this.fLexicalHandlerParameterEntities) {
            this.fLexicalHandler.endEntity(var1);
         }

      } catch (SAXException var4) {
         throw new XNIException(var4);
      }
   }

   public void elementDecl(String var1, String var2, Augmentations var3) throws XNIException {
      try {
         if (this.fDeclHandler != null) {
            this.fDeclHandler.elementDecl(var1, var2);
         }

      } catch (SAXException var5) {
         throw new XNIException(var5);
      }
   }

   public void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException {
      try {
         if (this.fDeclHandler != null) {
            String var9 = var1 + "<" + var2;
            if (this.fDeclaredAttrs.get(var9) != null) {
               return;
            }

            this.fDeclaredAttrs.put(var9, Boolean.TRUE);
            if (var3.equals("NOTATION") || var3.equals("ENUMERATION")) {
               StringBuffer var10 = new StringBuffer();
               if (var3.equals("NOTATION")) {
                  var10.append(var3);
                  var10.append(" (");
               } else {
                  var10.append("(");
               }

               for(int var11 = 0; var11 < var4.length; ++var11) {
                  var10.append(var4[var11]);
                  if (var11 < var4.length - 1) {
                     var10.append('|');
                  }
               }

               var10.append(')');
               var3 = var10.toString();
            }

            String var13 = var6 == null ? null : var6.toString();
            this.fDeclHandler.attributeDecl(var1, var2, var3, var5, var13);
         }

      } catch (SAXException var12) {
         throw new XNIException(var12);
      }
   }

   public void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException {
      try {
         if (this.fDeclHandler != null) {
            this.fDeclHandler.internalEntityDecl(var1, var2.toString());
         }

      } catch (SAXException var6) {
         throw new XNIException(var6);
      }
   }

   public void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      try {
         if (this.fDeclHandler != null) {
            String var4 = var2.getPublicId();
            String var5 = this.fResolveDTDURIs ? var2.getExpandedSystemId() : var2.getLiteralSystemId();
            this.fDeclHandler.externalEntityDecl(var1, var4, var5);
         }

      } catch (SAXException var6) {
         throw new XNIException(var6);
      }
   }

   public void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      try {
         if (this.fDTDHandler != null) {
            String var5 = var2.getPublicId();
            String var6 = this.fResolveDTDURIs ? var2.getExpandedSystemId() : var2.getLiteralSystemId();
            this.fDTDHandler.unparsedEntityDecl(var1, var5, var6, var3);
         }

      } catch (SAXException var7) {
         throw new XNIException(var7);
      }
   }

   public void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      try {
         if (this.fDTDHandler != null) {
            String var4 = var2.getPublicId();
            String var5 = this.fResolveDTDURIs ? var2.getExpandedSystemId() : var2.getLiteralSystemId();
            this.fDTDHandler.notationDecl(var1, var4, var5);
         }

      } catch (SAXException var6) {
         throw new XNIException(var6);
      }
   }

   public void endDTD(Augmentations var1) throws XNIException {
      super.fInDTD = false;

      try {
         if (this.fLexicalHandler != null) {
            this.fLexicalHandler.endDTD();
         }
      } catch (SAXException var3) {
         throw new XNIException(var3);
      }

      if (this.fDeclaredAttrs != null) {
         this.fDeclaredAttrs.clear();
      }

   }

   public void parse(String var1) throws SAXException, IOException {
      XMLInputSource var2 = new XMLInputSource((String)null, var1, (String)null);

      try {
         this.parse(var2);
      } catch (XMLParseException var6) {
         Exception var4 = var6.getException();
         if (var4 == null) {
            LocatorImpl var8 = new LocatorImpl() {
               public String getXMLVersion() {
                  return AbstractSAXParser.this.fVersion;
               }

               public String getEncoding() {
                  return null;
               }
            };
            var8.setPublicId(var6.getPublicId());
            var8.setSystemId(var6.getExpandedSystemId());
            var8.setLineNumber(var6.getLineNumber());
            var8.setColumnNumber(var6.getColumnNumber());
            throw new SAXParseException(var6.getMessage(), var8);
         } else if (var4 instanceof SAXException) {
            throw (SAXException)var4;
         } else if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new SAXException(var4);
         }
      } catch (XNIException var7) {
         Exception var5 = var7.getException();
         if (var5 == null) {
            throw new SAXException(var7.getMessage());
         } else if (var5 instanceof SAXException) {
            throw (SAXException)var5;
         } else if (var5 instanceof IOException) {
            throw (IOException)var5;
         } else {
            throw new SAXException(var5);
         }
      }
   }

   public void parse(InputSource var1) throws SAXException, IOException {
      try {
         XMLInputSource var2 = new XMLInputSource(var1.getPublicId(), var1.getSystemId(), (String)null);
         var2.setByteStream(var1.getByteStream());
         var2.setCharacterStream(var1.getCharacterStream());
         var2.setEncoding(var1.getEncoding());
         this.parse(var2);
      } catch (XMLParseException var5) {
         Exception var3 = var5.getException();
         if (var3 == null) {
            LocatorImpl var7 = new LocatorImpl() {
               public String getXMLVersion() {
                  return AbstractSAXParser.this.fVersion;
               }

               public String getEncoding() {
                  return null;
               }
            };
            var7.setPublicId(var5.getPublicId());
            var7.setSystemId(var5.getExpandedSystemId());
            var7.setLineNumber(var5.getLineNumber());
            var7.setColumnNumber(var5.getColumnNumber());
            throw new SAXParseException(var5.getMessage(), var7);
         } else if (var3 instanceof SAXException) {
            throw (SAXException)var3;
         } else if (var3 instanceof IOException) {
            throw (IOException)var3;
         } else {
            throw new SAXException(var3);
         }
      } catch (XNIException var6) {
         Exception var4 = var6.getException();
         if (var4 == null) {
            throw new SAXException(var6.getMessage());
         } else if (var4 instanceof SAXException) {
            throw (SAXException)var4;
         } else if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new SAXException(var4);
         }
      }
   }

   public void setEntityResolver(EntityResolver var1) {
      try {
         XMLEntityResolver var2 = (XMLEntityResolver)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
         if (this.fUseEntityResolver2 && var1 instanceof EntityResolver2) {
            if (var2 instanceof EntityResolver2Wrapper) {
               EntityResolver2Wrapper var5 = (EntityResolver2Wrapper)var2;
               var5.setEntityResolver((EntityResolver2)var1);
            } else {
               super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2)var1));
            }
         } else if (var2 instanceof EntityResolverWrapper) {
            EntityResolverWrapper var3 = (EntityResolverWrapper)var2;
            var3.setEntityResolver(var1);
         } else {
            super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(var1));
         }
      } catch (XMLConfigurationException var4) {
      }

   }

   public EntityResolver getEntityResolver() {
      Object var1 = null;

      try {
         XMLEntityResolver var2 = (XMLEntityResolver)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
         if (var2 != null) {
            if (var2 instanceof EntityResolverWrapper) {
               var1 = ((EntityResolverWrapper)var2).getEntityResolver();
            } else if (var2 instanceof EntityResolver2Wrapper) {
               var1 = ((EntityResolver2Wrapper)var2).getEntityResolver();
            }
         }
      } catch (XMLConfigurationException var3) {
      }

      return (EntityResolver)var1;
   }

   public void setErrorHandler(ErrorHandler var1) {
      try {
         XMLErrorHandler var2 = (XMLErrorHandler)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
         if (var2 instanceof ErrorHandlerWrapper) {
            ErrorHandlerWrapper var3 = (ErrorHandlerWrapper)var2;
            var3.setErrorHandler(var1);
         } else {
            super.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(var1));
         }
      } catch (XMLConfigurationException var4) {
      }

   }

   public ErrorHandler getErrorHandler() {
      ErrorHandler var1 = null;

      try {
         XMLErrorHandler var2 = (XMLErrorHandler)super.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
         if (var2 != null && var2 instanceof ErrorHandlerWrapper) {
            var1 = ((ErrorHandlerWrapper)var2).getErrorHandler();
         }
      } catch (XMLConfigurationException var3) {
      }

      return var1;
   }

   public void setLocale(Locale var1) throws SAXException {
      super.fConfiguration.setLocale(var1);
   }

   public void setDTDHandler(DTDHandler var1) {
      this.fDTDHandler = var1;
   }

   public void setDocumentHandler(DocumentHandler var1) {
      this.fDocumentHandler = var1;
   }

   public void setContentHandler(ContentHandler var1) {
      this.fContentHandler = var1;
   }

   public ContentHandler getContentHandler() {
      return this.fContentHandler;
   }

   public DTDHandler getDTDHandler() {
      return this.fDTDHandler;
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         if (var1.startsWith("http://xml.org/sax/features/")) {
            int var3 = var1.length() - "http://xml.org/sax/features/".length();
            if (var3 == "namespaces".length() && var1.endsWith("namespaces")) {
               super.fConfiguration.setFeature(var1, var2);
               this.fNamespaces = var2;
               return;
            }

            if (var3 == "namespace-prefixes".length() && var1.endsWith("namespace-prefixes")) {
               super.fConfiguration.setFeature(var1, var2);
               this.fNamespacePrefixes = var2;
               return;
            }

            if (var3 == "string-interning".length() && var1.endsWith("string-interning")) {
               if (!var2) {
                  throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "false-not-supported", new Object[]{var1}));
               }

               return;
            }

            if (var3 == "lexical-handler/parameter-entities".length() && var1.endsWith("lexical-handler/parameter-entities")) {
               this.fLexicalHandlerParameterEntities = var2;
               return;
            }

            if (var3 == "resolve-dtd-uris".length() && var1.endsWith("resolve-dtd-uris")) {
               this.fResolveDTDURIs = var2;
               return;
            }

            if (var3 == "unicode-normalization-checking".length() && var1.endsWith("unicode-normalization-checking")) {
               if (var2) {
                  throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "true-not-supported", new Object[]{var1}));
               }

               return;
            }

            if (var3 == "xmlns-uris".length() && var1.endsWith("xmlns-uris")) {
               this.fXMLNSURIs = var2;
               return;
            }

            if (var3 == "use-entity-resolver2".length() && var1.endsWith("use-entity-resolver2")) {
               if (var2 != this.fUseEntityResolver2) {
                  this.fUseEntityResolver2 = var2;
                  this.setEntityResolver(this.getEntityResolver());
               }

               return;
            }

            if (var3 == "is-standalone".length() && var1.endsWith("is-standalone") || var3 == "use-attributes2".length() && var1.endsWith("use-attributes2") || var3 == "use-locator2".length() && var1.endsWith("use-locator2") || var3 == "xml-1.1".length() && var1.endsWith("xml-1.1")) {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-read-only", new Object[]{var1}));
            }
         }

         super.fConfiguration.setFeature(var1, var2);
      } catch (XMLConfigurationException var5) {
         String var4 = var5.getIdentifier();
         if (var5.getType() == 0) {
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{var4}));
         } else {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-supported", new Object[]{var4}));
         }
      }
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         if (var1.startsWith("http://xml.org/sax/features/")) {
            int var2 = var1.length() - "http://xml.org/sax/features/".length();
            if (var2 == "namespace-prefixes".length() && var1.endsWith("namespace-prefixes")) {
               boolean var5 = super.fConfiguration.getFeature(var1);
               return var5;
            }

            if (var2 == "string-interning".length() && var1.endsWith("string-interning")) {
               return true;
            }

            if (var2 == "is-standalone".length() && var1.endsWith("is-standalone")) {
               return this.fStandalone;
            }

            if (var2 == "xml-1.1".length() && var1.endsWith("xml-1.1")) {
               return super.fConfiguration instanceof XML11Configurable;
            }

            if (var2 == "lexical-handler/parameter-entities".length() && var1.endsWith("lexical-handler/parameter-entities")) {
               return this.fLexicalHandlerParameterEntities;
            }

            if (var2 == "resolve-dtd-uris".length() && var1.endsWith("resolve-dtd-uris")) {
               return this.fResolveDTDURIs;
            }

            if (var2 == "xmlns-uris".length() && var1.endsWith("xmlns-uris")) {
               return this.fXMLNSURIs;
            }

            if (var2 == "unicode-normalization-checking".length() && var1.endsWith("unicode-normalization-checking")) {
               return false;
            }

            if (var2 == "use-entity-resolver2".length() && var1.endsWith("use-entity-resolver2")) {
               return this.fUseEntityResolver2;
            }

            if (var2 == "use-attributes2".length() && var1.endsWith("use-attributes2") || var2 == "use-locator2".length() && var1.endsWith("use-locator2")) {
               return true;
            }
         }

         return super.fConfiguration.getFeature(var1);
      } catch (XMLConfigurationException var4) {
         String var3 = var4.getIdentifier();
         if (var4.getType() == 0) {
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{var3}));
         } else {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "feature-not-supported", new Object[]{var3}));
         }
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         if (var1.startsWith("http://xml.org/sax/properties/")) {
            int var3 = var1.length() - "http://xml.org/sax/properties/".length();
            if (var3 == "lexical-handler".length() && var1.endsWith("lexical-handler")) {
               try {
                  this.setLexicalHandler((LexicalHandler)var2);
                  return;
               } catch (ClassCastException var5) {
                  throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "incompatible-class", new Object[]{var1, "org.xml.sax.ext.LexicalHandler"}));
               }
            }

            if (var3 == "declaration-handler".length() && var1.endsWith("declaration-handler")) {
               try {
                  this.setDeclHandler((DeclHandler)var2);
                  return;
               } catch (ClassCastException var6) {
                  throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "incompatible-class", new Object[]{var1, "org.xml.sax.ext.DeclHandler"}));
               }
            }

            if (var3 == "dom-node".length() && var1.endsWith("dom-node") || var3 == "document-xml-version".length() && var1.endsWith("document-xml-version")) {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-read-only", new Object[]{var1}));
            }
         }

         super.fConfiguration.setProperty(var1, var2);
      } catch (XMLConfigurationException var7) {
         String var4 = var7.getIdentifier();
         if (var7.getType() == 0) {
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-recognized", new Object[]{var4}));
         } else {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-supported", new Object[]{var4}));
         }
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         if (var1.startsWith("http://xml.org/sax/properties/")) {
            int var2 = var1.length() - "http://xml.org/sax/properties/".length();
            if (var2 == "document-xml-version".length() && var1.endsWith("document-xml-version")) {
               return this.fVersion;
            }

            if (var2 == "lexical-handler".length() && var1.endsWith("lexical-handler")) {
               return this.getLexicalHandler();
            }

            if (var2 == "declaration-handler".length() && var1.endsWith("declaration-handler")) {
               return this.getDeclHandler();
            }

            if (var2 == "dom-node".length() && var1.endsWith("dom-node")) {
               throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "dom-node-read-not-supported", (Object[])null));
            }
         }

         return super.fConfiguration.getProperty(var1);
      } catch (XMLConfigurationException var4) {
         String var3 = var4.getIdentifier();
         if (var4.getType() == 0) {
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-recognized", new Object[]{var3}));
         } else {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-supported", new Object[]{var3}));
         }
      }
   }

   protected void setDeclHandler(DeclHandler var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (this.fParseInProgress) {
         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{"http://xml.org/sax/properties/declaration-handler"}));
      } else {
         this.fDeclHandler = var1;
      }
   }

   protected DeclHandler getDeclHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.fDeclHandler;
   }

   protected void setLexicalHandler(LexicalHandler var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (this.fParseInProgress) {
         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(super.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{"http://xml.org/sax/properties/lexical-handler"}));
      } else {
         this.fLexicalHandler = var1;
      }
   }

   protected LexicalHandler getLexicalHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.fLexicalHandler;
   }

   protected final void startNamespaceMapping() throws SAXException {
      int var1 = this.fNamespaceContext.getDeclaredPrefixCount();
      if (var1 > 0) {
         String var2 = null;
         String var3 = null;

         for(int var4 = 0; var4 < var1; ++var4) {
            var2 = this.fNamespaceContext.getDeclaredPrefixAt(var4);
            var3 = this.fNamespaceContext.getURI(var2);
            this.fContentHandler.startPrefixMapping(var2, var3 == null ? "" : var3);
         }
      }

   }

   protected final void endNamespaceMapping() throws SAXException {
      int var1 = this.fNamespaceContext.getDeclaredPrefixCount();
      if (var1 > 0) {
         for(int var2 = 0; var2 < var1; ++var2) {
            this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(var2));
         }
      }

   }

   public void reset() throws XNIException {
      super.reset();
      super.fInDTD = false;
      this.fVersion = "1.0";
      this.fStandalone = false;
      this.fNamespaces = super.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
      this.fNamespacePrefixes = super.fConfiguration.getFeature("http://xml.org/sax/features/namespace-prefixes");
      this.fAugmentations = null;
      this.fDeclaredAttrs = null;
   }

   public ElementPSVI getElementPSVI() {
      return this.fAugmentations != null ? (ElementPSVI)this.fAugmentations.getItem("ELEMENT_PSVI") : null;
   }

   public AttributePSVI getAttributePSVI(int var1) {
      return (AttributePSVI)this.fAttributesProxy.fAttributes.getAugmentations(var1).getItem("ATTRIBUTE_PSVI");
   }

   public AttributePSVI getAttributePSVIByName(String var1, String var2) {
      return (AttributePSVI)this.fAttributesProxy.fAttributes.getAugmentations(var1, var2).getItem("ATTRIBUTE_PSVI");
   }

   protected static final class AttributesProxy implements AttributeList, Attributes2 {
      protected XMLAttributes fAttributes;

      public void setAttributes(XMLAttributes var1) {
         this.fAttributes = var1;
      }

      public int getLength() {
         return this.fAttributes.getLength();
      }

      public String getName(int var1) {
         return this.fAttributes.getQName(var1);
      }

      public String getQName(int var1) {
         return this.fAttributes.getQName(var1);
      }

      public String getURI(int var1) {
         String var2 = this.fAttributes.getURI(var1);
         return var2 != null ? var2 : "";
      }

      public String getLocalName(int var1) {
         return this.fAttributes.getLocalName(var1);
      }

      public String getType(int var1) {
         return this.fAttributes.getType(var1);
      }

      public String getType(String var1) {
         return this.fAttributes.getType(var1);
      }

      public String getType(String var1, String var2) {
         return var1.equals("") ? this.fAttributes.getType((String)null, var2) : this.fAttributes.getType(var1, var2);
      }

      public String getValue(int var1) {
         return this.fAttributes.getValue(var1);
      }

      public String getValue(String var1) {
         return this.fAttributes.getValue(var1);
      }

      public String getValue(String var1, String var2) {
         return var1.equals("") ? this.fAttributes.getValue((String)null, var2) : this.fAttributes.getValue(var1, var2);
      }

      public int getIndex(String var1) {
         return this.fAttributes.getIndex(var1);
      }

      public int getIndex(String var1, String var2) {
         return var1.equals("") ? this.fAttributes.getIndex((String)null, var2) : this.fAttributes.getIndex(var1, var2);
      }

      public boolean isDeclared(int var1) {
         if (var1 >= 0 && var1 < this.fAttributes.getLength()) {
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(var1).getItem("ATTRIBUTE_DECLARED"));
         } else {
            throw new ArrayIndexOutOfBoundsException(var1);
         }
      }

      public boolean isDeclared(String var1) {
         int var2 = this.getIndex(var1);
         if (var2 == -1) {
            throw new IllegalArgumentException(var1);
         } else {
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(var2).getItem("ATTRIBUTE_DECLARED"));
         }
      }

      public boolean isDeclared(String var1, String var2) {
         int var3 = this.getIndex(var1, var2);
         if (var3 == -1) {
            throw new IllegalArgumentException(var2);
         } else {
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(var3).getItem("ATTRIBUTE_DECLARED"));
         }
      }

      public boolean isSpecified(int var1) {
         if (var1 >= 0 && var1 < this.fAttributes.getLength()) {
            return this.fAttributes.isSpecified(var1);
         } else {
            throw new ArrayIndexOutOfBoundsException(var1);
         }
      }

      public boolean isSpecified(String var1) {
         int var2 = this.getIndex(var1);
         if (var2 == -1) {
            throw new IllegalArgumentException(var1);
         } else {
            return this.fAttributes.isSpecified(var2);
         }
      }

      public boolean isSpecified(String var1, String var2) {
         int var3 = this.getIndex(var1, var2);
         if (var3 == -1) {
            throw new IllegalArgumentException(var2);
         } else {
            return this.fAttributes.isSpecified(var3);
         }
      }
   }

   protected class LocatorProxy implements Locator2 {
      protected XMLLocator fLocator;

      public LocatorProxy(XMLLocator var2) {
         this.fLocator = var2;
      }

      public String getPublicId() {
         return this.fLocator.getPublicId();
      }

      public String getSystemId() {
         return this.fLocator.getExpandedSystemId();
      }

      public int getLineNumber() {
         return this.fLocator.getLineNumber();
      }

      public int getColumnNumber() {
         return this.fLocator.getColumnNumber();
      }

      public String getXMLVersion() {
         return this.fLocator.getXMLVersion();
      }

      public String getEncoding() {
         return this.fLocator.getEncoding();
      }
   }
}
