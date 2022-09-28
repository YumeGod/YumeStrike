package org.apache.batik.dom.traversal;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class DOMNodeIterator implements NodeIterator {
   protected static final short INITIAL = 0;
   protected static final short INVALID = 1;
   protected static final short FORWARD = 2;
   protected static final short BACKWARD = 3;
   protected AbstractDocument document;
   protected Node root;
   protected int whatToShow;
   protected NodeFilter filter;
   protected boolean expandEntityReferences;
   protected short state;
   protected Node referenceNode;

   public DOMNodeIterator(AbstractDocument var1, Node var2, int var3, NodeFilter var4, boolean var5) {
      this.document = var1;
      this.root = var2;
      this.whatToShow = var3;
      this.filter = var4;
      this.expandEntityReferences = var5;
      this.referenceNode = this.root;
   }

   public Node getRoot() {
      return this.root;
   }

   public int getWhatToShow() {
      return this.whatToShow;
   }

   public NodeFilter getFilter() {
      return this.filter;
   }

   public boolean getExpandEntityReferences() {
      return this.expandEntityReferences;
   }

   public Node nextNode() {
      switch (this.state) {
         case 0:
         case 3:
            this.state = 2;
            return this.referenceNode;
         case 1:
            throw this.document.createDOMException((short)11, "detached.iterator", (Object[])null);
         case 2:
         default:
            do {
               do {
                  this.unfilteredNextNode();
                  if (this.referenceNode == null) {
                     return null;
                  }
               } while((this.whatToShow & 1 << this.referenceNode.getNodeType() - 1) == 0);
            } while(this.filter != null && this.filter.acceptNode(this.referenceNode) != 1);

            return this.referenceNode;
      }
   }

   public Node previousNode() {
      switch (this.state) {
         case 0:
         case 2:
            this.state = 3;
            return this.referenceNode;
         case 1:
            throw this.document.createDOMException((short)11, "detached.iterator", (Object[])null);
         case 3:
         default:
            do {
               do {
                  this.unfilteredPreviousNode();
                  if (this.referenceNode == null) {
                     return this.referenceNode;
                  }
               } while((this.whatToShow & 1 << this.referenceNode.getNodeType() - 1) == 0);
            } while(this.filter != null && this.filter.acceptNode(this.referenceNode) != 1);

            return this.referenceNode;
      }
   }

   public void detach() {
      this.state = 1;
      this.document.detachNodeIterator(this);
   }

   public void nodeToBeRemoved(Node var1) {
      if (this.state != 1) {
         Node var2;
         for(var2 = this.referenceNode; var2 != null && var2 != this.root && var2 != var1; var2 = var2.getParentNode()) {
         }

         if (var2 != null && var2 != this.root) {
            Node var3;
            Node var4;
            if (this.state == 3) {
               if (var2.getNodeType() != 5 || this.expandEntityReferences) {
                  var3 = var2.getFirstChild();
                  if (var3 != null) {
                     this.referenceNode = var3;
                     return;
                  }
               }

               var3 = var2.getNextSibling();
               if (var3 != null) {
                  this.referenceNode = var3;
                  return;
               }

               var3 = var2;

               while((var3 = var3.getParentNode()) != null && var3 != this.root) {
                  var4 = var3.getNextSibling();
                  if (var4 != null) {
                     this.referenceNode = var4;
                     return;
                  }
               }

               this.referenceNode = null;
            } else {
               var3 = var2.getPreviousSibling();
               if (var3 == null) {
                  this.referenceNode = var2.getParentNode();
                  return;
               }

               if (var3.getNodeType() != 5 || this.expandEntityReferences) {
                  while((var4 = var3.getLastChild()) != null) {
                     var3 = var4;
                  }
               }

               this.referenceNode = var3;
            }

         }
      }
   }

   protected void unfilteredNextNode() {
      if (this.referenceNode != null) {
         Node var1;
         if (this.referenceNode.getNodeType() != 5 || this.expandEntityReferences) {
            var1 = this.referenceNode.getFirstChild();
            if (var1 != null) {
               this.referenceNode = var1;
               return;
            }
         }

         var1 = this.referenceNode.getNextSibling();
         if (var1 != null) {
            this.referenceNode = var1;
         } else {
            var1 = this.referenceNode;

            while((var1 = var1.getParentNode()) != null && var1 != this.root) {
               Node var2 = var1.getNextSibling();
               if (var2 != null) {
                  this.referenceNode = var2;
                  return;
               }
            }

            this.referenceNode = null;
         }
      }
   }

   protected void unfilteredPreviousNode() {
      if (this.referenceNode != null) {
         if (this.referenceNode == this.root) {
            this.referenceNode = null;
         } else {
            Node var1 = this.referenceNode.getPreviousSibling();
            if (var1 == null) {
               this.referenceNode = this.referenceNode.getParentNode();
            } else {
               Node var2;
               if (var1.getNodeType() != 5 || this.expandEntityReferences) {
                  while((var2 = var1.getLastChild()) != null) {
                     var1 = var2;
                  }
               }

               this.referenceNode = var1;
            }
         }
      }
   }
}
