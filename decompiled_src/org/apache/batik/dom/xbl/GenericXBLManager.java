package org.apache.batik.dom.xbl;

import org.apache.batik.dom.AbstractNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenericXBLManager implements XBLManager {
   protected boolean isProcessing;

   public void startProcessing() {
      this.isProcessing = true;
   }

   public void stopProcessing() {
      this.isProcessing = false;
   }

   public boolean isProcessing() {
      return this.isProcessing;
   }

   public Node getXblParentNode(Node var1) {
      return var1.getParentNode();
   }

   public NodeList getXblChildNodes(Node var1) {
      return var1.getChildNodes();
   }

   public NodeList getXblScopedChildNodes(Node var1) {
      return var1.getChildNodes();
   }

   public Node getXblFirstChild(Node var1) {
      return var1.getFirstChild();
   }

   public Node getXblLastChild(Node var1) {
      return var1.getLastChild();
   }

   public Node getXblPreviousSibling(Node var1) {
      return var1.getPreviousSibling();
   }

   public Node getXblNextSibling(Node var1) {
      return var1.getNextSibling();
   }

   public Element getXblFirstElementChild(Node var1) {
      Node var2;
      for(var2 = var1.getFirstChild(); var2 != null && var2.getNodeType() != 1; var2 = var2.getNextSibling()) {
      }

      return (Element)var2;
   }

   public Element getXblLastElementChild(Node var1) {
      Node var2;
      for(var2 = var1.getLastChild(); var2 != null && var2.getNodeType() != 1; var2 = var2.getPreviousSibling()) {
      }

      return (Element)var2;
   }

   public Element getXblPreviousElementSibling(Node var1) {
      Node var2 = var1;

      do {
         var2 = var2.getPreviousSibling();
      } while(var2 != null && var2.getNodeType() != 1);

      return (Element)var2;
   }

   public Element getXblNextElementSibling(Node var1) {
      Node var2 = var1;

      do {
         var2 = var2.getNextSibling();
      } while(var2 != null && var2.getNodeType() != 1);

      return (Element)var2;
   }

   public Element getXblBoundElement(Node var1) {
      return null;
   }

   public Element getXblShadowTree(Node var1) {
      return null;
   }

   public NodeList getXblDefinitions(Node var1) {
      return AbstractNode.EMPTY_NODE_LIST;
   }
}
