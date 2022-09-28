package org.apache.html.dom;

import org.w3c.dom.html.HTMLFontElement;

public class HTMLFontElementImpl extends HTMLElementImpl implements HTMLFontElement {
   private static final long serialVersionUID = 3257282535158264883L;

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

   public HTMLFontElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
