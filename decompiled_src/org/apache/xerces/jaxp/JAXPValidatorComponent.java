package org.apache.xerces.jaxp;

import java.io.IOException;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.opti.DefaultXMLDocumentHandler;
import org.apache.xerces.util.AttributesProxy;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.ErrorHandlerProxy;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.LocatorProxy;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

final class JAXPValidatorComponent extends TeeXMLDocumentFilterImpl implements XMLComponent {
   private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   private final ValidatorHandler validator;
   private final XNI2SAX xni2sax = new XNI2SAX();
   private final SAX2XNI sax2xni = new SAX2XNI();
   private final TypeInfoProvider typeInfoProvider;
   private Augmentations fCurrentAug;
   private XMLAttributes fCurrentAttributes;
   private SymbolTable fSymbolTable;
   private XMLErrorReporter fErrorReporter;
   private XMLEntityResolver fEntityResolver;
   private static final TypeInfoProvider noInfoProvider = new TypeInfoProvider() {
      public TypeInfo getElementTypeInfo() {
         return null;
      }

      public TypeInfo getAttributeTypeInfo(int var1) {
         return null;
      }

      public TypeInfo getAttributeTypeInfo(String var1) {
         return null;
      }

      public TypeInfo getAttributeTypeInfo(String var1, String var2) {
         return null;
      }

      public boolean isIdAttribute(int var1) {
         return false;
      }

      public boolean isSpecified(int var1) {
         return false;
      }
   };

   public JAXPValidatorComponent(ValidatorHandler var1) {
      this.validator = var1;
      TypeInfoProvider var2 = var1.getTypeInfoProvider();
      if (var2 == null) {
         var2 = noInfoProvider;
      }

      this.typeInfoProvider = var2;
      this.xni2sax.setContentHandler(this.validator);
      this.validator.setContentHandler(this.sax2xni);
      this.setSide(this.xni2sax);
      this.validator.setErrorHandler(new ErrorHandlerProxy() {
         protected XMLErrorHandler getErrorHandler() {
            XMLErrorHandler var1 = JAXPValidatorComponent.this.fErrorReporter.getErrorHandler();
            return (XMLErrorHandler)(var1 != null ? var1 : new ErrorHandlerWrapper(JAXPValidatorComponent.DraconianErrorHandler.getInstance()));
         }
      });
      this.validator.setResourceResolver(new LSResourceResolver() {
         public LSInput resolveResource(String var1, String var2, String var3, String var4, String var5) {
            if (JAXPValidatorComponent.this.fEntityResolver == null) {
               return null;
            } else {
               try {
                  XMLInputSource var6 = JAXPValidatorComponent.this.fEntityResolver.resolveEntity(new XMLResourceIdentifierImpl(var3, var4, var5, (String)null));
                  if (var6 == null) {
                     return null;
                  } else {
                     DOMInputImpl var7 = new DOMInputImpl();
                     var7.setBaseURI(var6.getBaseSystemId());
                     var7.setByteStream(var6.getByteStream());
                     var7.setCharacterStream(var6.getCharacterStream());
                     var7.setEncoding(var6.getEncoding());
                     var7.setPublicId(var6.getPublicId());
                     var7.setSystemId(var6.getSystemId());
                     return var7;
                  }
               } catch (IOException var8) {
                  throw new XNIException(var8);
               }
            }
         }
      });
   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      this.fCurrentAttributes = var2;
      this.fCurrentAug = var3;
      this.xni2sax.startElement(var1, var2, (Augmentations)null);
      this.fCurrentAttributes = null;
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      this.fCurrentAug = var2;
      this.xni2sax.endElement(var1, (Augmentations)null);
   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      this.startElement(var1, var2, var3);
      this.endElement(var1, var3);
   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      this.fCurrentAug = var2;
      this.xni2sax.characters(var1, (Augmentations)null);
   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      this.fCurrentAug = var2;
      this.xni2sax.ignorableWhitespace(var1, (Augmentations)null);
   }

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      this.fSymbolTable = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");
      this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");

