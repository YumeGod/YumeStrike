package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public abstract class NodeImpl implements Node, NodeList, EventTarget, Cloneable, Serializable {
   public static final short TREE_POSITION_PRECEDING = 1;
   public static final short TREE_POSITION_FOLLOWING = 2;
   public static final short TREE_POSITION_ANCESTOR = 4;
   public static final short TREE_POSITION_DESCENDANT = 8;
   public static final short TREE_POSITION_EQUIVALENT = 16;
   public static final short TREE_POSITION_SAME_NODE = 32;
   public static final short TREE_POSITION_DISCONNECTED = 0;
   public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
   public static final short DOCUMENT_POSITION_PRECEDING = 2;
   public static final short DOCUMENT_POSITION_FOLLOWING = 4;
   public static final short DOCUMENT_POSITION_CONTAINS = 8;
   public static final short DOCUMENT_POSITION_IS_CONTAINED = 16;
   public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
   static final long serialVersionUID = -6316591992167219696L;
   public static final short ELEMENT_DEFINITION_NODE = 21;
   protected NodeImpl ownerNode;
   protected short flags;
   protected static final short READONLY = 1;
   protected static final short SYNCDATA = 2;
   protected static final short SYNCCHILDREN = 4;
   protected static final short OWNED = 8;
   protected static final short FIRSTCHILD = 16;
   protected static final short SPECIFIED = 32;
   protected static final short IGNORABLEWS = 64;
   protected static final short HASSTRING = 128;
   protected static final short NORMALIZED = 256;
   protected static final short ID = 512;

   protected NodeImpl(CoreDocumentImpl var1) {
      this.ownerNode = var1;
   }

   public NodeImpl() {
   }

   public abstract short getNodeType();

   public abstract String getNodeName();

   public String getNodeValue() throws DOMException {
      return null;
   }

   public void setNodeValue(String var1) throws DOMException {
   }

   public Node appendChild(Node var1) throws DOMException {
      return this.insertBefore(var1, (Node)null);
   }

   public Node cloneNode(boolean var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      NodeImpl var2;
      try {
         var2 = (NodeImpl)this.clone();
      } catch (CloneNotSupportedException var4) {
         throw new RuntimeException("**Internal Error**" + var4);
      }

      var2.ownerNode = this.ownerDocument();
      var2.isOwned(false);
      var2.isReadOnly(false);
      this.ownerDocument().callUserDataHandlers(this, var2, (short)1);
      return var2;
   }

   public Document getOwnerDocument() {
      return (Document)(this.isOwned() ? this.ownerNode.ownerDocument() : (Document)this.ownerNode);
   }

   CoreDocumentImpl ownerDocument() {
      return this.isOwned() ? this.ownerNode.ownerDocument() : (CoreDocumentImpl)this.ownerNode;
   }

   void setOwnerDocument(CoreDocumentImpl var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (!this.isOwned()) {
         this.ownerNode = var1;
      }

   }

   protected int getNodeNumber() {
      CoreDocumentImpl var2 = (CoreDocumentImpl)this.getOwnerDocument();
      int var1 = var2.getNodeNumber(this);
      return var1;
   }

   public Node getParentNode() {
      return null;
   }

   NodeImpl parentNode() {
      return null;
   }

   public Node getNextSibling() {
      return null;
   }

   public Node getPreviousSibling() {
      return null;
   }

   ChildNode previousSibling() {
      return null;
   }

   public NamedNodeMap getAttributes() {
      return null;
   }

   public boolean hasAttributes() {
      return false;
   }

   public boolean hasChildNodes() {
      return false;
   }

   public NodeList getChildNodes() {
      return this;
   }

   public Node getFirstChild() {
      return null;
   }

   public Node getLastChild() {
      return null;
   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null));
   }

   public Node removeChild(Node var1) throws DOMException {
      throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null));
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null));
   }

   public int getLength() {
      return 0;
   }

   public Node item(int var1) {
      return null;
   }

   public void normalize() {
   }

   public boolean isSupported(String var1, String var2) {
      return this.ownerDocument().getImplementation().hasFeature(var1, var2);
   }

   public String getNamespaceURI() {
      return null;
   }

   public String getPrefix() {
      return null;
   }

   public void setPrefix(String var1) throws DOMException {
      throw new DOMException((short)14, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null));
   }

   public String getLocalName() {
      return null;
   }

   public void addEventListener(String var1, EventListener var2, boolean var3) {
      this.ownerDocument().addEventListener(this, var1, var2, var3);
   }

   public void removeEventListener(String var1, EventListener var2, boolean var3) {
      this.ownerDocument().removeEventListener(this, var1, var2, var3);
   }

   public boolean dispatchEvent(Event var1) {
      return this.ownerDocument().dispatchEvent(this, var1);
   }

   public String getBaseURI() {
      return null;
   }

   /** @deprecated */
   public short compareTreePosition(Node var1) {
      if (this == var1) {
         return 48;
      } else {
         short var2 = this.getNodeType();
         short var3 = var1.getNodeType();
         if (var2 != 6 && var2 != 12 && var3 != 6 && var3 != 12) {
            Object var5 = this;
            Object var6 = var1;
            int var7 = 0;
            int var8 = 0;

            Object var4;
            for(var4 = this; var4 != null; var4 = ((Node)var4).getParentNode()) {
               ++var7;
               if (var4 == var1) {
                  return 5;
               }

               var5 = var4;
            }

            for(Node var16 = var1; var16 != null; var16 = var16.getParentNode()) {
               ++var8;
               if (var16 == this) {
                  return 10;
               }

               var6 = var16;
            }

            Object var9 = this;
            Object var10 = var1;
            short var11 = ((Node)var5).getNodeType();
            short var12 = ((Node)var6).getNodeType();
            if (var11 == 2) {
               var9 = ((AttrImpl)var5).getOwnerElement();
            }

            if (var12 == 2) {
               var10 = ((AttrImpl)var6).getOwnerElement();
            }

            if (var11 == 2 && var12 == 2 && var9 == var10) {
               return 16;
            } else {
               if (var11 == 2) {
                  var7 = 0;

                  for(var4 = var9; var4 != null; var4 = ((Node)var4).getParentNode()) {
                     ++var7;
                     if (var4 == var10) {
                        return 1;
                     }

                     var5 = var4;
                  }
               }

               if (var12 == 2) {
                  var8 = 0;

                  for(var4 = var10; var4 != null; var4 = ((Node)var4).getParentNode()) {
                     ++var8;
                     if (var4 == var9) {
                        return 2;
                     }

                     var6 = var4;
                  }
               }

               if (var5 != var6) {
                  return 0;
               } else {
                  int var13;
                  if (var7 > var8) {
                     for(var13 = 0; var13 < var7 - var8; ++var13) {
                        var9 = ((Node)var9).getParentNode();
                     }

                     if (var9 == var10) {
                        return 1;
                     }
                  } else {
                     for(var13 = 0; var13 < var8 - var7; ++var13) {
                        var10 = ((Node)var10).getParentNode();
                     }

                     if (var10 == var9) {
                        return 2;
                     }
                  }

                  Node var17 = ((Node)var9).getParentNode();

                  for(Node var14 = ((Node)var10).getParentNode(); var17 != var14; var14 = var14.getParentNode()) {
                     var9 = var17;
                     var10 = var14;
                     var17 = var17.getParentNode();
                  }

                  for(Node var15 = var17.getFirstChild(); var15 != null; var15 = var15.getNextSibling()) {
                     if (var15 == var10) {
                        return 1;
                     }

                     if (var15 == var9) {
                        return 2;
                     }
                  }

                  return 0;
               }
            }
         } else {
            return 0;
         }
      }
   }

   public short compareDocumentPosition(Node var1) throws DOMException {
      if (this == var1) {
         return 0;
      } else {
         try {
            NodeImpl var2 = (NodeImpl)var1;
         } catch (ClassCastException var16) {
            String var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
            throw new DOMException((short)9, var3);
         }

         Document var17;
         if (this.getNodeType() == 9) {
            var17 = (Document)this;
         } else {
            var17 = this.getOwnerDocument();
         }

         Document var18;
         if (var1.getNodeType() == 9) {
            var18 = (Document)var1;
         } else {
            var18 = var1.getOwnerDocument();
         }

         if (var17 != var18 && var17 != null && var18 != null) {
            int var20 = ((CoreDocumentImpl)var18).getNodeNumber();
            int var21 = ((CoreDocumentImpl)var17).getNodeNumber();
            return (short)(var20 > var21 ? 37 : 35);
         } else {
            Object var5 = this;
            Object var6 = var1;
            int var7 = 0;
            int var8 = 0;

            Object var4;
            for(var4 = this; var4 != null; var4 = ((Node)var4).getParentNode()) {
               ++var7;
               if (var4 == var1) {
                  return 10;
               }

               var5 = var4;
            }

            for(Node var19 = var1; var19 != null; var19 = var19.getParentNode()) {
               ++var8;
               if (var19 == this) {
                  return 20;
               }

               var6 = var19;
            }

            short var10;
            Object var11;
            Object var12;
            DocumentType var13;
            short var9 = ((Node)var5).getNodeType();
            var10 = ((Node)var6).getNodeType();
            var11 = this;
            var12 = var1;
            label203:
            switch (var9) {
               case 2:
                  var11 = ((AttrImpl)var5).getOwnerElement();
                  if (var10 == 2) {
                     var12 = ((AttrImpl)var6).getOwnerElement();
                     if (var12 == var11) {
                        if (((NamedNodeMapImpl)((Node)var11).getAttributes()).precedes(var1, this)) {
                           return 34;
                        }

                        return 36;
                     }
                  }

                  var7 = 0;
                  var4 = var11;

                  while(true) {
                     if (var4 == null) {
                        break label203;
                     }

                     ++var7;
                     if (var4 == var12) {
                        return 10;
                     }

                     var5 = var4;
                     var4 = ((Node)var4).getParentNode();
                  }
               case 6:
               case 12:
                  var13 = var17.getDoctype();
                  if (var13 == var6) {
                     return 10;
                  }

                  switch (var10) {
                     case 6:
                     case 12:
                        if (var9 != var10) {
                           return (short)(var9 > var10 ? 2 : 4);
                        }

                        if (var9 == 12) {
                           if (((NamedNodeMapImpl)var13.getNotations()).precedes((Node)var6, (Node)var5)) {
                              return 34;
                           }

                           return 36;
                        }

                        if (((NamedNodeMapImpl)var13.getEntities()).precedes((Node)var6, (Node)var5)) {
                           return 34;
                        }

                        return 36;
                     default:
                        var5 = var17;
                        var11 = var17;
                        break label203;
                  }
               case 10:
                  if (var1 == var17) {
                     return 10;
                  }

                  if (var17 != null && var17 == var18) {
                     return 4;
                  }
            }

            label194:
            switch (var10) {
               case 2:
                  var8 = 0;
                  var12 = ((AttrImpl)var6).getOwnerElement();
                  var4 = var12;

                  while(true) {
                     if (var4 == null) {
                        break label194;
                     }

                     ++var8;
                     if (var4 == var11) {
                        return 20;
                     }

                     var6 = var4;
                     var4 = ((Node)var4).getParentNode();
                  }
               case 6:
               case 12:
                  var13 = var17.getDoctype();
                  if (var13 == this) {
                     return 20;
                  }

                  var6 = var17;
                  var12 = var17;
                  break;
               case 10:
                  if (var11 == var18) {
                     return 20;
                  }

                  if (var18 != null && var17 == var18) {
                     return 2;
                  }
            }

            int var22;
            if (var5 != var6) {
               var22 = ((NodeImpl)var5).getNodeNumber();
               int var23 = ((NodeImpl)var6).getNodeNumber();
               return (short)(var22 > var23 ? 37 : 35);
            } else {
               if (var7 > var8) {
                  for(var22 = 0; var22 < var7 - var8; ++var22) {
                     var11 = ((Node)var11).getParentNode();
                  }

                  if (var11 == var12) {
                     return 2;
                  }
               } else {
                  for(var22 = 0; var22 < var8 - var7; ++var22) {
                     var12 = ((Node)var12).getParentNode();
                  }

                  if (var12 == var11) {
                     return 4;
                  }
               }

               Node var24 = ((Node)var11).getParentNode();

               for(Node var14 = ((Node)var12).getParentNode(); var24 != var14; var14 = var14.getParentNode()) {
                  var11 = var24;
                  var12 = var14;
                  var24 = var24.getParentNode();
               }

               for(Node var15 = var24.getFirstChild(); var15 != null; var15 = var15.getNextSibling()) {
                  if (var15 == var12) {
                     return 2;
                  }

                  if (var15 == var11) {
                     return 4;
                  }
               }

               return 0;
            }
         }
      }
   }

   public String getTextContent() throws DOMException {
      return this.getNodeValue();
   }

   void getTextContent(StringBuffer var1) throws DOMException {
      String var2 = this.getNodeValue();
      if (var2 != null) {
         var1.append(var2);
      }

   }

   public void setTextContent(String var1) throws DOMException {
      this.setNodeValue(var1);
   }

   public boolean isSameNode(Node var1) {
      return this == var1;
   }

   public boolean isDefaultNamespace(String var1) {
      short var2 = this.getNodeType();
      switch (var2) {
         case 1:
            String var3 = this.getNamespaceURI();
            String var4 = this.getPrefix();
            if (var4 != null && var4.length() != 0) {
               if (this.hasAttributes()) {
                  ElementImpl var5 = (ElementImpl)this;
                  NodeImpl var6 = (NodeImpl)var5.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
                  if (var6 != null) {
                     String var7 = var6.getNodeValue();
                     if (var1 == null) {
                        return var3 == var7;
                     }

                     return var1.equals(var7);
                  }
               }

               NodeImpl var9 = (NodeImpl)this.getElementAncestor(this);
               if (var9 != null) {
                  return var9.isDefaultNamespace(var1);
               }

               return false;
            } else {
               if (var1 == null) {
                  return var3 == var1;
               }

               return var1.equals(var3);
            }
         case 2:
            if (this.ownerNode.getNodeType() == 1) {
               return this.ownerNode.isDefaultNamespace(var1);
            }

            return false;
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         default:
            NodeImpl var8 = (NodeImpl)this.getElementAncestor(this);
            if (var8 != null) {
               return var8.isDefaultNamespace(var1);
            }

            return false;
         case 6:
         case 10:
         case 11:
         case 12:
            return false;
         case 9:
            return ((NodeImpl)((Document)this).getDocumentElement()).isDefaultNamespace(var1);
      }
   }

   public String lookupPrefix(String var1) {
      if (var1 == null) {
         return null;
      } else {
         short var2 = this.getNodeType();
         switch (var2) {
            case 1:
               String var3 = this.getNamespaceURI();
               return this.lookupNamespacePrefix(var1, (ElementImpl)this);
            case 2:
               if (this.ownerNode.getNodeType() == 1) {
                  return this.ownerNode.lookupPrefix(var1);
               }

               return null;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            default:
               NodeImpl var4 = (NodeImpl)this.getElementAncestor(this);
               if (var4 != null) {
                  return var4.lookupPrefix(var1);
               }

               return null;
            case 6:
            case 10:
            case 11:
            case 12:
               return null;
            case 9:
               return ((NodeImpl)((Document)this).getDocumentElement()).lookupPrefix(var1);
         }
      }
   }

   public String lookupNamespaceURI(String var1) {
      short var2 = this.getNodeType();
      switch (var2) {
         case 1:
            String var3 = this.getNamespaceURI();
            String var4 = this.getPrefix();
            if (var3 != null) {
               if (var1 == null && var4 == var1) {
                  return var3;
               }

               if (var4 != null && var4.equals(var1)) {
                  return var3;
               }
            }

            if (this.hasAttributes()) {
               NamedNodeMap var5 = this.getAttributes();
               int var6 = var5.getLength();

               for(int var7 = 0; var7 < var6; ++var7) {
                  Node var8 = var5.item(var7);
                  String var9 = var8.getPrefix();
                  String var10 = var8.getNodeValue();
                  var3 = var8.getNamespaceURI();
                  if (var3 != null && var3.equals("http://www.w3.org/2000/xmlns/")) {
                     if (var1 == null && var8.getNodeName().equals("xmlns")) {
                        return var10;
                     }

                     if (var9 != null && var9.equals("xmlns") && var8.getLocalName().equals(var1)) {
                        return var10;
                     }
                  }
               }
            }

            NodeImpl var12 = (NodeImpl)this.getElementAncestor(this);
            if (var12 != null) {
               return var12.lookupNamespaceURI(var1);
            }

            return null;
         case 2:
            if (this.ownerNode.getNodeType() == 1) {
               return this.ownerNode.lookupNamespaceURI(var1);
            }

            return null;
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         default:
            NodeImpl var11 = (NodeImpl)this.getElementAncestor(this);
            if (var11 != null) {
               return var11.lookupNamespaceURI(var1);
            }

            return null;
         case 6:
         case 10:
         case 11:
         case 12:
            return null;
         case 9:
            return ((NodeImpl)((Document)this).getDocumentElement()).lookupNamespaceURI(var1);
      }
   }

   Node getElementAncestor(Node var1) {
      Node var2 = var1.getParentNode();
      if (var2 != null) {
         short var3 = var2.getNodeType();
         return var3 == 1 ? var2 : this.getElementAncestor(var2);
      } else {
         return null;
      }
   }

   String lookupNamespacePrefix(String var1, ElementImpl var2) {
      String var3 = this.getNamespaceURI();
      String var4 = this.getPrefix();
      if (var3 != null && var3.equals(var1) && var4 != null) {
         String var5 = var2.lookupNamespaceURI(var4);
         if (var5 != null && var5.equals(var1)) {
            return var4;
         }
      }

      if (this.hasAttributes()) {
         NamedNodeMap var13 = this.getAttributes();
         int var6 = var13.getLength();

         for(int var7 = 0; var7 < var6; ++var7) {
            Node var8 = var13.item(var7);
            String var9 = var8.getPrefix();
            String var10 = var8.getNodeValue();
            var3 = var8.getNamespaceURI();
            if (var3 != null && var3.equals("http://www.w3.org/2000/xmlns/") && (var8.getNodeName().equals("xmlns") || var9 != null && var9.equals("xmlns") && var10.equals(var1))) {
               String var11 = var8.getLocalName();
               String var12 = var2.lookupNamespaceURI(var11);
               if (var12 != null && var12.equals(var1)) {
                  return var11;
               }
            }
         }
      }

      NodeImpl var14 = (NodeImpl)this.getElementAncestor(this);
      return var14 != null ? var14.lookupNamespacePrefix(var1, var2) : null;
   }

   public boolean isEqualNode(Node var1) {
      if (var1 == this) {
         return true;
      } else if (var1.getNodeType() != this.getNodeType()) {
         return false;
      } else {
         if (this.getNodeName() == null) {
            if (var1.getNodeName() != null) {
               return false;
            }
         } else if (!this.getNodeName().equals(var1.getNodeName())) {
            return false;
         }

         if (this.getLocalName() == null) {
            if (var1.getLocalName() != null) {
               return false;
            }
         } else if (!this.getLocalName().equals(var1.getLocalName())) {
            return false;
         }

         if (this.getNamespaceURI() == null) {
            if (var1.getNamespaceURI() != null) {
               return false;
            }
         } else if (!this.getNamespaceURI().equals(var1.getNamespaceURI())) {
            return false;
         }

         if (this.getPrefix() == null) {
            if (var1.getPrefix() != null) {
               return false;
            }
         } else if (!this.getPrefix().equals(var1.getPrefix())) {
            return false;
         }

         if (this.getNodeValue() == null) {
            if (var1.getNodeValue() != null) {
               return false;
            }
         } else if (!this.getNodeValue().equals(var1.getNodeValue())) {
            return false;
         }

         return true;
      }
   }

   public Object getFeature(String var1, String var2) {
      return this.isSupported(var1, var2) ? this : null;
   }

   public Object setUserData(String var1, Object var2, UserDataHandler var3) {
      return this.ownerDocument().setUserData(this, var1, var2, var3);
   }

   public Object getUserData(String var1) {
      return this.ownerDocument().getUserData(this, var1);
   }

   protected Hashtable getUserDataRecord() {
      return this.ownerDocument().getUserDataRecord(this);
   }

   public void setReadOnly(boolean var1, boolean var2) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.isReadOnly(var1);
   }

   public boolean getReadOnly() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.isReadOnly();
   }

   public void setUserData(Object var1) {
      this.ownerDocument().setUserData(this, var1);
   }

   public Object getUserData() {
      return this.ownerDocument().getUserData(this);
   }

   protected void changed() {
      this.ownerDocument().changed();
   }

   protected int changes() {
      return this.ownerDocument().changes();
   }

   protected void synchronizeData() {
      this.needsSyncData(false);
   }

   protected Node getContainer() {
      return null;
   }

   final boolean isReadOnly() {
      return (this.flags & 1) != 0;
   }

   final void isReadOnly(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 1) : (short)(this.flags & -2);
   }

   final boolean needsSyncData() {
      return (this.flags & 2) != 0;
   }

   final void needsSyncData(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 2) : (short)(this.flags & -3);
   }

   final boolean needsSyncChildren() {
      return (this.flags & 4) != 0;
   }

   public final void needsSyncChildren(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 4) : (short)(this.flags & -5);
   }

   final boolean isOwned() {
      return (this.flags & 8) != 0;
   }

   final void isOwned(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 8) : (short)(this.flags & -9);
   }

   final boolean isFirstChild() {
      return (this.flags & 16) != 0;
   }

   final void isFirstChild(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 16) : (short)(this.flags & -17);
   }

   final boolean isSpecified() {
      return (this.flags & 32) != 0;
   }

   final void isSpecified(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 32) : (short)(this.flags & -33);
   }

   final boolean internalIsIgnorableWhitespace() {
      return (this.flags & 64) != 0;
   }

   final void isIgnorableWhitespace(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 64) : (short)(this.flags & -65);
   }

   final boolean hasStringValue() {
      return (this.flags & 128) != 0;
   }

   final void hasStringValue(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 128) : (short)(this.flags & -129);
   }

   final boolean isNormalized() {
      return (this.flags & 256) != 0;
   }

   final void isNormalized(boolean var1) {
      if (!var1 && this.isNormalized() && this.ownerNode != null) {
         this.ownerNode.isNormalized(false);
      }

      this.flags = var1 ? (short)(this.flags | 256) : (short)(this.flags & -257);
   }

   final boolean isIdAttribute() {
      return (this.flags & 512) != 0;
   }

   final void isIdAttribute(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 512) : (short)(this.flags & -513);
   }

   public String toString() {
      return "[" + this.getNodeName() + ": " + this.getNodeValue() + "]";
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      var1.defaultWriteObject();
   }
}
