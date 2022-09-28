package org.apache.xerces.parsers;

import java.util.Locale;
import java.util.Stack;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.DocumentTypeImpl;
import org.apache.xerces.dom.ElementDefinitionImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.EntityImpl;
import org.apache.xerces.dom.EntityReferenceImpl;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.NotationImpl;
import org.apache.xerces.dom.PSVIAttrNSImpl;
import org.apache.xerces.dom.PSVIDocumentImpl;
import org.apache.xerces.dom.PSVIElementNSImpl;
import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.ls.LSParserFilter;

public class AbstractDOMParser extends AbstractXMLDocumentParser {
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String CREATE_ENTITY_REF_NODES = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
   protected static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
   protected static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
   protected static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
   protected static final String DEFER_NODE_EXPANSION = "http://apache.org/xml/features/dom/defer-node-expansion";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/dom/create-entity-ref-nodes", "http://apache.org/xml/features/include-comments", "http://apache.org/xml/features/create-cdata-nodes", "http://apache.org/xml/features/dom/include-ignorable-whitespace", "http://apache.org/xml/features/dom/defer-node-expansion"};
   protected static final String DOCUMENT_CLASS_NAME = "http://apache.org/xml/properties/dom/document-class-name";
   protected static final String CURRENT_ELEMENT_NODE = "http://apache.org/xml/properties/dom/current-element-node";
   private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/dom/document-class-name", "http://apache.org/xml/properties/dom/current-element-node"};
   protected static final String DEFAULT_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.DocumentImpl";
   protected static final String CORE_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.CoreDocumentImpl";
   protected static final String PSVI_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.PSVIDocumentImpl";
   public static final RuntimeException abort = new RuntimeException();
   private static final boolean DEBUG_EVENTS = false;
   private static final boolean DEBUG_BASEURI = false;
   protected DOMErrorHandlerWrapper fErrorHandler = null;
   protected boolean fInDTD;
   protected boolean fCreateEntityRefNodes;
   protected boolean fIncludeIgnorableWhitespace;
   protected boolean fIncludeComments;
   protected boolean fCreateCDATANodes;
   protected Document fDocument;
   protected CoreDocumentImpl fDocumentImpl;
   protected boolean fStorePSVI;
   protected String fDocumentClassName;
   protected DocumentType fDocumentType;
   protected Node fCurrentNode;
   protected CDATASection fCurrentCDATASection;
   protected EntityImpl fCurrentEntityDecl;
   protected int fDeferredEntityDecl;
   protected final StringBuffer fStringBuffer = new StringBuffer(50);
   protected StringBuffer fInternalSubset;
   protected boolean fDeferNodeExpansion;
   protected boolean fNamespaceAware;
   protected DeferredDocumentImpl fDeferredDocumentImpl;
   protected int fDocumentIndex;
   protected int fDocumentTypeIndex;
   protected int fCurrentNodeIndex;
   protected int fCurrentCDATASectionIndex;
   protected boolean fInDTDExternalSubset;
   protected QName fRoot = new QName();
   protected boolean fInCDATASection;
   protected boolean fFirstChunk = false;
   protected boolean fFilterReject = false;
   protected Stack fBaseURIStack = new Stack();
   protected final QName fRejectedElement = new QName();
   protected Stack fSkippedElemStack = null;
   protected boolean fInEntityRef = false;
   private QName fAttrQName = new QName();
   protected LSParserFilter fDOMFilter = null;
   // $FF: synthetic field
   static Class class$org$w3c$dom$Document;

