package org.apache.html.dom;

import org.w3c.dom.html.HTMLHeadingElement;

public class HTMLHeadingElementImpl extends HTMLElementImpl implements HTMLHeadingElement {
   private static final long serialVersionUID = 3906362731924698937L;

   public String getAlign() {
      return this.getCapitalized("align");
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public HTMLHeadingElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
