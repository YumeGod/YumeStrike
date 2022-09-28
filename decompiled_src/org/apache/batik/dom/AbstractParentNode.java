package org.apache.batik.dom;

import java.io.Serializable;
import org.apache.batik.dom.events.DOMMutationEvent;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractParentNode extends AbstractNode {
   protected ChildNodes childNodes;

   public NodeList getChildNodes() {
      return this.childNodes == null ? (this.childNodes = new ChildNodes()) : this.childNodes;
   }

   public Node getFirstChild() {
      return this.childNodes == null ? null : this.childNodes.firstChild;
   }

   public Node getLastChild() {
      return this.childNodes == null ? null : this.childNodes.lastChild;
   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      if (var2 == null || this.childNodes != null && var2.getParentNode() == this) {
         this.checkAndRemove(var1, false);
         if (var1.getNodeType() != 11) {
            if (this.childNodes == null) {
               this.childNodes = new ChildNodes();
            }

            ExtendedNode var5 = this.childNodes.insert((ExtendedNode)var1, (ExtendedNode)var2);
            var5.setParentNode(this);
            this.nodeAdded(var5);
            this.fireDOMNodeInsertedEvent(var5);
            this.fireDOMSubtreeModifiedEvent();
            return var5;
         } else {
            Node var4;
            for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var4) {
               var4 = var3.getNextSibling();
               this.insertBefore(var3, var2);
            }

            return var1;
         }
      } else {
         throw this.createDOMException((short)8, "child.missing", new Object[]{new Integer(var2.getNodeType()), var2.getNodeName()});
      }
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      if (this.childNodes != null && var2.getParentNode() == this) {
         this.checkAndRemove(var1, true);
         if (var1.getNodeType() != 11) {
            this.fireDOMNodeRemovedEvent(var2);
            this.getCurrentDocument().nodeToBeRemoved(var2);
            this.nodeToBeRemoved(var2);
            ExtendedNode var6 = (ExtendedNode)var1;
            ExtendedNode var7 = this.childNodes.replace(var6, (ExtendedNode)var2);
            var6.setParentNode(this);
            var7.setParentNode((Node)null);
            this.nodeAdded(var6);
            this.fireDOMNodeInsertedEvent(var6);
            this.fireDOMSubtreeModifiedEvent();
            return var6;
         } else {
            Node var3 = var1.getLastChild();
            if (var3 == null) {
               return var1;
            } else {
               Node var4 = var3.getPreviousSibling();
               this.replaceChild(var3, var2);
               Node var5 = var3;

               for(var3 = var4; var3 != null; var3 = var4) {
                  var4 = var3.getPreviousSibling();
                  this.insertBefore(var3, var5);
                  var5 = var3;
               }

               return var1;
            }
         }
      } else {
         throw this.createDOMException((short)8, "child.missing", new Object[]{new Integer(var2.getNodeType()), var2.getNodeName()});
      }
   }

   public Node removeChild(Node var1) throws DOMException {
      if (this.childNodes != null && var1.getParentNode() == this) {
         if (this.isReadonly()) {
            throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
         } else {
            this.fireDOMNodeRemovedEvent(var1);
            this.getCurrentDocument().nodeToBeRemoved(var1);
            this.nodeToBeRemoved(var1);
            ExtendedNode var2 = this.childNodes.remove((ExtendedNode)var1);
            var2.setParentNode((Node)null);
            this.fireDOMSubtreeModifiedEvent();
            return var2;
         }
      } else {
         throw this.createDOMException((short)8, "child.missing", new Object[]{new Integer(var1.getNodeType()), var1.getNodeName()});
      }
   }

   public Node appendChild(Node var1) throws DOMException {
      this.checkAndRemove(var1, false);
      if (var1.getNodeType() != 11) {
         if (this.childNodes == null) {
            this.childNodes = new ChildNodes();
         }

         ExtendedNode var4 = this.childNodes.append((ExtendedNode)var1);
         var4.setParentNode(this);
         this.nodeAdded(var4);
         this.fireDOMNodeInsertedEvent(var4);
         this.fireDOMSubtreeModifiedEvent();
         return var4;
      } else {
         Node var3;
         for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var3) {
            var3 = var2.getNextSibling();
            this.appendChild(var2);
         }

         return var1;
      }
   }

   public boolean hasChildNodes() {
      return this.childNodes != null && this.childNodes.getLength() != 0;
   }

   public void normalize() {
      Node var1 = this.getFirstChild();
      if (var1 != null) {
         var1.normalize();
         Node var2 = var1.getNextSibling();

         while(true) {
            while(var2 != null) {
               if (var1.getNodeType() == 3 && var2.getNodeType() == 3) {
                  String var3 = var1.getNodeValue() + var2.getNodeValue();
                  AbstractText var4 = (AbstractText)var1;
                  var4.setNodeValue(var3);
                  this.removeChild(var2);
                  var2 = var1.getNextSibling();
               } else {
                  var2.normalize();
                  var1 = var2;
                  var2 = var2.getNextSibling();
               }
            }

            return;
         }
      }
   }

   public NodeList getElementsByTagName(String var1) {
      if (var1 == null) {
         return EMPTY_NODE_LIST;
      } else {
         AbstractDocument var2 = this.getCurrentDocument();
         ElementsByTagName var3 = var2.getElementsByTagName(this, var1);
         if (var3 == null) {
            var3 = new ElementsByTagName(var1);
            var2.putElementsByTagName(this, var1, var3);
         }

         return var3;
      }
   }

   public NodeList getElementsByTagNameNS(String var1, String var2) {
      if (var2 == null) {
         return EMPTY_NODE_LIST;
      } else {
         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         AbstractDocument var3 = this.getCurrentDocument();
         ElementsByTagNameNS var4 = var3.getElementsByTagNameNS(this, var1, var2);
         if (var4 == null) {
            var4 = new ElementsByTagNameNS(var1, var2);
            var3.putElementsByTagNameNS(this, var1, var2, var4);
         }

         return var4;
      }
   }

   public String getTextContent() {
      StringBuffer var1 = new StringBuffer();
      Node var2 = this.getFirstChild();

      while(var2 != null) {
         switch (var2.getNodeType()) {
            default:
               var1.append(((AbstractNode)var2).getTextContent());
            case 7:
            case 8:
               var2 = var2.getNextSibling();
         }
      }

      return var1.toString();
   }

   public void fireDOMNodeInsertedIntoDocumentEvent() {
      AbstractDocument var1 = this.getCurrentDocument();
      if (var1.getEventsEnabled()) {
         super.fireDOMNodeInsertedIntoDocumentEvent();

         for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            ((AbstractNode)var2).fireDOMNodeInsertedIntoDocumentEvent();
         }
      }

   }

   public void fireDOMNodeRemovedFromDocumentEvent() {
      AbstractDocument var1 = this.getCurrentDocument();
      if (var1.getEventsEnabled()) {
         super.fireDOMNodeRemovedFromDocumentEvent();

         for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            ((AbstractNode)var2).fireDOMNodeRemovedFromDocumentEvent();
         }
      }

   }

   protected void nodeAdded(Node var1) {
   }

   protected void nodeToBeRemoved(Node var1) {
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);

      for(Node var3 = this.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         Node var4 = ((AbstractNode)var3).deepExport(var3.cloneNode(false), var2);
         var1.appendChild(var4);
      }

      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);

      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         Node var3 = var2.cloneNode(true);
         var1.appendChild(var3);
      }

      return var1;
   }

   protected void fireDOMSubtreeModifiedEvent() {
      AbstractDocument var1 = this.getCurrentDocument();
      if (var1.getEventsEnabled()) {
         DOMMutationEvent var2 = (DOMMutationEvent)var1.createEvent("MutationEvents");
         var2.initMutationEventNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", true, false, (Node)null, (String)null, (String)null, (String)null, (short)1);
         this.dispatchEvent(var2);
      }

   }

   protected void fireDOMNodeInsertedEvent(Node var1) {
      AbstractDocument var2 = this.getCurrentDocument();
      if (var2.getEventsEnabled()) {
         DOMMutationEvent var3 = (DOMMutationEvent)var2.createEvent("MutationEvents");
         var3.initMutationEventNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", true, false, this, (String)null, (String)null, (String)null, (short)2);
         AbstractNode var4 = (AbstractNode)var1;
         var4.dispatchEvent(var3);
         var4.fireDOMNodeInsertedIntoDocumentEvent();
      }

   }

   protected void fireDOMNodeRemovedEvent(Node var1) {
      AbstractDocument var2 = this.getCurrentDocument();
      if (var2.getEventsEnabled()) {
         DOMMutationEvent var3 = (DOMMutationEvent)var2.createEvent("MutationEvents");
         var3.initMutationEventNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", true, false, this, (String)null, (String)null, (String)null, (short)3);
         AbstractNode var4 = (AbstractNode)var1;
         var4.dispatchEvent(var3);
         var4.fireDOMNodeRemovedFromDocumentEvent();
      }

   }

   protected void checkAndRemove(Node var1, boolean var2) {
      this.checkChildType(var1, var2);
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else if (var1.getOwnerDocument() != this.getCurrentDocument()) {
         throw this.createDOMException((short)4, "node.from.wrong.document", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else if (this == var1) {
         throw this.createDOMException((short)3, "add.self", new Object[]{this.getNodeName()});
      } else {
         Node var3 = var1.getParentNode();
         if (var3 != null) {
            for(Object var4 = this; var4 != null; var4 = ((Node)var4).getParentNode()) {
               if (var4 == var1) {
                  throw this.createDOMException((short)3, "add.ancestor", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
               }
            }

            var3.removeChild(var1);
         }
      }
   }

   protected class ChildNodes implements NodeList, Serializable {
      protected ExtendedNode firstChild;
      protected ExtendedNode lastChild;
      protected int children;
      protected int elementChildren;

      public ChildNodes() {
      }

      public Node item(int var1) {
         if (var1 >= 0 && var1 < this.children) {
            Object var2;
            int var3;
            if (var1 < this.children >> 1) {
               var2 = this.firstChild;

               for(var3 = 0; var3 < var1; ++var3) {
                  var2 = ((Node)var2).getNextSibling();
               }

               return (Node)var2;
            } else {
               var2 = this.lastChild;

               for(var3 = this.children - 1; var3 > var1; --var3) {
                  var2 = ((Node)var2).getPreviousSibling();
               }

               return (Node)var2;
            }
         } else {
            return null;
         }
      }

      public int getLength() {
         return this.children;
      }

      public ExtendedNode append(ExtendedNode var1) {
         if (this.lastChild == null) {
            this.firstChild = var1;
         } else {
            this.lastChild.setNextSibling(var1);
            var1.setPreviousSibling(this.lastChild);
         }

         this.lastChild = var1;
         ++this.children;
         if (var1.getNodeType() == 1) {
            ++this.elementChildren;
         }

         return var1;
      }

      public ExtendedNode insert(ExtendedNode var1, ExtendedNode var2) {
         if (var2 == null) {
            return this.append(var1);
         } else if (var2 == this.firstChild) {
            this.firstChild.setPreviousSibling(var1);
            var1.setNextSibling(this.firstChild);
            this.firstChild = var1;
            ++this.children;
            if (var1.getNodeType() == 1) {
               ++this.elementChildren;
            }

            return var1;
         } else {
            ExtendedNode var3;
            if (var2 == this.lastChild) {
               var3 = (ExtendedNode)var2.getPreviousSibling();
               var3.setNextSibling(var1);
               var2.setPreviousSibling(var1);
               var1.setNextSibling(var2);
               var1.setPreviousSibling(var3);
               ++this.children;
               if (var1.getNodeType() == 1) {
                  ++this.elementChildren;
               }

               return var1;
            } else {
               var3 = (ExtendedNode)var2.getPreviousSibling();
               if (var3.getNextSibling() == var2 && var3.getParentNode() == var2.getParentNode()) {
                  var3.setNextSibling(var1);
                  var1.setPreviousSibling(var3);
                  var1.setNextSibling(var2);
                  var2.setPreviousSibling(var1);
                  ++this.children;
                  if (var1.getNodeType() == 1) {
                     ++this.elementChildren;
                  }

                  return var1;
               } else {
                  throw AbstractParentNode.this.createDOMException((short)8, "child.missing", new Object[]{new Integer(var2.getNodeType()), var2.getNodeName()});
               }
            }
         }
      }

      public ExtendedNode replace(ExtendedNode var1, ExtendedNode var2) {
         ExtendedNode var3;
         if (var2 == this.firstChild) {
            var3 = (ExtendedNode)this.firstChild.getNextSibling();
            var1.setNextSibling(var3);
            if (var2 == this.lastChild) {
               this.lastChild = var1;
            } else {
               var3.setPreviousSibling(var1);
            }

            this.firstChild.setNextSibling((Node)null);
            this.firstChild = var1;
            if (var2.getNodeType() == 1) {
               --this.elementChildren;
            }

            if (var1.getNodeType() == 1) {
               ++this.elementChildren;
            }

            return var2;
         } else if (var2 == this.lastChild) {
            var3 = (ExtendedNode)this.lastChild.getPreviousSibling();
            var1.setPreviousSibling(var3);
            var3.setNextSibling(var1);
            this.lastChild.setPreviousSibling((Node)null);
            this.lastChild = var1;
            if (var2.getNodeType() == 1) {
               --this.elementChildren;
            }

            if (var1.getNodeType() == 1) {
               ++this.elementChildren;
            }

            return var2;
         } else {
            var3 = (ExtendedNode)var2.getPreviousSibling();
            ExtendedNode var4 = (ExtendedNode)var2.getNextSibling();
            if (var3.getNextSibling() == var2 && var4.getPreviousSibling() == var2 && var3.getParentNode() == var2.getParentNode() && var4.getParentNode() == var2.getParentNode()) {
               var3.setNextSibling(var1);
               var1.setPreviousSibling(var3);
               var1.setNextSibling(var4);
               var4.setPreviousSibling(var1);
               var2.setPreviousSibling((Node)null);
               var2.setNextSibling((Node)null);
               if (var2.getNodeType() == 1) {
                  --this.elementChildren;
               }

               if (var1.getNodeType() == 1) {
                  ++this.elementChildren;
               }

               return var2;
            } else {
               throw AbstractParentNode.this.createDOMException((short)8, "child.missing", new Object[]{new Integer(var2.getNodeType()), var2.getNodeName()});
            }
         }
      }

      public ExtendedNode remove(ExtendedNode var1) {
         if (var1 == this.firstChild) {
            if (var1 == this.lastChild) {
               this.firstChild = null;
               this.lastChild = null;
               --this.children;
               if (var1.getNodeType() == 1) {
                  --this.elementChildren;
               }

               return var1;
            } else {
               this.firstChild = (ExtendedNode)this.firstChild.getNextSibling();
               this.firstChild.setPreviousSibling((Node)null);
               var1.setNextSibling((Node)null);
               if (var1.getNodeType() == 1) {
                  --this.elementChildren;
               }

               --this.children;
               return var1;
            }
         } else if (var1 == this.lastChild) {
            this.lastChild = (ExtendedNode)this.lastChild.getPreviousSibling();
            this.lastChild.setNextSibling((Node)null);
            var1.setPreviousSibling((Node)null);
            --this.children;
            if (var1.getNodeType() == 1) {
               --this.elementChildren;
            }

            return var1;
         } else {
            ExtendedNode var2 = (ExtendedNode)var1.getPreviousSibling();
            ExtendedNode var3 = (ExtendedNode)var1.getNextSibling();
            if (var2.getNextSibling() == var1 && var3.getPreviousSibling() == var1 && var2.getParentNode() == var1.getParentNode() && var3.getParentNode() == var1.getParentNode()) {
               var2.setNextSibling(var3);
               var3.setPreviousSibling(var2);
               var1.setPreviousSibling((Node)null);
               var1.setNextSibling((Node)null);
               --this.children;
               if (var1.getNodeType() == 1) {
                  --this.elementChildren;
               }

               return var1;
            } else {
               throw AbstractParentNode.this.createDOMException((short)8, "child.missing", new Object[]{new Integer(var1.getNodeType()), var1.getNodeName()});
            }
         }
      }
   }

   protected class ElementsByTagNameNS implements NodeList {
      protected Node[] table;
      protected int size = -1;
      protected String namespaceURI;
      protected String localName;

      public ElementsByTagNameNS(String var2, String var3) {
         this.namespaceURI = var2;
         this.localName = var3;
      }

      public Node item(int var1) {
         if (this.size == -1) {
            this.initialize();
         }

         return this.table != null && var1 >= 0 && var1 <= this.size ? this.table[var1] : null;
      }

      public int getLength() {
         if (this.size == -1) {
            this.initialize();
         }

         return this.size;
      }

      public void invalidate() {
         this.size = -1;
      }

      protected void append(Node var1) {
         if (this.table == null) {
            this.table = new Node[11];
         } else if (this.size == this.table.length - 1) {
            Node[] var2 = new Node[this.table.length * 2 + 1];
            System.arraycopy(this.table, 0, var2, 0, this.size);
            this.table = var2;
         }

         this.table[this.size++] = var1;
      }

      protected void initialize() {
         this.size = 0;

         for(Node var1 = AbstractParentNode.this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
            this.initialize(var1);
         }

      }

      private void initialize(Node var1) {
         if (var1.getNodeType() == 1) {
            String var2 = var1.getNamespaceURI();
            String var3 = var2 == null ? var1.getNodeName() : var1.getLocalName();
            if (this.nsMatch(this.namespaceURI, var1.getNamespaceURI()) && (this.localName.equals("*") || this.localName.equals(var3))) {
               this.append(var1);
            }
         }

         for(Node var4 = var1.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
            this.initialize(var4);
         }

      }

      private boolean nsMatch(String var1, String var2) {
         if (var1 == null && var2 == null) {
            return true;
         } else if (var1 != null && var2 != null) {
            return var1.equals("*") ? true : var1.equals(var2);
         } else {
            return false;
         }
      }
   }

   protected class ElementsByTagName implements NodeList {
      protected Node[] table;
      protected int size = -1;
      protected String name;

      public ElementsByTagName(String var2) {
         this.name = var2;
      }

      public Node item(int var1) {
         if (this.size == -1) {
            this.initialize();
         }

         return this.table != null && var1 >= 0 && var1 < this.size ? this.table[var1] : null;
      }

      public int getLength() {
         if (this.size == -1) {
            this.initialize();
         }

         return this.size;
      }

      public void invalidate() {
         this.size = -1;
      }

      protected void append(Node var1) {
         if (this.table == null) {
            this.table = new Node[11];
         } else if (this.size == this.table.length - 1) {
            Node[] var2 = new Node[this.table.length * 2 + 1];
            System.arraycopy(this.table, 0, var2, 0, this.size);
            this.table = var2;
         }

         this.table[this.size++] = var1;
      }

      protected void initialize() {
         this.size = 0;

         for(Node var1 = AbstractParentNode.this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
            this.initialize(var1);
         }

      }

      private void initialize(Node var1) {
         if (var1.getNodeType() == 1) {
            String var2 = var1.getNodeName();
            if (this.name.equals("*") || this.name.equals(var2)) {
               this.append(var1);
            }
         }

         for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            this.initialize(var3);
         }

      }
   }
}
