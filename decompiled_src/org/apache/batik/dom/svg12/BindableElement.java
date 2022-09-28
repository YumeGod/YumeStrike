package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.AttributeInitializer;
import org.apache.batik.dom.svg.SVGGraphicsElement;
import org.w3c.dom.Node;

public class BindableElement extends SVGGraphicsElement {
   protected String namespaceURI;
   protected String localName;
   protected XBLOMShadowTreeElement xblShadowTree;

   protected BindableElement() {
   }

   public BindableElement(String var1, AbstractDocument var2, String var3, String var4) {
      super(var1, var2);
      this.namespaceURI = var3;
      this.localName = var4;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalName() {
      return this.localName;
   }

   protected AttributeInitializer getAttributeInitializer() {
      return null;
   }

   protected Node newNode() {
      return new BindableElement((String)null, (AbstractDocument)null, this.namespaceURI, this.localName);
   }

   public void setShadowTree(XBLOMShadowTreeElement var1) {
      this.xblShadowTree = var1;
   }

   public XBLOMShadowTreeElement getShadowTree() {
      return this.xblShadowTree;
   }

   public Node getCSSFirstChild() {
      return this.xblShadowTree != null ? this.xblShadowTree.getFirstChild() : null;
   }

   public Node getCSSLastChild() {
      return this.getCSSFirstChild();
   }
}
