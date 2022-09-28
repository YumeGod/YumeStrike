package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeIteratorImpl implements NodeIterator {
   private DocumentImpl fDocument;
   private Node fRoot;
   private int fWhatToShow = -1;
   private NodeFilter fNodeFilter;
   private boolean fDetach = false;
   private Node fCurrentNode;
   private boolean fForward = true;
   private boolean fEntityReferenceExpansion;

   public NodeIteratorImpl(DocumentImpl var1, Node var2, int var3, NodeFilter var4, boolean var5) {
      this.fDocument = var1;
      this.fRoot = var2;
      this.fCurrentNode = null;
      this.fWhatToShow = var3;
      this.fNodeFilter = var4;
      this.fEntityReferenceExpansion = var5;
   }

   public Node getRoot() {
      return this.fRoot;
   }

   public int getWhatToShow() {
      return this.fWhatToShow;
   }

   public NodeFilter getFilter() {
      return this.fNodeFilter;
   }

   public boolean getExpandEntityReferences() {
      return this.fEntityReferenceExpansion;
   }

   public Node nextNode() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else if (this.fRoot == null) {
         return null;
      } else {
         Node var1 = this.fCurrentNode;
         boolean var2 = false;

         do {
            if (var2) {
               return null;
            }

            if (!this.fForward && var1 != null) {
               var1 = this.fCurrentNode;
            } else if (!this.fEntityReferenceExpansion && var1 != null && var1.getNodeType() == 5) {
               var1 = this.nextNode(var1, false);
            } else {
               var1 = this.nextNode(var1, true);
            }

            this.fForward = true;
            if (var1 == null) {
               return null;
            }

            var2 = this.acceptNode(var1);
         } while(!var2);

         this.fCurrentNode = var1;
         return this.fCurrentNode;
      }
   }

   public Node previousNode() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else if (this.fRoot != null && this.fCurrentNode != null) {
         Node var1 = this.fCurrentNode;
         boolean var2 = false;

         do {
            if (var2) {
               return null;
            }

            if (this.fForward && var1 != null) {
               var1 = this.fCurrentNode;
            } else {
               var1 = this.previousNode(var1);
            }

            this.fForward = false;
            if (var1 == null) {
               return null;
            }

            var2 = this.acceptNode(var1);
         } while(!var2);

         this.fCurrentNode = var1;
         return this.fCurrentNode;
      } else {
         return null;
      }
   }

   boolean acceptNode(Node var1) {
      if (this.fNodeFilter == null) {
         return (this.fWhatToShow & 1 << var1.getNodeType() - 1) != 0;
      } else {
         return (this.fWhatToShow & 1 << var1.getNodeType() - 1) != 0 && this.fNodeFilter.acceptNode(var1) == 1;
      }
   }

   Node matchNodeOrParent(Node var1) {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         for(Node var2 = this.fCurrentNode; var2 != this.fRoot; var2 = var2.getParentNode()) {
            if (var1 == var2) {
               return var2;
            }
         }

         return null;
      }
   }

   Node nextNode(Node var1, boolean var2) {
      if (var1 == null) {
         return this.fRoot;
      } else {
         Node var3;
         if (var2 && var1.hasChildNodes()) {
            var3 = var1.getFirstChild();
            return var3;
         } else if (var1 == this.fRoot) {
            return null;
         } else {
            var3 = var1.getNextSibling();
            if (var3 != null) {
               return var3;
            } else {
               for(Node var4 = var1.getParentNode(); var4 != null && var4 != this.fRoot; var4 = var4.getParentNode()) {
                  var3 = var4.getNextSibling();
                  if (var3 != null) {
                     return var3;
                  }
               }

               return null;
            }
         }
      }
   }

   Node previousNode(Node var1) {
      if (var1 == this.fRoot) {
         return null;
      } else {
         Node var2 = var1.getPreviousSibling();
         if (var2 == null) {
            var2 = var1.getParentNode();
            return var2;
         } else {
            if (var2.hasChildNodes() && (this.fEntityReferenceExpansion || var2 == null || var2.getNodeType() != 5)) {
               while(var2.hasChildNodes()) {
                  var2 = var2.getLastChild();
               }
            }

            return var2;
         }
      }
   }

   public void removeNode(Node var1) {
      if (var1 != null) {
         Node var2 = this.matchNodeOrParent(var1);
         if (var2 != null) {
            if (this.fForward) {
               this.fCurrentNode = this.previousNode(var2);
            } else {
               Node var3 = this.nextNode(var2, false);
               if (var3 != null) {
                  this.fCurrentNode = var3;
               } else {
                  this.fCurrentNode = this.previousNode(var2);
                  this.fForward = true;
               }
            }

         }
      }
   }

   public void detach() {
      this.fDetach = true;
      this.fDocument.removeNodeIterator(this);
   }
}