   protected AbstractDOMParser(XMLParserConfiguration var1) {
      super(var1);
      super.fConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
      super.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", true);
      super.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
      super.fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
      super.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
      super.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", true);
      super.fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
      super.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "org.apache.xerces.dom.DocumentImpl");
   }

   protected String getDocumentClassName() {
      return this.fDocumentClassName;
   }

   protected void setDocumentClassName(String var1) {
      if (var1 == null) {
         var1 = "org.apache.xerces.dom.DocumentImpl";
      }

      if (!var1.equals("org.apache.xerces.dom.DocumentImpl") && !var1.equals("org.apache.xerces.dom.PSVIDocumentImpl")) {
         try {
            Class var2 = ObjectFactory.findProviderClass(var1, ObjectFactory.findClassLoader(), true);
            if (!(class$org$w3c$dom$Document == null ? (class$org$w3c$dom$Document = class$("org.w3c.dom.Document")) : class$org$w3c$dom$Document).isAssignableFrom(var2)) {
               throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidDocumentClassName", new Object[]{var1}));
            }
         } catch (ClassNotFoundException var3) {
            throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "MissingDocumentClassName", new Object[]{var1}));
         }
      }

      this.fDocumentClassName = var1;
      if (!var1.equals("org.apache.xerces.dom.DocumentImpl")) {
         this.fDeferNodeExpansion = false;
      }

   }

   public Document getDocument() {
      return this.fDocument;
   }

   public void reset() throws XNIException {
      super.reset();
      this.fCreateEntityRefNodes = super.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes");
      this.fIncludeIgnorableWhitespace = super.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace");
      this.fDeferNodeExpansion = super.fConfiguration.getFeature("http://apache.org/xml/features/dom/defer-node-expansion");
      this.fNamespaceAware = super.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
      this.fIncludeComments = super.fConfiguration.getFeature("http://apache.org/xml/features/include-comments");
      this.fCreateCDATANodes = super.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes");
      this.setDocumentClassName((String)super.fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name"));
      this.fDocument = null;
      this.fDocumentImpl = null;
      this.fStorePSVI = false;
      this.fDocumentType = null;
      this.fDocumentTypeIndex = -1;
      this.fDeferredDocumentImpl = null;
      this.fCurrentNode = null;
      this.fStringBuffer.setLength(0);
      this.fRoot.clear();
      this.fInDTD = false;
      this.fInDTDExternalSubset = false;
      this.fInCDATASection = false;
      this.fFirstChunk = false;
      this.fCurrentCDATASection = null;
      this.fCurrentCDATASectionIndex = -1;
      this.fBaseURIStack.removeAllElements();
   }

   public void setLocale(Locale var1) {
      super.fConfiguration.setLocale(var1);
   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (!this.fDeferNodeExpansion) {
         if (this.fFilterReject) {
            return;
         }

         this.setCharacterData(true);
         EntityReference var5 = this.fDocument.createEntityReference(var1);
         if (this.fDocumentImpl != null) {
            EntityReferenceImpl var6 = (EntityReferenceImpl)var5;
            var6.setBaseURI(var2.getExpandedSystemId());
            if (this.fDocumentType != null) {
               NamedNodeMap var7 = this.fDocumentType.getEntities();
               this.fCurrentEntityDecl = (EntityImpl)var7.getNamedItem(var1);
               if (this.fCurrentEntityDecl != null) {
                  this.fCurrentEntityDecl.setInputEncoding(var3);
               }
            }

            var6.needsSyncChildren(false);
         }

         this.fInEntityRef = true;
         this.fCurrentNode.appendChild(var5);
         this.fCurrentNode = var5;
      } else {
         int var9 = this.fDeferredDocumentImpl.createDeferredEntityReference(var1, var2.getExpandedSystemId());
         if (this.fDocumentTypeIndex != -1) {
            for(int var10 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false); var10 != -1; var10 = this.fDeferredDocumentImpl.getRealPrevSibling(var10, false)) {
               short var11 = this.fDeferredDocumentImpl.getNodeType(var10, false);
               if (var11 == 6) {
                  String var8 = this.fDeferredDocumentImpl.getNodeName(var10, false);
                  if (var8.equals(var1)) {
                     this.fDeferredEntityDecl = var10;
                     this.fDeferredDocumentImpl.setInputEncoding(var10, var3);
                     break;
                  }
               }
            }
         }

         this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var9);
         this.fCurrentNodeIndex = var9;
      }

   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (!this.fInDTD) {
         if (!this.fDeferNodeExpansion) {
            if (this.fCurrentEntityDecl != null && !this.fFilterReject) {
               this.fCurrentEntityDecl.setXmlEncoding(var2);
               if (var1 != null) {
                  this.fCurrentEntityDecl.setXmlVersion(var1);
               }
            }
         } else if (this.fDeferredEntityDecl != -1) {
            this.fDeferredDocumentImpl.setEntityInfo(this.fDeferredEntityDecl, var1, var2);
         }

      }
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fInDTD) {
         if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<!-- ");
            this.fInternalSubset.append(var1.toString());
            this.fInternalSubset.append(" -->");
         }

      } else if (this.fIncludeComments && !this.fFilterReject) {
         if (!this.fDeferNodeExpansion) {
            Comment var3 = this.fDocument.createComment(var1.toString());
            this.setCharacterData(false);
            this.fCurrentNode.appendChild(var3);
            if (this.fDOMFilter != null && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 128) != 0) {
               short var4 = this.fDOMFilter.acceptNode(var3);
               switch (var4) {
                  case 2:
                  case 3:
                     this.fCurrentNode.removeChild(var3);
                     this.fFirstChunk = true;
                     return;
                  case 4:
                     throw abort;
               }
            }
         } else {
            int var5 = this.fDeferredDocumentImpl.createDeferredComment(var1.toString());
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var5);
         }

      }
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (this.fInDTD) {
         if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
            this.fInternalSubset.append("<?");
            this.fInternalSubset.append(var1);
            this.fInternalSubset.append(' ');
            this.fInternalSubset.append(var2.toString());
            this.fInternalSubset.append("?>");
         }

      } else {
         if (!this.fDeferNodeExpansion) {
            if (this.fFilterReject) {
               return;
            }

            ProcessingInstruction var4 = this.fDocument.createProcessingInstruction(var1, var2.toString());
            this.setCharacterData(false);
            this.fCurrentNode.appendChild(var4);
            if (this.fDOMFilter != null && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 64) != 0) {
               short var5 = this.fDOMFilter.acceptNode(var4);
               switch (var5) {
                  case 2:
                  case 3:
                     this.fCurrentNode.removeChild(var4);
                     this.fFirstChunk = true;
                     return;
                  case 4:
                     throw abort;
               }
            }
         } else {
            int var6 = this.fDeferredDocumentImpl.createDeferredProcessingInstruction(var1, var2.toString());
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var6);
         }

      }
   }

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
      if (!this.fDeferNodeExpansion) {
         if (this.fDocumentClassName.equals("org.apache.xerces.dom.DocumentImpl")) {
            this.fDocument = new DocumentImpl();
            this.fDocumentImpl = (CoreDocumentImpl)this.fDocument;
            this.fDocumentImpl.setStrictErrorChecking(false);
            this.fDocumentImpl.setInputEncoding(var2);
            this.fDocumentImpl.setDocumentURI(var1.getExpandedSystemId());
         } else if (this.fDocumentClassName.equals("org.apache.xerces.dom.PSVIDocumentImpl")) {
            this.fDocument = new PSVIDocumentImpl();
            this.fDocumentImpl = (CoreDocumentImpl)this.fDocument;
            this.fStorePSVI = true;
            this.fDocumentImpl.setStrictErrorChecking(false);
            this.fDocumentImpl.setInputEncoding(var2);
            this.fDocumentImpl.setDocumentURI(var1.getExpandedSystemId());
         } else {
            try {
               ClassLoader var5 = ObjectFactory.findClassLoader();
               Class var6 = ObjectFactory.findProviderClass(this.fDocumentClassName, var5, true);
               this.fDocument = (Document)var6.newInstance();
               Class var7 = ObjectFactory.findProviderClass("org.apache.xerces.dom.CoreDocumentImpl", var5, true);
               if (var7.isAssignableFrom(var6)) {
                  this.fDocumentImpl = (CoreDocumentImpl)this.fDocument;
                  Class var8 = ObjectFactory.findProviderClass("org.apache.xerces.dom.PSVIDocumentImpl", var5, true);
                  if (var8.isAssignableFrom(var6)) {
                     this.fStorePSVI = true;
                  }

                  this.fDocumentImpl.setStrictErrorChecking(false);
                  this.fDocumentImpl.setInputEncoding(var2);
                  if (var1 != null) {
                     this.fDocumentImpl.setDocumentURI(var1.getExpandedSystemId());
                  }
               }
            } catch (ClassNotFoundException var9) {
            } catch (Exception var10) {
               throw new RuntimeException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "CannotCreateDocumentClass", new Object[]{this.fDocumentClassName}));
            }
         }

         this.fCurrentNode = this.fDocument;
      } else {
         this.fDeferredDocumentImpl = new DeferredDocumentImpl(this.fNamespaceAware);
         this.fDocument = this.fDeferredDocumentImpl;
         this.fDocumentIndex = this.fDeferredDocumentImpl.createDeferredDocument();
         this.fDeferredDocumentImpl.setInputEncoding(var2);
         this.fDeferredDocumentImpl.setDocumentURI(var1.getExpandedSystemId());
         this.fCurrentNodeIndex = this.fDocumentIndex;
      }

   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      if (!this.fDeferNodeExpansion) {
         if (this.fDocumentImpl != null) {
            if (var1 != null) {
               this.fDocumentImpl.setXmlVersion(var1);
            }

            this.fDocumentImpl.setXmlEncoding(var2);
            this.fDocumentImpl.setXmlStandalone("yes".equals(var3));
         }
      } else {
         if (var1 != null) {
            this.fDeferredDocumentImpl.setXmlVersion(var1);
         }

         this.fDeferredDocumentImpl.setXmlEncoding(var2);
         this.fDeferredDocumentImpl.setXmlStandalone("yes".equals(var3));
      }

   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      if (!this.fDeferNodeExpansion) {
         if (this.fDocumentImpl != null) {
            this.fDocumentType = this.fDocumentImpl.createDocumentType(var1, var2, var3);
            this.fCurrentNode.appendChild(this.fDocumentType);
         }
      } else {
         this.fDocumentTypeIndex = this.fDeferredDocumentImpl.createDeferredDocumentType(var1, var2, var3);
         this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, this.fDocumentTypeIndex);
      }

   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      int var5;
      int var6;
      int var17;
      if (!this.fDeferNodeExpansion) {
         if (this.fFilterReject) {
            return;
         }

         Element var4 = this.createElementNode(var1);
         var5 = var2.getLength();

         for(var6 = 0; var6 < var5; ++var6) {
            var2.getName(var6, this.fAttrQName);
            Attr var7 = this.createAttrNode(this.fAttrQName);
            String var8 = var2.getValue(var6);
            AttributePSVI var9 = (AttributePSVI)var2.getAugmentations(var6).getItem("ATTRIBUTE_PSVI");
            if (this.fStorePSVI && var9 != null) {
               ((PSVIAttrNSImpl)var7).setPSVI(var9);
            }

            var7.setValue(var8);
            var4.setAttributeNode(var7);
            if (this.fDocumentImpl != null) {
               AttrImpl var10 = (AttrImpl)var7;
               String var11 = null;
               boolean var12 = false;
               if (var9 != null && this.fNamespaceAware) {
                  XSSimpleTypeDefinition var22 = var9.getMemberTypeDefinition();
                  if (var22 == null) {
                     XSTypeDefinition var23 = var9.getTypeDefinition();
                     if (var23 != null) {
                        var12 = ((XSSimpleType)var23).isIDType();
                        var10.setType(var23);
                     }
                  } else {
                     var12 = ((XSSimpleType)var22).isIDType();
                     var10.setType(var22);
                  }
               } else {
                  boolean var13 = Boolean.TRUE.equals(var2.getAugmentations(var6).getItem("ATTRIBUTE_DECLARED"));
                  if (var13) {
                     var11 = var2.getType(var6);
                     var12 = "ID".equals(var11);
                  }

                  var10.setType(var11);
               }

               if (var12) {
                  ((ElementImpl)var4).setIdAttributeNode(var7, true);
               }

               var10.setSpecified(var2.isSpecified(var6));
            }
         }

         this.setCharacterData(false);
         if (var3 != null) {
            ElementPSVI var16 = (ElementPSVI)var3.getItem("ELEMENT_PSVI");
            if (var16 != null && this.fNamespaceAware) {
               Object var18 = var16.getMemberTypeDefinition();
               if (var18 == null) {
                  var18 = var16.getTypeDefinition();
               }

               ((ElementNSImpl)var4).setType((XSTypeDefinition)var18);
            }
         }

         if (this.fDOMFilter != null && !this.fInEntityRef) {
            if (this.fRoot.rawname == null) {
               this.fRoot.setValues(var1);
            } else {
               var17 = this.fDOMFilter.startElement(var4);
               switch (var17) {
                  case 2:
                     this.fFilterReject = true;
                     this.fRejectedElement.setValues(var1);
                     return;
                  case 3:
                     this.fSkippedElemStack.push(var1.clone());
                     return;
                  case 4:
                     throw abort;
               }
            }
         }

         this.fCurrentNode.appendChild(var4);
         this.fCurrentNode = var4;
      } else {
         Object var14 = null;
         if (var3 != null) {
            ElementPSVI var15 = (ElementPSVI)var3.getItem("ELEMENT_PSVI");
            if (var15 != null) {
               var14 = var15.getMemberTypeDefinition();
               if (var14 == null) {
                  var14 = var15.getTypeDefinition();
               }
            }
         }

         var5 = this.fDeferredDocumentImpl.createDeferredElement(this.fNamespaceAware ? var1.uri : null, var1.rawname, var14);
         var6 = var2.getLength();

         for(var17 = 0; var17 < var6; ++var17) {
            AttributePSVI var19 = (AttributePSVI)var2.getAugmentations(var17).getItem("ATTRIBUTE_PSVI");
            boolean var20 = false;
            if (var19 != null && this.fNamespaceAware) {
               var14 = var19.getMemberTypeDefinition();
               if (var14 == null) {
                  var14 = var19.getTypeDefinition();
                  if (var14 != null) {
                     var20 = ((XSSimpleType)var14).isIDType();
                  }
               } else {
                  var20 = ((XSSimpleType)var14).isIDType();
               }
            } else {
               boolean var21 = Boolean.TRUE.equals(var2.getAugmentations(var17).getItem("ATTRIBUTE_DECLARED"));
               if (var21) {
                  var14 = var2.getType(var17);
                  var20 = "ID".equals(var14);
               }
            }

            this.fDeferredDocumentImpl.setDeferredAttribute(var5, var2.getQName(var17), var2.getURI(var17), var2.getValue(var17), var2.isSpecified(var17), var20, var14);
         }

         this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var5);
         this.fCurrentNodeIndex = var5;
      }

   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      this.startElement(var1, var2, var3);
      this.endElement(var1, var3);
   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      String var3;
      if (!this.fDeferNodeExpansion) {
         if (this.fFilterReject) {
            return;
         }

         if (this.fInCDATASection && this.fCreateCDATANodes) {
            if (this.fCurrentCDATASection == null) {
               this.fCurrentCDATASection = this.fDocument.createCDATASection(var1.toString());
               this.fCurrentNode.appendChild(this.fCurrentCDATASection);
               this.fCurrentNode = this.fCurrentCDATASection;
            } else {
               this.fCurrentCDATASection.appendData(var1.toString());
            }
         } else if (!this.fInDTD) {
            if (var1.length == 0) {
               return;
            }

            var3 = var1.toString();
            Node var4 = this.fCurrentNode.getLastChild();
            if (var4 != null && var4.getNodeType() == 3) {
               if (this.fFirstChunk) {
                  if (this.fDocumentImpl != null) {
                     this.fStringBuffer.append(((TextImpl)var4).removeData());
                  } else {
                     this.fStringBuffer.append(((Text)var4).getData());
                     ((Text)var4).setNodeValue((String)null);
                  }

                  this.fFirstChunk = false;
               }

               this.fStringBuffer.append(var3);
            } else {
               this.fFirstChunk = true;
               Text var5 = this.fDocument.createTextNode(var3);
               this.fCurrentNode.appendChild(var5);
            }
         }
      } else if (this.fInCDATASection && this.fCreateCDATANodes) {
         int var6;
         if (this.fCurrentCDATASectionIndex == -1) {
            var6 = this.fDeferredDocumentImpl.createDeferredCDATASection(var1.toString());
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var6);
            this.fCurrentCDATASectionIndex = var6;
            this.fCurrentNodeIndex = var6;
         } else {
            var6 = this.fDeferredDocumentImpl.createDeferredTextNode(var1.toString(), false);
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var6);
         }
      } else if (!this.fInDTD) {
         if (var1.length == 0) {
            return;
         }

         var3 = var1.toString();
         int var7 = this.fDeferredDocumentImpl.createDeferredTextNode(var3, false);
         this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var7);
      }

   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fIncludeIgnorableWhitespace && !this.fFilterReject) {
         if (!this.fDeferNodeExpansion) {
            Node var3 = this.fCurrentNode.getLastChild();
            Text var4;
            if (var3 != null && var3.getNodeType() == 3) {
               var4 = (Text)var3;
               var4.appendData(var1.toString());
            } else {
               var4 = this.fDocument.createTextNode(var1.toString());
               if (this.fDocumentImpl != null) {
                  TextImpl var5 = (TextImpl)var4;
                  var5.setIgnorableWhitespace(true);
               }

               this.fCurrentNode.appendChild(var4);
            }
         } else {
            int var6 = this.fDeferredDocumentImpl.createDeferredTextNode(var1.toString(), true);
            this.fDeferredDocumentImpl.appendChild(this.fCurrentNodeIndex, var6);
         }

      }
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      if (!this.fDeferNodeExpansion) {
         if (var2 != null && this.fDocumentImpl != null && (this.fNamespaceAware || this.fStorePSVI)) {
            ElementPSVI var3 = (ElementPSVI)var2.getItem("ELEMENT_PSVI");
            if (var3 != null) {
               if (this.fNamespaceAware) {
                  Object var4 = var3.getMemberTypeDefinition();
                  if (var4 == null) {
                     var4 = var3.getTypeDefinition();
                  }

                  ((ElementNSImpl)this.fCurrentNode).setType((XSTypeDefinition)var4);
               }

               if (this.fStorePSVI) {
                  ((PSVIElementNSImpl)this.fCurrentNode).setPSVI(var3);
               }
            }
         }

         if (this.fDOMFilter != null) {
            if (this.fFilterReject) {
               if (var1.equals(this.fRejectedElement)) {
                  this.fFilterReject = false;
               }

               return;
            }

            if (!this.fSkippedElemStack.isEmpty() && this.fSkippedElemStack.peek().equals(var1)) {
               this.fSkippedElemStack.pop();
               return;
            }

            this.setCharacterData(false);
            if (!this.fRoot.equals(var1) && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 1) != 0) {
               short var8 = this.fDOMFilter.acceptNode(this.fCurrentNode);
               Node var9;
               switch (var8) {
                  case 2:
                     var9 = this.fCurrentNode.getParentNode();
                     var9.removeChild(this.fCurrentNode);
                     this.fCurrentNode = var9;
                     return;
                  case 3:
                     this.fFirstChunk = true;
                     var9 = this.fCurrentNode.getParentNode();
                     NodeList var5 = this.fCurrentNode.getChildNodes();
                     int var6 = var5.getLength();

                     for(int var7 = 0; var7 < var6; ++var7) {
                        var9.appendChild(var5.item(0));
                     }

                     var9.removeChild(this.fCurrentNode);
                     this.fCurrentNode = var9;
                     return;
                  case 4:
                     throw abort;
               }
            }

            this.fCurrentNode = this.fCurrentNode.getParentNode();
         } else {
            this.setCharacterData(false);
            this.fCurrentNode = this.fCurrentNode.getParentNode();
         }
      } else {
         this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
      }

   }

   public void startCDATA(Augmentations var1) throws XNIException {
      this.fInCDATASection = true;
      if (!this.fDeferNodeExpansion) {
         if (this.fFilterReject) {
            return;
         }

         if (this.fCreateCDATANodes) {
            this.setCharacterData(false);
         }
      }

   }

   public void endCDATA(Augmentations var1) throws XNIException {
      this.fInCDATASection = false;
      if (!this.fDeferNodeExpansion) {
         if (this.fFilterReject) {
            return;
         }

         if (this.fCurrentCDATASection != null) {
            if (this.fDOMFilter != null && !this.fInEntityRef && (this.fDOMFilter.getWhatToShow() & 8) != 0) {
               short var2 = this.fDOMFilter.acceptNode(this.fCurrentCDATASection);
               switch (var2) {
                  case 2:
                  case 3:
                     Node var3 = this.fCurrentNode.getParentNode();
                     var3.removeChild(this.fCurrentCDATASection);
                     this.fCurrentNode = var3;
                     return;
                  case 4:
                     throw abort;
               }
            }

            this.fCurrentNode = this.fCurrentNode.getParentNode();
            this.fCurrentCDATASection = null;
         }
      } else if (this.fCurrentCDATASectionIndex != -1) {
         this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
         this.fCurrentCDATASectionIndex = -1;
      }

   }

   public void endDocument(Augmentations var1) throws XNIException {
      if (!this.fDeferNodeExpansion) {
         if (this.fDocumentImpl != null) {
            this.fDocumentImpl.setStrictErrorChecking(true);
         }

         this.fCurrentNode = null;
      } else {
         this.fCurrentNodeIndex = -1;
      }

   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
      int var6;
      int var12;
      if (!this.fDeferNodeExpansion) {
         if (this.fFilterReject) {
            return;
         }

         this.setCharacterData(true);
         Node var5;
         if (this.fDocumentType != null) {
            NamedNodeMap var3 = this.fDocumentType.getEntities();
            this.fCurrentEntityDecl = (EntityImpl)var3.getNamedItem(var1);
            if (this.fCurrentEntityDecl != null) {
               if (this.fCurrentEntityDecl != null && this.fCurrentEntityDecl.getFirstChild() == null) {
                  this.fCurrentEntityDecl.setReadOnly(false, true);

                  for(Node var4 = this.fCurrentNode.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                     var5 = var4.cloneNode(true);
                     this.fCurrentEntityDecl.appendChild(var5);
                  }

                  this.fCurrentEntityDecl.setReadOnly(true, true);
               }

               this.fCurrentEntityDecl = null;
            }
         }

         this.fInEntityRef = false;
         boolean var10 = false;
         if (this.fCreateEntityRefNodes) {
            if (this.fDocumentImpl != null) {
               ((NodeImpl)this.fCurrentNode).setReadOnly(true, true);
            }

            if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 16) != 0) {
               var12 = this.fDOMFilter.acceptNode(this.fCurrentNode);
               switch (var12) {
                  case 2:
                     var5 = this.fCurrentNode.getParentNode();
                     var5.removeChild(this.fCurrentNode);
                     this.fCurrentNode = var5;
                     return;
                  case 3:
                     this.fFirstChunk = true;
                     var10 = true;
                     break;
                  case 4:
                     throw abort;
                  default:
                     this.fCurrentNode = this.fCurrentNode.getParentNode();
               }
            } else {
               this.fCurrentNode = this.fCurrentNode.getParentNode();
            }
         }

         if (!this.fCreateEntityRefNodes || var10) {
            NodeList var13 = this.fCurrentNode.getChildNodes();
            var5 = this.fCurrentNode.getParentNode();
            var6 = var13.getLength();
            if (var6 > 0) {
               Node var7 = this.fCurrentNode.getPreviousSibling();
               Node var8 = var13.item(0);
               if (var7 != null && var7.getNodeType() == 3 && var8.getNodeType() == 3) {
                  ((Text)var7).appendData(var8.getNodeValue());
                  this.fCurrentNode.removeChild(var8);
               } else {
                  var7 = var5.insertBefore(var8, this.fCurrentNode);
                  this.handleBaseURI(var7);
               }

               for(int var9 = 1; var9 < var6; ++var9) {
                  var7 = var5.insertBefore(var13.item(0), this.fCurrentNode);
                  this.handleBaseURI(var7);
               }
            }

            var5.removeChild(this.fCurrentNode);
            this.fCurrentNode = var5;
         }
      } else {
         int var11;
         if (this.fDocumentTypeIndex != -1) {
            for(var11 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false); var11 != -1; var11 = this.fDeferredDocumentImpl.getRealPrevSibling(var11, false)) {
               var12 = this.fDeferredDocumentImpl.getNodeType(var11, false);
               if (var12 == 6) {
                  String var14 = this.fDeferredDocumentImpl.getNodeName(var11, false);
                  if (var14.equals(var1)) {
                     this.fDeferredEntityDecl = var11;
                     break;
                  }
               }
            }
         }

         int var15;
         if (this.fDeferredEntityDecl != -1 && this.fDeferredDocumentImpl.getLastChild(this.fDeferredEntityDecl, false) == -1) {
            var11 = -1;

            for(var12 = this.fDeferredDocumentImpl.getLastChild(this.fCurrentNodeIndex, false); var12 != -1; var12 = this.fDeferredDocumentImpl.getRealPrevSibling(var12, false)) {
               var15 = this.fDeferredDocumentImpl.cloneNode(var12, true);
               this.fDeferredDocumentImpl.insertBefore(this.fDeferredEntityDecl, var15, var11);
               var11 = var15;
            }
         }

         if (this.fCreateEntityRefNodes) {
            this.fCurrentNodeIndex = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
         } else {
            var11 = this.fDeferredDocumentImpl.getLastChild(this.fCurrentNodeIndex, false);
            var12 = this.fDeferredDocumentImpl.getParentNode(this.fCurrentNodeIndex, false);
            var15 = this.fCurrentNodeIndex;
            var6 = var11;

            int var17;
            for(boolean var16 = true; var11 != -1; var11 = var17) {
               this.handleBaseURI(var11);
               var17 = this.fDeferredDocumentImpl.getRealPrevSibling(var11, false);
               this.fDeferredDocumentImpl.insertBefore(var12, var11, var15);
               var15 = var11;
            }

            if (var6 != -1) {
               this.fDeferredDocumentImpl.setAsLastChild(var12, var6);
            } else {
               var17 = this.fDeferredDocumentImpl.getRealPrevSibling(var15, false);
               this.fDeferredDocumentImpl.setAsLastChild(var12, var17);
            }

            this.fCurrentNodeIndex = var12;
         }

         this.fDeferredEntityDecl = -1;
      }

   }

   protected final void handleBaseURI(Node var1) {
      if (this.fDocumentImpl != null) {
         String var2 = null;
         short var3 = var1.getNodeType();
         if (var3 == 1) {
            if (this.fNamespaceAware) {
               if (((Element)var1).getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "base") != null) {
                  return;
               }
            } else if (((Element)var1).getAttributeNode("xml:base") != null) {
               return;
            }

            var2 = ((EntityReferenceImpl)this.fCurrentNode).getBaseURI();
            if (var2 != null && !var2.equals(this.fDocumentImpl.getDocumentURI())) {
               if (this.fNamespaceAware) {
                  ((Element)var1).setAttributeNS("http://www.w3.org/XML/1998/namespace", "base", var2);
               } else {
                  ((Element)var1).setAttribute("xml:base", var2);
               }
            }
         } else if (var3 == 7) {
            var2 = ((EntityReferenceImpl)this.fCurrentNode).getBaseURI();
            if (var2 != null && this.fErrorHandler != null) {
               DOMErrorImpl var4 = new DOMErrorImpl();
               var4.fType = "pi-base-uri-not-preserved";
               var4.fRelatedData = var2;
               var4.fSeverity = 1;
               this.fErrorHandler.getErrorHandler().handleError(var4);
            }
         }
      }

   }

   protected final void handleBaseURI(int var1) {
      short var2 = this.fDeferredDocumentImpl.getNodeType(var1, false);
      String var3;
      if (var2 == 1) {
         var3 = this.fDeferredDocumentImpl.getNodeValueString(this.fCurrentNodeIndex, false);
         if (var3 == null) {
            var3 = this.fDeferredDocumentImpl.getDeferredEntityBaseURI(this.fDeferredEntityDecl);
         }

         if (var3 != null && !var3.equals(this.fDeferredDocumentImpl.getDocumentURI())) {
            this.fDeferredDocumentImpl.setDeferredAttribute(var1, "xml:base", "http://www.w3.org/XML/1998/namespace", var3, true);
         }
      } else if (var2 == 7) {
         var3 = this.fDeferredDocumentImpl.getNodeValueString(this.fCurrentNodeIndex, false);
         if (var3 == null) {
            var3 = this.fDeferredDocumentImpl.getDeferredEntityBaseURI(this.fDeferredEntityDecl);
         }

         if (var3 != null && this.fErrorHandler != null) {
            DOMErrorImpl var4 = new DOMErrorImpl();
            var4.fType = "pi-base-uri-not-preserved";
            var4.fRelatedData = var3;
            var4.fSeverity = 1;
            this.fErrorHandler.getErrorHandler().handleError(var4);
         }
      }

   }

   public void startDTD(XMLLocator var1, Augmentations var2) throws XNIException {
      this.fInDTD = true;
      if (var1 != null) {
         this.fBaseURIStack.push(var1.getBaseSystemId());
      }

      if (this.fDeferNodeExpansion || this.fDocumentImpl != null) {
         this.fInternalSubset = new StringBuffer(1024);
      }

   }

   public void endDTD(Augmentations var1) throws XNIException {
      this.fInDTD = false;
      if (!this.fBaseURIStack.isEmpty()) {
         this.fBaseURIStack.pop();
      }

      String var2 = this.fInternalSubset != null && this.fInternalSubset.length() > 0 ? this.fInternalSubset.toString() : null;
      if (this.fDeferNodeExpansion) {
         if (var2 != null) {
            this.fDeferredDocumentImpl.setInternalSubset(this.fDocumentTypeIndex, var2);
         }
      } else if (this.fDocumentImpl != null && var2 != null) {
         ((DocumentTypeImpl)this.fDocumentType).setInternalSubset(var2);
      }

   }

   public void startConditional(short var1, Augmentations var2) throws XNIException {
   }

   public void endConditional(Augmentations var1) throws XNIException {
   }

   public void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException {
      this.fBaseURIStack.push(var1.getBaseSystemId());
      this.fInDTDExternalSubset = true;
   }

   public void endExternalSubset(Augmentations var1) throws XNIException {
      this.fInDTDExternalSubset = false;
      this.fBaseURIStack.pop();
   }

   public void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException {
      if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
         this.fInternalSubset.append("<!ENTITY ");
         if (var1.startsWith("%")) {
            this.fInternalSubset.append("% ");
            this.fInternalSubset.append(var1.substring(1));
         } else {
            this.fInternalSubset.append(var1);
         }

         this.fInternalSubset.append(' ');
         String var5 = var3.toString();
         boolean var6 = var5.indexOf(39) == -1;
         this.fInternalSubset.append((char)(var6 ? '\'' : '"'));
         this.fInternalSubset.append(var5);
         this.fInternalSubset.append((char)(var6 ? '\'' : '"'));
         this.fInternalSubset.append(">\n");
      }

      if (!var1.startsWith("%")) {
         if (this.fDocumentType != null) {
            NamedNodeMap var9 = this.fDocumentType.getEntities();
            EntityImpl var11 = (EntityImpl)var9.getNamedItem(var1);
            if (var11 == null) {
               var11 = (EntityImpl)this.fDocumentImpl.createEntity(var1);
               var11.setBaseURI((String)this.fBaseURIStack.peek());
               var9.setNamedItem(var11);
            }
         }

         if (this.fDocumentTypeIndex != -1) {
            boolean var10 = false;

            int var7;
            for(int var12 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false); var12 != -1; var12 = this.fDeferredDocumentImpl.getRealPrevSibling(var12, false)) {
               var7 = this.fDeferredDocumentImpl.getNodeType(var12, false);
               if (var7 == 6) {
                  String var8 = this.fDeferredDocumentImpl.getNodeName(var12, false);
                  if (var8.equals(var1)) {
                     var10 = true;
                     break;
                  }
               }
            }

            if (!var10) {
               var7 = this.fDeferredDocumentImpl.createDeferredEntity(var1, (String)null, (String)null, (String)null, (String)this.fBaseURIStack.peek());
               this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, var7);
            }
         }

      }
   }

   public void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      String var4 = var2.getPublicId();
      String var5 = var2.getLiteralSystemId();
      if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
         this.fInternalSubset.append("<!ENTITY ");
         if (var1.startsWith("%")) {
            this.fInternalSubset.append("% ");
            this.fInternalSubset.append(var1.substring(1));
         } else {
            this.fInternalSubset.append(var1);
         }

         this.fInternalSubset.append(' ');
         if (var4 != null) {
            this.fInternalSubset.append("PUBLIC '");
            this.fInternalSubset.append(var4);
            this.fInternalSubset.append("' '");
         } else {
            this.fInternalSubset.append("SYSTEM '");
         }

         this.fInternalSubset.append(var5);
         this.fInternalSubset.append("'>\n");
      }

      if (!var1.startsWith("%")) {
         if (this.fDocumentType != null) {
            NamedNodeMap var6 = this.fDocumentType.getEntities();
            EntityImpl var7 = (EntityImpl)var6.getNamedItem(var1);
            if (var7 == null) {
               var7 = (EntityImpl)this.fDocumentImpl.createEntity(var1);
               var7.setPublicId(var4);
               var7.setSystemId(var5);
               var7.setBaseURI(var2.getBaseSystemId());
               var6.setNamedItem(var7);
            }
         }

         if (this.fDocumentTypeIndex != -1) {
            boolean var10 = false;

            int var8;
            for(int var11 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false); var11 != -1; var11 = this.fDeferredDocumentImpl.getRealPrevSibling(var11, false)) {
               var8 = this.fDeferredDocumentImpl.getNodeType(var11, false);
               if (var8 == 6) {
                  String var9 = this.fDeferredDocumentImpl.getNodeName(var11, false);
                  if (var9.equals(var1)) {
                     var10 = true;
                     break;
                  }
               }
            }

            if (!var10) {
               var8 = this.fDeferredDocumentImpl.createDeferredEntity(var1, var4, var5, (String)null, var2.getBaseSystemId());
               this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, var8);
            }
         }

      }
   }

   public void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      this.fBaseURIStack.push(var2.getExpandedSystemId());
   }

   public void endParameterEntity(String var1, Augmentations var2) throws XNIException {
      this.fBaseURIStack.pop();
   }

   public void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      String var5 = var2.getPublicId();
      String var6 = var2.getLiteralSystemId();
      if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
         this.fInternalSubset.append("<!ENTITY ");
         this.fInternalSubset.append(var1);
         this.fInternalSubset.append(' ');
         if (var5 != null) {
            this.fInternalSubset.append("PUBLIC '");
            this.fInternalSubset.append(var5);
            if (var6 != null) {
               this.fInternalSubset.append("' '");
               this.fInternalSubset.append(var6);
            }
         } else {
            this.fInternalSubset.append("SYSTEM '");
            this.fInternalSubset.append(var6);
         }

         this.fInternalSubset.append("' NDATA ");
         this.fInternalSubset.append(var3);
         this.fInternalSubset.append(">\n");
      }

      if (this.fDocumentType != null) {
         NamedNodeMap var7 = this.fDocumentType.getEntities();
         EntityImpl var8 = (EntityImpl)var7.getNamedItem(var1);
         if (var8 == null) {
            var8 = (EntityImpl)this.fDocumentImpl.createEntity(var1);
            var8.setPublicId(var5);
            var8.setSystemId(var6);
            var8.setNotationName(var3);
            var8.setBaseURI(var2.getBaseSystemId());
            var7.setNamedItem(var8);
         }
      }

      if (this.fDocumentTypeIndex != -1) {
         boolean var11 = false;

         int var9;
         for(int var12 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false); var12 != -1; var12 = this.fDeferredDocumentImpl.getRealPrevSibling(var12, false)) {
            var9 = this.fDeferredDocumentImpl.getNodeType(var12, false);
            if (var9 == 6) {
               String var10 = this.fDeferredDocumentImpl.getNodeName(var12, false);
               if (var10.equals(var1)) {
                  var11 = true;
                  break;
               }
            }
         }

         if (!var11) {
            var9 = this.fDeferredDocumentImpl.createDeferredEntity(var1, var5, var6, var3, var2.getBaseSystemId());
            this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, var9);
         }
      }

   }

   public void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      String var4 = var2.getPublicId();
      String var5 = var2.getLiteralSystemId();
      if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
         this.fInternalSubset.append("<!NOTATION ");
         this.fInternalSubset.append(var1);
         if (var4 != null) {
            this.fInternalSubset.append(" PUBLIC '");
            this.fInternalSubset.append(var4);
            if (var5 != null) {
               this.fInternalSubset.append("' '");
               this.fInternalSubset.append(var5);
            }
         } else {
            this.fInternalSubset.append(" SYSTEM '");
            this.fInternalSubset.append(var5);
         }

         this.fInternalSubset.append("'>\n");
      }

      if (this.fDocumentImpl != null && this.fDocumentType != null) {
         NamedNodeMap var6 = this.fDocumentType.getNotations();
         if (var6.getNamedItem(var1) == null) {
            NotationImpl var7 = (NotationImpl)this.fDocumentImpl.createNotation(var1);
            var7.setPublicId(var4);
            var7.setSystemId(var5);
            var7.setBaseURI(var2.getBaseSystemId());
            var6.setNamedItem(var7);
         }
      }

      if (this.fDocumentTypeIndex != -1) {
         boolean var10 = false;

         int var8;
         for(int var11 = this.fDeferredDocumentImpl.getLastChild(this.fDocumentTypeIndex, false); var11 != -1; var11 = this.fDeferredDocumentImpl.getPrevSibling(var11, false)) {
            var8 = this.fDeferredDocumentImpl.getNodeType(var11, false);
            if (var8 == 12) {
               String var9 = this.fDeferredDocumentImpl.getNodeName(var11, false);
               if (var9.equals(var1)) {
                  var10 = true;
                  break;
               }
            }
         }

         if (!var10) {
            var8 = this.fDeferredDocumentImpl.createDeferredNotation(var1, var4, var5, var2.getBaseSystemId());
            this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, var8);
         }
      }

   }

   public void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void elementDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
         this.fInternalSubset.append("<!ELEMENT ");
         this.fInternalSubset.append(var1);
         this.fInternalSubset.append(' ');
         this.fInternalSubset.append(var2);
         this.fInternalSubset.append(">\n");
      }

   }

   public void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException {
      int var9;
      int var10;
      if (this.fInternalSubset != null && !this.fInDTDExternalSubset) {
         this.fInternalSubset.append("<!ATTLIST ");
         this.fInternalSubset.append(var1);
         this.fInternalSubset.append(' ');
         this.fInternalSubset.append(var2);
         this.fInternalSubset.append(' ');
         if (!var3.equals("ENUMERATION")) {
            this.fInternalSubset.append(var3);
         } else {
            this.fInternalSubset.append('(');

            for(var9 = 0; var9 < var4.length; ++var9) {
               if (var9 > 0) {
                  this.fInternalSubset.append('|');
               }

               this.fInternalSubset.append(var4[var9]);
            }

            this.fInternalSubset.append(')');
         }

         if (var5 != null) {
            this.fInternalSubset.append(' ');
            this.fInternalSubset.append(var5);
         }

         if (var6 != null) {
            this.fInternalSubset.append(" '");

            for(var9 = 0; var9 < var6.length; ++var9) {
               var10 = var6.ch[var6.offset + var9];
               if (var10 == 39) {
                  this.fInternalSubset.append("&apos;");
               } else {
                  this.fInternalSubset.append((char)var10);
               }
            }

            this.fInternalSubset.append('\'');
         }

         this.fInternalSubset.append(">\n");
      }

      if (this.fDeferredDocumentImpl != null) {
         if (var6 != null) {
            var9 = this.fDeferredDocumentImpl.lookupElementDefinition(var1);
            if (var9 == -1) {
               var9 = this.fDeferredDocumentImpl.createDeferredElementDefinition(var1);
               this.fDeferredDocumentImpl.appendChild(this.fDocumentTypeIndex, var9);
            }

            var10 = this.fDeferredDocumentImpl.createDeferredAttribute(var2, var6.toString(), false);
            if ("ID".equals(var3)) {
               this.fDeferredDocumentImpl.setIdAttribute(var10);
            }

            this.fDeferredDocumentImpl.appendChild(var9, var10);
         }
      } else if (this.fDocumentImpl != null && var6 != null) {
         NamedNodeMap var15 = ((DocumentTypeImpl)this.fDocumentType).getElements();
         ElementDefinitionImpl var14 = (ElementDefinitionImpl)var15.getNamedItem(var1);
         if (var14 == null) {
            var14 = this.fDocumentImpl.createElementDefinition(var1);
            ((DocumentTypeImpl)this.fDocumentType).getElements().setNamedItem(var14);
         }

         boolean var11 = this.fNamespaceAware;
         AttrImpl var12;
         if (!var11) {
            var12 = (AttrImpl)this.fDocumentImpl.createAttribute(var2);
         } else {
            String var13 = null;
            if (var2.startsWith("xmlns:") || var2.equals("xmlns")) {
               var13 = NamespaceContext.XMLNS_URI;
            }

            var12 = (AttrImpl)this.fDocumentImpl.createAttributeNS(var13, var2);
         }

         var12.setValue(var6.toString());
         var12.setSpecified(false);
         var12.setIdAttribute("ID".equals(var3));
         if (var11) {
            var14.getAttributes().setNamedItemNS(var12);
         } else {
            var14.getAttributes().setNamedItem(var12);
         }
      }

   }

   public void startAttlist(String var1, Augmentations var2) throws XNIException {
   }

   public void endAttlist(Augmentations var1) throws XNIException {
   }

   protected Element createElementNode(QName var1) {
      Element var2 = null;
      if (this.fNamespaceAware) {
         if (this.fDocumentImpl != null) {
            var2 = this.fDocumentImpl.createElementNS(var1.uri, var1.rawname, var1.localpart);
         } else {
            var2 = this.fDocument.createElementNS(var1.uri, var1.rawname);
         }
      } else {
         var2 = this.fDocument.createElement(var1.rawname);
      }

      return var2;
   }

   protected Attr createAttrNode(QName var1) {
      Attr var2 = null;
      if (this.fNamespaceAware) {
         if (this.fDocumentImpl != null) {
            var2 = this.fDocumentImpl.createAttributeNS(var1.uri, var1.rawname, var1.localpart);
         } else {
            var2 = this.fDocument.createAttributeNS(var1.uri, var1.rawname);
         }
      } else {
         var2 = this.fDocument.createAttribute(var1.rawname);
      }

      return var2;
   }

   protected void setCharacterData(boolean var1) {
      this.fFirstChunk = var1;
      Node var2 = this.fCurrentNode.getLastChild();
      if (var2 != null) {
         if (this.fStringBuffer.length() > 0) {
            if (var2.getNodeType() == 3) {
               if (this.fDocumentImpl != null) {
                  ((TextImpl)var2).replaceData(this.fStringBuffer.toString());
               } else {
                  ((Text)var2).setData(this.fStringBuffer.toString());
               }
            }

            this.fStringBuffer.setLength(0);
         }

         if (this.fDOMFilter != null && !this.fInEntityRef && var2.getNodeType() == 3 && (this.fDOMFilter.getWhatToShow() & 4) != 0) {
            short var3 = this.fDOMFilter.acceptNode(var2);
            switch (var3) {
               case 2:
               case 3:
                  this.fCurrentNode.removeChild(var2);
                  return;
               case 4:
                  throw abort;
            }
         }
      }

   }

   public void abort() {
      throw abort;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
