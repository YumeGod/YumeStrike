package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFormElement;

public class HTMLFormElementImpl extends HTMLElementImpl implements HTMLFormElement {
   private static final long serialVersionUID = 3690757284875876658L;
   private HTMLCollectionImpl _elements;

   public HTMLCollection getElements() {
      if (this._elements == null) {
         this._elements = new HTMLCollectionImpl(this, (short)8);
      }

      return this._elements;
   }

   public int getLength() {
      return this.getElements().getLength();
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getAcceptCharset() {
      return this.getAttribute("accept-charset");
   }

   public void setAcceptCharset(String var1) {
      this.setAttribute("accept-charset", var1);
   }

   public String getAction() {
      return this.getAttribute("action");
   }

   public void setAction(String var1) {
      this.setAttribute("action", var1);
   }

   public String getEnctype() {
      return this.getAttribute("enctype");
   }

   public void setEnctype(String var1) {
      this.setAttribute("enctype", var1);
   }

   public String getMethod() {
      return this.capitalize(this.getAttribute("method"));
   }

   public void setMethod(String var1) {
      this.setAttribute("method", var1);
   }

   public String getTarget() {
      return this.getAttribute("target");
   }

   public void setTarget(String var1) {
      this.setAttribute("target", var1);
   }

   public void submit() {
   }

   public void reset() {
   }

   public NodeList getChildNodes() {
      return this.getChildNodesUnoptimized();
   }

   public Node cloneNode(boolean var1) {
      HTMLFormElementImpl var2 = (HTMLFormElementImpl)super.cloneNode(var1);
      var2._elements = null;
      return var2;
   }

   public HTMLFormElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
