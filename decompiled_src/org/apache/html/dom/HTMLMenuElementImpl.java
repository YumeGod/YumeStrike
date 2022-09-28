package org.apache.html.dom;

import org.w3c.dom.html.HTMLMenuElement;

public class HTMLMenuElementImpl extends HTMLElementImpl implements HTMLMenuElement {
   private static final long serialVersionUID = 3256441421648247094L;

   public boolean getCompact() {
      return this.getBinary("compact");
   }

   public void setCompact(boolean var1) {
      this.setAttribute("compact", var1);
   }

   public HTMLMenuElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
