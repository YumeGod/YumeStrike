package org.apache.batik.gvt;

import java.util.List;

public class GVTTreeWalker {
   protected GraphicsNode gvtRoot;
   protected GraphicsNode treeRoot;
   protected GraphicsNode currentNode;

   public GVTTreeWalker(GraphicsNode var1) {
      this.gvtRoot = var1.getRoot();
      this.treeRoot = var1;
      this.currentNode = var1;
   }

   public GraphicsNode getRoot() {
      return this.treeRoot;
   }

   public GraphicsNode getGVTRoot() {
      return this.gvtRoot;
   }

   public void setCurrentGraphicsNode(GraphicsNode var1) {
      if (var1.getRoot() != this.gvtRoot) {
         throw new IllegalArgumentException("The node " + var1 + " is not part of the document " + this.gvtRoot);
      } else {
         this.currentNode = var1;
      }
   }

   public GraphicsNode getCurrentGraphicsNode() {
      return this.currentNode;
   }

   public GraphicsNode previousGraphicsNode() {
      GraphicsNode var1 = this.getPreviousGraphicsNode(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public GraphicsNode nextGraphicsNode() {
      GraphicsNode var1 = this.getNextGraphicsNode(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public GraphicsNode parentGraphicsNode() {
      if (this.currentNode == this.treeRoot) {
         return null;
      } else {
         CompositeGraphicsNode var1 = this.currentNode.getParent();
         if (var1 != null) {
            this.currentNode = var1;
         }

         return var1;
      }
   }

   public GraphicsNode getNextSibling() {
      GraphicsNode var1 = getNextSibling(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public GraphicsNode getPreviousSibling() {
      GraphicsNode var1 = getPreviousSibling(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public GraphicsNode firstChild() {
      GraphicsNode var1 = getFirstChild(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   public GraphicsNode lastChild() {
      GraphicsNode var1 = getLastChild(this.currentNode);
      if (var1 != null) {
         this.currentNode = var1;
      }

      return var1;
   }

   protected GraphicsNode getNextGraphicsNode(GraphicsNode var1) {
      if (var1 == null) {
         return null;
      } else {
         GraphicsNode var2 = getFirstChild(var1);
         if (var2 != null) {
            return var2;
         } else {
            var2 = getNextSibling(var1);
            if (var2 != null) {
               return var2;
            } else {
               Object var4 = var1;

               while((var4 = ((GraphicsNode)var4).getParent()) != null && var4 != this.treeRoot) {
                  GraphicsNode var3 = getNextSibling((GraphicsNode)var4);
                  if (var3 != null) {
                     return var3;
                  }
               }

               return null;
            }
         }
      }
   }

   protected GraphicsNode getPreviousGraphicsNode(GraphicsNode var1) {
      if (var1 == null) {
         return null;
      } else if (var1 == this.treeRoot) {
         return null;
      } else {
         GraphicsNode var2 = getPreviousSibling(var1);
         if (var2 == null) {
            return var1.getParent();
         } else {
            GraphicsNode var3;
            while((var3 = getLastChild(var2)) != null) {
               var2 = var3;
            }

            return var2;
         }
      }
   }

   protected static GraphicsNode getLastChild(GraphicsNode var0) {
      if (!(var0 instanceof CompositeGraphicsNode)) {
         return null;
      } else {
         CompositeGraphicsNode var1 = (CompositeGraphicsNode)var0;
         List var2 = var1.getChildren();
         if (var2 == null) {
            return null;
         } else {
            return var2.size() >= 1 ? (GraphicsNode)var2.get(var2.size() - 1) : null;
         }
      }
   }

   protected static GraphicsNode getPreviousSibling(GraphicsNode var0) {
      CompositeGraphicsNode var1 = var0.getParent();
      if (var1 == null) {
         return null;
      } else {
         List var2 = var1.getChildren();
         if (var2 == null) {
            return null;
         } else {
            int var3 = var2.indexOf(var0);
            return var3 - 1 >= 0 ? (GraphicsNode)var2.get(var3 - 1) : null;
         }
      }
   }

   protected static GraphicsNode getFirstChild(GraphicsNode var0) {
      if (!(var0 instanceof CompositeGraphicsNode)) {
         return null;
      } else {
         CompositeGraphicsNode var1 = (CompositeGraphicsNode)var0;
         List var2 = var1.getChildren();
         if (var2 == null) {
            return null;
         } else {
            return var2.size() >= 1 ? (GraphicsNode)var2.get(0) : null;
         }
      }
   }

   protected static GraphicsNode getNextSibling(GraphicsNode var0) {
      CompositeGraphicsNode var1 = var0.getParent();
      if (var1 == null) {
         return null;
      } else {
         List var2 = var1.getChildren();
         if (var2 == null) {
            return null;
         } else {
            int var3 = var2.indexOf(var0);
            return var3 + 1 < var2.size() ? (GraphicsNode)var2.get(var3 + 1) : null;
         }
      }
   }
}
