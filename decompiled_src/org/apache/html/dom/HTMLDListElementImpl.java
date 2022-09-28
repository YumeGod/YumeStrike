package org.apache.html.dom;

import org.w3c.dom.html.HTMLDListElement;

public class HTMLDListElementImpl extends HTMLElementImpl implements HTMLDListElement {
   private static final long serialVersionUID = 3256719576463847477L;

   public boolean getCompact() {
      return this.getBinary("compact");
   }

   public void setCompact(boolean var1) {
      this.setAttribute("compact", var1);
   }

   public HTMLDListElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
