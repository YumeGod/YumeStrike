package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

final class DOMValidatorHelper implements ValidatorHelper, EntityState {
   private static final int CHUNK_SIZE = 1024;
   private static final int CHUNK_MASK = 1023;
   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
   private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
   private XMLErrorReporter fErrorReporter;
   private NamespaceSupport fNamespaceContext;
   private DOMNamespaceContext fDOMNamespaceContext = new DOMNamespaceContext();
   private XMLSchemaValidator fSchemaValidator;
   private SymbolTable fSymbolTable;
   private ValidationManager fValidationManager;
   private XMLSchemaValidatorComponentManager fComponentManager;
   private final SimpleLocator fXMLLocator = new SimpleLocator((String)null, (String)null, -1, -1, -1);
   private DOMDocumentHandler fDOMValidatorHandler;
   private final DOMResultAugmentor fDOMResultAugmentor = new DOMResultAugmentor(this);
   private final DOMResultBuilder fDOMResultBuilder = new DOMResultBuilder();
   private NamedNodeMap fEntities = null;
   private char[] fCharBuffer = new char[1024];
   private Node fRoot;
   private Node fCurrentElement;
   final QName fElementQName = new QName();
   final QName fAttributeQName = new QName();
   final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
   final XMLString fTempString = new XMLString();

   public DOMValidatorHelper(XMLSchemaValidatorComponentManager var1) {
      this.fComponentManager = var1;
      this.fErrorReporter = (XMLErrorReporter)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
      this.fNamespaceContext = (NamespaceSupport)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context");
      this.fSchemaValidator = (XMLSchemaValidator)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema");
      this.fSymbolTable = (SymbolTable)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
      this.fValidationManager = (ValidationManager)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager");
   }

