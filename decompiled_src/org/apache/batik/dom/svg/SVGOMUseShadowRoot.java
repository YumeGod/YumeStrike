package org.apache.batik.dom.svg;

import org.apache.batik.css.engine.CSSNavigableNode;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractDocumentFragment;
import org.apache.batik.dom.events.NodeEventTarget;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGOMUseShadowRoot extends AbstractDocumentFragment implements CSSNavigableNode, IdContainer {
   protected Element cssParentElement;
   protected boolean isLocal;

   protected SVGOMUseShadowRoot() {
   }

   public SVGOMUseShadowRoot(AbstractDocument var1, Element var2, boolean var3) {
      this.ownerDocument = var1;
      this.cssParentElement = var2;
      this.isLocal = var3;
   }

   public boolean isReadonly() {
      return false;
   }

   public void setReadonly(boolean var1) {
   }

   public Element getElementById(String var1) {
      return this.ownerDocument.getChildElementById(this, var1);
   }

   public Node getCSSParentNode() {
      return this.cssParentElement;
   }

   public Node getCSSPreviousSibling() {
      return null;
   }

   public Node getCSSNextSibling() {
      return null;
   }

   public Node getCSSFirstChild() {
      return this.getFirstChild();
   }

   public Node getCSSLastChild() {
      return this.getLastChild();
   }

   public boolean isHiddenFromSelectors() {
      return false;
   }

   public NodeEventTarget getParentNodeEventTarget() {
      return (NodeEventTarget)this.getCSSParentNode();
   }

   protected Node newNode() {
      return new SVGOMUseShadowRoot();
   }
}
