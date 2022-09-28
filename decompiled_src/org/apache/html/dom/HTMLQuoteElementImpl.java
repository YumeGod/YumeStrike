package org.apache.html.dom;

import org.w3c.dom.html.HTMLQuoteElement;

public class HTMLQuoteElementImpl extends HTMLElementImpl implements HTMLQuoteElement {
   private static final long serialVersionUID = 3257852082097764401L;

   public String getCite() {
      return this.getAttribute("cite");
   }

   public void setCite(String var1) {
      this.setAttribute("cite", var1);
   }

   public HTMLQuoteElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