   public void validate(Source var1, Result var2) throws SAXException, IOException {
      if (!(var2 instanceof DOMResult) && var2 != null) {
         throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(Locale.getDefault(), "SourceResultMismatch", new Object[]{var1.getClass().getName(), var2.getClass().getName()}));
      } else {
         DOMSource var3 = (DOMSource)var1;
         DOMResult var4 = (DOMResult)var2;
         Node var5 = var3.getNode();
         this.fRoot = var5;
         if (var5 != null) {
            this.fComponentManager.reset();
            this.fValidationManager.setEntityState(this);
            this.fDOMNamespaceContext.reset();
            String var6 = var3.getSystemId();
            this.fXMLLocator.setLiteralSystemId(var6);
            this.fXMLLocator.setExpandedSystemId(var6);
            this.fErrorReporter.setDocumentLocator(this.fXMLLocator);

            try {
               this.setupEntityMap(var5.getNodeType() == 9 ? (Document)var5 : var5.getOwnerDocument());
               this.setupDOMResultHandler(var3, var4);
               this.fSchemaValidator.startDocument(this.fXMLLocator, (String)null, this.fDOMNamespaceContext, (Augmentations)null);
               this.validate(var5);
               this.fSchemaValidator.endDocument((Augmentations)null);
            } catch (XMLParseException var14) {
               throw Util.toSAXParseException(var14);
            } catch (XNIException var15) {
               throw Util.toSAXException(var15);
            } finally {
               this.fRoot = null;
               this.fCurrentElement = null;
               this.fEntities = null;
               if (this.fDOMValidatorHandler != null) {
                  this.fDOMValidatorHandler.setDOMResult((DOMResult)null);
               }

            }
         }

      }
   }

   public boolean isEntityDeclared(String var1) {
      return false;
   }

   public boolean isEntityUnparsed(String var1) {
      if (this.fEntities != null) {
         Entity var2 = (Entity)this.fEntities.getNamedItem(var1);
         if (var2 != null) {
            return var2.getNotationName() != null;
         }
      }

      return false;
   }

   private void validate(Node var1) {
      Node var3;
      label39:
      for(Node var2 = var1; var1 != null; var1 = var3) {
         this.beginNode(var1);
         var3 = var1.getFirstChild();

         do {
            do {
               if (var3 != null) {
                  continue label39;
               }

               this.finishNode(var1);
               if (var2 == var1) {
                  continue label39;
               }

               var3 = var1.getNextSibling();
            } while(var3 != null);

            var1 = var1.getParentNode();
         } while(var1 != null && var2 != var1);

         if (var1 != null) {
            this.finishNode(var1);
         }

         var3 = null;
      }

   }

   private void beginNode(Node var1) {
      switch (var1.getNodeType()) {
         case 1:
            this.fCurrentElement = var1;
            this.fNamespaceContext.pushContext();
            this.fillQName(this.fElementQName, var1);
            this.processAttributes(var1.getAttributes());
            this.fSchemaValidator.startElement(this.fElementQName, this.fAttributes, (Augmentations)null);
         case 2:
         case 5:
         case 6:
         case 9:
         default:
            break;
         case 3:
            if (this.fDOMValidatorHandler != null) {
               this.fDOMValidatorHandler.setIgnoringCharacters(true);
               this.sendCharactersToValidator(var1.getNodeValue());
               this.fDOMValidatorHandler.setIgnoringCharacters(false);
               this.fDOMValidatorHandler.characters((Text)var1);
            } else {
               this.sendCharactersToValidator(var1.getNodeValue());
            }
            break;
         case 4:
            if (this.fDOMValidatorHandler != null) {
               this.fDOMValidatorHandler.setIgnoringCharacters(true);
               this.fSchemaValidator.startCDATA((Augmentations)null);
               this.sendCharactersToValidator(var1.getNodeValue());
               this.fSchemaValidator.endCDATA((Augmentations)null);
               this.fDOMValidatorHandler.setIgnoringCharacters(false);
               this.fDOMValidatorHandler.cdata((CDATASection)var1);
            } else {
               this.fSchemaValidator.startCDATA((Augmentations)null);
               this.sendCharactersToValidator(var1.getNodeValue());
               this.fSchemaValidator.endCDATA((Augmentations)null);
            }
            break;
         case 7:
            if (this.fDOMValidatorHandler != null) {
               this.fDOMValidatorHandler.processingInstruction((ProcessingInstruction)var1);
            }
            break;
         case 8:
            if (this.fDOMValidatorHandler != null) {
               this.fDOMValidatorHandler.comment((Comment)var1);
            }
            break;
         case 10:
            if (this.fDOMValidatorHandler != null) {
               this.fDOMValidatorHandler.doctypeDecl((DocumentType)var1);
            }
      }

   }

   private void finishNode(Node var1) {
      if (var1.getNodeType() == 1) {
         this.fCurrentElement = var1;
         this.fillQName(this.fElementQName, var1);
         this.fSchemaValidator.endElement(this.fElementQName, (Augmentations)null);
         this.fNamespaceContext.popContext();
      }

   }

   private void setupEntityMap(Document var1) {
      if (var1 != null) {
         DocumentType var2 = var1.getDoctype();
         if (var2 != null) {
            this.fEntities = var2.getEntities();
            return;
         }
      }

      this.fEntities = null;
   }

   private void setupDOMResultHandler(DOMSource var1, DOMResult var2) throws SAXException {
      if (var2 == null) {
         this.fDOMValidatorHandler = null;
         this.fSchemaValidator.setDocumentHandler((XMLDocumentHandler)null);
      } else {
         Node var3 = var2.getNode();
         if (var1.getNode() == var3) {
            this.fDOMValidatorHandler = this.fDOMResultAugmentor;
            this.fDOMResultAugmentor.setDOMResult(var2);
            this.fSchemaValidator.setDocumentHandler(this.fDOMResultAugmentor);
         } else {
            if (var2.getNode() == null) {
               try {
                  DocumentBuilderFactory var4 = DocumentBuilderFactory.newInstance();
                  var4.setNamespaceAware(true);
                  DocumentBuilder var5 = var4.newDocumentBuilder();
                  var2.setNode(var5.newDocument());
               } catch (ParserConfigurationException var6) {
                  throw new SAXException(var6);
               }
            }

            this.fDOMValidatorHandler = this.fDOMResultBuilder;
            this.fDOMResultBuilder.setDOMResult(var2);
            this.fSchemaValidator.setDocumentHandler(this.fDOMResultBuilder);
         }
      }
   }

   private void fillQName(QName var1, Node var2) {
      String var3 = var2.getPrefix();
      String var4 = var2.getLocalName();
      String var5 = var2.getNodeName();
      String var6 = var2.getNamespaceURI();
      var1.prefix = var3 != null ? this.fSymbolTable.addSymbol(var3) : XMLSymbols.EMPTY_STRING;
      var1.localpart = var4 != null ? this.fSymbolTable.addSymbol(var4) : XMLSymbols.EMPTY_STRING;
      var1.rawname = var5 != null ? this.fSymbolTable.addSymbol(var5) : XMLSymbols.EMPTY_STRING;
      var1.uri = var6 != null && var6.length() > 0 ? this.fSymbolTable.addSymbol(var6) : null;
   }

   private void processAttributes(NamedNodeMap var1) {
      int var2 = var1.getLength();
      this.fAttributes.removeAllAttributes();

      for(int var3 = 0; var3 < var2; ++var3) {
         Attr var4 = (Attr)var1.item(var3);
         String var5 = var4.getValue();
         if (var5 == null) {
            var5 = XMLSymbols.EMPTY_STRING;
         }

         this.fillQName(this.fAttributeQName, var4);
         this.fAttributes.addAttributeNS(this.fAttributeQName, XMLSymbols.fCDATASymbol, var5);
         this.fAttributes.setSpecified(var3, var4.getSpecified());
         if (this.fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
            if (this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
               this.fNamespaceContext.declarePrefix(this.fAttributeQName.localpart, var5.length() != 0 ? this.fSymbolTable.addSymbol(var5) : null);
            } else {
               this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, var5.length() != 0 ? this.fSymbolTable.addSymbol(var5) : null);
            }
         }
      }

   }

   private void sendCharactersToValidator(String var1) {
      if (var1 != null) {
         int var2 = var1.length();
         int var3 = var2 & 1023;
         if (var3 > 0) {
            var1.getChars(0, var3, this.fCharBuffer, 0);
            this.fTempString.setValues(this.fCharBuffer, 0, var3);
            this.fSchemaValidator.characters(this.fTempString, (Augmentations)null);
         }

         int var4 = var3;

         while(var4 < var2) {
            int var10001 = var4;
            var4 += 1024;
            var1.getChars(var10001, var4, this.fCharBuffer, 0);
            this.fTempString.setValues(this.fCharBuffer, 0, 1024);
            this.fSchemaValidator.characters(this.fTempString, (Augmentations)null);
         }
      }

   }

   Node getCurrentElement() {
      return this.fCurrentElement;
   }

   final class DOMNamespaceContext implements NamespaceContext {
      protected String[] fNamespace = new String[32];
      protected int fNamespaceSize = 0;
      protected boolean fDOMContextBuilt = false;

      public void pushContext() {
         DOMValidatorHelper.this.fNamespaceContext.pushContext();
      }

      public void popContext() {
         DOMValidatorHelper.this.fNamespaceContext.popContext();
      }

      public boolean declarePrefix(String var1, String var2) {
         return DOMValidatorHelper.this.fNamespaceContext.declarePrefix(var1, var2);
      }

      public String getURI(String var1) {
         String var2 = DOMValidatorHelper.this.fNamespaceContext.getURI(var1);
         if (var2 == null) {
            if (!this.fDOMContextBuilt) {
               this.fillNamespaceContext();
               this.fDOMContextBuilt = true;
            }

            if (this.fNamespaceSize > 0 && !DOMValidatorHelper.this.fNamespaceContext.containsPrefix(var1)) {
               var2 = this.getURI0(var1);
            }
         }

         return var2;
      }

      public String getPrefix(String var1) {
         return DOMValidatorHelper.this.fNamespaceContext.getPrefix(var1);
      }

      public int getDeclaredPrefixCount() {
         return DOMValidatorHelper.this.fNamespaceContext.getDeclaredPrefixCount();
      }

      public String getDeclaredPrefixAt(int var1) {
         return DOMValidatorHelper.this.fNamespaceContext.getDeclaredPrefixAt(var1);
      }

      public Enumeration getAllPrefixes() {
         return DOMValidatorHelper.this.fNamespaceContext.getAllPrefixes();
      }

      public void reset() {
         this.fDOMContextBuilt = false;
         this.fNamespaceSize = 0;
      }

      private void fillNamespaceContext() {
         if (DOMValidatorHelper.this.fRoot != null) {
            for(Node var1 = DOMValidatorHelper.this.fRoot.getParentNode(); var1 != null; var1 = var1.getParentNode()) {
               if (1 == var1.getNodeType()) {
                  NamedNodeMap var2 = var1.getAttributes();
                  int var3 = var2.getLength();

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Attr var5 = (Attr)var2.item(var4);
                     String var6 = var5.getValue();
                     if (var6 == null) {
                        var6 = XMLSymbols.EMPTY_STRING;
                     }

                     DOMValidatorHelper.this.fillQName(DOMValidatorHelper.this.fAttributeQName, var5);
                     if (DOMValidatorHelper.this.fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                        if (DOMValidatorHelper.this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                           this.declarePrefix0(DOMValidatorHelper.this.fAttributeQName.localpart, var6.length() != 0 ? DOMValidatorHelper.this.fSymbolTable.addSymbol(var6) : null);
                        } else {
                           this.declarePrefix0(XMLSymbols.EMPTY_STRING, var6.length() != 0 ? DOMValidatorHelper.this.fSymbolTable.addSymbol(var6) : null);
                        }
                     }
                  }
               }
            }
         }

      }

      private void declarePrefix0(String var1, String var2) {
         if (this.fNamespaceSize == this.fNamespace.length) {
            String[] var3 = new String[this.fNamespaceSize * 2];
            System.arraycopy(this.fNamespace, 0, var3, 0, this.fNamespaceSize);
            this.fNamespace = var3;
         }

         this.fNamespace[this.fNamespaceSize++] = var1;
         this.fNamespace[this.fNamespaceSize++] = var2;
      }

      private String getURI0(String var1) {
         for(int var2 = 0; var2 < this.fNamespaceSize; var2 += 2) {
            if (this.fNamespace[var2] == var1) {
               return this.fNamespace[var2 + 1];
            }
         }

         return null;
      }
   }
}
