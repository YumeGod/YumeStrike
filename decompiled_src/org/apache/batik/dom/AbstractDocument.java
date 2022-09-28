package org.apache.batik.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.WeakHashMap;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.batik.dom.events.DocumentEventSupport;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.traversal.TraversalSupport;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.xbl.GenericXBLManager;
import org.apache.batik.dom.xbl.XBLManager;
import org.apache.batik.i18n.Localizable;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.CleanerThread;
import org.apache.batik.util.SoftDoublyIndexedTable;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMLocator;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MutationNameEvent;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;

public abstract class AbstractDocument extends AbstractParentNode implements Document, DocumentEvent, DocumentTraversal, Localizable, XPathEvaluator {
   protected static final String RESOURCES = "org.apache.batik.dom.resources.Messages";
   protected transient LocalizableSupport localizableSupport = new LocalizableSupport("org.apache.batik.dom.resources.Messages", this.getClass().getClassLoader());
   protected transient DOMImplementation implementation;
   protected transient TraversalSupport traversalSupport;
   protected transient DocumentEventSupport documentEventSupport;
   protected transient boolean eventsEnabled;
   protected transient WeakHashMap elementsByTagNames;
   protected transient WeakHashMap elementsByTagNamesNS;
   protected String inputEncoding;
   protected String xmlEncoding;
   protected String xmlVersion = "1.0";
   protected boolean xmlStandalone;
   protected String documentURI;
   protected boolean strictErrorChecking = true;
   protected DocumentConfiguration domConfig;
   protected transient XBLManager xblManager = new GenericXBLManager();
   protected transient Map elementsById;

   protected AbstractDocument() {
   }

   public AbstractDocument(DocumentType var1, DOMImplementation var2) {
      this.implementation = var2;
      if (var1 != null) {
         if (var1 instanceof GenericDocumentType) {
            GenericDocumentType var3 = (GenericDocumentType)var1;
            if (var3.getOwnerDocument() == null) {
               var3.setOwnerDocument(this);
            }
         }

         this.appendChild(var1);
      }

   }

   public void setDocumentInputEncoding(String var1) {
      this.inputEncoding = var1;
   }

   public void setDocumentXmlEncoding(String var1) {
      this.xmlEncoding = var1;
   }

   public void setLocale(Locale var1) {
      this.localizableSupport.setLocale(var1);
   }

   public Locale getLocale() {
      return this.localizableSupport.getLocale();
   }

   public String formatMessage(String var1, Object[] var2) throws MissingResourceException {
      return this.localizableSupport.formatMessage(var1, var2);
   }

   public boolean getEventsEnabled() {
      return this.eventsEnabled;
   }

   public void setEventsEnabled(boolean var1) {
      this.eventsEnabled = var1;
   }

   public String getNodeName() {
      return "#document";
   }

   public short getNodeType() {
      return 9;
   }

