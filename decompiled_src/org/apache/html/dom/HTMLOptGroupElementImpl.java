package org.apache.html.dom;

import org.w3c.dom.html.HTMLOptGroupElement;

public class HTMLOptGroupElementImpl extends HTMLElementImpl implements HTMLOptGroupElement {
   private static final long serialVersionUID = 3258416110138046776L;

   public boolean getDisabled() {
      return this.getBinary("disabled");
   }

   public void setDisabled(boolean var1) {
      this.setAttribute("disabled", var1);
   }

   public String getLabel() {
      return this.capitalize(this.getAttribute("label"));
   }

   public void setLabel(String var1) {
      this.setAttribute("label", var1);
   }

   public HTMLOptGroupElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
