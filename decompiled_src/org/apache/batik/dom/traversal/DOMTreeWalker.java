package org.apache.batik.dom.traversal;

import org.apache.batik.dom.AbstractNode;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

public class DOMTreeWalker implements TreeWalker {
   protected Node root;
   protected int whatToShow;
   protected NodeFilter filter;
   protected boolean expandEntityReferences;
   protected Node currentNode;

   public DOMTreeWalker(Node var1, int var2, NodeFilter var3, boolean var4) {
      this.root = var1;
      this.whatToShow = var2;
      this.filter = var3;
      this.expandEntityReferences = var4;
      this.currentNode = this.root;
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

   public Node getCurrentNode() {
      return this.currentNode;
   }

   public void setCurrentNode(Node var1) {
      if (var1 == null) {
         throw ((AbstractNode)this.root).createDOMException((short)9, "null.current.node", (Object[])null);
      } else {
         this.currentNode = var1;
      }
   }

   public Node parentNode() {
      Node var1 = this.parentNode(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public Node firstChild() {
      Node var1 = this.firstChild(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public Node lastChild() {
      Node var1 = this.lastChild(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public Node previousSibling() {
      Node var1 = this.previousSibling(this.currentNode, this.root);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public Node nextSibling() {
      Node var1 = this.nextSibling(this.currentNode, this.root);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public Node previousNode() {
      Node var1 = this.previousSibling(this.currentNode, this.root);
      if (var1 == null) {
         var1 = this.parentNode(this.currentNode);
         if (var1 != null) {
            this.currentNode = var1;
         }

         return var1;
      } else {
         Node var2 = this.lastChild(var1);

         Node var3;
         for(var3 = var2; var2 != null; var2 = this.lastChild(var2)) {
            var3 = var2;
         }

         return this.currentNode = var3 != null ? var3 : var1;
      }
   }

   public Node nextNode() {
      Node var1;
      if ((var1 = this.firstChild(this.currentNode)) != null) {
         return this.currentNode = var1;
      } else if ((var1 = this.nextSibling(this.currentNode, this.root)) != null) {
         return this.currentNode = var1;
      } else {
         Node var2 = this.currentNode;

         do {
            var2 = this.parentNode(var2);
            if (var2 == null) {
               return null;
            }
         } while((var1 = this.nextSibling(var2, this.root)) == null);

         return this.currentNode = var1;
      }
   }

   protected Node parentNode(Node var1) {
      if (var1 == this.root) {
         return null;
      } else {
         Node var2 = var1;

         do {
            do {
               var2 = var2.getParentNode();
               if (var2 == null) {
                  return null;
               }
            } while((this.whatToShow & 1 << var2.getNodeType() - 1) == 0);
         } while(this.filter != null && this.filter.acceptNode(var2) != 1);

         return var2;
      }
   }

   protected Node firstChild(Node var1) {
      if (var1.getNodeType() == 5 && !this.expandEntityReferences) {
         return null;
      } else {
         Node var2 = var1.getFirstChild();
         if (var2 == null) {
            return null;
         } else {
            switch (this.acceptNode(var2)) {
               case 1:
                  return var2;
               case 3:
                  Node var3 = this.firstChild(var2);
                  if (var3 != null) {
                     return var3;
                  }
               default:
                  return this.nextSibling(var2, var1);
            }
         }
      }
   }

   protected Node lastChild(Node var1) {
      if (var1.getNodeType() == 5 && !this.expandEntityReferences) {
         return null;
      } else {
         Node var2 = var1.getLastChild();
         if (var2 == null) {
            return null;
         } else {
            switch (this.acceptNode(var2)) {
               case 1:
                  return var2;
               case 3:
                  Node var3 = this.lastChild(var2);
                  if (var3 != null) {
                     return var3;
                  }
               default:
                  return this.previousSibling(var2, var1);
            }
         }
      }
   }

   protected Node previousSibling(Node var1, Node var2) {
      while(var1 != var2) {
         Node var3 = var1.getPreviousSibling();
         if (var3 == null) {
            var3 = var1.getParentNode();
            if (var3 != null && var3 != var2) {
               if (this.acceptNode(var3) == 3) {
                  var1 = var3;
                  continue;
               }

               return null;
            }

            return null;
         } else {
            switch (this.acceptNode(var3)) {
               case 1:
                  return var3;
               case 3:
                  Node var4 = this.lastChild(var3);
                  if (var4 != null) {
                     return var4;
                  }
               default:
                  var1 = var3;
            }
         }
      }

      return null;
   }

   protected Node nextSibling(Node var1, Node var2) {
      while(var1 != var2) {
         Node var3 = var1.getNextSibling();
         if (var3 == null) {
            var3 = var1.getParentNode();
            if (var3 != null && var3 != var2) {
               if (this.acceptNode(var3) == 3) {
                  var1 = var3;
                  continue;
               }

               return null;
            }

            return null;
         } else {
            switch (this.acceptNode(var3)) {
               case 1:
                  return var3;
               case 3:
                  Node var4 = this.firstChild(var3);
                  if (var4 != null) {
                     return var4;
                  }
               default:
                  var1 = var3;
            }
         }
      }

      return null;
   }

   protected short acceptNode(Node var1) {
      if ((this.whatToShow & 1 << var1.getNodeType() - 1) != 0) {
         return this.filter == null ? 1 : this.filter.acceptNode(var1);
      } else {
         return 3;
      }
   }
}
