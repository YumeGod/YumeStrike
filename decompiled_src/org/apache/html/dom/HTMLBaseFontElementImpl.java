package org.apache.html.dom;

import org.w3c.dom.html.HTMLBaseFontElement;

public class HTMLBaseFontElementImpl extends HTMLElementImpl implements HTMLBaseFontElement {
   private static final long serialVersionUID = 3257006557588763705L;

   public String getColor() {
      return this.capitalize(this.getAttribute("color"));
   }

   public void setColor(String var1) {
      this.setAttribute("color", var1);
   }

   public String getFace() {
      return this.capitalize(this.getAttribute("face"));
   }

   public void setFace(String var1) {
      this.setAttribute("face", var1);
   }

   public String getSize() {
      return this.getAttribute("size");
   }

   public void setSize(String var1) {
      this.setAttribute("size", var1);
   }

   public HTMLBaseFontElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
