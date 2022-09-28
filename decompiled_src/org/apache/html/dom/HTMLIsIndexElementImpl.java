package org.apache.html.dom;

import org.w3c.dom.html.HTMLIsIndexElement;

public class HTMLIsIndexElementImpl extends HTMLElementImpl implements HTMLIsIndexElement {
   private static final long serialVersionUID = 3688508787891777847L;

   public String getPrompt() {
      return this.getAttribute("prompt");
   }

   public void setPrompt(String var1) {
      this.setAttribute("prompt", var1);
   }

   public HTMLIsIndexElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