      try {
         this.fEntityResolver = (XMLEntityResolver)var1.getProperty("http://apache.org/xml/properties/internal/entity-manager");
      } catch (XMLConfigurationException var3) {
         this.fEntityResolver = null;
      }

   }

   private void updateAttributes(Attributes var1) {
      int var2 = var1.getLength();

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1.getQName(var3);
         int var5 = this.fCurrentAttributes.getIndex(var4);
         String var6 = var1.getValue(var3);
         if (var5 == -1) {
            int var8 = var4.indexOf(58);
            String var7;
            if (var8 < 0) {
               var7 = null;
            } else {
               var7 = this.symbolize(var4.substring(0, var8));
            }

            var5 = this.fCurrentAttributes.addAttribute(new QName(var7, this.symbolize(var1.getLocalName(var3)), this.symbolize(var4), this.symbolize(var1.getURI(var3))), var1.getType(var3), var6);
         } else if (!var6.equals(this.fCurrentAttributes.getValue(var5))) {
            this.fCurrentAttributes.setValue(var5, var6);
         }
      }

   }

   private String symbolize(String var1) {
      return this.fSymbolTable.addSymbol(var1);
   }

   public String[] getRecognizedFeatures() {
      return null;
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
   }

   public String[] getRecognizedProperties() {
      return new String[]{"http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/symbol-table"};
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
   }

   public Boolean getFeatureDefault(String var1) {
      return null;
   }

   public Object getPropertyDefault(String var1) {
      return null;
   }

   private static final class DraconianErrorHandler implements ErrorHandler {
      private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();

      public static DraconianErrorHandler getInstance() {
         return ERROR_HANDLER_INSTANCE;
      }

      public void warning(SAXParseException var1) throws SAXException {
      }

      public void error(SAXParseException var1) throws SAXException {
         throw var1;
      }

      public void fatalError(SAXParseException var1) throws SAXException {
         throw var1;
      }
   }

   private final class XNI2SAX extends DefaultXMLDocumentHandler {
      private ContentHandler fContentHandler;
      private String fVersion;
      protected NamespaceContext fNamespaceContext;
      private final AttributesProxy fAttributesProxy;

      private XNI2SAX() {
         this.fAttributesProxy = new AttributesProxy((XMLAttributes)null);
      }

      public void setContentHandler(ContentHandler var1) {
         this.fContentHandler = var1;
      }

      public ContentHandler getContentHandler() {
         return this.fContentHandler;
      }

      public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
         this.fVersion = var1;
      }

      public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
         this.fNamespaceContext = var3;
         this.fContentHandler.setDocumentLocator(new LocatorProxy(var1));

         try {
            this.fContentHandler.startDocument();
         } catch (SAXException var6) {
            throw new XNIException(var6);
         }
      }

      public void endDocument(Augmentations var1) throws XNIException {
         try {
            this.fContentHandler.endDocument();
         } catch (SAXException var3) {
            throw new XNIException(var3);
         }
      }

      public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
         try {
            this.fContentHandler.processingInstruction(var1, var2.toString());
         } catch (SAXException var5) {
            throw new XNIException(var5);
         }
      }

      public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
         try {
            int var4 = this.fNamespaceContext.getDeclaredPrefixCount();
            String var5;
            String var6;
            if (var4 > 0) {
               var5 = null;
               var6 = null;

               for(int var7 = 0; var7 < var4; ++var7) {
                  var5 = this.fNamespaceContext.getDeclaredPrefixAt(var7);
                  var6 = this.fNamespaceContext.getURI(var5);
                  this.fContentHandler.startPrefixMapping(var5, var6 == null ? "" : var6);
               }
            }

            var5 = var1.uri != null ? var1.uri : "";
            var6 = var1.localpart;
            this.fAttributesProxy.setAttributes(var2);
            this.fContentHandler.startElement(var5, var6, var1.rawname, this.fAttributesProxy);
         } catch (SAXException var8) {
            throw new XNIException(var8);
         }
      }

      public void endElement(QName var1, Augmentations var2) throws XNIException {
         try {
            String var3 = var1.uri != null ? var1.uri : "";
            String var4 = var1.localpart;
            this.fContentHandler.endElement(var3, var4, var1.rawname);
            int var5 = this.fNamespaceContext.getDeclaredPrefixCount();
            if (var5 > 0) {
               for(int var6 = 0; var6 < var5; ++var6) {
                  this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(var6));
               }
            }

         } catch (SAXException var7) {
            throw new XNIException(var7);
         }
      }

      public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
         this.startElement(var1, var2, var3);
         this.endElement(var1, var3);
      }

      public void characters(XMLString var1, Augmentations var2) throws XNIException {
         try {
            this.fContentHandler.characters(var1.ch, var1.offset, var1.length);
         } catch (SAXException var4) {
            throw new XNIException(var4);
         }
      }

      public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
         try {
            this.fContentHandler.ignorableWhitespace(var1.ch, var1.offset, var1.length);
         } catch (SAXException var4) {
            throw new XNIException(var4);
         }
      }

      // $FF: synthetic method
      XNI2SAX(Object var2) {
         this();
      }
   }

   private final class SAX2XNI extends DefaultHandler {
      private final Augmentations fAugmentations;
      private final QName fQName;

      private SAX2XNI() {
         this.fAugmentations = new AugmentationsImpl();
         this.fQName = new QName();
      }

      public void characters(char[] var1, int var2, int var3) throws SAXException {
         try {
            this.handler().characters(new XMLString(var1, var2, var3), this.aug());
         } catch (XNIException var5) {
            throw this.toSAXException(var5);
         }
      }

      public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
         try {
            this.handler().ignorableWhitespace(new XMLString(var1, var2, var3), this.aug());
         } catch (XNIException var5) {
            throw this.toSAXException(var5);
         }
      }

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         try {
            JAXPValidatorComponent.this.updateAttributes(var4);
            this.handler().startElement(this.toQName(var1, var2, var3), JAXPValidatorComponent.this.fCurrentAttributes, this.elementAug());
         } catch (XNIException var6) {
            throw this.toSAXException(var6);
         }
      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         try {
            this.handler().endElement(this.toQName(var1, var2, var3), this.aug());
         } catch (XNIException var5) {
            throw this.toSAXException(var5);
         }
      }

      private Augmentations elementAug() {
         Augmentations var1 = this.aug();
         return var1;
      }

      private Augmentations aug() {
         if (JAXPValidatorComponent.this.fCurrentAug != null) {
            Augmentations var1 = JAXPValidatorComponent.this.fCurrentAug;
            JAXPValidatorComponent.this.fCurrentAug = null;
            return var1;
         } else {
            this.fAugmentations.removeAllItems();
            return this.fAugmentations;
         }
      }

      private XMLDocumentHandler handler() {
         return JAXPValidatorComponent.this.getDocumentHandler();
      }

      private SAXException toSAXException(XNIException var1) {
         Object var2 = var1.getException();
         if (var2 == null) {
            var2 = var1;
         }

         return var2 instanceof SAXException ? (SAXException)var2 : new SAXException((Exception)var2);
      }

      private QName toQName(String var1, String var2, String var3) {
         String var4 = null;
         int var5 = var3.indexOf(58);
         if (var5 > 0) {
            var4 = JAXPValidatorComponent.this.symbolize(var3.substring(0, var5));
         }

         var2 = JAXPValidatorComponent.this.symbolize(var2);
         var3 = JAXPValidatorComponent.this.symbolize(var3);
         var1 = JAXPValidatorComponent.this.symbolize(var1);
         this.fQName.setValues(var4, var2, var3, var1);
         return this.fQName;
      }

      // $FF: synthetic method
      SAX2XNI(Object var2) {
         this();
      }
   }
}
