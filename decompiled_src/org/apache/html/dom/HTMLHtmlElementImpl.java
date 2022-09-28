package org.apache.html.dom;

import org.w3c.dom.html.HTMLHtmlElement;

public class HTMLHtmlElementImpl extends HTMLElementImpl implements HTMLHtmlElement {
   private static final long serialVersionUID = 3257285846578443060L;

   public String getVersion() {
      return this.capitalize(this.getAttribute("version"));
   }

   public void setVersion(String var1) {
      this.setAttribute("version", var1);
   }

   public HTMLHtmlElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
