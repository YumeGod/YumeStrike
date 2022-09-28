package org.apache.html.dom;

import org.w3c.dom.html.HTMLLIElement;

public class HTMLLIElementImpl extends HTMLElementImpl implements HTMLLIElement {
   private static final long serialVersionUID = 3258417244009607225L;

   public String getType() {
      return this.getAttribute("type");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public int getValue() {
      return this.getInteger(this.getAttribute("value"));
   }

   public void setValue(int var1) {
      this.setAttribute("value", String.valueOf(var1));
   }

   public HTMLLIElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
