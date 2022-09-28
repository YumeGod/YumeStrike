package org.apache.batik.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.dom.events.DOMMutationEvent;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.XBLManagerData;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;

public abstract class AbstractNode implements ExtendedNode, NodeXBL, XBLManagerData, Serializable {
   public static final NodeList EMPTY_NODE_LIST = new NodeList() {
      public Node item(int var1) {
         return null;
      }

      public int getLength() {
         return 0;
      }
   };
   protected AbstractDocument ownerDocument;
   protected transient EventSupport eventSupport;
   protected HashMap userData;
   protected HashMap userDataHandlers;
   protected Object managerData;
   public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
   public static final short DOCUMENT_POSITION_PRECEDING = 2;
   public static final short DOCUMENT_POSITION_FOLLOWING = 4;
   public static final short DOCUMENT_POSITION_CONTAINS = 8;
   public static final short DOCUMENT_POSITION_CONTAINED_BY = 16;
   public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;

   public void setNodeName(String var1) {
   }

   public void setOwnerDocument(Document var1) {
      this.ownerDocument = (AbstractDocument)var1;
   }

   public void setSpecified(boolean var1) {
      throw this.createDOMException((short)11, "node.type", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public String getNodeValue() throws DOMException {
      return null;
   }

   public void setNodeValue(String var1) throws DOMException {
   }

   public Node getParentNode() {
      return null;
   }

   public void setParentNode(Node var1) {
      throw this.createDOMException((short)3, "parent.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public NodeList getChildNodes() {
      return EMPTY_NODE_LIST;
   }

   public Node getFirstChild() {
      return null;
   }

   public Node getLastChild() {
      return null;
   }

   public void setPreviousSibling(Node var1) {
      throw this.createDOMException((short)3, "sibling.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public Node getPreviousSibling() {
      return null;
   }

   public void setNextSibling(Node var1) {
      throw this.createDOMException((short)3, "sibling.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public Node getNextSibling() {
      return null;
   }

   public boolean hasAttributes() {
      return false;
   }

   public NamedNodeMap getAttributes() {
      return null;
   }

   public Document getOwnerDocument() {
      return this.ownerDocument;
   }

   public String getNamespaceURI() {
      return null;
   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      throw this.createDOMException((short)3, "children.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      throw this.createDOMException((short)3, "children.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public Node removeChild(Node var1) throws DOMException {
      throw this.createDOMException((short)3, "children.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public Node appendChild(Node var1) throws DOMException {
      throw this.createDOMException((short)3, "children.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public boolean hasChildNodes() {
      return false;
   }

   public Node cloneNode(boolean var1) {
      Node var2 = var1 ? this.deepCopyInto(this.newNode()) : this.copyInto(this.newNode());
      this.fireUserDataHandlers((short)1, this, var2);
      return var2;
   }

   public void normalize() {
   }

   public boolean isSupported(String var1, String var2) {
      return this.getCurrentDocument().getImplementation().hasFeature(var1, var2);
   }

   public String getPrefix() {
      return this.getNamespaceURI() == null ? null : DOMUtilities.getPrefix(this.getNodeName());
   }

   public void setPrefix(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         String var2 = this.getNamespaceURI();
         if (var2 == null) {
            throw this.createDOMException((short)14, "namespace", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
         } else {
            String var3 = this.getLocalName();
            if (var1 == null) {
               this.setNodeName(var3);
            } else if (!var1.equals("") && !DOMUtilities.isValidName(var1)) {
               throw this.createDOMException((short)5, "prefix", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var1});
            } else if (!DOMUtilities.isValidPrefix(var1)) {
               throw this.createDOMException((short)14, "prefix", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var1});
            } else if ((!var1.equals("xml") || "http://www.w3.org/XML/1998/namespace".equals(var2)) && (!var1.equals("xmlns") || "http://www.w3.org/2000/xmlns/".equals(var2))) {
               this.setNodeName(var1 + ':' + var3);
            } else {
               throw this.createDOMException((short)14, "namespace.uri", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var2});
            }
         }
      }
   }

   public String getLocalName() {
      return this.getNamespaceURI() == null ? null : DOMUtilities.getLocalName(this.getNodeName());
   }

   public DOMException createDOMException(short var1, String var2, Object[] var3) {
      try {
         return new DOMException(var1, this.getCurrentDocument().formatMessage(var2, var3));
      } catch (Exception var5) {
         return new DOMException(var1, var2);
      }
   }

   protected String getCascadedXMLBase(Node var1) {
      String var2 = null;

      for(Node var3 = var1.getParentNode(); var3 != null; var3 = var3.getParentNode()) {
         if (var3.getNodeType() == 1) {
            var2 = this.getCascadedXMLBase(var3);
            break;
         }
      }

      if (var2 == null) {
         AbstractDocument var4;
         if (var1.getNodeType() == 9) {
            var4 = (AbstractDocument)var1;
         } else {
            var4 = (AbstractDocument)var1.getOwnerDocument();
         }

         var2 = var4.getDocumentURI();
      }

      while(var1 != null && var1.getNodeType() != 1) {
         var1 = var1.getParentNode();
      }

      if (var1 == null) {
         return var2;
      } else {
         Element var6 = (Element)var1;
         Attr var5 = var6.getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "base");
         if (var5 != null) {
            if (var2 == null) {
               var2 = var5.getNodeValue();
            } else {
               var2 = (new ParsedURL(var2, var5.getNodeValue())).toString();
            }
         }

         return var2;
      }
   }

   public String getBaseURI() {
      return this.getCascadedXMLBase(this);
   }

   public static String getBaseURI(Node var0) {
      return ((AbstractNode)var0).getBaseURI();
   }

   public short compareDocumentPosition(Node var1) throws DOMException {
      if (this == var1) {
         return 0;
      } else {
         ArrayList var2 = new ArrayList(10);
         ArrayList var3 = new ArrayList(10);
         int var4 = 0;
         int var5 = 0;
         Object var6;
         if (this.getNodeType() == 2) {
            var2.add(this);
            ++var4;
            var6 = ((Attr)this).getOwnerElement();
            if (var1.getNodeType() == 2) {
               Attr var7 = (Attr)var1;
               if (var6 == var7.getOwnerElement()) {
                  if (this.hashCode() < ((Attr)var1).hashCode()) {
                     return 34;
                  }

                  return 36;
               }
            }
         } else {
            var6 = this;
         }

         while(var6 != null) {
            if (var6 == var1) {
               return 20;
            }

            var2.add(var6);
            ++var4;
            var6 = ((Node)var6).getParentNode();
         }

         if (var1.getNodeType() == 2) {
            var3.add(var1);
            ++var5;
            var6 = ((Attr)var1).getOwnerElement();
         } else {
            var6 = var1;
         }

         while(var6 != null) {
            if (var6 == this) {
               return 10;
            }

            var3.add(var6);
            ++var5;
            var6 = ((Node)var6).getParentNode();
         }

         int var11 = var4 - 1;
         int var8 = var5 - 1;
         if (var2.get(var11) != var3.get(var8)) {
            return (short)(this.hashCode() < var1.hashCode() ? 35 : 37);
         } else {
            Object var9 = var2.get(var11);

            Object var10;
            for(var10 = var3.get(var8); var9 == var10; var10 = var3.get(var8)) {
               var6 = (Node)var9;
               --var11;
               var9 = var2.get(var11);
               --var8;
            }

            for(Node var12 = ((Node)var6).getFirstChild(); var12 != null; var12 = var12.getNextSibling()) {
               if (var12 == var9) {
                  return 2;
               }

               if (var12 == var10) {
                  return 4;
               }
            }

            return 1;
         }
      }
   }

   public String getTextContent() {
      return null;
   }

   public void setTextContent(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         if (this.getNodeType() != 10) {
            while(this.getFirstChild() != null) {
               this.removeChild(this.getFirstChild());
            }

            this.appendChild(this.getOwnerDocument().createTextNode(var1));
         }

      }
   }

   public boolean isSameNode(Node var1) {
      return this == var1;
   }

   public String lookupPrefix(String var1) {
      if (var1 != null && var1.length() != 0) {
         short var2 = this.getNodeType();
         switch (var2) {
            case 1:
               return this.lookupNamespacePrefix(var1, (Element)this);
            case 2:
               AbstractNode var4 = (AbstractNode)((Attr)this).getOwnerElement();
               if (var4 != null) {
                  return var4.lookupPrefix(var1);
               }

               return null;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            default:
               for(Node var5 = this.getParentNode(); var5 != null; var5 = var5.getParentNode()) {
                  if (var5.getNodeType() == 1) {
                     return ((AbstractNode)var5).lookupPrefix(var1);
                  }
               }

               return null;
            case 6:
            case 10:
            case 11:
            case 12:
               return null;
            case 9:
               AbstractNode var3 = (AbstractNode)((Document)this).getDocumentElement();
               return var3.lookupPrefix(var1);
         }
      } else {
         return null;
      }
   }

   protected String lookupNamespacePrefix(String var1, Element var2) {
      String var3 = var2.getNamespaceURI();
      String var4 = var2.getPrefix();
      if (var3 != null && var3.equals(var1) && var4 != null) {
         String var5 = ((AbstractNode)var2).lookupNamespaceURI(var4);
         if (var5 != null && var5.equals(var1)) {
            return var4;
         }
      }

      NamedNodeMap var11 = var2.getAttributes();
      if (var11 != null) {
         for(int var6 = 0; var6 < var11.getLength(); ++var6) {
            Node var7 = var11.item(var6);
            if ("xmlns".equals(var7.getPrefix()) && var7.getNodeValue().equals(var1)) {
               String var8 = var7.getLocalName();
               AbstractNode var9 = (AbstractNode)var2;
               String var10 = var9.lookupNamespaceURI(var8);
               if (var10 != null && var10.equals(var1)) {
                  return var8;
               }
            }
         }
      }

      for(Node var12 = this.getParentNode(); var12 != null; var12 = var12.getParentNode()) {
         if (var12.getNodeType() == 1) {
            return ((AbstractNode)var12).lookupNamespacePrefix(var1, var2);
         }
      }

      return null;
   }

   public boolean isDefaultNamespace(String var1) {
      switch (this.getNodeType()) {
         case 1:
            if (this.getPrefix() == null) {
               String var7 = this.getNamespaceURI();
               return var7 == null && var1 == null || var7 != null && var7.equals(var1);
            } else {
               NamedNodeMap var4 = this.getAttributes();
               if (var4 != null) {
                  for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                     Node var6 = var4.item(var5);
                     if ("xmlns".equals(var6.getLocalName())) {
                        return var6.getNodeValue().equals(var1);
                     }
                  }
               }
            }
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         default:
            for(Object var8 = this; var8 != null; var8 = ((Node)var8).getParentNode()) {
               if (((Node)var8).getNodeType() == 1) {
                  AbstractNode var9 = (AbstractNode)var8;
                  return var9.isDefaultNamespace(var1);
               }
            }

            return false;
         case 2:
            AbstractNode var3 = (AbstractNode)((Attr)this).getOwnerElement();
            if (var3 != null) {
               return var3.isDefaultNamespace(var1);
            }

            return false;
         case 6:
         case 10:
         case 11:
         case 12:
            return false;
         case 9:
            AbstractNode var2 = (AbstractNode)((Document)this).getDocumentElement();
            return var2.isDefaultNamespace(var1);
      }
   }

   public String lookupNamespaceURI(String var1) {
      label57:
      switch (this.getNodeType()) {
         case 1:
            NamedNodeMap var4 = this.getAttributes();
            if (var4 == null) {
               break;
            }

            int var5 = 0;

            while(true) {
               if (var5 >= var4.getLength()) {
                  break label57;
               }

               Node var6 = var4.item(var5);
               String var7 = var6.getPrefix();
               String var8 = var6.getLocalName();
               if (var8 == null) {
                  var8 = var6.getNodeName();
               }

               if ("xmlns".equals(var7) && this.compareStrings(var8, var1) || "xmlns".equals(var8) && var1 == null) {
                  String var9 = var6.getNodeValue();
                  return var9.length() > 0 ? var9 : null;
               }

               ++var5;
            }
         case 2:
            AbstractNode var3 = (AbstractNode)((Attr)this).getOwnerElement();
            if (var3 != null) {
               return var3.lookupNamespaceURI(var1);
            }

            return null;
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         default:
            break;
         case 6:
         case 10:
         case 11:
         case 12:
            return null;
         case 9:
            AbstractNode var2 = (AbstractNode)((Document)this).getDocumentElement();
            return var2.lookupNamespaceURI(var1);
      }

      for(Node var10 = this.getParentNode(); var10 != null; var10 = var10.getParentNode()) {
         if (var10.getNodeType() == 1) {
            AbstractNode var11 = (AbstractNode)var10;
            return var11.lookupNamespaceURI(var1);
         }
      }

      return null;
   }

   public boolean isEqualNode(Node var1) {
      if (var1 == null) {
         return false;
      } else {
         short var2 = var1.getNodeType();
         if (var2 == this.getNodeType() && this.compareStrings(this.getNodeName(), var1.getNodeName()) && this.compareStrings(this.getLocalName(), var1.getLocalName()) && this.compareStrings(this.getPrefix(), var1.getPrefix()) && this.compareStrings(this.getNodeValue(), var1.getNodeValue()) && this.compareStrings(this.getNodeValue(), var1.getNodeValue()) && this.compareNamedNodeMaps(this.getAttributes(), var1.getAttributes())) {
            if (var2 == 10) {
               DocumentType var3 = (DocumentType)this;
               DocumentType var4 = (DocumentType)var1;
               if (!this.compareStrings(var3.getPublicId(), var4.getPublicId()) || !this.compareStrings(var3.getSystemId(), var4.getSystemId()) || !this.compareStrings(var3.getInternalSubset(), var4.getInternalSubset()) || !this.compareNamedNodeMaps(var3.getEntities(), var4.getEntities()) || !this.compareNamedNodeMaps(var3.getNotations(), var4.getNotations())) {
                  return false;
               }
            }

            Node var5 = this.getFirstChild();
            Node var6 = var1.getFirstChild();
            if (var5 != null && var6 != null && !((AbstractNode)var5).isEqualNode(var6)) {
               return false;
            } else {
               return var5 == var6;
            }
         } else {
            return false;
         }
      }
   }

   protected boolean compareStrings(String var1, String var2) {
      return var1 != null && var1.equals(var2) || var1 == null && var2 == null;
   }

   protected boolean compareNamedNodeMaps(NamedNodeMap var1, NamedNodeMap var2) {
      if (var1 == null && var2 != null || var1 != null && var2 == null) {
         return false;
      } else {
         if (var1 != null) {
            int var3 = var1.getLength();
            if (var3 != var2.getLength()) {
               return false;
            }

            for(int var4 = 0; var4 < var3; ++var4) {
               Node var5 = var1.item(var4);
               String var6 = var5.getLocalName();
               Node var7;
               if (var6 != null) {
                  var7 = var2.getNamedItemNS(var5.getNamespaceURI(), var6);
               } else {
                  var7 = var2.getNamedItem(var5.getNodeName());
               }

               if (!((AbstractNode)var5).isEqualNode(var7)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public Object getFeature(String var1, String var2) {
      return null;
   }

   public Object getUserData(String var1) {
      return this.userData == null ? null : this.userData.get(var1);
   }

   public Object setUserData(String var1, Object var2, UserDataHandler var3) {
      if (this.userData == null) {
         this.userData = new HashMap();
         this.userDataHandlers = new HashMap();
      }

      if (var2 == null) {
         this.userData.remove(var1);
         return this.userDataHandlers.remove(var1);
      } else {
         this.userDataHandlers.put(var1, var3);
         return this.userData.put(var1, var2);
      }
   }

   protected void fireUserDataHandlers(short var1, Node var2, Node var3) {
      AbstractNode var4 = (AbstractNode)var2;
      if (var4.userData != null) {
         Iterator var5 = var4.userData.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            UserDataHandler var7 = (UserDataHandler)var4.userDataHandlers.get(var6.getKey());
            if (var7 != null) {
               var7.handle(var1, (String)var6.getKey(), var6.getValue(), var2, var3);
            }
         }
      }

   }

   public void addEventListener(String var1, EventListener var2, boolean var3) {
      if (this.eventSupport == null) {
         this.initializeEventSupport();
      }

      this.eventSupport.addEventListener(var1, var2, var3);
   }

   public void addEventListenerNS(String var1, String var2, EventListener var3, boolean var4, Object var5) {
      if (this.eventSupport == null) {
         this.initializeEventSupport();
      }

      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      this.eventSupport.addEventListenerNS(var1, var2, var3, var4, var5);
   }

   public void removeEventListener(String var1, EventListener var2, boolean var3) {
      if (this.eventSupport != null) {
         this.eventSupport.removeEventListener(var1, var2, var3);
      }

   }

   public void removeEventListenerNS(String var1, String var2, EventListener var3, boolean var4) {
      if (this.eventSupport != null) {
         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         this.eventSupport.removeEventListenerNS(var1, var2, var3, var4);
      }

   }

   public NodeEventTarget getParentNodeEventTarget() {
      return (NodeEventTarget)this.getXblParentNode();
   }

   public boolean dispatchEvent(Event var1) throws EventException {
      if (this.eventSupport == null) {
         this.initializeEventSupport();
      }

      return this.eventSupport.dispatchEvent(this, var1);
   }

   public boolean willTriggerNS(String var1, String var2) {
      return true;
   }

   public boolean hasEventListenerNS(String var1, String var2) {
      if (this.eventSupport == null) {
         return false;
      } else {
         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         return this.eventSupport.hasEventListenerNS(var1, var2);
      }
   }

   public EventSupport getEventSupport() {
      return this.eventSupport;
   }

   public EventSupport initializeEventSupport() {
      if (this.eventSupport == null) {
         AbstractDocument var1 = this.getCurrentDocument();
         AbstractDOMImplementation var2 = (AbstractDOMImplementation)var1.getImplementation();
         this.eventSupport = var2.createEventSupport(this);
         var1.setEventsEnabled(true);
      }

      return this.eventSupport;
   }

   public void fireDOMNodeInsertedIntoDocumentEvent() {
      AbstractDocument var1 = this.getCurrentDocument();
      if (var1.getEventsEnabled()) {
         DOMMutationEvent var2 = (DOMMutationEvent)var1.createEvent("MutationEvents");
         var2.initMutationEventNS("http://www.w3.org/2001/xml-events", "DOMNodeInsertedIntoDocument", true, false, (Node)null, (String)null, (String)null, (String)null, (short)2);
         this.dispatchEvent(var2);
      }

   }

   public void fireDOMNodeRemovedFromDocumentEvent() {
      AbstractDocument var1 = this.getCurrentDocument();
      if (var1.getEventsEnabled()) {
         DOMMutationEvent var2 = (DOMMutationEvent)var1.createEvent("MutationEvents");
         var2.initMutationEventNS("http://www.w3.org/2001/xml-events", "DOMNodeRemovedFromDocument", true, false, (Node)null, (String)null, (String)null, (String)null, (short)3);
         this.dispatchEvent(var2);
      }

   }

   protected void fireDOMCharacterDataModifiedEvent(String var1, String var2) {
      AbstractDocument var3 = this.getCurrentDocument();
      if (var3.getEventsEnabled()) {
         DOMMutationEvent var4 = (DOMMutationEvent)var3.createEvent("MutationEvents");
         var4.initMutationEventNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", true, false, (Node)null, var1, var2, (String)null, (short)1);
         this.dispatchEvent(var4);
      }

   }

   protected AbstractDocument getCurrentDocument() {
      return this.ownerDocument;
   }

   protected abstract Node newNode();

   protected Node export(Node var1, AbstractDocument var2) {
      AbstractNode var3 = (AbstractNode)var1;
      var3.ownerDocument = var2;
      var3.setReadonly(false);
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      AbstractNode var3 = (AbstractNode)var1;
      var3.ownerDocument = var2;
      var3.setReadonly(false);
      return var1;
   }

   protected Node copyInto(Node var1) {
      AbstractNode var2 = (AbstractNode)var1;
      var2.ownerDocument = this.ownerDocument;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      AbstractNode var2 = (AbstractNode)var1;
      var2.ownerDocument = this.ownerDocument;
      return var1;
   }

   protected void checkChildType(Node var1, boolean var2) {
      throw this.createDOMException((short)3, "children.not.allowed", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
   }

   public Node getXblParentNode() {
      return this.ownerDocument.getXBLManager().getXblParentNode(this);
   }

   public NodeList getXblChildNodes() {
      return this.ownerDocument.getXBLManager().getXblChildNodes(this);
   }

   public NodeList getXblScopedChildNodes() {
      return this.ownerDocument.getXBLManager().getXblScopedChildNodes(this);
   }

   public Node getXblFirstChild() {
      return this.ownerDocument.getXBLManager().getXblFirstChild(this);
   }

   public Node getXblLastChild() {
      return this.ownerDocument.getXBLManager().getXblLastChild(this);
   }

   public Node getXblPreviousSibling() {
      return this.ownerDocument.getXBLManager().getXblPreviousSibling(this);
   }

   public Node getXblNextSibling() {
      return this.ownerDocument.getXBLManager().getXblNextSibling(this);
   }

   public Element getXblFirstElementChild() {
      return this.ownerDocument.getXBLManager().getXblFirstElementChild(this);
   }

   public Element getXblLastElementChild() {
      return this.ownerDocument.getXBLManager().getXblLastElementChild(this);
   }

   public Element getXblPreviousElementSibling() {
      return this.ownerDocument.getXBLManager().getXblPreviousElementSibling(this);
   }

   public Element getXblNextElementSibling() {
      return this.ownerDocument.getXBLManager().getXblNextElementSibling(this);
   }

   public Element getXblBoundElement() {
      return this.ownerDocument.getXBLManager().getXblBoundElement(this);
   }

   public Element getXblShadowTree() {
      return this.ownerDocument.getXBLManager().getXblShadowTree(this);
   }

   public NodeList getXblDefinitions() {
      return this.ownerDocument.getXBLManager().getXblDefinitions(this);
   }

   public Object getManagerData() {
      return this.managerData;
   }

   public void setManagerData(Object var1) {
      this.managerData = var1;
   }
}
