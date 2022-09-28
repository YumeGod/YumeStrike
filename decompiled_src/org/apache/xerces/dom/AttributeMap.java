package org.apache.xerces.dom;

import java.util.Vector;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class AttributeMap extends NamedNodeMapImpl {
   static final long serialVersionUID = 8872606282138665383L;

   protected AttributeMap(ElementImpl var1, NamedNodeMapImpl var2) {
      super(var1);
      if (var2 != null) {
         this.cloneContent(var2);
         if (super.nodes != null) {
            this.hasDefaults(true);
         }
      }

   }

   public Node setNamedItem(Node var1) throws DOMException {
      boolean var2 = super.ownerNode.ownerDocument().errorChecking;
      if (var2) {
         String var6;
         if (this.isReadOnly()) {
            var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var6);
         }

         if (var1.getOwnerDocument() != super.ownerNode.ownerDocument()) {
            var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
            throw new DOMException((short)4, var6);
         }

         if (var1.getNodeType() != 2) {
            var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null);
            throw new DOMException((short)3, var6);
         }
      }

      AttrImpl var3 = (AttrImpl)var1;
      if (var3.isOwned()) {
         if (var2 && var3.getOwnerElement() != super.ownerNode) {
            String var7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INUSE_ATTRIBUTE_ERR", (Object[])null);
            throw new DOMException((short)10, var7);
         } else {
            return var1;
         }
      } else {
         var3.ownerNode = super.ownerNode;
         var3.isOwned(true);
         int var4 = this.findNamePoint(var1.getNodeName(), 0);
         AttrImpl var5 = null;
         if (var4 >= 0) {
            var5 = (AttrImpl)super.nodes.elementAt(var4);
            super.nodes.setElementAt(var1, var4);
            var5.ownerNode = super.ownerNode.ownerDocument();
            var5.isOwned(false);
            var5.isSpecified(true);
         } else {
            var4 = -1 - var4;
            if (null == super.nodes) {
               super.nodes = new Vector(5, 10);
            }

            super.nodes.insertElementAt(var1, var4);
         }

         super.ownerNode.ownerDocument().setAttrNode(var3, var5);
         if (!var3.isNormalized()) {
            super.ownerNode.isNormalized(false);
         }

         return var5;
      }
   }

   public Node setNamedItemNS(Node var1) throws DOMException {
      boolean var2 = super.ownerNode.ownerDocument().errorChecking;
      if (var2) {
         String var6;
         if (this.isReadOnly()) {
            var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var6);
         }

         if (var1.getOwnerDocument() != super.ownerNode.ownerDocument()) {
            var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
            throw new DOMException((short)4, var6);
         }

         if (var1.getNodeType() != 2) {
            var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null);
            throw new DOMException((short)3, var6);
         }
      }

      AttrImpl var3 = (AttrImpl)var1;
      if (var3.isOwned()) {
         if (var2 && var3.getOwnerElement() != super.ownerNode) {
            String var7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INUSE_ATTRIBUTE_ERR", (Object[])null);
            throw new DOMException((short)10, var7);
         } else {
            return var1;
         }
      } else {
         var3.ownerNode = super.ownerNode;
         var3.isOwned(true);
         int var4 = this.findNamePoint(var3.getNamespaceURI(), var3.getLocalName());
         AttrImpl var5 = null;
         if (var4 >= 0) {
            var5 = (AttrImpl)super.nodes.elementAt(var4);
            super.nodes.setElementAt(var1, var4);
            var5.ownerNode = super.ownerNode.ownerDocument();
            var5.isOwned(false);
            var5.isSpecified(true);
         } else {
            var4 = this.findNamePoint(var1.getNodeName(), 0);
            if (var4 >= 0) {
               var5 = (AttrImpl)super.nodes.elementAt(var4);
               super.nodes.insertElementAt(var1, var4);
            } else {
               var4 = -1 - var4;
               if (null == super.nodes) {
                  super.nodes = new Vector(5, 10);
               }

               super.nodes.insertElementAt(var1, var4);
            }
         }

         super.ownerNode.ownerDocument().setAttrNode(var3, var5);
         if (!var3.isNormalized()) {
            super.ownerNode.isNormalized(false);
         }

         return var5;
      }
   }

   public Node removeNamedItem(String var1) throws DOMException {
      return this.internalRemoveNamedItem(var1, true);
   }

   Node safeRemoveNamedItem(String var1) {
      return this.internalRemoveNamedItem(var1, false);
   }

   protected Node removeItem(Node var1, boolean var2) throws DOMException {
      int var3 = -1;
      if (super.nodes != null) {
         for(int var4 = 0; var4 < super.nodes.size(); ++var4) {
            if (super.nodes.elementAt(var4) == var1) {
               var3 = var4;
               break;
            }
         }
      }

      if (var3 < 0) {
         String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
         throw new DOMException((short)8, var5);
      } else {
         return this.remove((AttrImpl)var1, var3, var2);
      }
   }

   protected final Node internalRemoveNamedItem(String var1, boolean var2) {
      if (this.isReadOnly()) {
         String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var5);
      } else {
         int var3 = this.findNamePoint(var1, 0);
         if (var3 < 0) {
            if (var2) {
               String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
               throw new DOMException((short)8, var4);
            } else {
               return null;
            }
         } else {
            return this.remove((AttrImpl)super.nodes.elementAt(var3), var3, true);
         }
      }
   }

   private final Node remove(AttrImpl var1, int var2, boolean var3) {
      CoreDocumentImpl var4 = super.ownerNode.ownerDocument();
      String var5 = var1.getNodeName();
      if (var1.isIdAttribute()) {
         var4.removeIdentifier(var1.getValue());
      }

      if (this.hasDefaults() && var3) {
         NamedNodeMapImpl var6 = ((ElementImpl)super.ownerNode).getDefaultAttributes();
         Node var7;
         if (var6 != null && (var7 = var6.getNamedItem(var5)) != null && this.findNamePoint(var5, var2 + 1) < 0) {
            NodeImpl var8 = (NodeImpl)var7.cloneNode(true);
            if (var7.getLocalName() != null) {
               ((AttrNSImpl)var8).namespaceURI = var1.getNamespaceURI();
            }

            var8.ownerNode = super.ownerNode;
            var8.isOwned(true);
            var8.isSpecified(false);
            super.nodes.setElementAt(var8, var2);
            if (var1.isIdAttribute()) {
               var4.putIdentifier(var8.getNodeValue(), (ElementImpl)super.ownerNode);
            }
         } else {
            super.nodes.removeElementAt(var2);
         }
      } else {
         super.nodes.removeElementAt(var2);
      }

      var1.ownerNode = var4;
      var1.isOwned(false);
      var1.isSpecified(true);
      var1.isIdAttribute(false);
      var4.removedAttrNode(var1, super.ownerNode, var5);
      return var1;
   }

   public Node removeNamedItemNS(String var1, String var2) throws DOMException {
      return this.internalRemoveNamedItemNS(var1, var2, true);
   }

   Node safeRemoveNamedItemNS(String var1, String var2) {
      return this.internalRemoveNamedItemNS(var1, var2, false);
   }

   protected final Node internalRemoveNamedItemNS(String var1, String var2, boolean var3) {
      CoreDocumentImpl var4 = super.ownerNode.ownerDocument();
      if (var4.errorChecking && this.isReadOnly()) {
         String var12 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var12);
      } else {
         int var5 = this.findNamePoint(var1, var2);
         if (var5 < 0) {
            if (var3) {
               String var13 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
               throw new DOMException((short)8, var13);
            } else {
               return null;
            }
         } else {
            AttrImpl var6 = (AttrImpl)super.nodes.elementAt(var5);
            if (var6.isIdAttribute()) {
               var4.removeIdentifier(var6.getValue());
            }

            String var7 = var6.getNodeName();
            if (this.hasDefaults()) {
               NamedNodeMapImpl var8 = ((ElementImpl)super.ownerNode).getDefaultAttributes();
               Node var9;
               if (var8 != null && (var9 = var8.getNamedItem(var7)) != null) {
                  int var10 = this.findNamePoint(var7, 0);
                  if (var10 >= 0 && this.findNamePoint(var7, var10 + 1) < 0) {
                     NodeImpl var11 = (NodeImpl)var9.cloneNode(true);
                     var11.ownerNode = super.ownerNode;
                     if (var9.getLocalName() != null) {
                        ((AttrNSImpl)var11).namespaceURI = var1;
                     }

                     var11.isOwned(true);
                     var11.isSpecified(false);
                     super.nodes.setElementAt(var11, var5);
                     if (var11.isIdAttribute()) {
                        var4.putIdentifier(var11.getNodeValue(), (ElementImpl)super.ownerNode);
                     }
                  } else {
                     super.nodes.removeElementAt(var5);
                  }
               } else {
                  super.nodes.removeElementAt(var5);
               }
            } else {
               super.nodes.removeElementAt(var5);
            }

            var6.ownerNode = var4;
            var6.isOwned(false);
            var6.isSpecified(true);
            var6.isIdAttribute(false);
            var4.removedAttrNode(var6, super.ownerNode, var2);
            return var6;
         }
      }
   }

   public NamedNodeMapImpl cloneMap(NodeImpl var1) {
      AttributeMap var2 = new AttributeMap((ElementImpl)var1, (NamedNodeMapImpl)null);
      var2.hasDefaults(this.hasDefaults());
      var2.cloneContent(this);
      return var2;
   }

   protected void cloneContent(NamedNodeMapImpl var1) {
      Vector var2 = var1.nodes;
      if (var2 != null) {
         int var3 = var2.size();
         if (var3 != 0) {
            if (super.nodes == null) {
               super.nodes = new Vector(var3);
            }

            super.nodes.setSize(var3);

            for(int var4 = 0; var4 < var3; ++var4) {
               NodeImpl var5 = (NodeImpl)var2.elementAt(var4);
               NodeImpl var6 = (NodeImpl)var5.cloneNode(true);
               var6.isSpecified(var5.isSpecified());
               super.nodes.setElementAt(var6, var4);
               var6.ownerNode = super.ownerNode;
               var6.isOwned(true);
            }
         }
      }

   }

   void moveSpecifiedAttributes(AttributeMap var1) {
      int var2 = var1.nodes != null ? var1.nodes.size() : 0;

      for(int var3 = var2 - 1; var3 >= 0; --var3) {
         AttrImpl var4 = (AttrImpl)var1.nodes.elementAt(var3);
         if (var4.isSpecified()) {
            var1.remove(var4, var3, false);
            if (var4.getLocalName() != null) {
               this.setNamedItem(var4);
            } else {
               this.setNamedItemNS(var4);
            }
         }
      }

   }

   protected void reconcileDefaults(NamedNodeMapImpl var1) {
      int var2 = super.nodes != null ? super.nodes.size() : 0;

      for(int var3 = var2 - 1; var3 >= 0; --var3) {
         AttrImpl var4 = (AttrImpl)super.nodes.elementAt(var3);
         if (!var4.isSpecified()) {
            this.remove(var4, var3, false);
         }
      }

      if (var1 != null) {
         if (super.nodes != null && super.nodes.size() != 0) {
            int var9 = var1.nodes.size();

            for(int var5 = 0; var5 < var9; ++var5) {
               AttrImpl var6 = (AttrImpl)var1.nodes.elementAt(var5);
               int var7 = this.findNamePoint(var6.getNodeName(), 0);
               if (var7 < 0) {
                  var7 = -1 - var7;
                  NodeImpl var8 = (NodeImpl)var6.cloneNode(true);
                  var8.ownerNode = super.ownerNode;
                  var8.isOwned(true);
                  var8.isSpecified(false);
                  super.nodes.insertElementAt(var8, var7);
               }
            }
         } else {
            this.cloneContent(var1);
         }

      }
   }
}
