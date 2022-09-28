package org.apache.batik.dom;

import org.w3c.dom.Node;

public abstract class AbstractParentChildNode extends AbstractParentNode {
   protected Node parentNode;
   protected Node previousSibling;
   protected Node nextSibling;

   public Node getParentNode() {
      return this.parentNode;
   }

   public void setParentNode(Node var1) {
      this.parentNode = var1;
   }

   public void setPreviousSibling(Node var1) {
      this.previousSibling = var1;
   }

   public Node getPreviousSibling() {
      return this.previousSibling;
   }

   public void setNextSibling(Node var1) {
      this.nextSibling = var1;
   }

   public Node getNextSibling() {
      return this.nextSibling;
   }
}
