package org.apache.html.dom;

import org.w3c.dom.html.HTMLTableCaptionElement;

public class HTMLTableCaptionElementImpl extends HTMLElementImpl implements HTMLTableCaptionElement {
   private static final long serialVersionUID = 3546641018679144498L;

   public String getAlign() {
      return this.getAttribute("align");
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public HTMLTableCaptionElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
