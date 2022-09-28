package org.apache.xerces.dom;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class CoreDocumentImpl extends ParentNode implements Document {
   static final long serialVersionUID = 0L;
   protected DocumentTypeImpl docType;
   protected ElementImpl docElement;
   transient NodeListCache fFreeNLCache;
   protected String encoding;
   protected String actualEncoding;
   protected String version;
   protected boolean standalone;
   protected String fDocumentURI;
   protected Hashtable userData;
   protected Hashtable identifiers;
   transient DOMNormalizer domNormalizer;
   transient DOMConfigurationImpl fConfiguration;
   transient Object fXPathEvaluator;
   private static final int[] kidOK = new int[13];
   protected int changes;
   protected boolean allowGrammarAccess;
   protected boolean errorChecking;
   protected boolean xmlVersionChanged;
   private int documentNumber;
   private int nodeCounter;
   private Hashtable nodeTable;
   private boolean xml11Version;
   // $FF: synthetic field
   static Class class$org$w3c$dom$Document;

   public CoreDocumentImpl() {
      this(false);
   }

   public CoreDocumentImpl(boolean var1) {
      super((CoreDocumentImpl)null);
      this.domNormalizer = null;
      this.fConfiguration = null;
      this.fXPathEvaluator = null;
      this.changes = 0;
      this.errorChecking = true;
      this.xmlVersionChanged = false;
      this.documentNumber = 0;
      this.nodeCounter = 0;
      this.xml11Version = false;
      super.ownerDocument = this;
      this.allowGrammarAccess = var1;
   }

   public CoreDocumentImpl(DocumentType var1) {
      this(var1, false);
   }

   public CoreDocumentImpl(DocumentType var1, boolean var2) {
      this(var2);
      if (var1 != null) {
         DocumentTypeImpl var3;
         try {
            var3 = (DocumentTypeImpl)var1;
         } catch (ClassCastException var6) {
            String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
            throw new DOMException((short)4, var5);
         }

         var3.ownerDocument = this;
         this.appendChild(var1);
      }

   }

   public final Document getOwnerDocument() {
      return null;
   }

   public short getNodeType() {
      return 9;
   }

   public String getNodeName() {
      return "#document";
   }

   public Node cloneNode(boolean var1) {
      CoreDocumentImpl var2 = new CoreDocumentImpl();
      this.callUserDataHandlers(this, var2, (short)1);
      this.cloneNode(var2, var1);
      return var2;
   }

   protected void cloneNode(CoreDocumentImpl var1, boolean var2) {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      if (var2) {
         Hashtable var3 = null;
         if (this.identifiers != null) {
            var3 = new Hashtable();
            Enumeration var4 = this.identifiers.keys();

            while(var4.hasMoreElements()) {
               Object var5 = var4.nextElement();
               var3.put(this.identifiers.get(var5), var5);
            }
         }

         for(ChildNode var6 = super.firstChild; var6 != null; var6 = var6.nextSibling) {
            var1.appendChild(var1.importNode(var6, true, true, var3));
         }
      }

      var1.allowGrammarAccess = this.allowGrammarAccess;
      var1.errorChecking = this.errorChecking;
   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      short var3 = var1.getNodeType();
      if (!this.errorChecking || (var3 != 1 || this.docElement == null) && (var3 != 10 || this.docType == null)) {
         if (var1.getOwnerDocument() == null && var1 instanceof DocumentTypeImpl) {
            ((DocumentTypeImpl)var1).ownerDocument = this;
         }

         super.insertBefore(var1, var2);
         if (var3 == 1) {
            this.docElement = (ElementImpl)var1;
         } else if (var3 == 10) {
            this.docType = (DocumentTypeImpl)var1;
         }

         return var1;
      } else {
         String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null);
         throw new DOMException((short)3, var4);
      }
   }

   public Node removeChild(Node var1) throws DOMException {
      super.removeChild(var1);
      short var2 = var1.getNodeType();
      if (var2 == 1) {
         this.docElement = null;
      } else if (var2 == 10) {
         this.docType = null;
      }

      return var1;
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      if (var1.getOwnerDocument() == null && var1 instanceof DocumentTypeImpl) {
         ((DocumentTypeImpl)var1).ownerDocument = this;
      }

      if (this.errorChecking && (this.docType != null && var2.getNodeType() != 10 && var1.getNodeType() == 10 || this.docElement != null && var2.getNodeType() != 1 && var1.getNodeType() == 1)) {
         throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null));
      } else {
         super.replaceChild(var1, var2);
         short var3 = var2.getNodeType();
         if (var3 == 1) {
            this.docElement = (ElementImpl)var1;
         } else if (var3 == 10) {
            this.docType = (DocumentTypeImpl)var1;
         }

         return var2;
      }
   }

   public String getTextContent() throws DOMException {
      return null;
   }

   public void setTextContent(String var1) throws DOMException {
   }

   public Object getFeature(String var1, String var2) {
      boolean var3 = var2 == null || var2.length() == 0;
      if (var1.equalsIgnoreCase("+XPath") && (var3 || var2.equals("3.0"))) {
         if (this.fXPathEvaluator != null) {
            return this.fXPathEvaluator;
         } else {
            try {
               Class var4 = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
               Constructor var5 = var4.getConstructor(class$org$w3c$dom$Document == null ? (class$org$w3c$dom$Document = class$("org.w3c.dom.Document")) : class$org$w3c$dom$Document);
               Class[] var6 = var4.getInterfaces();

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  if (var6[var7].getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
                     this.fXPathEvaluator = var5.newInstance(this);
                     return this.fXPathEvaluator;
                  }
               }

               return null;
            } catch (Exception var8) {
               return null;
            }
         }
      } else {
         return super.getFeature(var1, var2);
      }
   }

   public Attr createAttribute(String var1) throws DOMException {
      if (this.errorChecking && !isXMLName(var1, this.xml11Version)) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
         throw new DOMException((short)5, var2);
      } else {
         return new AttrImpl(this, var1);
      }
   }

   public CDATASection createCDATASection(String var1) throws DOMException {
      return new CDATASectionImpl(this, var1);
   }

   public Comment createComment(String var1) {
      return new CommentImpl(this, var1);
   }

   public DocumentFragment createDocumentFragment() {
      return new DocumentFragmentImpl(this);
   }

   public Element createElement(String var1) throws DOMException {
      if (this.errorChecking && !isXMLName(var1, this.xml11Version)) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
         throw new DOMException((short)5, var2);
      } else {
         return new ElementImpl(this, var1);
      }
   }

   public EntityReference createEntityReference(String var1) throws DOMException {
      if (this.errorChecking && !isXMLName(var1, this.xml11Version)) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
         throw new DOMException((short)5, var2);
      } else {
         return new EntityReferenceImpl(this, var1);
      }
   }

   public ProcessingInstruction createProcessingInstruction(String var1, String var2) throws DOMException {
      if (this.errorChecking && !isXMLName(var1, this.xml11Version)) {
         String var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
         throw new DOMException((short)5, var3);
      } else {
         return new ProcessingInstructionImpl(this, var1, var2);
      }
   }

   public Text createTextNode(String var1) {
      return new TextImpl(this, var1);
   }

   public DocumentType getDoctype() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.docType;
   }

   public Element getDocumentElement() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.docElement;
   }

   public NodeList getElementsByTagName(String var1) {
      return new DeepNodeListImpl(this, var1);
   }

   public DOMImplementation getImplementation() {
      return CoreDOMImplementationImpl.getDOMImplementation();
   }

   public void setErrorChecking(boolean var1) {
      this.errorChecking = var1;
   }

   public void setStrictErrorChecking(boolean var1) {
      this.errorChecking = var1;
   }

   public boolean getErrorChecking() {
      return this.errorChecking;
   }

   public boolean getStrictErrorChecking() {
      return this.errorChecking;
   }

   public String getInputEncoding() {
      return this.actualEncoding;
   }

   public void setInputEncoding(String var1) {
      this.actualEncoding = var1;
   }

   public void setXmlEncoding(String var1) {
      this.encoding = var1;
   }

   /** @deprecated */
   public void setEncoding(String var1) {
      this.setXmlEncoding(var1);
   }

   public String getXmlEncoding() {
      return this.encoding;
   }

   /** @deprecated */
   public String getEncoding() {
      return this.getXmlEncoding();
   }

   public void setXmlVersion(String var1) {
      if (!var1.equals("1.0") && !var1.equals("1.1")) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
         throw new DOMException((short)9, var2);
      } else {
         if (!this.getXmlVersion().equals(var1)) {
            this.xmlVersionChanged = true;
            this.isNormalized(false);
            this.version = var1;
         }

         if (this.getXmlVersion().equals("1.1")) {
            this.xml11Version = true;
         } else {
            this.xml11Version = false;
         }

      }
   }

   /** @deprecated */
   public void setVersion(String var1) {
      this.setXmlVersion(var1);
   }

   public String getXmlVersion() {
      return this.version == null ? "1.0" : this.version;
   }

   /** @deprecated */
   public String getVersion() {
      return this.getXmlVersion();
   }

   public void setXmlStandalone(boolean var1) throws DOMException {
      this.standalone = var1;
   }

   /** @deprecated */
   public void setStandalone(boolean var1) {
      this.setXmlStandalone(var1);
   }

   public boolean getXmlStandalone() {
      return this.standalone;
   }

   /** @deprecated */
   public boolean getStandalone() {
      return this.getXmlStandalone();
   }

   public String getDocumentURI() {
      return this.fDocumentURI;
   }

   public Node renameNode(Node var1, String var2, String var3) throws DOMException {
      String var12;
      if (this.errorChecking && var1.getOwnerDocument() != this && var1 != this) {
         var12 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
         throw new DOMException((short)4, var12);
      } else {
         Object var4;
         Node var8;
         switch (var1.getNodeType()) {
            case 1:
               var4 = (ElementImpl)var1;
               if (var4 instanceof ElementNSImpl) {
                  ((ElementNSImpl)var4).rename(var2, var3);
                  this.callUserDataHandlers((Node)var4, (Node)null, (short)4);
               } else if (var2 == null) {
                  if (this.errorChecking) {
                     int var10 = var3.indexOf(58);
                     String var13;
                     if (var10 != -1) {
                        var13 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
                        throw new DOMException((short)14, var13);
                     }

                     if (!isXMLName(var3, this.xml11Version)) {
                        var13 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
                        throw new DOMException((short)5, var13);
                     }
                  }

                  ((ElementImpl)var4).rename(var3);
                  this.callUserDataHandlers((Node)var4, (Node)null, (short)4);
               } else {
                  ElementNSImpl var11 = new ElementNSImpl(this, var2, var3);
                  this.copyEventListeners((NodeImpl)var4, var11);
                  Hashtable var14 = this.removeUserDataTable((Node)var4);
                  Node var15 = ((ChildNode)var4).getParentNode();
                  var8 = ((ChildNode)var4).getNextSibling();
                  if (var15 != null) {
                     var15.removeChild((Node)var4);
                  }

                  for(Node var9 = ((ParentNode)var4).getFirstChild(); var9 != null; var9 = ((ParentNode)var4).getFirstChild()) {
                     ((ParentNode)var4).removeChild(var9);
                     var11.appendChild(var9);
                  }

                  var11.moveSpecifiedAttributes((ElementImpl)var4);
                  this.setUserDataTable(var11, var14);
                  this.callUserDataHandlers((Node)var4, var11, (short)4);
                  if (var15 != null) {
                     var15.insertBefore(var11, var8);
                  }

                  var4 = var11;
               }

               this.renamedElement((Element)var1, (Element)var4);
               return (Node)var4;
            case 2:
               var4 = (AttrImpl)var1;
               Element var5 = ((AttrImpl)var4).getOwnerElement();
               if (var5 != null) {
                  var5.removeAttributeNode((Attr)var4);
               }

               if (var1 instanceof AttrNSImpl) {
                  ((AttrNSImpl)var4).rename(var2, var3);
                  if (var5 != null) {
                     var5.setAttributeNodeNS((Attr)var4);
                  }

                  this.callUserDataHandlers((Node)var4, (Node)null, (short)4);
               } else if (var2 == null) {
                  ((AttrImpl)var4).rename(var3);
                  if (var5 != null) {
                     var5.setAttributeNode((Attr)var4);
                  }

                  this.callUserDataHandlers((Node)var4, (Node)null, (short)4);
               } else {
                  AttrNSImpl var6 = new AttrNSImpl(this, var2, var3);
                  this.copyEventListeners((NodeImpl)var4, var6);
                  Hashtable var7 = this.removeUserDataTable((Node)var4);

                  for(var8 = ((AttrImpl)var4).getFirstChild(); var8 != null; var8 = ((AttrImpl)var4).getFirstChild()) {
                     ((AttrImpl)var4).removeChild(var8);
                     var6.appendChild(var8);
                  }

                  this.setUserDataTable(var6, var7);
                  this.callUserDataHandlers((Node)var4, var6, (short)4);
                  if (var5 != null) {
                     var5.setAttributeNode(var6);
                  }

                  var4 = var6;
               }

               this.renamedAttrNode((Attr)var1, (Attr)var4);
               return (Node)var4;
            default:
               var12 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
               throw new DOMException((short)9, var12);
         }
      }
   }

   public void normalizeDocument() {
      if (!this.isNormalized() || this.isNormalizeDocRequired()) {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         if (this.domNormalizer == null) {
            this.domNormalizer = new DOMNormalizer();
         }

         if (this.fConfiguration == null) {
            this.fConfiguration = new DOMConfigurationImpl();
         } else {
            this.fConfiguration.reset();
         }

         this.domNormalizer.normalizeDocument(this, this.fConfiguration);
         this.isNormalized(true);
         this.xmlVersionChanged = false;
      }
   }

   public DOMConfiguration getDomConfig() {
      if (this.fConfiguration == null) {
         this.fConfiguration = new DOMConfigurationImpl();
      }

      return this.fConfiguration;
   }

   public String getBaseURI() {
      if (this.fDocumentURI != null && this.fDocumentURI.length() != 0) {
         try {
            return (new URI(this.fDocumentURI)).toString();
         } catch (URI.MalformedURIException var2) {
            return null;
         }
      } else {
         return this.fDocumentURI;
      }
   }

   public void setDocumentURI(String var1) {
      this.fDocumentURI = var1;
   }

   public boolean getAsync() {
      return false;
   }

   public void setAsync(boolean var1) {
      if (var1) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
         throw new DOMException((short)9, var2);
      }
   }

   public void abort() {
   }

   public boolean load(String var1) {
      return false;
   }

   public boolean loadXML(String var1) {
      return false;
   }

   public String saveXML(Node var1) throws DOMException {
      if (this.errorChecking && var1 != null && this != ((Node)var1).getOwnerDocument()) {
         String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
         throw new DOMException((short)4, var4);
      } else {
         DOMImplementationLS var2 = (DOMImplementationLS)DOMImplementationImpl.getDOMImplementation();
         LSSerializer var3 = var2.createLSSerializer();
         if (var1 == null) {
            var1 = this;
         }

         return var3.writeToString((Node)var1);
      }
   }

   void setMutationEvents(boolean var1) {
   }

   boolean getMutationEvents() {
      return false;
   }

   public DocumentType createDocumentType(String var1, String var2, String var3) throws DOMException {
      return new DocumentTypeImpl(this, var1, var2, var3);
   }

   public Entity createEntity(String var1) throws DOMException {
      if (this.errorChecking && !isXMLName(var1, this.xml11Version)) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
         throw new DOMException((short)5, var2);
      } else {
         return new EntityImpl(this, var1);
      }
   }

   public Notation createNotation(String var1) throws DOMException {
      if (this.errorChecking && !isXMLName(var1, this.xml11Version)) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
         throw new DOMException((short)5, var2);
      } else {
         return new NotationImpl(this, var1);
      }
   }

   public ElementDefinitionImpl createElementDefinition(String var1) throws DOMException {
      if (this.errorChecking && !isXMLName(var1, this.xml11Version)) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
         throw new DOMException((short)5, var2);
      } else {
         return new ElementDefinitionImpl(this, var1);
      }
   }

   protected int getNodeNumber() {
      if (this.documentNumber == 0) {
         CoreDOMImplementationImpl var1 = (CoreDOMImplementationImpl)CoreDOMImplementationImpl.getDOMImplementation();
         this.documentNumber = var1.assignDocumentNumber();
      }

      return this.documentNumber;
   }

   protected int getNodeNumber(Node var1) {
      int var2;
      if (this.nodeTable == null) {
         this.nodeTable = new Hashtable();
         var2 = --this.nodeCounter;
         this.nodeTable.put(var1, new Integer(var2));
      } else {
         Integer var3 = (Integer)this.nodeTable.get(var1);
         if (var3 == null) {
            var2 = --this.nodeCounter;
            this.nodeTable.put(var1, new Integer(var2));
         } else {
            var2 = var3;
         }
      }

      return var2;
   }

   public Node importNode(Node var1, boolean var2) throws DOMException {
      return this.importNode(var1, var2, false, (Hashtable)null);
   }

   private Node importNode(Node var1, boolean var2, boolean var3, Hashtable var4) throws DOMException {
      Object var5 = null;
      Hashtable var6 = null;
      if (var1 instanceof NodeImpl) {
         var6 = ((NodeImpl)var1).getUserDataRecord();
      }

      short var7 = var1.getNodeType();
      NamedNodeMap var10;
      int var12;
      String var16;
      switch (var7) {
         case 1:
            boolean var23 = var1.getOwnerDocument().getImplementation().hasFeature("XML", "2.0");
            Element var21;
            if (var23 && var1.getLocalName() != null) {
               var21 = this.createElementNS(var1.getNamespaceURI(), var1.getNodeName());
            } else {
               var21 = this.createElement(var1.getNodeName());
            }

            var10 = var1.getAttributes();
            if (var10 != null) {
               int var24 = var10.getLength();

               for(var12 = 0; var12 < var24; ++var12) {
                  Attr var13 = (Attr)var10.item(var12);
                  if (var13.getSpecified() || var3) {
                     Attr var14 = (Attr)this.importNode(var13, true, var3, var4);
                     if (var23 && var13.getLocalName() != null) {
                        var21.setAttributeNodeNS(var14);
                     } else {
                        var21.setAttributeNode(var14);
                     }
                  }
               }
            }

            if (var4 != null) {
               Object var26 = var4.get(var1);
               if (var26 != null) {
                  if (this.identifiers == null) {
                     this.identifiers = new Hashtable();
                  }

                  this.identifiers.put(var26, var21);
               }
            }

            var5 = var21;
            break;
         case 2:
            if (var1.getOwnerDocument().getImplementation().hasFeature("XML", "2.0")) {
               if (var1.getLocalName() == null) {
                  var5 = this.createAttribute(var1.getNodeName());
               } else {
                  var5 = this.createAttributeNS(var1.getNamespaceURI(), var1.getNodeName());
               }
            } else {
               var5 = this.createAttribute(var1.getNodeName());
            }

            if (var1 instanceof AttrImpl) {
               AttrImpl var19 = (AttrImpl)var1;
               if (var19.hasStringValue()) {
                  AttrImpl var22 = (AttrImpl)var5;
                  var22.setValue(var19.getValue());
                  var2 = false;
               } else {
                  var2 = true;
               }
            } else if (var1.getFirstChild() == null) {
               ((Node)var5).setNodeValue(var1.getNodeValue());
               var2 = false;
            } else {
               var2 = true;
            }
            break;
         case 3:
            var5 = this.createTextNode(var1.getNodeValue());
            break;
         case 4:
            var5 = this.createCDATASection(var1.getNodeValue());
            break;
         case 5:
            var5 = this.createEntityReference(var1.getNodeName());
            var2 = false;
            break;
         case 6:
            Entity var17 = (Entity)var1;
            EntityImpl var20 = (EntityImpl)this.createEntity(var1.getNodeName());
            var20.setPublicId(var17.getPublicId());
            var20.setSystemId(var17.getSystemId());
            var20.setNotationName(var17.getNotationName());
            var20.isReadOnly(false);
            var5 = var20;
            break;
         case 7:
            var5 = this.createProcessingInstruction(var1.getNodeName(), var1.getNodeValue());
            break;
         case 8:
            var5 = this.createComment(var1.getNodeValue());
            break;
         case 9:
         default:
            var16 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
            throw new DOMException((short)9, var16);
         case 10:
            if (!var3) {
               var16 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
               throw new DOMException((short)9, var16);
            }

            DocumentType var15 = (DocumentType)var1;
            DocumentTypeImpl var18 = (DocumentTypeImpl)this.createDocumentType(var15.getNodeName(), var15.getPublicId(), var15.getSystemId());
            var10 = var15.getEntities();
            NamedNodeMap var11 = var18.getEntities();
            if (var10 != null) {
               for(var12 = 0; var12 < var10.getLength(); ++var12) {
                  var11.setNamedItem(this.importNode(var10.item(var12), true, true, var4));
               }
            }

            var10 = var15.getNotations();
            var11 = var18.getNotations();
            if (var10 != null) {
               for(var12 = 0; var12 < var10.getLength(); ++var12) {
                  var11.setNamedItem(this.importNode(var10.item(var12), true, true, var4));
               }
            }

            var5 = var18;
            break;
         case 11:
            var5 = this.createDocumentFragment();
            break;
         case 12:
            Notation var8 = (Notation)var1;
            NotationImpl var9 = (NotationImpl)this.createNotation(var1.getNodeName());
            var9.setPublicId(var8.getPublicId());
            var9.setSystemId(var8.getSystemId());
            var5 = var9;
      }

      if (var6 != null) {
         this.callUserDataHandlers(var1, (Node)var5, (short)2, var6);
      }

      if (var2) {
         for(Node var25 = var1.getFirstChild(); var25 != null; var25 = var25.getNextSibling()) {
            ((Node)var5).appendChild(this.importNode(var25, true, var3, var4));
         }
      }

      if (((Node)var5).getNodeType() == 6) {
         ((NodeImpl)var5).setReadOnly(true, true);
      }

      return (Node)var5;
   }

   public Node adoptNode(Node var1) {
      Hashtable var3 = null;

      NodeImpl var2;
      try {
         var2 = (NodeImpl)var1;
      } catch (ClassCastException var9) {
         return null;
      }

      if (var1 == null) {
         return null;
      } else {
         if (var1 != null && var1.getOwnerDocument() != null) {
            DOMImplementation var4 = this.getImplementation();
            DOMImplementation var5 = var1.getOwnerDocument().getImplementation();
            if (var4 != var5) {
               if (var4 instanceof DOMImplementationImpl && var5 instanceof DeferredDOMImplementationImpl) {
                  this.undeferChildren(var2);
               } else if (!(var4 instanceof DeferredDOMImplementationImpl) || !(var5 instanceof DOMImplementationImpl)) {
                  return null;
               }
            }
         }

         String var10;
         Node var11;
         switch (var2.getNodeType()) {
            case 1:
               var3 = var2.getUserDataRecord();
               var11 = var2.getParentNode();
               if (var11 != null) {
                  var11.removeChild(var1);
               }

               var2.setOwnerDocument(this);
               if (var3 != null) {
                  this.setUserDataTable(var2, var3);
               }

               ((ElementImpl)var2).reconcileDefaultAttributes();
               break;
            case 2:
               AttrImpl var12 = (AttrImpl)var2;
               if (var12.getOwnerElement() != null) {
                  var12.getOwnerElement().removeAttributeNode(var12);
               }

               var12.isSpecified(true);
               var3 = var2.getUserDataRecord();
               var12.setOwnerDocument(this);
               if (var3 != null) {
                  this.setUserDataTable(var2, var3);
               }
               break;
            case 3:
            case 4:
            case 7:
            case 8:
            case 11:
            default:
               var3 = var2.getUserDataRecord();
               var11 = var2.getParentNode();
               if (var11 != null) {
                  var11.removeChild(var1);
               }

               var2.setOwnerDocument(this);
               if (var3 != null) {
                  this.setUserDataTable(var2, var3);
               }
               break;
            case 5:
               var3 = var2.getUserDataRecord();
               var11 = var2.getParentNode();
               if (var11 != null) {
                  var11.removeChild(var1);
               }

               Node var13;
               while((var13 = var2.getFirstChild()) != null) {
                  var2.removeChild(var13);
               }

               var2.setOwnerDocument(this);
               if (var3 != null) {
                  this.setUserDataTable(var2, var3);
               }

               if (this.docType != null) {
                  NamedNodeMap var6 = this.docType.getEntities();
                  Node var7 = var6.getNamedItem(var2.getNodeName());
                  if (var7 != null) {
                     for(var13 = var7.getFirstChild(); var13 != null; var13 = var13.getNextSibling()) {
                        Node var8 = var13.cloneNode(true);
                        var2.appendChild(var8);
                     }
                  }
               }
               break;
            case 6:
            case 12:
               var10 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
               throw new DOMException((short)7, var10);
            case 9:
            case 10:
               var10 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
               throw new DOMException((short)9, var10);
         }

         if (var3 != null) {
            this.callUserDataHandlers(var1, (Node)null, (short)5, var3);
         }

         return var2;
      }
   }

   protected void undeferChildren(Node var1) {
      Node var3;
      label39:
      for(Node var2 = var1; null != var1; var1 = var3) {
         if (((NodeImpl)var1).needsSyncData()) {
            ((NodeImpl)var1).synchronizeData();
         }

         var3 = null;
         var3 = var1.getFirstChild();

         do {
            do {
               if (null != var3 || var2.equals(var1)) {
                  continue label39;
               }

               var3 = var1.getNextSibling();
            } while(null != var3);

            var1 = var1.getParentNode();
         } while(null != var1 && !var2.equals(var1));

         var3 = null;
      }

   }

   public Element getElementById(String var1) {
      return this.getIdentifier(var1);
   }

   protected final void clearIdentifiers() {
      if (this.identifiers != null) {
         this.identifiers.clear();
      }

   }

   public void putIdentifier(String var1, Element var2) {
      if (var2 == null) {
         this.removeIdentifier(var1);
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         if (this.identifiers == null) {
            this.identifiers = new Hashtable();
         }

         this.identifiers.put(var1, var2);
      }
   }

   public Element getIdentifier(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.identifiers == null) {
         return null;
      } else {
         Element var2 = (Element)this.identifiers.get(var1);
         if (var2 != null) {
            for(Node var3 = var2.getParentNode(); var3 != null; var3 = var3.getParentNode()) {
               if (var3 == this) {
                  return var2;
               }
            }
         }

         return null;
      }
   }

   public void removeIdentifier(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.identifiers != null) {
         this.identifiers.remove(var1);
      }
   }

   public Enumeration getIdentifiers() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.identifiers == null) {
         this.identifiers = new Hashtable();
      }

      return this.identifiers.keys();
   }

   public Element createElementNS(String var1, String var2) throws DOMException {
      return new ElementNSImpl(this, var1, var2);
   }

   public Element createElementNS(String var1, String var2, String var3) throws DOMException {
      return new ElementNSImpl(this, var1, var2, var3);
   }

   public Attr createAttributeNS(String var1, String var2) throws DOMException {
      return new AttrNSImpl(this, var1, var2);
   }

   public Attr createAttributeNS(String var1, String var2, String var3) throws DOMException {
      return new AttrNSImpl(this, var1, var2, var3);
   }

   public NodeList getElementsByTagNameNS(String var1, String var2) {
      return new DeepNodeListImpl(this, var1, var2);
   }

   public Object clone() throws CloneNotSupportedException {
      CoreDocumentImpl var1 = (CoreDocumentImpl)super.clone();
      var1.docType = null;
      var1.docElement = null;
      return var1;
   }

   public static final boolean isXMLName(String var0, boolean var1) {
      if (var0 == null) {
         return false;
      } else {
         return !var1 ? XMLChar.isValidName(var0) : XML11Char.isXML11ValidName(var0);
      }
   }

   public static final boolean isValidQName(String var0, String var1, boolean var2) {
      if (var1 == null) {
         return false;
      } else {
         boolean var3 = false;
         if (!var2) {
            var3 = (var0 == null || XMLChar.isValidNCName(var0)) && XMLChar.isValidNCName(var1);
         } else {
            var3 = (var0 == null || XML11Char.isXML11ValidNCName(var0)) && XML11Char.isXML11ValidNCName(var1);
         }

         return var3;
      }
   }

   protected boolean isKidOK(Node var1, Node var2) {
      if (this.allowGrammarAccess && var1.getNodeType() == 10) {
         return var2.getNodeType() == 1;
      } else {
         return 0 != (kidOK[var1.getNodeType()] & 1 << var2.getNodeType());
      }
   }

   protected void changed() {
      ++this.changes;
   }

   protected int changes() {
      return this.changes;
   }

   NodeListCache getNodeListCache(ParentNode var1) {
      if (this.fFreeNLCache == null) {
         return new NodeListCache(var1);
      } else {
         NodeListCache var2 = this.fFreeNLCache;
         this.fFreeNLCache = this.fFreeNLCache.next;
         var2.fChild = null;
         var2.fChildIndex = -1;
         var2.fLength = -1;
         if (var2.fOwner != null) {
            var2.fOwner.fNodeListCache = null;
         }

         var2.fOwner = var1;
         return var2;
      }
   }

   void freeNodeListCache(NodeListCache var1) {
      var1.next = this.fFreeNLCache;
      this.fFreeNLCache = var1;
   }

   public Object setUserData(Node var1, String var2, Object var3, UserDataHandler var4) {
      Hashtable var5;
      Object var6;
      ParentNode.UserDataRecord var7;
      if (var3 == null) {
         if (this.userData != null) {
            var5 = (Hashtable)this.userData.get(var1);
            if (var5 != null) {
               var6 = var5.remove(var2);
               if (var6 != null) {
                  var7 = (ParentNode.UserDataRecord)var6;
                  return var7.fData;
               }
            }
         }

         return null;
      } else {
         if (this.userData == null) {
            this.userData = new Hashtable();
            var5 = new Hashtable();
            this.userData.put(var1, var5);
         } else {
            var5 = (Hashtable)this.userData.get(var1);
            if (var5 == null) {
               var5 = new Hashtable();
               this.userData.put(var1, var5);
            }
         }

         var6 = var5.put(var2, new ParentNode.UserDataRecord(var3, var4));
         if (var6 != null) {
            var7 = (ParentNode.UserDataRecord)var6;
            return var7.fData;
         } else {
            return null;
         }
      }
   }

   public Object getUserData(Node var1, String var2) {
      if (this.userData == null) {
         return null;
      } else {
         Hashtable var3 = (Hashtable)this.userData.get(var1);
         if (var3 == null) {
            return null;
         } else {
            Object var4 = var3.get(var2);
            if (var4 != null) {
               ParentNode.UserDataRecord var5 = (ParentNode.UserDataRecord)var4;
               return var5.fData;
            } else {
               return null;
            }
         }
      }
   }

   protected Hashtable getUserDataRecord(Node var1) {
      if (this.userData == null) {
         return null;
      } else {
         Hashtable var2 = (Hashtable)this.userData.get(var1);
         return var2 == null ? null : var2;
      }
   }

   Hashtable removeUserDataTable(Node var1) {
      return this.userData == null ? null : (Hashtable)this.userData.get(var1);
   }

   void setUserDataTable(Node var1, Hashtable var2) {
      if (this.userData == null) {
         this.userData = new Hashtable();
      }

      if (var2 != null) {
         this.userData.put(var1, var2);
      }

   }

   void callUserDataHandlers(Node var1, Node var2, short var3) {
      if (this.userData != null) {
         if (var1 instanceof NodeImpl) {
            Hashtable var4 = ((NodeImpl)var1).getUserDataRecord();
            if (var4 == null || var4.isEmpty()) {
               return;
            }

            this.callUserDataHandlers(var1, var2, var3, var4);
         }

      }
   }

   void callUserDataHandlers(Node var1, Node var2, short var3, Hashtable var4) {
      if (var4 != null && !var4.isEmpty()) {
         Enumeration var5 = var4.keys();

         while(var5.hasMoreElements()) {
            String var6 = (String)var5.nextElement();
            ParentNode.UserDataRecord var7 = (ParentNode.UserDataRecord)var4.get(var6);
            if (var7.fHandler != null) {
               var7.fHandler.handle(var3, var6, var7.fData, var1, var2);
            }
         }

      }
   }

   protected final void checkNamespaceWF(String var1, int var2, int var3) {
      if (this.errorChecking) {
         if (var2 == 0 || var2 == var1.length() - 1 || var3 != var2) {
            String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
            throw new DOMException((short)14, var4);
         }
      }
   }

   protected final void checkDOMNSErr(String var1, String var2) {
      if (this.errorChecking) {
         String var3;
         if (var2 == null) {
            var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
            throw new DOMException((short)14, var3);
         }

         if (var1.equals("xml") && !var2.equals(NamespaceContext.XML_URI)) {
            var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
            throw new DOMException((short)14, var3);
         }

         if (var1.equals("xmlns") && !var2.equals(NamespaceContext.XMLNS_URI) || !var1.equals("xmlns") && var2.equals(NamespaceContext.XMLNS_URI)) {
            var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
            throw new DOMException((short)14, var3);
         }
      }

   }

   protected final void checkQName(String var1, String var2) {
      if (this.errorChecking) {
         boolean var3 = false;
         if (!this.xml11Version) {
            var3 = (var1 == null || XMLChar.isValidNCName(var1)) && XMLChar.isValidNCName(var2);
         } else {
            var3 = (var1 == null || XML11Char.isXML11ValidNCName(var1)) && XML11Char.isXML11ValidNCName(var2);
         }

         if (!var3) {
            String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
            throw new DOMException((short)5, var4);
         }
      }
   }

   boolean isXML11Version() {
      return this.xml11Version;
   }

   boolean isNormalizeDocRequired() {
      return true;
   }

   boolean isXMLVersionChanged() {
      return this.xmlVersionChanged;
   }

   protected void setUserData(NodeImpl var1, Object var2) {
      this.setUserData(var1, "XERCES1DOMUSERDATA", var2, (UserDataHandler)null);
   }

   protected Object getUserData(NodeImpl var1) {
      return this.getUserData(var1, "XERCES1DOMUSERDATA");
   }

   protected void addEventListener(NodeImpl var1, String var2, EventListener var3, boolean var4) {
   }

   protected void removeEventListener(NodeImpl var1, String var2, EventListener var3, boolean var4) {
   }

   protected void copyEventListeners(NodeImpl var1, NodeImpl var2) {
   }

   protected boolean dispatchEvent(NodeImpl var1, Event var2) {
      return false;
   }

   void replacedText(NodeImpl var1) {
   }

   void deletedText(NodeImpl var1, int var2, int var3) {
   }

   void insertedText(NodeImpl var1, int var2, int var3) {
   }

   void modifyingCharacterData(NodeImpl var1, boolean var2) {
   }

   void modifiedCharacterData(NodeImpl var1, String var2, String var3, boolean var4) {
   }

   void insertingNode(NodeImpl var1, boolean var2) {
   }

   void insertedNode(NodeImpl var1, NodeImpl var2, boolean var3) {
   }

   void removingNode(NodeImpl var1, NodeImpl var2, boolean var3) {
   }

   void removedNode(NodeImpl var1, boolean var2) {
   }

   void replacingNode(NodeImpl var1) {
   }

   void replacedNode(NodeImpl var1) {
   }

   void replacingData(NodeImpl var1) {
   }

   void replacedCharacterData(NodeImpl var1, String var2, String var3) {
   }

   void modifiedAttrValue(AttrImpl var1, String var2) {
   }

   void setAttrNode(AttrImpl var1, AttrImpl var2) {
   }

   void removedAttrNode(AttrImpl var1, NodeImpl var2, String var3) {
   }

   void renamedAttrNode(Attr var1, Attr var2) {
   }

   void renamedElement(Element var1, Element var2) {
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      kidOK[9] = 1410;
      kidOK[11] = kidOK[6] = kidOK[5] = kidOK[1] = 442;
      kidOK[2] = 40;
      kidOK[10] = kidOK[7] = kidOK[8] = kidOK[3] = kidOK[4] = kidOK[12] = 0;
   }
}