   public DocumentType getDoctype() {
      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 10) {
            return (DocumentType)var1;
         }
      }

      return null;
   }

   public void setDoctype(DocumentType var1) {
      if (var1 != null) {
         this.appendChild(var1);
         ((ExtendedNode)var1).setReadonly(true);
      }

   }

   public DOMImplementation getImplementation() {
      return this.implementation;
   }

   public Element getDocumentElement() {
      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return null;
   }

   public Node importNode(Node var1, boolean var2) throws DOMException {
      return this.importNode(var1, var2, false);
   }

   public Node importNode(Node var1, boolean var2, boolean var3) {
      Object var4;
      switch (var1.getNodeType()) {
         case 1:
            Element var5 = this.createElementNS(var1.getNamespaceURI(), var1.getNodeName());
            var4 = var5;
            if (var1.hasAttributes()) {
               NamedNodeMap var6 = var1.getAttributes();
               int var7 = var6.getLength();

               for(int var8 = 0; var8 < var7; ++var8) {
                  Attr var9 = (Attr)var6.item(var8);
                  if (var9.getSpecified()) {
                     AbstractAttr var10 = (AbstractAttr)this.importNode(var9, true);
                     if (var3 && var10.isId()) {
                        var10.setIsId(false);
                     }

                     var5.setAttributeNodeNS(var10);
                  }
               }
            }
            break;
         case 2:
            var4 = this.createAttributeNS(var1.getNamespaceURI(), var1.getNodeName());
            break;
         case 3:
            var4 = this.createTextNode(var1.getNodeValue());
            var2 = false;
            break;
         case 4:
            var4 = this.createCDATASection(var1.getNodeValue());
            var2 = false;
            break;
         case 5:
            var4 = this.createEntityReference(var1.getNodeName());
            break;
         case 6:
         case 9:
         case 10:
         default:
            throw this.createDOMException((short)9, "import.node", new Object[0]);
         case 7:
            var4 = this.createProcessingInstruction(var1.getNodeName(), var1.getNodeValue());
            var2 = false;
            break;
         case 8:
            var4 = this.createComment(var1.getNodeValue());
            var2 = false;
            break;
         case 11:
            var4 = this.createDocumentFragment();
      }

      if (var1 instanceof AbstractNode) {
         this.fireUserDataHandlers((short)2, var1, (Node)var4);
      }

      if (var2) {
         for(Node var11 = var1.getFirstChild(); var11 != null; var11 = var11.getNextSibling()) {
            ((Node)var4).appendChild(this.importNode(var11, true));
         }
      }

      return (Node)var4;
   }

   public Node cloneNode(boolean var1) {
      Document var2 = (Document)this.newNode();
      this.copyInto(var2);
      this.fireUserDataHandlers((short)1, this, var2);
      if (var1) {
         for(Node var3 = this.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            var2.appendChild(var2.importNode(var3, var1));
         }
      }

      return var2;
   }

   public abstract boolean isId(Attr var1);

   public Element getElementById(String var1) {
      return this.getChildElementById(this.getDocumentElement(), var1);
   }

   public Element getChildElementById(Node var1, String var2) {
      if (var2 != null && var2.length() != 0) {
         if (this.elementsById == null) {
            return null;
         } else {
            Node var3 = this.getRoot(var1);
            Object var4 = this.elementsById.get(var2);
            if (var4 == null) {
               return null;
            } else if (var4 instanceof IdSoftRef) {
               var4 = ((IdSoftRef)var4).get();
               if (var4 == null) {
                  this.elementsById.remove(var2);
                  return null;
               } else {
                  Element var9 = (Element)var4;
                  return this.getRoot(var9) == var3 ? var9 : null;
               }
            } else {
               List var5 = (List)var4;
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  IdSoftRef var7 = (IdSoftRef)var6.next();
                  var4 = var7.get();
                  if (var4 == null) {
                     var6.remove();
                  } else {
                     Element var8 = (Element)var4;
                     if (this.getRoot(var8) == var3) {
                        return var8;
                     }
                  }
               }

               return null;
            }
         }
      } else {
         return null;
      }
   }

   protected Node getRoot(Node var1) {
      Node var2;
      for(var2 = var1; var1 != null; var1 = var1.getParentNode()) {
         var2 = var1;
      }

      return var2;
   }

   public void removeIdEntry(Element var1, String var2) {
      if (var2 != null) {
         if (this.elementsById != null) {
            synchronized(this.elementsById) {
               Object var4 = this.elementsById.get(var2);
               if (var4 != null) {
                  if (var4 instanceof IdSoftRef) {
                     this.elementsById.remove(var2);
                  } else {
                     List var5 = (List)var4;
                     Iterator var6 = var5.iterator();

                     while(var6.hasNext()) {
                        IdSoftRef var7 = (IdSoftRef)var6.next();
                        var4 = var7.get();
                        if (var4 == null) {
                           var6.remove();
                        } else if (var1 == var4) {
                           var6.remove();
                           break;
                        }
                     }

                     if (var5.size() == 0) {
                        this.elementsById.remove(var2);
                     }

                  }
               }
            }
         }
      }
   }

   public void addIdEntry(Element var1, String var2) {
      if (var2 != null) {
         if (this.elementsById == null) {
            HashMap var3 = new HashMap();
            var3.put(var2, new IdSoftRef(var1, var2));
            this.elementsById = var3;
         } else {
            synchronized(this.elementsById) {
               Object var4 = this.elementsById.get(var2);
               if (var4 == null) {
                  this.elementsById.put(var2, new IdSoftRef(var1, var2));
               } else if (var4 instanceof IdSoftRef) {
                  IdSoftRef var10 = (IdSoftRef)var4;
                  Object var6 = var10.get();
                  if (var6 == null) {
                     this.elementsById.put(var2, new IdSoftRef(var1, var2));
                  } else {
                     ArrayList var7 = new ArrayList(4);
                     var10.setList(var7);
                     var7.add(var10);
                     var7.add(new IdSoftRef(var1, var2, var7));
                     this.elementsById.put(var2, var7);
                  }
               } else {
                  List var5 = (List)var4;
                  var5.add(new IdSoftRef(var1, var2, var5));
               }
            }
         }
      }
   }

   public void updateIdEntry(Element var1, String var2, String var3) {
      if (var2 != var3 && (var2 == null || !var2.equals(var3))) {
         this.removeIdEntry(var1, var2);
         this.addIdEntry(var1, var3);
      }
   }

   public AbstractParentNode.ElementsByTagName getElementsByTagName(Node var1, String var2) {
      if (this.elementsByTagNames == null) {
         return null;
      } else {
         SoftDoublyIndexedTable var3 = (SoftDoublyIndexedTable)this.elementsByTagNames.get(var1);
         return var3 == null ? null : (AbstractParentNode.ElementsByTagName)var3.get((Object)null, var2);
      }
   }

   public void putElementsByTagName(Node var1, String var2, AbstractParentNode.ElementsByTagName var3) {
      if (this.elementsByTagNames == null) {
         this.elementsByTagNames = new WeakHashMap(11);
      }

      SoftDoublyIndexedTable var4 = (SoftDoublyIndexedTable)this.elementsByTagNames.get(var1);
      if (var4 == null) {
         this.elementsByTagNames.put(var1, var4 = new SoftDoublyIndexedTable());
      }

      var4.put((Object)null, var2, var3);
   }

   public AbstractParentNode.ElementsByTagNameNS getElementsByTagNameNS(Node var1, String var2, String var3) {
      if (this.elementsByTagNamesNS == null) {
         return null;
      } else {
         SoftDoublyIndexedTable var4 = (SoftDoublyIndexedTable)this.elementsByTagNamesNS.get(var1);
         return var4 == null ? null : (AbstractParentNode.ElementsByTagNameNS)var4.get(var2, var3);
      }
   }

   public void putElementsByTagNameNS(Node var1, String var2, String var3, AbstractParentNode.ElementsByTagNameNS var4) {
      if (this.elementsByTagNamesNS == null) {
         this.elementsByTagNamesNS = new WeakHashMap(11);
      }

      SoftDoublyIndexedTable var5 = (SoftDoublyIndexedTable)this.elementsByTagNamesNS.get(var1);
      if (var5 == null) {
         this.elementsByTagNamesNS.put(var1, var5 = new SoftDoublyIndexedTable());
      }

      var5.put(var2, var3, var4);
   }

   public Event createEvent(String var1) throws DOMException {
      if (this.documentEventSupport == null) {
         this.documentEventSupport = ((AbstractDOMImplementation)this.implementation).createDocumentEventSupport();
      }

      return this.documentEventSupport.createEvent(var1);
   }

   public boolean canDispatch(String var1, String var2) {
      if (var2 == null) {
         return false;
      } else {
         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         if (var1 != null && !var1.equals("http://www.w3.org/2001/xml-events")) {
            return false;
         } else {
            return var2.equals("Event") || var2.equals("MutationEvent") || var2.equals("MutationNameEvent") || var2.equals("UIEvent") || var2.equals("MouseEvent") || var2.equals("KeyEvent") || var2.equals("KeyboardEvent") || var2.equals("TextEvent") || var2.equals("CustomEvent");
         }
      }
   }

   public NodeIterator createNodeIterator(Node var1, int var2, NodeFilter var3, boolean var4) throws DOMException {
      if (this.traversalSupport == null) {
         this.traversalSupport = new TraversalSupport();
      }

      return this.traversalSupport.createNodeIterator(this, var1, var2, var3, var4);
   }

   public TreeWalker createTreeWalker(Node var1, int var2, NodeFilter var3, boolean var4) throws DOMException {
      return TraversalSupport.createTreeWalker(this, var1, var2, var3, var4);
   }

   public void detachNodeIterator(NodeIterator var1) {
      this.traversalSupport.detachNodeIterator(var1);
   }

   public void nodeToBeRemoved(Node var1) {
      if (this.traversalSupport != null) {
         this.traversalSupport.nodeToBeRemoved(var1);
      }

   }

   protected AbstractDocument getCurrentDocument() {
      return this;
   }

   protected Node export(Node var1, Document var2) {
      throw this.createDOMException((short)9, "import.document", new Object[0]);
   }

   protected Node deepExport(Node var1, Document var2) {
      throw this.createDOMException((short)9, "import.document", new Object[0]);
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractDocument var2 = (AbstractDocument)var1;
      var2.implementation = this.implementation;
      var2.localizableSupport = new LocalizableSupport("org.apache.batik.dom.resources.Messages", this.getClass().getClassLoader());
      var2.inputEncoding = this.inputEncoding;
      var2.xmlEncoding = this.xmlEncoding;
      var2.xmlVersion = this.xmlVersion;
      var2.xmlStandalone = this.xmlStandalone;
      var2.documentURI = this.documentURI;
      var2.strictErrorChecking = this.strictErrorChecking;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractDocument var2 = (AbstractDocument)var1;
      var2.implementation = this.implementation;
      var2.localizableSupport = new LocalizableSupport("org.apache.batik.dom.resources.Messages", this.getClass().getClassLoader());
      return var1;
   }

   protected void checkChildType(Node var1, boolean var2) {
      short var3 = var1.getNodeType();
      switch (var3) {
         case 1:
         case 7:
         case 8:
         case 10:
         case 11:
            if ((var2 || var3 != 1 || this.getDocumentElement() == null) && (var3 != 10 || this.getDoctype() == null)) {
               return;
            } else {
               throw this.createDOMException((short)9, "document.child.already.exists", new Object[]{new Integer(var3), var1.getNodeName()});
            }
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 9:
         default:
            throw this.createDOMException((short)3, "child.type", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), new Integer(var3), var1.getNodeName()});
      }
   }

   public String getInputEncoding() {
      return this.inputEncoding;
   }

   public String getXmlEncoding() {
      return this.xmlEncoding;
   }

   public boolean getXmlStandalone() {
      return this.xmlStandalone;
   }

   public void setXmlStandalone(boolean var1) throws DOMException {
      this.xmlStandalone = var1;
   }

   public String getXmlVersion() {
      return this.xmlVersion;
   }

   public void setXmlVersion(String var1) throws DOMException {
      if (var1 != null && (var1.equals("1.0") || var1.equals("1.1"))) {
         this.xmlVersion = var1;
      } else {
         throw this.createDOMException((short)9, "xml.version", new Object[]{var1});
      }
   }

   public boolean getStrictErrorChecking() {
      return this.strictErrorChecking;
   }

   public void setStrictErrorChecking(boolean var1) {
      this.strictErrorChecking = var1;
   }

   public String getDocumentURI() {
      return this.documentURI;
   }

   public void setDocumentURI(String var1) {
      this.documentURI = var1;
   }

   public DOMConfiguration getDomConfig() {
      if (this.domConfig == null) {
         this.domConfig = new DocumentConfiguration();
      }

      return this.domConfig;
   }

   public Node adoptNode(Node var1) throws DOMException {
      if (!(var1 instanceof AbstractNode)) {
         return null;
      } else {
         switch (var1.getNodeType()) {
            case 6:
            case 12:
               return null;
            case 7:
            case 8:
            case 11:
            default:
               AbstractNode var2 = (AbstractNode)var1;
               if (var2.isReadonly()) {
                  throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(var2.getNodeType()), var2.getNodeName()});
               }

               Node var3 = var1.getParentNode();
               if (var3 != null) {
                  var3.removeChild(var1);
               }

               this.adoptNode1((AbstractNode)var1);
               return var1;
            case 9:
               throw this.createDOMException((short)9, "adopt.document", new Object[0]);
            case 10:
               throw this.createDOMException((short)9, "adopt.document.type", new Object[0]);
         }
      }
   }

   protected void adoptNode1(AbstractNode var1) {
      var1.ownerDocument = this;
      AbstractAttr var2;
      label35:
      switch (var1.getNodeType()) {
         case 1:
            NamedNodeMap var3 = var1.getAttributes();
            int var4 = var3.getLength();
            int var5 = 0;

            while(true) {
               if (var5 >= var4) {
                  break label35;
               }

               var2 = (AbstractAttr)var3.item(var5);
               if (var2.getSpecified()) {
                  this.adoptNode1(var2);
               }

               ++var5;
            }
         case 2:
            var2 = (AbstractAttr)var1;
            var2.ownerElement = null;
            var2.unspecified = false;
         case 3:
         case 4:
         default:
            break;
         case 5:
            while(var1.getFirstChild() != null) {
               var1.removeChild(var1.getFirstChild());
            }
      }

      this.fireUserDataHandlers((short)5, var1, (Node)null);
      Node var6 = var1.getFirstChild();

      while(var6 != null) {
         switch (var6.getNodeType()) {
            case 6:
            case 10:
            case 12:
               return;
            default:
               this.adoptNode1((AbstractNode)var6);
               var6 = var6.getNextSibling();
         }
      }

   }

   public Node renameNode(Node var1, String var2, String var3) {
      AbstractNode var4 = (AbstractNode)var1;
      if (var4 == this.getDocumentElement()) {
         throw this.createDOMException((short)9, "rename.document.element", new Object[0]);
      } else {
         short var5 = var1.getNodeType();
         if (var5 != 1 && var5 != 2) {
            throw this.createDOMException((short)9, "rename.node", new Object[]{new Integer(var5), var1.getNodeName()});
         } else if ((!this.xmlVersion.equals("1.1") || DOMUtilities.isValidName11(var3)) && DOMUtilities.isValidName(var3)) {
            if (var1.getOwnerDocument() != this) {
               throw this.createDOMException((short)9, "node.from.wrong.document", new Object[]{new Integer(var5), var1.getNodeName()});
            } else {
               int var6 = var3.indexOf(58);
               if (var6 != 0 && var6 != var3.length() - 1) {
                  String var7 = DOMUtilities.getPrefix(var3);
                  if (var2 != null && var2.length() == 0) {
                     var2 = null;
                  }

                  if (var7 != null && var2 == null) {
                     throw this.createDOMException((short)14, "prefix", new Object[]{new Integer(var5), var1.getNodeName(), var7});
                  } else if (!this.strictErrorChecking || (!"xml".equals(var7) || "http://www.w3.org/XML/1998/namespace".equals(var2)) && (!"xmlns".equals(var7) || "http://www.w3.org/2000/xmlns/".equals(var2))) {
                     String var8 = var1.getNamespaceURI();
                     String var9 = var1.getNodeName();
                     if (var5 != 1) {
                        Element var19;
                        if (var1 instanceof AbstractAttrNS) {
                           AbstractAttrNS var18 = (AbstractAttrNS)var1;
                           var19 = var18.getOwnerElement();
                           if (var19 != null) {
                              var19.removeAttributeNode(var18);
                           }

                           var18.namespaceURI = var2;
                           var18.nodeName = var3;
                           if (var19 != null) {
                              var19.setAttributeNodeNS(var18);
                           }

                           this.fireUserDataHandlers((short)4, var18, (Node)null);
                           if (this.getEventsEnabled()) {
                              MutationNameEvent var21 = (MutationNameEvent)this.createEvent("MutationNameEvent");
                              var21.initMutationNameEventNS("http://www.w3.org/2001/xml-events", "DOMAttrNameChanged", true, false, var18, var8, var9);
                              this.dispatchEvent(var21);
                           }

                           return var18;
                        } else {
                           AbstractAttr var17 = (AbstractAttr)var1;
                           var19 = var17.getOwnerElement();
                           if (var19 != null) {
                              var19.removeAttributeNode(var17);
                           }

                           AbstractAttr var20 = (AbstractAttr)this.createAttributeNS(var2, var3);
                           var20.setNodeValue(var17.getNodeValue());
                           var20.userData = var17.userData == null ? null : (HashMap)var17.userData.clone();
                           var20.userDataHandlers = var17.userDataHandlers == null ? null : (HashMap)var17.userDataHandlers.clone();
                           if (var19 != null) {
                              var19.setAttributeNodeNS(var20);
                           }

                           this.fireUserDataHandlers((short)4, var17, var20);
                           if (this.getEventsEnabled()) {
                              MutationNameEvent var22 = (MutationNameEvent)this.createEvent("MutationNameEvent");
                              var22.initMutationNameEventNS("http://www.w3.org/2001/xml-events", "DOMAttrNameChanged", true, false, var20, var8, var9);
                              this.dispatchEvent(var22);
                           }

                           return var20;
                        }
                     } else {
                        Node var10 = var1.getParentNode();
                        AbstractElement var11 = (AbstractElement)this.createElementNS(var2, var3);
                        EventSupport var12 = var4.getEventSupport();
                        EventSupport var13;
                        if (var12 != null) {
                           var13 = var11.getEventSupport();
                           if (var13 == null) {
                              AbstractDOMImplementation var14 = (AbstractDOMImplementation)this.implementation;
                              var13 = var14.createEventSupport(var11);
                              this.setEventsEnabled(true);
                              var11.eventSupport = var13;
                           }

                           var12.moveEventListeners(var11.getEventSupport());
                        }

                        var11.userData = var11.userData == null ? null : (HashMap)var4.userData.clone();
                        var11.userDataHandlers = var11.userDataHandlers == null ? null : (HashMap)var4.userDataHandlers.clone();
                        var13 = null;
                        if (var10 != null) {
                           var1.getNextSibling();
                           var10.removeChild(var1);
                        }

                        while(var1.getFirstChild() != null) {
                           var11.appendChild(var1.getFirstChild());
                        }

                        NamedNodeMap var23 = var1.getAttributes();

                        for(int var15 = 0; var15 < var23.getLength(); ++var15) {
                           Attr var16 = (Attr)var23.item(var15);
                           var11.setAttributeNodeNS(var16);
                        }

                        if (var10 != null) {
                           if (var13 == null) {
                              var10.appendChild(var11);
                           } else {
                              var10.insertBefore(var13, var11);
                           }
                        }

                        this.fireUserDataHandlers((short)4, var1, var11);
                        if (this.getEventsEnabled()) {
                           MutationNameEvent var24 = (MutationNameEvent)this.createEvent("MutationNameEvent");
                           var24.initMutationNameEventNS("http://www.w3.org/2001/xml-events", "DOMElementNameChanged", true, false, (Node)null, var8, var9);
                           this.dispatchEvent(var24);
                        }

                        return var11;
                     }
                  } else {
                     throw this.createDOMException((short)14, "namespace", new Object[]{new Integer(var5), var1.getNodeName(), var2});
                  }
               } else {
                  throw this.createDOMException((short)14, "qname", new Object[]{new Integer(var5), var1.getNodeName(), var3});
               }
            }
         } else {
            throw this.createDOMException((short)9, "wf.invalid.name", new Object[]{var3});
         }
      }
   }

   public void normalizeDocument() {
      if (this.domConfig == null) {
         this.domConfig = new DocumentConfiguration();
      }

      boolean var1 = this.domConfig.getBooleanParameter("cdata-sections");
      boolean var2 = this.domConfig.getBooleanParameter("comments");
      boolean var3 = this.domConfig.getBooleanParameter("element-content-whitespace");
      boolean var4 = this.domConfig.getBooleanParameter("namespace-declarations");
      boolean var5 = this.domConfig.getBooleanParameter("namespaces");
      boolean var6 = this.domConfig.getBooleanParameter("split-cdata-sections");
      DOMErrorHandler var7 = (DOMErrorHandler)this.domConfig.getParameter("error-handler");
      this.normalizeDocument(this.getDocumentElement(), var1, var2, var3, var4, var5, var6, var7);
   }

   protected boolean normalizeDocument(Element var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, DOMErrorHandler var8) {
      AbstractElement var9 = (AbstractElement)var1;
      Object var10 = var1.getFirstChild();

      while(true) {
         Node var14;
         String var27;
         while(var10 != null) {
            short var11 = ((Node)var10).getNodeType();
            if (var11 == 3 || !var2 && var11 == 4) {
               Object var24 = var10;
               StringBuffer var13 = new StringBuffer();
               var13.append(((Node)var10).getNodeValue());

               Node var22;
               for(var22 = ((Node)var10).getNextSibling(); var22 != null && (var22.getNodeType() == 3 || !var2 && var22.getNodeType() == 4); var22 = var14) {
                  var13.append(var22.getNodeValue());
                  var14 = var22.getNextSibling();
                  var1.removeChild(var22);
               }

               var27 = var13.toString();
               if (var27.length() == 0) {
                  Node var30 = var22.getNextSibling();
                  var1.removeChild(var22);
                  var10 = var30;
                  continue;
               }

               if (!var27.equals(((Node)var10).getNodeValue())) {
                  if (!var2 && var11 == 3) {
                     var10 = this.createTextNode(var27);
                     var1.replaceChild((Node)var10, (Node)var24);
                  } else {
                     var10 = var10;
                     ((Node)var24).setNodeValue(var27);
                  }
               } else {
                  var10 = var10;
               }

               if (!var4) {
                  var11 = ((Node)var10).getNodeType();
                  if (var11 == 3) {
                     AbstractText var15 = (AbstractText)var10;
                     if (var15.isElementContentWhitespace()) {
                        Node var16 = ((Node)var10).getNextSibling();
                        var1.removeChild((Node)var10);
                        var10 = var16;
                        continue;
                     }
                  }
               }

               if (var11 == 4 && var7 && !this.splitCdata(var1, (Node)var10, var8)) {
                  return false;
               }
            } else if (var11 == 4 && var7) {
               if (!this.splitCdata(var1, (Node)var10, var8)) {
                  return false;
               }
            } else if (var11 == 8 && !var3) {
               Node var12 = ((Node)var10).getPreviousSibling();
               if (var12 == null) {
                  var12 = ((Node)var10).getNextSibling();
               }

               var1.removeChild((Node)var10);
               var10 = var12;
               continue;
            }

            var10 = ((Node)var10).getNextSibling();
         }

         NamedNodeMap var23 = var1.getAttributes();
         LinkedList var25 = new LinkedList();
         HashMap var26 = new HashMap();

         String var17;
         int var28;
         Attr var31;
         String var33;
         for(var28 = 0; var28 < var23.getLength(); ++var28) {
            var31 = (Attr)var23.item(var28);
            var33 = var31.getPrefix();
            if (var31 != null && "xmlns".equals(var33) || var31.getNodeName().equals("xmlns")) {
               if (!var5) {
                  var25.add(var31);
               } else {
                  var17 = var31.getNodeValue();
                  if (!var31.getNodeValue().equals("http://www.w3.org/2000/xmlns/") && var17.equals("http://www.w3.org/2000/xmlns/")) {
                     var26.put(var33, var17);
                  }
               }
            }
         }

         if (!var5) {
            Iterator var29 = var25.iterator();

            while(var29.hasNext()) {
               var1.removeAttributeNode((Attr)var29.next());
            }
         } else if (var6) {
            var27 = var1.getNamespaceURI();
            if (var27 != null) {
               String var32 = var1.getPrefix();
               if (!this.compareStrings(var9.lookupNamespaceURI(var32), var27)) {
                  var1.setAttributeNS("http://www.w3.org/2000/xmlns/", var32 == null ? "xmlns" : "xmlns:" + var32, var27);
               }
            } else if (var1.getLocalName() != null && var9.lookupNamespaceURI((String)null) == null) {
               var1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
            }

            var23 = var1.getAttributes();

            for(int var34 = 0; var34 < var23.getLength(); ++var34) {
               Attr var35 = (Attr)var23.item(var34);
               var17 = var35.getNamespaceURI();
               if (var17 == null) {
                  if (var35.getLocalName() == null) {
                  }
               } else {
                  String var18 = var35.getPrefix();
                  if ((var18 == null || !var18.equals("xml") && !var18.equals("xmlns")) && !var17.equals("http://www.w3.org/2000/xmlns/")) {
                     String var19 = var18 == null ? null : var9.lookupNamespaceURI(var18);
                     if (var18 == null || var19 == null || !var19.equals(var17)) {
                        String var20 = var9.lookupPrefix(var17);
                        if (var20 != null) {
                           var35.setPrefix(var20);
                        } else if (var18 != null && var9.lookupNamespaceURI(var18) == null) {
                           var1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var18, var17);
                        } else {
                           byte var21 = 1;

                           do {
                              var20 = "NS" + var21;
                           } while(var9.lookupPrefix(var20) != null);

                           var1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var20, var17);
                           var35.setPrefix(var20);
                        }
                     }
                  }
               }
            }
         }

         var23 = var1.getAttributes();

         for(var28 = 0; var28 < var23.getLength(); ++var28) {
            var31 = (Attr)var23.item(var28);
            if (!this.checkName(var31.getNodeName()) && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character-in-node-name", (short)2, "wf.invalid.name", new Object[]{var31.getNodeName()}, var31, (Exception)null))) {
               return false;
            }

            if (!this.checkChars(var31.getNodeValue()) && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character", (short)2, "wf.invalid.character", new Object[]{new Integer(2), var31.getNodeName(), var31.getNodeValue()}, var31, (Exception)null))) {
               return false;
            }
         }

         for(var14 = var1.getFirstChild(); var14 != null; var14 = var14.getNextSibling()) {
            short var36 = var14.getNodeType();
            switch (var36) {
               case 1:
                  if (!this.checkName(var14.getNodeName()) && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character-in-node-name", (short)2, "wf.invalid.name", new Object[]{var14.getNodeName()}, var14, (Exception)null))) {
                     return false;
                  }

                  if (!this.normalizeDocument((Element)var14, var2, var3, var4, var5, var6, var7, var8)) {
                     return false;
                  }
               case 2:
               case 5:
               case 6:
               default:
                  break;
               case 3:
                  var33 = var14.getNodeValue();
                  if (!this.checkChars(var33) && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character", (short)2, "wf.invalid.character", new Object[]{new Integer(var14.getNodeType()), var14.getNodeName(), var33}, var14, (Exception)null))) {
                     return false;
                  }
                  break;
               case 4:
                  var33 = var14.getNodeValue();
                  if ((!this.checkChars(var33) || var33.indexOf("]]>") != -1) && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character", (short)2, "wf.invalid.character", new Object[]{new Integer(var14.getNodeType()), var14.getNodeName(), var33}, var14, (Exception)null))) {
                     return false;
                  }
                  break;
               case 7:
                  if (var14.getNodeName().equalsIgnoreCase("xml") && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character-in-node-name", (short)2, "wf.invalid.name", new Object[]{var14.getNodeName()}, var14, (Exception)null))) {
                     return false;
                  }

                  var33 = var14.getNodeValue();
                  if ((!this.checkChars(var33) || var33.indexOf("?>") != -1) && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character", (short)2, "wf.invalid.character", new Object[]{new Integer(var14.getNodeType()), var14.getNodeName(), var33}, var14, (Exception)null))) {
                     return false;
                  }
                  break;
               case 8:
                  var33 = var14.getNodeValue();
                  if ((!this.checkChars(var33) || var33.indexOf("--") != -1 || var33.charAt(var33.length() - 1) == '-') && var8 != null && !var8.handleError(this.createDOMError("wf-invalid-character", (short)2, "wf.invalid.character", new Object[]{new Integer(var14.getNodeType()), var14.getNodeName(), var33}, var14, (Exception)null))) {
                     return false;
                  }
            }
         }

         return true;
      }
   }

   protected boolean splitCdata(Element var1, Node var2, DOMErrorHandler var3) {
      String var4 = var2.getNodeValue();
      int var5 = var4.indexOf("]]>");
      if (var5 != -1) {
         String var6 = var4.substring(0, var5 + 2);
         String var7 = var4.substring(var5 + 2);
         var2.setNodeValue(var6);
         Node var8 = var2.getNextSibling();
         if (var8 == null) {
            var1.appendChild(this.createCDATASection(var7));
         } else {
            var1.insertBefore(this.createCDATASection(var7), var8);
         }

         if (var3 != null && !var3.handleError(this.createDOMError("cdata-sections-splitted", (short)1, "cdata.section.split", new Object[0], var2, (Exception)null))) {
            return false;
         }
      }

      return true;
   }

   protected boolean checkChars(String var1) {
      int var2 = var1.length();
      int var3;
      if (this.xmlVersion.equals("1.1")) {
         for(var3 = 0; var3 < var2; ++var3) {
            if (!DOMUtilities.isXML11Character(var1.charAt(var3))) {
               return false;
            }
         }
      } else {
         for(var3 = 0; var3 < var2; ++var3) {
            if (!DOMUtilities.isXMLCharacter(var1.charAt(var3))) {
               return false;
            }
         }
      }

      return true;
   }

   protected boolean checkName(String var1) {
      return this.xmlVersion.equals("1.1") ? DOMUtilities.isValidName11(var1) : DOMUtilities.isValidName(var1);
   }

   protected DOMError createDOMError(String var1, short var2, String var3, Object[] var4, Node var5, Exception var6) {
      try {
         return new DocumentError(var1, var2, this.getCurrentDocument().formatMessage(var3, var4), var5, var6);
      } catch (Exception var8) {
         return new DocumentError(var1, var2, var3, var5, var6);
      }
   }

   public void setTextContent(String var1) throws DOMException {
   }

   public void setXBLManager(XBLManager var1) {
      boolean var2 = this.xblManager.isProcessing();
      this.xblManager.stopProcessing();
      if (var1 == null) {
         var1 = new GenericXBLManager();
      }

      this.xblManager = (XBLManager)var1;
      if (var2) {
         this.xblManager.startProcessing();
      }

   }

   public XBLManager getXBLManager() {
      return this.xblManager;
   }

   public XPathExpression createExpression(String var1, XPathNSResolver var2) throws DOMException, XPathException {
      return new XPathExpr(var1, var2);
   }

   public XPathNSResolver createNSResolver(Node var1) {
      return new XPathNodeNSResolver(var1);
   }

   public Object evaluate(String var1, Node var2, XPathNSResolver var3, short var4, Object var5) throws XPathException, DOMException {
      XPathExpression var6 = this.createExpression(var1, var3);
      return var6.evaluate(var2, var4, var5);
   }

   public XPathException createXPathException(short var1, String var2, Object[] var3) {
      try {
         return new XPathException(var1, this.formatMessage(var2, var3));
      } catch (Exception var5) {
         return new XPathException(var1, var2);
      }
   }

   public Node getXblParentNode() {
      return this.xblManager.getXblParentNode(this);
   }

   public NodeList getXblChildNodes() {
      return this.xblManager.getXblChildNodes(this);
   }

   public NodeList getXblScopedChildNodes() {
      return this.xblManager.getXblScopedChildNodes(this);
   }

   public Node getXblFirstChild() {
      return this.xblManager.getXblFirstChild(this);
   }

   public Node getXblLastChild() {
      return this.xblManager.getXblLastChild(this);
   }

   public Node getXblPreviousSibling() {
      return this.xblManager.getXblPreviousSibling(this);
   }

   public Node getXblNextSibling() {
      return this.xblManager.getXblNextSibling(this);
   }

   public Element getXblFirstElementChild() {
      return this.xblManager.getXblFirstElementChild(this);
   }

   public Element getXblLastElementChild() {
      return this.xblManager.getXblLastElementChild(this);
   }

   public Element getXblPreviousElementSibling() {
      return this.xblManager.getXblPreviousElementSibling(this);
   }

   public Element getXblNextElementSibling() {
      return this.xblManager.getXblNextElementSibling(this);
   }

   public Element getXblBoundElement() {
      return this.xblManager.getXblBoundElement(this);
   }

   public Element getXblShadowTree() {
      return this.xblManager.getXblShadowTree(this);
   }

   public NodeList getXblDefinitions() {
      return this.xblManager.getXblDefinitions(this);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.implementation.getClass().getName());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.localizableSupport = new LocalizableSupport("org.apache.batik.dom.resources.Messages", this.getClass().getClassLoader());
      Class var2 = Class.forName((String)var1.readObject());

      try {
         Method var3 = var2.getMethod("getDOMImplementation", (Class[])null);
         this.implementation = (DOMImplementation)var3.invoke((Object)null, (Object[])null);
      } catch (Exception var6) {
         try {
            this.implementation = (DOMImplementation)var2.newInstance();
         } catch (Exception var5) {
         }
      }

   }

   protected class XPathNodeNSResolver implements XPathNSResolver {
      protected Node contextNode;

      public XPathNodeNSResolver(Node var2) {
         this.contextNode = var2;
      }

      public String lookupNamespaceURI(String var1) {
         return ((AbstractNode)this.contextNode).lookupNamespaceURI(var1);
      }
   }

   protected class XPathExpr implements XPathExpression {
      protected XPath xpath;
      protected XPathNSResolver resolver;
      protected NSPrefixResolver prefixResolver;
      protected XPathContext context;

      public XPathExpr(String var2, XPathNSResolver var3) throws DOMException, XPathException {
         this.resolver = var3;
         this.prefixResolver = new NSPrefixResolver();

         try {
            this.xpath = new XPath(var2, (SourceLocator)null, this.prefixResolver, 0);
            this.context = new XPathContext();
         } catch (TransformerException var5) {
            throw AbstractDocument.this.createXPathException((short)51, "xpath.invalid.expression", new Object[]{var2, var5.getMessage()});
         }
      }

      public Object evaluate(Node var1, short var2, Object var3) throws XPathException, DOMException {
         if ((var1.getNodeType() == 9 || var1.getOwnerDocument() == AbstractDocument.this) && (var1.getNodeType() != 9 || var1 == AbstractDocument.this)) {
            if (var2 >= 0 && var2 <= 9) {
               switch (var1.getNodeType()) {
                  case 5:
                  case 6:
                  case 10:
                  case 11:
                  case 12:
                     throw AbstractDocument.this.createDOMException((short)9, "xpath.invalid.context.node", new Object[]{new Integer(var1.getNodeType()), var1.getNodeName()});
                  case 7:
                  case 8:
                  case 9:
                  default:
                     this.context.reset();
                     XObject var4 = null;

                     try {
                        var4 = this.xpath.execute(this.context, var1, this.prefixResolver);
                     } catch (TransformerException var7) {
                        throw AbstractDocument.this.createXPathException((short)51, "xpath.error", new Object[]{this.xpath.getPatternString(), var7.getMessage()});
                     }

                     try {
                        switch (var2) {
                           case 0:
                              switch (var4.getType()) {
                                 case 1:
                                    return this.convertBoolean(var4);
                                 case 2:
                                    return this.convertNumber(var4);
                                 case 3:
                                    return this.convertString(var4);
                                 case 4:
                                    return this.convertNodeIterator(var4, (short)4);
                              }
                           default:
                              return null;
                           case 1:
                              return this.convertNumber(var4);
                           case 2:
                              return this.convertString(var4);
                           case 3:
                              return this.convertBoolean(var4);
                           case 4:
                           case 5:
                           case 6:
                           case 7:
                              return this.convertNodeIterator(var4, var2);
                           case 8:
                           case 9:
                              return this.convertSingleNode(var4, var2);
                        }
                     } catch (TransformerException var6) {
                        throw AbstractDocument.this.createXPathException((short)52, "xpath.cannot.convert.result", new Object[]{new Integer(var2), var6.getMessage()});
                     }
               }
            } else {
               throw AbstractDocument.this.createDOMException((short)9, "xpath.invalid.result.type", new Object[]{new Integer(var2)});
            }
         } else {
            throw AbstractDocument.this.createDOMException((short)4, "node.from.wrong.document", new Object[]{new Integer(var1.getNodeType()), var1.getNodeName()});
         }
      }

      protected Result convertSingleNode(XObject var1, short var2) throws TransformerException {
         return new Result(var1.nodelist().item(0), var2);
      }

      protected Result convertBoolean(XObject var1) throws TransformerException {
         return new Result(var1.bool());
      }

      protected Result convertNumber(XObject var1) throws TransformerException {
         return new Result(var1.num());
      }

      protected Result convertString(XObject var1) {
         return new Result(var1.str());
      }

      protected Result convertNodeIterator(XObject var1, short var2) throws TransformerException {
         return new Result(var1.nodelist(), var2);
      }

      protected class NSPrefixResolver implements PrefixResolver {
         public String getBaseIdentifier() {
            return null;
         }

         public String getNamespaceForPrefix(String var1) {
            return XPathExpr.this.resolver == null ? null : XPathExpr.this.resolver.lookupNamespaceURI(var1);
         }

         public String getNamespaceForPrefix(String var1, Node var2) {
            return XPathExpr.this.resolver == null ? null : XPathExpr.this.resolver.lookupNamespaceURI(var1);
         }

         public boolean handlesNullPrefixes() {
            return false;
         }
      }

      public class Result implements XPathResult {
         protected short resultType;
         protected double numberValue;
         protected String stringValue;
         protected boolean booleanValue;
         protected Node singleNodeValue;
         protected NodeList iterator;
         protected int iteratorPosition;

         public Result(Node var2, short var3) {
            this.resultType = var3;
            this.singleNodeValue = var2;
         }

         public Result(boolean var2) throws TransformerException {
            this.resultType = 3;
            this.booleanValue = var2;
         }

         public Result(double var2) throws TransformerException {
            this.resultType = 1;
            this.numberValue = var2;
         }

         public Result(String var2) {
            this.resultType = 2;
            this.stringValue = var2;
         }

         public Result(NodeList var2, short var3) {
            this.resultType = var3;
            this.iterator = var2;
         }

         public short getResultType() {
            return this.resultType;
         }

         public boolean getBooleanValue() {
            if (this.resultType != 3) {
               throw AbstractDocument.this.createXPathException((short)52, "xpath.invalid.result.type", new Object[]{new Integer(this.resultType)});
            } else {
               return this.booleanValue;
            }
         }

         public double getNumberValue() {
            if (this.resultType != 1) {
               throw AbstractDocument.this.createXPathException((short)52, "xpath.invalid.result.type", new Object[]{new Integer(this.resultType)});
            } else {
               return this.numberValue;
            }
         }

         public String getStringValue() {
            if (this.resultType != 2) {
               throw AbstractDocument.this.createXPathException((short)52, "xpath.invalid.result.type", new Object[]{new Integer(this.resultType)});
            } else {
               return this.stringValue;
            }
         }

         public Node getSingleNodeValue() {
            if (this.resultType != 8 && this.resultType != 9) {
               throw AbstractDocument.this.createXPathException((short)52, "xpath.invalid.result.type", new Object[]{new Integer(this.resultType)});
            } else {
               return this.singleNodeValue;
            }
         }

         public boolean getInvalidIteratorState() {
            return false;
         }

         public int getSnapshotLength() {
            if (this.resultType != 6 && this.resultType != 7) {
               throw AbstractDocument.this.createXPathException((short)52, "xpath.invalid.result.type", new Object[]{new Integer(this.resultType)});
            } else {
               return this.iterator.getLength();
            }
         }

         public Node iterateNext() {
            if (this.resultType != 4 && this.resultType != 5) {
               throw AbstractDocument.this.createXPathException((short)52, "xpath.invalid.result.type", new Object[]{new Integer(this.resultType)});
            } else {
               return this.iterator.item(this.iteratorPosition++);
            }
         }

         public Node snapshotItem(int var1) {
            if (this.resultType != 6 && this.resultType != 7) {
               throw AbstractDocument.this.createXPathException((short)52, "xpath.invalid.result.type", new Object[]{new Integer(this.resultType)});
            } else {
               return this.iterator.item(var1);
            }
         }
      }
   }

   protected class DocumentConfiguration implements DOMConfiguration {
      protected String[] booleanParamNames = new String[]{"canonical-form", "cdata-sections", "check-character-normalization", "comments", "datatype-normalization", "element-content-whitespace", "entities", "infoset", "namespaces", "namespace-declarations", "normalize-characters", "split-cdata-sections", "validate", "validate-if-schema", "well-formed"};
      protected boolean[] booleanParamValues = new boolean[]{false, true, false, true, false, false, true, false, true, true, false, true, false, false, true};
      protected boolean[] booleanParamReadOnly = new boolean[]{true, false, true, false, true, false, false, false, false, false, true, false, true, true, false};
      protected Map booleanParamIndexes = new HashMap();
      protected Object errorHandler;
      protected ParameterNameList paramNameList;

      protected DocumentConfiguration() {
         for(int var2 = 0; var2 < this.booleanParamNames.length; ++var2) {
            this.booleanParamIndexes.put(this.booleanParamNames[var2], new Integer(var2));
         }

      }

      public void setParameter(String var1, Object var2) {
         if ("error-handler".equals(var1)) {
            if (var2 != null && !(var2 instanceof DOMErrorHandler)) {
               throw AbstractDocument.this.createDOMException((short)17, "domconfig.param.type", new Object[]{var1});
            } else {
               this.errorHandler = var2;
            }
         } else {
            Integer var3 = (Integer)this.booleanParamIndexes.get(var1);
            if (var3 == null) {
               throw AbstractDocument.this.createDOMException((short)8, "domconfig.param.not.found", new Object[]{var1});
            } else if (var2 == null) {
               throw AbstractDocument.this.createDOMException((short)9, "domconfig.param.value", new Object[]{var1});
            } else if (!(var2 instanceof Boolean)) {
               throw AbstractDocument.this.createDOMException((short)17, "domconfig.param.type", new Object[]{var1});
            } else {
               int var4 = var3;
               boolean var5 = (Boolean)var2;
               if (this.booleanParamReadOnly[var4] && this.booleanParamValues[var4] != var5) {
                  throw AbstractDocument.this.createDOMException((short)9, "domconfig.param.value", new Object[]{var1});
               } else {
                  this.booleanParamValues[var4] = var5;
                  if (var1.equals("infoset")) {
                     this.setParameter("validate-if-schema", Boolean.FALSE);
                     this.setParameter("entities", Boolean.FALSE);
                     this.setParameter("datatype-normalization", Boolean.FALSE);
                     this.setParameter("cdata-sections", Boolean.FALSE);
                     this.setParameter("well-formed", Boolean.TRUE);
                     this.setParameter("element-content-whitespace", Boolean.TRUE);
                     this.setParameter("comments", Boolean.TRUE);
                     this.setParameter("namespaces", Boolean.TRUE);
                  }

               }
            }
         }
      }

      public Object getParameter(String var1) {
         if ("error-handler".equals(var1)) {
            return this.errorHandler;
         } else {
            Integer var2 = (Integer)this.booleanParamIndexes.get(var1);
            if (var2 == null) {
               throw AbstractDocument.this.createDOMException((short)8, "domconfig.param.not.found", new Object[]{var1});
            } else {
               return this.booleanParamValues[var2] ? Boolean.TRUE : Boolean.FALSE;
            }
         }
      }

      public boolean getBooleanParameter(String var1) {
         Boolean var2 = (Boolean)this.getParameter(var1);
         return var2;
      }

      public boolean canSetParameter(String var1, Object var2) {
         if (var1.equals("error-handler")) {
            return var2 == null || var2 instanceof DOMErrorHandler;
         } else {
            Integer var3 = (Integer)this.booleanParamIndexes.get(var1);
            if (var3 != null && var2 != null && var2 instanceof Boolean) {
               int var4 = var3;
               boolean var5 = (Boolean)var2;
               return !this.booleanParamReadOnly[var4] || this.booleanParamValues[var4] == var5;
            } else {
               return false;
            }
         }
      }

      public DOMStringList getParameterNames() {
         if (this.paramNameList == null) {
            this.paramNameList = new ParameterNameList();
         }

         return this.paramNameList;
      }

      protected class ParameterNameList implements DOMStringList {
         public String item(int var1) {
            if (var1 < 0) {
               return null;
            } else if (var1 < DocumentConfiguration.this.booleanParamNames.length) {
               return DocumentConfiguration.this.booleanParamNames[var1];
            } else {
               return var1 == DocumentConfiguration.this.booleanParamNames.length ? "error-handler" : null;
            }
         }

         public int getLength() {
            return DocumentConfiguration.this.booleanParamNames.length + 1;
         }

         public boolean contains(String var1) {
            if ("error-handler".equals(var1)) {
               return true;
            } else {
               for(int var2 = 0; var2 < DocumentConfiguration.this.booleanParamNames.length; ++var2) {
                  if (DocumentConfiguration.this.booleanParamNames[var2].equals(var1)) {
                     return true;
                  }
               }

               return false;
            }
         }
      }
   }

   protected class DocumentError implements DOMError {
      protected String type;
      protected short severity;
      protected String message;
      protected Node relatedNode;
      protected Object relatedException;
      protected DOMLocator domLocator;

      public DocumentError(String var2, short var3, String var4, Node var5, Exception var6) {
         this.type = var2;
         this.severity = var3;
         this.message = var4;
         this.relatedNode = var5;
         this.relatedException = var6;
      }

      public String getType() {
         return this.type;
      }

      public short getSeverity() {
         return this.severity;
      }

      public String getMessage() {
         return this.message;
      }

      public Object getRelatedData() {
         return this.relatedNode;
      }

      public Object getRelatedException() {
         return this.relatedException;
      }

      public DOMLocator getLocation() {
         if (this.domLocator == null) {
            this.domLocator = new ErrorLocation(this.relatedNode);
         }

         return this.domLocator;
      }

      protected class ErrorLocation implements DOMLocator {
         protected Node node;

         public ErrorLocation(Node var2) {
            this.node = var2;
         }

         public int getLineNumber() {
            return -1;
         }

         public int getColumnNumber() {
            return -1;
         }

         public int getByteOffset() {
            return -1;
         }

         public int getUtf16Offset() {
            return -1;
         }

         public Node getRelatedNode() {
            return this.node;
         }

         public String getUri() {
            AbstractDocument var1 = (AbstractDocument)this.node.getOwnerDocument();
            return var1.getDocumentURI();
         }
      }
   }

   protected class IdSoftRef extends CleanerThread.SoftReferenceCleared {
      String id;
      List list;

      IdSoftRef(Object var2, String var3) {
         super(var2);
         this.id = var3;
      }

      IdSoftRef(Object var2, String var3, List var4) {
         super(var2);
         this.id = var3;
         this.list = var4;
      }

      public void setList(List var1) {
         this.list = var1;
      }

      public void cleared() {
         if (AbstractDocument.this.elementsById != null) {
            synchronized(AbstractDocument.this.elementsById) {
               if (this.list != null) {
                  this.list.remove(this);
               } else {
                  Object var2 = AbstractDocument.this.elementsById.remove(this.id);
                  if (var2 != this) {
                     AbstractDocument.this.elementsById.put(this.id, var2);
                  }
               }

            }
         }
      }
   }
}
