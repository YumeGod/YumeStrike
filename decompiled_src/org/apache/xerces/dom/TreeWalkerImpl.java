package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

public class TreeWalkerImpl implements TreeWalker {
   private boolean fEntityReferenceExpansion = false;
   int fWhatToShow = -1;
   NodeFilter fNodeFilter;
   Node fCurrentNode;
   Node fRoot;

   public TreeWalkerImpl(Node var1, int var2, NodeFilter var3, boolean var4) {
      this.fCurrentNode = var1;
      this.fRoot = var1;
      this.fWhatToShow = var2;
      this.fNodeFilter = var3;
      this.fEntityReferenceExpansion = var4;
   }

   public Node getRoot() {
      return this.fRoot;
   }

   public int getWhatToShow() {
      return this.fWhatToShow;
   }

   public void setWhatShow(int var1) {
      this.fWhatToShow = var1;
   }

   public NodeFilter getFilter() {
      return this.fNodeFilter;
   }

   public boolean getExpandEntityReferences() {
      return this.fEntityReferenceExpansion;
   }

   public Node getCurrentNode() {
      return this.fCurrentNode;
   }

   public void setCurrentNode(Node var1) {
      if (var1 == null) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
         throw new DOMException((short)9, var2);
      } else {
         this.fCurrentNode = var1;
      }
   }

   public Node parentNode() {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         Node var1 = this.getParentNode(this.fCurrentNode);
         if (var1 != null) {
            this.fCurrentNode = var1;
         }

         return var1;
      }
   }

   public Node firstChild() {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         Node var1 = this.getFirstChild(this.fCurrentNode);
         if (var1 != null) {
            this.fCurrentNode = var1;
         }

         return var1;
      }
   }

   public Node lastChild() {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         Node var1 = this.getLastChild(this.fCurrentNode);
         if (var1 != null) {
            this.fCurrentNode = var1;
         }

         return var1;
      }
   }

   public Node previousSibling() {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         Node var1 = this.getPreviousSibling(this.fCurrentNode);
         if (var1 != null) {
            this.fCurrentNode = var1;
         }

         return var1;
      }
   }

   public Node nextSibling() {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         Node var1 = this.getNextSibling(this.fCurrentNode);
         if (var1 != null) {
            this.fCurrentNode = var1;
         }

         return var1;
      }
   }

   public Node previousNode() {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         Node var1 = this.getPreviousSibling(this.fCurrentNode);
         if (var1 == null) {
            var1 = this.getParentNode(this.fCurrentNode);
            if (var1 != null) {
               this.fCurrentNode = var1;
               return this.fCurrentNode;
            } else {
               return null;
            }
         } else {
            Node var2 = this.getLastChild(var1);

            Node var3;
            for(var3 = var2; var2 != null; var2 = this.getLastChild(var2)) {
               var3 = var2;
            }

            if (var3 != null) {
               this.fCurrentNode = var3;
               return this.fCurrentNode;
            } else if (var1 != null) {
               this.fCurrentNode = var1;
               return this.fCurrentNode;
            } else {
               return null;
            }
         }
      }
   }

   public Node nextNode() {
      if (this.fCurrentNode == null) {
         return null;
      } else {
         Node var1 = this.getFirstChild(this.fCurrentNode);
         if (var1 != null) {
            this.fCurrentNode = var1;
            return var1;
         } else {
            var1 = this.getNextSibling(this.fCurrentNode);
            if (var1 != null) {
               this.fCurrentNode = var1;
               return var1;
            } else {
               for(Node var2 = this.getParentNode(this.fCurrentNode); var2 != null; var2 = this.getParentNode(var2)) {
                  var1 = this.getNextSibling(var2);
                  if (var1 != null) {
                     this.fCurrentNode = var1;
                     return var1;
                  }
               }

               return null;
            }
         }
      }
   }

   Node getParentNode(Node var1) {
      if (var1 != null && var1 != this.fRoot) {
         Node var2 = var1.getParentNode();
         if (var2 == null) {
            return null;
         } else {
            short var3 = this.acceptNode(var2);
            return var3 == 1 ? var2 : this.getParentNode(var2);
         }
      } else {
         return null;
      }
   }

   Node getNextSibling(Node var1) {
      return this.getNextSibling(var1, this.fRoot);
   }

   Node getNextSibling(Node var1, Node var2) {
      if (var1 != null && var1 != var2) {
         Node var3 = var1.getNextSibling();
         short var4;
         if (var3 == null) {
            var3 = var1.getParentNode();
            if (var3 != null && var3 != var2) {
               var4 = this.acceptNode(var3);
               return var4 == 3 ? this.getNextSibling(var3, var2) : null;
            } else {
               return null;
            }
         } else {
            var4 = this.acceptNode(var3);
            if (var4 == 1) {
               return var3;
            } else if (var4 == 3) {
               Node var5 = this.getFirstChild(var3);
               return var5 == null ? this.getNextSibling(var3, var2) : var5;
            } else {
               return this.getNextSibling(var3, var2);
            }
         }
      } else {
         return null;
      }
   }

   Node getPreviousSibling(Node var1) {
      return this.getPreviousSibling(var1, this.fRoot);
   }

   Node getPreviousSibling(Node var1, Node var2) {
      if (var1 != null && var1 != var2) {
         Node var3 = var1.getPreviousSibling();
         short var4;
         if (var3 == null) {
            var3 = var1.getParentNode();
            if (var3 != null && var3 != var2) {
               var4 = this.acceptNode(var3);
               return var4 == 3 ? this.getPreviousSibling(var3, var2) : null;
            } else {
               return null;
            }
         } else {
            var4 = this.acceptNode(var3);
            if (var4 == 1) {
               return var3;
            } else if (var4 == 3) {
               Node var5 = this.getLastChild(var3);
               return var5 == null ? this.getPreviousSibling(var3, var2) : var5;
            } else {
               return this.getPreviousSibling(var3, var2);
            }
         }
      } else {
         return null;
      }
   }

   Node getFirstChild(Node var1) {
      if (var1 == null) {
         return null;
      } else if (!this.fEntityReferenceExpansion && var1.getNodeType() == 5) {
         return null;
      } else {
         Node var2 = var1.getFirstChild();
         if (var2 == null) {
            return null;
         } else {
            short var3 = this.acceptNode(var2);
            if (var3 == 1) {
               return var2;
            } else if (var3 == 3 && var2.hasChildNodes()) {
               Node var4 = this.getFirstChild(var2);
               return var4 == null ? this.getNextSibling(var2, var1) : var4;
            } else {
               return this.getNextSibling(var2, var1);
            }
         }
      }
   }

   Node getLastChild(Node var1) {
      if (var1 == null) {
         return null;
      } else if (!this.fEntityReferenceExpansion && var1.getNodeType() == 5) {
         return null;
      } else {
         Node var2 = var1.getLastChild();
         if (var2 == null) {
            return null;
         } else {
            short var3 = this.acceptNode(var2);
            if (var3 == 1) {
               return var2;
            } else if (var3 == 3 && var2.hasChildNodes()) {
               Node var4 = this.getLastChild(var2);
               return var4 == null ? this.getPreviousSibling(var2, var1) : var4;
            } else {
               return this.getPreviousSibling(var2, var1);
            }
         }
      }
   }

   short acceptNode(Node var1) {
      if (this.fNodeFilter == null) {
         return (short)((this.fWhatToShow & 1 << var1.getNodeType() - 1) != 0 ? 1 : 3);
      } else {
         return (this.fWhatToShow & 1 << var1.getNodeType() - 1) != 0 ? this.fNodeFilter.acceptNode(var1) : 3;
      }
   }
}
